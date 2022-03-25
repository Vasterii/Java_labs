package bsu.rfe.java.group7.lab1.khomenko.varA8;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Field extends JPanel {


    private boolean isPaused;
    private boolean blueBallsPaused;

    // Динамический список скачущих мячей
    private ArrayList<BouncingBall> balls = new ArrayList<BouncingBall>(10);


    private Timer repaintTimer = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            repaint();
        }
    });


    // Конструктор класса BouncingBall
    public Field() {
        setBackground(Color.WHITE);
        repaintTimer.start();
    }

    public void pauseBlueBalls(){
        blueBallsPaused = true;
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;

        for (BouncingBall ball: balls) {
            ball.paint(canvas);
        }
    }


    public void addBall() {
        balls.add(new BouncingBall(this));
    }


    // Метод синхронизированный, т.е. только один поток может одновременно быть внутри
    public synchronized void pause() {
        isPaused = true;
    }


    public synchronized void resume() {
        isPaused = false;
        blueBallsPaused = false;
        notifyAll(); // Будим все ожидающие продолжения потоки
    }


    // Синхронизированный метод проверки, может ли мяч двигаться (не включен ли режим паузы?)
    public synchronized void canMove(BouncingBall ball) throws InterruptedException {
        if (isPaused) {
            wait(); // Поток, зашедший внутрь данного метода, засыпает
        }
        if (blueBallsPaused && 2 * (ball.getRedColor() +
                ball.getGreenColor()) < ball.getBlueColor()){
            wait();
        }
    }
}

