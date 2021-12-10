package bsu.rfe.java.group7.lab1.khomenko.varB8;

public class Cocktail extends Food {
    private String fruit;
    private String drink;

    public Cocktail(String drink, String fruit) {
        super("Коктейль");
        this.fruit = fruit;
        this.drink = drink;
    }

    public String getDrink() {
        return drink;
    }

    public String getFruit() {
        return fruit;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }

    public void setFruit(String fruit) {
        this.fruit = fruit;
    }

    @Override
    public void consume() {
        System.out.println(this + " выпит");
    }

    @Override
    public String toString() {
        return super.toString() + " из '" + drink.toUpperCase() + " и '" + fruit.toUpperCase() + "'";
    }


    @Override
    public boolean equals(Object arg0) {
        if (!super.equals(arg0))
            return false;
        if (!(arg0 instanceof Cocktail))
            return false;
        return (drink.equals(((Cocktail) arg0).drink) && (fruit.equals(((Cocktail) arg0).fruit)));
    }

    @Override
    public int calculateCalories() {
        int cal = 0;
        if (drink.equals("вода")) {      //подсчёт калорийности напитка-основы коктейля
            cal += 1;
        }
        else if (drink.equals("лимонад")) {
            cal += 40;
        }
        else if (drink.equals("сок")) {
            cal += 54;
        }
        else if (drink.equals("смути")) {
            cal += 37;
        }
        else if (drink.equals("молоко")) {
            cal += 42;
        }
        else if (drink.equals("кофе")) {
            cal += 1;
        }
        else if (drink.equals("чай")) {
            cal += 1;
        }


        if (fruit.equals("клубника")) { //подсчёт калорийности напитка-основы коктейля
            cal += 33;
        }
        else if (fruit.equals("арбуз")) {
            cal += 30;
        }
        else if (fruit.equals("лимон")) {
            cal += 29;
        }
        else if (fruit.equals("сахар")) {
            cal += 387;
        }
        else if (fruit.equals("сливки")) {
            cal += 196;
        }
        else if (fruit.equals("киви")) {
            cal += 61;
        }
        else if (fruit.equals("шоколад")) {
            cal += 546;
        }

        return cal;
    }
}
