package do_an.traodoido.controller;

import do_an.traodoido.dto.response.LabelDTO;
import do_an.traodoido.service.CategoryMapper;
import do_an.traodoido.service.VisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vision")
@RequiredArgsConstructor
public class VisionController {

    private final VisionService visionService;
    private final CategoryMapper categoryMapper;
    @PostMapping("/detect")
    public ResponseEntity<?> detect(@RequestParam("file") MultipartFile file) throws Exception {

        List<LabelDTO> labels = visionService.detectLabels(file.getBytes());
        String mappedCategory = categoryMapper.aggregateCategory(labels);

        return ResponseEntity.ok(Map.of(
                "labels", mappedCategory
        ));
    }
}

