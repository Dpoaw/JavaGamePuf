import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.bind.SchemaOutputResolver;

public class HandlingEvents implements Runnable {

    JFrame frame;
    Canvas canvas;
    BufferStrategy bufferStrategy;
    private SpriteSheet spriteSheet;
    boolean running = true;

    private String FILENAME = "C:\\Users\\sivanov\\Desktop\\Homeworks\\Tanks - Copy\\players44.png";
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

    // offset player 1 for 0 degrees, 5 degrees, 10 degrees, ...
    private int[][] offsetXY = {{90, 0}, {85, 5}, {80, 10}, {75, 15}, {70, 20}, {65, 25}, {60, 26}, {55, 27}, {50, 29}, {45, 31},
            {40, 33}, {35, 35}, {30, 37}, {25, 38}, {20, 40}, {15, 43}, {10, 45}, {5, 48}, {0, 50},
            {-5, 48}, {-10, 45}, {-15, 43}, {-20, 40}, {-25, 38}, {-30, 37}, {-35, 35}, {-40, 33},
            {-45, 31}, {-50, 29}, {-55, 27}, {-60, 26}, {-65, 25}, {-70, 20}, {-75, 15}, {-80, 10}, {-85, 5}, {-90, 0}};
    private int[] ground = new int[SCREEN_WIDTH];

    player player1 = new player(300, 0);
    player player2 = new player(800, 180);
    boolean player1Shooting = true;

    public void defineGround() {
        Random r = new Random();
        double[] val1 = new double[4];
        double[] val2 = new double[4];

        for (int i = 0; i < 4; i++) {
            val1[i] = r.nextInt(100);
            val2[i] = (r.nextInt(10) - 20.0) / SCREEN_WIDTH;
        }

        //ground[0] = 7 * SCREEN_HEIGHT / 10;
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
        frame = new JFrame("Tanks");
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

    public static void main(String[] args) {
        HandlingEvents ex = new HandlingEvents();
        new Thread(ex).start();
    }

    public void Paint() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        drawGround(g, Color.green, SCREEN_HEIGHT);
        Landscape.drawSun(g, Color.yellow, SOLAR_POSITION_X, SOLAR_POSITION_Y, SOLAR_RADIUS);

        Paint(g);
        bufferStrategy.show();
    }

    protected void Paint(Graphics2D g) {
        try {
            spriteSheet = new SpriteSheet(ImageIO.read(new File(FILENAME)));
            g.drawImage(spriteSheet.crop((player1.myAngle / 5) * IMAGE_WIDTH, 0, IMAGE_WIDTH, IMAGE_HEIGHT), player1.myX - IMAGE_HORIZONTAL_OFFSET, ground[player1.myX] - IMAGE_VERTICAL_OFFSET, null);
            g.drawImage(spriteSheet.crop(((180 - player2.myAngle) / 5) * IMAGE_WIDTH, IMAGE_HEIGHT, (int) (1 * IMAGE_WIDTH), IMAGE_HEIGHT), player2.myX - IMAGE_HORIZONTAL_OFFSET, ground[player2.myX] - IMAGE_VERTICAL_OFFSET, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (fire) {
            g.setColor(Color.red);
            g.fillOval(ballCoord()[0], ballCoord()[1], 10, 10);
        }
    }

    int[] ballCoord() {
        int v0 = 100; // Initial velocity
        int playerX0, playerAngle0;

        if (player1Shooting) {
            playerX0 = player1.myX0;
            playerAngle0 = player1.myAngle0;
        } else {
            playerX0 = player2.myX0;
            playerAngle0 = player2.myAngle0;
        }

        int x0 = playerX0 + (int) (90 * Math.cos(playerAngle0 * Math.PI / 180));
        int y0 = ground[playerX0] - (int) (90 * Math.sin(playerAngle0 * Math.PI / 180));

        double vX = v0 * Math.cos(playerAngle0 * Math.PI / 180);
        double vY = -v0 * Math.sin(playerAngle0 * Math.PI / 180);

        double gAcc = -9.80665; // Earth's gravity acceleration

        int xCoord = (int) (x0 + vX * time / 10.0);
        int yCoord = (int) (y0 + vY * time / 10.0 - gAcc * Math.pow(time / 10.0, 2));

        //REDEFINE GROUND
        System.out.println(xCoord);
        if (yCoord - ground[xCoord] > 0 && fire) {
            fire = false;
            for (int x = 0; x < SCREEN_WIDTH; x++) {
                ground[x] += 50 * Math.exp(-Math.pow((x - xCoord), 2) / 2000.0);
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
                    break;
            }
        }
    }
}