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
import java.io.FileInputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import javafx.application.Platform;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.Duration;
import sankine.framework.utils.SK_RegexUtils;
import sankine.marketing.entities.SK_CustomerEntity;
import sankine.marketing.entities.SK_CustomerGroupEntity;
import sankine.plugin.core.SK_Application;
import sankine.plugin.customer.business.SK_CustomerListBusinessLogic;
import sankine.plugin.customer.models.SK_CustomerListModel;
import sankine.plugin.customer.services.SK_CustomerGroupService;
import sankine.plugin.customer.services.SK_CustomerListService;
import sankine.plugin.messages.SK_GetAllRequest;
import sankine.plugin.messages.SK_SaveRequest;
import sankine.plugin.messages.SK_SaveResponse;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class SK_PluginCustomerAddListController implements Initializable {

	private SK_CustomerListService service;
	private SK_CustomerListModel model;
	private SK_CustomerGroupService serviceGroup;
	private SK_Application application;
	private ResourceBundle lang;
	private Service<ObservableList<SK_CustomerEntity>> task;
	private Service<Boolean> taskSave;

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
	private TableColumn<Object, Object> tblClmDisplay;
	@FXML
	private Button btnImport;
	@FXML
	private TextArea txtAreaEmail;
	
	
	private CheckBox cbSelectAllEmail;

	public SK_PluginCustomerAddListController() {
		service = new SK_CustomerListService();
		serviceGroup = new SK_CustomerGroupService();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		application = SK_Application.getInstance();
		lang = application.getLanguage();
		model = new SK_CustomerListModel();
		cbSelectAllEmail = new CheckBox();
		preLoading();
		tblDataList.setEditable(true);		
	}

	private void preLoading() {
		bindData();
		reloadData();
	}

	private void reloadData() {
		model.listEntity.clear();
		model.listGroups
				.addAll(serviceGroup.GetAllEntityExtend(new SK_GetAllRequest<SK_CustomerGroupEntity>()).listEntity);
		customerGroup.setItems(model.listGroups);
		if (model.listGroups.size() > 1) {
			customerGroup.getSelectionModel().select(model.listGroups.get(0));
		}
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
		tblClmDisplay.setCellValueFactory(new PropertyValueFactory<>("displayName"));
		tblClmSelect.setGraphic(cbSelectAllEmail);
		cbSelectAllEmail.setOnAction(e -> selectAllEmail(e));
	}

	public void selectAllEmail(ActionEvent e) {

		for (SK_CustomerEntity item : model.listEntity) {
			item.setSelected(((CheckBox) e.getSource()).isSelected());
		}
		tblDataList.refresh();
	}

	@FXML
	public void saveChange(ActionEvent event) {
		SK_CustomerGroupEntity checkGroup = customerGroup.getSelectionModel().getSelectedItem();
		if (checkGroup == null) {
			TrayNotification tn = new TrayNotification(lang.getString("key64"), lang.getString("key140"),
					NotificationType.WARNING);
			tn.showAndDismiss(Duration.seconds(5));
			return;
		}
		taskSave = new Service<Boolean>() {
			@Override
			protected Task<Boolean> createTask() {
				return new Task<Boolean>() {

					@Override
					protected Boolean call() throws Exception {
						updateProgress(-1, -1);
						for (int i = 0; i < model.listEntity.size(); i++) {
							model.listEntity.get(i).setCustomerGroup(checkGroup);
						}
						SK_SaveRequest<SK_CustomerEntity> request = new SK_SaveRequest<SK_CustomerEntity>(
								model.listEntity);
						SK_SaveResponse<SK_CustomerEntity> response = service.SaveEntity(request);
						if (!response.Success) {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									TrayNotification tn = new TrayNotification(lang.getString("key88"),
											response.Description, NotificationType.ERROR);
									tn.showAndDismiss(Duration.seconds(5));
								}
							});

						} else {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									reloadData();
									TrayNotification tn = new TrayNotification(lang.getString("key88"),
											lang.getString("key89"), NotificationType.SUCCESS);
									tn.showAndDismiss(Duration.seconds(5));
								}
							});
						}
						return true;
					}
				};
			}
		};
		taskSave.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				stopProgress();
			}
		});
		taskSave.setOnFailed(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				stopProgress();
				TrayNotification tn = new TrayNotification(lang.getString("key88"), lang.getString("key145"),
						NotificationType.SUCCESS);
				tn.showAndDismiss(Duration.seconds(5));
			}
		});
		application.progress.progressProperty().bind(taskSave.progressProperty());
		taskSave.restart();
	}

	@FXML
	public void removeSelected(ActionEvent event) {
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
			model.listEntity.removeIf(o -> o.isSelected() == true);
			tblDataList.refresh();
		}
	}

	@FXML
	public void importExcell(ActionEvent event) {
		FileChooser chooser = new FileChooser();
		// Set extension filter
		FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Excel Files(*.xls)", "*.xls");
		chooser.getExtensionFilters().add(filter);
		// Show save dialog
		File file = chooser.showOpenDialog(btnImport.getScene().getWindow());
		if (file != null) {
			importFullExcell(file, SK_CustomerEntity.class);
		}
	}

	@FXML
	public void importGmail(ActionEvent event) {
		task = new Service<ObservableList<SK_CustomerEntity>>() {
			@Override
			protected Task<ObservableList<SK_CustomerEntity>> createTask() {
				return new Task<ObservableList<SK_CustomerEntity>>() {

					@Override
					protected ObservableList<SK_CustomerEntity> call() throws Exception {
						updateProgress(-1, -1);
						model.listEntity
								.addAll(SK_CustomerListBusinessLogic.importContact(model.listEntity).listEntity);
						updateValue(model.listEntity);
						tblDataList.refresh();
						return model.listEntity;
					}
				};
			}
		};
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				stopProgress();
				TrayNotification tn = new TrayNotification(lang.getString("key118"), lang.getString("key158"),
						NotificationType.SUCCESS);
				tn.showAndDismiss(Duration.seconds(5));
			}
		});
		task.setOnFailed(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				stopProgress();
				TrayNotification tn = new TrayNotification(lang.getString("key118"), lang.getString("key157"),
						NotificationType.ERROR);
				tn.showAndDismiss(Duration.seconds(5));
			}
		});

		application.progress.progressProperty().bind(task.progressProperty());
		tblDataList.itemsProperty().bind(task.valueProperty());
		task.restart();
	}

	private boolean showAlert(AlertType type, String title, String header, String content) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
		return alert.getResult() == ButtonType.OK;
	}

	private void importFullExcell(File path, Object type) {
		try {
			// Đọc một file XSL.
			Service<Void> taskImport = new Service<Void>() {
				@Override
				protected Task<Void> createTask() {
					return new Task<Void>() {

						@Override
						protected Void call() throws Exception {

							updateProgress(-1, -1);
							FileInputStream inputStream = new FileInputStream(path);

							// Đối tượng workbook cho file XSL.
							HSSFWorkbook workbook = new HSSFWorkbook(inputStream);

							// Lấy ra sheet đầu tiên từ workbook
							int sheetCount = workbook.getNumberOfSheets();
							Integer index = model.listEntity.size() + 1;
							Matcher matcherEm = null;
							for (int i = 0; i < sheetCount; i++) {
								HSSFSheet sheet = workbook.getSheetAt(i);
								// Lấy ra Iterator cho tất cả các dòng của sheet
								// hiện tại.
								Iterator<Row> rowIterator = sheet.iterator();								
								if (type == SK_CustomerEntity.class) {
									while (rowIterator.hasNext()) {
										Row row = rowIterator.next();
										if (row.getRowNum() == 0) {
											continue;
										}
										if (row.getCell(8) == null) {
											continue;
										}
										SK_CustomerEntity info = new SK_CustomerEntity();
										info.setEmail(row.getCell(8).getStringCellValue());
										if (info.getEmail() == null || info.getEmail().isEmpty()) {
											continue;
										}
										matcherEm = patternEm.matcher(info.getEmail().toLowerCase());
										if (!matcherEm.find()) {
											continue;
										}else{
											info.setEmail(matcherEm.group());
										}										
										if (row.getCell(2) != null) {
											info.setFirstName(row.getCell(2).getStringCellValue());
										}
										if (row.getCell(3) != null) {
											info.setLastName(row.getCell(3).getStringCellValue());
										}
										if (row.getCell(4) != null) {
											info.setDisplayName(row.getCell(4).getStringCellValue());
										}
										if (row.getCell(10) != null) {
											info.setProvince(row.getCell(10).getStringCellValue());
										}
										info.setIndex(index);
										info.setIsUnsubcrible(false);
										info.setSelected(true);
										model.listEntity.add(info);
										index++;
									}
									tblDataList.setItems(model.listEntity);
									Platform.runLater(() -> {
										tblDataList.scrollTo(model.listEntity.size() - 1);
									});
								}
							}
							inputStream.close();
							workbook.close();
							return null;
						}
					};
				}
			};

			taskImport.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					stopProgress();
					TrayNotification tn = new TrayNotification(lang.getString("key64"), lang.getString("key138"),
							NotificationType.SUCCESS);
					tn.showAndDismiss(Duration.seconds(5));
				}
			});
			taskImport.setOnFailed(new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					stopProgress();
					TrayNotification tn = new TrayNotification(lang.getString("key64"), lang.getString("key139"),
							NotificationType.ERROR);
					tn.showAndDismiss(Duration.seconds(5));
				}
			});
			application.progress.progressProperty().bind(taskImport.progressProperty());
			taskImport.restart();
		} catch (Exception e) {
			stopProgress();
			TrayNotification tn = new TrayNotification(lang.getString("key64"), e.getMessage(), NotificationType.ERROR);
			tn.showAndDismiss(Duration.seconds(5));
		}
	}

	private Pattern patternEm = Pattern.compile(SK_RegexUtils.REGEX_MAIL);
	
	@FXML
	public void importTextEmail(ActionEvent event) {
		task = new Service<ObservableList<SK_CustomerEntity>>() {
			@Override
			protected Task<ObservableList<SK_CustomerEntity>> createTask() {
				return new Task<ObservableList<SK_CustomerEntity>>() {

					@Override
					protected ObservableList<SK_CustomerEntity> call() throws Exception {
						updateProgress(-1, -1);
						String importText = txtAreaEmail.getText();						
						if(importText != null && importText.length() > 0){
							String[] arrLine = importText.split("\\n");
							Integer index = model.listEntity.size() + 1;
							for (int i = 0; i < arrLine.length; i++) {
								Matcher matcherEm = patternEm.matcher(arrLine[i].toLowerCase());
								if (matcherEm.find()) {
									
									SK_CustomerEntity info = new SK_CustomerEntity();
									info.setEmail(matcherEm.group());									
									info.setIndex(index);
									info.setIsUnsubcrible(false);
									info.setSelected(true);
									model.listEntity.add(info);
									updateValue(model.listEntity);
									tblDataList.refresh();
									index++;
								}
							}
						}												
						return model.listEntity;
					}
				};
			}
		};
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				stopProgress();
				TrayNotification tn = new TrayNotification(lang.getString("key118"), lang.getString("key158"),
						NotificationType.SUCCESS);
				tn.showAndDismiss(Duration.seconds(5));
			}
		});
		task.setOnFailed(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				stopProgress();
				TrayNotification tn = new TrayNotification(lang.getString("key118"), lang.getString("key157"),
						NotificationType.ERROR);
				tn.showAndDismiss(Duration.seconds(5));
			}
		});

		application.progress.progressProperty().bind(task.progressProperty());
		tblDataList.itemsProperty().bind(task.valueProperty());
		task.restart();
	}
	
	private void stopProgress() {
		if (task != null && task.isRunning()) {
			task.cancel();
		}
		if (taskSave != null && taskSave.isRunning()) {
			taskSave.cancel();
		}
		application.progress.progressProperty().unbind();
		application.progress.progressProperty().setValue(0);
	}
}
