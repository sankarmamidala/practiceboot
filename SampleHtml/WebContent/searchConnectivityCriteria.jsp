<%@ page language="java" %>

<!-- IMPORT JAVA FILES HERE -->
<%@ page import =  "javax.servlet.*,
java.util.*,java.sql.*,model.*,
com.verizon.ebiz.wholesale.cpd.common.*,
com.verizon.ebiz.wholesale.clec.common.*,
com.verizon.ebiz.wholesale.database.*,
com.verizon.ebiz.wholesale.clec.util.*,
com.verizon.ebiz.wholesale.clec.dataprovider.*,
com.verizon.ebiz.wholesale.cpd.common.*,
com.verizon.ebiz.wholesale.cpd.dbm.*,
com.verizon.ebiz.wholesale.clec.dbm.*,
com.verizon.ebiz.wholesale.clec.holder.*,
com.verizon.ebiz.wholesale.cpd.logger.*,
com.verizon.ebiz.wholesale.connectivity.extranet.client.taglib.*,
com.verizon.ebiz.wholesale.connectivity.intranet.server.common.*,
com.verizon.ebiz.wholesale.connectivity.common.*,
com.verizon.ebiz.wholesale.connectivity.extranet.client.common.*,
com.verizon.ebiz.wholesale.connectivity.intranet.server.util.*;"
%>

<!-- Include the Common Header files -->
<jsp:include page="<%=WebConstants.JSP_HEADER%>" flush="true"/>

<!-- PLACE YOUR TITLE HERE -->
<title>
		Search Connectivity Request - Criteria Window
</title>

<!-- JSP CODE GOES HERE  -->
<%@ taglib uri="ConnectivityTags.tld" prefix="Connectivity" %>

<%
String privilege = (String) request.getAttribute("privilege") ;

//For testing purpose
FormVO formVO = FSFHelper.getFormVO(5);
ArrayList alFieldObjs = (ArrayList)formVO.getFieldList();
session.setAttribute("FIELDS_FOR_FORM_SECTION", alFieldObjs);

ConnectivityCache connectivityCache = ConnectivityCache.getInstance();
StaticData sd = StaticData.getInstance();
CPDCache cpdCacheObj = CPDCache.getInstance();

Hashtable acnaHash = null ;
Hashtable custTypeHash = null;
Vector vCustTypes = null;
try 
{	
    acnaHash = sd.getAcnaHash();
	custTypeHash = cpdCacheObj.getCustomerTypes();
	vCustTypes = cpdCacheObj.getCustomerTypesVector();
}
catch (Exception e) 
{
	e.printStackTrace();
}

ArrayList alAcnas = new ArrayList() ;
ArrayList alBusinessNames = new ArrayList() ;
ArrayList alStates = new ArrayList();
ArrayList alProductGroups = new ArrayList();


String acna = "" ;
String BusinessName = "";
String AcnaCode = "";
String[] acnaList = null ;
String[] acnaCodeList = null ;
String[] stateList = null;
String[] custTypeList = null;

int userType = ((Integer) session.getAttribute("userType")).intValue() ;

if ( userType == WCIDConstants.CUSTOMER_USER_TYPE || userType == WCIDConstants.CUSTOMERADMIN_USER_TYPE )
{	
		System.out.println("User is External");
		acnaList  = (String[]) session.getAttribute("acnalist");
		if( acnaList != null && acnaList.length > 0) 
		{
			for(int i=0; i<acnaList.length; i++) 
			{
			String id = ((ACNA)acnaHash.get(acnaList[i])).getAcna() ;
			String desc = ((ACNA)acnaHash.get(acnaList[i])).getBusinessName() ;
			SimpleObject so = new SimpleObject() ;
			so.setID( id ) ;
			so.setDescription( desc ) ;
			alBusinessNames.add(so);
			}
		}
		
				
		acnaCodeList = (String[]) session.getAttribute("acnaCodelist");
		if( acnaCodeList != null && acnaCodeList.length > 0) 
		{
			for(int i=0; i<acnaCodeList.length; i++) 
			{
			ACNA ac = (ACNA)acnaHash.get(acnaCodeList[i]) ;
			String id = ((ACNA)acnaHash.get(acnaCodeList[i])).getAcna() ;
			String desc = ((ACNA)acnaHash.get(acnaCodeList[i])).getAcnaCode() ;
			SimpleObject so = new SimpleObject() ;
			so.setID( id ) ;
			so.setDescription( desc ) ;
			alAcnas.add(so);
			}
		}	
		
		custTypeList = (String[]) session.getAttribute("custTypelist");
		for (int j = 0; j < custTypeList.length; j++)
		{
		    alProductGroups.add(custTypeHash.get(custTypeList[j]));
		}	
		
		stateList = (String[]) session.getAttribute("statelist");
		for (int k = 0; k < stateList.length; k++)
		{
			alStates.add(stateList[k]);
		}
}
else 
{	
	alAcnas = sd.getAcnaCodeArrayList();
	alBusinessNames = sd.getBusinessNameArrayList();	
	alStates = ProfileDataProvider.getBusinessStates();
    for(int j=0 ; j < vCustTypes.size() ; j++)
	{
	    alProductGroups.add(vCustTypes.get(j));
	}
}


