package gregtech.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.util.GT_LanguageManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class GT_Block_Casings8
        extends GT_Block_Casings_Abstract {
    public GT_Block_Casings8() {
        super(GT_Item_Casings8.class, "gt.blockcasings8", GT_Material_Casings.INSTANCE);
        for (int i = 0; i < 1; i = (i + 1)) {
            Textures.BlockIcons.casingTexturePages[1][i+48] = new GT_CopiedBlockTexture(this, 6, i);
        }
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Chemically Inert Machine Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "PTFE Pipe Casing");

        ItemList.Casing_Chemically_Inert.set(new ItemStack(this, 1, 0));
        ItemList.Casing_Pipe_Polytetrafluoroethylene.set(new ItemStack(this, 1, 1));
    }
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int aSide, int aMeta) {
        switch (aMeta) {
        case 0:
            return Textures.BlockIcons.MACHINE_CASING_CHEMICALLY_INERT.getIcon();
        case 1:
            return Textures.BlockIcons.MACHINE_CASING_PIPE_POLYTETRAFLUOROETHYLENE.getIcon();
        }
        return Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
    }
}
