import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TankTest {

    private KeyEvent fromKeyCode(int keyCode) {
        return new KeyEvent(new JButton(), 0, 0, 0, keyCode, (char) keyCode, 0);
    }

    @Test
    void determineDirection() {
        Tank tank = new Tank(40, 40);

        tank.keyPressed(fromKeyCode(KeyEvent.VK_LEFT));
        assertEquals(Direction.Left, tank.direction());
        tank.keyReleased(fromKeyCode(KeyEvent.VK_LEFT));
        assertNull(tank.direction());

        tank.keyPressed(fromKeyCode(KeyEvent.VK_LEFT));
        tank.keyPressed(fromKeyCode(KeyEvent.VK_UP));
        assertEquals(Direction.LeftUp, tank.direction());

        tank.keyReleased(fromKeyCode(KeyEvent.VK_LEFT));
        assertEquals(Direction.Up, tank.direction());
        tank.keyReleased(fromKeyCode(KeyEvent.VK_UP));
        assertNull(tank.direction());

        tank.keyPressed(fromKeyCode(KeyEvent.VK_LEFT));
        tank.keyPressed(fromKeyCode(KeyEvent.VK_UP));
        tank.keyPressed(fromKeyCode(KeyEvent.VK_DOWN));
        assertNull(tank.direction());

        tank.keyReleased(fromKeyCode(KeyEvent.VK_DOWN));
        assertEquals(Direction.LeftUp, tank.direction());
    }
}
