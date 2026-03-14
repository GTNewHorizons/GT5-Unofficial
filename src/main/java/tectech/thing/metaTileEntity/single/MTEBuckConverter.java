package tectech.thing.metaTileEntity.single;

import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.GTValues.VN;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.function.IntConsumer;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import bartworks.util.MathUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.common.modularui2.factory.GTBaseGuiBuilder;
import tectech.thing.gui.DebugUIHelper;
import tectech.util.CommonValues;

public class MTEBuckConverter extends MTETieredMachineBlock implements IAddUIWidgets, IAddGregtechLogo {

    private static ITexture BUCK, BUCK_ACTIVE;
    public int EUT = 0, AMP = 0;
    private static final NumberFormatMUI numberFormat = new NumberFormatMUI();

    public MTEBuckConverter(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            0,
            new String[] { CommonValues.TEC_MARK_GENERAL, translateToLocal("gt.blockmachines.machine.tt.buck.desc.0"),
                EnumChatFormatting.BLUE + translateToLocal("gt.blockmachines.machine.tt.buck.desc.1"), });
    }

    public MTEBuckConverter(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBuckConverter(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        BUCK = TextureFactory.of(new Textures.BlockIcons.CustomIcon("iconsets/BUCK"));
        BUCK_ACTIVE = TextureFactory.of(new Textures.BlockIcons.CustomIcon("iconsets/BUCK_ACTIVE"));
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][colorIndex + 1],
            side == facing ? (aActive ? BUCK_ACTIVE : BUCK)
                : (side == facing.getOpposite() ? Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_16A[mTier + 1]
                    : (aActive ? Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_16A[mTier + 1]
                        : Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_16A[mTier + 1])) };
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
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        EUT = aNBT.getInteger("eEUT");
        AMP = aNBT.getInteger("eAMP");
        getBaseMetaTileEntity().setActive((long) AMP * EUT >= 0);
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
        return true;
    }

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getBackFacing();
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return getBaseMetaTileEntity().isActive() && side != getBaseMetaTileEntity().getFrontFacing()
            && side != getBaseMetaTileEntity().getBackFacing();
    }

    @Override
    public long maxAmperesIn() {
        return 2;
    }

    @Override
    public long maxAmperesOut() {
        return getBaseMetaTileEntity().isActive() ? Math.min(Math.abs(AMP), 64) : 0;
    }

    @Override
    public long maxEUInput() {
        return V[mTier];
    }

    @Override
    public long maxEUOutput() {
        return getBaseMetaTileEntity().isActive() ? Math.min(Math.abs(EUT), maxEUInput()) : 0;
    }

    @Override
    public long maxEUStore() {
        return V[mTier] << 4;
    }

    @Override
    public long getMinimumStoredEU() {
        return V[mTier] << 2;
    }

    @Override
    public int getProgresstime() {
        return (int) getBaseMetaTileEntity().getUniversalEnergyStored();
    }

    @Override
    public int maxProgresstime() {
        return (int) getBaseMetaTileEntity().getUniversalEnergyCapacity();
    }

    public void setEUT(int EUT) {
        // Same as maxEUOutput
        this.EUT = MathUtils.clamp(EUT, 0, (int) maxEUInput());
        setAMP(AMP); // refresh AMP since it might be too high for current EUT
        getBaseMetaTileEntity().setActive((long) this.AMP * this.EUT >= 0);
    }

    public void setAMP(int AMP) {
        // Max AMP should be limited to what the machine can theoretically output at current EUT
        this.AMP = MathUtils.clamp(AMP, 0, Math.min((int) (maxEUInput() / Math.max(EUT, 1)) * 2, 64));
        getBaseMetaTileEntity().setActive((long) this.AMP * this.EUT >= 0);
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
        builder.widget(new FakeSyncWidget.IntegerSyncer(() -> EUT, val -> EUT = val));
        builder.widget(new FakeSyncWidget.IntegerSyncer(() -> AMP, val -> AMP = val));

        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.PICTURE_SCREEN_BLACK)
                .setSize(90, 72)
                .setPos(43, 4))
            .widget(
                new TextWidget()
                    .setStringSupplier(() -> translateToLocal("tt.gui.text.debug.eut") + " " + numberFormat.format(EUT))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(46, 8))
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> translateToLocalFormatted("tt.gui.text.debug.tier", VN[GTUtility.getTier(Math.abs(EUT))]))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(46, 16))
            .widget(
                new TextWidget()
                    .setStringSupplier(() -> translateToLocal("tt.gui.text.debug.amp") + " " + numberFormat.format(AMP))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(46, 24))
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> translateToLocalFormatted("tt.gui.text.debug.sum", numberFormat.format((long) AMP * EUT)))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(46, 32));

        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_MINUS_LARGE, subDiv[0], 512, 64, 7, 4);
        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_MINUS_LARGE, subDiv[1], 512, 64, 7, 22);
        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_MINUS_LARGE, subDiv[2], 512, 64, 7, 40);
        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_MINUS_LARGE, subDiv[3], 512, 64, 7, 58);

        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_MINUS_SMALL, subDiv[0], 16, 1, 25, 4);
        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_MINUS_SMALL, subDiv[1], 16, 2, 25, 22);
        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_MINUS_SMALL, subDiv[2], 16, 1, 25, 40);
        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_MINUS_SMALL, subDiv[3], 16, 2, 25, 58);

        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_PLUS_SMALL, addMult[0], 16, 1, 133, 4);
        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_PLUS_SMALL, addMult[1], 16, 2, 133, 22);
        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_PLUS_SMALL, addMult[2], 16, 1, 133, 40);
        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_PLUS_SMALL, addMult[3], 16, 2, 133, 58);

        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_PLUS_LARGE, addMult[0], 512, 64, 151, 4);
        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_PLUS_LARGE, addMult[1], 512, 64, 151, 22);
        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_PLUS_LARGE, addMult[2], 512, 64, 151, 40);
        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_PLUS_LARGE, addMult[3], 512, 64, 151, 58);
    }

    private void addChangeNumberButton(ModularWindow.Builder builder, IDrawable overlay, IntConsumer setter,
        int changeNumberShift, int changeNumber, int xPos, int yPos) {
        builder.widget(
            new ButtonWidget()
                .setOnClick(
                    (clickData, widget) -> { setter.accept(clickData.shift ? changeNumberShift : changeNumber); })
                .setBackground(GTUITextures.BUTTON_STANDARD, overlay)
                .setSize(18, 18)
                .setPos(xPos, yPos));
    }

    private static final int extraGuiWidth = 10;
    private final IntConsumer[] subDiv = new IntConsumer[] { val -> setEUT(EUT - val), val -> setEUT(EUT / val),
        val -> setAMP(AMP - val), val -> setAMP(AMP / val) };
    private final IntConsumer[] addMult = new IntConsumer[] { val -> setEUT(EUT + val),
        val -> setEUT((int) Math.min((long) EUT * val, Integer.MAX_VALUE)), val -> setAMP(AMP + val),
        val -> setAMP(AMP * val) };

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new GTBaseGuiBuilder(this, data, syncManager, uiSettings).doesAddGregTechLogo(false)
            .doesAddGhostCircuitSlot(false)
            .setWidth(176 + extraGuiWidth)
            .build()
            .child(
                DebugUIHelper.getContentSection(getScreen(), subDiv, addMult, 512, 64, 16, new int[] { 1, 2, 1, 2 }));
    }

    private Flow getScreen() {
        return DebugUIHelper.getScreen(extraGuiWidth, 0, 3)
            .child(
                IKey.dynamic(() -> translateToLocal("tt.gui.text.debug.eut") + " " + numberFormat.format(EUT))
                    .asWidget()
                    .color(COLOR_TEXT_WHITE.get()))
            .child(
                IKey.dynamic(
                    () -> translateToLocalFormatted("tt.gui.text.debug.tier", VN[GTUtility.getTier(Math.abs(EUT))]))
                    .asWidget()
                    .color(COLOR_TEXT_WHITE.get()))
            .child(
                IKey.dynamic(() -> translateToLocal("tt.gui.text.debug.amp") + " " + numberFormat.format(AMP))
                    .asWidget()
                    .color(COLOR_TEXT_WHITE.get()))
            .child(
                new Column().child(
                    IKey.lang(translateToLocalFormatted("tt.gui.text.debug.sum", ""))
                        .asWidget()
                        .color(COLOR_TEXT_WHITE.get()))
                    .child(
                        IKey.dynamic(() -> numberFormat.format((long) AMP * EUT))
                            .asWidget()
                            .color(COLOR_TEXT_WHITE.get()))
                    .coverChildren()
                    .crossAxisAlignment(Alignment.CrossAxis.START));
    }
}
