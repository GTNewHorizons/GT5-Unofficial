package gregtech.common.tools;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;

public class ToolTurbineLarge extends ToolTurbine {

    @Override
    public float getSpeedMultiplier() {
        return 3.0F;
    }

    @Override
    public float getMaxDurabilityMultiplier() {
        return 3.0F;
    }

    @Override
    public float getBaseDamage() {
        return 5.0F;
    }

    @Override
    public IIconContainer getTurbineIcon() {
        return Textures.ItemIcons.TURBINE_LARGE;
    }
}
