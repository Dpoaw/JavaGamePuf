package game;

import java.awt.*;

public class Bullet {
    private int x;
    private int angle;
    private int strength;
    int[] ground;
    private int[] coordinates;
    int time = 0;
    public static int xCoord;
    public static int yCoord;

    public Bullet(int x, int angle, int strength, int[] ground) {
        this.x = x;
        this.angle = angle;
        this.strength = strength;
        this.ground = ground;
    }

    public void tick() {
        int currentX = this.x - 40;

        int x0 = currentX + (int) (90 * Math.cos(this.angle * Math.PI / 180));
        int y0 = this.ground[this.x] - (int) (90 * Math.sin(this.angle * Math.PI / 180));

        double vX = (this.strength / 1.5) * Math.cos(this.angle * Math.PI / 180);
        double vY = (-this.strength / 1.5) * Math.sin(this.angle * Math.PI / 180);

        double gAcc = -9.80665; // Earth's gravity acceleration

        xCoord = (int) (x0 + vX * this.time / 10.0);
        yCoord = (int) (y0 + vY * this.time / 10.0 - gAcc * Math.pow(this.time / 10.0, 2));

        //REDEFINE GROUND
        int p1X = Game.player01.x;
        int p2X = Game.player02.x;
        boolean condition = ((p1X - xCoord) * (p1X - xCoord) + (ground[p1X] - yCoord) * (ground[p1X] - yCoord) <= 500) ||
                (p2X - xCoord) * (p2X - xCoord) + (ground[p2X] - yCoord) * (ground[p2X] - yCoord) <= 500;
        System.out.println(condition);
        //checking if bullet is out of the frame(window), delete it and change players turn
        if (xCoord < 0 || xCoord > Game.groundPoints.length - 1) {
            Game.turn = !Game.turn;
            Game.bulletCreated = false;
        }
        if (yCoord < 0) {
            Game.turn = !Game.turn;
            Game.bulletCreated = false;
        }
        this.coordinates = new int[]{xCoord, yCoord};
        this.time++;
    }

    public void render(Graphics graphics) {
        graphics.setColor(Color.black);
        graphics.fillOval(coordinates[0], coordinates[1], 10, 10);
    }
}
