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

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import sankine.plugin.base.SK_BaseEntity;
import sankine.plugin.base.SK_UUIDAttributeConverter;

@Entity
@Table(name = "DIM_ACCOUNT")
public class SK_LoginEntity extends SK_BaseEntity {

	@Id
	@Column(name = "ID", nullable = false)
	@Convert(converter = SK_UUIDAttributeConverter.class)
	private UUID AccountId;

	@Column(name = "V_MEMBER")
	public String UserName;

	@Column(name = "V_VALUE")
	public String Password;

	@Transient
	public Boolean Active;
	
	@Transient
	public String newPassword;
	
	@Transient
	public String ConfirmNewPassword;

	@Transient
	public String DePassword;
	
	public UUID getAccountId() {
		return AccountId;
	}

	public void setAccountId(UUID accountId) {
		AccountId = accountId;
		super.setId(accountId);
	}
	
	
	public SK_LoginEntity() {
		super();
	}

	public SK_LoginEntity(String userName, String password) {
		super();
		UserName = userName;
		Password = password;
	}

	public SK_LoginEntity(UUID accountId, String userName, String password) {
		super();
		AccountId = accountId;
		UserName = userName;
		Password = password;
	}
}
