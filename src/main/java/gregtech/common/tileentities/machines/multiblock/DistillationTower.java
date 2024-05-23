package gregtech.common.tileentities.machines.multiblock;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.multitileentity.enums.PartMode.ENERGY_INPUT;
import static gregtech.api.multitileentity.enums.PartMode.FLUID_INPUT;
import static gregtech.api.multitileentity.enums.PartMode.FLUID_OUTPUT;
import static gregtech.api.multitileentity.enums.PartMode.ITEM_INPUT;
import static gregtech.api.multitileentity.enums.PartMode.ITEM_OUTPUT;
import static gregtech.api.multitileentity.enums.PartMode.NOTHING;
import static gregtech.api.util.GT_StructureUtilityMuTE.MOTOR_CASINGS;
import static gregtech.api.util.GT_StructureUtilityMuTE.ofMuTECasings;

import javax.annotation.Nonnull;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.multitileentity.enums.GT_MultiTileCasing;
import gregtech.api.multitileentity.multiblock.base.StackableController;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_StructureUtility;
import gregtech.common.tileentities.machines.multiblock.logic.DistillationTowerProcessingLogic;

public class DistillationTower extends StackableController<DistillationTower, DistillationTowerProcessingLogic> {

    private static IStructureDefinition<DistillationTower> STRUCTURE_DEFINITION_MEGA = null;
    private static final Vec3Impl STRUCTURE_OFFSET_MEGA = new Vec3Impl(8, 3, 0);
    private static final Vec3Impl STRUCTURE_OFFSET_MEGA_START = new Vec3Impl(0, 3, 0);
    private static final Vec3Impl STRUCTURE_OFFSET_MEGA_STOP = new Vec3Impl(0, 5, 0);
    private static final Vec3Impl STRUCTURE_OFFSET_MEGA_STACK = new Vec3Impl(0, 3, 0);
    private static final String STACKABLE_MIDDLE_1 = "STACKABLE_MIDDLE_1";
    private static final String STACKABLE_MIDDLE_2 = "STACKABLE_MIDDLE_2";
    private boolean isMega = true;

    @Override
    public short getCasingRegistryID() {
        return 0;
    }

    @Override
    public int getCasingMeta() {
        return GT_MultiTileCasing.Distillation.getId();
    }

    @Override
    public GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Distillation Tower")
            .addInfo("Controller block for the Distillation Tower")
            .addInfo("Can be specialised to be a mega structure")
            .addInfo(GT_Values.Authorminecraft7771)
            .addSeparator()
            .beginStructureBlock(5, 3, 3, false)
            .addController("Front center")
            .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public Vec3Impl getStartingStructureOffset() {
        return STRUCTURE_OFFSET_MEGA;
    }

