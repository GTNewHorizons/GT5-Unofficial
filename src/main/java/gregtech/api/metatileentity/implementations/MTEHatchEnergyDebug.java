package gregtech.api.metatileentity.implementations;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.BOLD;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.GREEN;
import static gregtech.api.enums.GTValues.TIER_COLORS;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.modularui2.GTGuis;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;

public class MTEHatchEnergyDebug extends MTEHatchEnergy {

    public MTEHatchEnergyDebug(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEHatchEnergyDebug(String aName, byte aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public long getInputTier() {
        return voltageTier;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_ENERGY_IN_DEBUG) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_ENERGY_IN_DEBUG) };
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchEnergyDebug(mName, mTier, new String[] { "" }, mTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("debugAmperage", amperage);
        aNBT.setInteger("debugVTier", voltageTier);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        amperage = aNBT.getInteger("debugAmperage");
        voltageTier = aNBT.getInteger("debugVTier");
    }

    @Override
    public String[] getDescription() {
        return new String[] { EnumChatFormatting.GRAY + "Buffers EU internally",
            EnumChatFormatting.GRAY + "Configure Amps and Voltage in the UI",
            EnumChatFormatting.ITALIC + "/gt global_energy_set Develo-... begone!",
            "Author: " + GREEN + BOLD + "Chrom" };
    }

    @Override
    public boolean isEnetInput() {
        return false;
    }

    // 0 = ulv -> 14 MAX+
    private int voltageTier = 15;
    private int amperage = 2;

    @Override
    public long getMinimumStoredEU() {
        return amperage * V[voltageTier];
    }

    @Override
    public long maxEUStore() {
        return Long.MAX_VALUE;
    }

    @Override
    public long maxEUInput() {
        return V[voltageTier];
    }

    @Override
    public long maxAmperesIn() {
        return amperage;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity baseMetaTileEntity) {
        if (baseMetaTileEntity.isServerSide()) fetchEnergy();
    }

    @Override
    public void doExplosion(long aExplosionPower) {
        return;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            // refill entirely every 30 seconds
            if (aTick % 30 * SECONDS == 0L) {
                fetchEnergy();
            }
        }
    }

    private void fetchEnergy() {
        setEUVar(Long.MAX_VALUE);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    private TextFieldWidget createNumberTextField() {
        return new TextFieldWidget().setTextAlignment(Alignment.CenterRight)
            .setFormatAsInteger(true)
            .height(18)
            .marginRight(2);
    }

    private boolean onVoltageModifierButtonPressed(int mouseButton, IntSyncValue voltageTierSyncer) {

        int changedTier = voltageTierSyncer.getIntValue();
        switch (mouseButton) {
            case 0 -> changedTier = Math.min(changedTier + 1, 15);
            case 1 -> changedTier = Math.max(0, changedTier - 1);
        }
        voltageTierSyncer.setValue(changedTier);
        return true;
    }

    private void createVoltageModifierButtonTooltip(RichTooltip t) {
        t.addLine(
            EnumChatFormatting.AQUA + "Left"
                + EnumChatFormatting.RESET
                + "/"
                + EnumChatFormatting.RED
                + "Right"
                + EnumChatFormatting.RESET
                + " Click to "
                + EnumChatFormatting.AQUA
                + "Increment"
                + EnumChatFormatting.RESET
                + "/"
                + EnumChatFormatting.RED
                + "Decrement"
                + EnumChatFormatting.RESET
                + " Tier");
    }

    private void createAmperageModifierButtonTooltip(RichTooltip t) {
        t.addLine(
            EnumChatFormatting.AQUA + "Left"
                + EnumChatFormatting.RESET
                + "/"
                + EnumChatFormatting.RED
                + "Right"
                + EnumChatFormatting.RESET
                + " Click to "
                + EnumChatFormatting.AQUA
                + "Double"
                + EnumChatFormatting.RESET
                + "/"
                + EnumChatFormatting.RED
                + "Halve"
                + EnumChatFormatting.RESET
                + " Amperage");
        t.addLine(EnumChatFormatting.GRAY + "" + EnumChatFormatting.ITALIC + "Max Amperage of 536,870,912");
    }

    final int MAX_AMPERAGE = 536870912;

    private boolean onAmperageModifierButtonPressed(int mouseButton, IntSyncValue amperageSyncer) {

        int changedAmperage = amperageSyncer.getIntValue();
        switch (mouseButton) {
            case 0 -> changedAmperage = GTUtility.clamp(changedAmperage * 2, 1, MAX_AMPERAGE);
            case 1 -> changedAmperage = Math.max(1, changedAmperage / 2);
        }
        amperageSyncer.setValue(changedAmperage);
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {

        IntSyncValue voltageTierSyncer = new IntSyncValue(() -> voltageTier, tier -> voltageTier = tier);
        IntSyncValue amperageSyncer = new IntSyncValue(() -> amperage, amp -> amperage = amp);

        Flow numberInputColumn = Flow.column();
        numberInputColumn.widthRel(1f)
            .height(18 * 2 + 8)
            .paddingTop(4);

        Flow voltageRow = Flow.row()
            .height(18)
            .coverChildrenWidth()
            .left(4)
            .marginBottom(4);

        // add a number input field to determine voltage tier
        voltageRow.child(
            createNumberTextField().width(40)
                .setNumbers(0, 15)
                .value(voltageTierSyncer)
                .setDefaultNumber(0));

        // add the changing tier description widget
        voltageRow.child(IKey.dynamic(() -> {
            int clampedTier = GTUtility.clamp(voltageTierSyncer.getIntValue(), 0, TIER_COLORS.length - 1);
            String color = GTValues.TIER_COLORS[clampedTier];
            return IKey.lang(
                "GT5U.gui.text.voltagetier") + " (" + color + GTValues.VN[clampedTier] + EnumChatFormatting.RESET + ")";
        })
            .asWidget()
            .width(80)
            .height(18)
            .marginRight(2));

        // add a button to increment / decrement voltage tier
        voltageRow.child(
            new ButtonWidget<>().overlay(GuiTextures.GRAPH)
                .size(18)
                .onMousePressed(mouseButton -> this.onVoltageModifierButtonPressed(mouseButton, voltageTierSyncer))
                .tooltip(this::createVoltageModifierButtonTooltip));

        Flow amperageRow = Flow.row()
            .height(18)
            .left(4)
            .coverChildrenWidth();

        // number field for amperage
        amperageRow.child(
            createNumberTextField().width(60)
                .setNumbers(1, MAX_AMPERAGE)
                .value(amperageSyncer)
                .setDefaultNumber(2));

        // text widget for Amperage, is static. width is larger for nice spacing
        amperageRow.child(
            new TextWidget<>(IKey.lang("GT5U.gui.text.amperage")).width(60)
                .height(18)
                .marginRight(2));

        // button to double / halve amperage, up to 536,870,912
        amperageRow.child(
            new ButtonWidget<>().overlay(GuiTextures.MAZE)
                .size(18)
                .onMousePressed(mouseButton -> onAmperageModifierButtonPressed(mouseButton, amperageSyncer))
                .tooltip(this::createAmperageModifierButtonTooltip));

        numberInputColumn.child(voltageRow);
        numberInputColumn.child(amperageRow);

        return GTGuis.mteTemplatePanelBuilder(this, data, syncManager, uiSettings)
            .doesAddGregTechLogo(true)
            .build()
            .child(numberInputColumn);
    }

}
