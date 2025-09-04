package com.baeldung.lmock.service.defaultimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.baeldung.lmock.domain.model.Campaign;
import com.baeldung.lmock.persistence.repository.CampaignRepository;

class DefaultCampaignServiceUnitTest {

    @Mock
    private CampaignRepository campaignRepository;

    @InjectMocks
    private DefaultCampaignService campaignService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        Campaign c1 = new Campaign("code 1", "name 1", "description 1");
        Campaign c2 = new Campaign("code 2", "name 2", "description 2");

        when(campaignRepository.findAll()).thenReturn(List.of(c1, c2));
        when(campaignRepository.findById(1L)).thenReturn(Optional.of(c1));
        when(campaignRepository.findById(2L)).thenReturn(Optional.of(c2));
    }

    @Test
    void whenFindCampaigns_thenReturnAllCampaigns() {
        List<Campaign> campaigns = campaignService.findCampaigns();

        assertEquals(2, campaigns.size());
    }

}