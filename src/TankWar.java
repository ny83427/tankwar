import com.sun.javafx.application.PlatformImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Code provided here is just for showcase purpose. You need to
 * change them freely to make the game work as expected
 */
class TankWar extends JComponent {
    private static final int WIDTH = 800, HEIGHT = 600;

    private static final int REPAINT_INTERVAL = 50;

    private int x = WIDTH / 2, y = HEIGHT / 2 - 40;
    private int my = HEIGHT / 2 + 50;

    private TankWar() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_CONTROL:
                        Tools.playAudio("shoot.wav");
                        my += 10;
                        break;
                    case KeyEvent.VK_A:
                        Tools.playAudio(Tools.nextBoolean() ? "supershoot.wav" : "supershoot.aiff");
                        my += 10;
                        break;
                    case KeyEvent.VK_LEFT:
                        x -= 5;
                        break;
                    case KeyEvent.VK_UP:
                        y -= 5;
                        break;
                    case KeyEvent.VK_RIGHT:
                        x += 5;
                        break;
                    case KeyEvent.VK_DOWN:
                        y += 5;
                        break;
                }

                if (my >= HEIGHT) {
                    my = Tools.nextInt(HEIGHT);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.DARK_GRAY);
        g.fillRect(250, 100, 300, 20);
        g.fillRect(100, 200, 20, 150);
        g.fillRect(680, 200, 20, 150);

        g.setColor(Color.MAGENTA);
        g.drawImage(Tools.getImage("blood.png"), WIDTH / 2, HEIGHT / 2 - 100, null);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Default", Font.BOLD, 14));
        g.drawString("Missiles: " + Tools.nextInt(10), 10, 50);
        g.drawString("Explodes: " + Tools.nextInt(10), 10, 70);
        g.drawString("Our Tank HP: " + Tools.nextInt(10), 10, 90);
        g.drawString("Enemies Left: " + Tools.nextInt(10), 10, 110);
        g.drawString("Enemies Killed: " + Tools.nextInt(10), 10, 130);

        g.setColor(Color.RED);
        g.fillRect(x, y - 10, 35, 10);
        g.drawImage(Direction.Down.getImage("tank"), x, y, null);

        int dist = (WIDTH - 120) / 11;
        for (int i = 0; i < 12; i++) {
            Direction direction = Direction.values()[i % 8];
            g.drawImage(direction.getImage("tank"), 42 + dist * i, HEIGHT / 2 + 100, null);
        }

        g.drawImage(Direction.Left.getImage("missile"), WIDTH / 2 - 18, my, null);
        g.drawImage(Direction.Up.getImage("missile"), WIDTH / 2, my - 18, null);
        g.drawImage(Direction.Right.getImage("missile"), WIDTH / 2 + 14, my, null);
        g.drawImage(Direction.Down.getImage("missile"), WIDTH / 2, my + 12, null);
        g.drawImage(Direction.LeftUp.getImage("missile"), WIDTH / 2 - 12, my - 18, null);
        g.drawImage(Direction.RightUp.getImage("missile"), WIDTH / 2 + 12, my - 18, null);
        g.drawImage(Direction.RightDown.getImage("missile"), WIDTH / 2 + 12, my + 12, null);
        g.drawImage(Direction.LeftDown.getImage("missile"), WIDTH / 2 - 12, my + 12, null);

        g.drawImage(Tools.getImage((step++) % 11 + ".gif"), WIDTH / 2, 100, null);
    }

    private int step = 0;

    private void start() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                while (true) {
                    try {
                        repaint();
                        Tools.sleepSilently(REPAINT_INTERVAL);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }

    public static void main(String[] args) {
        PlatformImpl.startup(() -> {});
        Tools.setTheme();
        JFrame frame = new JFrame("Tank War");
        frame.setIconImage(Tools.getImage("/icon.png"));
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocation(400, 100);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);

        TankWar tankWar = new TankWar();
        frame.add(tankWar);
        tankWar.setFocusable(true);
        frame.setVisible(true);
        tankWar.start();
    }
}

