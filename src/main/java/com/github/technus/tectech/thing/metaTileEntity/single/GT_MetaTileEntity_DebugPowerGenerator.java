package com.github.technus.tectech.thing.metaTileEntity.single;

import static com.github.technus.tectech.thing.metaTileEntity.Textures.MACHINE_CASINGS_TT;
import static com.github.technus.tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_IN_LASER_TT;
import static com.github.technus.tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_IN_POWER_TT;
import static com.github.technus.tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_OUT_LASER_TT;
import static com.github.technus.tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_OUT_POWER_TT;
import static com.github.technus.tectech.util.CommonValues.TRANSFER_AT;
import static com.github.technus.tectech.util.CommonValues.VN;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.function.Consumer;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.github.technus.tectech.mechanics.pipe.IConnectsToEnergyTunnel;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyTunnel;
import com.github.technus.tectech.thing.metaTileEntity.pipe.GT_MetaTileEntity_Pipe_Energy;
import com.github.technus.tectech.util.CommonValues;
import com.github.technus.tectech.util.TT_Utility;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;

/**
 * Created by Tec on 23.03.2017.
 */
public class GT_MetaTileEntity_DebugPowerGenerator extends GT_MetaTileEntity_TieredMachineBlock
        implements IConnectsToEnergyTunnel, IAddUIWidgets, IAddGregtechLogo {

    private static GT_RenderedTexture GENNY;
    private boolean LASER = false;
    public int EUT = 0, AMP = 0;
    public boolean producing = true;

    public GT_MetaTileEntity_DebugPowerGenerator(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "");
        TT_Utility.setTier(aTier, this);
    }

    public GT_MetaTileEntity_DebugPowerGenerator(String aName, int aTier, String aDescription,
            ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
        TT_Utility.setTier(aTier, this);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_DebugPowerGenerator(mName, mTier, mDescription, mTextures);
    }

    @Override
    public final void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        LASER = !LASER;
        GT_Utility.sendChatToPlayer(
                aPlayer,
                String.format(StatCollector.translateToLocal("tt.chat.debug.generator"), LASER ? "ON" : "OFF"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        GENNY = new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("iconsets/GENNY"));
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex,
            boolean aActive, boolean aRedstone) {
        return new ITexture[] { MACHINE_CASINGS_TT[mTier][aColorIndex + 1],
                aSide != aFacing
                        ? LASER ? (aActive ? OVERLAYS_ENERGY_OUT_LASER_TT[mTier] : OVERLAYS_ENERGY_IN_LASER_TT[mTier])
                                : (aActive ? OVERLAYS_ENERGY_OUT_POWER_TT[mTier] : OVERLAYS_ENERGY_IN_POWER_TT[mTier])
                        : GENNY };
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return null;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity iGregTechTileEntity, int i, byte b, ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity iGregTechTileEntity, int i, byte b, ItemStack itemStack) {
        return false;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("eEUT", EUT);
        aNBT.setInteger("eAMP", AMP);
        aNBT.setBoolean("eLaser", LASER);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        EUT = aNBT.getInteger("eEUT");
        AMP = aNBT.getInteger("eAMP");
        LASER = aNBT.getBoolean("eLaser");
        producing = (long) AMP * EUT >= 0;
        getBaseMetaTileEntity().setActive(producing);
    }

    @Override
    public boolean isSimpleMachine() {
        return false;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            aBaseMetaTileEntity.setActive(producing);
            if (!LASER) {
                if (aBaseMetaTileEntity.isActive()) {
                    setEUVar(maxEUStore());
                } else {
                    setEUVar(0);
                }
            } else {
                byte Tick = (byte) (aTick % 20);
                if (aBaseMetaTileEntity.isActive() && TRANSFER_AT == Tick) {
                    setEUVar(maxEUStore());
                    moveAround(aBaseMetaTileEntity);
                } else if (TRANSFER_AT == Tick) {
                    setEUVar(0);
                }
            }
        }
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public String[] getDescription() {
        return new String[] { CommonValues.TEC_MARK_GENERAL, translateToLocal("gt.blockmachines.debug.tt.genny.desc.0"), // Power
                                                                                                                         // from
                                                                                                                         // nothing
                EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.debug.tt.genny.desc.3"), //
                EnumChatFormatting.BLUE + translateToLocal("gt.blockmachines.debug.tt.genny.desc.1"), // Infinite
                                                                                                      // Producer/Consumer
                EnumChatFormatting.BLUE + translateToLocal("gt.blockmachines.debug.tt.genny.desc.2") // Since i wanted
                                                                                                     // one...
        };
    }

    @Override
    public boolean isElectric() {
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
    public boolean isInputFacing(byte aSide) {
        return !producing && aSide != getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isOutputFacing(byte aSide) {
        return producing && aSide != getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public long maxAmperesIn() {
        return producing ? 0 : Math.abs(AMP);
    }

    @Override
    public long maxAmperesOut() {
        return producing ? Math.abs(AMP) : 0;
    }

    @Override
    public long maxEUInput() {
        return producing ? 0 : Integer.MAX_VALUE;
    }

    @Override
    public long maxEUOutput() {
        return producing ? Math.abs(EUT) : 0;
    }

    @Override
    public long maxEUStore() {
        return LASER ? Math.abs((long) EUT * AMP * 24) : Math.abs((long) EUT * AMP) << 2;
    }

    @Override
    public long getMinimumStoredEU() {
        return Math.abs((long) EUT * AMP);
    }

    @Override
    public int getProgresstime() {
        return (int) getBaseMetaTileEntity().getUniversalEnergyStored();
    }

    @Override
    public int maxProgresstime() {
        return (int) getBaseMetaTileEntity().getUniversalEnergyCapacity();
    }

    @Override
    public boolean canConnect(byte side) {
        return LASER && side != getBaseMetaTileEntity().getFrontFacing();
    }

    private void moveAround(IGregTechTileEntity aBaseMetaTileEntity) {
        for (byte face = 0; face < 6; face++) {
            if (face == aBaseMetaTileEntity.getFrontFacing()) continue;
            byte opposite = GT_Utility.getOppositeSide(face);
            for (short dist = 1; dist < 1000; dist++) {
                IGregTechTileEntity tGTTileEntity = aBaseMetaTileEntity
                        .getIGregTechTileEntityAtSideAndDistance(face, dist);
                if (tGTTileEntity != null) {
                    IMetaTileEntity aMetaTileEntity = tGTTileEntity.getMetaTileEntity();
                    if (aMetaTileEntity != null) {
                        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyTunnel
                                && opposite == tGTTileEntity.getFrontFacing()) {
                            if (maxEUOutput() > ((GT_MetaTileEntity_Hatch_EnergyTunnel) aMetaTileEntity).maxEUInput()) {
                                aMetaTileEntity.doExplosion(maxEUOutput());
                            } else {
                                long diff = Math.min(
                                        AMP * 20L * maxEUOutput(),
                                        Math.min(
                                                ((GT_MetaTileEntity_Hatch_EnergyTunnel) aMetaTileEntity).maxEUStore()
                                                        - aMetaTileEntity.getBaseMetaTileEntity().getStoredEU(),
                                                aBaseMetaTileEntity.getStoredEU()));
                                ((GT_MetaTileEntity_Hatch_EnergyTunnel) aMetaTileEntity)
                                        .setEUVar(aMetaTileEntity.getBaseMetaTileEntity().getStoredEU() + diff);
                            }
                        } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Pipe_Energy) {
                            if (((GT_MetaTileEntity_Pipe_Energy) aMetaTileEntity).connectionCount < 2) {} else {
                                ((GT_MetaTileEntity_Pipe_Energy) aMetaTileEntity).markUsed();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
                new DrawableWidget().setDrawable(GT_UITextures.PICTURE_GT_LOGO_17x17_TRANSPARENT_GRAY).setSize(17, 17)
                        .setPos(113, 56));
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
                new DrawableWidget().setDrawable(GT_UITextures.PICTURE_SCREEN_BLACK).setSize(90, 72).setPos(43, 4))
                .widget(
                        TextWidget.dynamicString(() -> "EUT: " + EUT).setDefaultColor(COLOR_TEXT_WHITE.get())
                                .setPos(46, 8))
                .widget(
                        TextWidget.dynamicString(() -> "TIER: " + VN[TT_Utility.getTier(Math.abs(EUT))])
                                .setDefaultColor(COLOR_TEXT_WHITE.get()).setPos(46, 16))
                .widget(
                        TextWidget.dynamicString(() -> "AMP: " + AMP).setDefaultColor(COLOR_TEXT_WHITE.get())
                                .setPos(46, 24))
                .widget(
                        TextWidget.dynamicString(() -> "SUM: " + (long) AMP * EUT)
                                .setDefaultColor(COLOR_TEXT_WHITE.get()).setPos(46, 32));

        addChangeNumberButton(builder, GT_UITextures.OVERLAY_BUTTON_MINUS_LARGE, val -> EUT -= val, 512, 64, 7, 4);
        addChangeNumberButton(builder, GT_UITextures.OVERLAY_BUTTON_MINUS_LARGE, val -> EUT /= val, 512, 64, 7, 22);
        addChangeNumberButton(builder, GT_UITextures.OVERLAY_BUTTON_MINUS_LARGE, val -> AMP -= val, 512, 64, 7, 40);
        addChangeNumberButton(builder, GT_UITextures.OVERLAY_BUTTON_MINUS_LARGE, val -> AMP /= val, 512, 64, 7, 58);

        addChangeNumberButton(builder, GT_UITextures.OVERLAY_BUTTON_MINUS_SMALL, val -> EUT -= val, 16, 1, 25, 4);
        addChangeNumberButton(builder, GT_UITextures.OVERLAY_BUTTON_MINUS_SMALL, val -> EUT /= val, 16, 2, 25, 22);
        addChangeNumberButton(builder, GT_UITextures.OVERLAY_BUTTON_MINUS_SMALL, val -> AMP -= val, 16, 1, 25, 40);
        addChangeNumberButton(builder, GT_UITextures.OVERLAY_BUTTON_MINUS_SMALL, val -> AMP /= val, 16, 2, 25, 58);

        addChangeNumberButton(builder, GT_UITextures.OVERLAY_BUTTON_PLUS_SMALL, val -> EUT += val, 16, 1, 133, 4);
        addChangeNumberButton(builder, GT_UITextures.OVERLAY_BUTTON_PLUS_SMALL, val -> EUT *= val, 16, 2, 133, 22);
        addChangeNumberButton(builder, GT_UITextures.OVERLAY_BUTTON_PLUS_SMALL, val -> AMP += val, 16, 1, 133, 40);
        addChangeNumberButton(builder, GT_UITextures.OVERLAY_BUTTON_PLUS_SMALL, val -> AMP *= val, 16, 2, 133, 58);

        addChangeNumberButton(builder, GT_UITextures.OVERLAY_BUTTON_PLUS_LARGE, val -> EUT += val, 512, 64, 151, 4);
        addChangeNumberButton(builder, GT_UITextures.OVERLAY_BUTTON_PLUS_LARGE, val -> EUT *= val, 512, 64, 151, 22);
        addChangeNumberButton(builder, GT_UITextures.OVERLAY_BUTTON_PLUS_LARGE, val -> AMP += val, 512, 64, 151, 40);
        addChangeNumberButton(builder, GT_UITextures.OVERLAY_BUTTON_PLUS_LARGE, val -> AMP *= val, 512, 64, 151, 58);
    }

    private void addChangeNumberButton(ModularWindow.Builder builder, IDrawable overlay, Consumer<Integer> setter,
            int changeNumberShift, int changeNumber, int xPos, int yPos) {
        builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
            setter.accept(clickData.shift ? changeNumberShift : changeNumber);
            producing = (long) AMP * EUT >= 0;
        }).setBackground(GT_UITextures.BUTTON_STANDARD, overlay).setSize(18, 18).setPos(xPos, yPos));
    }
}
