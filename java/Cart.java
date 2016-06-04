import java.util.Set;
import java.util.Map;
import java.util.HashMap;

public class Cart {
    private HashMap<String, Integer> cartItems;
    public Cart() {
        cartItems = new HashMap<String, Integer>();
    }
    public Set<Map.Entry<String, Integer>> getCart() {
        return cartItems.entrySet();
    }
    public int getQuantity(String id) {
        return cartItems.get(id);
    }
    public void addToCart(String id) {
        if (cartItems.containsKey(id)) {
            cartItems.put(id, cartItems.get(id)+1);
        }
        else {
            cartItems.put(id, 1);
        }
    }
    public void changeQuantity(String id, Integer q) {
        cartItems.put(id, q);
    }
    public void deleteItem(String id) {
        cartItems.remove(id);
    }
    public void clearCart() {
        cartItems.clear();
    }
}
