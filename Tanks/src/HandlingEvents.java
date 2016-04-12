import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.swing.*;

public class HandlingEvents implements Runnable {

    JFrame frame;
    Canvas canvas;
    BufferStrategy bufferStrategy;
    private SpriteSheet spriteSheet;
    boolean running = true;

    private String FILENAME = "players44.png";
    private String PLAYER1="player1.png";
    private String PLAYER2="player2.png";
    private int SOLAR_RADIUS = 50;
    private int SOLAR_POSITION_X = 50;
    private int SOLAR_POSITION_Y = 50;
    private int IMAGE_WIDTH = 95;
    private int IMAGE_HEIGHT = 95;
    private int IMAGE_VERTICAL_OFFSET = 70;
    private int IMAGE_HORIZONTAL_OFFSET = IMAGE_WIDTH / 2;
    private int SCREEN_WIDTH = 1200;
    private int SCREEN_HEIGHT = 700;
    private boolean fire = false;
    int time = 0;

    private int[] ground = new int[SCREEN_WIDTH];

    player player1 = new player(300, 0, 70, 100);
    player player2 = new player(800, 180, 70, 100);
    boolean player1Shooting = true;

    public void defineGround() {
        Random r = new Random();
        double[] val1 = new double[4];
        double[] val2 = new double[4];

        for (int i = 0; i < 4; i++) {
            val1[i] = r.nextInt(100);
            val2[i] = (r.nextInt(10) - 20.0) / SCREEN_WIDTH;
        }

        for (int i = 0; i < ground.length; i++) {
            ground[i] = 7 * SCREEN_HEIGHT / 10 + (int) (val2[0] + 0.5 * val1[1] * Math.sin(val1[0] - val2[1] * i) + 0.5 * val1[0] * Math.sin(val1[1] - val2[2] * i) + 0.5 * val1[2] * Math.sin(val1[2] + val2[3] * i));
        }
    }


    void drawGround(Graphics2D g2d, Color clr, int height) {
        g2d.setColor(clr);
        int step = 1;
        for (int x = 0; x < ground.length; x++) {
            g2d.drawLine(step * x, height, step * x, ground[x]);
        }

    }

    public HandlingEvents() {
        Dimension dimension = new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT);
        frame = new JFrame("Chilli War");
        frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        canvas = new Canvas();
        canvas.setMaximumSize(dimension);
        canvas.setMinimumSize(dimension);
        canvas.setPreferredSize(dimension);

