import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class GameState {
    private Player player;
    private ArrayList<Enemy> enemies;
    private int worldCounter;
    private int currentMinionIndex;
    private int restPenaltyCount;
    private boolean inWorldExploration;
    private boolean zeusPhase1Completed;
    private ZodiacGUI gui;
    private BattlePanel battlePanel;
    private boolean bossReadyAfterCleared;
    private boolean pendingBossBattle;
    private String currentMerchantName;
    
    private Enemy currentEnemy;
    private boolean isInitialZeusBattle;

    public GameState(ZodiacGUI gui, BattlePanel battlePanel) {
        this.gui = gui;
        this.battlePanel = battlePanel;
        this.bossReadyAfterCleared = false;
        this.pendingBossBattle = false;
        this.currentMerchantName = "Merchant";
        enemies = new ArrayList<>();
        worldCounter = 0;
        currentMinionIndex = 0;
        restPenaltyCount = 0;
        inWorldExploration = false;
        zeusPhase1Completed = false;
        isInitialZeusBattle = false;
    }

    public void setBattlePanel(BattlePanel battlePanel) {
        this.battlePanel = battlePanel;
    }

    public void createPlayer(String name, String zodiacType) {
        player = new Player(name, zodiacType);
    }

    public void initializeEnemies() {
        enemies.clear();
        if (!player.getZodiacType().equals("Aries")) {
            enemies.add(new Enemy("Aresios", "Aries", 600, 150));
        }
        if (!player.getZodiacType().equals("Cancer")) {
            enemies.add(new Enemy("Selinia", "Cancer", 700, 200));
        }
        if (!player.getZodiacType().equals("Sagittarius")) {
            enemies.add(new Enemy("Orionis", "Sagittarius", 550, 180));
        }
        enemies.add(new Enemy("Zeus (Human Form)", "God_Phase1", 1200, 400));
    }

    public Player getPlayer() { return player; }
    public ArrayList<Enemy> getEnemies() { return enemies; }
    public int getWorldCounter() { return worldCounter; }
    public boolean isInWorldExploration() { return inWorldExploration; }
    public int getCurrentMinionIndex() { return currentMinionIndex; }
    public int getRestPenaltyCount() { return restPenaltyCount; }
    public void incrementCurrentMinionIndex() { currentMinionIndex++; }
    public void setCurrentMinionIndex(int index) { currentMinionIndex = index; }
    public Enemy getCurrentEnemy() { return currentEnemy; }
    public boolean isInitialZeusBattle() { return isInitialZeusBattle; }
    public String getCurrentMerchantName() { return currentMerchantName; }

    public GameWorld createWorld(String bossType) {
        if (bossType.equals("Aries")) return new AriesWorld();
        else if (bossType.equals("Cancer")) return new CancerWorld();
        else return new SagittariusWorld();
    }

    public void runIntroSequence() {
        gui.showScreen("Story");
        StoryPanel.showStory(gui, "=== THE AWAKENING ===\n\n" +
                "As your powers awaken, thunder splits the sky...\nZeus himself descends from Olympus!\n\n" +
                "Zeus: \"Another Zodiac-born dares to challenge my rule?\"\n\n" +
                "You fight, but Zeus is too powerful...");
        
        player.takeDamage(500);
        StoryPanel.showStory(gui, "=== DEFEAT ===\n\n" +
                "Zeus: \"You are not ready, young one. Return when you have proven yourself worthy.\"\n" +
                "Lightning strikes you down... Everything goes dark...");
        
        StoryPanel.showStory(gui, "=== RESCUE PATH BEGINS ===\n\n" +
                "You awaken in a small town, your body aching.\n" +
                "A kind healer named Kyle tends to your wounds.\n\n" +
                "Kyle: \"Zeus chained your kin. You're the only one strong enough to free them.\"\n" +
                "Kyle hands you a pouch with 50 Eclipsium.");
        
        player.addEclipsium(50);
        player.restoreFullHealth();
        player.restoreFullMana();
        initializeEnemies();
        
        String rescueStory = getRescuePathStory();
        StoryPanel.showStory(gui, rescueStory);
    }

    private String getRescuePathStory() {
        if (player.getZodiacType().equals("Aries")) {
            return "But as " + player.getName() + " steps through the Gateway,\n" +
                "a brutal truth is revealed:\n\n" +
                "Selinia is trapped within the Moonlit Bastion...\n" +
                "Orionis roams the Starlight Expanse, bound by celestial chains.\n\n" +
                player.getName() + " grips the blazing blade:\n" +
                "\"Zeus broke us... now I'll break his chains.\"";
        } else if (player.getZodiacType().equals("Cancer")) {
            return "As " + player.getName() + " crosses the Gateway,\n" +
                "the tides whisper a terrible revelation:\n\n" +
                "Aresios is imprisoned in the Infernal Crucible...\n" +
                "Orionis is locked within the Constellation Crucible...\n\n" +
                player.getName() + "'s light brightens:\n" +
                "\"I could not save them before... but I will save them now.\"";
        } else {
            return "As " + player.getName() + " steps through the Gateway,\n" +
                "the stars reveal Zeus's final cruelty:\n\n" +
                "Aresios rages within the War-Bound Citadel...\n" +
                "Selinia drifts inside the Lunar Abyss...\n\n" +
                player.getName() + " lowers the bow:\n" +
                "\"They were never the prey... Zeus was.\"";
        }
    }

    public void startWorldExploration() {
        if (enemies.isEmpty()) return;
        
        if (bossReadyAfterCleared) {
            bossReadyAfterCleared = false;
            proceedToBossAfterRest();
            return;
        }
        
        Enemy boss = enemies.get(0);
        if (!boss.getName().contains("Zeus")) {
            if (!inWorldExploration) {
                worldCounter++;
                currentMinionIndex = 0;
                inWorldExploration = true;
                restPenaltyCount = 0;
                GameWorld world = createWorld(boss.getType());
                showStoryText(world.getDescription());
                player.resetCooldowns();
                startNextMinionBattle();
            } else {
                startNextMinionBattle();
            }
        } else {
            startZeusBattle();
        }
    }

    public void startNextMinionBattle() {
        if (enemies.isEmpty()) return;
        Enemy boss = enemies.get(0);
        GameWorld world = createWorld(boss.getType());
        String[] minionNames = world.getMinionNames();
        int totalMinions = 4 + restPenaltyCount;

        if (currentMinionIndex < totalMinions) {
            int minionIndex = currentMinionIndex % 4;
            String minionName = minionNames[minionIndex];
            if (currentMinionIndex >= 4) minionName += " (Reinforcement)";
            currentEnemy = new Enemy(minionName, boss.getType() + "_Minion",
                    200 + (minionIndex * 30), 80 + (minionIndex * 10));
            isInitialZeusBattle = false;
            String dialogue = world.getMinionDialogue(minionIndex);
            showBattleStart(dialogue, "Minion " + (currentMinionIndex+1) + " of " + totalMinions);
        } else {
            currentMinionIndex = 0;
            restPenaltyCount = 0;
            inWorldExploration = false;
            
            int choice = JOptionPane.showConfirmDialog(null,
                    "All minions defeated!\nDo you want to fight the boss now?\n(No = return to town to shop/rest)",
                    "Boss Ready", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                proceedToBoss();
            } else {
                bossReadyAfterCleared = true;
                gui.showScreen("MainMenu");
            }
        }
    }

    public void proceedToBoss() {
        if (enemies.isEmpty()) return;
        player.restoreFullHealth();
        player.restoreFullMana();
        player.resetCooldowns();
        setMerchantNameForWorld(enemies.get(0).getType());
        pendingBossBattle = true;
        gui.showScreen("Shop");
    }

    private void proceedToBossAfterRest() {
        if (enemies.isEmpty()) return;
        player.restoreFullHealth();
        player.restoreFullMana();
        player.resetCooldowns();
        currentEnemy = enemies.get(0);
        GameWorld world = createWorld(currentEnemy.getType());
        String bossDialogue = world.getBossDialogue(player.getName());
        showBattleStart(bossDialogue, "BOSS BATTLE");
    }

    public void onShopClosed() {
        if (pendingBossBattle) {
            pendingBossBattle = false;
            currentEnemy = enemies.get(0);
            GameWorld world = createWorld(currentEnemy.getType());
            String bossDialogue = world.getBossDialogue(player.getName());
            showBattleStart(bossDialogue, "BOSS BATTLE");
        }
    }

    private void showBattleStart(String dialogue, String subTitle) {
        gui.showScreen("Battle");
        SwingUtilities.invokeLater(() -> {
            battlePanel.beginBattle(dialogue, subTitle);
        });
    }

    private void setMerchantNameForWorld(String bossType) {
        if (bossType.equals("Aries")) currentMerchantName = "Jhush";
        else if (bossType.equals("Cancer")) currentMerchantName = "Rex";
        else if (bossType.equals("Sagittarius")) currentMerchantName = "Clarence";
        else currentMerchantName = "Merchant";
    }

    public void startZeusBattle() {
        if (!zeusPhase1Completed) {
            showStoryText("=== ENTERING THE REALM OF ZEUS ===");
            showStoryText("When the final demigod is freed... [Zeus intro story]");
            setMerchantNameForWorld("Zeus");
            pendingBossBattle = true;
            gui.showScreen("Shop");
        } else {
            currentEnemy = new Enemy("Zeus (God Form)", "God_Phase2", 1800, 600);
            isInitialZeusBattle = false;
            showBattleStart("Zeus rises in god form...", "FINAL BATTLE - PHASE 2");
        }
    }

    public void onBossDefeated() {
        player.addEclipsium(100);
        player.boostStats();
        Enemy defeated = enemies.remove(0);
        inWorldExploration = false;
        currentMinionIndex = 0;
        restPenaltyCount = 0;
        bossReadyAfterCleared = false;
        showStoryText("You defeated " + defeated.getName() + "! Chains shatter!");
        player.restoreFullHealth();
        player.restoreFullMana();
        player.resetCooldowns();
        gui.showScreen("MainMenu");
    }

    public void onZeusPhase1Defeated() {
        zeusPhase1Completed = true;
        enemies.remove(0);
        currentEnemy = new Enemy("Zeus (God Form)", "God_Phase2", 1800, 600);
        showBattleStart("Zeus: 'You wanted a god... Now face one.'", "PHASE 2");
    }

    public void onFinalVictory() {
        showVictoryEnding();
        enemies.clear();
        gui.showScreen("MainMenu");
    }

    private void showVictoryEnding() {
        String ally1, ally2;
        if (player.getZodiacType().equals("Aries")) { ally1 = "Selinia"; ally2 = "Orionis"; }
        else if (player.getZodiacType().equals("Cancer")) { ally1 = "Aresios"; ally2 = "Orionis"; }
        else { ally1 = "Aresios"; ally2 = "Selinia"; }
        String ending = "\n=== FINAL BATTLE ENDING ===\n" +
                "When Zeus falls, the storm collapses.\n" +
                ally1 + ", " + ally2 + ", and " + player.getName() + " stand together.\n" +
                "The divine chains are broken!\n" +
                "All three demigods rise again—united at last!\n" +
                "!!! ULTIMATE VICTORY !!!";
        showStoryText(ending);
    }

    public void onBattleLost() {
        player.restoreFullHealth();
        player.restoreFullMana();
        if (currentEnemy != null) {
            currentEnemy.resetToMaxHealth();
        }
        if (currentEnemy != null && !currentEnemy.getType().contains("Minion")) {
            inWorldExploration = false;
            currentMinionIndex = 0;
            restPenaltyCount = 0;
            bossReadyAfterCleared = false;
        }
        if (currentEnemy != null && !isInitialZeusBattle) {
            showStoryText("You were defeated by " + currentEnemy.getName() + "... Restored at town.");
        } else {
            showStoryText("You were defeated... Restored at town.");
        }
        gui.showScreen("MainMenu");
    }

    public void restAndReturn() {
        player.rest();
        restPenaltyCount++;
        showStoryText("You rest. +1 reinforcement added to this world.");
        gui.showScreen("MainMenu");
    }

    public int playerUseSkill(int skillNum) {
        return player.useSkill(skillNum);
    }

    public void enemyTurn() {
        if (currentEnemy == null || !currentEnemy.isAlive()) return;
        int damage = currentEnemy.attack();
        player.takeDamage(damage);
    }

    public boolean isBattleOver() {
        return !player.isAlive() || (currentEnemy != null && !currentEnemy.isAlive());
    }

    public boolean isPlayerVictory() {
        return player.isAlive() && currentEnemy != null && !currentEnemy.isAlive();
    }

    public void showStoryText(String text) {
        StoryPanel.showStory(gui, text);
    }

    public boolean buyHealthPotion() {
        if (player.spendEclipsium(50)) {
            player.addPotion(new Potion("Health Potion", "Restores 200 HP", 1, 200, 0));
            return true;
        }
        return false;
    }

    public boolean buyManaPotion() {
        if (player.spendEclipsium(40)) {
            player.addPotion(new Potion("Mana Potion", "Restores 100 MP", 1, 0, 100));
            return true;
        }
        return false;
    }

    public boolean buyFullRestore() {
        if (player.spendEclipsium(100)) {
            player.addPotion(new Potion("Full Restore", "Restores all HP and MP", 1, 9999, 9999));
            return true;
        }
        return false;
    }
}