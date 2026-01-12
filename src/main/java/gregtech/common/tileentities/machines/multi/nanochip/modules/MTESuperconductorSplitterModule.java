package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.NAC_MODULE;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.TOOLTIP_CC;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleStructureDefinition;

public class MTESuperconductorSplitterModule extends MTENanochipAssemblyModuleBase<MTESuperconductorSplitterModule> {

    protected static final int COOLANT_CONSUMED_PER_SEC = 1000;

    private MTEHatchInput coolantInputHatch;

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final int SUPERCOND_SPLITTER_OFFSET_X = 3;
    protected static final int SUPERCOND_SPLITTER_OFFSET_Y = 7;
    protected static final int SUPERCOND_SPLITTER_OFFSET_Z = 0;
    protected static final String[][] SUPERCOND_SPLITTER_STRUCTURE = new String[][] {
        { "       ", "       ", " DEEED ", "       ", "       ", "       ", " DEEED " },
        { "       ", " D   D ", "DBD DBD", " B   B ", " B   B ", " B   B ", "DDD DDD" },
        { "   C   ", "  CFC  ", "EDCFCDE", "  CFC  ", "  CFC  ", "  CFC  ", "EDCFCDE" },
        { "  CCC  ", "  FAF  ", "E FAF E", "  FAF  ", "  FAF  ", "  FAF  ", "E FAF E" },
        { "   C   ", "  CFC  ", "EDCFCDE", "  CFC  ", "  CFC  ", "  CFC  ", "EDCFCDE" },
        { "       ", " D   D ", "DBD DBD", " B   B ", " B   B ", " B   B ", "DDD DDD" },
        { "       ", "       ", " DEEED ", "       ", "       ", "       ", " DEEED " } };

    public static final IStructureDefinition<MTESuperconductorSplitterModule> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<MTESuperconductorSplitterModule>builder()
        .addShape(STRUCTURE_PIECE_MAIN, SUPERCOND_SPLITTER_STRUCTURE)
        // UHV Solenoid
        .addElement('A', ofBlock(GregTechAPI.sSolenoidCoilCasings, 7))
        // UEV Solenoid
        .addElement('B', ofBlock(GregTechAPI.sSolenoidCoilCasings, 8))
        // White casing block
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings8, 5))
        // Black casing block
        .addElement('D', ofBlock(GregTechAPI.sBlockCasings8, 10))
        // Naquadah Frame box
        .addElement('E', ofFrame(Materials.Naquadah))
        // Black glass
        .addElement('F', ofBlock(GregTechAPI.sBlockTintedGlass, 3))
        .build();

    public MTESuperconductorSplitterModule(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTESuperconductorSplitterModule(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTESuperconductorSplitterModule> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        buildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            hintsOnly,
            SUPERCOND_SPLITTER_OFFSET_X,
            SUPERCOND_SPLITTER_OFFSET_Y,
            SUPERCOND_SPLITTER_OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            SUPERCOND_SPLITTER_OFFSET_X,
            SUPERCOND_SPLITTER_OFFSET_Y,
            SUPERCOND_SPLITTER_OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        // Check base structure
        if (!super.checkMachine(aBaseMetaTileEntity, aStack)) return false;
        // Add coolant hatch
        if (!findCoolantHatch()) return false;
        // Now check module structure
        return checkPiece(
            STRUCTURE_PIECE_MAIN,
            SUPERCOND_SPLITTER_OFFSET_X,
            SUPERCOND_SPLITTER_OFFSET_Y,
            SUPERCOND_SPLITTER_OFFSET_Z);
    }

    private boolean findCoolantHatch() {
        if (!mInputHatches.isEmpty()) {
            coolantInputHatch = mInputHatches.get(0);
            return true;
        }
        return false;

    }

    private int ticker = 0;

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (!super.onRunningTick(aStack)) {
            return false;
        }

        if (ticker % 20 == 0) {
            FluidStack fluidToBeDrained = Materials.SuperCoolant.getFluid(COOLANT_CONSUMED_PER_SEC);
            if (!drain(coolantInputHatch, fluidToBeDrained, true)) {
                stopMachine(ShutDownReasonRegistry.outOfFluid(fluidToBeDrained));
                return false;
            }
            ticker = 0;
        }

        ticker++;

        return true;
    }

    /**
     * Try to find a recipe in the recipe map using the given stored inputs
     *
     * @return A recipe if one was found, null otherwise
     */
    protected GTRecipe findRecipe(ArrayList<ItemStack> inputs) {
        RecipeMap<?> recipeMap = this.getRecipeMap();
        return recipeMap.findRecipeQuery()
            .items(inputs.toArray(new ItemStack[] {}))
            .find();
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType("NAC Module")
            .addInfo(NAC_MODULE)
            .addInfo("Splits your Superconductor " + TOOLTIP_CC + "s")
            .addInfo(
                "Requires " + EnumChatFormatting.BLUE + "1000L/s Super Coolant" + EnumChatFormatting.GRAY + " to run")
            .addInfo("Outputs into the VCO with the same color as the input VCI")
            .addStructureInfo("Any base casing - Vacuum Conveyor Input")
            .addStructureInfo("Any base casing - Input Hatch")
            .addStructureInfo("Any base casing - Vacuum Conveyor Output")
            .toolTipFinisher("GregTech");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESuperconductorSplitterModule(this.mName);
    }

    public static void registerLocalName(String unprocessedName, CircuitComponent component) {
        component.fallbackLocalizedName = unprocessedName + " Die";
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipSuperconductorSplitter;
    }
}
