def call(Map config) {

    stage('Clone') {

        deleteDir()

        checkout([
            $class: 'GitSCM',
            branches: [[name: '*/main']],
            userRemoteConfigs: [[url: config.REPO_URL]]
        ])
    }

    if(config.KEEP_APPROVAL_STAGE) {

        stage('User Approval') {

            input(
                message: "Deploy to ${config.ENVIRONMENT} ?",
                ok: "Proceed"
            )
        }
    }

    stage('Playbook Execution') {

        sh """
        export PATH=/opt/homebrew/bin:\$PATH

        pwd

        ls -R

        ansible-playbook \
        playbooks/deploy.yml \
        -i inventory/prod
        """
    }

    stage('Notification') {

        echo "${config.ACTION_MESSAGE}"
    }
}

