package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SPLITTER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SPLITTER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SPLITTER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SPLITTER_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.CASING_INDEX_WHITE;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.gui.modularui.multiblock.MTESplitterModuleGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchSplitterRedstone;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchVacuumConveyorInput;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchVacuumConveyorOutput;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleStructureDefinition;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleTypes;
import gregtech.common.tileentities.machines.multi.nanochip.util.SplitterRule;

public class MTESplitterModule extends MTENanochipAssemblyModuleBase<MTESplitterModule> {

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final int SPLITTER_OFFSET_X = 3;
    protected static final int SPLITTER_OFFSET_Y = 2;
    protected static final int SPLITTER_OFFSET_Z = 0;
    protected static final String[][] SPLITTER_STRUCTURE = new String[][] { { "  A    ", " CBBBC " },
        { "  AA A ", "CBBBBBC" }, { "   ACA ", "BBBBBBB" }, { "AA A A ", "BBBBBBB" }, { " ACA AA", "BBBBBBB" },
        { " A A   ", "CBBBBBC" }, { "   AA  ", " CBBBC " } };

    public List<SplitterRule> rules = new ArrayList<>();
    public final RedstoneChannelInfo redstoneChannelInfo = new RedstoneChannelInfo();
    public final ArrayList<MTEHatchSplitterRedstone> redstoneHatches = new ArrayList<>();

