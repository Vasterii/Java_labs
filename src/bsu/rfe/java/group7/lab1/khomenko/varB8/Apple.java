package bsu.rfe.java.group7.lab1.khomenko.varB8;

public class Apple extends Food
{
    private String size;

    public Apple(String size) {
        super("Яблоко");
        this.size = size;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public void consume() {
        System.out.println(this + " съедено");
    }

    @Override
    public String toString() {
        return super.toString() + " размера '" + size.toUpperCase() + "'";
    }

    @Override
    public boolean equals(Object arg0) {
        if (!super.equals(arg0))
            return false;
        if (!(arg0 instanceof Apple))
            return false;
        return size.equals(((Apple)arg0).size);
    }

    @Override
    public int calculateCalories() {
        return 52;
    }
}

