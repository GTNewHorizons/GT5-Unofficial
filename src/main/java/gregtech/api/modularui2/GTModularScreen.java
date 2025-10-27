package gregtech.api.modularui2;

import static gregtech.api.enums.Mods.GregTech;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * This class is responsible for client-only functionalities of the GUI. Override {@link IGuiHolder#createScreen} to
 * use.
 */
@SideOnly(Side.CLIENT)
public class GTModularScreen extends ModularScreen {

    public GTModularScreen(@NotNull ModularPanel mainPanel, GTGuiTheme theme) {
        super(GregTech.ID, mainPanel);
        useTheme(theme.getId());
    }
}
