package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.AuthorLeon;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.ExoticDynamo;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LNE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LNE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LNE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LNE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.getCasingTextureForId;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.ArrayList;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.API.recipe.BartWorksRecipeMaps;
import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.GregTechTileClientEvents;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.maps.FuelBackend;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.IDualInputHatch;

public class MTELargeNeutralizationEngine extends MTEEnhancedMultiBlockBase<MTELargeNeutralizationEngine>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";

    private int structureTier;
    private int mCasing;

    private static final int HORIZONTAL_OFF_SET = 5;
    private static final int VERTICAL_OFF_SET = 1;
    private static final int DEPTH_OFF_SET = 0;

    private int fuelValue = 0;
    private int fuelConsumption = 0;
    private float boosterEUBoost = 1.0F;
    private int boosterBoostTicks = 0;
    private int toxicResidue = 0;
    private int residueDecay;
    private int residueIncrease;

    public MTELargeNeutralizationEngine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTELargeNeutralizationEngine(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeNeutralizationEngine(this.mName);
    }

    @Nullable
    private static Integer getStructureCasingTier(Block b, int m) {
        if (b != getBlock()) return null;
        if (m <= 4 || m >= 8) return null;
        return m - 4;
    }

    private int getResidueCapacity() {
        return switch (structureTier) {
            case 1 -> 75000;
            case 2 -> 200000;
            case 3 -> 500000;
            default -> -1;
        };
    }

    private int getBaseResidueDecay() {
        return switch (structureTier) {
            case 1 -> 200;
            case 2 -> 400;
            case 3 -> 700;
            default -> -1;
        };
    }

    private float getRandomDecayMultiplier() {
        return (90 + getBaseMetaTileEntity().getWorld().rand.nextInt(21)) / 100F;
    }

    private int getResidueDecay() {
        return Math.max(0, (int) (getBaseResidueDecay() * getRandomDecayMultiplier()));
    }

    private int getResidueIncrease() {
        return 0;
    }

    private static Block getBlock() {
        return GregTechAPI.sBlockCasings12;
    }

    private VoltageIndex getRobotArmTier() {

        return null;
    }

    private static IStructureDefinition<MTELargeNeutralizationEngine> STRUCTURE_DEFINITION = null;

    @Override
    public IStructureDefinition<MTELargeNeutralizationEngine> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTELargeNeutralizationEngine>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    transpose(
                        new String[][] { { "           ", "   PPPPP   ", "           " },
                            { "   CC~CC   ", "  P-----P  ", "   CCCCC   " },
                            { "  CCFFFCC  ", " P--CCC--P ", "  CCFFFCC  " },
                            { " CCF   FCC ", "P--C   C--P", " CCF   FCC " },
                            { " CF     FC ", "P-CF   FC-P", " CF     FC " },
                            { " CF     FC ", "P-CF   FC-P", " CF     FC " },
                            { " F F   F F ", " CFCF FCFC ", " F F   F F " } }))
                .addElement(
                    'C',
                    ofChain(
                        buildHatchAdder(MTELargeNeutralizationEngine.class)
                            .atLeast(Dynamo.or(ExoticDynamo), Maintenance, InputBus, InputHatch)
                            .casingIndex(getCasingTextureId())
                            .allowOnly(ForgeDirection.NORTH)
                            .hint(1)
                            .build(),
                        onElementPass(
                            m -> m.mCasing++,
                            ofBlocksTiered(
                                MTELargeNeutralizationEngine::getStructureCasingTier,
                                ImmutableList.of(
                                    Pair.of(getBlock(), Casings.StrengthenedInanimateCasing.getBlockMeta()),
                                    Pair.of(getBlock(), Casings.PreciseStationaryCasing.getBlockMeta()),
                                    Pair.of(getBlock(), Casings.UltimateStaticCasing.getBlockMeta())),
                                -1,
                                (m, t) -> m.structureTier = t,
                                m -> m.structureTier))))
                .addElement('F', ofFrame(Materials.Polytetrafluoroethylene))
                .addElement('P', ofBlock(GregTechAPI.sBlockCasings8, 1))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Acid Generator, LNE")
            .addInfo("(Dis)solves all your problems!")
            .addInfo("Provide an acid to burn, and a base to boost efficiency")
            .addInfo("TBD: Finish residue/base info")
            .beginStructureBlock(11, 7, 3, true)
            .addController("Top center")
            .addCasingInfoRange(
                "Tiered Casings: Strengthened Inanimate Casing, Precise Stationary Casing, Ultimate Static Casing",
                30,
                46,
                false)
            .addCasingInfoExactly("Polytetrafluoroethylene Frame Box", 34, false)
            .addCasingInfoExactly("PTFE Pipe Casing", 15, false)
            .addInputBus("Any Tiered Casing", 1)
            .addInputHatch("Any Tiered Casing", 1)
            .addMaintenanceHatch("Any Tiered Casing", 1)
            .addDynamoHatch("Any Tiered Casing", 1)
            .toolTipFinisher(AuthorLeon);
        return tt;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        structureTier = -1;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, HORIZONTAL_OFF_SET, VERTICAL_OFF_SET, DEPTH_OFF_SET)) return false;
        if (mCasing < 30 || structureTier < 1) return false;
        if (mMaintenanceHatches.size() != 1) return false;
        if (getBaseMetaTileEntity() == null) return false;
        getBaseMetaTileEntity().sendBlockEvent(GregTechTileClientEvents.CHANGE_CUSTOM_DATA, getUpdateData());
        updateHatchTexture();
        return true;
    }

    public void updateHatchTexture() {
        for (IDualInputHatch h : mDualInputHatches) h.updateTexture(getCasingTextureId());
        for (MTEHatch h : mInputBusses) h.updateTexture(getCasingTextureId());
        for (MTEHatch h : mMaintenanceHatches) h.updateTexture(getCasingTextureId());
        for (MTEHatch h : mEnergyHatches) h.updateTexture(getCasingTextureId());
        for (MTEHatch h : mInputHatches) h.updateTexture(getCasingTextureId());
        for (MTEHatch h : mExoticDynamoHatches) h.updateTexture(getCasingTextureId());
        for (MTEHatchDynamo h : mDynamoHatches) h.updateTexture(getCasingTextureId());
    }

    /*
     * NOTE!!!! CONTROLLER TEXTURE IS STILL CURRENTLY NULL!!!!!!!!!!!!!!
     */
    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { getCasingTexture(), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LNE_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LNE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { getCasingTexture(), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LNE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LNE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { getCasingTexture() };
    }

    private ITexture getCasingTexture() {
        return getCasingTextureForId(getCasingTextureId());
    }

    private int getCasingTextureId() {
        return switch (structureTier) {
            case 2 -> Casings.PreciseStationaryCasing.getTextureId();
            case 3 -> Casings.UltimateStaticCasing.getTextureId();
            default -> Casings.StrengthenedInanimateCasing.getTextureId();
        };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, HORIZONTAL_OFF_SET, VERTICAL_OFF_SET, DEPTH_OFF_SET);
    }

    @Override
    public RecipeMap<FuelBackend> getRecipeMap() {
        return BartWorksRecipeMaps.acidGenFuels;
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @Override
    protected boolean filtersFluid() {
        return false;
    }

    @Override
    public void onValueUpdate(byte aValue) {
        structureTier = aValue;
    }

    @Override
    public byte getUpdateData() {
        return (byte) structureTier;
    }

    private void useBooster() {
        if (depleteInput(Materials.FranciumHydroxide.getDust(1))) {
            this.boosterEUBoost = 5F;
            this.boosterBoostTicks = 600;
        } else if (depleteInput(Materials.CaesiumHydroxide.getDust(1))) {
            this.boosterEUBoost = 2.5F;
            this.boosterBoostTicks = 200;
        } else if (depleteInput(Materials.PotassiumHydroxide.getDust(1))) {
            this.boosterEUBoost = 1.9F;
            this.boosterBoostTicks = 20;
        } else if (depleteInput(Materials.SodiumHydroxide.getDust(1))) {
            this.boosterEUBoost = 1.5F;
            this.boosterBoostTicks = 20;
        } else {
            this.boosterEUBoost = 1.0F;
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("structureTier", (byte) structureTier);
        aNBT.setInteger("fuelConsumption", fuelConsumption);
        aNBT.setFloat("boosterEUBoost", boosterEUBoost);
        aNBT.setInteger("boosterBoostTicks", boosterBoostTicks);
        aNBT.setInteger("toxicResidue", toxicResidue);
        aNBT.setInteger("residueIncrease", residueIncrease);
        aNBT.setInteger("residueDecay", residueDecay);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        structureTier = aNBT.getByte("structureTier");
        fuelConsumption = aNBT.getInteger("fuelConsumption");
        boosterEUBoost = aNBT.getFloat("boosterEUBoost");
        boosterBoostTicks = aNBT.getInteger("boosterBoostTicks");
        toxicResidue = aNBT.getInteger("toxicResidue");
        residueIncrease = aNBT.getInteger("residueIncrease");
        residueDecay = aNBT.getInteger("residueDecay");
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            HORIZONTAL_OFF_SET,
            VERTICAL_OFF_SET,
            DEPTH_OFF_SET,
            elementBudget,
            env,
            false,
            true);
    }

    public long getMaximumEUOutput() {
        long aTotal = 0;
        for (MTEHatchDynamo aDynamo : validMTEList(mDynamoHatches)) {
            long aVoltage = aDynamo.maxEUOutput();
            aTotal = aDynamo.maxAmperesOut() * aVoltage;
        }
        for (MTEHatch aDynamo : validMTEList(mExoticDynamoHatches)) {
            long aVoltage = aDynamo.maxEUOutput();
            aTotal = aDynamo.maxAmperesOut() * aVoltage;
        }
        return aTotal;
    }

    private int getFuelEUOutput(int fluidAmount) {
        return this.fuelValue * fluidAmount;
    }

    private int getEUOutput(int fluidAmount) {
        return Math
            .min((int) (getFuelEUOutput(fluidAmount) * this.boosterEUBoost), GTUtility.safeInt(getMaximumEUOutput()));
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        ArrayList<FluidStack> storedFluids = this.getStoredFluids();
        if (storedFluids.isEmpty()) {
            this.shutDown();
            return CheckRecipeResultRegistry.NO_FUEL_FOUND;
        }
        for (FluidStack storedFluid : storedFluids) {
            GTRecipe recipe = getRecipeMap().getBackend()
                .findFuel(storedFluid);
            if (recipe == null) continue;
            this.fuelValue = recipe.mSpecialValue;
            FluidStack storedFluidConsume = storedFluid.copy();
            this.fuelConsumption = storedFluidConsume.amount;
            if (!depleteInput(storedFluidConsume)) return CheckRecipeResultRegistry.NO_FUEL_FOUND;
            if (boosterBoostTicks > 0) {
                boosterBoostTicks--;
            } else {
                useBooster();
            }
            this.mEUt = getEUOutput(fuelConsumption);
            this.mEfficiencyIncrease = 50;
            this.mProgresstime = 1;
            this.mMaxProgresstime = 1;
            residueIncrease = getResidueIncrease();
            residueDecay = getResidueDecay();
            this.toxicResidue = Math.max(0, this.toxicResidue + residueIncrease - residueDecay);
            return CheckRecipeResultRegistry.GENERATING;
        }
        this.shutDown();
        return CheckRecipeResultRegistry.NO_FUEL_FOUND;
    }

    private void shutDown() {
        this.mEUt = 0;
        this.mEfficiency = 0;
    }

    @Override
    public String[] getInfoData() {
        long storedEnergy = 0;
        long maxEnergy = 0;
        for (MTEHatchDynamo tHatch : validMTEList(mDynamoHatches)) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }
        for (MTEHatch tHatch : validMTEList(mExoticDynamoHatches)) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }

        return new String[] {
            EnumChatFormatting.BLUE + StatCollector.translateToLocal("GT5U.infodata.large_neutralization_engine")
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + formatNumber(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            getIdealStatus() == getRepairStatus()
                ? EnumChatFormatting.GREEN + StatCollector.translateToLocal("GT5U.turbine.maintenance.false")
                    + EnumChatFormatting.RESET
                : EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.turbine.maintenance.true")
                    + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("GT5U.engine.output") + ": "
                + EnumChatFormatting.RED
                + formatNumber(((long) mEUt * mEfficiency / 10000))
                + EnumChatFormatting.RESET
                + " EU/t",
            StatCollector.translateToLocal("GT5U.engine.consumption") + ": "
                + EnumChatFormatting.YELLOW
                + formatNumber(fuelConsumption)
                + EnumChatFormatting.RESET
                + " L/t",
            StatCollector.translateToLocal("GT5U.engine.value") + ": "
                + EnumChatFormatting.YELLOW
                + formatNumber(fuelValue)
                + EnumChatFormatting.RESET
                + " EU/L",
            StatCollector.translateToLocal("GT5U.engine.efficiency") + ": "
                + EnumChatFormatting.YELLOW
                + (mEfficiency / 100F)
                + EnumChatFormatting.YELLOW
                + " %",
            StatCollector.translateToLocal("GT5U.multiblock.recipesDone") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(recipesDone)
                + EnumChatFormatting.RESET };
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEMultiBlockBaseGui<MTELargeNeutralizationEngine>(this) {

            @Override
            protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager,
                ModularPanel parent) {
                ListWidget<IWidget, ?> terminalText = super.createTerminalTextWidget(syncManager, parent);
                BooleanSyncValue checkMachineSyncer = new BooleanSyncValue(() -> mMachine);
                syncManager.syncValue("checkMachine", checkMachineSyncer);
                if (!checkMachineSyncer.getBoolValue()) return terminalText;

                IntSyncValue toxicResidueSyncer = new IntSyncValue(() -> toxicResidue);
                IntSyncValue residueCapacitySyncer = new IntSyncValue(() -> getResidueCapacity());
                syncManager.syncValue("toxicResidue", toxicResidueSyncer);
                syncManager.syncValue("residueCapacitySyncer", residueCapacitySyncer);
                final float residuePercentage = toxicResidueSyncer.getValue()
                    .floatValue() / residueCapacitySyncer.getValue()
                    * 100F;
                terminalText.child(
                    IKey.dynamic(
                        () -> StatCollector.translateToLocalFormatted(
                            "GT5U.gui.text.toxic_residue",
                            toxicResidueSyncer.getStringValue(),
                            residueCapacitySyncer.getStringValue(),
                            Float.toString(residuePercentage)))
                        .asWidget());
                IntSyncValue residueDecaySyncer = new IntSyncValue(() -> residueDecay);
                IntSyncValue residueIncreaseSyncer = new IntSyncValue(() -> residueIncrease);
                syncManager.syncValue("residueDecay", residueDecaySyncer);
                syncManager.syncValue("residueIncrease", residueIncreaseSyncer);
                final int residueChange = residueIncreaseSyncer.getIntValue() - residueDecaySyncer.getIntValue();
                terminalText.child(
                    IKey.dynamic(
                        () -> StatCollector.translateToLocalFormatted(
                            "GT5U.gui.text.residue_change",
                            residueIncreaseSyncer.getStringValue(),
                            residueDecaySyncer.getStringValue(),
                            Integer.toString(residueChange)))
                        .asWidget());
                return terminalText;
            }
        };
    }
}
