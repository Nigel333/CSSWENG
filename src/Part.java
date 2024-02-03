public class Part{
    public String brand;
    public String name;
    public int year;
    public int quantity;
    public double price;
    public boolean isNew;
    public boolean isOriginal;


    Part(String brand, String name, int year, int quantity, double price, boolean isNew, boolean isOriginal)
    {
        this.brand = brand;
        this.name = name;
        this.year = year;
        this.quantity = quantity;
        this.price = price;
        this.isNew = isNew;
        this.isOriginal = isOriginal;
    }

    @Override
    public boolean equals(Object o)
    {
        Part part = (Part) o;
        return this.brand.equals(part.brand) && this.name.equals(part.name) && this.isNew == part.isNew && this.isOriginal == part.isOriginal;
    }
}
