////package com.sandbox;
//
//import javax.imageio.ImageIO;
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//import javax.swing.WindowConstants;
//import java.awt.Graphics;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//
//public class SwingSandbox {
//
//    public static void main(String[] args) throws IOException {
//        JFrame frame = Points.buildFrame();
//
//        final BufferedImage image = ImageIO.read(new File("C:\\Users\\sivanov\\Desktop\\Group site\\lujoo.jpg"));
//
//        JPanel pane = new JPanel() {
//            @Override
//            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
//                g.drawImage(image, 100, 100, null);
//            }
//        };
//
//
//        frame.add(pane);
//    }
//
//
//
//
//
//}
