package tectech.thing.metaTileEntity.multi;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.GregTechAPI.sBlockCasings1;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.gui.modularui.multiblock.MTEActiveTransformerGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import tectech.thing.casing.BlockGTCasingsTT;
import tectech.thing.casing.TTCasingsContainer;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoMulti;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyMulti;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.thing.metaTileEntity.multi.base.render.TTRenderedExtendedFacingTexture;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class MTEActiveTransformer extends TTMultiblockBase implements ISurvivalConstructable {

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

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        casingCount = 0;
        if (structureCheck_EM("main", 1, 1, 0) && casingCount >= 5) {
            grace = true;
            return true;
        } else if (grace) {
            grace = false;
            return true;
        }
        return false;
    }

    @Override
    public void onFirstTick_EM(IGregTechTileEntity aBaseMetaTileEntity) {
        if (!mMachine) {
            aBaseMetaTileEntity.disableWorking();
        }
    }

    // region structure
    private static final String[] description = new String[] {
        EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
        translateToLocal("gt.blockmachines.multimachine.em.transformer.hint"), // 1 - Energy IO Hatches or High
                                                                               // Power Casing
    };
    private static final IStructureDefinition<MTEActiveTransformer> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTEActiveTransformer>builder()
        .addShape(
            "main",
            new String[][] { { "111", "1~1", "111", }, { "111", "101", "111", }, { "111", "111", "111", }, })
        .addElement('0', ofBlock(sBlockCasings1, 15))
        .addElement(
            '1',
            buildHatchAdder(MTEActiveTransformer.class)
                .atLeast(Energy, HatchElement.EnergyMulti, Dynamo, HatchElement.DynamoMulti)
                .casingIndex(BlockGTCasingsTT.textureOffset)
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

        // Only update the samples once every second
        if (transferSamples >= TRANSFER_UPDATE_PERIOD) {
            transferSamples %= TRANSFER_UPDATE_PERIOD;

            transferredLast5Secs = (transferredLast5Secs * (1d - INV_5SECS)) + (eu * INV_5SECS);
            transferredLast30Secs = (transferredLast30Secs * (1d - INV_30SECS)) + (eu * INV_30SECS);
            transferredLast1Min = (transferredLast1Min * (1d - INV_60SECS)) + (eu * INV_60SECS);
        }
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(translateToLocal("gt.blockmachines.multimachine.em.transformer.machinetype")) // Machine Type:
            // Transformer
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.transformer.desc.1")) // Can transform to
                                                                                              // and from any
                                                                                              // voltage
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.transformer.desc.2")) // Only 0.004% power
                                                                                              // loss, HAYO!
            .addTecTechHatchInfo()
            .beginStructureBlock(3, 3, 3, false)
            .addController(translateToLocal("tt.keyword.Structure.FrontCenter")) // Controller: Front center
            .addCasingInfoMin(translateToLocal("gt.blockcasingsTT.0.name"), 5, false) // 5x High Power Casing
                                                                                      // (minimum)
            .addOtherStructurePart(
                translateToLocal("tt.keyword.Structure.SuperconductingCoilBlock"),
                translateToLocal("tt.keyword.Structure.Center")) // SuperconductingCoilBlock: Center
            .addEnergyHatch(translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1) // Energy Hatch: Any
                                                                                            // High Power Casing
            .addDynamoHatch(translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1) // Dynamo Hatch: Any
                                                                                            // High Power Casing
            .toolTipFinisher();
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[BlockGTCasingsTT.texturePage][0],
                new TTRenderedExtendedFacingTexture(aActive ? TTMultiblockBase.ScreenON : TTMultiblockBase.ScreenOFF) };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[BlockGTCasingsTT.texturePage][0] };
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
    public String[] getInfoData() {
        ArrayList<String> lines = new ArrayList<>(Arrays.asList(super.getInfoData()));

        lines.add(MessageFormat.format("Min hatch tier: {0}", calculateHatchTier()));
        lines.add(MessageFormat.format("Last 5 seconds: {0} EU/t", transferredLast5Secs));
        lines.add(MessageFormat.format("Last 30 seconds: {0} EU/t", transferredLast30Secs));
        lines.add(MessageFormat.format("Last minute: {0} EU/t", transferredLast1Min));

        return lines.toArray(new String[0]);
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
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM("main", 1, 1, 0, stackSize, hintsOnly);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) return -1;
        return survivalBuildPiece("main", stackSize, 1, 1, 0, elementBudget, source, actor, false, true);
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
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
    public boolean showRecipeTextInGUI() {
        return false;
    }

    @Override
    public boolean hasRunningText() {
        return false;
    }
}