        frame.add(canvas);
        frame.pack();

        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                moveIt(evt);
            }
        });

        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();
    }

    public void run() {
        defineGround();
        while (running = true) {
            Paint();
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
            }
            time++;
        }
    }

    public void Paint() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        drawGround(g, Color.green, SCREEN_HEIGHT);
        Landscape.drawSun(g, Color.yellow, SOLAR_POSITION_X, SOLAR_POSITION_Y, SOLAR_RADIUS);


        try {
            spriteSheet = new SpriteSheet(ImageIO.read(new File(FILENAME)));
            g.drawImage(spriteSheet.crop((player1.myAngle / 5) * IMAGE_WIDTH, 0, IMAGE_WIDTH, IMAGE_HEIGHT), player1.myX - IMAGE_HORIZONTAL_OFFSET, ground[player1.myX] - IMAGE_VERTICAL_OFFSET, null);
            g.drawImage(spriteSheet.crop(((180 - player2.myAngle) / 5) * IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT), player2.myX - IMAGE_HORIZONTAL_OFFSET, ground[player2.myX] - IMAGE_VERTICAL_OFFSET, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (fire) {
            int[] coordOfBall = ballCoord();
            g.setColor(Color.red);
            g.fillOval(coordOfBall[0], coordOfBall[1], 10, 10);
        }


        try {
            spriteSheet = new SpriteSheet(ImageIO.read(new File(PLAYER1)));
            g.drawImage(spriteSheet.crop(0,0,47,80), (int)(0.05 * SCREEN_WIDTH), (int)(0.85 * SCREEN_HEIGHT), null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            spriteSheet = new SpriteSheet(ImageIO.read(new File(PLAYER2)));
            g.drawImage(spriteSheet.crop(0,0,41,70), (int)(0.9 * SCREEN_WIDTH), (int)(0.85 * SCREEN_HEIGHT), null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Times New Roman", 10, 30));


        g.drawString("Health: " + player1.health, (int)(0.1 * SCREEN_WIDTH), (int)(0.9 * SCREEN_HEIGHT));
        g.drawString("Health: " + player2.health, (int)(0.8 * SCREEN_WIDTH), (int)(0.9 * SCREEN_HEIGHT));

        g.drawString("Power: " + player1.myVelocity, (int)(0.1 * SCREEN_WIDTH), (int)(0.85 * SCREEN_HEIGHT));
        g.drawString("Power: " + player2.myVelocity, (int)(0.8 * SCREEN_WIDTH), (int)(0.85 * SCREEN_HEIGHT));

        g.drawString("Angle: " + player1.myAngle, (int)(0.1 * SCREEN_WIDTH), (int)(0.95 * SCREEN_HEIGHT));
        g.drawString("Angle: " + player2.myAngle, (int)(0.8 * SCREEN_WIDTH), (int)(0.95 * SCREEN_HEIGHT));

        bufferStrategy.show();
    }


    public int[] ballCoord() {
        int playerX0, playerAngle0, playerVelocity0, playerHealth;

        if (player1Shooting) {
            playerX0 = player1.myX0;
            playerAngle0 = player1.myAngle0;
            playerVelocity0 = player1.myVelocity0;

        } else {
            playerX0 = player2.myX0;
            playerAngle0 = player2.myAngle0;
            playerVelocity0 = player2.myVelocity0;

        }

        int x0 = playerX0 + (int) (90 * Math.cos(playerAngle0 * Math.PI / 180));
        int y0 = ground[playerX0] - (int) (90 * Math.sin(playerAngle0 * Math.PI / 180));

        double vX = playerVelocity0 * Math.cos(playerAngle0 * Math.PI / 180);
        double vY = -playerVelocity0 * Math.sin(playerAngle0 * Math.PI / 180);

        double gAcc = -9.80665; // Earth's gravity acceleration

        int xCoord = (int) (x0 + vX * time / 10.0);
        int yCoord = (int) (y0 + vY * time / 10.0 - gAcc * Math.pow(time / 10.0, 2));

        //REDEFINE GROUND

        boolean condition =
                ((xCoord<=player1.myX+50)&&(xCoord>=player1.myX-50)&&(yCoord>=ground[player1.myX]-50)&&(yCoord<=ground[player1.myX]+50))||
                        ((xCoord<=player2.myX+50)&&(xCoord>=player2.myX-50)&&(yCoord>=ground[player2.myX]-50)&&(yCoord<=ground[player2.myX]+50));
        System.out.println(condition);



        if (fire && (yCoord >= ground[xCoord] || condition)) {
            fire = false;
            for (int x = 0; x < SCREEN_WIDTH; x++) {
                ground[x] += 50 * Math.exp(-Math.pow((x - xCoord), 2) / 2000.0);
            }
            if((xCoord<=player1.myX+50)&&(xCoord>=player1.myX-50)&&(yCoord>=ground[player1.myX]-50)&&(yCoord<=ground[player1.myX]+50)){
                player1.health-=20;
            }else{
                if((xCoord<=player2.myX+50)&&(xCoord>=player2.myX-50)&&(yCoord>=ground[player2.myX]-50)&&(yCoord<=ground[player2.myX]+50)){
                    player2.health-=20;
                }
            }

            player1Shooting = !player1Shooting; // Now the other tank is shooting
        }

        int[] temp = new int[]{xCoord, yCoord};
        return temp;
    }


    public void moveIt(KeyEvent evt) {
        if (player1Shooting) {
            switch (evt.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (player1.myAngle <= 90 - 5) {
                        player1.myAngle += 5;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (player1.myAngle >= 5) {
                        player1.myAngle -= 5;
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (player1.myX >= 5) {
                        player1.myX -= 5;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (player1.myX <= SCREEN_WIDTH - 100) {
                        player1.myX += 5;
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    fire = true;
                    time = 0;
                    player1.myX0 = player1.myX;
                    player1.myAngle0 = player1.myAngle;
                    player1.myVelocity0 = player1.myVelocity;
                    break;
                case KeyEvent.VK_PAGE_DOWN:
                    if (player1.myVelocity > 50) {
                        player1.myVelocity -= 5;
                    }
                    break;
                case KeyEvent.VK_PAGE_UP:
                    if (player1.myVelocity < 200) {
                        player1.myVelocity += 5;
                    }
                    break;
            }
        } else {
            switch (evt.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (player2.myAngle >= 90 + 5) {
                        player2.myAngle -= 5;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (player2.myAngle <= 180 - 5) {
                        player2.myAngle += 5;
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (player2.myX >= 5) {
                        player2.myX -= 5;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (player2.myX <= SCREEN_WIDTH - 100) {
                        player2.myX += 5;
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    fire = true;
                    time = 0;
                    player2.myX0 = player2.myX;
                    player2.myAngle0 = player2.myAngle;
                    player2.myVelocity0 = player2.myVelocity;
                    break;
                case KeyEvent.VK_PAGE_DOWN:
                    if (player2.myVelocity > 50) {
                        player2.myVelocity -= 5;
                    }
                    break;
                case KeyEvent.VK_PAGE_UP:
                    if (player2.myVelocity < 200) {
                        player2.myVelocity += 5;
                    }
                    break;

            }
        }
    }
}