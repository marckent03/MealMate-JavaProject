public class CancerWorld extends GameWorld {

    public CancerWorld() {
        super("Thalassic Depths (Moonlit Realm)",
                "=== ENTERING THALASSIC DEPTHS - THE MOONLIT REALM ===\n" +
                        "You descend into vast oceans lit by eternal moonlight.\n" +
                        "Glowing coral palaces shimmer in the deep. Tides shift with\n" +
                        "lunar rhythm. The water is both beautiful and treacherous...");
    }

    @Override
    public String[] getMinionNames() {
        return new String[] { "Tidal Sprite", "Lunar Guardian", "Coral Sentinel", "Ocean Leviathan" };
    }

    @Override
    public String getMinionDialogue(int index) {
        String[] dialogues = {
                "A Tidal Sprite rises from the water!\nSprite: 'The depths will claim you!'",
                "A Lunar Guardian blocks your way!\nGuardian: 'Selinia's moon shall eclipse your hope!'",
                "A Coral Sentinel emerges!\nSentinel: 'Turn back before the tide drowns you!'",
                "An Ocean Leviathan surfaces!\nLeviathan: 'The abyss hungers for challengers!'"
        };
        return dialogues[index];
    }

    @Override
    public String getBossDialogue(String playerName) {
        return "=== BOSS ENCOUNTER: SELINIA ===\n" +
                "Selinia waits beneath the eternal moon...\n\n" +
                "Selinia: 'You've come far, " + playerName + ".'\n" +
                "Selinia: 'Once, I would have healed your wounds. But mortals taught me\n" +
                "that kindness is weakness. Zeus showed me true strength.'\n\n" +
                playerName + ": 'Selinia, this bitterness isn't strength!\n" +
                "We can still fight Zeus together!'\n\n" +
                "Selinia: 'Together? No. Only the strongest survive. Let the tide decide!'";
    }
}