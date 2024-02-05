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
        return this.carModel.equals(part.carModel) && this.name.equals(part.name) && this.isNew == part.isNew && this.authenticity.equals(part.authenticity);
    }

    @Override
    public String toString() {
        return carBrand + '\t' + carModel + '\t' + String.valueOf(year) + '\t' + String.valueOf(quantity) + '\t' + String.valueOf(price) + '\t' + String.valueOf(price) + '\t' + authenticity;
    }
}
