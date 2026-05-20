import javax.swing.*;
import java.awt.*;

public class ShopPanel extends JPanel {
    private ZodiacGUI gui;
    private GameState gameState;
    private JLabel eclipsiumLabel;
    private JTextArea logArea;
    private JButton healthBtn, manaBtn, fullBtn, leaveBtn;

    public ShopPanel(ZodiacGUI gui, GameState gameState) {
        this.gui = gui;
        this.gameState = gameState;
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JPanel top = new JPanel(new FlowLayout());
        top.setBackground(Color.BLACK);
        eclipsiumLabel = new JLabel("Eclipsium: 0");
        eclipsiumLabel.setForeground(Color.YELLOW);
        top.add(eclipsiumLabel);
        add(top, BorderLayout.NORTH);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(Color.DARK_GRAY);
        logArea.setForeground(Color.WHITE);
        add(new JScrollPane(logArea), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(2, 2, 10, 10));
        buttons.setBackground(Color.BLACK);
        healthBtn = new JButton("Health Potion (50)");
        manaBtn = new JButton("Mana Potion (40)");
        fullBtn = new JButton("Full Restore (100)");
        leaveBtn = new JButton("Leave");
        healthBtn.addActionListener(e -> buy("health"));
        manaBtn.addActionListener(e -> buy("mana"));
        fullBtn.addActionListener(e -> buy("full"));
        leaveBtn.addActionListener(e -> {
            gameState.onShopClosed();
            gui.showScreen("MainMenu");
        });
        buttons.add(healthBtn);
        buttons.add(manaBtn);
        buttons.add(fullBtn);
        buttons.add(leaveBtn);
        add(buttons, BorderLayout.SOUTH);
    }

    public void refreshShop() {
        String merchant = gameState.getCurrentMerchantName();
        logArea.setText(merchant + ": Welcome, traveler.\nYour Eclipsium: " + gameState.getPlayer().getEclipsium() + "\n");
        eclipsiumLabel.setText("Eclipsium: " + gameState.getPlayer().getEclipsium());
        updateButtons();
    }

    private void updateButtons() {
        int e = gameState.getPlayer().getEclipsium();
        healthBtn.setEnabled(e >= 50);
        manaBtn.setEnabled(e >= 40);
        fullBtn.setEnabled(e >= 100);
    }

    private void buy(String type) {
        boolean success = false;
        if (type.equals("health")) success = gameState.buyHealthPotion();
        else if (type.equals("mana")) success = gameState.buyManaPotion();
        else success = gameState.buyFullRestore();

        if (success) {
            logArea.append("Purchased!\n");
            eclipsiumLabel.setText("Eclipsium: " + gameState.getPlayer().getEclipsium());
            updateButtons();
        } else {
            logArea.append("Not enough Eclipsium!\n");
        }
    }
}