 # Glassfish3 ClickStack plugin
#
# TODO: SSL port?

plugin_name = glassfish3-full-plugin
publish_bucket = cloudbees-clickstack
publish_repo = testing
publish_url = s3://$(publish_bucket)/$(publish_repo)/

deps = lib lib/glassfish.zip lib/jmxtrans-agent.jar java

pkg_files = README.md LICENSE setup functions control server lib java

include plugin.mk

glassfish_ver = 3.1.2.2
glassfish_src = "http://download.java.net/glassfish/$(glassfish_ver)/release/glassfish-$(glassfish_ver).zip"
glassfish_src_md5 = ae8e17e9dcc80117cb4b39284302763f

lib:
	mkdir -p lib
	
lib/glassfish.zip: lib/genapp-setup-glassfish3.jar
	mkdir -p lib
	curl -fLo lib/glassfish.zip "$(glassfish_src)"
	echo "$(glassfish_src_md5)  lib/glassfish.zip" | md5sum --check

JAVA_SOURCES := $(shell find genapp-setup-glassfish3/src -name "*.java")
JAVA_JARS = $(shell find genapp-setup-glassfish3/target -name "*.jar")

lib/genapp-setup-glassfish3.jar: $(JAVA_SOURCES) $(JAVA_JARS) lib
	cd genapp-setup-glassfish3; \
	mvn -q clean test assembly:single; \
	cd target; \
	cp genapp-setup-glassfish3-*-jar-with-dependencies.jar \
	$(CURDIR)/lib/genapp-setup-glassfish3.jar

jmxtrans_agent_ver = 1.0.1
# jmxtrans_agent_url = http://repo1.maven.org/maven2/org/jmxtrans/agent/jmxtrans-agent/$(jmxtrans_agent_ver)/jmxtrans-agent-$(jmxtrans_agent_ver).jar
# jmxtrans_agent_md5 = d568995c11baad912919dbbb0783934e
jmxtrans_agent_url = https://jmxtrans.ci.cloudbees.com/job/jmxtrans-agent/lastSuccessfulBuild/artifact/target/jmxtrans-agent-1.0.2-SNAPSHOT.jar
jmxtrans_agent_md5 = b4b364d57e9de163b16ea915933c97fa

lib/jmxtrans-agent.jar:
	mkdir -p lib
	curl -fLo lib/jmxtrans-agent.jar "$(jmxtrans_agent_url)"
	$(call check-md5,lib/jmxtrans-agent.jar,$(jmxtrans_agent_md5))

java_plugin_gitrepo = git://github.com/CloudBees-community/java-clickstack.git

java:
	git clone $(java_plugin_gitrepo) java
	rm -rf java/.git
	cd java; make clean; make deps

hello.war:
	cd example/hello; zip -r ../../hello.war .

clean:
	rm -f hello.war
