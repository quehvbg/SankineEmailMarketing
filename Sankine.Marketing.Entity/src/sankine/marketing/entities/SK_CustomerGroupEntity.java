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

import java.math.BigInteger;
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
import javax.persistence.Transient;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import sankine.plugin.base.SK_BaseEntity;
import sankine.plugin.base.SK_UUIDAttributeConverter;

@Entity
@Table(name = "DIM_GROUP") 
public class SK_CustomerGroupEntity extends SK_BaseEntity {

	@Id
	@Column(name = "ID", nullable = false)
	@Convert(converter = SK_UUIDAttributeConverter.class)
	private UUID groupId;

	@Transient
	public Boolean selected;

	@Column(name = "V_NAME")
	private String name;
	
	@Transient
	private String nameDecode;
	
	@Column(name = "V_ACTIVE", length = 5)
	private String isActive;

	@Column(name = "V_REANONLY", length = 5)
	private String isReadonly;

	@Transient
	private BigInteger totalEmail;

	@Column(name = "D_CREATED")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@OneToMany(mappedBy = "customerGroup", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<SK_CustomerEntity> listCustomer = new ArrayList<>();

	@OneToMany(mappedBy = "customerStrategy", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<SK_CampaignStrategyEntity> listStrategy = new ArrayList<>();

//	@OneToMany(mappedBy = "customerReport", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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

	public void setGroupId(UUID groupId) {
		this.groupId = groupId;
		super.setId(groupId);
	}

	public UUID getGroupId() {
		return groupId;
	}

	public Boolean isSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getIsReadonly() {
		return isReadonly;
	}

	public void setIsReadonly(String isReadonly) {
		this.isReadonly = isReadonly;
	}

	public BigInteger getTotalEmail() {
		// return listCustomer.size();
		return totalEmail;
	}

	public void setTotalEmail(BigInteger totalEmail) {
		this.totalEmail = totalEmail;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public List<SK_CustomerEntity> getListCustomer() {
		return listCustomer;
	}

	public void setListCustomer(List<SK_CustomerEntity> listCustomer) {
		this.listCustomer = listCustomer;
	}

	public List<SK_CampaignStrategyEntity> getListStrategy() {
		return listStrategy;
	}

	public void setListStrategy(List<SK_CampaignStrategyEntity> listStrategy) {
		this.listStrategy = listStrategy;
	}

	public SK_CustomerGroupEntity() {
		super();
	}

	public SK_CustomerGroupEntity(String name, String isActive, String isReadonly) {
		this.groupId = this.defaultGuid();
		this.name = name;
		this.isActive = isActive;
		this.isReadonly = isReadonly;
		this.totalEmail = BigInteger.valueOf(0);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		SK_CustomerGroupEntity identity = (SK_CustomerGroupEntity) obj;
		return Objects.equals(this.groupId, identity.groupId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.groupId);
	}

	@Override
	public String toString() {
		return this.getName();
	}

	@Override
	public boolean isGuidEmpty() {
		if (this.groupId == null) {
			return true;
		}
		return this.groupId.equals(new UUID(0L, 0L));
	}
}
