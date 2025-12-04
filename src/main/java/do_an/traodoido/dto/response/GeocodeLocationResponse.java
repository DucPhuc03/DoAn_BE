package do_an.traodoido.dto.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GeocodeLocationResponse {
    String formattedAddress;
    double latitude;
    double longitude;
}

