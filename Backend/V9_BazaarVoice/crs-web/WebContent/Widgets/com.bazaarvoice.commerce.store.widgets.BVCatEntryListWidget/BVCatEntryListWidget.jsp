<%--
	=================================================================
	Copyright [2021] [HCL Technologies]

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
	=================================================================
--%>

<!-- BEGIN CatalogEntryList.jsp -->

<%@ include file="/Widgets_801/Common/EnvironmentSetup.jspf" %>

<c:if test="${!empty param.sortBy && !(param.sortBy == '0' && empty WCParam.orderBy) &&( param.sortBy != WCParam.orderBy) }">
	<c:remove var="includedCategoryNavigationSetupJSPF" />
</c:if>
<c:if test="${(!empty param.pgl_widgetSlotId) && (!empty param.pgl_widgetDefId) && (!empty param.pgl_widgetId)}">
	<c:set var="widgetSuffix" value="_${fn:escapeXml(param.pgl_widgetSlotId)}_${fn:escapeXml(param.pgl_widgetDefId)}_${fn:escapeXml(param.pgl_widgetId)}" scope="request" />
	<c:set var="widgetPrefix" value="${fn:escapeXml(param.pgl_widgetSlotId)}_${fn:escapeXml(param.pgl_widgetId)}" scope="request" />
</c:if>

<c:if test="${requestScope.pageGroup != 'Product'}">
	<%@ include file="ext/BVCatEntryListWidget_Data.jspf" %>
	<%@ include file="BVCatEntryListWidget_Data.jspf" %>
</c:if>

<span class="spanacce" id="searchBasedNavigation_widget_ACCE_Label"  aria-hidden="true"><wcst:message key="ACCE_Region_Product_List" bundle="${widgetText}" /></span>

<script type="text/javascript">
	$(document).ready(function() {
		if(typeof SKUListJS != 'undefined'){
			SKUListJS.setCommonParameters('<c:out value="${langId}"/>','<c:out value="${storeId}" />','<c:out value="${catalogId}" />','<c:out value="${disableProductCompare}" />');
			wcTopic.subscribe('DefiningAttributes_Resolved', SKUListJS.filterSkusByAttribute);
			wcTopic.subscribe('DefiningAttributes_Changed', SKUListJS.filterSkusByAttribute);
			wcTopic.subscribe('ProductAddedToCart', SKUListJS.removeAllQuantities);
			wcTopic.subscribe('SKUsAddedToReqList', SKUListJS.removeAllQuantities);
		}
	});
	
	window.onresize = function() {
		if(typeof SKUListJS != 'undefined'){
			SKUListJS.arrangeProductDetailTables();
		}
	};
</script>

<jsp:include page="/Widgets_801/com.ibm.commerce.store.widgets.PDP_AddToRequisitionLists/AddToRequisitionLists.jsp" flush="true">
	<jsp:param name="buttonStyle" value="none"/>
	<jsp:param name="addMultipleSKUs" value="true"/>
	<jsp:param name="nestedAddToRequisitionListsWidget" value="true"/>
	<jsp:param name="parentPage" value="${fn:escapeXml(widgetPrefix)}"/>
</jsp:include>

<c:choose>
		<c:when test="${!empty emsName && !empty contentPositions && !empty contentNames}">	
				<c:set var="widgetManagedByMarketing" value="true" />
		</c:when>
		<c:otherwise>
				<c:set var="widgetManagedByMarketing" value="false" />
		</c:otherwise>
</c:choose>

<c:if test="${env_inPreview && !env_storePreviewLink}">
  <jsp:useBean id="previewWidgetProperties" class="java.util.LinkedHashMap" scope="page" />
	<c:set target="${previewWidgetProperties}" property="pageView" value="${param.pageView}" />	
	<c:set target="${previewWidgetProperties}" property="sortBy" value="${param.sortBy}" />
	<c:set target="${previewWidgetProperties}" property="disableProductCompare" value="${disableProductCompare}" />
	<c:set target="${previewWidgetProperties}" property="enableSKUListView" value="${param.enableSKUListView}" />
	<%@ include file="/Widgets_801/Common/StorePreviewShowInfo_Start.jspf" %>
</c:if>
	
	<%@ include file="ext/BVCatEntryListWidget_UI.jspf" %>
	<%@ include file="BVCatEntryListWidget_UI.jspf" %>

<%@ include file="/Widgets_801/Common/StorePreviewShowInfo_End.jspf" %>

<wcpgl:pageLayoutWidgetCache/>
<!-- END CatalogEntryList.jsp -->