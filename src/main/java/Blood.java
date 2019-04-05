import java.awt.*;

class Blood extends GameObject {
    private int step;

    private final int[][] points = {
        {380, 260}, {360, 300}, {375, 275}, {400, 200}, {360, 270}, {365, 290}, {340, 280}
    };

    private final Image image;
    private final int width, height;

    Blood() {
        this.x = points[0][0];
        this.y = points[0][1];
        this.image = Tools.getImage("blood.png");
        this.width = this.image.getWidth(null);
        this.height = this.image.getHeight(null);
    }

    @Override
    void draw(Graphics g) {
        g.drawImage(image, x, y, null);
        step++;
        step %= points.length;
        x = points[step][0];
        y = points[step][1];
    }

    @Override
    Rectangle getRectangle() {
        return new Rectangle(x, y, width, height);
    }
}
