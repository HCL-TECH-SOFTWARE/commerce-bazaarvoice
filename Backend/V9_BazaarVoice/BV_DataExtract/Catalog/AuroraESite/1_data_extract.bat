@echo off 

:: Copyright [2021] [HCL America, Inc.]


Rem Commerce Data Extract Configuration
set COMMERCE_DATA_EXTRACT_BAT_PATH=C:\WCDE_V9\bin\dataextract.bat
Rem Example => set COMMERCE_DATA_EXTRACT_BAT_PATH=C:\WCDE_V9\bin\dataextract.bat
set COMMERCE_WC_DATA_EXTRACT_XML_PATH=C:\WCDE_V9\BV_DataExtract\Catalog\AuroraESite\wc-dataextract.xml
Rem Example => set COMMERCE_WC_DATA_EXTRACT_XML_PATH=C:\WCDE_V9\BV_DataExtract\Catalog\AuroraESite\wc-dataextract.xml


echo #########################################################################################################################################
echo ##########################   Step 1 : Create BazaarVoice XML From Commerce DB (Using Data Extract)
echo #########################################################################################################################################
 
%COMMERCE_DATA_EXTRACT_BAT_PATH% %COMMERCE_WC_DATA_EXTRACT_XML_PATH%