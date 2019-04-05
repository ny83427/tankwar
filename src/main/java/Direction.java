import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * Eight directions of game objects, such as {@link Tank}, {@link Missile} and etc.
 * Left, Up, Right, Down and etc. To simplify direction detection, we will use
 * bit operation to detect: 0000(Down Right Up Left), value will be filled accordingly.
 * </pre>
 */
enum Direction {
    Left("L", -1, 0, 1),
    LeftUp("LU", -1, -1, 3),
    Up("U", 0, -1, 2),
    RightUp("RU", 1, -1, 6),
    Right("R", 1, 0, 4),
    RightDown("RD", 1, 1, 12),
    Down("D", 0, 1, 8),
    LeftDown("LD", -1, 1, 9);

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

    final int code;

    Direction(String abbrev, int xFactor, int yFactor, int code) {
        this.abbrev = abbrev;
        this.xFactor = xFactor;
        this.yFactor = yFactor;
        this.code = code;
    }

    static Direction get(int code) {
        for (Direction dir : Direction.values()) {
            if (dir.code == code) {
                return dir;
            }
        }
        return null;
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
        return CACHE.computeIfAbsent(objectType + this.abbrev,
            key -> Tools.getImage(key + ".gif"));
    }
}
