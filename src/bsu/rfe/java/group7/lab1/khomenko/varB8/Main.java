package bsu.rfe.java.group7.lab1.khomenko.varB8;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;

public class Main {

    public static void main(String[] args) {

        System.out.println("_______________________________________________");
        System.out.println("Лабораторная работа 1:");
        System.out.println(" ");

        Food[] breakfast = new Food[20];
        int itemsSoFar = 0;
        boolean sort_needed = false, calories_needed = false;

        int cheeseN = 0, cocktailN = 0, appleN = 0, gumN = 0;

        for (int i = 0; i < args.length; i++){
            if (args[i].equals("-calories"))
                calories_needed = true;
            if (args[i].equals("-sort"))
                sort_needed = true;
            else{
                String[] parts = args[i].split("/");
                if (parts[0].equals("Сыр")) {
                    breakfast[itemsSoFar] = new Cheese();
                    cheeseN++;
                    itemsSoFar++;
                }
                if (parts[0].equals("Яблоко")) {
                    breakfast[itemsSoFar] = new Apple(parts[1]);
                    appleN++;
                    itemsSoFar++;
                }
                if (parts[0].equals("Жевачка")) {
                    breakfast[itemsSoFar] = new ChewingGum(parts[1]);
                    gumN++;
                    itemsSoFar++;
                }
                if (parts[0].equals("Коктейль")) {
                    breakfast[itemsSoFar] = new Cocktail(parts[1],parts[2]);
                    cocktailN++;
                    itemsSoFar++;
                }

            }
        }

        System.out.println(" ");
        for (int i = 0; i < breakfast.length; i++) {
            if (breakfast[i] != null)
                breakfast[i].consume();
            else
                break;
        }

        if (calories_needed) {
            int calories = 0;
            for (int i = 0; i < itemsSoFar; i++)
                calories += breakfast[i].calculateCalories();
            System.out.println("\nОбщая калорийность завтрака: " + calories + "ккал");
        }

        if (sort_needed) {
            Arrays.sort(breakfast, new Comparator() {
                public int compare(Object f1, Object f2) {
                    if (f1==null)
                        return 1;
                    if (f2==null)
                        return -1;
                    if (((Food)f1).calculateCalories() == ((Food)f2).calculateCalories())
                        return 0;
                    if (((Food)f1).calculateCalories() > ((Food)f2).calculateCalories())
                        return -1;
                    return 1;
                }
            });
        }

        System.out.println("\nКол-во яблок:" + " " + appleN);
        System.out.println("Кол-во сыров:" + " " + cheeseN);
        System.out.println("Кол-во коктейлей:" + " " + cocktailN);
        System.out.println("Кол-во жевачек:" + " " + gumN);

        System.out.println("\nОтсортированные продукты: ");
        for (int i = 0; i < breakfast.length; i++) {
            if (breakfast[i] == null)
                continue;
            System.out.println(breakfast[i].toString() + " " + breakfast[i].calculateCalories() + "ккал");
        }

            System.out.println("\nСъедено продуктов: " + itemsSoFar);
            System.out.println("\nВсего хорошего!");
            System.out.println("_______________________________________________");

    }
}
