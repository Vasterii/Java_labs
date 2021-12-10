package bsu.rfe.java.group7.lab2.khomenko.varB8;

//Импортируются классы, используемые в приложении
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import static java.lang.Math.*;

@SuppressWarnings("serial")

public class MainFrame extends JFrame {

    // Размеры окна приложения в виде констант
    private static final int WIDTH = 440;
    private static final int HEIGHT = 270;

    // Текстовые поля для считывания значений переменных
    private JTextField textFieldX, textFieldY, textFieldZ;

    // Поля выводов
    JLabel labelMemory = new JLabel("0.0", SwingConstants.CENTER);
    JLabel labelResult = new JLabel("0.0");

    // Переменные внутренней памяти
    private Double mem1 = new Double(0);
    private Double mem2 = new Double(0);
    private Double mem3 = new Double(0);

    // Группы радио-кнопок
    private ButtonGroup radioButtons = new ButtonGroup();
    private ButtonGroup memoryRadioButtons = new ButtonGroup();

    // Контейнер для отображения радио-кнопок
    private Box hboxFormulaType = Box.createHorizontalBox();
    private Box hboxMemoryType = Box.createHorizontalBox();
    private int formulaId = 1, memoryId = 1;

    // Формула №1 для рассчѐта
    public Double formula1(Double x, Double y, Double z) {

        double part1 = pow((log((1+x)*(1+x)) + cos(PI*z*z*z)), sin(y));
        double part2 = pow((pow(E, x*x) + cos(pow(E, z)) + sqrt(1/y)), 1/x);
        if (x == -1){
            JOptionPane.showMessageDialog(MainFrame.this,
                    "x не может быть -1 (внутри логарифма не может быть 0)", "" +
                            "Ошибка ввода", JOptionPane.WARNING_MESSAGE);
            return 0.0;
        }
        if (y == 0){
            JOptionPane.showMessageDialog(MainFrame.this,
                    "y не может быть 0 (деление на ноль)", "" +
                            "Ошибка ввода", JOptionPane.WARNING_MESSAGE);
            return 0.0;
        }
        return part1 + part2;
    }

    // Формула №2 для рассчѐта
    public Double formula2(Double x, Double y, Double z) {

        double part1 = y*x*x;
        double part2 = log10(pow(z,y)) + pow(cos(cbrt(x)), 2);
        if (z == 0){
            JOptionPane.showMessageDialog(MainFrame.this,
                    "z не может быть -1 (внутри логарифма не может быть 0)", "" +
                            "Ошибка ввода", JOptionPane.WARNING_MESSAGE);
            return 0.0;
        }
        if (z < 0 && y % 2 != 0){
            JOptionPane.showMessageDialog(MainFrame.this,
                    "z не может быть отрицательным при нечётном y " +
                            "(внутри логарифма не может быть отрицательное число)", "" +
                            "Ошибка ввода", JOptionPane.WARNING_MESSAGE);
            return 0.0;
        }
        if (part2 == 0){
            JOptionPane.showMessageDialog(MainFrame.this,
                    "В знаменателе дроби не может быть 0", "" +
                            "Ошибка вычислений", JOptionPane.WARNING_MESSAGE);
            return 0.0;
        }
        return part1/part2;
    }

