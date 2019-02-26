import java.awt.*;
import java.util.List;

class Missile extends GameObject {
    private static final int SPEED = 10;

    static final int WIDTH = 10, HEIGHT = 10;

    static final String OBJECT_TYPE = Missile.class.getName().toLowerCase();

    private final Direction dir;

    private boolean isEnemy;

    private Missile(int x, int y, Direction dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    Missile(int x, int y, boolean isEnemy, Direction dir) {
        this(x, y, dir);
        this.isEnemy = isEnemy;
    }

    void draw(Graphics g) {
        g.drawImage(dir.get(OBJECT_TYPE), x, y, null);
        x += dir.xFactor * SPEED;
        y += dir.yFactor * SPEED;
        if (x < 0 || x > TankWar.WIDTH || y < 0 || y > TankWar.HEIGHT) {
            this.setLive(false);
        }
    }

    @Override
    Rectangle getRectangle() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    private static final int LETHALITY = 20;

    boolean hitTank(Tank tank) {
        if (!this.isLive() || !tank.isLive() || this.isEnemy == tank.isEnemy() ||
            !this.getRectangle().intersects(tank.rectangleForMissileHit())) {
            return false;
        }

        if (!tank.isEnemy()) {
            if (!tank.isIronSkin()) {
                tank.setHp(tank.getHp() - LETHALITY);
                if (tank.getHp() <= 0) {
                    tank.setLive(false);
                }
            }
        } else {
            tank.setLive(false);
        }

        this.setLive(false);
        TankWar.getInstance().addExplode(new Explode(x, y));
        return true;
    }

    void hitTanks(List<Tank> tanks) {
        for (Tank tank : tanks) {
            if (this.hitTank(tank)) {
                break;
            }
        }
    }

    void hitWalls(List<Wall> walls) {
        for (Wall wall : walls) {
            if (this.isCollidedWith(wall)) {
                this.setLive(false);
                break;
            }
        }
    }
}
