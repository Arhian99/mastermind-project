# Welcome to Mastermind!

### Follow the instructions below to build and run the application:

1. Follow **[these instructions](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm#using-a-node-version-manager-to-install-nodejs-and-npm)** to download and install Node.js and npm on your local machine.
    - To see if you already have Node.js and npm installed  and check the installed version run the following commands:
      - `node -v`
      - `npm -v`
    - NOTE: This application was developed using `Node.js v18.14.2` and `npm v9.5.0`

2. Follow **[these instructions](https://docs.oracle.com/en/java/javase/21/install/overview-jdk-installation.html#GUID-8677A77F-231A-40F7-98B9-1FD0B48C346A)** to download and install Java Development Kit (JDK) on your local machine. 
    - To see if you already have JDK installed run the following command:
        - `java -version` 
    - NOTE: This application was developed using `java version 20.0.1`

3. Ensure that the `JAVA_HOME` environment variable is set to match the location of your java installation. 
    - Follow [these instructions](https://confluence.atlassian.com/doc/setting-the-java_home-variable-in-windows-8895.html) to set the `JAVA_HOME` environment variable on **windows**.  
      - On windows, the path of your Java JDK installation is usually `.\Program Files\Java\jdk-xx`, where `xx` is the feature release number.
      - To confirm the value of the `JAVA_HOME` environment variable run the command `echo %JAVA_HOME%`
    - Follow [these instructions](http://www.sajeconsultants.com/how-to-set-java_home-on-mac-os-x/) to set the `JAVA_HOME` environment variable on **macOS**.
      - On macOS, the path of your Java JDK installation is usually `./Library/Java/JavaVirtualMachines/xxxx.jdk`
      - To confirm the value of the `JAVA_HOME` environment variable run the command `echo $JAVA_HOME`
4. Install Maven on you local machine:
   - Follow [these instructions](https://www.digitalocean.com/community/tutorials/install-maven-mac-os#2-install-maven-on-mac-os) on **_macOS_**
   - Follow [these instructions](https://phoenixnap.com/kb/install-maven-windows) on **_windows_**
      - **_NOTE:_** Run `mvn -version` to verify maven installation
5. Copy the **_absolute path_** of the directory inside the root of this project named `mastermind-db` 
    - EX: `../mastermind-project/mastermind-db`
    - Ensure you replace the "`..`" with the absolute path.
7. Open the `application.properties` file located in the following directory: \
`../linkedin-project_backend/src/main/resources`

1. Set the following property to the path that points to the directory you just made. \
`de.flapdoodle.mongodb.embedded.databaseDir=<PATH>
`
    - **_NOTE:_** This is an embedded instance of mongoDB which runs locally on application startup. It is to be used ONLY for testing purposes therefore its storage capacity is limited. 
    - **_[Link to flapdoodle Github Repository](https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo)_**

1. ### To Run the Frontend:
   1. From the command line: cd into `linkedin-project_frontend` directory and run `npm install` to install the frontend dependencies.
   2. Then run `npm start` to run the frontend application in development mode. 
   3. Open [http://localhost:3000](http://localhost:3000) to view it in your browser.

2. ### To Run the Backend:
   1. From a command line which has a maven instance installed and has the JAVA_HOME environment variable set: cd into `linkedin-project_backend` directory and run:
        #### `mvn spring-boot:run`
   2. The backend will run on http://localhost:8080
   3. The mongoDB instance will run on http://localhost:27018


