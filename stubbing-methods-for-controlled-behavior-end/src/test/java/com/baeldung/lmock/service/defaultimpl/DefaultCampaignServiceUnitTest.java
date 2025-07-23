package com.baeldung.lmock.service.defaultimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.baeldung.lmock.domain.model.Campaign;
import com.baeldung.lmock.persistence.repository.CampaignRepository;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultCampaignServiceUnitTest {

    @Mock
    private CampaignRepository campaignRepo;
    @InjectMocks
    private DefaultCampaignService service;

    @Test
    void givenStubbedFindById_whenNoStubbingSet_thenReturnsEmptyCampaign() {
        // when
        Optional<Campaign> result = service.findById(1L);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void givenStubbedFindById_whenCalledOnce_thenReturnsCampaign() {
        // given
        Campaign testCampaign = new Campaign("C1", "First Campaign", "Description");
        when(campaignRepo.findById(1L)).thenReturn(Optional.of(testCampaign));

        // when
        Optional<Campaign> result = service.findById(1L);

        // then
        assertTrue(result.isPresent());
        assertEquals("C1", result.get()
          .getCode());
    }
}