ArrayList alApplications = connectivityCache.getActiveApplicationTypeList();
ArrayList alTransportMethods = connectivityCache.getActiveTransportMethodList();
ArrayList alTransactionTypes = connectivityCache.getActiveTransactionList();
ArrayList alEnvironments = connectivityCache.getActiveEnvironmentList();
ArrayList alECCList = ProfileDataProvider.getEccsForAcnaStatePG(null, null, null);
ArrayList alStatus = connectivityCache.getApplicableStatusForDisplay("E");

// Applications Default Values
ConnectivityCriteria criteriaObj ; 
if ( session.getAttribute("CONNECTIVITY_CRITERIA_OBJECT") != null)
{
	criteriaObj = (ConnectivityCriteria) session.getAttribute("CONNECTIVITY_CRITERIA_OBJECT");
}
else
{
    criteriaObj = new ConnectivityCriteria();
	criteriaObj.initialize();
}
Object bnDefValueObject = (Object) new String(criteriaObj.getBusinessName());
Object acnaDefValueObject = (Object) new String(criteriaObj.getAcnaId());
Object appDefValueObject = (Object) new String(criteriaObj.getApplication());
ArrayList statesDefValueObject = new ArrayList();
String[] saStates = (String[])criteriaObj.getStates();
for(int i=0 ; i < saStates.length ; i++)
{
	statesDefValueObject.add((String)saStates[i]);
}
Object transactionTypeDefValueObject = (Object) new String(criteriaObj.getTransactionType());
Object transportTypeDefValueObject = (Object) new String(criteriaObj.getTransportMethod());
Object ediSenderIdDefValueObject = (Object) new String(criteriaObj.getEdiSenderId());
Object ipAddressDefValueObject   = (Object) new String(criteriaObj.getIpAddress());
Object nodeNameDefValueObject = (Object) new String(criteriaObj.getNodeName());
Object mailboxIDSouthDefValueObject = (Object) new String(criteriaObj.getMailboxIDSouth());


System.out.println("The environment from criteriaObj is --->>> " + criteriaObj.getEnvironment());
Object envDefValueObject = (Object) new String(criteriaObj.getEnvironment());

Object aecnDefValueObject = (Object) new String(criteriaObj.getAecn());
String pgDefValueObject = "";
String[] saPG = (String[])criteriaObj.getProductGroup();
for(int i=0 ; i < saPG.length ; i++)
{
	pgDefValueObject = (String)saPG[0];
}
String statusDefValueObject = "";
String[] saStatus = (String[])criteriaObj.getStatusId();
for(int i=0 ; i < saStatus.length ; i++)
{
	statusDefValueObject = (String)saStatus[0];
}

Object resultsPerPageDefValueObject = (Object) new String(criteriaObj.getRowsPerPage());