    @Override
    public IStructureDefinition<DistillationTower> getStructureDefinition() {
        if (STRUCTURE_DEFINITION_MEGA == null) {
            STRUCTURE_DEFINITION_MEGA = StructureDefinition.<DistillationTower>builder()
                .addShape(
                    STACKABLE_START,
                    transpose(
                        // spotless:off
                        new String[][]{
                            {"                   ","                   ","                   ","                   ","                   ","    ECCCCCE        ","    CCCCCCC        ","   C       C       "," EC         CE     "," CC         CC     "," CC         CC ABA "," CC         CC B B "," CC         CC ABA "," CC         CC     "," EC         CE     ","   C       C       ","    CCCCCCC        ","    ECCCCCE        ","                   "},
                            {"                   ","      EEE          ","       E           ","       E           ","       E           ","    E  E  E        ","    CCCCCCC        ","   C       C       "," EC         CE     ","  C         C      ","  C         C  ABA ","  C         C  B B ","  C         C  ABA ","  C         C      "," EC         CE     ","   C       C       ","    CCCCCCC        ","    E     E        ","                   "},
                            {"     CCCCC         ","     CCCCC         ","     CCCCC         ","                   ","                   ","    E     E        ","    CCCCCCC        ","   C       C       "," EC         CE     ","  C         C      ","  C         C  ABA ","  C         C  B B ","  C         C  ABA ","  C         C      "," EC         CE     ","   C       C       ","    CCCCCCC        ","    E     E        ","                   "},
                            {"     CXC~C         ","     BDDDB         ","     CBCBC         ","      B B          ","      B B          ","    E B B E        ","    CCCCCCC        ","   C       C       "," EC         CE     ","  C         C      ","  C         C  AAA ","  C         C  A A ","  C         C  AAA ","  C         C      "," EC         CE     ","   C       C       ","    CCCCCCC        ","    E     E        ","                   "},
                            {"     CCCCC         ","     CCCCC         ","     CCCCC         ","                   ","  CCCCCCCCCCC      ","  CAAAAAAAAAC      ","CCCACCCCCCCACCC    ","CAACAAAAAAACAAC    ","CACAADDDDDAACAC    ","CACADDDDDDDACACCCCC","CACADDAAADDACACCCCC","CACADDAAADDACACCCCC","CACADDAAADDACACCCCC","CACADDDDDDDACACCCCC","CACAADDDDDAACAC    ","CAACAAAAAAACAAC    ","CCCACCCCCCCACCC    ","  CAAAAAAAAAC      ","  CCCCCCCCCCC      "}
                        }))
                .addShape(
                    STACKABLE_STOP,
                    transpose(
                        new String[][]{
                            {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","      CCC          ","      CCC          ","      CCC          ","                   ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","    E     E        ","    E     E        ","    E     E        "," EEEEAAAAAEEEE     ","    AAAAAAA        ","    AA   AA        ","    AA   AA        ","    AA   AA        ","    AAAAAAA        "," EEEEAAAAAEEEE     ","    E     E        ","    E     E        ","    E     E        ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","    ECCCCCE        ","   CCCCCCCCC       ","  CCCCCCCCCCC      "," ECCC     CCCE     "," CCC       CCC     "," CCC       CCC     "," CCC       CCC     "," CCC       CCC     "," CCC       CCC     "," ECCC     CCCE     ","  CCCCCCCCCCC      ","   CCCCCCCCC       ","    ECCCCCE        ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","    E     E        ","    CCCCCCC        ","   C       C       "," EC         CE     ","  C         C      ","  C         C      ","  C         C      ","  C         C      ","  C         C      "," EC         CE     ","   C       C       ","    CCCCCCC        ","    E     E        ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","    E     E        ","    CCCCCCC        ","   C       C       "," EC         CE     ","  C         C      ","  C         CAAAAA ","  C         CABBBA ","  C         CAAAAA ","  C         C      "," EC         CE     ","   C       C       ","    CCCCCCC        ","    E     E        ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","    ECCCCCE        ","    CCCCCCC        ","   C       C       "," EC         CE     "," CC         CC     "," CC         CABBBA "," CC         C    B "," CC         CABBBA "," CC         CC     "," EC         CE     ","   C       C       ","    CCCCCCC        ","    ECCCCCE        ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","    E     E        ","    CCCCCCC        ","   C       C       "," EC         CE     ","  C         C      ","  C         CAAABA ","  C         CAAA B ","  C         CAAABA ","  C         C      "," EC         CE     ","   C       C       ","    CCCCCCC        ","    E     E        ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","    E     E        ","    CCCCCCC        ","   C       C       "," EC         CE     ","  C         C      ","  C         C  ABA ","  C         C  A B ","  C         C  ABA ","  C         C      "," EC         CE     ","   C       C       ","    CCCCCCC        ","    E     E        ","                   "},
                        }))
                .addShape(
                    STACKABLE_MIDDLE_1,
                    transpose(
                        new String[][]{
                            {"                   ","                   ","                   ","                   ","                   ","    E     E        ","    CCCCCCC        ","   C       C       "," EC         CE     ","  C         C      ","  C         C  ABA ","  C         C  B B ","  C         C  ABA ","  C         C      "," EC         CE     ","   C       C       ","    CCCCCCC        ","    E     E        ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","    E     E        ","    CCCCCCC        ","   C       C       "," EC         CE     ","  C         C      ","  C         C  ABA ","  C         C  B B ","  C         C  ABA ","  C         C      "," EC         CE     ","   C       C       ","    CCCCCCC        ","    E     E        ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","    E     E        ","    CCCCCCC        ","   C       C       "," EC         CE     ","  C         C      ","  C         C  ABA ","  C         C  B B ","  C         C  ABA ","  C         C      "," EC         CE     ","   C       C       ","    CCCCCCC        ","    E     E        ","                   "},
                        }))
                .addShape(
                    STACKABLE_MIDDLE_2,
                    transpose(
                        new String[][]{
                            {"                   ","                   ","                   ","                   ","                   ","    ECCCCCE        ","    CCCCCCC        ","   C       C       "," EC         CE     "," CC         CC     "," CC         CC ABA "," CC         CC B B "," CC         CC ABA "," CC         CC     "," EC         CE     ","   C       C       ","    CCCCCCC        ","    ECCCCCE        ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","    E     E        ","    CCCCCCC        ","   C       C       "," EC         CE     ","  C         C      ","  C         C  ABA ","  C         C  B B ","  C         C  ABA ","  C         C      "," EC         CE     ","   C       C       ","    CCCCCCC        ","    E     E        ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","    E     E        ","    CCCCCCC        ","   C       C       "," EC         CE     ","  C         C      ","  C         C  ABA ","  C         C  B B ","  C         C  ABA ","  C         C      "," EC         CE     ","   C       C       ","    CCCCCCC        ","    E     E        ","                   "},
                        }))
                        // spotless:on
                .addElement(
                    'C',
                    ofMuTECasings(
                        FLUID_INPUT.getValue() | ITEM_INPUT.getValue()
                            | FLUID_OUTPUT.getValue()
                            | ITEM_OUTPUT.getValue()
                            | ENERGY_INPUT.getValue(),
                        GT_MultiTileCasing.Distillation.getCasing()))
                .addElement('E', GT_StructureUtility.ofFrame(Materials.StainlessSteel))
                .addElement('A', ofBlock(GregTech_API.sBlockCasings2, 0))
                .addElement('B', ofBlock(GregTech_API.sBlockCasings2, 13))
                .addElement('X', ofMuTECasings(NOTHING.getValue(), MOTOR_CASINGS))
                .addElement('D', GT_StructureUtility.ofCoil((tile, meta) -> {}, (tile) -> HeatingCoilLevel.None))
                .build();
        }
        return STRUCTURE_DEFINITION_MEGA;
    }

    @Override
    public int getFluidOutputCount() {
        return 12;
    }

    @Override
    public int getMinStacks() {
        return 0;
    }

    @Override
    public int getMaxStacks() {
        return 9;
    }

    @Override
    public Vec3Impl getStartingStackOffset() {
        return STRUCTURE_OFFSET_MEGA_START;
    }

    @Override
    public Vec3Impl getPerStackOffset() {
        return STRUCTURE_OFFSET_MEGA_STACK;
    }

    @Override
    public Vec3Impl getAfterLastStackOffset() {
        return STRUCTURE_OFFSET_MEGA_STOP;
    }

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.distillationtower";
    }

    public String getLocalName() {
        return "Distillation Tower";
    }

    @Override
    protected String getStackableMiddle(int stackIndex) {
        return stackIndex % 2 == 0 ? STACKABLE_MIDDLE_1 : STACKABLE_MIDDLE_2;
    }

    @Override
    @Nonnull
    protected DistillationTowerProcessingLogic createProcessingLogic() {
        return new DistillationTowerProcessingLogic();
    }
}
