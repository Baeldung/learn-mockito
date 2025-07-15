package com.baeldung.lmock.service.defaultimpl;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.baeldung.lmock.domain.model.Campaign;
import com.baeldung.lmock.persistence.repository.CampaignRepository;

class DefaultCampaignServiceUnitTest {

    @Mock
    private CampaignRepository campaignRepo;
    @InjectMocks
    private DefaultCampaignService service;

    @BeforeEach
    public void setupDataSource() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenStubbedFindById_whenNoStubbingSet_thenReturnsEmptyCampaign() {
        // when
        Optional<Campaign> result = service.findById(1L);

        // then
        Assertions.assertTrue(result.isEmpty());
    }
}