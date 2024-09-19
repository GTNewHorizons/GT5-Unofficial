package ggfab;

import com.gtnewhorizon.gtnhlib.config.SimpleGuiFactory;
import net.minecraft.client.gui.GuiScreen;

public class GGFabGUIFactory implements SimpleGuiFactory {

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return GGFabGUIClientConfig.class;
    }
}
