package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.NAC_MODULE;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.TOOLTIP_CC;

import java.util.ArrayList;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchVacuumConveyorInput;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchVacuumConveyorOutput;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleStructureDefinition;

public class Splitter extends MTENanochipAssemblyModuleBase<Splitter> {

    protected static final int STRUCTURE_OFFSET_X = 3;
    protected static final int STRUCTURE_OFFSET_Y = 3;
    protected static final int STRUCTURE_OFFSET_Z = -2;

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String[][] structure = new String[][] { { "  AAA  ", "  AAA  ", "  AAA  " },
        { "  AAA  ", "  A A  ", "  AAA  " }, { "  AAA  ", "  AAA  ", "  AAA  " } };

    public static final IStructureDefinition<Splitter> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<Splitter>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        .addElement('A', ofBlock(GregTechAPI.sBlockCasings4, 0))
        .build();

    public Splitter(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected Splitter(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<Splitter> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        buildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            hintsOnly,
            STRUCTURE_OFFSET_X,
            STRUCTURE_OFFSET_Y,
            STRUCTURE_OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            STRUCTURE_OFFSET_X,
            STRUCTURE_OFFSET_Y,
            STRUCTURE_OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
    }

    private static EnumChatFormatting getPrefixColor(byte color) {
        return switch (color) {
            case 0 -> EnumChatFormatting.BLACK;
            case 1 -> EnumChatFormatting.RED;
            case 2 -> EnumChatFormatting.DARK_GREEN;
            case 3 -> EnumChatFormatting.DARK_RED;
            case 4 -> EnumChatFormatting.DARK_BLUE;
            case 5 -> EnumChatFormatting.DARK_AQUA;
            case 6 -> EnumChatFormatting.AQUA;
            case 7 -> EnumChatFormatting.GRAY;
            case 8 -> EnumChatFormatting.DARK_GRAY;
            case 9 -> EnumChatFormatting.LIGHT_PURPLE;
            case 10 -> EnumChatFormatting.GREEN;
            case 11 -> EnumChatFormatting.YELLOW;
            case 12 -> EnumChatFormatting.BLUE;
            case 13 -> EnumChatFormatting.DARK_PURPLE;
            case 14 -> EnumChatFormatting.GOLD;
            case 15 -> EnumChatFormatting.WHITE;
            default -> EnumChatFormatting.RESET;
        };
    }

    private void assignHatchIdentifiers() {
        // Assign ID of all hatches based on their color, index and whether they are an input or an output hatch.

        int hatchID = 0;
        for (Map.Entry<Byte, ArrayList<MTEHatchVacuumConveyorInput>> inputList : this.vacuumConveyorInputs.hatchMap()
            .entrySet()) {
            byte color = inputList.getKey();
            EnumChatFormatting colorFormat = getPrefixColor(color);
            ArrayList<MTEHatchVacuumConveyorInput> hatches = inputList.getValue();
            for (MTEHatchVacuumConveyorInput hatch : hatches) {
                hatch.identifier = colorFormat + "In/" + hatchID;
                hatchID += 1;
            }
        }

        hatchID = 0;
        for (Map.Entry<Byte, ArrayList<MTEHatchVacuumConveyorOutput>> outputList : this.vacuumConveyorOutputs.hatchMap()
            .entrySet()) {
            byte color = outputList.getKey();
            EnumChatFormatting colorFormat = getPrefixColor(color);
            ArrayList<MTEHatchVacuumConveyorOutput> hatches = outputList.getValue();
            for (MTEHatchVacuumConveyorOutput hatch : hatches) {
                hatch.identifier = colorFormat + "Out/" + hatchID;
                hatchID += 1;
            }
        }
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        // Check base structure
        if (!super.checkMachine(aBaseMetaTileEntity, aStack)) return false;
        // Now check module structure
        if (!checkPiece(STRUCTURE_PIECE_MAIN, STRUCTURE_OFFSET_X, STRUCTURE_OFFSET_Y, STRUCTURE_OFFSET_Z)) {
            return false;
        }
        assignHatchIdentifiers();
        return true;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addInfo(NAC_MODULE)
            .addInfo("Splits your " + TOOLTIP_CC + "s from one color VCI to one or more VCOs")
            .addInfo("Select the color mappings in the Controller GUI")
            .addStructureInfo("Any base casing - Vacuum Conveyor Input")
            .addStructureInfo("Any base casing - Vacuum Conveyor Output")
            .toolTipFinisher("GregTech");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new Splitter(this.mName);
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        // Always keep the machine running, it doesn't run recipes directly.
        if (isAllowedToWork()) {
            mEfficiencyIncrease = 10000;
            mMaxProgresstime = 1 * SECONDS;

            return CheckRecipeResultRegistry.SUCCESSFUL;
        }

        mEfficiencyIncrease = 0;
        mMaxProgresstime = 0;
        return CheckRecipeResultRegistry.NO_RECIPE;
    }
}
