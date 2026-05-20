public class SagittariusWorld extends GameWorld {

    public SagittariusWorld() {
        super("Stellaros (Celestial Hunting Grounds)",
                "=== ENTERING STELLAROS - CELESTIAL HUNTING GROUNDS ===\n" +
                        "Starry wilderness extends infinitely around you. Constellations\n" +
                        "flow like rivers through the night sky. Celestial beasts roam\n" +
                        "among crystalline trees. You feel watched from above...");
    }

    @Override
    public String[] getMinionNames() {
        return new String[] { "Star Wolf", "Nebula Hawk", "Astral Hunter", "Cosmic Beast" };
    }

    @Override
    public String getMinionDialogue(int index) {
        String[] dialogues = {
                "A Star Wolf prowls nearby!\nWolf: 'You're prey in these hunting grounds!'",
                "A Nebula Hawk descends from above!\nHawk: 'Orionis never misses his mark!'",
                "An Astral Hunter tracks you!\nHunter: 'The stars have marked you for death!'",
                "A Cosmic Beast roars!\nBeast: 'Your constellation ends here!'"
        };
        return dialogues[index];
    }

    @Override
    public String getBossDialogue(String playerName) {
        return "=== BOSS ENCOUNTER: ORIONIS ===\n" +
                "Orionis stands atop a celestial peak, bow drawn...\n\n" +
                "Orionis: 'My arrows never miss, " + playerName + ".\n" +
                "You've been in my sights since you entered Stellaros.'\n\n" +
                playerName + ": 'Why serve Zeus? We were meant to be free!'\n\n" +
                "Orionis: 'Free? Zeus offered me the ultimate hunt - immortal prey,\n" +
                "eternal challenge. And you... you're the greatest prey of all.\n" +
                "Let's see if you can dodge starlight!'";
    }
}