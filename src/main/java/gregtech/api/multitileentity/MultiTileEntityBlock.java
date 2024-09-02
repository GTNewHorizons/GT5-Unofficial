package gregtech.api.multitileentity;

import static gregtech.api.enums.GTValues.OFFX;
import static gregtech.api.enums.GTValues.OFFY;
import static gregtech.api.enums.GTValues.OFFZ;
import static gregtech.api.util.GTUtil.LAST_BROKEN_TILEENTITY;
import static gregtech.api.util.GTUtil.getTileEntity;
import static gregtech.api.util.GTUtil.setTileEntity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;

import com.cricketcraft.chisel.api.IFacade;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IDebugableBlock;
import gregtech.api.interfaces.tileentity.IDebugableTileEntity;
import gregtech.api.metatileentity.BaseTileEntity;
import gregtech.api.metatileentity.CoverableTileEntity;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTUtil;
import gregtech.common.covers.CoverInfo;
import gregtech.common.render.MultiTileRenderer;

/*
 * MultiTileEntityBlock ported from GT6
 */
@Optional.Interface(iface = "com.cricketcraft.chisel.api.IFacade", modid = "ChiselAPI")
public class MultiTileEntityBlock extends BlockContainer implements IDebugableBlock, ITileEntityProvider, IFacade {

    private MultiTileEntityRegistry registry;

    private static boolean LOCK = false;

    private boolean registered = false;
    private boolean normalCube;
    protected String internalName, toolName, materialName, modID;

    public String getName() {
        return String.join(
            ".",
            "gt.block.multiblock",
            materialName,
            stepSound.soundName,
            toolName,
            Boolean.toString(opaque),
            Boolean.toString(normalCube));
    }

    public MultiTileEntityBlock(Material material) {
        super(material);
        if (GregTechAPI.sPreloadFinished)
            throw new IllegalStateException("Blocks can only be initialized within preInit!");
    }

    public MultiTileEntityBlock tool(String toolName) {
        this.toolName = toolName.toLowerCase();
        return this;
    }

    public MultiTileEntityBlock sound(SoundType soundtype) {
        setStepSound(soundtype);
        return this;
    }

    public MultiTileEntityBlock opaque(Boolean opaque) {
        this.opaque = opaque;
        return this;
    }

    public MultiTileEntityBlock normalCube(Boolean normalCube) {
        this.normalCube = normalCube;
        return this;
    }

    public MultiTileEntityBlock modID(String modID) {
        this.modID = modID;
        return this;
    }

    public MultiTileEntityBlock materialName(String materialName) {
        this.materialName = materialName;
        return this;
    }

    public MultiTileEntityBlock register() {
        if (registered) throw new IllegalStateException("Block already registered " + internalName);
        if (GregTechAPI.sPreloadFinished)
            throw new IllegalStateException("Blocks can only be initialized within preInit!");

        registered = true;
        internalName = getName();
        lightOpacity = isOpaqueCube() ? 255 : 0;

        GameRegistry.registerBlock(this, MultiTileEntityItem.class, internalName);
        return this;
    }

    @Override
    public final void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
        final TileEntity tileEntity = getTileEntity(world, x, y, z, true);
        if (tileEntity != null) LAST_BROKEN_TILEENTITY.set(tileEntity);
        if (tileEntity == null || !tileEntity.shouldRefresh(this, block, metadata, metadata, world, x, y, z)) return;
        if (tileEntity instanceof IMultiTileEntity mute) mute.onBlockBroken();
        // spotless:off
        // if (aTileEntity instanceof IMTE_HasMultiBlockMachineRelevantData
        //     && ((IMTE_HasMultiBlockMachineRelevantData) aTileEntity).hasMultiBlockMachineRelevantData())
        //     GregTechAPI.causeMachineUpdate(world, x, y, z);
        // spotless:on

