package com.voyageai.destination;

import com.voyageai.common.exception.ResourceNotFoundException;
import com.voyageai.destination.dto.DestinationDetailResponse;
import com.voyageai.destination.dto.DestinationSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DestinationService {

    private final DestinationRepository destinationRepository;

    public DestinationService(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

    @Transactional(readOnly = true)
    public Page<DestinationSummaryResponse> searchDestinations(
            String search, String region, String travelStyle, String budgetTier, Pageable pageable) {
        return destinationRepository.findWithFilters(search, region, travelStyle, budgetTier, pageable)
                .map(DestinationMapper::toSummary);
    }

    @Transactional(readOnly = true)
    public List<DestinationSummaryResponse> getAllDestinations() {
        return destinationRepository.findAll().stream()
                .map(DestinationMapper::toSummary)
                .toList();
    }

    @Transactional(readOnly = true)
    public DestinationDetailResponse getDestinationById(String id) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destination not found with id: " + id));

        return DestinationMapper.toDetail(destination);
    }
}
