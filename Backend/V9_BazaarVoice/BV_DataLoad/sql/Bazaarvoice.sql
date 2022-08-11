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

-- Execute below queries if you are using SOLR for Elastic it is not required
INSERT INTO SRCHATTR (SRCHATTR_ID, INDEXSCOPE, INDEXTYPE, IDENTIFIER) VALUES (101, '0', 'CatalogEntry', '_cat.X_field3_d');
INSERT INTO SRCHATTRPROP (SRCHATTR_ID, PROPERTYNAME, PROPERTYVALUE) values (101, 'facet', 'x_field3_d:[5.0 TO 6.0};[4.0 TO 5.0};[3.0 TO 4.0};[2.0 TO 3.0};[1.0 TO 2.0}');
INSERT INTO FACET (FACET_ID, SRCHATTR_ID, SELECTION, KEYWORD_SEARCH, STOREENT_ID, MAX_DISPLAY) VALUES (101, 101, 1, 1, 1, 5);