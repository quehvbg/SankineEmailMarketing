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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import sankine.framework.google.SK_GmailConnection;
import sankine.marketing.entities.SK_CustomerEntity;
import sankine.marketing.entities.SK_CustomerGroupEntity;
import sankine.plugin.base.SK_BaseTransaction;
import sankine.plugin.data.SK_DataProvider;
import sankine.plugin.messages.SK_GetAllRequest;
import sankine.plugin.messages.SK_GetAllResponse;
import sankine.plugin.messages.SK_SaveAllRequest;
import sankine.plugin.messages.SK_SaveRequest;
import sankine.plugin.messages.SK_SaveResponse;
import sankine.repository.interfaces.SK_Parameter;
import sankine.repository.interfaces.SK_Specification;

import javax.persistence.ParameterMode;
import com.google.api.client.auth.oauth2.Credential;
import com.google.gdata.client.Query;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.extensions.Email;
import com.google.gdata.data.extensions.Name;

public class SK_CustomerListBusinessLogic {
	private SK_DataProvider<SK_CustomerEntity> provider;

	public SK_CustomerListBusinessLogic(SK_DataProvider<SK_CustomerEntity> provider) {
		this.provider = provider;
	}

	public SK_SaveResponse<SK_CustomerEntity> SaveEntity(SK_SaveRequest<SK_CustomerEntity> request) {
		SK_SaveResponse<SK_CustomerEntity> response = new SK_SaveResponse<SK_CustomerEntity>();
		if ((request.ListEntity == null || request.ListEntity.size() <= 0) && request.Entity != null) {
			try {				
				SK_GetAllResponse<SK_CustomerEntity> checkExist = new SK_GetAllResponse<SK_CustomerEntity>();
				checkExist.listEntity.addAll(provider.query(new SK_Specification(
						"SELECT p FROM SK_CustomerEntity p where p.email = '" + request.Entity.getEmail() + "'")));
				if (checkExist.listEntity.size() > 0) {										
					request.Entity.setCustomerId(checkExist.listEntity.get(0).getCustomerId());
					request.Transaction = SK_BaseTransaction.Update;
					System.out.println("Ok");
				} else {
					request.Entity.setCustomerId(request.Entity.generateUUID());
					request.Transaction = SK_BaseTransaction.Insert;
				}
				response = provider.SaveEntity(request);
				if (!response.Success) {
					response.Success = response.Success;
					response.Description = response.Description;
					response.MessageId = 404;
					return response;
				}
				response.Success = true;
				response.Description = "Save successfuly !!!";
			} catch (Exception e) {
				e.printStackTrace();
				response.Success = false;
				response.Description = e.getMessage();
			}
			return response;

		} else {
			try {
				SK_GetAllResponse<SK_CustomerEntity> tempData = provider
						.GetAllEntity(new SK_GetAllRequest<SK_CustomerEntity>());
				SK_SaveAllRequest<SK_CustomerEntity> requestIns = new SK_SaveAllRequest<SK_CustomerEntity>();
				SK_SaveAllRequest<SK_CustomerEntity> requestUp = new SK_SaveAllRequest<SK_CustomerEntity>();
				requestIns.Transaction = SK_BaseTransaction.Insert;
				requestUp.Transaction = SK_BaseTransaction.Update;
				for (SK_CustomerEntity item : request.ListEntity) {
					SK_CustomerEntity checkExist = findCustomer(tempData.listEntity, item.getEmail());
					if (checkExist != null) {
						checkExist.setCustomerGroup(item.getCustomerGroup());
						checkExist.setFirstName(item.getFirstName());
						checkExist.setLastName(item.getLastName());
						request.Entity = checkExist;
						request.Transaction = SK_BaseTransaction.Update;
						requestUp.ListEntties.add(request.Entity);
					} else {
						request.Entity = item;
						request.Entity.setCustomerId(request.Entity.generateUUID());
						request.Transaction = SK_BaseTransaction.Insert;
						requestIns.ListEntties.add(request.Entity);
					}
				}
				provider.SaveAllEntity(requestUp);
				provider.SaveAllEntity(requestIns);
				response.Success = true;
				response.Description = "Save successfuly !!!";
			} catch (Exception e) {
				e.printStackTrace();
				response.Success = false;
				response.Description = e.getMessage();
			}
			return response;
		}
	}

