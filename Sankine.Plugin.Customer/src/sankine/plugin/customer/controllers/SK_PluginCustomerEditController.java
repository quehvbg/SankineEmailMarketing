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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import sankine.marketing.entities.SK_CustomerEntity;
import sankine.marketing.entities.SK_CustomerGroupEntity;
import sankine.plugin.core.SK_Application;

import sankine.plugin.customer.models.SK_CustomerListModel;
import sankine.plugin.customer.services.SK_CustomerGroupService;
import sankine.plugin.customer.services.SK_CustomerListService;
import sankine.plugin.messages.SK_GetAllRequest;
import sankine.plugin.messages.SK_SaveRequest;
import sankine.plugin.messages.SK_SaveResponse;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class SK_PluginCustomerEditController implements Initializable {

	private SK_CustomerListService service;
	private SK_CustomerListModel model;
	private SK_CustomerGroupService serviceGroup;
	private SK_Application application;
	private ResourceBundle lang;

	@FXML
	private TextField txtFirstName;
	@FXML
	private TextField txtLastName;
	@FXML
	private TextField txtEmail;
	@FXML
	private TextField txtDisplay;
	@FXML
	private TextField txtPhone;
	@FXML
	private TextField txtProvince;
	@FXML
	private CheckBox cbIsActive;

	@FXML
	private ComboBox<SK_CustomerGroupEntity> customerGroup;

	public SK_PluginCustomerEditController() {
		service = new SK_CustomerListService();
		serviceGroup = new SK_CustomerGroupService();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		model = new SK_CustomerListModel();
		application = SK_Application.getInstance();
		lang = application.getLanguage();
		clearData();
		txtPhone.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					txtPhone.setText(newValue.replaceAll("[^\\d]", ""));
				} else {
					txtPhone.setText(newValue);
				}
			}
		});
	}

	private void clearData() {
		txtFirstName.setText("");
		txtLastName.setText("");
		txtDisplay.setText("");
		model.listGroups
				.addAll(serviceGroup.GetAllEntityExtend(new SK_GetAllRequest<SK_CustomerGroupEntity>()).listEntity);
		customerGroup.setItems(model.listGroups);
		if (model.listGroups.size() > 1) {
			customerGroup.getSelectionModel().select(model.listGroups.get(0));
		}
	}

	@FXML
	public void resetForm(ActionEvent event) {
		clearData();
	}

	@FXML
	public void saveChange(ActionEvent event) {
		SK_CustomerGroupEntity checkGroup = customerGroup.getSelectionModel().getSelectedItem();
		if (checkGroup == null) {
			TrayNotification tn = new TrayNotification(lang.getString("key88"), lang.getString("key140"),
					NotificationType.WARNING);
			tn.showAndDismiss(Duration.seconds(5));
			return;
		}
		if (txtFirstName.getText() == null || txtFirstName.getText().isEmpty() || txtLastName.getText() == null
				|| txtLastName.getText().isEmpty() || txtEmail.getText() == null || txtEmail.getText().isEmpty()
				|| txtDisplay.getText() == null || txtDisplay.getText().isEmpty()) {
			TrayNotification tn = new TrayNotification(lang.getString("key88"), lang.getString("key90"),
					NotificationType.WARNING);
			tn.showAndDismiss(Duration.seconds(5));
			return;
		}
		if(model.Entity == null){
			model.Entity = new SK_CustomerEntity();
			model.Entity.setCustomerId(model.Entity.generateUUID());
		}
		model.Entity.setFirstName(txtFirstName.getText());
		model.Entity.setLastName(txtLastName.getText());
		model.Entity.setDisplayName(txtDisplay.getText());
		model.Entity.setEmail(txtEmail.getText());
		model.Entity.setProvince(txtProvince.getText());
		model.Entity.setType(txtPhone.getText());
		model.Entity.setIsUnsubcrible(this.cbIsActive.isSelected());
		model.Entity.setCustomerGroup(checkGroup);
		SK_SaveRequest<SK_CustomerEntity> request = new SK_SaveRequest<SK_CustomerEntity>(model.Entity);
		SK_SaveResponse<SK_CustomerEntity> response = service.UpdateEntity(request);
		if (!response.Success) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					TrayNotification tn = new TrayNotification(lang.getString("key88"), response.Description,
							NotificationType.ERROR);
					tn.showAndDismiss(Duration.seconds(5));
				}
			});

		} else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					TrayNotification tn = new TrayNotification(lang.getString("key88"), lang.getString("key89"),
							NotificationType.SUCCESS);
					tn.showAndDismiss(Duration.seconds(5));
					try {
						FXMLLoader fXMLLoader = new FXMLLoader();
						fXMLLoader.setResources(lang);
						fXMLLoader.load(getClass()
								.getResource("/sankine/plugin/customer/views/FrmCustomerListView.fxml").openStream());
						fXMLLoader.getController();
						Tab views = fXMLLoader.getRoot();
						application.displayExtendView(views);
					} catch (IOException e) {
						application.showPopup(e);
					}
				}
			});
		}
	}

	public void setModel(SK_CustomerEntity modelTemp, SK_CustomerGroupEntity group) {
		model.Entity = modelTemp;
		txtFirstName.setText(modelTemp.getFirstName());
		txtLastName.setText(modelTemp.getLastName());
		txtDisplay.setText(modelTemp.getDisplayName());
		txtEmail.setText(modelTemp.getEmail());		
		txtProvince.setText(modelTemp.getProvince());
		cbIsActive.setSelected(modelTemp.getIsUnsubcrible());
		customerGroup.getSelectionModel().select(group);
		if(modelTemp.getType() != null && !modelTemp.getType().isEmpty()){
			txtPhone.setText(modelTemp.getType().trim());
		}
	}
}
