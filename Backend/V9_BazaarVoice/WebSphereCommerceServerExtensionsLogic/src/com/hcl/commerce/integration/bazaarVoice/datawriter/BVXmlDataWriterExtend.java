package com.hcl.commerce.integration.bazaarVoice.datawriter;

/**
	*==================================================
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
	*==================================================
**/

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.TransformerException;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;

import com.ibm.commerce.foundation.common.util.logging.LoggingHelper;
import com.ibm.commerce.foundation.dataload.datawriter.AbstractDataWriter;
import com.ibm.commerce.foundation.dataload.exception.DataLoadConfigException;
import com.ibm.commerce.foundation.dataload.exception.DataLoadException;
import com.ibm.commerce.foundation.dataload.object.TableDataObject;

/**
 * Custom XML Writer for Bazaarvoice
 * @author rahulkisan.yewale
 */
public class BVXmlDataWriterExtend extends AbstractDataWriter {
	private static final String COPYRIGHT = "(c) Copyright International Business Machines Corporation 1996,2008";
	private static final String CLASSNAME = BVXmlDataWriterExtend.class.getName();
	private static final Logger LOGGER = LoggingHelper.getLogger(BVXmlDataWriterExtend.class);
	String fileName;
	String mode;
	String name;
	boolean isIncremental;
	boolean isGenerator;
	private Node dataWriterDataNode;
	private BVOutputDataWriterExtend writer;

	public BVXmlDataWriterExtend() {
		fileName = null;
		mode = "Create";
		name = "NONE";
		isIncremental = false;
		isGenerator = true;
		dataWriterDataNode = null;
		writer = null;
	}

	/**
	 * Initializing Properties Which are set in Data Extract Configuration XML
	 * @throws DataLoadException
	 */
	public void init() throws DataLoadException {
		String METHODNAME = "init";
		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER))
			LOGGER.entering(CLASSNAME, METHODNAME);
		super.init();
		dataWriterDataNode = (Node) getConfigProperties().getPropertiesMap().get("DataWriterDataNode");
		fileName = getConfigProperties().getProperty("DataOutputLocation");
		if (fileName == null || fileName.isEmpty()) {
			if (LoggingHelper.isEntryExitTraceEnabled(LOGGER))
				LOGGER.exiting(CLASSNAME, METHODNAME, "DataOutputLocation is missing");
			throw new DataLoadConfigException("_ERR_MISSING_DATA_OUTPUT", new Object[] { "DataOutputLocation" }, CLASSNAME, METHODNAME);
		}
			
		String mode = getConfigProperties().getProperty("mode");
		if (!StringUtils.isEmpty(mode))
			this.mode = mode;
		String name = getConfigProperties().getProperty("bvaccount");
		if (!StringUtils.isEmpty(name))
			this.name = name;
		String incremental = getConfigProperties().getProperty("incremental");
		if (!StringUtils.isEmpty(incremental))
			this.isIncremental = Boolean.parseBoolean(incremental);
		String generator = getConfigProperties().getProperty("generator");
		if (!StringUtils.isEmpty(generator))
			this.isGenerator = Boolean.parseBoolean(generator);
		if (LOGGER.isLoggable(Level.FINER))
			LOGGER.logp(Level.FINER, CLASSNAME, METHODNAME,
					(new StringBuilder("DataOutput filename: ")).append(fileName).append(", iMode=").append(mode)
							.append(", iName=").append(name).append(", iIncremental=").append(isIncremental)
							.append(", iGenerator=").append(isGenerator).toString());
		try {
			writer = new BVOutputDataWriterExtend(fileName, mode, name, isIncremental, isGenerator);
		} catch (Exception e) {
			if (LoggingHelper.isEntryExitTraceEnabled(LOGGER))
				LOGGER.exiting(CLASSNAME, METHODNAME, "Can not create output writer due to exception.");
			throw new RuntimeException("Can not create output writer due to exception.", e);
		}
		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER))
			LOGGER.exiting(CLASSNAME, METHODNAME);
	}

	/**
	 * Save the each row in XML file, which is getting as result of query 
	 * @throws DataLoadException
	 */
	public void save(Object obj) {
		String METHODNAME = "save";
		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER))
			LOGGER.entering(CLASSNAME, METHODNAME);
		
		if (obj != null && (obj instanceof List)) {
			List objList = (List) obj;
			
			for (int i = 0; i < objList.size(); i++) {
				Object objMap = objList.get(i);
				if (objMap instanceof HashMap) {
					HashMap hMap = (HashMap) objMap;
					writer.write(hMap);
				}
			}
		}
		
		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER))
			LOGGER.exiting(CLASSNAME, METHODNAME);
	}

	/**
	 * 
	 * @param tabledataobject
	 * @throws DataLoadException
	 */
	protected void processData(TableDataObject tabledataobject) throws DataLoadException {
		
	}

	/**
	 * Save and Close Complete XML Document
	 * @throws DataLoadException
	 */
	public void close() throws DataLoadException {
		String METHODNAME = "close";
		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER))
			LOGGER.entering(CLASSNAME, "close");
		
		if (writer != null) {
			try {
				writer.saveAndcloseDataWriter();
			} catch (TransformerException e) {
				if (LoggingHelper.isEntryExitTraceEnabled(LOGGER))
					LOGGER.exiting(CLASSNAME, METHODNAME, "Exception occured while creating XML file");
				e.printStackTrace();
			}
		}
		
		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER))
			LOGGER.exiting(CLASSNAME, METHODNAME);
	}

}
