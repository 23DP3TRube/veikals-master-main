package tr.rvt;

public class CartEntry {
    public Item item;
    public int quantity;

    public CartEntry(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }
}
