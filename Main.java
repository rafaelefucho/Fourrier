package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class Main extends JComponent implements Runnable {

    static int HeightPanel = 360 * 2;
    static int WidthPanel = 360 * 2;
    double time = Math.PI;
    ArrayList<Point2D> wave;
    ArrayList<ResultImaginary> fourierX;
    ArrayList<ResultImaginary> fourierY;




    private ArrayList<ResultImaginary> dft(ArrayList<Double> yImage) {
        ArrayList<ResultImaginary> result = new ArrayList<>();
        int N = yImage.size();

        for (int k = 0; k < N; k++) {
            double re = 0;
            double im = 0;
            for (int n = 0; n < N; n++) {
                double phi = (2 * Math.PI * k * n) / N;
                re += yImage.get(n) * Math.cos(phi);
                im -= yImage.get(n) * Math.sin(phi);
            }


            re = re / N;
            im = im / N;

            result.add(k, new ResultImaginary(re,im,k));

        }

        result.sort(new Comparator<ResultImaginary>() {
            @Override
            public int compare(ResultImaginary o1, ResultImaginary o2) {
                return (int)(o2.amp - o1.amp);
            }
        });
        return result;
    }

    private void timeStep() {
        double dt = 2*Math.PI/fourierY.size();
        time += dt;

        if (time > 3*Math.PI) {
//            wave.remove(wave.size() - 1);
            wave.clear();
            time = Math.PI;
        }
    }
    public Main() {
        wave = new ArrayList<>();
        wave = new ArrayList<>();

        ArrayList<Double> yImage = new ArrayList<>();
        ArrayList<Double> xImage = new ArrayList<>();


        for (int i = 0; i<100;i++){
            double angle = mapP5(i, 0d,100d,0,Math.PI*2);
            Random random = new Random();

            xImage.add(100 * Math.cos(angle));
        }

        for (int i = 0; i<100;i++){
            double angle = mapP5(i, 0d,100d,0,Math.PI*2);
            yImage.add(100 * Math.sin(angle));
        }

        fourierX = dft(xImage);
        fourierY = dft(yImage);


        Thread t = new Thread(this);
        t.start();
    }

    private double mapP5(int n, double start1, double stop1, int start2, double stop2) {

        return ((n-start1)/(stop1-start1))*(stop2-start2)+start2;

    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setBackground(new Color(0, 0, 0));


        Point2D point2DX = epiCycles(g2,400,200,0,fourierX);
        Point2D point2DY = epiCycles(g2,100,400,Math.PI/2,fourierY);

        wave.add(0,new Point2D.Double(point2DX.getX(),point2DY.getY()));


        Line2D line2DX = new Line2D.Double(point2DX.getX(),point2DX.getY(),point2DX.getX(),point2DY.getY());
        Line2D line2DY = new Line2D.Double(point2DY.getX(),point2DY.getY(),point2DX.getX(),point2DY.getY());
        g2.setStroke(new BasicStroke(1/2));
        g2.draw(line2DX);
        g2.draw(line2DY);



        Path2D path2D = new Path2D.Float();
        path2D.moveTo(wave.get(0).getX(), wave.get(0).getY());
        for (Point2D temp : wave) {
            path2D.lineTo(temp.getX(), temp.getY());
        }
        g2.setStroke(new BasicStroke(1));
        g2.draw(path2D);



    }

    private Point2D.Double epiCycles(Graphics2D g2, double X, double Y, double rotation, ArrayList<ResultImaginary> fourier) {
        double x = X;
        double y = Y;

        for (int i = 0; i < fourier.size(); i ++) {

            double radius = fourier.get(i).amp;
            double freq = fourier.get(i).freq;
            double phase = fourier.get(i).phase;

            g2.setStroke(new BasicStroke(1 / 2));
            drawCenteredCircle(g2, x, y, radius);

            double xC = x;
            double yC = y;

            x += radius  * (Math.cos(freq * time + phase + rotation));
            y += radius  * (Math.sin(freq * time + phase + rotation));

            g2.setStroke(new BasicStroke(1));
            Line2D line2D = new Line2D.Double(xC, yC, x, y);
            g2.draw(line2D);

        }

        drawPoint(g2, x, y);

        return new Point2D.Double(x,y);


    }

    private void drawPoint(Graphics2D g2, double x, double y) {
        double r = 4;
        x = x - (r / 2);
        y = y - (r / 2);
        Ellipse2D ellipse2D = new Ellipse2D.Double(x, y, r, r);
        g2.fill(ellipse2D);

    }

    public void drawCenteredCircle(Graphics2D g, double x, double y, double r) {
        x = x - (r / 2);
        y = y - (r / 2);
        Ellipse2D ellipse2D = new Ellipse2D.Double(x, y, r, r);
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
