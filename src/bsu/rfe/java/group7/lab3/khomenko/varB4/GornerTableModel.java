package bsu.rfe.java.group7.lab3.khomenko.varB4;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class GornerTableModel extends AbstractTableModel {

    private Double[] coefficients;
    private Double from, to, step;

    public GornerTableModel(Double from, Double to, Double step, Double[] coefficients) {
        this.from = from;
        this.to = to;
        this.step = step;
        this.coefficients = coefficients;
    }

    public Double getFrom() {
        return from;
    }

    public Double getTo() {
        return to;
    }

    public Double getStep() {
        return step;
    }

    public int getColumnCount() {
        return 3; // В данной модели три столбца
    }

    // Вычислить количество точек между началом и концом отрезка исходя из шага табулирования
    public int getRowCount() {
        return new Double(Math.ceil((to-from)/step)).intValue() + 1;
    }

    private int gcd(int a, int b) {
        int c;
        while (b > 0) {
            c = a % b;
            a = b;
            b = c;
        }
        return a;
    }


    // Вычислить значение X как НАЧАЛО_ОТРЕЗКА + ШАГ*НОМЕР_СТРОКИ
    public Object getValueAt(int row, int col) {
        double X = from + step * row;
        double result = coefficients[0];
        for (int i = 1; i < coefficients.length; ++i)
            result = result * X + coefficients[i];

        switch (col) {
            case 0:
                return X; // Если 1 столбец, то это X
            case 1:
                return result; // Если 2 столбец, то это значение многочлена
            case 2:
                return gcd((int)X, (int)result) == 1;
            default:
                return 0.0;
        }
    }

    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return "Значение X"; // Название 1-го столбца
            case 1:
                return "Значение многочлена"; // Название 2-го столбца
            case 2:
                return "Взаимно простые?"; // Названиек 3-го столбца
            default:
                return "";
        }
    }

    public Class<?> getColumnClass(int col) {
        if (col == 2)
            return Boolean.class; // В 3-м столбце находятся значения типа Boolean
        else
            return Double.class; // И в 1-ом и во 2-ом столбце находятся значения типа Double
    }
}