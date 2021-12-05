package bsu.rfe.java.group7.lab3.khomenko.varB4;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;


public class GornerTableCellRenderer implements TableCellRenderer {

    private JPanel panel = new JPanel();
    private JLabel label = new JLabel();

    // Ищем ячейки, строковое представление которых совпадает с needle (иголкой).
    // Применяется аналогия поиска иголки в стоге сена, в роли стога сена - таблица
    private String needle = null;
    private DecimalFormat formatter = (DecimalFormat)NumberFormat.getInstance();

    public GornerTableCellRenderer() {

        formatter.setMaximumFractionDigits(5); // Показывать только 5 знаков после запятой
        formatter.setGroupingUsed(false); // Не использовать группировку (не отделять тысячи ни запятыми, ни пробелами)

        // Установить в качестве разделителя дробной части точку, а не запятую
        DecimalFormatSymbols dottedDouble = formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);

        panel.add(label); // Разместить надпись внутри панели
        panel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Установить выравнивание надписи по левому краю панели

    }

    private int getFractionDigits(String number) {

        String[] str = number.split("\\."); // Делим число на дробную и целую части
        if (str.length == 2)
            return str[1].length(); // Количество цифр после запятой (Если [0], то до)
        else
            return 0;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int col) {

        String formattedType = formatter.format(value); // Преобразовать double в строку
        label.setText(formattedType); // Установить текст надписи равным строковому представлению числа

        if (needle != null && needle.equals(formattedType)) {
            // Номер столбца = 1 (т.е. второй столбец) + иголка не null (значит что-то ищем) +
            // значение иголки совпадает со значением ячейки таблицы - окрасить задний фон панели в красный цвет
            panel.setBackground(Color.RED);
        }
        else if(col == 1 && getFractionDigits(formattedType) <= 3) {
            panel.setBackground(Color.GREEN);
        }
        else {
            // Иначе - в обычный белый
            panel.setBackground(Color.WHITE);
        }
        return panel;
    }

    public void setNeedle(String needle) {
        this.needle = needle;
    }
}