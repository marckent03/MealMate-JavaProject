import javax.swing.*;
import java.awt.*;

public class StatsPanel extends JPanel {
    private ZodiacGUI gui;
    private GameState gameState;
    private JTextArea statsArea;

    public StatsPanel(ZodiacGUI gui, GameState gameState) {
        this.gui = gui;
        this.gameState = gameState;
        setLayout(new BorderLayout());
        statsArea = new JTextArea();
        statsArea.setEditable(false);
        statsArea.setBackground(Color.DARK_GRAY);
        statsArea.setForeground(Color.WHITE);
        statsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(statsArea), BorderLayout.CENTER);
        JButton back = new JButton("Back to Main Menu");
        back.addActionListener(e -> gui.showScreen("MainMenu"));
        add(back, BorderLayout.SOUTH);
    }

    public void refreshStats() {
        Player p = gameState.getPlayer();
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(p.getName().toUpperCase()).append(" STATS ===\n");
        sb.append("Zodiac: ").append(p.getZodiacType()).append("\n");
        sb.append("Boss Victories: ").append(p.getBossesDefeated()).append("\n");
        sb.append("HP: ").append(p.getCurrentHP()).append("/").append(p.getMaxHP()).append("\n");
        sb.append("MP: ").append(p.getCurrentMP()).append("/").append(p.getMaxMP()).append("\n");
        sb.append("Damage Multiplier: ").append(String.format("%.2f", p.getDamageMultiplier())).append("x\n");
        sb.append("Eclipsium: ").append(p.getEclipsium()).append("\n\n");
        sb.append("Skills:\n");
        for (int i=1;i<=3;i++) {
            sb.append(i).append(". ").append(p.getSkillName(i)).append(" (DMG: ").append(p.getSkillDamage(i))
              .append(", MP: ").append(p.getSkillMpCost(i)).append(")\n");
            sb.append("   - ").append(p.getSkillDescription(i)).append("\n");
        }
        sb.append("\nInventory:\n");
        if (p.getInventory().isEmpty()) sb.append("  Empty");
        else {
            for (Item item : p.getInventory()) {
                sb.append("  ").append(item.getName()).append(" x").append(item.getQuantity()).append("\n");
            }
        }
        statsArea.setText(sb.toString());
    }
}