package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.getFluidUnit;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import goodgenerator.api.recipe.ExtremeHeatExchangerRecipe;
import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;
import goodgenerator.loader.Loaders;
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.machines.IRecipeProcessingAwareHatch;
import gregtech.common.tileentities.machines.MTEHatchInputME;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;

public class MTEExtremeHeatExchanger extends TTMultiblockBase implements ISurvivalConstructable {

    protected IStructureDefinition<MTEExtremeHeatExchanger> multiDefinition = null;

    public static double penalty_per_config = 0.015d;
    protected int casingAmount = 0;
    protected MTEHatchInput mHotFluidHatch;
    protected MTEHatchOutput mCooledFluidHatch;
    private boolean transformed = false;
    private String hotName;
    private ExtremeHeatExchangerRecipe tRunningRecipe;

    public MTEExtremeHeatExchanger(String name) {
        super(name);
    }

    public MTEExtremeHeatExchanger(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    @Override
    public IStructureDefinition<MTEExtremeHeatExchanger> getStructure_EM() {
        if (multiDefinition == null) {
            multiDefinition = StructureDefinition.<MTEExtremeHeatExchanger>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] {
                            // spotless:off
                                            { " CCC ", "TTTTT", "TTTTT", "TTTTT", "TTTTT", "TTTTT", "TTTTT", "TTTTT", "TTTTT", "TTTTT", " CCC " },
                                            { " CCC ", "GPPPG", "GWWWG", "GPPPG", "GWWWG", "GPPPG", "GWWWG", "GPPPG", "GWWWG", "GPPPG", " CCC " },
                                            { " CFC ", "GPPPG", "GWWWG", "GPPPG", "GWWWG", "GPPPG", "GWWWG", "GPPPG", "GWWWG", "GPPPG", " CEC " },
                                            { " CCC ", "GPPPG", "GWWWG", "GPPPG", "GWWWG", "GPPPG", "GWWWG", "GPPPG", "GWWWG", "GPPPG", " CCC " },
                                            { " CCC ", "GPPPG", "GWWWG", "GPPPG", "GWWWG", "GPPPG", "GWWWG", "GPPPG", "GWWWG", "GPPPG", " CCC " },
                                            { " C~C ", "BBBBB", "BBBBB", "BBBBB", "BBBBB", "BBBBB", "BBBBB", "BBBBB", "BBBBB", "BBBBB", " CCC " },
                                        //spotless:on
                        }))
                .addElement(
                    'B',
                    ofChain(
                        buildHatchAdder(MTEExtremeHeatExchanger.class)
                            .atLeast(
                                gregtech.api.enums.HatchElement.InputHatch,
                                gregtech.api.enums.HatchElement.Maintenance)
                            .casingIndex(48)
                            .hint(1)
                            .build(),
                        onElementPass(x -> x.casingAmount++, ofBlock(GregTechAPI.sBlockCasings4, 0))))
                .addElement(
                    'T',
                    ofChain(
                        buildHatchAdder(MTEExtremeHeatExchanger.class)
                            .atLeast(
                                gregtech.api.enums.HatchElement.OutputHatch,
                                gregtech.api.enums.HatchElement.Maintenance)
                            .casingIndex(48)
                            .hint(2)
                            .build(),
                        onElementPass(x -> x.casingAmount++, ofBlock(GregTechAPI.sBlockCasings4, 0))))
                .addElement('F', EHEHatches.HotInputHatch.newAny(48, 3))
                .addElement('E', EHEHatches.ColdOutputHatch.newAny(48, 4))
                .addElement(
                    'C',
                    ofChain(
                        buildHatchAdder(MTEExtremeHeatExchanger.class)
                            .atLeast(gregtech.api.enums.HatchElement.Maintenance)
                            .casingIndex(48)
                            .hint(5)
                            .build(),
                        onElementPass(x -> x.casingAmount++, ofBlock(GregTechAPI.sBlockCasings4, 0))))
                .addElement('G', chainAllGlasses())
                .addElement('P', ofBlock(GregTechAPI.sBlockCasings2, 15))
                .addElement('W', ofBlock(Loaders.pressureResistantWalls, 0))
                .build();
        }
        return multiDefinition;
    }

    public boolean addHotFluidInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchInput) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            mHotFluidHatch = (MTEHatchInput) aMetaTileEntity;
            return true;
        }
        return false;
    }

    public boolean addColdFluidOutputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchOutput) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            mCooledFluidHatch = (MTEHatchOutput) aMetaTileEntity;
            return true;
        }
        return false;
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        transformed = aNBT.getBoolean("transformed");
        if (aNBT.hasKey("hotName", Constants.NBT.TAG_STRING)) {
            String loadedHotName = aNBT.getString("hotName");
            Fluid hotFluid = FluidRegistry.getFluid(loadedHotName);
            if (hotFluid != null) {
                hotName = loadedHotName;
                tRunningRecipe = (ExtremeHeatExchangerRecipe) GoodGeneratorRecipeMaps.extremeHeatExchangerFuels
                    .getBackend()
                    .findFuel(hotFluid);
            }
        } else {
            hotName = null;
            tRunningRecipe = null;
        }
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setBoolean("transformed", transformed);
        if (hotName != null) aNBT.setString("hotName", hotName);
        super.saveNBTData(aNBT);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GoodGeneratorRecipeMaps.extremeHeatExchangerFuels;
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        mCooledFluidHatch = null;
        mHotFluidHatch = null;
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        this.casingAmount = 0;
        if (!checkPiece(mName, 2, 5, 0, errors)) return;
        checkCasingMin(errors, casingAmount, 25);
        checkHasMaintenanceHatch(errors);
        checkHasInputHatch(errors);
        checkHasOutputHatch(errors);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Heat Exchanger, EHE")
            .addInfo(StatCollector.translateToLocal("gt.multiblock.ExtremeHeatExchanger.desc1"))
            .addInfo(StatCollector.translateToLocal("gt.multiblock.ExtremeHeatExchanger.desc2"))
            .addInfo(StatCollector.translateToLocal("gt.multiblock.ExtremeHeatExchanger.desc3"))
            .addInfo(StatCollector.translateToLocal("gt.multiblock.ExtremeHeatExchanger.desc4"))
            .addInfo(StatCollector.translateToLocal("gt.multiblock.ExtremeHeatExchanger.desc5"))
            .addSeparator()
            .addInfo(
                StatCollector.translateToLocalFormatted(
                    "gt.multiblock.ExtremeHeatExchanger.lava",
                    getFluidUnit(),
                    getFluidUnit(),
                    getFluidUnit()))
            .addInfo(
                StatCollector.translateToLocalFormatted(
                    "gt.multiblock.ExtremeHeatExchanger.hotcoolant",
                    getFluidUnit(),
                    getFluidUnit(),
                    getFluidUnit()))
            .addInfo(
                StatCollector.translateToLocalFormatted(
                    "gt.multiblock.ExtremeHeatExchanger.hotsolarsalt",
                    getFluidUnit(),
                    getFluidUnit(),
                    getFluidUnit()))
            .addSeparator()
            .addInfo(StatCollector.translateToLocal("gt.multiblock.ExtremeHeatExchanger.plasma1"))
            .addInfo(StatCollector.translateToLocal("gt.multiblock.ExtremeHeatExchanger.plasma2"))
            .addSeparator()
            .addInfo(StatCollector.translateToLocal("gt.multiblock.ExtremeHeatExchanger.throttle1"))
            .addInfo(StatCollector.translateToLocal("gt.multiblock.ExtremeHeatExchanger.throttle2"))
            .beginStructureBlock(5, 6, 11, false)
            .addController("Front bottom center")
            .addCasing("25-120", "Robust Tungstensteel Machine Casing", false)
            .addCasing("72", "EV+ Tiered Glass", false)
            .addCasing("60", "Tungstensteel Pipe Casing", false)
            .addCasing("48", "Pressure Resistant Wall", false)
            .addMaintenanceHatch("1", "Any casing", 1, 2, 5)
            .addInputHatch("2+", "Front center casing (hot fluid), any bottom casing (distilled water)", 1, 3)
            .addOutputHatch("2+", "Back center casing (cool fluid), any top casing (steam)", 2, 4)
            .addStructureInfo("")
            .addSubChannel(GTStructureChannels.BOROGLASS)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing_EM() {
        tRunningRecipe = null;
        FluidStack hotFluid = null;
        if (mHotFluidHatch instanceof MTEHatchInputME inputME) {
            FluidStack[] fluids = inputME.getStoredFluids();
            if (fluids.length > 0) {
                hotFluid = fluids[0];
            }
        } else {
            hotFluid = mHotFluidHatch.getFluid();
        }
        if (hotFluid == null) return CheckRecipeResultRegistry.SUCCESSFUL;
        ExtremeHeatExchangerRecipe tRecipe = (ExtremeHeatExchangerRecipe) GoodGeneratorRecipeMaps.extremeHeatExchangerFuels
            .getBackend()
            .findFuel(hotFluid);
        if (tRecipe == null) return CheckRecipeResultRegistry.NO_RECIPE;
        tRunningRecipe = tRecipe;
        this.hotName = hotFluid.getFluid()
            .getName();
        int tMaxConsume = tRecipe.getMaxHotFluidConsume();
        int transformed_threshold = tRecipe.mSpecialValue;
        int tRealConsume = Math.min(tMaxConsume, hotFluid.amount);
        double penalty = 0.0d;
        double efficiency = 1d;
        int shs_reduction_per_config = 150;

        if (mInventory[1] != null && mInventory[1].getUnlocalizedName()
            .startsWith("gt.integrated_circuit")) {
            int circuit_config = mInventory[1].getItemDamage();
            if (circuit_config >= 1 && circuit_config <= 25) {
                penalty = (circuit_config - 1) * penalty_per_config;
                transformed_threshold -= (shs_reduction_per_config * (circuit_config - 1));
            }
        }
        efficiency -= penalty;

        if (transformed_threshold <= 0) transformed_threshold = 1;

        transformed = tRealConsume >= transformed_threshold;

        this.mMaxProgresstime = 20;
        this.mEUt = (int) (tRecipe.getEUt() * efficiency * ((double) tRealConsume / (double) tMaxConsume));
        // the 3-arg drain will work on both normal hatch and ME hatch
        mHotFluidHatch.drain(ForgeDirection.UNKNOWN, new FluidStack(hotFluid.getFluid(), tRealConsume), true);
        mCooledFluidHatch.fill(new FluidStack(tRecipe.getCooledFluid(), tRealConsume), true);
        this.mEfficiencyIncrease = 160;

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (this.mEUt > 0 && tRunningRecipe != null) {
            Fluid tReadySteam = transformed ? tRunningRecipe.getHeatedSteam() : tRunningRecipe.getNormalSteam();
            int waterAmount = (int) (this.mEUt / getUnitSteamPower(tReadySteam.getName())) / 160;
            if (waterAmount < 0) return false;
            int steamToOutput;
            startRecipeProcessing();
            boolean isDepleteSuccess = depleteInput(GTModHandler.getDistilledWater(waterAmount));
            endRecipeProcessing();
            if (isDepleteSuccess) {
                if (tRunningRecipe.mFluidInputs[0].getUnlocalizedName()
                    .contains("plasma")) {
                    steamToOutput = waterAmount * 160 / 1000;
                } else {
                    steamToOutput = waterAmount * 160;
                }
                addOutput(new FluidStack(tReadySteam, steamToOutput));
            } else {
                GTLog.writeExplosionLog(this, "had no more distilled water!");
                mHotFluidHatch.getBaseMetaTileEntity()
                    .doExplosion(V[8]);
                return false;
            }
        }
        return true;
    }

    public double getUnitSteamPower(String steam) {
        return switch (steam) {
            case "steam" -> 0.5;
            case "ic2superheatedsteam", "supercriticalsteam", "densesupercriticalsteam" -> 1;
            default -> -1;
        };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 2, 5, 0);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEExtremeHeatExchanger(mName);
    }

    @Override
    public String[] getInfoData() {
        int tThreshold = tRunningRecipe != null ? tRunningRecipe.mSpecialValue : 0;
        return new String[] {
            IGregTechDeviceInformation.encode(
                "GT5U.multiblock.Progress.fmt.s",
                formatNumber(mProgresstime / 20),
                formatNumber(mMaxProgresstime / 20)),
            IGregTechDeviceInformation.encode(
                "GT5U.multiblock.problems.efficiency.fmt",
                getIdealStatus() - getRepairStatus(),
                mEfficiency / 100.0F + " %"),
            IGregTechDeviceInformation.encode(
                "gg.infodata.xhe.steam_output",
                (transformed ? EnumChatFormatting.RED : EnumChatFormatting.YELLOW) + formatNumber(this.mEUt)
                    + EnumChatFormatting.RESET),
            IGregTechDeviceInformation.encode("gg.infodata.xhe.threshold", formatNumber(tThreshold)),
            IGregTechDeviceInformation.encode("GT5U.multiblock.recipesDone.fmt", formatNumber(recipesDone)) };
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { casingTexturePages[0][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[0][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_HEAT_EXCHANGER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_HEAT_EXCHANGER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[0][48] };
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(mName, stackSize, 2, 5, 0, elementBudget, env, false, true);
    }

    private enum EHEHatches implements IHatchElement<MTEExtremeHeatExchanger> {

        HotInputHatch(MTEExtremeHeatExchanger::addHotFluidInputToMachineList, MTEHatchInput.class) {

            @Override
            public long count(MTEExtremeHeatExchanger t) {
                if (t.mHotFluidHatch == null) return 0;
                return 1;
            }
        },
        ColdOutputHatch(MTEExtremeHeatExchanger::addColdFluidOutputToMachineList, MTEHatchOutput.class) {

            @Override
            public long count(MTEExtremeHeatExchanger t) {
                if (t.mCooledFluidHatch == null) return 0;
                return 1;
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGTHatchAdder<MTEExtremeHeatExchanger> adder;

        EHEHatches(IGTHatchAdder<MTEExtremeHeatExchanger> adder, Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        @Override
        public IGTHatchAdder<? super MTEExtremeHeatExchanger> adder() {
            return adder;
        }
    }

    @Override
    public void startRecipeProcessing() {
        super.startRecipeProcessing();
        if (mHotFluidHatch instanceof IRecipeProcessingAwareHatch aware && mHotFluidHatch.isValid()) {
            aware.startRecipeProcessing();
        }
    }

    @Override
    public void endRecipeProcessing() {
        super.endRecipeProcessing();
        if (mHotFluidHatch instanceof IRecipeProcessingAwareHatch aware && mHotFluidHatch.isValid()) {
            aware.endRecipeProcessing(this);
        }
    }
}
