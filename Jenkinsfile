pipeline {
    agent any
    
    environment {
        DOCKER_REGISTRY = 'docker.io'
        KUBE_NAMESPACE = 'hcm'
        SONARQUBE_URL = 'http://192.168.0.155:30033'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Detect Changes') {
            steps {
                script {
                    // Get list of changed files
                    def changes = []
                    if (env.CHANGE_ID) {
                        // Pull request
                        changes = sh(
                            script: "git diff --name-only origin/${env.CHANGE_TARGET}...HEAD",
                            returnStdout: true
                        ).trim().split('\n')
                    } else {
                        // Direct push to branch
                        changes = sh(
                            script: "git diff --name-only HEAD~1 HEAD",
                            returnStdout: true
                        ).trim().split('\n')
                    }
                    
                    // Determine which services are affected
                    def servicesToBuild = []
                    def serviceDirs = [
                        'application-service',
                        'candidate-service', 
                        'tenant-service', 
                        'organization-service', 
                        'vendor-service',
                        'position-service',
                        'jobrequisition-service',
                        'hcm-core',
                        'hcm-ui'
                    ]
                    
                    serviceDirs.each { service ->
                        if (changes.any { it.startsWith(service + '/') || it.startsWith('hcm-common/') || it.startsWith('hcm-message-broker/') }) {
                            servicesToBuild.add(service)
                        }
                    }
                    
                    // Always build hcm-common and hcm-message-broker if they have changes
                    if (changes.any { it.startsWith('hcm-common/') }) {
                        servicesToBuild.add('hcm-common')
                    }
                    if (changes.any { it.startsWith('hcm-message-broker/') }) {
                        servicesToBuild.add('hcm-message-broker')
                    }
                    
                    // If no specific service changes, build all services
                    if (servicesToBuild.isEmpty()) {
                        servicesToBuild = serviceDirs
                    }
                    
                    env.SERVICES_TO_BUILD = servicesToBuild.join(',')
                    echo "Services to build: ${env.SERVICES_TO_BUILD}"
                }
            }
        }
        
        stage('Build Common Dependencies') {
            when {
                anyOf {
                    expression { env.SERVICES_TO_BUILD.contains('hcm-common') }
                    expression { env.SERVICES_TO_BUILD.contains('hcm-message-broker') }
                    expression { env.SERVICES_TO_BUILD.split(',').length > 0 }
                }
            }
            steps {
                script {
                    // Check if dependencies need to be built
                    def needsDependencyBuild = false
                    def dependencyServices = []
                    
                    if (env.SERVICES_TO_BUILD.contains('hcm-common')) {
                        needsDependencyBuild = true
                        dependencyServices.add('hcm-common')
                    }
                    
                    if (env.SERVICES_TO_BUILD.contains('hcm-message-broker')) {
                        needsDependencyBuild = true
                        dependencyServices.add('hcm-message-broker')
                    }
                    
                    // Check if any service depends on hcm-common (all services do)
                    if (env.SERVICES_TO_BUILD.split(',').any { it != 'hcm-common' && it != 'hcm-message-broker' }) {
                        if (!dependencyServices.contains('hcm-common')) {
                            needsDependencyBuild = true
                            dependencyServices.add('hcm-common')
                        }
                    }
                    
                    if (needsDependencyBuild) {
                        echo "Dependencies that need to be built: ${dependencyServices.join(', ')}"
                        
                        // Manual approval for dependency builds
                        def userInput = input(
                            id: 'dependencyApproval',
                            message: "Approve building dependencies?",
                            parameters: [
                                choice(
                                    name: 'BUILD_DEPENDENCIES',
                                    choices: ['Yes', 'No'],
                                    description: 'Do you want to build dependencies?'
                                ),
                                text(
                                    name: 'REASON',
                                    defaultValue: '',
                                    description: 'Reason for building/not building dependencies (optional)'
                                )
                            ]
                        )
                        
                        if (userInput.BUILD_DEPENDENCIES == 'Yes') {
                            echo "Dependency build approved. Reason: ${userInput.REASON ?: 'No reason provided'}"
                            
                            // Build hcm-common first as it's a dependency
                            if (dependencyServices.contains('hcm-common')) {
                                echo "Building hcm-common..."
                                dir('hcm-common') {
                                    sh 'mvn clean install -DskipTests'
                                }
                            }
                            
                            // Build hcm-message-broker if it has changes
                            if (dependencyServices.contains('hcm-message-broker')) {
                                echo "Building hcm-message-broker..."
                                dir('hcm-message-broker') {
                                    sh 'mvn clean install -DskipTests'
                                }
                            }
                        } else {
                            echo "Dependency build rejected. Reason: ${userInput.REASON ?: 'No reason provided'}"
                            echo "Proceeding without building dependencies. This may cause build failures if dependencies are not available."
                        }
                    } else {
                        echo "No dependencies need to be built."
                    }
                }
            }
        }
        
        stage('Build Services') {
            parallel {
                stage('Build Application Service') {
                    when {
                        expression { env.SERVICES_TO_BUILD.contains('application-service') }
                    }
                    steps {
                        script {
                            buildService('application-service', 'techbu/hcm-application-service')
                        }
                    }
                }
                
                stage('Build Candidate Service') {
                    when {
                        expression { env.SERVICES_TO_BUILD.contains('candidate-service') }
                    }
                    steps {
                        script {
                            buildService('candidate-service', 'techbu/hcm-candidate-service')
                        }
                    }
                }
                
                stage('Build Tenant Service') {
                    when {
                        expression { env.SERVICES_TO_BUILD.contains('tenant-service') }
                    }
                    steps {
                        script {
                            buildService('tenant-service', 'techbu/hcm-tenant-service')
                        }
                    }
                }
                
                stage('Build Organization Service') {
                    when {
                        expression { env.SERVICES_TO_BUILD.contains('organization-service') }
                    }
                    steps {
                        script {
                            buildService('organization-service', 'techbu/hcm-organization-service')
                        }
                    }
                }
                
                stage('Build Vendor Service') {
                    when {
                        expression { env.SERVICES_TO_BUILD.contains('vendor-service') }
                    }
                    steps {
                        script {
                            buildService('vendor-service', 'techbu/hcm-vendor-service')
                        }
                    }
                }
                
                stage('Build Position Service') {
                    when {
                        expression { env.SERVICES_TO_BUILD.contains('position-service') }
                    }
                    steps {
                        script {
                            buildService('position-service', 'techbu/hcm-position-service')
                        }
                    }
                }
                
                stage('Build Job Requisition Service') {
                    when {
                        expression { env.SERVICES_TO_BUILD.contains('jobrequisition-service') }
                    }
                    steps {
                        script {
                            buildService('jobrequisition-service', 'techbu/hcm-jobrequisition-service')
                        }
                    }
                }
                
                stage('Build HCM Core') {
                    when {
                        expression { env.SERVICES_TO_BUILD.contains('hcm-core') }
                    }
                    steps {
                        script {
                            buildService('hcm-core', 'techbu/hcm-core')
                        }
                    }
                }
                
                stage('Build Frontend') {
                    when {
                        expression { env.SERVICES_TO_BUILD.contains('hcm-ui') }
                    }
                    steps {
                        script {
                            buildService('hcm-ui/hcm-frontend', 'techbu/hcm-frontend')
                        }
                    }
                }
            }
        }
        
        stage('SonarQube Analysis') {
            when {
                expression { env.SERVICES_TO_BUILD != '' }
            }
            steps {
                script {
                    // Define services that need SonarQube analysis
                    def servicesForSonarQube = [
                        'hcm-common': ['hcm-common', 'HCM Common'],
                        'hcm-message-broker': ['hcm-message-broker', 'HCM Message Broker'],
                        'application-service': ['hcm-application-service', 'HCM Application Service'],
                        'candidate-service': ['hcm-candidate-service', 'HCM Candidate Service'],
                        'tenant-service': ['hcm-tenant-service', 'HCM Tenant Service'],
                        'organization-service': ['hcm-organization-service', 'HCM Organization Service'],
                        'vendor-service': ['hcm-vendor-service', 'HCM Vendor Service'],
                        'position-service': ['hcm-position-service', 'HCM Position Service'],
                        'jobrequisition-service': ['hcm-jobrequisition-service', 'HCM Job Requisition Service'],
                        'hcm-core': ['hcm-core', 'HCM Core']
                    ]
                    
                    // Run SonarQube analysis for each service that needs to be built
                    servicesForSonarQube.each { serviceDir, serviceInfo ->
                        def projectKey = serviceInfo[0]
                        def projectName = serviceInfo[1]
                        
                        if (env.SERVICES_TO_BUILD.contains(serviceDir)) {
                            // Check if service directory exists and has source code
                            if (fileExists("${serviceDir}/pom.xml")) {
                                echo "Running SonarQube analysis for ${projectName}..."
                                
                                try {
                                    withCredentials([usernamePassword(credentialsId: 'sonarqube-credentials', usernameVariable: 'SONAR_USER', passwordVariable: 'SONAR_TOKEN')]) {
                                        dir(serviceDir) {
                                            sh '''
                                                mvn clean compile test-compile
                                                mvn sonar:sonar \
                                                  -Dsonar.host.url="${SONARQUBE_URL}" \
                                                  -Dsonar.login="${SONAR_TOKEN}" \
                                                  -Dsonar.projectKey="''' + projectKey + '''" \
                                                  -Dsonar.projectName="''' + projectName + '''" \
                                                  -Dsonar.sources="src/main/java" \
                                                  -Dsonar.java.binaries="target/classes" \
                                                  -Dsonar.java.test.binaries="target/test-classes" \
                                                  -Dsonar.coverage.jacoco.xmlReportPaths="target/site/jacoco/jacoco.xml" \
                                                  -Dsonar.java.source=11
                                            '''
                                            echo "SonarQube analysis completed successfully for ${projectName}"
                                        }
                                    }
                                } catch (Exception e) {
                                    echo "SonarQube analysis failed for ${projectName}: ${e.getMessage()}"
                                    // Don't fail the entire pipeline for SonarQube analysis failures
                                }
                            } else {
                                echo "No pom.xml found in ${serviceDir}, skipping SonarQube analysis"
                            }
                        }
                    }
                }
            }
        }
        
        stage('Deploy to Nexus') {
            when {
                anyOf {
                    expression { env.SERVICES_TO_BUILD.contains('hcm-common') }
                    expression { env.SERVICES_TO_BUILD.contains('hcm-message-broker') }
                }
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
                    script {
                        // Create Maven settings.xml with Nexus credentials
                        writeFile file: 'settings.xml', text: """<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 
          http://maven.apache.org/xsd/settings-1.0.0.xsd">
    <servers>
        <server>
            <id>nexus</id>
            <username>${NEXUS_USER}</username>
            <password>${NEXUS_PASS}</password>
        </server>
    </servers>
</settings>"""
                        
                        if (env.SERVICES_TO_BUILD.contains('hcm-common')) {
                            dir('hcm-common') {
                                sh 'mvn deploy -s ../settings.xml'
                            }
                        }
                        
                        if (env.SERVICES_TO_BUILD.contains('hcm-message-broker')) {
                            dir('hcm-message-broker') {
                                sh 'mvn deploy -s ../settings.xml'
                            }
                        }
                    }
                }
            }
        }
    }
    
    post {
        always {
            cleanWs()
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed! Check the logs for details.'
        }
    }
}


