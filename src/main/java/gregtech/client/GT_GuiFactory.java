package gregtech.client;

import net.minecraft.client.gui.GuiScreen;

import com.gtnewhorizon.gtnhlib.config.SimpleGuiFactory;

public class GT_GuiFactory implements SimpleGuiFactory {

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return GT_GUI_ClientConfig.class;
    }
}
