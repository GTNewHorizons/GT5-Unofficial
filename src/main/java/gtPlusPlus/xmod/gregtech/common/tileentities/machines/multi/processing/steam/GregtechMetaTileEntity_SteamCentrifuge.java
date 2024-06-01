package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.GregTech_API.*;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

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
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_SteamMultiBase;

public class GregtechMetaTileEntity_SteamCentrifuge
    extends GregtechMeta_SteamMultiBase<GregtechMetaTileEntity_SteamCentrifuge> implements ISurvivalConstructable {

    private String mCasingName = "Solid Steel Machine Casing";
    private String tCasing1 = "Steel Frame Box";
    private String tCasing2 = "Steel Firebox Casing";
    private String tCasing3 = "Steel Gear Box Casing";
    private String tCasing4 = "Steel Pipe Casing";

    private static final int CASING_TEXTURE_ID = 16;
    private static int mCasing;

    @Override
    public String getMachineType() {
        return "Centrifuge";
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

    public GregtechMetaTileEntity_SteamCentrifuge(String aName) {
        super(aName);
    }

    public GregtechMetaTileEntity_SteamCentrifuge(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    protected GT_RenderedTexture getFrontOverlay() {
        return new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_STEAM_CENTRIFUGE);
    }

    @Override
    protected GT_RenderedTexture getFrontOverlayActive() {
        return new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_STEAM_CENTRIFUGE_ACTIVE);
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
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_SteamCentrifuge(this.mName);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.centrifugeRecipes;
    }

    private static IStructureDefinition<GregtechMetaTileEntity_SteamCentrifuge> STRUCTURE_DEFINITION = null;

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_SteamCentrifuge> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_SteamCentrifuge>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] { { " AAA ", "AAAAA", "AAAAA", "AAAAA", " AAA " },
                            { "     ", " EBE ", " BDB ", " EBE ", "     " },
                            { "  A  ", " ECE ", "ACDCA", " ECE ", "  A  " },
                            { " A~A ", "AEBEA", "ABDBA", "AEBEA", " AAA " },
                            { " AAA ", "AAAAA", "AAAAA", "AAAAA", " AAA " }, }))
                .addElement('E', ofFrame(Materials.Steel))
                .addElement('C', ofBlock(sBlockCasings2, 13))
                .addElement('D', ofBlock(sBlockCasings3, 14))
                .addElement('B', ofBlock(sBlockCasings2, 3))
                .addElement(
                    'A',
                    ofChain(
                        buildSteamInput(GregtechMetaTileEntity_SteamCentrifuge.class).casingIndex(CASING_TEXTURE_ID)
                            .dot(1)
                            .build(),
                        buildHatchAdder(GregtechMetaTileEntity_SteamCentrifuge.class)
                            .atLeast(SteamHatchElement.InputBus_Steam, SteamHatchElement.OutputBus_Steam)
                            .casingIndex(CASING_TEXTURE_ID)
                            .dot(1)
                            .build(),
                        onElementPass(x -> ++x.mCasing, ofBlock(sBlockCasings2, 0))))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        fixAllMaintenanceIssue();
        boolean didBuild = checkPiece(mName, 2, 3, 0);
        return didBuild && mCasing >= 50 && checkHatch();
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 2, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 2, 3, 0, elementBudget, env, false, true);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType())
            .addInfo("Controller Block for the Steam Centrifuge")
            .addInfo("Runs recipes up to MV tier")
            .addInfo("Centrifuges up to " + getMaxParallelRecipes() + " things at a time")
            .addSeparator()
            .beginStructureBlock(5, 5, 5, false)
            .addCasingInfoMin(mCasingName, 50, false)
            .addCasingInfo(tCasing1, 12)
            .addCasingInfo(tCasing2, 3)
            .addCasingInfo(tCasing3, 8)
            .addCasingInfo(tCasing4, 4)
            .addOtherStructurePart(TT_steaminputbus, "Any casing", 1)
            .addOtherStructurePart(TT_steamoutputbus, "Any casing", 1)
            .addOtherStructurePart(TT_steamhatch, "Any casing", 1)
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
