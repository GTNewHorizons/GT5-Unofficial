package gregtech.api.multitileentity;

import static gregtech.api.enums.GT_Values.ALL_VALID_SIDES;
import static gregtech.api.util.GT_Util.LAST_BROKEN_TILEENTITY;
import static gregtech.api.util.GT_Util.getTileEntity;
import static gregtech.api.util.GT_Util.setTileEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
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
import net.minecraft.item.ItemBlock;
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
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IDebugableBlock;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IDebugableTileEntity;
import gregtech.api.metatileentity.BaseTileEntity;
import gregtech.api.metatileentity.CoverableTileEntity;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity.IMTE_BreakBlock;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity.IMTE_GetBlockHardness;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity.IMTE_GetComparatorInputOverride;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity.IMTE_GetWeakChanges;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity.IMTE_HasMultiBlockMachineRelevantData;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity.IMTE_IsProvidingStrongPower;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity.IMTE_IsProvidingWeakPower;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity.IMTE_OnNeighborBlockChange;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity.IMTE_ShouldCheckWeakPower;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Util;
import gregtech.api.util.GT_Utility;
import gregtech.common.covers.CoverInfo;
import gregtech.common.render.GT_Renderer_Block;
import gregtech.common.render.IRenderedBlock;

/*
 * MultiTileEntityBlock ported from GT6
 */
