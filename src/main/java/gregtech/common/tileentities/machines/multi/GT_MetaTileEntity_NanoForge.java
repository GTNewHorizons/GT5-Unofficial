package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_Values.AuthorBlueWeabo;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.Rotation;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.GregTech_API;
import gregtech.api.enums.CheckRecipeResult;
import gregtech.api.enums.CheckRecipeResults;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings8;

public class GT_MetaTileEntity_NanoForge extends
    GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_MetaTileEntity_NanoForge> implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String STRUCTURE_PIECE_TIER2 = "tier2";
    private static final String STRUCTURE_PIECE_TIER3 = "tier3";
    private static final IStructureDefinition<GT_MetaTileEntity_NanoForge> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_NanoForge>builder()
        // spotless:off
                    .addShape(STRUCTURE_PIECE_MAIN, transpose(new String[][] {
                        {"         ","         ","    F    ","    C    ","    C    ","    C    ","    C    ","    F    ","         ","         "},
                        {"         ","         ","    F    ","    C    ","    C    ","    C    ","    C    ","    F    ","         ","         "},
                        {"         ","         ","    F    ","    C    ","    C    ","    C    ","    C    ","    F    ","         ","         "},
                        {"         ","         ","    F    ","    C    ","    C    ","    C    ","    C    ","    F    ","         ","         "},
                        {"         ","         ","    F    ","    C    ","    C    ","    C    ","    C    ","    F    ","         ","         "},
                        {"         ","         ","   FCF   ","   C C   ","   C C   ","   C C   ","   C C   ","   FCF   ","         ","         "},
                        {"         ","         ","   FCF   ","   C C   ","   C C   ","   C C   ","   C C   ","   FCF   ","         ","         "},
                        {"         ","         ","   FCF   ","   C C   ","   C C   ","   C C   ","   C C   ","   FCF   ","         ","         "},
                        {"         ","         ","   FCF   ","   C C   ","   C C   ","   C C   ","   C C   ","   FCF   ","         ","         "},
                        {"         ","         ","   FCF   ","   C C   ","   C C   ","   C C   ","   C C   ","   FCF   ","         ","         "},
                        {"         ","         ","   FCF   ","   C C   ","   C C   ","   C C   ","   C C   ","   FCF   ","         ","         "},
                        {"         ","         ","   FCF   ","   C C   ","   C C   ","   C C   ","   C C   ","   FCF   ","         ","         "},
                        {"         ","         ","   FCF   ","   C C   ","   C C   ","   C C   ","   C C   ","   FCF   ","         ","         "},
                        {"         ","         ","   FCF   ","   C C   ","   C C   ","   C C   ","   C C   ","   FCF   ","         ","         "},
                        {"         ","   FCF   ","  FC CF  ","   C C   ","   C C   ","   C C   ","   C C   ","  FC CF  ","   FCF   ","         "},
                        {"         ","   FCF   ","  FC CF  ","   C C   ","   C C   ","   C C   ","   C C   ","  FC CF  ","   FCF   ","         "},
                        {"         ","   FCF   ","  FC CF  ","  C   C  ","  C   C  ","  C   C  ","  C   C  ","  FC CF  ","   FCF   ","         "},
                        {"         ","   FCF   ","  FC CF  ","  C   C  ","  C   C  ","  C   C  ","  C   C  ","  FC CF  ","   FCF   ","         "},
                        {"    C    ","   FCF   ","  CC CC  ","  C   C  ","  C   C  ","  C   C  ","  C   C  ","  CC CC  ","   FCF   ","    C    "},
                        {"    C    ","   FCF   ","  CC CC  ","  C   C  ","  C   C  ","  C   C  ","  C   C  ","  CC CC  ","   FCF   ","    C    "},
                        {"    C    ","   FCF   ","  CC CC  ","  C   C  ","  C   C  ","  C   C  ","  C   C  ","  CC CC  ","   FCF   ","    C    "},
                        {"    C    ","   FCF   ","  CC CC  "," CC   CC "," CC   CC "," CC   CC "," CC   CC ","  CC CC  ","   FCF   ","    C    "},
                        {"    C    ","   FCF   ","  CC CC  "," CC   CC "," CC   CC "," CC   CC "," CC   CC ","  CC CC  ","   FCF   ","    C    "},
                        {"    C    ","   FCF   ","  CC CC  ","  C   C  ","  C   C  ","  C   C  ","  C   C  ","  CC CC  ","   FCF   ","    C    "},
                        {"    C    ","   FCF   ","  CC CC  ","  C   C  ","  C   C  ","  C   C  ","  C   C  ","  CC CC  ","   FCF   ","    C    "},
                        {"    C    ","   FCF   ","  CC CC  ","  C   C  ","  C   C  ","  C   C  ","  C   C  ","  CC CC  ","   FCF   ","    C    "},
                        {"         ","   FCF   ","  FC CF  ","  C   C  ","  C   C  ","  C   C  ","  C   C  ","  FC CF  ","   FCF   ","         "},
                        {"         ","   FCF   ","  FC CF  ","  C   C  ","  C   C  ","  C   C  ","  C   C  ","  FC CF  ","   FCF   ","         "},
                        {"         ","   FCF   ","  FC CF  ","   C C   ","   C C   ","   C C   ","   C C   ","  FC CF  ","   FCF   ","         "},
                        {"         ","   FCF   ","  FC CF  ","   C C   ","   C C   ","   C C   ","   C C   ","  FC CF  ","   FCF   ","         "},
                        {"         ","         ","   FCF   ","   C C   ","   C C   ","   C C   ","   C C   ","   FCF   ","         ","         "},
                        {"         ","         ","   FCF   ","   C C   ","   C C   ","   C C   ","   C C   ","   FCF   ","         ","         "},
                        {"         ","         ","   FCF   ","   C C   ","   C C   ","   C C   ","   C C   ","   FCF   ","         ","         "},
                        {"         ","         ","   FCF   ","   C C   ","   C C   ","   C C   ","   C C   ","   FCF   ","         ","         "},
                        {"         ","         ","   FCF   ","   C C   ","   C C   ","   C C   ","   C C   ","   FCF   ","         ","         "},
                        {"         ","         ","   FCF   ","   C C   ","   C C   ","   C C   ","   C C   ","   FCF   ","         ","         "},
                        {"         ","         ","   FCF   ","   C C   ","   C C   ","   C C   ","   C C   ","   FCF   ","         ","         "},
                        {"         ","  BB~BB  "," BBBBBBB ","BBBBBBBBB","BBBBBBBBB","BBBBBBBBB","BBBBBBBBB"," BBBBBBB ","  BBBBB  ","         "}
                    }))
                    .addShape(STRUCTURE_PIECE_TIER2, transpose(new String[][] {
                        {"        ", "        ", "   CC   ", "  CCCC  ", "  CCCC  ", "   CC   ", "        ", "        "},
                        {"        ", "        ", "   AA   ", "  ACCA  ", "  ACCA  ", "   AA   ", "        ", "        "},
                        {"        ", "        ", "   CC   ", "  CCCC  ", "  CCCC  ", "   CC   ", "        ", "        "},
                        {"        ", "        ", "        ", "   CC   ", "   CC   ", "        ", "        ", "        "},
                        {"        ", "        ", "        ", "   CC   ", "   CC   ", "        ", "        ", "        "},
                        {"        ", "        ", "        ", "   CC   ", "   CC   ", "        ", "        ", "        "},
                        {"        ", "        ", "        ", "   CC   ", "   CC   ", "        ", "        ", "        "},
                        {"        ", "        ", "   CC   ", "  CCCC  ", "  CCCC  ", "   CC   ", "        ", "        "},
                        {"        ", "        ", "   AA   ", "  ACCA  ", "  ACCA  ", "   AA   ", "        ", "        "},
                        {"        ", "        ", "   CC   ", "  CCCC  ", "  CCCC  ", "   CC   ", "        ", "        "},
                        {"        ", "        ", "        ", "   CC   ", "   CC   ", "        ", "        ", "        "},
                        {"        ", "        ", "        ", "   CC   ", "   CC   ", "        ", "        ", "        "},
                        {"        ", "        ", "        ", "   CC   ", "   CC   ", "        ", "        ", "        "},
                        {"        ", "        ", "        ", "   CC   ", "   CC   ", "        ", "        ", "        "},
                        {" CCCCCC ", "CCCCCCCC", "CCCCCCCC", "CCCCCCCC", "CCCCCCCC", "CCCCCCCC", "CCCCCCCC", " CCCCCC "}
                    }))
                    .addShape(STRUCTURE_PIECE_TIER3, transpose(new String[][] {
                        {"        ", "        ", "   CC   ", "  CCCC  ", "  CCCC  ", "   CC   ", "        ", "        "},
                        {"        ", "        ", " FFAA   ", "  ACCA  ", "  ACCA  ", "   AAFF ", "        ", "        "},
                        {"        ", "        ", "F  CC   ", "F CCCC  ", "  CCCC F", "   CC  F", "        ", "        "},
                        {"        ", "        ", "       F", "   CC  F", "F  CC   ", "F       ", "        ", "        "},
                        {"        ", "      F ", "        ", "   CC   ", "   CC   ", "        ", " F      ", "        "},
                        {"    FF  ", "        ", "        ", "   CC   ", "   CC   ", "        ", "        ", "  FF    "},
                        {"  FF    ", "        ", "        ", "   CC   ", "   CC   ", "        ", "        ", "    FF  "},
                        {"        ", " F      ", "        ", "   CC   ", "   CC   ", "        ", "      F ", "        "},
                        {"        ", "        ", "F       ", "F  CC   ", "   CC  F", "       F", "        ", "        "},
                        {"        ", "        ", "   CC  F", "  CCCC F", "F CCCC  ", "F  CC   ", "        ", "        "},
                        {"        ", "      F ", "   CC   ", "  CCCC  ", "  CCCC  ", "   CC   ", " F      ", "        "},
                        {"    FF  ", "        ", "   CC   ", "  CCCC  ", "  CCCC  ", "   CC   ", "        ", "  FF    "},
                        {"  FF    ", "        ", "   CC   ", "  CCCC  ", "  CCCC  ", "   CC   ", "        ", "    FF  "},
                        {"        ", " F      ", "        ", "   CC   ", "   CC   ", "        ", "      F ", "        "},
                        {"        ", "        ", "F       ", "F  CC   ", "   CC  F", "       F", "        ", "        "},
                        {"        ", "        ", "       F", "   CC  F", "F  CC   ", "F       ", "        ", "        "},
                        {"        ", "      F ", "        ", "   CC   ", "   CC   ", "        ", " F      ", "        "},
                        {"    FF  ", "        ", "        ", "   CC   ", "   CC   ", "        ", "        ", "  FF    "},
                        {"  FF    ", "        ", "        ", "   CC   ", "   CC   ", "        ", "        ", "    FF  "},
                        {"        ", " F      ", "        ", "   CC   ", "   CC   ", "        ", "      F ", "        "},
                        {"        ", "        ", "F  CC   ", "F CCCC  ", "  CCCC F", "   CC  F", "        ", "        "},
                        {"        ", "        ", "   AA  F", "  ACCA F", "F ACCA  ", "F  AA   ", "        ", "        "},
                        {"        ", "      F ", "   CC   ", "  CCCC  ", "  CCCC  ", "   CC   ", " F      ", "        "},
                        {"    FF  ", "        ", "        ", "   CC   ", "   CC   ", "        ", "        ", "  FF    "},
                        {"  FF    ", "        ", "        ", "   CC   ", "   CC   ", "        ", "        ", "    FF  "},
                        {"        ", " F      ", "        ", "   CC   ", "   CC   ", "        ", "      F ", "        "},
                        {" CCCCCC ", "CCCCCCCC", "CCCCCCCC", "CCCCCCCC", "CCCCCCCC", "CCCCCCCC", "CCCCCCCC", " CCCCCC "}
                    }))
                    //spotless:on
        .addElement('F', ofFrame(Materials.StellarAlloy))
        .addElement('C', ofBlock(GregTech_API.sBlockCasings8, 10))
        .addElement('A', ofBlock(GregTech_API.sBlockCasings2, 5))
        .addElement(
            'B',
            buildHatchAdder(GT_MetaTileEntity_NanoForge.class)
                .atLeast(InputHatch, OutputBus, InputBus, Maintenance, Energy.or(ExoticEnergy))
                .dot(1)
                .casingIndex(((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(10))
                .buildAndChain(GregTech_API.sBlockCasings8, 10))
        .build();
    private byte mSpecialTier = 0;

    public GT_MetaTileEntity_NanoForge(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_NanoForge(String aName) {
        super(aName);
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sNanoForge;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 4, 37, 1);
        if (stackSize.stackSize > 1) {
            buildPiece(STRUCTURE_PIECE_TIER2, stackSize, hintsOnly, -7, 14, 4);
        }
        if (stackSize.stackSize > 2) {
            buildPiece(STRUCTURE_PIECE_TIER3, stackSize, hintsOnly, 14, 26, 4);
        }
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_NanoForge(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection == facingDirection) {
            if (active) return new ITexture[] {
                BlockIcons.getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings8, 10)),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] {
                BlockIcons.getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings8, 10)),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] {
            BlockIcons.getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings8, 10)) };
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_NanoForge> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    protected ProcessingLogic getProcessingLogic() {
        if (super.getProcessingLogic() == null) {
            processingLogic = new ProcessingLogic() {

                @Override
                protected CheckRecipeResult checkRecipe(GT_Recipe recipe) {
                    return recipe.mSpecialValue <= mSpecialTier ? CheckRecipeResults.SUCCESSFUL
                        : CheckRecipeResults.NO_RECIPE;
                }
            };
        }
        return super.getProcessingLogic();
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mSpecialTier = 0;
        if (aStack == null) return false;
        if (aStack.isItemEqual(Materials.Carbon.getNanite(1)) && checkPiece(STRUCTURE_PIECE_MAIN, 4, 37, 1)) {
            mSpecialTier = 1;
        }

        if (aStack.isItemEqual(Materials.Neutronium.getNanite(1)) && checkPiece(STRUCTURE_PIECE_MAIN, 4, 37, 1)
            && checkPiece(STRUCTURE_PIECE_TIER2, -7, 14, 4)) {
            mSpecialTier = 2;
        }

        if (aStack.isItemEqual(MaterialsUEVplus.TranscendentMetal.getNanite(1))
            && checkPiece(STRUCTURE_PIECE_MAIN, 4, 37, 1)
            && checkPiece(STRUCTURE_PIECE_TIER2, -7, 14, 4)
            && checkPiece(STRUCTURE_PIECE_TIER3, 14, 26, 4)) {
            mSpecialTier = 3;
        }

        if (mMaintenanceHatches.size() != 1 || mInputBusses.isEmpty()
            || mOutputBusses.isEmpty()
            || mInputHatches.isEmpty()) {
            return false;
        }

        if (!checkExoticAndNormalEnergyHatches()) {
            return false;
        }

        return mSpecialTier > 0;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int built = 0;
        built += survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 4, 37, 1, elementBudget, env, false, true);
        if (stackSize.stackSize > 1) {
            built += survivialBuildPiece(STRUCTURE_PIECE_TIER2, stackSize, -7, 14, 4, elementBudget, env, false, true);
        }
        if (stackSize.stackSize > 2) {
            built += survivialBuildPiece(STRUCTURE_PIECE_TIER3, stackSize, 14, 26, 4, elementBudget, env, false, true);
        }
        return built;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("mSpecialTier", mSpecialTier);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (!aNBT.hasKey(INPUT_SEPARATION_NBT_KEY)) {
            inputSeparation = aNBT.getBoolean("mSeparate");
        }
        mSpecialTier = aNBT.getByte("mSpecialTier");
    }

    /**
     * Get possible alignments of this controller
     *
     * @return List of alignments that are possible or denied
     */
    @Override
    public IAlignmentLimits getAlignmentLimits() {
        // The nano forge should only be buildable upright
        return IAlignmentLimits.Builder.allowAll()
            .deny(ForgeDirection.DOWN)
            .deny(ForgeDirection.UP)
            .deny(Rotation.UPSIDE_DOWN)
            .deny(Rotation.CLOCKWISE)
            .deny(Rotation.COUNTER_CLOCKWISE)
            .build();
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);
        screenElements
            .widget(
                new TextWidget(StatCollector.translateToLocal("GT5U.gui.button.tier") + " " + mSpecialTier)
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getBaseMetaTileEntity().getErrorDisplayID() == 0))
            .widget(
                new FakeSyncWidget.IntegerSyncer(
                    () -> (int) mSpecialTier,
                    val -> mSpecialTier = (byte) (val % Byte.MAX_VALUE)));
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Nanite Fabricator")
            .addInfo("Controller block for the Nano Forge")
            .addInfo("Requires insane amounts of power to create nanites. Each tier")
            .addInfo("the multi gains a new building next to it. The nanite in the")
            .addInfo("controller slot controls the tier.")
            .addInfo("--------------------------------------------")
            .addInfo("Requires a Carbon Nanite to use tier " + EnumChatFormatting.DARK_PURPLE + 1)
            .addInfo("Requires a Neutronium Nanite to use tier " + EnumChatFormatting.DARK_PURPLE + 2)
            .addInfo("Requires a Transcendent Metal Nanite to use tier " + EnumChatFormatting.DARK_PURPLE + 3)
            .addInfo("--------------------------------------------")
            .addInfo("If a recipe's tier is lower than the tier of the Nano Forge")
            .addInfo("it gains " + EnumChatFormatting.RED + "perfect overclock" + EnumChatFormatting.GRAY + ".")
            .addInfo(AuthorBlueWeabo)
            .addSeparator()
            .beginStructureBlock(30, 38, 13, false)
            .addStructureInfo("Nano Forge Structure is too complex! See schematic for details.")
            .addStructureInfo("--------------------------------------------")
            .addStructureInfo("Tier " + EnumChatFormatting.DARK_PURPLE + 1 + EnumChatFormatting.GRAY)
            .addStructureInfo(
                EnumChatFormatting.GOLD + "527" + EnumChatFormatting.GRAY + " Radiant Naquadah Alloy Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "171" + EnumChatFormatting.GRAY + " Stellar Alloy Frame Box")
            .addStructureInfo("--------------------------------------------")
            .addStructureInfo("Tier " + EnumChatFormatting.DARK_PURPLE + 2 + EnumChatFormatting.GRAY)
            .addStructureInfo(
                EnumChatFormatting.GOLD + "148" + EnumChatFormatting.GRAY + " Radiant Naquadah Alloy Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "16" + EnumChatFormatting.GRAY + " Assembling Line Casing")
            .addStructureInfo("--------------------------------------------")
            .addStructureInfo("Tier " + EnumChatFormatting.DARK_PURPLE + 3 + EnumChatFormatting.GRAY)
            .addStructureInfo(
                EnumChatFormatting.GOLD + "228" + EnumChatFormatting.GRAY + " Radiant Naquadah Alloy Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "84" + EnumChatFormatting.GRAY + " Stellar Alloy Frame Box")
            .addStructureInfo(EnumChatFormatting.GOLD + "16" + EnumChatFormatting.GRAY + " Assembling Line Casing")
            .addStructureInfo("--------------------------------------------")
            .addStructureInfo(
                "Requires " + EnumChatFormatting.GOLD
                    + "1"
                    + EnumChatFormatting.GRAY
                    + "-"
                    + EnumChatFormatting.GOLD
                    + "2"
                    + EnumChatFormatting.GRAY
                    + " energy hatches or "
                    + EnumChatFormatting.GOLD
                    + "1"
                    + EnumChatFormatting.GRAY
                    + " TT energy hatch.")
            .addStructureInfo(
                "Requires " + EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + " maintenance hatch.")
            .addStructureInfo(
                "Requires " + EnumChatFormatting.GOLD
                    + 1
                    + EnumChatFormatting.GRAY
                    + "+"
                    + EnumChatFormatting.GRAY
                    + " input hatches.")
            .addStructureInfo(
                "Requires " + EnumChatFormatting.GOLD
                    + 0
                    + EnumChatFormatting.GRAY
                    + "+"
                    + EnumChatFormatting.GRAY
                    + " output hatches.")
            .addStructureInfo(
                "Requires " + EnumChatFormatting.GOLD
                    + 1
                    + EnumChatFormatting.GRAY
                    + "+"
                    + EnumChatFormatting.GRAY
                    + " input busses.")
            .addStructureInfo(
                "Requires " + EnumChatFormatting.GOLD
                    + 1
                    + EnumChatFormatting.GRAY
                    + "+"
                    + EnumChatFormatting.GRAY
                    + " output busses.")
            .addStructureInfo("--------------------------------------------")
            .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        inputSeparation = !inputSeparation;
        GT_Utility.sendChatToPlayer(
            aPlayer,
            StatCollector.translateToLocal("GT5U.machines.separatebus") + " " + inputSeparation);
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }
}
