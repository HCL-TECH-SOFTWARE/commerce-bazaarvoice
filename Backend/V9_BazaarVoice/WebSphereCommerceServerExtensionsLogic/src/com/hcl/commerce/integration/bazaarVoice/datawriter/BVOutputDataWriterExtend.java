package com.hcl.commerce.integration.bazaarVoice.datawriter;

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

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.ibm.commerce.foundation.common.util.logging.LoggingHelper;

public class BVOutputDataWriterExtend {
	private static final String COPYRIGHT = "Copyright [2022] [HCL America, Inc.]";
	private static final String CLASSNAME = BVOutputDataWriterExtend.class.getName();
	private static final Logger LOGGER = LoggingHelper.getLogger(BVOutputDataWriterExtend.class);
	
	private static final String GENERATOR = "Bazaarvoice HCL WebSphere Commerce V9 extension V1.1";
	private static final String XML_ELEMENT_FEED = "Feed";
	private static final String XML_ELEMENT_CATEGORIES = "Categories";
	private static final String XML_ELEMENT_PRODUCTS = "Products";
	private static final String XML_ELEMENT_CATEGORY = "Category";
	private static final String XML_ELEMENT_PRODUCT = "Product";
	private static final String XML_ELEMENT_NAME = "Name";
	private static final String XML_ELEMENT_EXTERNAL_ID = "ExternalId";
	private static final String XML_ELEMENT_PARENT_EXTERNAL_ID = "ParentExternalId";
	private static final String XML_ELEMENT_CATEGORY_PAGE_URLS = "CategoryPageUrls";
	private static final String XML_ELEMENT_CATEGORY_PAGE_URL = "CategoryPageUrl";
	private static final String XML_ELEMENT_IMAGE_URLS = "ImageUrls";
	private static final String XML_ELEMENT_IMAGE_URL = "ImageUrl";
	private static final String XML_ELEMENT_CATEGORY_EXTERNAL_ID = "CategoryExternalId";
	private static final String XML_ELEMENT_MANUFACTURER_PART_NUMBERS = "ManufacturerPartNumbers";
	private static final String XML_ELEMENT_MANUFACTURER_PART_NUMBER = "ManufacturerPartNumber";
	private static final String XML_ELEMENT_NAMES = "Names";
	private static final String XML_ELEMENT_DESCRIPTIONS = "Descriptions";
	private static final String XML_ELEMENT_DESCRIPTION = "Description";
	private static final String XML_ELEMENT_PRODUCT_PAGE_URLS = "ProductPageUrls";
	private static final String XML_ELEMENT_PRODUCT_PAGE_URL = "ProductPageUrl";
	
	private static final String XML_ELEMENT_ATTR_LOCAL = "locale";
	private static final String XML_ELEMENT_ATTR_VALUE_LOCAL = "en_US";
	
	private static final String NAME = "NAME";
	private static final String CATGROUP_ID = "CATGROUP_ID";
	private static final String PARENT_CATGROUP_ID = "PARENT_CATGROUP_ID";
	private static final String CATEGORY_PAGE_URL = "CATEGORY_PAGE_URL";
	private static final String IMAGE_URL = "IMAGE_URL";
	private static final String CATENTRY_ID = "CATENTRY_ID";
	private static final String PARTNUMBER = "PARTNUMBER";
	private static final String PRODUCT_PAGE_URL = "PRODUCT_PAGE_URL";
	private static final String SHORTDESCRIPTION = "SHORTDESCRIPTION";
	
	private Document document;
	private Element rootElement;
	private Element categoriesElement;
	private Element productsElement;
	private String targetFilePath;
	private int outputMode;
	
	private boolean firstInvoke;
	private String groupName;
	private String name;
	private boolean isIncremental;
	private boolean isGenerator;

