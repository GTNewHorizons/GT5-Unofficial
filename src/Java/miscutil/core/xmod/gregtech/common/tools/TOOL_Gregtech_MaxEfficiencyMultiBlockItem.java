package miscutil.core.xmod.gregtech.common.tools;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;

public class TOOL_Gregtech_MaxEfficiencyMultiBlockItem extends TOOL_Gregtech_BaseMultiblockItem {
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
        return 3.0F;
    }

    @Override
    public IIconContainer getTurbineIcon() {
        return Textures.ItemIcons.TURBINE_HUGE;
    }

}
