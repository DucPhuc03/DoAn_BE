package do_an.traodoido.controller;

import do_an.traodoido.util.VisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/vision")
@RequiredArgsConstructor
public class VisionController {

    private final VisionService visionService;
    @PostMapping("/detect")
    public ResponseEntity<?> detect(@RequestParam("file") MultipartFile file) throws Exception {

        String labels = visionService.detectLabels(file.getBytes());

        return ResponseEntity.ok(Map.of(
                "labels", labels
        ));
    }
}