	public static List<SK_CustomerEntity> containsSelect(final List<SK_CustomerEntity> list) {
		return list.stream().filter(o -> o.isSelected() == true).collect(Collectors.toList());
	}

	public static List<SK_CustomerEntity> containsNotVerify(final List<SK_CustomerEntity> list, int maxThread) {
		if (list == null || list.size() == 0) {
			return list;
		}
		List<SK_CustomerEntity> listVerify = list.stream().filter(o -> o.getStatus() == null).collect(Collectors.toList());
		if(listVerify != null && listVerify.size() > maxThread){			
			return listVerify.stream().limit(maxThread).collect(Collectors.toList());
		}		
		return listVerify; 
	}
	
	public static List<SK_CustomerEntity> containsNotSelect(final List<SK_CustomerEntity> list) {
		return list.stream().filter(o -> o.isSelected() == false).collect(Collectors.toList());
	}

	public static List<SK_CustomerEntity> paginationData(final List<SK_CustomerEntity> list, int index, int pageSize) {
		if (list == null || list.size() == 0) {
			return list;
		}
		return list.stream()
				.filter(o -> o.getIndex() >= ((index - 1) * pageSize) + 1 && o.getIndex() <= (index * pageSize))
				.collect(Collectors.toList());
	}

	public static boolean containsCustomer(final List<SK_CustomerEntity> list, final String email) {
		try {
			return list.stream().filter(o -> o.getEmail().equalsIgnoreCase(email)).findFirst().isPresent();
		} catch (Exception e) {
			return false;
		}
	}

	public static SK_CustomerEntity findCustomer(final List<SK_CustomerEntity> list, final String email) {
		try {
			return list.stream().filter(o -> o.getEmail().equalsIgnoreCase(email)).findFirst().get();
		} catch (Exception e) {
			return null;
		}
	}

	public SK_GetAllResponse<SK_CustomerEntity> GetAllEntityExtend(SK_GetAllRequest<SK_CustomerEntity> request) {
		SK_GetAllResponse<SK_CustomerEntity> response = new SK_GetAllResponse<SK_CustomerEntity>();
		List<Object[]> result = provider.executeProcedure(new SK_Specification("PR_REPORT_RECIPIENT"));
		for (Object[] objects : result) {
			SK_CustomerEntity item = new SK_CustomerEntity();
			item.setIndex(Integer.valueOf(objects[0].toString()));
			item.setEmail(objects[1].toString());
			if (objects[2] != null) {
				item.setFirstName(objects[2].toString());
			}
			if (objects[3] != null) {
				item.setLastName(objects[3].toString());
			}
			if (objects[4] != null) {
				item.setDisplayName(objects[4].toString());
			}
			item.setGroupName(objects[5].toString());
			item.setCustomerId(UUID.fromString(objects[6].toString().trim()));
			response.listEntity.add(item);
		}
		return response;
	}

	public SK_GetAllResponse<SK_CustomerEntity> GetAllEntityExtend(int[] range) {
		SK_GetAllResponse<SK_CustomerEntity> response = new SK_GetAllResponse<SK_CustomerEntity>();
		response.listEntity = provider.GetAllRangeEntity(range).listEntity;
		return response;
	}

