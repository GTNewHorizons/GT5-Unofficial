package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static gregtech.api.enums.GTValues.AuthorSteamIsTheNumber;
import static gregtech.api.recipe.RecipeMaps.steamWoodcutterRecipes;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import javax.annotation.Nonnull;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasingsSteam;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTEBetterSteamMultiBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTESteamWoodcutter extends MTEBetterSteamMultiBase<MTESteamWoodcutter> implements ISurvivalConstructable {

    public MTESteamWoodcutter(String aName) {
        super(aName);
    }

    public MTESteamWoodcutter(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    public String getMachineType() {
        return "Woodcutter";
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";

    @Override
    public IStructureDefinition<MTESteamWoodcutter> getStructureDefinition() {
        return StructureDefinition.<MTESteamWoodcutter>builder()
            .addShape(
                STRUCTURE_PIECE_MAIN,
                (new String[][] { { "  CCC  ", "       ", "       ", "       ", "       ", "       ", "  C~C  " },
                    { " CCBCC ", "  AAA  ", "  AAA  ", "  AAA  ", "  AAA  ", "  AAA  ", " CCBCC " },
                    { "CCBDBCC", " A   A ", " A   A ", " A   A ", " A   A ", " A   A ", "CCEEECC" },
                    { "CBDDDBC", " A   A ", " A   A ", " A   A ", " A   A ", " A   A ", "CBEEEBC" },
                    { "CCBDBCC", " A   A ", " A   A ", " A   A ", " A   A ", " A   A ", "CCEEECC" },
                    { " CCBCC ", "  AAA  ", "  AAA  ", "  AAA  ", "  AAA  ", "  AAA  ", " CCBCC " },
                    { "  CCC  ", "       ", "       ", "       ", "       ", "       ", "  CCC  " } }))
            .addElement('A', chainAllGlasses())
            .addElement('B', ofBlock(GregTechAPI.sBlockCasingsSteam, 4))
            .addElement(
                'C',
                ofChain(
                    buildSteamInput(MTESteamWoodcutter.class)
                        .casingIndex(((BlockCasingsSteam) GregTechAPI.sBlockCasingsSteam).getTextureIndex(5))
                        .dot(1)
                        .build(),
                    buildHatchAdder(MTESteamWoodcutter.class)
                        .atLeast(SteamHatchElement.InputBus_Steam, SteamHatchElement.OutputBus_Steam)
                        .casingIndex(((BlockCasingsSteam) GregTechAPI.sBlockCasingsSteam).getTextureIndex(5))
                        .dot(1)
                        .buildAndChain(),
                    ofBlock(GregTechAPI.sBlockCasingsSteam, 5)))
            .addElement('D', ofBlock(GregTechAPI.sBlockCasingsSteam, 6))
            .addElement('E', ofBlock(Blocks.dirt, 0))
            .build();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return steamWoodcutterRecipes;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 6, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 6, 0, elementBudget, env, false, true);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Nonnull
            @Override
            protected CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                if (availableVoltage < recipe.mEUt) {
                    return CheckRecipeResultRegistry.insufficientPower(recipe.mEUt);
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        };
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo(EnumChatFormatting.AQUA + "" + EnumChatFormatting.ITALIC + "Grows trees slowly from saplings.")
            .addInfo("Created by: ")
            .addInfo(AuthorSteamIsTheNumber)
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
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasingsSteam, 5)),
                    TextureFactory.builder()
                        .addIcon(TexturesGtBlock.oMCATreeFarmActive)
                        .extFacing()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasingsSteam, 5)),
                    TextureFactory.builder()
                        .addIcon(TexturesGtBlock.oMCATreeFarm)
                        .extFacing()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasingsSteam, 5)) };
        }
        return rTexture;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, 3, 6, 0);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESteamWoodcutter(this.mName);
    }
}
