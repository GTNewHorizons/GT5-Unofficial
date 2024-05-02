package gregtech.common.tileentities.machines.multi.purification;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.GT_Values.AuthorNotAPenguin;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_StructureUtility;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_PurificationUnitSifter extends
    GT_MetaTileEntity_PurificationUnitBase<GT_MetaTileEntity_PurificationUnitSifter> implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String STRUCTURE_PIECE_MAIN_SURVIVAL = "main_survival";

    private static final int STRUCTURE_X_OFFSET = 5;
    private static final int STRUCTURE_Y_OFFSET = 2;
    private static final int STRUCTURE_Z_OFFSET = 1;

    // Chance that the filter is damaged every cycle.
    private static final float FILTER_DAMAGE_RATE = 20.0f;

    private static final String[][] structure =
        // spotless:off
        new String[][] {
            { "           ", "           ", "           ", "           " },
            { "           ", "   AAAAA   ", "   AH~HA   ", "   AAAAA   " },
            { "           ", "  A     A  ", "  AWWWWWA  ", "  AAAAAAA  " },
            { "           ", " A       A ", " AWWWWWWWA ", " AAAAAAAAA " },
            { "           ", "A         A", "AWWWCCCWWWA", "AAAAFFFAAAA" },
            { "    DDD    ", "A         A", "HWWCWWWCWWH", "AAAFFFFFAAA" },
            { "DDDDDBD    ", "A    B    A", "AWWCWBWCWWA", "AAAFFFFFAAA" },
            { "    DDD    ", "A         A", "HWWCWWWCWWH", "AAAFFFFFAAA" },
            { "           ", "A         A", "AWWWCCCWWWA", "AAAAFFFAAAA" },
            { "           ", " A       A ", " AWWWWWWWA ", " AAAAAAAAA " },
            { "           ", "  A     A  ", "  AWWWWWA  ", "  AAAAAAA  " },
            { "           ", "   AAAAA   ", "   AHAHA   ", "   AAAAA   " } };
    // spotless:on

    private static final IStructureDefinition<GT_MetaTileEntity_PurificationUnitSifter> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_PurificationUnitSifter>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        .addShape(
            STRUCTURE_PIECE_MAIN_SURVIVAL,
            Arrays.stream(structure)
                .map(
                    sa -> Arrays.stream(sa)
                        .map(s -> s.replaceAll("W", " "))
                        .toArray(String[]::new))
                .toArray(String[][]::new))
        // Hatches
        .addElement(
            'H',
            ofChain(
                lazy(
                    t -> GT_StructureUtility.<GT_MetaTileEntity_PurificationUnitSifter>buildHatchAdder()
                        .atLeastList(t.getAllowedHatches())
                        .casingIndex(49)
                        .dot(1)
                        .build()),
                // Clean stainless steel machine casing
                ofBlock(GregTech_API.sBlockCasings4, 1)))
        // Clean stainless steel machine casing
        .addElement('A', ofBlock(GregTech_API.sBlockCasings4, 1))
        // PTFE pipe casing
        .addElement('B', ofBlock(GregTech_API.sBlockCasings8, 1))
        .addElement('C', ofFrame(Materials.Iridium))
        .addElement('D', ofFrame(Materials.DamascusSteel))
        .addElement('W', ofChain(ofBlock(Blocks.water, 0)))
        // Filter machine casing
        .addElement('F', ofBlock(GregTech_API.sBlockCasings3, 11))
        .build();

    public GT_MetaTileEntity_PurificationUnitSifter(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_PurificationUnitSifter(String aName) {
        super(aName);
    }

    @Override
    public int getWaterTier() {
        return 1;
    }

    @Override
    public long getActivePowerUsage() {
        return TierEU.RECIPE_LuV;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.purificationPlantGrade1Recipes;
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        this.startRecipeProcessing();
        RecipeMap<?> recipeMap = this.getRecipeMap();

        GT_Recipe recipe = recipeMap.findRecipeQuery()
            .fluids(
                this.getStoredFluids()
                    .toArray(new FluidStack[] {}))
            .items(
                this.getStoredInputs()
                    .toArray(new ItemStack[] {}))
            .find();

        this.endRecipeProcessing();
        if (recipe == null) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        if (this.protectsExcessFluid() && !this.canOutputAll(recipe.mFluidOutputs)) {
            return CheckRecipeResultRegistry.FLUID_OUTPUT_FULL;
        }

        if (this.protectsExcessItem() && !this.canOutputAll(recipe.mOutputs)) {
            return CheckRecipeResultRegistry.ITEM_OUTPUT_FULL;
        }

        this.currentRecipe = recipe;
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public void depleteRecipeInputs() {
        super.depleteRecipeInputs();

        // Now do random roll to determine if the filter should be destroyed
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int roll = random.nextInt(1, 101);
        if (roll < FILTER_DAMAGE_RATE) {
            this.depleteInput(this.currentRecipe.mInputs[0]);
        }
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_PurificationUnitSifter> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        // Rotated sifter not allowed, water will flow out.
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Purification Unit")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.BOLD
                    + "Water Tier: "
                    + EnumChatFormatting.WHITE
                    + GT_Utility.formatNumbers(getWaterTier())
                    + EnumChatFormatting.RESET)
            .addInfo("Controller block for the Sifter Purification Unit.")
            .addInfo("Must be linked to a Purification Plant to work.")
            .addSeparator()
            .addInfo("Filters out large particles in the water such as")
            .addInfo("dirt, sticks, and perhaps even some treasure!")
            .addInfo("Requires a filter in the input bus to work.")
            .addInfo(
                "Every cycle, has a " + EnumChatFormatting.RED
                    + GT_Utility.formatNumbers(FILTER_DAMAGE_RATE)
                    + "%"
                    + EnumChatFormatting.GRAY
                    + " chance to destroy the filter.")
            .addInfo(AuthorNotAPenguin)
            .beginStructureBlock(11, 4, 11, false)
            .addSeparator()
            .addCasingInfoRangeColored(
                "Clean Stainless Steel Machine Casing",
                EnumChatFormatting.GRAY,
                123,
                131,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Filter Machine Casing",
                EnumChatFormatting.GRAY,
                21,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Iridium Frame Box",
                EnumChatFormatting.GRAY,
                21,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Damascus Steel Frame Box",
                EnumChatFormatting.GRAY,
                12,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored("PTFE Pipe Casing", EnumChatFormatting.GRAY, 3, EnumChatFormatting.GOLD, false)
            .addController("Front center")
            .addInputBus(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + "+", 1)
            .addOutputBus(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + "+", 1)
            .addInputHatch(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + "+", 1)
            .addOutputHatch(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + "+", 1)
            .addStructureInfo("Use the StructureLib Hologram Projector to build the structure.")
            .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            hintsOnly,
            STRUCTURE_X_OFFSET,
            STRUCTURE_Y_OFFSET,
            STRUCTURE_Z_OFFSET);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        int built = survivialBuildPiece(
            STRUCTURE_PIECE_MAIN_SURVIVAL,
            stackSize,
            STRUCTURE_X_OFFSET,
            STRUCTURE_Y_OFFSET,
            STRUCTURE_Z_OFFSET,
            elementBudget,
            env,
            true);
        if (built == -1) {
            GT_Utility.sendChatToPlayer(
                env.getActor(),
                EnumChatFormatting.GREEN + "Auto placing done ! Now go place the water yourself !");
            return 0;
        }
        return built;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_PurificationUnitSifter(this.mName);
    }

    private List<IHatchElement<? super GT_MetaTileEntity_PurificationUnitSifter>> getAllowedHatches() {
        return ImmutableList.of(InputBus, InputHatch, OutputBus, OutputHatch);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection == facingDirection) {
            if (active) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(49), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(49), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(49) };
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (!checkPiece(STRUCTURE_PIECE_MAIN, STRUCTURE_X_OFFSET, STRUCTURE_Y_OFFSET, STRUCTURE_Z_OFFSET)) return false;
        return super.checkMachine(aBaseMetaTileEntity, aStack);
    }
}