@Optional.Interface(iface = "com.cricketcraft.chisel.api.IFacade", modid = "ChiselAPI")
public class MultiTileEntityBlock extends Block
    implements IDebugableBlock, ITileEntityProvider, IRenderedBlock, IFacade {

    protected static final Map<String, MultiTileEntityBlock> MULTI_BLOCK_MAP = new HashMap<>();
    private static boolean LOCK = false;

    protected final String mNameInternal, mTool;
    protected final int mHarvestLevelOffset, mHarvestLevelMinimum, mHarvestLevelMaximum;
    protected final boolean mOpaque, mNormalCube;

    public static String getName(String aMaterialName, SoundType aSoundType, String aTool, int aHarvestLevelOffset,
        int aHarvestLevelMinimum, int aHarvestLevelMaximum, boolean aOpaque, boolean aNormalCube) {
        return "gt.block.multiblock." + aMaterialName
            + "."
            + aSoundType.soundName
            + "."
            + aTool
            + "."
            + aHarvestLevelOffset
            + "."
            + aHarvestLevelMinimum
            + "."
            + aHarvestLevelMaximum
            + "."
            + aOpaque
            + "."
            + aNormalCube;
    }

    /**
     * @param aMaterialName        the Name of the vanilla Material Field. In case this is not a vanilla Material,
     *                             insert the Name you want to give your own Material instead.
     * @param aMaterial            the Material used to determine the Block.
     * @param aSoundType           the Sound Type of the Block.
     * @param aTool                the Tool used to harvest this Block.
     * @param aHarvestLevelOffset  obvious
     * @param aHarvestLevelMinimum obvious
     * @param aHarvestLevelMaximum obvious
     * @param aOpaque              if this Block is Opaque.
     * @param aNormalCube          if this Block is a normal Cube (for Redstone Stuff).
     */
    public static MultiTileEntityBlock getOrCreate(String aModID, String aMaterialName, Material aMaterial,
        SoundType aSoundType, String aTool, int aHarvestLevelOffset, int aHarvestLevelMinimum, int aHarvestLevelMaximum,
        boolean aOpaque, boolean aNormalCube) {
        final MultiTileEntityBlock rBlock = MULTI_BLOCK_MAP.get(
            aModID + ":"
                + getName(
                    aMaterialName,
                    aSoundType,
                    aTool = aTool.toLowerCase(),
                    aHarvestLevelOffset,
                    aHarvestLevelMinimum,
                    aHarvestLevelMaximum,
                    aOpaque,
                    aNormalCube));
        return rBlock == null
            ? new MultiTileEntityBlock(
                aModID,
                aMaterialName,
                aMaterial,
                aSoundType,
                aTool,
                aHarvestLevelOffset,
                aHarvestLevelMinimum,
                aHarvestLevelMaximum,
                aOpaque,
                aNormalCube)
            : rBlock;
    }

    protected MultiTileEntityBlock(String aModID, String aMaterialName, Material aMaterial, SoundType aSoundType,
        String aTool, int aHarvestLevelOffset, int aHarvestLevelMinimum, int aHarvestLevelMaximum, boolean aOpaque,
        boolean aNormalCube) {
        super(aMaterial);
        if (GregTech_API.sPreloadFinished)
            throw new IllegalStateException("Blocks can only be initialized within preInit!");

        mNameInternal = getName(
            aMaterialName,
            aSoundType,
            aTool,
            aHarvestLevelOffset,
            aHarvestLevelMinimum,
            aHarvestLevelMaximum,
            aOpaque,
            aNormalCube);
        GameRegistry.registerBlock(this, ItemBlock.class, mNameInternal);

        MULTI_BLOCK_MAP.put(aModID + ":" + mNameInternal, this);

        setStepSound(aSoundType);
        mOpaque = aOpaque;
        mNormalCube = aNormalCube;

        mTool = aTool.toLowerCase();
        mHarvestLevelOffset = aHarvestLevelOffset;
        mHarvestLevelMinimum = Math.max(0, aHarvestLevelMinimum);
        mHarvestLevelMaximum = Math.max(aHarvestLevelMinimum, aHarvestLevelMaximum);

        opaque = isOpaqueCube();
        lightOpacity = isOpaqueCube() ? 255 : 0;
    }

    @Override
    public final void breakBlock(World aWorld, int aX, int aY, int aZ, Block aBlock, int aMetaData) {
        final TileEntity aTileEntity = getTileEntity(aWorld, aX, aY, aZ, true);
        if (aTileEntity != null) LAST_BROKEN_TILEENTITY.set(aTileEntity);
        if (aTileEntity == null || !aTileEntity.shouldRefresh(this, aBlock, aMetaData, aMetaData, aWorld, aX, aY, aZ))
            return;
        if (aTileEntity instanceof IMTE_BreakBlock && ((IMTE_BreakBlock) aTileEntity).breakBlock()) return;
        if (aTileEntity instanceof IMTE_HasMultiBlockMachineRelevantData
            && ((IMTE_HasMultiBlockMachineRelevantData) aTileEntity).hasMultiBlockMachineRelevantData())
            GregTech_API.causeMachineUpdate(aWorld, aX, aY, aZ);

        aWorld.removeTileEntity(aX, aY, aZ);
    }

    @Override
    public ArrayList<String> getDebugInfo(EntityPlayer aPlayer, int aX, int aY, int aZ, int aLogLevel) {
        final TileEntity aTileEntity = aPlayer.worldObj.getTileEntity(aX, aY, aZ);
        if (aTileEntity instanceof IDebugableTileEntity) {
            return ((IDebugableTileEntity) aTileEntity).getDebugInfo(aPlayer, aLogLevel);
        }
        return (ArrayList<String>) Collections.<String>emptyList();
    }

    @Override
    public final boolean func_149730_j /* isFullBlock */() {
        return mOpaque;
    }

    @Override
    public final boolean isNormalCube() {
        return mNormalCube;
    }

    @Override
    public final boolean renderAsNormalBlock() {
        return mOpaque || mNormalCube;
    }

    @Override
    public int getRenderType() {
        return GT_Renderer_Block.INSTANCE == null ? super.getRenderType() : GT_Renderer_Block.INSTANCE.mRenderID;
    }

    @Override
    public final float getBlockHardness(World aWorld, int aX, int aY, int aZ) {
        final TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        return aTileEntity instanceof IMTE_GetBlockHardness ? ((IMTE_GetBlockHardness) aTileEntity).getBlockHardness()
            : 1.0F;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(IBlockAccess aIBlockAccess, int aX, int aY, int aZ, int aSide) {
        return Textures.BlockIcons.MACHINE_LV_SIDE.getIcon();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int aSide, int aMeta) {
        return Textures.BlockIcons.MACHINE_LV_SIDE.getIcon();
    }

    @Override
    @SuppressWarnings("unchecked")
    public final void addCollisionBoxesToList(World aWorld, int aX, int aY, int aZ, AxisAlignedBB aAABB,
        List<AxisAlignedBB> aList, Entity aEntity) {
        final TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (aTileEntity instanceof IMultiTileEntity)
            ((IMultiTileEntity) aTileEntity).addCollisionBoxesToList(aAABB, aList, aEntity);
        else super.addCollisionBoxesToList(aWorld, aX, aY, aZ, aAABB, aList, aEntity);
    }

    @Override
    public final AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
        final TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        return aTileEntity instanceof IMultiTileEntity
            ? ((IMultiTileEntity) aTileEntity).getCollisionBoundingBoxFromPool()
            : aTileEntity == null ? null : super.getCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ);
    }

    @Override
    public final AxisAlignedBB getSelectedBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
        final TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        return aTileEntity instanceof IMultiTileEntity
            ? ((IMultiTileEntity) aTileEntity).getSelectedBoundingBoxFromPool()
            : super.getSelectedBoundingBoxFromPool(aWorld, aX, aY, aZ);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int aX, int aY, int aZ) {
        final TileEntity aTileEntity = blockAccess.getTileEntity(aX, aY, aZ);
        if (aTileEntity instanceof IMultiTileEntity) {
            ((IMultiTileEntity) aTileEntity).setBlockBoundsBasedOnState(this);
            return;
        }
        super.setBlockBoundsBasedOnState(blockAccess, aX, aY, aZ);
    }

    @Override
    public final boolean isOpaqueCube() {
        return mOpaque;
    }

    @Override
    public final void onNeighborChange(IBlockAccess aWorld, int aX, int aY, int aZ, int aTileX, int aTileY,
        int aTileZ) {
        final TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (!LOCK) {
            LOCK = true;
            if (aTileEntity instanceof BaseTileEntity)
                ((BaseTileEntity) aTileEntity).onAdjacentBlockChange(aTileX, aTileY, aTileZ);
            LOCK = false;
        }
    }

    @Override
    public void onNeighborBlockChange(World aWorld, int aX, int aY, int aZ, Block aBlock) {
        final TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (!LOCK) {
            LOCK = true;
            if (aTileEntity instanceof BaseTileEntity) ((BaseTileEntity) aTileEntity).onAdjacentBlockChange(aX, aY, aZ);
            LOCK = false;
        }
        if (aTileEntity instanceof IMTE_OnNeighborBlockChange)
            ((IMTE_OnNeighborBlockChange) aTileEntity).onNeighborBlockChange(aWorld, aBlock);
        if (aTileEntity == null) aWorld.setBlockToAir(aX, aY, aZ);
    }

    @Override
    public final void onBlockAdded(World aWorld, int aX, int aY, int aZ) {
        final TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (aTileEntity instanceof IMultiTileEntity) ((IMultiTileEntity) aTileEntity).onBlockAdded();
    }

    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ) {
        final TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        return aTileEntity instanceof IMultiTileEntity && ((IMultiTileEntity) aTileEntity).privateAccess()
            && !((IMultiTileEntity) aTileEntity).playerOwnsThis(aPlayer, true) ? -1.0F
                : super.getPlayerRelativeBlockHardness(aPlayer, aWorld, aX, aY, aZ);
    }

    @Override
    public final void onBlockClicked(World aWorld, int aX, int aY, int aZ, EntityPlayer aPlayer) {
        final TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (aTileEntity instanceof IMultiTileEntity) ((IMultiTileEntity) aTileEntity).onLeftClick(aPlayer);
        else super.onBlockClicked(aWorld, aX, aY, aZ, aPlayer);
    }

    @Override
    public boolean onBlockActivated(World aWorld, int aX, int aY, int aZ, EntityPlayer aPlayer, int aSide, float aHitX,
        float aHitY, float aHitZ) {
        final TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (aPlayer != null && ItemList.TC_Thaumometer.isStackEqual(aPlayer.getHeldItem(), true, true)) return false;
        return aTileEntity instanceof IMultiTileEntity
            && ((IMultiTileEntity) aTileEntity).onBlockActivated(aPlayer, (byte) aSide, aHitX, aHitY, aHitZ);
    }

    @Override
    public final int isProvidingWeakPower(IBlockAccess aWorld, int aX, int aY, int aZ, int aSide) {
        final TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        return aTileEntity instanceof IMTE_IsProvidingWeakPower
            ? ((IMTE_IsProvidingWeakPower) aTileEntity).isProvidingWeakPower((byte) aSide)
            : super.isProvidingWeakPower(aWorld, aX, aY, aZ, aSide);
    }

    @Override
    public final int isProvidingStrongPower(IBlockAccess aWorld, int aX, int aY, int aZ, int aSide) {
        final TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        return aTileEntity instanceof IMTE_IsProvidingStrongPower
            ? ((IMTE_IsProvidingStrongPower) aTileEntity).isProvidingStrongPower((byte) aSide)
            : super.isProvidingStrongPower(aWorld, aX, aY, aZ, aSide);
    }

    @Override
    public final boolean shouldCheckWeakPower(IBlockAccess aWorld, int aX, int aY, int aZ, int aSide) {
        final TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        return aTileEntity instanceof IMTE_ShouldCheckWeakPower
            ? ((IMTE_ShouldCheckWeakPower) aTileEntity).shouldCheckWeakPower((byte) aSide)
            : isNormalCube(aWorld, aX, aY, aZ);
    }

    @Override
    public final boolean getWeakChanges(IBlockAccess aWorld, int aX, int aY, int aZ) {
        final TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        return aTileEntity instanceof IMTE_GetWeakChanges ? ((IMTE_GetWeakChanges) aTileEntity).getWeakChanges()
            : super.getWeakChanges(aWorld, aX, aY, aZ);
    }

    @Override
    public final void harvestBlock(World aWorld, EntityPlayer aPlayer, int aX, int aY, int aZ, int aMeta) {
        if (aPlayer == null) aPlayer = harvesters.get();
        aPlayer.addStat(StatList.mineBlockStatArray[getIdFromBlock(this)], 1);
        aPlayer.addExhaustion(0.025F);
        final boolean aSilkTouch = EnchantmentHelper.getSilkTouchModifier(aPlayer);
        final int aFortune = EnchantmentHelper.getFortuneModifier(aPlayer);
        float aChance = 1.0F;
        final TileEntity aTileEntity = getTileEntity(aWorld, aX, aY, aZ, true);
        if (aTileEntity instanceof IMultiTileEntity) {
            final ArrayList<ItemStack> tList = ((IMultiTileEntity) aTileEntity).getDrops(aFortune, aSilkTouch);
            aChance = ForgeEventFactory
                .fireBlockHarvesting(tList, aWorld, this, aX, aY, aZ, aMeta, aFortune, aChance, aSilkTouch, aPlayer);
            for (ItemStack tStack : tList)
                if (XSTR.XSTR_INSTANCE.nextFloat() <= aChance) dropBlockAsItem(aWorld, aX, aY, aZ, tStack);
        }
    }

    @Override
    public ITexture[] getTexture(Block aBlock, byte aSide, int aRenderPass, boolean[] aShouldSideBeRendered) {
        return null;
    }

    @Override
    public ITexture[] getTexture(Block aBlock, byte aSide, boolean isActive, int aRenderPass) {
        // TODO: MTE(Texture)
        return null;
    }

    @Override
    public int getRenderPasses(Block aBlock) {
        return 0;
    }

    @Override
    public boolean usesRenderPass(int aRenderPass) {
        return true;
    }

    @Override
    public boolean setBlockBounds(Block aBlock, int aRenderPass) {
        return false;
    }

    @Override
    public IRenderedBlock passRenderingToObject(ItemStack aStack) {
        return null;
    }

    @Override
    public IRenderedBlock passRenderingToObject(IBlockAccess aWorld, int aX, int aY, int aZ) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        return tTileEntity instanceof IRenderedBlock ? (IRenderedBlock) tTileEntity : null;
    }

    @Override
    public final boolean shouldSideBeRendered(IBlockAccess aWorld, int aX, int aY, int aZ, int aSide) {
        final TileEntity aTileEntity = aWorld.getTileEntity(
            aX - ForgeDirection.VALID_DIRECTIONS[aSide].offsetX,
            aY - ForgeDirection.VALID_DIRECTIONS[aSide].offsetY,
            aZ - ForgeDirection.VALID_DIRECTIONS[aSide].offsetZ);

        return aTileEntity instanceof IMultiTileEntity
            ? ((IMultiTileEntity) aTileEntity).shouldSideBeRendered((byte) aSide)
            : super.shouldSideBeRendered(aWorld, aX, aY, aZ, aSide);
    }

    @Override
    public Block getFacade(IBlockAccess aWorld, int aX, int aY, int aZ, int side) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof CoverableTileEntity tile) {
            final byte aSide = (byte) side;
            if (side != -1) {
                final Block facadeBlock = tile.getCoverInfoAtSide(aSide)
                    .getFacadeBlock();
                if (facadeBlock != null) return facadeBlock;
            } else {
                // we do not allow more than one type of facade per block, so no need to check every side
                // see comment in gregtech.common.covers.GT_Cover_FacadeBase.isCoverPlaceable
                for (byte tSide : ALL_VALID_SIDES) {
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
    public int getFacadeMetadata(IBlockAccess aWorld, int aX, int aY, int aZ, int side) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof CoverableTileEntity tile) {
            final byte aSide = (byte) side;
            if (side != -1) {
                final CoverInfo coverInfo = tile.getCoverInfoAtSide(aSide);
                final Block facadeBlock = coverInfo.getFacadeBlock();
                if (facadeBlock != null) return coverInfo.getFacadeMeta();
            } else {
                // we do not allow more than one type of facade per block, so no need to check every side
                // see comment in gregtech.common.covers.GT_Cover_FacadeBase.isCoverPlaceable
                for (byte tSide : ALL_VALID_SIDES) {
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

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }

    @Override
    public final boolean canProvidePower() {
        return !mNormalCube;
    }

    @Override
    public final String getLocalizedName() {
        return StatCollector.translateToLocal(mNameInternal + ".name");
    }

    @Override
    public final String getUnlocalizedName() {
        return mNameInternal;
    }

    @Override
    public final boolean onBlockEventReceived(World aWorld, int aX, int aY, int aZ, int aID, int aData) {
        final TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        return aTileEntity == null || aTileEntity.receiveClientEvent(aID, aData);
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
    public final int getComparatorInputOverride(World aWorld, int aX, int aY, int aZ, int aSide) {
        final TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        return aTileEntity instanceof IMTE_GetComparatorInputOverride
            ? ((IMTE_GetComparatorInputOverride) aTileEntity).getComparatorInputOverride((byte) aSide)
            : aTileEntity instanceof IMTE_IsProvidingWeakPower
                ? ((IMTE_IsProvidingWeakPower) aTileEntity).isProvidingWeakPower(GT_Utility.getOppositeSide(aSide))
                : super.getComparatorInputOverride(aWorld, aX, aY, aZ, aSide);
    }

    @Override
    public final void registerBlockIcons(IIconRegister aIconRegister) {
        /**/
    }

    @Override
    public final boolean isNormalCube(IBlockAccess aWorld, int aX, int aY, int aZ) {
        return mNormalCube;
    }

    @Override
    public final boolean isSideSolid(IBlockAccess aWorld, int aX, int aY, int aZ, ForgeDirection aSide) {
        final TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        return aTileEntity instanceof IMultiTileEntity
            ? ((IMultiTileEntity) aTileEntity)
                .isSideSolid((byte) (aSide != null ? aSide.ordinal() : GT_Values.SIDE_UNKNOWN))
            : mOpaque;
    }

    @Override
    public boolean removedByPlayer(World aWorld, EntityPlayer aPlayer, int aX, int aY, int aZ, boolean aWillHarvest) {
        final TileEntity aTileEntity = GT_Util.getTileEntity(aWorld, aX, aY, aZ, true);
        if (aTileEntity != null) LAST_BROKEN_TILEENTITY.set(aTileEntity);
        return super.removedByPlayer(aWorld, aPlayer, aX, aY, aZ, aWillHarvest);
    }

    @Override
    public int getFlammability(IBlockAccess aWorld, int aX, int aY, int aZ, ForgeDirection face) {
        return 0;
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess aWorld, int aX, int aY, int aZ, ForgeDirection face) {
        return GregTech_API.sMachineFlammable && (aWorld.getBlockMetadata(aX, aY, aZ) == 0) ? 100 : 0;
    }

    @Override
    public boolean hasTileEntity(int aMeta) {
        return true;
    }

    @Override
    public final ArrayList<ItemStack> getDrops(World aWorld, int aX, int aY, int aZ, int aUnusableMetaData,
        int aFortune) {
        final TileEntity aTileEntity = getTileEntity(aWorld, aX, aY, aZ, true);
        if (aTileEntity instanceof IMultiTileEntity) return ((IMultiTileEntity) aTileEntity).getDrops(aFortune, false);
        return new ArrayList<>();
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess aWorld, int aX, int aY, int aZ) {
        return false;
    }

    @Override
    public final float getExplosionResistance(Entity aExploder, World aWorld, int aX, int aY, int aZ,
        double aExplosionX, double aExplosionY, double aExplosionZ) {
        final TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        return aTileEntity instanceof IMultiTileEntity
            ? ((IMultiTileEntity) aTileEntity).getExplosionResistance(aExploder, aExplosionX, aExplosionY, aExplosionZ)
            : 1.0F;
    }

    @Override
    public final void onBlockExploded(World aWorld, int aX, int aY, int aZ, Explosion aExplosion) {
        if (aWorld.isRemote) return;
        final TileEntity aTileEntity = getTileEntity(aWorld, aX, aY, aZ, true);
        if (aTileEntity != null) LAST_BROKEN_TILEENTITY.set(aTileEntity);
        if (aTileEntity instanceof IMultiTileEntity) {
            GT_Log.exp.printf(
                "Explosion at : %d | %d | %d DIMID: %s due to near explosion!%n",
                aX,
                aY,
                aZ,
                aWorld.provider.dimensionId);
            ((IMultiTileEntity) aTileEntity).onExploded(aExplosion);
        } else aWorld.setBlockToAir(aX, aY, aZ);
    }

    @Override
    public final boolean canConnectRedstone(IBlockAccess aWorld, int aX, int aY, int aZ, int aSide) {
        return true;
    }

    @Override
    public final boolean recolourBlock(World aWorld, int aX, int aY, int aZ, ForgeDirection aSide, int aColor) {
        final TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        return aTileEntity instanceof IMultiTileEntity
            && ((IMultiTileEntity) aTileEntity).recolourBlock((byte) aSide.ordinal(), (byte) aColor);
    }

    @Override
    public final String getHarvestTool(int aMeta) {
        return mTool;
    }

    @Override
    public final int getHarvestLevel(int aMeta) {
        return Math.max(mHarvestLevelMinimum, Math.min(mHarvestLevelMaximum, mHarvestLevelOffset + aMeta));
    }

    @Override
    public final boolean isToolEffective(String aType, int aMeta) {
        return getHarvestTool(aMeta).equals(aType);
    }

    @Override
    public final ItemStack getPickBlock(MovingObjectPosition aTarget, World aWorld, int aX, int aY, int aZ,
        EntityPlayer aPlayer) {
        final TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        return aTileEntity instanceof IMultiTileEntity ? ((IMultiTileEntity) aTileEntity).getPickBlock(aTarget) : null;
    }

    @Override
    public final ItemStack getPickBlock(MovingObjectPosition aTarget, World aWorld, int aX, int aY, int aZ) {
        final TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        return aTileEntity instanceof IMultiTileEntity ? ((IMultiTileEntity) aTileEntity).getPickBlock(aTarget) : null;
    }

    public final IMultiTileEntity receiveMultiTileEntityData(IBlockAccess aWorld, int aX, short aY, int aZ, short aRID,
        short aID) {
        if (!(aWorld instanceof World)) return null;
        TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);

        if (!(aTileEntity instanceof IMultiTileEntity)
            || ((IMultiTileEntity) aTileEntity).getMultiTileEntityRegistryID() != aRID
            || ((IMultiTileEntity) aTileEntity).getMultiTileEntityID() != aID) {
            final MultiTileEntityRegistry tRegistry = MultiTileEntityRegistry.getRegistry(aRID);
            if (tRegistry == null) return null;

            aTileEntity = tRegistry.getNewTileEntity((World) aWorld, aX, aY, aZ, aID);
            if (!(aTileEntity instanceof IMultiTileEntity)) return null;

            setTileEntity((World) aWorld, aX, aY, aZ, aTileEntity, false);
        }
        return ((IMultiTileEntity) aTileEntity);
    }

    public void receiveCoverData(IMultiTileEntity mte, int aCover0, int aCover1, int aCover2, int aCover3, int aCover4,
        int aCover5) {
        boolean updated;
        updated = mte.setCoverIDAtSideNoUpdate((byte) 0, aCover0);
        updated |= mte.setCoverIDAtSideNoUpdate((byte) 1, aCover1);
        updated |= mte.setCoverIDAtSideNoUpdate((byte) 2, aCover2);
        updated |= mte.setCoverIDAtSideNoUpdate((byte) 3, aCover3);
        updated |= mte.setCoverIDAtSideNoUpdate((byte) 4, aCover4);
        updated |= mte.setCoverIDAtSideNoUpdate((byte) 5, aCover5);

        if (updated) {
            mte.issueBlockUpdate();
        }
    }
    //
    // te.receiveClientEvent(GregTechTileClientEvents.CHANGE_COMMON_DATA, aTextureData);
    //
    // te.receiveClientEvent(GregTechTileClientEvents.CHANGE_CUSTOM_DATA, aUpdateData & 0x7F);
    // te.receiveClientEvent(GregTechTileClientEvents.CHANGE_CUSTOM_DATA, aTexturePage | 0x80);
    //
    // te.receiveClientEvent(GregTechTileClientEvents.CHANGE_COLOR, aColorData);
    // te.receiveClientEvent(GregTechTileClientEvents.CHANGE_REDSTONE_OUTPUT, aRedstoneData);

    @Override
    public final TileEntity createTileEntity(World aWorld, int aMeta) {
        return null;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return null;
    }
}
