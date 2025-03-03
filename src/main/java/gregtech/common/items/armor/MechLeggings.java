package gregtech.common.items.armor;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraft.client.renderer.texture.IIconRegister;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MechLeggings extends MechArmorBase {

    public MechLeggings() {
        super(SLOT_LEGS);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister aIconRegister) {
        super.registerIcons(aIconRegister);
        itemIcon = aIconRegister.registerIcon(GregTech.ID + ":gt.itemLeggingsMech");
        coreIcon = aIconRegister.registerIcon(GregTech.ID + ":gt.mechanicalCoreLeggings");
    }
}
