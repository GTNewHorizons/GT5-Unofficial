package gregtech.common.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTOreDictUnificator;

public class BlockStones extends BlockStonesAbstract {

    public BlockStones() {
        super(ItemGranites.class, "gt.blockstones");
        setResistance(60.0F);
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Marble");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Marble Cobblestone");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Mossy Marble Cobblestone");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Marble Bricks");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Cracked Marble Bricks");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Mossy Marble Bricks");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Chiseled Marble");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Smooth Marble");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "Basalt");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".9.name", "Basalt Cobblestone");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".10.name", "Mossy Basalt Cobblestone");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".11.name", "Basalt Bricks");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".12.name", "Cracked Basalt Bricks");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".13.name", "Mossy Basalt Bricks");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".14.name", "Chiseled Basalt");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".15.name", "Smooth Basalt");
        for (int i = 0; i < 16; i++) {
            GTOreDictUnificator
                .registerOre(OrePrefixes.stone, i < 8 ? Materials.Marble : Materials.Basalt, new ItemStack(this, 1, i));
            GTOreDictUnificator
                .registerOre(OrePrefixes.block, i < 8 ? Materials.Marble : Materials.Basalt, new ItemStack(this, 1, i));
            GTOreDictUnificator.registerOre(
                (i < 8 ? Materials.Marble.mName.toLowerCase() : Materials.Basalt.mName.toLowerCase()),
                new ItemStack(this, 1, i));
        }
    }

    @Override
    public int getHarvestLevel(int aMeta) {
        return 2;
    }

    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        if ((aMeta >= 0) && (aMeta < 16)) {
            return gregtech.api.enums.Textures.BlockIcons.STONES[aMeta].getIcon();
        }
        return gregtech.api.enums.Textures.BlockIcons.STONES[0].getIcon();
    }
}
