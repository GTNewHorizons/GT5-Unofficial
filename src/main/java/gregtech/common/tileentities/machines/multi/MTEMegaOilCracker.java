package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MEGA_OIL_CRACKER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MEGA_OIL_CRACKER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MEGA_OIL_CRACKER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MEGA_OIL_CRACKER_GLOW;
import static gregtech.api.util.GTStructureUtility.activeCoils;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofCoil;
import static gregtech.api.util.GTStructureUtility.ofSheetMetal;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.common.configs.Configuration;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.casing.Casings;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchMultiInput;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.maps.OilCrackerBackend;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrorRegistry;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.api.util.tooltip.TooltipTier;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.machines.IRecipeProcessingAwareHatch;
import gregtech.common.tileentities.machines.MTEHatchInputME;

public class MTEMegaOilCracker extends MTEExtendedPowerMultiBlockBase<MTEMegaOilCracker>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int VERTICAL_OFFSET = 7;
    private static final int HORIZONTAL_OFFSET = 6;
    private static final int DEPTH_OFFSET = 0;
    private static final IStructureDefinition<MTEMegaOilCracker> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEMegaOilCracker>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            transpose(new String[][]{
                {" D         D "," D         D "," D         D "," D         D "," D         D "," D         D "," D         D "," D         D "," D         D "},
                {" D         D ","DBAAAAAAAAABD"," BAAAAAAAAAB "," BAAAMMMAAAB "," BFFFMMMFFFB "," BAAAMMMAAAB "," BAAAAAAAAAB ","DBAAAAAAAAABD"," D         D "},
                {" D         D ","DBAAAAAAAAABD"," A         A "," AE E E E EA "," FE E E E EF "," AE E E E EA "," A         A ","DBAAAAAAAAABD"," D         D "},
                {" D         D ","DBAAAAAAAAABD"," AE E E E EA "," L         R "," LE E E E ER "," L         R "," AE E E E EA ","DBAAAAAAAAABD"," D         D "},
                {" D         D ","DBAAAAAAAAABD"," FE E E E EF "," LE E E E ER "," LCCCCCCCCCR "," LE E E E ER "," FE E E E EF ","DBAAAAAAAAABD"," D         D "},
                {" D         D ","DBAAAAAAAAABD"," AE E E E EA "," L         R "," LE       ER "," L         R "," AE E E E EA ","DBAAAAAAAAABD"," D         D "},
                {" D         D ","DBAAAAAAAAABD"," A         A "," AE E E E EA "," FE E E E EF "," AE E E E EA "," A         A ","DBAAAAAAAAABD"," D         D "},
                {"DBBBBB~BBBBBD","DBBBBBBBBBBBD","DBBBBBBBBBBBD","DBBBBBBBBBBBD","DBBBBBBBBBBBD","DBBBBBBBBBBBD","DBBBBBBBBBBBD","DBBBBBBBBBBBD","DBBBBBBBBBBBD"}
            }))
        //spotless:on
        .addElement('A', chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier))
        .addElement(
            'B',
            buildHatchAdder(MTEMegaOilCracker.class).atLeast(Energy.or(ExoticEnergy), Maintenance, InputBus)
                .casingIndex(Casings.NaquadahReinforcedDistillationCasing.textureId)
                .hint(1)
                .buildAndChain(
                    onElementPass(
                        MTEMegaOilCracker::onCasingAdded,
                        Casings.NaquadahReinforcedDistillationCasing.asElement())))
        .addElement('C', Casings.SteelPipeCasing.asElement())
        .addElement('D', Casings.CleanStainlessSteelMachineCasing.asElement())
        .addElement(
            'E', // coils
            GTStructureChannels.HEATING_COIL
                .use(activeCoils(ofCoil(MTEMegaOilCracker::setCoilLevel, MTEMegaOilCracker::getCoilLevel))))
        .addElement('F', ofSheetMetal(Materials.Naquadah))
        .addElement(
            'M',
            buildHatchAdder(MTEMegaOilCracker.class)
                .atLeast(InputHatch.withAdder(MTEMegaOilCracker::addMiddleInputToMachineList))
                .casingIndex(Casings.NaquadahReinforcedDistillationCasing.textureId)
                .hint(3)
                .buildAndChain(
                    onElementPass(
                        MTEMegaOilCracker::onCasingAdded,
                        Casings.NaquadahReinforcedDistillationCasing.asElement())))
        .addElement(
            'L',
            buildHatchAdder(MTEMegaOilCracker.class)
                .atLeast(InputHatch.withAdder(MTEMegaOilCracker::addLeftHatchToMachineList))
                .casingIndex(Casings.NaquadahReinforcedDistillationCasing.textureId)
                .hint(2)
                .buildAndChain(
                    onElementPass(
                        MTEMegaOilCracker::onCasingAdded,
                        Casings.NaquadahReinforcedDistillationCasing.asElement())))
        .addElement(
            'R',
            buildHatchAdder(MTEMegaOilCracker.class)
                .atLeast(OutputHatch.withAdder(MTEMegaOilCracker::addRightHatchToMachineList))
                .casingIndex(Casings.NaquadahReinforcedDistillationCasing.textureId)
                .hint(4)
                .buildAndChain(
                    onElementPass(
                        MTEMegaOilCracker::onCasingAdded,
                        Casings.NaquadahReinforcedDistillationCasing.asElement())))
        .build();

    public MTEMegaOilCracker(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEMegaOilCracker(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEMegaOilCracker> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMegaOilCracker(this.mName);
    }

    private void onCasingAdded() {
        casingAmount++;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(Casings.NaquadahReinforcedDistillationCasing.getTextureId()),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MEGA_OIL_CRACKER_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MEGA_OIL_CRACKER_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(Casings.NaquadahReinforcedDistillationCasing.getTextureId()),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MEGA_OIL_CRACKER)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MEGA_OIL_CRACKER_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(Casings.NaquadahReinforcedDistillationCasing.getTextureId()) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Cracker, MOC")
            .addInfo(
                TooltipHelper.coloredText(
                    TooltipHelper.italicText("\"Thermally cracks heavy hydrocarbons into lighter fractions\""),
                    EnumChatFormatting.DARK_GRAY))
            .addStaticParallelInfo(Configuration.Multiblocks.megaMachinesMax)
            .addInfo("EU Usage = " + TooltipHelper.effText("0.9^") + TooltipHelper.tierText(TooltipTier.COIL) + " Tier")
            .addSeparator()
            .addInfo("Gives different benefits whether it hydro or steam-cracks:")
            .addInfo(
                "Hydro - Consumes " + TooltipHelper.coloredText("20%", EnumChatFormatting.DARK_AQUA)
                    + " less Hydrogen and outputs "
                    + TooltipHelper.coloredText("25%", EnumChatFormatting.DARK_AQUA)
                    + " more cracked fluid")
            .addInfo(
                "Steam - Outputs " + TooltipHelper.coloredText("50%", EnumChatFormatting.DARK_AQUA)
                    + " more cracked fluid")
            .addInfo(TooltipHelper.italicText("In comparison to a chemical reactor"))
            .addSeparator()
            .addTecTechHatchInfo()
            .addMinGlassForLaser(VoltageIndex.UV)
            .addGlassEnergyLimitInfo()
            .addUnlimitedTierSkips()
            .beginStructureBlock(13, 8, 9, true)
            .addController("Front bottom center")
            .addCasingInfoMin("Naquadah Reinforced Distillation Machine Casing", 145, false)
            .addCasingInfoExactly("Clean Stainless Steel Machine Casing", 84, false)
            .addCasingInfoExactly("Heating Coil", 77, true)
            .addCasingInfoExactly("Any Tiered Glass", 162, true)
            .addCasingInfoExactly("Steel Pipe Casing", 9, false)
            .addInputBus("Any Base Naquadah Reinforced Distillation Machine Casing, for Programmed Circuits", 1)
            .addEnergyHatch("Any Base Naquadah Reinforced Distillation Machine Casing", 1)
            .addMaintenanceHatch("Any Base Naquadah Reinforced Distillation Machine Casing", 1)
            .addInputHatch("Left Side Section, Uncracked Fluid Only", 2)
            .addInputHatch("Top Side Section, Steam/Hydrogen Only", 3)
            .addOutputHatch("Right Side Section", 4)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .addSubChannelUsage(GTStructureChannels.HEATING_COIL)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            HORIZONTAL_OFFSET,
            VERTICAL_OFFSET,
            DEPTH_OFFSET,
            realBudget,
            env,
            false,
            true);
    }

    public HeatingCoilLevel getCoilLevel() {
        return this.heatLevel;
    }

    public void setCoilLevel(HeatingCoilLevel aCoilLevel) {
        this.heatLevel = aCoilLevel;
    }

    private int glassTier = -1;
    private HeatingCoilLevel heatLevel;
    protected final List<MTEHatchInput> mMiddleInputHatches = new ArrayList<>();
    protected int mInputOnSide = -1;
    protected int mOutputOnSide = -1;
    protected int casingAmount;

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        this.glassTier = -1;
        this.casingAmount = 0;
        this.mInputOnSide = -1;
        this.mOutputOnSide = -1;
        this.mMiddleInputHatches.clear();
        if (!this.checkPiece(STRUCTURE_PIECE_MAIN, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET, errors)) return;
        checkOneMaintenanceHatch(errors);
        checkHasAnyEnergy(errors);
        checkCasingMin(errors, casingAmount, 145);
        checkHasInputHatch(errors);
        checkHasOutputHatch(errors);
        if (this.glassTier < VoltageIndex.UV) {
            for (MTEHatch hatch : this.mExoticEnergyHatches) {
                if (hatch.getConnectionType() == MTEHatch.ConnectionType.LASER) {
                    errors.add(StructureErrors.glassTierNotEnough(VoltageIndex.UV));
                    return;
                }
            }
        }
        for (MTEHatch mEnergyHatch : this.getExoticAndNormalEnergyHatchList()) {
            if (this.glassTier < mEnergyHatch.getTierForStructure()) {
                errors.add(StructureErrorRegistry.ENERGY_TIER_EXCEED_GLASS);
                break;
            }
        }
    }

    private boolean addLeftHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        addIfSmartInput(aMetaTileEntity);
        if (aMetaTileEntity instanceof MTEHatchInput tHatch) {
            if (this.mInputOnSide == 1) {
                return false;
            }
            this.mInputOnSide = 0;
            this.mOutputOnSide = 1;
            tHatch.updateTexture(aBaseCasingIndex);
            tHatch.mRecipeMap = this.getRecipeMap();
            return this.mInputHatches.add(tHatch);
        }
        if (aMetaTileEntity instanceof MTEHatchOutput tHatch) {
            if (this.mOutputOnSide == 1) {
                return false;
            }
            this.mInputOnSide = 1;
            this.mOutputOnSide = 0;
            tHatch.updateTexture(aBaseCasingIndex);
            return this.mOutputHatches.add(tHatch);
        }
        return false;
    }

    private boolean addRightHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        addIfSmartInput(aMetaTileEntity);
        if (aMetaTileEntity instanceof MTEHatchInput tHatch) {
            if (this.mInputOnSide == 0) {
                return false;
            }
            this.mInputOnSide = 1;
            this.mOutputOnSide = 0;
            tHatch.updateTexture(aBaseCasingIndex);
            tHatch.mRecipeMap = this.getRecipeMap();
            return this.mInputHatches.add(tHatch);
        }
        if (aMetaTileEntity instanceof MTEHatchOutput tHatch) {
            if (this.mOutputOnSide == 0) {
                return false;
            }
            this.mInputOnSide = 0;
            this.mOutputOnSide = 1;
            tHatch.updateTexture(aBaseCasingIndex);
            return this.mOutputHatches.add(tHatch);
        }
        return false;
    }

    private boolean addMiddleInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof MTEHatchInput tHatch) {
            tHatch.updateTexture(aBaseCasingIndex);
            tHatch.mRecipeMap = this.getRecipeMap();
            return this.mMiddleInputHatches.add(tHatch);
        }
        return false;
    }

    @Override
    public ArrayList<FluidStack> getStoredFluidsForColor(Optional<Byte> color) {
        final ArrayList<FluidStack> rList = new ArrayList<>();
        Map<Fluid, FluidStack> inputsFromME = new HashMap<>();
        for (final MTEHatchInput tHatch : validMTEList(mInputHatches)) {
            byte hatchColor = tHatch.getBaseMetaTileEntity()
                .getColorization();
            if (color.isPresent() && hatchColor != -1 && hatchColor != color.get()) continue;
            tHatch.mRecipeMap = getRecipeMap();
            if (tHatch instanceof MTEHatchInputME meHatch) {
                for (FluidStack tFluid : meHatch.getStoredFluids()) {
                    if (tFluid != null && !getRecipeMap().getBackend()
                        .isValidCatalystFluid(tFluid)) {
                        inputsFromME.put(tFluid.getFluid(), tFluid);
                    }
                }
            } else if (tHatch instanceof MTEHatchMultiInput) {
                for (final FluidStack tFluid : ((MTEHatchMultiInput) tHatch).getStoredFluid()) {
                    if (tFluid != null && !getRecipeMap().getBackend()
                        .isValidCatalystFluid(tFluid)) {
                        rList.add(tFluid);
                    }
                }
            } else {
                if (tHatch.getFillableStack() != null) {
                    if (!getRecipeMap().getBackend()
                        .isValidCatalystFluid(tHatch.getFillableStack())) rList.add(tHatch.getFillableStack());
                }
            }
        }
        for (final MTEHatchInput tHatch : validMTEList(mMiddleInputHatches)) {
            byte hatchColor = tHatch.getBaseMetaTileEntity()
                .getColorization();
            if (color.isPresent() && hatchColor != -1 && hatchColor != color.get()) continue;
            tHatch.mRecipeMap = getRecipeMap();
            if (tHatch instanceof MTEHatchInputME meHatch) {
                for (FluidStack tFluid : meHatch.getStoredFluids()) {
                    if (tFluid != null && getRecipeMap().getBackend()
                        .isValidCatalystFluid(tFluid)) {
                        inputsFromME.put(tFluid.getFluid(), tFluid);
                    }
                }
            } else if (tHatch instanceof MTEHatchMultiInput) {
                for (final FluidStack tFluid : ((MTEHatchMultiInput) tHatch).getStoredFluid()) {
                    if (tFluid != null && getRecipeMap().getBackend()
                        .isValidCatalystFluid(tFluid)) {
                        rList.add(tFluid);
                    }
                }
            } else {
                if (tHatch.getFillableStack() != null) {
                    final FluidStack tStack = tHatch.getFillableStack();
                    if (getRecipeMap().getBackend()
                        .isValidCatalystFluid(tStack)) {
                        rList.add(tStack);
                    }
                }
            }
        }
        if (!inputsFromME.isEmpty()) {
            rList.addAll(inputsFromME.values());
        }
        return rList;
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(this.getMaxInputEu());
        logic.setAvailableAmperage(1);
        logic.setUnlimitedTierSkips();
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setMaxParallelSupplier(this::getTrueParallel)
            .setEuModifierSupplier(this::getEuModifier);
    }

    @Override
    public int getMaxParallelRecipes() {
        return Configuration.Multiblocks.megaMachinesMax;
    }

    public double getEuModifier() {
        return Math.pow(0.9, this.heatLevel.getTier() + 1);
    }

    @Override
    public RecipeMap<OilCrackerBackend> getRecipeMap() {
        return RecipeMaps.crackingRecipes;
    }

    @Override
    public void startRecipeProcessing() {
        for (MTEHatchInput hatch : validMTEList(mMiddleInputHatches)) {
            if (hatch instanceof IRecipeProcessingAwareHatch aware) {
                aware.startRecipeProcessing();
            }
        }
        super.startRecipeProcessing();
    }

    @Override
    public void endRecipeProcessing() {
        super.endRecipeProcessing();
        for (MTEHatchInput hatch : validMTEList(mMiddleInputHatches)) {
            if (hatch instanceof IRecipeProcessingAwareHatch aware) {
                setResultIfFailure(aware.endRecipeProcessing(this));
            }
        }
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }
    // doesn't support input separation.

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GTCEU_LOOP_FIRE;
    }
}
