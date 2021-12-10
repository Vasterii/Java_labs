package bsu.rfe.java.group7.lab3.khomenko.varB4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

    private static final int WIDTH = 700, HEIGHT = 500;

    private Double[] coefficients;  // Массив коэффициентов многочлена

    // Объект диалогового окна для выбора файлов
    // Компонент не создается изначально, т.к. может не понадобиться (если данные не сохранятся в файл пользователем)
    private JFileChooser fileChooser = null;

    // Элементы меню вынесены в поля данных класса, так как ими необходимо манипулировать из разных мест
    private JMenuItem saveToTextMenuItem, saveToGraphicsMenuItem, searchValueMenuItem, aboutProgramMenuItem;

    // Поля ввода для считывания значений переменных
    private JTextField textFieldFrom, textFieldTo, textFieldStep;
    private Box hBoxResult;

    // Визуализатор ячеек таблицы
    private GornerTableCellRenderer renderer = new GornerTableCellRenderer();

    // Модель данных с результатами вычислений
    private GornerTableModel data;


    public MainFrame(Double[] coefficients) {

        super("Табулирование многочлена на отрезке по схеме Горнера"); // Обязательный вызов конструктора предка
        this.coefficients = coefficients; // Запомнить во внутреннем поле переданные коэффициенты

        // Установить размеры окна и отцентрировать его на экране
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH) / 2, (kit.getScreenSize().height - HEIGHT) / 2);

        // Меню
        JMenuBar menuBar = new JMenuBar(); // Создать меню
        setJMenuBar(menuBar); // Установить меню в качестве главного меню приложения
        JMenu fileMenu = new JMenu("Файл"); // Добавить в меню пункт меню "Файл"
        menuBar.add(fileMenu); // Добавить его в главное меню
        JMenu tableMenu = new JMenu("Таблица"); // Создать пункт меню "Таблица"
        menuBar.add(tableMenu);
        JMenu helpMenu = new JMenu("Справка");
        menuBar.add(helpMenu);

        // Создать новое действие по сохранению в текстовый файл
        Action saveToTextAction = new AbstractAction("Сохранить в текстовый файл") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) { // Если экземпляр диалогового окна "Открыть файл" еще не создан:
                    fileChooser = new JFileChooser(); // создать его
                    fileChooser.setCurrentDirectory(new File(".")); // и инициализировать текущей директорией
                }
                // Показать диалоговое окно
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION)
                    // Если результат его показа успешный, сохранить данные в текстовый файл
                    saveToTextFile(fileChooser.getSelectedFile());
            }
        };


        // Добавить соответствующий пункт подменю в меню "Файл"
        saveToTextMenuItem = fileMenu.add(saveToTextAction);
        saveToTextMenuItem.setEnabled(false); // данных ещё нет

        // Создать новое действие по сохранению в текстовый файл
        Action saveToGraphicsAction = new AbstractAction("Сохранить данные для построения графика") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                // Показать диалоговое окно
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) ;
                // Если результат его показа успешный, сохранить данные в двоичный файл
                saveToGraphicsFile(fileChooser.getSelectedFile());
            }
        };


        // Добавить соответствующий пункт подменю в меню "Файл"
        saveToGraphicsMenuItem = fileMenu.add(saveToGraphicsAction);
        saveToGraphicsMenuItem.setEnabled(false);

        // Создать новое действие по поиску значений многочлена
        Action searchValueAction = new AbstractAction("Найти значение многочлена или X") {
            public void actionPerformed(ActionEvent event) {
                // Запросить пользователя ввести искомую строку
                String value = JOptionPane.showInputDialog(MainFrame.this,
                        "Введите значение для поиска", "Поиск значения", JOptionPane.QUESTION_MESSAGE);
                renderer.setNeedle(value); // Установить введенное значение в качестве иголки
                getContentPane().repaint(); // Обновить таблицу
            }
        };

        // Добавить действие в меню "Таблица"
        searchValueMenuItem = tableMenu.add(searchValueAction);
        searchValueMenuItem.setEnabled(false);

        // Создать новое действие по выведению описания программы
        Action aboutProgramAction = new AbstractAction("О программе") {
            public void actionPerformed(ActionEvent event) {
                String message = "";
                try {
                    Scanner scanner = new Scanner(new File("README.txt"));
                    while (scanner.hasNextLine())
                    {
                        message += scanner.nextLine() + "\n";
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                JOptionPane.showMessageDialog(MainFrame.this, message);
            }
        };

        // Добавить действие в меню "Справка"
        aboutProgramMenuItem = helpMenu.add(aboutProgramAction);
        aboutProgramMenuItem.setEnabled(true);

        // Создать область с полями ввода для границ отрезка и шаг
        JLabel labelForFrom = new JLabel("X изменяется на интервале от:");
        textFieldFrom = new JTextField("0.0", 10);
        textFieldFrom.setMaximumSize(textFieldFrom.getPreferredSize());

        JLabel labelForTo = new JLabel("до:");
        textFieldTo = new JTextField("1.0", 10);
        textFieldTo.setMaximumSize(textFieldTo.getPreferredSize());

        JLabel labelForStep = new JLabel("с шагом:");
        textFieldStep = new JTextField("0.1", 10);
        textFieldStep.setMaximumSize(textFieldStep.getPreferredSize());

        // Коробка
        Box hboxRange = Box.createHorizontalBox();
        hboxRange.setBorder(BorderFactory.createBevelBorder(1));
        hboxRange.add(Box.createHorizontalGlue());
        hboxRange.add(labelForFrom);
        hboxRange.add(Box.createHorizontalStrut(10));
        hboxRange.add(textFieldFrom);
        hboxRange.add(Box.createHorizontalStrut(20));
        hboxRange.add(labelForTo);
        hboxRange.add(Box.createHorizontalStrut(10));
        hboxRange.add(textFieldTo);
        hboxRange.add(Box.createHorizontalStrut(20));
        hboxRange.add(labelForStep);
        hboxRange.add(Box.createHorizontalStrut(10));
        hboxRange.add(textFieldStep);
        hboxRange.add(Box.createHorizontalGlue());
        // Установить предпочтительный размер области равным удвоенному
        // минимальному, чтобы при компоновке область совсем не сдавили
        hboxRange.setPreferredSize(new Dimension(new Double(hboxRange.getMaximumSize().getWidth()).intValue(),
                new Double(hboxRange.getMinimumSize().getHeight()).intValue() * 2));
        getContentPane().add(hboxRange, BorderLayout.NORTH);

// Создать кнопку "Вычислить"
        JButton buttonCalc = new JButton("Вычислить");
        buttonCalc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    // Считать значения начала и конца отрезка, шага
                    Double from = Double.parseDouble(textFieldFrom.getText());
                    Double to = Double.parseDouble(textFieldTo.getText());
                    Double step = Double.parseDouble(textFieldStep.getText());

                    // На основе считанных данных создать новый экземпляр модели таблицы
                    data = new GornerTableModel(from, to, step, MainFrame.this.coefficients);
                    JTable table = new JTable(data); // Экземпляр таблицы
                    table.setDefaultRenderer(Double.class, renderer); // Установить в качестве визуализатора ячеек
                                                                      // для класса Double разработанный визуализатор
                    table.setRowHeight(30); // Установить размер строки таблицы в 30 пикселов
                    hBoxResult.removeAll(); // Удалить все вложенные элементы из контейнера hBoxResult
                    hBoxResult.add(new JScrollPane(table)); // Добавить в hBoxResult таблицу,
                                                            // "обернутую" в панель с полосами прокрутки
                    getContentPane().validate(); // Обновить область содержания главного окна

                    // Пометить ряд элементов меню как доступных
                    saveToTextMenuItem.setEnabled(true);
                    saveToGraphicsMenuItem.setEnabled(true);
                    searchValueMenuItem.setEnabled(true);

                } catch (NumberFormatException ex) {
                    // В случае ошибки преобразования чисел показать сообщение об ошибке
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Ошибка в формате записи числа с плавающей точкой", "Ошибочный формат числа",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });


// Создать кнопку "Очистить поля"
        JButton buttonReset = new JButton("Очистить поля");
        buttonReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                textFieldFrom.setText("0.0");
                textFieldTo.setText("1.0");
                textFieldStep.setText("0.1");

                hBoxResult.removeAll();
                hBoxResult.add(new JPanel()); // Добавить в контейнер пустую панель

                // Пометить элементы меню как недоступные
                saveToTextMenuItem.setEnabled(false);
                saveToGraphicsMenuItem.setEnabled(false);
                searchValueMenuItem.setEnabled(false);

                getContentPane().validate();
            }
        });

        // Поместить созданные кнопки в контейнер
        Box hboxButtons = Box.createHorizontalBox();
        hboxButtons.setBorder(BorderFactory.createBevelBorder(1));
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.add(buttonCalc);
        hboxButtons.add(Box.createHorizontalStrut(30));
        hboxButtons.add(buttonReset);
        hboxButtons.add(Box.createHorizontalGlue());

        hboxButtons.setPreferredSize(new Dimension(new
        Double(hboxButtons.getMaximumSize().getWidth()).intValue(),
                new Double(hboxButtons.getMinimumSize().getHeight()).intValue()*2));
        getContentPane().add(hboxButtons, BorderLayout.SOUTH);

        hBoxResult = Box.createHorizontalBox();
        hBoxResult.add(new JPanel()); // Область для вывода результата пока что пустая
        getContentPane().add(hBoxResult, BorderLayout.CENTER);
    }

