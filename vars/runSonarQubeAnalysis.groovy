/**
 * This function is responsible for performing SonarQube analysis on a specified project.
 * It first cleans and builds the project, then runs SonarQube analysis using the Gradle wrapper.
 * 
 * Parameters:
 * - sonarEnv: (String) The SonarQube environment to use (e.g., 'sn1', 'sn2', etc.)
 * - projectDir: (String) The directory of the project to be analyzed
 * 
 * The SonarQube analysis is executed using the `sonar` plugin for Gradle.
 * A SonarQube authentication token should be stored in Jenkins credentials and passed as the environment variable `SONAR_TOKEN`.
 * 
 * Example usage:
 * runSonarQubeAnalysis(
 *     sonarEnv: 'sn1',
 *     projectDir: 'my-demo-app'
 * )
 */
def call(Map config = [:]) {
    // Set default values for config params if not provided
    def sonarEnv = config.sonarEnv ?: 'sn1' // Default to 'sn1' if not provided
    def projectDir = config.projectDir ?: 'my-demo-app' // Default to 'my-demo-app' directory if not provided

    // Use the SonarQube environment configuration ('sn1') once in the pipeline
    withSonarQubeEnv(sonarEnv) { 
        // Navigate to the project directory and run the analysis
        runSonarQubeInDir(projectDir)
    }
}

/**
 * This helper function handles the actual SonarQube analysis in the specified directory.
 * 
 * Parameters:
 * - projectDir: (String) The directory of the project to be analyzed
 * - sonarProjectKey: (String) The unique identifier for the project in SonarQube
 * - sonarProjectName: (String) The name of the project in SonarQube
 */
def runSonarQubeInDir(String projectDir) {
    dir(projectDir) {
        echo "Building and analyzing project ${sonarProjectName}..."

        // Clean and build the project using Gradle
        sh './gradlew clean build'
        
        // Run SonarQube analysis after build
        echo "Running SonarQube analysis..."
        sh """
            ./gradlew sonarqube --info
        """
    }
}
