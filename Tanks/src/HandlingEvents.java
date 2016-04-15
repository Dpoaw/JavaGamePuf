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
    private String PLAYER1 = "player1.png";
    private String PLAYER2 = "player2.png";
    private String POWER = "power.png";
    private String HEARTH = "hearth.png";
    private String ANGLE = "angle.png";
    private int SOLAR_RADIUS = 50;
    private int SOLAR_POSITION_X = 50;
    private int SOLAR_POSITION_Y = 50;
    private int IMAGE_WIDTH = 95;
    private int IMAGE_HEIGHT = 95;
    private int IMAGE_VERTICAL_OFFSET = 70;
    private int IMAGE_HORIZONTAL_OFFSET = IMAGE_WIDTH / 2;
    private int BALL_OFFSET_X = 10;
    private int BALL_OFFSET_Y = 15;
    private int SCREEN_WIDTH = 1200;
    private int SCREEN_HEIGHT = 700;
    private int IMPACT_RADIUS = 50;
    private double DELAY_FACTOR = 3.0;

    private boolean fire = false;
    int time = 0;
    private int vTerminal = -100;

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
            g.drawImage(spriteSheet.crop(0, 0, 47, 80), (int) (0.05 * SCREEN_WIDTH), (int) (0.85 * SCREEN_HEIGHT), null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            spriteSheet = new SpriteSheet(ImageIO.read(new File(PLAYER2)));
            g.drawImage(spriteSheet.crop(0, 0, 41, 70), (int) (0.9 * SCREEN_WIDTH), (int) (0.85 * SCREEN_HEIGHT), null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            spriteSheet = new SpriteSheet(ImageIO.read(new File(POWER)));
            g.drawImage(spriteSheet.crop(0, 0, 80, 80), (int) (0.090 * SCREEN_WIDTH), (int) (0.80 * SCREEN_HEIGHT), null);
            g.drawImage(spriteSheet.crop(0, 0, 80, 80), (int) (0.8 * SCREEN_WIDTH), (int) (0.80 * SCREEN_HEIGHT), null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            spriteSheet = new SpriteSheet(ImageIO.read(new File(HEARTH)));
            g.drawImage(spriteSheet.crop(0, 0, 80, 80), (int) (0.090 * SCREEN_WIDTH), (int) (0.85 * SCREEN_HEIGHT), null);
            g.drawImage(spriteSheet.crop(0, 0, 80, 80), (int) (0.8 * SCREEN_WIDTH), (int) (0.85 * SCREEN_HEIGHT), null);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            spriteSheet = new SpriteSheet(ImageIO.read(new File(ANGLE)));
            g.drawImage(spriteSheet.crop(0, 0, 80, 80), (int) (0.0908 * SCREEN_WIDTH), (int) (0.907 * SCREEN_HEIGHT), null);
            g.drawImage(spriteSheet.crop(0, 0, 80, 80), (int) (0.805 * SCREEN_WIDTH), (int) (0.907 * SCREEN_HEIGHT), null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Times New Roman", 10, 30));


        g.drawString("" + player1.health, (int) (0.13 * SCREEN_WIDTH), (int) (0.9 * SCREEN_HEIGHT));
        g.drawString("" + player2.health, (int) (0.84 * SCREEN_WIDTH), (int) (0.9 * SCREEN_HEIGHT));

        g.drawString("" + player1.myVelocity, (int) (0.13 * SCREEN_WIDTH), (int) (0.85 * SCREEN_HEIGHT));
        g.drawString("" + player2.myVelocity, (int) (0.84 * SCREEN_WIDTH), (int) (0.85 * SCREEN_HEIGHT));

        g.drawString("" + player1.myAngle, (int) (0.13 * SCREEN_WIDTH), (int) (0.95 * SCREEN_HEIGHT));
        g.drawString("" + (180 - player2.myAngle), (int) (0.84 * SCREEN_WIDTH), (int) (0.95 * SCREEN_HEIGHT));

        g.drawString(String.format("Terminal speed: %d m/s", Math.abs(vTerminal)), (int) (0.40 * SCREEN_WIDTH), (int) (0.95 * SCREEN_HEIGHT));

        bufferStrategy.show();
    }


    public int[] ballCoord() {
        int playerX0, playerAngle0, playerVelocity0;

        if (player1Shooting) {
            playerX0 = player1.myX0;
            playerAngle0 = player1.myAngle0;
            playerVelocity0 = player1.myVelocity0;

        } else {
            playerX0 = player2.myX0;
            playerAngle0 = player2.myAngle0;
            playerVelocity0 = player2.myVelocity0;

        }

        int x0 = playerX0 - BALL_OFFSET_X + (int) (40 * Math.cos(playerAngle0 * Math.PI / 180));
        int y0 = ground[playerX0] - BALL_OFFSET_Y - (int) (30 * Math.sin(playerAngle0 * Math.PI / 180));

        double vX = playerVelocity0 * Math.cos(playerAngle0 * Math.PI / 180);
        double vY = -playerVelocity0 * Math.sin(playerAngle0 * Math.PI / 180);

        double gAcc = -9.80665; // Earth's gravity acceleration

        int xCoord = (int) (x0 + (vX * vTerminal / gAcc) * (1 - Math.exp((-gAcc * time / DELAY_FACTOR) / vTerminal)));
        int yCoord = (int) (y0 + (vTerminal / gAcc) * (vY + vTerminal) * (1 - Math.exp((-gAcc * time / DELAY_FACTOR) / vTerminal)) - vTerminal * time / DELAY_FACTOR);

        //the current velocity; current[0] is x component, current[1] is y component
        int[] currentVelocity = new int[] {
                (int) ((vX / DELAY_FACTOR) * Math.exp(-gAcc * time / (DELAY_FACTOR * vTerminal))),
                (int) (-vTerminal / DELAY_FACTOR + ((vY + vTerminal) / DELAY_FACTOR) * Math.exp(-gAcc * time / (DELAY_FACTOR * vTerminal)))};

        int[] normalVectorPlayer1 = new int[]{xCoord - player1.myX, yCoord - ground[player1.myX]};
        int[] normalVectorPlayer2 = new int[]{xCoord - player2.myX, yCoord - ground[player2.myX]};

        // Check whether the ball is within the Impact radius of a player and if the velocity vector points toward the bounding circle of the same player
        boolean isPlayer1Hit = (Math.pow((player1.myX - xCoord), 2) + Math.pow((ground[player1.myX] - yCoord), 2) <= Math.pow(IMPACT_RADIUS, 2)) &&
                (normalVectorPlayer1[0] * currentVelocity[0] + normalVectorPlayer1[1] * currentVelocity[1] < 0);
        boolean isPlayer2Hit = (Math.pow((player2.myX - xCoord), 2) + Math.pow((ground[player2.myX] - yCoord), 2) <= Math.pow(IMPACT_RADIUS, 2)) &&
                (normalVectorPlayer2[0] * currentVelocity[0] + normalVectorPlayer2[1] * currentVelocity[1] < 0);

        // The ball hits the ground or one of the players
        if (fire && (yCoord >= ground[xCoord] || isPlayer1Hit || isPlayer2Hit)) {
            fire = false;
            for (int x = 0; x < SCREEN_WIDTH; x++) {
                ground[x] += 50 * Math.exp(-Math.pow((x - xCoord), 2) / 2000.0);
            }
            //if bullet is in area of player1
            if (isPlayer1Hit) {
                player1.health -= 20;
            }
            if (isPlayer2Hit) {
                player2.health -= 20;
            }

            player1Shooting = !player1Shooting; // Now the other tank is shooting
        }

        int[] temp = new int[]{xCoord, yCoord};
        return temp;
    }


    public void moveIt(KeyEvent evt) {
        if (!fire) {
            switch (evt.getKeyCode()) {
                case KeyEvent.VK_ADD:
                    if (vTerminal > -100) vTerminal -= 10;
                    break;
                case KeyEvent.VK_SUBTRACT:
                    if (vTerminal <= -20) vTerminal += 10;
                    break;
            }
        }


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