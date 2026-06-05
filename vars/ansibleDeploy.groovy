def call(Map config) {

    stage('Clone') {

        git config.REPO_URL
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
        echo "Running Playbook"

        echo "Environment: ${config.ENVIRONMENT}"
        echo "Code Base: ${config.CODE_BASE_PATH}"

        echo "ansible-playbook ${config.PLAYBOOK_NAME}"
        """
    }

    stage('Notification') {

        echo "${config.ACTION_MESSAGE}"
    }
}
