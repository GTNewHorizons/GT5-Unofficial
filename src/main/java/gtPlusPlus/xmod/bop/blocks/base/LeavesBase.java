package gtPlusPlus.xmod.bop.blocks.base;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class LeavesBase extends BlockLeaves {

    protected IIcon[][] leafTextures = new IIcon[2][];
    protected String[][] leafType = new String[][] { {}, {} };
    protected String[] treeType = new String[] {};
    protected ItemStack[] bonusDrops;

    public LeavesBase(String blockNameLocalized, String blockNameUnlocalized, ItemStack[] bonusDrops) {
        this.bonusDrops = bonusDrops;
        String blockName = "block" + Utils.sanitizeString(blockNameLocalized) + "Leaves";
        GameRegistry.registerBlock(this, ItemBlock.class, blockName);
        this.setBlockName(blockName);
        ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(this), "treeLeaves", true);
        this.setCreativeTab(AddToCreativeTab.tabBOP);
        Blocks.fire.setFireInfo(this, 80, 150);
    }

    private void setVanillaVariable(Object toSet, Object value) {
        toSet = value;
    }

    @Override
    public int quantityDropped(Random p_149745_1_) {
        return p_149745_1_.nextInt(20) == 0 ? 1 : 0;
    }

    @Override // Drops when Leaf is broken
    protected void func_150124_c(World world, int x, int y, int z, int meta, int randomChance) {
        Logger.INFO("Dropping Bonus Drops");
        for (ItemStack bonusDrop : this.bonusDrops) {
            if (bonusDrop != null && world.rand.nextInt(randomChance) == 0) {
                this.dropBlockAsItem(world, x, y, z, ItemUtils.getSimpleStack(bonusDrop, 1));
            }
        }
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, @SuppressWarnings("rawtypes") List metaList) {
        for (int i = 0; i < this.treeType.length; ++i) {
            metaList.add(new ItemStack(item, 1, i));
        }
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int metaID) {
        return (metaID & 3) == 1 ? this.leafTextures[this.field_150127_b][1]
                : this.leafTextures[this.field_150127_b][0];
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iIcon) {
        for (int i = 0; i < leafType.length; ++i) {
            this.leafTextures[i] = new IIcon[leafType[i].length];
            for (int j = 0; j < leafType[i].length; ++j) {
                this.leafTextures[i][j] = iIcon
                        .registerIcon(GTPlusPlus.ID + ":" + "trees/" + "leaves/" + "leaves_" + leafType[i][j]);
            }
        }
        setVanillaVariable(this.field_150129_M, this.leafTextures);
    }

    @Override
    public String[] func_150125_e() {
        return treeType;
    }
}
