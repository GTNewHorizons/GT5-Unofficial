package gregtech.common.blocks.rubbertree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ToolDictNames;
import gregtech.common.items.ItemRubberTreeTap;
import gregtech.crossmod.backhand.Backhand;

public class BlockRubberLogNatural extends Block {

    public static final String NAME = "gt.block_rubber_log_natural";

    public static final int META_EMPTY = 0;
    public static final int META_TAPPED_HOLE_MIN = 2;
    public static final int META_TAPPED_HOLE_MAX = 5;

    private IIcon sideIcon;
    private IIcon topIcon;
    private IIcon tappedSideIcon;
    @SideOnly(Side.CLIENT)
    private static IIcon woodendTreeTapRenderIcon;
    @SideOnly(Side.CLIENT)
    private static IIcon bronzeTreeTapRenderIcon;
    @SideOnly(Side.CLIENT)
    private static IIcon steelTreeTapRenderIcon;

    public BlockRubberLogNatural() {
        super(Material.wood);
        setBlockName(NAME);
        setCreativeTab(null);
        setStepSound(soundTypeWood);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("axe", 0);
        GameRegistry.registerBlock(this, ItemBlockRubberLogNatural.class, NAME);
    }

    public static boolean hasTappedHole(int meta) {
        return meta >= META_TAPPED_HOLE_MIN && meta <= META_TAPPED_HOLE_MAX;
    }

