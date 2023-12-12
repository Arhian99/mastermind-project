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
6. Open the `application.properties` file located in the following directory: \
`../linkedin-project_backend/src/main/resources`

7. Set the following property to the path that points to the directory you just made. \
`de.flapdoodle.mongodb.embedded.databaseDir=<PATH>
`
    - **_NOTE:_** This is an embedded instance of mongoDB which runs locally on application startup. It is to be used ONLY for testing purposes therefore its storage capacity is limited. 
    - **_[Link to flapdoodle Github Repository](https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo)_**

8. ### To Run the Frontend:
   1. From the command line: cd into `linkedin-project_frontend` directory and run `npm install` to install the frontend dependencies.
   2. Then run `npm start` to run the frontend application in development mode. 
   3. Open [http://localhost:3000](http://localhost:3000) to view it in your browser.

9. ### To Run the Backend:
   1. From a command line which has a maven instance installed and has the JAVA_HOME environment variable set: cd into `linkedin-project_backend` directory and run:
        #### `mvn spring-boot:run`
   2. The backend will run on http://localhost:8080
   3. The mongoDB instance will run on http://localhost:27018

--------------------------------------------------------------------------------
## Features and Creative Extensions

### Configurable Difficulty level:
- Players are allowed to pick between Easy, Medium, or Hard difficulty.
  - **_Easy:_** The codebreaker gets 10 attempts, the secret code has a length of 4 digits, and each digit ranges from 0-7, inclusive.
  - **_Medium:_** The codebreaker gets 9 attempts, the secret code has a length of 5 digits, and each digit ranges from 0-8, inclusive.
  - **_Hard:_** The codebreaker gets 8 attemtps, the secret code has a length of 6 digits, and each digit ranges from 0-9, inclusive.

### Configurable Repeated Digits:
- Players are allowed to pick whether they would allow the secret code to have repeated digits or not.
  - **_On:_** The secret code is allowed to have repeated digits, since it is a randomly generated number (in singleplayer mode) it may or it may not.
  - **_Off:_** The secret code is NOT allowed to have repeated digits, therefore each digit in the secret code only shows up once.

### Game Session Persistence:
- Both singleplayer and multiplayer modes allow players to leave a game session at any time and return to the game session using the sessionID, without loosing any progress or data.
- Use the "Continue Game" button on the home page and enter the player credentials (username and password), the sessionID associated with the game session, as well as whether the session was a singleplayer or a multiplayer session and the system will allow you to resume this session OR it will allow you to view old sessions that have already ended. 
  - **_NOTE:_** I used an embedded instance of mongoDB which runs locally on application startup. It is to be used **_ONLY_** for testing purposes therefore its storage capacity is limited. 
    - **_[Link to flapdoodle Github Repository](https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo)_**

### Multiplayer Mode:
- This application supports multiplayer sessions for users in different clients. 
- The player that starts the game session gets to pick whether they would like to play as the codemaker (the person who sets the code) or the codebreaker (the person who attempts to break the code). And they get to pick a sessionID.
  - Note: even though the code is not randomly generated in multiplayer mode, the system still validates the code to ensure it follows the set game settings (difficulty level and repeated digits) and prompts the user to enter a new code if the code entered does not follow the game settings.
- After a game session has been created the player that started the session can share the sessionID with the other player and the other player can join the session. The player that joins the session will be assigned the vacant role (the role that wasn't chosen by the player that started the session).
  - The second player can join the session using the "Continue Game" button in the Home Page
- Either player can leave the session and come back to it at a later time.
  - Note: To test this feature one session should be opened in a browser window and the other player should join the session in a separate "Incognito" browser window.

### Player Authentication and Persistence:
- Every player is authenticated before starting a game session.
  - If the username has never been used in the application then the system creates a new player objet with the associated username and _hashed_ password and persists this new player object to the database.
  - Upon every subsequent authentication attempt the system authenticates the player.
    - I used a **[Bcrypt Java Library](https://github.com/patrickfav/bcrypt)** which is an implementation of the OpenBSD Blowfish password hashing algorithm.
    - **[Link to patrickfav Github Repository](https://github.com/patrickfav/bcrypt)** 
- This makes future exenstions alot easier to implement, in the future one could keep a total number of wins per player, total number of game sessions played, total score/points once a point system has been implemented, etc...

--------------------------------------------------------------------------------
## Code Structure/ Thought Process

- As per project instructions my main focus was on the backend and the non-visible application business logic, therefore while the frontend is well organized it is not documented with in-code comments. The frontend also has small UI bugs that shows text outside the borders when resizing the browser window, These bugs do not affect gameplay, data transfer or the backend. 
- This is a full stack web application written using JavaScript (React) for the frontend and Java (SpringBoot) for the backend.
- **_linkedin-project_frontend_** houses the react application, the frontend dependencies and all the frontend (UI) code.
- **_linkedin-project_backend_** houses the SpringBoot application and all the related backend code. 
  - I attempted to develop the backend following an architecture similar to the **_Model View Controller (MVC) architecture_**, with the "View" component being the frontend react application. 
    - The **controllers package** houses **_AuthController_** and **_GameController_** classes which in turn contain methods that exposes API endpoints to the frontend. These two classes contain all the methods that handle requests from the frontend to authenticate a player, retrieve a game session, create a new game session, set the secret for a specific game session, post a guess, etc. 
      - These controllers in turn call methods that are housed in classes located in **services package** to perform application business logic. 
    - The **services package** contains classes which in turn contain methods that perform all necessary application business logic.
      - The **_AuthenticationService_** class contains methods for parsing an Authorization header and authenticating a player.
      - The methods in the **_RandomNumberService_** class contains all the methods related to calling the external Random Number API, parsing the response, and generating a secret code according to the game settings.
      - The **_PlayerService_** class exposes methods that in turn interact with the PlayerRepository class to persist and/or retrieve Player models from the database.
      - Lastly, **_GameService_** class contains all the methods that initiate a new game session, persist a game session to te database, checks the winner, generates the feedback for a certain guess, etc... The methods within these classes are the "game engine" if you will.
  - The **repos package** houses **_Repository interfaces_** which extend the MondoDB repository interface which exposes method for performing **_CRUD operations_** on the **mongoDB database**.
  - The **models package** contains the two proper model classes of the application (which are persisted to the database as documents) which are the **_Player_** and **_GameSession_** classes. In addition this package also contains key data types utilized throughout the application such as **_GameSettings_** **_EDifficulty_** and **_Guess_**.
  - **DTOs package** contains data transfer objects which are POJOs that represent data coming and going to the frontend.
  - Lastly, the **exceptionHandling package** contains all the custom Exception classes used throughout the application as well as method that catches these exceptions and returns appropriate responses to the frontend.
