package gregtech.common.tileentities.machines.multi.fuelboilers;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.GregTech_API.*;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.util.GT_StructureUtility.*;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import gregtech.api.enums.Materials;
import gregtech.api.util.GT_StructureUtility;

public class FBBronze extends FueledBoiler<FBBronze> implements ISurvivalConstructable {

    private int tierWall = -1;
    private int tierPipe = -1;
    private int tierHotplate = -1;
    private int tierFirebox = -1;

    // There's only one piece to this structure... for now >:)
    // TODO: multiple boiler chambers + superheater
    private static final String MAIN_PIECE_NAME = "main";
    private static final int X_OFFSET = 1;
    private static final int Y_OFFSET = 1;
    private static final int Z_OFFSET = 2;
    private static String[][] structure =
        // spotless:off
        new String[][] {
            { "     F   F   ", "     F   F   ", "     BBBBB   ", "     BBBBB   ", "     BBBBB   ", "             ", "             " },
            { "             ", "    BBBBBBB  ", "    BH----B  ", "    BH----B  ", "    BH----B  ", "    BBBBBBB  ", "             " },
            { "FFF  BBBBB   ", " ~  BH----B  ", "   T-PPPPPBBB", "   T-H----BBB", "   T-PPPPPBBB", "    BH----B  ", "     BBBBB   " },
            { "FFF  BBBBB   ", "EP  BH----B  ", " P T-H----BBB", " PPP-H----BBW", "   T-H----BBM", "    BH----B  ", "     BBSBB   " },
            { " F   BBBBB   ", "    BH----B  ", "   T-PPPPPBBB", "   T-H----BBB", "   T-PPPPPBBB", "    BH----B  ", "     BBBBB   " },
            { "             ", "    BBBBBBB  ", "    BH----B  ", "    BH----B  ", "    BH----B  ", "    BBBBBBB  ", "             " },
            { "     F   F   ", "     F   F   ", "     BBBBB   ", "     BBBBB   ", "     BBBBB   ", "             ", "             " } };
    // spotless:on

    private static final IStructureDefinition<FBBronze> STRUCTURE_DEFINITION = StructureDefinition.<FBBronze>builder()
        .addShape(MAIN_PIECE_NAME, structure)
        // IO
        // Fuel in
        .addElement(
            'E',
            GT_StructureUtility.<FBBronze>buildHatchAdder()
                .atLeastList(ImmutableList.of(InputHatch))
                .build())
        // Water in
        .addElement(
            'W',
            GT_StructureUtility.<FBBronze>buildHatchAdder()
                .atLeastList(ImmutableList.of(InputHatch))
                .build())
        // Pollution out
        .addElement(
            'M',
            GT_StructureUtility.<FBBronze>buildHatchAdder()
                .atLeastList(ImmutableList.of(Muffler))
                .build())
        // Steam out
        .addElement(
            'S',
            GT_StructureUtility.<FBBronze>buildHatchAdder()
                .atLeastList(ImmutableList.of(OutputHatch))
                .build())
        // Building blocks
        // Invar frame
        .addElement('F', ofFrame(Materials.Invar))
        // Bronze plated bricks
        // TODO: OR glass
        .addElement(
            'B',
            StructureUtility.ofBlocksTiered(
                FueledBoiler::getTierCasing,
                ImmutableList.of(Pair.of(sBlockCasings1, 10), Pair.of(sBlockCasings2, 0)),
                -1,
                (t, m) -> t.tierWall = m,
                t -> t.tierWall))
        // Pipe casing
        .addElement(
            'P',
            StructureUtility.ofBlocksTiered(
                FueledBoiler::getTierPipe,
                ImmutableList.of(Pair.of(sBlockCasings2, 12), Pair.of(sBlockCasings2, 13)),
                -1,
                (t, m) -> t.tierPipe = m,
                t -> t.tierPipe))
        // Hotplate - every other tier
        .addElement(
            'H',
            StructureUtility.ofBlocksTiered(
                FueledBoiler::getTierHotplate,
                ImmutableList.of(Pair.of(sBlockMetal1, 3)),
                -1,
                (t, m) -> t.tierHotplate = m,
                t -> t.tierHotplate))
        // Firebox
        .addElement(
            'T',
            StructureUtility.ofBlocksTiered(
                FueledBoiler::getTierFirebox,
                ImmutableList.of(Pair.of(sBlockCasings3, 13), Pair.of(sBlockCasings3, 14)),
                -1,
                (t, m) -> t.tierFirebox = m,
                t -> t.tierFirebox))
        .build();

    protected FBBronze(int id, String name, String localizedName) {
        super(id, name, localizedName);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        // Validate structure
        return checkPiece(MAIN_PIECE_NAME, X_OFFSET, Y_OFFSET, Z_OFFSET);

        // TODO: check for glass amount
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(
                MAIN_PIECE_NAME,
                stackSize,
                hintsOnly,
                X_OFFSET,
                Y_OFFSET,
                Z_OFFSET
        );
    }

    @Override
    public IStructureDefinition<FBBronze> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Boiler")
                .addInfo("Controller block for the Burner Boiler")
                .addInfo("Burns fuels to generate steam efficiently")
                .addInfo("Each tier allows higher heat and 4X throughput");
        return null;
    }


    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return null;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing, int colorIndex, boolean active, boolean redstoneLevel) {
        return new ITexture[0];
    }
}
