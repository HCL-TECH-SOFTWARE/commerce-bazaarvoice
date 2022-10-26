package com.hcl.commerce.integration.bazaarVoice.command;

/**
*==================================================
Copyright [2022] [HCL America, Inc.]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*==================================================
**/


import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import com.bazaarvoice.seo.sdk.BVManagedUIContent;
import com.bazaarvoice.seo.sdk.BVUIContent;
import com.bazaarvoice.seo.sdk.config.BVClientConfig;
import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.config.BVSdkConfiguration;
import com.bazaarvoice.seo.sdk.model.BVParameters;
import com.bazaarvoice.seo.sdk.model.ContentSubType;
import com.bazaarvoice.seo.sdk.model.ContentType;
import com.bazaarvoice.seo.sdk.model.SubjectType;
import com.ibm.commerce.command.ControllerCommandImpl;
import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.exception.ECApplicationException;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.foundation.common.util.logging.LoggingHelper;
import com.ibm.commerce.foundation.internal.server.services.registry.StoreConfigurationRegistry;
import com.ibm.commerce.ras.ECMessage;
import com.ibm.commerce.ras.ECTrace;

public class BazaarVoiceGetContentCmdImpl extends ControllerCommandImpl implements BazaarVoiceGetContentCmd {
	private static final String COPYRIGHT = "Copyright [2021] [HCL America, Inc.]";
	private static final String CLASS_NAME = BazaarVoiceGetContentCmdImpl.class.getName();
	private static final Logger LOGGER = LoggingHelper.getLogger(BazaarVoiceGetContentCmdImpl.class);

	public static final String BV_CONTENT_OUTPUT = "com.bazaarvoice.commerce.store.<ContentType>_bvContentOutput";
	public static final String CONTENT_TYPE = "com.bazaarvoice.commerce.store.<ContentType>_contentType";
	public static final String PAGE_NUMBER = "com.bazaarvoice.commerce.store.pageNumber";
	public static final String SUBJECT_TYPE = "com.bazaarvoice.commerce.store.subjectType";
	public static final String CONTENT_SUB_TYPE = "com.bazaarvoice.commerce.store.contentSubType";
	public static final String BOT_DETECTION = "com.bazaarvoice.commerce.store.botDetection";
	public static final String BV_ROOT_FOLDER = "Main_Site-<locale>";
	public static final String CLOUD_KEY = "com.bazaarvoice.commerce.store.cloudKey";
	public static final String LOAD_SEO_FILES_LOCALLY = "com.bazaarvoice.commerce.store.loadSeoFilesLocally";
	public static final String LOCAL_SEO_FILE_ROOT = "com.bazaarvoice.commerce.store.local_seo_file_root";
	public static final String CONNECT_TIME_OUT = "com.bazaarvoice.commerce.store.connectTimeout";
	public static final String SOCKET_TIME_OUT = "com.bazaarvoice.commerce.store.socketTimeout";
	public static final String INCLUDE_DISPLAY_INTEGRATION_CODE = "com.bazaarvoice.commerce.store.includeDisplayIntegrationCode";
	public static final String CRAWLER_AGENT_PATTERN = "com.bazaarvoice.commerce.store.crawlerAgentPattern";
	public static final String SEO_SDK_ENABLED = "com.bazaarvoice.commerce.store.seoSdkEnabled";
	public static final String STAGING = "com.bazaarvoice.commerce.store.staging";
	public static final String TESTING = "com.bazaarvoice.commerce.store.testing";
	public static final String EXECUTION_TIME_OUT = "com.bazaarvoice.commerce.store.executionTimeout";
	public static final String EXECUTION_TIME_OUT_BOT = "com.bazaarvoice.commerce.store.executionTimeoutBot";
	public static final String PROXY_HOST = "com.bazaarvoice.commerce.store.proxyHost";
	public static final String PROXY_PORT = "com.bazaarvoice.commerce.store.proxyPort";
	public static final String CHARSET = "com.bazaarvoice.commerce.store.charset";
	public static final String SSL_ENABLED = "com.bazaarvoice.commerce.store.sslEnabled";

