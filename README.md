# TrustSource Plugin 4 Jenkins 

Jenkins plugin to transfer dependency information to TrustSource service (OpenSource Compliance & Vulnerabilities). It does not only allow transferring the dependencies and automated vulnerability assessment but also to warn or break builds depending on legal status. Find more about trustSource at https://app.trustsource.io

![some screenshot](https://github.com/eacg-gmbh/ecs-jenkins/blob/master/doc/img/jenkins-ecs-analysis.png)

## Requirements

* Chart.js >= 2.6.0
* org.apache.httpcomponents.httpclient >=4.5.3
* org.apache.httpcomponents.fluent-hc >=4.5.3

## Supported plugins

* [eacg-gmbh/ecs-composer](https://github.com/eacg-gmbh/ecs-composer) version 1.0.1 +
* [ecs_bundler](https://github.com/eacg-gmbh/ecs-bundler) version 1.0.1 +
* [ecs-node-client](https://github.com/eacg-gmbh/ecs-node-client) version 0.2.5+
* [ts-maven-plugin](https://github.com/eacg-gmbh/ecs-mvn-plugin) version 0.2.0+
* [ts-gradle-plugin](https://github.com/eacg-gmbh/ecs-gradle-plugin) version 0.2.0+ 

## Installation

There are two ways to install that plugin.

**First way** is installation from jenkins.

To do that go to plugin manager page on jenkins and click on "available" tab(*/pluginManager/available*).
Then find plugin by name "ecspublisher", toggle checkbox and click install.


**Second way** is installation from source code.
To do that run commands:
```
# Clone source code
git clone git@github.com:eacg-gmbh/ecs-jenkins.git
# Generate *.hpi package
mvn package
# package will be located at target/ecspublisher.hpi
```
Then go to plugin manager page on jenkins and click on "advanced" tab(*/pluginManager/advanced*).

Then find upload plugin block and choose *.hpi package and click upload.
<kbd><img src="/doc/img/jenkins-upload-plugin.png" alt="jenkins-upload-plugin" width="500px"></kbd>

## Usage

* First you need to setup global configuration for this plugin.
  
**You can skip that step** if you want to save configuration under step definition.

Go to "Manage Jenkins" and click "Configure System" configure(/configure).
  
<kbd><img src="/doc/img/jenkins-global-config.png" alt="jenkins-global-config" width="500px"></kbd>

Then find block called "ecspublisher", change configuration and click "save"

* Secondly you need go to project configuration
  - Add post-build action called "ecspublisher"
  - Type project name
    <kbd><img src="/doc/img/jenkins-post-build-action-closed.png" alt="jenkins-post-build-action-closed" width="500px"></kbd>
  - If your project has different structure you can specify paths to plugins
  - **You can override global configuration** just toggle checkboxes
    <kbd><img src="/doc/img/jenkins-post-build-action-open.png" alt="jenkins-post-build-action-open" width="500px"></kbd>
  - Click "save"

After you add post-build action it will automatically create two pages ECS analysis for build and project.
<kbd><img src="/doc/img/jenkins-ecs-analysis.png" alt="jenkins-ecs-analysis" width="500px"></kbd>

## Development

This plugin can be built and started with maven and Jenkins hpi plugin:
Just run command:
```
./run
```
Jenkins will be available on port [8089](http://localhost:8089/jenkins)

## Deploy
```
mvn release:prepare release:perform
```
