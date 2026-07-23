package gregtech.common.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.ShapeBlock;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.material.MU;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.BaseTileEntity;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GTUtility;
import gregtech.common.render.GTRendererBlock;

/// The block backing one pipe-family MaterialLib shape (a wire, cable, fluid pipe, or item pipe of one
/// size): metadata is the material's global index, and every placement carries a [BaseMetaPipeEntity]
/// whose meta tile entity is the single material-agnostic instance registered for this shape's [#mteId].
/// World behavior (collision, redstone, drops, updates) mirrors the machine block that hosts the legacy
/// per-material pipe MTEs.
public class PipeShapeBlock extends ShapeBlock {

    /// The pipe kind a shape belongs to, deciding harvest tool, tile entity kind, and stat derivation.
    public enum PipeFamily {
        WIRE,
        CABLE,
        FLUID,
        FLUID_MULTI,
        ITEM,
        ITEM_RESTRICTIVE
    }

    private static final ThreadLocal<IGregTechTileEntity> temporaryTileEntity = new ThreadLocal<>();

    private final String prefixKey;
    private final int mteId;
    private final PipeFamily family;
    private final int sizeIndex;
    private final int pipeAmount;
    private final float thickness;

    private boolean checkingAdjacent = false;

    public PipeShapeBlock(String name, String displayNameFormat, String prefixKey, int mteId, PipeFamily family,
        int sizeIndex, int pipeAmount, float thickness, String... oreDicts) {
        super(new MaterialMachines(), "gregtech", name, displayNameFormat, oreDicts);
        this.prefixKey = prefixKey;
        this.mteId = mteId;
        this.family = family;
        this.sizeIndex = sizeIndex;
        this.pipeAmount = pipeAmount;
        this.thickness = thickness;
        setHardness(1.0F);
        setCreativeTab(GregTechAPI.TAB_GREGTECH);
        useNeighborBrightness = true;
        GregTechAPI.registerMachineBlock(this, -1);
    }

    /// The language key of this shape's `gt.oreprefix.*` display-name format.
    public String getPrefixKey() {
        return prefixKey;
    }

    /// The id of the single material-agnostic MTE every placement of this shape binds.
    public int getMteId() {
        return mteId;
    }

    public PipeFamily getFamily() {
        return family;
    }

    /// The size step within the family: wires/cables 0 to 5, pipes 0 (tiny) to 4 (huge).
    public int getSizeIndex() {
        return sizeIndex;
    }

    /// Fluid channel count; 1 outside [PipeFamily#FLUID_MULTI].
    public int getPipeAmount() {
        return pipeAmount;
    }

    public float getThickness() {
        return thickness;
    }

    private boolean isWireOrCable() {
        return family == PipeFamily.WIRE || family == PipeFamily.CABLE;
    }

    @Override
    public void registerWithGame() {
        GameRegistry.registerBlock(this, PipeShapeItemBlock.class, getName());
    }

