package kubatech.tileentity.gregtech.multiblock;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASING_MAGIC;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASING_MAGIC_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASING_MAGIC_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASING_MAGIC_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_GLOW;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static kubatech.api.Variables.StructureHologram;
import static kubatech.api.Variables.buildAuthorList;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import kubatech.Tags;
import kubatech.api.implementations.KubaTechGTMultiBlockBase;
import kubatech.loaders.BlockLoader;
import kubatech.loaders.DEFCRecipes;

public class GT_MetaTileEntity_DEFusionCrafter extends KubaTechGTMultiBlockBase<GT_MetaTileEntity_DEFusionCrafter>
    implements ISurvivalConstructable {

    private static final int CASING_INDEX = (1 << 7) + (15 + 48);
    private int mTierCasing = 0;
    private int mFusionTierCasing = 0;
    private int mCasing = 0;

    @SuppressWarnings("unused")
    public GT_MetaTileEntity_DEFusionCrafter(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_DEFusionCrafter(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_DEFusionCrafter(mName);
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final List<Pair<Block, Integer>> fusionCasingTiers = Arrays
        .asList(Pair.of(GregTech_API.sBlockCasings4, 6), Pair.of(GregTech_API.sBlockCasings4, 8));
    private static final List<Pair<Block, Integer>> coreTiers = Arrays.asList(
        Pair.of(BlockLoader.defcCasingBlock, 8),
        Pair.of(BlockLoader.defcCasingBlock, 9),
        Pair.of(BlockLoader.defcCasingBlock, 10),
        Pair.of(BlockLoader.defcCasingBlock, 11),
        Pair.of(BlockLoader.defcCasingBlock, 12));
    private static final IStructureDefinition<GT_MetaTileEntity_DEFusionCrafter> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_DEFusionCrafter>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] { // spotless:off
                    { "nnnnn", "nnnnn", "nnnnn", "nnnnn", "nnnnn" },
                    { "     ", "  F  ", " FfF ", "  F  ", "     " },
                    { "     ", "  F  ", " FfF ", "  F  ", "     " },
                    { "RRRRR", "R F R", "RFfFR", "R F R", "RRRRR" },
                    { "     ", "  F  ", " FfF ", "  F  ", "     " },
                    { "     ", "  F  ", " FfF ", "  F  ", "     " },
                    { "RRRRR", "R F R", "RFfFR", "R F R", "RRRRR" },
                    { "     ", "  F  ", " FfF ", "  F  ", "     " },
                    { "     ", "  F  ", " FfF ", "  F  ", "     " },
                    { "NN~NN", "NNNNN", "NNNNN", "NNNNN", "NNNNN" }
                })) // spotless:on
        .addElement(
            'N',
            buildHatchAdder(GT_MetaTileEntity_DEFusionCrafter.class)
                .atLeast(InputBus, InputHatch, OutputBus, OutputHatch, Energy, Maintenance)
                .casingIndex(CASING_INDEX)
                .dot(1)
                .buildAndChain(onElementPass(e -> e.mCasing++, ofBlock(BlockLoader.defcCasingBlock, 7))))
        .addElement('n', onElementPass(e -> e.mCasing++, ofBlock(BlockLoader.defcCasingBlock, 7)))
        .addElement('f', ofBlock(GregTech_API.sBlockCasings4, 7))
        .addElement('F', ofBlocksTiered((Block b, int m) -> {
            if (b != GregTech_API.sBlockCasings4 || (m != 6 && m != 8)) return -2;
            return m == 6 ? 1 : 2;
        }, fusionCasingTiers, -1, (e, i) -> e.mFusionTierCasing = i, e -> e.mFusionTierCasing))
        .addElement('R', ofBlocksTiered((Block b, int m) -> {
            if (b != BlockLoader.defcCasingBlock || m < 8 || m > 12) return -2;
            return m - 7;
        }, coreTiers, -1, (e, i) -> e.mTierCasing = i, e -> e.mTierCasing))
        .build();

    @Override
    public IStructureDefinition<GT_MetaTileEntity_DEFusionCrafter> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        mTierCasing = -1;
        mFusionTierCasing = -1;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 2, 9, 0)) return false;
        if (mCasing < 19) return false;
        if (mTierCasing == -2 || mFusionTierCasing == -2) return false;
        if (mTierCasing > 3 && mFusionTierCasing < 2) return false;
        return mMaintenanceHatches.size() == 1;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Fusion Crafter")
            .addInfo("Controller Block for the Draconic Evolution Fusion Crafter")
            .addInfo(buildAuthorList("kuba6000", "Prometheus0000"))
            .addInfo("Machine can be overclocked by using casings above the recipe tier:")
            .addInfo("Recipe time is divided by number of tiers above the recipe")
            .addInfo("Normal EU OC still applies !")
            .addInfo(StructureHologram)
            .addSeparator()
            .beginStructureBlock(5, 10, 5, false)
            .addController("Front bottom center")
            .addCasingInfoMin("Naquadah Alloy Fusion Casing", 19, false)
            .addOtherStructurePart("Fusion Coil Block", "Center pillar")
            .addOtherStructurePart("Fusion Machine Casing", "Touching Fusion Coil Block at every side")
            .addOtherStructurePart("Tiered Fusion Casing", "Rings (5x5 hollow) at layer 4 and 7")
            .addStructureInfo("Bloody Ichorium for tier 1, Draconium for tier 2, etc")
            .addStructureInfo("To use tier 3 + you have to use fusion casing MK II")
            .addInputBus("Any bottom casing", 1)
            .addInputHatch("Any bottom casing", 1)
            .addOutputBus("Any bottom casing", 1)
            .addOutputHatch("Any bottom casing", 1)
            .addEnergyHatch("Any bottom casing", 1)
            .addMaintenanceHatch("Any bottom casing", 1)
            .toolTipFinisher(Tags.MODNAME);
        return tt;
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { TextureFactory.of(MACHINE_CASING_MAGIC), TextureFactory.builder()
                .addIcon(OVERLAY_TELEPORTER_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_TELEPORTER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { TextureFactory.of(MACHINE_CASING_MAGIC), TextureFactory.builder()
                .addIcon(OVERLAY_TELEPORTER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_TELEPORTER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        if (aActive) return new ITexture[] { TextureFactory.of(MACHINE_CASING_MAGIC), TextureFactory.builder()
            .addIcon(MACHINE_CASING_MAGIC_ACTIVE)
            .extFacing()
            .build(),
            TextureFactory.builder()
                .addIcon(MACHINE_CASING_MAGIC_ACTIVE_GLOW)
                .extFacing()
                .glow()
                .build() };
        return new ITexture[] { TextureFactory.of(MACHINE_CASING_MAGIC), TextureFactory.builder()
            .addIcon(MACHINE_CASING_MAGIC)
            .extFacing()
            .build(),
            TextureFactory.builder()
                .addIcon(MACHINE_CASING_MAGIC_GLOW)
                .extFacing()
                .glow()
                .build() };
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return DEFCRecipes.fusionCraftingRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GT_Recipe recipe) {
                return recipe.mSpecialValue <= mTierCasing ? CheckRecipeResultRegistry.SUCCESSFUL
                    : CheckRecipeResultRegistry.NO_RECIPE;
            }

            @Override
            protected double calculateDuration(@NotNull GT_Recipe recipe, @NotNull GT_ParallelHelper helper,
                @NotNull GT_OverclockCalculator calculator) {
                return Math.max(
                    1d,
                    super.calculateDuration(recipe, helper, calculator) / ((mTierCasing - recipe.mSpecialValue) + 1));
            }
        };
    }

    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    public int getPollutionPerTick(ItemStack aStack) {
        return 0;
    }

    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, 2, 9, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 2, 9, 0, elementBudget, env, true, true);
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
