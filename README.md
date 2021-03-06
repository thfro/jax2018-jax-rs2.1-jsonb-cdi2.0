
# Code samples from JAX 2018
## Live Coding with JAX-RS 2.1, JSON-B & CDI 2.0

**This code is for demonstration purposes only.**

* Download latest Development Build of Open Liberty from https://openliberty.io/downloads/ and store the file locally.
* Edit *pom.xml* and replace the path in `<assemblyArchive>` with your local path.

* Build the project, deploy to Open Liberty and start the server: `mvn package liberty:start-server`

* Rebuild project and redeploy (keep server running): `mvn package liberty:install-apps`

* An executable JAR containing both Open Liberty and the application can be found in *target/server-samples.jar*

Test requests:
* curl http://localhost:9080/server-samples/api/registrations
* curl http://localhost:9080/server-samples/api/registrations -d '{"name": "Donald Duck"}' -H "Content-Type: application/json" -i
* curl http://localhost:9080/server-samples/api/registrations/1 -X PATCH -d '[{"op": "replace", "path": "/name", "value": "Daisy Duck"}]' -H "Content-Type: application/json-patch+json" -i
* curl http://localhost:9080/server-samples/api/sse/ -F 'event=Das ist Event 1' -i
* curl http://localhost:9080/server-samples/api/sse/broadcast -F 'event=Das ist Event 1' -i

