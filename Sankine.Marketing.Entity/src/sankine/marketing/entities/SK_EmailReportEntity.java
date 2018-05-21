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

import sankine.plugin.base.SK_BaseEntity;
import sankine.plugin.base.SK_UUIDAttributeConverter;

@Entity
@Table(name = "FCT_EMAIL_REPORT")
public class SK_EmailReportEntity extends SK_BaseEntity {

	@Id
	@Column(name = "ID", nullable = false)
	@Convert(converter = SK_UUIDAttributeConverter.class)
	private UUID reportId;

	@Column(name = "N_DATE_SKEY")
	private Integer nDateSkey;

	@Column(name = "D_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateDay;

	@Column(name = "N_TOTAL_SEND")
	private Integer totalSent;

	@Column(name = "V_STATUS")
	private String status;

	public UUID getReportId() {
		return reportId;
	}

	public void setReportId(UUID reportId) {
		this.reportId = reportId;
		super.setId(reportId);
	}

	public Integer getnDateSkey() {
		return nDateSkey;
	}

	public void setnDateSkey(Integer nDateSkey) {
		this.nDateSkey = nDateSkey;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
		
	public SK_EmailReportEntity() {
		super();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		SK_EmailReportEntity identity = (SK_EmailReportEntity) obj;
		return Objects.equals(reportId, identity.reportId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(reportId);
	}

	@Override
	public String toString() {
		return this.dateDay.toString() + "/" + this.totalSent;
	}

	@Override
	public boolean isGuidEmpty() {
		if (this.reportId == null) {
			return true;
		}
		return this.reportId.equals(new UUID(0L, 0L));
	}
}