	/**
	 * Initialize BVOutputDataWriterExtend Object
	 * @param aTargetFilePath
	 * @param anOutputMode
	 * @param name
	 * @param isIncremental
	 * @param isGenerator
	 * @throws Exception
	 */
	public BVOutputDataWriterExtend(String targetFilePath, String anOutputMode, String name, boolean isIncremental,
			boolean isGenerator) throws Exception {
		this.firstInvoke = true;
		this.groupName = "";
		this.name = name;
		this.isIncremental = isIncremental;
		this.targetFilePath = targetFilePath;
		this.isGenerator = isGenerator;
		if (anOutputMode.equalsIgnoreCase("CREATE")) {
			this.outputMode = 1;
			createNewDocument();
		} else {
			this.outputMode = 0;
			getExistingDocument();
		}
	}

	public boolean isCreate() {
		return outputMode == 1;
	}

	/**
	 * Create new Document with root Element as Feed
	 * @throws ParserConfigurationException
	 */
	public void createNewDocument() throws ParserConfigurationException {
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
		document = documentBuilder.newDocument();

		// root element
		rootElement = document.createElement(XML_ELEMENT_FEED);
		document.appendChild(rootElement);

		// set an attribute to root node
		rootElement.setAttribute("xmlns", "http://www.bazaarvoice.com/xs/PRR/ProductFeed/14.7");
		rootElement.setAttribute("name", name);
		rootElement.setAttribute("incremental", String.valueOf(isIncremental));
		if (isGenerator)
			rootElement.setAttribute("generator", GENERATOR);
		
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
		String dateString = now.format(format);
		dateString = dateString.replace('_', 'T');

		rootElement.setAttribute("extractDate", dateString);
	}

	/**
	 * Get Exiting XML file and parse it to Document Object.
	 * To add the Product Data in parsed Document Object.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public void getExistingDocument() throws ParserConfigurationException, SAXException, IOException {
		File xmlFile = new File(targetFilePath);
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		document = documentBuilder.parse(xmlFile);
	}

	/**
	 * 
	 * @param hMap
	 */
	public void write(HashMap hMap) {
		String METHODNAME = "write";
		if (firstInvoke) {
			if (LOGGER.isLoggable(Level.FINER)) 
				LOGGER.logp(Level.FINER, CLASSNAME, METHODNAME, (new StringBuilder("firstInvoke:::::::")).append(firstInvoke).append(":: hMap:::").append(hMap).toString());
			if (hMap.get(PRODUCT_PAGE_URL) != null) {
				// Products element
				rootElement = (Element) document.getElementsByTagName(XML_ELEMENT_FEED).item(0);
				productsElement = document.createElement(XML_ELEMENT_PRODUCTS);
				rootElement.appendChild(productsElement);
				firstInvoke = false;

			} else if (hMap.get(CATEGORY_PAGE_URL) != null) {
				// Categories element
				categoriesElement = document.createElement(XML_ELEMENT_CATEGORIES);
				rootElement.appendChild(categoriesElement);
				firstInvoke = false;
			}
		}
		if (hMap.get(PRODUCT_PAGE_URL) != null)
			writeProduct(hMap);
		else if (hMap.get(CATEGORY_PAGE_URL) != null)
			writeCategory(hMap);
	}

