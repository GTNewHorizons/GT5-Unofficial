package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_STEAM_MACERATOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_STEAM_MACERATOR_ACTIVE;
import static gregtech.api.recipe.RecipeMaps.steamFusionReactorRecipes;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.recipe.metadata.SteamFusionTierKey;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasingsSteam;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTEBetterSteamMultiBase;

public class MTESteamFusionReactor extends MTEBetterSteamMultiBase<MTESteamFusionReactor>
    implements ISurvivalConstructable {

    public MTESteamFusionReactor(String aName) {
        super(aName);
    }

    public MTESteamFusionReactor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    public String getMachineType() {
        return "High Pressure Reactor";
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";

    @Override
    public IStructureDefinition<MTESteamFusionReactor> getStructureDefinition() {
        return StructureDefinition.<MTESteamFusionReactor>builder()
            .addShape(
                STRUCTURE_PIECE_MAIN,
                (new String[][] { { "               ", "      DDD      ", "               " },
                    { "      CCC      ", "    DDBBBDD    ", "      CCC      " },
                    { "    CC   CC    ", "   ABBDDDBBA   ", "    CC   CC    " },
                    { "   C       C   ", "  ABAD   DABA  ", "   C       C   " },
                    { "  C         C  ", " DBA       ABD ", "  C         C  " },
                    { "  C         C  ", " DBD       DBD ", "  C         C  " },
                    { " C           C ", "DBD         DBD", " C           C " },
                    { " C           C ", "DBD         DBD", " C           C " },
                    { " C           C ", "DBD         DBD", " C           C " },
                    { "  C         C  ", " DBD       DBD ", "  C         C  " },
                    { "  C         C  ", " DBA       ABD ", "  C         C  " },
                    { "   C       C   ", "  ABAD   DABA  ", "   C       C   " },
                    { "    CC   CC    ", "   ABBA~ABBA   ", "    CC   CC    " },
                    { "      CCC      ", "    DDBBBDD    ", "      CCC      " },
                    { "               ", "      DDD      ", "               " } }))
            .addElement(
                'A',
                ofChain(
                    buildSteamInput(MTESteamFusionReactor.class)
                        .casingIndex(((BlockCasingsSteam) GregTechAPI.sBlockCasingsSteam).getTextureIndex(11))
                        .dot(1)
                        .build(),
                    buildHatchAdder(MTESteamFusionReactor.class).atLeast(InputHatch, OutputHatch)
                        .casingIndex(((BlockCasingsSteam) GregTechAPI.sBlockCasingsSteam).getTextureIndex(11))
                        .dot(1)
                        .buildAndChain(),
                    ofBlock(GregTechAPI.sBlockCasingsSteam, 11)))
            .addElement('B', ofBlock(GregTechAPI.sBlockCasingsSteam, 7))
            .addElement('D', chainAllGlasses())
            .addElement('C', ofBlock(GregTechAPI.sBlockCasingsSteam, 11))
            .build();
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 7, 1, 12);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 7, 1, 12, elementBudget, env, false, true);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (recipe.getMetadataOrDefault(SteamFusionTierKey.INSTANCE, 0) != 0) {
                    return SimpleCheckRecipeResult.ofFailure("metadata.steamfusion");
                }
                return super.validateRecipe(recipe);
            }
        };
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return steamFusionReactorRecipes;
    }

    @Override
    public int getMaxParallelRecipes() {
        // Max call to prevent seeing -16 parallels in waila for unformed multi
        return 1;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_FUSION_LOOP;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("Combines Steam with hot fluids in order to reach higher temperatures")
            .addInfo("Requires Steam to work? No, but kinda.")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "What's this? Fusion? HAHAHA, tell another joke silly!")
            .addInputHatch("1-8, Blocks Adjacent to Glass", 1)
            .addOutputHatch("1-8, Blocks Adjacent to Glass", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        ITexture[] rTexture;
        if (side == facing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasingsSteam, 11)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_TOP_STEAM_MACERATOR_ACTIVE)
                        .extFacing()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasingsSteam, 11)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_TOP_STEAM_MACERATOR)
                        .extFacing()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasingsSteam, 11)) };
        }
        return rTexture;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, 7, 1, 12);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESteamFusionReactor(this.mName);
    }
}
