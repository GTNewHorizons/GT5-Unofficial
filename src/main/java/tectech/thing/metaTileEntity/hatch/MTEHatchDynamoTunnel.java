package tectech.thing.metaTileEntity.hatch;

import static gregtech.api.enums.GTValues.V;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.Collections;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.common.gui.modularui.hatch.MTEHatchDynamoTunnelGui;
import tectech.mechanics.pipe.IConnectsToEnergyTunnel;
import tectech.thing.metaTileEntity.pipe.MTEPipeLaser;
import tectech.thing.metaTileEntity.pipe.MTEPipeLaserMirror;
import tectech.util.CommonValues;

/**
 * Created by danie_000 on 16.12.2016.
 */
@IMetaTileEntity.SkipGenerateDescription
public class MTEHatchDynamoTunnel extends MTEHatchDynamoMulti implements IConnectsToEnergyTunnel {

    private static final MTEPipeLaser[] NO_PIPES = new MTEPipeLaser[0];

    private MTEHatchEnergyTunnel cachedTarget;
    private MTEPipeLaser[] cachedPipes = NO_PIPES;
    private byte cachedColor;
    private ForgeDirection cachedFront;

    public MTEHatchDynamoTunnel(int ID, String unlocalisedName, String localisedName, int tier, int amps) {
        super(ID, unlocalisedName, localisedName, tier, 0, null, amps);
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
    public void onUnload() {
        clearCachedRoute();
        super.onUnload();
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
            clearCachedRoute();
            return;
        }
        final ForgeDirection front = aBaseMetaTileEntity.getFrontFacing();

        // An intact pipe prefix lets the scan resume past it instead of reading those blocks from the world again.
        short startDist = 1;
        if (isCachedPrefixValid(aBaseMetaTileEntity, color, front)) {
            if (cachedTarget != null && isCachedTargetValid(aBaseMetaTileEntity, color, front)) {
                if (!transferEnergy(aBaseMetaTileEntity, cachedTarget)) {
                    clearCachedRoute();
                }
                return;
            }
            startDist = (short) (cachedPipes.length + 1);
        } else {
            clearCachedRoute();
        }

        ForgeDirection opposite = front.getOpposite();
        ArrayList<MTEPipeLaser> pipes = null;
        boolean cacheable = true;
        for (short dist = startDist; dist < 1000; dist++) {

            IGregTechTileEntity tGTTileEntity = aBaseMetaTileEntity
                .getIGregTechTileEntityAtSideAndDistance(front, dist);
            if (tGTTileEntity != null && tGTTileEntity.getColorization() == color) {
                IMetaTileEntity aMetaTileEntity = tGTTileEntity.getMetaTileEntity();
                if (aMetaTileEntity != null) {
                    // If we hit a mirror, use the mirror's view instead
                    if (aMetaTileEntity instanceof MTEPipeLaserMirror tMirror) {
                        cacheable = false;

                        tGTTileEntity = tMirror.bendAround(opposite);
                        if (tGTTileEntity == null) {
                            break;
                        } else {
                            aMetaTileEntity = tGTTileEntity.getMetaTileEntity();
                            opposite = tMirror.getChainedFrontFacing();
                        }
                    }

                    if (aMetaTileEntity instanceof MTEHatchEnergyTunnel target
                        && opposite == tGTTileEntity.getFrontFacing()) {
                        if (transferEnergy(aBaseMetaTileEntity, target) && cacheable) {
                            cachedTarget = target;
                            cachedPipes = pipes == null ? cachedPipes : pipes.toArray(new MTEPipeLaser[0]);
                            cachedColor = color;
                            cachedFront = front;
                        } else {
                            clearCachedRoute();
                        }
                        return;
                    } else if (aMetaTileEntity instanceof MTEPipeLaser pipe) {
                        if (pipe.connectionCount < 2) {
                            break;
                        } else {
                            pipe.markUsed();
                            if (cacheable) {
                                if (pipes == null) {
                                    pipes = new ArrayList<>(cachedPipes.length + 1);
                                    Collections.addAll(pipes, cachedPipes);
                                }
                                pipes.add(pipe);
                            }
                        }
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        // No tunnel hatch reached, but the pipes walked so far are still worth caching.
        if (cacheable) {
            cachedTarget = null;
            if (pipes != null) cachedPipes = pipes.toArray(new MTEPipeLaser[0]);
            cachedColor = color;
            cachedFront = front;
        } else {
            clearCachedRoute();
        }
    }

    private boolean isCachedTargetValid(IGregTechTileEntity source, byte color, ForgeDirection front) {
        if (!cachedTarget.isValid()) {
            return false;
        }
        IGregTechTileEntity target = cachedTarget.getBaseMetaTileEntity();
        return isAtDistance(source, target, front, cachedPipes.length + 1) && target.getColorization() == color
            && target.getFrontFacing() == front.getOpposite();
    }

    private boolean isCachedPrefixValid(IGregTechTileEntity source, byte color, ForgeDirection front) {
        if (cachedColor != color || cachedFront != front) {
            return false;
        }

        for (int i = 0; i < cachedPipes.length; i++) {
            MTEPipeLaser pipe = cachedPipes[i];
            IGregTechTileEntity pipeBase = pipe.getBaseMetaTileEntity();
            if (pipeBase == null || pipeBase.isDead()
                || pipeBase.getMetaTileEntity() != pipe
                || !isAtDistance(source, pipeBase, front, i + 1)
                || pipe.connectionCount < 2
                || pipeBase.getColorization() != color) {
                return false;
            }
            pipe.markUsed();
        }

        return true;
    }

    private static boolean isAtDistance(IGregTechTileEntity source, IGregTechTileEntity tile, ForgeDirection direction,
        int distance) {
        return tile.getWorld() == source.getWorld()
            && tile.getXCoord() == source.getXCoord() + direction.offsetX * distance
            && tile.getYCoord() == source.getYCoord() + direction.offsetY * distance
            && tile.getZCoord() == source.getZCoord() + direction.offsetZ * distance;
    }

    /** @return false if the target was blown up, meaning the route must not be cached. */
    private boolean transferEnergy(IGregTechTileEntity source, MTEHatchEnergyTunnel target) {
        long outputVoltage = maxEUOutput();
        if (outputVoltage > target.maxEUInput()) {
            target.doExplosion(outputVoltage);
            setEUVar(source.getStoredEU() - outputVoltage);
            return false;
        } else if (outputVoltage == target.maxEUInput()) {
            IGregTechTileEntity targetBase = target.getBaseMetaTileEntity();
            long diff = Math.min(
                Amperes * 20L * outputVoltage,
                Math.min(target.maxEUStore() - targetBase.getStoredEU(), source.getStoredEU()));

            setEUVar(source.getStoredEU() - diff);
            target.setEUVar(targetBase.getStoredEU() + diff);
        }
        return true;
    }

    private void clearCachedRoute() {
        cachedTarget = null;
        cachedPipes = NO_PIPES;
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

    @Override
    public String[] getDescription() {
        return MTEHatch.formatEnergyInfoDesc(
            translateToLocal("gt.blockmachines.hatch.screwdrivertooltip"),
            true,
            mTier,
            maxAmperes,
            "gt.blockmachines.hatch.dynamotunnel.desc");
    }
}
