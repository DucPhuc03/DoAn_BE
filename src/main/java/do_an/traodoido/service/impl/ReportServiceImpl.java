package do_an.traodoido.service.impl;

import do_an.traodoido.dto.request.CreateReportDTO;
import do_an.traodoido.dto.response.ResReportDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.dto.response.UserChat;
import do_an.traodoido.entity.Report;
import do_an.traodoido.entity.User;
import do_an.traodoido.enums.ReportStatus;
import do_an.traodoido.exception.InvalidException;
import do_an.traodoido.repository.ReportRepository;
import do_an.traodoido.repository.UserRepository;
import do_an.traodoido.service.ReportService;
import do_an.traodoido.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    @Override
    public RestResponse<String> createReport(CreateReportDTO createReportDTO) {
        User currentUser = userService.getCurrentUser();
        User reportedUser = userRepository.findById(createReportDTO.getReportedId()).orElseThrow(()->new InvalidException("User","id",String.valueOf(createReportDTO.getReportedId())));
        Report report = Report.builder()
                .reporter(currentUser)
                .reportedUser(reportedUser)
                .reason(createReportDTO.getReason())
                .reportDate(LocalDate.now())
                .type(createReportDTO.getType())
                .postId(createReportDTO.getPostId())
                .status(ReportStatus.PENDING)
                .build();
        reportRepository.save(report);
        return RestResponse.<String>builder()
                .data("Report created successfully")
                .message("Report created successfully")
                .code(1000)
                .build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public RestResponse<List<ResReportDTO>> getAllReports() {
        List<Report> reports = reportRepository.findAll();
        List<ResReportDTO> resReportDTOS = reports.stream().map(report -> ResReportDTO.builder()
                .id(report.getId())
                .reportedId(report.getReportedUser().getId())
                .reason(report.getReason())
                .type(report.getType())
                .postId(report.getPostId()!=null?report.getPostId():null)

                .reporter(UserChat.builder()
                        .userId(report.getReporter().getId())
                        .username(report.getReporter().getFullName())
                        .avatarUrl(report.getReporter().getAvatarUrl())
                        .build())
                .reportedUser(UserChat.builder()
                        .userId(report.getReportedUser().getId())
                        .username(report.getReportedUser().getFullName())
                        .avatarUrl(report.getReportedUser().getAvatarUrl())
                        .build())
                .reportDate(report.getReportDate())
                .status(report.getStatus())
                .build()).toList();
        return RestResponse.<List<ResReportDTO>>builder()
                .data(resReportDTOS)
                .message("Get all reports successfully")
                .code(1000)
                .build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public RestResponse<String> deleteReport(Long id) {
        reportRepository.deleteById(id);
        return RestResponse.<String>builder()
                .data(null)
                .message("Delete report successfully")
                .code(1000)
                .build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public RestResponse<String> updateStatus(Long id) {
        Report report=reportRepository.findById(id).orElseThrow();
        report.setStatus(ReportStatus.RESOLVED);
        reportRepository.save(report);
        return RestResponse.<String>builder()
                .data(null)
                .message("Update report successfully")
                .code(1000)
                .build();
    }
}
