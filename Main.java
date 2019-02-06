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

        if (wave.size() > 500){
            wave.remove(wave.size()-1);
        }
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setBackground(new Color(0, 0, 0));

        g2.translate(200, 200);
        double x = 0;
        double y = 0;
        double radius;

        for (int i=0;i<20;i+= 2){
            radius = 100/(i+1);
            drawCenteredCircle(g2,x,y,radius);

            x = radius/2 * (Math.cos(time*(i+1)) + 0) + x;
            y = radius/2 * (Math.sin(time*(i+1)) + 0) + y;

        }

        wave.add(0,new Point2D.Double(x, y));


        drawPoint(g2,x,y);

        Path2D path2D = new Path2D.Float();
        path2D.moveTo(200, wave.get(0).getY());
        int index=0;
        for (Point2D temp : wave) {
            path2D.lineTo(index+200,temp.getY());
            index++;
        }

        g2.draw(path2D);


    }

    private void drawPoint(Graphics2D g2, double x, double y) {
        double r = 4;
        x = x-(r/2);
        y = y-(r/2);
        Ellipse2D ellipse2D = new Ellipse2D.Double(x, y, r,r);
        g2.fill(ellipse2D);

    }

    public void drawCenteredCircle(Graphics2D g, double x, double y, double r) {
        x = x-(r/2);
        y = y-(r/2);
        Ellipse2D ellipse2D = new Ellipse2D.Double(x, y, r,r);
        g.draw(ellipse2D);
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
