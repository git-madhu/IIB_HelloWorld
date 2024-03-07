pipeline {
    agent any
    environment {
        USER = "surya"
        Bar_Creation = "/mnt/d/iib/ace-12.0.2.0/tools/mqsicreatebar"
        Bar_Store = "/mnt/d/iib"
        Job_Name = "Sample"
        Jfrog_Repository = "http://localhost:8082/artifactory/example-repo-local/"
        Artifactory_Repo_Source = "example-repo-local"
        Artifactory_Repo_Destination = "example-repo-local-destination"
        Artifactory_URL = "http://localhost:8082/artifactory"
        Artifact_Name = "Sample.bar"
    }
    stages {
        stage('git login') {
            steps {
                git branch: 'main', changelog: false, credentialsId: 'git', poll: false, url: 'https://github.com/git-madhu/IIB_HelloWorld.git'
//                git branch: 'main', credentialsId: 'Git', url: 'https://github.com/git-madhu/IIB_HelloWorld.git'
            }
        }
//        stage('bar build') {
//            steps {
//                script {
//                    def xvfb = [$class: 'Xvfb', additionalOptions: '-nolisten tcp -screen 0 1024x768x24', autoDisplayName: true, timeout: 60, display_name: '0.1']
//                    wrap(xvfb) {
                        // sh 'export DISPLAY=:99'
//                        sh "$Bar_Creation -data $WORKSPACE -b $Bar_Store/HelloWorld.bar -a $Job_Name"
                        // sh "chmod +r $Bar_Store/$Job_Name.bar"
//                    }
//            }
//        }
//        stage('Bar Store') {
//            steps {
//                script {    
//                    withCredentials([usernamePassword(credentialsId: 'Jfrog', passwordVariable: 'Jfrog_Password', usernameVariable: 'Jfrog_Username')]) {
//                    sh "curl -v -u$Jfrog_Username:$Jfrog_Password --upload-file $Bar_Store/HelloWorld.bar $Jfrog_Repository"
//                   }    
//                }
//            }
//        }
        stage('Docker Build') {
           steps{
               script {
                 withCredentials([usernamePassword(credentialsId: 'docker', passwordVariable: 'Docker_password', usernameVariable: 'Docker_username')]) {
                 sh "docker login -u ${Docker_username} -p ${Docker_password}"
                 def dockerfilePath = 'dockerfile_app2'
                 def dockerImage = 'ace'
                 def dockerTag = 'v1'
                 def dockerTag1 = 'v2'                 
 //                def DOCKER_REGISTRY_URL = "https://hub.docker.com/r/madhavdocker123"
                 sh " docker build -t ${dockerImage}:${dockerTag} -f ${dockerfilePath} ."
                 sh "docker tag ${dockerImage}:${dockerTag} madhavdocker123/${dockerImage}:${dockerTag1}"
                 sh "docker push madhavdocker123/${dockerImage}:${dockerTag1}"
//               sh " docker pull madhavdocker123/ace:v1"
//               sh " "
                   }
                }
             
            }   
          
        }
        
    }
}
