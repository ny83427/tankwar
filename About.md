## About the Solution

* It's highly recommended to use JDK8 as it ships with JavaFX packages.
If you use JDK11 or newer versions, you need to setup JavaFX libraries
yourself, in that case I would suggest you using Maven to manage
dependencies(you can convert to Maven project in IDEA easily):

    ```xml
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-media</artifactId>
        <version>11.0.2</version>
    </dependency>
    ```

* You might be surprised to see certain old style `for` loops, however that's
purposely written to avoid introducing `CopyOnWriteArrayList` or relatively complex
thread safe handling, you can figure it out yourself via comparing two code snippets
in the below in different contexts:

    ```java
    for (int i = 0; i < objects.size(); i++) {
        this.drawGameObject(objects.get(i), g);
    }
    ```

    VS

    ```java
    for (GameObject obj : objects) {
        this.drawGameObject(obj, g);
    }
    ```

* You can press F11 to switch to IRON SKIN mode for testing purpose or what ever

* This is a tiny project aiming to make students warm up with basic OOP design and
implementation, there are obviously certain simplified implementation seem naive,
and you are always welcome to improve it to the level you want

* You are encouraged to implement a Client/Server TankWar which supports PVP

* You are encouraged to improve enemy tank's AI so that they won't be too foolish

* You are encouraged to make game easily customizable, such as screen width/height,
moving speed of Tank and Missile and etc. You can also introduce more powerful
weapons or bombs that can be equipped or used by player tank...To Infinite and Beyond!

* And finally, Happy Coding! [Java Never Sleep](https://www.javaneversleep.com)!
![](assets/images/happy-coding.jpg)