import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DirectionTest {

    private final String[] objectTypes = {Tank.OBJECT_TYPE, Tank.ENEMY_OBJECT_TYPE, Missile.OBJECT_TYPE};

    @Test
    void getImage() {
        for (Direction d : Direction.values()) {
            for (String objType : objectTypes) {
                assertTrue(d.getImage(objType).getWidth(null) > 0);
            }
        }
    }
}