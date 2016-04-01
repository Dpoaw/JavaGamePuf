package game;

import graphics.ImageLoader;
import graphics.SpriteSheet;

import java.awt.*;

public class Player {

    private String name;
    private int x, y, width, height, health, velocity;
    private SpriteSheet spriteSheet;
    private int i = 0;

    public static boolean isMovingLeft, isMovingRight;

    public Player(String name, int x, int y, int width, int height) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.velocity = 2;
        this.health = 100;
        this.spriteSheet = new SpriteSheet(ImageLoader.load("/images/cannon6.png"));
    }

    public void tick() {//tick for every player -> don't use main tick method for updating player
        i++;
        if (i >= 10) i = 0;
        if (isMovingLeft) {
            this.x -= this.velocity;
        } else if (isMovingRight) {
            this.x += this.velocity;
        }
    }

    public void render(Graphics graphics) {
        graphics.drawImage(this.spriteSheet.crop(95 * i, 0, this.width, this.height), this.x, this.y, null);
    }
}
