public class Part{
    public String carBrand;
    public String carModel;
    public String name;
    public int year;
    public int quantity;
    public double price;
    public boolean isNew;
    public String authenticity;


    Part(String carBrand, String carModel, String name, int year, int quantity, double price, boolean isNew, String authenticity)
    {
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.name = name;
        this.year = year;
        this.quantity = quantity;
        this.price = price;
        this.isNew = isNew;
        this.authenticity = authenticity;
    }

    @Override
    public boolean equals(Object o)
    {
        Part part = (Part) o;
        return this.carBrand.equals(part.carBrand) && this.carModel.equals(part.carModel) && this.name.equals(part.name) && this.year == part.year && this.isNew == part.isNew && this.authenticity.equals(part.authenticity);
    }

    @Override
    public String toString() {
        return carBrand + '\t' + carModel + '\t' + name + '\t' + String.valueOf(year) + '\t' + authenticity;
    }
}
