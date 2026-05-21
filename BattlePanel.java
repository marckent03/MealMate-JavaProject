import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class BattlePanel extends JPanel {
    private ZodiacGUI gui;
    private GameState gameState;
    private JTextArea logArea;
    private JPanel actionPanel;
    private JLabel playerHpLabel, playerMpLabel, enemyHpLabel;
    private JButton[] skillButtons;
    private JButton potionButton, runButton;
    private boolean playerTurn;
    private String preludeText;
    private String battleSubtitle;
    private boolean battleActive;

    public BattlePanel(ZodiacGUI gui, GameState gameState) {
        this.gui = gui;
        this.gameState = gameState;
        this.battleActive = false;
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(Color.DARK_GRAY);
        logArea.setForeground(Color.WHITE);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(logArea), BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new GridLayout(1, 3));
        statusPanel.setBackground(Color.BLACK);
        playerHpLabel = new JLabel("HP: ", SwingConstants.CENTER);
        playerHpLabel.setForeground(Color.CYAN);
        playerMpLabel = new JLabel("MP: ", SwingConstants.CENTER);
        playerMpLabel.setForeground(Color.GREEN);
        enemyHpLabel = new JLabel("Enemy HP: ", SwingConstants.CENTER);
        enemyHpLabel.setForeground(Color.RED);
        statusPanel.add(playerHpLabel);
        statusPanel.add(playerMpLabel);
        statusPanel.add(enemyHpLabel);
        add(statusPanel, BorderLayout.NORTH);

        actionPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        actionPanel.setBackground(Color.BLACK);
        skillButtons = new JButton[3];
        for (int i = 0; i < 3; i++) {
            final int idx = i+1;
            skillButtons[i] = new JButton();
            skillButtons[i].addActionListener(e -> playerSkill(idx));
            actionPanel.add(skillButtons[i]);
        }
        potionButton = new JButton("Use Potion");
        potionButton.addActionListener(e -> usePotion());
        runButton = new JButton("Run");
        runButton.addActionListener(e -> runAway());
        actionPanel.add(potionButton);
        actionPanel.add(runButton);
        add(actionPanel, BorderLayout.SOUTH);
    }

    public void beginBattle(String prelude, String subtitle) {
        if (gameState.getPlayer() == null) {
            appendLog("ERROR: No player character!");
            return;
        }
        if (gameState.getCurrentEnemy() == null) {
            appendLog("ERROR: No enemy to fight!");
            return;
        }
        
        this.preludeText = prelude;
        this.battleSubtitle = subtitle;
        this.battleActive = true;
        this.playerTurn = true;
        logArea.setText("");
        appendLog(preludeText);
        appendLog("\n=== " + battleSubtitle + " ===\n");
        updateStatus();
        updateSkillButtons();
        actionPanel.setVisible(true);
        actionPanel.revalidate();
        actionPanel.repaint();
        revalidate();
        repaint();
    }

    private void appendLog(String msg) {
        logArea.append(msg + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void updateStatus() {
        Player p = gameState.getPlayer();
        Enemy e = gameState.getCurrentEnemy();
        if (p != null) {
            playerHpLabel.setText(p.getName() + " HP: " + p.getCurrentHP() + "/" + p.getMaxHP());
            playerMpLabel.setText("MP: " + p.getCurrentMP() + "/" + p.getMaxMP());
        } else {
            playerHpLabel.setText("HP: --/--");
            playerMpLabel.setText("MP: --/--");
        }
        if (e != null) {
            enemyHpLabel.setText(e.getName() + " HP: " + e.getCurrentHP() + "/" + e.getMaxHP());
        } else {
            enemyHpLabel.setText("Enemy HP: --/--");
        }
    }

    private void updateSkillButtons() {
        Player p = gameState.getPlayer();
        if (p == null) return;
        for (int i = 0; i < 3; i++) {
            int num = i+1;
            String name = p.getSkillName(num);
            int cost = p.getSkillMpCost(num);
            int cooldown = p.getSkillCooldown(num);
            String text = "<html>" + name + "<br>DMG: " + p.getSkillDamage(num) + " MP:" + cost;
            if (cooldown > 0) text += " CD:" + cooldown;
            text += "</html>";
            skillButtons[i].setText(text);
            skillButtons[i].setEnabled(cooldown == 0 && p.getCurrentMP() >= cost);
        }
        potionButton.setEnabled(!p.getInventory().isEmpty());
    }

    private void playerSkill(int skillNum) {
        if (!battleActive || !playerTurn) return;
        Player p = gameState.getPlayer();
        int result = gameState.playerUseSkill(skillNum);
        if (result == -1) {
            appendLog("Skill on cooldown!");
            return;
        } else if (result == -2) {
            appendLog("Not enough MP!");
            return;
        }
        appendLog(p.getSkillVoiceLine(skillNum));
        appendLog("You use " + p.getSkillName(skillNum) + "!");
        if (p.getZodiacType().equals("Cancer") && skillNum == 2) {
            appendLog("You heal for " + result + " HP!");
        } else {
            Enemy e = gameState.getCurrentEnemy();
            e.takeDamage(result);
            appendLog("Dealt " + result + " damage to " + e.getName() + "!");
        }
        p.reduceCooldowns();
        updateStatus();
        if (checkBattleEnd()) return;
        playerTurn = false;
        Timer timer = new Timer(800, e -> enemyTurn());
        timer.setRepeats(false);
        timer.start();
    }

    private void enemyTurn() {
        if (!battleActive) return;
        gameState.enemyTurn();
        appendLog(gameState.getCurrentEnemy().getAttackMessage());
        appendLog("You take damage!");
        gameState.getPlayer().reduceCooldowns();
        updateStatus();
        if (checkBattleEnd()) return;
        playerTurn = true;
        updateSkillButtons();
    }

    private boolean checkBattleEnd() {
        if (gameState.isBattleOver()) {
            battleActive = false;
            if (gameState.isPlayerVictory()) {
                appendLog("\n=== VICTORY! ===");
                Enemy e = gameState.getCurrentEnemy();
                if (e.getName().contains("Zeus (Human Form)")) {
                    gameState.onZeusPhase1Defeated();
                } else if (e.getName().contains("Zeus (God Form)")) {
                    gameState.onFinalVictory();
                } else if (e.getType().contains("Minion")) {
                    gameState.getPlayer().addEclipsium(30);
                    appendLog("You earned 30 Eclipsium!");
                    gameState.incrementCurrentMinionIndex();
                    showPostMinionOptions();
                } else {
                    gameState.onBossDefeated();
                }
            } else {
                appendLog("\n=== DEFEAT ===");
                gameState.onBattleLost();
            }
            return true;
        }
        return false;
    }

    private void showPostMinionOptions() {
        int total = 4 + gameState.getRestPenaltyCount();
        if (gameState.getCurrentMinionIndex() < total) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Minion defeated. Continue to next battle?\n(Choose No to rest and add +1 minion)",
                    "Path Choice", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.NO_OPTION) {
                gameState.restAndReturn();
                gui.showScreen("MainMenu");
            } else {
                gameState.startNextMinionBattle();
            }
        } else {
            gameState.proceedToBoss();
        }
    }

    private void usePotion() {
        if (!battleActive || !playerTurn) return;
        Player p = gameState.getPlayer();
        if (p.getInventory().isEmpty()) {
            appendLog("No potions!");
            return;
        }
        String[] potionNames = p.getInventory().stream().map(Item::getName).toArray(String[]::new);
        String selected = (String) JOptionPane.showInputDialog(this,
                "Choose potion", "Use Potion",
                JOptionPane.QUESTION_MESSAGE, null, potionNames, potionNames[0]);
        if (selected != null) {
            Potion pot = (Potion) p.getInventory().stream()
                    .filter(i -> i.getName().equals(selected)).findFirst().get();
            pot.use(p);
            if (pot.getQuantity() <= 0) p.getInventory().remove(pot);
            appendLog("Used " + selected);
            updateStatus();
            playerTurn = false;
            Timer timer = new Timer(500, e -> enemyTurn());
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void runAway() {
        appendLog("You cannot run from this battle!");
    }
}