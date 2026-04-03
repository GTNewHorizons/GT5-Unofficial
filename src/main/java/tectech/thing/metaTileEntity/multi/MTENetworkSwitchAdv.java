package tectech.thing.metaTileEntity.multi;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.util.GTUtility.validMTEList;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;
import static tectech.thing.CustomItemList.Machine_Multi_Switch;
import static tectech.thing.metaTileEntity.multi.base.TTMultiblockBase.HatchElement.DynamoMulti;
import static tectech.thing.metaTileEntity.multi.base.TTMultiblockBase.HatchElement.EnergyMulti;
import static tectech.thing.metaTileEntity.multi.base.TTMultiblockBase.HatchElement.InputData;
import static tectech.thing.metaTileEntity.multi.base.TTMultiblockBase.HatchElement.OutputData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.util.Vec3Impl;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.casing.Casings;
import gregtech.api.enums.StructureError;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.structure.IStructureInstance;
import gregtech.api.structure.IStructureProvider;
import gregtech.api.structure.StructureWrapper;
import gregtech.api.structure.StructureWrapperInstanceInfo;
import gregtech.api.structure.StructureWrapperTooltipBuilder;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.misc.GTStructureChannels;
import it.unimi.dsi.fastutil.Pair;
import tectech.mechanics.dataTransport.QuantumDataPacket;
import tectech.thing.metaTileEntity.hatch.MTEHatchDataInput;
import tectech.thing.metaTileEntity.hatch.MTEHatchDataOutput;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.thing.metaTileEntity.multi.base.render.TTRenderedExtendedFacingTexture;

