/*******************************************************************************
Copyright © 2017 Sankine Corporation Inc. All rights reserved.
Author: Hoang Van Que
Email: quehvbg@gmail.com
Phone : 0983.138.328
Description: 
Modifier :
Version : 1.0.0.1
********************************************************************************/

package sankine.plugin.customer.business;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.TableView;
import sankine.framework.utils.SK_EmailVerifyUtils;
import sankine.marketing.entities.SK_CustomerEntity;

public class SK_VerifyEmailThread extends Service<ObservableList<SK_CustomerEntity>> {

	private SK_CustomerEntity entity;
	private ObservableList<SK_CustomerEntity> model;
	private TableView<SK_CustomerEntity> tblDataList;
	public SK_VerifyEmailThread(TableView<SK_CustomerEntity> dataList, ObservableList<SK_CustomerEntity> model, SK_CustomerEntity customer){
		tblDataList = dataList;
		entity = customer;
		this.model = model;
		
	}
	
	@Override
	protected Task<ObservableList<SK_CustomerEntity>> createTask() {
		return new Task<ObservableList<SK_CustomerEntity>>() {
			@Override
			protected ObservableList<SK_CustomerEntity> call() throws Exception {
//				System.out.println("Start verify : " + entity.getEmail());
				int index = model.indexOf(entity);
				String status = SK_EmailVerifyUtils.verify(entity.getEmail());
				model.get(index).setStatus(status);
				if (!status.equalsIgnoreCase("Ok")) {
					model.get(index).setSelected(true);
				}
//				System.out.println("Finish : " + entity.getEmail());
				updateValue(model);
				tblDataList.refresh();
				return model;
			}
		};
	}
}
