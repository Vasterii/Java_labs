package bsu.rfe.java.group7.lab1.khomenko.varB8;

public abstract class Food implements Consumable, Nutritious
{
    private String name = null;

    public Food(String name)
    {
        this.name = name;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public boolean equals(Object arg0)
    {
        if (!(arg0 instanceof Food)) //instanceof определить, является ли аргумент arg0
            return false;            //экземпляром класса Food или какого-либо из его потомков
        if (name==null || ((Food)arg0).name==null)
            return false;
        return name.equals(((Food)arg0).name);
    }

    @Override
    public String toString()
    {
        return name;
    } //возвращает значение внутреннего поля данных

}
