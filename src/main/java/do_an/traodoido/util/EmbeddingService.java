package do_an.traodoido.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import do_an.traodoido.entity.Post;
import do_an.traodoido.repository.PostRepository;
import do_an.traodoido.service.PostService;
import lombok.RequiredArgsConstructor;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmbeddingService {
    private final BedrockRuntimeClient bedrockClient;
    private final S3Service s3Service;
    private final PostRepository postRepository;
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
    public String createEmbeddingForPost(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post không tồn tại: " + id));

        String content = buildContent(post);
        log.info("Embedding post {} with content: {}", id, content);

//        List<Float> vector = embed(content);
//        log.info("Vector size for post {}: {}", id, vector.size());
//
//        try {
//            s3Service.saveItemVector(post.getId(), vector);
//        } catch (Exception e) {
//            log.error("Lưu embedding lên S3 thất bại cho post {}: {}", id, e.getMessage(), e);
//            throw e;
//        }

        return content;
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
