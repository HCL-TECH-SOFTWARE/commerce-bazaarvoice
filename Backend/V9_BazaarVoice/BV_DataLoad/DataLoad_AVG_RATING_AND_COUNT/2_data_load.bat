@echo off 

:: Copyright [2021] [HCL America, Inc.]

Rem Commerce Data Extract Configuration
set COMMERCE_DATA_LOAD_BAT_PATH=C:\WCDE_V9\bin\dataload.bat
Rem Example => set COMMERCE_DATA_LOAD_BAT_PATH=C:\WCDE_V9\bin\dataextract.bat
set COMMERCE_WC_DATA_LOAD_XML_PATH=C:\WCDE_V9\BV_DataLoad\DataLoad_AVG_RATING_AND_COUNT\wc-dataload.xml
Rem Example => set COMMERCE_WC_DATA_LOAD_XML_PATH=C:\WCDE_V9\DataLoad\DataLoad_AVG_RATING_AND_COUNT\wc-dataload.xml


echo #########################################################################################################################################
echo ##########################   Step 3 : Load BazaarVoice FEED XML in Commerce DB (Using Data Load)
echo #########################################################################################################################################
 
%COMMERCE_DATA_LOAD_BAT_PATH% %COMMERCE_WC_DATA_LOAD_XML_PATH%