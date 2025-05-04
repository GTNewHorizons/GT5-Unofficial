package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.modularui2.GTGuis.createPopUpPanel;
import static gregtech.api.modularui2.GTGuis.mteTemplatePanelBuilder;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.utils.Alignment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumChatFormatting;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IIcon;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.WidgetTree;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.CategoryList;
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

    // Maps an input color to an output color
    public Map<Byte, List<Byte>> colorMap = new HashMap<>();

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
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Imagine the factorio splitter but big")
            .addInfo("Can split")
            .toolTipFinisher();
        return tt;
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

    public Map<Byte, List<Byte>> createEmptyRulesList() {
        Map<Byte, List<Byte>> rulesMap = new HashMap<>();
        for (Dyes dye : Dyes.VALUES) {
            rulesMap.put(dye.mIndex, ImmutableList.of(dye.mIndex));
        }
        return rulesMap;
    }

    public NBTTagList createRulesTagList() {
        NBTTagList list = new NBTTagList();
        for (Map.Entry<Byte, List<Byte>> entry : colorMap.entrySet()) {
            Byte key = entry.getKey();
            List<Byte> value = entry.getValue();
            String outputsString = "";
            for (Byte color : value) {
                outputsString = outputsString + color + "+";
            }
            list.appendTag(new NBTTagString(key + "->" + outputsString));
        }
        return list;
    }

    public Map<Byte, List<Byte>> loadRulesTagList(NBTTagList tagList) {
        Map<Byte, List<Byte>> rulesMap = new HashMap<>();
        for (Object a : tagList.tagList) {
            if (!(a instanceof NBTTagString tag)) continue;
            // Obfuscated method returns the stored string
            String[] data = tag.func_150285_a_()
                .split("->");
            String[] outputData = data[1].split("\\+");
            List<Byte> outputs = new ArrayList<>();
            for (String string : outputData) {
                if (string.isEmpty()) break;
                outputs.add(Byte.valueOf(string));
            }
            rulesMap.put(Byte.valueOf(data[0]), outputs);
        }

        return rulesMap;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager) {
        ModularPanel ui = mteTemplatePanelBuilder(this, data, syncManager).build();
        IPanelHandler popupPanel = syncManager.panel("popup", (m, h) -> createRuleManagerPanel(syncManager), true);
        syncManager.syncValue(
            "map",
            0,
            new GenericSyncValue<>(() -> colorMap, map -> { colorMap = map; }, new ColorMapAdapter()));

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

        ListWidget<IWidget, CategoryList.Root> list = new ListWidget<>();
        list.childSeparator(IIcon.EMPTY_2PX);
        list.size(168, 138);
        list.pos(4, 21);

        // Add existing rules
        for (Map.Entry<Byte, List<Byte>> entry : colorMap.entrySet()) {
            Byte input = entry.getKey();
            List<Byte> outputs = entry.getValue();
            // Skip if the rule is empty
            if (outputs.contains(input) && outputs.size() == 1) continue;
            list.child(createColorManager(syncManager, ImmutableList.of(input), outputs));
        }

        return ui.child(list)
            .child(new ButtonWidget<>().onMousePressed(mouseButton -> {
                list.child(createColorManager(syncManager, null, null));
                WidgetTree.resize(ui);
                return true;
            })
                .pos(80, 4)
                .size(16, 16)
                .overlay(GuiTextures.ADD)
                .tooltip(tooltip -> tooltip.add("Add new Rule")))
            .child(
                GuiTextures.EXCLAMATION.asWidget()
                    .background(GTGuiTextures.BUTTON_STANDARD)
                    .tooltip(this::getRulesInfo)
                    .pos(144, 4)
                    .size(16, 16))
            .posRel(0.75F, 0.5F);
    }

    private void getRulesInfo(RichTooltip t) {
        t.pos(RichTooltip.Pos.ABOVE)
            .add("This is the rules manager\n")
            .add(
                "Here you can set a color of Vacuum Conveyor Input to always route to another color of Vacuum Conveyor Output.\n")
            .newLine()
            .add("Check out this awesome icon: ")
            .add(GTGuiTextures.PICTURE_GT_LOGO_STANDARD);
    }

    public IWidget createColorManager(PanelSyncManager syncManager, List<Byte> inputSelected,
        List<Byte> outputSelected) {
        ColorGridWidget inputGrid = new ColorGridWidget();
        ColorGridWidget outputGrid = new ColorGridWidget();

        GenericSyncValue<Map<Byte, List<Byte>>> map = (GenericSyncValue<Map<Byte, List<Byte>>>) syncManager
            .getSyncHandler("map:0");

        return new ParentWidget<>()
            // Arrow icon
            .child(
                GTGuiTextures.PROGRESSBAR_ARROW_STANDARD.getSubArea(0F, 0F, 1F, 0.5F)
                    .asWidget()
                    .size(20, 18)
                    .posRel(0.5F, 0.5F))
            .child(
                inputGrid.setInitialSelected(inputSelected)
                    .setMaxSelected(1)
                    .build()
                    .pos(5, 13))
            .child(
                outputGrid.setInitialSelected(outputSelected)
                    .setMaxSelected(16)
                    .build()
                    .pos(121, 13))
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
             IKey.str("[Hover]")
             .asWidget().tooltipBuilder(t -> getOutputInfo(t, outputGrid))
             .scale(0.8F)
             .alignment(Alignment.Center)
             .size(42, 8)
             .pos(120, 5))
            // Save button
            .child(
                new ButtonWidget<>()
                    .onMousePressed(a -> saveColorData(inputGrid.getSelected(), outputGrid.getSelected(), map))
                    .tooltip(tooltip -> tooltip.add("Save"))
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                    .posRel(0.5F, 0.1F)
                    .size(8, 8))
            .size(166, 58)
            .background(GTGuiTextures.BACKGROUND_POPUP_STANDARD);
    }

    public RichTooltip getOutputInfo(RichTooltip t, ColorGridWidget grid) {
        List<Byte> selected = grid.getSelected();
        int amount = selected.size();
        if (amount == 0) return t.add("None selected");
        t.pos(RichTooltip.Pos.ABOVE).add("Currently selected:\n");
        for (int i = 0; i < amount; i++) {
            boolean shouldNewLine = ((i % 3) == 0);
            Dyes color = Dyes.get(selected.get(i));
            t.add(color.getLocalizedDyeName() + (shouldNewLine ? "\n" : ", "));
        }
        return t;
    }

    private boolean saveColorData(List<Byte> input, List<Byte> output, GenericSyncValue<Map<Byte, List<Byte>>> map) {
        if (input.isEmpty() || output.isEmpty()) return false;
        for (Byte inputColor : input) {
            List<Byte> newOutputs = new ArrayList<>(output);
            colorMap.put(inputColor, newOutputs);
            map.setValue(colorMap);
        }
        return true;
    }

    private static class ColorMapAdapter implements IByteBufAdapter<Map<Byte, List<Byte>>> {

        @Override
        public Map<Byte, List<Byte>> deserialize(PacketBuffer buffer) {
            Map<Byte, List<Byte>> rulesMap = new HashMap<>();
            int size = buffer.readInt();
            for (int i = 0; i < size; i++) {
                Byte key = buffer.readByte();
                List<Byte> value = new ArrayList<>();
                int ruleCount = buffer.readInt();
                for (int j = 0; j < ruleCount; j++) {
                    value.add(buffer.readByte());
                }
                rulesMap.put(key, value);
            }
            return rulesMap;
        }

        @Override
        public void serialize(PacketBuffer buffer, Map<Byte, List<Byte>> map) {
            buffer.writeInt(map.size());
            for (Map.Entry<Byte, List<Byte>> entry : map.entrySet()) {
                Byte key = entry.getKey();
                List<Byte> value = entry.getValue();
                buffer.writeByte(key);
                buffer.writeInt(value.size());
                for (Byte dye : value) {
                    buffer.writeByte(dye);
                }
            }
        }

        @Override
        public boolean areEqual(@NotNull Map<Byte, List<Byte>> t1, @NotNull Map<Byte, List<Byte>> t2) {
            return t1.equals(t2);
        }
    }
}
