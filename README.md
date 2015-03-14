## Introduction
As a java developer/tester, __have you been trying to find a "Servlet Appender"__ which prints log entries on the page so that you don't have to bother to get the log file on the server?

__Glasswall__ is what you are looking for. It shows log messages produced by the application as if you were watching the running application through a glasswall. Take a look:

![screenshot](/glasswall/doc/ad/glasswall-usage.png?raw=true)

While Glasswall does show log messages, it's not really a "logging appender". It doesn't require any log configuration (log4j.xml, etc) and it is much less invasive or not invasive at all sometimes. Finally,its architecture is not limited to logging and is extensible to show any other kind of messages

## Getting Started

* Download Glasswall at http://code.google.com/p/glasswall/downloads/list
* Extract it:
  * linux/mac: `tar -xvf glasswall-x.x.x.tar.gz`
  * windows: extract it with 7zip or some tool you like
* Run it with your web application (Take Tomcat as example)
  * You can run it while you start up your web application. Add the following to your catalina.sh (catalina.bat for Windows)
CATALINA_OPTS=-javaagent:somewhere/glasswall.jar
  * Or you can run it after your web application has been started (only for Sun Java >=6, other vendors' JDKs(>=6) may also work if you are lucky)
    * Linux: chmod +x glasswall.sh
    * find the java process-id of your tomcat
      * Linux: ps a|grep catalina or jps
      * Widows:jps
    * start printing log entries
      * Linux: ./glasswall.sh <pid>
      * Windows: glasswall.bat <pid>
    * stop printing log entries
      * Linux: ./glasswall.sh <pid> silent
      * Windows: glasswall.bat <pid> silent

## Features & Limitations

### Usage
Glasswall can be useful when you are developing or testing a web application. It exposes the logger entries produced by the system, which can benefit you significantly in some cases. Without Glasswall, you'll have to get the log file from your server, which could be tricky, especially when there is clustering.
### Supported Logging Frameworks
Glasswall currently supports log4j , java.util.logging and logback, the 3 most popular logging frameworks. Supports for other logging frameworks can be enabled in the future per your needs.
### No invasion
As Section "Getting Started" shows, there is no invasion to your application; There is even no invasion to your application's start-up script if you choose to "Run Glasswall after the application has been started"
### Switching off
What if you don't want to see log output any longer after having turned on Glasswall some while ago? You can simply turn it off
./glasswall.sh <pid> silent
Note that it will still work if Glasswall was originally started with the startup of the web application.
And of course, you can turn it on again!
./glasswall.sh <pid>
### Supporting all Operating Systems
Glasswall is a pure-java program and can be run in all operating systems which ships JDK.
### Java 5 Required
Glasswall is build upon Java Instrumentation API, which is not brought into Java until Java 5. With Java 5, you can start Glasswall while starting up your web application (the "-javaagent" mode). However, with java 5 you can't start it after the web application has been started. See next section for the reason.
### Sun JDK & Java 6 Needed for Invocation-on-Demand
If you want to start Glasswall after the web application has been started, you'll need at least Java6 and, normally, Sun's JDK. That's because this feature leverages Java Attach API, which only exists in Java >=6 and is declared as a SUN-specific extension (Some versions of IBM JDK do work, however)
Support for Non-Logging Outputs
Glasswall's architecture is not limited to logging frameworks. It's a simple Message Provider/Consumer model. Currently providers are Loggers, but they can be any java module. Let's see what we will have in the future.
### Warning
Please never apply Glasswall to production systems. Your users do not expect to see logs in the browser, although hackers may like it.
### Acknowledgments
I got this idea from a company I used to work in (Let's say e******). This indeed is a great idea.
I was inspired by BTrace while trying to make the tool kind-of zero-invasion. It's BTrace that reminds me of Java Attach API.
