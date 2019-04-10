import java.awt.*;

class Blood extends GameObject {
    private static final int JUMP_DIST = 25;
    private int step;

    private final Image image;
    private final int[][] points;
    private final int width, height;

    Blood() {
        this.image = Tools.getImage("blood.png");
        this.width = this.image.getWidth(null);
        this.height = this.image.getHeight(null);
        this.x = TankWar.WIDTH / 2 - this.width / 2;
        this.y = TankWar.HEIGHT * 3 / 10;
        int[][] dirs = new int[][]{{-1, 0, 1}, {-1, 0, 1}};
        this.points = new int[9][2];
        this.points[0] = new int[]{x, y};
        int index = 1;
        for (int horizontal : dirs[0]) {
            for (int vertical : dirs[1]) {
                if (horizontal == 0 && vertical == 0)
                    continue;
                this.points[index++] = new int[]{
                    x + horizontal * JUMP_DIST + Tools.nextInt(-5, 6),
                    y + vertical * JUMP_DIST + Tools.nextInt(-4, 5)
                };
            }
        }
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
