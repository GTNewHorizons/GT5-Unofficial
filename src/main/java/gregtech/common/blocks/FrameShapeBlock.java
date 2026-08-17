package gregtech.common.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
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
import gregtech.api.enums.Dyes;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IBlockWithTextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.material.GTMaterialIcons;
import gregtech.api.material.GTMaterialTextures;
import gregtech.api.material.MaterialUtils;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.BaseTileEntity;
import gregtech.api.metatileentity.CoverableTileEntity;
import gregtech.api.metatileentity.implementations.MTEFrame;
import gregtech.common.render.GTRendererBlock;

/// The block backing the frame-box MaterialLib shape: metadata is the material's global index, the raw index
/// with no flag bits. A plain frame has no tile entity; [#hasTileEntity] answers true unconditionally so a
/// tile entity attached later (a cover applied through [#spawnFrameEntity]) survives chunk save/load, while
/// [#createTileEntity] returns null so placement never auto-creates one.
public class FrameShapeBlock extends ShapeBlock implements IBlockWithTextures {

    private static final ThreadLocal<IGregTechTileEntity> temporaryTileEntity = new ThreadLocal<>();

    private final int mteId;
    private final Map<Integer, ITexture[][]> texturesByIndex = new ConcurrentHashMap<>();

    public FrameShapeBlock(String name, String displayNameFormat, int mteId, String... oreDicts) {
        super(new MaterialMachines(), "gregtech", name, displayNameFormat, oreDicts);
        this.mteId = mteId;
        setCreativeTab(GregTechAPI.TAB_GREGTECH_MATERIALS);
        GregTechAPI.registerMachineBlock(this, -1);
    }

    /// The id of the single material-agnostic frame MTE bound by [#spawnFrameEntity].
    public int getMteId() {
        return mteId;
    }

    /// The display name of a material's frame box. The Infused stones and the sand-family materials name the
    /// material alone.
    public static String displayName(Material material) {
        String internalName = MaterialUtils.internalName(material);
        return switch (internalName) {
            case "InfusedAir", "InfusedDull", "InfusedEarth", "InfusedEntropy", "InfusedFire", "InfusedOrder", "InfusedVis", "InfusedWater" -> OrePrefixes
                .getLocalizedNameForItem("%material Infused Stone", internalName);
            case "Vermiculite", "Bentonite", "Kaolinite", "Talc", "BasalticMineralSand", "GraniticMineralSand", "GlauconiteSand", "CassiteriteSand", "GarnetSand", "QuartzSand", "Pitchblende", "FullersEarth" -> OrePrefixes
                .getLocalizedNameForItem("%material", internalName);
            default -> OrePrefixes.frameGt.getLocalizedNameForItem(internalName);
        };
    }

    @Override
    public void registerWithGame() {
        GameRegistry.registerBlock(this, FrameShapeItemBlock.class, getName());
    }

    private BaseMetaPipeEntity spawnFrameEntity(World world, int x, int y, int z) {
        return spawnFrameEntity(world, null, x, y, z);
    }

