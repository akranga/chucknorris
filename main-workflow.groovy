import groovy.text.*

def imageName       = "akranga/chucknorris"
def version         = "0.1.0"
def tag             = "${version}-b${env.BUILD_NUMBER}"
def privateRegistry = "${System.getenv('PRIVATE_REGISTRY_SERVICE_HOST')}:${System.getenv('PRIVATE_REGISTRY_SERVICE_PORT_API')}"

node("java") {
  stage 'Compile and run Unit tests'
  sh "./gradlew assemble"
}

compilation["docker-collector"] = {
  node("docker") {
    stage 'Make analytics-collector container'

    docker.withRegistry("http://${privateRegistry}/") { 
      def image = docker.build("${imageName}:${tag}", '')
      image.push()
      image.push("latest")
    }
    echo "Done!"
  }
}

//@NonCPS
def renderManifest(source, target, bindings=[:]) {    
  def sourceContent  = readFile source
  def templateEngine = new SimpleTemplateEngine()
  def template       = templateEngine.createTemplate sourceContent 
  def result         = template.make(bindings).toString()
  echo "Rendered to: ${result}"
  writeFile file: target, text: result
}