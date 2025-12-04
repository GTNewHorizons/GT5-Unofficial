package gtPlusPlus.core.block.fuel;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import org.apache.commons.lang3.tuple.Pair;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.item.base.itemblock.ItemBlockMeta;
import gtPlusPlus.core.lib.GTPPCore;

public class BlockSugarCharcoal extends Block {

    private final IIcon[] textureArray = new IIcon[6];

    public BlockSugarCharcoal() {
        super(Material.rock);
        this.setBlockName("blockSugarCharcoal");
        this.setStepSound(soundTypeStone);
        setResistance(20);
        this.setHardness(5);
        this.setHarvestLevel("pickaxe", 1);
        GameRegistry.registerBlock(this, ItemBlockMeta.class, "blockSugarCharcoal");
        for (int i = 0; i < textureArray.length; i++) {
            GTPPCore.burnables.add(Pair.of(4000 * (int) GTUtility.powInt(9, i), new ItemStack(this, 1, i)));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        for (int i = 0; i < textureArray.length; i++) {
            this.textureArray[i] = iconRegister.registerIcon(GTPlusPlus.ID + ":fuel/blockSugarCharcoal" + i);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return this.textureArray[meta];
    }

    @Override
    public void getSubBlocks(final Item item, final CreativeTabs tab, final List<ItemStack> list) {
        for (int i = 0; i < textureArray.length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

}
