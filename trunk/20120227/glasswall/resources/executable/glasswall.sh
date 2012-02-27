#! /bin/sh
 
    if [ "${JAVA_HOME}" != "" ]; then
       case "`uname`" in
          Darwin*)
              # In Mac OS X, tools.jar is classes.jar and is kept in a 
              # different location. Check if we can locate classes.jar
              # based on ${JAVA_VERSION}
              TOOLS_JAR="/System/Library/Frameworks/JavaVM.framework/Versions/${JAVA_VERSION}/Classes/classes.jar"

              # if we can't find, try relative path from ${JAVA_HOME}. Usually,
              # /System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Home
              # is JAVA_HOME. (or whatever version beyond 1.6.0!)
              if [ ! -f ${TOOLS_JAR} ] ; then
                  TOOLS_JAR="${JAVA_HOME}/../Classes/classes.jar" 
              fi

              # If we still can't find, tell the user to set JAVA_VERSION.
              # This way, we can avoid zip file errors from the agent side.              
              if [ ! -f ${TOOLS_JAR} ] ; then
                  echo "Please set JAVA_VERSION to the target java version"
                  exit 1
              fi
          ;;
          *)
              TOOLS_JAR="${JAVA_HOME}/lib/tools.jar"
          ;;
       esac
       echo "USING JAVA_HOME=${JAVA_HOME}"
       
       PRG="$0"     
       while [ -h "$PRG" ] ; do   
           ls=`ls -ld "$PRG"`   
           link=`expr "$ls" : '.*-> \(.*\)$'`    
           if expr "$link" : '/.*' > /dev/null; then   
               PRG="$link"  
           else  
               PRG=`dirname "$PRG"`/"$link"   
           fi  
       done         
       PRGDIR=`dirname "$PRG"`
       echo "The executable is in ${PRGDIR}"
       
       ${JAVA_HOME}/bin/java -cp ${TOOLS_JAR}:${PRGDIR}/glasswall.jar org.googlecode.glasswall.attach.AttachMain $*
    else
       echo "Please set JAVA_HOME before running this script"
       exit 1
    fi
 
 
 
