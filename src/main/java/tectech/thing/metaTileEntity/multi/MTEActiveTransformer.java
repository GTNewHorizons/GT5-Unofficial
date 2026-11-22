package tectech.thing.metaTileEntity.multi;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.GregTechAPI.sBlockCasings1;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.tooltip.TooltipHelper.anyCasingText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.casing.Casings;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.gui.modularui.multiblock.MTEActiveTransformerGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.misc.GTStructureChannels;
import tectech.thing.casing.BlockGTCasingsTT;
import tectech.thing.casing.TTCasingsContainer;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoMulti;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyMulti;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.thing.metaTileEntity.multi.base.render.TTRenderedExtendedFacingTexture;

/**
 * Created by danie_000 on 17.12.2016.
 */
@IMetaTileEntity.SkipGenerateDescription
public class MTEActiveTransformer extends TTMultiblockBase implements ISurvivalConstructable, ICasingTextureProvider {

    // Gives a one-chance grace period for deforming the multi. This is to allow you to hotswap hatches without
    // powerfailing due to an unlucky tick timing - this grace period is already a part of base TecTech but the
    // tick timer is essentially random, so it was extremely unreliable. Now you are guaranteed the length
    // of one structure check to finish your hotswap before it deforms.
    private boolean grace = false;

    private double transferBuffer = 0d;
    private int transferSamples = 0;

    private double transferredLast5Secs = 0d;
    private double transferredLast30Secs = 0d;
    private double transferredLast1Min = 0d;

    /** The number of ticks between UI updates for the transfer widgets. */
    private static final int TRANSFER_UPDATE_PERIOD = 20;
    private static final double INV_5SECS = 1d / 5d;
    private static final double INV_30SECS = 1d / 30d;
    private static final double INV_60SECS = 1d / 60d;

    private static final int MAX_LENGTH = 6; // 8 - 2 to account for first and last slice
    private static final int CASING_MAX = 5;

