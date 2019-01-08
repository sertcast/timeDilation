import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    private static JFrame frame;
    private static JPanel mainPanel;
    private static JButton start, reset;
    private static Display displayTop, displayBottom;
    private static JSlider speedDisplayTopSlider, speedDisplayBottomSlider, speedLightSlider;

    private static final int FRAMEW = 1000, FRAMEH = 700;
    private static final int DISPLAYW = FRAMEW - 20, DISPLAYH = FRAMEH / 4;
    private static int SPEED_OF_LIGHT = 7;
    private static final int Y_BOX = 10, W_BOX = 100, H_BOX = DISPLAYH - Y_BOX * 2;

    private static class Display extends JPanel implements ActionListener {

        private int size_particle = 20;
        private double x_box = 10, xspeed = 0;
        private double y_particle = Y_BOX + H_BOX - size_particle, yspeed_particle = 0;
        private double x_particle = x_box + (W_BOX - size_particle) / 2;

        private double pyspeed;
        private boolean animate = false;
        private Timer tm = new Timer(5, this);

        public Display(int xspeed) {
            resetAnimation(xspeed);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            this.setBackground(Color.white);

            g.setColor(Color.black);
            g.drawRect((int) x_box, Y_BOX, W_BOX, H_BOX);

            g.setColor(Color.red);
            g.fillOval((int) x_particle, (int) y_particle, size_particle, size_particle);

            if (animate) {
                if (x_box + W_BOX + xspeed >= DISPLAYW) {
                    animate = false;
                } else {

                    x_box += xspeed;
                    x_particle += xspeed;
                    y_particle += yspeed_particle;


                    if (y_particle + yspeed_particle + size_particle >= Y_BOX + H_BOX) {
                        yspeed_particle = -pyspeed;
                    } else if (y_particle + yspeed_particle <= Y_BOX) {
                        yspeed_particle = pyspeed;
                    }
                }
            }

            tm.start();

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            repaint();
        }

        private void startAnimation() {
            animate = true;
        }

        private void resetAnimation(int xspeed) {
            x_box = 10;
            this.xspeed = xspeed * 0.1 * SPEED_OF_LIGHT;
            x_particle = x_box + ((double) W_BOX - size_particle) / 2;
            y_particle = Y_BOX + H_BOX - size_particle;
            pyspeed = (Math.sqrt(Math.pow(SPEED_OF_LIGHT, 2) - Math.pow(xspeed, 2)));
            yspeed_particle = -pyspeed;
            animate = false;
        }
    }

    public static void main(String[] args) {
        mainPanel = new JPanel();
        mainPanel.setLayout(null);


        start = new JButton("START");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayTop.startAnimation();
                displayBottom.startAnimation();
            }
        });
        start.setBounds(FRAMEW - 120, 450, 100, 50);

        reset = new JButton("RESET");
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayTop.resetAnimation(speedDisplayTopSlider.getValue());
                displayBottom.resetAnimation(speedDisplayBottomSlider.getValue());
            }
        });
        reset.setBounds(FRAMEW - 120, 500, 100, 50);

        speedDisplayTopSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
        speedDisplayTopSlider.setMajorTickSpacing(5);
        speedDisplayTopSlider.setMinorTickSpacing(1);
        speedDisplayTopSlider.setPaintTicks(true);
        speedDisplayTopSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                displayTop.resetAnimation(speedDisplayTopSlider.getValue());
                displayBottom.resetAnimation(speedDisplayBottomSlider.getValue());
            }
        });
        speedDisplayTopSlider.setBounds(10, 400, FRAMEW - 200, 100);

        speedDisplayBottomSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
        speedDisplayBottomSlider.setMajorTickSpacing(5);
        speedDisplayBottomSlider.setMinorTickSpacing(1);
        speedDisplayBottomSlider.setPaintTicks(true);
        speedDisplayBottomSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                displayTop.resetAnimation(speedDisplayTopSlider.getValue());
                displayBottom.resetAnimation(speedDisplayBottomSlider.getValue());
            }
        });
        speedDisplayBottomSlider.setBounds(10, 500, FRAMEW - 200, 100);

        speedLightSlider = new JSlider(JSlider.HORIZONTAL, 3, 7, 3);
        speedLightSlider.setMinorTickSpacing(1);
        speedLightSlider.setPaintTicks(true);
        speedLightSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                SPEED_OF_LIGHT = speedLightSlider.getValue();
                displayTop.resetAnimation(speedDisplayTopSlider.getValue());
                displayBottom.resetAnimation(speedLightSlider.getValue());
            }
        });
        speedLightSlider.setBounds(10, 600, FRAMEW - 200, 100);

        displayTop = new Display(speedDisplayTopSlider.getValue());
        displayTop.setBounds(10, 10, DISPLAYW, DISPLAYH);

        displayBottom = new Display(speedDisplayBottomSlider.getValue());
        displayBottom.setBounds(10, 20 + DISPLAYH, DISPLAYW, DISPLAYH);

        mainPanel.add(displayTop);
        mainPanel.add(displayBottom);
        mainPanel.add(start);
        mainPanel.add(reset);
        mainPanel.add(speedDisplayTopSlider);
        mainPanel.add(speedDisplayBottomSlider);
        mainPanel.add(speedLightSlider);

        frame = new JFrame("Time thing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(FRAMEW, FRAMEH));
        frame.setResizable(false);
        frame.add(mainPanel);
        frame.setVisible(true);

    }


}
