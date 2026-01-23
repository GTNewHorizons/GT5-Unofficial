package tectech.thing.metaTileEntity.hatch;

import static gregtech.api.enums.GTValues.V;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.hatch.MTEHatchDynamoTunnelGui;
import tectech.mechanics.pipe.IConnectsToEnergyTunnel;
import tectech.thing.metaTileEntity.pipe.MTEPipeLaser;
import tectech.thing.metaTileEntity.pipe.MTEPipeLaserMirror;
import tectech.util.CommonValues;

/**
 * Created by danie_000 on 16.12.2016.
 */
public class MTEHatchDynamoTunnel extends MTEHatchDynamoMulti implements IConnectsToEnergyTunnel {

    public MTEHatchDynamoTunnel(int ID, String unlocalisedName, String localisedName, int tier, int amps) {
        super(
            ID,
            unlocalisedName,
            localisedName,
            tier,
            0,
            new String[] { CommonValues.TEC_MARK_GENERAL,
                translateToLocal("gt.blockmachines.hatch.dynamotunnel.desc.0"),
                translateToLocal("gt.blockmachines.hatch.screwdrivertooltip"),
                translateToLocal("gt.blockmachines.hatch.dynamotunnel.desc.1") + ": "
                    + EnumChatFormatting.YELLOW
                    + formatNumber(amps * V[tier])
                    + EnumChatFormatting.RESET
                    + " EU/t" },
            amps);
    }

    public MTEHatchDynamoTunnel(String aName, int aTier, int aAmp, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aAmp, aDescription, aTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_LASER[mTier + 1] };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_LASER[mTier + 1] };
    }

    @Override
    public long getMinimumStoredEU() {
        return V[mTier];
    }

    @Override
    public long maxEUStore() {
        return V[mTier] * 24L * Amperes;
    }

    @Override
    public boolean isEnetOutput() {
        return false;
    }

    @Override
    public ConnectionType getConnectionType() {
        return ConnectionType.LASER;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchDynamoTunnel(mName, mTier, Amperes, mDescriptionArray, mTextures);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            byte Tick = (byte) (aTick % 20);
            if (CommonValues.TRANSFER_AT == Tick) {
                if (aBaseMetaTileEntity.getStoredEU() > 0) {
                    setEUVar(aBaseMetaTileEntity.getStoredEU() - Amperes);
                    if (aBaseMetaTileEntity.getStoredEU() < 0) {
                        setEUVar(0);
                    }
                }
                if (aBaseMetaTileEntity.getStoredEU() > getMinimumStoredEU()) {
                    moveAround(aBaseMetaTileEntity);
                }
            }
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (Amperes != maxAmperes) {
            aNBT.setInteger("amperes", Amperes);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        int savedAmperes = aNBT.getInteger("amperes");
        if (savedAmperes != 0) {
            Amperes = savedAmperes;
        }
    }

    private void moveAround(IGregTechTileEntity aBaseMetaTileEntity) {
        byte color = getBaseMetaTileEntity().getColorization();
        if (color < 0) {
            return;
        }
        final ForgeDirection front = aBaseMetaTileEntity.getFrontFacing();
        ForgeDirection opposite = front.getOpposite();
        for (short dist = 1; dist < 1000; dist++) {

            IGregTechTileEntity tGTTileEntity = aBaseMetaTileEntity
                .getIGregTechTileEntityAtSideAndDistance(front, dist);
            if (tGTTileEntity != null && tGTTileEntity.getColorization() == color) {
                IMetaTileEntity aMetaTileEntity = tGTTileEntity.getMetaTileEntity();
                if (aMetaTileEntity != null) {
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
                            setEUVar(aBaseMetaTileEntity.getStoredEU() - maxEUOutput());
                            return;
                        } else if (maxEUOutput() == ((MTEHatchEnergyTunnel) aMetaTileEntity).maxEUInput()) {
                            long diff = Math.min(
                                Amperes * 20L * maxEUOutput(),
                                Math.min(
                                    ((MTEHatchEnergyTunnel) aMetaTileEntity).maxEUStore()
                                        - aMetaTileEntity.getBaseMetaTileEntity()
                                            .getStoredEU(),
                                    aBaseMetaTileEntity.getStoredEU()));

                            setEUVar(aBaseMetaTileEntity.getStoredEU() - diff);

                            ((MTEHatchEnergyTunnel) aMetaTileEntity).setEUVar(
                                aMetaTileEntity.getBaseMetaTileEntity()
                                    .getStoredEU() + diff);
                        }
                        return;
                    } else if (aMetaTileEntity instanceof MTEPipeLaser) {
                        if (((MTEPipeLaser) aMetaTileEntity).connectionCount < 2) {
                            return;
                        } else {
                            ((MTEPipeLaser) aMetaTileEntity).markUsed();
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        openGui(aPlayer);
        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ, aTool);
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchDynamoTunnelGui(this).build(data, syncManager, uiSettings);
    }

    @Override
    public boolean canConnect(ForgeDirection side) {
        return isOutputFacing(side);
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

}
