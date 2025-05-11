package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.modularui2.GTGuis.createPopUpPanel;
import static gregtech.api.modularui2.GTGuis.mteTemplatePanelBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumChatFormatting;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IIcon;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.WidgetTree;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.modularui2.widget.ColorGridWidget;
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

    // Maps the "id" of a rule to the rule it represents. Don't use this to lookup output colors, use
    // Splitter$getOutputColors instead.
    public Map<Integer, ColorRule> colorMap = new HashMap<>();

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

    public List<Byte> getGetOutputColors(byte color) {
        Set<Byte> set = new HashSet<>();
        for (Map.Entry<Integer, ColorRule> entry : colorMap.entrySet()) {
            ColorRule rule = entry.getValue();
            if (rule.getInputColor() != color) continue;
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
        if (!checkPiece(STRUCTURE_PIECE_MAIN, STRUCTURE_OFFSET_X, STRUCTURE_OFFSET_Y, STRUCTURE_OFFSET_Z)) {
            return false;
        }
        assignHatchIdentifiers();
        return true;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Splitter")
            .addInfo("Splits inputs of the same " + rainbowColor(false) + " evenly into their respective outputs")
            .addInfo("You can add Rules to override what " + rainbowColor(false) + " inputs will go to")
            .addInfo(
                "If a rule has multiple output " + rainbowColor(true)
                    + ", inputs will be split evenly into those as well")
            .addInfo(
                "If there are multiple rules of the same " + rainbowColor(false) + ", they will be treated as if they")
            .addInfo("were merged into a single rule")
            .beginStructureBlock(3, 3, 3, true)
            .addCasingInfoExactly("Robust Tungstensteel machine casing", 26, false)
            .addStructureInfo(EnumChatFormatting.BOLD + "Must be connected to a NAC")
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
        // Type "8" is NBTTagString
        colorMap = loadRulesTagList(aNBT.getTagList("rules", 8));
    }

    public NBTTagList createRulesTagList() {
        NBTTagList list = new NBTTagList();
        for (Map.Entry<Integer, ColorRule> entry : colorMap.entrySet()) {
            ColorRule rule = entry.getValue();
            StringBuilder outputsString = new StringBuilder();
            List<Byte> outputs = rule.getOutputColors();
            for (int i = 0; i < outputs.size(); i++) {
                outputsString.append(outputs.get(i));
                if (i != outputs.size() - 1) outputsString.append("+");
            }
            list.appendTag(new NBTTagString(entry.getKey() + "+" + rule.getInputColor() + "->" + outputsString));
        }
        return list;
    }

    public Map<Integer, ColorRule> loadRulesTagList(NBTTagList tagList) {
        Map<Integer, ColorRule> map = new HashMap<>();
        for (Object a : tagList.tagList) {
            if (!(a instanceof NBTTagString t)) continue;
            // Obfuscated method returns the stored string
            String tag = t.func_150285_a_();
            String[] data = tag.split("->");
            String[] inputs = data[0].split("\\+");
            Set<Byte> outputs = new HashSet<>();
            for (String string : data[1].split("\\+")) {
                outputs.add(Byte.valueOf(string));
            }
            int id = Integer.parseInt(inputs[0]);
            map.put(id, new ColorRule(Byte.valueOf(inputs[1]), new ArrayList<>(outputs)));
        }
        return map;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager) {
        ModularPanel ui = mteTemplatePanelBuilder(this, data, syncManager).build();
        IPanelHandler popupPanel = syncManager.panel("popup", (m, h) -> createRuleManagerPanel(syncManager), true);
        GenericSyncValue<Map<Integer, ColorRule>> listSyncer = new GenericSyncValue<>(
            () -> colorMap,
            map -> { colorMap = map; },
            new ColorMapAdapter());
        syncManager.syncValue("rules", 0, listSyncer);

        return ui.child(new ButtonWidget<>().onMousePressed(mouseButton -> {
            popupPanel.openPanel();
            return popupPanel.isPanelOpen();
        })
            .background(GTGuiTextures.BUTTON_STANDARD)
            .tooltip(tooltip -> tooltip.add("Add Rule"))
            .pos(153, 5)
            .size(18, 18));
    }

    public ModularPanel createRuleManagerPanel(PanelSyncManager syncManager) {
        ModularPanel ui = createPopUpPanel("gt:splitter:rules_manager", false, false);

        ListWidget<IWidget, ?> list = new ListWidget<>();
        list.childSeparator(IIcon.EMPTY_2PX);
        list.size(168, 138);
        list.pos(4, 21);

        // Add existing rules
        for (Map.Entry<Integer, ColorRule> entry : colorMap.entrySet()) {
            int id = entry.getKey();
            ColorRule rule = entry.getValue();
            if (rule == null) continue;
            Byte input = rule.getInputColor();
            List<Byte> outputs = rule.getOutputColors();
            // spotless:off
            list.child(createColorManager(syncManager, Stream.of(input).collect(Collectors.toList()), outputs, id));
            // spotless:on
        }

        return ui.child(list)
            .child(new ButtonWidget<>().onMousePressed(mouseButton -> {
                list.child(createColorManager(syncManager, null, null, null));
                WidgetTree.resize(ui);
                return true;
            })
                .pos(80, 4)
                .size(16, 16)
                .overlay(GuiTextures.ADD)
                .tooltip(tooltip -> tooltip.add("Add new Rule")))
            .posRel(0.75F, 0.5F);
    }

    public IWidget createColorManager(PanelSyncManager syncManager, List<Byte> inputSelected, List<Byte> outputSelected,
        Integer indexOverride) {
        ColorGridWidget inputGrid = new ColorGridWidget();
        ColorGridWidget outputGrid = new ColorGridWidget();
        ColorGridSelector selector = new ColorGridSelector(syncManager, indexOverride);

        return selector
            // Arrow icon
            .child(
                GTGuiTextures.PROGRESSBAR_ARROW_STANDARD.getSubArea(0F, 0F, 1F, 0.5F)
                    .asWidget()
                    .size(20, 18)
                    .posRel(0.5F, 0.5F))
            .setInputGrid(
                (ColorGridWidget) inputGrid.setInitialSelected(inputSelected)
                    .setMaxSelected(1)
                    .build()
                    .pos(5, 17))
            .setOutputGrid(
                (ColorGridWidget) outputGrid.setInitialSelected(outputSelected)
                    .setMaxSelected(16)
                    .build()
                    .pos(121, 17))
            // Input grid color display
            .child(
                IKey.dynamic(() -> inputGrid.getName(0))
                    .asWidget()
                    .scale(0.8F)
                    .alignment(Alignment.Center)
                    .size(42, 8)
                    .pos(4, 5))
            // Output grid color display
            .child(
                // spotless makes this look vile and disgusting and abominable and atrocious and yucky and horrid and
                // offensive to the eyes and nasty and foul and repugnant and abhorrent and deplorable and nauseating
                // and Dirty
                // spotless:off
                IKey.dynamic(() -> switch (outputGrid.getAmountSelected()) {
                        case 0: yield "None";
                        case 1: yield outputGrid.getName(0);
                        default: yield "[Hover]";
                    })
                // spotless:on
                    .asWidget()
                    .tooltipAutoUpdate(true)
                    .tooltipBuilder(t -> getInfo(t, outputGrid))
                    .scale(0.8F)
                    .alignment(Alignment.Center)
                    .size(42, 8)
                    .pos(120, 5))
            // Delete button
            .child(
                new ButtonWidget<>().tooltip(t -> t.add("Delete"))
                    .onMousePressed(a -> {
                        selector.removeColorData();
                        WidgetTree.resize(selector.getParent());
                        return true;
                    })
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_CROSS)
                    .pos(80, 5)
                    .size(8, 8))
            .size(166, 58)
            .background(GTGuiTextures.BACKGROUND_POPUP_STANDARD);
    }

    public void getInfo(RichTooltip t, ColorGridWidget grid) {
        List<Byte> selected = grid.getSelected();
        int amount = selected.size();
        if (amount < 2) return;
        t.pos(RichTooltip.Pos.ABOVE)
            .add("Currently selected:\n");
        for (int i = 0; i < amount; i++) {
            boolean shouldNewLine = (((i - 2) % 3) == 0);
            Dyes color = Dyes.get(selected.get(i));
            String name = color.getLocalizedDyeName();
            if (color == Dyes.dyeBlack) color = Dyes.dyeGray;
            t.add(color.formatting + name + IKey.RESET + (shouldNewLine ? "\n" : ", "));
        }
    }

    private class ColorGridSelector extends ParentWidget<ColorGridSelector> {

        int id;
        ColorGridWidget inputGrid;
        ColorGridWidget outputGrid;
        PanelSyncManager manager;
        GenericSyncValue<Map<Integer, ColorRule>> colorMapSyncer;

        public ColorGridSelector(PanelSyncManager syncManager, Integer indexOverride) {
            super();
            manager = syncManager;
            colorMapSyncer = (GenericSyncValue<Map<Integer, ColorRule>>) syncManager.getSyncHandler("rules:0");
            if (indexOverride == null) {
                while (colorMap.get(id) != null) {
                    id++;
                }
            } else id = indexOverride;
        }

        @Override
        public void onInit() {
            super.onInit();
            saveColorData();
        }

        public void removeSelector() {
            IWidget widget = getParent();
            if (!(widget instanceof ListWidget list)) return;
            list.getChildren()
                .remove(this);
        }

        public ColorGridSelector setInputGrid(ColorGridWidget widget) {
            inputGrid = widget.onButtonToggled(this::saveColorData);
            return this.child(inputGrid);
        }

        public ColorGridSelector setOutputGrid(ColorGridWidget widget) {
            outputGrid = widget.onButtonToggled(this::saveColorData);
            return this.child(outputGrid);
        }

        private void saveColorData() {
            if (manager.isClient()) {
                colorMap.put(id, thisAsRule());
                colorMapSyncer.setValue(colorMap);
            }
        }

        private void removeColorData() {
            if (manager.isClient()) {
                colorMap.remove(id);
                colorMapSyncer.setValue(colorMap);
            }
            removeSelector();
        }

        public ColorRule thisAsRule() {
            List<Byte> input = inputGrid.getSelected();
            List<Byte> output = outputGrid.getSelected();
            if (input.isEmpty()) input = ImmutableList.of((byte) -1);
            if (output.isEmpty()) output = ImmutableList.of((byte) -1);
            return new ColorRule(input.get(0), output);
        }
    }

    private static class ColorMapAdapter implements IByteBufAdapter<Map<Integer, ColorRule>> {

        @Override
        public Map<Integer, ColorRule> deserialize(PacketBuffer buffer) {
            Map<Integer, ColorRule> list = new HashMap<>();
            int size = buffer.readInt();
            for (int i = 0; i < size; i++) {
                int id = buffer.readInt();
                Byte input = buffer.readByte();
                List<Byte> outputs = new ArrayList<>();
                int ruleCount = buffer.readInt();
                for (int j = 0; j < ruleCount; j++) {
                    outputs.add(buffer.readByte());
                }
                list.put(id, new ColorRule(input, outputs));
            }
            return list;
        }

        @Override
        public void serialize(PacketBuffer buffer, Map<Integer, ColorRule> map) {
            buffer.writeInt(map.size());
            for (Map.Entry<Integer, ColorRule> entry : map.entrySet()) {
                ColorRule rule = entry.getValue();
                Byte input = rule.getInputColor();
                List<Byte> outputs = rule.getOutputColors();
                buffer.writeInt(entry.getKey());
                buffer.writeByte(input);
                buffer.writeInt(outputs.size());
                for (Byte dye : outputs) {
                    buffer.writeByte(dye);
                }
            }
        }

        @Override
        public boolean areEqual(Map<Integer, ColorRule> t1, Map<Integer, ColorRule> t2) {
            return t1.equals(t2);
        }
    }

    public static class ColorRule {

        Byte inputColor;
        List<Byte> outputColors;

        public ColorRule(Byte input, List<Byte> outputs) {
            inputColor = input;
            outputColors = outputs;
        }

        public Byte getInputColor() {
            return inputColor;
        }

        public List<Byte> getOutputColors() {
            return outputColors;
        }
    }
}
