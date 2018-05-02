#!/bin/bash

java -jar node.jar --layerNumber=1 --nodeName=R --port=6010 --nodeType=R --nextLayerNodePort=6011 --nextRouterNodePort=6020&
java -jar node.jar --layerNumber=2 --nodeName=R --port=6020 --nodeType=R --nextLayerNodePort=6021 --nextRouterNodePort=6010&

java -jar node.jar --layerNumber=1 --nodeName=A --port=6011 --nodeType=L --nextLayerNodePort=6012&
java -jar node.jar --layerNumber=1 --nodeName=B --port=6012 --nodeType=L --nextLayerNodePort=6010&

java -jar node.jar --layerNumber=2 --nodeName=A --port=6021 --nodeType=L --nextLayerNodePort=6022&
java -jar node.jar --layerNumber=2 --nodeName=B --port=6022 --nodeType=L --nextLayerNodePort=6020&


