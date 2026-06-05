def call(Map config) {

    stage('Clone') {

        deleteDir()

        git config.REPO_URL
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
        export PATH=/opt/homebrew/bin:\$PATH

        echo "Current Workspace:"
        pwd

        echo "Workspace Files:"
        ls -la

        echo "Searching Playbook:"
        find . -name deploy.yml

        echo "Searching Inventory:"
        find . -name prod

        ansible-playbook \
        playbooks/deploy.yml \
        -i inventory/prod
        """
    }

    stage('Notification') {

        echo "${config.ACTION_MESSAGE}"
    }
}
