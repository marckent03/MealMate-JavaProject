public abstract class Item {
    protected String name;
    protected String description;
    protected int quantity;
    
    public Item(String name, String description, int quantity) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
    }
    
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getQuantity() { return quantity; }
    
    public abstract void use(Player player);
}