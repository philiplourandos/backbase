# 

# Running

To run the application run the following in the terminal of this project:

```
mvn spring-boot:run
```

The following output should be displayed:

```
[INFO] --- spring-boot-maven-plugin:2.1.6.RELEASE:run (default-cli) @ mancala-rest-service ---

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.1.8.RELEASE)

2019-09-08 12:37:42.876  INFO 72090 --- [           main] c.b.m.s.MancalaRestServiceApplication    : Starting MancalaRestServiceApplication on Philips-MBP.home with PID 72090 (/Users/philiplourandos/dev/projects/misc/backbase/mancala-rest-service/target/classes started by philiplourandos in /Users/philiplourandos/dev/projects/misc/backbase/mancala-rest-service)
2019-09-08 12:37:42.887  INFO 72090 --- [           main] c.b.m.s.MancalaRestServiceApplication    : No active profile set, falling back to default profiles: default
2019-09-08 12:37:45.558  INFO 72090 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2019-09-08 12:37:45.654  INFO 72090 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2019-09-08 12:37:45.655  INFO 72090 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.24]
2019-09-08 12:37:45.867  INFO 72090 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2019-09-08 12:37:45.867  INFO 72090 --- [           main] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 2875 ms
2019-09-08 12:37:46.751  INFO 72090 --- [           main] pertySourcedRequestMappingHandlerMapping : Mapped URL path [/v2/api-docs] onto method [public org.springframework.http.ResponseEntity<springfox.documentation.spring.web.json.Json> springfox.documentation.swagger2.web.Swagger2Controller.getDocumentation(java.lang.String,javax.servlet.http.HttpServletRequest)]
2019-09-08 12:37:47.245  INFO 72090 --- [           main] d.s.w.p.DocumentationPluginsBootstrapper : Context refreshed
2019-09-08 12:37:47.292  INFO 72090 --- [           main] d.s.w.p.DocumentationPluginsBootstrapper : Found 1 custom documentation plugin(s)
2019-09-08 12:37:47.376  INFO 72090 --- [           main] s.d.s.w.s.ApiListingReferenceScanner     : Scanning for api listing references
2019-09-08 12:37:47.720  INFO 72090 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2019-09-08 12:37:47.729  INFO 72090 --- [           main] c.b.m.s.MancalaRestServiceApplication    : Started MancalaRestServiceApplication in 5.497 seconds (JVM running for 11.456)
```

Documentation from swagger can be access via this URI:

```
http://localhost:8080/swagger-ui.html
```

This will list the operations the webservice exposes. The service can also be invoked via this mechanism