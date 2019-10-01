pipeline {
    agent any

    stages {
        stage('PackageAPKs') {
                    steps {
                        sh './build_debug.sh'
                    }
        }
    }
}