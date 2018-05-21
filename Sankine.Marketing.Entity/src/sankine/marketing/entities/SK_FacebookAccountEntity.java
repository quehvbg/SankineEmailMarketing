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
import javax.persistence.Transient;

import sankine.framework.security.SK_PasswordUtils;
import sankine.plugin.base.SK_BaseEntity;
import sankine.plugin.base.SK_UUIDAttributeConverter;

@Entity
@Table(name = "DIM_FACE_ACCOUNT")
public class SK_FacebookAccountEntity extends SK_BaseEntity {

	@Transient
	private String index;
	
	@Transient
	private boolean selected;
	
	@Id
	@Column(name = "ID", nullable = false)
	@Convert(converter = SK_UUIDAttributeConverter.class)
	private UUID facebookId;

	@Column(name = "V_USER")
	private String userName;

	@Column(name = "V_PASS")
	private String password;

	@Column(name = "V_TOKEN")
	private String token;

	@Column(name = "V_ACTIVE")
	private boolean isActive;

	@Transient
	private String securePassword;
	
	@Transient
	private String status;
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public UUID getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(UUID facebookId) {
		this.facebookId = facebookId;
		super.setId(facebookId);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
		
	public String getSecurePassword() {
		if(password != null && !password.isEmpty()){
			return SK_PasswordUtils.hiddenPassword(password);
		}
		return null;
	}

	public void setSecurePassword(String securePassword) {
		this.securePassword = securePassword;
	}	
	
	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}	
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public SK_FacebookAccountEntity() {
		super();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		SK_FacebookAccountEntity identity = (SK_FacebookAccountEntity) obj;		
		return Objects.equals(facebookId, identity.facebookId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(facebookId);
	}

	@Override
	public String toString() {		
		return this.getUserName();
	}

	@Override
	public boolean isGuidEmpty() {
		if (this.facebookId == null) {
			return true;
		}
		return this.facebookId.equals(new UUID(0L, 0L));
	}
}
