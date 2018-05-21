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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.Duration;
import sankine.framework.common.SK_ComboBoxItem;
import sankine.framework.io.SK_ExcellExport;
import sankine.framework.security.SK_LicenseVerify;
import sankine.marketing.entities.SK_CustomerEntity;
import sankine.marketing.entities.SK_CustomerGroupEntity;
import sankine.plugin.core.SK_Application;
import sankine.plugin.customer.business.SK_CustomerListBusinessLogic;
import sankine.plugin.customer.models.SK_CustomerListModel;
import sankine.plugin.customer.services.SK_CustomerGroupService;
import sankine.plugin.customer.services.SK_CustomerListService;
import sankine.plugin.messages.SK_GetAllRequest;
import sankine.plugin.messages.SK_GetRequest;
import sankine.plugin.messages.SK_GetResponse;
import sankine.plugin.messages.SK_SaveResponse;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class SK_PluginCustomerListController implements Initializable {

	private SK_CustomerListService service;
	private SK_CustomerGroupService serviceGroup;
	private SK_CustomerListModel model;
	private SK_Application application;
	private ResourceBundle lang;
	private int limitRow = 100;
	private SK_LicenseVerify license;
	private Service<Boolean> taskExport;

	@FXML
	private TextField txtAddress;

	@FXML
	private TableView<SK_CustomerEntity> tblDataList;
	@FXML
	private TableColumn<SK_CustomerEntity, Boolean> tblClmSelect;
	@FXML
	private TableColumn<Object, Object> tblClmStt;
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
	@FXML
	private ComboBox<SK_CustomerGroupEntity> customerGroup;
	@FXML
	private ComboBox<SK_CustomerGroupEntity> customerMove;
	@FXML
	private ComboBox<SK_ComboBoxItem> pageRow;
	@FXML
	private Pagination pageIndex;
	@FXML
	private Button btnExport;

	private CheckBox cbSelectAllEmail;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		model = new SK_CustomerListModel();
		cbSelectAllEmail = new CheckBox();
		application = SK_Application.getInstance();
		lang = application.getLanguage();
		preLoading();
		tblDataList.setEditable(true);
		license = application.getLicense();
		if (license.getIsActive() != null && license.getIsActive()) {
			limitRow = 999999999;
		}
		
	}

	public SK_PluginCustomerListController() {
		service = new SK_CustomerListService();
		serviceGroup = new SK_CustomerGroupService();
	}

	private void preLoading() {
		bindData();		
		reloadAll();
		bindFilterTable();
		bindPageView();		
	}

	@FXML
	public void resetForm(ActionEvent event) {
		clearData();
	}

	private void clearData() {
		model.Entity = new SK_CustomerEntity();
		txtAddress.setText("");
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
		tblClmEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tblClmFirst.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		tblClmLast.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		tblClmDisplay.setCellValueFactory(new PropertyValueFactory<>("displayName"));
		tblClmGroup.setCellValueFactory(new PropertyValueFactory<>("groupName"));
		tblClmSelect.setGraphic(cbSelectAllEmail);
		cbSelectAllEmail.setOnAction(e -> selectAllEmail(e));

		model.listViewRow.clear();
		model.listViewRow.add(new SK_ComboBoxItem(15, lang.getString("key107")));
		model.listViewRow.add(new SK_ComboBoxItem(20, lang.getString("key108")));
		model.listViewRow.add(new SK_ComboBoxItem(30, lang.getString("key109")));
		model.listViewRow.add(new SK_ComboBoxItem(40, lang.getString("key110")));
		model.listViewRow.add(new SK_ComboBoxItem(50, lang.getString("key111")));
		model.listViewRow.add(new SK_ComboBoxItem(100, lang.getString("key112")));
		model.listViewRow.add(new SK_ComboBoxItem(1000, lang.getString("key113")));
		model.listViewRow.add(new SK_ComboBoxItem(10000, lang.getString("key114")));
		model.listViewRow.add(new SK_ComboBoxItem(100000, lang.getString("key115")));
	}

	public void selectAllEmail(ActionEvent e) {
		for (SK_CustomerEntity item : model.listEntity) {
			item.setSelected(((CheckBox) e.getSource()).isSelected());
		}
		tblDataList.refresh();
	}

	private void reloadData() {
		model.listAllEntity.clear();
		int pageSize = pageRow.getSelectionModel().getSelectedItem().getValue();
		model.listAllEntity.addAll(service.GetAllEntityExtend(new SK_GetAllRequest<SK_CustomerEntity>()).listEntity);
		pageIndex.setPageCount((model.listAllEntity.size() / pageSize) + 1);
		bindPageView();
	}

	private void reloadAll() {
		model.listAllEntity.clear();
		model.listGroups.clear();
		model.listAllEntity.addAll(service.GetAllEntityExtend(new SK_GetAllRequest<SK_CustomerEntity>()).listEntity);
		SK_CustomerGroupEntity item = new SK_CustomerGroupEntity();
		item.setGroupId(item.defaultGuid());
		item.setName(lang.getString("key146"));
		model.listGroups.add(item);
		model.listGroups
				.addAll(serviceGroup.GetAllEntityExtend(new SK_GetAllRequest<SK_CustomerGroupEntity>()).listEntity);
		customerGroup.setItems(model.listGroups);
		customerMove.setItems(model.listGroups);
		pageRow.setItems(model.listViewRow);
		tblDataList.setItems(model.listEntity);
		if (model.listGroups.size() > 0) {
			customerGroup.getSelectionModel().select(model.listGroups.get(0));
			customerMove.getSelectionModel().select(model.listGroups.get(0));
		}
		pageRow.valueProperty().addListener(new ChangeListener<SK_ComboBoxItem>() {
			@Override
			public void changed(ObservableValue<? extends SK_ComboBoxItem> observable, SK_ComboBoxItem oldValue,
					SK_ComboBoxItem newValue) {
				reloadPage(newValue.getValue());
			}
		});
		pageRow.getSelectionModel().select(0);
		customerGroup.valueProperty().addListener(new ChangeListener<SK_CustomerGroupEntity>() {
			@Override
			public void changed(ObservableValue<? extends SK_CustomerGroupEntity> observable,
					SK_CustomerGroupEntity oldValue, SK_CustomerGroupEntity newValue) {
				if (!newValue.isGuidEmpty()) {
					int pageSize = pageRow.getSelectionModel().getSelectedItem().getValue();
					reloadGroup(newValue, pageSize);
				}
			}
		});
	}

	private void reloadGroup(SK_CustomerGroupEntity group, int pageSize) {
		model.listAllEntity.clear();
		model.listAllEntity.addAll(service.GetAllEntityGroup(group).listEntity);
		pageIndex.setPageCount((model.listAllEntity.size() / pageSize) + 1);
		model.listEntity.clear();
		model.listEntity.addAll(SK_CustomerListBusinessLogic.paginationData(model.listAllEntity, 1, pageSize));
	}

	private void reloadPage(Integer pageSize) {		
		pageIndex.setPageCount((model.listAllEntity.size() / pageSize) + 1);
		model.listEntity.clear();
		model.listEntity.addAll(SK_CustomerListBusinessLogic.paginationData(model.listAllEntity, 1, pageSize));
		
	}

	private void bindPageView() {
		pageIndex.setPageFactory(new Callback<Integer, Node>() {

			@Override
			public Node call(Integer param) {
				model.listEntity.clear();
				model.listEntity.addAll(SK_CustomerListBusinessLogic.paginationData(model.listAllEntity, param + 1,
						pageRow.getSelectionModel().getSelectedItem().getValue()));
				VBox box = new VBox(param);
				return box;
			}
		});
	}

	@FXML
	private void selectedAction(MouseEvent event) {
		if (event.getClickCount() == 2) {
			model.Entity = tblDataList.getSelectionModel().getSelectedItem();
			SK_GetResponse<SK_CustomerEntity> response = service.GetEntity(new SK_GetRequest<SK_CustomerEntity>(model.Entity));
			if (response.Success) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						try {
							FXMLLoader fXMLLoader = new FXMLLoader();
							fXMLLoader.setResources(lang);
							fXMLLoader.load(
									getClass().getResource("/sankine/plugin/customer/views/FrmCustomerEditView.fxml")
											.openStream());
							SK_PluginCustomerEditController ctl = fXMLLoader.getController();
							ctl.setModel(response.Entity, response.Entity.getCustomerGroup());
							Tab views = fXMLLoader.getRoot();
							application.displayExtendView(views);
						} catch (IOException e) {
							e.printStackTrace();
							application.showPopup(e);
						}
					}
				});
			}
		}
	}

	@FXML
	public void addNew(ActionEvent event) {
		try {
			FXMLLoader fXMLLoader = new FXMLLoader();
			fXMLLoader.setResources(lang);
			fXMLLoader.load(
					getClass().getResource("/sankine/plugin/customer/views/FrmCustomerEditView.fxml").openStream());
			fXMLLoader.getController();
			Tab views = fXMLLoader.getRoot();
			application.displayExtendView(views);
		} catch (IOException e) {
			application.logMessages(e.getMessage(), "ERR");
		}
	}
	
	@FXML
	public void addQuick(ActionEvent event) {
		try {
			FXMLLoader fXMLLoader = new FXMLLoader();
			fXMLLoader.setResources(lang);
			fXMLLoader.load(
					getClass().getResource("/sankine/plugin/customer/views/FrmCustomerAddListView.fxml").openStream());
			fXMLLoader.getController();
			Tab views = fXMLLoader.getRoot();
			application.displayView(views);
		} catch (IOException e) {
			application.logMessages(e.getMessage(), "ERR");
		}
	}

	@FXML
	public void exportExcel(ActionEvent event) {
		FileChooser chooser = new FileChooser();
		// Set extension filter
		FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Excel Files(*.xls)", "*.xls");
		chooser.getExtensionFilters().add(filter);
		// Show save dialog
		File file = chooser.showSaveDialog(btnExport.getScene().getWindow());
		if (file != null) {

			taskExport = new Service<Boolean>() {
				@Override
				protected Task<Boolean> createTask() {
					return new Task<Boolean>() {

						@Override
						protected Boolean call() throws Exception {
							updateProgress(-1, -1);
							SK_ExcellExport excell = new SK_ExcellExport();
							String status = excell.Export(tblDataList, file, true, limitRow);
							if (!status.equalsIgnoreCase("Ok")) {
								Platform.runLater(new Runnable() {

									@Override
									public void run() {
										application.logMessages(status, "ERR");
									}
								});
							}
							return true;
						}
					};
				}
			};
			taskExport.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					stopProgress();
					TrayNotification tn = new TrayNotification(lang.getString("key92"), lang.getString("key93"),
							NotificationType.SUCCESS);
					tn.showAndDismiss(Duration.seconds(5));
				}
			});
			taskExport.setOnFailed(new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					stopProgress();
					TrayNotification tn = new TrayNotification(lang.getString("key92"), lang.getString("key94"),
							NotificationType.ERROR);
					tn.showAndDismiss(Duration.seconds(5));
				}
			});
			application.progress.progressProperty().bind(taskExport.progressProperty());
			taskExport.restart();
		}
	}

	@FXML
	public void moveGroup(ActionEvent event) {
		if (model.listAllEntity == null || model.listAllEntity.size() <= 0) {
			TrayNotification tn = new TrayNotification(lang.getString("key148"), lang.getString("key144"),
					NotificationType.ERROR);
			tn.showAndDismiss(Duration.seconds(5));
			return;
		}
		model.listSelected = SK_CustomerListBusinessLogic.containsSelect(model.listEntity);
		if(model.listSelected == null || model.listSelected.size() <= 0){
			model.listSelected = SK_CustomerListBusinessLogic.containsSelect(model.listAllEntity);
		}
		if (model.listSelected == null || model.listSelected.size() <= 0) {
			TrayNotification tn = new TrayNotification(lang.getString("key148"), lang.getString("key142"),
					NotificationType.ERROR);
			tn.showAndDismiss(Duration.seconds(5));
			return;
		}
		SK_CustomerGroupEntity toGroup = customerMove.getSelectionModel().getSelectedItem();
		if (toGroup.isGuidEmpty()) {
			TrayNotification tn = new TrayNotification(lang.getString("key148"), lang.getString("key149"),
					NotificationType.WARNING);
			tn.showAndDismiss(Duration.seconds(5));
			return;
		}
		if (showAlert(AlertType.CONFIRMATION, lang.getString("key148"), lang.getString("key148"),
				lang.getString("key150"))) {
			SK_SaveResponse<SK_CustomerEntity> result = service.MoveEntity(toGroup,
					model.listSelected.get(0).getIndex(),
					model.listSelected.get(model.listSelected.size() - 1).getIndex());
			if (result.Success) {
				TrayNotification tn = new TrayNotification(lang.getString("key148"), lang.getString("key151"),
						NotificationType.SUCCESS);
				tn.showAndDismiss(Duration.seconds(5));
				reloadData();
			} else {
				application.showPopup(lang.getString("key148"), lang.getString("key148"), lang.getString("key152"),
						result.Description);
			}
		}
	}

	@FXML
	public void removeSelected(ActionEvent event) {
		if (model.listAllEntity == null || model.listAllEntity.size() <= 0) {
			TrayNotification tn = new TrayNotification(lang.getString("key91"), lang.getString("key144"),
					NotificationType.ERROR);
			tn.showAndDismiss(Duration.seconds(5));
			return;
		}
		model.listSelected = SK_CustomerListBusinessLogic.containsSelect(model.listEntity);
		if(model.listSelected == null || model.listSelected.size() <= 0){
			model.listSelected = SK_CustomerListBusinessLogic.containsSelect(model.listAllEntity);
		}
		if (model.listSelected == null || model.listSelected.size() <= 0) {
			TrayNotification tn = new TrayNotification(lang.getString("key91"), lang.getString("key142"),
					NotificationType.ERROR);
			tn.showAndDismiss(Duration.seconds(5));
			return;
		}
		if (showAlert(AlertType.CONFIRMATION, lang.getString("key91"), lang.getString("key91"),
				lang.getString("key143"))) {
			service.removeAll(model.listSelected);
			reloadData();
			TrayNotification tn = new TrayNotification(lang.getString("key91"), lang.getString("key153"),
					NotificationType.SUCCESS);
			tn.showAndDismiss(Duration.seconds(5));
		}
	}

	@FXML
	public void deleteAll(ActionEvent event) {
		if (model.listAllEntity == null || model.listAllEntity.size() <= 0) {
			TrayNotification tn = new TrayNotification(lang.getString("key91"), lang.getString("key144"),
					NotificationType.ERROR);
			tn.showAndDismiss(Duration.seconds(5));
			return;
		}
		if (showAlert(AlertType.CONFIRMATION, lang.getString("key91"), lang.getString("key91"),
				lang.getString("key147"))) {
			service.removeAll();
			reloadData();
			TrayNotification tn = new TrayNotification(lang.getString("key91"), lang.getString("key153"),
					NotificationType.SUCCESS);
			tn.showAndDismiss(Duration.seconds(5));
		}
	}
	
	private void bindFilterTable(){
		                       
		// 2. Set the filter Predicate whenever the filter changes.
		txtAddress.textProperty().addListener((observable, oldValue, newValue) -> {
			
			// If filter text is empty, display all persons.
            if (newValue == null || newValue.isEmpty() || newValue.length() <= 0) { 
            	tblDataList.setItems(model.listEntity);
                return;
            }
            // 1. Wrap the ObservableList in a FilteredList (initially display all data).
            FilteredList<SK_CustomerEntity> filteredData = new FilteredList<>(model.listAllEntity, p -> true);
            
            filteredData.setPredicate(person -> {            	
                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();               
                if (person.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                }
                return false; // Does not match.
            });
            
            // 3. Wrap the FilteredList in a SortedList. 
            SortedList<SK_CustomerEntity> sortedData = new SortedList<>(filteredData);

            // 4. Bind the SortedList comparator to the TableView comparator.
            sortedData.comparatorProperty().bind(tblDataList.comparatorProperty());

            // 5. Add sorted (and filtered) data to the table.
            tblDataList.setItems(sortedData);
            
        });       
	}
	
	private void stopProgress() {
		application.progress.progressProperty().unbind();
		application.progress.progressProperty().setValue(0);
	}

	private boolean showAlert(AlertType type, String title, String header, String content) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
		return alert.getResult() == ButtonType.OK;
	}
}
