package do_an.traodoido.service;

import do_an.traodoido.dto.request.CreateTradeDTO;
import do_an.traodoido.dto.response.RestResponse;

public interface TradeService {
    RestResponse<String> createTrade(CreateTradeDTO createTradeDTO);
}
