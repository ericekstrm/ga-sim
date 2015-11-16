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

public class Bil extends JFrame implements Runnable {

    static BufferedImage bgi = new BufferedImage(600, 400, BufferedImage.TYPE_INT_RGB);

    static JPanel panel;
    String hostName = "148.136.200.190";
    int portNumber = 25566;

    public static void main(String[] args) {
        new Bil();
    }

    public Bil() {
        setTitle("Simulator Bil");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setVisible(true);
        setLocationRelativeTo(null);
        panel = new JPanel();
        add(panel);

        Thread th = new Thread(this);
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
                System.out.println(s);

                String[] split = s.split(" ");

                int speed = Integer.parseInt(split[0]);
                if (speed >= 128) {
                    speed -= 128;

                } else {
                    speed = -speed;
                }
                int steering = Integer.parseInt(split[1]);
                if (steering >= 128) {
                    steering -= 128;

                } else {
                    steering = -steering;
                }
                BilVar.speed = speed;
                BilVar.steering = steering;

                System.out.println(speed + "  " + steering);

            } catch (IOException ex) {

            }
        }
    }

    private void paintThread() {
        while (true) {
            //Graphics2D g = (Graphics2D) panel.getGraphics();
            bgi.getGraphics().clearRect(0, 0, getWidth(), getHeight());
            paint((Graphics2D) bgi.getGraphics());
            panel.getGraphics().drawImage(bgi, 0, 0, null);
        }
    }

    public void paint(Graphics2D g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 600, 400);
        g.setColor(Color.black);
        g.drawOval(400, 70, 50, 50);
        g.drawOval(470, 70, 50, 50);
        System.out.println(BilVar.speed + " " + BilVar.steering);
        g.setColor(Color.red);
        for (int i = 0; i < 10; i++) {
            int n = (int) (BilVar.speed / 12.8);
            if (n < i + 1) {
                g.setColor(Color.black);
            }
            g.fillRect(125 - 5 * i, 120 - 12 * i, 90 + 10 * i, 8);
        }
        g.setColor(Color.black);
        for (int i = 0; i < 10; i++) {
            int n = (int) (BilVar.speed / 12.8);
            if (n > i) {
                g.setColor(Color.red);
            }
            g.fillRect(80 + 5 * i, 330 - 12 * i, 180 - 10 * i, 8);
        }

        g.setColor(Color.black);
        for (int i = 0; i < 10; i++) {
            int n = (int) (BilVar.steering / 12.8);
            if (n > i) {
                g.setColor(Color.red);
            }
            g.fillRect(12 * i + 8, 5 * i + 85, 8, 180 - 10 * i);
        }

        g.setColor(Color.red);
        for (int i = 0; i < 10; i++) {
            int n = (int) (BilVar.steering / 12.8);
            if (n < i) {
                g.setColor(Color.black);
            }
            g.fillRect(12 * i + 215, 130 - 5 * i, 8, 90 + 10 * i);
        }
        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {
            Logger.getLogger(Bil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
