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

Rem Commerce Data Extract Configuration
set COMMERCE_DATA_LOAD_BAT_PATH=C:\WCDE_V9\bin\dataload.bat
Rem Example => set COMMERCE_DATA_LOAD_BAT_PATH=C:\WCDE_V9\bin\dataextract.bat
set COMMERCE_WC_DATA_LOAD_XML_PATH=C:\WCDE_V9\BV_DataLoad\DataLoad_AVG_RATING_AND_COUNT\wc-dataload.xml
Rem Example => set COMMERCE_WC_DATA_LOAD_XML_PATH=C:\WCDE_V9\DataLoad\DataLoad_AVG_RATING_AND_COUNT\wc-dataload.xml


echo #########################################################################################################################################
echo ##########################   Step 3 : Load BazaarVoice FEED XML in Commerce DB (Using Data Load)
echo #########################################################################################################################################
 
%COMMERCE_DATA_LOAD_BAT_PATH% %COMMERCE_WC_DATA_LOAD_XML_PATH%