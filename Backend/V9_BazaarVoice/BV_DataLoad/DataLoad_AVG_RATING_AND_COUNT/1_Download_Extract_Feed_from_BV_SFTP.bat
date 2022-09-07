@echo off 

:: Copyright [2021] [HCL America, Inc.]

Rem BV SFTP Configuration
set BV_USERNAME=<bvaccount>
set BV_SFTP_HOSTNAME=sftp-stg.bazaarvoice.com
Rem 7 Zip should be installed to extract .gz file
set SEVENTZ_EXE_PATH=C:\Program Files\7-Zip\7z.exe


echo #########################################################################################################################################
echo ##########################   Step 1 : Connect to BV SFTP and Download Generated BazaarVoice FEED file from SFTP
echo #########################################################################################################################################

scp %BV_USERNAME%@%BV_SFTP_HOSTNAME%:feeds\bv_<bvaccount>_ratings.xml.gz bvFeeds\

echo #########################################################################################################################################
echo ##########################   Step 2 : Extract the BV FEED file (.gz file)
echo #########################################################################################################################################

"%SEVENTZ_EXE_PATH%" x bvFeeds\bv_<bvaccount>_ratings.xml.gz -so > bvFeeds\bv_<bvaccount>_ratings.xml