// Default Field Names
Object bnFieldNameObject = (Object) new String("businessName");
Object acnaFieldNameObject = (Object) new String("acnaId");
Object appFieldNameObject = (Object) new String("application");
Object stateFieldNameObject = (Object) new String("states");
Object tmFieldNameObject = (Object) new String("transportMethod");
Object ttFieldNameObject = (Object) new String("transactionType");
Object ediFieldNameObject = (Object) new String("ediSenderId");
Object ipAddressFieldNameObject = (Object) new String("ipAddress");
Object nodeNameFieldNameObject = (Object) new String("nodeName");
Object envFieldNameObject = (Object) new String("environment");
Object aecnFieldNameObject = (Object) new String("aecn");
Object pgFieldNameObject = (Object) new String("productGroup");
Object statusFieldNameObject = (Object) new String("statusId");
Object rowsPerPageFieldNameObject = (Object) new String("rowsPerPage");
Object mailboxIDSouthFieldNameObject = (Object) new String("mailboxIDSouth");



//Javascript events
HashMap hmOfEventsForBN = new HashMap();
hmOfEventsForBN.put("onChange", "businessNameFunction()");

HashMap hmOfEventsForIAC = new HashMap();
hmOfEventsForIAC.put("onChange", "iacFunction()");

HashMap hmOfEventsForAPP = new HashMap();
hmOfEventsForAPP.put("onChange", "applicationFunction()");

HashMap hmOfEventsForStates = new HashMap();
hmOfEventsForStates.put("onClick", "resetAllStates()");
	
// Remove the TRANSFER and CRITERIA Objects from the session
session.removeAttribute("CONNECTIVITY_CRITERIA_OBJECT");
session.removeAttribute("CONNECTIVITY_TRANSFER_OBJECT");

if ( privilege != null )
{
	session.setAttribute("privilege", privilege ) ;
}
%>

<!-- END JSP CODE -->

<!-- Javascript functions -->
<script src="/wholesale/lsp/apphome/cpd/secure/includes/javascript/formValidation.js" language="JavaScript" type="text/javascript"></script>
<script language = "javascript">
function businessNameFunction()
{
	//document.connectivity.acnaId.selectedIndex = 0;
	var acnaId = document.connectivity.businessName.options[document.connectivity.businessName.selectedIndex].value;
	
	for ( var ii = 0; ii < document.connectivity.acnaId.options.length; ii ++ ) {
		if ( document.connectivity.acnaId.options[ii].value == acnaId ) {
			document.connectivity.acnaId.options[ii].selected = true;
			break;
		}
	}
	
	//alert("This is a Business name function");
}

function iacFunction()
{
    //document.connectivity.businessName.selectedIndex = 0;
	var acnaId = document.connectivity.acnaId.options[document.connectivity.acnaId.selectedIndex].value;
	
	for ( var ii = 0; ii < document.connectivity.businessName.options.length; ii ++ ) {
		if ( document.connectivity.businessName.options[ii].value == acnaId ) {
			document.connectivity.businessName.options[ii].selected = true;
			break;
		}
	}
		
	//alert("This is a iac function");
}

function applicationFunction()
{
}

function submitForm()
{
     if( document.connectivity.application[document.connectivity.application.selectedIndex].value == "")
	 {
	 	alert("Select an application.");
		return;
	 }
	 if( isInteger(document.connectivity.rowsPerPage.value) == false)
	 {
	 	alert("Enter Integer values in the results per page field.");
		document.connectivity.rowsPerPage.focus();
		document.connectivity.rowsPerPage.select();
		return;
	 }
	 if( (document.connectivity.rowsPerPage.value == 0) || (document.connectivity.rowsPerPage.value == "") )
	 {
		alert("Enter a value greater than zero in the results per page field.");
		document.connectivity.rowsPerPage.focus();
		document.connectivity.rowsPerPage.select();
		return;
	 }
	 else
	 {
	 	//alert("The Application is ---> "+document.connectivity.application.value);
	 	document.connectivity.submit();
	 }
}

function resetForm()
{
       var len = document.connectivity.elements.length;
	   for (i=0 ; i < len ; i++)
	   {	
		   if (document.connectivity.elements[i].type == "checkbox")
		   {
			  document.connectivity.elements[i].checked = false;
		   }
		   else if (document.connectivity.elements[i].type == "radio")
		   {
			  document.connectivity.elements[i].checked = false;
		   }
		   else if (document.connectivity.elements[i].type == "select-one")
		   {
			  document.connectivity.elements[i].selectedIndex = 0;
		   }
		   else if (document.connectivity.elements[i].type == "text")
		   {
			  document.connectivity.elements[i].value = "";
		   }
	   } 
	   document.connectivity.rowsPerPage.value="20";
}
function selectAllStates()
{
    var toCheck = false;
    if(document.connectivity.allStates.checked)
	{ 
	    toCheck = true;
	}
	for (i = 0; i < document.connectivity.states.length; i++)
	{
		document.connectivity.states[i].checked  = toCheck;
	}
}
function resetAllStates()
{
   document.connectivity.allStates.checked  = false;
}

