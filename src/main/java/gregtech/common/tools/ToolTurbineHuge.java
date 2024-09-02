package gregtech.common.tools;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;

public class ToolTurbineHuge extends ToolTurbine {

    @Override
    public float getSpeedMultiplier() {
        return 4.0F;
    }

    @Override
    public float getMaxDurabilityMultiplier() {
        return 4.0F;
    }

    @Override
    public float getBaseDamage() {
        return 7.5F;
    }

    @Override
    public IIconContainer getTurbineIcon() {
        return Textures.ItemIcons.TURBINE_HUGE;
    }
}
