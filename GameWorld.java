public abstract class GameWorld {
    protected String name;
    protected String description;

    public GameWorld(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public abstract String[] getMinionNames();

    public abstract String getMinionDialogue(int index);

    public abstract String getBossDialogue(String playerName);
}