package bsu.rfe.java.group7.lab1.khomenko.varA8;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

    // Константы, задающие размер окна приложения, если оно не распахнуто на весь экран
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;
    private JMenuItem pauseMenuItem;
    private JMenuItem resumeMenuItem;

    private JMenuItem pauseRedMenuItem;
    private JMenuItem pauseGreenMenuItem;
    private JMenuItem pauseBlueMenuItem;

    private JMenuItem pauseFirstQuarterMenuItem;
    private JMenuItem pauseSecondQuarterMenuItem;
    private JMenuItem pauseThirdQuarterMenuItem;
    private JMenuItem pauseForthQuarterMenuItem;

    // Поле, по которому прыгают мячи
    private Field field = new Field();



    public MainFrame() {

        super("Программирование и синхронизация потоков");
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();

        // Отцентрировать окно приложения на экране
        setLocation((kit.getScreenSize().width - WIDTH) / 2,
                (kit.getScreenSize().height - HEIGHT) / 2);

        // Установить начальное состояние окна развернутым на весь экран
        setExtendedState(MAXIMIZED_BOTH);


        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);


        JMenu ballMenu = new JMenu("Мячи");
        Action addBallAction = new AbstractAction("Добавить мяч") {
            public void actionPerformed(ActionEvent event) {
                field.addBall();
                // Ни один из пунктов меню не являются доступными - сделать доступным "Паузу"
                if (!pauseMenuItem.isEnabled() && !resumeMenuItem.isEnabled()) {
                    pauseMenuItem.setEnabled(true);
                    pauseRedMenuItem.setEnabled(true);
                    pauseGreenMenuItem.setEnabled(true);
                    pauseBlueMenuItem.setEnabled(true);
                }
            }
        };
        menuBar.add(ballMenu);
        ballMenu.add(addBallAction);


        JMenu controlMenu = new JMenu("Управление");
        menuBar.add(controlMenu);

        JMenu pauseColoredBallsMenu = new JMenu("Остановить мячи определённого цвета");
        JMenu pauseAngledBallsMenu = new JMenu("Остановить мячи определённого угла");
        JMenu pauseSpeededBallsMenu = new JMenu("Остановить мячи определённой скорости");
        JMenu pauseRadiusedBallsMenu = new JMenu("Остановить мячи определённого радиуса");

        Action pauseAction = new AbstractAction("Приостановить движение") {
            public void actionPerformed(ActionEvent event) {
                field.pause();
                pauseMenuItem.setEnabled(false);
                pauseRedMenuItem.setEnabled(false);
                pauseGreenMenuItem.setEnabled(false);
                pauseBlueMenuItem.setEnabled(false);
                resumeMenuItem.setEnabled(true);
            }
        };
        pauseMenuItem = controlMenu.add(pauseAction);
        pauseMenuItem.setEnabled(false);

        Action resumeAction = new AbstractAction("Возобновить движение") {
            public void actionPerformed(ActionEvent event) {
                field.resume();
                pauseMenuItem.setEnabled(true);

                pauseRedMenuItem.setEnabled(true);
                pauseGreenMenuItem.setEnabled(true);
                pauseBlueMenuItem.setEnabled(true);

                pauseFirstQuarterMenuItem.setEnabled(true);
                pauseSecondQuarterMenuItem.setEnabled(true);
                pauseThirdQuarterMenuItem.setEnabled(true);
                pauseForthQuarterMenuItem.setEnabled(true);

                resumeMenuItem.setEnabled(false);
            }
        };
        resumeMenuItem = controlMenu.add(resumeAction);
        resumeMenuItem.setEnabled(false);






        Action pauseRedAction = new AbstractAction("Остановить красные мячи") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                field.pauseRedBalls();
                pauseRedMenuItem.setEnabled(false);
                resumeMenuItem.setEnabled(true);
            }
        };
        pauseRedMenuItem = pauseColoredBallsMenu.add(pauseRedAction);
        pauseRedMenuItem.setEnabled(true);

        Action pauseGreenAction = new AbstractAction("Остановить зелёные мячи") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                field.pauseGreenBalls();
                pauseGreenMenuItem.setEnabled(false);
                resumeMenuItem.setEnabled(true);
            }
        };
        pauseGreenMenuItem = pauseColoredBallsMenu.add(pauseGreenAction);
        pauseGreenMenuItem.setEnabled(true);

        Action pauseBlueAction = new AbstractAction("Остановить синие мячи") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                field.pauseBlueBalls();
                pauseBlueMenuItem.setEnabled(false);
                resumeMenuItem.setEnabled(true);
            }
        };
        pauseBlueMenuItem = pauseColoredBallsMenu.add(pauseBlueAction);
        pauseBlueMenuItem.setEnabled(true);

        controlMenu.add(pauseColoredBallsMenu);






        Action pauseFirstQuarter = new AbstractAction("Остановить мячи с углом 1 четверти") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                field.pauseFirstQuarter();
                pauseFirstQuarterMenuItem.setEnabled(false);
                resumeMenuItem.setEnabled(true);
            }
        };
        pauseFirstQuarterMenuItem = pauseAngledBallsMenu.add(pauseFirstQuarter);
        pauseFirstQuarterMenuItem.setEnabled(true);

        Action pauseSecondQuarter = new AbstractAction("Остановить мячи с углом 2 четверти") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                field.pauseSecondQuarter();
                pauseSecondQuarterMenuItem.setEnabled(false);
                resumeMenuItem.setEnabled(true);
            }
        };
        pauseSecondQuarterMenuItem = pauseAngledBallsMenu.add(pauseSecondQuarter);
        pauseSecondQuarterMenuItem.setEnabled(true);

        Action pauseThirdQuarter = new AbstractAction("Остановить мячи с углом 3 четверти") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                field.pauseThirdQuarter();
                pauseThirdQuarterMenuItem.setEnabled(false);
                resumeMenuItem.setEnabled(true);
            }
        };
        pauseThirdQuarterMenuItem = pauseAngledBallsMenu.add(pauseThirdQuarter);
        pauseThirdQuarterMenuItem.setEnabled(true);

        Action pauseForthQuarter = new AbstractAction("Остановить мячи с углом 4 четверти") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                field.pauseForthQuarter();
                pauseForthQuarterMenuItem.setEnabled(false);
                resumeMenuItem.setEnabled(true);
            }
        };
        pauseForthQuarterMenuItem = pauseAngledBallsMenu.add(pauseForthQuarter);
        pauseForthQuarterMenuItem.setEnabled(true);

        controlMenu.add(pauseAngledBallsMenu);

        // Добавить в центр граничной компоновки поле Field
        getContentPane().add(field, BorderLayout.CENTER);
    }
}
