package gregtech.common.tileentities.machines.multi.purification;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import java.util.Arrays;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
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

public class GT_MetaTileEntity_PurificationUnitSifter
    extends GT_MetaTileEntity_PurificationUnitBase<GT_MetaTileEntity_PurificationUnitSifter> {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String STRUCTURE_PIECE_MAIN_SURVIVAL = "main_survival";

    private static final int STRUCTURE_X_OFFSET = 5;
    private static final int STRUCTURE_Y_OFFSET = 2;
    private static final int STRUCTURE_Z_OFFSET = 1;

    private int mCasingAmount;

    private static final String[][] structure =
        // spotless:off
        new String[][] {
            { "           ", "           ", "           ", "           " },
            { "           ", "   AAAAA   ", "   AA~AA   ", "   AAAAA   " },
            { "           ", "  A     A  ", "  AWWWWWA  ", "  AAAAAAA  " },
            { "           ", " A       A ", " AWWWWWWWA ", " AAAAAAAAA " },
            { "           ", "A         A", "AWWWCCCWWWA", "AAAAAAAAAAA" },
            { "    DDD    ", "A         A", "AWWCWWWCWWA", "AAAAAAAAAAA" },
            { "DDDDDBD    ", "A    B    A", "AWWCWBWCWWA", "AAAAAAAAAAA" },
            { "    DDD    ", "A         A", "AWWCWWWCWWA", "AAAAAAAAAAA" },
            { "           ", "A         A", "AWWWCCCWWWA", "AAAAAAAAAAA" },
            { "           ", " A       A ", " AWWWWWWWA ", " AAAAAAAAA " },
            { "           ", "  A     A  ", "  AWWWWWA  ", "  AAAAAAA  " },
            { "           ", "   AAAAA   ", "   AAAAA   ", "   AAAAA   " } };
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
        .addElement(
            'A',
            ofChain(
                lazy(
                    t -> GT_StructureUtility.<GT_MetaTileEntity_PurificationUnitSifter>buildHatchAdder()
                        .atLeastList(t.getAllowedHatches())
                        .casingIndex(49)
                        .dot(1)
                        .build()),
                // Currently clean stainless steel casing
                onElementPass(t -> t.mCasingAmount++, ofBlock(GregTech_API.sBlockCasings4, 1))))
        // currently ptfe pipe casing
        .addElement('B', ofBlock(GregTech_API.sBlockCasings8, 1))
        .addElement('C', ofFrame(Materials.Iridium))
        .addElement('D', ofFrame(Materials.DamascusSteel))
        .addElement('W', ofChain(ofBlock(Blocks.water, 0)))
        .build();

    public GT_MetaTileEntity_PurificationUnitSifter(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_PurificationUnitSifter(String aName) {
        super(aName);
    }

    @Override
    public long getActivePowerUsage() {
        // TODO: Balancing, etc.
        return 32720;
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
                    + "Water tier: "
                    + EnumChatFormatting.WHITE
                    + "1"
                    + EnumChatFormatting.RESET)
            .addSeparator()
            .beginStructureBlock(11, 4, 11, true)
            .addController("Front center")
            .toolTipFinisher("Gregtech");
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
