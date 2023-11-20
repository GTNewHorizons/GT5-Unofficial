package gregtech.common.blocks;

import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.cricketcraft.chisel.api.IFacade;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IDebugableBlock;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IColoredTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IDebugableTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_Generic_Block;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.BaseTileEntity;
import gregtech.api.metatileentity.CoverableTileEntity;
import gregtech.api.util.GT_BaseCrop;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Utility;
import gregtech.common.covers.CoverInfo;
import gregtech.common.render.GT_Renderer_Block;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_QuantumChest;

@Optional.Interface(iface = "com.cricketcraft.chisel.api.IFacade", modid = "ChiselAPI")
public class GT_Block_Machines extends GT_Generic_Block implements IDebugableBlock, ITileEntityProvider, IFacade {

    private static final ThreadLocal<IGregTechTileEntity> mTemporaryTileEntity = new ThreadLocal<>();
    private boolean renderAsNormalBlock;

    public GT_Block_Machines() {
        super(GT_Item_Machines.class, "gt.blockmachines", new GT_Material_Machines());
        GregTech_API.registerMachineBlock(this, -1);
        setHardness(1.0F);
        setResistance(10.0F);
        setStepSound(soundTypeMetal);
        setCreativeTab(GregTech_API.TAB_GREGTECH);
        this.isBlockContainer = true;
        this.renderAsNormalBlock = true;
        this.useNeighborBrightness = true;
    }

    @Override
    public String getHarvestTool(int aMeta) {
        if (aMeta >= 8 && aMeta <= 11) {
            return "cutter";
        }
        return "wrench";
    }

    @Override
    public int getHarvestLevel(int aMeta) {
        return aMeta % 4;
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }

