# SparkSwagger
Live Documentation for *Spark* applications using *Swagger* spec.

### Motivation

Create and maintain documentation for API´s is a tedious part of application development. Developing in Spark Framework is not an exception. Its even worse since the current *Swagger* platform (https://swagger.io/) doesn´t have support for *Spark* (http://sparkjava.com/). To contrast these approaches, taking advantage of existing solutions, we'll provide a non-official *Swagger* support for *Spark* application.

Besides *Swagger* spec generation in JSON and YAML formats, this extension uses two open-source libraries in order to 
provide a beautiful documentation interface for any spark application.

<img src="/doc/SparkSwaggerFlow.png" width="100%">

### Used Libraries
 - Swagger UI - is a collection of HTML, Javascript, and CSS assets that dynamically generate beautiful documentation from a Swagger-compliant API (https://github.com/swagger-api/swagger-ui)
 - Swagger UI Themes - is a collection of themes to spice up those default Swagger Docs, ready to be dropped right into any project that needs a new look for your API docs (http://meostrander.com/swagger-ui-themes/).

### Tips
1 - Generated Swagger definitions can be accessed via HTTP directly from runnig Spark-Swagger application. To do that you need to submit a GET **http://hostname:port/doc.yaml** for the *YAML* format and GET **http://hostname:port/doc.json** for the *JSON* format.

2 - Exported Swagger definitions can be imported into Swagger editor (https://swagger.io/swagger-editor/) where you can Generate Server and Client implementations for your *API* in more than 50 programing languages and frameworks.

3 - Generated documentation is served directly from Spark server and can be accessed from **http://hostname:port/** or **http://hostname:port/index.html**.

### Add to your project
Gradle
```groovy
  compile 'com.coriant.sdn.ss:spark-swagger:1.0.0.40'
```
Maven
```xml
   <dependency>
      <groupId>com.coriant.sdn.ss</groupId>
      <artifactId>spark-swagger</artifactId>
      <version>1.0.0.40</version>
   </dependency>
```
 
# Configuration

### How to configure

SparkSwagger accepts configuration parameters from a configuration file named *spark-swagger.conf* that can be placed at *''src/main/resources/''* or another specified directory

### Parameters

Parameters with dots in their names are single strings used to organize subordinate parameters, and are not indicative of a nested structure.

For readability, parameters are grouped by category and sorted alphabetically.

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

##### Example

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
