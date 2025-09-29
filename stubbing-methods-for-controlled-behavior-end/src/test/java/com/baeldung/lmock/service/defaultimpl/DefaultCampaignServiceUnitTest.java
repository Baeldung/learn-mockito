package com.baeldung.lmock.service.defaultimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.argThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.baeldung.lmock.domain.model.Campaign;
import com.baeldung.lmock.persistence.repository.CampaignRepository;
import com.baeldung.lmock.service.defaultimpl.DefaultCampaignService;
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

    @Test
    @DisplayName("should create a campaign only if its ID is null")
    void givenNewCampaign_whenCreate_thenCampaignIsSaved() {
        // 1. Arrange: Create a campaign without an ID.
        Campaign newCampaign = new Campaign("NEW-CAMPAIGN", "New Campaign", "A fresh campaign.");
        
        // Create the expected campaign that the repository will return (with an ID).
        Campaign savedCampaign = new Campaign("NEW-CAMPAIGN", "New Campaign", "A fresh campaign.");
        savedCampaign.setId(1L);

        // Stub the save method. The argThat matcher is still used here to ensure
        // this stubbing is only active for a campaign with a null ID.
        when(campaignRepo.save(argThat(c -> c.getId() == null && c.getName().equals("New Campaign"))))
            .thenReturn(savedCampaign);

        // 2. Act: Call the service method to create the campaign.
        Campaign result = service.create(newCampaign);

        // Assert the state of the final returned object.
        assertNotNull(result, "The result from the service should not be null.");
        assertEquals(1L, result.getId(), "The returned campaign should have the ID assigned by the repository.");
    }
}
