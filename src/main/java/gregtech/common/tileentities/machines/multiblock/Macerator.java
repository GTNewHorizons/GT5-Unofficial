package gregtech.common.tileentities.machines.multiblock;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.multitileentity.enums.PartMode.ENERGY_INPUT;
import static gregtech.api.multitileentity.enums.PartMode.FLUID_INPUT;
import static gregtech.api.multitileentity.enums.PartMode.FLUID_OUTPUT;
import static gregtech.api.multitileentity.enums.PartMode.ITEM_INPUT;
import static gregtech.api.multitileentity.enums.PartMode.ITEM_OUTPUT;
import static gregtech.api.multitileentity.enums.PartMode.NOTHING;
import static gregtech.api.util.GT_StructureUtilityMuTE.*;

import javax.annotation.Nonnull;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import gregtech.api.multitileentity.enums.GT_MultiTileCasing;
import gregtech.api.multitileentity.multiblock.base.StackableController;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.common.tileentities.machines.multiblock.logic.MaceratorProcessingLogic;

public class Macerator extends StackableController<Macerator, MaceratorProcessingLogic> {

    private static IStructureDefinition<Macerator> STRUCTURE_DEFINITION = null;

    public Macerator() {
        super();
    }

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.macerator";
    }

    @Override
    public IStructureDefinition<Macerator> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<Macerator>builder()
                .addShape(
                    STACKABLE_STOP,
                    transpose(new String[][] { { " CCC ", "CCCCC", "CCCCC", "CCCCC", " CCC " }, }))
                .addShape(
                    STACKABLE_MIDDLE,
                    transpose(new String[][] { { "  BBB  ", " B---B ", "DC---CD", " B---B ", "  BBB  " }, }))
                .addShape(
                    STACKABLE_START,
                    transpose(new String[][] { { " G~F ", "AAAAA", "AAAAA", "AAAAA", " AAA " }, }))
                .addElement('A', ofMuTECasings(ENERGY_INPUT.getValue(), GT_MultiTileCasing.Macerator.getCasing()))
                .addElement(
                    'B',
                    ofMuTECasings(
                        FLUID_INPUT.getValue() | ITEM_INPUT.getValue()
                            | FLUID_OUTPUT.getValue()
                            | ITEM_OUTPUT.getValue(),
                        GT_MultiTileCasing.Macerator.getCasing()))
                .addElement('C', ofMuTECasings(NOTHING.getValue(), GT_MultiTileCasing.Macerator.getCasing()))
                .addElement('D', ofMuTECasings(NOTHING.getValue(), GT_MultiTileCasing.Macerator.getCasing()))
                .addElement('F', ofMuTECasings(NOTHING.getValue(), MOTOR_CASINGS))
                .addElement('G', ofMuTECasings(NOTHING.getValue(), INVENTORY_CASINGS))
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
        return 18000;
    }

    @Override
    public boolean hasTop() {
        return true;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Macerator")
            .addInfo("Controller for the Macerator")
            .addSeparator()
            .beginVariableStructureBlock(7, 9, 2 + getMinStacks(), 2 + getMaxStacks(), 7, 9, true)
            .addController("Bottom Front Center")
            .addCasingInfoExactly("Test Casing", 60, false)
            .addEnergyHatch("Any bottom layer casing")
            .addInputHatch("Any non-optional external facing casing on the stacks")
            .addInputBus("Any non-optional external facing casing on the stacks")
            .addOutputHatch("Any non-optional external facing casing on the stacks")
            .addOutputBus("Any non-optional external facing casing on the stacks")
            .addStructureInfo(
                String.format("Stackable middle stacks between %d-%d time(s).", getMinStacks(), getMaxStacks()))
            .toolTipFinisher("Wildcard");
        return tt;
    }

    @Override
    public int getMinStacks() {
        return 1;
    }

    @Override
    public int getMaxStacks() {
        return 10;
    }

    @Override
    public Vec3Impl getStartingStructureOffset() {
        return new Vec3Impl(2, 0, 0);
    }

    @Override
    public Vec3Impl getStartingStackOffset() {
        return new Vec3Impl(1, 1, 0);
    }

    @Override
    public Vec3Impl getPerStackOffset() {
        return new Vec3Impl(0, 1, 0);
    }

    @Override
    public Vec3Impl getAfterLastStackOffset() {
        return new Vec3Impl(-1, 0, 0);
    }

    @Override
    @Nonnull
    protected MaceratorProcessingLogic createProcessingLogic() {
        return new MaceratorProcessingLogic();
    }
}
