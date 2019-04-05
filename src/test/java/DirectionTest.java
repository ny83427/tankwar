import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DirectionTest {

    private final String[] objectTypes = {Tank.OBJECT_TYPE, Tank.ENEMY_OBJECT_TYPE, Missile.OBJECT_TYPE};

    @Test
    void getImage() {
        for (Direction d : Direction.values()) {
            for (String objType : objectTypes) {
                assertNotNull(d.getImage(objType));
            }
        }
    }
}