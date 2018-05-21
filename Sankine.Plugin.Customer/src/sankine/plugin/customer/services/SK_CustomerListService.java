/*******************************************************************************
Copyright © 2017 Sankine Corporation Inc. All rights reserved.
Author: Hoang Van Que
Email: quehvbg@gmail.com
Phone : 0983.138.328
Description: 
Modifier :
Version : 1.0.0.1
********************************************************************************/

package sankine.plugin.customer.services;

import sankine.marketing.entities.SK_CustomerEntity;
import sankine.marketing.entities.SK_CustomerGroupEntity;
import sankine.plugin.customer.business.SK_CustomerListBusinessLogic;
import sankine.plugin.data.SK_PluginService;
import sankine.plugin.data.SK_PluginServiceFactory;
import sankine.plugin.messages.SK_GetAllRequest;
import sankine.plugin.messages.SK_GetAllResponse;
import sankine.plugin.messages.SK_SaveRequest;
import sankine.plugin.messages.SK_SaveResponse;

public class SK_CustomerListService extends SK_PluginServiceFactory<SK_CustomerEntity> {
	public SK_CustomerListService() {
		setService(new SK_PluginService<SK_CustomerEntity>(SK_CustomerEntity.class));
	}
	
	@Override
	public SK_SaveResponse<SK_CustomerEntity> SaveEntity(SK_SaveRequest<SK_CustomerEntity> request) {
		return new SK_CustomerListBusinessLogic(service.provider).SaveEntity(request);
	}
	
	public SK_GetAllResponse<SK_CustomerEntity> GetAllEntityExtend(SK_GetAllRequest<SK_CustomerEntity> request) {	
		return new SK_CustomerListBusinessLogic(service.provider).GetAllEntityExtend(request);
	}
	
	public SK_GetAllResponse<SK_CustomerEntity> GetAllEntityExtend(int[] range) {	
		return new SK_CustomerListBusinessLogic(service.provider).GetAllEntityExtend(range);
	}
	
	public SK_GetAllResponse<SK_CustomerEntity> GetAllEntityGroup(SK_CustomerGroupEntity request) {	
		return new SK_CustomerListBusinessLogic(service.provider).GetAllEntityGroup(request);
	}
	
	public SK_SaveResponse<SK_CustomerEntity> MoveEntity(SK_CustomerGroupEntity group, Integer min, Integer max)
	{
		return new SK_CustomerListBusinessLogic(service.provider).MoveEntity(group,min,max);
	}
	
	public SK_SaveResponse<SK_CustomerEntity> UpdateEntity(SK_SaveRequest<SK_CustomerEntity> request) {
		return new SK_CustomerListBusinessLogic(service.provider).UpdateEntity(request);
	}
}
