#!/bin/bash

# Set the variables for the keytool command
export ALIAS=server
export KEYALG=RSA
export KEYSIZE=4096
export STORETYPE=JKS
export KEYSTORE=springboot.jks
export VALIDITY=365

# Get the keystore password from the GitLab secret variable
export STOREPASS=$KEYSTORE_PASSWORD

# Generate a new key pair and store it directly in the keystore
keytool -genkey -alias $ALIAS -keyalg $KEYALG -keysize $KEYSIZE -keystore $KEYSTORE -storepass $STOREPASS -validity $VALIDITY -dname "CN=[Advanta], OU=[siemens-advanta], O=[siemens], L=[Bengaluru], ST=[Karnataka], C=[India]"
#keytool -genkeypair -alias springboot -keyalg RSA -keysize 4096 -storetype PKCS12 -keystore springboot.p12 -validity 365 -storepass changeit -dname "CN=[Advanta], OU=[siemens-advanta], O=[siemens], L=[Bengaluru], ST=[Karnataka], C=[India]"