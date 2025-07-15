package com.baeldung.lmock.service.defaultimpl;

import com.baeldung.lmock.persistence.repository.CampaignRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.Closeable;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultCampaignServiceTest {

    private AutoCloseable closeable;

    @Mock(extraInterfaces = Closeable.class) CampaignRepository campaignRepository;

    @InjectMocks DefaultCampaignService defaultCampaignService;

    @Test
    void givenCampaignRepository_whenInitialized_thenMockImplementsCloseable() {
        assertInstanceOf(Closeable.class, campaignRepository);
    }

    @Test
    void givenInvalidId_whenFindById_thenEmptyResult() {
        var result = defaultCampaignService.findById(1L);
        assertTrue(result.isEmpty());
    }

    @BeforeEach
    void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach void closeMocks() throws Exception{
        closeable.close();
    }
}