package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.Textures.BlockIcons.COKE_OVEN_OVERLAY_INACTIVE;

import net.minecraft.item.ItemStack;
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
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;

public class MTECokeOven extends MTEEnhancedMultiBlockBase<MTECokeOven> implements ISurvivalConstructable {

    public MTECokeOven(String name) {
        super(name);
    }

    public MTECokeOven(int ID, String name, String nameRegional) {
        super(ID, name, nameRegional);
    }

    // spotless:off
    private static final String[][] shape = new String[][] {
        { "CCC", "CCC", "CCC" },
        { "C~C", "C-C", "CCC" },
        { "CCC", "CCC", "CCC" } };
    //spotless:on

    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final IStructureDefinition<MTECokeOven> STRUCTURE_DEFINITION = StructureDefinition
        .<MTECokeOven>builder()
        .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings12, 0))
        .build();

    @Override
    public IStructureDefinition getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType("Coke Oven")
            .toolTipFinisher();
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, 1, 1, 0);
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.cokeOvenRecipes;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTECokeOven(this.mName);
    }

    private static final ITexture[] TEXTURE_CASING = {
        Textures.BlockIcons.getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, 0)) };

    private static final ITexture[] TEXTURE_CONTROLLER_INACTIVE = {
        Textures.BlockIcons.getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, 0)),
        TextureFactory.builder()
            .addIcon(COKE_OVEN_OVERLAY_INACTIVE)
            .extFacing()
            .build() };

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {

        if (side == facing) {
            return TEXTURE_CONTROLLER_INACTIVE;
        } else {
            return TEXTURE_CASING;
        }
    }
}
