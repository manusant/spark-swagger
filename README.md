# SparkSwagger  <img src="https://travis-ci.org/manusant/SparkSwagger.svg?branch=master" alt="build:started">
Live Documentation for *Spark* (http://sparkjava.com/) applications using *Swagger* (https://swagger.io/) spec.

### Motivation

Create and maintain documentation for API´s is a tedious part of application development. Developing in Spark Framework is not an exception. Its even worse since the current *Swagger* platform doesn´t have support for *Spark*. To contrast these approaches, taking advantage of existing solutions, we'll provide a non-official *Swagger* support for *Spark* application.

<img src="/doc/SparkSwaggerFlow.png" width="100%">

Besides *Swagger* spec generation in JSON and YAML formats, this extension uses two open-source libraries in order to 
provide a beautiful documentation interface for any spark application.

### Used Libraries
 - **Swagger UI** - is a collection of HTML, Javascript, and CSS assets that dynamically generate beautiful documentation from a Swagger-compliant API (https://github.com/swagger-api/swagger-ui)
 - **Swagger UI Themes** - is a collection of themes to spice up those default Swagger Docs, ready to be dropped right into any project that needs a new look for your API docs (http://meostrander.com/swagger-ui-themes/).

### Tips
1 - Generated Swagger definitions can be accessed via HTTP directly from runnig Spark-Swagger application. To do that you need to submit a GET **http://hostname:port/doc.yaml** for the *YAML* format and GET **http://hostname:port/doc.json** for the *JSON* format.

2 - Exported Swagger definitions can be imported into Swagger editor (https://swagger.io/swagger-editor/) where you can Generate Server and Client implementations for your *API* in more than 50 programing languages and frameworks.

3 - Generated documentation is served directly from Spark server and can be accessed from **http://hostname:port/** or **http://hostname:port/index.html**.

### Add to your project
#### Gradle
Add this entry to your *build.gradle* file
```groovy
 repositories {
    maven {
        url "https://packagecloud.io/manusant/beerRepo/maven2"
    }
}
```
And add the dependency
```groovy
  compile 'com.beerboy.ss:spark-swagger:1.0.0.47'
```
#### Maven
Add this to *dependencyManagement* section of your *pom.xml* 
```xml
<repositories>
  <repository>
    <id>manusant-beerRepo</id>
    <url>https://packagecloud.io/manusant/beerRepo/maven2</url>
    <releases>
      <enabled>true</enabled>
    </releases>
    <snapshots>
      <enabled>true</enabled>
    </snapshots>
  </repository>
</repositories>
```
And add the dependency
```xml
   <dependency>
      <groupId>com.beerboy.ss</groupId>
      <artifactId>spark-swagger</artifactId>
      <version>1.0.0.47</version>
   </dependency>
```
# Usage
To use the extension you need to wrap the *Spark* Service instance into a *SparkSwagger* instance. All methods provided by *Spark* remains but new ones was added in order to provide a more modular api.

## Ignition
Start Spark and wrap it with SparkSwagger using configurations under "resources/spark-swagger.conf"
```java
   Service spark = Service.ignite().port(55555);
   SparkSwagger.of(spark)
```
Start Spark and wrap it with SparkSwagger using configurations under provided path
```java
   Service spark = Service.ignite().port(55555);
   SparkSwagger.of(spark, "conf/" + SparkSwagger.CONF_FILE_NAME)
```
## Endpoints Binding
An Interface class named **Endpoint** was introduced in order to facilitate Endpoints modularization. Code below is an Endpoint implementation example.
```java
   public class HammerEndpoint implements Endpoint {

    private static final String NAME_SPACE = "/hammer";

    @Override
    public void bind(final SparkSwagger restApi) {

        restApi.endpoint(endpointPath(NAME_SPACE)
                .withDescription("Hammer REST API exposing all Thor utilities "), (q, a) -> LOGGER.info("Received request for Hammer Rest API"))

                .get(path("/export")
                        .withDescription("Gets the whole Network")
                        .withResponseType(Network.class), new GsonRoute() {
                    @Override
                    public Object handleAndTransform(Request request, Response response) {
                        return ok(response, getNetwork());
                    }
                })

                .post(path("/backup")
                        .withDescription("Trigger Network Backup")
                        .withRequestType(BackupNetworkRequest.class)
                        .withGenericResponse(), new TypedGsonRoute<BackupNetworkRequest, Object>() {

                    @Override
                    public Object handleAndTransform(BackupNetworkRequest body, Request request, Response response) {
			return badRequest(response, "Backup Name required in order to backup Network Data");
                    }
                })

                .delete(path("/")
                        .withDescription("Clear Thor network resources")
                        .withGenericResponse(), new GsonRoute() {
                    @Override
                    public Object handleAndTransform(Request request, Response response) {
                        return ok(response, "Thor Store successfully cleared");
                    }
                })
    }
}
```
There are two ways to bind endpoins:

1 - Via **SparkSwagger.endpoint()** method (Example above)

2 - Via an endpoints resolver using **SparkSwagger.endpoints()** method. An endpoint resolver is anything that can supply endpoint instances. The code below shows a *Guice* (https://github.com/google/guice) impelentation for a resolver.
```java
   Service spark = Service.ignite().port(55555);
   SparkSwagger.of(spark, "conf/" + SparkSwagger.CONF_FILE_NAME)
	    .endpoints(() ->
			    ThorModule.getInjector()
				    .findBindingsByType(TypeLiteral.get(Endpoint.class))
				    .stream()
				    .map(binding -> binding.getProvider().get())
				    .collect(Collectors.toSet())
		       )
```
Another resolver implementation can be a simple collection of endpoint instances
```java
   Service spark = Service.ignite().port(55555);
   SparkSwagger.of(spark, "conf/" + SparkSwagger.CONF_FILE_NAME)
	    .endpoints(() -> Arrays.asList(new HammerEndpoint(), new ShieldEndpoint()))
```
## Ignore/Exclude Specification
Ignores are specified via an **IgnoreSpec** . Basically the library can be configured to ignore any field that has one of specified annotations and types or even ignore an entire endpoint matching a specified path. Once ignored the respective field or endpoint is skipped from JSON translation and Swagger documentation. Example of how to configure:
```java
   Service spark = Service.ignite().port(55555);
   SparkSwagger.of(spark, "conf/" + SparkSwagger.CONF_FILE_NAME)
    .ignores(IgnoreSpec.newBuilder().withIgnoreAnnotated(JsonIgnore.class).withIgnoreTypes(Shield.class, GeoPosition.class)::build)
    .endpoints(() -> ...)
```
## Metadata Specification
Two metadata descriptors are provided. **EndpointDescriptor** to describe documentation for an endpoint and **MethodDescriptor** to describe a specific method of an endpoint.
 - *EndpointDescriptor* example:
 ```java
    restApi.endpoint(EndpointDescriptor.Builder.newBuilder()
                // Namespace path
                .withPath(NAME_SPACE)
                // External Doc
                .withExternalDoc(ExternalDocs.newBuilder().withDescription("Find out more").withUrl("https://goo.gl/eNUixh").build()) 
                // Endpoint Description
                .withDescription("Hammer REST API exposing all Thor utilities "), (q, a) -> LOGGER.info("Received request for Hammer Rest API"))
			// endpoint methods
                .get(...)
                .post(...);
```
- *MethodDescriptor* example:
```java
   restApi.endpoint(endpointPath(NAME_SPACE), (q, a) -> LOGGER.info("Received request for Hammer Rest API"))
	// endpoint methods
	.get(MethodDescriptor.Builder.newBuilder()
		// Method path
		.withPath("export/:example1/:example2/:example3")
		// Method description
		.withDescription("Clear Thor network resources")
		// Path params specifications. If param type is String you don´t need to specify it
		.withPathParam().withName("example2").withObject(CardType.class).and()
		.withPathParam().withName("example3").withCollectionOf(NeType.class).and()
		// Query params
		 .withQueryParam().withName("shieldPower").withDescription("Specify the power of the shield").and()
		// Specify response type
		.withGenericResponse(), new GsonRoute() {
		    @Override
		    public Object handleAndTransform(Request request, Response response) {

			return ok(response, "Thor Store successfully cleared");
		    }
	})
```
## DOC Generation
To generate the Swagger Spec and UI you need to explicitly call **SparkSwagger.generateDoc()** method. Once you do that, the UI and spec will be generated and published to a "swagger-ui" folder under the temporary directory and then the directory is mapped to be served by Spark as static resouces.
```java
   Service spark = Service.ignite().port(55555);
   SparkSwagger.of(spark, "conf/" + SparkSwagger.CONF_FILE_NAME)
	    .endpoints(() ->Arrays.asList(new HammerRestApi(),new ShieldRestApi()))
	    .generateDoc();
```
# Configuration

### How to configure

SparkSwagger accepts configuration parameters from a configuration file named *spark-swagger.conf* that can be placed at *''src/main/resources/''* or another specified directory

### Parameters

Parameters with dots in their names are single strings used to organize subordinate parameters, and are not indicative of a nested structure.

Type notations are formatted like so:
- `String=""` means a String type with a default value of `""`.
- `String=["a"*, "b", "c", "d"]` means a String type that can be `a`, `b`, `c`, or `d`, with the `*` indicating that `a` is the default value.

##### Information
All configurations related to information goes under the "info" namespace

Parameter Name | Description
--- | ---
`description` | `String`. Service description
`title` |`String`, Service name
`host` |`String`, The host name/Ip address where the service is running
`docPath` |`String`, Path to access the documentation (Swagger UI)
`termsOfService` | `String`, Terms of service if available
`contact.name` | `String`. Contact name of responsible for the service
`contact.email` | `String`. Contact name of responsible for the service
`contact.url` | `String`. Contact name of responsible for the service
`license.name` | `String`. License Name
`license.url` | `String`. URL for license specifications

##### Display

Parameter Name | Description
--- | ---
`theme` |`String`,  `String=["OUTLINE"*, "FEELING_BLUE", "FLATTOP", "MATERIAL", "MONOKAI", "MUTED", "NEWSPAPER"]`. Controls the UI look and feel.
`deepLinking` | `Boolean=false`. If set to `true`, enables deep linking for tags and operations. See the [Deep Linking documentation](/docs/usage/deep-linking.md) for more information.
`displayOperationId` | `Boolean=false`. Controls the display of operationId in operations list. The default is `false`.
`defaultModelsExpandDepth` | `Number=1`. The default expansion depth for models (set to -1 completely hide the models).
`defaultModelExpandDepth` | `Number=1`. The default expansion depth for the model on the model-example section.
`defaultModelRendering` | `String=["example"*, "model"]`. Controls how the model is shown when the API is first rendered. (The user can always switch the rendering for a given model by clicking the 'Model' and 'Example Value' links.)
`displayRequestDuration` | `Boolean=false`. Controls the display of the request duration (in milliseconds) for Try-It-Out requests.
`docExpansion` | `String=["LIST"*, "FULL", "NONE"]`. Controls the default expansion setting for the operations and tags. It can be 'list' (expands only the tags), 'full' (expands the tags and operations) or 'none' (expands nothing).
`filter` | `Boolean=false OR String`. If set, enables filtering. The top bar will show an edit box that you can use to filter the tagged operations that are shown. Can be Boolean to enable or disable, or a string, in which case filtering will be enabled using that string as the filter expression. Filtering is case sensitive matching the filter expression anywhere inside the tag.
`operationsSorter` | `Function=(a => a)`. Apply a sort to the operation list of each API. It can be 'alpha' (sort by paths alphanumerically), 'method' (sort by HTTP method) or a function (see Array.prototype.sort() to know how sort function works). Default is the order returned by the server unchanged.
`showExtensions` | `Boolean=false`. Controls the display of vendor extension (`x-`) fields and values for Operations, Parameters, and Schema.
`showCommonExtensions` | `Boolean=false`. Controls the display of extensions (`pattern`, `maxLength`, `minLength`, `maximum`, `minimum`) fields and values for Parameters.
`tagsSorter` | `Function=(a => a)`. Apply a sort to the tag list of each API. It can be 'alpha' (sort by paths alphanumerically) or a function (see [Array.prototype.sort()](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/sort) to learn how to write a sort function). Two tag name strings are passed to the sorter for each pass. Default is the order determined by Swagger-UI.

##### Configuration Example

```conf
spark-swagger {

  # UI related configs
  theme = "MATERIAL"
  deepLinking = false
  displayOperationId = false
  defaultModelsExpandDepth = 1
  defaultModelExpandDepth = 1
  defaultModelRendering = "model"
  displayRequestDuration = false
  docExpansion = "LIST"
  filter = true
  operationsSorter = "alpha"
  showExtensions = false
  showCommonExtensions = false
  tagsSorter = "alpha"


  # API related configs
  host = "localhost"
  basePath = "/thor"
  docPath = "/doc"
  info {
    description = "API designed to serve all network operations"
    version = "4.0.0.0.1"
    title = "Thor"
    termsOfService = ""
    schemes = ["HTTP", "HTTPS", "WS", "WSS"]
    project {
      groupId = "com.beerboy.thor"
      artifactId = "thor-hammer"
    }
    contact {
      name = "Example Team"
      email = "example@team.com"
      url = "example.team.com"
    }
    license {
      name = "Apache 2.0"
      url = "http://www.apache.org/licenses/LICENSE-2.0.html"
    }
    externalDoc {
      description = "Example Doc"
      url="com.example.doc"
    }
  }
}
```

# UI Example
<img src="/doc/UiExample.PNG" width="100%">
