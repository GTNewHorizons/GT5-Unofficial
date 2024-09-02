package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.AuthorBlueWeabo;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.blocks.BlockCasings8;

public class MTENanoForge extends MTEExtendedPowerMultiBlockBase<MTENanoForge> implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String STRUCTURE_PIECE_TIER2 = "tier2";
    private static final String STRUCTURE_PIECE_TIER3 = "tier3";
    private static final IStructureDefinition<MTENanoForge> STRUCTURE_DEFINITION = StructureDefinition
        .<MTENanoForge>builder()
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
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings8, 10))
        .addElement('A', ofBlock(GregTechAPI.sBlockCasings2, 5))
        .addElement(
            'B',
            buildHatchAdder(MTENanoForge.class)
                .atLeast(InputHatch, OutputBus, InputBus, Maintenance, Energy.or(ExoticEnergy))
                .dot(1)
                .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(10))
                .buildAndChain(GregTechAPI.sBlockCasings8, 10))
        .build();
    private byte mSpecialTier = 0;

    public MTENanoForge(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTENanoForge(String aName) {
        super(aName);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanoForgeRecipes;
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
        return new MTENanoForge(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection == facingDirection) {
            if (active) return new ITexture[] {
                BlockIcons.getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 10)),
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
                BlockIcons.getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 10)),
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
            BlockIcons.getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 10)) };
    }

    @Override
    public IStructureDefinition<MTENanoForge> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            protected @Nonnull CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                return recipe.mSpecialValue <= mSpecialTier ? CheckRecipeResultRegistry.SUCCESSFUL
                    : CheckRecipeResultRegistry.NO_RECIPE;
            }

            @Nonnull
            @Override
            protected OverclockCalculator createOverclockCalculator(@Nonnull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe)
                    .setDurationDecreasePerOC(mSpecialTier > recipe.mSpecialValue ? 4.0 : 2.0);
            }
        };
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(getMaxInputEu());
        logic.setAvailableAmperage(1);
        logic.setAmperageOC(false);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            // TODO: Look for proper fix
            // Updates every 10 sec
            if (mUpdate <= -150) mUpdate = 50;
        }
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mSpecialTier = 0;
        if (checkPiece(STRUCTURE_PIECE_MAIN, 4, 37, 1) && aStack != null) {
            if (aStack.isItemEqual(Materials.Carbon.getNanite(1))) {
                mSpecialTier = 1;
            }

            if (aStack.isItemEqual(Materials.Neutronium.getNanite(1)) && checkPiece(STRUCTURE_PIECE_TIER2, -7, 14, 4)) {
                mSpecialTier = 2;
            }

            if (aStack.isItemEqual(MaterialsUEVplus.TranscendentMetal.getNanite(1))
                && checkPiece(STRUCTURE_PIECE_TIER2, -7, 14, 4)
                && checkPiece(STRUCTURE_PIECE_TIER3, 14, 26, 4)) {
                mSpecialTier = 3;
            }
        }

        if (mMaintenanceHatches.size() != 1) {
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
        int built = survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 4, 37, 1, elementBudget, env, false, true);
        if (built >= 0) return built;
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
        if (aNBT.hasKey("mSeparate")) {
            // backward compatibility
            inputSeparation = aNBT.getBoolean("mSeparate");
        }
        mSpecialTier = aNBT.getByte("mSpecialTier");
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
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
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
        GTUtility.sendChatToPlayer(
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

    @Override
    public boolean supportsBatchMode() {
        return true;
    }
}
