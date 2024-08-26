package gregtech.common.blocks;

import static gregtech.api.enums.GT_Values.W;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.BaseTileEntity;
import gregtech.api.metatileentity.CoverableTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Frame;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;
import gregtech.common.render.GT_Renderer_Block;

public class GT_Block_FrameBox extends BlockContainer {

    protected final String mUnlocalizedName;

    private static final String DOT_NAME = ".name";
    private static final String DOT_TOOLTIP = ".tooltip";

    // We need to keep around a temporary TE to preserve this TE after breaking the block, so we can
    // properly call getDrops() on it
    private static final ThreadLocal<IGregTechTileEntity> mTemporaryTileEntity = new ThreadLocal<>();

    public GT_Block_FrameBox() {
        super(new GT_Material_Machines());
        this.mUnlocalizedName = "gt.blockframes";
        setBlockName(this.mUnlocalizedName);
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + "." + W + ".name", "Any Sub Block of this one");

        GameRegistry.registerBlock(this, GT_Item_Frames.class, getUnlocalizedName());

        for (int meta = 1; meta < GregTech_API.sGeneratedMaterials.length; meta++) {
            Materials material = GregTech_API.sGeneratedMaterials[meta];
            if (material != null && (material.mTypes & 0x02) != 0) {
                GT_LanguageManager.addStringLocalization(
                    getUnlocalizedName() + "." + meta + DOT_NAME,
                    GT_LanguageManager.i18nPlaceholder ? getLocalizedNameFormat(material) : getLocalizedName(material));
                GT_LanguageManager
                    .addStringLocalization(getUnlocalizedName() + "." + meta + DOT_TOOLTIP, material.getToolTip());
            }
        }
    }

    public ItemStack getStackForm(int amount, int meta) {
        return new ItemStack(this, amount, meta);
    }

    public String getLocalizedNameFormat(Materials aMaterial) {
        return switch (aMaterial.mName) {
            case "InfusedAir", "InfusedDull", "InfusedEarth", "InfusedEntropy", "InfusedFire", "InfusedOrder", "InfusedVis", "InfusedWater" -> "%material Infused Stone";
            case "Vermiculite", "Bentonite", "Kaolinite", "Talc", "BasalticMineralSand", "GraniticMineralSand", "GlauconiteSand", "CassiteriteSand", "GarnetSand", "QuartzSand", "Pitchblende", "FullersEarth" -> "%material";
            default -> "%material" + " Frame Box";
        };
    }

    @Override
    public String getUnlocalizedName() {
        return mUnlocalizedName;
    }

    public String getLocalizedName(Materials aMaterial) {
        return aMaterial.getDefaultLocalizedNameForItem(getLocalizedNameFormat(aMaterial));
    }

    private boolean isCover(ItemStack item) {
        return GT_Utility.isStackInList(item, GregTech_API.sCovers.keySet());
    }

    private void createFrame(World worldIn, int x, int y, int z, BaseMetaPipeEntity baseMte) {
        // Obtain metadata to grab proper material identifier
        int meta = worldIn.getBlockMetadata(x, y, z);
        Materials material = getMaterial(meta);
        GT_MetaPipeEntity_Frame frame = new GT_MetaPipeEntity_Frame("GT_Frame_" + material, material);
        baseMte.setMetaTileEntity(frame);
        frame.setBaseMetaTileEntity(baseMte);
    }

    private BaseMetaPipeEntity spawnFrameEntity(World worldIn, int x, int y, int z) {
        // Spawn a TE frame box at this location, then apply the cover
        BaseMetaPipeEntity newTileEntity = new BaseMetaPipeEntity();
        createFrame(worldIn, x, y, z, newTileEntity);
        worldIn.setTileEntity(x, y, z, newTileEntity);
        return newTileEntity;
    }

    // Get the material that this frame box is made of
    public static Materials getMaterial(int meta) {
        return GregTech_API.sGeneratedMaterials[meta];
    }

    @Override
    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX,
        float subY, float subZ) {
        // Get ForgeDirection from side identifier.
        ForgeDirection direction = ForgeDirection.getOrientation(side);
        // If this block already holds a TE, just forward the call
        TileEntity te = worldIn.getTileEntity(x, y, z);
        if (te instanceof BaseMetaPipeEntity baseTileEntity) {
            // If this baseTileEntity has no MetaTileEntity associated with it, we need to create it
            // This happens on world load for some reason
            if (baseTileEntity.getMetaTileEntity() == null) {
                createFrame(worldIn, x, y, z, baseTileEntity);
            }
            return baseTileEntity.onRightclick(player, direction, subX, subY, subZ);
        }

        // If there was no TileEntity yet, we need to check if the player was holding a cover item and if so
        // spawn a new frame box to apply the cover to
        ItemStack item = player.getHeldItem();
        if (isCover(item)) {
            BaseMetaPipeEntity newTileEntity = spawnFrameEntity(worldIn, x, y, z);
            newTileEntity.onRightclick(player, direction, subX, subY, subZ);
            return true;
        }

        return false;
    }

    @Override
    public int getRenderType() {
        if (GT_Renderer_Block.INSTANCE == null) {
            return super.getRenderType();
        }
        return GT_Renderer_Block.mRenderID;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item aItem, CreativeTabs aTab, List<ItemStack> aList) {
        for (int i = 0; i < GregTech_API.sGeneratedMaterials.length; i++) {
            Materials tMaterial = GregTech_API.sGeneratedMaterials[i];
            // If material is not null and has a frame box item associated with it
            if ((tMaterial != null) && ((tMaterial.mTypes & 0x02) != 0)) {
                aList.add(new ItemStack(aItem, 1, i));
            }
        }
    }

    @Override
    public String getHarvestTool(int aMeta) {
        return "wrench";
    }

    @Override
    public int getHarvestLevel(int aMeta) {
        return aMeta % 4;
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess aWorld, int aX, int aY, int aZ, int ordinalSide) {
        ForgeDirection forgeSide = switch (ordinalSide) {
            case (-2) -> ForgeDirection.DOWN;
            case (-1) -> ForgeDirection.UP;
            case (0) -> ForgeDirection.NORTH;
            case (2) -> ForgeDirection.SOUTH;
            case (3) -> ForgeDirection.WEST;
            case (1) -> ForgeDirection.EAST;
            default -> ForgeDirection.UNKNOWN;
        };
        final TileEntity frameEntity = aWorld.getTileEntity(aX, aY, aZ);
        return frameEntity instanceof CoverableTileEntity cte && cte.getCoverInfoAtSide(forgeSide)
            .getCoverID() != 0;
    }

    @Override
    public int getDamageValue(World aWorld, int aX, int aY, int aZ) {
        return aWorld.getBlockMetadata(aX, aY, aZ);
    }

    @Override
    public void breakBlock(World aWorld, int aX, int aY, int aZ, Block aBlock, int aMetadata) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IGregTechTileEntity gtTE) {
            gtTE.onBlockDestroyed();
            mTemporaryTileEntity.set(gtTE);
        }
        // Cause structure update
        GregTech_API.causeMachineUpdate(aWorld, aX, aY, aZ);
        super.breakBlock(aWorld, aX, aY, aZ, aBlock, aMetadata);
    }

    @Override
    public void onBlockAdded(World worldIn, int x, int y, int z) {
        // Cause structure update
        GregTech_API.causeMachineUpdate(worldIn, x, y, z);
        super.onBlockAdded(worldIn, x, y, z);
    }

    @Override
    public void onEntityCollidedWithBlock(World aWorld, int aX, int aY, int aZ, Entity collider) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IGregTechTileEntity gtTE && gtTE.getMetaTileEntity() != null) {
            gtTE.onEntityCollidedWithBlock(aWorld, aX, aY, aZ, collider);
            return;
        }
        super.onEntityCollidedWithBlock(aWorld, aX, aY, aZ, collider);
    }

    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        return (tTileEntity instanceof BaseMetaTileEntity baseMTE) && baseMTE.privateAccess()
            && !baseMTE.playerOwnsThis(aPlayer, true) ? -1.0F
                : super.getPlayerRelativeBlockHardness(aPlayer, aWorld, aX, aY, aZ);
    }

    @Override
    public void onNeighborChange(IBlockAccess aWorld, int aX, int aY, int aZ, int aTileX, int aTileY, int aTileZ) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof BaseTileEntity baseTE) {
            baseTE.onAdjacentBlockChange(aTileX, aTileY, aTileZ);
        }
    }

    @Override
    public void onNeighborBlockChange(World aWorld, int aX, int aY, int aZ, Block aBlock) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof BaseMetaPipeEntity baseMetaPipe) {
            baseMetaPipe.onNeighborBlockChange(aX, aY, aZ);
        }
    }

    @Override
    public void addCollisionBoxesToList(World aWorld, int aX, int aY, int aZ, AxisAlignedBB inputAABB,
        List<AxisAlignedBB> outputAABB, Entity collider) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IGregTechTileEntity gtTE && gtTE.getMetaTileEntity() != null) {
            gtTE.addCollisionBoxesToList(aWorld, aX, aY, aZ, inputAABB, outputAABB, collider);
            return;
        }
        super.addCollisionBoxesToList(aWorld, aX, aY, aZ, inputAABB, outputAABB, collider);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IGregTechTileEntity gtTE && gtTE.getMetaTileEntity() != null) {
            return gtTE.getCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ);
        }
        return super.getCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IGregTechTileEntity gtTE && gtTE.getMetaTileEntity() != null) {
            return gtTE.getCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ);
        }
        return super.getSelectedBoundingBoxFromPool(aWorld, aX, aY, aZ);
    }

    @Override
    public boolean isSideSolid(IBlockAccess aWorld, int aX, int aY, int aZ, ForgeDirection side) {
        if (aWorld.getBlockMetadata(aX, aY, aZ) == 0) {
            return true;
        }
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof BaseMetaTileEntity) {
            return true;
        }
        if (tTileEntity instanceof BaseMetaPipeEntity baseMetaPipe && (baseMetaPipe.mConnections & 0xFFFFFFC0) != 0) {
            return true;
        }
        return (tTileEntity instanceof ICoverable coverable) && coverable.getCoverIDAtSide(side) != 0;
    }

    @Override // THIS
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int aX, int aY, int aZ) {
        final TileEntity tTileEntity = blockAccess.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IGregTechTileEntity gtTE && gtTE.getMetaTileEntity() != null) {
            final AxisAlignedBB bbb = gtTE.getCollisionBoundingBoxFromPool(gtTE.getWorld(), 0, 0, 0);
            minX = bbb.minX; // This essentially sets block bounds
            minY = bbb.minY;
            minZ = bbb.minZ;
            maxX = bbb.maxX;
            maxY = bbb.maxY;
            maxZ = bbb.maxZ;
            return;
        }
        super.setBlockBoundsBasedOnState(blockAccess, aX, aY, aZ);
    }

    @Override
    public void setBlockBoundsForItemRender() {
        super.setBlockBounds(0, 0, 0, 1, 1, 1);
    }

    @Override
    public int getFlammability(IBlockAccess aWorld, int aX, int aY, int aZ, ForgeDirection face) {
        return 0;
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess aWorld, int aX, int aY, int aZ, ForgeDirection face) {
        return 0;
    }

    @Override
    public boolean isFireSource(World aWorld, int aX, int aY, int aZ, ForgeDirection side) {
        return false;
    }

    @Override
    public boolean isFlammable(IBlockAccess aWorld, int aX, int aY, int aZ, ForgeDirection face) {
        return false;
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess aWorld, int aX, int aY, int aZ) {
        return false;
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess aWorld, int aX, int aY, int aZ) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockAccess aWorld, int aX, int aY, int aZ) {
        return false;
    }

    @Override
    public boolean hasTileEntity(int aMeta) {
        return true;
    }

    @Override
    public boolean onBlockEventReceived(World aWorld, int aX, int aY, int aZ, int aData1, int aData2) {
        super.onBlockEventReceived(aWorld, aX, aY, aZ, aData1, aData2);
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        return tTileEntity != null && tTileEntity.receiveClientEvent(aData1, aData2);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        // Find temporary TE if there was one
        final IGregTechTileEntity tempTe = mTemporaryTileEntity.get();
        ArrayList<ItemStack> drops = new ArrayList<>();
        drops.add(getStackForm(1, metadata));
        // If there is one, grab all attached covers and drop them
        if (tempTe != null) {
            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
                ItemStack cover = tempTe.getCoverItemAtSide(direction);
                if (cover != null) drops.add(cover);
            }
        }
        // Make sure to clear the temporary TE
        mTemporaryTileEntity.remove();
        return drops;
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess aWorld, int aX, int aY, int aZ, int ordinalSide) {
        if (ordinalSide < 0 || ordinalSide > 5) {
            return 0;
        }
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IGregTechTileEntity gtTE) {
            return gtTE.getOutputRedstoneSignal(
                ForgeDirection.getOrientation(ordinalSide)
                    .getOpposite());
        }
        return 0;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess aWorld, int aX, int aY, int aZ, int ordinalSide) {
        if (ordinalSide < 0 || ordinalSide > 5) {
            return 0;
        }
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IGregTechTileEntity gtTE) {
            return gtTE.getStrongOutputRedstoneSignal(
                ForgeDirection.getOrientation(ordinalSide)
                    .getOpposite());
        }
        return 0;
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        Materials material = GregTech_API.sGeneratedMaterials[meta];
        if (material == null) return null;
        return material.mIconSet.mTextures[OrePrefixes.frameGt.mTextureIndex].getIcon();
    }

    public ITexture[] getTexture(int meta) {
        Materials material = getMaterial(meta);
        if (material == null) return null;
        return new ITexture[] { TextureFactory.of(
            material.mIconSet.mTextures[OrePrefixes.frameGt.mTextureIndex],
            Dyes.getModulation(-1, material.mRGBa)) };
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }
}
