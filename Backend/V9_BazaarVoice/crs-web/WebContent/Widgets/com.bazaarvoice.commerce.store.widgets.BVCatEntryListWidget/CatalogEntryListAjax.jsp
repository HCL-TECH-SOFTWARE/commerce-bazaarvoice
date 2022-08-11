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

<!-- BEGIN CatalogEntryListAjax.jsp -->
<%@ include file="/Widgets_801/Common/EnvironmentSetup.jspf" %>
<%@ include file="/Widgets_801/Common/nocache.jspf" %>

<c:set var="widgetSuffix" value="${fn:escapeXml(param.objectId)}" scope="request" />
<c:set var="widgetPrefix" value="${fn:escapeXml(param.widgetPrefix)}" scope="request" />
<c:remove var="includedCategoryNavigationSetupJSPF"/>
<%@ include file="ext/BVCatEntryListWidget_Data.jspf" %>
<%@ include file="BVCatEntryListWidget_Data.jspf" %>

<%-- Search for the Master catalogId being used for a given storeId --%>
<%@ include file="/Bazaarvoice/BVSearchForMasterCatalogId.jspf" %>

	<c:choose>
		<c:when test="${!empty param.emsName && !empty param.contentPositions && !empty param.contentNames}">	
			<c:set var="widgetManagedByMarketing" value="true" />
		</c:when>
		<c:otherwise>
			<c:set var="widgetManagedByMarketing" value="false" />
		</c:otherwise>
	</c:choose>
	
	<c:if test="${env_inPreview && !env_storePreviewLink}">
	  <jsp:useBean id="previewWidgetProperties" class="java.util.LinkedHashMap" scope="page" />
		<c:set target="${previewWidgetProperties}" property="pageView" value="${initPageView}" />	
		<c:set target="${previewWidgetProperties}" property="sortBy" value="${initSortOrder}" />
		<c:set target="${previewWidgetProperties}" property="disableProductCompare" value="${disableProductCompare}" />
		<%@ include file="/Widgets_801/Common/StorePreviewShowInfo_Start.jspf" %>
	</c:if>
		

<%@ include file="ext/BVCatEntryListWidget_UI.jspf" %>
<%@ include file="BVCatEntryListWidget_UI.jspf" %>

<%@ include file="/Widgets_801/Common/StorePreviewShowInfo_End.jspf" %>

<!-- END CatalogEntryListAjax.jsp -->
