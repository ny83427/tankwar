import java.awt.*;

class Explode extends GameObject {

    private int step;

    private static final Image[] IMAGES = new Image[11];
    static {
        for (int i = 0; i < IMAGES.length; i++) {
            IMAGES[i] = Tools.getImage(i + ".gif");
        }
    }

    Explode(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    void draw(Graphics g) {
        // display 10 images continuously to simulate animation effect and end after last frame
        boolean live = step < IMAGES.length;
        if (!live) {
            this.setLive(false);
            return;
        }

        g.drawImage(IMAGES[step++], x, y, null);
        if (step == 1) {
            Tools.playAudio("explode.wav");
        }
    }

    @Override
    Rectangle getRectangle() {
        return new Rectangle(x, y, IMAGES[step].getWidth(null), IMAGES[step].getHeight(null));
    }

}
