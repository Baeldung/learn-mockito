package com.baeldung.lmock.service.defaultimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.baeldung.lmock.domain.model.Campaign;
import com.baeldung.lmock.persistence.repository.CampaignRepository;
import com.baeldung.lmock.service.CampaignService;

@ExtendWith(MockitoExtension.class)
class DefaultCampaignServiceExceptionUnitTest {

    @Mock
    CampaignRepository campaignRepository;
    
    @Test
    void givenExceptionThrown_whenClosingCampaign_thenItIsPropagated() {
        CampaignService campaignService = new DefaultCampaignService(campaignRepository);
        when(campaignRepository.findById(1L)).thenThrow(new IllegalStateException("DB failure"));
        
        Optional<Campaign> result = campaignService.findById(1L);
        
        assertTrue(result.isEmpty());
    }

    @Test
    void givenInvalidCampaignId_whenDeleting_thenPropagateThrownException() {
        CampaignService campaignService = new DefaultCampaignService(campaignRepository);
        doThrow(new IllegalStateException("Can't delete")).when(campaignRepository).deleteById(eq(1L));
        assertThrows(IllegalStateException.class, () -> {
            campaignService.deleteCampaign(1L);
        });
    }

    @Test
    void givenExceptionThrown_whenClosingCampaign_thenVerifyTheExceptionMessage() {
        CampaignService campaignService = new DefaultCampaignService(campaignRepository);
        when(campaignRepository.findById(1L)).thenThrow(new IllegalArgumentException("Illegal Argument"));
        RuntimeException ex = assertThrows(IllegalArgumentException.class, () -> {
            campaignService.closeCampaign(1L);
        });
        Mockito.verify(campaignRepository).findById(1L);
        assertEquals("Illegal Argument", ex.getMessage());
        
        Mockito.verify(campaignRepository, Mockito.never()).save(any());
    }

}
