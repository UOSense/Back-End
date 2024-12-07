package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.dto.ReportRequest;
import UOSense.UOSense_Backend.dto.ReportResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ReportService {
    void register(ReportRequest reportRequest, int userId);
    List<ReportResponse> findList();
}
