package gregtech.common.blocks;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IBlockOnWalkOver;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTOreDictUnificator;

public class BlockConcretes extends BlockStonesAbstract implements IBlockOnWalkOver {

    public BlockConcretes() {
        super(ItemConcretes.class, "gt.blockconcretes");
        setResistance(20.0F);
        // this.slipperiness = 0.9F;
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Dark Concrete");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Dark Concrete Cobblestone");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Mossy Dark Concrete Cobblestone");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Dark Concrete Bricks");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Cracked Dark Concrete Bricks");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Mossy Dark Concrete Bricks");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Chiseled Dark Concrete");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Smooth Dark Concrete");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "Light Concrete");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".9.name", "Light Concrete Cobblestone");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".10.name", "Mossy Light Concrete Cobblestone");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".11.name", "Light Concrete Bricks");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".12.name", "Cracked Light Concrete Bricks");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".13.name", "Mossy Light Concrete Bricks");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".14.name", "Chiseled Light Concrete");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".15.name", "Smooth Light Concrete");
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.Concrete, new ItemStack(this, 1, 0));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.Concrete, new ItemStack(this, 1, 1));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.Concrete, new ItemStack(this, 1, 2));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.Concrete, new ItemStack(this, 1, 3));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.Concrete, new ItemStack(this, 1, 4));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.Concrete, new ItemStack(this, 1, 5));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.Concrete, new ItemStack(this, 1, 6));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.Concrete, new ItemStack(this, 1, 7));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.Concrete, new ItemStack(this, 1, 8));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.Concrete, new ItemStack(this, 1, 9));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.Concrete, new ItemStack(this, 1, 10));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.Concrete, new ItemStack(this, 1, 11));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.Concrete, new ItemStack(this, 1, 12));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.Concrete, new ItemStack(this, 1, 13));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.Concrete, new ItemStack(this, 1, 14));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.Concrete, new ItemStack(this, 1, 15));
    }

    @Override
    public int getHarvestLevel(int aMeta) {
        return 1;
    }

    @Override
    public float getBlockHardness(World aWorld, int aX, int aY, int aZ) {
        return this.blockHardness = Blocks.stone.getBlockHardness(aWorld, aX, aY, aZ);
    }

    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        if ((aMeta >= 0) && (aMeta < 16)) {
            return gregtech.api.enums.Textures.BlockIcons.CONCRETES[aMeta].getIcon();
        }
        return gregtech.api.enums.Textures.BlockIcons.CONCRETES[0].getIcon();
    }

    @Override
    public void onWalkOver(EntityLivingBase aEntity, World aWorld, int aX, int aY, int aZ) {
        if ((aEntity.motionX != 0 || aEntity.motionZ != 0) && !aEntity.isInWater()
            && !aEntity.isWet()
            && !aEntity.isSneaking()) {
            double tSpeed = (aWorld.getBlock(aX, aY - 1, aZ).slipperiness >= 0.8 ? 1.5 : 1.2);
            aEntity.motionX *= tSpeed;
            aEntity.motionZ *= tSpeed;
        }
    }
}
