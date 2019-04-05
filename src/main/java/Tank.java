import java.awt.*;
import java.awt.event.KeyEvent;

class Tank extends GameObject {
    private static final int SPEED = 5;

    private static final int BLOOD_BAR_HEIGHT = 10;

    private static final int MAX_HP = 100;

    private static final int LOW_HP_THRESHOLD = 50;

    private static final int DISTANCE_TO_PET = 5;

    static final String OBJECT_TYPE = Tank.class.getName().toLowerCase();

    static final String ENEMY_OBJECT_TYPE = "e" + OBJECT_TYPE;

    private int hp = MAX_HP;

    private boolean stopped;

    private Direction direction;

    private final int width, height;

    private final boolean enemy;

    private Image pet;

    private int petWidth;

    Tank(int x, int y) {
        this(x, y, false, Direction.Down);
    }

    Tank(int x, int y, boolean enemy, Direction direction) {
        this.x = x;
        this.y = y;
        this.enemy = enemy;
        if (!enemy) {
            pet = Tools.getImage("pet-camel.gif");
            petWidth = pet.getWidth(null);
        }
        this.direction = direction;
        Image image = this.getImage();
        this.width = image.getWidth(null);
        this.height = image.getHeight(null);
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
        if (!this.isLive() && !this.enemy) {
            Tools.playAudio("death.mp3");
        }
    }

    boolean isDying() {
        return this.hp <= LOW_HP_THRESHOLD;
    }

    boolean isEnemy() {
        return this.enemy;
    }

    private Image getImage() {
        return direction.getImage(this.enemy ? ENEMY_OBJECT_TYPE : OBJECT_TYPE);
    }

    @Override
    void draw(Graphics g) {
        // display blood bar for the tank player can control
        if (!enemy) {
            g.setColor(Color.RED);
            g.drawRect(x, y - BLOOD_BAR_HEIGHT, width, BLOOD_BAR_HEIGHT);
            int availableHPWidth = width * hp / MAX_HP;
            g.fillRect(x, y - BLOOD_BAR_HEIGHT, availableHPWidth, BLOOD_BAR_HEIGHT);
            g.drawImage(pet, x - petWidth - DISTANCE_TO_PET, y, null);
        }

        g.drawImage(this.getImage(), x, y, null);
        if (!stopped) {
            int oldX = x, oldY = y;
            x = x + this.direction.xFactor * SPEED;
            y = y + this.direction.yFactor * SPEED;
            // Cannot proceed further if meets walls or other tanks
            if (TankWar.getInstance().isCollidedWithOtherObjects(this)) {
                this.x = oldX;
                this.y = oldY;
            }
            this.checkBound();
        }
    }

    private void checkBound() {
        int minX = enemy ? 0 : (petWidth + DISTANCE_TO_PET);
        if (x < minX) x = minX;
        if (x > TankWar.WIDTH - width) {
            x = TankWar.WIDTH - width;
        }

        int minY = enemy ? 0 : BLOOD_BAR_HEIGHT;
        if (y < minY) y = minY;
        if (y > TankWar.HEIGHT - height - minY) {
            y = TankWar.HEIGHT - height - minY;
        }
    }

    void actRandomly() {
        Direction[] dirs = Direction.values();
        if (step == 0) {
            step = Tools.nextInt(12) + 3;
            int rn = Tools.nextInt(dirs.length);
            direction = dirs[rn];
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
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT: dirCode |= Direction.Left.code; break;
            case KeyEvent.VK_RIGHT: dirCode |= Direction.Right.code; break;
            case KeyEvent.VK_UP: dirCode |= Direction.Up.code; break;
            case KeyEvent.VK_DOWN: dirCode |= Direction.Down.code; break;
            case KeyEvent.VK_SPACE: TankWar.getInstance().startGame(); break;
            case KeyEvent.VK_F2:
                if (!this.isLive()) {
                    this.setLive(true);
                    this.hp = MAX_HP;
                    TankWar.getInstance().restart();
                }
                break;
            case KeyEvent.VK_F11:
                ironSkin = !this.enemy && !ironSkin;
                if (ironSkin)
                    System.err.println("CHEATING: Player Tank in Iron Skin Mode!");
                else
                    System.out.println("Player Tank switched to normal mode.");
                break;
        }
        this.determineDirection();
    }

    void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_CONTROL: fire(); break;
            case KeyEvent.VK_A: superFire(); break;
            case KeyEvent.VK_LEFT: dirCode ^= Direction.Left.code; break;
            case KeyEvent.VK_RIGHT: dirCode ^= Direction.Right.code; break;
            case KeyEvent.VK_UP: dirCode ^= Direction.Up.code; break;
            case KeyEvent.VK_DOWN: dirCode ^= Direction.Down.code; break;
        }
        this.determineDirection();
    }

    private void fire() {
        Tools.playAudio("shoot.wav");
        this.fire(direction);
    }

    private void superFire() {
        Tools.playAudio(Tools.nextBoolean() ? "supershoot.wav" : "supershoot.aiff");
        for (Direction dir : Direction.values()) {
            this.fire(dir);
        }
    }

    private void fire(Direction dir) {
        if (!this.isLive()) return;
        int x = this.x + width / 2 - Missile.WIDTH / 2;
        int y = this.y + height / 2 - Missile.HEIGHT / 2;
        TankWar.getInstance().addMissile(new Missile(x, y, enemy, dir));
    }

    private void determineDirection() {
        Direction newDirection = Direction.get(this.dirCode);
        stopped = newDirection == null;
        if (!stopped)
            this.direction = newDirection;
    }

    @Override
    Rectangle getRectangle() {
        int delta = this.enemy ? 0 : petWidth + DISTANCE_TO_PET;
        return new Rectangle(x - delta, y, width + delta, height);
    }

    /**
     * For missile hit detection we need to exclude the little camel pet
     */
    Rectangle rectangleForMissileHit() {
        return new Rectangle(x, y, width, height);
    }
}
