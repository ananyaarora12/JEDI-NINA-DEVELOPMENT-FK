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



<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.flipkart</groupId>
    <artifactId>flipfit-dropwizard-rest</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.release>17</maven.compiler.release>
        <dropwizard.version>5.0.0</dropwizard.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>io.dropwizard</groupId>
            <artifactId>dropwizard-core</artifactId>
            <version>${dropwizard.version}</version>
        </dependency>
        <dependency>
            <groupId>io.dropwizard</groupId>
            <artifactId>dropwizard-jetty</artifactId>
            <version>${dropwizard.version}</version>
        </dependency>
        <dependency>
            <groupId>io.dropwizard</groupId>
            <artifactId>dropwizard-db</artifactId>
            <version>${dropwizard.version}</version>
        </dependency>
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <version>8.0.33</version>
        </dependency>
    </dependencies>

    <build>
        <sourceDirectory>.</sourceDirectory>
        <plugins>
            <plugin>
                <groupId>io.dropwizard</groupId>
                <artifactId>dropwizard-maven-plugin</artifactId>
                <version>${dropwizard.version}</version>
                <configuration>
                    <mainClass>com.flipkart.client.FlipFitApplication</mainClass>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.5.1</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <createDependencyReducedPom>false</createDependencyReducedPom>
                            <transformers>
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    <mainClass>com.flipkart.client.FlipFitApplication</mainClass>
                                </transformer>
                            </transformers>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>



