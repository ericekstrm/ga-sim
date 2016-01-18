package bilsimulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
        new BilVar();
        setTitle("Simulator Bil");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                if (socket != null) {
                    if (!socket.isClosed()) {
                        out.println("d");
                    }
                }
                System.exit(0);
            }
        });
        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
        panel = new JPanel();
        add(panel);

        Thread th = new Thread(this);
        th.start();

        paintThread();
    }

    Socket socket;
    BufferedReader in;
    PrintWriter out;

    @Override
    public void run() {
        try {
            socket = new Socket(hostName, portNumber);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ioe) {
            System.out.println("Va Fan, inte bra");
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
            } catch (IOException ex) {

            }
        }
    }

    private void paintThread() {
        while (true) {
            paint((Graphics2D) bgi.getGraphics());
            panel.getGraphics().drawImage(bgi, 0, 0, null);
        }
    }

    public void paint(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.BLACK);
        g.drawOval(400, 70, 50, 50);
        g.drawOval(470, 70, 50, 50);

        //framåt
        g.setColor(Color.red);
        for (int i = 0; i < 9; i++) {
            int n = (int) (BilVar.speed / 12.8);
            if (n < i + 1) {
                g.setColor(Color.black);
            }
            g.fillRect(125 - 5 * i, 120 - 12 * i, 90 + 10 * i, 8);
        }
        //bakåt
        g.setColor(Color.red);
        for (int i = 0; i < 9; i++) {
            int n = (int) (BilVar.speed / 12.8);
            if (n >= -i) {
                g.setColor(Color.black);
            }
            g.fillRect(126 - 5 * i, 222 + 12 * i, 89 + 10 * i, 8);
        }
        //vänster
        g.setColor(Color.red);
        for (int i = 0; i < 9; i++) {
            int n = (int) (BilVar.steering / 12.8);
            if (n >= -i) {
                g.setColor(Color.black);
            }
            g.fillRect(117 - 12 * i, 130 - 5 * i, 8, 91 + 10 * i);
        }
        //höger
        g.setColor(Color.red);
        for (int i = 0; i < 9; i++) {
            int n = (int) (BilVar.steering / 12.8);
            if (n <= i) {
                g.setColor(Color.black);
            }
            g.fillRect(12 * i + 215, 130 - 5 * i, 8, 90 + 10 * i);
        }

        if (BilVar.leftBlinkers == true) {
            g.setColor(Color.yellow);
            g.fillOval(400, 70, 50, 50);
        }
        if (BilVar.rightBlinkers == true) {
            g.setColor(Color.yellow);
            g.fillOval(470, 70, 50, 50);
        }
        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {
            Logger.getLogger(Bil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
