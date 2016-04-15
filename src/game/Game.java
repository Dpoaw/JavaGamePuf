package game;

import java.awt.*;

import display.Display;
import graphics.ImageLoader;
import graphics.SpriteSheet;

import java.awt.image.BufferStrategy;
import java.util.Random;

public class Game implements Runnable {

    private String title;
    private int width, height;

    private Thread thread;
    private Display display;
    private InputHandler inputHandler;

    private boolean isRunning;
    public static boolean fire = false;
    public static boolean turn = true;
    public static boolean bulletCreated = false;
    private boolean explosion = false;

    private BufferStrategy buffStrat;
    private Graphics graphics;
    private SpriteSheet spriteSheet;

    private int playerWidth = 95;
    private int playerHeight = 94;
    private int playerHeight02 = 94;
    private int playerWidth02 = 95;

    private int playerX01 = 100;
    private int playerX02 = 1_100;

    private int explosionX;
    private int explosionY;
    private int iterator = 0;
    private int cols = 0;
    private int counter = 0;

    public static int[] groundPoints = new int[1_200];

    public static Player01 player01;
    public static Player02 player02;
    public static Bullet bullet;

    public Game(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.isRunning = false;

    }

    private void init() {
        this.display = new Display(this.title, this.width, this.height);
        this.display.getCanvas().requestFocus();  //making display focusable
        this.spriteSheet = new SpriteSheet(ImageLoader.load("/images/explosion.png"));
        this.inputHandler = new InputHandler(this.display);

        //defining ground
        defineGround();
        //creating two players
        this.player01 = new Player01("Player01", playerX01, playerWidth, playerHeight, groundPoints);
        this.player02 = new Player02("Player02", playerX02, playerWidth02, playerHeight02, groundPoints);
    }

