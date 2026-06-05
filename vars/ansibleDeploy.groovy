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
        export PATH=/opt/homebrew/bin:\$PATH

        ansible-playbook \
        ${config.PLAYBOOK_NAME} \
        -i ${config.INVENTORY_FILE}
        """
    }

    stage('Notification') {

        echo "${config.ACTION_MESSAGE}"
    }
}
