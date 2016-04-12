import java.awt.*;


public class Landscape {
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
}
