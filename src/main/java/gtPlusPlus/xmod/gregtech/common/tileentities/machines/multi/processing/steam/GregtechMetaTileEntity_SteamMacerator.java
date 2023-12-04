package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.GregTech_API.sBlockCasings1;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_SteamMultiBase;

public class GregtechMetaTileEntity_SteamMacerator
        extends GregtechMeta_SteamMultiBase<GregtechMetaTileEntity_SteamMacerator> implements ISurvivalConstructable {

    private String mCasingName = "Bronze Plated Bricks";
    private static IStructureDefinition<GregtechMetaTileEntity_SteamMacerator> STRUCTURE_DEFINITION = null;
    private int mCasing;

    public GregtechMetaTileEntity_SteamMacerator(String aName) {
        super(aName);
    }

    public GregtechMetaTileEntity_SteamMacerator(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity arg0) {
        return new GregtechMetaTileEntity_SteamMacerator(this.mName);
    }

    @Override
    protected GT_RenderedTexture getFrontOverlay() {
        return new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TOP_STEAM_MACERATOR);
    }

    @Override
    protected GT_RenderedTexture getFrontOverlayActive() {
        return new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TOP_STEAM_MACERATOR_ACTIVE);
    }

    @Override
    public String getMachineType() {
        return "Macerator";
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        if (mCasingName.contains("gt.blockcasings")) {
            mCasingName = ItemList.Casing_BronzePlatedBricks.get(1).getDisplayName();
        }
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Controller Block for the Steam Macerator")
                .addInfo("Macerates " + getMaxParallelRecipes() + " ores at a time").addSeparator()
                .beginStructureBlock(3, 3, 3, true).addController("Front center")
                .addCasingInfoMin(mCasingName, 14, false).addOtherStructurePart(TT_steaminputbus, "Any casing", 1)
                .addOtherStructurePart(TT_steamoutputbus, "Any casing", 1)
                .addOtherStructurePart(TT_steamhatch, "Any casing", 1).toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_SteamMacerator> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_SteamMacerator>builder().addShape(
                    mName,
                    transpose(
                            new String[][] { { "CCC", "CCC", "CCC" }, { "C~C", "C-C", "CCC" },
                                    { "CCC", "CCC", "CCC" }, }))
                    .addElement(
                            'C',
                            ofChain(
                                    buildSteamInput(GregtechMetaTileEntity_SteamMacerator.class).casingIndex(10).dot(1)
                                            .build(),
                                    buildHatchAdder(GregtechMetaTileEntity_SteamMacerator.class).atLeast(
                                            SteamHatchElement.InputBus_Steam,
                                            SteamHatchElement.OutputBus_Steam).casingIndex(10).dot(1).build(),
                                    onElementPass(x -> ++x.mCasing, ofBlock(sBlockCasings1, 10))))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivialBuildPiece(mName, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        fixAllMaintenanceIssue();
        return checkPiece(mName, 1, 1, 0) && mCasing >= 14;
    }

    @Override
    public int getMaxParallelRecipes() {
        return 8;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.maceratorRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            @Nonnull
            protected GT_OverclockCalculator createOverclockCalculator(@NotNull GT_Recipe recipe) {
                return GT_OverclockCalculator.ofNoOverclock(recipe);
            }

        }.setMaxParallel(getMaxParallelRecipes());
    }

    @Override
    public int getItemOutputLimit() {
        return 1;
    }
}