    /// Attaches the frame MTE to a plain frame block, without touching the block metadata. The signature is
    /// relied on externally (matter-manipulator mixes into it).
    public BaseMetaPipeEntity spawnFrameEntity(World world, EntityPlayer player, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof BaseMetaPipeEntity base) {
            return base;
        }
        BaseMetaPipeEntity base = new BaseMetaPipeEntity();
        world.setTileEntity(x, y, z, base);
        base.setInitialValuesAsNBT(null, (short) mteId);
        if (player != null) {
            base.setOwnerName(player.getDisplayName());
            base.setOwnerUuid(player.getUniqueID());
        }
        base.getMetaTileEntity()
            .initDefaultModes(null);
        return base;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float subX,
        float subY, float subZ) {
        ForgeDirection direction = ForgeDirection.getOrientation(side);
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof BaseMetaPipeEntity baseTileEntity) {
            return baseTileEntity.onRightclick(player, direction, subX, subY, subZ);
        }
        ItemStack item = player.getHeldItem();
        if (CoverRegistry.isCover(item)) {
            BaseMetaPipeEntity newTileEntity = spawnFrameEntity(world, player, x, y, z);
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

    @Override
    public boolean canRenderInPass(int pass) {
        return pass == 0 || pass == 1;
    }

    @Override
    public String getHarvestTool(int meta) {
        return "wrench";
    }

    @Override
    public int getHarvestLevel(int meta) {
        return 2;
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
        final TileEntity frameEntity = world.getTileEntity(x, y, z);
        return frameEntity instanceof CoverableTileEntity cte && cte.hasCoverAtSide(forgeSide);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
        if (world.isRemote) return;
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IGregTechTileEntity gtTE && gtTE.canAccessData()) {
            IMetaTileEntity mte = gtTE.getMetaTileEntity();
            mte.onBlockDestroyed();
            temporaryTileEntity.set(gtTE);
        }
        GregTechAPI.causeMachineUpdate(world, x, y, z);
        super.breakBlock(world, x, y, z, block, metadata);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        GregTechAPI.causeMachineUpdate(world, x, y, z);
        super.onBlockAdded(world, x, y, z);
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
    public float getBlockHardness(World world, int x, int y, int z) {
        return Blocks.iron_block.getBlockHardness(world, x, y, z);
    }

    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity instanceof BaseMetaTileEntity baseMTE && baseMTE.privateAccess()
            && !baseMTE.playerOwnsThis(player, true) ? -1.0F
                : super.getPlayerRelativeBlockHardness(player, world, x, y, z);
    }

    @Override
    public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof BaseTileEntity baseTE) {
            baseTE.onAdjacentBlockChange(tileX, tileY, tileZ);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof BaseMetaPipeEntity baseMetaPipe) {
            baseMetaPipe.onNeighborBlockChange(x, y, z);
        }
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
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity == null) return false;
        if (tileEntity instanceof BaseMetaTileEntity) {
            return true;
        }
        if (tileEntity instanceof BaseMetaPipeEntity baseMetaPipe && (baseMetaPipe.mConnections & 0xFFFFFFC0) != 0) {
            return true;
        }
        return tileEntity instanceof ICoverable coverable && coverable.hasCoverAtSide(side);
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

    @Override
    public boolean isNormalCube(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean hasTileEntity(int meta) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return null;
    }

    @Override
    public boolean onBlockEventReceived(World world, int x, int y, int z, int data1, int data2) {
        super.onBlockEventReceived(world, x, y, z, data1, data2);
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity != null && tileEntity.receiveClientEvent(data1, data2);
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
        return texturesByIndex.computeIfAbsent(meta, index -> {
            Material material = MaterialLibAPI.getMaterialByIndex(index);
            short[] rgba = MaterialUtils.rgba(material);
            if (material == null || rgba == null) return null;
            ITexture[] texture = { GTMaterialTextures.stored(
                GTMaterialIcons.block(getName(), material),
                () -> Dyes.getModulation(-1, MaterialUtils.rgba(material)),
                false) };
            return new ITexture[][] { texture, texture, texture, texture, texture, texture };
        });
    }

    /// The item form of the frame shape: damage is the material's global index, named by [#displayName].
    public static class FrameShapeItemBlock extends ItemBlock {

        public FrameShapeItemBlock(Block block) {
            super(block);
            setHasSubtypes(true);
            setMaxDamage(0);
        }

        @Override
        public int getMetadata(int damage) {
            return damage;
        }

        @Override
        public String getItemStackDisplayName(ItemStack stack) {
            Material material = MaterialLibAPI.getMaterialByIndex(stack.getItemDamage());
            if (material == null) return super.getItemStackDisplayName(stack);
            return displayName(material);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean advanced) {
            super.addInformation(stack, player, list, advanced);
            Material material = MaterialLibAPI.getMaterialByIndex(stack.getItemDamage());
            MaterialUtils.addTooltips(material, list);
            list.add(StatCollector.translateToLocal(MTEFrame.LOCALIZED_DESC_FORMAT));
        }
    }
}
