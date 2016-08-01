# LinkstoneStub

Generates empty methods from CraftBukkit/NMS classes.

### Building

LinkstoneStub is built using Maven 3.x. 

Once Maven is setup, use `mvn pacakge` in the root directory (containing the *pom.xml* file). Shortly, the executable archive will be created in the *target* directory under the name `linkstone-stub-1.0-SNAPSHOT.jar`.

### Usage

1.  Build LinstoneStub to get an archive using the steps above
2.  Download msysgit or an equivalent bash-capable git client
3.  Download a fresh copy of [BuildTools](https://hub.spigotmc.org/jenkins/job/BuildTools/lastBuild/), create a new folder named `BuildTools` and move the downloaded file into that folder
4.  From GIT Bash, run `java -jar BuildTools.jar`. For a specific Minecraft version, use `java -jar BuildTools.jar --rev <version>`
5.  Wait for the build to finish (might take a few minutes). Once complete, run `java -jar linkstone-stub-1.0-SNAPSHOT.jar <pathToBuildToolsDir>` from inside the *target* directory. Replace `<pathToBuildToolsDir>` with the full path of the BuildTools directory (named `BuildTools` as described in step 3.)
6.  The output source files will be located inside the *src* directory next to your executable `linkstone-stub.jar` archive.  
