import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TankTest {

    private KeyEvent fromKeyCode(int keyCode) {
        return new KeyEvent(new JButton(), 0, 0, 0, keyCode, (char) keyCode, 0);
    }

    private Field getField(String name) throws NoSuchFieldException {
        Field field = Tank.class.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    private Direction dirOf(Tank tank) throws NoSuchFieldException, IllegalAccessException {
        return (Direction) getField("direction").get(tank);
    }
    
    private boolean isStopped(Tank tank) throws NoSuchFieldException, IllegalAccessException {
        return (boolean) getField("stopped").get(tank);
    }

    @Test
    void determineDirection() throws NoSuchFieldException, IllegalAccessException {
        Tank tank = new Tank(40, 40);

        tank.keyPressed(fromKeyCode(KeyEvent.VK_LEFT));
        assertEquals(Direction.Left, dirOf(tank));
        tank.keyReleased(fromKeyCode(KeyEvent.VK_LEFT));
        assertTrue(isStopped(tank));

        tank.keyPressed(fromKeyCode(KeyEvent.VK_LEFT));
        tank.keyPressed(fromKeyCode(KeyEvent.VK_UP));
        assertEquals(Direction.LeftUp, dirOf(tank));

        tank.keyReleased(fromKeyCode(KeyEvent.VK_LEFT));
        assertEquals(Direction.Up, dirOf(tank));
        tank.keyReleased(fromKeyCode(KeyEvent.VK_UP));
        assertTrue(isStopped(tank));

        tank.keyPressed(fromKeyCode(KeyEvent.VK_LEFT));
        tank.keyPressed(fromKeyCode(KeyEvent.VK_UP));
        tank.keyPressed(fromKeyCode(KeyEvent.VK_DOWN));
        assertTrue(isStopped(tank));

        tank.keyReleased(fromKeyCode(KeyEvent.VK_DOWN));
        assertEquals(Direction.LeftUp, dirOf(tank));
    }
}
