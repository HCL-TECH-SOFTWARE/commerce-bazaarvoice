@echo off 

:: Copyright [2021] [HCL America, Inc.]


Rem BV SFTP Configuration
set BV_PRODUCT_FEED_PATH=C:\WCDE_V9\BV_DataExtract\Catalog\AuroraESite\Extracted_Data\<bvaccount>_product_feed.xml
set BV_USERNAME=<bvaccount>
set BV_SFTP_HOSTNAME=sftp-stg.bazaarvoice.com


echo #########################################################################################################################################
echo ##########################   Step 2 : Connect to BV SFTP and Upload Generated BazaarVoice XML on SFTP
echo #########################################################################################################################################

scp %BV_PRODUCT_FEED_PATH% %BV_USERNAME%@%BV_SFTP_HOSTNAME%:import-inbox
