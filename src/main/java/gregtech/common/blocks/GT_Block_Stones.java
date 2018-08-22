package gregtech.common.blocks;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class GT_Block_Stones extends GT_Block_Stones_Abstract {
    public GT_Block_Stones() {
        super(GT_Item_Granites.class, "gt.blockstones");
        setResistance(60.0F);
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Marble");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Marble Cobblestone");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Mossy Marble Cobblestone");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Marble Bricks");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Cracked Marble Bricks");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Mossy Marble Bricks");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Chiseled Marble");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Smooth Marble");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "Basalt");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".9.name", "Basalt Cobblestone");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".10.name", "Mossy Basalt Cobblestone");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".11.name", "Basalt Bricks");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".12.name", "Cracked Basalt Bricks");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".13.name", "Mossy Basalt Bricks");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".14.name", "Chiseled Basalt");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".15.name", "Smooth Basalt");
        for(int i = 0;i<16;i++){
            GT_OreDictUnificator.registerOre(OrePrefixes.stone, i < 8 ? Materials.Marble : Materials.Basalt, new ItemStack(this, 1, i));
            GT_OreDictUnificator.registerOre(OrePrefixes.block, i < 8 ? Materials.Marble : Materials.Basalt, new ItemStack(this, 1, i));
            GT_OreDictUnificator.registerOre((i < 8 ? Materials.Marble.mName.toLowerCase() : Materials.Basalt.mName.toLowerCase()), new ItemStack(this, 1, i));        	
        }
    }

    public int getHarvestLevel(int aMeta) {
        return 1;
    }

    public float getBlockHardness(World aWorld, int aX, int aY, int aZ) {
        return this.blockHardness = Blocks.stone.getBlockHardness(aWorld, aX, aY, aZ) * 3.0F;
    }

    public IIcon getIcon(int aSide, int aMeta) {
        if ((aMeta >= 0) && (aMeta < 16)) {
            return gregtech.api.enums.Textures.BlockIcons.STONES[aMeta].getIcon();
        }
        return gregtech.api.enums.Textures.BlockIcons.STONES[0].getIcon();
    }
}