    @Override
    public void onNeighborChange(IBlockAccess aWorld, int aX, int aY, int aZ, int aTileX, int aTileY, int aTileZ) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if ((tTileEntity instanceof BaseTileEntity)) {
            ((BaseTileEntity) tTileEntity).onAdjacentBlockChange(aTileX, aTileY, aTileZ);
        }
    }

    @Override
    public void onNeighborBlockChange(World aWorld, int aX, int aY, int aZ, Block aBlock) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if ((tTileEntity instanceof BaseMetaPipeEntity)) {
            ((BaseMetaPipeEntity) tTileEntity).onNeighborBlockChange(aX, aY, aZ);
        }
    }

    @Override
    public void onBlockAdded(World aWorld, int aX, int aY, int aZ) {
        super.onBlockAdded(aWorld, aX, aY, aZ);
        if (GregTech_API.isMachineBlock(this, aWorld.getBlockMetadata(aX, aY, aZ))) {
            GregTech_API.causeMachineUpdate(aWorld, aX, aY, aZ);
        }
    }

    @Override
    public String getUnlocalizedName() {
        return "gt.blockmachines";
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal(getUnlocalizedName() + ".name");
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
    public int getRenderType() {
        if (GT_Renderer_Block.INSTANCE == null) {
            return super.getRenderType();
        }
        return GT_Renderer_Block.INSTANCE.mRenderID;
    }

    @Override
    public boolean isFireSource(World aWorld, int aX, int aY, int aZ, ForgeDirection side) {
        return GregTech_API.sMachineFlammable && (aWorld.getBlockMetadata(aX, aY, aZ) == 0);
    }

    @Override
    public boolean isFlammable(IBlockAccess aWorld, int aX, int aY, int aZ, ForgeDirection face) {
        return GregTech_API.sMachineFlammable && (aWorld.getBlockMetadata(aX, aY, aZ) == 0);
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess aWorld, int aX, int aY, int aZ) {
        return false;
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess aWorld, int aX, int aY, int aZ, int ordinalSide) {
        int forgeSide = -100;
        switch (ordinalSide) {
            case (-2):
                forgeSide = 0;
                break;
            case (-1):
                forgeSide = 1;
                break;
            case (0):
                forgeSide = 2;
                break;
            case (2):
                forgeSide = 3;
                break;
            case (3):
                forgeSide = 4;
                break;
            case (1):
                forgeSide = 5;
                break;
            default:
                break;
        }
        final TileEntity machineEntity = aWorld.getTileEntity(aX, aY, aZ);
        return machineEntity instanceof CoverableTileEntity cte
            && cte.getCoverInfoAtSide(ForgeDirection.getOrientation(forgeSide))
                .getCoverID() != 0;
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
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return renderAsNormalBlock;
    }

    public GT_Block_Machines setRenderAsNormalBlock(boolean aBool) {
        renderAsNormalBlock = aBool;
        return this;
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World aWorld, int aMeta) {
        return createTileEntity(aWorld, aMeta);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(IBlockAccess aIBlockAccess, int aX, int aY, int aZ, int ordinalSide) {
        return Textures.BlockIcons.MACHINE_LV_SIDE.getIcon();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return Textures.BlockIcons.MACHINE_LV_SIDE.getIcon();
    }

    @Override
    public boolean onBlockEventReceived(World aWorld, int aX, int aY, int aZ, int aData1, int aData2) {
        super.onBlockEventReceived(aWorld, aX, aY, aZ, aData1, aData2);
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        return tTileEntity != null && tTileEntity.receiveClientEvent(aData1, aData2);
    }

    @Override
    public void addCollisionBoxesToList(World aWorld, int aX, int aY, int aZ, AxisAlignedBB inputAABB,
        List<AxisAlignedBB> outputAABB, Entity collider) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IGregTechTileEntity
            && ((IGregTechTileEntity) tTileEntity).getMetaTileEntity() != null) {
            ((IGregTechTileEntity) tTileEntity)
                .addCollisionBoxesToList(aWorld, aX, aY, aZ, inputAABB, outputAABB, collider);
            return;
        }
        super.addCollisionBoxesToList(aWorld, aX, aY, aZ, inputAABB, outputAABB, collider);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IGregTechTileEntity
            && ((IGregTechTileEntity) tTileEntity).getMetaTileEntity() != null) {
            return ((IGregTechTileEntity) tTileEntity).getCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ);
        }
        return super.getCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IGregTechTileEntity
            && ((IGregTechTileEntity) tTileEntity).getMetaTileEntity() != null) {
            return ((IGregTechTileEntity) tTileEntity).getCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ);
        }
        return super.getSelectedBoundingBoxFromPool(aWorld, aX, aY, aZ);
    }

    @Override // THIS
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int aX, int aY, int aZ) {
        final TileEntity tTileEntity = blockAccess.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IGregTechTileEntity
            && (((IGregTechTileEntity) tTileEntity).getMetaTileEntity() != null)) {
            final AxisAlignedBB bbb = ((IGregTechTileEntity) tTileEntity)
                .getCollisionBoundingBoxFromPool(((IGregTechTileEntity) tTileEntity).getWorld(), 0, 0, 0);
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
    public void onEntityCollidedWithBlock(World aWorld, int aX, int aY, int aZ, Entity collider) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IGregTechTileEntity
            && ((IGregTechTileEntity) tTileEntity).getMetaTileEntity() != null) {
            ((IGregTechTileEntity) tTileEntity).onEntityCollidedWithBlock(aWorld, aX, aY, aZ, collider);
            return;
        }
        super.onEntityCollidedWithBlock(aWorld, aX, aY, aZ, collider);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister aIconRegister) {
        if (!GregTech_API.sPostloadFinished) return;
        GT_Log.out.println("GT_Mod: Setting up Icon Register for Blocks");
        GregTech_API.setBlockIconRegister(aIconRegister);

        GT_Log.out.println("GT_Mod: Registering MetaTileEntity specific Textures");
        try {
            for (IMetaTileEntity tMetaTileEntity : GregTech_API.METATILEENTITIES) {
                if (tMetaTileEntity != null) {
                    tMetaTileEntity.registerIcons(aIconRegister);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(GT_Log.err);
        }
        GT_Log.out.println("GT_Mod: Registering Crop specific Textures");
        try {
            for (GT_BaseCrop tCrop : GT_BaseCrop.sCropList) {
                tCrop.registerSprites(aIconRegister);
            }
        } catch (Exception e) {
            e.printStackTrace(GT_Log.err);
        }
        GT_Log.out.println("GT_Mod: Starting Block Icon Load Phase");
        GT_FML_LOGGER.info("GT_Mod: Starting Block Icon Load Phase");
        try {
            for (Runnable tRunnable : GregTech_API.sGTBlockIconload) {
                tRunnable.run();
            }
        } catch (Exception e) {
            e.printStackTrace(GT_Log.err);
        }
        GT_Log.out.println("GT_Mod: Finished Block Icon Load Phase");
        GT_FML_LOGGER.info("GT_Mod: Finished Block Icon Load Phase");
    }

    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        return tTileEntity instanceof BaseMetaTileEntity && ((BaseMetaTileEntity) tTileEntity).privateAccess()
            && !((BaseMetaTileEntity) tTileEntity).playerOwnsThis(aPlayer, true) ? -1.0F
                : super.getPlayerRelativeBlockHardness(aPlayer, aWorld, aX, aY, aZ);
    }

    @Override
    public boolean onBlockActivated(World aWorld, int aX, int aY, int aZ, EntityPlayer aPlayer, int ordinalSide,
        float aOffsetX, float aOffsetY, float aOffsetZ) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity == null) {
            return false;
        }
        if (aPlayer.isSneaking()) {
            final ItemStack tCurrentItem = aPlayer.inventory.getCurrentItem();
            if (tCurrentItem != null && !GT_Utility.isStackInList(tCurrentItem, GregTech_API.sScrewdriverList)
                && !GT_Utility.isStackInList(tCurrentItem, GregTech_API.sWrenchList)
                && !GT_Utility.isStackInList(tCurrentItem, GregTech_API.sWireCutterList)
                && !GT_Utility.isStackInList(tCurrentItem, GregTech_API.sSolderingToolList)
                && !GT_Utility.isStackInList(tCurrentItem, GregTech_API.sJackhammerList)) return false;
        }
        if (tTileEntity instanceof IGregTechTileEntity gtTE) {
            if (gtTE.getTimer() < 50L) {
                return false;
            }
            if ((!aWorld.isRemote) && !gtTE.isUseableByPlayer(aPlayer)) {
                return true;
            }
            return ((IGregTechTileEntity) tTileEntity)
                .onRightclick(aPlayer, ForgeDirection.getOrientation(ordinalSide), aOffsetX, aOffsetY, aOffsetZ);
        }
        return false;
    }

    @Override
    public void onBlockClicked(World aWorld, int aX, int aY, int aZ, EntityPlayer aPlayer) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IGregTechTileEntity gtTE) {
            gtTE.onLeftclick(aPlayer);
        }
    }

    @Override
    public int getDamageValue(World aWorld, int aX, int aY, int aZ) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IGregTechTileEntity gtTE) {
            return gtTE.getMetaTileID();
        }
        return 0;
    }

    @Override
    public void onBlockExploded(World aWorld, int aX, int aY, int aZ, Explosion aExplosion) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof BaseMetaTileEntity baseTE) {
            GT_Log.exp.printf(
                "Explosion at : %d | %d | %d DIMID: %s due to near explosion!%n",
                aX,
                aY,
                aZ,
                aWorld.provider.dimensionId);
            baseTE.doEnergyExplosion();
        }
        super.onBlockExploded(aWorld, aX, aY, aZ, aExplosion);
    }

    @Override
    public void breakBlock(World aWorld, int aX, int aY, int aZ, Block aBlock, int aMetadata) {
        GregTech_API.causeMachineUpdate(aWorld, aX, aY, aZ);
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IGregTechTileEntity gtTE) {
            gtTE.onBlockDestroyed();
            mTemporaryTileEntity.set(gtTE);
            if (!(gtTE.getMetaTileEntity() instanceof GT_MetaTileEntity_QuantumChest)) {
                for (int i = 0; i < gtTE.getSizeInventory(); i++) {
                    final ItemStack tItem = gtTE.getStackInSlot(i);
                    if ((tItem != null) && (tItem.stackSize > 0) && (gtTE.isValidSlot(i)) && gtTE.shouldDropItemAt(i)) {
                        final EntityItem tItemEntity = new EntityItem(
                            aWorld,
                            aX + XSTR_INSTANCE.nextFloat() * 0.8F + 0.1F,
                            aY + XSTR_INSTANCE.nextFloat() * 0.8F + 0.1F,
                            aZ + XSTR_INSTANCE.nextFloat() * 0.8F + 0.1F,
                            new ItemStack(tItem.getItem(), tItem.stackSize, tItem.getItemDamage()));
                        if (tItem.hasTagCompound()) {
                            tItemEntity.getEntityItem()
                                .setTagCompound(
                                    (NBTTagCompound) tItem.getTagCompound()
                                        .copy());
                        }
                        tItemEntity.motionX = (XSTR_INSTANCE.nextGaussian() * 0.05D);
                        tItemEntity.motionY = (XSTR_INSTANCE.nextGaussian() * 0.25D);
                        tItemEntity.motionZ = (XSTR_INSTANCE.nextGaussian() * 0.05D);
                        aWorld.spawnEntityInWorld(tItemEntity);
                        tItem.stackSize = 0;
                        gtTE.setInventorySlotContents(i, null);
                    }
                }
            }
        }
        super.breakBlock(aWorld, aX, aY, aZ, aBlock, aMetadata);
        aWorld.removeTileEntity(aX, aY, aZ);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World aWorld, int aX, int aY, int aZ, int aMeta, int aFortune) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IGregTechTileEntity gtTE) {
            return gtTE.getDrops();
        }
        final IGregTechTileEntity tGregTechTileEntity = mTemporaryTileEntity.get();
        final ArrayList<ItemStack> tDrops;
        if (tGregTechTileEntity == null) {
            tDrops = new ArrayList<>();
        } else {
            tDrops = tGregTechTileEntity.getDrops();
            mTemporaryTileEntity.remove();
        }
        return tDrops;
    }

    @Override
    public boolean removedByPlayer(World aWorld, EntityPlayer aPlayer, int aX, int aY, int aZ, boolean aWillHarvest) {
        // This delays deletion of the block until after getDrops
        return aWillHarvest || super.removedByPlayer(aWorld, aPlayer, aX, aY, aZ, false);
    }

    @Override
    public void harvestBlock(World aWorld, EntityPlayer aPlayer, int aX, int aY, int aZ, int aMeta) {
        super.harvestBlock(aWorld, aPlayer, aX, aY, aZ, aMeta);
        aWorld.setBlockToAir(aX, aY, aZ);
    }

    @Override
    public int getComparatorInputOverride(World aWorld, int aX, int aY, int aZ, int ordinalSide) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IGregTechTileEntity gtTE) {
            return gtTE.getComparatorValue(ForgeDirection.getOrientation(ordinalSide));
        }
        return 0;
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
    public void dropBlockAsItemWithChance(World aWorld, int aX, int aY, int aZ, int aMetadata, float chance,
        int aFortune) {
        if (!aWorld.isRemote) {
            final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
            if (tTileEntity != null && (chance < 1.0F)) {
                if (tTileEntity instanceof BaseMetaTileEntity bmte && (GregTech_API.sMachineNonWrenchExplosions)) {
                    GT_Log.exp.printf(
                        "Explosion at : %d | %d | %d DIMID: %s NonWrench picking/Rain!%n",
                        aX,
                        aY,
                        aZ,
                        aWorld.provider.dimensionId);
                    bmte.doEnergyExplosion();
                }
            } else {
                super.dropBlockAsItemWithChance(aWorld, aX, aY, aZ, aMetadata, chance, aFortune);
            }
        }
    }

    @Override
    public boolean isSideSolid(IBlockAccess aWorld, int aX, int aY, int aZ, ForgeDirection side) {
        if (aWorld.getBlockMetadata(aX, aY, aZ) == 0) {
            return true;
        }
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity != null) {
            if (tTileEntity instanceof BaseMetaTileEntity) {
                return true;
            }
            if (tTileEntity instanceof BaseMetaPipeEntity
                && (((BaseMetaPipeEntity) tTileEntity).mConnections & 0xFFFFFFC0) != 0) {
                return true;
            }
            return tTileEntity instanceof ICoverable && ((ICoverable) tTileEntity).getCoverIDAtSide(side) != 0;
        }
        return false;
    }

    @Override
    public boolean isBlockNormalCube() {
        return true;
    }

    /**
     * Returns the default ambient occlusion value based on block opacity
     */
    @SideOnly(Side.CLIENT)
    @Override
    public float getAmbientOcclusionLightValue() {
        return this.renderAsNormalBlock() ? 0.2F : 0.5F;
    }

    /**
     * @inheritDoc
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World aWorld, int aX, int aY, int aZ, Random aRandom) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IGregTechTileEntity) {
            ((IGregTechTileEntity) tTileEntity).onRandomDisplayTick();
        }
    }

    @Override
    public int getLightOpacity(IBlockAccess aWorld, int aX, int aY, int aZ) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IGregTechTileEntity) {
            return ((IGregTechTileEntity) tTileEntity).getLightOpacity();
        }
        return aWorld.getBlockMetadata(aX, aY, aZ) == 0 ? 255 : 0;
    }

    @Override
    public int getLightValue(IBlockAccess aWorld, int aX, int aY, int aZ) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof BaseMetaTileEntity) {
            return ((BaseMetaTileEntity) tTileEntity).getLightValue();
        }
        return 0;
    }

    @Override
    public TileEntity createTileEntity(World aWorld, int aMeta) {
        return GregTech_API.createTileEntity(aMeta);
    }

    @Override
    public float getExplosionResistance(Entity entity, World aWorld, int aX, int aY, int aZ, double explosionX,
        double explosionY, double explosionZ) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IGregTechTileEntity) {
            return ((IGregTechTileEntity) tTileEntity).getBlastResistance(ForgeDirection.UNKNOWN);
        }
        return 10.0F;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item item, CreativeTabs aCreativeTab, List<ItemStack> outputSubBlocks) {
        for (int i = 1; i < GregTech_API.METATILEENTITIES.length; i++) {
            if (GregTech_API.METATILEENTITIES[i] != null) {
                outputSubBlocks.add(new ItemStack(item, 1, i));
            }
        }
    }

    @Override
    public void onBlockPlacedBy(World aWorld, int aX, int aY, int aZ, EntityLivingBase aPlayer, ItemStack aStack) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (!(tTileEntity instanceof IGregTechTileEntity iGregTechTileEntity)) return;
        iGregTechTileEntity.setFrontFacing(
            BaseTileEntity.getSideForPlayerPlacing(aPlayer, ForgeDirection.UP, iGregTechTileEntity.getValidFacings()));
    }

    @Override
    public ArrayList<String> getDebugInfo(EntityPlayer aPlayer, int aX, int aY, int aZ, int aLogLevel) {
        final TileEntity tTileEntity = aPlayer.worldObj.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IDebugableTileEntity) {
            return ((IDebugableTileEntity) tTileEntity).getDebugInfo(aPlayer, aLogLevel);
        }
        return new ArrayList<>();
    }

    @Override
    public boolean recolourBlock(World aWorld, int aX, int aY, int aZ, ForgeDirection side, int aColor) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IColoredTileEntity coloredTE) {
            if (coloredTE.getColorization() == (byte) ((~aColor) & 0xF)) {
                return false;
            }
            coloredTE.setColorization((byte) ((~aColor) & 0xF));
            return true;
        }
        return false;
    }

    @Override
    public Block getFacade(IBlockAccess aWorld, int aX, int aY, int aZ, int ordinalSide) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof CoverableTileEntity tile) {
            final ForgeDirection dir = ForgeDirection.getOrientation(ordinalSide);
            if (dir != ForgeDirection.UNKNOWN) {
                final Block facadeBlock = tile.getCoverInfoAtSide(dir)
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
    public int getFacadeMetadata(IBlockAccess aWorld, int aX, int aY, int aZ, int ordinalSide) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof CoverableTileEntity tile) {
            final ForgeDirection dir = ForgeDirection.getOrientation(ordinalSide);
            if (ordinalSide != -1) {
                final CoverInfo coverInfo = tile.getCoverInfoAtSide(dir);
                final Block facadeBlock = coverInfo.getFacadeBlock();
                if (facadeBlock != null) return coverInfo.getFacadeMeta();
            } else {
                // we do not allow more than one type of facade per block, so no need to check every side
                // see comment in gregtech.common.covers.GT_Cover_FacadeBase.isCoverPlaceable
                for (final ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                    final CoverInfo coverInfo = tile.getCoverInfoAtSide(d);
                    final Block facadeBlock = coverInfo.getFacadeBlock();
                    if (facadeBlock != null) {
                        return coverInfo.getFacadeMeta();
                    }
                }
            }
        }
        return 0;
    }
}