    // Вспомогательные методы для добавления кнопок на панель
    private void addRadioButton(String buttonName, final int formulaId) {
        JRadioButton button = new JRadioButton(buttonName);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                MainFrame.this.formulaId = formulaId;
            }
        });
        radioButtons.add(button);
        hboxFormulaType.add(button);
    }
    private void addMemoryRadioButton(String buttonName, final int memoryId) {
        JRadioButton button = new JRadioButton(buttonName);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                MainFrame.this.memoryId = memoryId;
                if (memoryId == 1)
                    labelMemory.setText(mem1.toString());
                if (memoryId == 2)
                    labelMemory.setText(mem2.toString());
                if (memoryId == 3)
                    labelMemory.setText(mem3.toString());
            }
        });
        memoryRadioButtons.add(button);
        hboxMemoryType.add(button);
    }


    // Конструктор класса
    public MainFrame() {
        super("Вычисление формулы");
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();

        // Отцентрировать окно приложения на экране
        setLocation((kit.getScreenSize().width - WIDTH) / 2, (kit.getScreenSize().height - HEIGHT) / 2);

        //Радио-кнопки формул
        hboxFormulaType.add(Box.createHorizontalGlue());
        addRadioButton("Формула 1", 1);
        addRadioButton("Формула 2", 2);
        radioButtons.setSelected(radioButtons.getElements().nextElement().getModel(), true);
        hboxFormulaType.add(Box.createHorizontalGlue());

        //Радио-кнопки памяти
        hboxMemoryType.add(Box.createHorizontalGlue());
        addMemoryRadioButton("Переменная 1", 1);
        addMemoryRadioButton("Переменная 2", 2);
        addMemoryRadioButton("Переменная 3", 3);
        memoryRadioButtons.setSelected(memoryRadioButtons.getElements().nextElement().getModel(), true);
        hboxMemoryType.add(Box.createHorizontalGlue());
        hboxMemoryType.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Создать область с полями ввода для X, Y и Z
        JLabel labelForX = new JLabel("X:");
        textFieldX = new JTextField("0", 10);
        textFieldX.setMaximumSize(textFieldX.getPreferredSize());
        JLabel labelForY = new JLabel("Y:");
        textFieldY = new JTextField("0", 10);
        textFieldY.setMaximumSize(textFieldY.getPreferredSize());
        JLabel labelForZ = new JLabel("Z:");
        textFieldZ = new JTextField("0", 10);
        textFieldZ.setMaximumSize(textFieldZ.getPreferredSize());

        //X
        Box hboxVariableX = Box.createHorizontalBox();
        hboxVariableX.add(labelForX);
        hboxVariableX.add(Box.createHorizontalStrut(10));
        hboxVariableX.add(textFieldX);
        hboxVariableX.add(Box.createHorizontalGlue());
        //Y
        Box hboxVariableY = Box.createHorizontalBox();
        hboxVariableY.add(labelForY);
        hboxVariableY.add(Box.createHorizontalStrut(10));
        hboxVariableY.add(textFieldY);
        hboxVariableY.add(Box.createHorizontalGlue());
        //Z
        Box hboxVariableZ = Box.createHorizontalBox();
        hboxVariableZ.add(labelForZ);
        hboxVariableZ.add(Box.createHorizontalStrut(10));
        hboxVariableZ.add(textFieldZ);
        hboxVariableZ.add(Box.createHorizontalGlue());
        //Всё в один контейнер
        Box hboxVariables = Box.createVerticalBox();
        hboxVariables.add(hboxVariableX);
        hboxVariables.add(hboxVariableY);
        hboxVariables.add(hboxVariableZ);
        hboxVariables.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Область для вывода результата
        JLabel labelForResult = new JLabel("Результат:");
        Box hboxResult = Box.createVerticalBox();
        Box hboxHResult = Box.createHorizontalBox();
        hboxHResult.add(Box.createHorizontalGlue());
        hboxHResult.add(labelForResult);
        hboxHResult.add(Box.createHorizontalStrut(10));
        hboxHResult.add(labelResult);
        hboxHResult.add(Box.createHorizontalGlue());
        hboxResult.add(hboxHResult);
        hboxResult.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        //Объединим результат и переменные для компактности программы
        Box hboxVandR = Box.createHorizontalBox();
        hboxVandR.add(hboxVariables);
        hboxVandR.add(hboxResult);
        hboxVandR.setBorder(BorderFactory.createLineBorder(Color.BLACK));


        // Создать область для кнопок
        JButton buttonCalc = new JButton("Вычислить");
        buttonCalc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    Double x = Double.parseDouble(textFieldX.getText());
                    Double y = Double.parseDouble(textFieldY.getText());
                    Double z = Double.parseDouble(textFieldZ.getText());
                    Double result;
                    if (formulaId == 1)
                        result = formula1(x, y, z);
                    else
                        result = formula2(x, y, z);
                    labelResult.setText(result.toString());
                }
                catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Ошибка в формате записи числа с плавающей точкой", "Ошибочный формат числа",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        //Кнопка Reset
        JButton buttonReset = new JButton("Очистить поля");
        buttonReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                textFieldX.setText("0");
                textFieldY.setText("0");
                textFieldZ.setText("0");
                labelResult.setText("0");
            }
        });

        //Кнопки МС и М+, а также поле вывода значения М+

        Box hboxMemory = Box.createHorizontalBox();
        //Кнопка MC
        JButton buttonMC = new JButton("MC");
        buttonMC.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                if (memoryId == 1)
                    mem1 = (double) 0;
                if (memoryId == 2)
                    mem2 = (double) 0;
                if (memoryId == 3)
                    mem3 = (double) 0;
                labelMemory.setText("0.0");
            }
        });

        //Кнопка M+
        JButton buttonMPlus = new JButton("M+");
        buttonMPlus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                double result = Double.parseDouble(labelResult.getText());
                if (memoryId == 1)
                {
                    mem1 += result;
                    labelMemory.setText(mem1.toString());
                }
                if (memoryId == 2)
                {
                    mem2 += result;
                    labelMemory.setText(mem2.toString());
                }
                if (memoryId == 3)
                {
                    mem3 += result;
                    labelMemory.setText(mem3.toString());
                }
            }
        });
        hboxMemory.add(Box.createHorizontalGlue());
        hboxMemory.add(buttonMPlus);
        hboxMemory.add(Box.createHorizontalStrut(10));
        labelMemory.setPreferredSize(new Dimension(120, 10));
        hboxMemory.add(labelMemory, BorderLayout.CENTER);
        hboxMemory.add(Box.createHorizontalStrut(10));
        hboxMemory.add(buttonMC);
        hboxMemory.add(Box.createHorizontalGlue());
        hboxMemory.setBorder(BorderFactory.createLineBorder(Color.BLACK));


        Box hboxButtons = Box.createHorizontalBox();
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.add(buttonCalc);
        hboxButtons.add(Box.createHorizontalStrut(30));
        hboxButtons.add(buttonReset);
        hboxButtons.add(Box.createHorizontalGlue());
        //hboxButtons.setBorder(BorderFactory.createLineBorder(Color.BLACK));




        // Связать области воедино в компоновке BoxLayout
        Box contentBox = Box.createVerticalBox();
        contentBox.add(Box.createVerticalGlue());
        contentBox.add(hboxFormulaType);
        contentBox.add(hboxMemory);
        contentBox.add(hboxVandR);
        contentBox.add(hboxMemoryType);
        contentBox.add(hboxButtons);
        contentBox.add(Box.createVerticalGlue());
        getContentPane().add(contentBox, BorderLayout.CENTER);
    }
}