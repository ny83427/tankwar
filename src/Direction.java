import java.awt.*;
import java.util.HashMap;
import java.util.Map;

enum Direction {
    Left("L", -1, 0),
    LeftUp("LU", -1, -1),
    Up("U", 0, -1),
    RightUp("RU", 1, -1),
    Right("R", 1, 0),
    RightDown("RD", 1, 1),
    Down("D", 0, 1),
    LeftDown("LD", -1, 1);

    private final String abbrev;

    /**
     * <pre>
     * factor to multiply with horizontal moving speed.
     * for example, if tank is located at (x, y) currently
     * with a certain moving speed, the next location will
     * be (x - speed, y) if it's moving toward left direction.
     * Thus the factor of Left direction will be -1
     * </pre>
     */
    final int xFactor;

    /**
     * factor to multiply with vertical moving speed
     */
    final int yFactor;

    Direction(String abbrev, int xFactor, int yFactor) {
        this.abbrev = abbrev;
        this.xFactor = xFactor;
        this.yFactor = yFactor;
    }

    private static final Map<String, Image> CACHE = new HashMap<>();

    /**
     * <pre>
     * get image of current object in giving direction, based on convention over configuration
     * image name should follow pattern of "${objectType}${direction.abbrev}.gif", for example:
     * "tankD.gif", "missileL.gif", it can be used to determine the image to draw for game objects
     * like tank, missile and etc.
     * </pre>
     * @param objectType    object type
     */
    Image getImage(String objectType) {
        return CACHE.computeIfAbsent(objectType + this.abbrev, key -> Tools.getImage(key + ".gif"));
    }
}
