package gregtech.api.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.util.GTLanguageManager;
import gregtech.common.blocks.ItemLongDistancePipe;
import gregtech.common.blocks.MaterialMachines;

public class BlockLongDistancePipe extends GTGenericBlock {

    public IIconContainer[] mIcons;

    public BlockLongDistancePipe() {
        super(ItemLongDistancePipe.class, "gt.block.longdistancepipe", new MaterialMachines());
        setStepSound(soundTypeMetal);
        setCreativeTab(GregTechAPI.TAB_GREGTECH);
        GregTechAPI.registerMachineBlock(this, -1);

        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Long Distance Fluid Pipeline Pipe");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Long Distance Item Pipeline Pipe");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + "." + 32767 + ".name", "Any Sub Block of this");

        ItemList.Long_Distance_Pipeline_Fluid_Pipe.set(new ItemStack(this, 1, 0));
        ItemList.Long_Distance_Pipeline_Item_Pipe.set(new ItemStack(this, 1, 1));
        mIcons = new IIconContainer[] { Textures.BlockIcons.LONG_DISTANCE_PIPE_FLUID,
            Textures.BlockIcons.LONG_DISTANCE_PIPE_ITEM };
    }

    @Override
    public void onBlockAdded(World aWorld, int aX, int aY, int aZ) {
        super.onBlockAdded(aWorld, aX, aY, aZ);
        if (GregTechAPI.isMachineBlock(this, aWorld.getBlockMetadata(aX, aY, aZ))) {
            GregTechAPI.causeMachineUpdate(aWorld, aX, aY, aZ);
        }
    }

    @Override
    public void breakBlock(World aWorld, int aX, int aY, int aZ, Block aBlock, int aMetaData) {
        GregTechAPI.causeMachineUpdate(aWorld, aX, aY, aZ);
        super.breakBlock(aWorld, aX, aY, aZ, aBlock, aMetaData);
    }

    @Override
    public String getHarvestTool(int aMeta) {
        return "wrench";
    }

    @Override
    public int getHarvestLevel(int aMeta) {
        return 2;
    }

    @Override
    public float getBlockHardness(World aWorld, int aX, int aY, int aZ) {
        return Blocks.stone.getBlockHardness(aWorld, aX, aY, aZ);
    }

    @Override
    public float getExplosionResistance(Entity aTNT) {
        return Blocks.iron_block.getExplosionResistance(aTNT);
    }

    @Override
    public String getUnlocalizedName() {
        return this.mUnlocalizedName;
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal(this.mUnlocalizedName + ".name");
    }

    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return mIcons[aMeta % mIcons.length].getIcon();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister aIconRegister) {}

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    @Override
    public int getDamageValue(World aWorld, int aX, int aY, int aZ) {
        return aWorld.getBlockMetadata(aX, aY, aZ);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item aItem, CreativeTabs aCreativeTab, List<ItemStack> aList) {
        for (int i = 0; i < 3; i++) {
            ItemStack aStack = new ItemStack(aItem, 1, i);
            if (!aStack.getDisplayName()
                .contains(".name")) aList.add(aStack);
        }
    }
}