    public static final IStructureDefinition<MTESplitterModule> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<MTESplitterModule>builder()
        .addShape(STRUCTURE_PIECE_MAIN, SPLITTER_STRUCTURE)
        // Nanochip Mesh Interface Casing
        .addElement(
            'A',
            buildHatchAdder(MTESplitterModule.class).hint(2)
                .casingIndex(Casings.NanochipMeshInterfaceCasing.getTextureId())
                .atLeast(SpecialHatchElement.redstoneHatch)
                .buildAndChain(Casings.NanochipMeshInterfaceCasing.asElement()))
        // Nanochip Reinforcement Casing
        .addElement('B', Casings.NanochipReinforcementCasing.asElement())
        // Kevlar FrameBox
        .addElement('C', ofFrame(Materials.Kevlar))
        .build();

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SPLITTER)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SPLITTER_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SPLITTER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SPLITTER)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SPLITTER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE) };
    }

    public MTESplitterModule(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTESplitterModule(String aName) {
        super(aName);
    }

    @Override
    public ModuleTypes getModuleType() {
        return ModuleTypes.Splitter;
    }

    @Override
    public IStructureDefinition<MTESplitterModule> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    private boolean addRedstoneHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity instanceof MTEHatchSplitterRedstone redstoneHatch) {
            redstoneHatch.updateTexture(aBaseCasingIndex);
            return this.redstoneHatches.add(redstoneHatch);
        }
        return false;
    }

    public int structureOffsetX() {
        return SPLITTER_OFFSET_X;
    }

    public int structureOffsetY() {
        return SPLITTER_OFFSET_Y;
    }

    public int structureOffsetZ() {
        return SPLITTER_OFFSET_Z;
    }

    public List<Byte> getGetOutputColors(byte color, ItemStack item) {
        Set<Byte> set = new HashSet<>();
        for (SplitterRule rule : rules) {
            if (!rule.appliesTo(color, item, redstoneChannelInfo)) continue;
            set.addAll(rule.outputColors);
        }
        return new ArrayList<>(set);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType(getModuleType().getMachineModeText())
            .addInfo(TOOLTIP_MODULE_DESCRIPTION)
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.module.splitter.action", TOOLTIP_CCs))
            .addSeparator()
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.module.splitter.body.1", TOOLTIP_COLORED))
            .addInfo(translateToLocal("GT5U.tooltip.nac.module.splitter.body.2"))
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.module.splitter.body.3", TOOLTIP_COLOR, TOOLTIP_COLOR))
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.module.splitter.body.4", TOOLTIP_CCs))
            .addSeparator()
            .addInfo(tooltipFlavorText(translateToLocal("GT5U.tooltip.nac.module.splitter.flavor.1")))
            .beginStructureBlock(7, 5, 7, false)
            .addController(translateToLocal("GT5U.tooltip.nac.module.controller"))
            // Nanochip Reinforcement Casing
            .addCasingInfoExactly(translateToLocal("gt.blockcasings12.2.name"), 37, false)
            // Nanochip Mesh Interface Casing
            .addCasingInfoExactly(translateToLocal("gt.blockcasings12.1.name"), 18, false)
            // Kevlar Frame Box
            .addCasingInfoExactly(
                translateToLocal("gt.blockframes.10.name").replace("%material", Materials.Kevlar.getLocalizedName()),
                10,
                false)
            .addStructureInfo(TOOLTIP_STRUCTURE_BASE_VCI)
            .addStructureInfo(TOOLTIP_STRUCTURE_BASE_VCO)
            .addStructureInfo(
                EnumChatFormatting.WHITE + "gt.blockmachines.hatch.splitter.redstone.name"
                    + EnumChatFormatting.GRAY
                    + "GT5U.tooltip.nac.module.splitter.redstone_hatch")
            .toolTipFinisher();
    }

    @Override
    public int getMaxRecipeDuration() {
        // Splitter holds no power
        return 0;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESplitterModule(this.mName);
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {

        // Note, during this checkProcessing() we set the outputs ourselves.
        // The mOutputItems doesn't work for our use-case of splitting itemStacks.
        if (!isConnected()) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        // Update redstone channel state
        redstoneChannelInfo.clear();
        for (var hatch : redstoneHatches) {
            redstoneChannelInfo.set(hatch.getChannel(), hatch.getRedstoneInput());
        }

        // Necessary because it's possible these maps are out of date
        // if players color a hatch between structure checks on the splitter.
        // Note: If it turns out the splitter is really laggy, instead we need to call it
        // in VacuumConveyorHatch.onColorChange
        this.vacuumConveyorInputs.fixConsistency();
        this.vacuumConveyorOutputs.fixConsistency();
        // Splitter logic needs to carefully separate input colors so we can't just use refreshInputItems, we have to do
        // it manually
        // Some day I'll refactor this, maybe.
        this.inputFakeItems.clear();
        for (ArrayList<MTEHatchVacuumConveyorInput> conveyorList : this.vacuumConveyorInputs.allHatches()) {
            for (MTEHatchVacuumConveyorInput conveyor : conveyorList) {
                // Get the contents of this hatch as fake items.
                if (conveyor.contents == null) continue;
                List<ItemStack> itemsInHatch = conveyor.contents.getItemRepresentations();
                // Get the color of the items in this hatch
                byte currentDye = conveyor.getColorization();
                if (currentDye == -1) continue;
                for (ItemStack stack : itemsInHatch) {
                    // Add it to the internal module fake item list
                    this.inputFakeItems.add(stack);
                    // Now process routing for this stack
                    List<Byte> outputDyes = getGetOutputColors(currentDye, stack);
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
                            ItemStack stackToOutput = new ItemStack(
                                stack.getItem(),
                                itemsForThisBus,
                                stack.getItemDamage());
                            this.addVCOutput(stackToOutput, group.get(busIndex));
                            this.removeItemFromInputByColor(stackToOutput, currentDye);
                        }
                    }
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
        rules = loadRulesTagList(aNBT.getTagList("rules", Constants.NBT.TAG_COMPOUND));
    }

    public NBTTagList createRulesTagList() {
        NBTTagList list = new NBTTagList();
        for (SplitterRule rule : rules) {
            list.appendTag(rule.saveToNBT());
        }
        return list;
    }

    public List<SplitterRule> loadRulesTagList(NBTTagList tagList) {
        List<SplitterRule> list = new ArrayList<>();
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound ruleTag = tagList.getCompoundTagAt(i);
            list.add(SplitterRule.loadFromNBT(ruleTag));
        }
        return list;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<MTESplitterModule> getGui() {
        return new MTESplitterModuleGui(this);
    }

    public static class RedstoneChannelInfo {

        private Map<Integer, Integer> levels = new HashMap<>();

        public void clear() {
            levels.clear();
        }

        public int get(int channel) {
            return levels.getOrDefault(channel, 0);
        }

        public void set(int channel, int value) {
            levels.put(channel, value);
        }
    }

    private enum SpecialHatchElement implements IHatchElement<MTESplitterModule> {

        redstoneHatch(MTESplitterModule::addRedstoneHatchToMachineList, MTEHatchSplitterRedstone.class) {

            @Override
            public long count(MTESplitterModule splitterModule) {
                return splitterModule.redstoneHatches.size();
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGTHatchAdder<MTESplitterModule> adder;

        @SafeVarargs
        SpecialHatchElement(IGTHatchAdder<MTESplitterModule> adder, Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        public IGTHatchAdder<? super MTESplitterModule> adder() {
            return adder;
        }
    }
}
