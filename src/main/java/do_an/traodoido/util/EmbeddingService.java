package do_an.traodoido.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import do_an.traodoido.dto.response.RecommendedItem;
import do_an.traodoido.dto.response.ResPostDTO;
import do_an.traodoido.entity.Post;
import do_an.traodoido.entity.Trade;
import do_an.traodoido.entity.ViewHistory;
import do_an.traodoido.enums.TradeStatus;
import do_an.traodoido.repository.LikeRepository;
import do_an.traodoido.repository.PostRepository;
import do_an.traodoido.repository.TradeRepository;
import do_an.traodoido.repository.ViewHistoryRepository;
import do_an.traodoido.service.PostService;
import do_an.traodoido.service.ViewHistoryService;
import lombok.RequiredArgsConstructor;


import lombok.extern.slf4j.Slf4j;

import org.apache.logging.log4j.util.TriConsumer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

import java.util.*;
import java.util.function.BiConsumer;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmbeddingService {
    private final BedrockRuntimeClient bedrockClient;
    private final S3Service s3Service;
    private final PostRepository postRepository;
    private final TradeRepository tradeRepository;
    private final ViewHistoryRepository viewHistoryRepository;
    private final LikeRepository likeRepository;
    private final Gson gson = new Gson();

    @Value("${aws.bedrock.model-id}")
    private String modelId;

    public List<Float> embed(String text) {

        // Request JSON
        String payload = """
            {
                "inputText": "%s"
            }
        """.formatted(text);

        // Gửi request tới Bedrock Titan Embedding v2
        InvokeModelRequest request = InvokeModelRequest.builder()
                .modelId(modelId)
                .body(SdkBytes.fromUtf8String(payload))
                .build();

        InvokeModelResponse response = bedrockClient.invokeModel(request);

        String json = response.body().asUtf8String();

        // Parse JSON bằng Gson
        JsonObject root = gson.fromJson(json, JsonObject.class);
        JsonArray arr = root.getAsJsonArray("embedding");

        List<Float> vector = new ArrayList<>();
        arr.forEach(v -> vector.add(v.getAsFloat()));

        return vector;
    }
    public List<Float> createEmbeddingForPost(Long id){
        Post post=postRepository.findById(id).orElseThrow();
        String content =buildContent(post);

        List<Float> vector=embed(content);

        s3Service.saveItemVector(post.getId(),vector);
        return vector;
    }

    public List<Float> createEmbeddingForUser(Long id){
        List<Long> liked= likeRepository.findLikedPostIds(id);
        List<Long> viewed=viewHistoryRepository.findViewedPostIds(id);
        List<Trade> posts=tradeRepository.findCounterpartyPosts(id);
        List<Long> traded= posts.stream().map(trade -> trade.getRequester().getId().equals(id)?trade.getOwnerPost().getId():trade.getRequesterPost().getId()).toList();
        if (liked.isEmpty() && viewed.isEmpty() && traded.isEmpty()) {
          liked=postRepository.findTop10PopularPosts(PageRequest.of(0,10));
        }

        // Trọng số
        float W_LIKE = 1.0f;
        float W_VIEW = 0.3f;
        float W_TRADE = 1.8f;

        List<Float> sumVec = null;
        float totalWeight = 0;


        sumVec = apply(sumVec, liked, W_LIKE);
        totalWeight += liked.size() * W_LIKE;

        sumVec = apply(sumVec, viewed, W_VIEW);
        totalWeight += viewed.size() * W_VIEW;

        sumVec = apply(sumVec, traded, W_TRADE);
        totalWeight += traded.size() * W_TRADE;

        // Chuẩn hoá vector
        for (int i = 0; i < sumVec.size(); i++) {
            sumVec.set(i, sumVec.get(i) / totalWeight);
        }
        s3Service.saveUserVector(id,sumVec);

        return sumVec;
    }
    private List<Float> apply(List<Float> sumVec, List<Long> ids, float weight) {

        for (Long id : ids) {
            try {
                List<Float> vec = s3Service.loadPostVector(id);
                if (vec == null) {
                    throw new RuntimeException("Vector null! postId = " + id);
                }
                if (sumVec == null) {
                    sumVec = new ArrayList<>(vec);
                } else {
                    for (int i = 0; i < vec.size(); i++) {
                        sumVec.set(i, sumVec.get(i) + vec.get(i) * weight);
                    }
                }

            } catch (Exception e) {
                System.err.println("Lỗi khi load vector cho postId = " + id);
                e.printStackTrace();
                throw e;
            }
        }

        return sumVec;
    }
    public List<RecommendedItem> recommendForUser(Long userId) {
        List<Long> liked= likeRepository.findLikedPostIds(userId);
        List<Long> viewed=viewHistoryRepository.findViewedPostIds(userId);
        List<Trade> posts=tradeRepository.findCounterpartyPosts(userId);
        List<Long> traded= posts.stream().map(trade -> trade.getRequester().getId().equals(userId)?trade.getOwnerPost().getId():trade.getRequesterPost().getId()).toList();

        List<Long> allItems=postRepository.findAllPostId(userId);
        Set<Long> allSet = new HashSet<>(allItems);
        liked.forEach(allSet::remove);
        viewed.forEach(allSet::remove);
        traded.forEach(allSet::remove);
        List<Long> candidateItems = new ArrayList<>(allSet);
        List<Float> userVec = s3Service.loadUserVector(userId);
        List<RecommendedItem> results = new ArrayList<>();
        for (Long itemId : candidateItems) {
            List<Float> itemVec = s3Service.loadPostVector(itemId);

            double sim = cosineSimilarity(userVec, itemVec);

            results.add(RecommendedItem.builder().id(itemId).similarity(sim).build());
        }

        // Sắp xếp theo điểm giảm dần
        results.sort((a, b) -> Double.compare(b.getSimilarity(), a.getSimilarity()));

        return results.stream().limit(6).toList();
    }



    public double cosineSimilarity(List<Float> v1, List<Float> v2) {
        if (v1.size() != v2.size()) {
            throw new IllegalArgumentException("Vector dimensions mismatch");
        }

        double dot = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < v1.size(); i++) {
            dot += v1.get(i) * v2.get(i);
            normA += Math.pow(v1.get(i), 2);
            normB += Math.pow(v2.get(i), 2);
        }

        normA = Math.sqrt(normA);
        normB = Math.sqrt(normB);

        if (normA == 0 || normB == 0) {
            return 0.0; // Tránh chia 0
        }

        return dot / (normA * normB);
    }


    private String buildContent(Post post) {
        String content = """
        Title: %s
        Description: %s
        Category: %s
        Condition: %s
    """.formatted(
                post.getTitle(),
                post.getDescription(),
                post.getCategory().getName(),
                post.getItemCondition()
        );

        // Chuyển newline thật thành "\n"
        return content.replace("\n", "\\n").trim();
    }

}
