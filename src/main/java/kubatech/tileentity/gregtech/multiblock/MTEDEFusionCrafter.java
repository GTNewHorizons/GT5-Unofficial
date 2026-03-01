package kubatech.tileentity.gregtech.multiblock;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASING_MAGIC;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASING_MAGIC_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASING_MAGIC_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASING_MAGIC_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_GLOW;
import static gregtech.api.util.GTRecipeConstants.DEFC_CASING_TIER;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

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

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import kubatech.api.implementations.KubaTechGTMultiBlockBase;
import kubatech.loaders.BlockLoader;
import kubatech.loaders.DEFCRecipes;

public class MTEDEFusionCrafter extends KubaTechGTMultiBlockBase<MTEDEFusionCrafter> implements ISurvivalConstructable {

    private static final int CASING_INDEX = (1 << 7) + (15 + 48);
    private int mTierCasing = 0;
    private int mFusionTierCasing = 0;
    private int mCasing = 0;

    @SuppressWarnings("unused")
    public MTEDEFusionCrafter(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEDEFusionCrafter(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEDEFusionCrafter(mName);
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final List<Pair<Block, Integer>> fusionCasingTiers = Arrays
        .asList(Pair.of(GregTechAPI.sBlockCasings4, 6), Pair.of(GregTechAPI.sBlockCasings4, 8));
    private static final List<Pair<Block, Integer>> coreTiers = Arrays.asList(
        Pair.of(BlockLoader.defcCasingBlock, 8),
        Pair.of(BlockLoader.defcCasingBlock, 9),
        Pair.of(BlockLoader.defcCasingBlock, 10),
        Pair.of(BlockLoader.defcCasingBlock, 11),
        Pair.of(BlockLoader.defcCasingBlock, 12));
    private static final IStructureDefinition<MTEDEFusionCrafter> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEDEFusionCrafter>builder()
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
            buildHatchAdder(MTEDEFusionCrafter.class)
                .atLeast(InputBus, InputHatch, OutputBus, OutputHatch, Energy, Maintenance)
                .casingIndex(CASING_INDEX)
                .hint(1)
                .buildAndChain(onElementPass(e -> e.mCasing++, ofBlock(BlockLoader.defcCasingBlock, 7))))
        .addElement('n', onElementPass(e -> e.mCasing++, ofBlock(BlockLoader.defcCasingBlock, 7)))
        .addElement('f', ofBlock(GregTechAPI.sBlockCasings4, 7))
        .addElement('F', ofBlocksTiered((Block b, int m) -> {
            if (b != GregTechAPI.sBlockCasings4 || (m != 6 && m != 8)) return null;
            return m == 6 ? 1 : 2;
        }, fusionCasingTiers, -1, (e, i) -> e.mFusionTierCasing = i, e -> e.mFusionTierCasing))
        .addElement('R', ofBlocksTiered((Block b, int m) -> {
            if (b != BlockLoader.defcCasingBlock || m < 8 || m > 12) return null;
            return m - 7;
        }, coreTiers, -1, (e, i) -> e.mTierCasing = i, e -> e.mTierCasing))
        .build();

    @Override
    public IStructureDefinition<MTEDEFusionCrafter> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        mTierCasing = -1;
        mFusionTierCasing = -1;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 2, 9, 0)) return false;
        if (mCasing < 19) return false;
        if (mTierCasing > 3 && mFusionTierCasing < 2) return false;
        return mMaintenanceHatches.size() == 1;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("machtype.defc")
            .addInfo("gt.defc.tips")
            .beginStructureBlock(5, 10, 5, false)
            .addController("front_bottom_middle")
            .addCasingInfoMin("defc.casing.7.name", 19)
            .addStructurePart("gt.blockcasings4.7.name", "gt.defc.info.coil")
            .addStructurePart("gt.blockcasings4.6.name", "gt.defc.info.casing")
            .addStructurePart("GT5U.tooltip.structure.tiered_fusion_casing", "gt.defc.info.tiered_casing")
            .addStructureInfo("gt.defc.info.tiers")
            .addInputBus("<bottom casing>", 1)
            .addInputHatch("<bottom casing>", 1)
            .addOutputBus("<bottom casing>", 1)
            .addOutputHatch("<bottom casing>", 1)
            .addEnergyHatch("<bottom casing>", 1)
            .addMaintenanceHatch("<bottom casing>", 1)
            .toolTipFinisher(GTValues.AuthorKuba, "Prometheus0000");
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
    public RecipeMap<?> getRecipeMap() {
        return DEFCRecipes.fusionCraftingRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                int recipetier = recipe.getMetadataOrDefault(DEFC_CASING_TIER, 1);

                return recipetier <= mTierCasing ? CheckRecipeResultRegistry.SUCCESSFUL
                    : CheckRecipeResultRegistry.insufficientMachineTier(recipetier);
            }

            @NotNull
            @Override
            protected OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                int recipetier = recipe.getMetadataOrDefault(DEFC_CASING_TIER, 1);
                return super.createOverclockCalculator(recipe)
                    .setMachineHeat(mTierCasing > recipetier ? 1800 * (mTierCasing - recipetier) : 1)
                    .setRecipeHeat(0)
                    .setHeatOC(true)
                    .setHeatDiscount(false);
            }

        };
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, 2, 9, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 2, 9, 0, elementBudget, env, true, true);
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
