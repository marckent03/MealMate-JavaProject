public class AriesWorld extends GameWorld {

    public AriesWorld() {
        super("Pyroxis (War-torn Fields)",
                "=== ENTERING PYROXIS - THE WAR-TORN FIELDS ===\n" +
                        "Endless battlefields stretch before you. The sky burns red,\n" +
                        "scorched earth cracks beneath your feet. The air smells of smoke\n" +
                        "and steel. War cries echo in the distance...");
    }

    @Override
    public String[] getMinionNames() {
        return new String[] { "Scorched Warrior", "Flame Berserker", "Inferno Guardian", "War Titan" };
    }

    @Override
    public String getMinionDialogue(int index) {
        String[] dialogues = {
                "A Scorched Warrior blocks your path!\nWarrior: 'None shall pass through Pyroxis!'",
                "A Flame Berserker emerges from the flames!\nBerserker: 'Your journey ends in fire!'",
                "An Inferno Guardian stands before you!\nGuardian: 'Aresios will crush you, weakling!'",
                "A massive War Titan appears!\nTitan: 'Face the might of eternal war!'"
        };
        return dialogues[index];
    }

    @Override
    public String getBossDialogue(String playerName) {
        return "=== BOSS ENCOUNTER: ARESIOS ===\n" +
                "Aresios stands in the center of burning battlefield...\n\n" +
                "Aresios: 'So you've survived my warriors. Impressive.'\n" +
                "Aresios: 'But I am war incarnate! Zeus promised me eternal battle,\n" +
                "and I will not let you take that from me!'\n\n" +
                playerName + ": 'You've lost yourself to violence, Aresios!\n" +
                "This isn't who you were meant to be!'\n\n" +
                "Aresios: 'ENOUGH TALK! Show me your strength in combat!'";
    }
}