function showMsg ( Msg )
{
	if ( Msg != "" )
    {
		alert( Msg ) ;
	}
}
</script>

<!-- INCLUDE BODY -->
<body bgcolor="#FFFFFF" link="#333366" vlink="#333366" alink="#FF0000" leftmargin="0" topmargin="0" marginheight="0" marginwidth="0" onLoad="showMsg('<%=request.getAttribute("ResultMsg")%>')">
 
<!-- Start the TABLE here -->
<jsp:include page="<%=WebConstants.TABLE_START%>" flush="true"/>


		<!-- page title -->
		<tr><td class="pgTitle"> 
		 <% String helpTitle = "/wholesale/lsp/apphome/cpd/secure/jsp/cpdiHeader.jsp?pageName=Search Connectivity Requests&pageURL=Search_Connectivity_Request_Page.htm";	%>
	     <jsp:include page="<%= helpTitle %>" flush="true"/>  
		</td></tr>
		
		<!-- space between the page title and the body table -->
		<tr><td height="30"><spacer type="block" width="638" height="30"></td></tr>
		
		<!-- body table -->
		<tr><td><div id=conflict></div><div id=conflict1></div>
			<span class="bodyCopy">
			<!-- PLACE YOUR HEADING HERE -->			
			Specify search criteria for retrieving connectivity requests.
			</span><br><br>
			
			<!-- ENTER THE FORM NAME AND ACTION HERE -->
		    <form name="connectivity" method="post" action = "/wholesale/lsp/apphome/cpd/Action/">
			
			<!-- PLACE YOUR HIDDEN FIELDS HERE -->
			<input type=hidden name="extraPath" value="SEARCH_CONNECTIVITY">
			<input type=hidden name="actionAttribute" value="FROM_CRITERIA_PAGE">
			<input type=hidden name="searchConnectivityFormBean.startIndex" value="0">
			<input type=hidden name="searchConnectivityFormBean.arttId" value="">
			
			<table cellpadding="0" cellspacing="0" border="0" width="638" bgcolor="#FFFFCC">
			<tr>
				<!-- column one -->	
				<td width="638" valign="top">

				<!--  BOX 1 -->
				<table cellpadding="0" cellspacing="0" width="638" border="0">
				<tr bgcolor="#CCCCCC">
					<td width="7" height="20" colspan="3"><spacer type="block" width="7" height="20"></td>
					<td width="470" class="subhead">Specify Search Criteria</td>
					<td width="156" class="subbodyCopy" align="right"><span class="requiredField">*</span> Denotes Required Field&nbsp;</td>
					<td width="5" colspan="2"><spacer type="block" width="5" height="20"></td>
				</tr>
				<tr>
					<td width="1" bgcolor="#CCCCCC"><spacer type="block" width="1" height="1"></td>
					<td width="4"><spacer type="block" width="4" height="1"></td>
					<td colspan="3" valign="top"><br>
					
					<!-- PLACE YOUR TABLE CONTENT HERETITLE HERE -->
					<!-- SAMPLE TABLE IS SUPPIED..REPLACE WITH YOUR REQUIREMENT -->					
					<table cellpadding="2" cellspacing="0" border="0">
						<Connectivity:ListElement httpServletRequest="<%=request%>" eventsAndFunctionsObj="<%= hmOfEventsForBN %>" displayValuesObj="<%=alBusinessNames %>" defaultValuesObj="<%= bnDefValueObject %>" fieldNameObj="<%=bnFieldNameObject %>" />
						<tr>
							<td class="requiredField">&nbsp;</td>
							<td class="formlabel">&nbsp;</td>
							<td colspan="4" class="formlabel">
							- OR -
							</td>
						</tr>						
						<Connectivity:ListElement httpServletRequest="<%=request%>" eventsAndFunctionsObj="<%= hmOfEventsForIAC %>" displayValuesObj="<%=alAcnas %>" defaultValuesObj="<%= acnaDefValueObject %>" fieldNameObj="<%=acnaFieldNameObject %>" />
						<Connectivity:ListElement httpServletRequest="<%=request%>" eventsAndFunctionsObj="<%= hmOfEventsForAPP %>" displayValuesObj="<%=alApplications %>" defaultValuesObj="<%= appDefValueObject %>" fieldNameObj="<%= appFieldNameObject %>" />
						<tr> 
							<td class="requiredField">&nbsp;</td> 
							<td class="formlabel">State(s) :</td> 
							<td colspan="4"> 
									<table colspan="100%"> 
									<tr> 
										<td class="formsublabel">
										<input type=checkbox name=allStates value="All" onClick="selectAllStates()">All&nbsp;&nbsp;&nbsp;</td> 
									</table>
							</td> 
						</tr>
						<Connectivity:CheckboxElement httpServletRequest="<%=request%>" displayValuesObj="<%=alStates %>" defaultValuesObj="<%=statesDefValueObject%>" fieldNameObj="<%= stateFieldNameObject %>"  eventsAndFunctionsObj="<%= hmOfEventsForStates %>" displayLabel="NOLABEL" />
						<Connectivity:ListElement httpServletRequest="<%=request%>" displayValuesObj="<%=alTransactionTypes %>" defaultValuesObj="<%= transactionTypeDefValueObject %>" fieldNameObj="<%= ttFieldNameObject %>" fromSearch="Y" />
						<Connectivity:ListElement httpServletRequest="<%=request%>" displayValuesObj="<%=alTransportMethods %>" defaultValuesObj="<%= transportTypeDefValueObject %>" fieldNameObj="<%= tmFieldNameObject %>" />
						<Connectivity:TextElement httpServletRequest="<%=request%>" defaultValuesObj="<%= ediSenderIdDefValueObject %>" fieldNameObj="<%= ediFieldNameObject %>" />
						<Connectivity:TextElement httpServletRequest="<%=request%>" defaultValuesObj="<%= ipAddressDefValueObject %>" fieldNameObj="<%= ipAddressFieldNameObject %>" />
						<Connectivity:TextElement httpServletRequest="<%=request%>" defaultValuesObj="<%= nodeNameDefValueObject %>" fieldNameObj="<%= nodeNameFieldNameObject %>" />
						<Connectivity:RadioElement httpServletRequest="<%=request%>" displayValuesObj="<%=alEnvironments %>" defaultValuesObj="<%= envDefValueObject %>" fieldNameObj="<%= envFieldNameObject %>" fromSearch="Y" />
						<Connectivity:ListElement httpServletRequest="<%=request%>" displayValuesObj="<%=alECCList %>" defaultValuesObj="<%= aecnDefValueObject %>" fieldNameObj="<%= aecnFieldNameObject %>" />
						<Connectivity:ListElement httpServletRequest="<%=request%>" displayValuesObj="<%=alProductGroups %>" defaultValuesObj="<%= pgDefValueObject %>" fieldNameObj="<%= pgFieldNameObject %>" />
						<Connectivity:ListElement httpServletRequest="<%=request%>" displayValuesObj="<%=alStatus %>" defaultValuesObj="<%= statusDefValueObject %>" fieldNameObj="<%= statusFieldNameObject %>" />
						<Connectivity:TextElement httpServletRequest="<%=request%>" defaultValuesObj="<%= mailboxIDSouthDefValueObject %>" fieldNameObj="<%= mailboxIDSouthFieldNameObject %>" />
						<Connectivity:TextElement httpServletRequest="<%=request%>" defaultValuesObj="<%= resultsPerPageDefValueObject %>" fieldNameObj="<%= rowsPerPageFieldNameObject %>" />
						<tr>
							<td colspan="2">&nbsp;</td>
							<td colspan="4"><br><br>
							<span class="button"><a href="javascript:submitForm()">Continue</a></span>&nbsp;&nbsp;&nbsp;
							<span class="button"><a href="javascript:resetForm()">Reset</a></span>
							</td>
						</tr>
					</table>
					<!--  END BOX 1 -->

<!-- End the TABLE here -->					
<jsp:include page="<%=WebConstants.TABLE_END%>" flush="true"/>
