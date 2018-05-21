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
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import sankine.plugin.base.SK_BaseEntity;
import sankine.plugin.base.SK_UUIDAttributeConverter;

@Entity
@Table(name = "FCT_EMAIL_TRACKING")
public class SK_EmailReportDetailsEntity extends SK_BaseEntity {

	@Id
	@Column(name = "ID", nullable = false)
	@Convert(converter = SK_UUIDAttributeConverter.class)
	private UUID reportDetailsId;

	@Transient
	private boolean selected;
	
	@Transient
	private Integer index;
	
	@Column(name = "N_DATE_SKEY")
	private Integer skey;

	@Column(name = "D_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateDay;

	@Column(name = "N_TOTAL_SEND")
	private Integer totalSent;

	@Column(name = "N_TOTAL_OPEN")
	private Integer totalOpen;

	@Column(name = "N_TOTAL_BOUNCED")
	private Integer totalBounced;

	@Column(name = "N_TOTAL_UNSUB")
	private Integer totalUnsub;

	@Column(name = "V_EMAIL")
	private String eventLabel;

	@Column(name = "V_ACTION_TYPE")
	private String eventAction;

	@Column(name = "V_NETWORK_LOCATION")
	private String region;
	
	@Column(name = "V_COUNTRY")
	private String country;

	@Column(name = "V_CITY")
	private String city;

	@Column(name = "V_BROWER")
	private String browser;

	@Column(name = "V_MOBILE_DEVICE_INFO")
	private String browserVersion;
	
	@Column(name = "V_DEVICE_CATEGORY")
	private String deviceCategory;

	@Column(name = "V_OPERATOR_SYSTEM")
	private String operatorSystem;

	@Column(name = "V_OPERATOR_VERSION")
	private String operatorVersion;

	@Column(name = "V_MOBILE_DEVICE_MODEL")
	private String userType;

	@Transient
	private String yearWeek;

	@Transient
	private String yearMonth;

	@Column(name = "V_STATUS")
	private String vStatus;

	@Transient
	private String actionDate;

	@Transient
	private String hour;

	@Transient
	private String minute;

	@Transient
	private String campaignName;
	
	public UUID getReportDetailsId() {
		return reportDetailsId;
	}

	public void setReportDetailsId(UUID reportDetailsId) {
		this.reportDetailsId = reportDetailsId;
		super.setId(reportDetailsId);
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getSkey() {
		return skey;
	}

	public void setSkey(Integer skey) {
		this.skey = skey;
	}

	public Date getDateDay() {
		return dateDay;
	}

	public void setDateDay(Date dateDay) {
		this.dateDay = dateDay;
	}

	public Integer getTotalSent() {
		return totalSent;
	}

	public void setTotalSent(Integer totalSent) {
		this.totalSent = totalSent;
	}

	public Integer getTotalOpen() {
		return totalOpen;
	}

	public void setTotalOpen(Integer totalOpen) {
		this.totalOpen = totalOpen;
	}

	public Integer getTotalBounced() {
		return totalBounced;
	}

	public void setTotalBounced(Integer totalBounced) {
		this.totalBounced = totalBounced;
	}

	public Integer getTotalUnsub() {
		return totalUnsub;
	}

	public void setTotalUnsub(Integer totalUnsub) {
		this.totalUnsub = totalUnsub;
	}

	public String getEventLabel() {
		return eventLabel;
	}

	public void setEventLabel(String eventLabel) {
		this.eventLabel = eventLabel;
	}

	public String getEventAction() {
		return eventAction;
	}

	public void setEventAction(String eventAction) {
		this.eventAction = eventAction;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getBrowserVersion() {
		return browserVersion;
	}

	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}

	public String getDeviceCategory() {
		return deviceCategory;
	}

	public void setDeviceCategory(String deviceCategory) {
		this.deviceCategory = deviceCategory;
	}

	public String getOperatorSystem() {
		return operatorSystem;
	}

	public void setOperatorSystem(String operatorSystem) {
		this.operatorSystem = operatorSystem;
	}

	public String getOperatorVersion() {
		return operatorVersion;
	}

	public void setOperatorVersion(String operatorVersion) {
		this.operatorVersion = operatorVersion;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getYearWeek() {
		return yearWeek;
	}

	public void setYearWeek(String yearWeek) {
		this.yearWeek = yearWeek;
	}

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	public String getvStatus() {
		return vStatus;
	}

	public void setvStatus(String vStatus) {
		this.vStatus = vStatus;
	}

	public String getActionDate() {
		return actionDate;
	}

	public void setActionDate(String actionDate) {
		this.actionDate = actionDate;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getMinute() {
		return minute;
	}

	public void setMinute(String minute) {
		this.minute = minute;
	}	
	
	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getCampaignName() {
		return campaignName;
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

	@Column(name = "CAMPAIGN_ID")
	@Convert(converter = SK_UUIDAttributeConverter.class)
	private UUID campaignId;
		
	@Column(name = "GROUP_ID")
	@Convert(converter = SK_UUIDAttributeConverter.class)
	private UUID groupId;

	@Column(name = "TEMPLATE_ID")
	@Convert(converter = SK_UUIDAttributeConverter.class)
	private UUID templateId;
	
	public SK_EmailReportDetailsEntity() {
		super();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || getClass() != obj.getClass())
			return false;
		SK_EmailReportDetailsEntity identity = (SK_EmailReportDetailsEntity) obj;
		if (this.campaignId.equals(identity.campaignId) && this.groupId.equals(identity.groupId)
				&& this.templateId.equals(identity.templateId) && this.skey.equals(identity.skey) &&
				this.eventLabel.equalsIgnoreCase(identity.eventLabel)) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(reportDetailsId);
	}

	@Override
	public String toString() {
		return this.reportDetailsId.toString();
//		return this.campaign.getCampaignName().substring(25) + ".../" + this.customerReport.getName().substring(25) + ".../" + this.templateTracking.getSubject().substring(25) + "...";
		// return this.campaign.getCampaignName().substring(25) + ".../" + this.customerGroup.getName().substring(25) + "...";
	}

	@Override
	public boolean isGuidEmpty() {
		if (this.reportDetailsId == null) {
			return true;
		}
		return this.reportDetailsId.equals(new UUID(0L, 0L));
	}
}
