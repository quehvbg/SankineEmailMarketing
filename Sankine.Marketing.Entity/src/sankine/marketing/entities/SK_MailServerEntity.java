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
import javax.persistence.Transient;

import sankine.plugin.base.SK_BaseEntity;
import sankine.plugin.base.SK_UUIDAttributeConverter;

@Entity
@Table(name = "DIM_MAIL_SERVER")
public class SK_MailServerEntity extends SK_BaseEntity {

	@Id
	@Column(name = "ID", nullable = false)
	@Convert(converter = SK_UUIDAttributeConverter.class)
	private UUID serverId;

	@Column(name = "V_TYPE")
	private String types;

	@Column(name = "V_NAME")
	private String name;

	@Column(name = "V_ADDRESS")
	private String address;

	@Column(name = "V_AUTHENTICATION")
	private String auth;

	@Column(name = "N_PORT")
	private Integer port;

	@Transient
	private String displayName;
			
	@OneToMany(mappedBy = "mailServer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<SK_EmailSenderEntity> listEmailSender = new ArrayList<>();
	
	public UUID getServerId() {
		return serverId;
	}

	public void setServerId(UUID serverId) {
		this.serverId = serverId;
		super.setId(serverId);
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getDisplayName() {
		displayName = this.name + " " + this.types;
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
		
	public List<SK_EmailSenderEntity> getListEmailSender() {
		return listEmailSender;
	}
	
	public void setListEmailSender(List<SK_EmailSenderEntity> listEmailSender) {
		this.listEmailSender = listEmailSender;
	}

	public SK_MailServerEntity() {
		super();
	}

	public SK_MailServerEntity(String types, String name, String address, String auth, Integer port) {		
		this.serverId = this.defaultGuid();
		this.types = types;
		this.name = name;
		this.address = address;
		this.auth = auth;
		this.port = port;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		SK_MailServerEntity identity = (SK_MailServerEntity) obj;
		return Objects.equals(serverId, identity.serverId);
	}
	
	@Override
	public int hashCode() {		
		return Objects.hash(serverId);
	}

	@Override
	public String toString() {
		return this.getName();
	}
	
	@Override
	public boolean isGuidEmpty() {
		if(this.serverId == null){
			return true;
		}
		return this.serverId.equals(new UUID( 0L , 0L ));
	}
}
