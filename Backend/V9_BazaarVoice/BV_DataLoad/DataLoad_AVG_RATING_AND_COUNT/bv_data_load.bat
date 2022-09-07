@echo off 

:: Copyright [2021] [HCL America, Inc.]

call 1_Download_Extract_Feed_from_BV_SFTP
rem pause


call 2_data_load.bat
rem pause