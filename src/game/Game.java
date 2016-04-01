package game;

import java.awt.Graphics;

import display.Display;
import graphics.Assets;
import graphics.ImageLoader;
import graphics.SpriteSheet;

import java.awt.image.BufferStrategy;

public class Game implements Runnable {

    private String title;
    private int width, height;

    private Thread thread;
    private Display display;
    private InputHandler inputHandler;
    private boolean isRunning;

    private BufferStrategy buffStrat;
    private Graphics graphics;
    private SpriteSheet spriteSheet;
    private int playerWidth = 95;
    private int playerHeight = 130;
    private int playerX = 100;
    private int playerY = 400;

    private Player player;

    public Game(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.isRunning = false;

    }

    private void init() {
        this.display = new Display(title, width, height);
        this.inputHandler = new InputHandler(this.display);
        //this.spriteSheet = new SpriteSheet(ImageLoader.load("/images/player.png"));//here we adding path to the picture.png to crop
        //Assets.init();
        this.player = new Player("myPlayer", 100, 400, 100, 100);
    }

    private void tick() {  //TICK -> calculate
        this.player.tick();
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


        this.graphics.drawImage(ImageLoader.load("/images/back.jpg"), 0, 0, null);//drawing background


        //ImageLoader.load(x, y, width, height ->// width & height - size of the picture)
        //this.graphics.drawImage(this.spriteSheet.crop(this.w * this.i, this.h, 95, 130), 100, 100, null);//last we draw player and move him
        this.player.render(graphics);//here we call player -> he draws itself, counts his current condition with his tick() method
        //this.graphics.drawImage(Assets.player1, 100, 200, null);


        //STOP DRAWING
        this.buffStrat.show();
        this.graphics.dispose();
    }


    @Override
    public void run() { //  RUN -> here is while loop, here we call tick and render
        this.init();

        int fps = 20;
        double ticksPerFrame = 1_000_000_000 /fps;//every sec we make 30 ticks!!!
        double delta = 0;
        long now;
        long lastTimeTicked = System.nanoTime();

        while (isRunning) {
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

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
