package ci.build.simulator.app

import com.offbytwo.jenkins.JenkinsServer

class App {
    static void main(String[] args) {
        String jenkinsUrl = args[0]
        String jenkinsUsername = args[1]
        String jenkinsPassword = args[2]
        def jenkins = new JenkinsServer(new URI(jenkinsUrl), jenkinsUsername, jenkinsPassword)

        println("Current jobs: ${jenkins.getJobs()}")
        def jobTemplate = Thread.currentThread().getContextClassLoader().getResourceAsStream("jenkins-job-template.xml")
        def reader = new BufferedReader(new InputStreamReader(jobTemplate))
        def xml = reader.lines().collect().join("\n").replace("{{BRANCH}}", "main")
        println("Adding new job:\n-----\n$xml\n-----")
        jenkins.createJob("Build-main", xml, true)
    }

    static String run() {
        "Hello world!"
    }
}
