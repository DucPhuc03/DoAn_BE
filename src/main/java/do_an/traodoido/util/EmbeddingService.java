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
    public List<Float> createEmbeddingForPost(Long id){
        Post post=postRepository.findById(id).orElseThrow();
        String content =buildContent(post);
        log.info(content);
        List<Float> vector=embed(content);
        log.info("test",vector);
        s3Service.saveItemVector(post.getId(),vector);
        return vector;
    }

    private String buildContent(Post post) {
        return """
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
    }
}
