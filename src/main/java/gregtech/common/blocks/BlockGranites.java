package gregtech.common.blocks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTOreDictUnificator;

public class BlockGranites extends BlockStonesAbstract {

    public BlockGranites() {
        super(ItemGranites.class, "gt.blockgranites");
        setResistance(60.0F);
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Black Granite");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Black Granite Cobblestone");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Mossy Black Granite Cobblestone");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Black Granite Bricks");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Cracked Black Granite Bricks");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Mossy Black Granite Bricks");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Chiseled Black Granite");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Smooth Black Granite");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "Red Granite");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".9.name", "Red Granite Cobblestone");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".10.name", "Mossy Red Granite Cobblestone");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".11.name", "Red Granite Bricks");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".12.name", "Cracked Red Granite Bricks");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".13.name", "Mossy Red Granite Bricks");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".14.name", "Chiseled Red Granite");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".15.name", "Smooth Red Granite");
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.GraniteBlack, new ItemStack(this, 1, 0));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.GraniteBlack, new ItemStack(this, 1, 1));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.GraniteBlack, new ItemStack(this, 1, 2));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.GraniteBlack, new ItemStack(this, 1, 3));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.GraniteBlack, new ItemStack(this, 1, 4));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.GraniteBlack, new ItemStack(this, 1, 5));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.GraniteBlack, new ItemStack(this, 1, 6));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.GraniteBlack, new ItemStack(this, 1, 7));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.GraniteRed, new ItemStack(this, 1, 8));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.GraniteRed, new ItemStack(this, 1, 9));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.GraniteRed, new ItemStack(this, 1, 10));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.GraniteRed, new ItemStack(this, 1, 11));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.GraniteRed, new ItemStack(this, 1, 12));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.GraniteRed, new ItemStack(this, 1, 13));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.GraniteRed, new ItemStack(this, 1, 14));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.GraniteRed, new ItemStack(this, 1, 15));
    }

    @Override
    public int getHarvestLevel(int aMeta) {
        return 3;
    }

    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        if ((aMeta >= 0) && (aMeta < 16)) {
            return gregtech.api.enums.Textures.BlockIcons.GRANITES[aMeta].getIcon();
        }
        return gregtech.api.enums.Textures.BlockIcons.GRANITES[0].getIcon();
    }

    @Override
    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
        return !(entity instanceof EntityWither);
    }
}
