import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;


public class Points extends JPanel {

    private String FILENAME = "C:\\Users\\sivanov\\Desktop\\Homeworks\\Tanks\\backfire-effect.png";
    private int SOLAR_RADIUS = 50;
    private int SOLAR_POSITION_X = 50;
    private int SOLAR_POSITION_Y = 50;
    private int IMAGE_WIDTH = 80;
    private int IMAGE_HEIGHT = 80;
    private int IMAGE_VERTICAL_OFFSET = 35;


    private int[] ground = new int[1000];

    public void defineGround(int[] ground) {
        double consts = 100;
        ground[0] = 500;
        for (int i = 1; i < ground.length; i++) {
            ground[i] = ground[0] + 2 * (int) (-1.54778 + 9 * Math.sin(100 - 0.6 * i / consts) - 6 * Math.sin(40 - 0.4 * i / consts) + 5 * Math.sin(100 + 2 * i / consts));
        }
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        Dimension size = getSize(); // The size of the window
        int width = size.width;
        int height = size.height;

        defineGround(ground);
        drawGround(ground, g2d, Color.green, width, height);
        drawSun(g2d, Color.yellow, SOLAR_POSITION_X, SOLAR_POSITION_Y, SOLAR_RADIUS);

//        for (int i = 0; i < 100; i++) {
//            drawPlayer(g2d, i);
//            drawPlayer(g2d, 550);
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }
        drawPlayer(g2d, 100);
        drawPlayer(g2d, 550);

    }

    private void drawPlayer(Graphics2D g2d, int xPos) {
        try {
            final BufferedImage image = ImageIO.read(new File(FILENAME));
            g2d.drawImage(image, xPos, ground[xPos] - IMAGE_VERTICAL_OFFSET, IMAGE_WIDTH, IMAGE_HEIGHT, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void drawGround(int[] ground, Graphics2D g2d, Color clr, int width, int height) {
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


    public static void main(String[] args) throws IOException {
        Points points = new Points();
        JFrame frame = new JFrame("Points");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(points);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}