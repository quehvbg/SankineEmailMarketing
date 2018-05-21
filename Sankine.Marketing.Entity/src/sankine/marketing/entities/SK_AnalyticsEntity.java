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

import sankine.plugin.base.SK_BaseEntity;

public class SK_AnalyticsEntity extends SK_BaseEntity {
	private String Name;
	private Integer TotalSent;
	private Integer TotalSentFailed;
	private Integer TotalOpen;
	private Integer TotalUnsubcrible;

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public Integer getTotalSent() {
		return TotalSent;
	}

	public void setTotalSent(Integer totalSent) {
		TotalSent = totalSent;
	}

	public Integer getTotalSentFailed() {
		return TotalSentFailed;
	}

	public void setTotalSentFailed(Integer totalSentFailed) {
		TotalSentFailed = totalSentFailed;
	}

	public Integer getTotalOpen() {
		return TotalOpen;
	}

	public void setTotalOpen(Integer totalOpen) {
		TotalOpen = totalOpen;
	}

	public Integer getTotalUnsubcrible() {
		return TotalUnsubcrible;
	}

	public void setTotalUnsubcrible(Integer totalUnsubcrible) {
		TotalUnsubcrible = totalUnsubcrible;
	}

	public SK_AnalyticsEntity() {
		super();
	}

}
