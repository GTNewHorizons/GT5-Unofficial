package gtPlusPlus.xmod.gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.common.blocks.BlockCasingsAbstract;
import gtPlusPlus.core.creative.AddToCreativeTab;

public abstract class GregtechMetaCasingBlocksAbstract extends BlockCasingsAbstract {

    public GregtechMetaCasingBlocksAbstract(final Class<? extends ItemBlock> aItemClass, final String aName,
        final Material aMaterial) {
        super(aItemClass, aName, aMaterial);
        this.setStepSound(soundTypeMetal);
        this.setCreativeTab(AddToCreativeTab.tabMachines);
        GregTechAPI.registerMachineBlock(this, -1);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderBlockPass() {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister aIconRegister) {}

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item aItem, final CreativeTabs par2CreativeTabs, final List aList) {
        for (int i = 0; i < 16; i++) {
            aList.add(new ItemStack(aItem, 1, i));
        }
    }
}