    public static boolean isTappedHoleSide(int meta, int side) {
        return hasTappedHole(meta) && meta == side;
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return hasTappedHole(metadata);
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return hasTappedHole(metadata) ? new TileEntityRubberLogTapped() : null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(@NotNull IIconRegister iconRegister) {
        this.sideIcon = iconRegister.registerIcon("gregtech:rubbertree/rubber_log_side");
        this.topIcon = iconRegister.registerIcon("gregtech:rubbertree/rubber_log_top");

        this.tappedSideIcon = iconRegister.registerIcon("gregtech:rubbertree/rubber_log_tap_side");
        woodendTreeTapRenderIcon = iconRegister.registerIcon("gregtech:rubbertree/tree_tap_wooden");
        bronzeTreeTapRenderIcon = iconRegister.registerIcon("gregtech:rubbertree/tree_tap_bronze");
        steelTreeTapRenderIcon = iconRegister.registerIcon("gregtech:rubbertree/tree_tap_steel");

        this.blockIcon = this.sideIcon;
    }

    @Override
    public String getUnlocalizedName() {
        return NAME;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (side == 0 || side == 1) {
            return this.topIcon;
        }

        return isTappedHoleSide(meta, side) ? this.tappedSideIcon : this.sideIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(@NotNull IBlockAccess world, int x, int y, int z, int side) {
        return getIcon(side, world.getBlockMetadata(x, y, z));
    }

    @SuppressWarnings({ "rawtypes" })
    @Override
    public void getSubBlocks(Item item, net.minecraft.creativetab.CreativeTabs tab, List list) {
        // Natural log is worldgen-only.
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return 5;
    }

    @Override
    public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return 5;
    }

    @Override
    public boolean canSustainLeaves(IBlockAccess world, int x, int y, int z) {
        return true;
    }

    @Override
    public boolean isWood(IBlockAccess world, int x, int y, int z) {
        return true;
    }

    @Override
    public Item getItemDropped(int meta, java.util.Random random, int fortune) {
        return Item.getItemFromBlock(GregTechAPI.sBlockRubberLog);
    }

    @Override
    protected ItemStack createStackedBlock(int meta) {
        return new ItemStack(GregTechAPI.sBlockRubberLog, 1, 0);
    }

    @Override
    public Item getItem(World world, int x, int y, int z) {
        return Item.getItemFromBlock(GregTechAPI.sBlockRubberLog);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<>(1);
        drops.add(new ItemStack(GregTechAPI.sBlockRubberLog, 1, 0));
        return drops;
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        return new ItemStack(GregTechAPI.sBlockRubberLog, 1, 0);
    }

    @Override
    public boolean onBlockActivated(@NotNull World world, int x, int y, int z, @NotNull EntityPlayer player, int side,
        float hitX, float hitY, float hitZ) {
        int meta = world.getBlockMetadata(x, y, z);

        if (world.isRemote) {
            return canClientAttemptInteraction(player, side, meta);
        }

        if (hasTappedHole(meta)) {
            if (player.isSneaking()) {
                TileEntityRubberLogTapped tile = getTappedTileEntity(world, x, y, z);

                if (tile != null && tile.hasInstalledTap()) {
                    removeInstalledTap(world, x, y, z, true, player);
                    return true;
                }

                return false;
            }

            if (!canInstallTapIntoExistingHole(world, x, y, z, player, side, meta)) {
                return false;
            }

            installTapIntoExistingHole(world, x, y, z, player, side);
            return true;
        }

        if (!canCreateTappedHole(world, x, y, z, player, side, meta)) {
            return false;
        }

        createTappedHoleAndInstallTap(world, x, y, z, player, side);
        return true;
    }

    private boolean canClientAttemptInteraction(@NotNull EntityPlayer player, int side, int meta) {
        if (player.isSneaking() && hasTappedHole(meta)) {
            return true;
        }

        return side >= META_TAPPED_HOLE_MIN && side <= META_TAPPED_HOLE_MAX
            && isSoftMallet(player.getCurrentEquippedItem())
            && isTreeTapItem(Backhand.getOffhandItem(player))
            && (meta == META_EMPTY || hasTappedHole(meta));
    }

    private boolean canCreateTappedHole(@NotNull World world, int x, int y, int z, @NotNull EntityPlayer player,
        int side, int meta) {
        return side >= META_TAPPED_HOLE_MIN && side <= META_TAPPED_HOLE_MAX
            && meta == META_EMPTY
            && isSoftMallet(player.getCurrentEquippedItem())
            && isTreeTapItem(Backhand.getOffhandItem(player))
            && RubberTreeResinLogic.isLowerTrunkPosition(world, x, y, z)
            && RubberTreeResinLogic.countTappedHolesOnLowerTrunk(world, x, y, z)
                < RubberTreeResinLogic.MAX_TAPPED_HOLES_PER_TREE
            && !RubberTreeResinLogic.isTreeReservoirExhausted(world, x, y, z)
            && RubberTreeResinLogic.isReplaceableSide(world, x, y, z, side);
    }

    private boolean canInstallTapIntoExistingHole(@NotNull World world, int x, int y, int z,
        @NotNull EntityPlayer player, int side, int meta) {
        if (!hasTappedHole(meta)) {
            return false;
        }

        if (side != meta) {
            return false;
        }

        if (!isSoftMallet(player.getCurrentEquippedItem()) || !isTreeTapItem(Backhand.getOffhandItem(player))) {
            return false;
        }

        if (!RubberTreeResinLogic.isLowerTrunkPosition(world, x, y, z)) {
            return false;
        }

        if (RubberTreeResinLogic.isTreeReservoirExhausted(world, x, y, z)) {
            return false;
        }

        TileEntityRubberLogTapped tile = getTappedTileEntity(world, x, y, z);
        return tile == null || tile.canAcceptTap();
    }

    private void createTappedHoleAndInstallTap(@NotNull World world, int x, int y, int z, @NotNull EntityPlayer player,
        int side) {
        ItemStack offhandTap = Backhand.getOffhandItem(player);
        if (!isTreeTapItem(offhandTap)) {
            return;
        }

        ItemStack installedTap = copyOne(offhandTap);
        int initialRemainingResin = RubberTreeResinLogic.getInitialRemainingResinForNewHole(world, x, y, z, world.rand);

        world.setBlockMetadataWithNotify(x, y, z, side, 3);

        TileEntityRubberLogTapped tile = getOrCreateTappedTileEntity(world, x, y, z);
        if (tile == null) {
            world.setBlockMetadataWithNotify(x, y, z, META_EMPTY, 3);
            return;
        }

        consumeOffhandTap(player, offhandTap);

        tile.configureNewHole(installedTap, side, world.rand, initialRemainingResin);
        RubberTreeResinLogic.syncRemainingResinOnLowerTrunk(world, x, y, z, initialRemainingResin);
    }

    private void installTapIntoExistingHole(@NotNull World world, int x, int y, int z, @NotNull EntityPlayer player,
        int side) {
        ItemStack offhandTap = Backhand.getOffhandItem(player);
        if (!isTreeTapItem(offhandTap)) {
            return;
        }

        ItemStack installedTap = copyOne(offhandTap);

        TileEntityRubberLogTapped tile = getOrCreateTappedTileEntity(world, x, y, z);
        if (tile == null || !tile.canAcceptTap()) {
            return;
        }

        int remainingResin = RubberTreeResinLogic.getInitialRemainingResinForNewHole(world, x, y, z, world.rand);
        if (remainingResin <= 0) {
            return;
        }

        consumeOffhandTap(player, offhandTap);

        tile.setRemainingResin(remainingResin);
        tile.installTap(installedTap, side, world.rand);
        RubberTreeResinLogic.syncRemainingResinOnLowerTrunk(world, x, y, z, remainingResin);
    }

    public void removeInstalledTap(@NotNull World world, int x, int y, int z, boolean dropTap,
        @Nullable EntityPlayer player) {
        TileEntityRubberLogTapped tile = getTappedTileEntity(world, x, y, z);
        if (tile == null) {
            return;
        }

        ItemStack tapStack = dropTap ? tile.removeInstalledTapForDrop() : null;

        if (!dropTap) {
            tile.clearInstalledTap();
        }

        if (tapStack != null && tapStack.stackSize > 0) {
            giveOrDropTap(world, x, y, z, tapStack, player);
        }
    }

    @Override
    public boolean removedByPlayer(@NotNull World world, EntityPlayer player, int x, int y, int z,
        boolean willHarvest) {
        boolean shouldFellTree = !world.isRemote && RubberTreeResinLogic.isFellingCutPosition(world, x, y, z);

        List<RubberTreeResinLogic.LogPosition> logsToTransform = shouldFellTree
            ? RubberTreeResinLogic.collectNaturalTrunkLogs(world, x, y, z)
            : Collections.emptyList();

        boolean removed = super.removedByPlayer(world, player, x, y, z, willHarvest);

        if (removed && shouldFellTree) {
            RubberTreeResinLogic.fellNaturalTree(world, logsToTransform, x, y, z);
        }

        return removed;
    }

    @Override
    public void breakBlock(@NotNull World world, int x, int y, int z, Block block, int meta) {
        if (!world.isRemote && hasTappedHole(meta)
            && world.getGameRules()
                .getGameRuleBooleanValue("doTileDrops")) {
            TileEntity tile = world.getTileEntity(x, y, z);

            if (tile instanceof TileEntityRubberLogTapped) {
                ItemStack tapStack = ((TileEntityRubberLogTapped) tile).getTapStackForDrop();

                if (tapStack != null && tapStack.stackSize > 0) {
                    spawnItemStack(world, x, y, z, tapStack);
                }
            }
        }

        super.breakBlock(world, x, y, z, block, meta);
    }

    private static @Nullable TileEntityRubberLogTapped getTappedTileEntity(@NotNull World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);
        return tile instanceof TileEntityRubberLogTapped ? (TileEntityRubberLogTapped) tile : null;
    }