	private String userAgent;
	private String subjectId;
	private String baseURI;
	private String pageURI;

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public void setBaseURI(String baseURI) {
		this.baseURI = baseURI;
	}

	public void setPageURI(String pageURI) {
		this.pageURI = pageURI;
	}

	@Override
	public void validateParameters() throws ECApplicationException {
		boolean traceEnabled = LoggingHelper.isTraceEnabled(LOGGER);
		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER))
			LOGGER.entering(getClass().getName(), "validateParameters");

		if (StringUtils.isEmpty(userAgent)) {
			if (traceEnabled)
				LOGGER.exiting(getClass().getName(), "validateParameters", "have a null User Agent");
			throw new ECApplicationException(ECMessage._ERR_MISSING_PARAMETER, getClass().getName(),
					"validateParameters");
		}

		if (StringUtils.isEmpty(subjectId)) {
			if (traceEnabled)
				LOGGER.exiting(getClass().getName(), "validateParameters", "have a null Subject Id");
			throw new ECApplicationException(ECMessage._ERR_MISSING_PARAMETER, getClass().getName(),
					"validateParameters");
		}

		if (StringUtils.isEmpty(baseURI)) {
			if (traceEnabled)
				LOGGER.exiting(getClass().getName(), "validateParameters", "have a null Base URI");
			throw new ECApplicationException(ECMessage._ERR_MISSING_PARAMETER, getClass().getName(),
					"validateParameters");
		}

		if (StringUtils.isEmpty(pageURI)) {
			if (traceEnabled)
				LOGGER.exiting(getClass().getName(), "validateParameters", "have a null Page URI");
			throw new ECApplicationException(ECMessage._ERR_MISSING_PARAMETER, getClass().getName(),
					"validateParameters");
		}

		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER))
			LOGGER.exiting(getClass().getName(), "validateParameters");
	}

	@Override
	public void setRequestProperties(TypedProperty typeProperties) throws ECException {
		String strMethodName = "setRequestProperties";
		ECTrace.entry(4L, getClass().getName(), strMethodName);

		requestProperties = typeProperties;

		setUserAgent(requestProperties.getString("userAgent", null));
		setSubjectId(requestProperties.getString("subjectId", null));
		setBaseURI(requestProperties.getString("baseURI", null));
		setPageURI(requestProperties.getString("pageURI", null));

		ECTrace.trace(4L, getClass().getName(), strMethodName, "reqProperties =  " + typeProperties.toString());
		ECTrace.exit(4L, getClass().getName(), strMethodName);
	}

	public void performExecute() throws ECApplicationException {
		boolean traceEnabled = LoggingHelper.isTraceEnabled(LOGGER);
		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER))
			LOGGER.entering(getClass().getName(), "performExecute");

		TypedProperty result = new TypedProperty();

		Integer storeId = getStoreId();
		List<String> contentTypes = Arrays.asList("ratingSummary", "reviews", "questions");
		StoreConfigurationRegistry storeConfRegistry = StoreConfigurationRegistry.getSingleton();

		for(String contentTypeStr : contentTypes) {
			// Content output type to be retrieved
			String bvContentOutput = storeConfRegistry.getValue(storeId,
					BV_CONTENT_OUTPUT.replaceAll("<ContentType>", contentTypeStr));
	
			// BV Parameters
			String userAgent = this.userAgent;
			String baseURI = this.baseURI;
			String pageURI = this.pageURI;
			String subjectId = this.subjectId;
			String pageNumber = storeConfRegistry.getValue(storeId, PAGE_NUMBER);
			String contentType = storeConfRegistry.getValue(storeId,
					CONTENT_TYPE.replaceAll("<ContentType>", contentTypeStr));
			String subjectType = storeConfRegistry.getValue(storeId, SUBJECT_TYPE);
			String contentSubType = storeConfRegistry.getValue(storeId, CONTENT_SUB_TYPE);
	
			// Legacy, inactive properties.
			//
			// These are only retained to stop old client code from breaking when
			// the
			// SDK version is updated. They have no effect if set.
			// Property removed starting with version 2.1.0.1
			String botDetection = storeConfRegistry.getValue(storeId, BOT_DETECTION);
	
			// Active configuration properties.
			String bvRootFolder = BV_ROOT_FOLDER.replaceAll("<locale>", "en_US");
			String cloudKey = storeConfRegistry.getValue(storeId, CLOUD_KEY);
			String loadSeoFilesLocally = storeConfRegistry.getValue(storeId, LOAD_SEO_FILES_LOCALLY);
			String localSeoFileRoot = storeConfRegistry.getValue(storeId, LOCAL_SEO_FILE_ROOT);
			String connectTimeout = storeConfRegistry.getValue(storeId, CONNECT_TIME_OUT);
			String socketTimeout = storeConfRegistry.getValue(storeId, SOCKET_TIME_OUT);
			String includeDisplayIntegrationCode = storeConfRegistry.getValue(storeId, INCLUDE_DISPLAY_INTEGRATION_CODE);
			String crawlerAgentPattern = storeConfRegistry.getValue(storeId, CRAWLER_AGENT_PATTERN);
			String seoSdkEnabled = storeConfRegistry.getValue(storeId, SEO_SDK_ENABLED);
			String staging = storeConfRegistry.getValue(storeId, STAGING);
			String testing = storeConfRegistry.getValue(storeId, TESTING);
			String executionTimeout = storeConfRegistry.getValue(storeId, EXECUTION_TIME_OUT);
			String executionTimeoutBot = storeConfRegistry.getValue(storeId, EXECUTION_TIME_OUT_BOT);
			String proxyHost = storeConfRegistry.getValue(storeId, PROXY_HOST);
			String proxyPort = storeConfRegistry.getValue(storeId, PROXY_PORT);
			String charset = storeConfRegistry.getValue(storeId, CHARSET);
			String sslEnabled = storeConfRegistry.getValue(storeId, SSL_ENABLED);
	
			//
			// Set the BVSdkConfiguration client instance configuration.
			//
			BVConfiguration bvConfig = new BVSdkConfiguration();
	
			// BOT_DETECTION property
			// This property has been deprecated starting with version 2.1.0.1
			// Property is still retained to stop old client code from breaking when
			// the
			// SDK version is updated. They have no effect if set.
			if (botDetection != null && !botDetection.isEmpty()) {
				// false is the default value. False disables bot detection. BV
				// recommends you use bot detection only in special ocassions.
				bvConfig.addProperty(BVClientConfig.BOT_DETECTION, botDetection);
			}
	
			// BV_ROOT_FOLDER property
			if (bvRootFolder != null && !bvRootFolder.isEmpty()) {
				// 'feeds' Contact BV to obtain the value you need for this
				// property, also knows as the display code.
				bvConfig.addProperty(BVClientConfig.BV_ROOT_FOLDER, bvRootFolder);
			}
	
			// CLOUD_KEY property
			if (cloudKey != null && !cloudKey.isEmpty()) {
				bvConfig.addProperty(BVClientConfig.CLOUD_KEY, cloudKey);
			}
	
			// LOAD_SEO_FILES_LOCALLY property
			// If the LOAD_SEO_FILES_LOCALLY property is set to true, then the SEO
			// contents will be retrieved
			// from the local filesystem rather than via an HTTP request. Enable
			// this flag if you are retrieving
			// your daily SEO feed from the Bazaarvoice FTP server and expanding it
			// into a directory accessible
			// through your local filesystem. You must also set the root directory
			// in the LOCAL_SEO_FILE_ROOT property.
			if (loadSeoFilesLocally != null && !loadSeoFilesLocally.isEmpty()) {
				bvConfig.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, loadSeoFilesLocally);
			}
	
			// LOCAL_SEO_FILE_ROOT property
			if (localSeoFileRoot != null && !localSeoFileRoot.isEmpty()) {
				bvConfig.addProperty(BVClientConfig.LOCAL_SEO_FILE_ROOT, localSeoFileRoot);
			}
	
			// CONNECT_TIMEOUT property
			if (connectTimeout != null && !connectTimeout.isEmpty()) {
				// 2000ms is the default value (to match
				// BVClient.EXECUTION_TIMEOUT_BOT).
				bvConfig.addProperty(BVClientConfig.CONNECT_TIMEOUT, connectTimeout);
			}
	
			// SOCKET_TIMEOUT property
			if (socketTimeout != null && !socketTimeout.isEmpty()) {
				// 2000ms is the default value (to match
				// BVClient.EXECUTION_TIMEOUT_BOT).
				bvConfig.addProperty(BVClientConfig.SOCKET_TIMEOUT, socketTimeout);
			}
	
			// INCLUDE_DISPLAY_INTEGRATION_CODE property
			if (includeDisplayIntegrationCode != null && !includeDisplayIntegrationCode.isEmpty()) {
				bvConfig.addProperty(BVClientConfig.INCLUDE_DISPLAY_INTEGRATION_CODE, includeDisplayIntegrationCode);
			}
	
			// CRAWLER_AGENT_PATTERN property
			if (crawlerAgentPattern != null && !crawlerAgentPattern.isEmpty()) {
				bvConfig.addProperty(BVClientConfig.CRAWLER_AGENT_PATTERN, crawlerAgentPattern);
			}
	
			// SEO_SDK_ENABLED property
			if (seoSdkEnabled != null && !seoSdkEnabled.isEmpty()) {
				// true is the default value
				bvConfig.addProperty(BVClientConfig.SEO_SDK_ENABLED, seoSdkEnabled);
			}
	
			// STAGING property
			if (staging != null && !staging.isEmpty()) {
				// MAKE THIS CONFIGURABLE. Set to true for staging environment data.
				bvConfig.addProperty(BVClientConfig.STAGING, staging);
			}
	
			// TESTING property
			if (testing != null && !testing.isEmpty()) {
				// MAKE THIS CONFIGURABLE. Set to true for testing environment data.
				// bvConfig.addProperty(BVClientConfig.TESTING, testing);
			}
	
			// EXECUTION_TIMEOUT property
			if (executionTimeout != null && !executionTimeout.isEmpty()) {
				// 500ms is the default value. If this value is set to 0ms, no
				// connection attempts will occur.
				bvConfig.addProperty(BVClientConfig.EXECUTION_TIMEOUT, executionTimeout);
			}
	
			// EXECUTION_TIMEOUT_BOT property
			if (executionTimeoutBot != null && !executionTimeoutBot.isEmpty()) {
				// Default value of 2000ms, which is the execution timeout intended
				// for search bots. The minimum configurable value for this timeout
				// is 100ms.
				bvConfig.addProperty(BVClientConfig.EXECUTION_TIMEOUT_BOT, executionTimeoutBot);
			}
	
			// PROXY_HOST property
			if (proxyHost != null && !proxyHost.isEmpty()) {
				bvConfig.addProperty(BVClientConfig.PROXY_HOST, proxyHost);
			}
	
			// PROXY_PORT property
			if (proxyPort != null && !proxyPort.isEmpty()) {
				bvConfig.addProperty(BVClientConfig.PROXY_PORT, proxyPort);
			}
	
			// CHARSET property
			if (charset != null && !charset.isEmpty()) {
				// Enable or disable custom character set support. If not
				// recognized, it will use the default.
				bvConfig.addProperty(BVClientConfig.CHARSET, sslEnabled);
			}
	
			// SSL_ENABLED property
			if (sslEnabled != null && !sslEnabled.isEmpty()) {
				// Enable or disable SSL support.
				bvConfig.addProperty(BVClientConfig.SSL_ENABLED, sslEnabled);
			}
	
			//
			// Set the BVParameters instance.
			//
			BVParameters bvParameters = new BVParameters();
	
			// UserAgent parameter
			if (userAgent != null && !userAgent.isEmpty()) {
				// Taken from HTTPServletRequest, required parameter. //
				// request.getHeader("User-Agent")
				bvParameters.setUserAgent(userAgent);
			}
	
			// BaseURI parameter
			// URL that contains complete information about the product, category,
			// and page number.
			// The URL can be absolute or relative. The URL can also be a new
			// Conversations-supported
			// URL that can contain bvpage query parameters.
			if (baseURI != null && !baseURI.isEmpty()) {
				// for example Example-Myshco.jsp
				bvParameters.setBaseURI(baseURI);
			}
	
			// PageURI parameter
			if (pageURI != null && !pageURI.isEmpty()) {
				// for example request.getRequestURI() + "?" +
				// request.getQueryString()
				bvParameters.setPageURI(pageURI);
			}
	
			// SubjectId parameter
			// Identifier for the subject to retrieve the content.
			// If SubjectType is Product, for example, with reviews or questions and
			// answers, this value will be productId.
			// If SubjectType is Category, this value will be reviewId.
			if (subjectId != null && !subjectId.isEmpty()) {
				bvParameters.setSubjectId(subjectId);
			}
	
			// PageNumber parameter
			if (pageNumber != null && !pageNumber.isEmpty()) {
				bvParameters.setPageNumber(pageNumber);
			}
	
			// ContentType parameter
			if (contentType != null && !contentType.isEmpty()) {
	
				// Ratings and reviews content type
				if (contentType.equals("reviews"))
					bvParameters.setContentType(ContentType.REVIEWS);
	
				// Reviews page content type
				if (contentType.equals("reviews page"))
					bvParameters.setContentType(ContentType.REVIEWSPAGE);
	
				// Q&A content type
				if (contentType.equals("questions"))
					bvParameters.setContentType(ContentType.QUESTIONS);
	
				// Questions page content type
				if (contentType.equals("questions page"))
					bvParameters.setContentType(ContentType.QUESTIONSPAGE);
	
				// Stories content type
				if (contentType.equals("stories"))
					bvParameters.setContentType(ContentType.STORIES);
	
				// Stories page content type
				if (contentType.equals("stories page"))
					bvParameters.setContentType(ContentType.STORIESPAGE);
	
				// Universal content type
				if (contentType.equals("universal"))
					bvParameters.setContentType(ContentType.UNIVERSAL);
			}
	
			// SubjectType parameter
			if (subjectType != null && !subjectType.isEmpty()) {
	
				if (subjectType.equals("product"))
					bvParameters.setSubjectType(SubjectType.PRODUCT);
	
				if (subjectType.equals("category"))
					bvParameters.setSubjectType(SubjectType.CATEGORY);
	
				if (subjectType.equals("entry"))
					bvParameters.setSubjectType(SubjectType.ENTRY);
	
				if (subjectType.equals("detail"))
					bvParameters.setSubjectType(SubjectType.DETAIL);
			}
	
			// ContentSubType parameter
			if (contentSubType != null && !contentSubType.isEmpty()) {
	
				if (contentSubType.equals(""))
					bvParameters.setContentSubType(ContentSubType.NONE);
	
				if (contentSubType.equals("stories list"))
					bvParameters.setContentSubType(ContentSubType.STORIES_LIST);
	
				if (contentSubType.equals("stories grid"))
					bvParameters.setContentSubType(ContentSubType.STORIES_GRID);
			}
	
			//
			// Retrieve bazaarvoice content based on the configuration and
			// parameters provided.
			//
			BVUIContent bvManagedUiContent = new BVManagedUIContent(bvConfig);
			String sContent = "";
	
			if (bvContentOutput != null && !bvContentOutput.isEmpty()) {
	
				if (bvContentOutput.equalsIgnoreCase("content")) {
					// This method returns bazaarvoice managed content.
					sContent = bvManagedUiContent.getContent(bvParameters);
				} else if (bvContentOutput.equalsIgnoreCase("aggregateRating")) {
					// This method returns bazaarvoice aggregate rating content.
					sContent = bvManagedUiContent.getAggregateRating(bvParameters);
				} else if (bvContentOutput.equalsIgnoreCase("reviews")) {
					// This method returns bazaarvoice review content.
					sContent = bvManagedUiContent.getReviews(bvParameters);
				} else if (bvContentOutput.equalsIgnoreCase("postProcess")) {
					// This method returns bazaarvoice post process content.
					sContent = bvManagedUiContent.getReviews(bvParameters);
				} else {
					// If content output parameter does not exist, then try to get
					// managed content by default.
					sContent = bvManagedUiContent.getContent(bvParameters);
				}
			} else {
				// If no content output is specified, then try to get managed
				// content by default.
				sContent = bvManagedUiContent.getContent(bvParameters);
			}
	
			result.put(contentTypeStr, sContent);
		}
		
		setResponseProperties(result);
	}
}
