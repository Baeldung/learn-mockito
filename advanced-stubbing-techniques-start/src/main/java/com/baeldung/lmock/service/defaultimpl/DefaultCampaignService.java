package com.baeldung.lmock.service.defaultimpl;

import java.util.List;
import java.util.Optional;

import com.baeldung.lmock.domain.model.Campaign;
import com.baeldung.lmock.domain.model.TaskStatus;
import com.baeldung.lmock.persistence.repository.CampaignRepository;
import com.baeldung.lmock.persistence.repository.inmemory.InMemoryCampaignRepository;
import com.baeldung.lmock.service.CampaignService;

public class DefaultCampaignService implements CampaignService {

    private CampaignRepository campaignRepository;

    public DefaultCampaignService() {
        this.campaignRepository = new InMemoryCampaignRepository();
    }

    public DefaultCampaignService(CampaignRepository campaignRepository) {
        super();
        this.campaignRepository = campaignRepository;
    }

    @Override
    public Optional<Campaign> findById(Long id) {
        return campaignRepository.findById(id);
    }

    @Override
    public Campaign create(Campaign campaign) {
        if (campaign.getId() != null) {
            throw new IllegalArgumentException("Can't create Campaign with assigned 'id'");
        }
        return campaignRepository.save(campaign);
    }

    @Override
    public List<Campaign> findCampaigns() {
        return campaignRepository.findAll();
    }

    @Override
    public Optional<Campaign> closeCampaign(Long id) {
        return campaignRepository.findById(id)
            .map(campaign -> {
                campaign.setClosed(true);
                campaign.getTasks()
                    .forEach(task -> {
                        task.setStatus(TaskStatus.DONE);
                    });
                return campaign;
            });

    }
}
