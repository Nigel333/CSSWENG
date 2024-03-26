import java.util.ArrayList;

public class ShoppingCart {
    int cartNum;
    ArrayList<Part> parts = new ArrayList<>();
    double finalPrice;

    public ShoppingCart(int cartNum){
        this.cartNum = cartNum;
    }
}
