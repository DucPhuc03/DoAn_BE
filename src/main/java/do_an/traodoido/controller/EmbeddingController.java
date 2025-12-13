package do_an.traodoido.controller;

import do_an.traodoido.dto.response.RecommendedItem;
import do_an.traodoido.entity.Post;
import do_an.traodoido.repository.PostRepository;
import do_an.traodoido.util.EmbeddingService;
import do_an.traodoido.util.S3Service;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    private final PostRepository postRepository;

    @GetMapping("/test")
    public List<Float> test(){
        return embeddingService.embed("Áo khoác gió xxxx");
    }
    @PostMapping("/embedding/post/{postId}")
    public List<Float> getEmbeddingPost(@PathVariable("postId") Long id){
        return embeddingService.createEmbeddingForPost(id);
    }
    @PostMapping("/embedding/post/all")
    public String getEmbeddingAllPost(){
        List<Post> postList=postRepository.findAll();
        postList.forEach(post -> embeddingService.createEmbeddingForPost(post.getId()));

        return "thanh cong";
    }

    @PostMapping("/embedding/user/{userId}")
    public List<Float> getEmbeddingUser(@PathVariable("userId") Long id){
        return embeddingService.createEmbeddingForUser(id);
    }

    @GetMapping("/post/{postId}")
    public List<Float> loadPostVector(@PathVariable("postId") Long id){
        return s3Service.loadPostVector(id);
    }

    @GetMapping("/recommend/{userId}")
    public ResponseEntity<?> recommend(@PathVariable Long userId) {

        List<RecommendedItem> list = embeddingService.recommendForUser(userId);

        return ResponseEntity.ok(list);
    }
}