    private static @Nullable TileEntityRubberLogTapped getOrCreateTappedTileEntity(@NotNull World world, int x, int y,
        int z) {
        if (!hasTappedHole(world.getBlockMetadata(x, y, z))) {
            return null;
        }

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntityRubberLogTapped) {
            return (TileEntityRubberLogTapped) tile;
        }

        TileEntityRubberLogTapped tappedTile = new TileEntityRubberLogTapped();
        tappedTile.setSide(world.getBlockMetadata(x, y, z));
        world.setTileEntity(x, y, z, tappedTile);

        return tappedTile;
    }

    private static boolean isTreeTapItem(@Nullable ItemStack stack) {
        return stack != null && stack.getItem() instanceof ItemRubberTreeTap;
    }

    private static boolean isSoftMallet(@Nullable ItemStack stack) {
        if (stack == null) {
            return false;
        }

        int[] oreIds = OreDictionary.getOreIDs(stack);
        for (int oreId : oreIds) {
            if (ToolDictNames.craftingToolSoftMallet.name()
                .equals(OreDictionary.getOreName(oreId))) {
                return true;
            }
        }

        return false;
    }

    private static @NotNull ItemStack copyOne(@NotNull ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.stackSize = 1;
        return copy;
    }

    private static void consumeOffhandTap(@NotNull EntityPlayer player, @NotNull ItemStack offhandTap) {
        if (player.capabilities.isCreativeMode) {
            return;
        }

        offhandTap.stackSize--;

        if (offhandTap.stackSize <= 0) {
            int offhandSlot = Backhand.getOffhandSlot(player);

            if (offhandSlot >= 0) {
                player.inventory.setInventorySlotContents(offhandSlot, null);
            }
        }

        player.inventory.markDirty();
        player.inventoryContainer.detectAndSendChanges();
    }

    private static void giveOrDropTap(@NotNull World world, int x, int y, int z, @NotNull ItemStack tapStack,
        @Nullable EntityPlayer player) {
        if (player != null) {
            if (player.capabilities.isCreativeMode) {
                return;
            }

            if (player.inventory.addItemStackToInventory(tapStack.copy())) {
                player.inventory.markDirty();
                player.inventoryContainer.detectAndSendChanges();
                return;
            }
        }

        spawnItemStack(world, x, y, z, tapStack);
    }

    private static void spawnItemStack(@NotNull World world, int x, int y, int z, @NotNull ItemStack stack) {
        double spawnX = x + 0.5D;
        double spawnY = y + 0.5D;
        double spawnZ = z + 0.5D;

        EntityItem entityItem = new EntityItem(world, spawnX, spawnY, spawnZ, stack);
        entityItem.delayBeforeCanPickup = 10;
        world.spawnEntityInWorld(entityItem);
    }

    // 3D special renderer
    @SideOnly(Side.CLIENT)
    public static IIcon getWoodendTreeTapRenderIcon() {
        return woodendTreeTapRenderIcon;
    }

    @SideOnly(Side.CLIENT)
    public static IIcon getBronzeTreeTapRenderIcon() {
        return bronzeTreeTapRenderIcon;
    }

    @SideOnly(Side.CLIENT)
    public static IIcon getSteelTreeTapRenderIcon() {
        return steelTreeTapRenderIcon;
    }
}
