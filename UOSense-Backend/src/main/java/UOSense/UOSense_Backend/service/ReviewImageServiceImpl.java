package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.repository.ReviewImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewImageServiceImpl implements ReviewImageService{
    private final ReviewImageRepository reviewImageRepository;
}
