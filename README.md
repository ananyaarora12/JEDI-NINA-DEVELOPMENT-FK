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
