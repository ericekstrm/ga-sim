package bilsimulator;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bil implements Runnable {

    static BufferedImage bgi = new BufferedImage(600, 400, BufferedImage.TYPE_INT_RGB);

    static JPanel panel;
    static JFrame frame;
    String hostName = "192.101.10";
    int portNumber = 13;

    static BilVar input;

    public static void main(String[] args) {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        panel = new JPanel();
        frame.add(panel);

        input = new BilVar();

        Thread th = new Thread();
        th.start();

        paintThread();
    }

    BufferedReader in;
    PrintWriter out;

    @Override
    public void run() {
        try {
            Socket echoSocket = new Socket(hostName, portNumber);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        } catch (IOException ioe) {
            System.out.println("Test");
        }
        while (true) {
            try {
                String s = in.readLine();

                String[] thingi = s.split(" ");

                String[] b = new String[3];

                for (int i = 0; i < thingi.length; i++) {
                    b[i] = Integer.toBinaryString(Integer.parseInt(thingi[i]));
                    int bitsToAdd = 8 - b[i].length();
                    for (int k = 0; k < bitsToAdd; k++) {
                        b[i] = "0" + b[i];
                    }
                }
                int speed = Byte.parseByte(b[0].substring(1), 2);
                if (b[0].charAt(0) == '0') {
                    speed = -speed;
                }

                int steering = Byte.parseByte(b[1].substring(1), 2);
                if (b[1].charAt(0) == '0') {
                    steering = -steering;
                }

                BilVar.speed = speed;
                BilVar.steering = steering;
            } catch (IOException ex) {

            }
        }
    }

    private static void paintThread() {

        while (true) {
            //Graphics2D g = (Graphics2D) panel.getGraphics();
            bgi.getGraphics().clearRect(0, 0, frame.getWidth(), frame.getHeight());
            paint((Graphics2D) bgi.getGraphics());
            panel.getGraphics().drawImage(bgi, 0, 0, null);
        }

    }

    public static void paint(Graphics2D g) {
        g.clearRect(0, 0, frame.getWidth(), frame.getHeight());
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 600, 400);
        g.setColor(Color.black);
        g.drawOval(400, 70, 50, 50);
        g.drawOval(470, 70, 50, 50);

        //g.drawRect(140, 100, 60, 40);
        //g.drawRect(110, 55, 120, 40);
        //g.drawRect(80, 10, 180, 40);

        g.drawRect(80, 300, 180, 40);
        g.drawRect(110, 255, 120, 40);
        g.drawRect(140, 210, 60, 40);

        g.drawRect(80, 300, 180, 40);
        g.drawRect(110, 255, 120, 40);
        g.drawRect(140, 210, 60, 40);

        g.drawRect(10, 85, 40, 180);
        g.drawRect(55, 115, 40, 120);
        g.drawRect(100, 145, 40, 60);

        g.drawRect(200, 145, 40, 60);
        g.drawRect(245, 115, 40, 120);
        g.drawRect(290, 85, 40, 180);

        if (input.leftBlinkers == true) {
            g.setColor(Color.yellow);
            g.fillOval(400, 70, 50, 50);

        }
        if (input.rightBlinkers == true) {
            g.setColor(Color.yellow);
            g.fillOval(470, 70, 50, 50);
        }
        switch (input.speed) {
            case 1:
                g.setColor(Color.green);
                g.fillRect(140, 100, 60, 40);
                break;
            case 2:
                g.setColor(Color.green);
                g.fillRect(140, 100, 60, 40);
                g.fillRect(110, 55, 120, 40);
                break;
            case 3:
                g.setColor(Color.green);
                g.fillRect(140, 100, 60, 40);
                g.fillRect(110, 55, 120, 40);
                g.fillRect(80, 10, 180, 40);
                break;
            case -1:
                g.setColor(Color.red);
                g.fillRect(140, 210, 60, 40);
                break;
            case -2:
                g.setColor(Color.red);
                g.fillRect(140, 210, 60, 40);
                g.fillRect(110, 255, 120, 40);
                break;
            case -3:
                g.setColor(Color.red);
                g.fillRect(140, 210, 60, 40);
                g.fillRect(110, 255, 120, 40);
                g.fillRect(80, 300, 180, 40);
                break;
        }
        g.setColor(Color.blue);
        switch (input.steering) {
            case 1:
                g.fillRect(200, 145, 40, 60);
                break;
            case 2:
                g.fillRect(200, 145, 40, 60);
                g.fillRect(245, 115, 40, 120);
                break;
            case 3:
                g.fillRect(200, 145, 40, 60);
                g.fillRect(245, 115, 40, 120);
                g.fillRect(290, 85, 40, 180);
                break;
            case -1:
                g.fillRect(100, 145, 40, 60);
                break;
            case -2:
                g.fillRect(100, 145, 40, 60);
                g.fillRect(55, 115, 40, 120);
                break;
            case -3:
                g.fillRect(100, 145, 40, 60);
                g.fillRect(55, 115, 40, 120);
                g.fillRect(10, 85, 40, 180);
                break;
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(Bil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
