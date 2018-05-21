/*******************************************************************************
Copyright © 2017 Sankine Corporation Inc. All rights reserved.
Author: Hoang Van Que
Email: quehvbg@gmail.com
Phone : 0983.138.328
Description: 
Modifier :
Version : 1.0.0.1
********************************************************************************/

package sankine.plugin.customer.controllers;

import java.net.URL;
import java.util.ResourceBundle;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import sankine.marketing.entities.SK_CustomerGroupEntity;
import sankine.plugin.core.SK_Application;
import sankine.plugin.customer.models.SK_CustomerGroupModel;
import sankine.plugin.customer.services.SK_CustomerGroupService;
import sankine.plugin.messages.SK_GetAllRequest;
import sankine.plugin.messages.SK_GetAllResponse;
import sankine.plugin.messages.SK_SaveRequest;
import sankine.plugin.messages.SK_SaveResponse;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class SK_PluginCustomerGroupController implements Initializable {

	private SK_CustomerGroupService service;
	private SK_CustomerGroupModel model;
	private SK_Application application;
	private ResourceBundle lang;

	@FXML
	private TextField groupName;
	
	@FXML
    private TableView<SK_CustomerGroupEntity> tblDataList;

    @FXML
    private TableColumn<Object, Object> tblClmName;
    @FXML
    private TableColumn<Object, Object> tblClmTotal;
    @FXML
    private TableColumn<Object, Object> tblClmDate;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		model = new SK_CustomerGroupModel();
		application = SK_Application.getInstance();
		lang = application.getLanguage();
		clearData();
		reloadData();
	}

	public SK_PluginCustomerGroupController() {
		service = new SK_CustomerGroupService();
	}	
	
	@FXML
	public void resetForm(ActionEvent event) {
		clearData();
	}
	
	private void clearData(){
		model.Entity = new SK_CustomerGroupEntity();
		groupName.setText("");
	}
	
	private void reloadData(){
		model.listEntity.clear();
		SK_GetAllResponse<SK_CustomerGroupEntity> responseGroup = service.GetAllEntityExtend(new SK_GetAllRequest<SK_CustomerGroupEntity>());
		if(responseGroup.listEntity != null && responseGroup.listEntity.size() > 0){
			model.listEntity.addAll(responseGroup.listEntity);
		}				
		tblClmName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tblClmTotal.setCellValueFactory(new PropertyValueFactory<>("totalEmail"));
		tblClmDate.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
		tblDataList.setItems(model.listEntity);		
	}
	
	@FXML
	public void saveChange(ActionEvent event) {
		if (groupName.getText() != null && !groupName.getText().isEmpty()) {	
			if (model.Entity == null || model.Entity.getGroupId() == null) {
				model.Entity = new SK_CustomerGroupEntity();
				model.Entity.setGroupId(model.Entity.defaultGuid());
			}else{
				model.Entity.setCreatedDate(model.Entity.getCreatedDate());
			}
			model.Entity.setName(groupName.getText());
			model.Entity.setIsReadonly("False");
			model.Entity.setIsActive("True");
			SK_SaveRequest<SK_CustomerGroupEntity> request = new SK_SaveRequest<SK_CustomerGroupEntity>(model.Entity);				
			SK_SaveResponse<SK_CustomerGroupEntity> response = service.SaveEntity(request);
			if (!response.Success) {
				TrayNotification tn = new TrayNotification(lang.getString("key88"), response.Description, NotificationType.ERROR);
                tn.showAndDismiss(Duration.seconds(5));
			}else{				
				clearData();
            	reloadData();
            	TrayNotification tn = new TrayNotification(lang.getString("key88"), lang.getString("key89"), NotificationType.SUCCESS);
            	tn.showAndDismiss(Duration.seconds(5));           	
			}
		} else {		
			TrayNotification tn = new TrayNotification(lang.getString("key88"), lang.getString("key90"),
					NotificationType.ERROR);
			tn.showAndDismiss(Duration.seconds(5));
		}
	}

	
	@FXML
	private void selectedAction(MouseEvent event) {
        if(model.listEntity != null && model.listEntity.size() > 0 && event.getClickCount() == 2){
        	model.Entity = tblDataList.getSelectionModel().getSelectedItem();
        	groupName.setText(model.Entity.getName());
        }
	}
}