        world.removeTileEntity(x, y, z);
        super.breakBlock(world, x, y, z, block, metadata);

    }

    @Override
    public ArrayList<String> getDebugInfo(EntityPlayer aPlayer, int x, int y, int z, int aLogLevel) {
        final TileEntity tileEntity = aPlayer.worldObj.getTileEntity(x, y, z);
        if (tileEntity instanceof IDebugableTileEntity mte) {
            return mte.getDebugInfo(aPlayer, aLogLevel);
        }
        return new ArrayList<>();
    }

    @Override
    public int getRenderType() {
        return MultiTileRenderer.INSTANCE == null ? super.getRenderType() : MultiTileRenderer.INSTANCE.getRenderId();
    }

    @Override
    public final float getBlockHardness(World world, int x, int y, int z) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity instanceof IMultiTileEntity ? ((IMultiTileEntity) tileEntity).getBlockHardness() : 1.0F;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(IBlockAccess aIBlockAccess, int x, int y, int z, int ordinalSide) {
        return Textures.BlockIcons.MACHINE_LV_SIDE.getIcon();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int ordinalSide, int meta) {
        return Textures.BlockIcons.MACHINE_LV_SIDE.getIcon();
    }

    @Override
    @SuppressWarnings("unchecked")
    public final void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aAABB,
        List<AxisAlignedBB> aList, Entity aEntity) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IMultiTileEntity)
            ((IMultiTileEntity) tileEntity).addCollisionBoxesToList(aAABB, aList, aEntity);
        else super.addCollisionBoxesToList(world, x, y, z, aAABB, aList, aEntity);
    }

    @Override
    public final AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity instanceof IMultiTileEntity mte ? mte.getCollisionBoundingBoxFromPool()
            : tileEntity == null ? null : super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public final AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity instanceof IMultiTileEntity mte ? mte.getSelectedBoundingBoxFromPool()
            : super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
        final TileEntity tileEntity = blockAccess.getTileEntity(x, y, z);
        if (tileEntity instanceof IMultiTileEntity mte) {
            mte.setBlockBoundsBasedOnState(this);
            return;
        }
        super.setBlockBoundsBasedOnState(blockAccess, x, y, z);
    }

    @Override
    public final boolean isOpaqueCube() {
        return opaque;
    }

    @Override
    public final void onNeighborChange(IBlockAccess world, int x, int y, int z, int aTileX, int aTileY, int aTileZ) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (!LOCK) {
            LOCK = true;
            if (tileEntity instanceof BaseTileEntity)
                ((BaseTileEntity) tileEntity).onAdjacentBlockChange(aTileX, aTileY, aTileZ);
            LOCK = false;
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (!LOCK) {
            LOCK = true;
            if (tileEntity instanceof BaseTileEntity bte) bte.onAdjacentBlockChange(x, y, z);
            LOCK = false;
        }
        if (tileEntity instanceof IMultiTileEntity change) change.onNeighborBlockChange(world, block);

        if (tileEntity == null) world.setBlockToAir(x, y, z);
    }

    @Override
    public final void onBlockAdded(World world, int x, int y, int z) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IMultiTileEntity mte) mte.onBlockAdded();
    }

    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer aPlayer, World world, int x, int y, int z) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity instanceof IMultiTileEntity mte && mte.privateAccess()
            && !((IMultiTileEntity) tileEntity).playerOwnsThis(aPlayer, true) ? -1.0F
                : super.getPlayerRelativeBlockHardness(aPlayer, world, x, y, z);
    }

    @Override
    public final void onBlockClicked(World world, int x, int y, int z, EntityPlayer aPlayer) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IMultiTileEntity mte) mte.onLeftClick(aPlayer);
        else super.onBlockClicked(world, x, y, z, aPlayer);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer aPlayer, int ordinalSide,
        float aHitX, float aHitY, float aHitZ) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (aPlayer != null && ItemList.TC_Thaumometer.isStackEqual(aPlayer.getHeldItem(), true, true)) return false;
        return tileEntity instanceof IMultiTileEntity mte
            && mte.onBlockActivated(aPlayer, ForgeDirection.getOrientation(ordinalSide), aHitX, aHitY, aHitZ);
    }

    @Override
    public final int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int ordinalSide) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity instanceof IMultiTileEntity power
            ? power.isProvidingWeakPower(ForgeDirection.getOrientation(ordinalSide))
            : super.isProvidingWeakPower(world, x, y, z, ordinalSide);
    }

    @Override
    public final int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int ordinalSide) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity instanceof IMultiTileEntity power
            ? power.isProvidingStrongPower(ForgeDirection.getOrientation(ordinalSide))
            : super.isProvidingStrongPower(world, x, y, z, ordinalSide);
    }

    @Override
    public final boolean shouldCheckWeakPower(IBlockAccess world, int x, int y, int z, int ordinalSide) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity instanceof IMultiTileEntity power
            ? power.shouldCheckWeakPower(ForgeDirection.getOrientation(ordinalSide))
            : isNormalCube(world, x, y, z);
    }

    @Override
    public final boolean getWeakChanges(IBlockAccess world, int x, int y, int z) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity instanceof IMultiTileEntity changes ? changes.getWeakChanges()
            : super.getWeakChanges(world, x, y, z);
    }

    @Override
    public final void harvestBlock(World world, EntityPlayer aPlayer, int x, int y, int z, int meta) {
        if (aPlayer == null) aPlayer = harvesters.get();
        aPlayer.addStat(StatList.mineBlockStatArray[getIdFromBlock(this)], 1);
        aPlayer.addExhaustion(0.025F);
        final boolean aSilkTouch = EnchantmentHelper.getSilkTouchModifier(aPlayer);
        final int aFortune = EnchantmentHelper.getFortuneModifier(aPlayer);
        float aChance = 1.0F;
        final TileEntity tileEntity = getTileEntity(world, x, y, z, true);

        if (!(tileEntity instanceof IMultiTileEntity mte)) {
            return;
        }

        final ArrayList<ItemStack> tList = mte.getDrops(aFortune, aSilkTouch);
        aChance = ForgeEventFactory
            .fireBlockHarvesting(tList, world, this, x, y, z, meta, aFortune, aChance, aSilkTouch, aPlayer);
        for (final ItemStack tStack : tList)
            if (XSTR.XSTR_INSTANCE.nextFloat() <= aChance) dropBlockAsItem(world, x, y, z, tStack);

    }

    @Override
    public final boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int ordinalSide) {
        final TileEntity tileEntity = world
            .getTileEntity(x - OFFX[ordinalSide], y - OFFY[ordinalSide], z - OFFZ[ordinalSide]);
        return tileEntity instanceof IMultiTileEntity mte
            ? mte.shouldSideBeRendered(ForgeDirection.getOrientation(ordinalSide))
            : super.shouldSideBeRendered(world, x, y, z, ordinalSide);
    }

    @Override
    public Block getFacade(IBlockAccess world, int x, int y, int z, int ordinalSide) {
        final TileEntity tTileEntity = world.getTileEntity(x, y, z);
        if (tTileEntity instanceof CoverableTileEntity tile) {
            final ForgeDirection side = ForgeDirection.getOrientation(ordinalSide);
            if (ordinalSide != -1) {
                final Block facadeBlock = tile.getCoverInfoAtSide(side)
                    .getFacadeBlock();
                if (facadeBlock != null) return facadeBlock;
            } else {
                // we do not allow more than one type of facade per block, so no need to check every side
                // see comment in gregtech.common.covers.GT_Cover_FacadeBase.isCoverPlaceable
                for (final ForgeDirection tSide : ForgeDirection.VALID_DIRECTIONS) {
                    final Block facadeBlock = tile.getCoverInfoAtSide(tSide)
                        .getFacadeBlock();
                    if (facadeBlock != null) {
                        return facadeBlock;
                    }
                }
            }
        }
        return Blocks.air;
    }

    @Override
    public int getFacadeMetadata(IBlockAccess world, int x, int y, int z, int ordinalSide) {
        final TileEntity tTileEntity = world.getTileEntity(x, y, z);
        if (tTileEntity instanceof CoverableTileEntity tile) {
            final ForgeDirection side = ForgeDirection.getOrientation(ordinalSide);
            if (ordinalSide != -1) {
                final CoverInfo coverInfo = tile.getCoverInfoAtSide(side);
                final Block facadeBlock = coverInfo.getFacadeBlock();
                if (facadeBlock != null) return coverInfo.getFacadeMeta();
            } else {
                // we do not allow more than one type of facade per block, so no need to check every side
                // see comment in gregtech.common.covers.GT_Cover_FacadeBase.isCoverPlaceable
                for (final ForgeDirection tSide : ForgeDirection.VALID_DIRECTIONS) {
                    final CoverInfo coverInfo = tile.getCoverInfoAtSide(tSide);
                    final Block facadeBlock = coverInfo.getFacadeBlock();
                    if (facadeBlock != null) {
                        return coverInfo.getFacadeMeta();
                    }
                }
            }
        }
        return 0;
    }

    public MultiTileEntityRegistry getRegistry() {
        return registry;
    }

    public void setRegistry(MultiTileEntityRegistry registry) {
        this.registry = registry;
    }

    public boolean isRegistered() {
        return registered;
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }

    @Override
    public final String getLocalizedName() {
        return StatCollector.translateToLocal(internalName + ".name");
    }

    @Override
    public final String getUnlocalizedName() {
        return internalName;
    }

    @Override
    public final boolean onBlockEventReceived(World world, int x, int y, int z, int aID, int aData) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity == null || tileEntity.receiveClientEvent(aID, aData);
    }

    @Override
    public final void getSubBlocks(Item aItem, CreativeTabs aCreativeTab, List<ItemStack> aList) {
        /**/
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public final int getComparatorInputOverride(World world, int x, int y, int z, int ordinalSide) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IMultiTileEntity mute) {
            if (mute.hasComparatorInputOverride())
                return mute.getComparatorInputOverride(ForgeDirection.getOrientation(ordinalSide));
            else return mute.isProvidingWeakPower(
                ForgeDirection.getOrientation(ordinalSide)
                    .getOpposite());
        }

        return super.getComparatorInputOverride(world, x, y, z, ordinalSide);
    }

    @Override
    public final void registerBlockIcons(IIconRegister aIconRegister) {
        /* Do Nothing */
    }

    @Override
    public final boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity instanceof IMultiTileEntity mute ? mute.isSideSolid(side) : opaque;
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer aPlayer, int x, int y, int z, boolean aWillHarvest) {
        final TileEntity tileEntity = GTUtil.getTileEntity(world, x, y, z, true);
        if (tileEntity != null) LAST_BROKEN_TILEENTITY.set(tileEntity);
        return super.removedByPlayer(world, aPlayer, x, y, z, aWillHarvest);
    }

    @Override
    public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return 0;
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return GregTechAPI.sMachineFlammable && (world.getBlockMetadata(x, y, z) == 0) ? 100 : 0;
    }

    @Override
    public boolean hasTileEntity(int meta) {
        return true;
    }

    @Override
    public final ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int aUnusableMetaData, int aFortune) {
        final TileEntity tileEntity = getTileEntity(world, x, y, z, true);
        if (tileEntity instanceof IMultiTileEntity mute) return mute.getDrops(aFortune, false);
        return new ArrayList<>();
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public final float getExplosionResistance(Entity aExploder, World world, int x, int y, int z, double aExplosionX,
        double aExplosionY, double aExplosionZ) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity instanceof IMultiTileEntity mute
            ? mute.getExplosionResistance(aExploder, aExplosionX, aExplosionY, aExplosionZ)
            : 1.0F;
    }

    @Override
    public final void onBlockExploded(World world, int x, int y, int z, Explosion aExplosion) {
        if (world.isRemote) return;
        final TileEntity tileEntity = getTileEntity(world, x, y, z, true);
        if (tileEntity != null) LAST_BROKEN_TILEENTITY.set(tileEntity);
        if (tileEntity instanceof IMultiTileEntity mute) {
            GTLog.exp.printf(
                "Explosion at : %d | %d | %d DIMID: %s due to near explosion!%n",
                x,
                y,
                z,
                world.provider.dimensionId);
            mute.onExploded(aExplosion);
        } else world.setBlockToAir(x, y, z);
    }

    @Override
    public final boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int ordinalSide) {
        return true;
    }

    @Override
    public final boolean recolourBlock(World world, int x, int y, int z, ForgeDirection side, int color) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity instanceof IMultiTileEntity mute && mute.recolourBlock(side, (byte) color);
    }

    @Override
    public final String getHarvestTool(int meta) {
        return toolName;
    }

    @Override
    public int getHarvestLevel(int meta) {
        return meta % 4;
    }

    @Override
    public final boolean isToolEffective(String toolType, int meta) {
        return getHarvestTool(meta).equals(toolType);
    }

    @Override
    public final ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z,
        EntityPlayer aPlayer) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity instanceof IMultiTileEntity mte ? mte.getPickBlock(target) : null;
    }

    @Override
    public final ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity instanceof IMultiTileEntity mte ? mte.getPickBlock(target) : null;
    }

    @Nullable
    public final IMultiTileEntity receiveMultiTileEntityData(@Nonnull IBlockAccess world, int x, int y, int z,
        int registryId, int aID) {
        if (!(world instanceof World)) return null;
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (!(tileEntity instanceof IMultiTileEntity mute) || mute.getMultiTileEntityRegistryID() != registryId
            || mute.getMultiTileEntityID() != aID) {
            final MultiTileEntityRegistry tRegistry = MultiTileEntityRegistry.getRegistry(registryId);
            if (tRegistry == null) return null;

            tileEntity = tRegistry.getNewTileEntity((World) world, x, y, z, aID);
            if (!(tileEntity instanceof IMultiTileEntity)) return null;

            setTileEntity((World) world, x, y, z, tileEntity, false);
        }
        return (IMultiTileEntity) tileEntity;
    }

    public void receiveCoverData(IMultiTileEntity mute, int aCover0, int aCover1, int aCover2, int aCover3, int aCover4,
        int aCover5) {
        boolean updated;
        updated = mute.setCoverIDAtSideNoUpdate(ForgeDirection.DOWN, aCover0);
        updated |= mute.setCoverIDAtSideNoUpdate(ForgeDirection.UP, aCover1);
        updated |= mute.setCoverIDAtSideNoUpdate(ForgeDirection.NORTH, aCover2);
        updated |= mute.setCoverIDAtSideNoUpdate(ForgeDirection.SOUTH, aCover3);
        updated |= mute.setCoverIDAtSideNoUpdate(ForgeDirection.WEST, aCover4);
        updated |= mute.setCoverIDAtSideNoUpdate(ForgeDirection.EAST, aCover5);

        if (updated) {
            mute.issueBlockUpdate();
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return registry.getNewTileEntity(meta);
    }
}
