package do_an.traodoido.controller;

import do_an.traodoido.service.ViewHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/view")
@RequiredArgsConstructor
public class ViewHistoryController {
    private final ViewHistoryService viewHistoryService;


}
