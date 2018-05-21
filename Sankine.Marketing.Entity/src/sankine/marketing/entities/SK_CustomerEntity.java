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
import javax.persistence.Transient;

import sankine.plugin.base.SK_BaseEntity;
import sankine.plugin.base.SK_UUIDAttributeConverter;

@Entity
@Table(name = "DIM_RECIPIENT")
public class SK_CustomerEntity extends SK_BaseEntity{
	
	@Id
	@Column(name = "ID", nullable = false)
	@Convert(converter = SK_UUIDAttributeConverter.class)
	private UUID customerId;
	
	@Transient
	private Boolean selected;
	
	@Transient
	private Integer index;
	
	@Transient
	private String status;
		
	@Column(name = "V_PROVINCE")
	private String province;
	
	@Column(name = "V_TITLE")
	private String title;
	
	@Column(name = "V_EMAIL")
	private String email;
	
	@Column(name = "V_FIRST_NAME")
	private String firstName;

	@Column(name = "V_MID_NAME")
	private String midName;
	
	@Column(name = "V_LAST_NAME")
	private String lastName;
	
	@Transient
	private String displayName;
	
	@Column(name = "V_TYPE")
	private String type;
	
	@Column(name = "V_UNSUBCRIBLE")
	private boolean isUnsubcrible;
	
	@Column(name = "D_CREATED")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@Column(name = "D_UNSUBCRIBLE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date unsubcribleDate;
	
	@Transient
	private String groupName;

	@Transient
	private UUID campaignId;
	
	@Transient
	private UUID groupId;
	
	@Transient
	private UUID templateId;
	
	@Transient
	private SK_CampaignEntity campaign;
	
	@Transient
	private SK_EmailTemplateEntity template;
	
	@Transient
	private boolean tracking;
	
	public UUID getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(UUID customerId) {
		this.customerId = customerId;
		super.setId(customerId);
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMidName() {
		return midName;
	}

	public void setMidName(String midName) {
		this.midName = midName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDisplayName() {
		if(midName != null && !midName.isEmpty()){
			displayName = lastName + " " + firstName + " " + midName;
		}else if(lastName != null && firstName != null){
			displayName =  lastName + " " +  firstName ;
		}else if(firstName != null){
			displayName = firstName;
		}		
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean getIsUnsubcrible() {
		return isUnsubcrible;
	}

	public void setIsUnsubcrible(boolean isUnsubcrible) {
		this.isUnsubcrible = isUnsubcrible;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUnsubcribleDate() {
		return unsubcribleDate;
	}

	public void setUnsubcribleDate(Date unsubcribleDate) {
		this.unsubcribleDate = unsubcribleDate;
	}
	
	public String getGroupName() {
		//return this.customerGroup.getName();
//		return  this.groupName;
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GROUP_ID", referencedColumnName = "ID")
	@Convert(converter = SK_UUIDAttributeConverter.class)
	private SK_CustomerGroupEntity customerGroup;
	
	public SK_CustomerGroupEntity getCustomerGroup() {
		return customerGroup;
	}
	
	public void setCustomerGroup(SK_CustomerGroupEntity customerGroup) {
		this.customerGroup = customerGroup;
	}

	public SK_CampaignEntity getCampaign() {
		return campaign;
	}

	public void setCampaign(SK_CampaignEntity campaign) {
		this.campaign = campaign;
	}

	public SK_EmailTemplateEntity getTemplate() {
		return template;
	}

	public void setTemplate(SK_EmailTemplateEntity template) {
		this.template = template;
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
	
	public boolean isTracking() {
		return tracking;
	}

	public void setTracking(boolean tracking) {
		this.tracking = tracking;
	}

	public SK_CustomerEntity() {
		super();
		setSelected(false);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		SK_CustomerEntity identity = (SK_CustomerEntity) obj;
		return Objects.equals(customerId, identity.customerId);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(customerId);
	}

	@Override
	public String toString() {		
		return this.getDisplayName();
	}
	
	@Override
	public boolean isGuidEmpty() {
		if(this.customerId == null){
			return true;
		}
		return this.customerId.equals(new UUID( 0L , 0L ));
	}
}
