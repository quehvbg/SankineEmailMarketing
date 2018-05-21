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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import sankine.plugin.base.SK_BaseEntity;

@Entity
@Table(name = "DIM_TIME")
public class SK_DimtimeEntity extends SK_BaseEntity {

	@Id
	@Column(name = "N_DATE_SKEY", nullable = false)
	private Integer skey;

	@Column(name = "N_DATE7")
	private Integer date7;

	@Column(name = "D_CALENDAR_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date calendarDate;

	@Column(name = "D_WEEK_START_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date weekStartDate;

	@Column(name = "D_WEEK_END_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date weekEndDate;

	@Column(name = "D_MONTH_START_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date monthStartDate;

	@Column(name = "D_MONTH_END_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date monthEndDate;

	@Column(name = "D_QUARTER_START_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date quarterStartDate;

	@Column(name = "D_QUARTER_END_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date quarterEndDate;

	@Column(name = "N_WEEK_CALENDAR")
	private Integer weekCalendar;

	@Column(name = "N_MONTH_CALENDAR")
	private Integer monthCalendar;

	@Column(name = "N_QTR_CALENDAR")
	private Integer qtrCalendar;

	@Column(name = "N_YEAR_CALENDAR")
	private Integer yearCalendar;

	@Column(name = "N_DAY_OF_WEEK")
	private Integer dayOfWeek;

	@Column(name = "N_DAY_OF_MONTH")
	private Integer dayOfMonth;

	@Column(name = "N_DAY_MONTH_COUNT")
	private Integer dayMonthCount;

	@Column(name = "N_DAY_OF_QUARTER")
	private Integer dayOfQuarter;

	@Column(name = "N_DAY_QUARTER_COUNT")
	private Integer dayQuarterCount;

	@Column(name = "N_DAY_OF_YEAR")
	private Integer dayOfYear;

	@Column(name = "N_DAY_YEAR_COUNT")
	private Integer dayYearCount;

	public Number getSkey() {
		return skey;
	}

	public Integer getDate7() {
		return date7;
	}

	public void setDate7(Integer date7) {
		this.date7 = date7;
	}

	public Date getCalendarDate() {
		return calendarDate;
	}

	public void setCalendarDate(Date calendarDate) {
		this.calendarDate = calendarDate;
	}

	public Date getWeekStartDate() {
		return weekStartDate;
	}

	public void setWeekStartDate(Date weekStartDate) {
		this.weekStartDate = weekStartDate;
	}

	public Date getWeekEndDate() {
		return weekEndDate;
	}

	public void setWeekEndDate(Date weekEndDate) {
		this.weekEndDate = weekEndDate;
	}

	public Date getMonthStartDate() {
		return monthStartDate;
	}

	public void setMonthStartDate(Date monthStartDate) {
		this.monthStartDate = monthStartDate;
	}

	public Date getMonthEndDate() {
		return monthEndDate;
	}

	public void setMonthEndDate(Date monthEndDate) {
		this.monthEndDate = monthEndDate;
	}

	public Date getQuarterStartDate() {
		return quarterStartDate;
	}

	public void setQuarterStartDate(Date quarterStartDate) {
		this.quarterStartDate = quarterStartDate;
	}

	public Date getQuarterEndDate() {
		return quarterEndDate;
	}

	public void setQuarterEndDate(Date quarterEndDate) {
		this.quarterEndDate = quarterEndDate;
	}

	public Integer getWeekCalendar() {
		return weekCalendar;
	}

	public void setWeekCalendar(Integer weekCalendar) {
		this.weekCalendar = weekCalendar;
	}

	public Integer getMonthCalendar() {
		return monthCalendar;
	}

	public void setMonthCalendar(Integer monthCalendar) {
		this.monthCalendar = monthCalendar;
	}

	public Integer getQtrCalendar() {
		return qtrCalendar;
	}

	public void setQtrCalendar(Integer qtrCalendar) {
		this.qtrCalendar = qtrCalendar;
	}

	public Integer getYearCalendar() {
		return yearCalendar;
	}

	public void setYearCalendar(Integer yearCalendar) {
		this.yearCalendar = yearCalendar;
	}

	public Integer getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(Integer dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public Integer getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(Integer dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public Integer getDayMonthCount() {
		return dayMonthCount;
	}

	public void setDayMonthCount(Integer dayMonthCount) {
		this.dayMonthCount = dayMonthCount;
	}

	public Integer getDayOfQuarter() {
		return dayOfQuarter;
	}

	public void setDayOfQuarter(Integer dayOfQuarter) {
		this.dayOfQuarter = dayOfQuarter;
	}

	public Integer getDayQuarterCount() {
		return dayQuarterCount;
	}

	public void setDayQuarterCount(Integer dayQuarterCount) {
		this.dayQuarterCount = dayQuarterCount;
	}

	public Integer getDayOfYear() {
		return dayOfYear;
	}

	public void setDayOfYear(Integer dayOfYear) {
		this.dayOfYear = dayOfYear;
	}

	public Integer getDayYearCount() {
		return dayYearCount;
	}

	public void setDayYearCount(Integer dayYearCount) {
		this.dayYearCount = dayYearCount;
	}

	public void setSkey(Integer skey) {
		this.skey = skey;
	}

	public SK_DimtimeEntity() {
		super();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		SK_DimtimeEntity identity = (SK_DimtimeEntity) obj;
		return Objects.equals(this.skey, identity.skey);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.skey);
	}

	@Override
	public String toString() {
		return this.calendarDate.toString();
	}

	@Override
	public boolean isGuidEmpty() {
		if (this.skey == null) {
			return true;
		}
		return this.skey.equals(0);
	}
}
