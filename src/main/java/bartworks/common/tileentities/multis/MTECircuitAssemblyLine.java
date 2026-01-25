/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bartworks.common.tileentities.multis;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.SoundResource.GTCEU_LOOP_ASSEMBLER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTUtility.getColoredTierNameFromTier;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import bartworks.API.enums.CircuitImprint;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.CycleButtonWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;

import bartworks.API.modularUI.BWUITextures;
import bartworks.API.recipe.BartWorksRecipeMaps;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTECircuitAssemblyLine extends MTEEnhancedMultiBlockBase<MTECircuitAssemblyLine>
    implements ISurvivalConstructable {

    private enum Mode {

        CircuitAssemblyLine(0),
        CircuitAssembler(1);

        private final int index;

        Mode(int index) {
            this.index = index;
        }

        private static @NotNull Mode fromIndex(int index) {
            return switch (index) {
                case 0 -> Mode.CircuitAssemblyLine;
                case 1 -> Mode.CircuitAssembler;
                default -> {
                    GTMod.GT_FML_LOGGER.error("Invalid mode for Circuit Assembly Line. Falling back to default.");
                    yield Mode.CircuitAssemblyLine;
                }
            };
        }
    }

    private static final int CASING_INDEX = 16;

    private static final String STRUCTURE_PIECE_FIRST = "first";
    private static final String STRUCTURE_PIECE_NEXT = "next";
    private static final String STRUCTURE_PIECE_NEXT_HINT = "next_hint";
    private static final String STRUCTURE_PIECE_LAST = "last";

    private static final int MINIMUM_CIRCUIT_ASSEMBLER_LENGTH = 5;
    public static final String IMPRINT_KEY = "Type";
    public static final String IMPRINT_ID_KEY = "id";
    protected static final String LENGTH_KEY = "Length";
    protected static final String RUNNING_MODE_KEY = "RunningMode";

    private int length;
    private Mode mode = Mode.CircuitAssemblyLine;
    private int glassTier = -1;

    private CircuitImprint circuitImprint;
    private String test;

    private static final IStructureDefinition<MTECircuitAssemblyLine> STRUCTURE_DEFINITION = StructureDefinition
        .<MTECircuitAssemblyLine>builder()
        .addShape(
            STRUCTURE_PIECE_FIRST,
            transpose(new String[][] { { "~", "G", "G" }, { "g", "l", "g" }, { "b", "i", "b" }, }))
        .addShape(
            STRUCTURE_PIECE_NEXT,
            transpose(new String[][] { { "G", "G", "G" }, { "g", "l", "g" }, { "b", "I", "b" }, }))
        .addShape(
            STRUCTURE_PIECE_NEXT_HINT,
            transpose(new String[][] { { "G", "G", "G" }, { "g", "l", "g" }, { "b", "i", "b" }, }))
        .addShape(
            STRUCTURE_PIECE_LAST,
            transpose(new String[][] { { "G", "G", "G" }, { "g", "l", "g" }, { "b", "o", "b" }, }))
        .addElement(
            'G',
            buildHatchAdder(MTECircuitAssemblyLine.class).atLeast(Energy)
                .casingIndex(CASING_INDEX)
                .hint(1)
                .buildAndChain(GregTechAPI.sBlockCasings3, 10))
        .addElement('g', chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier))
        .addElement('l', ofBlock(GregTechAPI.sBlockCasings2, 5)) // assembly line casings
        .addElement(
            'b',
            buildHatchAdder(MTECircuitAssemblyLine.class).atLeast(InputHatch, Maintenance)
                .casingIndex(CASING_INDEX)
                .hint(2)
                .disallowOnly(ForgeDirection.EAST, ForgeDirection.WEST)
                .buildAndChain(GregTechAPI.sBlockCasings2, 0))
        .addElement('i', InputBus.newAny(CASING_INDEX, 3, ForgeDirection.DOWN))
        .addElement(
            'I',
            buildHatchAdder(MTECircuitAssemblyLine.class).atLeast(InputHatch, InputBus, OutputBus)
                .casingIndex(CASING_INDEX)
                .hint(2)
                .disallowOnly(ForgeDirection.EAST, ForgeDirection.WEST)
                .buildAndChain(GregTechAPI.sBlockCasings2, 0))
        .addElement('o', OutputBus.newAny(CASING_INDEX, 2, ForgeDirection.DOWN))
        .build();

    @Override
    public IStructureDefinition<MTECircuitAssemblyLine> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Circuit Assembler, CAL")
            .addInfo("Change Mode with Screwdriver")
            .addPerfectOCInfo()
            .addSeparator()
            .addInfo(EnumChatFormatting.GOLD + StatCollector.translateToLocal("chat.cal.mode.0") + ":")
            .addInfo("Imprint this machine with a Circuit Imprint,")
            .addInfo("by putting the imprint in the controller")
            .addInfo("Every Circuit Assembly Line can only be imprinted ONCE")
            .addSeparator()
            .addInfo(EnumChatFormatting.GOLD + StatCollector.translateToLocal("chat.cal.mode.1") + ":")
            .addInfo(
                "Does Circuit Assembler recipes, Minimum Length: " + EnumChatFormatting.RED
                    + MINIMUM_CIRCUIT_ASSEMBLER_LENGTH
                    + EnumChatFormatting.GRAY)
            .addInfo("Recipe tier in Circuit Assembler mode is at most Energy Hatch tier - 1")
            .addInfo("This mode supports Crafting Input Buffer/Bus and allows bus separation")
            .beginVariableStructureBlock(2, 7, 3, 3, 3, 3, false)
            .addStructureInfo("From Bottom to Top, Left to Right")
            .addStructureInfo(
                "Layer 1 - Solid Steel Machine Casing, Input bus (Last Output bus), Solid Steel Machine Casing")
            .addStructureInfo(
                "Layer 2 - " + getColoredTierNameFromTier((byte) 4)
                    + "+ Tier Glass, Assembly Line Casing, "
                    + getColoredTierNameFromTier((byte) 4)
                    + "+ Tier Glass")
            .addStructureInfo("Layer 3 - Grate Machine Casing")
            .addStructureInfo("Up to 7 repeating slices, last is Output Bus")
            .addController("Layer 3 first slice front")
            .addOtherStructurePart(
                "1x " + StatCollector.translateToLocal("GT5U.MBTT.EnergyHatch"),
                "Any layer 3 casing",
                1)
            .addInputHatch("Any layer 1 casing", 2)
            .addInputBus("As specified on layer 1", 3, 4)
            .addOutputBus("As specified in final slice on layer 1", 4)
            .addOtherStructurePart(
                StatCollector
                    .translateToLocalFormatted("tooltip.bw.structure.tier_glass", getColoredTierNameFromTier((byte) 4)),
                "As specified on layer 2",
                5)
            .addMaintenanceHatch("Any layer 1 casing", 2)

            .toolTipFinisher();
        return tt;
    }

    private void switchMode() {
        mode = switch (mode) {
            case CircuitAssembler -> Mode.CircuitAssemblyLine;
            case CircuitAssemblyLine -> Mode.CircuitAssembler;
        };
    }

    private void setMode(boolean value) {
        mode = value ? Mode.CircuitAssembler : Mode.CircuitAssemblyLine;
    }

    public String getTypeForDisplay() {

        if (!isImprinted()) return "";
        return this.circuitImprint.circuit.get(1).getDisplayName();
    }

    public MTECircuitAssemblyLine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    private MTECircuitAssemblyLine(String aName) {
        super(aName);
    }

    public boolean isImprinted() {
        return this.circuitImprint != null;
    }

    private boolean isValidImprint(ItemStack stack){
        return GTUtility.isStackValid(stack) &&
            GTUtility.areStacksEqual(stack, CircuitImprint.NANDChipArray.imprint.get(1), true) &&
            stack.getTagCompound() != null;
    }

    private boolean imprintMachine(ItemStack itemStack) {
        if (isImprinted()) return true;
        if (isValidImprint(itemStack)) {
            ItemStack imprintedCircuit = ItemStack.loadItemStackFromNBT(itemStack.getTagCompound());
            if (imprintedCircuit != null) {
                this.circuitImprint = CircuitImprint.IMPRINT_LOOKUPS_BY_UNLOCALISED_NAMES.get(imprintedCircuit.getUnlocalizedName());

                itemStack.stackSize -= 1;
                if (itemStack == getControllerSlot() && itemStack.stackSize <= 0) {
                    mInventory[getControllerSlotIndex()] = null;
                }
                this.getBaseMetaTileEntity()
                    .issueBlockUpdate();
                return true;
            }
        }
        return false;
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        if (aIndex == 20) {
            GTUtility.doSoundAtClient(GTCEU_LOOP_ASSEMBLER, 10, 1.0F, aX, aY, aZ);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        if (aNBT.hasKey(IMPRINT_KEY)) {// old NBT migration code
            String name = ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag(IMPRINT_KEY)).getUnlocalizedName();
            if (CircuitImprint.IMPRINT_LOOKUPS_BY_UNLOCALISED_NAMES.containsKey(name)) {
                this.circuitImprint = CircuitImprint.IMPRINT_LOOKUPS_BY_UNLOCALISED_NAMES.get(name);
            }
        }
        else{
            // IDs here will make sure we never shift again
            if (aNBT.hasKey(IMPRINT_ID_KEY)){
                int circuitID = aNBT.getInteger(IMPRINT_ID_KEY);
                this.circuitImprint = CircuitImprint.IMPRINT_LOOKUPS_BY_IDS.get(circuitID);
            }
        }
        mode = Mode.fromIndex(aNBT.getInteger(RUNNING_MODE_KEY));
        if (aNBT.hasKey(LENGTH_KEY)) {
            length = aNBT.getInteger(LENGTH_KEY);
        }
        super.loadNBTData(aNBT);
    }

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        if (isImprinted()) aNBT.setInteger(IMPRINT_ID_KEY, this.circuitImprint.id);
        super.setItemNBT(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        if (isImprinted()) aNBT.setInteger(IMPRINT_KEY, this.circuitImprint.id);
        aNBT.setInteger(RUNNING_MODE_KEY, mode.index);
        aNBT.setInteger(LENGTH_KEY, length);
        super.saveNBTData(aNBT);
    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (mode == Mode.CircuitAssemblyLine && !isImprinted() && getBaseMetaTileEntity().isServerSide()) {
            ItemStack heldItem = aPlayer.getHeldItem();
            if (imprintMachine(heldItem)) {
                if (heldItem.stackSize <= 0) {
                    aPlayer.inventory.setInventorySlotContents(aPlayer.inventory.currentItem, null);
                }
                return;
            }
        }
        super.onLeftclick(aBaseMetaTileEntity, aPlayer);
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (getBaseMetaTileEntity().isServerSide()) {
            switchMode();
            GTUtility.sendChatTrans(aPlayer, "chat.cal.mode." + mode.index);
        }
        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ, aTool);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return switch (mode) {
            case CircuitAssemblyLine -> BartWorksRecipeMaps.circuitAssemblyLineRecipes;
            case CircuitAssembler -> RecipeMaps.circuitAssemblerRecipes;
        };
    }

    @Nonnull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(BartWorksRecipeMaps.circuitAssemblyLineRecipes, RecipeMaps.circuitAssemblerRecipes);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            @Nonnull
            protected CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                // limit CA mode recipes to hatch tier - 1
                if (mode == Mode.CircuitAssembler
                    && recipe.mEUt > MTECircuitAssemblyLine.this.getMaxInputVoltage() / 4) {
                    return CheckRecipeResultRegistry.NO_RECIPE;
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        }.enablePerfectOverclock();
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        switch (mode) {
            case CircuitAssemblyLine -> {
                if (!this.imprintMachine(this.getControllerSlot())) {// Imprint check embedded in it
                    return SimpleCheckRecipeResult.ofFailure("no_imprint");
                }
            }
            case CircuitAssembler -> {
                if (length < MINIMUM_CIRCUIT_ASSEMBLER_LENGTH) {
                    return SimpleCheckRecipeResult.ofFailure("not_enough_length");
                }
            }
        }
        return super.checkProcessing();
    }

    @Override
    protected void setupProcessingLogic(ProcessingLogic logic) {
        super.setupProcessingLogic(logic);
        logic.setSpecialSlotItem(this.circuitImprint.imprint.get(1));
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return GTCEU_LOOP_ASSEMBLER;
    }

    @Override
    public ArrayList<ItemStack> getStoredInputsForColor(Optional<Byte> color) {
        if (mode == Mode.CircuitAssembler) return super.getStoredInputsForColor(color);

        ArrayList<ItemStack> rList = new ArrayList<>();
        for (MTEHatchInputBus tHatch : validMTEList(mInputBusses)) {
            tHatch.mRecipeMap = this.getRecipeMap();
            for (int i = 0; i < tHatch.getBaseMetaTileEntity()
                .getSizeInventory(); i++) {
                if (tHatch.getBaseMetaTileEntity()
                    .getStackInSlot(i) != null) {
                    rList.add(
                        tHatch.getBaseMetaTileEntity()
                            .getStackInSlot(i));
                    break;
                }
            }
        }
        return rList;
    }

    @Override
    public boolean addInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity instanceof MTEHatchInput) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((MTEHatchInput) aMetaTileEntity).mRecipeMap = this.getRecipeMap();
            return this.mInputHatches.add((MTEHatchInput) aMetaTileEntity);
        } else if (aMetaTileEntity instanceof MTEHatchInputBus) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((MTEHatchInputBus) aMetaTileEntity).mRecipeMap = this.getRecipeMap();
            return this.mInputBusses.add((MTEHatchInputBus) aMetaTileEntity);
        } else {
            return false;
        }
    }

    @Override
    public boolean addInputHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (!(aMetaTileEntity instanceof MTEHatchInput)) {
            return false;
        } else {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((MTEHatchInput) aMetaTileEntity).mRecipeMap = this.getRecipeMap();
            return this.mInputHatches.add((MTEHatchInput) aMetaTileEntity);
        }
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MTECircuitAssemblyLine(this.mName);
    }

    private String[] infoDataBuffer;

    @Override
    public String[] getInfoData() {
        if (this.infoDataBuffer != null) return this.infoDataBuffer;

        String[] oldInfo = super.getInfoData();
        this.infoDataBuffer = new String[oldInfo.length + 1];
        System.arraycopy(oldInfo, 0, this.infoDataBuffer, 0, oldInfo.length);
        this.infoDataBuffer[oldInfo.length] = StatCollector.translateToLocalFormatted("tooltip.cal.imprintedWith",EnumChatFormatting.YELLOW + this.getTypeForDisplay());
        return this.infoDataBuffer;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX) };
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.glassTier = -1;
        if (!this.checkPiece(STRUCTURE_PIECE_FIRST, 0, 0, 0)) {
            return false;
        }
        if (this.glassTier < VoltageIndex.EV) return false;
        return this.checkMachine(true) || this.checkMachine(false);
    }

    private boolean checkMachine(boolean leftToRight) {

        for (int i = 1; i < 7; ++i) {
            if (!this.checkPiece(STRUCTURE_PIECE_NEXT, leftToRight ? -i : i, 0, 0)) {
                return false;
            }
            length = i + 1;

            if (!this.mOutputBusses.isEmpty()) {
                return this.mEnergyHatches.size() == 1 && this.mMaintenanceHatches.size() == 1;
            }
        }

        return false;
    }

    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece(STRUCTURE_PIECE_FIRST, stackSize, hintsOnly, 0, 0, 0);
        int tLength = Math.min(stackSize.stackSize + 1, 7);

        for (int i = 1; i < tLength; ++i) {
            this.buildPiece(STRUCTURE_PIECE_NEXT, stackSize, hintsOnly, -i, 0, 0);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        int built;
        built = survivalBuildPiece(STRUCTURE_PIECE_FIRST, stackSize, 0, 0, 0, elementBudget, env, false, true);
        if (built >= 0) return built;
        int tLength = Math.min(stackSize.stackSize + 1, 7);

        for (int i = 1; i < tLength - 1; ++i) {
            built = survivalBuildPiece(STRUCTURE_PIECE_NEXT_HINT, stackSize, -i, 0, 0, elementBudget, env, false, true);
            if (built >= 0) return built;
        }
        return survivalBuildPiece(
            STRUCTURE_PIECE_LAST,
            stackSize,
            -(tLength - 1),
            0,
            0,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public void addAdditionalTooltipInformation(ItemStack stack, List<String> tooltip) {
        if (stack.hasTagCompound() && stack.stackTagCompound.hasKey(IMPRINT_KEY)) {
            tooltip.add(StatCollector.translateToLocalFormatted("tooltip.cal.imprintedWith",EnumChatFormatting.YELLOW + this.getTypeForDisplay()));
        }
    }

    @Override
    protected boolean useMui2() {
        return false;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        builder
            .widget(new FakeSyncWidget.StringSyncer(() -> this.circuitImprint != null ? this.circuitImprint.circuit.get(1).getDisplayName() : "", val -> {}));
        builder.widget(
            new CycleButtonWidget().setToggle(() -> mode == Mode.CircuitAssembler, this::setMode)
                .setTextureGetter(
                    state -> state == 1 ? BWUITextures.OVERLAY_BUTTON_ASSEMBLER_MODE
                        : BWUITextures.OVERLAY_BUTTON_LINE_MODE)
                .setBackground(GTUITextures.BUTTON_STANDARD)
                .setPos(80, 91)
                .setSize(16, 16)
                .dynamicTooltip(
                    () -> Collections.singletonList(StatCollector.translateToLocal("chat.cal.mode." + mode.index)))
                .setUpdateTooltipEveryTick(true)
                .setTooltipShowUpDelay(TOOLTIP_DELAY));
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    protected boolean supportsSlotAutomation(int aSlot) {
        return aSlot == getControllerSlotIndex();
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (!aPlayer.isSneaking()) {
            if (mode == Mode.CircuitAssemblyLine) return false;
            inputSeparation = !inputSeparation;
            GTUtility.sendChatTrans(
                aPlayer,
                inputSeparation ? "GT5U.machines.separatebus.true" : "GT5U.machines.separatebus.false");
        } else {
            batchMode = !batchMode;
            if (batchMode) {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOn");
            } else {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOff");
            }
        }
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        if (mode == null) return false; // required because super calls this before mode is set
        return switch (mode) {
            case CircuitAssemblyLine -> false;
            case CircuitAssembler -> true;
        };
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public boolean isInputSeparationEnabled() {
        return mode == Mode.CircuitAssembler && super.isInputSeparationEnabled();
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        NBTTagCompound tag = accessor.getNBTData();
        currenttip.add(
            StatCollector.translateToLocal("GT5U.multiblock.runningMode") + " "
                + EnumChatFormatting.WHITE
                + StatCollector.translateToLocal("chat.cal.mode." + tag.getInteger(RUNNING_MODE_KEY)));
        if (tag.hasKey("ImprintedWith") && tag.getInteger(RUNNING_MODE_KEY) == 0) currenttip.add(StatCollector.translateToLocalFormatted("tooltip.cal.imprintedWith",EnumChatFormatting.YELLOW + tag.getString("ImprintedWith")));
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        String imprintedWith = this.getTypeForDisplay();
        if (!imprintedWith.isEmpty()) tag.setString("ImprintedWith", imprintedWith);
        tag.setInteger(RUNNING_MODE_KEY, mode.index);
    }

    @Override
    protected boolean supportsCraftingMEBuffer() {
        return switch (mode) {
            case CircuitAssemblyLine -> false;
            case CircuitAssembler -> true;
        };
    }

}
