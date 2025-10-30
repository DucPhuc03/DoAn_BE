package do_an.traodoido.service.impl;

import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.repository.TradeRepository;
import do_an.traodoido.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService {
    private final TradeRepository tradeRepository;

    public RestResponse<String> createTrade(){
        return null;
    }

}
