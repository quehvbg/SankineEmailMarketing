/*******************************************************************************
Copyright © 2017 Sankine Corporation Inc. All rights reserved.
Author: Hoang Van Que
Email: quehvbg@gmail.com
Phone : 0983.138.328
Description: 
Modifier :
Version : 1.0.0.1
********************************************************************************/

package sankine.plugin.customer.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sankine.marketing.entities.SK_CustomerGroupEntity;

public class SK_CustomerGroupModel {
	public SK_CustomerGroupEntity Entity;
	public ObservableList<SK_CustomerGroupEntity> listEntity;
	
	public SK_CustomerGroupModel() {
		listEntity = FXCollections.observableArrayList();
	}	
}
