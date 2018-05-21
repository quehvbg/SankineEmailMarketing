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

import java.io.File;
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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;

import javafx.scene.image.Image;
import sankine.framework.utils.SK_FileUtils;
//import sankine.framework.utils.SK_TextUtils;
import sankine.plugin.base.SK_BaseEntity;
import sankine.plugin.base.SK_UUIDAttributeConverter;

@Entity
@Table(name = "DIM_EMAIL_TEMPLATE")
public class SK_EmailTemplateEntity extends SK_BaseEntity {

	@Id
	@Column(name = "ID", nullable = false)
	@Convert(converter = SK_UUIDAttributeConverter.class)
	private UUID templateId;

	@Column(name = "V_SUBJECT")
	private String subject;
	
	@Column(name = "V_CONFIDENTAL")
	private String confidental;

	@Column(name = "V_CATEGORY")
	private String category;

	@Column(name = "V_TYPE")
	private String types;

	@Column(name = "V_SYSTEM")
	private String isSystem;

	@Column(name = "V_DESCRIPTION")
	private String description;

	@Column(name = "V_MESSAGES")
	@Type(type = "org.hibernate.type.StringType")
	private String messages;

	@Transient
	private String messagesApi;
	
	@Column(name = "V_THUMBNAI")
	@Type(type = "org.hibernate.type.BinaryType")
	private byte[] thumbnai;

	@Transient
	private Image thumbnaImage;

	public UUID getTemplateId() {
		return templateId;
	}

	public void setTemplateId(UUID templateId) {
		this.templateId = templateId;
		super.setId(templateId);
	}

	public String getSubject() {
		return subject;
	}

	public String getShortSubject() {
		if(subject.length() > 25){
			return subject.substring(0, 25) + "...";
		}
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getConfidental() {
		return confidental;
	}

	public void setConfidental(String confidental) {
		this.confidental = confidental;
	}

	public String getCategory() {
		return category.trim();
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getType() {
		return types.trim();
	}

	public void setType(String Types) {
		this.types = Types;
	}

	public String getIsSystem() {
		return isSystem;
	}

	public void setIsSystem(String isSystem) {
		this.isSystem = isSystem;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}

	public String getMessagesApi() {
		return messagesApi;
	}

	public void setMessagesApi(String messages) {
		this.messagesApi = messages;
	}
	
//	public String getMessagesData() {
//		if(this.messages == null || this.messages.isEmpty()){
//			return "";
//		}
//		return SK_TextUtils.forHTML(this.messages).replace("\n", "");		
//		//return messagesData;
//	}
//
//	public void setMessagesData(String messagesData) {
//		this.messagesData = messagesData;
//	}

	public byte[] getThumbnai() {
		return thumbnai;
	}

	public void setThumbnai(byte[] thumbnai) {
		this.thumbnai = thumbnai;
	}

	public Image getThumbnaImage() {
		if(thumbnaImage != null){
			return thumbnaImage;
		}
		return SK_FileUtils.getJavaFXImage(thumbnai);
	}

	public void setThumbnaImage(Image thumbnaImage) {
		this.thumbnaImage = thumbnaImage;		
	}
	
	public void setThumbnaiExtend(File imgFile){
		this.thumbnai = SK_FileUtils.getByteImage(imgFile, 256, 256);
	}

	@OneToMany(mappedBy = "templateStrategy", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<SK_CampaignStrategyEntity> listStrategy = new ArrayList<>();
			
	public List<SK_CampaignStrategyEntity> getListStrategy() {
		return listStrategy;
	}

	public void setListStrategy(List<SK_CampaignStrategyEntity> listStrategy) {
		this.listStrategy = listStrategy;
	}

//	@OneToMany(mappedBy = "templateTracking", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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
	
	public SK_EmailTemplateEntity() {
		super();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		SK_EmailTemplateEntity identity = (SK_EmailTemplateEntity) obj;
		return Objects.equals(templateId, identity.templateId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(templateId);
	}

	@Override
	public String toString() {
		return this.getSubject();
	}

	@Override
	public boolean isGuidEmpty() {
		if (this.templateId == null) {
			return true;
		}
		return this.templateId.equals(new UUID(0L, 0L));
	}
}
