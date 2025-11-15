package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.NAC_MODULE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;

import org.jetbrains.annotations.NotNull;

import com.google.common.primitives.Bytes;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.gui.MTEMultiBlockBaseGui;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.gui.modularui.multiblock.SplitterGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchVacuumConveyorInput;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchVacuumConveyorOutput;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleStructureDefinition;

public class Splitter extends MTENanochipAssemblyModuleBase<Splitter> {

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final int SPLITTER_OFFSET_X = 3;
    protected static final int SPLITTER_OFFSET_Y = 2;
    protected static final int SPLITTER_OFFSET_Z = 0;
    protected static final String[][] SPLITTER_STRUCTURE = new String[][] { { "  A    ", " CBBBC " },
        { "  AA A ", "CBBBBBC" }, { "   ACA ", "BBBBBBB" }, { "AA A A ", "BBBBBBB" }, { " ACA AA", "BBBBBBB" },
        { " A A   ", "CBBBBBC" }, { "   AA  ", " CBBBC " } };

    // Maps the "id" of a rule to the rule it represents. Don't use this to lookup output colors, use
    // Splitter$getOutputColors instead.
    public Map<Integer, ColorRule> colorMap = new HashMap<>();

    public static final IStructureDefinition<Splitter> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<Splitter>builder()
        .addShape(STRUCTURE_PIECE_MAIN, SPLITTER_STRUCTURE)
        // White casing block
        .addElement('A', ofBlock(GregTechAPI.sBlockCasings8, 5))
        // Black casing block
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings8, 10))
        // Epoxy Resin Casing
        .addElement('C', ofFrame(Materials.EpoxidFiberReinforced))
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
        buildPiece(STRUCTURE_PIECE_MAIN, trigger, hintsOnly, SPLITTER_OFFSET_X, SPLITTER_OFFSET_Y, SPLITTER_OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            SPLITTER_OFFSET_X,
            SPLITTER_OFFSET_Y,
            SPLITTER_OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
    }

    public List<Byte> getGetOutputColors(byte color) {
        Set<Byte> set = new HashSet<>();
        for (Map.Entry<Integer, ColorRule> entry : colorMap.entrySet()) {
            ColorRule rule = entry.getValue();
            if (!rule.getInputColors()
                .contains(color)) continue;
            set.addAll(rule.getOutputColors());
        }
        return new ArrayList<>(set);
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
        if (!checkPiece(STRUCTURE_PIECE_MAIN, SPLITTER_OFFSET_X, SPLITTER_OFFSET_Y, SPLITTER_OFFSET_Z)) {
            return false;
        }
        assignHatchIdentifiers();
        return true;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Splitter, NAC Module")
            .addInfo(NAC_MODULE)
            .addInfo("Splits inputs of the same " + rainbowColor(false) + " evenly into their respective outputs")
            .addInfo("You can add Rules to override what " + rainbowColor(false) + " inputs will go to")
            .addInfo(
                "If a rule has multiple output " + rainbowColor(true)
                    + ", inputs will be split evenly into those as well")
            .addInfo(
                "If there are multiple rules of the same " + rainbowColor(false) + ", they will be treated as if they")
            .addInfo("were merged into a single rule")
            .addStructureInfo("Any base casing - Vacuum Conveyor Input")
            .addStructureInfo("Any base casing - Vacuum Conveyor Output")
            .toolTipFinisher();
        return tt;
    }

    private static String rainbowColor(boolean plural) {
        // spotless:off
        return
            EnumChatFormatting.RED + "c" +
            EnumChatFormatting.YELLOW + "o" +
            EnumChatFormatting.GREEN + "l" +
            EnumChatFormatting.AQUA + "o" +
            EnumChatFormatting.LIGHT_PURPLE + "r" +
            (plural ? EnumChatFormatting.DARK_PURPLE + "s" : "")
            + EnumChatFormatting.GRAY;
        // spotless:on
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new Splitter(this.mName);
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {

        // Note, during this checkProcessing() we set the outputs ourselves.
        // The mOutputItems doesn't work for our use-case of splitting itemStacks.
        if (!isConnected()) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        // First step in recipe checking is finding all inputs we have to deal with.
        // As a result of this process, we also get the colors of the hatch each item is found in, which
        // we will use for routing the outputs
        ItemInputInformation inputInfo = refreshInputItems();

        Map<GTUtility.ItemId, Byte> colors = inputInfo.colors;
        Map<GTUtility.ItemId, ItemStack> inputs = inputInfo.inputs;

        for (Map.Entry<GTUtility.ItemId, Byte> color : colors.entrySet()) {
            Byte currentDye = color.getValue();
            ItemStack stack = inputs.get(color.getKey());
            if (currentDye == -1) continue;

            List<Byte> outputDyes = getGetOutputColors(currentDye);
            if (outputDyes == null) continue;

            // Find output hatches that have the color of the dye
            List<List<MTEHatchVacuumConveyorOutput>> availableOutputHatches = new ArrayList<>();
            for (Byte potentialDye : outputDyes) {
                List<MTEHatchVacuumConveyorOutput> outputHatchesForDye = this.vacuumConveyorOutputs
                    .findColoredHatches(potentialDye);
                if (outputHatchesForDye != null && !outputHatchesForDye.isEmpty()) {
                    availableOutputHatches.add(outputHatchesForDye);
                }
            }

            // Distribute the stack amongst the available output hatches.
            // TODO: Add randomness to even out the busses / groups?
            int numberOfGroups = availableOutputHatches.size();
            if (numberOfGroups == 0) continue;

            // First, split between groups.
            int itemsPerGroup = stack.stackSize / numberOfGroups;

            // How many groups should get 1 extra item, to split the remainder.
            int groupRemainder = stack.stackSize % numberOfGroups;

            // For each group
            for (int groupIndex = 0; groupIndex < numberOfGroups; groupIndex++) {
                List<MTEHatchVacuumConveyorOutput> group = availableOutputHatches.get(groupIndex);

                // Calculate items for this group (including remainder distribution)
                int itemsForThisGroup = itemsPerGroup + (groupIndex < groupRemainder ? 1 : 0);

                // Now split within the group
                int hatchesInGroup = group.size();
                int itemsPerBus = itemsForThisGroup / hatchesInGroup;
                // How many hatches should get 1 extra item, to split the remainder.
                int busRemainder = itemsForThisGroup % hatchesInGroup;

                // Distribute to each bus in the group
                for (int busIndex = 0; busIndex < hatchesInGroup; busIndex++) {
                    int itemsForThisBus = itemsPerBus + (busIndex < busRemainder ? 1 : 0);
                    // We can just output, we don't have to worry about packet size or anything.
                    ItemStack stackToOutput = new ItemStack(stack.getItem(), itemsForThisBus, stack.getItemDamage());
                    this.addOutput(stackToOutput, group.get(busIndex));
                    this.removeItemFromInputByColor(stackToOutput, currentDye);
                }
            }
        }
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setTag("rules", createRulesTagList());
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        colorMap = loadRulesTagList(aNBT.getTagList("rules", new NBTTagCompound().getId()));
    }

    public NBTTagList createRulesTagList() {
        NBTTagList list = new NBTTagList();
        for (Map.Entry<Integer, ColorRule> entry : colorMap.entrySet()) {
            ColorRule rule = entry.getValue();
            NBTTagCompound compound = new NBTTagCompound();
            compound.setTag("inputs", new NBTTagByteArray(Bytes.toArray(rule.getInputColors())));
            compound.setTag("outputs", new NBTTagByteArray(Bytes.toArray(rule.getOutputColors())));
            list.appendTag(compound);
        }
        return list;
    }

    public Map<Integer, ColorRule> loadRulesTagList(NBTTagList tagList) {
        Map<Integer, ColorRule> map = new HashMap<>();
        int sustain = 0;
        for (Object a : tagList.tagList) {
            if (!(a instanceof NBTTagCompound compound)) continue;
            List<Byte> inputs = Bytes.asList(compound.getByteArray("inputs"));
            List<Byte> outputs = Bytes.asList(compound.getByteArray("outputs"));
            map.put(sustain, new ColorRule(inputs, outputs));
            sustain++;
        }
        return map;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<Splitter> getGui() {
        return new SplitterGui(this);
    }

    public static class ColorRule {

        List<Byte> inputColors;
        List<Byte> outputColors;

        public ColorRule(List<Byte> inputs, List<Byte> outputs) {
            inputColors = inputs;
            outputColors = outputs;
        }

        public List<Byte> getInputColors() {
            return inputColors;
        }

        public List<Byte> getOutputColors() {
            return outputColors;
        }
    }
}
