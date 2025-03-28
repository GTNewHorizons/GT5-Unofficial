package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.GTValues.AuthorSteamIsTheNumber;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_STEAM_EXTRACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_STEAM_EXTRACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_STEAM_EXTRACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_STEAM_EXTRACTOR_GLOW;
import static gregtech.api.recipe.RecipeMaps.steamCarpenterRecipes;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTEBetterSteamMultiBase;

public class MTESteamCarpenter extends MTEBetterSteamMultiBase<MTESteamCarpenter> implements ISurvivalConstructable {

    public MTESteamCarpenter(String aName) {
        super(aName);
    }

    public MTESteamCarpenter(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    public String getMachineType() {
        return "Carpenter";
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";

    @Override
    public IStructureDefinition<MTESteamCarpenter> getStructureDefinition() {
        return StructureDefinition.<MTESteamCarpenter>builder()
            .addShape(
                STRUCTURE_PIECE_MAIN,
                (new String[][] { { "A A", "   ", "A~A" }, { "ABA", "C C", "AAA" }, { " A ", " C ", " A " } }))
            .addElement('A', ofBlock(GregTechAPI.sBlockCasings1, 10))
            .addElement('B', ofBlock(GregTechAPI.sBlockCasings2, 2))
            .addElement('C', ofFrame(Materials.Wood))
            .build();
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 2, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 2, 0, elementBudget, env, false, true);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return steamCarpenterRecipes;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo(
                EnumChatFormatting.AQUA + "" + EnumChatFormatting.ITALIC + "Cuts wood efficiently into many forms.")
            .addInfo("Created by: ")
            .addInfo(AuthorSteamIsTheNumber)
            .beginStructureBlock(3, 3, 3, false)
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
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings1, 10)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_STEAM_EXTRACTOR_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_STEAM_EXTRACTOR_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings1, 10)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_STEAM_EXTRACTOR)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_STEAM_EXTRACTOR_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings1, 10)) };
        }
        return rTexture;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, 1, 2, 0);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESteamCarpenter(this.mName);
    }
}
