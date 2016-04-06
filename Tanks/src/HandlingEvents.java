import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;

public class HandlingEvents implements Runnable {

    JFrame frame;
    int myX = 400;
    int myAngle = 0;
    Canvas canvas;
    BufferStrategy bufferStrategy;
    private SpriteSheet spriteSheet;
    boolean running = true;

    private String FILENAME = "C:\\Users\\sivanov\\Desktop\\Homeworks\\Tanks\\cannon6.png";
    private int SOLAR_RADIUS = 50;
    private int SOLAR_POSITION_X = 50;
    private int SOLAR_POSITION_Y = 50;
    private int IMAGE_WIDTH = 105;
    private int IMAGE_HEIGHT = 80;
    private int IMAGE_VERTICAL_OFFSET = 50;
    private int SCREEN_WIDTH = 1000;
    private int SCREEN_HEIGHT = 700;
    private boolean fire = false;
    int time = 0;

    private int[] ground = new int[1000];

    public void defineGround() {
        double consts = 100;
        ground[0] = 500;
        for (int i = 1; i < ground.length; i++) {
            ground[i] = ground[0] + (2 * (int) (-1.54778 + 9 * Math.sin(100 - 0.6 * i / consts) - 6 * Math.sin(40 - 0.4 * i / consts) + 5 * Math.sin(100 + 2 * i / consts)));
        }
    }


    void drawGround(Graphics2D g2d, Color clr, int height) {
        g2d.setColor(clr);
        int step = 1;
        Random r = new Random();
        for (int x = 0; x < ground.length; x++) {
            g2d.drawLine(step * x, height, step * x, ground[x]);
        }

    }

    static void drawSun(Graphics2D g2d, Color clr, int xCoord, int yCoord, int radius) {
        int rayLength = 3 * radius;
        int rayLengthDiag = (int) (rayLength / Math.sqrt(2));
        g2d.setColor(clr);
        g2d.fillOval(xCoord, yCoord, 2 * radius, 2 * radius);

        xCoord += radius;
        yCoord += radius;
        g2d.drawLine(xCoord, yCoord - rayLength, xCoord, yCoord + rayLength);
        g2d.drawLine(xCoord - rayLength, yCoord, xCoord + rayLength, yCoord);
        g2d.drawLine(xCoord - rayLengthDiag, yCoord - rayLengthDiag, xCoord + rayLengthDiag, yCoord + rayLengthDiag);
        g2d.drawLine(xCoord + rayLengthDiag, yCoord - rayLengthDiag, xCoord - rayLengthDiag, yCoord + rayLengthDiag);
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
        drawSun(g, Color.yellow, SOLAR_POSITION_X, SOLAR_POSITION_Y, SOLAR_RADIUS);

        Paint(g);
        bufferStrategy.show();
    }

    protected void Paint(Graphics2D g) {
        try {
            spriteSheet = new SpriteSheet(ImageIO.read(new File(FILENAME)));
            g.drawImage(spriteSheet.crop(myAngle * IMAGE_WIDTH, 0, IMAGE_WIDTH, IMAGE_HEIGHT), myX, ground[myX] - IMAGE_VERTICAL_OFFSET, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (fire) {
            g.setColor(Color.red);
            g.fillOval(myX + IMAGE_WIDTH + 5 * time, ground[myX] - 10 * myAngle, 10, 10);
        }
    }

    public void moveIt(KeyEvent evt) {
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (myAngle <= 12) {
                    myAngle += 1;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (myAngle >= 1) {
                    myAngle -= 1;
                }
                break;
            case KeyEvent.VK_LEFT:
                if (myX >= 5) {
                    myX -= 5;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (myX <= SCREEN_WIDTH - 100) {
                    myX += 5;
                }
                break;
            case KeyEvent.VK_SPACE:
                fire = true;
                time = 0;
                break;
        }
    }
}