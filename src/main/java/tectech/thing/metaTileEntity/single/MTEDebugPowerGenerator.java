package tectech.thing.metaTileEntity.single;

import static gregtech.api.enums.GTValues.VN;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.NumericWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUIInfos;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.objects.GTRenderedTexture;
import gregtech.api.util.GTUtility;
import tectech.mechanics.pipe.IConnectsToEnergyTunnel;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyTunnel;
import tectech.thing.metaTileEntity.pipe.MTEPipeEnergy;
import tectech.util.CommonValues;
import tectech.util.TTUtility;

/**
 * Created by Tec on 23.03.2017.
 */
public class MTEDebugPowerGenerator extends MTETieredMachineBlock
    implements IConnectsToEnergyTunnel, IAddUIWidgets, IAddGregtechLogo {

    public static GTRenderedTexture GENNY;
    private boolean LASER = false;
    public int EUT = 0, AMP = 0;
    public boolean producing = true;
    private static final NumberFormatMUI numberFormat = new NumberFormatMUI();

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
        TTUtility.setTier(aTier, this);
    }

    public MTEDebugPowerGenerator(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
        TTUtility.setTier(aTier, this);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEDebugPowerGenerator(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        LASER = !LASER;
        GTUtility.sendChatToPlayer(
            aPlayer,
            String.format(StatCollector.translateToLocal("tt.chat.debug.generator"), LASER ? "ON" : "OFF"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        GENNY = new GTRenderedTexture(new Textures.BlockIcons.CustomIcon("iconsets/GENNY"));
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        return new ITexture[] { tectech.thing.metaTileEntity.Textures.MACHINE_CASINGS_TT[mTier][colorIndex + 1],
            side != facing
                ? LASER
                    ? (aActive ? tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_OUT_LASER_TT[mTier]
                        : tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_IN_LASER_TT[mTier])
                    : (aActive ? tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_OUT_POWER_TT[mTier]
                        : tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_IN_POWER_TT[mTier])
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
                if (aBaseMetaTileEntity.isActive() && CommonValues.TRANSFER_AT == Tick) {
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
        GTUIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
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
    public boolean isInputFacing(ForgeDirection side) {
        return !producing && side != getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return producing && side != getBaseMetaTileEntity().getFrontFacing();
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

    public int getEUT() {
        return EUT;
    }

    public void setEUT(int EUT) {
        this.EUT = EUT;
    }

    public int getAMP() {
        return AMP;
    }

    public void setAMP(int AMP) {
        this.AMP = AMP;
    }

    @Override
    public boolean canConnect(ForgeDirection side) {
        return LASER && side != getBaseMetaTileEntity().getFrontFacing();
    }

    private void moveAround(IGregTechTileEntity aBaseMetaTileEntity) {
        for (final ForgeDirection face : ForgeDirection.VALID_DIRECTIONS) {
            if (face == aBaseMetaTileEntity.getFrontFacing()) continue;
            final ForgeDirection opposite = face.getOpposite();
            for (short dist = 1; dist < 1000; dist++) {
                IGregTechTileEntity tGTTileEntity = aBaseMetaTileEntity
                    .getIGregTechTileEntityAtSideAndDistance(face, dist);
                if (tGTTileEntity != null) {
                    IMetaTileEntity aMetaTileEntity = tGTTileEntity.getMetaTileEntity();
                    if (aMetaTileEntity != null) {
                        if (aMetaTileEntity instanceof MTEHatchEnergyTunnel
                            && opposite == tGTTileEntity.getFrontFacing()) {
                            if (maxEUOutput() > ((MTEHatchEnergyTunnel) aMetaTileEntity).maxEUInput()) {
                                aMetaTileEntity.doExplosion(maxEUOutput());
                            } else {
                                long diff = Math.min(
                                    AMP * 20L * maxEUOutput(),
                                    Math.min(
                                        ((MTEHatchEnergyTunnel) aMetaTileEntity).maxEUStore()
                                            - aMetaTileEntity.getBaseMetaTileEntity()
                                                .getStoredEU(),
                                        aBaseMetaTileEntity.getStoredEU()));
                                ((MTEHatchEnergyTunnel) aMetaTileEntity).setEUVar(
                                    aMetaTileEntity.getBaseMetaTileEntity()
                                        .getStoredEU() + diff);
                            }
                        } else if (aMetaTileEntity instanceof MTEPipeEnergy) {
                            if (((MTEPipeEnergy) aMetaTileEntity).connectionCount < 2) {} else {
                                ((MTEPipeEnergy) aMetaTileEntity).markUsed();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.PICTURE_GT_LOGO_17x17_TRANSPARENT_GRAY)
                .setSize(17, 17)
                .setPos(113, 56));
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.PICTURE_SCREEN_BLACK)
                .setSize(90, 72)
                .setPos(43, 4))

            .widget(
                new TextWidget().setStringSupplier(() -> "TIER: " + VN[TTUtility.getTier(Math.abs(EUT))])
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(46, 22))

            .widget(
                new TextWidget().setStringSupplier(() -> "SUM: " + numberFormat.format((long) AMP * EUT))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(46, 46));

        addLabelledIntegerTextField(builder, "EUT: ", 24, this::getEUT, this::setEUT, 46, 8);
        addLabelledIntegerTextField(builder, "AMP: ", 24, this::getAMP, this::setAMP, 46, 34);

        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_MINUS_LARGE, val -> EUT -= val, 512, 64, 7, 4);
        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_MINUS_LARGE, val -> EUT /= val, 512, 64, 7, 22);
        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_MINUS_LARGE, val -> AMP -= val, 512, 64, 7, 40);
        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_MINUS_LARGE, val -> AMP /= val, 512, 64, 7, 58);

        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_MINUS_SMALL, val -> EUT -= val, 16, 1, 25, 4);
        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_MINUS_SMALL, val -> EUT /= val, 16, 2, 25, 22);
        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_MINUS_SMALL, val -> AMP -= val, 16, 1, 25, 40);
        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_MINUS_SMALL, val -> AMP /= val, 16, 2, 25, 58);

        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_PLUS_SMALL, val -> EUT += val, 16, 1, 133, 4);
        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_PLUS_SMALL, val -> EUT *= val, 16, 2, 133, 22);
        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_PLUS_SMALL, val -> AMP += val, 16, 1, 133, 40);
        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_PLUS_SMALL, val -> AMP *= val, 16, 2, 133, 58);

        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_PLUS_LARGE, val -> EUT += val, 512, 64, 151, 4);
        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_PLUS_LARGE, val -> EUT *= val, 512, 64, 151, 22);
        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_PLUS_LARGE, val -> AMP += val, 512, 64, 151, 40);
        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_PLUS_LARGE, val -> AMP *= val, 512, 64, 151, 58);
    }

    private void addLabelledIntegerTextField(ModularWindow.Builder builder, String label, int labelWidth,
        IntSupplier getter, IntConsumer setter, int xPos, int yPos) {
        builder.widget(
            new TextWidget(label).setDefaultColor(COLOR_TEXT_WHITE.get())
                .setPos(xPos, yPos))
            .widget(
                new NumericWidget().setGetter(getter::getAsInt)
                    .setSetter(val -> setter.accept((int) val))
                    .setTextColor(COLOR_TEXT_WHITE.get())
                    .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD.withOffset(-1, -1, 2, 2))
                    .setPos(xPos + labelWidth, yPos - 1)
                    .setSize(56, 10));
    }

    private void addChangeNumberButton(ModularWindow.Builder builder, IDrawable overlay, Consumer<Integer> setter,
        int changeNumberShift, int changeNumber, int xPos, int yPos) {
        builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
            setter.accept(clickData.shift ? changeNumberShift : changeNumber);
            producing = (long) AMP * EUT >= 0;
        })
            .setBackground(GTUITextures.BUTTON_STANDARD, overlay)
            .setSize(18, 18)
            .setPos(xPos, yPos));
    }
}
