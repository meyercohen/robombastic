_____       _                     _               _   _
 |  __ \     | |                   | |             | | (_)
 | |__) |___ | |__   ___  _ __ ___ | |__   __ _ ___| |_ _  ___
 |  _  // _ \| '_ \ / _ \| '_ ` _ \| '_ \ / _` / __| __| |/ __|
 | | \ \ (_) | |_) | (_) | | | | | | |_) | (_| \__ \ |_| | (__
 |_|  \_\___/|_.__/ \___/|_| |_| |_|_.__/ \__,_|___/\__|_|\___|


Authors: Swamynathan CANDASSAMY & Meyer COHEN

How to compile the project ?

- Open up a terminal
- Go to the directory where you have store the build.xml ant file
- IF NEEDED : Install Ant to have access to ant outside eclipse
              Follow this link : http://ant.apache.org/manual/install.html
- Type the following command line : ant

How to launch the game ?

- Open up a terminal
- Go to the directory where you have stored the CANDASSAMYCOHEN.jar file
- Type the following command line : java -jar CANDASSAMYCOHEN.jar

Is there any parameters that we can use ?

Here is the list of all parameters you can use :

-arena : Allows you to specify a file that contains the description of an arena
-radarSize : Allows you to specify the field of view of the robots
-armyLimit : Allows you to specify the max number of robots per army
             Note that the game takes the minimum between what you have
             entered and the default army limit size of an army
             Default Army size for US Army : 5
             Default Army size for RU Army : 4
             Default Army size for FR Army : 3

-turnDuration : Allows you to specify the duration between two turns
                (in milliseconds)
-bombDuration : Allows you to specify the duration of the bombs
-robotsDir : Allows you to specify a folder which contains the description
             of armies that will fight
-loadSave : Allows you to save the last game save you've made

How to use these parameters ?

Below, you'll find example of possible command line executions :

If you want to customize all the parameters :

java -jar CANDASSAMYCOHEN.jar -arena "arena/arena.txt" -radarSize 2 -armyLimit 4
-turnDuration 1000 -bombDuration 3 -robotsDir "bots"

If you want to load your last save :

java -jar CANDASSAMYCOHEN.jar -loadSave

You can also use some of the above parameters like :

java -jar CANDASSAMYCOHEN.jar -arena "arena/arena2.txt" -turnDuration 500
