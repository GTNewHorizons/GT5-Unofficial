package gregtech.common.tileentities.machines.multiblock;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_Values.E;
import static gregtech.api.multitileentity.multiblock.base.MultiBlockPart.*;
import static gregtech.api.multitileentity.multiblock.base.MultiBlockPart.ENERGY_IN;
import static gregtech.api.multitileentity.multiblock.base.MultiBlockPart.FLUID_OUT;
import static gregtech.loaders.preload.GT_Loader_MultiTileEntities.UPGRADE_CASING_REGISTRY_NAME;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import gregtech.api.enums.GT_Values;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.logic.interfaces.ProcessingLogicHost;
import gregtech.api.multitileentity.enums.GT_MultiTileCasing;
import gregtech.api.multitileentity.enums.GT_MultiTileUpgradeCasing;
import gregtech.api.multitileentity.multiblock.base.ComplexParallelController;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.common.tileentities.machines.multiblock.logic.GenericProcessingLogic;

public class LaserEngraver extends ComplexParallelController<LaserEngraver> implements ProcessingLogicHost {

    private static IStructureDefinition<LaserEngraver> STRUCTURE_DEFINITION = null;
    private static final String MAIN = "Main";
    private static final Vec3Impl OFFSET = new Vec3Impl(1, 1, 0);
    private final ProcessingLogic PROCESSING_LOGIC = new GenericProcessingLogic(
        GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes);
    protected static final String STRUCTURE_PIECE_T1 = "T1";
    protected static final String STRUCTURE_PIECE_T2 = "T2";
    protected static final String STRUCTURE_PIECE_T3 = "T3";
    protected static final String STRUCTURE_PIECE_T4 = "T4";
    protected static final String STRUCTURE_PIECE_T5_6 = "T5_6";
    protected static final Vec3Impl STRUCTURE_OFFSET_T1 = new Vec3Impl(3, 1, 0);
    protected static final Vec3Impl STRUCTURE_OFFSET_T2 = new Vec3Impl(1, 4, -3);
    protected static final Vec3Impl STRUCTURE_OFFSET_T3 = new Vec3Impl(8, 0, 5);
    protected static final Vec3Impl STRUCTURE_OFFSET_T4 = new Vec3Impl(-14, 0, 0);
    protected static final Vec3Impl STRUCTURE_OFFSET_T5 = new Vec3Impl(14, 0, -6);
    protected static final Vec3Impl STRUCTURE_OFFSET_T6 = new Vec3Impl(-16, 0, 0);

    public LaserEngraver() {
        super();
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        buildState.startBuilding(getStartingStructureOffset());
        buildPiece(MAIN, trigger, hintsOnly, buildState.stopBuilding());
    }

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.laserengraver";
    }

    @Override
    public boolean checkMachine() {
        buildState.startBuilding(getStartingStructureOffset());
        if (!checkPiece(STRUCTURE_PIECE_T1, buildState.getCurrentOffset())) return buildState.failBuilding();
        if (maxComplexParallels > 1) {
            buildState.addOffset(STRUCTURE_OFFSET_T2);
            if (!checkPiece(STRUCTURE_PIECE_T2, buildState.getCurrentOffset())) return buildState.failBuilding();
        }
        if (maxComplexParallels > 2) {
            buildState.addOffset(STRUCTURE_OFFSET_T3);
            if (!checkPiece(STRUCTURE_PIECE_T3, buildState.getCurrentOffset())) return buildState.failBuilding();
        }
        if (maxComplexParallels > 3) {
            buildState.addOffset(STRUCTURE_OFFSET_T4);
            if (!checkPiece(STRUCTURE_PIECE_T4, buildState.getCurrentOffset())) return buildState.failBuilding();
        }
        if (maxComplexParallels > 4) {
            buildState.addOffset(STRUCTURE_OFFSET_T5);
            if (!checkPiece(STRUCTURE_PIECE_T5_6, buildState.getCurrentOffset())) return buildState.failBuilding();
        }
        if (maxComplexParallels > 5) {
            buildState.addOffset(STRUCTURE_OFFSET_T6);
            if (!checkPiece(STRUCTURE_PIECE_T5_6, buildState.getCurrentOffset())) return buildState.failBuilding();
        }
        buildState.stopBuilding();
        return super.checkMachine();
    }

    @Override
    public IStructureDefinition<LaserEngraver> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<LaserEngraver>builder()
                .addShape(
                    STRUCTURE_PIECE_T1,
                    transpose(
                        new String[][] { { "ADECA", "AAAAA", "AAAAA" }, { "AAA~A", "FAAAA", "AAAAA" },
                            { "AAAAA", "AAAAA", "AAAAA" } }))
                .addShape(
                    STRUCTURE_PIECE_T2,
                    new String[][] { { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "} })
                .addShape(
                    STRUCTURE_PIECE_T3,
                    new String[][] { { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "} })
                .addShape(
                    STRUCTURE_PIECE_T4,
                    new String[][] { { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "} })
                .addShape(
                    STRUCTURE_PIECE_T5_6,
                    new String[][] { { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "},
                        { "       ", "       ", "       ", "       ", "       "} })
                            .addElement(
                                'A',
                                ofChain(
                                    addMultiTileCasing(
                                        "gt.multitileentity.casings",
                                        getCasingMeta(),
                                        ENERGY_IN | FLUID_IN | ITEM_IN | FLUID_OUT | ITEM_OUT)))
                            .addElement(
                                'B',
                                ofChain(
                                    addMultiTileCasing(
                                        "gt.multitileentity.casings",
                                        getCasingMeta(),
                                        FLUID_IN | ITEM_IN | FLUID_OUT | ITEM_OUT)))
                            .addElement(
                                'C',
                                ofChain(
                                    addMultiTileCasing(
                                        UPGRADE_CASING_REGISTRY_NAME,
                                        GT_MultiTileUpgradeCasing.Cleanroom.getId(),
                                        NOTHING),
                                    addMultiTileCasing(
                                        "gt.multitileentity.casings",
                                        getCasingMeta(),
                                        FLUID_IN | ITEM_IN | FLUID_OUT | ITEM_OUT | ENERGY_IN)))
                            .addElement(
                                'D',
                                ofChain(
                                    addMultiTileCasing(
                                        UPGRADE_CASING_REGISTRY_NAME,
                                        GT_MultiTileUpgradeCasing.Wireless.getId(),
                                        NOTHING),
                                    addMultiTileCasing(
                                        "gt.multitileentity.casings",
                                        getCasingMeta(),
                                        FLUID_IN | ITEM_IN | FLUID_OUT | ITEM_OUT | ENERGY_IN)))
                            .addElement('E', addMotorCasings(NOTHING))
                .addElement(
                    'F',
                    ofChain(addMultiTileCasing(GT_MultiTileCasing.Mirror.getId(), getCasingMeta(), NOTHING))

                )
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public short getCasingRegistryID() {
        return 0;
    }

    @Override
    public int getCasingMeta() {
        return GT_MultiTileCasing.LaserEngraver.getId();
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Laser Engraver")
            .addInfo("Used for Engraving")
            .addSeparator()
            .beginStructureBlock(3, 3, 3, true)
            .addController("Front Center")
            .addCasingInfoExactly("Laser Engraver Casing", 25, false)
            .toolTipFinisher(GT_Values.AuthorTheEpicGamer274);
        return tt;
    }

    @Override
    public Vec3Impl getStartingStructureOffset() {
        return OFFSET;
    }

    public ProcessingLogic getProcessingLogic() {
        return PROCESSING_LOGIC;
    }
}
