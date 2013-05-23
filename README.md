# GlassFish v3 ClickStack

Glassfish v3 ClickStack for CloudBees PaaS. Deploy any Servlet2.x/3.x/JSP/JAX-RS app on GlassFish v3. 

# Pre-requisite

* OpenJDK 6
* Bash shell

# Build 

    $ make package

After successful build glassfish3-plugin.zip is uploaded in to RUN platform and assigned 'glassfish3' plugin name.


# Package and Deploy Sample App

## Create application war file
    $ cd example/hello
    $ jar cvf ../../hello.war .
    $ cd ../..

## Deploy 

    $ bees app:deploy -a APP_ID -t glassfish3  hello.war

# TODOs

* Database resource injection
* Clustering, session store
* Lot more testing...