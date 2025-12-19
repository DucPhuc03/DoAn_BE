package do_an.traodoido.service;

import do_an.traodoido.dto.request.CreateReportDTO;
import do_an.traodoido.dto.response.ResReportDTO;
import do_an.traodoido.dto.response.RestResponse;

import java.util.List;

public interface ReportService {
    RestResponse<String> createReport(CreateReportDTO createReportDTO);

    RestResponse<List<ResReportDTO>> getAllReports();
    RestResponse<String> deleteReport(Long id);
    RestResponse<String> updateStatus(Long id);
}
