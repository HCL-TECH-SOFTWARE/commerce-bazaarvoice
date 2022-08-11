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

<!-- BEGIN BazaarvoiceWidget.jsp -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://commerce.ibm.com/foundation" prefix="wcf" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://commerce.ibm.com/base" prefix="wcbase" %>
<%@ taglib uri="flow.tld" prefix="flow" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://commerce.ibm.com/coremetrics"  prefix="cm" %>
<%@ taglib uri="http://commerce.ibm.com/foundation-fep/stores" prefix="wcst" %>
<%@include file="/Widgets_801/Common/EnvironmentSetup.jspf"%>
<fmt:setBundle basename="/Widgets/Properties/widgettext" var="widgettext" />
<c:set var="widgetPreviewText" value="${widgettext}"/>
<c:set var="emptyWidget" value="false"/>

<%@ include file="ext/BazaarvoiceWidget_Data.jspf" %>
<c:if test = "${param.custom_data ne 'true'}">
	<%@ include file="BazaarvoiceWidget_Data.jspf" %>
</c:if>

<c:if test="${env_inPreview && !env_storePreviewLink}">	
	<jsp:useBean id="previewWidgetProperties" class="java.util.LinkedHashMap" scope="page" />
	<c:set target="${previewWidgetProperties}" property="displayType" value="${param.displayType}" />
</c:if>

<%@ include file="/Widgets_801/Common/StorePreviewShowInfo_Start.jspf" %>

<%@ include file="ext/BazaarvoiceWidget_UI.jspf" %>
<c:if test = "${param.custom_data ne 'true'}">
	<%@ include file="BazaarvoiceWidget_UI.jspf" %>
</c:if>

<%@ include file="/Widgets_801/Common/StorePreviewShowInfo_End.jspf" %>
<!-- END BazaarvoiceWidget.jsp -->
