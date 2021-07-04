# ci-build-simulator

This project can be used to seed a [Jenkins installation](https://github.com/robmoore-i/JenkinsEC2)
with builds. 

## Usage

### Creating a simulation

A simulation is a self-perpetuating job which continuously creates builds. It would be pretty
boring if the builds were all the same, so in every run, the build modifies the source code so
that subsequent builds are a bit different.

Correspondingly, there are two Gradle plugins:

- Jenkins (ci.build.simulator.jenkins)
- Simulate Development (ci.build.simulator.simulate.development)

### Gradle plugin: Jenkins

Creates a Jenkins job for a simulation.

For example, running `./gradlew :sleeper:createJob -Pbranch=simulation/1 -Purl=http://13.229.56.106:8080 -Puser=jenkins -Ppassword=secret` 
will use the provided user/password credentials to log into the Jenkins installation at the provided URL, and create a 
new simulation which will continuously push updates to the provided branch.

#### Assumptions made by the plugin

- The given branch starts with `simulator/` and contains no underscores (`_`).
- The indicated Jenkins installation needs to have some appropriate plugins in order to run the
  jobs correctly. Honestly I don't know what the minimal subset is, but it certainly is a subset
  of those seen [here](https://github.com/robmoore-i/JenkinsEC2/blob/main/jenkins_install_plugins.sh#L17).
- The created jobs are named after the given project (i.e. `sleeper` in `:sleeper:createJob`) and
  the given branch. If a job already exists for that combination, the job creation will fail with
  a descriptive error message.

### Gradle plugin: Simulate Development

Modifies source code for introducing some variety in the build.

For example, running `./gradlew :sleeper:simulateDevelopment` will run the development simulation in the `sleeper` 
project, which should generate some code.

#### Assumptions made by the plugin

- Main and test sources are both written in Groovy, under the default groovy source set
  (i.e. `src/main/groovy` and `src/test/groovy`)
- There is a base package `ci.build.simulator.<project-name>` for both the main and test 
  sources, which is where the plugin will generate code. For example, 
  `ci.build.simulator.sleeper`. The `<project name>` is the name of the subdirectory of this 
  repo (i.e. the value within the 'include' call in `settings.gradle.kts`).

## Extending this simulator

There are different kinds of projects, whose builds have different kinds of characteristics
in terms of test duration, test flakiness, and in other ways too. You may want to generate data
for different phases of the build, like creating different kinds of artefacts like JARs, 
executables, containers or something else. To do this, you'll need to be able to extend this 
simulator to cover whatever custom requirements you might have.

## CodeDay Labs

This project exists mainly for use by my CodeDay labs team while they create a tool for 
[Developer Productivity](#developer-productivity) that could be used to view some basic 
analytics about builds.

## Developer Productivity

Many excellent software organisations employ teams dedicated to developer productivity. Here
are a few examples:

- [Gradle](https://gradle.com/blog/top-three-reasons-to-launch-a-dedicated-developer-productivity-engineering-team/)
- [Netflix](https://jobs.netflix.com/jobs/59145792)
- [Airbnb](https://www.airbnb.com.sg/careers/departments/engineering/dev_infra)
- [Twitter](https://careers.twitter.com/en/work-for-twitter/202008/035a8b9d-3a5b-4156-bdeb-8042e4e06826/f46512c8-0ed2-4c9c-be08-bfbdae0fbcb8.html/staff-backend-engineer-developer-productivity-buildtools.html)
- [Gitlab](https://about.gitlab.com/handbook/engineering/quality/engineering-productivity-team/)
- [Spotify](https://engineering.atspotify.com/2020/08/27/how-we-improved-developer-productivity-for-our-devops-teams/)