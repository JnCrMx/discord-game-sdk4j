# discord-game-sdk4j

[![](https://jitpack.io/v/JnCrMx/discord-game-sdk4j.svg)](https://jitpack.io/#JnCrMx/discord-game-sdk4j)

This project provides Java bindings for the
[Discord GameSDK](https://discordapp.com/developers/docs/game-sdk/sdk-starter-guide).

To be honest I'm not sure if people even need this, because Discord apparently discarded its game store idea.

But maybe the activity, overlay, user, and relationship features could be useful to some people.

## Installation 

### Pre-compiled

#### Maven, Gradle and other build tools

There are pre-compiled builds on [JitPack](https://jitpack.io/#JnCrMx/discord-game-sdk4j)
together with instructions how to use them for all common build tools.

#### Manual installation

For projects not using any build tools, download a pre-compiled JAR-file (``discord-game-sdk4j-<version>.jar``)
from the [releases page](https://github.com/JnCrMx/discord-game-sdk4j/releases).

If you want, you can also download the JavaDocs (``discord-game-sdk4j-<version>-javadoc.jar``) or
the sources (``discord-game-sdk4j-<version>-sources.jar``).

You do **not** need to download the .dll or .so files! They are packed in the JAR and will be automatically extracted.

After downloading those JARs, just add the main JAR to your project's classpath and optionally
attach sources or JavaDocs.

### Building from source

To install the library from source first of all clone the repository:
```shell script
git clone https://github.com/JnCrMx/discord-game-sdk4j.git
```
To obtain the native libraries you can build them from source too (see below) or just download them:
````shell script
sh jitpack-download-natives.sh
````

Finally build (and install) the library with Maven:
````shell script
mvn install -Dmaven.antrun.skip=true
````

If you want to skip the tests (sometimes they fail for really weird reasons), add ``-DskipTests`` to the command arguments.

#### Building the native library from source

To build the native libraries from source make sure you have CMake, a compiler that works with CMake (e.g. gcc)
and a JDK11 installed and properly set up.

Then download [Discord's native library](https://dl-game-sdk.discordapp.net/latest/discord_game_sdk.zip)
and extract it to ``./discord_game_sdk/``.

The CMake build system is integrated in Maven, so just execute to following command to
build and install the Java and native library:

```shell script
mvn install
```

## Usage

To use the library, you first need to download [Discord's native library](https://dl-game-sdk.discordapp.net/latest/discord_game_sdk.zip).
Extract the ZIP file and remember where you put it.

In code the first step is initializing the Core. To do this you need to pass the path to Discord's native library as an argument.
You can find this library in the directory you just extracted the ZIP file at ``lib/x86_64/discord_game_sdk.dll`` (for 64-bit Windows)
and ``lib/x86_64/discord_game_sdk.so`` (for 64-bit Linux):

```java
Core.init(new File("<path to the native library>"));
```

Now you are ready to use the library!

````java
try(CreateParams params = new CreateParams())
{
    params.setClientID(<your application ID as a long>);
    params.setFlags(CreateParams.getDefaultFlags());

    try(Core core = new Core(params))
    {
        // do something with your Core
    }
}
````

For real examples see the ``examples/`` directory in this repository.