	/**
	 * Write a Category Element under Categories Element
	 * @param hMap
	 */
	protected void writeCategory(HashMap hMap) {
		// Category element
		Element category = document.createElement(XML_ELEMENT_CATEGORY);
		categoriesElement.appendChild(category);

		if (hMap.get(NAME) != null) {
			// Name element
			Element name = document.createElement(XML_ELEMENT_NAME);
			name.appendChild(document.createTextNode((String) hMap.get(NAME)));
			category.appendChild(name);
		}

		if (hMap.get(CATGROUP_ID) != null) {
			// ExternalId element
			Element externalId = document.createElement(XML_ELEMENT_EXTERNAL_ID);
			externalId.appendChild(document.createTextNode((String) hMap.get(CATGROUP_ID)));
			category.appendChild(externalId);
		}

		if (hMap.get(PARENT_CATGROUP_ID) != null) {
			// ParentExternalId element
			Element parentExternalId = document.createElement(XML_ELEMENT_PARENT_EXTERNAL_ID);
			parentExternalId.appendChild(document.createTextNode((String) hMap.get(PARENT_CATGROUP_ID)));
			category.appendChild(parentExternalId);
		}

		if (hMap.get(CATEGORY_PAGE_URL) != null) {
			// CategoryPageUrl element
			Element categoryPageUrl = document.createElement(XML_ELEMENT_CATEGORY_PAGE_URL);
			categoryPageUrl.appendChild(document.createTextNode((String) hMap.get(CATEGORY_PAGE_URL)));
			category.appendChild(categoryPageUrl);
		}

		if (hMap.get(IMAGE_URL) != null) {
			// ImageUrl element
			Element imageUrl = document.createElement(XML_ELEMENT_IMAGE_URL);
			imageUrl.appendChild(document.createTextNode((String) hMap.get(IMAGE_URL)));
			category.appendChild(imageUrl);
		}

		if (hMap.get(CATEGORY_PAGE_URL) != null) {
			// CategoryPageUrls element
			Element categoryPageUrls = document.createElement(XML_ELEMENT_CATEGORY_PAGE_URLS);
			category.appendChild(categoryPageUrls);

			Element categoryPageUrl = document.createElement(XML_ELEMENT_CATEGORY_PAGE_URL);
			categoryPageUrl.appendChild(document.createTextNode((String) hMap.get(CATEGORY_PAGE_URL)));
			categoryPageUrl.setAttribute(XML_ELEMENT_ATTR_LOCAL, XML_ELEMENT_ATTR_VALUE_LOCAL);
			categoryPageUrls.appendChild(categoryPageUrl);
		}

		if (hMap.get(IMAGE_URL) != null) {
			// ImageUrls element
			Element imageUrls = document.createElement(XML_ELEMENT_IMAGE_URLS);
			category.appendChild(imageUrls);

			Element imageUrl = document.createElement(XML_ELEMENT_IMAGE_URL);
			imageUrl.appendChild(document.createTextNode((String) hMap.get(IMAGE_URL)));
			imageUrl.setAttribute(XML_ELEMENT_ATTR_LOCAL, XML_ELEMENT_ATTR_VALUE_LOCAL);
			imageUrls.appendChild(imageUrl);
		}
	}

