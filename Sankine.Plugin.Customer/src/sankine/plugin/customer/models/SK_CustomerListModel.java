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

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sankine.framework.common.SK_ComboBoxItem;
import sankine.marketing.entities.SK_CustomerEntity;
import sankine.marketing.entities.SK_CustomerGroupEntity;

public class SK_CustomerListModel {
	public SK_CustomerEntity Entity;
	public ObservableList<SK_CustomerEntity> listEntity;
	public ObservableList<SK_CustomerEntity> listAllEntity;
	public List<SK_CustomerEntity> listSelected;
	public List<SK_CustomerEntity> listVerify;
	public ObservableList<SK_CustomerGroupEntity> listGroups;
	public ObservableList<SK_CustomerEntity> listInsert;
	public ObservableList<SK_CustomerEntity> listUpdate;
	public ObservableList<SK_ComboBoxItem> listViewRow;
	
	public SK_CustomerListModel() {
		listAllEntity = FXCollections.observableArrayList();
		listEntity = FXCollections.observableArrayList();
		listGroups = FXCollections.observableArrayList();
		listSelected = new ArrayList<SK_CustomerEntity>();
		listVerify = new ArrayList<SK_CustomerEntity>();
		listInsert = FXCollections.observableArrayList();
		listUpdate = FXCollections.observableArrayList();
		listViewRow = FXCollections.observableArrayList();
	}	
}	