    @Override
    public void checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack,
        List<StructureError> errors) {
        casingCount = 0;
        int checkLength = 1;
        if (!checkPiece(STRUCTURE_SHAPE_FIRST, 1, 1, 0, errors)) return;
        while (checkLength <= MAX_LENGTH && checkPiece(STRUCTURE_SHAPE_MIDDLE, 1, 1, -checkLength, errors)) {
            checkLength++;
        }
        // needed because while loop can look for a 'middle' piece where an 'end' piece exists and error
        errors.clear();
        if (!checkPiece(STRUCTURE_SHAPE_LAST, 1, 1, -(checkLength), errors)) return;
        checkCasingMin(errors, casingCount, CASING_MAX);
        checkHasAnyEnergy(errors);

        // hack to prevent hatch duplication from while loop of middle slice scanning end slice
        GTDataUtils.dedupList(mExoticEnergyHatches);
        GTDataUtils.dedupList(mEnergyHatches);
        GTDataUtils.dedupList(eEnergyMulti);
        GTDataUtils.dedupList(eDynamoMulti);
        if (errors.isEmpty()) {
            grace = true;
        } else if (grace) {
            grace = false;
            errors.clear();
        }
    }

    @Override
    public void onFirstTick_EM(IGregTechTileEntity aBaseMetaTileEntity) {
        if (!mMachine) {
            aBaseMetaTileEntity.disableWorking();
        }
    }

    private static final String STRUCTURE_SHAPE_FIRST = "first";
    private static final String STRUCTURE_SHAPE_MIDDLE = "middle";
    private static final String STRUCTURE_SHAPE_LAST = "last";

    private static final String[] FIRST = { "111", "1~1", "111" };

    private static final String[] MIDDLE = { "111", "101", "111" };

    private static final String[] LAST = { "111", "111", "111" };

    private static final IStructureDefinition<MTEActiveTransformer> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTEActiveTransformer>builder()
        .addShape(STRUCTURE_SHAPE_FIRST, new String[][] { FIRST })
        .addShape(STRUCTURE_SHAPE_MIDDLE, new String[][] { MIDDLE })
        .addShape(STRUCTURE_SHAPE_LAST, new String[][] { LAST })
        .addElement('0', ofBlock(sBlockCasings1, 15))
        .addElement(
            '1',
            buildHatchAdder(MTEActiveTransformer.class)
                .atLeast(Energy, HatchElement.EnergyMulti, Dynamo, HatchElement.DynamoMulti)
                .casingIndex(Casings.HighPowerCasing.textureId)
                .hint(1)
                .buildAndChain(onElementPass(t -> t.casingCount++, ofBlock(TTCasingsContainer.sBlockCasingsTT, 0))))
        .build();
    private int casingCount = 0;

    @Override
    public IStructureDefinition<MTEActiveTransformer> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }
    // endregion

    public MTEActiveTransformer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        eDismantleBoom = false;
    }

    public MTEActiveTransformer(String aName) {
        super(aName);
        eDismantleBoom = false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEActiveTransformer(mName);
    }

    @Override
    @NotNull
    protected CheckRecipeResult checkProcessing_EM() {
        if (ePowerPass) {
            mEfficiencyIncrease = 10000;
            mMaxProgresstime = 20;
        } else {
            mEfficiencyIncrease = 0;
            mMaxProgresstime = 0;
        }
        eAmpereFlow = 0;
        mEUt = 0;
        return ePowerPass ? SimpleCheckRecipeResult.ofSuccess("routing")
            : SimpleCheckRecipeResult.ofFailure("no_routing");
    }

    @Override
    protected void onPostPowerPass(double eu) {
        super.onPostPowerPass(eu);
        // Called once per tick

        transferBuffer += eu;
        transferSamples++;

        // Only update the samples once every second, using the average EU/t over the whole period rather than the
        // value of a single tick. Power transfer is often periodic (e.g. with hatches of different tiers), so sampling
        // one tick would alias to a constant 0 or a constant spike.
        if (transferSamples >= TRANSFER_UPDATE_PERIOD) {
            transferSamples = 0;

            double avgEUt = transferBuffer / TRANSFER_UPDATE_PERIOD;
            transferBuffer = 0d;

            transferredLast5Secs = (transferredLast5Secs * (1d - INV_5SECS)) + (avgEUt * INV_5SECS);
            transferredLast30Secs = (transferredLast30Secs * (1d - INV_30SECS)) + (avgEUt * INV_30SECS);
            transferredLast1Min = (transferredLast1Min * (1d - INV_60SECS)) + (avgEUt * INV_60SECS);
        }
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // spotless:off
        tt.addMachineType(StatCollector.translateToLocal("gt.blockmachines.multimachine.em.transformer.machinetype"))
            .addMarkdown(new ResourceLocation("gregtech", "active-transformer"))
            .addSupportAny()
            .beginStructureBlock(3, 3, 3, false)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_center_2nd_layer"))
            .addCasing("5-24", StatCollector.translateToLocal("gt.blockcasingsTT.0.name"), false)
            .addCasing("1", StatCollector.translateToLocal("tt.keyword.Structure.SuperconductingCoilBlock"), false)
            .addEnergyHatch("1+", anyCasingText(), 1)
            .addDynamoHatch("0+", anyCasingText(), 1)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { getCasingTexture(),
                new TTRenderedExtendedFacingTexture(aActive ? TTMultiblockBase.ScreenON : TTMultiblockBase.ScreenOFF) };
        }
        return new ITexture[] { getCasingTexture() };
    }

    @Override
    public ITexture getCasingTexture() {
        return Textures.BlockIcons.casingTexturePages[BlockGTCasingsTT.texturePage][0];
    }

    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.TECTECH_MACHINES_NOISE;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean doRandomMaintenanceDamage() {
        return true;
    }

    @Override
    public boolean showMachineStatusInGUI() {
        return false;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if ((aTick & 31) == 31) {
            ePowerPass = aBaseMetaTileEntity.isAllowedToWork();
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (!ePowerPass) {
            // Manually add a zero to the averages when the AT is off because it won't happen otherwise
            onPostPowerPass(0);
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setDouble("transferredLast5Secs", transferredLast5Secs);
        aNBT.setDouble("transferredLast30Secs", transferredLast30Secs);
        aNBT.setDouble("transferredLast1Min", transferredLast1Min);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        transferredLast5Secs = aNBT.getDouble("transferredLast5Secs");
        transferredLast30Secs = aNBT.getDouble("transferredLast30Secs");
        transferredLast1Min = aNBT.getDouble("transferredLast1Min");
    }

    public int calculateHatchTier() {
        long minEUt = Long.MAX_VALUE;

        for (MTEHatchDynamo dynamo : mDynamoHatches) {
            minEUt = Math.min(minEUt, dynamo.maxEUOutput());
        }

        for (MTEHatchEnergy energy : mEnergyHatches) {
            minEUt = Math.min(minEUt, energy.maxEUInput());
        }

        for (MTEHatchDynamoMulti dynamo : eDynamoMulti) {
            minEUt = Math.min(minEUt, dynamo.maxEUOutput());
        }

        for (MTEHatchEnergyMulti energy : eEnergyMulti) {
            minEUt = Math.min(minEUt, energy.maxEUInput());
        }

        if (minEUt == Long.MAX_VALUE) minEUt = 0;

        return GTUtility.getTier(minEUt);
    }

    // Same as AE
    private static final String[] AMP_UNITS = { "", "k", "M", "G", "T", "P", "E" };

    public static String formatUIAmperage(double amps) {
        int unit = 0;

        while (amps > 1000 && unit + 1 < AMP_UNITS.length) {
            amps /= 1000d;
            unit++;
        }

        return formatNumber(amps) + AMP_UNITS[unit];
    }

    public static String formatUIEUt(double eut) {
        if (eut < 1_000_000_000) return formatNumber(eut);

        int exp = 0;

        while (eut > 1_000) {
            eut /= 1000d;
            exp += 3;
        }

        return formatNumber(eut) + "e" + exp;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEActiveTransformerGui(this);
    }

    public double getTransferredLast30Secs() {
        return transferredLast30Secs;
    }

    public void setTransferredLast30Secs(double transferredLast30Secs) {
        this.transferredLast30Secs = transferredLast30Secs;
    }

    public double getTransferredLast5Secs() {
        return transferredLast5Secs;
    }

    public void setTransferredLast5Secs(double transferredLast5Secs) {
        this.transferredLast5Secs = transferredLast5Secs;
    }

    public double getTransferredLast1Min() {
        return transferredLast1Min;
    }

    public void setTransferredLast1Min(double transferredLast1Min) {
        this.transferredLast1Min = transferredLast1Min;
    }

    @Override
    public boolean canBeMuffled() {
        return false;
    }

    @Override
    public void getExtraInfoData(List<String> info) {
        info.add(
            IGregTechDeviceInformation.encode("tt.infodata.active_transformer.min_hatch_tier", calculateHatchTier()));
        info.add(IGregTechDeviceInformation.encode("tt.infodata.active_transformer.last_5s", transferredLast5Secs));
        info.add(IGregTechDeviceInformation.encode("tt.infodata.active_transformer.last_30s", transferredLast30Secs));
        info.add(IGregTechDeviceInformation.encode("tt.infodata.active_transformer.last_1min", transferredLast1Min));
    }

    @Override
    public Map<String, String> getInfoMap() {
        HashMap<String, String> map = new HashMap<>(super.getInfoMap());

        map.put("minHatchTier", Integer.toString(calculateHatchTier()));
        map.put("last5Seconds", Double.toString(transferredLast5Secs));
        map.put("last30Seconds", Double.toString(transferredLast30Secs));
        map.put("lastMinute", Double.toString(transferredLast1Min));

        return map;
    }

    @Override
    public void construct(ItemStack stack, boolean hintsOnly) {
        buildPiece(STRUCTURE_SHAPE_FIRST, stack, hintsOnly, 1, 1, 0);
        int length = GTStructureChannels.STRUCTURE_LENGTH.getValueClamped(stack, 1, MAX_LENGTH);
        for (int i = 1; i <= length; i++) {
            buildPiece(STRUCTURE_SHAPE_MIDDLE, stack, hintsOnly, 1, 1, -i);
        }
        buildPiece(STRUCTURE_SHAPE_LAST, stack, hintsOnly, 1, 1, -(length + 1));
    }

    @Override
    public int survivalConstruct(ItemStack stack, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int length = GTStructureChannels.STRUCTURE_LENGTH.getValueClamped(stack, 1, MAX_LENGTH);
        int built = 0, temp;
        temp = survivalBuildPiece(STRUCTURE_SHAPE_FIRST, stack, 1, 1, 0, elementBudget - built, env, false, false);
        if (temp > -1) built += temp;

        if (elementBudget - built <= 0) return built;

        for (int i = 1; i <= length; i++) {
            temp = survivalBuildPiece(
                STRUCTURE_SHAPE_MIDDLE,
                stack,
                1,
                1,
                -i,
                elementBudget - built,
                env,
                false,
                false);
            if (temp > -1) built += temp;
            if (elementBudget - built <= 0) return built;
        }
        temp = survivalBuildPiece(
            STRUCTURE_SHAPE_LAST,
            stack,
            1,
            1,
            -(length + 1),
            elementBudget - built,
            env,
            false,
            false);
        if (temp > -1) built += temp;
        return temp == -1 ? -1 : built;
    }

    @Override
    public boolean isSafeVoidButtonEnabled() {
        return false;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    public boolean showRecipeTextInGUI() {
        return false;
    }

    @Override
    public boolean hasRunningText() {
        return false;
    }
}
