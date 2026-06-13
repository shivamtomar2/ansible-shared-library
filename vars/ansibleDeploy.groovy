def call() {

    def config = evaluate(readFile('deployment-config.groovy'))

    stage('Clone') {

        echo "Repository already checked out by Jenkins"
    }

    if (config.KEEP_APPROVAL_STAGE) {

        stage('User Approval') {

            input(
                message: "Deploy to ${config.ENVIRONMENT} ?",
                ok: "Proceed"
            )
        }
    }

    stage('Playbook Execution') {

        sh """
        export ENVIRONMENT=${config.ENVIRONMENT}

        export PATH=/opt/homebrew/bin:/usr/bin:/bin:/usr/sbin:/sbin

        ansible-playbook \
        ${config.CODE_BASE_PATH}/${config.PLAYBOOK_NAME} \
        -i inventory/prod
        """
    }

    stage('Notification') {

        try {

            slackSend(
                channel: "#${config.SLACK_CHANNEL_NAME}",
                message: "${config.ACTION_MESSAGE}"
            )

        } catch(Exception ex) {

            echo "Slack notification skipped"
        }

        echo "Deployment Completed"
    }
}
