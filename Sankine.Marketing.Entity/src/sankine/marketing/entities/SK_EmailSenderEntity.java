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
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import sankine.framework.security.SK_PasswordUtils;
import sankine.plugin.base.SK_BaseEntity;
import sankine.plugin.base.SK_UUIDAttributeConverter;

@Entity
@Table(name = "DIM_EMAIL_SENDER")
public class SK_EmailSenderEntity extends SK_BaseEntity {
	
	@Transient
	private boolean selected;
	
	@Id
	@Column(name = "ID", nullable = false)
	@Convert(converter = SK_UUIDAttributeConverter.class)
	private UUID senderId;
	
	@Transient
	private Integer index;
	
	@Column(name = "V_USER")
	private String userName;
	
	@Column(name = "V_PASS")
	private String password;
	
	@Column(name = "V_FROM")
	private String email;
	
	@Column(name = "V_DISPLAY")
	private String displayName;
	
	@Column(name = "V_ACTIVE")
	private boolean isActive;
	
	@Column(name = "N_TOTAL_SEND")
	private Integer totalSend;
	
	@Transient
	private String mailServerName;
	
	@Transient
	private String emailTest;	

	@Transient
	private String securePassword;
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public UUID getSenderId() {
		return senderId;
	}

	public void setSenderId(UUID senderId) {
		this.senderId = senderId;
		super.setId(senderId);
	}
	
	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDisplayName() {
		
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Integer getTotalSend() {
		return totalSend;
	}

	public void setTotalSend(Integer totalSend) {
		this.totalSend = totalSend;
	}

	public String getMailServerName() {
		if(this.mailServer != null){
			return this.mailServer.getName();
		}
		return mailServerName;
	}

	public String getEmailTest() {
		return emailTest;
	}

	public void setEmailTest(String emailTest) {
		this.emailTest = emailTest;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SERVER_ID", referencedColumnName = "ID")
	@Convert(converter = SK_UUIDAttributeConverter.class)
	private SK_MailServerEntity mailServer;
			
	public SK_MailServerEntity getMailServer() {
		return mailServer;
	}

	public void setMailServer(SK_MailServerEntity mailServer) {
		this.mailServer = mailServer;
	}

	public SK_EmailSenderEntity() {
		super();
	}	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		SK_EmailSenderEntity identity = (SK_EmailSenderEntity) obj;
		return Objects.equals(senderId, identity.senderId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(senderId);
	}

	@Override
	public String toString() {		
		return this.getDisplayName();
	}

	@Override
	public boolean isGuidEmpty() {
		if (this.senderId == null) {
			return true;
		}
		return this.senderId.equals(new UUID(0L, 0L));
	}
}
