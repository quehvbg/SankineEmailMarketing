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

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import sankine.marketing.entities.SK_CustomerGroupEntity;
import sankine.plugin.base.SK_BaseTransaction;
import sankine.plugin.data.SK_DataProvider;
import sankine.plugin.messages.SK_GetAllRequest;
import sankine.plugin.messages.SK_GetAllResponse;
import sankine.plugin.messages.SK_SaveRequest;
import sankine.plugin.messages.SK_SaveResponse;
import sankine.repository.interfaces.SK_Specification;

public class SK_CustomerGroupBusinessLogic {
	private SK_DataProvider<SK_CustomerGroupEntity> provider;

	public SK_CustomerGroupBusinessLogic(SK_DataProvider<SK_CustomerGroupEntity> provider) {
		this.provider = provider;
	}
	
	public SK_SaveResponse<SK_CustomerGroupEntity> SaveEntity(SK_SaveRequest<SK_CustomerGroupEntity> request) {		
		SK_SaveResponse<SK_CustomerGroupEntity> response = new SK_SaveResponse<SK_CustomerGroupEntity>();
		if(request.Entity.isGuidEmpty()){
			request.Entity.setGroupId(request.Entity.generateUUID());
			Calendar now = Calendar.getInstance();
			request.Entity.setCreatedDate(now.getTime());
			request.Transaction = SK_BaseTransaction.Insert;
		}else{			
			request.Transaction = SK_BaseTransaction.Update;
		}				
		response = provider.SaveEntity(request);
		if (!response.Success) {
			response.Success = response.Success;
			response.MessageId = 404;
			return response;
		}
		response.Success = true;
		response.Description = "Save successfuly !!!";
		return response;		
	}

	public SK_GetAllResponse<SK_CustomerGroupEntity> GetAllEntity(SK_GetAllRequest<SK_CustomerGroupEntity> request) {
		SK_GetAllResponse<SK_CustomerGroupEntity> listData = new SK_GetAllResponse<SK_CustomerGroupEntity>();
		listData.listEntity = provider.GetAllEntity(request).listEntity;
//		Comparator<SK_CustomerGroupEntity> comparator = Comparator.comparing(SK_CustomerGroupEntity::getName);
//		listData.listEntity.stream().sorted(comparator.reversed()).collect(Collectors.toList());
		return listData;
	}
	
	public SK_GetAllResponse<SK_CustomerGroupEntity> GetAllEntityExtend(SK_GetAllRequest<SK_CustomerGroupEntity> request) {
		SK_GetAllResponse<SK_CustomerGroupEntity> listData = new SK_GetAllResponse<SK_CustomerGroupEntity>();
		List<Object[]> result = provider.executeProcedure(new SK_Specification("PR_GROUP_RECIPIENT"));
		for (Object[] objects : result) {
			SK_CustomerGroupEntity item = new SK_CustomerGroupEntity();			
			item.setGroupId(UUID.fromString(objects[0].toString().trim()));
			item.setName(objects[1].toString());
			item.setTotalEmail(new BigInteger(objects[2].toString()));
			item.setCreatedDate(Timestamp.valueOf(objects[3].toString()));
			listData.listEntity.add(item);			
		}			
		return listData;
	}
	
}
