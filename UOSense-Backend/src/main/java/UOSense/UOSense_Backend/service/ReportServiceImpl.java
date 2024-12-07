package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.common.Role;
import UOSense.UOSense_Backend.dto.CustomUserDetails;
import UOSense.UOSense_Backend.dto.ReportResponse;
import UOSense.UOSense_Backend.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService{
    private final ReportRepository reportRepository;

    @Override
    public List<ReportResponse> findList(Authentication authentication) {
        return reportRepository.findAll().stream()
                .map(ReportResponse::from)
                .toList();
    }
}
