package gregtech.common.tileentities.machines.basic;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_SIDES;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_SIDES_GLOW;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicTank;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTItemTransfer;
import gregtech.api.util.GTUtility;
import gregtech.common.config.MachineStats;
import gregtech.common.gui.modularui.singleblock.MTETeleporterGui;
import ic2.core.block.EntityItnt;
import ic2.core.block.EntityNuke;

@IMetaTileEntity.SkipGenerateDescription
public class MTETeleporter extends MTEBasicTank {

    private static boolean sInterDimensionalTeleportAllowed = true;
    private static int sPassiveEnergyDrain = 2048;
    private static int sPowerMultiplyer = 100;
    private static double sFPowerMultiplyer = 1.0;
    private int mTargetX = 0;
    private int mTargetY = 0;
    private int mTargetZ = 0;
    private int mTargetD = Integer.MIN_VALUE;
    public boolean mDebug = false;

    public MTETeleporter(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 3, new String[] { "gt.blockmachines.basicmachine.teleporter.tooltip" });
    }

    public String[] getDescription() {
        return GTUtility.translateMultiline("gt.blockmachines.basicmachine.teleporter.tooltip");
    }

    public MTETeleporter(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    public int getTargetX() {
        return mTargetX;
    }

    public void setTargetX(int targetX) {
        this.mTargetX = targetX;
    }

    public int getTargetY() {
        return mTargetY;
    }

    public void setTargetY(int targetY) {
        this.mTargetY = targetY;
    }

    public int getTargetZ() {
        return mTargetZ;
    }

    public void setTargetZ(int targetZ) {
        this.mTargetZ = targetZ;
    }

    public int getTargetD() {
        return mTargetD;
    }

    public void setTargetD(int targetD) {
        this.mTargetD = targetD;
    }

    private static float calculateWeight(Entity entity) {
        if ((entity instanceof EntityFishHook)) {
            return -1.0F;
        }
        if ((entity instanceof EntityDragonPart)) {
            return -1.0F;
        }
        if ((entity instanceof EntityWeatherEffect)) {
            return -1.0F;
        }
        if ((entity instanceof EntityPlayer tPlayer)) {
            int tCount = 64;
            for (int i = 0; i < 36; i++) {
                if (tPlayer.inventory.getStackInSlot(i) != null) {
                    tCount += (tPlayer.inventory.getStackInSlot(i)
                        .getMaxStackSize() > 1 ? tPlayer.inventory.getStackInSlot(i).stackSize : 64);
                }
            }
            for (int i = 0; i < 4; i++) {
                if (tPlayer.inventory.armorInventory[i] != null) {
                    tCount += 256;
                }
            }
            return Math.min(5.0F, tCount / 666.6F);
        }
        if (entity instanceof EntityItnt) {
            return 5.0F;
        }
        if (entity instanceof EntityNuke) {
            return 50.0F;
        }
        if ((entity instanceof EntityArrow)) {
            return 0.001F;
        }
        if ((entity instanceof EntityBoat)) {
            return 0.1F;
        }
        if ((entity instanceof EntityEnderCrystal)) {
            return 2.0F;
        }
        if ((entity instanceof EntityEnderEye)) {
            return 0.001F;
        }
        if ((entity instanceof EntityFireball)) {
            return 0.001F;
        }
        if ((entity instanceof EntityFireworkRocket)) {
            return 0.001F;
        }
        if ((entity instanceof EntityHanging)) {
            return 0.005F;
        }
        if ((entity instanceof EntityItem)) {
            return 0.001F;
        }
        if ((entity instanceof EntityLiving)) {
            return 0.5F;
        }
        if ((entity instanceof EntityMinecart)) {
            return 0.1F;
        }
        if ((entity instanceof EntityThrowable)) {
            return 0.001F;
        }
        if ((entity instanceof EntityTNTPrimed)) {
            return 5.0F;
        }
        if ((entity instanceof EntityXPOrb)) {
            return 0.001F;
        }
        return -1.0F;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) return true;
        openGui(aPlayer);
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTETeleporter(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[] { StatCollector.translateToLocal("GT5U.infodata.coordinates"),
            StatCollector.translateToLocalFormatted(
                "GT5U.infodata.coordinates.x",
                EnumChatFormatting.GREEN + formatNumber(this.mTargetX) + EnumChatFormatting.RESET),
            StatCollector.translateToLocalFormatted(
                "GT5U.infodata.coordinates.y",
                EnumChatFormatting.GREEN + formatNumber(this.mTargetY) + EnumChatFormatting.RESET),
            StatCollector.translateToLocalFormatted(
                "GT5U.infodata.coordinates.z",
                EnumChatFormatting.GREEN + formatNumber(this.mTargetZ) + EnumChatFormatting.RESET),
            StatCollector.translateToLocalFormatted(
                "GT5U.infodata.dimension",
                "" + EnumChatFormatting.GREEN + this.mTargetD + EnumChatFormatting.RESET),
            StatCollector.translateToLocalFormatted(
                "GT5U.infodata.dimension.valid",
                (GTUtility.isRealDimension(this.mTargetD)
                    ? EnumChatFormatting.GREEN + StatCollector.translateToLocal("GT5U.infodata.yes")
                        + EnumChatFormatting.RESET
                    : EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.infodata.no")
                        + EnumChatFormatting.RESET)),
            StatCollector.translateToLocalFormatted(
                "GT5U.infodata.dimension.registered",
                (DimensionManager.isDimensionRegistered(this.mTargetD)
                    ? EnumChatFormatting.GREEN + StatCollector.translateToLocal("GT5U.infodata.yes")
                        + EnumChatFormatting.RESET
                    : EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.infodata.no")
                        + EnumChatFormatting.RESET)) };
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side != this.getBaseMetaTileEntity()
            .getFrontFacing())
            return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1], TextureFactory.of(OVERLAY_TELEPORTER_SIDES),
                TextureFactory.builder()
                    .addIcon(OVERLAY_TELEPORTER_SIDES_GLOW)
                    .glow()
                    .build() };
        if (aActive) return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1],
            TextureFactory.of(OVERLAY_TELEPORTER_ACTIVE), TextureFactory.builder()
                .addIcon(OVERLAY_TELEPORTER_ACTIVE_GLOW)
                .glow()
                .build() };
        return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1], TextureFactory.of(OVERLAY_TELEPORTER),
            TextureFactory.builder()
                .addIcon(OVERLAY_TELEPORTER_GLOW)
                .glow()
                .build() };
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        if (mFluid != null) aNBT.setTag("mFluid", mFluid.writeToNBT(new NBTTagCompound()));
        aNBT.setInteger("mTargetX", this.mTargetX);
        aNBT.setInteger("mTargetY", this.mTargetY);
        aNBT.setInteger("mTargetZ", this.mTargetZ);
        aNBT.setInteger("mTargetD", this.mTargetD);
        aNBT.setBoolean("mDebug", this.mDebug);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        mFluid = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("mFluid"));
        this.mTargetX = aNBT.getInteger("mTargetX");
        this.mTargetY = aNBT.getInteger("mTargetY");
        this.mTargetZ = aNBT.getInteger("mTargetZ");
        this.mTargetD = aNBT.getInteger("mTargetD");
        this.mDebug = aNBT.getBoolean("mDebug");
    }

    @Override
    public void onConfigLoad() {
        sInterDimensionalTeleportAllowed = MachineStats.teleporter.interDimensionalTPAllowed;
        sPassiveEnergyDrain = MachineStats.teleporter.passiveEnergyDrain;
        sPowerMultiplyer = MachineStats.teleporter.powerMultiplier;
        sFPowerMultiplyer = sPowerMultiplyer / 100.0;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        if (getBaseMetaTileEntity().isServerSide()) {
            if ((this.mTargetX == 0) && (this.mTargetY == 0)
                && (this.mTargetZ == 0)
                && (this.mTargetD == Integer.MIN_VALUE)) {
                this.mTargetX = aBaseMetaTileEntity.getXCoord();
                this.mTargetY = aBaseMetaTileEntity.getYCoord();
                this.mTargetZ = aBaseMetaTileEntity.getZCoord();
                this.mTargetD = aBaseMetaTileEntity.getWorld().provider.dimensionId;
            }
        }
    }

    public boolean hasDimensionalTeleportCapability() {
        return this.mDebug || sInterDimensionalTeleportAllowed;
    }

    public boolean isDimensionalTeleportAvailable() {
        return this.mDebug || (hasDimensionalTeleportCapability() && GTUtility.isRealDimension(this.mTargetD)
            && GTUtility.isRealDimension(getBaseMetaTileEntity().getWorld().provider.dimensionId));
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {

        if (mFluid != null) { // Was if null -> Materials.Nitrogen.getPlasma(0);
            mFluid = null;
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (getBaseMetaTileEntity().isServerSide()) {
            if ((getBaseMetaTileEntity().isAllowedToWork()) && (getBaseMetaTileEntity().getRedstone())) {
                if (getBaseMetaTileEntity().decreaseStoredEnergyUnits(sPassiveEnergyDrain, false)) {
                    int tDistance = distanceCalculation();
                    if (mInventory[0] != null) {
                        TileEntity tTile = null;
                        if (this.mTargetD == getBaseMetaTileEntity().getWorld().provider.dimensionId) {
                            tTile = getBaseMetaTileEntity().getTileEntity(this.mTargetX, this.mTargetY, this.mTargetZ);
                        } else {
                            World tWorld = DimensionManager.getWorld(this.mTargetD);
                            if (tWorld != null) {
                                tTile = tWorld.getTileEntity(this.mTargetX, this.mTargetY, this.mTargetZ);
                            }
                        }

                        GTItemTransfer transfer = new GTItemTransfer();

                        transfer.source(this, ForgeDirection.UNKNOWN);
                        transfer.sink(tTile, ForgeDirection.UNKNOWN);

                        int transferred = transfer.transfer();

                        getBaseMetaTileEntity().decreaseStoredEnergyUnits(
                            (long) (Math.pow(tDistance, 1.5) * tDistance * transferred * sFPowerMultiplyer),
                            false);
                    }

                    List<Entity> entities_in_box = getBaseMetaTileEntity().getWorld()
                        .getEntitiesWithinAABB(
                            Entity.class,
                            AxisAlignedBB.getBoundingBox(
                                getBaseMetaTileEntity().getOffsetX(getBaseMetaTileEntity().getFrontFacing(), 2) - 1,
                                getBaseMetaTileEntity().getOffsetY(getBaseMetaTileEntity().getFrontFacing(), 2) - 1,
                                getBaseMetaTileEntity().getOffsetZ(getBaseMetaTileEntity().getFrontFacing(), 2) - 1,
                                getBaseMetaTileEntity().getOffsetX(getBaseMetaTileEntity().getFrontFacing(), 2) + 2,
                                getBaseMetaTileEntity().getOffsetY(getBaseMetaTileEntity().getFrontFacing(), 2) + 2,
                                getBaseMetaTileEntity().getOffsetZ(getBaseMetaTileEntity().getFrontFacing(), 2) + 2));

                    for (Object tObject : entities_in_box) {
                        if (((tObject instanceof Entity tEntity)) && (!((Entity) tObject).isDead)) {
                            if (getBaseMetaTileEntity().decreaseStoredEnergyUnits(
                                (long) (Math.pow(tDistance, 1.5) * calculateWeight(tEntity) * sFPowerMultiplyer),
                                false)) {

                                if (tEntity.ridingEntity != null) {
                                    tEntity.mountEntity(null);
                                }
                                if (tEntity.riddenByEntity != null) {
                                    tEntity.riddenByEntity.mountEntity(null);
                                }
                                if ((this.mTargetD == getBaseMetaTileEntity().getWorld().provider.dimensionId)
                                    || (!isDimensionalTeleportAvailable())
                                    || (!GTUtility.moveEntityToDimensionAtCoords(
                                        tEntity,
                                        this.mTargetD,
                                        this.mTargetX + 0.5D,
                                        this.mTargetY + 0.5D,
                                        this.mTargetZ + 0.5D))) {
                                    if ((tEntity instanceof EntityLivingBase)) {
                                        ((EntityLivingBase) tEntity).setPositionAndUpdate(
                                            this.mTargetX + 0.5D,
                                            this.mTargetY + 0.5D,
                                            this.mTargetZ + 0.5D);
                                    } else {
                                        tEntity.setPosition(
                                            this.mTargetX + 0.5D,
                                            this.mTargetY + 0.5D,
                                            this.mTargetZ + 0.5D);
                                    }
                                }
                            }
                        }
                    }
                }
                getBaseMetaTileEntity().setActive(true);
            } else {
                getBaseMetaTileEntity().setActive(false);
            }
        }
    }

    private int distanceCalculation() {
        double dx = getBaseMetaTileEntity().getXCoord() - this.mTargetX;
        double dy = getBaseMetaTileEntity().getYCoord() - this.mTargetY;
        double dz = getBaseMetaTileEntity().getZCoord() - this.mTargetZ;
        return Math.abs(
            ((this.mTargetD != getBaseMetaTileEntity().getWorld().provider.dimensionId)
                && (isDimensionalTeleportAvailable()) ? 4000 : (int) Math.sqrt(dx * dx + dy * dy + dz * dz)));
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return true;
    }

    @Override
    public boolean isTeleporterCompatible() {
        return false;
    }

    @Override
    public long getMinimumStoredEU() {
        return V[mTier] * 16;
    }

    @Override
    public long maxEUStore() {
        return 100000000;
    }

    @Override
    public long maxEUInput() {
        return V[mTier];
    }

    @Override
    public long maxAmperesIn() {
        return 2;
    }

    @Override
    public int getOutputSlot() {
        return 0;
    }

    @Override
    public int getCapacity() {
        return 64000;
    }

    @Override
    public boolean doesFillContainers() {
        return false;
    }

    @Override
    public boolean doesEmptyContainers() {
        return false;
    }

    @Override
    public boolean canTankBeFilled() {
        return true;
    }

    @Override
    public boolean canTankBeEmptied() {
        return true;
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return null;
    }

    @Override
    public ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTETeleporterGui(this).build(guiData, syncManager, uiSettings);
    }
}
