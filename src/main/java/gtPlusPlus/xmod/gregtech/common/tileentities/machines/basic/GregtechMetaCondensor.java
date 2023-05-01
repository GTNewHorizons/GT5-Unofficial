package gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.gui.GTPP_UITextures;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.generators.GregtechMetaBoilerBase;

public class GregtechMetaCondensor extends GregtechMetaBoilerBase implements IAddGregtechLogo {

    public GregtechMetaCondensor(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional, "A Steam condenser - [IC2->Steam]", new ITexture[0]);
    }

    public GregtechMetaCondensor(final String aName, final int aTier, final String aDescription,
            final ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        return new String[] { this.mDescription, "IC2 Steam + Water = Normal Steam.",
                "Requires no power to run, although it's not very fast.", CORE.GT_Tooltip.get() };
    }

    @Override
    public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
        final ITexture[][][] rTextures = new ITexture[5][17][];
        for (byte i = -1; i < 16; i++) {
            rTextures[0][(i + 1)] = new ITexture[] { new GT_RenderedTexture(
                    Textures.BlockIcons.MACHINE_CASING_VENT,
                    Dyes.getModulation(i, Dyes.MACHINE_METAL.mRGBa)) };
            rTextures[1][(i + 1)] = new ITexture[] {
                    new GT_RenderedTexture(
                            Textures.BlockIcons.MACHINE_CASING_VENT,
                            Dyes.getModulation(i, Dyes.MACHINE_METAL.mRGBa)),
                    new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE) };
            rTextures[2][(i + 1)] = new ITexture[] {
                    new GT_RenderedTexture(
                            Textures.BlockIcons.MACHINE_CASING_VENT,
                            Dyes.getModulation(i, Dyes.MACHINE_METAL.mRGBa)),
                    new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE) };
            rTextures[3][(i + 1)] = new ITexture[] {
                    new GT_RenderedTexture(
                            Textures.BlockIcons.MACHINE_CASING_VENT,
                            Dyes.getModulation(i, Dyes.MACHINE_METAL.mRGBa)),
                    new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_POTIONBREWER) };
            rTextures[4][(i + 1)] = new ITexture[] {
                    new GT_RenderedTexture(
                            Textures.BlockIcons.MACHINE_CASING_VENT,
                            Dyes.getModulation(i, Dyes.MACHINE_METAL.mRGBa)),
                    new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_POTIONBREWER_ACTIVE) };
        }
        return rTextures;
    }

    @Override
    public int maxProgresstime() {
        return 1000;
    }

    @Override
    public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMetaCondensor(this.mName, this.mTier, this.mDescription, this.mTextures);
    }

    @Override
    public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
        this.RI = MathUtils.randLong(5L, 30L);
        if ((aBaseMetaTileEntity.isServerSide()) && (aTick > 20L)) {
            if (this.mTemperature <= 5) {
                this.mTemperature = 5;
                this.mLossTimer = 0;
            }
            if (++this.mLossTimer > 10) {
                this.mTemperature -= 1;
                this.mLossTimer = 0;
            }
            for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                if (this.mSteam == null) break;
                if (side != aBaseMetaTileEntity.getFrontFacing()) {
                    final IFluidHandler tTileEntity = aBaseMetaTileEntity.getITankContainerAtSide(side);
                    if (tTileEntity != null) {
                        final FluidStack tDrained = aBaseMetaTileEntity
                                .drain(side, Math.max(1, this.mSteam.amount / 2), false);
                        if (tDrained != null) {
                            final int tFilledAmount = tTileEntity.fill(side.getOpposite(), tDrained, false);
                            if (tFilledAmount > 0) {
                                tTileEntity.fill(
                                        side.getOpposite(),
                                        aBaseMetaTileEntity.drain(side, tFilledAmount, true),
                                        true);
                            }
                        }
                    }
                }
            }
            if ((aTick % 10L) == 0L) {
                if (this.mTemperature > 5) {
                    if ((this.mFluid == null) || (!GT_ModHandler.isWater(this.mFluid)) || (this.mFluid.amount <= 0)) {
                        this.mHadNoWater = true;
                    } else {
                        if (this.mHadNoWater) {
                            aBaseMetaTileEntity.doExplosion(2048L);
                            return;
                        }
                        this.mFluid.amount -= 1;
                        if (this.mSteam == null) {
                            this.mSteam = GT_ModHandler.getSteam(30L);
                        } else if (GT_ModHandler.isSteam(this.mSteam)) {
                            this.mSteam.amount += 30;
                        } else {
                            this.mSteam = GT_ModHandler.getSteam(30L);
                        }
                    }
                } else {
                    this.mHadNoWater = false;
                }
            }
            if ((this.mSteam != null) && (this.mSteam.amount > getSteamCapacity())) {
                this.sendSound((byte) 1);
                this.mSteam.amount = getSteamCapacity() * 3 / 4;
            }
            /*
             * if ((this.mProcessingEnergy <= 0) && (aBaseMetaTileEntity.isAllowedToWork()) &&
             * (GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[2],
             * OrePrefixes.bucket.get(IC2.getItemFromBlock(p_150898_0_))))) { this.mProcessingEnergy += 1000;
             * aBaseMetaTileEntity.decrStackSize(2, 1); aBaseMetaTileEntity.addStackToSlot(3,
             * GT_OreDictUnificator.get(OrePrefixes.bucket, Materials.Empty, 1L)); }
             */
            if ((this.mTemperature < 1000) && (this.mProcessingEnergy > 0) && ((aTick % this.RI) == 0L)) {
                this.mProcessingEnergy -= 40;
                this.mTemperature += 2;
            }
            aBaseMetaTileEntity.setActive(this.mProcessingEnergy > 0);
        }
    }

    @Override
    public final int fill(final FluidStack aFluid, final boolean doFill) {
        if ((Utils.isIC2Steam(aFluid)) && (this.mProcessingEnergy < 50)) {
            final int tFilledAmount = Math.min(50, aFluid.amount);
            if (doFill) {
                this.mProcessingEnergy += tFilledAmount;
            }
            return tFilledAmount;
        }
        return super.fill(aFluid, doFill);
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {}

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
                new SlotWidget(inventoryHandler, 0).setPos(43, 25)
                        .setBackground(getGUITextureSet().getItemSlot(), GT_UITextures.OVERLAY_SLOT_IN))
                .widget(
                        new SlotWidget(inventoryHandler, 1).setPos(43, 61)
                                .setBackground(getGUITextureSet().getItemSlot(), GT_UITextures.OVERLAY_SLOT_OUT))
                .widget(
                        new SlotWidget(inventoryHandler, 2).setPos(115, 61)
                                .setBackground(getGUITextureSet().getItemSlot(), GTPP_UITextures.OVERLAY_SLOT_COAL))
                .widget(
                        new SlotWidget(inventoryHandler, 3).setPos(115, 25)
                                .setBackground(getGUITextureSet().getItemSlot(), GT_UITextures.OVERLAY_SLOT_DUST))
                .widget(
                        new ProgressBar().setProgress(() -> mSteam == null ? 0 : (float) mSteam.amount / getCapacity())
                                .setTexture(
                                        GTPP_UITextures.PROGRESSBAR_BOILER_EMPTY,
                                        GT_UITextures.PROGRESSBAR_BOILER_STEAM,
                                        10)
                                .setDirection(ProgressBar.Direction.UP).setPos(70, 25).setSize(10, 54))
                .widget(
                        new ProgressBar().setProgress(() -> mFluid == null ? 0 : (float) mFluid.amount / getCapacity())
                                .setTexture(
                                        GTPP_UITextures.PROGRESSBAR_BOILER_EMPTY,
                                        GT_UITextures.PROGRESSBAR_BOILER_WATER,
                                        10)
                                .setDirection(ProgressBar.Direction.UP).setPos(83, 25).setSize(10, 54))
                .widget(
                        new ProgressBar().setProgress(() -> (float) mTemperature / maxProgresstime())
                                .setTexture(
                                        GTPP_UITextures.PROGRESSBAR_BOILER_EMPTY,
                                        GT_UITextures.PROGRESSBAR_BOILER_HEAT,
                                        10)
                                .setDirection(ProgressBar.Direction.UP).setPos(96, 25).setSize(10, 54))
                .widget(
                        new ProgressBar()
                                // cap minimum so that one can easily see there's fuel remaining
                                .setProgress(
                                        () -> mProcessingEnergy > 0 ? Math.max((float) mProcessingEnergy / 1000, 1f / 5)
                                                : 0)
                                .setTexture(GTPP_UITextures.PROGRESSBAR_FUEL, 14).setDirection(ProgressBar.Direction.UP)
                                .setPos(116, 45).setSize(14, 14))
                .widget(
                        new DrawableWidget().setDrawable(GTPP_UITextures.OVERLAY_SLOT_CANISTER_DARK).setPos(43, 43)
                                .setSize(18, 18));
    }
}
