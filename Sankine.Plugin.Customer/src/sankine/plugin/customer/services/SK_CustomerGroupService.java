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

import sankine.marketing.entities.SK_CustomerGroupEntity;
import sankine.plugin.customer.business.SK_CustomerGroupBusinessLogic;
import sankine.plugin.data.SK_PluginService;
import sankine.plugin.data.SK_PluginServiceFactory;
import sankine.plugin.messages.SK_GetAllRequest;
import sankine.plugin.messages.SK_GetAllResponse;
import sankine.plugin.messages.SK_SaveRequest;
import sankine.plugin.messages.SK_SaveResponse;

public class SK_CustomerGroupService extends SK_PluginServiceFactory<SK_CustomerGroupEntity> {
	public SK_CustomerGroupService() {
		setService(new SK_PluginService<SK_CustomerGroupEntity>(SK_CustomerGroupEntity.class));
	}
	
	@Override
	public SK_SaveResponse<SK_CustomerGroupEntity> SaveEntity(SK_SaveRequest<SK_CustomerGroupEntity> request) {
		return new SK_CustomerGroupBusinessLogic(service.provider).SaveEntity(request);
	}

	@Override
	public SK_GetAllResponse<SK_CustomerGroupEntity> GetAllEntity(SK_GetAllRequest<SK_CustomerGroupEntity> request) {		
		return new SK_CustomerGroupBusinessLogic(service.provider).GetAllEntity(request);
	}
	
	public SK_GetAllResponse<SK_CustomerGroupEntity> GetAllEntityExtend(SK_GetAllRequest<SK_CustomerGroupEntity> request) {	
		return new SK_CustomerGroupBusinessLogic(service.provider).GetAllEntityExtend(request);
	}
}
