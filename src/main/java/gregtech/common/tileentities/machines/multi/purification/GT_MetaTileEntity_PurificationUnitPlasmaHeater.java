package gregtech.common.tileentities.machines.multi.purification;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static gregtech.api.enums.GT_Values.AuthorNotAPenguin;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW;
import static gregtech.api.recipe.RecipeMaps.purificationPlasmaHeatingRecipes;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;

public class GT_MetaTileEntity_PurificationUnitPlasmaHeater
    extends GT_MetaTileEntity_PurificationUnitBase<GT_MetaTileEntity_PurificationUnitPlasmaHeater>
    implements ISurvivalConstructable {

    private static final int CASING_INDEX_HEATER = getTextureIndex(GregTech_API.sBlockCasings9, 10);

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int STRUCTURE_X_OFFSET = 2;
    private static final int STRUCTURE_Y_OFFSET = 14;
    private static final int STRUCTURE_Z_OFFSET = 5;

    private static final String[][] structure = new String[][] {
        // spotless:off
        { "            DDDDDDD    ", "                       ", "                       ", "                       ", "                       ", "                       ", "                       ", "                       ", "                       ", "                       ", "                       ", "                       ", "             DDDDD     ", "             DDDDD     ", "             DDDDD     " },
        { "          DD       DD  ", "            DDDDDDD    ", "             DDDDD     ", "                       ", "                       ", "                       ", "                       ", "                       ", "             DDDDD     ", "             DDDDD     ", "             DDDDD     ", "             DDDDD     ", "           DD     DD   ", "           DD     DD   ", "           DDDDDDDDD   " },
        { "         D           D ", "          DDD     DDD  ", "            DDDDDDD    ", "             DDDDD     ", "             DDDDD     ", "             DDDDD     ", "             DDDDD     ", "             DDDDD     ", "           DD     DD   ", "           DD     DD   ", "           DD     DD   ", "           DD     DD   ", "          D         D  ", "          D         D  ", "          DDDDDDDDDDD  " },
        { "         D           D ", "          D        DD  ", "           DD      D   ", "           DD     DD   ", "           DD     DD   ", "           DD     DD   ", "           DD     DD   ", "           DD     DD   ", "          D         D  ", "          D         D  ", "          D         D  ", "          D         D  ", "         D           D ", "         D           D ", "         DDDDDDDDDDDDD " },
        { "        D             D", "         DD         DD ", "          DD        D  ", "           D       D   ", "           D       D   ", "           D       D   ", "           D       D   ", "           D       D   ", "          D         D  ", "          D         D  ", "          D         D  ", "          D         D  ", "         D           D ", "         D           D ", "         DDDDDDDDDDDDD " },
        { "        D             D", "         D           D ", "         DD          D ", "          D         D  ", "          D         D  ", "GBBBG     D         D  ", "G   G     D         D  ", "G   G     D         D  ", "G   G    D           D ", "G   G    D           D ", "G   G    D           D ", "G   G    D           D ", "G   G   D             D", "G   G   D             D", "GB~BG   DDDDDDDDDDDDDDD" },
        { "        D             D", "         D           D ", "         DD          D ", "          D         D  ", " BBB      D         D  ", "BBBBB     D         D  ", " EEE      D         D  ", " EEE      D         D  ", " EEE     D           D ", " EEE     D           D ", " EEE     D           D ", " EEE     D           D ", " EEE    D             D", " EEEBBBBD             D", "BAAAB   DDDDDDDDDDDDDDD" },
        { "        D             D", "         D           D ", "         DD          D ", "          D         D  ", " BBB      D         D  ", "BBBBB     D         D  ", " EFE      D         D  ", " EFE      D         D  ", " EFE     D           D ", " EFE     D           D ", " EFE     D           D ", " EFE     D           D ", " EFEBBBBD             D", " EFE    D             D", "BAAABBBBDDDDDDDDDDDDDDD" },
        { "        D             D", "         D           D ", "         DD          D ", "          D         D  ", " BBB      D         D  ", "BBBBB     D         D  ", " EEE      D         D  ", " EEE      D         D  ", " EEE     D           D ", " EEE     D           D ", " EEE     D           D ", " EEE     D           D ", " EEE    D             D", " EEEBBBBD             D", "BAAAB   DDDDDDDDDDDDDDD" },
        { "        D             D", "         D           D ", "         DD          D ", "          D         D  ", "          D         D  ", "GBBBG     D         D  ", "G   G     D         D  ", "G   G     D         D  ", "G   G    D           D ", "G   G    D           D ", "G   G    D           D ", "G   G    D           D ", "G   G   D             D", "G   G   D             D", "GBBBG   DDDDDDDDDDDDDDD" },
        { "        D             D", "         D           D ", "          D         D  ", "           D       DD  ", "           D       DD  ", "           D       DD  ", "           D       DD  ", "           D       DD  ", "          D          D ", "          D          D ", "          D          D ", "          D          D ", "         D           D ", "         D           D ", "         DDDDDDDDDDDDD " },
        { "         D           DD", "          D         D  ", "           D       D   ", "           DD     DD   ", "           DD     DD   ", "           DD     DD   ", "           DD     DD   ", "           DD     DD   ", "          D         D  ", "          D         D  ", "          D         D  ", "          D         D  ", "         D           D ", "         D           D ", "         DDDDDDDDDDDDD " },
        { "         D           D ", "          DD       DD  ", "            D     D    ", "             DDDDD     ", "             DDDDD     ", "             DDDDD     ", "             DDDDD     ", "             DDDDD     ", "           DD     DD   ", "           DD     DD   ", "           DD     DD   ", "           DD     DD   ", "          D         D  ", "          D         D  ", "          DDDDDDDDDDD  " },
        { "          DD       DD  ", "            DDDDDDD    ", "             DDDDD     ", "                       ", "                       ", "                       ", "                       ", "                       ", "             DDDDD     ", "             DDDDD     ", "             DDDDD     ", "             DDDDD     ", "           DD     DD   ", "           DD     DD   ", "           DDDDDDDDD   " },
        { "            DDDDDDD    ", "                       ", "                       ", "                       ", "                       ", "                       ", "                       ", "                       ", "                       ", "                       ", "                       ", "                       ", "             DDDDD     ", "             DDDDD     ", "             DDDDD     " } };
    // spotless:on

    private static final IStructureDefinition<GT_MetaTileEntity_PurificationUnitPlasmaHeater> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_PurificationUnitPlasmaHeater>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        // Superconducting coil block
        .addElement('A', ofBlock(GregTech_API.sBlockCasings1, 15))
        // Plasma Heating Casing
        .addElement('B', ofBlock(GregTech_API.sBlockCasings9, 10))
        // Water Plant Concrete Casing
        .addElement('D', ofBlock(GregTech_API.sBlockCasings9, 4))
        // Any Tinted Glass
        .addElement('E', ofBlockAnyMeta(GregTech_API.sBlockTintedGlass, 0))
        // Neonite, with fallback to air
        .addElement('F', lazy(t -> {
            if (Mods.Chisel.isModLoaded()) {
                Block neonite = GameRegistry.findBlock(Mods.Chisel.ID, "neonite");
                return ofBlockAnyMeta(neonite, 7);
            } else {
                return ofBlockAnyMeta(Blocks.air);
            }
        }))
        // Superconductor Base ZPM frame box
        .addElement('G', ofFrame(Materials.Tetranaquadahdiindiumhexaplatiumosminid))
        .build();

    public GT_MetaTileEntity_PurificationUnitPlasmaHeater(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_PurificationUnitPlasmaHeater(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_PurificationUnitPlasmaHeater(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            if (active) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_HEATER),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_HEATER),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_HEATER) };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            hintsOnly,
            STRUCTURE_X_OFFSET,
            STRUCTURE_Y_OFFSET,
            STRUCTURE_Z_OFFSET);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            STRUCTURE_X_OFFSET,
            STRUCTURE_Y_OFFSET,
            STRUCTURE_Z_OFFSET,
            elementBudget,
            env,
            true);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_PurificationUnitPlasmaHeater> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return purificationPlasmaHeatingRecipes;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addInfo(AuthorNotAPenguin);
        tt.toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public int getWaterTier() {
        return 5;
    }

    @Override
    public long getActivePowerUsage() {
        return TierEU.RECIPE_UV;
    }
}