    @Override
    public int getRenderType() {
        return GTRendererBlock.RENDER_ID;
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
    public boolean canRenderInPass(int pass) {
        return pass == 0 || pass == 1;
    }

    @Override
    public boolean isNormalCube(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube() {
        return true;
    }

    @Override
    public boolean hasTileEntity(int meta) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new BaseMetaPipeEntity();
    }

    @Override
    public String getHarvestTool(int meta) {
        return isWireOrCable() ? "cutter" : "wrench";
    }

    @Override
    public int getHarvestLevel(int meta) {
        return switch (family) {
            case WIRE -> 0;
            case CABLE -> 1;
            default -> GTUtility.clamp(MU.toolQuality(MaterialLibAPI.getMaterialByIndex(meta)), 0, 3);
        };
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        GregTechAPI.causeMachineUpdate(world, x, y, z);
        if (isWireOrCable()) {
            GregTechAPI.causeCableUpdate(world, x, y, z);
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
        GregTechAPI.causeMachineUpdate(world, x, y, z);
        if (isWireOrCable()) {
            GregTechAPI.causeCableUpdate(world, x, y, z);
        }
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IGregTechTileEntity gtTE && gtTE.canAccessData()) {
            IMetaTileEntity mte = gtTE.getMetaTileEntity();
            mte.onBlockDestroyed();
            temporaryTileEntity.set(gtTE);
            for (int i = 0; i < gtTE.getSizeInventory(); i++) {
                final ItemStack stack = gtTE.getStackInSlot(i);
                if (stack != null && stack.stackSize > 0 && gtTE.isValidSlot(i) && mte.shouldDropItemAt(i)) {
                    final EntityItem itemEntity = new EntityItem(
                        world,
                        x + XSTR.XSTR_INSTANCE.nextFloat() * 0.8F + 0.1F,
                        y + XSTR.XSTR_INSTANCE.nextFloat() * 0.8F + 0.1F,
                        z + XSTR.XSTR_INSTANCE.nextFloat() * 0.8F + 0.1F,
                        new ItemStack(stack.getItem(), stack.stackSize, stack.getItemDamage()));
                    if (stack.hasTagCompound()) {
                        itemEntity.getEntityItem()
                            .setTagCompound(
                                (NBTTagCompound) stack.getTagCompound()
                                    .copy());
                    }
                    itemEntity.motionX = XSTR.XSTR_INSTANCE.nextGaussian() * 0.05D;
                    itemEntity.motionY = XSTR.XSTR_INSTANCE.nextGaussian() * 0.25D;
                    itemEntity.motionZ = XSTR.XSTR_INSTANCE.nextGaussian() * 0.05D;
                    world.spawnEntityInWorld(itemEntity);
                    stack.stackSize = 0;
                    gtTE.setInventorySlotContents(i, null);
                }
            }
        }
        super.breakBlock(world, x, y, z, block, metadata);
        world.removeTileEntity(x, y, z);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        final IGregTechTileEntity tempTe = temporaryTileEntity.get();
        final ArrayList<ItemStack> drops = new ArrayList<>();
        final Material material = MaterialLibAPI.getMaterialByIndex(metadata);
        if (material != null) {
            drops.add(getStack(material, 1));
        }
        if (tempTe != null) {
            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
                ItemStack cover = tempTe.getCoverItemAtSide(direction);
                if (cover != null) drops.add(cover);
            }
        }
        temporaryTileEntity.remove();
        return drops;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int ordinalSide,
        float offsetX, float offsetY, float offsetZ) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity == null) {
            return false;
        }

        final ItemStack currentItem = player.inventory.getCurrentItem();
        if (player.isSneaking()) {
            if (currentItem != null && !GTUtility.isStackInList(currentItem, GregTechAPI.sScrewdriverList)
                && !GTUtility.isStackInList(currentItem, GregTechAPI.sWrenchList)
                && !GTUtility.isStackInList(currentItem, GregTechAPI.sWireCutterList)
                && !GTUtility.isStackInList(currentItem, GregTechAPI.sSolderingToolList)
                && !GTUtility.isStackInList(currentItem, GregTechAPI.sJackhammerList)
                && !GTUtility.isStackInList(currentItem, GregTechAPI.sHardHammerList)
                && !GTUtility.isStackInList(currentItem, GregTechAPI.sCrowbarList)
                && !CoverRegistry.isCover(currentItem)) return false;
        }

        final ForgeDirection side = ForgeDirection.getOrientation(ordinalSide);
        if (currentItem != null && currentItem.getItem() instanceof MetaBaseItem mbItem) {
            if (mbItem.forEachBehavior(
                currentItem,
                behavior -> behavior.shouldInterruptBlockActivation(player, tileEntity, side))) {
                return false;
            }
        }

        if (tileEntity instanceof IGregTechTileEntity gtTE) {
            if (gtTE.getTimer() < 1L) {
                return false;
            }
            if (!world.isRemote && !gtTE.isUseableByPlayer(player)) {
                return true;
            }
            return gtTE.onRightclick(player, side, offsetX, offsetY, offsetZ);
        }
        return false;
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IGregTechTileEntity gtTE) {
            gtTE.onLeftclick(player);
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity collider) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IGregTechTileEntity gtTE && gtTE.canAccessData()) {
            gtTE.getMetaTileEntity()
                .onEntityCollidedWithBlock(world, x, y, z, collider);
            return;
        }
        super.onEntityCollidedWithBlock(world, x, y, z, collider);
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB inputAABB,
        List<AxisAlignedBB> outputAABB, Entity collider) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IGregTechTileEntity gtTE && gtTE.canAccessData()) {
            gtTE.getMetaTileEntity()
                .addCollisionBoxesToList(world, x, y, z, inputAABB, outputAABB, collider);
            return;
        }
        super.addCollisionBoxesToList(world, x, y, z, inputAABB, outputAABB, collider);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IGregTechTileEntity gtTE && gtTE.canAccessData()) {
            return gtTE.getMetaTileEntity()
                .getCollisionBoundingBoxFromPool(world, x, y, z);
        }
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IGregTechTileEntity gtTE && gtTE.canAccessData()) {
            return gtTE.getMetaTileEntity()
                .getCollisionBoundingBoxFromPool(world, x, y, z);
        }
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
        final TileEntity tileEntity = blockAccess.getTileEntity(x, y, z);
        if (tileEntity instanceof IGregTechTileEntity gtTE && gtTE.canAccessData()) {
            final AxisAlignedBB bbb = gtTE.getMetaTileEntity()
                .getCollisionBoundingBoxFromPool(gtTE.getWorld(), 0, 0, 0);
            minX = bbb.minX;
            minY = bbb.minY;
            minZ = bbb.minZ;
            maxX = bbb.maxX;
            maxY = bbb.maxY;
            maxZ = bbb.maxZ;
            return;
        }
        super.setBlockBoundsBasedOnState(blockAccess, x, y, z);
    }

    @Override
    public void setBlockBoundsForItemRender() {
        setBlockBounds(0, 0, 0, 1, 1, 1);
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity == null) return false;
        if (tileEntity instanceof BaseMetaPipeEntity pipe && (pipe.mConnections & 0xFFFFFFC0) != 0) {
            return true;
        }
        return tileEntity instanceof ICoverable coverable && coverable.hasCoverAtSide(side);
    }

    @Override
    public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
        // Guards against StackOverflowErrors on chunk load; see BlockMachines#onNeighborChange.
        if (checkingAdjacent) return;
        checkingAdjacent = true;
        if (world.getTileEntity(x, y, z) instanceof BaseTileEntity base) {
            base.onAdjacentBlockChange(tileX, tileY, tileZ);
        }
        checkingAdjacent = false;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof BaseMetaPipeEntity pipe) {
            pipe.onNeighborBlockChange(x, y, z);
        }
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int ordinalSide) {
        ForgeDirection forgeSide = switch (ordinalSide) {
            case (-2) -> ForgeDirection.DOWN;
            case (-1) -> ForgeDirection.UP;
            case (0) -> ForgeDirection.NORTH;
            case (2) -> ForgeDirection.SOUTH;
            case (3) -> ForgeDirection.WEST;
            case (1) -> ForgeDirection.EAST;
            default -> ForgeDirection.UNKNOWN;
        };
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity instanceof BaseMetaPipeEntity pipe && pipe.hasCoverAtSide(forgeSide);
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int ordinalSide) {
        if (ordinalSide < 0 || ordinalSide > 5) {
            return 0;
        }
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IGregTechTileEntity gtTE) {
            return gtTE.getOutputRedstoneSignal(
                ForgeDirection.getOrientation(ordinalSide)
                    .getOpposite());
        }
        return 0;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int ordinalSide) {
        if (ordinalSide < 0 || ordinalSide > 5) {
            return 0;
        }
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IGregTechTileEntity gtTE) {
            return gtTE.getStrongOutputRedstoneSignal(
                ForgeDirection.getOrientation(ordinalSide)
                    .getOpposite());
        }
        return 0;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int ordinalSide) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IGregTechTileEntity gtTE) {
            return gtTE.getComparatorValue(ForgeDirection.getOrientation(ordinalSide));
        }
        return 0;
    }

    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity instanceof BaseMetaTileEntity baseTE && baseTE.privateAccess()
            && !baseTE.playerOwnsThis(player, true) ? -1.0F
                : super.getPlayerRelativeBlockHardness(player, world, x, y, z);
    }

    @Override
    public int getLightOpacity(IBlockAccess world, int x, int y, int z) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IGregTechTileEntity gtTE) {
            return gtTE.getLightOpacity();
        }
        return super.getLightOpacity(world, x, y, z);
    }

    @Override
    public boolean onBlockEventReceived(World world, int x, int y, int z, int data1, int data2) {
        super.onBlockEventReceived(world, x, y, z, data1, data2);
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity != null && tileEntity.receiveClientEvent(data1, data2);
    }

    @Override
    public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return 0;
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return 0;
    }

    @Override
    public boolean isFireSource(World world, int x, int y, int z, ForgeDirection side) {
        return false;
    }

    @Override
    public boolean isFlammable(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return false;
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
        return false;
    }
}
