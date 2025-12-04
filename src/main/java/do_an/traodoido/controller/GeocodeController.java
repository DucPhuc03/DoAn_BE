package do_an.traodoido.controller;

import do_an.traodoido.dto.response.GeocodeLocationResponse;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.service.GeocodingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/geocode")
public class GeocodeController {

    private final GeocodingService geocodingService;

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

