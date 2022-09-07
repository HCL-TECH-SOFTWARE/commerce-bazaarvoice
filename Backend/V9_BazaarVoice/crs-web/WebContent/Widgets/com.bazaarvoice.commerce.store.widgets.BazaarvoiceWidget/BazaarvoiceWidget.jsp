<%--
	=================================================================
	Copyright [2021] [HCL America, Inc.]
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
