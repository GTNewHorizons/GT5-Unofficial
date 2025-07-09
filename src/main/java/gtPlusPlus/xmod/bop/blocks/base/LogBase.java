package gtPlusPlus.xmod.bop.blocks.base;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.block.BlockLog;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GTRecipeBuilder;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.util.Utils;

public abstract class LogBase extends BlockLog {

    public String[] treeType;
    protected IIcon[] textureSide;
    protected IIcon[] textureTop;

    public LogBase(String blockNameLocalized, String blockNameUnlocalized, String[] treeTypes) {
        this.treeType = treeTypes;
        String blockName = "block" + Utils.sanitizeString(blockNameLocalized) + "Log";
        GameRegistry.registerBlock(this, ItemBlock.class, blockName);
        this.setBlockName(blockName);
        OreDictionary.registerOre("logWood", new ItemStack(this, 1, GTRecipeBuilder.WILDCARD));
        this.setCreativeTab(AddToCreativeTab.tabBOP);
        Blocks.fire.setFireInfo(this, 20, 100);
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List metaList) {
        for (int i = 0; i < this.textureSide.length; ++i) {
            metaList.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected IIcon getTopIcon(int meta) {
        return this.textureTop[meta % this.textureTop.length];
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected IIcon getSideIcon(int metaID) {
        return this.textureSide[metaID % this.textureSide.length];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iIcon) {
        this.textureSide = new IIcon[treeType.length];
        this.textureTop = new IIcon[treeType.length];

        for (int i = 0; i < this.textureSide.length; ++i) {
            this.textureSide[i] = iIcon.registerIcon(GTPlusPlus.ID + ":" + "trees/" + "logs/" + "log_" + treeType[i]);
            this.textureTop[i] = iIcon
                .registerIcon(GTPlusPlus.ID + ":" + "trees/" + "logs/" + "log_" + treeType[i] + "_top");
        }
        // TODO uncomment the line below, I don't want to do it now in case it causes a crash
        // this.field_150167_a = this.textureSide;
        // this.field_150166_b = this.textureTop;
    }
}
