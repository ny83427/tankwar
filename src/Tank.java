import java.awt.*;
import java.awt.event.KeyEvent;

class Tank extends GameObject {
    /**
     * It would be better if width and height are retrieved from image of current direction.
     * However, due to asset not prepared well accordingly, they are different and would
     * result in player tank cannot move sometimes, this is a simply workaround to avoid
     * complex handling when collision happens
     */
    private static final int WIDTH = 44, HEIGHT = 44;

    private static final int SPEED = 5;

    private static final int BLOOD_BAR_HEIGHT = 10;

    private static final int MAX_HP = 100;

    private static final int LOW_HP_THRESHOLD = 50;

    private static final String OBJECT_TYPE = Tank.class.getName().toLowerCase();

    private int hp = MAX_HP;

    private Direction direction;

    void stop() {
        this.direction = null;
    }

    private Direction previousDirection = Direction.Down;

    private final boolean isEnemy;

    private Image pet;

    private int petWidth;

    Tank(int x, int y) {
        this(x, y, false);
    }

    Tank(int x, int y, boolean isEnemy) {
        this.x = x;
        this.y = y;
        this.isEnemy = isEnemy;
        if (!isEnemy) {
            pet = Tools.getImage("pet-camel.gif");
            petWidth = pet.getWidth(null);
        }
    }

    void initDirection(Direction direction) {
        this.direction = direction;
        this.previousDirection = direction;
    }

    int getHp() {
        return this.hp;
    }

    void setHp(int hp) {
        this.hp = hp;
    }

    void revive() {
        this.hp = MAX_HP;
    }

    void setLive(boolean live) {
        super.setLive(live);
        if (!this.isLive() && !this.isEnemy) {
            Tools.playAudio("death.mp3");
        }
    }

    boolean isDying() {
        return this.hp <= LOW_HP_THRESHOLD;
    }

    boolean isEnemy() {
        return this.isEnemy;
    }

    @Override
    void draw(Graphics g) {
        Image img = previousDirection.get(OBJECT_TYPE);
        int width = img.getWidth(null);
        // display blood bar for the tank player can control
        if (!isEnemy) {
            g.setColor(Color.RED);
            g.drawRect(x, y - BLOOD_BAR_HEIGHT, width, BLOOD_BAR_HEIGHT);
            int availableHPWidth = width * hp / MAX_HP;
            g.fillRect(x, y - BLOOD_BAR_HEIGHT, availableHPWidth, BLOOD_BAR_HEIGHT);
            g.drawImage(pet, x - petWidth - 5, y, null);
        }

        g.drawImage(img, x, y, null);
        if (this.direction != null) {
            int oldX = x, oldY = y;
            x = x + this.direction.xFactor * SPEED;
            y = y + this.direction.yFactor * SPEED;
            // Cannot proceed further if meets walls or other tanks
            if (TankWar.getInstance().isCollidedWith(this)) {
                this.x = oldX;
                this.y = oldY;
            }
            this.previousDirection = this.direction;
            this.checkBound();
        }
    }

    private static final int BORDER_DELTA_X = 15, BORDER_DELTA_Y = 25;

    private void checkBound() {
        int minX = isEnemy ? 0 : (petWidth + 5);
        if (x < minX) x = minX;
        if (x > TankWar.WIDTH - WIDTH - BORDER_DELTA_X) {
            x = TankWar.WIDTH - WIDTH - BORDER_DELTA_X;
        }

        int minY = isEnemy ? 0 : BLOOD_BAR_HEIGHT;
        if (y < minY) y = minY;
        if (y > TankWar.HEIGHT - HEIGHT - minY - BORDER_DELTA_Y) {
            y = TankWar.HEIGHT - HEIGHT - minY - BORDER_DELTA_Y;
        }
    }

    void actRandomly() {
        Direction[] dirs = Direction.values();
        if (step == 0) {
            step = Tools.nextInt(12) + 3;
            int rn = Tools.nextInt(dirs.length);
            direction = dirs[rn];
            previousDirection = dirs[rn];
            if (Tools.nextBoolean())
                this.fire();
        }
        step--;
    }

    private int step = Tools.nextInt(12) + 3;

    private int dirCode;

    /**
     * Cheating Mode: Player tank in IRON SKIN mode or not?
     */
    private boolean ironSkin;

    boolean isIronSkin() {
        return this.ironSkin;
    }

    void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_SPACE) {
            TankWar.getInstance().startGame();
        } else if (key == KeyEvent.VK_F2) {
            if (!this.isLive()) {
                this.setLive(true);
                this.hp = MAX_HP;
                TankWar.getInstance().restart();
            }
        } else if (key == KeyEvent.VK_LEFT) {
            dirCode |= Direction.Left.code;
        } else if (key == KeyEvent.VK_UP) {
            dirCode |= Direction.Up.code;
        } else if (key == KeyEvent.VK_RIGHT) {
            dirCode |= Direction.Right.code;
        } else if (key == KeyEvent.VK_DOWN) {
            dirCode |= Direction.Down.code;
        } else if (key == KeyEvent.VK_F11) {
            ironSkin = !this.isEnemy && !ironSkin;
            if (ironSkin)
                System.err.println("CHEATING: Player Tank in Iron Skin Mode!");
            else
                System.out.println("Player Tank switched to normal mode.");
        }
        this.determineDirection();
    }

    void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_CONTROL) {
            fire();
        } else if (key == KeyEvent.VK_A) {
            superFire();
        } else if (key == KeyEvent.VK_LEFT) {
            dirCode ^= Direction.Left.code;
        } else if (key == KeyEvent.VK_UP) {
            dirCode ^= Direction.Up.code;
        } else if (key == KeyEvent.VK_RIGHT) {
            dirCode ^= Direction.Right.code;
        } else if (key == KeyEvent.VK_DOWN) {
            dirCode ^= Direction.Down.code;
        }
        this.determineDirection();
    }

    private void fire() {
        Tools.playAudio("shoot.wav");
        this.fire(previousDirection);
    }

    private void superFire() {
        Tools.playAudio(Tools.nextBoolean() ? "supershoot.wav" : "supershoot.aiff");
        for (Direction dir : Direction.values()) {
            this.fire(dir);
        }
    }

    private void fire(Direction dir) {
        if (!this.isLive()) return;
        int x = this.x + WIDTH / 2 - Missile.WIDTH / 2;
        int y = this.y + HEIGHT / 2 - Missile.HEIGHT / 2;
        Missile m = new Missile(x, y, isEnemy, dir);
        TankWar.getInstance().addMissile(m);
    }

    private void determineDirection() {
        this.direction = Direction.get(this.dirCode);
    }

    @Override
    Rectangle getRectangle() {
        int delta = this.isEnemy ? 0 : 40;
        return new Rectangle(x - delta, y, WIDTH + delta, HEIGHT);
    }

    /**
     * For missile hit detection we need to exclude the little camel pet
     */
    Rectangle rectangleForMissileHit() {
        Image image = previousDirection.get(OBJECT_TYPE);
        return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
    }
}
