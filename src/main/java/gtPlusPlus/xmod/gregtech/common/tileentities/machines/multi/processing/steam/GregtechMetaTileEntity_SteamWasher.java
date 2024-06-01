package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.GregTech_API.*;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_SteamMultiBase;
import ic2.core.init.BlocksItems;
import ic2.core.init.InternalName;

public class GregtechMetaTileEntity_SteamWasher extends GregtechMeta_SteamMultiBase<GregtechMetaTileEntity_SteamWasher>
    implements ISurvivalConstructable {

    private String mCasingName = "Solid Steel Machine Casing";
    private String tCasing1 = "Steel Frame Box";
    private String tCasing2 = "Steel Gear Box Casing";
    private String tCasing3 = "Steel Pipe Casing";
    private String tCasing4 = "Glass";

    private static final int CASING_TEXTURE_ID = 16;
    private int mCasing;

    @Override
    public String getMachineType() {
        return "Washer";
    }

    @Override
    public int getMaxParallelRecipes() {
        return 8;
    }

    protected static int getCasingTextureIndex() {
        return CASING_TEXTURE_ID;
    }

    protected static String getNickname() {
        return "EvgenWarGold";
    }

    public GregtechMetaTileEntity_SteamWasher(String aName) {
        super(aName);
    }

    public GregtechMetaTileEntity_SteamWasher(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    protected GT_RenderedTexture getFrontOverlay() {
        return new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_STEAM_WASHER);
    }

    @Override
    protected GT_RenderedTexture getFrontOverlayActive() {
        return new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_STEAM_WASHER_ACTIVE);
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
        final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(getCasingTextureIndex()),
                aActive ? getFrontOverlayActive() : getFrontOverlay() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(getCasingTextureIndex()) };
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        // don't rotate a washer, water will flow out.
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_SteamWasher(this.mName);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.oreWasherRecipes;
    }

    private static IStructureDefinition<GregtechMetaTileEntity_SteamWasher> STRUCTURE_DEFINITION = null;

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_SteamWasher> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_SteamWasher>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] { { "         ", "         ", " CCCCCC  ", "         ", "         " },
                            { "         ", "         ", " C    C  ", "         ", "         " },
                            { "     AAA ", "    A   A", " C  A C A", "    A   A", "     AAA " },
                            { "    ADDDA", "FAF D   D", "AAA D C D", "FAF D   D", "    ADDDA" },
                            { "    ADDDA", "F~F DEEED", "AAA DECED", "FAF DEEED", "    ADDDA" },
                            { "    AAAAA", "AAA ABBBA", "AAA ABABA", "AAA ABBBA", "    AAAAA" }, }))
                .addElement('F', ofFrame(Materials.Steel))
                .addElement('C', ofBlock(getCasingBlock2(), getCasingMeta2()))
                .addElement(
                    'E',
                    ofChain(
                        isAir(),
                        ofBlockAnyMeta(Blocks.water),
                        ofBlockAnyMeta(Blocks.flowing_water),
                        ofBlockAnyMeta(BlocksItems.getFluidBlock(InternalName.fluidDistilledWater))))
                .addElement('D', Glasses.chainAllGlasses())
                .addElement('B', ofBlock(getCasingBlock4(), getCasingMeta4()))
                .addElement(
                    'A',
                    ofChain(
                        buildSteamInput(GregtechMetaTileEntity_SteamWasher.class).casingIndex(CASING_TEXTURE_ID)
                            .dot(1)
                            .build(),
                        buildHatchAdder(GregtechMetaTileEntity_SteamWasher.class)
                            .atLeast(SteamHatchElement.InputBus_Steam, SteamHatchElement.OutputBus_Steam, InputHatch)
                            .casingIndex(CASING_TEXTURE_ID)
                            .dot(1)
                            .build(),
                        onElementPass(x -> ++x.mCasing, ofBlock(getCasingBlock5(), getCasingMeta5()))))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    public Block getCasingBlock2() {
        return sBlockCasings2;
    }

    public byte getCasingMeta2() {
        return 13;
    }

    public Block getCasingBlock3() {
        return sBlockCasings3;
    }

    public byte getCasingMeta3() {
        return 14;
    }

    public Block getCasingBlock4() {
        return sBlockCasings2;
    }

    public byte getCasingMeta4() {
        return 3;
    }

    public Block getCasingBlock5() {
        return sBlockCasings2;
    }

    public byte getCasingMeta5() {
        return 0;
    }

    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        fixAllMaintenanceIssue();
        boolean didBuild = checkPiece(mName, 1, 4, 1);
        return didBuild && mCasing >= 45 && checkHatch();
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 4, 1);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 1, 4, 1, elementBudget, env, false, true);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType())
            .addInfo("Controller Block for the Steam Washer")
            .addInfo("Runs recipes up to LV tier")
            .addInfo("Washing up to " + getMaxParallelRecipes() + " things at a time")
            .addSeparator()
            .beginStructureBlock(5, 5, 9, false)
            .addCasingInfoMin(mCasingName, 45, false)
            .addCasingInfo(tCasing1, 8)
            .addCasingInfo(tCasing2, 8)
            .addCasingInfo(tCasing3, 12)
            .addCasingInfo(tCasing4, 24)
            .addOtherStructurePart(TT_steaminputbus, "Any casing", 1)
            .addOtherStructurePart(TT_steamoutputbus, "Any casing", 1)
            .addOtherStructurePart(TT_steamhatch, "Any casing", 1)
            .addInputHatch("Any casing", 1)
            .toolTipFinisher(getNickname());
        return tt;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            @Nonnull
            protected GT_OverclockCalculator createOverclockCalculator(@NotNull GT_Recipe recipe) {
                return GT_OverclockCalculator.ofNoOverclock(recipe)
                    .setEUtDiscount(1.33F)
                    .setSpeedBoost(1.5F);
            }
        }.setMaxParallel(getMaxParallelRecipes());
    }
}
