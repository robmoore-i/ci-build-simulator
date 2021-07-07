# ci-build-simulator

This repository contains a 
[multi-project Gradle build](https://docs.gradle.org/current/userguide/multi_project_builds.html#sec:creating_multi_project_builds)
which can be used to seed a [Jenkins installation](https://github.com/robmoore-i/JenkinsEC2) with executed jobs and
their corresponding data.

## CodeDay Labs

This project exists mainly for use by my [CodeDay labs](https://labs.codeday.org) team to generate test data while they 
create a tool for [Developer Productivity](#developer-productivity) that could be used to view some basic analytics 
about builds.

## Tools and Technologies

The main technologies that program makes use of are Gradle, Kotlin, Groovy, Git, and Jenkins, and the conventions are
based on empathy and object-oriented thinking.

Please read these: 

- [Gradle - Key Concepts](https://docs.gradle.org/current/userguide/tutorial_using_tasks.html)
- [Gradle - Plugins](https://docs.gradle.org/current/userguide/plugins.html)
- [Design - Thinking with objects](http://c2.com/doc/oopsla89/paper.html)
- [Design - Developer Empathy](https://100daysdx.com/3/)

For Groovy, Kotlin, Git and Jenkins, Google is your friend.

## Usage

### Creating a simulation

A simulation is a self-perpetuating job which continuously creates builds. It would be pretty boring if the builds were
all the same, so in every run, the build modifies the source code on each run, so that subsequent builds are a bit
different, in a way that acts as a rudimentary simulation of developers making changes.

Correspondingly, there are two [Gradle plugins](https://docs.gradle.org/current/userguide/plugins.html),

- Jenkins
  [ci.build.simulator.jenkins](buildSrc/src/main/kotlin/ci/build/simulator/jenkins)
- Simulate Development
  [ci.build.simulator.simulate.development](buildSrc/src/main/kotlin/ci/build/simulator/simulate/development)
  
These two Gradle plugins configure some [Gradle tasks](https://docs.gradle.org/current/userguide/more_about_tasks.html) 
used for executing CI build simulations.

### Gradle plugin: Jenkins

Configures Gradle tasks for managing simulation jobs on Jenkins.

These tasks accept command line arguments as [project properties](https://docs.gradle.org/current/userguide/build_environment.html#sec:project_properties) 
using the `-P` flag.

Running `./gradlew :sleeper:createSimulationJob -Pbranch=simulation/1 -Purl=http://13.229.56.106:8080 -Puser=jenkins -Ppassword=secret`
will log into the Jenkins instance running at `http://13.229.56.106:8080` using the username `jenkins` and the password
`secret`, and create a simulation job for the git branch `simulation/1`. It also creates the branch remotely if it
doesn't already exist.

Running `./gradlew :sleeper:deleteSimulationJob -Pbranch=simulation/1 -Purl=http://13.229.56.106:8080 -Puser=jenkins -Ppassword=secret`
will use the same mechanism to delete the simulation job for this branch.

Simulations are Jenkins jobs with stages defined in the predefined, shared
[Jenkinsfile](buildSrc/src/main/resources/Jenkinsfile.groovy). In short, these Jenkins jobs will run the build, run the
development simulation Gradle task, and finish by triggering another build if needed. By default, simulations create 5
builds. You can change this by editing the Jenkinsfile on the simulation branch.

Putting in the URL, username and password for Jenkins all the time would be a chore, so instead you can put them in the
file `buildSrc/src/main/resources/jenkins.properties`, which is ignored from version control. For example,

```
url=http://13.229.56.106:8080
user=jenkins
password=secret
```

If this file is present, the tasks for this Gradle plugin will use them, and you won't have to supply them on the 
command line.

#### Assumptions made by the plugin

- The indicated Jenkins installation needs to have some appropriate plugins in order to run the jobs correctly. Honestly
  I don't know what the minimal subset is, but it certainly is a subset (but not necessarily a _proper subset_) of those
  seen [here](https://github.com/robmoore-i/JenkinsEC2/blob/main/jenkins_install_plugins.sh#L17).
- The given branch starts with `simulation/` and contains no underscores (`_`).

### Gradle plugin: Simulate Development

Modifies source code for introducing some variety in the build.

For example, running `./gradlew :sleeper:simulateDevelopment` will run the development simulation in the `sleeper`
project, which should generate some code.

To configure this plugin, a project which applies it needs to supply an instance of `DevelopmentSimulator`, which is a
functional interface for generating source and test code files. If no instance is supplied, the configured
`simulateDevelopment` task will do nothing.

To pass in a custom implementation of `DevelopmentSimulator`, use the following code in the project's `build.gradle.kts`

```
simulator {
    instance.set( <Instance of your custom implementation here> )
}
```

#### Assumptions made by the plugin

- Main and test sources are both written in Groovy, under the default groovy source set
  (i.e. `src/main/groovy` and `src/test/groovy`)
- There is a base package `ci.build.simulator.<project-name>` for both the main and test sources, which is where the
  plugin will generate code. For example,
  `ci.build.simulator.sleeper`. The `<project name>` is the name of the subdirectory of this repo (i.e. the value within
  the 'include' call in `settings.gradle.kts`).
- The Jenkins instance has a [username/password credential](https://www.jenkins.io/doc/book/using/using-credentials/) 
  configured, called `GitHubPushAccess`, which holds valid credentials for pushing to this repository's Git remote.

## Extending this simulator

### Different kinds of project

There are different kinds of projects, whose builds have different kinds of characteristics in terms of test duration,
test flakiness, and in other ways too. You may want to generate data for different phases of the build, like creating
different kinds of artefacts like JARs, executables, containers or something else. To do this, you'll need to be able to
extend this simulator to cover whatever custom requirements you might have.

Let's say you wanted to create simulations for some new kind of build. Let's say you want to do a simulation for a
SpringBoot API. I would suggest taking the following steps:

- Create a new Gradle subproject, like `springboot`
- Write code in the `springboot` subproject so that the API works, has basic tests and meets the assumptions of the
  above mentioned Gradle plugins.
- Create a custom implementation of `DevelopmentSimulator` in `buildSrc/src/main/kotlin` under the
  `ci.build.simulator.simulate.development.simulators` package, called `SpringBootDevelopmentSimulator` and implement it
  however you like.
- Configure the 'development simulation' plugin in `springboot/build.gradle.kts` so that it uses an instance of the
  `SpringBootDevelopmentSimulator` for generating code. Presumably this implementation generates some code that is
  specific to SpringBoot.
- You should then be able to create your custom simulations on Jenkins.

### Different CI tools

Jenkins is just one CI provider. It's a very popular one, especially at companies with poor developer productivity in
my experience. There are many others that you might want to run tests on though. In that case, I would suggest taking
the following steps:

- Add a new Gradle plugin in buildSrc under the package `ci.build.simulator.<ci provider name>`.
- Implement it to have the create and delete tasks for simulations, like the Jenkins Gradle plugin.
- Make sure the simulations created by the plugin work.
- Apply the plugin wherever you want.
- You should then be able to create your simulations on the new CI provider.

## Developer Productivity

Many excellent software organisations employ teams dedicated to developer productivity. Here are a few examples:

- [Gradle](https://gradle.com/blog/top-three-reasons-to-launch-a-dedicated-developer-productivity-engineering-team/)
- [Netflix](https://jobs.netflix.com/jobs/59145792)
- [Airbnb](https://www.airbnb.com.sg/careers/departments/engineering/dev_infra)
- [Twitter](https://careers.twitter.com/en/work-for-twitter/202008/035a8b9d-3a5b-4156-bdeb-8042e4e06826/f46512c8-0ed2-4c9c-be08-bfbdae0fbcb8.html/staff-backend-engineer-developer-productivity-buildtools.html)
- [Gitlab](https://about.gitlab.com/handbook/engineering/quality/engineering-productivity-team/)
- [Spotify](https://engineering.atspotify.com/2020/08/27/how-we-improved-developer-productivity-for-our-devops-teams/)