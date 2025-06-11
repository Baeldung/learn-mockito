package com.baeldung.lmock.service.defaultimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.baeldung.lmock.domain.model.Campaign;
import com.baeldung.lmock.persistence.repository.CampaignRepository;
import com.baeldung.lmock.persistence.repository.inmemory.InMemoryCampaignRepository;

@ExtendWith(MockitoExtension.class)
class DefaultCampaignServiceSpyUnitTest {

    @Spy
    InMemoryCampaignRepository spyRepository;
    @InjectMocks
    DefaultCampaignService service;

    @Test
    void givenSpyOnRepository_whenCallingMethod_thenRealMethodIsCalled() {
        // Given
        Campaign sampleCampaign = new Campaign();
        sampleCampaign.setId(1L);
        spyRepository.save(sampleCampaign);

        // When
        Optional<Campaign> result = service.findById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get()
            .getId());
        verify(spyRepository).save(sampleCampaign);
        // findById is called in spyRepository.save and service.findById
        verify(spyRepository, times(2)).findById(1L);
    }

    @Test
    void givenSpyOnRepositoryUsingStaticMethod_whenCallingMethod_thenRealMethodIsCalled() {
        // Given
        Campaign sampleCampaign = new Campaign();
        sampleCampaign.setId(1L);
        CampaignRepository realRepository = new InMemoryCampaignRepository();
        CampaignRepository spyRepository = spy(realRepository);
        spyRepository.save(sampleCampaign);
        DefaultCampaignService service = new DefaultCampaignService(spyRepository);
        // When
        Optional<Campaign> result = service.findById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get()
            .getId());
        verify(spyRepository).save(sampleCampaign);
        // findById is called in spyRepository.save and service.findById
        verify(spyRepository, times(2)).findById(1L);
    }

    @Test
    void givenSpyOnRepositoryUsingSpyStaticFunction_whenCallingMethod_thenRealMethodIsCalled() {
        // Given
        Campaign sampleCampaign = new Campaign();
        sampleCampaign.setId(1L);

        CampaignRepository anotherSpy = spy(new InMemoryCampaignRepository());
        anotherSpy.save(sampleCampaign);

        DefaultCampaignService service = new DefaultCampaignService(anotherSpy);

        // When
        Optional<Campaign> result = service.findById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get()
            .getId());
        // findById is called in spyRepository.save and service.findById
        verify(anotherSpy, times(2)).findById(1L);
    }

    @Test
    void givenSpyOnRepository_whenCallingMethodWithOverride_thenStubbedMethodIsCalled() {
        // Given
        CampaignRepository realRepository = new InMemoryCampaignRepository();
        CampaignRepository spyRepository = spy(realRepository);

        Campaign stubbedCampaign = new Campaign(); // no id
        when(spyRepository.findById(1L)).thenReturn(Optional.of(stubbedCampaign));

        DefaultCampaignService service = new DefaultCampaignService(spyRepository);

        // When
        Optional<Campaign> result = service.findById(1L);

        // Then
        assertTrue(result.isPresent());
        assertNull(result.get()
            .getId()); // since it's a stubbed empty campaign
        verify(spyRepository).findById(1L);
    }

    @Test
    void givenSpyOnRepository_whenCallingMethodWithOverride_thenStubbedMethodIsCalledInsteadOfReal() {
        // Given
        InMemoryCampaignRepository realRepo = new InMemoryCampaignRepository(); // Real repository
        InMemoryCampaignRepository repoSpy = spy(realRepo); // Spy on the real repository

        // Create a sample campaign
        Campaign campaign = new Campaign("CODE", "Name", "Desc");

        // Stub the findAll() method to always return an empty list
        when(repoSpy.findAll()).thenReturn(Collections.emptyList());

        DefaultCampaignService service = new DefaultCampaignService(repoSpy);

        // When
        service.create(campaign);

        List<Campaign> allCampaigns = service.findCampaigns();

        // Then verify invocations
        assertTrue(allCampaigns.isEmpty());

        verify(repoSpy).save(campaign);
    }
}
