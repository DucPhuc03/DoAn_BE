package do_an.traodoido.service.impl;

import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.entity.Post;
import do_an.traodoido.entity.User;
import do_an.traodoido.entity.ViewHistory;
import do_an.traodoido.repository.PostRepository;
import do_an.traodoido.repository.ViewHistoryRepository;
import do_an.traodoido.service.UserService;
import do_an.traodoido.service.ViewHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ViewHistoryServiceImpl implements ViewHistoryService {
    private final ViewHistoryRepository viewHistoryRepository;
    private final PostRepository postRepository;
    private UserService userService;

    @Override
    public RestResponse<String> createViewHistory(Long postId) {
        User currentUser=userService.getCurrentUser();
        Post post=postRepository.findById(postId).orElseThrow();
        Optional<ViewHistory> viewHistory=viewHistoryRepository.findByPostIdAndUserId(postId,currentUser.getId());
        if(viewHistory.isEmpty()){
            viewHistoryRepository.save(ViewHistory.builder()
                            .viewCount(1)
                            .post(post)
                            .user(currentUser)
                    .build());
        }
        else{
            viewHistory.get().setViewCount(viewHistory.get().getViewCount()+1);

            viewHistoryRepository.save(viewHistory.get());
        }


        return RestResponse.<String>builder()
                .code(1000)
                .message("viewHistory created successfully")
                .data("Success")
                .build();
    }

    @Override
    public List<Long> getPostView(Long id) {
        List<ViewHistory> list=viewHistoryRepository.findAllByUserId(id);
        return list.stream().map(view->view.getPost().getId()).toList();

    }
}
