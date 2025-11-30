package do_an.traodoido.service;

import do_an.traodoido.dto.request.CreateTradeDTO;
import do_an.traodoido.dto.request.UpdateTradePostDTO;
import do_an.traodoido.dto.response.ResTradeDTO;
import do_an.traodoido.dto.response.ResTradeDetailDTO;
import do_an.traodoido.dto.response.RestResponse;

import java.util.List;

public interface TradeService {
    RestResponse<String> createTrade(CreateTradeDTO createTradeDTO);
    RestResponse<String> updateTradeStatus(Long tradeId);
    RestResponse<String> updateRequesterPost(UpdateTradePostDTO updateTradePostDTO);

    RestResponse<List<ResTradeDTO>> getTradesByUser();

    RestResponse<ResTradeDetailDTO> getTradeDetails(Long tradeId);
}
