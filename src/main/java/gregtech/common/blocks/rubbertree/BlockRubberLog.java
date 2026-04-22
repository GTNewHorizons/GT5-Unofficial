package gregtech.common.blocks.rubbertree;

import java.util.List;

import net.minecraft.block.BlockLog;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;

public class BlockRubberLog extends BlockLog {

    public static final String NAME = "gt.block_rubber_log";
    public static final int META_AXIS_MASK = 0xC;

    public static final int AXIS_Y = 0x0;
    public static final int AXIS_X = 0x4;
    public static final int AXIS_Z = 0x8;

    @SideOnly(Side.CLIENT)
    protected IIcon sideIcon;

    @SideOnly(Side.CLIENT)
    protected IIcon topIcon;

    public BlockRubberLog() {
        super();

        setBlockName(NAME);
        setCreativeTab(GregTechAPI.TAB_GREGTECH);
        setStepSound(soundTypeWood);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("axe", 0);

        GameRegistry.registerBlock(this, ItemBlockRubberLog.class, NAME);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(@NotNull IIconRegister iconRegister) {
        this.sideIcon = iconRegister.registerIcon("gregtech:rubbertree/rubber_log_side");
        this.topIcon = iconRegister.registerIcon("gregtech:rubbertree/rubber_log_top");
        this.blockIcon = this.sideIcon;
    }

    @Override
    public String getUnlocalizedName() {
        return NAME;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        int axis = meta & META_AXIS_MASK;

        return switch (axis) {
            case AXIS_Y -> side == 0 || side == 1 ? this.topIcon : this.sideIcon;
            case AXIS_X -> side == 4 || side == 5 ? this.topIcon : this.sideIcon;
            case AXIS_Z -> side == 2 || side == 3 ? this.topIcon : this.sideIcon;
            default -> this.sideIcon;
        };
    }

    @Override
    public int damageDropped(int meta) {
        return 0;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, @NotNull List list) {
        list.add(new net.minecraft.item.ItemStack(item, 1, 0));
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return 5;
    }

    @Override
    public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return 5;
    }
}
