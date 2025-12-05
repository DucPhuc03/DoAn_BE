package do_an.traodoido.controller;

import do_an.traodoido.dto.response.GeocodeLocationResponse;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.service.GeocodingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/geocode")
@Tag(name = "Geocode", description = "API chuyển đổi địa chỉ thành tọa độ địa lý")
public class GeocodeController {

    private final GeocodingService geocodingService;

    @Operation(summary = "Lấy tọa độ địa lý từ địa chỉ", description = "Chuyển đổi địa chỉ văn bản thành tọa độ địa lý (latitude, longitude) sử dụng Google Maps API. Không yêu cầu xác thực.")
    @GetMapping
    public RestResponse<GeocodeLocationResponse> getLocation(@RequestParam String address) {
        GeocodeLocationResponse location = geocodingService.getLocation(address);
        return RestResponse.<GeocodeLocationResponse>builder()
                .code(1000)
                .message("Thành công")
                .data(location)
                .build();
    }
}

