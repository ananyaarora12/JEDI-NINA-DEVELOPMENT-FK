 # FlipFit - Setup Guide
 
 ## Requirements (Another Laptop)
 - macOS/Linux/Windows
 - Java JDK 17+ (or JDK 11+)
 - MySQL Server 8.x (running)
 - MySQL client CLI (`mysql`)
 - MySQL Connector/J jar (JDBC driver)
 - Git
 
 ## Steps to Run (Another Laptop)
 1) Clone the repo
 ```bash
 git clone <YOUR_REPO_URL>
 cd JEDI-FLIPFIT-DEVELOPMENT-PROJECT
 ```
 
 2) Configure database credentials
 - Edit `JEDI-GROUP-E-FLIPFIT-POS/src/config.properties` and set:
 ```
 db_url=jdbc:mysql://localhost:3306/flipfit
 db_user=root
 db_password=YOUR_PASSWORD
 ```
 - If you get `Public Key Retrieval is not allowed`, use:
 ```
 db_url=jdbc:mysql://localhost:3306/flipfit?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
 ```
 
 3) Create database and tables
 ```bash
 mysql -u root -p < JEDI-GROUP-E-FLIPFIT-POS/src/schema.sql
 ```
 
 4) Add MySQL Connector/J jar
 - Download MySQL Connector/J from MySQL site or install via package manager.
 - Put the jar inside a local `lib/` folder at repo root.
 
 5) Compile the project
 ```bash
 mkdir -p out
 javac -d out -cp "lib/mysql-connector-j-*.jar" $(/usr/bin/find JEDI-GROUP-E-FLIPFIT-POS/src -name "*.java")
 ```
 
 6) Run the app
 ```bash
 java -cp "out:lib/mysql-connector-j-*.jar" com.flipkart.client.FlipFitApplication
 ```



 ananya.arora@FK1089-258285L JEDI-FLIPFIT-NINA-DROPWIZARD-REST % java -jar target/flipfit-dropwizard-rest-1.0.0.jar server config.yml
io.dropwizard.configuration.ConfigurationParsingException: config.yml has an error:
  * Failed to parse configuration at: server.applicationConnectors.[0]; Could not resolve type id 'http' as a subtype of `io.dropwizard.jetty.ConnectorFactory`: known type ids = [] (for POJO property 'applicationConnectors')
 at [Source: UNKNOWN; byte offset: #UNKNOWN] (through reference chain: com.flipkart.client.FlipFitConfiguration["server"]->io.dropwizard.core.server.DefaultServerFactory["applicationConnectors"]->java.util.ArrayList[0])

        at io.dropwizard.configuration.ConfigurationParsingException$Builder.build(ConfigurationParsingException.java:277)
        at io.dropwizard.configuration.BaseConfigurationFactory.build(BaseConfigurationFactory.java:177)
        at io.dropwizard.configuration.BaseConfigurationFactory.build(BaseConfigurationFactory.java:94)
        at io.dropwizard.core.cli.ConfiguredCommand.parseConfiguration(ConfiguredCommand.java:139)
        at io.dropwizard.core.cli.ConfiguredCommand.run(ConfiguredCommand.java:85)
        at io.dropwizard.core.cli.Cli.run(Cli.java:78)
        at io.dropwizard.core.Application.run(Application.java:94)
        at com.flipkart.client.FlipFitApplication.main(FlipFitApplication.java:11)
Caused by: com.fasterxml.jackson.databind.exc.InvalidTypeIdException: Could not resolve type id 'http' as a subtype of `io.dropwizard.jetty.ConnectorFactory`: known type ids = [] (for POJO property 'applicationConnectors')
 at [Source: UNKNOWN; byte offset: #UNKNOWN] (through reference chain: com.flipkart.client.FlipFitConfiguration["server"]->io.dropwizard.core.server.DefaultServerFactory["applicationConnectors"]->java.util.ArrayList[0])
        at com.fasterxml.jackson.databind.exc.InvalidTypeIdException.from(InvalidTypeIdException.java:43)
        at com.fasterxml.jackson.databind.DeserializationContext.invalidTypeIdException(DeserializationContext.java:2096)
        at com.fasterxml.jackson.databind.DeserializationContext.handleUnknownTypeId(DeserializationContext.java:1645)
        at com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase._handleUnknownTypeId(TypeDeserializerBase.java:299)
        at com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase._findDeserializer(TypeDeserializerBase.java:164)
        at com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer._deserializeTypedForId(AsPropertyTypeDeserializer.java:150)
        at com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer.deserializeTypedFromObject(AsPropertyTypeDeserializer.java:135)
        at com.fasterxml.jackson.databind.deser.AbstractDeserializer.deserializeWithType(AbstractDeserializer.java:262)
        at com.fasterxml.jackson.databind.deser.std.CollectionDeserializer._deserializeNoNullChecks(CollectionDeserializer.java:503)
        at com.fasterxml.jackson.databind.deser.std.CollectionDeserializer._deserializeFromArray(CollectionDeserializer.java:358)
        at com.fasterxml.jackson.databind.deser.std.CollectionDeserializer.deserialize(CollectionDeserializer.java:245)
        at com.fasterxml.jackson.databind.deser.std.CollectionDeserializer.deserialize(CollectionDeserializer.java:29)
        at com.fasterxml.jackson.module.blackbird.deser.SettableObjectProperty.deserializeAndSet(SettableObjectProperty.java:44)
        at com.fasterxml.jackson.databind.deser.BeanDeserializer.vanillaDeserialize(BeanDeserializer.java:302)
        at com.fasterxml.jackson.databind.deser.BeanDeserializer._deserializeOther(BeanDeserializer.java:207)
        at com.fasterxml.jackson.module.blackbird.deser.SuperSonicBeanDeserializer.deserialize(SuperSonicBeanDeserializer.java:120)
        at com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer._deserializeTypedForId(AsPropertyTypeDeserializer.java:169)
        at com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer.deserializeTypedFromObject(AsPropertyTypeDeserializer.java:135)
        at com.fasterxml.jackson.databind.deser.AbstractDeserializer.deserializeWithType(AbstractDeserializer.java:262)
        at com.fasterxml.jackson.databind.deser.impl.MethodProperty.deserializeAndSet(MethodProperty.java:138)
        at com.fasterxml.jackson.databind.deser.BeanDeserializer.deserialize(BeanDeserializer.java:265)
        at com.fasterxml.jackson.module.blackbird.deser.SuperSonicBeanDeserializer.deserialize(SuperSonicBeanDeserializer.java:155)
        at com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.readRootValue(DefaultDeserializationContext.java:342)
        at com.fasterxml.jackson.databind.ObjectMapper._readValue(ObjectMapper.java:5011)
        at com.fasterxml.jackson.databind.ObjectMapper.readValue(ObjectMapper.java:3104)
        at io.dropwizard.configuration.BaseConfigurationFactory.build(BaseConfigurationFactory.java:148)
        ... 6 more
