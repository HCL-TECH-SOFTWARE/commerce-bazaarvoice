@echo off 

:: Copyright [2021] [HCL Technologies]

:: Licensed under the Apache License, Version 2.0 (the "License");
:: you may not use this file except in compliance with the License.
:: You may obtain a copy of the License at

::    http://www.apache.org/licenses/LICENSE-2.0

:: Unless required by applicable law or agreed to in writing, software
:: distributed under the License is distributed on an "AS IS" BASIS,
:: WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
:: See the License for the specific language governing permissions and
:: limitations under the License. 

Rem BV SFTP Configuration
set BV_PRODUCT_FEED_PATH=C:\WCDE_V9\BV_DataExtract\Catalog\Emerald\Extracted_Data\<bvaccount>_product_feed.xml
set BV_USERNAME=<bvaccount>
set BV_SFTP_HOSTNAME=sftp-stg.bazaarvoice.com


echo #########################################################################################################################################
echo ##########################   Step 2 : Connect to BV SFTP and Upload Generated BazaarVoice XML on SFTP
echo #########################################################################################################################################

scp %BV_PRODUCT_FEED_PATH% %BV_USERNAME%@%BV_SFTP_HOSTNAME%:import-inbox
