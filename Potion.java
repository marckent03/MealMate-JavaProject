public class Potion extends Item {
    private int hpRestore;
    private int mpRestore;
    
    public Potion(String name, String description, int quantity, int hpRestore, int mpRestore) {
        super(name, description, quantity);
        this.hpRestore = hpRestore;
        this.mpRestore = mpRestore;
    }
    
    public void addQuantity(int amount) {
        this.quantity += amount;
    }
    
    @Override
    public void use(Player player) {
        if (quantity > 0) {
            player.heal(hpRestore);
            player.restoreMana(mpRestore);
            quantity--;
            System.out.println("Used " + name + "!");
            if (hpRestore > 0) System.out.println("Restored " + hpRestore + " HP!");
            if (mpRestore > 0) System.out.println("Restored " + mpRestore + " MP!");
        }
    }
}