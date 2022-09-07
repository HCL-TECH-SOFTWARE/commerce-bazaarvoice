@echo off 

:: Copyright [2021] [HCL America, Inc.]


call 1_data_extract.bat
rem pause


call 2_upload_XML_to_bv_SFTP.bat
rem pause