package do_an.traodoido.util;

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

    private static final String GEOCODE_URL = "https://rsapi.goong.io/geocode";
    private static final double EARTH_RADIUS_KM = 6371.0; // bán kính trái đất (km)

    @Value("${google.map.api.key}")
    private String apiKey;

    public GeocodeLocationResponse getLocation(String address) {
        // Tạo RestTemplate và ObjectMapper trực tiếp để tránh lỗi thiếu bean
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        URI uri = UriComponentsBuilder.
                fromUriString(GEOCODE_URL)
                .queryParam("address", address)
                .queryParam("api_key", apiKey)
                .build()
                .encode()
                .toUri();

        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidException("Không thể kết nối tới Google Geocoding API");
        } else {
            response.getBody();
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

    public double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = EARTH_RADIUS_KM * c; // khoảng cách (km)

        return Math.round(distance * 10.0) / 10.0;
    }

    public double getDistanceApiKm(double originLat, double originLng,
                                double destLat, double destLng) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            URI uri = UriComponentsBuilder
                    .fromUriString("https://rsapi.goong.io/DistanceMatrix")
                    .queryParam("origins", originLat + "," + originLng)
                    .queryParam("destinations", destLat + "," + destLng)
                    .queryParam("api_key", apiKey)
                    .build(true)
                    .toUri();

            ResponseEntity<String> response =
                    restTemplate.getForEntity(uri, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode element = root.path("rows").get(0)
                    .path("elements").get(0);

            String status = element.path("status").asText();
            if (!"OK".equals(status)) {
                throw new RuntimeException("Goong API trả về lỗi: " + status);
            }

            double distanceMeters = element
                    .path("distance")
                    .path("value")
                    .asDouble();

            return Math.round((distanceMeters / 1000.0)*10.0)/10.0; // đổi sang km

        } catch (Exception e) {
            throw new RuntimeException("Lỗi gọi Goong Distance API: " + e.getMessage());
        }
    }
}

