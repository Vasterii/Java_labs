package mainpackage;

public class ChewingGum extends Food{
    private String flavour;

    public ChewingGum(String flavour) {
        super("Жевачка");
        this.flavour = flavour;
    }

    public String getFlavour() {
        return flavour;
    }
    public void setFlavour(String drink) {
        this.flavour = flavour;
    }


    @Override
    public void consume() {
        System.out.println(this + " съедена");
    }

    @Override
    public String toString() {
        return super.toString() + " со вкусом '" + flavour.toUpperCase() + "'";
    }

    @Override
    public boolean equals(Object arg0) {
        if (!super.equals(arg0))
            return false;
        if (!(arg0 instanceof ChewingGum))
            return false;
        return (flavour.equals(((ChewingGum) arg0).flavour));
    }

    @Override
    public int calculateCalories() { return 360;}
}
