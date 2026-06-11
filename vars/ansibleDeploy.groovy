def call(Map config) {

    stage('Clone') {

        echo "Repository already checked out by Jenkins"
    }

    if (config.KEEP_APPROVAL_STAGE) {

        stage('User Approval') {

            input(
                message: "Approve Kafka Deployment?",
                ok: "Deploy"
            )
        }
    }

    stage('Playbook Execution') {

        sh """
        ansible-playbook \
        ${config.CODE_BASE_PATH}/${config.PLAYBOOK_NAME} \
        -i inventory/hosts.ini
        """
    }

    stage('Notification') {

        slackSend(
            channel: "#${config.SLACK_CHANNEL_NAME}",
            message: "${config.ACTION_MESSAGE}"
        )

        echo "Notification Sent"
    }
}
