package tectech.thing.metaTileEntity.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.GregTechAPI.sBlockCasings1;
import static gregtech.api.enums.GTValues.TIER_COLORS;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.GTValues.VN;
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
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

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
import gregtech.common.data.IntValue;
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

    private double transferredLastMin = 0d;
    private double transferredLast5Min = 0d;
    private double transferredLast30Min = 0d;

    private static final double TICKS_PER_MIN_INV = 1d / (20d * 60d);
    private static final double TICKS_PER_5MIN_INV = 1d / (20d * 60d * 5d);
    private static final double TICKS_PER_30MIN_INV = 1d / (20d * 60d * 30d);

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
                .dot(1)
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
        // called once per tick

        transferredLastMin = (transferredLastMin * (1d - TICKS_PER_MIN_INV)) + (eu * TICKS_PER_MIN_INV);
        transferredLast5Min = (transferredLast5Min * (1d - TICKS_PER_5MIN_INV)) + (eu * TICKS_PER_5MIN_INV);
        transferredLast30Min = (transferredLast30Min * (1d - TICKS_PER_30MIN_INV)) + (eu * TICKS_PER_30MIN_INV);
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

        aNBT.setDouble("transferredLastMin", transferredLastMin);
        aNBT.setDouble("transferredLast5Min", transferredLast5Min);
        aNBT.setDouble("transferredLast30Min", transferredLast30Min);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        transferredLastMin = aNBT.getDouble("transferredLastMin");
        transferredLast5Min = aNBT.getDouble("transferredLast5Min");
        transferredLast30Min = aNBT.getDouble("transferredLast30Min");
    }

    private int calculateHatchTier() {
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

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);

        IntValue hatchTier = new IntValue();

        screenElements
            .widget(new FakeSyncWidget.DoubleSyncer(() -> transferredLastMin, value -> transferredLastMin = value));
        screenElements
            .widget(new FakeSyncWidget.DoubleSyncer(() -> transferredLast5Min, value -> transferredLast5Min = value));
        screenElements
            .widget(new FakeSyncWidget.DoubleSyncer(() -> transferredLast30Min, value -> transferredLast30Min = value));
        screenElements.widget(new FakeSyncWidget.IntegerSyncer(this::calculateHatchTier, hatchTier::setValue));

        screenElements.widget(TextWidget.localised("GT5U.gui.text.at_eu_transferred"));

        screenElements.widget(TextWidget.dynamicString(() -> {
            String amperage = GTUtility.formatNumbers(transferredLastMin / V[hatchTier.value]);
            String tier = TIER_COLORS[hatchTier.value] + VN[hatchTier.value];

            return GTUtility.translate("GT5U.gui.text.at_past_1min", amperage, tier);
        })
            .setSynced(false));

        screenElements.widget(TextWidget.dynamicString(() -> {
            String amperage = GTUtility.formatNumbers(transferredLast5Min / V[hatchTier.value]);
            String tier = TIER_COLORS[hatchTier.value] + VN[hatchTier.value];

            return GTUtility.translate("GT5U.gui.text.at_past_5min", amperage, tier);
        })
            .setSynced(false));

        screenElements.widget(TextWidget.dynamicString(() -> {
            String amperage = GTUtility.formatNumbers(transferredLast30Min / V[hatchTier.value]);
            String tier = TIER_COLORS[hatchTier.value] + VN[hatchTier.value];

            return GTUtility.translate("GT5U.gui.text.at_past_30min", amperage, tier);
        })
            .setSynced(false));
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> lines = new ArrayList<>(Arrays.asList(super.getInfoData()));

        lines.add(MessageFormat.format("Min hatch tier: {0}", calculateHatchTier()));
        lines.add(MessageFormat.format("Last minute: {0} EU/t", transferredLastMin));
        lines.add(MessageFormat.format("Last 5 minutes: {0} EU/t", transferredLast5Min));
        lines.add(MessageFormat.format("Last 30 minutes: {0} EU/t", transferredLast30Min));

        return lines.toArray(new String[0]);
    }

    @Override
    public Map<String, String> getInfoMap() {
        HashMap<String, String> map = new HashMap<>(super.getInfoMap());

        map.put("minHatchTier", Integer.toString(calculateHatchTier()));
        map.put("lastMinute", Double.toString(transferredLastMin));
        map.put("last5Minutes", Double.toString(transferredLast5Min));
        map.put("last30Minutes", Double.toString(transferredLast30Min));

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
}
