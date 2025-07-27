package gregtech.crossmod.navigator;

import net.minecraft.util.ResourceLocation;

import com.gtnewhorizons.navigator.api.model.SupportedMods;
import com.gtnewhorizons.navigator.api.model.buttons.ButtonManager;

import gregtech.api.enums.Mods;
import gregtech.api.util.GTUtility;

public class PowerfailButtonManager extends ButtonManager {

    @Override
    public ResourceLocation getIcon(SupportedMods mod, String theme) {
        return new ResourceLocation(Mods.ModIDs.GREG_TECH, "textures/gui/navigator/powerfail.png");
    }

    @Override
    public String getButtonText() {
        return GTUtility.translate("GT5U.gui.text.show-powerfail-layer");
    }
}
