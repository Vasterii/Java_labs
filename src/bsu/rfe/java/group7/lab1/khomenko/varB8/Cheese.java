package bsu.rfe.java.group7.lab1.khomenko.varB8;

public class Cheese extends Food
{

    public Cheese()
    {
        super("Сыр");
    }

    @Override
    public void consume()
    {
        System.out.println(this + " съеден");
    }

    @Override
    public int calculateCalories()
    {
        return 402;
    }
}
