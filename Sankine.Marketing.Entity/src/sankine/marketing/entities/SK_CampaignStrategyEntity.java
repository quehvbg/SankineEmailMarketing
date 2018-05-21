/*******************************************************************************
Copyright © 2017 Sankine Corporation Inc. All rights reserved.
Author: Hoang Van Que
Email: quehvbg@gmail.com
Phone : 0983.138.328
Description: 
Modifier :
Version : 1.0.0.1
********************************************************************************/

package sankine.marketing.entities;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import sankine.plugin.base.SK_BaseEntity;
import sankine.plugin.base.SK_UUIDAttributeConverter;

@Entity
@Table(name = "DIM_STRATEGY")
public class SK_CampaignStrategyEntity extends SK_BaseEntity {

	@Id
	@Column(name = "ID")
	@Convert(converter = SK_UUIDAttributeConverter.class)
	private UUID strategyId;

	@Column(name = "D_CREATED")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated;

	public UUID getStrategyId() {
		return this.strategyId;
	}

	public void setStrategyId(UUID strategyId) {
		this.strategyId = strategyId;
		super.setId(strategyId);
	}	
	
	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Column(name = "CAMPAIGN_ID" , insertable = false, updatable = false)
	@Convert(converter = SK_UUIDAttributeConverter.class)
	private UUID campaignId;
	
	@Column(name = "GROUP_ID" , insertable = false, updatable = false)
	@Convert(converter = SK_UUIDAttributeConverter.class)
	private UUID groupId;
	
	@Column(name = "TEMPLATE_ID" , insertable = false, updatable = false)
	@Convert(converter = SK_UUIDAttributeConverter.class)
	private UUID templateId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CAMPAIGN_ID", referencedColumnName = "ID")
	@Convert(converter = SK_UUIDAttributeConverter.class)
	private SK_CampaignEntity campaign;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GROUP_ID", referencedColumnName = "ID")
	@Convert(converter = SK_UUIDAttributeConverter.class)
	private SK_CustomerGroupEntity customerStrategy;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEMPLATE_ID", referencedColumnName = "ID")
	@Convert(converter = SK_UUIDAttributeConverter.class)
	private SK_EmailTemplateEntity templateStrategy;
	
	public SK_CampaignEntity getCampaign() {
		return campaign;
	}

	public void setCampaign(SK_CampaignEntity campaign) {
		this.campaign = campaign;
	}

	public SK_CustomerGroupEntity getCustomerStrategy() {
		return customerStrategy;
	}

	public void setCustomerStrategy(SK_CustomerGroupEntity customerStrategy) {
		this.customerStrategy = customerStrategy;
	}

	public SK_EmailTemplateEntity getTemplateStrategy() {
		return templateStrategy;
	}

	public void setTemplateStrategy(SK_EmailTemplateEntity templateStrategy) {
		this.templateStrategy = templateStrategy;
	}
	
	public UUID getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(UUID campaignId) {
		this.campaignId = campaignId;
	}

	public UUID getGroupId() {
		return groupId;
	}

	public void setGroupId(UUID groupId) {
		this.groupId = groupId;
	}

	public UUID getTemplateId() {
		return templateId;
	}

	public void setTemplateId(UUID templateId) {
		this.templateId = templateId;
	}

	public SK_CampaignStrategyEntity() {
		super();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		SK_CampaignStrategyEntity identity = (SK_CampaignStrategyEntity) obj;
		return Objects.equals(strategyId, identity.strategyId);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(strategyId);
	}

	@Override
	public String toString() {
		return this.campaign.getCampaignName() + "/" + this.customerStrategy.getName();
	}
	
	@Override
	public boolean isGuidEmpty() {
		if(this.strategyId == null){
			return true;
		}
		return this.strategyId.equals(new UUID( 0L , 0L ));
	}
}
