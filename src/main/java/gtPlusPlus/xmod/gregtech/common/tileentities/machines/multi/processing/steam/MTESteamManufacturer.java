package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_CARPENTER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_CARPENTER_ACTIVE;
import static gregtech.api.recipe.RecipeMaps.steamManufacturerRecipes;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
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
import gregtech.common.blocks.BlockCasings2;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTEBetterSteamMultiBase;

public class MTESteamManufacturer extends MTEBetterSteamMultiBase<MTESteamManufacturer>
    implements ISurvivalConstructable {

    public MTESteamManufacturer(String aName) {
        super(aName);
    }

    public MTESteamManufacturer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    public String getMachineType() {
        return "Assembler";
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";

    @Override
    public IStructureDefinition<MTESteamManufacturer> getStructureDefinition() {
        return StructureDefinition.<MTESteamManufacturer>builder()
            .addShape(
                STRUCTURE_PIECE_MAIN,
                (new String[][] {
                    { "         ", "         ", "         ", "         ", " AAAAA   ", " BB~BB   ", " AAAAA   " },
                    { "         ", "         ", "         ", "         ", "A     A  ", "B     B  ", "AAAAAAA  " },
                    { "       D ", "     DDED", "       D ", "       D ", "A     AD ", "B CCC CD ", "AAAAAAAAA" },
                    { "    DDDAD", "   EEEEEA", "   E   CA", "   E   CA", "A     ACA", "B C CCCCA", "AAAAAAAAA" },
                    { "       D ", "     DDED", "       D ", "       D ", "A     AD ", "B CCC CD ", "AAAAAAAAA" },
                    { "         ", "         ", "         ", "         ", "A     A  ", "B     B  ", "AAAAAAA  " },
                    { "         ", "         ", "         ", "         ", " AAAAA   ", " BBBBB   ", " AAAAA   " } }))
            .addElement(
                'A',
                ofChain(
                    buildSteamInput(MTESteamManufacturer.class)
                        .casingIndex(((BlockCasings2) GregTechAPI.sBlockCasings2).getTextureIndex(0))
                        .dot(1)
                        .build(),
                    buildHatchAdder(MTESteamManufacturer.class)
                        .atLeast(SteamHatchElement.InputBus_Steam, SteamHatchElement.OutputBus_Steam)
                        .casingIndex(((BlockCasings2) GregTechAPI.sBlockCasings2).getTextureIndex(0))
                        .dot(1)
                        .buildAndChain(),
                    ofBlock(GregTechAPI.sBlockCasings2, 0)))
            .addElement('B', ofBlock(GregTechAPI.sBlockCasings2, 3))
            .addElement('D', ofBlock(GregTechAPI.sBlockCasings2, 13))
            .addElement('C', ofFrame(Materials.Steel))
            .addElement('E', ofBlock(GregTechAPI.sBlockCasingsSteam, 10))
            .build();
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 5, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 5, 0, elementBudget, env, false, true);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return steamManufacturerRecipes;
    }

    @Override
    public int getMaxParallelRecipes() {
        // Max call to prevent seeing -16 parallels in waila for unformed multi
        return 4;
    }

    @Override
    protected SteamTypes getSteamType() {
        return SteamTypes.SH_STEAM;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("Assembles assemblies assemblically")
            .addInfo("Requires Super Heated Steam to Work")
            .addInfo(EnumChatFormatting.AQUA + "" + EnumChatFormatting.ITALIC + "Slave labor? Not in my GTNH!")
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
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_CARPENTER_ACTIVE)
                        .extFacing()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_CARPENTER)
                        .extFacing()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0)) };
        }
        return rTexture;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, 3, 5, 0);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESteamManufacturer(this.mName);
    }
}
