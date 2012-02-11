@echo off

if "%JAVA_HOME%" == "" goto noJavaHome
  "%JAVA_HOME%/bin/java" -cp %JAVA_HOME%/lib/tools.jar;glasswall.jar org.googlecode.glasswall.attach.AttachMain %*
  goto end
:noJavaHome
  echo Please set JAVA_HOME before running this script
  goto end
:end