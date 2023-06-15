package kubatech;

import javax.swing.*;

public class standalone {

    public static void main(String[] args) {
        JOptionPane.showMessageDialog(
            null,
            "Get some TEA",
            "TEA",
            JOptionPane.ERROR_MESSAGE,
            new ImageIcon(standalone.class.getResource("/assets/kubatech/textures/gui/green_tea.png")));
    }
}