	public SK_GetAllResponse<SK_CustomerEntity> GetAllEntityGroup(SK_CustomerGroupEntity request) {
		SK_GetAllResponse<SK_CustomerEntity> response = new SK_GetAllResponse<SK_CustomerEntity>();
		SK_Parameter para = new SK_Parameter("GROUP_ID", request.getGroupId().toString().toUpperCase(), String.class,
				ParameterMode.IN);
		List<Object[]> result = provider.executeProcedure(new SK_Specification("PR_REPORT_RECIPIENT_GROUP", para));
		for (Object[] objects : result) {
			SK_CustomerEntity item = new SK_CustomerEntity();
			item.setIndex(Integer.valueOf(objects[0].toString()));
			item.setEmail(objects[1].toString());
			if (objects[2] != null) {
				item.setFirstName(objects[2].toString());
			}
			if (objects[3] != null) {
				item.setLastName(objects[3].toString());
			}
			if (objects[4] != null) {
				item.setDisplayName(objects[4].toString());
			}
			item.setGroupName(objects[5].toString());
			item.setCustomerId(UUID.fromString(objects[6].toString().trim()));
			response.listEntity.add(item);
		}
		return response;
	}

	public SK_SaveResponse<SK_CustomerEntity> MoveEntity(SK_CustomerGroupEntity group, Integer min, Integer max) {
		SK_SaveResponse<SK_CustomerEntity> response = new SK_SaveResponse<SK_CustomerEntity>();
		try {
			if (group != null) {
				List<SK_Parameter> listPara = new ArrayList<SK_Parameter>();
				listPara.add(new SK_Parameter("V_MIN", min, Integer.class, ParameterMode.IN));
				listPara.add(new SK_Parameter("V_MAX", max, Integer.class, ParameterMode.IN));
				listPara.add(new SK_Parameter("GROUP_ID", group.getGroupId().toString().toUpperCase(), String.class,
						ParameterMode.IN));
				String result = provider.runProcedure(new SK_Specification("PR_MOVE_RECIPIENT", listPara));
				if (!result.equalsIgnoreCase("Ok")) {
					response.Success = false;
					response.Description = result;
					return response;
				}
				response.Success = true;
				response.Description = "Save successfuly !!!";
			}
		} catch (Exception e) {
			response.Success = false;
			response.Description = e.getMessage();
		}
		return response;
	}

	public static SK_GetAllResponse<SK_CustomerEntity> importContact(List<SK_CustomerEntity> listData) {
		SK_GetAllResponse<SK_CustomerEntity> response = new SK_GetAllResponse<SK_CustomerEntity>();
		try {
			Credential auth = SK_GmailConnection.authorize();
			if (auth == null) {
				return response;
			}
			ContactsService myService = new ContactsService("Sankine Email Marketing");
			myService.setOAuth2Credentials(auth);
			URL feedUrl = new URL("https://www.google.com/m8/feeds/contacts/default/full");

			Query myQuery = new Query(feedUrl);
			myQuery.setMaxResults(10000);
			ContactFeed resultFeed = myService.query(myQuery, ContactFeed.class);
			int index = listData.size() + 1;
			for (ContactEntry entry : resultFeed.getEntries()) {
				for (Email email : entry.getEmailAddresses()) {
					if (containsCustomer(listData, email.getAddress())
							|| containsCustomer(response.listEntity, email.getAddress())) {
						continue;
					}
					SK_CustomerEntity customer = new SK_CustomerEntity();
					customer.setIndex(index);
					customer.setStatus("Gmail Contact");
					if (entry.hasName()) {
						Name name = entry.getName();
						if (name.hasFullName()) {
							customer.setDisplayName(name.getFullName().getValue());
						}
					}
					customer.setEmail(email.getAddress());
					response.listEntity.add(customer);
					index++;
				}
			}
		} catch (Exception e) {
			response.Description = e.getMessage().toString();
		}
		return response;
	}
	
	public SK_SaveResponse<SK_CustomerEntity> UpdateEntity(SK_SaveRequest<SK_CustomerEntity> request) {
		SK_SaveResponse<SK_CustomerEntity> response = new SK_SaveResponse<SK_CustomerEntity>();
		try {
			request.Transaction = SK_BaseTransaction.Update;
			response = provider.SaveEntity(request);
			if (!response.Success) {
				response.Success = response.Success;
				response.Description = response.Description;
				response.MessageId = 404;
				return response;
			}
			response.Success = true;
			response.Description = "Save successfuly !!!";
		} catch (Exception e) {
			e.printStackTrace();
			response.Success = false;
			response.Description = e.getMessage();
		}
		return response; 
	}
}
