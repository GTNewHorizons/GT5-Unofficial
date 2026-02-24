package gregtech.common.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
import gregtech.api.GregTechAPI;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IBlockWithTextures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.BaseTileEntity;
import gregtech.api.metatileentity.CoverableTileEntity;
import gregtech.api.metatileentity.implementations.MTEFrame;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTLanguageManager;
import gregtech.common.render.GTRendererBlock;

public class BlockFrameBox extends BlockContainer implements IBlockWithTextures {

    protected final String mUnlocalizedName;

    // We need to keep around a temporary TE to preserve this TE after breaking the block, so we can
    // properly call getDrops() on it
    private static final ThreadLocal<IGregTechTileEntity> mTemporaryTileEntity = new ThreadLocal<>();

    // Texture[meta][side][layer]
    private ITexture[][][] textures = new ITexture[1000][][];

    public BlockFrameBox() {
        super(new MaterialMachines());
        this.mUnlocalizedName = "gt.blockframes";
        setBlockName(this.mUnlocalizedName);
        GTLanguageManager.addAnySubBlockLocalization(getUnlocalizedName());

        GameRegistry.registerBlock(this, ItemFrames.class, getUnlocalizedName());

        for (int meta = 1; meta < GregTechAPI.sGeneratedMaterials.length; meta++) {
            Materials material = GregTechAPI.sGeneratedMaterials[meta];
            if (material != null && material.hasMetalItems()) {

                ITexture[] texture = { TextureFactory.of(
                    material.mIconSet.mTextures[OrePrefixes.frameGt.getTextureIndex()],
                    Dyes.getModulation(-1, material.mRGBa)) };

                textures[meta] = new ITexture[][] { texture, texture, texture, texture, texture, texture };
            }
        }
        GregTechAPI.registerMachineBlock(this, -1);
    }

    public ItemStack getStackForm(int amount, int meta) {
        return new ItemStack(this, amount, meta);
    }

    public static String getLocalizedNameFormat(Materials aMaterial) {
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

    public static String getLocalizedName(Materials materials) {
        return OrePrefixes.getLocalizedNameForItem(getLocalizedNameFormat(materials), materials.getLocalizedName());
    }

    public static String getLocalizedName(int meta) {
        return getLocalizedName(getMaterial(meta));
    }

    private void createFrame(World worldIn, int x, int y, int z, BaseMetaPipeEntity baseMte) {
        // Obtain metadata to grab proper material identifier
        int meta = worldIn.getBlockMetadata(x, y, z);
        Materials material = getMaterial(meta);
        MTEFrame frame = new MTEFrame("GT_Frame_" + material, material);
        baseMte.setMetaTileEntity(frame);
        baseMte.setInitialValuesAsNBT(null, (short) (4096 + meta)); // 4096 is found in LoaderMetaTileEntities for
                                                                    // frames
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
        return GregTechAPI.sGeneratedMaterials[meta];
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
        if (CoverRegistry.isCover(item)) {
            BaseMetaPipeEntity newTileEntity = spawnFrameEntity(worldIn, x, y, z);
            return newTileEntity.onRightclick(player, direction, subX, subY, subZ);
        }

        return false;
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

    /**
     * {@inheritDoc}
     *
     * @implNote Can render in both passes: cut-out and alpha-blended.<br>
     *           Final choice on {@link ITexture} or {@link IIconContainer}.
     */
    @Override
    public boolean canRenderInPass(int pass) {
        return pass == 0 || pass == 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item aItem, CreativeTabs aTab, List<ItemStack> aList) {
        for (int i = 0; i < GregTechAPI.sGeneratedMaterials.length; i++) {
            Materials tMaterial = GregTechAPI.sGeneratedMaterials[i];
            // If material is not null and has a frame box item associated with it
            if (tMaterial != null && tMaterial.hasMetalItems()) {
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
        return 3;
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
        return frameEntity instanceof CoverableTileEntity cte && cte.hasCoverAtSide(forgeSide);
    }

    @Override
    public int getDamageValue(World aWorld, int aX, int aY, int aZ) {
        return aWorld.getBlockMetadata(aX, aY, aZ);
    }

    @Override
    public void breakBlock(World aWorld, int aX, int aY, int aZ, Block aBlock, int aMetadata) {
        if (aWorld.isRemote) return;

        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IGregTechTileEntity gtTE && gtTE.canAccessData()) {
            IMetaTileEntity mte = gtTE.getMetaTileEntity();
            mte.onBlockDestroyed();
            mTemporaryTileEntity.set(gtTE);
        }
        // Cause structure update
        GregTechAPI.causeMachineUpdate(aWorld, aX, aY, aZ);
        super.breakBlock(aWorld, aX, aY, aZ, aBlock, aMetadata);
    }

    @Override
    public void onBlockAdded(World worldIn, int x, int y, int z) {
        // Cause structure update
        GregTechAPI.causeMachineUpdate(worldIn, x, y, z);
        super.onBlockAdded(worldIn, x, y, z);
    }

    @Override
    public void onEntityCollidedWithBlock(World aWorld, int aX, int aY, int aZ, Entity collider) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IGregTechTileEntity gtTE && gtTE.canAccessData()) {
            gtTE.getMetaTileEntity()
                .onEntityCollidedWithBlock(aWorld, aX, aY, aZ, collider);
            return;
        }
        super.onEntityCollidedWithBlock(aWorld, aX, aY, aZ, collider);
    }

    @Override
    public float getBlockHardness(World aWorld, int aX, int aY, int aZ) {
        return Blocks.iron_block.getBlockHardness(aWorld, aX, aY, aZ);
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
        if (tTileEntity instanceof IGregTechTileEntity gtTE && gtTE.canAccessData()) {
            gtTE.getMetaTileEntity()
                .addCollisionBoxesToList(aWorld, aX, aY, aZ, inputAABB, outputAABB, collider);
            return;
        }
        super.addCollisionBoxesToList(aWorld, aX, aY, aZ, inputAABB, outputAABB, collider);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IGregTechTileEntity gtTE && gtTE.canAccessData()) {
            return gtTE.getMetaTileEntity()
                .getCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ);
        }
        return super.getCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IGregTechTileEntity gtTE && gtTE.canAccessData()) {
            return gtTE.getMetaTileEntity()
                .getCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ);
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
        return (tTileEntity instanceof ICoverable coverable) && coverable.hasCoverAtSide(side);
    }

    @Override // THIS
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int aX, int aY, int aZ) {
        final TileEntity tTileEntity = blockAccess.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IGregTechTileEntity gtTE && gtTE.canAccessData()) {
            final AxisAlignedBB bbb = gtTE.getMetaTileEntity()
                .getCollisionBoundingBoxFromPool(gtTE.getWorld(), 0, 0, 0);
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
        Materials material = GregTechAPI.sGeneratedMaterials[meta];
        if (material == null) return null;
        return material.mIconSet.mTextures[OrePrefixes.frameGt.getTextureIndex()].getIcon();
    }

    @Override
    public ITexture[][] getTextures(IBlockAccess world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);

        if (te instanceof BaseMetaPipeEntity bmpe) {
            ITexture[][] textures = new ITexture[6][];

            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                textures[dir.ordinal()] = bmpe.getTexture(this, dir);
            }

            return textures;
        }

        return getTextures(world.getBlockMetadata(x, y, z));
    }

    @Override
    public ITexture[][] getTextures(int meta) {
        return meta < 0 || meta >= 1000 ? null : textures[meta];
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }
}