    private void tick() {  //TICK -> calculate
        this.player01.tick();
        this.player02.tick();

        if (this.bulletCreated) {
            this.bullet.tick();
        }

        if (this.explosion) {
            this.iterator++;
            if (this.iterator == 7) {
                this.iterator = 0;
                this.cols++;
            }
        }
        if (this.cols == 6) {
            this.explosion = false;
            this.iterator = 0;
            this.cols = 0;
        }
        //if bullet has hitted ground -> we call redefineGroung() method, change players turn and delete bullet
        if ((this.bullet.xCoord >= 0 && this.bullet.xCoord <= this.groundPoints.length - 1) || this.bullet.yCoord >= 0) {
            if (this.bulletCreated && this.turn && this.bullet.yCoord >= this.groundPoints[this.bullet.xCoord]) {
                redefineGround();
                this.turn = false;
                this.bulletCreated = false;
                //creating explosion
                this.explosionX = this.bullet.xCoord;
                this.explosionY = this.bullet.yCoord;
                this.explosion = true;
                //stop moving player, because his turn is over
                this.player01.isMovingLeft01 = false;
                this.player01.isMovingRight01 = false;
                this.player01.isMovingUp01 = false;
                this.player01.isMovingDown01 = false;
                this.bullet.xCoord = 0;
                this.bullet.yCoord = 0;
            } else if (this.bulletCreated && !this.turn && this.bullet.yCoord >= this.groundPoints[this.bullet.xCoord]) {
                redefineGround();
                this.turn = true;
                this.bulletCreated = false;
                //creating explosion
                this.explosionX = this.bullet.xCoord;
                this.explosionY = this.bullet.yCoord;
                this.explosion = true;
                //stop moving player, because his turn is over
                this.player02.isMovingLeft02 = false;
                this.player02.isMovingRight02 = false;
                this.player02.isMovingUp02 = false;
                this.player02.isMovingDown02 = false;
                this.bullet.xCoord = 0;
                this.bullet.yCoord = 0;
            }
        }

        if (Math.pow((this.player01.x - this.bullet.xCoord), 2) + Math.pow((this.groundPoints[this.player01.x] - this.bullet.yCoord), 2) <= 2500) {
            this.player01.health -= 10;//decreasing player's health
            //again creating explosion
            this.explosionX = this.player01.x - 20;
            this.explosionY = this.player01.y - 40;
            this.explosion = true;
            this.bullet.xCoord = 0;//changing coordinates of bullet to stop decreasing player's health
            this.bullet.yCoord = 0;//changing coordinates of bullet to stop decreasing player's health
            this.bulletCreated = false;
            this.turn = !this.turn;//changing player's turn
            //stop moving player, because his turn is over
            this.player01.isMovingLeft01 = false;
            this.player01.isMovingRight01 = false;
            this.player01.isMovingUp01 = false;
            this.player01.isMovingDown01 = false;

            //this formula to check if player is hitted DOESN'T WORK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! -> condition is always TRUE
//        if (Math.sqrt((this.player01.x - this.bullet.xCoord)) + Math.sqrt((this.groundPoints[this.player01.x] - this.bullet.yCoord)) <= 500) {
//            //this.player01.health -= 20;
//            //this.bulletCreated = false;
//
//        //this formula to check if player is hitted DOESN'T WORK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! -> condition is always TRUE
//        if (Math.pow((this.player02.x-this.bullet.xCoord),2)+Math.pow((this.player02.y-this.bullet.yCoord),2)<=2500) {
//            this.bulletCreated = false;
//            this.player01.health -= 20;
//
//
//        }

            if (Math.pow((this.player02.x - this.bullet.xCoord), 2) + Math.pow((this.groundPoints[this.player02.x] - this.bullet.yCoord), 2) <= 2500) {
                this.player02.health -= 10;
                //again creating explosion
                this.explosionX = this.player02.x - 20;
                this.explosionY = this.player02.y - 40;
                this.explosion = true;
                this.bullet.xCoord = 0;
                this.bullet.yCoord = 0;
                this.bulletCreated = false;
                this.turn = !this.turn;
                this.player02.isMovingLeft02 = false;
                this.player02.isMovingRight02 = false;
                this.player02.isMovingUp02 = false;
                this.player02.isMovingDown02 = false;
            }


//        if (Math.sqrt((this.player02.x - this.bullet.xCoord)) + Math.sqrt((this.groundPoints[this.player02.x] - this.bullet.yCoord)) <= 500) {
//            //this.player02.health -= 20;
//            //this.bulletCreated = false;
//
//        if (Math.pow((this.player01.x-this.bullet.xCoord),2)+Math.pow((this.player01.y-this.bullet.yCoord),2)<=2500) {
//            this.bulletCreated = false;
//            this.player02.health -= 20;
//
//        }
        }
    }

