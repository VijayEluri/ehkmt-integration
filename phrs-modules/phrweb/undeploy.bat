cls

tomcat_home="/lab0/apache/tomcat/apache-tomcat-6.0.35/"

set tomcat_home="D:\tomcat\apache-tomcat-6.0.20"
call mvn  cargo:undeploy  -Dcatalina.home=%tomcat_home%

echo ""
echo "The phrweb is undeployed(removed) from web container located here :"%tomcat_home%
echo ""
