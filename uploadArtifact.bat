@echo off
setlocal


set REPOSITORY=internal
set REPOSITORYID=internal

set GROUPID=com.rsmaxwell.utilities
set ARTIFACTID=utilities
set VERSION=0.0.1
set PACKAGING=jar

set URL=http://192.168.5.35/archiva/repository/%REPOSITORY%

set FILENAME=%ARTIFACTID%-%VERSION%.%PACKAGING%

set ROOT=C:\Users\Richard\git\github.com\rsmaxwell\utilities
cd %ROOT%\target

@echo on

mvn deploy:deploy-file -DgroupId=%GROUPID% -DartifactId=%ARTIFACTID% -Dversion=%VERSION% -Dpackaging=%PACKAGING% -Dfile=%FILENAME% -DrepositoryId=%REPOSITORYID% -Durl=%URL% -DrepositoryId=%REPOSITORYID%

