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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import sankine.plugin.base.SK_BaseEntity;
import sankine.plugin.base.SK_UUIDAttributeConverter;

@Entity
@Table(name = "DIM_CAMPAIGN")
public class SK_CampaignEntity extends SK_BaseEntity {

	@Id
	@Column(name = "ID")
	@Convert(converter = SK_UUIDAttributeConverter.class)
	private UUID campaignId;

	@Column(name = "CAMPAIGN_CODE")
	private String campaignCode;

	@Column(name = "CAMPAIGN_NAME")
	private String campaignName;
	
	@Column(name = "IS_ACTIVE")
	private boolean isActive;

	@Column(name = "IS_SHUTDOWN")
	private boolean isShutdown;

	@Column(name = "IS_TRACKING")
	private boolean isTracking;

	@Column(name = "MODIFIED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	@Column(name = "SPONSOR")
	private String sponsor;

	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "VALUE_PROPOSITION")
	private String valueProposition;

	@Column(name = "GOAL_DESCRIPTION")
	private String goalDescription;

	@Column(name = "START_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	@Column(name = "END_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	public UUID getCampaignId() {
		return this.campaignId;
	}

	public void setCampaignId(UUID campaignId) {
		this.campaignId = campaignId;
		super.setId(campaignId);
	}	
	
	public String getCampaignCode() {
		return campaignCode;
	}

	public void setCampaignCode(String campaignCode) {
		this.campaignCode = campaignCode;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean getIsShutdown() {
		return isShutdown;
	}

	public void setIsShutdown(boolean isShutdown) {
		this.isShutdown = isShutdown;
	}

	public boolean getIsTracking() {
		return isTracking;
	}

	public void setIsTracking(boolean isTracking) {
		this.isTracking = isTracking;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getSponsor() {
		return sponsor;
	}

	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValueProposition() {
		return valueProposition;
	}

	public void setValueProposition(String valueProposition) {
		this.valueProposition = valueProposition;
	}

	public String getGoalDescription() {
		return goalDescription;
	}

	public void setGoalDescription(String goalDescription) {
		this.goalDescription = goalDescription;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<SK_CampaignStrategyEntity> listStrategy = new ArrayList<>();
			
	public List<SK_CampaignStrategyEntity> getListStrategy() {
		return listStrategy;
	}

	public void setListStrategy(List<SK_CampaignStrategyEntity> listStrategy) {
		this.listStrategy = listStrategy;
	}

//	@OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@Fetch(value = FetchMode.SUBSELECT)
//	private List<SK_EmailReportDetailsEntity> listTracking = new ArrayList<>();
//		
//	public List<SK_EmailReportDetailsEntity> getListTracking() {
//		return listTracking;
//	}
//
//	public void setListTracking(List<SK_EmailReportDetailsEntity> listTracking) {
//		this.listTracking = listTracking;
//	}

	public SK_CampaignEntity() {
		super();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		SK_CampaignEntity identity = (SK_CampaignEntity) obj;
		return Objects.equals(campaignId, identity.campaignId);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(campaignId);
	}

	@Override
	public String toString() {
		return this.getCampaignName();
	}
	
	@Override
	public boolean isGuidEmpty() {
		if(this.campaignId == null){
			return true;
		}
		return this.campaignId.equals(new UUID( 0L , 0L ));
	}
}
