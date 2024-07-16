package gregtech.common.tileentities.machines.multi.purification;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static gregtech.api.enums.GT_Values.AuthorNotAPenguin;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import net.minecraft.item.ItemStack;
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

public class GT_MetaTileEntity_PurificationUnitDegasifier
    extends GT_MetaTileEntity_PurificationUnitBase<GT_MetaTileEntity_PurificationUnitDegasifier>
    implements ISurvivalConstructable {

    // TODO
    private static final int CASING_INDEX_MAIN = getTextureIndex(GregTech_API.sBlockCasings9, 11);

    private static final String STRUCTURE_PIECE_MAIN = "main";

    // Temporary, while testing
    // spotless:off
    private static final String[][] structure = new String[][] {
        { "           ", "           ", "           ", "     E     ", "           ", "           ", "           ", "           ", "   AAAAA   ", "  AAA~AAA  ", " AAAAAAAAA " },
        { "           ", "           ", "     E     ", "           ", "           ", "   CCCCC   ", "   CDCDC   ", "   CCCCC   ", "  ACCCCCA  ", " AAAAAAAAA ", "AAAAAAAAAAA" },
        { "           ", "           ", "     E     ", "           ", "           ", "  CAAAAAC  ", "  C     C  ", "  C     C  ", " AC     CA ", "AAAAAAAAAAA", "AAAAAAAAAAA" },
        { "           ", "     E     ", "           ", "    CCC    ", "   FCCCF   ", " CAA   AAC ", " C       C ", " C       C ", "AC       CA", "AAAAAAAAAAA", "AAAAAAAAAAA" },
        { "     E     ", "           ", "    BBB    ", "   C   C   ", "   C   C   ", " CA     AC ", " D       D ", " C       C ", "AC       CA", "AAAAAAAAAAA", "AAAAAAAAAAA" },
        { "     E     ", "     E     ", "    BEB    ", "   C E C   ", "   C E C   ", " CA  E  AC ", " C   E   C ", " C   E   C ", "AC   E   CA", "AAAAAAAAAAA", "AAAAAAAAAAA" },
        { "           ", "           ", "    BBB    ", "   C   C   ", "   C   C   ", " CA     AC ", " D       D ", " C       C ", "AC       CA", "AAAAAAAAAAA", "AAAAAAAAAAA" },
        { "           ", "           ", "           ", "    CCC    ", "   FCCCF   ", " CAA   AAC ", " C       C ", " C       C ", "AC       CA", "AAAAAAAAAAA", "AAAAAAAAAAA" },
        { "           ", "           ", "           ", "           ", "           ", "  CAAAAAC  ", "  C     C  ", "  C     C  ", " AC     CA ", "AAAAAAAAAAA", "AAAAAAAAAAA" },
        { "           ", "           ", "           ", "           ", "           ", "   CCCCC   ", "   CDCDC   ", "   CCCCC   ", "  ACCCCCA  ", " AAAAAAAAA ", "AAAAAAAAAAA" },
        { "           ", "           ", "           ", "           ", "           ", "           ", "           ", "           ", "   AAAAA   ", "  AAAAAAA  ", " AAAAAAAAA " } };
    // spotless:on

    private static final int STRUCTURE_X_OFFSET = 5;
    private static final int STRUCTURE_Y_OFFSET = 9;
    private static final int STRUCTURE_Z_OFFSET = 0;

    private static final IStructureDefinition<GT_MetaTileEntity_PurificationUnitDegasifier> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_PurificationUnitDegasifier>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        // PLACEHOLDER ELEMENTS
        .addElement('A', ofBlock(GregTech_API.sBlockCasings8, 0))
        .addElement('B', ofBlock(GregTech_API.sBlockCasings8, 1))
        .addElement('C', ofBlock(GregTech_API.sBlockCasings8, 7))
        .addElement('D', ofBlockAnyMeta(GregTech_API.sBlockTintedGlass, 0))
        .addElement('E', ofFrame(Materials.Longasssuperconductornameforuvwire))
        .addElement('F', ofFrame(Materials.Carbon))
        .build();

    public GT_MetaTileEntity_PurificationUnitDegasifier(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected GT_MetaTileEntity_PurificationUnitDegasifier(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_PurificationUnitDegasifier(mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            if (active) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_MAIN),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_MAIN),
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
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_MAIN) };
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
    public IStructureDefinition<GT_MetaTileEntity_PurificationUnitDegasifier> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Purification Unit")
            .addSeparator()
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
        return 7;
    }

    @Override
    public long getActivePowerUsage() {
        return TierEU.RECIPE_UHV;
    }
}
