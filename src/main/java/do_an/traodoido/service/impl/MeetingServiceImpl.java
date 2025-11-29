package do_an.traodoido.service.impl;

import do_an.traodoido.dto.request.CreateMeetingDTO;
import do_an.traodoido.dto.response.ResMeetingDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.dto.response.TradeMeetingDTO;
import do_an.traodoido.entity.Meeting;
import do_an.traodoido.entity.Trade;
import do_an.traodoido.entity.User;
import do_an.traodoido.exception.InvalidException;
import do_an.traodoido.repository.MeetingRepository;
import do_an.traodoido.repository.TradeRepository;
import do_an.traodoido.service.MeetingService;
import do_an.traodoido.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {
    private final MeetingRepository meetingRepository;
    private final TradeRepository tradeRepository;
    private final UserService userService;

    @Override
    public RestResponse<String> createMeeting(CreateMeetingDTO request) {
        // Validate trade exists
        Trade trade = tradeRepository.findById(request.getTradeId())
                .orElseThrow(() -> new InvalidException("Trade not found with id: " + request.getTradeId()));

        // Validate required fields
        if (request.getLocation() == null || request.getLocation().trim().isEmpty()) {
            throw new InvalidException("Location is required");
        }
        if (request.getDate() == null) {
            throw new InvalidException("Meeting date is required");
        }

        // Create meeting
        Meeting meeting = Meeting.builder()
                .trade(trade)
                .note(request.getNote())
                .creator(userService.getCurrentUser())
                .time(request.getTime())
                .location(request.getLocation())
                .meetingDate(request.getDate())
                .build();

        meeting = meetingRepository.save(meeting);


        return RestResponse.<String>builder()
                .code(200)
                .message("Meeting created successfully")
                .data("Meeting created successfully")
                .build();
    }

    @Override
    public RestResponse<List<ResMeetingDTO>> getMeetingsByUser() {
        User user = userService.getCurrentUser();
        List<Meeting> meetings = meetingRepository.findByUserId(user.getId());

        List<ResMeetingDTO> responses = meetings.stream()
                .map(meeting -> {
                    Trade trade = meeting.getTrade();

                    User partner;
                    if (trade != null && trade.getOwner() != null && Objects.equals(trade.getOwner().getId(), user.getId())) {
                        partner = trade.getRequester();
                    } else {
                        partner = trade != null ? trade.getOwner() : null;
                    }

                    String titleTrade = null;
                    if (trade != null && trade.getOwnerPost() != null) {
                        titleTrade = trade.getOwnerPost().getTitle();
                    }

                    return ResMeetingDTO.builder()
                            .id(meeting.getId())
                            .location(meeting.getLocation())
                            .meetingDate(meeting.getMeetingDate())
                            .titleTrade(titleTrade)
                            .namePartner(partner != null ? partner.getFullName() : null)
                            .avatarPartner(partner != null ? partner.getAvatarUrl() : null)
                            .build();
                })
                .collect(Collectors.toList());

        return RestResponse.<List<ResMeetingDTO>>builder()
                .code(200)
                .message("Meetings retrieved successfully")
                .data(responses)
                .build();
    }

    @Override
    public TradeMeetingDTO getMeetingTrade(Long tradeId) {
        return null;
    }

    @Override
    public RestResponse<String> updateMeeting(Long meetingId, CreateMeetingDTO request) {
        return null;
    }

    @Override
    public RestResponse<String> cancelMeeting(Long meetingId) {
        return null;
    }
}