    private void render() {  //RENDER -> draws images

        this.buffStrat = this.display.getCanvas().getBufferStrategy();
        if (this.buffStrat == null) {
            this.display.getCanvas().createBufferStrategy(2);
            return;
        }

        this.graphics = this.buffStrat.getDrawGraphics();
        this.graphics.clearRect(0, 0, this.width, this.height);
        //START DRAWING
        //this.graphics.setColor(Color.cyan);
        //this.graphics.fillRect(0, 0, this.width, this.height);

        this.graphics.drawImage(ImageLoader.load("/images/back.jpg"), 0, 0, null);
        //this.graphics.drawImage(ImageLoader.load("/images/back.jpg"), 0, 0, null);//drawing background ->
        // maybe without this, or picture with smaller resolution
        //drawing random ground
        this.graphics.setColor(Color.GREEN);
        for (int i = 0; i < this.groundPoints.length; i++) {
            this.graphics.drawLine(i, this.height, i, this.groundPoints[i]);
        }
        //drawint text
        this.graphics.setColor(Color.BLACK);
        Font fnt = new Font("Times New Roman", 5, 20);
        this.graphics.setFont(fnt);
        this.graphics.drawString(("Player01 health: " + this.player01.health), (int)(0.05 * this.width), (int)(0.9 * this.height));
        this.graphics.drawString(("Player02 health: " + this.player02.health), (int)(0.80 * this.width), (int)(0.9 * this.height));
        this.graphics.drawString(("Player01 power: " + this.player01.strength), (int)(0.05 * this.width), (int)(0.95 * this.height));
        this.graphics.drawString(("Player02 power: " + this.player02.strength), (int)(0.80 * this.width), (int)(0.95 * this.height));

        //drawing point(or something else like arrow) above players to show which player will shoot
        this.graphics.setColor(Color.RED);
        if (this.turn) {
            this.graphics.fillOval(this.player01.x - 30, this.groundPoints[this.player01.x] - 50, 10, 10);
        } else {
            this.graphics.fillOval(this.player02.x + 30, this.groundPoints[this.player02.x] - 50, 10, 10);
        }
        //drawing players
        this.player01.render(this.graphics);//here we call player -> he draws itself, counts his current condition with his tick() method
        this.player02.render(this.graphics);// -> player02

        //drawing explsion if it's created
        if (this.explosion) {
            this.graphics.drawImage(this.spriteSheet.crop(this.iterator * 79, this.cols * 79, 79, 79), this.explosionX, this.explosionY, null);
        }


        //this.graphics.drawRect(this.player01.x-60,this.groundPoints[this.player01.x]-50, 100,50);
        //this.graphics.drawRect(this.player02.x-60, this.groundPoints[this.player02.x]-50,100,50);

        //drawing bullet, if it has been created!!! -> to avoid exceptions
        if (this.bulletCreated) {
            this.bullet.render(graphics);
        }

        //STOP DRAWING
        this.buffStrat.show();
        this.graphics.dispose();
    }

    //defining random coordinates of ground -> we call this method in init();
    private void defineGround() {
        Random r = new Random();
        double[] val1 = new double[4];
        double[] val2 = new double[4];

        for (int i = 0; i < 4; i++) {
            val1[i] = r.nextInt(100);
            val2[i] = (r.nextInt(10) - 20.0) / this.width;
        }
        for (int i = 0; i < this.groundPoints.length; i++) {
            this.groundPoints[i] = 7 * this.height / 10 + (int) (val2[0] + 0.5 * val1[1] * Math.sin(val1[0] -
                    val2[1] * i) + 0.5 * val1[0] * Math.sin(val1[1] - val2[2] * i) + 0.5 * val1[2] * Math.sin(val1[2] + val2[3] * i));
        }
    }

    //redefining ground coordinates
    private void redefineGround() {
        for (int x = 0; x < this.groundPoints.length; x++) {
            this.groundPoints[x] += 50 * Math.exp(-Math.pow((x - this.bullet.xCoord), 2) / 2000.0);
        }
    }

    @Override
    public void run() { //  RUN -> here is while loop, here we call tick and render
        this.init();

        int fps = 50;
        double ticksPerFrame = 1_000_000_000 / fps;//every sec we make 50 ticks!!!
        double delta = 0;
        long now;
        long lastTimeTicked = System.nanoTime();

        while (isRunning) {

            if (this.fire && this.turn) {
                this.bullet = new Bullet(this.player01.x, (this.player01.i + 1) * 5, this.player01.strength, this.groundPoints);
                this.bulletCreated = true;
            }
            if (this.fire && !this.turn) {
                this.bullet = new Bullet(this.player02.x, (this.player02.i + 1) * 5 + 90, this.player02.strength, this.groundPoints);
                this.bulletCreated = true;
            }

            now = System.nanoTime();
            delta += (now - lastTimeTicked) / ticksPerFrame;
            lastTimeTicked = now;

            if (delta >= 1) {
                tick();
                render();
                delta--;
            }
        }
        this.stop();
    }

    public synchronized void start() {
        if (!this.isRunning) {
            this.isRunning = true;
            this.thread = new Thread(this);
            this.thread.start();
        }
    }

    public synchronized void stop() {
        if (this.isRunning) {
            try {
                this.isRunning = false;
                this.thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
