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

import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import sankine.plugin.base.SK_BaseEntity;
import sankine.plugin.base.SK_UUIDAttributeConverter;

@Entity
@Table(name = "DIM_CONFIGURATION")
public class SK_ParameterEntity extends SK_BaseEntity {

	@Id
	@Column(name = "ID", nullable = false)
	@Convert(converter = SK_UUIDAttributeConverter.class)
	private UUID paraId;

	@Column(name = "V_TITLE")
	private String title;	
	
	@Column(name = "V_TYPE")
	private String type;

	@Column(name = "V_PARAMETER")
	private String parameter;

	@Column(name = "V_VALUE")
	private String value;
	
	@Column(name = "V_DESCRIPTION")
	private String description;
	
	@Column(name = "V_SCOPE")
	private String scope;
	
	@Column(name = "V_ACTIVE")
	private boolean isActive;

	@Column(name = "V_REANONLY")
	private boolean isReadonly;
	
	public UUID getParaId() {
		return paraId;
	}

	public void setParaId(UUID paraId) {
		this.paraId = paraId;
		super.setId(paraId);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean getIsReadonly() {
		return isReadonly;
	}

	public void setIsReadonly(boolean isReadonly) {
		this.isReadonly = isReadonly;
	}	

	public SK_ParameterEntity() {
		super();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		SK_ParameterEntity identity = (SK_ParameterEntity) obj;
		return Objects.equals(paraId, identity.paraId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(paraId);
	}

	@Override
	public String toString() {
		return this.getType() + " " + this.getParameter();
	}

	@Override
	public boolean isGuidEmpty() {
		if (this.paraId == null) {
			return true;
		}
		return this.paraId.equals(new UUID(0L, 0L));
	}
}
