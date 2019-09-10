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

    //WATCH OUT FOR TEXTURE ID's
    public GT_Block_Casings8() {
        super(GT_Item_Casings8.class, "gt.blockcasings8", GT_Material_Casings.INSTANCE);
        for (int i = 0; i < 1; i = (i + 1)) {
            Textures.BlockIcons.casingTexturePages[1][i+48] = new GT_CopiedBlockTexture(this, 6, i);
        }
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Chemically Inert Machine Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "PTFE Pipe Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Robust Naquadah Alloy Machine Casing");//adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Bloody Ichorium Machine Casing");//adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Machine Casing from The End");//adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Draconium Machine Casing");//adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Awakened Draconium Machine Casing");//adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Chaotic Machine Casing");//adding
        

        ItemList.Casing_Chemically_Inert.set(new ItemStack(this, 1, 0));
        ItemList.Casing_Pipe_Polytetrafluoroethylene.set(new ItemStack(this, 1, 1));
        ItemList.Casing_NaquadahAlloy.set(new ItemStack(this, 1, 2));//adding
        ItemList.Casing_BloodyIchorium.set(new ItemStack(this, 1, 3));//adding
        ItemList.Casing_Wyvern.set(new ItemStack(this, 1, 4));//adding
        ItemList.Casing_Draconium.set(new ItemStack(this, 1, 5));//adding
        ItemList.Casing_DraconiumAwakened.set(new ItemStack(this, 1, 6));//adding
        ItemList.Casing_Chaotic.set(new ItemStack(this, 1, 7));//adding
        
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int aSide, int aMeta) {
        switch (aMeta) {
        case 0:
            return Textures.BlockIcons.MACHINE_CASING_CHEMICALLY_INERT.getIcon();
        case 1:
            return Textures.BlockIcons.MACHINE_CASING_PIPE_POLYTETRAFLUOROETHYLENE.getIcon();
        case 2:
            return Textures.BlockIcons.MACHINE_CASING_ROBUST_NAQUADAHALLOY.getIcon();
        case 3:
            return Textures.BlockIcons.MACHINE_CASING_BLOODYICHORIUM.getIcon();
        case 4:
            return Textures.BlockIcons.MACHINE_CASING_WYVERN.getIcon();
        case 5:
            return Textures.BlockIcons.MACHINE_CASING_DRACONIUM.getIcon();
        case 6:
            return Textures.BlockIcons.MACHINE_CASING_DRACONIUMAWAKENED.getIcon();
        case 7:
            return Textures.BlockIcons.MACHINE_CASING_CHAOTIC.getIcon();
        }
        return Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
    }
}
