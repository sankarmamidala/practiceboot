package com.verizon.ebiz.wholesale.connectivity.extranet.client.model.search;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.verizon.ebiz.wholesale.connectivity.common.ConnectivityConstants;
import com.verizon.ebiz.wholesale.connectivity.common.ConnectivityCriteria;
import com.verizon.ebiz.wholesale.connectivity.common.StatusVO;
import com.verizon.ebiz.wholesale.connectivity.extranet.client.beans.search.SearchConnectivityFormBean;
import com.verizon.ebiz.wholesale.connectivity.extranet.server.search.SearchConnectivityEngine;
import com.verizon.ebiz.wholesale.connectivity.extranet.server.search.SearchConnectivityFactory;
import com.verizon.ebiz.wholesale.connectivity.intranet.server.common.ConnectivityCache;
import com.verizon.ebiz.wholesale.cpd.logger.CPDLogger;

import model.BaseAction;

/**
 * @author Krishna Bollineni
 * @date Jun 29, 2005 4:28:04 PM
 * SearchConnectivityRequestAction.java
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SearchConnectivityRequestAction extends BaseAction
{
	// Process the Common Stuff for all the requests here ...
	String _sClassName = "SearchConnectivityRequestAction";
	/**
	 * This is the method called on by ActionServlet
	 * when a request is made for our action class.
	 * 
	 * 
	 */
	
	private SearchConnectivityFormBean searchConnectivityFormBean;
	
	
	/**
	 * @return the searchConnectivityFormBean
	 */
	public SearchConnectivityFormBean getSearchConnectivityFormBean() {
		return searchConnectivityFormBean;
	}
	/**
	 * @param searchConnectivityFormBean the searchConnectivityFormBean to set
	 */
	public void setSearchConnectivityFormBean(SearchConnectivityFormBean searchConnectivityFormBean) {
		this.searchConnectivityFormBean = searchConnectivityFormBean;
	}
	public String perform()
	{
		String sMethodName = "perform";
		String sReturnValue = "";
		String sAttr = "";
		String sUserType = "";
		String[] saStatus = null;
		ArrayList alSearchResults = null;
		HttpSession httpSession = request.getSession(false);
		ConnectivityCriteria criteriaObj = null;
		if (request.getParameter("actionAttribute") != null)
		{
			sAttr = (String) request.getParameter("actionAttribute");
		}
		CPDLogger.log.logd("", _sClassName, sMethodName, sAttr);
		if(request.getParameter("userType") != null)
		{
			sUserType = (String)request.getParameter("userType");
		}
		else
		{
			sUserType = ConnectivityConstants.EXTERNAL;
		}
		CPDLogger.log.logd("", _sClassName, sMethodName, "User Type - "
											+ sUserType);
		if (sAttr.equalsIgnoreCase("FROM_CRITERIA_PAGE"))
		{
			// Get the Search Conectivity Form Bean from the request ...
			/*SearchConnectivityFormBean searchConnectivityFormBean =
				(SearchConnectivityFormBean) form;*/
			// Populate the criteria object with the form bean values ...
			populateCriteriaObject(
				request,
				searchConnectivityFormBean);
			if (httpSession.getAttribute("CONNECTIVITY_CRITERIA_OBJECT")
				!= null)
			{
				criteriaObj =
					(ConnectivityCriteria) httpSession.getAttribute(
						"CONNECTIVITY_CRITERIA_OBJECT");
				saStatus = criteriaObj.getStatusId();
				this.buildStatusList(
					criteriaObj,
					sUserType);
				httpSession.setAttribute(
					"CONNECTIVITY_CRITERIA_OBJECT",
					criteriaObj);
			}
			else
			{
				print(
					sMethodName,
					"Connectivity Criteria Object not in the session. Irrecoverable error.");
				sReturnValue = "FAILURE";
				CPDLogger.log.logd("", _sClassName, sMethodName, "User Type - "
						+ sUserType);
				return sReturnValue;
			}
		} // Gopi Modification Start here on 10/26/2006 for Copy search results page..
		 else if(sAttr.equalsIgnoreCase("FROM_COPY_CRITERIA_PAGE"))
		 {
          // Get the Search Conectivity Form Bean from the request ...
			/*SearchConnectivityFormBean searchConnectivityFormBean =
				(SearchConnectivityFormBean) form;*/
			// Populate the criteria object with the form bean values ...
			populateCriteriaObject(
				request,
				searchConnectivityFormBean);
			if (httpSession.getAttribute("CONNECTIVITY_CRITERIA_OBJECT")
				!= null)
			{
				criteriaObj =
					(ConnectivityCriteria) httpSession.getAttribute(
						"CONNECTIVITY_CRITERIA_OBJECT");
				saStatus = criteriaObj.getStatusId();
				
				/*populate the statusId into criteria object only CTE Implemented and 
				Production Implemented. Because If the user didn't selected the status in the criteria page..
				the requests to be shown in the copy searchresults page should have the status as either CTE Implemented or 
				Production Implemented.*/
				
				this.buildStatusListForCopy(
					criteriaObj,
					sUserType);
				httpSession.setAttribute(
					"CONNECTIVITY_CRITERIA_OBJECT",
					criteriaObj);
			}
			else
			{
				print(
					sMethodName,
					"Connectivity Criteria Object not in the session. Irrecoverable error.");
				sReturnValue = "FAILURE";
				return sReturnValue;
			}
		 }
		 else if(sAttr.equalsIgnoreCase("RETURN_TO_COPY_SEARCH_RESULTS_PAGE"))
		 {
		 	if (httpSession.getAttribute("CONNECTIVITY_CRITERIA_OBJECT")
					!= null)
				{
					criteriaObj =
						(ConnectivityCriteria) httpSession.getAttribute(
							"CONNECTIVITY_CRITERIA_OBJECT");
					saStatus = criteriaObj.getStatusId();
					
					/*populate the statusId into criteria object only CTE Implemented and 
					Production Implemented. Because If the user didn't selected the status in the criteria page..
					the requests to be shown in the copy searchresults page should have the status as either CTE Implemented or 
					Production Implemented.*/
					
					this.buildStatusListForCopy(
						criteriaObj,
						sUserType);
					
					String sStartIndex = "0";
					if (request.getParameter("startIndex") != null)
					{
						sStartIndex = (String) request.getParameter("startIndex");
					}
					criteriaObj.setStartIndex(sStartIndex);
					httpSession.setAttribute(
						"CONNECTIVITY_CRITERIA_OBJECT",
						criteriaObj);
				}
				else
				{
					print(
						sMethodName,
						"Connectivity Criteria Object not in the session. Irrecoverable error.");
					sReturnValue = "FAILURE";
					return sReturnValue;
				}
		 }
		else
		{
			if (httpSession.getAttribute("CONNECTIVITY_CRITERIA_OBJECT")
				!= null)
			{
				criteriaObj =
					(ConnectivityCriteria) httpSession.getAttribute(
						"CONNECTIVITY_CRITERIA_OBJECT");
				saStatus = criteriaObj.getStatusId();
				this.buildStatusList(
					criteriaObj,
					sUserType);
				String sStartIndex = "0";
				if (request.getParameter("startIndex") != null)
				{
					sStartIndex = (String) request.getParameter("startIndex");
				}
				criteriaObj.setStartIndex(sStartIndex);
				httpSession.setAttribute(
					"CONNECTIVITY_CRITERIA_OBJECT",
					criteriaObj);
			}
			else
			{
				print(
					sMethodName,
					"Connectivity Criteria Object not in the session. Irrecoverable error.");
				sReturnValue = "FAILURE";
				return sReturnValue;
			}
		}
		// Get the Search Results for the given criteria.
		try
		{
			alSearchResults = getSearchResults(request, sUserType);
			if (alSearchResults != null)
			{
				request.setAttribute(
					"CONNECTIVITY_SEARCH_RESULTS",
					alSearchResults);
				sReturnValue = "CONNECTIVITY_SEARCH_RESULTS";
			}
			else
			{
				print(sMethodName, "ArrayList from SearchFactory is NULL");
				request.setAttribute(
					"ResultMsg",
					"No connectivity request exists for the selected criteria.");
				sReturnValue = "CONNECTIVITY_SEARCH_CRITERIA";
			}
			if (httpSession.getAttribute("CONNECTIVITY_CRITERIA_OBJECT")
				!= null)
			{
				criteriaObj =
					(ConnectivityCriteria) httpSession.getAttribute(
						"CONNECTIVITY_CRITERIA_OBJECT");
				// Set the user selected Status back to original.
				criteriaObj.setStatusId(saStatus);
				httpSession.setAttribute(
					"CONNECTIVITY_CRITERIA_OBJECT",
					criteriaObj);
			}
		}
		catch (Exception ce)
		{
			print(
				sMethodName,
				"ArrayList from SearchFactory throws exception....");
			print(sMethodName, "Exception", ce);
			sReturnValue = "FAILURE";
		}
		return sReturnValue;
	}
	/**
	 * @param httpServReq
	 * @param SearchConnectivityFormBean
	 */
	protected void populateCriteriaObject(
		HttpServletRequest httpServReq,
		SearchConnectivityFormBean connectivityFormBean)
	{
		String sMethodName = "populateCriteriaObject";
		ConnectivityCriteria criteriaObj = null;
		HttpSession httpSession = httpServReq.getSession(false);
		//See if the Criteria Object is already in session
		if (httpSession.getAttribute("CONNECTIVITY_CRITERIA_OBJECT") != null)
		{
			criteriaObj =
				(ConnectivityCriteria) httpSession.getAttribute(
					"CONNECTIVITY_CRITERIA_OBJECT");
			criteriaObj.initialize();
		}
		else
		{
			//Create a new one
			criteriaObj = new ConnectivityCriteria();
			criteriaObj.initialize();
		}
		print(sMethodName, "Got the Connectivity Criteria Object");

		// Get all the fields from the FormBean ...
		if ( httpSession.getAttribute("currentTask") != null ) {
			criteriaObj.setTaskId((String)httpSession.getAttribute("currentTask"));
			print(sMethodName, "Got the TaskId is "+(String)httpSession.getAttribute("currentTask"));
		} else
			print(sMethodName, "Task Id is NULL ");

		try
		{
			Class beanClass = connectivityFormBean.getClass();
			Class criteriaClass = criteriaObj.getClass();
			Field[] beanFields = beanClass.getFields();
			print(sMethodName, "Bean Field Length [" + beanFields.length + "]");
			for (int i = 0; i < beanFields.length; i++)
			{
				Field beanFld = beanFields[i];
				String sBeanFieldName = beanFld.getName();
				String sBeanFieldValue = null;
				String[] sBeanFieldValues = null;
				if (beanFld.get(connectivityFormBean) instanceof String)
					sBeanFieldValue =
						(String) beanFld.get(connectivityFormBean);
				else if (beanFld.get(connectivityFormBean) instanceof String[])
					sBeanFieldValues =
						(String[]) beanFld.get(connectivityFormBean);
				Field criteriaField = criteriaClass.getField(sBeanFieldName);
				print(sMethodName, "Bean Field== " + sBeanFieldName);
				print(
					sMethodName,
					"Criteria Field== " + criteriaField.getName());
				print(sMethodName, "Value == " + sBeanFieldValue);
				if (sBeanFieldValue != null)
				{
					print(
						sMethodName,
						"CriteriaField=="
							+ criteriaField.getName()
							+ "Setting value of"
							+ sBeanFieldValue);
					criteriaField.set(criteriaObj, sBeanFieldValue);
				}
				else if (sBeanFieldValues != null)
				{
					print(
						sMethodName,
						"CriteriaField=="
							+ criteriaField.getName()
							+ "Setting value of"
							+ sBeanFieldValues);
					criteriaField.set(criteriaObj, sBeanFieldValues);
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		//Load the Criteria object in session
		httpSession.setAttribute("CONNECTIVITY_CRITERIA_OBJECT", criteriaObj);
	}
	protected ArrayList getSearchResults(HttpServletRequest request,
						String sUserType) throws Exception
	{
		String sMethodName = "getSearchResults";
		ConnectivityCriteria criteriaObj = null;
		HttpSession httpSession = request.getSession(false);
		if (httpSession.getAttribute("CONNECTIVITY_CRITERIA_OBJECT") != null)
		{
			criteriaObj =
				(ConnectivityCriteria) httpSession.getAttribute(
					"CONNECTIVITY_CRITERIA_OBJECT");
		}
		SearchConnectivityEngine searchConnectivityEngine = null;
		ArrayList al = null;

		// Setting the User Id ...
		if (sUserType.equalsIgnoreCase(ConnectivityConstants.EXTERNAL))
		{
			criteriaObj.setUserId( (String) httpSession.getAttribute("userid") );
		}

		try
		{
			searchConnectivityEngine =
				SearchConnectivityFactory.createSearchConnectivityEngine();
			al =
				searchConnectivityEngine.searchConnectivityRequest(criteriaObj);
		}
		catch (Exception e)
		{
			CPDLogger.log.logc(
				"",
				_sClassName,
				sMethodName,
				"Exception in search",
				e);
			return null;
		}
		//ArrayList al = sessionObj.searchConnectivityRequest(criteriaObj);
		/*
		SearchRequest sr =
			SearchFactory.getSearchRequest(criteriaObj.getApplication());
		al = sr.getRecords(criteriaObj);
		*/
		return al;
	}
	protected void buildStatusList(
		ConnectivityCriteria criteriaObj,
		String sType)
	{
		String sMethodName = "buildStatusList";
		ConnectivityCache cc = ConnectivityCache.getInstance();
		HashMap hmStatus = cc.getApplicableStatusForQuery(sType);
		ArrayList alStatusQuery = null;
		if (sType.equalsIgnoreCase(ConnectivityConstants.EXTERNAL))
		{
			alStatusQuery = this.getExternalStatus(hmStatus, criteriaObj);
		}
		else if (sType.equalsIgnoreCase(ConnectivityConstants.INTERNAL))
		{
			alStatusQuery = this.getInternalStatus(hmStatus, criteriaObj);
		}
		else
		{
			alStatusQuery = this.getDeleteStatus(criteriaObj);
		}
		print(sMethodName, "Status Query size = " + alStatusQuery.size());
		String[] sStatusToQuery = new String[alStatusQuery.size()];
		for (int i = 0; i < alStatusQuery.size(); i++)
		{
			sStatusToQuery[i] = (String) alStatusQuery.get(i);
		}
		criteriaObj.setStatusId(sStatusToQuery);
	}
	
	/*
	 * which will identify the which requests to be shown in the search results
	 * page.Right now we are showing only either CTE Implemented or Production Implemented requests.
	 *  
	 */
	protected void buildStatusListForCopy(
			ConnectivityCriteria criteriaObj,
			String sType)
		{
			String sMethodName = "buildStatusListForCopy";
			ConnectivityCache cc = ConnectivityCache.getInstance();
			HashMap hmStatus = cc.getApplicableStatusForQuery(sType);
			HashMap hmAppStatus=null;
			ArrayList alStatusQuery = null;
            //HashMap hmStatus = getApplicableStatusForCopy(sType);
				
			hmAppStatus=(HashMap)hmStatus.get(criteriaObj.getApplication());
			alStatusQuery= getCopyStatus(hmAppStatus,criteriaObj);
			print(sMethodName, "Status Query size = " + alStatusQuery.size());
			String[] sStatusToQuery = new String[alStatusQuery.size()];
			for (int i = 0; i < alStatusQuery.size(); i++)
			{
				sStatusToQuery[i] = (String) alStatusQuery.get(i);
			}
			criteriaObj.setStatusId(sStatusToQuery);
		}
	
	/*
	 * which will identify the statuses applicable to the application and 
	 * populate those statuses to the criteria object if the statusId was not selected in the 
	 *   criteria page.If selected only populate the statusId corresponding to that status
	*/
	
	private ArrayList getCopyStatus(HashMap hmAppStatus,
						ConnectivityCriteria criteriaObj)
		{
			String sMethodName = "getCopyStatus";
			String sSelectedStatus = "";
			String[] saStatus = criteriaObj.getStatusId();
			HashMap hmCopyStatus=new HashMap();
			ArrayList alTempStatus = null;
			ArrayList alStatus=new ArrayList();
			
			print(sMethodName, "saSelectedStatus length[" + saStatus.length + "]");
			if (!(saStatus[0].equalsIgnoreCase("")))
			{
				sSelectedStatus = saStatus[0];
			}
			print(sMethodName, "sSelectedStatus[" + sSelectedStatus + "]");
			if (sSelectedStatus.equals(""))
			{
				//alStatus.add(String.valueOf(ConnectivityConstants.IMPLEMENTED_STATUS));
				//alStatus.add(String.valueOf(ConnectivityConstants.CTE_IMPLEMENTED_STATUS));
				//alStatus.add(String.valueOf(ConnectivityConstants.PRODUCTION_IMPLEMENTED_STATUS));
				
				alTempStatus=(ArrayList)hmAppStatus.get(String.valueOf(ConnectivityConstants.IMPLEMENTED_STATUS));
				
				for (int i = 0; i < alTempStatus.size(); i++)
				{
					print(
						sMethodName,
						"Actual Status from selectedStatus = "
							+ ((StatusVO) alTempStatus.get(i)).getID());
					alStatus.add(
						Integer.toString(((StatusVO) alTempStatus.get(i)).getID()));
				}
				
				/*alTempStatus=(ArrayList)hmAppStatus.get(String.valueOf(ConnectivityConstants.CTE_IMPLEMENTED_STATUS));
				
				for (int i = 0; i < alTempStatus.size(); i++)
				{
					print(
						sMethodName,
						"Actual Status from selectedStatus = "
							+ ((StatusVO) alTempStatus.get(i)).getID());
					alStatus.add(
						Integer.toString(((StatusVO) alTempStatus.get(i)).getID()));
				}	
				*/
				
			}
			else
			{
				ArrayList alStatusVO = (ArrayList) hmAppStatus.get(sSelectedStatus);
				for (int i = 0; i < alStatusVO.size(); i++)
				{
					print(
						sMethodName,
						"Actual Status from selectedStatus = "
							+ ((StatusVO) alStatusVO.get(i)).getID());
					alStatus.add(
						Integer.toString(((StatusVO) alStatusVO.get(i)).getID()));
				}
			}
			return alStatus;
		}
	
	private ArrayList getExternalStatus(
		HashMap hmStatus,
		ConnectivityCriteria criteriaObj)
	{
		String sMethodName = "getExternalStatusList";
		HashMap hmExtStatus =
			(HashMap) hmStatus.get(criteriaObj.getApplication());
		String sSelectedStatus = "";
		String[] saStatus = criteriaObj.getStatusId();
		ArrayList alStatusQuery = new ArrayList();
		print(sMethodName, "saSelectedStatus length[" + saStatus.length + "]");
		if (!(saStatus[0].equalsIgnoreCase("")))
		{
			sSelectedStatus = saStatus[0];
		}
		print(sMethodName, "sSelectedStatus[" + sSelectedStatus + "]");
		if (sSelectedStatus.equals(""))
		{
			for (Iterator it = ((Set) hmExtStatus.keySet()).iterator();
				it.hasNext();
				)
			{
				String sExtStatus = (String) it.next();
				System.out.println("   Ext Status = " + sExtStatus);
				ArrayList alStatusVO = (ArrayList) hmExtStatus.get(sExtStatus);
				for (int i = 0; i < alStatusVO.size(); i++)
				{
					print(
						sMethodName,
						"Actual Status = "
							+ ((StatusVO) alStatusVO.get(i)).getID());
					alStatusQuery.add(
						Integer.toString(
							((StatusVO) alStatusVO.get(i)).getID()));
				}
			}
		}
		else
		{
			ArrayList alStatusVO = (ArrayList) hmExtStatus.get(sSelectedStatus);
			for (int i = 0; i < alStatusVO.size(); i++)
			{
				print(
					sMethodName,
					"Actual Status from selectedStatus = "
						+ ((StatusVO) alStatusVO.get(i)).getID());
				alStatusQuery.add(
					Integer.toString(((StatusVO) alStatusVO.get(i)).getID()));
			}
		}
		return alStatusQuery;
	}
	private ArrayList getInternalStatus(
		HashMap hmStatus,
		ConnectivityCriteria criteriaObj)
	{
		String sMethodName = "getInternalStatus";
		String sSelectedStatus = "";
		String[] saStatus = criteriaObj.getStatusId();
		ArrayList alStatusQuery = new ArrayList();
		print(sMethodName, "saSelectedStatus length[" + saStatus.length + "]");
		if (!(saStatus[0].equalsIgnoreCase("")))
		{
			sSelectedStatus = saStatus[0];
		}
		print(sMethodName, "sSelectedStatus[" + sSelectedStatus + "]");
		if (sSelectedStatus.equals(""))
		{
			for (Iterator it = ((Set) hmStatus.keySet()).iterator();
				it.hasNext();
				)
			{
				String sIntStatus = (String) it.next();
				System.out.println("   Int Status = " + sIntStatus);
				ArrayList alStatusVO = (ArrayList) hmStatus.get(sIntStatus);
				for (int i = 0; i < alStatusVO.size(); i++)
				{
					print(
						sMethodName,
						"Actual Status = "
							+ ((StatusVO) alStatusVO.get(i)).getID());
					alStatusQuery.add(
						Integer.toString(
							((StatusVO) alStatusVO.get(i)).getID()));
				}
			}
		}
		else
		{
			ArrayList alStatusVO = (ArrayList) hmStatus.get(sSelectedStatus);
			for (int i = 0; i < alStatusVO.size(); i++)
			{
				print(
					sMethodName,
					"Actual Status from selectedStatus = "
						+ ((StatusVO) alStatusVO.get(i)).getID());
				alStatusQuery.add(
					Integer.toString(((StatusVO) alStatusVO.get(i)).getID()));
			}
		}
		return alStatusQuery;
	}
	private ArrayList getDeleteStatus(ConnectivityCriteria criteriaObj)
	{
		String sMethodName = "getDeleteStatus";
		String sSelectedStatus = "";
		String[] saStatus = criteriaObj.getStatusId();
		ArrayList alStatusQuery = new ArrayList();
		print(sMethodName, "saSelectedStatus length[" + saStatus.length + "]");
		if (!(saStatus[0].equalsIgnoreCase("")))
		{
			sSelectedStatus = saStatus[0];
		}
		print(sMethodName, "sSelectedStatus[" + sSelectedStatus + "]");
		if (sSelectedStatus.equals(""))
		{
			ConnectivityCache conCache = ConnectivityCache.getInstance();
			ArrayList alStatus = conCache.getStatusList();
			for (int i = 0; i < alStatus.size(); i++)
			{
				StatusVO statusVO = (StatusVO) alStatus.get(i);
				if (statusVO.getID() == ConnectivityConstants.DRAFT_STATUS
					|| statusVO.getID() == ConnectivityConstants.REJECTED_STATUS)
				{
					alStatusQuery.add(Integer.toString(statusVO.getID()));
				}
			}
		}
		else
		{
			alStatusQuery.add(sSelectedStatus);
		}
		return alStatusQuery;
	}
	private void print(String sMethodName, String sMsg)
	{
		CPDLogger.log.logd("", _sClassName, sMethodName, sMsg);
	}
	private void print(String sMethodName, String sMsg, Exception e)
	{
		CPDLogger.log.logc("", _sClassName, sMethodName, sMsg, e);
	}
}