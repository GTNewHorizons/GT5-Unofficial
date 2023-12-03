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

public class GregtechMetaTileEntity_SteamCompressor
        extends GregtechMeta_SteamMultiBase<GregtechMetaTileEntity_SteamCompressor> implements ISurvivalConstructable {

    private String mCasingName = "Bronze Plated Bricks";
    private static IStructureDefinition<GregtechMetaTileEntity_SteamCompressor> STRUCTURE_DEFINITION = null;
    private int mCasing;

    public GregtechMetaTileEntity_SteamCompressor(String aName) {
        super(aName);
    }

    public GregtechMetaTileEntity_SteamCompressor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity arg0) {
        return new GregtechMetaTileEntity_SteamCompressor(this.mName);
    }

    @Override
    protected GT_RenderedTexture getFrontOverlay() {
        return new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_STEAM_COMPRESSOR);
    }

    @Override
    protected GT_RenderedTexture getFrontOverlayActive() {
        return new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_STEAM_COMPRESSOR_ACTIVE);
    }

    @Override
    public String getMachineType() {
        return "Compressor";
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Controller Block for the Steam Compressor")
                .addInfo("Compresses " + getMaxParallelRecipes() + " things at a time").addSeparator()
                .beginStructureBlock(3, 3, 4, true).addController("Front center")
                .addCasingInfoMin(mCasingName, 28, false).addOtherStructurePart(TT_steaminputbus, "Any casing", 1)
                .addOtherStructurePart(TT_steamoutputbus, "Any casing", 1)
                .addOtherStructurePart(TT_steamhatch, "Any casing", 1).toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_SteamCompressor> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_SteamCompressor>builder().addShape(
                    mName,
                    transpose(
                            new String[][] { { "CCC", "CCC", "CCC", "CCC" }, { "C~C", "C-C", "C-C", "CCC" },
                                    { "CCC", "CCC", "CCC", "CCC" }, }))
                    .addElement(
                            'C',
                            ofChain(
                                    buildSteamInput(GregtechMetaTileEntity_SteamCompressor.class).casingIndex(10).dot(1)
                                            .build(),
                                    buildHatchAdder(GregtechMetaTileEntity_SteamCompressor.class).atLeast(
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
        return checkPiece(mName, 1, 1, 0) && mCasing >= 28;
    }

    @Override
    public int getMaxParallelRecipes() {
        return 8;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.compressorRecipes;
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

}
