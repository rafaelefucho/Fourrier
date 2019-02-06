package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Main extends JComponent implements Runnable {

    static int HeightPanel = 360 * 2;
    static int WidthPanel = 360 * 2;
    double time = Math.PI;
    ArrayList<Point2D> wave;

    public Main() {
        wave = new ArrayList<>();
        Thread t = new Thread(this);
        t.start();
    }

    private void timeStep() {
        time += 0.05;

        if (wave.size() > 200){
            wave.remove(0);
        }
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setBackground(new Color(0, 0, 0));

        g2.translate(200, 200);

        double radius = 50;
        Ellipse2D ellipse2D = new Ellipse2D.Double(0 - radius, 0 - radius, radius * 2, radius * 2);
        g2.draw(ellipse2D);

        double x = radius * Math.cos(time);
        double y = radius * Math.sin(time);

        wave.add(new Point2D.Double(x, y));

        Ellipse2D point = new Ellipse2D.Double(
                ((Ellipse2D.Double) ellipse2D).x + x + radius,
                ((Ellipse2D.Double) ellipse2D).y + y + radius,
                5, 5);
        g2.fill(point);

        Path2D path2D = new Path2D.Float();
        path2D.moveTo(100, wave.get(0).getY());
        int index=0;
        for (Point2D temp : wave) {
            path2D.lineTo(index+100,temp.getY());
            index++;
        }

        g2.draw(path2D);


    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Fourrier");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new Main());
        f.setSize(WidthPanel, HeightPanel);
        f.setBackground(new Color(0, 0, 0));
        f.setVisible(true);
    }

    @Override
    public void run() {
        try {
            while (true) {
                timeStep();
                repaint();
                Thread.sleep(1000 / 24);
            }
        } catch (InterruptedException ie) {
        }
    }


}
