package do_an.traodoido.controller;

import do_an.traodoido.util.EmbeddingService;
import do_an.traodoido.util.S3Service;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.bedrockruntime.endpoints.internal.Value;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EmbeddingController {
    private final EmbeddingService embeddingService;
    private final S3Service s3Service;

    @GetMapping("/test")
    public List<Float> test(){
        return embeddingService.embed("Áo khoác gió xxxx");
    }
    @PostMapping("/embedding/{postId}")
    public List<Float> getEmbedding(@PathVariable("postId") Long id){
        String content=embeddingService.createEmbeddingForPost(id);

        List<Float> res=embeddingService.embed(content);
        s3Service.saveItemVector(id,res);
        return res;
    }


}