	/**
	 * Write a Product Element under Products Element
	 * @param hMap
	 */
	protected void writeProduct(HashMap hMap) {
		// Product element
		Element product = document.createElement(XML_ELEMENT_PRODUCT);
		productsElement.appendChild(product);

		if (hMap.get(CATENTRY_ID) != null) {
			// ExternalId element
			Element externalId = document.createElement(XML_ELEMENT_EXTERNAL_ID);
			externalId.appendChild(document.createTextNode((String) hMap.get(CATENTRY_ID)));
			product.appendChild(externalId);
		}

		if (hMap.get(CATGROUP_ID) != null) {
			// CategoryExternalId element
			Element categoryExternalId = document.createElement(XML_ELEMENT_CATEGORY_EXTERNAL_ID);
			categoryExternalId.appendChild(document.createTextNode((String) hMap.get(CATGROUP_ID)));
			product.appendChild(categoryExternalId);
		}

		if (hMap.get(PARTNUMBER) != null) {
			// ManufacturerPartNumbers element
			Element manufacturerPartNumbers = document.createElement(XML_ELEMENT_MANUFACTURER_PART_NUMBERS);
			product.appendChild(manufacturerPartNumbers);

			// ManufacturerPartNumber element
			Element manufacturerPartNumber = document.createElement(XML_ELEMENT_MANUFACTURER_PART_NUMBER);
			manufacturerPartNumber.appendChild(document.createTextNode((String) hMap.get(PARTNUMBER)));
			manufacturerPartNumbers.appendChild(manufacturerPartNumber);
		}

		if (hMap.get(NAME) != null) {
			// Name element
			Element name = document.createElement(XML_ELEMENT_NAME);
			name.appendChild(document.createTextNode((String) hMap.get(NAME)));
			product.appendChild(name);
		}

		if (hMap.get(PRODUCT_PAGE_URL) != null) {
			// ProductPageUrl element
			Element productPageUrl = document.createElement(XML_ELEMENT_PRODUCT_PAGE_URL);
			productPageUrl.appendChild(document.createTextNode((String) hMap.get(PRODUCT_PAGE_URL)));
			product.appendChild(productPageUrl);
		}

		if (hMap.get(IMAGE_URL) != null) {
			// ImageUrl element
			Element imageUrl = document.createElement(XML_ELEMENT_IMAGE_URL);
			imageUrl.appendChild(document.createTextNode((String) hMap.get(IMAGE_URL)));
			product.appendChild(imageUrl);
		}

		if (hMap.get(NAME) != null) {
			// Names element
			Element names = document.createElement(XML_ELEMENT_NAMES);
			product.appendChild(names);

			// Name element
			Element name = document.createElement(XML_ELEMENT_NAME);
			name.appendChild(document.createTextNode((String) hMap.get(NAME)));
			name.setAttribute(XML_ELEMENT_ATTR_LOCAL, XML_ELEMENT_ATTR_VALUE_LOCAL);
			names.appendChild(name);
		}

		if (hMap.get(SHORTDESCRIPTION) != null) {
			// Descriptions element
			Element descriptions = document.createElement(XML_ELEMENT_DESCRIPTIONS);
			product.appendChild(descriptions);

			// Description element
			Element description = document.createElement(XML_ELEMENT_DESCRIPTION);
			description.appendChild(document.createTextNode((String) hMap.get(SHORTDESCRIPTION)));
			description.setAttribute(XML_ELEMENT_ATTR_LOCAL, XML_ELEMENT_ATTR_VALUE_LOCAL);
			descriptions.appendChild(description);
		}

		if (hMap.get(PRODUCT_PAGE_URL) != null) {
			// ProductPageUrls element
			Element productPageUrls = document.createElement(XML_ELEMENT_PRODUCT_PAGE_URLS);
			product.appendChild(productPageUrls);

			// ProductPageUrl element
			Element productPageUrl = document.createElement(XML_ELEMENT_PRODUCT_PAGE_URL);
			productPageUrl.appendChild(document.createTextNode((String) hMap.get(PRODUCT_PAGE_URL)));
			productPageUrl.setAttribute(XML_ELEMENT_ATTR_LOCAL, XML_ELEMENT_ATTR_VALUE_LOCAL);
			productPageUrls.appendChild(productPageUrl);
		}

		if (hMap.get(IMAGE_URL) != null) {
			// ImageUrls element
			Element imageUrls = document.createElement(XML_ELEMENT_IMAGE_URLS);
			product.appendChild(imageUrls);

			// ImageUrl element
			Element imageUrl = document.createElement(XML_ELEMENT_IMAGE_URL);
			imageUrl.appendChild(document.createTextNode((String) hMap.get(IMAGE_URL)));
			imageUrl.setAttribute(XML_ELEMENT_ATTR_LOCAL, XML_ELEMENT_ATTR_VALUE_LOCAL);
			imageUrls.appendChild(imageUrl);
		}
	}
	
	/**
	 * Create the XML file
	 * @throws TransformerException
	 */
	public void saveAndcloseDataWriter() throws TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource domSource = new DOMSource(document);
		StreamResult streamResult = new StreamResult(new File(targetFilePath));
		transformer.transform(domSource, streamResult);
	}

}
