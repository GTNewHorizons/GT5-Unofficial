package tectech.thing.metaTileEntity.single;

import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import tectech.mechanics.pipe.IConnectsToEnergyTunnel;
import tectech.thing.gui.MTEDebugPowerGeneratorGui;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyTunnel;
import tectech.thing.metaTileEntity.pipe.MTEPipeLaser;
import tectech.thing.metaTileEntity.pipe.MTEPipeLaserMirror;
import tectech.util.CommonValues;

/**
 * Created by Tec on 23.03.2017.
 */
public class MTEDebugPowerGenerator extends MTETieredMachineBlock implements IConnectsToEnergyTunnel {

    public static ITexture GENNY;
    private boolean LASER = false;
    private boolean isProducing = true;
    private int voltage = 8;
    private byte voltageTier = 0;
    private boolean isUsingTiers = true;
    private int amperage = 1;

    public MTEDebugPowerGenerator(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            0,
            new String[] { CommonValues.TEC_MARK_GENERAL, translateToLocal("gt.blockmachines.debug.tt.genny.desc.0"),
                EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.debug.tt.genny.desc.3"),
                EnumChatFormatting.BLUE + translateToLocal("gt.blockmachines.debug.tt.genny.desc.1"),
                EnumChatFormatting.BLUE + translateToLocal("gt.blockmachines.debug.tt.genny.desc.2") });
    }

    public MTEDebugPowerGenerator(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEDebugPowerGenerator(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        LASER = !LASER;
        GTUtility.sendChatToPlayer(aPlayer, translateToLocalFormatted("tt.chat.debug.generator", LASER ? "ON" : "OFF"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        GENNY = TextureFactory.of(Textures.BlockIcons.custom("iconsets/GENNY"));
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][colorIndex + 1],
            side != facing
                ? LASER
                    ? (isProducing ? Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_LASER[mTier + 1]
                        : Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_LASER[mTier + 1])
                    : (isProducing ? Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_64A[mTier + 1]
                        : Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_64A[mTier + 1])
                : GENNY };
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return null;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity iGregTechTileEntity, int i, ForgeDirection side,
        ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity iGregTechTileEntity, int i, ForgeDirection side,
        ItemStack itemStack) {
        return false;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("eVoltage", voltage);
        aNBT.setByte("eVoltageTier", voltageTier);
        aNBT.setInteger("eAmperage", amperage);
        aNBT.setBoolean("eUsingTiers", isUsingTiers);
        aNBT.setBoolean("eProducing", isProducing);
        aNBT.setBoolean("eLaser", LASER);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        voltage = aNBT.getInteger("eVoltage");
        voltageTier = aNBT.getByte("eVoltageTier");
        amperage = aNBT.getInteger("eAmperage");
        isUsingTiers = aNBT.getBoolean("eUsingTiers");
        isProducing = aNBT.getBoolean("eProducing");
        LASER = aNBT.getBoolean("eLaser");
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isAllowedToWork() && aBaseMetaTileEntity.isServerSide()) {
            if (!LASER) {
                if (isProducing) {
                    setEUVar(maxEUStore());
                } else {
                    setEUVar(0);
                }
            } else {
                byte Tick = (byte) (aTick % 20);
                if (isProducing && CommonValues.TRANSFER_AT == Tick) {
                    setEUVar(maxEUStore());
                    moveAround(aBaseMetaTileEntity);
                } else if (CommonValues.TRANSFER_AT == Tick) {
                    setEUVar(0);
                }
            }
        }
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isEnetOutput() {
        return !LASER;
    }

    @Override
    public boolean isEnetInput() {
        return !LASER;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return !isProducing && side != getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return isProducing && side != getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public long maxAmperesIn() {
        return isProducing ? 0 : amperage;
    }

    @Override
    public long maxAmperesOut() {
        return isProducing ? amperage : 0;
    }

    @Override
    public long maxEUInput() {
        // could be set to Long.MAX_VALUE to avoid exploding
        return isProducing ? 0 : getActualVoltage();
    }

    @Override
    public long maxEUOutput() {
        return isProducing ? getActualVoltage() : 0;
    }

    @Override
    public long maxEUStore() {
        // should not be increased to avoid overflow
        return getActualVoltage() * amperage << (LASER ? 2 : 3);
    }

    @Override
    public long getMinimumStoredEU() {
        return getActualVoltage() * amperage;
    }

    @Override
    public int getProgresstime() {
        return (int) getBaseMetaTileEntity().getUniversalEnergyStored();
    }

    @Override
    public int maxProgresstime() {
        return (int) getBaseMetaTileEntity().getUniversalEnergyCapacity();
    }

    public int getVoltage() {
        return voltage;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    public byte getVoltageTier() {
        return voltageTier;
    }

    public void setVoltageTier(byte voltageTier) {
        this.voltageTier = voltageTier;
    }

    public boolean isUsingTiers() {
        return isUsingTiers;
    }

    public void setUsingTiers(boolean usingTiers) {
        isUsingTiers = usingTiers;
    }

    public int getAmperage() {
        return amperage;
    }

    public void setAmperage(int amperage) {
        this.amperage = amperage;
    }

    public boolean isLASER() {
        return LASER;
    }

    public void setLASER(boolean LASER) {
        this.LASER = LASER;
        getBaseMetaTileEntity().issueTextureUpdate();
    }

    public boolean isProducing() {
        return isProducing;
    }

    public void setProducing(boolean producing) {
        isProducing = producing;
        getBaseMetaTileEntity().issueTextureUpdate();
    }

    public long getActualVoltage() {
        return (isUsingTiers ? GTValues.V[voltageTier] : voltage);
    }

    @Override
    public boolean canConnect(ForgeDirection side) {
        return LASER && side != getBaseMetaTileEntity().getFrontFacing();
    }

    private void moveAround(IGregTechTileEntity aBaseMetaTileEntity) {
        for (final ForgeDirection face : ForgeDirection.VALID_DIRECTIONS) {
            if (face == aBaseMetaTileEntity.getFrontFacing()) continue;
            ForgeDirection opposite = face.getOpposite();
            for (short dist = 1; dist < 1000; dist++) {
                IGregTechTileEntity tGTTileEntity = aBaseMetaTileEntity
                    .getIGregTechTileEntityAtSideAndDistance(face, dist);
                if (tGTTileEntity == null) {
                    break;
                }
                IMetaTileEntity aMetaTileEntity = tGTTileEntity.getMetaTileEntity();
                if (aMetaTileEntity == null) {
                    break;
                }

                // If we hit a mirror, use the mirror's view instead
                if (aMetaTileEntity instanceof MTEPipeLaserMirror tMirror) {
                    tGTTileEntity = tMirror.bendAround(opposite);
                    if (tGTTileEntity == null) {
                        break;
                    } else {
                        aMetaTileEntity = tGTTileEntity.getMetaTileEntity();
                        opposite = tMirror.getChainedFrontFacing();
                    }
                }

                if (aMetaTileEntity instanceof MTEHatchEnergyTunnel && opposite == tGTTileEntity.getFrontFacing()) {
                    if (maxEUOutput() > ((MTEHatchEnergyTunnel) aMetaTileEntity).maxEUInput()) {
                        aMetaTileEntity.doExplosion(maxEUOutput());
                    } else {
                        long diff = Math.min(
                            amperage * 20L * maxEUOutput(),
                            Math.min(
                                ((MTEHatchEnergyTunnel) aMetaTileEntity).maxEUStore()
                                    - aMetaTileEntity.getBaseMetaTileEntity()
                                        .getStoredEU(),
                                aBaseMetaTileEntity.getStoredEU()));
                        ((MTEHatchEnergyTunnel) aMetaTileEntity).setEUVar(
                            aMetaTileEntity.getBaseMetaTileEntity()
                                .getStoredEU() + diff);
                    }
                } else if (aMetaTileEntity instanceof MTEPipeLaser) {
                    if (((MTEPipeLaser) aMetaTileEntity).connectionCount < 2) {
                        break;
                    } else {
                        ((MTEPipeLaser) aMetaTileEntity).markUsed();
                    }
                } else {
                    break;
                }
            }
        }
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEDebugPowerGeneratorGui(this).build(data, syncManager, uiSettings);
    }
}
