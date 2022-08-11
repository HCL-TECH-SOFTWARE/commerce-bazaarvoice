/*
 * Licensed Materials - Property of HCL Technologies Limited. (c) Copyright HCL Technologies Limited 1996, 2020.
 */

import java.time.Instant

import org.apache.nifi.annotation.behavior.SideEffectFree
import org.apache.nifi.annotation.documentation.CapabilityDescription
import org.apache.nifi.components.PropertyDescriptor
import org.apache.nifi.flowfile.FlowFile
import org.apache.nifi.processor.AbstractProcessor
import org.apache.nifi.processor.ProcessContext
import org.apache.nifi.processor.ProcessSession
import org.apache.nifi.processor.Relationship
import org.apache.nifi.processor.exception.ProcessException
import org.apache.nifi.processor.io.OutputStreamCallback
import org.apache.nifi.processor.util.StandardValidators

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

@SideEffectFree
@CapabilityDescription("... short description of this custom processor ...")
final class CustomConnectorPipeProcessor extends AbstractProcessor {

	final Relationship RELATIONSHIP_SUCCESS = new Relationship.Builder().name("success")
	.description("The flow file with the specified content was successfully transferred").build();

	final Relationship RELATIONSHIP_FAILURE = new Relationship.Builder().name("failure")
	.description("The flow file with the specified content has encountered an error during the transfer").build();

	final Relationship RELATIONSHIP_UPDATE = new Relationship.Builder().name("update")
	.description("The flow file with the specified content for updating was successfully transferred").build();

	final Relationship RELATIONSHIP_INVALID = new Relationship.Builder().name("invalid")
	.description("The flow file contains invalid content").build();

	final PropertyDescriptor DELAY_COMPLETION = new PropertyDescriptor.Builder().name("Delay Completion")
	.description("Amount of time in milliseconds to wait for incoming flowfiles and delay the completion of this processor.")
	.required(true).addValidator(StandardValidators.NON_EMPTY_VALIDATOR).addValidator(StandardValidators.INTEGER_VALIDATOR)
	.expressionLanguageSupported(true).build();

	boolean isTransferred = false;

	@Override
	final Set<Relationship> getRelationships() {
		return [RELATIONSHIP_SUCCESS, RELATIONSHIP_FAILURE, RELATIONSHIP_UPDATE, RELATIONSHIP_INVALID] as Set;
	}

	@Override
	final void onTrigger(final ProcessContext context, final ProcessSession session) throws ProcessException {

		FlowFile lastFlowFile = null;
		FlowFile flowFile = null;

		// Amount of time to wait before completing
		final String delay = context.getProperty(DELAY_COMPLETION).evaluateAttributeExpressions().getValue();

		while (true) {

			isTransferred = false;
			flowFile = session.get();
			if (flowFile == null) {
				// Wait for 100 more milliseconds to ensure everything has been completed
				System.sleep(Long.valueOf(delay));
				flowFile = session.get();
				if (flowFile == null) {
					break;
				}
			}

			try {

				// Retrieve incoming flow file
				final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				session.exportTo(flowFile, bytes);
				bytes.close();

				// Parse JSON content in flow file
				final String contents = bytes.toString();
				final Map json = new JsonSlurper().parseText(contents);

				// Resulting document
				final Map document = new HashMap();
				final String environment = flowFile.getAttribute("environment.name");
				final String time = flowFile.getAttribute("time.id");
				final String store = flowFile.getAttribute("param.storeId");
				final String index = "." + environment.toLowerCase() + "." + store + ".product." + time;
				final String catalog = flowFile.getAttribute("param.catalogId");
				final String masterCatalog = flowFile.getAttribute("master.catalog");
				final String language = flowFile.getAttribute("param.langId");

				// Update last modified timestamp
				final Map meta = new HashMap();
				meta.put("modified", Instant.now().toString());
				document.put("__meta", meta);

				// ------------------------------------------------
				// Add custom logic in Apache Groovy language below
				// ------------------------------------------------

				final String CATENTRY_ID = json.get("CATENTRY_ID");
				// id.catentry
				final String catentry = CATENTRY_ID.trim();

				
				final String FIELD1 = json.get("FIELD1");
				final String FIELD3 = json.get("FIELD3");
				final Map p = new HashMap();
				if(FIELD1 != null && FIELD3 != null) {
					p.put("X_FIELD1", Integer.valueOf(FIELD1.trim()));
					p.put("X_FIELD3", Double.valueOf(FIELD3.trim()));
					document.put("BVcustom", p);
				}
				

				// Skip flow file based on master catalog
				if(!masterCatalog.matches(catalog)){
					 final StringBuilder skipMessage = new StringBuilder();
					 skipMessage.append("{ \"message\": \"CustomConnectorPipeProcessor skipped flowFile\" }");
					 flowFile = session.write(flowFile, new OutputStreamCallback() {
						 @Override
						 public void process(final OutputStream outputStream) throws IOException {
							 outputStream.write(skipMessage.toString().getBytes());
							 outputStream.flush();
						 } 
					}); 
					lastFlowFile = flowFile;
					session.transfer(flowFile, RELATIONSHIP_SUCCESS);
					continue;
				}
								
				
				// id = store + language + catalog + parent
				final StringBuilder id = new StringBuilder().append(store).append("-")
					.append(language).append("-").append(catalog).append("-").append(catentry);
				final StringBuilder output = new StringBuilder();
				//	{
				//		"update": {
				//		  "_id": "1--1-10502-12874",
				//		  "retry_on_conflict": 5,
				//		  "_source": false
				//		}
				//	}
				output.append("{ \"update\": { \"_id\": \"").append(id)
				    .append("\", \"_index\": \"").append(index)
					.append("\", \"retry_on_conflict\": 5, \"_source\": false } }\n");
				//	{
				//		"doc": {
				//		  .....  your custom Json document goes in here
				//		}
				//	}
				final JsonBuilder builder = new JsonBuilder(document);
				output.append("{ \"doc\": ").append(builder.toString()).append(" }\n");

				// Forward updated flow file to Elasticsearch _bulk endpoint for indexing
				flowFile = session.write(flowFile, new OutputStreamCallback() {
							@Override
							public void process(final OutputStream outputStream) throws IOException {
								outputStream.write(output.toString().getBytes());
								outputStream.flush();
							}
						});

				// Persist to provenance
				lastFlowFile = flowFile;
				session.getProvenanceReporter().modifyContent(flowFile);
				session.transfer(flowFile, RELATIONSHIP_UPDATE);

			} catch (Throwable e) {

				if (! isTransferred) {
					final String message = "Unhandled error encountered: " + e.getMessage();
					session.putAttribute(flowFile, "logger.message.cause", message);
					session.transfer(flowFile, RELATIONSHIP_FAILURE);
				}
			}
		}

		// Reach the end of queue and nothing else to be processed
		FlowFile successFlowFile = (lastFlowFile != null) ? session.create(lastFlowFile) : session.create();
		final StringBuilder message = new StringBuilder();
		message.append("{ \"message\": \"CustomConnectorPipeProcessor completed successfully\" }");
		successFlowFile = session.write(successFlowFile, new OutputStreamCallback() {
					@Override
					public void process(final OutputStream outputStream) throws IOException {
						outputStream.write(message.toString().getBytes());
						outputStream.flush();
					}
				});
		session.transfer(successFlowFile, RELATIONSHIP_SUCCESS);
	}
}

processor = new CustomConnectorPipeProcessor();
