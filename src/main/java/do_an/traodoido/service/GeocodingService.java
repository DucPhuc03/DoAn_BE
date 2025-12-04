package do_an.traodoido.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import do_an.traodoido.dto.response.GeocodeLocationResponse;
import do_an.traodoido.exception.InvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class GeocodingService {

    private static final String GEOCODE_URL = "https://maps.googleapis.com/maps/api/geocode/json";

    @Value("${google.map.api.key}")
    private String apiKey;


    public GeocodeLocationResponse getLocation(String address) {
        // Tạo RestTemplate và ObjectMapper trực tiếp để tránh lỗi thiếu bean
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        URI uri = UriComponentsBuilder.fromHttpUrl(GEOCODE_URL)
                .queryParam("address", address)
                .queryParam("key", apiKey)
                .build()
                .encode()
                .toUri();

        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new InvalidException("Không thể kết nối tới Google Geocoding API");
        }

        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            String status = root.path("status").asText();
            if (!"OK".equals(status)) {
                throw new InvalidException("Không tìm thấy tọa độ cho địa chỉ: " + address);
            }

            JsonNode resultNode = root.path("results").get(0);
            JsonNode locationNode = resultNode.path("geometry").path("location");

            return GeocodeLocationResponse.builder()
                    .formattedAddress(resultNode.path("formatted_address").asText())
                    .latitude(locationNode.path("lat").asDouble())
                    .longitude(locationNode.path("lng").asDouble())
                    .build();
        } catch (Exception e) {
            if (e instanceof InvalidException) {
                throw (InvalidException) e;
            }
            throw new InvalidException("Có lỗi khi xử lý phản hồi từ Google Geocoding API");
        }
    }
}

