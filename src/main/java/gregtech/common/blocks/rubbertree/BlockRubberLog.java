package gregtech.common.blocks.rubbertree;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import net.minecraft.block.BlockLog;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockRubberLog extends BlockLog {

    public static final int META_NATURAL = 0x1;
    public static final int META_AXIS_MASK = 0xC;

    public static final int AXIS_Y = 0x0;
    public static final int AXIS_X = 0x4;
    public static final int AXIS_Z = 0x8;

    private static final int ELECTRIC_TREETAP_EU_PER_USE = 50;

    @SideOnly(Side.CLIENT)
    private IIcon sideIcon;

    @SideOnly(Side.CLIENT)
    private IIcon topIcon;

    @SideOnly(Side.CLIENT)
    private IIcon resinSideIcon;

    public BlockRubberLog() {
        super();

        setBlockName("gt.block_rubber_log");
        setCreativeTab(GregTechAPI.TAB_GREGTECH);
        setStepSound(soundTypeWood);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("axe", 0);

        GameRegistry.registerBlock(this, ItemBlockRubberLog.class, getUnlocalizedName());
    }

    public static boolean isNatural(int meta) {
        return (meta & META_NATURAL) != 0;
    }

    public static int makeMeta(boolean natural, int axisMeta) {
        return (natural ? META_NATURAL : 0) | (axisMeta & META_AXIS_MASK);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(@NotNull IIconRegister iconRegister) {
        this.sideIcon = iconRegister.registerIcon("gregtech:rubbertree/rubber_log_side");
        this.topIcon = iconRegister.registerIcon("gregtech:rubbertree/rubber_log_top");
        this.resinSideIcon = iconRegister.registerIcon("gregtech:rubbertree/rubber_log_resin_side");
        this.blockIcon = this.sideIcon;
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
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(@NotNull IBlockAccess world, int x, int y, int z, int side) {
        int meta = world.getBlockMetadata(x, y, z);
        int axis = meta & META_AXIS_MASK;

        if (axis != AXIS_Y) {
            return getIcon(side, meta);
        }

        if (side == 0 || side == 1) {
            return this.topIcon;
        }

        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityRubberLog rubberTe) {
            if (rubberTe.hasResin() && rubberTe.getResinSide() == side) {
                return this.resinSideIcon;
            }
        }

        return this.sideIcon;
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return isNatural(metadata);
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return isNatural(metadata) ? new TileEntityRubberLog() : null;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, @NotNull EntityPlayer player, int side,
                                    float hitX, float hitY, float hitZ) {

        boolean canHarvestFromThisFace = RubberTreeResinLogic.canHarvestResinFromSide(world, x, y, z, side);
        ItemStack heldStack = player.getCurrentEquippedItem();
        boolean usableTool = canUseToolForResinHarvest(heldStack, player);

        if (world.isRemote) {
            return canHarvestFromThisFace && usableTool;
        }

        if (!canHarvestFromThisFace || !usableTool) {
            return false;
        }

        if (!consumeToolForResinHarvest(heldStack, player)) {
            return false;
        }

        player.inventory.markDirty();
        player.inventoryContainer.detectAndSendChanges();

        if (!RubberTreeResinLogic.tryHarvestResin(world, x, y, z, side)) {
            return false;
        }

        spawnResinDrop(world, x, y, z, side, ItemList.Sticky_Resin.get(getResinAmount(world)));

        return true;
    }

    @Override
    public int damageDropped(int meta) {
        return 0;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubBlocks(Item item, net.minecraft.creativetab.CreativeTabs tab, @NotNull List list) {
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

    private static @Nullable Item getIC2ToolItem(String ic2Name) {
        ItemStack stack = GTModHandler.getIC2Item(ic2Name, 1L);
        return stack == null ? null : stack.getItem();
    }

    private static boolean isRegularTreetap(ItemStack stack) {
        if (stack == null) return false;

        Item treetap = getIC2ToolItem("treetap");
        return treetap != null && stack.getItem() == treetap;
    }

    private static boolean isElectricTreetap(ItemStack stack) {
        if (stack == null) return false;

        Item electricTreetap = getIC2ToolItem("electricTreetap");
        return electricTreetap != null && stack.getItem() == electricTreetap;
    }

    private static boolean canUseToolForResinHarvest(ItemStack stack, EntityPlayer player) {
        if (stack == null || player == null) {
            return false;
        }

        if (player.capabilities.isCreativeMode) {
            return isRegularTreetap(stack) || isElectricTreetap(stack);
        }

        if (isElectricTreetap(stack)) {
            return GTModHandler.canUseElectricItem(stack, ELECTRIC_TREETAP_EU_PER_USE);
        }

        return isRegularTreetap(stack);
    }

    private static boolean consumeToolForResinHarvest(ItemStack stack, EntityPlayer player) {
        if (stack == null || player == null) {
            return false;
        }

        if (player.capabilities.isCreativeMode) {
            return true;
        }

        if (isElectricTreetap(stack)) {
            return GTModHandler.useElectricItem(stack, ELECTRIC_TREETAP_EU_PER_USE, player);
        }

        if (isRegularTreetap(stack)) {
            stack.damageItem(1, player);

            if (stack.stackSize <= 0) {
                player.destroyCurrentEquippedItem();
            }

            return true;
        }

        return false;
    }

    private int getResinAmount(@NotNull World world) {
        int resinAmount = 1;
        if (world.rand.nextFloat() < 0.35F) resinAmount++;
        if (world.rand.nextFloat() < 0.20F) resinAmount++;
        if (world.rand.nextFloat() < 0.10F) resinAmount++;

        return resinAmount;
    }

    private void spawnResinDrop(World world, int x, int y, int z, int side, ItemStack stack) {
        ForgeDirection dir = ForgeDirection.getOrientation(side);

        double spawnX = x + 0.5D + dir.offsetX * 0.70D;
        double spawnY = y + 0.55D;
        double spawnZ = z + 0.5D + dir.offsetZ * 0.70D;

        EntityItem entityItem = new EntityItem(world, spawnX, spawnY, spawnZ, stack);
        entityItem.motionX = dir.offsetX * 0.08D;
        entityItem.motionY = 0.02D + world.rand.nextDouble() * 0.03D;
        entityItem.motionZ = dir.offsetZ * 0.08D;
        entityItem.delayBeforeCanPickup = 5;

        world.spawnEntityInWorld(entityItem);

        GTUtility.sendSoundToPlayers(
            world,
            "gregtech:resin.harvest",
            0.55F,
            0.96F + world.rand.nextFloat() * 0.10F,
            x + 0.5D,
            y + 0.5D,
            z + 0.5D
        );
    }
}
