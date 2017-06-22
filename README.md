# ECS Publisher 

Jenkins plugin to transfer dependency information to our ECS server. Find the solution at https://ecs-app.eacg.de

##Requirements

* Chart.js >= 2.6.0
* org.apache.httpcomponents.httpclient >=4.5.3
* org.apache.httpcomponents.fluent-hc >=4.5.3

## Supported plugins

* [eacg-gmbh/ecs-composer](https://github.com/eacg-gmbh/ecs-composer) version 1.0.1 +
* [ecs_bundler](https://github.com/eacg-gmbh/ecs-bundler) version 1.0.1 +

## Installation

There are two ways to install that plugin.

**First way** is installation from jenkins.

**That way is not available now since plugin is under beta testing.**

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
![](/doc/img/jenkins-upload-plugin.png)

## Usage

* First you need to setup global configuration for this plugin.
  
  **You can skip that step** if you want to save configuration under step definition.

  Go to "Manage Jenkins" and click "Configure System"
configure(/configure).
  
  ![](/doc/img/jenkins-global-config.png)

  Then find block called "ecspublisher", change configuration and click "save"

* Secondly you need go to project configuration
  - Add post-build action called "ecspublisher"
  - Type project name
    ![](/doc/img/jenkins-post-build-action-closed.png)
  - If your project has different structure you can specify paths to plugins
  - **You can override global configuration** just toggle checkboxes
    ![](/doc/img/jenkins-post-build-action-open.png)
  - Click "save"

After you add post-build action it will automatically create two pages ECS analysis for build and project.
![](/doc/img/jenkins-ecs-analysis.png)

## Development

This plugin can be built and started with maven and Jenkins hpi plugin:
Just run command:
```
./run
```
Jenkins will be available on port [8089](http://localhost:8089/jenkins)
