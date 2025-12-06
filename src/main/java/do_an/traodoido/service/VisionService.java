package do_an.traodoido.service;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import do_an.traodoido.dto.response.LabelDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class VisionService {

    public List<LabelDTO> detectLabels(byte[] imageBytes) throws IOException {

        List<LabelDTO> results = new ArrayList<>();

        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

            ByteString bs = ByteString.copyFrom(imageBytes);
            Image img = Image.newBuilder().setContent(bs).build();

            Feature feature = Feature.newBuilder()
                    .setType(Feature.Type.LABEL_DETECTION)
                    .setMaxResults(10)
                    .build();

            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feature)
                    .setImage(img)
                    .build();

            BatchAnnotateImagesResponse response =
                    vision.batchAnnotateImages(List.of(request));

            AnnotateImageResponse res = response.getResponsesList().get(0);

            if (res.hasError()) {
                throw new RuntimeException("Vision API error: " + res.getError().getMessage());
            }

            res.getLabelAnnotationsList().forEach(label ->
                    results.add(new LabelDTO(label.getDescription(), label.getScore()))
            );
        }

        return results;
    }
}