protected void saveToGraphicsFile(File selectedFile) {
        try {
            // Создать новый байтовый поток вывода, направленный в указанный файл
            DataOutputStream out = new DataOutputStream(new FileOutputStream(selectedFile));

            // Записать в поток вывода попарно значение X в точке, значение многочлена в точке
            for (int i = 0; i < data.getRowCount(); i++) {
                out.writeDouble((Double)data.getValueAt(i,0));
                out.writeDouble((Double)data.getValueAt(i,1));
            }

            out.close(); // Закрыть поток вывода
        }
        catch (Exception e) {
            // Исключительную ситуацию "ФайлНеНайден" в данном случае можно не обрабатывать,
            // так как мы файл создаѐм, а не открываем для чтения
        }
    }

protected void saveToTextFile(File selectedFile) {
        try {
            // Создать новый символьный поток вывода, направленный в указанный файл
            PrintStream out = new PrintStream(selectedFile);

            // Записать в поток вывода заголовочные сведения
            out.println("Результаты табулирования многочлена по схеме Горнера");
            out.print("Многочлен: ");
            for (int i = 0; i < coefficients.length; i++) {
                out.print(coefficients[i] + "*X^" + (coefficients.length - i - 1));
                if (i != coefficients.length - 1)
                    out.print(" + ");
            }
            out.println("");
            out.println("Интервал от " + data.getFrom() + " до " + data.getTo() + " с шагом " + data.getStep());
            out.println("====================================================");

            // Записать в поток вывода значения в точках
            for (int i = 0; i < data.getRowCount(); i++) {
                out.println("Значение в точке " + data.getValueAt(i,0) + " равно " + data.getValueAt(i,1));
            }

            out.close();
        }
        catch (FileNotFoundException e) {
        }
    }
}