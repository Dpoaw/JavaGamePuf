package game;

import graphics.ImageLoader;
import graphics.SpriteSheet;

import java.awt.*;

public class Player01 {

    private String name;
    public static int x, y;
    public static int health;
    private int width, height, velocity;
    public static int[] ground;
    private SpriteSheet spriteSheet;
    public static int strength;
    public static int i = 0;

    public static boolean isMovingLeft01, isMovingRight01, isMovingUp01, isMovingDown01, powerUp, powerDown;

    public Player01(String name, int x, int width, int height, int[] ground) {
        this.name = name;
        this.x = x;
        this.width = width;
        this.height = height;
        this.y = ground[x];
        this.ground = ground;

        this.velocity = 2;
        this.health = 100;
        this.strength = 100;
        this.spriteSheet = new SpriteSheet(ImageLoader.load("/images/players4.png"));
    }

    public void tick() {//tick for every player -> don't use main tick method for updating player
        if (isMovingLeft01) {
            if (this.x >= 90 / 2) {
                this.x -= this.velocity;
            }
        } else if (isMovingRight01) {
            if (this.x <= 1200 - (95 / 2)) {
                this.x += this.velocity;
            }
        }
        this.y = this.ground[this.x];
        if (isMovingUp01) {
            i++;
            if (i >= 18) i = 17;
        } else if (isMovingDown01) {
            i--;
            if (i <= 0) i = 0;
        }
        if (powerUp && this.strength < 200) {
            this.strength++;
        } else if (powerDown && this.strength > 100) {
            this.strength--;
        }
    }

    public void render(Graphics graphics) {
        graphics.drawImage(this.spriteSheet.crop(i * this.width, 0, this.width, this.height), this.x - 47, this.y - 70, null);
    }
}
