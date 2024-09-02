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

import static bartworks.util.BWTooltipReference.MULTIBLOCK_ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS;
import static bartworks.util.BWUtil.ofGlassTieredMixed;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.SoundResource.IC2_MACHINES_MAGNETIZER_LOOP;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTUtility.filterValidMTEs;
import static gregtech.api.util.GTUtility.getColoredTierNameFromTier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

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
import bartworks.system.material.CircuitGeneration.BWMetaItems;
import bartworks.system.material.CircuitGeneration.CircuitImprintLoader;
import bartworks.util.BWTooltipReference;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
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
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTECircuitAssemblyLine extends MTEEnhancedMultiBlockBase<MTECircuitAssemblyLine>
    implements ISurvivalConstructable {

    private static final int CASING_INDEX = 16;

    private static final String STRUCTURE_PIECE_FIRST = "first";
    private static final String STRUCTURE_PIECE_NEXT = "next";
    private static final String STRUCTURE_PIECE_NEXT_HINT = "next_hint";
    private static final String STRUCTURE_PIECE_LAST = "last";

    private static final int MINIMUM_CIRCUIT_ASSEMBLER_LENGTH = 5;
    protected static final String IMPRINT_KEY = "Type";
    protected static final String LENGTH_KEY = "Length";
    protected static final String RUNNING_MODE_KEY = "RunningMode";

    private int length;
    private int mode;
    private String imprintedItemName;
    private ItemStack imprintedStack;

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
                .dot(1)
                .buildAndChain(GregTechAPI.sBlockCasings3, 10))
        .addElement('g', ofGlassTieredMixed((byte) 4, (byte) 127, 5))
        .addElement('l', ofBlock(GregTechAPI.sBlockCasings2, 5)) // assembling line casings
        .addElement(
            'b',
            buildHatchAdder(MTECircuitAssemblyLine.class).atLeast(InputHatch, Maintenance)
                .casingIndex(CASING_INDEX)
                .dot(2)
                .disallowOnly(ForgeDirection.EAST, ForgeDirection.WEST)
                .buildAndChain(GregTechAPI.sBlockCasings2, 0))
        .addElement('i', InputBus.newAny(CASING_INDEX, 3, ForgeDirection.DOWN))
        .addElement(
            'I',
            buildHatchAdder(MTECircuitAssemblyLine.class).atLeast(InputHatch, InputBus, OutputBus)
                .casingIndex(CASING_INDEX)
                .dot(2)
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
        tt.addMachineType("Circuit Assembler/Circuit Assembly Line")
            .addInfo("Controller block for the Circuit Assembly Line")
            .addInfo("Change Mode with Screwdriver")
            .addInfo("Does not lose efficiency when overclocked")
            .addInfo(
                "--------- " + EnumChatFormatting.GOLD
                    + StatCollector.translateToLocal("chat.cal.mode.0")
                    + EnumChatFormatting.GRAY
                    + " --------")
            .addInfo("Imprint this machine with a Circuit Imprint,")
            .addInfo("by putting the imprint in the controller")
            .addInfo("Every Circuit Assembly Line can only be imprinted ONCE")
            .addInfo(
                "--------- " + EnumChatFormatting.GOLD
                    + StatCollector.translateToLocal("chat.cal.mode.1")
                    + EnumChatFormatting.GRAY
                    + " --------")
            .addInfo(
                "Does Circuit Assembler recipes, Minimum Length: " + EnumChatFormatting.RED
                    + MINIMUM_CIRCUIT_ASSEMBLER_LENGTH
                    + EnumChatFormatting.GRAY)
            .addInfo("Recipe tier in Circuit Assembler mode is at most Energy Hatch tier - 1.")
            .addInfo("This mode supports Crafting Input Buffer/Bus and allows bus separation")
            .addInfo("")
            .addSeparator()
            .addInfo(BWTooltipReference.TT_BLUEPRINT)
            .beginVariableStructureBlock(2, 7, 3, 3, 3, 3, false)
            .addStructureInfo("From Bottom to Top, Left to Right")
            .addStructureInfo(
                "Layer 1 - Solid Steel Machine Casing, Input bus (Last Output bus), Solid Steel Machine Casing")
            .addStructureInfo(
                "Layer 2 - " + getColoredTierNameFromTier((byte) 4)
                    + "+ Tier Glass, Assembling Line Casing, "
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
            .addOtherStructurePart(getColoredTierNameFromTier((byte) 4) + "+ Tier Glass", "As specified on layer 2", 5)
            .addMaintenanceHatch("Any layer 1 casing", 2)
            .toolTipFinisher(MULTIBLOCK_ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS);
        return tt;
    }

    public String getTypeForDisplay() {

        if (!isImprinted()) return "";
        return GTLanguageManager.getTranslation(
            GTLanguageManager.getTranslateableItemStackName(CircuitImprintLoader.getStackFromTag(this.type)));
    }

    private NBTTagCompound type = new NBTTagCompound();

    public MTECircuitAssemblyLine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    private MTECircuitAssemblyLine(String aName) {
        super(aName);
    }

    public boolean isImprinted() {
        return !this.type.hasNoTags();
    }

    private boolean imprintMachine(ItemStack itemStack) {
        if (isImprinted()) return true;
        if (!GTUtility.isStackValid(itemStack)) return false;
        if (itemStack.getItem() instanceof BWMetaItems.BW_GT_MetaGenCircuits && itemStack.getItemDamage() == 0
            && itemStack.getTagCompound() != null) {
            this.type = itemStack.getTagCompound();
            itemStack.stackSize -= 1;
            if (itemStack == getControllerSlot() && itemStack.stackSize <= 0) {
                mInventory[getControllerSlotIndex()] = null;
            }
            this.getBaseMetaTileEntity()
                .issueBlockUpdate();
            return true;
        }
        return false;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack itemStack) {
        return true;
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        if (aIndex == 20) {
            GTUtility.doSoundAtClient(IC2_MACHINES_MAGNETIZER_LOOP, 10, 1.0F, aX, aY, aZ);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.type = aNBT.getCompoundTag(IMPRINT_KEY);
        this.imprintedItemName = this.type == null ? ""
            : GTLanguageManager.getTranslateableItemStackName(ItemStack.loadItemStackFromNBT(this.type));
        mode = aNBT.getInteger(RUNNING_MODE_KEY);
        if (aNBT.hasKey(LENGTH_KEY)) {
            length = aNBT.getInteger(LENGTH_KEY);
        }
        super.loadNBTData(aNBT);
    }

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        if (isImprinted()) aNBT.setTag(IMPRINT_KEY, this.type);
        aNBT.setInteger(RUNNING_MODE_KEY, mode);
        super.saveNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        if (isImprinted()) aNBT.setTag(IMPRINT_KEY, this.type);
        aNBT.setInteger(RUNNING_MODE_KEY, mode);
        aNBT.setInteger(LENGTH_KEY, length);
        super.saveNBTData(aNBT);
    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (mode == 0 && !isImprinted() && getBaseMetaTileEntity().isServerSide()) {
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
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isServerSide()) {
            this.mode = (this.mode + 1) % 2;
            GTUtility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("chat.cal.mode." + this.mode));
        }
        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        if (this.mode == 0) return BartWorksRecipeMaps.circuitAssemblyLineRecipes;
        return RecipeMaps.circuitAssemblerRecipes;
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
                if (MTECircuitAssemblyLine.this.mode == 1
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
        if (mode == 0) {
            if (!isImprinted() && !this.imprintMachine(this.getControllerSlot()))
                return SimpleCheckRecipeResult.ofFailure("no_imprint");
            if (this.imprintedItemName == null || this.imprintedStack == null) {
                this.imprintedStack = new ItemStack(BWMetaItems.getCircuitParts(), 1, 0);
                this.imprintedStack.setTagCompound(this.type);
                this.imprintedItemName = GTLanguageManager.getTranslateableItemStackName(this.imprintedStack);
            }
        } else if (length < MINIMUM_CIRCUIT_ASSEMBLER_LENGTH) {
            return SimpleCheckRecipeResult.ofFailure("not_enough_length");
        }

        return super.checkProcessing();
    }

    @Override
    protected void setupProcessingLogic(ProcessingLogic logic) {
        super.setupProcessingLogic(logic);
        logic.setSpecialSlotItem(this.imprintedStack);
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.IC2_MACHINES_MAGNETIZER_LOOP;
    }

    @Override
    public ArrayList<ItemStack> getStoredInputs() {
        if (mode == 0) {
            ArrayList<ItemStack> rList = new ArrayList<>();
            for (MTEHatchInputBus tHatch : filterValidMTEs(mInputBusses)) {
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

        return super.getStoredInputs();
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
        if (aMetaTileEntity == null || !(aMetaTileEntity instanceof MTEHatchInput)) {
            return false;
        } else {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((MTEHatchInput) aMetaTileEntity).mRecipeMap = this.getRecipeMap();
            return this.mInputHatches.add((MTEHatchInput) aMetaTileEntity);
        }
    }

    @Override
    public int getMaxEfficiency(ItemStack itemStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerTick(ItemStack itemStack) {
        return 0;
    }

    @Override
    public int getDamageToComponent(ItemStack itemStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack itemStack) {
        return false;
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
        this.infoDataBuffer[oldInfo.length] = StatCollector.translateToLocal("tooltip.cal.imprintedWith") + " "
            + EnumChatFormatting.YELLOW
            + this.getTypeForDisplay();
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
        if (!this.checkPiece(STRUCTURE_PIECE_FIRST, 0, 0, 0)) {
            return false;
        }
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
        built = survivialBuildPiece(STRUCTURE_PIECE_FIRST, stackSize, 0, 0, 0, elementBudget, env, false, true);
        if (built >= 0) return built;
        int tLength = Math.min(stackSize.stackSize + 1, 7);

        for (int i = 1; i < tLength - 1; ++i) {
            built = survivialBuildPiece(
                STRUCTURE_PIECE_NEXT_HINT,
                stackSize,
                -i,
                0,
                0,
                elementBudget,
                env,
                false,
                true);
            if (built >= 0) return built;
        }
        return survivialBuildPiece(
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
            tooltip.add(
                StatCollector.translateToLocal("tooltip.cal.imprintedWith") + " "
                    + EnumChatFormatting.YELLOW
                    + StatCollector.translateToLocal(
                        GTLanguageManager.getTranslateableItemStackName(
                            ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("Type")))));
        }
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        builder
            .widget(new FakeSyncWidget.StringSyncer(() -> this.imprintedItemName, val -> this.imprintedItemName = val));
        builder.widget(
            new CycleButtonWidget().setToggle(() -> mode == 1, val -> mode = val ? 1 : 0)
                .setTextureGetter(
                    state -> state == 1 ? BWUITextures.OVERLAY_BUTTON_ASSEMBLER_MODE
                        : BWUITextures.OVERLAY_BUTTON_LINE_MODE)
                .setBackground(GTUITextures.BUTTON_STANDARD)
                .setPos(80, 91)
                .setSize(16, 16)
                .dynamicTooltip(
                    () -> Collections.singletonList(StatCollector.translateToLocal("chat.cal.mode." + mode)))
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
        float aX, float aY, float aZ) {
        if (!aPlayer.isSneaking()) {
            if (mode == 0) return false;
            inputSeparation = !inputSeparation;
            GTUtility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("GT5U.machines.separatebus") + " " + inputSeparation);
            return true;
        } else {
            batchMode = !batchMode;
            if (batchMode) {
                GTUtility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOn"));
            } else {
                GTUtility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOff"));
            }
            return true;
        }
    }

    @Override
    public boolean supportsInputSeparation() {
        return mode != 0;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public boolean isInputSeparationEnabled() {
        return mode == 1 && super.isInputSeparationEnabled();
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        NBTTagCompound tag = accessor.getNBTData();
        currenttip.add(
            StatCollector.translateToLocal("GT5U.machines.oreprocessor1") + " "
                + EnumChatFormatting.WHITE
                + StatCollector.translateToLocal("chat.cal.mode." + tag.getInteger(RUNNING_MODE_KEY)));
        if (tag.hasKey("ImprintedWith") && tag.getInteger(RUNNING_MODE_KEY) == 0) currenttip.add(
            StatCollector.translateToLocal("tooltip.cal.imprintedWith") + " "
                + EnumChatFormatting.YELLOW
                + tag.getString("ImprintedWith"));
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        String imprintedWith = this.getTypeForDisplay();
        if (!imprintedWith.isEmpty()) tag.setString("ImprintedWith", imprintedWith);
        tag.setInteger(RUNNING_MODE_KEY, mode);
    }

    @Override
    protected boolean supportsCraftingMEBuffer() {
        return mode != 0;
    }

}
