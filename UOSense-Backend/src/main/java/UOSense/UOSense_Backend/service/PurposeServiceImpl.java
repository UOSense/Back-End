package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.entity.PurposeRestaurant;
import UOSense.UOSense_Backend.repository.PurposeRestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PurposeServiceImpl implements  PurposeService{
    private final PurposeRestRepository purposeRestRepository;

    @Override
    public PurposeRestaurant getPurposeRestById(int purposeRestId) {
        return purposeRestRepository.findById(purposeRestId)
                .orElseThrow(() -> new IllegalArgumentException("정보 수정 제안 이력이 존재하지 않습니다."));
    }

}
