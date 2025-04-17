def call(Map config = [:]) {
    // Load the YAML file from the resources directory
    def podYaml = libraryResource 'pod.yaml'
    
    // Write it to a file so kubectl can use it
    writeFile file: 'pod.yaml', text: podYaml

    // Use withKubeConfig to apply it
    withKubeConfig([credentialsId: config.credentialsId, serverUrl: config.serverUrl]) {
        sh 'kubectl apply -f pod.yaml'
    }
}
