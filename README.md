#  Description
JWT Oauth Proxy leveraging Keycloak and based on the Quarkus runtime.

# Implementation details
The implementation of Restful services with the microprofile specification claim the use of an interface to declare the target service.
Here, this is a Proxy that should forward all the requests and it's not possible to know them in advance.
That's why the implementation doesn't make use of this microprofile specification.

# Issues
The thin jar can be used.
However I still have many issues with the native compilation.

# Use over Openshift
The application is aimed at being used as a Proxy sidecar on Openshift.
For this reason the code of the proxy is made to forward all the request to another container located behind "localhost".

# Build
mvnw clean package
mvnw quarkus:dev

# Openshift Build & Deploy
The project can be deployed to openshift using the templates located in the /template directory.

The "quarkus-bc-tmpl.yml" template creates a build config and an image stream

sh "oc process openidconnect-quarkus-buildconfig-template -n ${namespace} --param IMAGE_VERSION=latest"
sh "oc start-build openidconnect-gateway-quarkus-s2i --from-dir ./target"

Bear in mind that there should be only one jar file in the target directory of the container while the maven build actually creates 2.
So I had to add the following commands to my Jenkins pipeline:
	sh "mv target/*-runner.jar ."
	sh "rm target/*.jar"
	sh "mv *-runner.jar target"

The "template-openidconnect-quarkus.yml" template creates the DeploymentConfig.


# SSL
There is a truststore in the /resources directory that is supposed to be mounted as a Secret.
With Quarkus, SSL requires an extra environment variable:
JAVA_OPTIONS='-Djavax.net.ssl.trustStore=/mnt/truststore/truststore-replace.jks -Djavax.net.ssl.trustStorePassword=xxx -Djavax.net.ssl.trustStoreType=jks'


