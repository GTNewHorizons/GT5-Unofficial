package com.github.technus.tectech.thing.metaTileEntity.hatch;

import static com.github.technus.tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_OUT_LASER_TT;
import static com.github.technus.tectech.util.CommonValues.TRANSFER_AT;
import static gregtech.api.enums.GT_Values.V;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.github.technus.tectech.mechanics.pipe.IConnectsToEnergyTunnel;
import com.github.technus.tectech.thing.metaTileEntity.pipe.GT_MetaTileEntity_Pipe_Energy;
import com.github.technus.tectech.thing.metaTileEntity.pipe.GT_MetaTileEntity_Pipe_EnergyMirror;
import com.github.technus.tectech.util.CommonValues;
import com.github.technus.tectech.util.TT_Utility;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.NumericWidget;

import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.PowerLogic;
import gregtech.api.logic.interfaces.PowerLogicHost;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GT_Utility;

/**
 * Created by danie_000 on 16.12.2016.
 */
public class GT_MetaTileEntity_Hatch_DynamoTunnel extends GT_MetaTileEntity_Hatch_DynamoMulti
    implements IConnectsToEnergyTunnel {

    public GT_MetaTileEntity_Hatch_DynamoTunnel(int ID, String unlocalisedName, String localisedName, int tier,
        int amps) {
        super(
            ID,
            unlocalisedName,
            localisedName,
            tier,
            0,
            new String[] { CommonValues.TEC_MARK_GENERAL,
                translateToLocal("gt.blockmachines.hatch.dynamotunnel.desc.0"),
                translateToLocal("gt.blockmachines.hatch.dynamotunnel.desc.1") + ": "
                    + EnumChatFormatting.YELLOW
                    + GT_Utility.formatNumbers(amps * V[tier])
                    + EnumChatFormatting.RESET
                    + " EU/t" },
            amps);

        TT_Utility.setTier(tier, this);
    }

    public GT_MetaTileEntity_Hatch_DynamoTunnel(String aName, int aTier, int aAmp, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aAmp, aDescription, aTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, OVERLAYS_ENERGY_OUT_LASER_TT[mTier] };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, OVERLAYS_ENERGY_OUT_LASER_TT[mTier] };
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public long getMinimumStoredEU() {
        return V[mTier];
    }

    @Override
    public long maxEUOutput() {
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
    public boolean isEnetInput() {
        return false;
    }

    @Override
    public ConnectionType getConnectionType() {
        return ConnectionType.LASER;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_DynamoTunnel(mName, mTier, Amperes, mDescriptionArray, mTextures);
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            byte Tick = (byte) (aTick % 20);
            if (TRANSFER_AT == Tick) {
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
                    if (aMetaTileEntity instanceof GT_MetaTileEntity_Pipe_EnergyMirror tMirror) {

                        tGTTileEntity = tMirror.bendAround(opposite);
                        if (tGTTileEntity == null) {
                            break;
                        } else {
                            aMetaTileEntity = tGTTileEntity.getMetaTileEntity();
                            opposite = tMirror.getChainedFrontFacing();
                        }
                    }

                    if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyTunnel
                        && opposite == tGTTileEntity.getFrontFacing()) {
                        if (maxEUOutput() > ((GT_MetaTileEntity_Hatch_EnergyTunnel) aMetaTileEntity).maxEUInput()) {
                            aMetaTileEntity.doExplosion(maxEUOutput());
                            setEUVar(aBaseMetaTileEntity.getStoredEU() - maxEUOutput());
                            return;
                        } else if (maxEUOutput()
                            == ((GT_MetaTileEntity_Hatch_EnergyTunnel) aMetaTileEntity).maxEUInput()) {
                                long diff = Math.min(
                                    Amperes * 20L * maxEUOutput(),
                                    Math.min(
                                        ((GT_MetaTileEntity_Hatch_EnergyTunnel) aMetaTileEntity).maxEUStore()
                                            - aMetaTileEntity.getBaseMetaTileEntity()
                                                .getStoredEU(),
                                        aBaseMetaTileEntity.getStoredEU()));

                                setEUVar(aBaseMetaTileEntity.getStoredEU() - diff);

                                ((GT_MetaTileEntity_Hatch_EnergyTunnel) aMetaTileEntity).setEUVar(
                                    aMetaTileEntity.getBaseMetaTileEntity()
                                        .getStoredEU() + diff);
                            }
                        return;
                    } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Pipe_Energy) {
                        if (((GT_MetaTileEntity_Pipe_Energy) aMetaTileEntity).connectionCount < 2) {
                            return;
                        } else {
                            ((GT_MetaTileEntity_Pipe_Energy) aMetaTileEntity).markUsed();
                        }
                    } else {
                        return;
                    }
                } else {
                    if (tGTTileEntity instanceof PowerLogicHost) {
                        PowerLogic logic = ((PowerLogicHost) tGTTileEntity).getPowerLogic(opposite);
                        if (logic == null || !logic.canUseLaser() || opposite != tGTTileEntity.getFrontFacing()) {
                            return;
                        }

                        long ampsUsed = logic.injectEnergy(maxEUOutput(), Amperes);
                        setEUVar(aBaseMetaTileEntity.getStoredEU() - ampsUsed * maxEUOutput());
                    }
                    return;
                }
            } else {
                return;
            }
        }
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.setBackground(GT_UITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        final int x = getGUIWidth() / 2 - 37;
        final int y = getGUIHeight() / 5 - 7;
        builder.widget(
            TextWidget.localised("GT5U.machines.laser_hatch.amperage")
                .setPos(x, y)
                .setSize(74, 14))
            .widget(
                new NumericWidget().setSetter(val -> Amperes = (int) val)
                    .setGetter(() -> Amperes)
                    .setBounds(1, maxAmperes)
                    .setScrollValues(1, 4, 64)
                    .setTextAlignment(Alignment.Center)
                    .setTextColor(Color.WHITE.normal)
                    .setSize(70, 18)
                    .setPos(x, y + 16)
                    .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD));
    }

    @Override
    public boolean canConnect(ForgeDirection side) {
        return isOutputFacing(side);
    }
}
