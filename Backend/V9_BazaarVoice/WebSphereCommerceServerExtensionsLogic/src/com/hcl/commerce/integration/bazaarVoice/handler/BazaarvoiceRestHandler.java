package com.hcl.commerce.integration.bazaarVoice.handler;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.rest.classic.core.AbstractConfigBasedClassicHandler;
import com.ibm.commerce.rest.javadoc.ResponseSchema;

@Path("/store/{storeId}/bazaarvoice")
public class BazaarvoiceRestHandler extends AbstractConfigBasedClassicHandler {
	private static final String CLASSNAME = BazaarvoiceRestHandler.class.getName();
	private static final String RESOURCE_NAME = "bazaarvoice";

	private static final String BAZAARVOICE_GET_CONTENT = "content";

	private static final String CLASS_NAME_PARAMETER_BAZAARVOICE_GET_CONTENT = "com.hcl.commerce.integration.bazaarVoice.command.BazaarVoiceGetContentCmd";

	@Override
	public String getResourceName() {
		return RESOURCE_NAME;
	}

	@Path(BAZAARVOICE_GET_CONTENT)
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_XHTML_XML,
		MediaType.APPLICATION_ATOM_XML })
	@ResponseSchema(parameterGroup = RESOURCE_NAME, responseCodes = {
			@com.ibm.commerce.rest.javadoc.ResponseCode(code = 200, reason = "The requested completed successfully."),
			@com.ibm.commerce.rest.javadoc.ResponseCode(code = 400, reason = "Bad request. Some of the inputs provided to the request aren't valid."),
			@com.ibm.commerce.rest.javadoc.ResponseCode(code = 401, reason = "Not authenticated. The user session isn't valid."),
			@com.ibm.commerce.rest.javadoc.ResponseCode(code = 403, reason = "The user isn't authorized to perform the specified request."),
			@com.ibm.commerce.rest.javadoc.ResponseCode(code = 404, reason = "The specified resource couldn't be found."),
			@com.ibm.commerce.rest.javadoc.ResponseCode(code = 500, reason = "Internal server error. Additional details will be contained on the server logs.") })
	public Response getBazaarvoiceContent(@PathParam("storeId") String storeId,
			@QueryParam(value = "responseFormat") String responseFormat) throws Exception {
		String METHODNAME = "getBazaarvoiceContent";
		Response response = null;
		try {
			TypedProperty requestProperties = initializeRequestPropertiesFromRequestMap(responseFormat);
			requestProperties.put("userAgent", request.getHeader("user-agent"));
			
			if (responseFormat == null)
				responseFormat = "application/json";
			response = executeControllerCommandWithContext(storeId, CLASS_NAME_PARAMETER_BAZAARVOICE_GET_CONTENT,
					requestProperties, responseFormat);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}
}
