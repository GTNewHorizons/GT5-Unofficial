package gregtech.common.tools;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.MaterialIconRegistry;
import gregtech.api.enums.Textures;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.material.MU;

public class ToolFileMV extends ToolFileLV {

    @Override
    public int getToolDamagePerContainerCraft() {
        return 3200;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mRGBa
            : MU.rgba(Materials2Materials.Aluminium);
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead
            ? MetaGeneratedTool
                .getPrimaryMaterial(aStack).mIconSet.mTextures[MaterialIconRegistry.IconType.TOOL_HEAD_ANGLE_GRINDER
                    .ordinal()]
            : Textures.ItemIcons.POWER_UNIT_MV;
    }

}