// Helper function to build a service (Java or Frontend)
def buildService(String serviceName, String dockerImage) {
    def dockerTag = "${BUILD_NUMBER}"
    
    // Check if this is a frontend service (contains 'hcm-ui' or has package.json)
    def isFrontend = serviceName.contains('hcm-ui') || fileExists("${serviceName}/package.json")
    
    if (isFrontend) {
        // Frontend build - Dockerfile handles npm install and build
        dir(serviceName) {
            echo "Frontend build will be handled by Dockerfile"
        }
    } else {
        // Java service build process
        withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
            writeFile file: 'settings.xml', text: """
<settings>
  <servers>
    <server>
      <id>nexus</id>
      <username>${NEXUS_USER}</username>
      <password>${NEXUS_PASS}</password>
    </server>
  </servers>
</settings>
"""
            dir(serviceName) {
                sh "mvn clean package -s ../settings.xml -DskipTests"
            }
        }
    }
    
    // Build and push Docker image
    withCredentials([usernamePassword(credentialsId: 'docker-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
        dir(serviceName) {
            // Check if Dockerfile exists before attempting Docker build
            if (fileExists('Dockerfile')) {
                sh """
                    echo "Current working directory: \$(pwd)"
                    echo "Checking if Dockerfile exists: \$(ls -la Dockerfile)"
                    
                    echo "Logging into Docker Hub..."
                    echo "$DOCKER_PASS" | docker login -u $DOCKER_USER --password-stdin || exit 1
                    
                    echo "Building Docker image for ${serviceName}..."
                    docker build -t ${dockerImage}:${dockerTag} . || exit 1
                    
                    echo "Tagging latest image..."
                    docker tag ${dockerImage}:${dockerTag} ${dockerImage}:latest || exit 1
                    
                    echo "Pushing versioned image..."
                    docker push ${dockerImage}:${dockerTag} || exit 1
                    
                    echo "Pushing latest image..."
                    docker push ${dockerImage}:latest || exit 1
                """
            } else {
                echo "No Dockerfile found for ${serviceName}, skipping Docker build"
            }
        }
    }
    
    // Deploy to Kubernetes (only if Docker build was successful)
    if (fileExists("${serviceName}/Dockerfile")) {
        withCredentials([file(credentialsId: 'kubeconfig-jenkins', variable: 'KUBECONFIG')]) {
            script {
                // Determine the actual service name for deployment (remove path prefix for frontend)
                def deploymentServiceName = serviceName.contains('/') ? serviceName.split('/')[-1] : serviceName
                
                // Check if service has Helm chart
                if (fileExists("${serviceName}/helm/Chart.yaml")) {
                    // Deploy using Helm
                    dir("${serviceName}/helm") {
                        sh """
                            echo "Deploying ${deploymentServiceName} using Helm..."
                            helm upgrade --install ${deploymentServiceName} . \
                              --namespace ${KUBE_NAMESPACE} \
                              --create-namespace \
                              --set image.tag=${dockerTag} \
                              --set image.repository=${dockerImage} \
                              --wait \
                              --timeout=10m \
                              --kubeconfig=${KUBECONFIG} || exit 1
                            
                            echo "Waiting for deployment to complete..."
                            kubectl --kubeconfig=${KUBECONFIG} rollout status deployment/${deploymentServiceName} -n ${KUBE_NAMESPACE} || exit 1
                            
                            echo "Deployment completed successfully!"
                        """
                    }
                } else if (fileExists("${serviceName}/k8s/")) {
                    // Deploy using Kubernetes manifests
                    sh """
                        echo "Verifying cluster access..."
                        kubectl --kubeconfig=${KUBECONFIG} cluster-info || exit 1
                        
                        echo "Creating namespace if it doesn't exist..."
                        kubectl --kubeconfig=${KUBECONFIG} create namespace ${KUBE_NAMESPACE} --dry-run=client -o yaml | kubectl --kubeconfig=${KUBECONFIG} apply -f - || exit 1
                        
                        echo "Applying ConfigMap..."
                        kubectl --kubeconfig=${KUBECONFIG} apply -n ${KUBE_NAMESPACE} -f ${serviceName}/k8s/configmap.yaml || exit 1
                        
                                            echo "Applying Secrets (if present)..."
                    if [ -f ${serviceName}/k8s/secrets.yaml ]; then
                      echo "Found secrets.yaml, applying..."
                      kubectl --kubeconfig=${KUBECONFIG} apply -n ${KUBE_NAMESPACE} -f ${serviceName}/k8s/secrets.yaml || exit 1
                    elif [ -f ${serviceName}/k8s/secret.yaml ]; then
                      echo "Found secret.yaml, applying..."
                      kubectl --kubeconfig=${KUBECONFIG} apply -n ${KUBE_NAMESPACE} -f ${serviceName}/k8s/secret.yaml || exit 1
                    else
                      echo "No secret file found (neither secrets.yaml nor secret.yaml)"
                    fi
                        
                        echo "Applying Service..."
                        kubectl --kubeconfig=${KUBECONFIG} apply -n ${KUBE_NAMESPACE} -f ${serviceName}/k8s/service.yaml || exit 1
                        
                        echo "Checking if deployment exists..."
                        if ! kubectl --kubeconfig=${KUBECONFIG} get deployment ${deploymentServiceName} -n ${KUBE_NAMESPACE} &>/dev/null; then
                            echo "Deployment does not exist. Creating initial deployment..."
                            kubectl --kubeconfig=${KUBECONFIG} apply -n ${KUBE_NAMESPACE} -f ${serviceName}/k8s/deployment.yaml || exit 1
                        else
                            echo "Updating existing deployment with new image..."
                            kubectl --kubeconfig=${KUBECONFIG} set image deployment/${deploymentServiceName} \
                                ${deploymentServiceName}=${dockerImage}:${dockerTag} \
                                -n ${KUBE_NAMESPACE} || exit 1
                        fi
                        
                        echo "Waiting for deployment to complete..."
                        kubectl --kubeconfig=${KUBECONFIG} rollout status deployment/${deploymentServiceName} -n ${KUBE_NAMESPACE} || exit 1
                        
                        echo "Deployment completed successfully!"
                    """
                } else {
                    echo "No deployment configuration found for ${serviceName}"
                }
            }
        }
    } else {
        echo "Skipping Kubernetes deployment for ${serviceName} as no Dockerfile exists"
    }
    
    // Summary of what was done for this service
    if (fileExists("${serviceName}/Dockerfile")) {
        echo "${serviceName}: Built and deployed successfully"
    } else {
        echo "${serviceName}: Skipped (no Dockerfile found)"
    }
}