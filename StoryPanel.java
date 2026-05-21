import javax.swing.*;
import java.awt.*;

public class StoryPanel {
    public static void showStory(JFrame parent, String text) {
        JDialog dialog = new JDialog(parent, "Narrator", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(parent);
        JTextArea area = new JTextArea(text);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(Color.BLACK);
        area.setForeground(Color.WHITE);
        area.setFont(new Font("Serif", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(area);
        JButton ok = new JButton("Continue");
        ok.addActionListener(e -> dialog.dispose());
        dialog.add(scroll, BorderLayout.CENTER);
        dialog.add(ok, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}