public class MTENetworkSwitchAdv extends TTMultiblockBase
    implements ISurvivalConstructable, IStructureProvider<MTENetworkSwitchAdv> {

    private static final String STRUCTURE_SHAPE_FIRST = "first";
    private static final String STRUCTURE_SHAPE_MIDDLE = "middle";
    private static final String STRUCTURE_SHAPE_LAST = "last";

    private static final String[] FIRST = { "AAA", "A~A", "AAA" };

    private static final String[] MIDDLE = { "ACA", "CBC", "ACA" };

    private static final String[] LAST = { "AAA", "ACA", "AAA" };

    private static final int MAX_LENGTH = 16;

    protected final StructureWrapper<MTENetworkSwitchAdv> structure;
    protected final StructureWrapperInstanceInfo<MTENetworkSwitchAdv> structureInstanceInfo;

    private int length;
    private long pendingComputation, wastedComputation;

    public MTENetworkSwitchAdv(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);

        structure = new StructureWrapper<>(this);
        structureInstanceInfo = null;

        structure.loadStructure();
    }

    public MTENetworkSwitchAdv(MTENetworkSwitchAdv prototype) {
        super(prototype.mName);

        structure = prototype.structure;
        structureInstanceInfo = new StructureWrapperInstanceInfo<>(structure);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTENetworkSwitchAdv(this);
    }

    @Override
    public String[][] getDefinition() {
        return new String[][] { FIRST, MIDDLE, LAST };
    }

    @Override
    public String[][] getMaxDefinition() {
        ArrayList<String[]> slices = new ArrayList<>();

        slices.add(FIRST);

        for (int i = 0; i < MAX_LENGTH; i++) slices.add(MIDDLE);

        slices.add(LAST);

        return slices.toArray(new String[0][]);
    }

    @Override
    public IStructureDefinition<MTENetworkSwitchAdv> compile(String[][] definition) {
        structure.addCasing('A', Casings.ComputerCasing)
            .withUnlimitedHatches(1, Arrays.asList(Energy, EnergyMulti, Dynamo, DynamoMulti, OutputData));
        structure.addCasing('B', Casings.AdvancedComputerCasing);
        structure.addCasing('C', Casings.AdvancedComputerCasing)
            .withUnlimitedHatches(2, Arrays.asList(InputData, OutputData));

        var shapes = Arrays.asList(
            Pair.of(STRUCTURE_SHAPE_FIRST, new String[][] { FIRST }),
            Pair.of(STRUCTURE_SHAPE_MIDDLE, new String[][] { MIDDLE }),
            Pair.of(STRUCTURE_SHAPE_LAST, new String[][] { LAST }));

        return structure.getStructureBuilder(shapes)
            .build();
    }

    @Override
    public IStructureInstance getStructureInstance() {
        return structureInstanceInfo;
    }

    @Override
    public IStructureDefinition<? extends TTMultiblockBase> getStructure_EM() {
        return structure.structureDefinition;
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        int length = GTStructureChannels.STRUCTURE_LENGTH.getValueClamped(trigger, 1, MAX_LENGTH);

        Vec3Impl offset = new Vec3Impl(0, 0, 0);
        Vec3Impl inc = new Vec3Impl(0, 0, -1);

        structure.construct(this, trigger, hintsOnly, STRUCTURE_SHAPE_FIRST, offset);

        for (int i = 0; i < length; i++) {
            offset = offset.add(inc);

            structure.construct(this, trigger, hintsOnly, STRUCTURE_SHAPE_MIDDLE, offset);
        }

        offset = offset.add(inc);

        structure.construct(this, trigger, hintsOnly, STRUCTURE_SHAPE_LAST, offset);
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        int length = GTStructureChannels.STRUCTURE_LENGTH.getValueClamped(trigger, 1, MAX_LENGTH);

        Vec3Impl offset = new Vec3Impl(0, 0, 0);
        Vec3Impl inc = new Vec3Impl(0, 0, -1);

        int built = 0, temp;

        temp = structure.survivalConstruct(this, trigger, elementBudget - built, env, STRUCTURE_SHAPE_FIRST, offset);

        if (temp > -1) built += temp;
        if (elementBudget - built <= 0) return built;

        for (int i = 0; i < length; i++) {
            offset = offset.add(inc);

            temp = structure
                .survivalConstruct(this, trigger, elementBudget - built, env, STRUCTURE_SHAPE_MIDDLE, offset);

            if (temp > -1) built += temp;
            if (elementBudget - built <= 0) return built;
        }

        offset = offset.add(inc);

        temp = structure.survivalConstruct(this, trigger, elementBudget - built, env, STRUCTURE_SHAPE_LAST, offset);

        if (temp > -1) built += temp;

        return temp == -1 ? -1 : built;
    }

    private void resetDataHatches() {
        for (MTEHatchDataOutput output : validMTEList(eOutputData)) {
            output.allowComputationConfiguring = false;
        }
    }

    @Override
    public void onRemoval() {
        super.onRemoval();

        resetDataHatches();
    }

    @Override
    public void onUnload() {
        super.onUnload();

        resetDataHatches();
    }

    @Override
    protected void clearHatches_EM() {
        resetDataHatches();

        super.clearHatches_EM();

        this.length = 0;

        structureInstanceInfo.clearHatches();
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        Vec3Impl offset = new Vec3Impl(0, 0, 0);
        Vec3Impl inc = new Vec3Impl(0, 0, -1);

        if (!structure.checkStructure(this, STRUCTURE_SHAPE_FIRST, offset)) return false;

        offset = offset.add(inc);

        while (length < MAX_LENGTH && structure.checkStructure(this, STRUCTURE_SHAPE_MIDDLE, offset)) {
            length++;
            offset = offset.add(inc);
        }

        if (!structure.checkStructure(this, STRUCTURE_SHAPE_LAST, offset)) return false;

        for (MTEHatchDataOutput output : validMTEList(eOutputData)) {
            output.allowComputationConfiguring = true;
        }

        // Hack to prevent hatch duplication
        // The while-loop may add hatches several times since structure check hatch adding is not atomic.
        // If a check for a piece fails (e.g. the middle), hatches will still have been added, and there's no way to
        // roll back the hatch lists automatically when this happens.
        GTDataUtils.dedupList(mExoticEnergyHatches);
        GTDataUtils.dedupList(mEnergyHatches);
        GTDataUtils.dedupList(eEnergyMulti);
        GTDataUtils.dedupList(eDynamoMulti);
        GTDataUtils.dedupList(eInputData);
        GTDataUtils.dedupList(eOutputData);

        return true;
    }

    @Override
    protected void validateStructure(Collection<StructureError> errors, NBTTagCompound context) {
        super.validateStructure(errors, context);

        structureInstanceInfo.validate(errors, context);
    }

    @Override
    protected void localizeStructureErrors(Collection<StructureError> errors, NBTTagCompound context,
        List<String> lines) {
        super.localizeStructureErrors(errors, context, lines);

        structureInstanceInfo.localizeStructureErrors(errors, context, lines);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        StructureWrapperTooltipBuilder<MTENetworkSwitchAdv> tt = new StructureWrapperTooltipBuilder<>(structure);

        tt.addMachineType(translateToLocal("gt.blockmachines.multimachine.em.switch.type"))
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.switch.adv.desc.0"))
            .addSeparator()
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.switch.adv.desc.1"))
            .addSeparator()
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.switch.adv.desc.2"))
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.switch.adv.desc.3"))
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.switch.adv.desc.4"))
            .addInfo(
                translateToLocalFormatted(
                    "gt.blockmachines.multimachine.em.switch.adv.desc.5",
                    Machine_Multi_Switch.get(1)
                        .getDisplayName()))
            .addSeparator();

        tt.beginStructureBlock();
        tt.addAllCasingInfo();

        tt.addSubChannelUsage(GTStructureChannels.STRUCTURE_LENGTH, "middle slice count");

        tt.toolTipFinisher();

        return tt;
    }

    @Override
    public boolean shouldCheckMaintenance() {
        return false;
    }

    @Override
    protected boolean checkComputationTimeout() {
        // disable computation checks (badly named method)
        return true;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        List<ITexture> textures = new ArrayList<>();

        textures.add(Casings.AdvancedComputerCasing.getCasingTexture());

        if (side == facing) {
            textures.add(
                new TTRenderedExtendedFacingTexture(aActive ? TTMultiblockBase.ScreenON : TTMultiblockBase.ScreenOFF));
        }

        return textures.toArray(new ITexture[0]);
    }

    @Override
    @NotNull
    protected CheckRecipeResult checkProcessing_EM() {
        useLongPower = true;
        lEUt = 0;
        mMaxProgresstime = 0;
        mEfficiencyIncrease = 0;

        pendingComputation = 0;

        for (MTEHatchDataInput di : validMTEList(eInputData)) {
            if (di.q != null) {
                pendingComputation += di.q.getContent();
                di.setContents(null);
            }
        }

        if (pendingComputation < 0) pendingComputation = Long.MAX_VALUE;

        if (pendingComputation == 0) {
            return SimpleCheckRecipeResult.ofFailure("no_routing");
        }

        lEUt = length * -524_288L;
        mMaxProgresstime = 20;
        mEfficiencyIncrease = 10000;

        return SimpleCheckRecipeResult.ofSuccess("routing");
    }

    @Override
    public void outputAfterRecipe_EM() {
        super.outputAfterRecipe_EM();

        Vec3Impl pos = new Vec3Impl(
            getBaseMetaTileEntity().getXCoord(),
            getBaseMetaTileEntity().getYCoord(),
            getBaseMetaTileEntity().getZCoord());

        for (MTEHatchDataOutput output : validMTEList(eOutputData)) {
            if (pendingComputation <= 0) break;

            long toConsume = Math.min(pendingComputation, output.requestedComputation);
            pendingComputation -= toConsume;

            output.providePacket(new QuantumDataPacket(toConsume).unifyTraceWith(pos));
        }

        wastedComputation = pendingComputation;
        pendingComputation = 0;
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);

        screenElements
            .widget(new FakeSyncWidget.LongSyncer(() -> pendingComputation, value -> pendingComputation = value));
        screenElements
            .widget(new FakeSyncWidget.LongSyncer(() -> wastedComputation, value -> wastedComputation = value));

        screenElements.widget(
            TextWidget.dynamicString(
                () -> GTUtility
                    .translate("GT5U.machines.computation_hatch.pending_computation", formatNumber(pendingComputation)))
                .setSynced(false)
                .setTextAlignment(Alignment.CenterLeft)
                .setEnabled(w -> mMaxProgresstime > 0));
        screenElements.widget(
            TextWidget.dynamicString(
                () -> GTUtility
                    .translate("GT5U.machines.computation_hatch.wasted_computation", formatNumber(wastedComputation)))
                .setSynced(false)
                .setTextAlignment(Alignment.CenterLeft)
                .setEnabled(w -> mMaxProgresstime > 0));
    }
}
