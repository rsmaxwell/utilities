@echo off
setlocal


set URL=https://server.rsmaxwell.co.uk/archiva
set REPOSITORY=internal
set PACKAGING=jar
set GROUPID=com.rsmaxwell.utilities
set ARTIFACTID=utilities
set VERSION=8

@echo on
call mvn versions:dependency:get -DgroupId=%GROUPID:.=/% -DartifactId=%ARTIFACTID% -Dversion=%VERSION% -Dpackaging=%PACKAGING% -DremoteRepositories=%URL%
