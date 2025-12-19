package do_an.traodoido.controller;

import do_an.traodoido.dto.request.CreateReportDTO;
import do_an.traodoido.dto.response.ResReportDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
@Tag(name = "Report", description = "API quản lý báo cáo vi phạm")
public class ReportController {
    private final ReportService reportService;

    @PostMapping
    @Operation(summary = "Tạo báo cáo vi phạm", description = "Tạo một báo cáo vi phạm mới")
    public ResponseEntity<RestResponse<String>> createReport(@RequestBody CreateReportDTO createReportDTO) {
        RestResponse<String> response = reportService.createReport(createReportDTO);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    @Operation(summary = "Lấy tất cả báo cáo vi phạm", description = "Lấy danh sách tất cả các báo cáo vi phạm")
    public ResponseEntity<RestResponse<List<ResReportDTO>>> getAllReports() {
        RestResponse<List<ResReportDTO>> response = reportService.getAllReports();
        return ResponseEntity.ok(response);
    }
    @PatchMapping("/admin/{id}")
    public ResponseEntity<RestResponse<String>> updateReport(@PathVariable("id") Long id) {
        RestResponse<String> response = reportService.updateStatus(id);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<RestResponse<String>> deleteReport(@PathVariable("id") Long id) {
        RestResponse<String> response = reportService.deleteReport(id);
        return ResponseEntity.ok(response);
    }
}
