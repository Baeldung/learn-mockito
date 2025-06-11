package com.baeldung.lmock.service;

import java.util.List;
import java.util.Optional;

import com.baeldung.lmock.domain.model.Campaign;

public interface CampaignService {

    List<Campaign> findCampaigns();

    Optional<Campaign> findById(Long id);

    Campaign create(Campaign campaign);

    Optional<Campaign> closeCampaign(Long id);

    void deleteCampaign(Long id);
}
