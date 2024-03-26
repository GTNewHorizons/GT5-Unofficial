package gregtech.common.tileentities.machines.multiblock;

import static gregtech.api.multitileentity.enums.PartMode.ITEM_INPUT;
import static gregtech.api.multitileentity.enums.PartMode.ITEM_OUTPUT;
import static gregtech.api.util.GT_StructureUtilityMuTE.ofMuTECasings;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import gregtech.api.enums.GT_Values;
import gregtech.api.multitileentity.enums.GT_MultiTileCasing;
import gregtech.api.multitileentity.multiblock.base.Controller;
import gregtech.api.task.tasks.PollutionTask;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.common.tileentities.machines.multiblock.logic.CokeOvenProcessingLogic;

public class CokeOven extends Controller<CokeOven, CokeOvenProcessingLogic> {

    private static IStructureDefinition<CokeOven> STRUCTURE_DEFINITION = null;
    private static final Vec3Impl OFFSET = new Vec3Impl(1, 1, 0);
    private static final String MAIN = "Main";
    private static final int POLLUTION_AMOUNT = 10;

    public CokeOven() {
        super();
        setElectric(false);
        new PollutionTask<>(this).setPollutionPerSecond(POLLUTION_AMOUNT);
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        buildState.startBuilding(getStartingStructureOffset());
        buildPiece(MAIN, trigger, hintsOnly, buildState.stopBuilding());
    }

    @Override
    public boolean checkMachine() {
        buildState.startBuilding(getStartingStructureOffset());
        return checkPiece(MAIN, buildState.stopBuilding());
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        buildState.startBuilding(getStartingStructureOffset());
        return survivalBuildPiece(MAIN, trigger, buildState.stopBuilding(), elementBudget, env, false);
    }

    @Override
    public short getCasingRegistryID() {
        return 0;
    }

    @Override
    public int getCasingMeta() {
        return GT_MultiTileCasing.CokeOven.getId();
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Coke Oven")
            .addInfo("Used for charcoal")
            .beginStructureBlock(3, 3, 3, true)
            .addCasingInfoExactly("Coke Oven Bricks", 25, false)
            .addPollutionAmount(POLLUTION_AMOUNT)
            .toolTipFinisher(GT_Values.AuthorBlueWeabo);
        return tt;
    }

    @Override
    public Vec3Impl getStartingStructureOffset() {
        return OFFSET;
    }

    @Override
    public IStructureDefinition<CokeOven> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<CokeOven>builder()
                .addShape(
                    MAIN,
                    new String[][] { { "AAA", "A~A", "AAA" }, { "AAA", "A-A", "AAA" }, { "AAA", "AAA", "AAA" } })
                .addElement(
                    'A',
                    ofMuTECasings(
                        ITEM_INPUT.getValue() | ITEM_OUTPUT.getValue(),
                        GT_MultiTileCasing.CokeOven.getCasing()))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean hasFluidInput() {
        return false;
    }

    public String getLocalName() {
        return StatCollector.translateToLocal("gt.multiBlock.controller.cokeOven");
    }

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.cokeOven";
    }

    @Override
    @Nonnull
    protected CokeOvenProcessingLogic createProcessingLogic() {
        return new CokeOvenProcessingLogic();
    }
}
