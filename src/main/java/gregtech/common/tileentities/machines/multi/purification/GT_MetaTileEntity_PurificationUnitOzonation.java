package gregtech.common.tileentities.machines.multi.purification;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static gregtech.api.enums.GT_Values.AuthorNotAPenguin;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import java.util.Arrays;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_PurificationUnitOzonation
    extends GT_MetaTileEntity_PurificationUnitBase<GT_MetaTileEntity_PurificationUnitOzonation>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String STRUCTURE_PIECE_MAIN_SURVIVAL = "main_survival";

    private static final String[][] structure = new String[][] {
        // spotless:off
        { "         ", "         ", "      A  ", "      A  ", "     AAA ", "     AAA ", "     A A ", "     A A ", "     A A ", "     A~A " },
        { "      A  ", "      A  ", "     A A ", "     A A ", "BBBBA   A", "BDDBA   A", "BBBBA D A", "E   AWDWA", "E   AWDWA", "E   AAAAA" },
        { "     AAA ", "     A A ", "    A   A", "    A   A", "BDDBA   A", "B  BA   A", "BBBBA   A", "  C AWWWA", "  CCAWWWA", "    AAAAA" },
        { "      A  ", "      A  ", "     A A ", "     A A ", "BBBBA   A", "BDDBA   A", "BBBBA   A", "E   AWWWA", "E   AWWWA", "E   AAAAA" },
        { "         ", "         ", "      A  ", "      A  ", "     AAA ", "     AAA ", "     AAA ", "     AAA ", "     AAA ", "     AAA " } };
    // spotless:on

    // placeholder
    private static final int MAIN_CASING_INDEX = getTextureIndex(GregTech_API.sBlockCasings9, 9);

    private static final int OFFSET_X = 6;
    private static final int OFFSET_Y = 9;
    private static final int OFFSET_Z = 0;

    private static final IStructureDefinition<GT_MetaTileEntity_PurificationUnitOzonation> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_PurificationUnitOzonation>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        .addShape(
            STRUCTURE_PIECE_MAIN_SURVIVAL,
            Arrays.stream(structure)
                .map(
                    sa -> Arrays.stream(sa)
                        .map(s -> s.replaceAll("W", " "))
                        .toArray(String[]::new))
                .toArray(String[][]::new))
        // Ozonation Casing (placeholder name)
        .addElement('A', ofBlock(GregTech_API.sBlockCasings9, 9))
        // High Pressure Resistant Casing (possibly placeholder name)
        .addElement('B', ofBlock(GregTech_API.sBlockCasings9, 8))
        // PTFE pipe casing
        .addElement('C', ofBlock(GregTech_API.sBlockCasings8, 1))
        // Any tinted industrial glass
        .addElement('D', ofBlockAnyMeta(GregTech_API.sBlockTintedGlass))
        .addElement('E', ofFrame(Materials.TungstenSteel))
        .addElement('W', ofBlock(Blocks.water, 0))
        .build();

    public GT_MetaTileEntity_PurificationUnitOzonation(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected GT_MetaTileEntity_PurificationUnitOzonation(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_PurificationUnitOzonation(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(MAIN_CASING_INDEX),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(MAIN_CASING_INDEX),
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
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(MAIN_CASING_INDEX) };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        int built = survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            OFFSET_X,
            OFFSET_Y,
            OFFSET_Z,
            elementBudget,
            env,
            true);

        if (built == -1) {
            GT_Utility.sendChatToPlayer(
                env.getActor(),
                EnumChatFormatting.GREEN + "Auto placing done ! Now go place the water yourself !");
            return 0;
        }
        return built;
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_PurificationUnitOzonation> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Purification Unit")
            .addInfo(AuthorNotAPenguin)
            .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public int getWaterTier() {
        return 2;
    }

    @Override
    public long getActivePowerUsage() {
        return TierEU.RECIPE_LuV;
    }
}
