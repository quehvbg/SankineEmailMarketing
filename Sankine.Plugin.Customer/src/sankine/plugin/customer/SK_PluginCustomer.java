/*******************************************************************************
Copyright © 2017 Sankine Corporation Inc. All rights reserved.
Author: Hoang Van Que
Email: quehvbg@gmail.com
Phone : 0983.138.328
Description: 
Modifier :
Version : 1.0.0.1
********************************************************************************/

package sankine.plugin.customer;

import java.util.ResourceBundle;

import sankine.marketing.entities.SK_CustomerEntity;
import sankine.marketing.entities.SK_CustomerGroupEntity;
import sankine.plugin.core.SK_Application;
import sankine.plugin.core.SK_Menu;
import sankine.plugin.core.SK_MenuItem;
import sankine.plugin.interfaces.SK_IPlugin;

public class SK_PluginCustomer implements SK_IPlugin {

	private SK_Application mainApp;
	private SK_Menu pluginMenu;
	private ResourceBundle lang;
	
	public SK_PluginCustomer() {
		this.mainApp = SK_Application.getInstance();
		lang = mainApp.getLanguage();
		Initialize();
	}

	@Override
	public String pluginName() {
		return "CustomerPlugin";
	}

	@Override
	public String pluginId() {
		return "b41bf472-91dc-4263-bc1e-53b5656952de";
	}


	@Override
	public SK_Menu pluginMenu() {
		return pluginMenu;
	}

	@Override
	public void Initialize() {
		initMenu();
		initRepository();
		initService();
	}

	private void initMenu(){
		pluginMenu = new SK_Menu(lang.getString("key32"), "Customer Plugin","customer_plugin",true, "","CustomerPlugin");
		pluginMenu.addMenuItem(new SK_MenuItem(lang.getString("key33"), "Customer Group", "customer_group", true, "/sankine/plugin/customer/views/FrmCustomerGroupView.fxml","CustomerPlugin"));
		pluginMenu.addMenuItem(new SK_MenuItem(lang.getString("key34"), "Customer Contact", "customer_group", true, "/sankine/plugin/customer/views/FrmCustomerListView.fxml","CustomerPlugin"));
		pluginMenu.addMenuItem(new SK_MenuItem(lang.getString("key105"), "Add new", "customer_group", true, "/sankine/plugin/customer/views/FrmCustomerEditView.fxml","CustomerPlugin"));
		pluginMenu.addMenuItem(new SK_MenuItem(lang.getString("key35"), "Add Quick Customer", "customer_group", true, "/sankine/plugin/customer/views/FrmCustomerAddListView.fxml","CustomerPlugin"));
		pluginMenu.addMenuItem(new SK_MenuItem(lang.getString("key36"), "Verify all Email", "customer_group", true, "/sankine/plugin/customer/views/FrmCustomerListVerifyView.fxml","CustomerPlugin"));
		mainApp.addMenu(pluginMenu);		
	}
	
	private void initRepository() {
		mainApp.addEntity(SK_CustomerGroupEntity.class);
		mainApp.addEntity(SK_CustomerEntity.class);	
	}

	private void initService() {
		
	}
}
