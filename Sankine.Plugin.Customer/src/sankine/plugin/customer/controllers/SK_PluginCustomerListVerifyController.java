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
import java.util.stream.Collectors;

import org.controlsfx.control.RangeSlider;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.Duration;
import sankine.marketing.entities.SK_CustomerEntity;
import sankine.marketing.entities.SK_CustomerGroupEntity;
import sankine.plugin.core.SK_Application;
import sankine.plugin.customer.business.SK_CustomerListBusinessLogic;
import sankine.plugin.customer.business.SK_VerifyEmailThread;
import sankine.plugin.customer.models.SK_CustomerListModel;
import sankine.plugin.customer.services.SK_CustomerGroupService;
import sankine.plugin.customer.services.SK_CustomerListService;
import sankine.plugin.messages.SK_GetAllRequest;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class SK_PluginCustomerListVerifyController implements Initializable {

	private SK_CustomerListService service;
	private SK_CustomerGroupService serviceGroup;
	private SK_CustomerListModel model;
	private SK_Application application;
	private ResourceBundle lang;
	private Service<ObservableList<SK_CustomerEntity>> task;

	@FXML
	private ComboBox<SK_CustomerGroupEntity> customerGroup;
	@FXML
	private TableView<SK_CustomerEntity> tblDataList;
	@FXML
	private TableColumn<SK_CustomerEntity, Boolean> tblClmSelect;
	@FXML
	private TableColumn<Object, Object> tblClmStt;
	@FXML
	private TableColumn<Object, Object> tblClmStatus;
	@FXML
	private TableColumn<Object, Object> tblClmEmail;
	@FXML
	private TableColumn<Object, Object> tblClmFirst;
	@FXML
	private TableColumn<Object, Object> tblClmLast;
	@FXML
	private TableColumn<Object, Object> tblClmDisplay;
	@FXML
	private TableColumn<Object, Object> tblClmGroup;

	private CheckBox cbSelectAllEmail;

	@FXML
	private RangeSlider maxSpeed;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		model = new SK_CustomerListModel();
		cbSelectAllEmail = new CheckBox();
		application = SK_Application.getInstance();
		lang = application.getLanguage();
		preLoading();
		tblDataList.setEditable(true);
		maxSpeed.setShowTickMarks(true);
		maxSpeed.setShowTickLabels(true);
	}

	public SK_PluginCustomerListVerifyController() {
		service = new SK_CustomerListService();
		serviceGroup = new SK_CustomerGroupService();
	}

	private void preLoading() {
		bindData();
		reloadData();
	}

	private void bindData() {
		tblClmSelect.setCellValueFactory(
				new Callback<CellDataFeatures<SK_CustomerEntity, Boolean>, ObservableValue<Boolean>>() {

					@Override
					public ObservableValue<Boolean> call(CellDataFeatures<SK_CustomerEntity, Boolean> param) {
						SK_CustomerEntity person = param.getValue();

						SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(person.isSelected());

						booleanProp.addListener(new ChangeListener<Boolean>() {

							@Override
							public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
									Boolean newValue) {
								person.setSelected(newValue);
							}
						});
						return booleanProp;
					}
				});

		tblClmSelect.setCellFactory(
				new Callback<TableColumn<SK_CustomerEntity, Boolean>, TableCell<SK_CustomerEntity, Boolean>>() {
					@Override
					public TableCell<SK_CustomerEntity, Boolean> call(TableColumn<SK_CustomerEntity, Boolean> p) {
						CheckBoxTableCell<SK_CustomerEntity, Boolean> cell = new CheckBoxTableCell<SK_CustomerEntity, Boolean>();
						cell.setAlignment(Pos.CENTER);
						return cell;
					}
				});
		tblClmStt.setCellValueFactory(new PropertyValueFactory<>("index"));
		tblClmStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
		tblClmEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tblClmFirst.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		tblClmLast.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		tblClmDisplay.setCellValueFactory(new PropertyValueFactory<>("displayName"));
		tblClmGroup.setCellValueFactory(new PropertyValueFactory<>("groupName"));
		tblClmSelect.setGraphic(cbSelectAllEmail);
		cbSelectAllEmail.setOnAction(e -> selectAllEmail(e));
	}

	public void selectAllEmail(ActionEvent e) {
		for (SK_CustomerEntity item : model.listEntity) {
			item.setSelected(((CheckBox) e.getSource()).isSelected());
		}
		tblDataList.refresh();
	}

	private void reloadData() {
		model.listEntity.clear();
		model.listGroups.clear();
		model.listEntity.addAll(service.GetAllEntityExtend(new SK_GetAllRequest<SK_CustomerEntity>()).listEntity);
		SK_CustomerGroupEntity item = new SK_CustomerGroupEntity();
		item.setGroupId(item.defaultGuid());
		item.setName(lang.getString("key146"));
		model.listGroups.add(item);
		model.listGroups
				.addAll(serviceGroup.GetAllEntityExtend(new SK_GetAllRequest<SK_CustomerGroupEntity>()).listEntity);
		tblDataList.setItems(model.listEntity);
		customerGroup.setItems(model.listGroups);
		if (model.listGroups.size() > 0) {
			customerGroup.getSelectionModel().select(model.listGroups.get(0));
		}
		customerGroup.valueProperty().addListener(new ChangeListener<SK_CustomerGroupEntity>() {
			@Override
			public void changed(ObservableValue<? extends SK_CustomerGroupEntity> observable,
					SK_CustomerGroupEntity oldValue, SK_CustomerGroupEntity newValue) {
				if (!newValue.isGuidEmpty()) {
					reloadGroup(newValue);
				}
			}
		});
	}

	private void reloadGroup(SK_CustomerGroupEntity group) {
		model.listEntity.clear();
		model.listEntity.addAll(service.GetAllEntityGroup(group).listEntity);
	}

	private boolean showAlert(AlertType type, String title, String header, String content) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
		return alert.getResult() == ButtonType.OK;
	}

	@FXML
	public void verifyAllEmail(ActionEvent event) {
		if (model.listEntity == null || model.listEntity.size() <= 0) {
			TrayNotification tn = new TrayNotification(lang.getString("key119"), lang.getString("key144"),
					NotificationType.ERROR);
			tn.showAndDismiss(Duration.seconds(5));
			return;
		}
		model.listSelected = SK_CustomerListBusinessLogic.containsSelect(model.listEntity);
		if (model.listSelected == null || model.listSelected.size() <= 0) {
			TrayNotification tn = new TrayNotification(lang.getString("key119"), lang.getString("key142"),
					NotificationType.ERROR);
			tn.showAndDismiss(Duration.seconds(5));
			return;
		}
		try {
			for (SK_CustomerEntity item : model.listEntity) {
				item.setSelected(false);
			}
			tblDataList.refresh();
			task = new Service<ObservableList<SK_CustomerEntity>>() {
				@Override
				protected Task<ObservableList<SK_CustomerEntity>> createTask() {
					return new Task<ObservableList<SK_CustomerEntity>>() {

						@Override
						protected ObservableList<SK_CustomerEntity> call() throws Exception {
							updateProgress(-1, -1);				
							int maxThread = (int)maxSpeed.getHighValue();
							model.listVerify = SK_CustomerListBusinessLogic.containsNotVerify(model.listSelected, maxThread);							
							do {
								if (isCancelled()) {
									break;
								}
								if (model.listVerify != null) {									
									SK_VerifyEmailThread[] listTask = new SK_VerifyEmailThread[model.listVerify.size()];
									for (int i = 0; i < listTask.length; i++) {
										if (isCancelled()) {
											break;
										}
										listTask[i] = new SK_VerifyEmailThread(tblDataList, model.listEntity, model.listVerify.get(i));								
										listTask[i].restart();
									}
								}
								updateValue(model.listEntity);
								tblDataList.refresh();
								Thread.sleep(5000);
								model.listVerify = SK_CustomerListBusinessLogic.containsNotVerify(model.listSelected, maxThread);
							} while (model.listVerify != null && model.listVerify.size() > 0);
							return model.listEntity;
						}
					};
				}
			};
			task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					stopProgress();
					TrayNotification tn = new TrayNotification(lang.getString("key119"), lang.getString("key154"),
							NotificationType.SUCCESS);
					tn.showAndDismiss(Duration.seconds(5));
				}
			});
			task.setOnFailed(new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					stopProgress();
					TrayNotification tn = new TrayNotification(lang.getString("key119"), lang.getString("key155"),
							NotificationType.ERROR);
					tn.showAndDismiss(Duration.seconds(5));
				}
			});

			application.progress.progressProperty().bind(task.progressProperty());
			tblDataList.itemsProperty().bind(task.valueProperty());
			task.restart();
		} catch (Exception e) {
			stopProgress();
			TrayNotification tn = new TrayNotification(lang.getString("key119"), e.getMessage(),
					NotificationType.ERROR);
			tn.showAndDismiss(Duration.seconds(5));
		}
	}

	@FXML
	public void stopProgress(ActionEvent event) {
		if (task != null && task.isRunning()) {
			task.cancel();
		}
		stopProgress();
		TrayNotification tn = new TrayNotification(lang.getString("key119"), lang.getString("key154"),
				NotificationType.INFORMATION);
		tn.showAndDismiss(Duration.seconds(5));
	}

	@FXML
	public void deleteSelected(ActionEvent event) {
		if (model.listEntity == null || model.listEntity.size() <= 0) {
			TrayNotification tn = new TrayNotification(lang.getString("key91"), lang.getString("key144"),
					NotificationType.ERROR);
			tn.showAndDismiss(Duration.seconds(5));
			return;
		}
		model.listSelected = SK_CustomerListBusinessLogic.containsSelect(model.listEntity);
		if (model.listSelected == null || model.listSelected.size() <= 0) {
			TrayNotification tn = new TrayNotification(lang.getString("key91"), lang.getString("key142"),
					NotificationType.ERROR);
			tn.showAndDismiss(Duration.seconds(5));
			return;
		}
		if (showAlert(AlertType.CONFIRMATION, lang.getString("key91"), lang.getString("key91"),
				lang.getString("key143"))) {
			service.removeAll(
					model.listEntity.stream().filter(o -> o.isSelected() == true).collect(Collectors.toList()));
			model.listEntity.removeIf(o -> o.isSelected() == true);
			tblDataList.refresh();
		}
	}

	private void stopProgress() {
		if (task != null && task.isRunning()) {
			task.cancel();
		}
		application.progress.progressProperty().unbind();
		application.progress.progressProperty().setValue(0);
	}
}
