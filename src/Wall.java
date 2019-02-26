import java.awt.*;

class Wall extends GameObject {
    private final Image image;

    private final boolean horizontal;
    private final int brickCount;

    Wall(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.image = Tools.getImage("brick.png");
        this.horizontal = w > this.image.getWidth(null);
        if (this.horizontal) {
            this.brickCount = w / this.image.getWidth(null);
        } else {
            this.brickCount = h / this.image.getHeight(null);
        }
    }

    @Override
    void setLive(boolean live) {
        throw new UnsupportedOperationException("Dude, how would you set a wall to dead?");
    }

    @Override
    void draw(Graphics g) {
        for (int i = 0; i < brickCount; i++) {
            int deltaX = horizontal ? i * image.getWidth(null) : 0;
            int deltaY = horizontal ? 0 : i * image.getHeight(null);
            g.drawImage(image, x + deltaX, y + deltaY, null);
        }
    }

    @Override
    Rectangle getRectangle() {
        if (horizontal)
            return new Rectangle(x, y, brickCount * image.getWidth(null), image.getHeight(null));
        else
            return new Rectangle(x, y, image.getWidth(null), brickCount * image.getHeight(null));
    }
}
