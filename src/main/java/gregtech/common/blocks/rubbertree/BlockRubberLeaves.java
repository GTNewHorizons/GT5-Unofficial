package gregtech.common.blocks.rubbertree;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockLeaves;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;

public class BlockRubberLeaves extends BlockLeaves {

    public static final String NAME = "gt.block_rubber_leaves";

    @SideOnly(Side.CLIENT)
    private final IIcon[][] icons = new IIcon[2][1];

    public BlockRubberLeaves() {
        super();

        setBlockName(NAME);
        setCreativeTab(GregTechAPI.TAB_GREGTECH);
        setStepSound(soundTypeGrass);
        setHardness(0.2F);
        setResistance(1.0F);
        setHarvestLevel("shears", 0);
        setLightOpacity(1);

        GameRegistry.registerBlock(this, ItemBlockRubberLeaves.class, NAME);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(@NotNull IIconRegister iconRegister) {
        this.icons[0][0] = iconRegister.registerIcon("gregtech:rubbertree/rubber_leaves_fancy");
        this.icons[1][0] = iconRegister.registerIcon("gregtech:rubbertree/rubber_leaves_fast");
        this.blockIcon = this.icons[0][0];
    }

    @Override
    public String getUnlocalizedName() {
        return NAME;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        this.setGraphicsLevel(Minecraft.getMinecraft().gameSettings.fancyGraphics);
        return this.icons[this.field_150127_b][0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setGraphicsLevel(boolean fancy) {
        super.setGraphicsLevel(fancy);
        this.blockIcon = this.icons[this.field_150127_b][0];
    }

    @Override
    public String[] func_150125_e() {
        return new String[] { "rubber" };
    }

    @Override
    public int damageDropped(int meta) {
        return 0;
    }

    @Override
    public Item getItemDropped(int meta, Random random, int fortune) {
        return GregTechAPI.sBlockRubberSapling == null ? null : Item.getItemFromBlock(GregTechAPI.sBlockRubberSapling);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, @NotNull List list) {
        list.add(new ItemStack(item, 1, 0));
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return 30;
    }

    @Override
    public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return 60;
    }
}
