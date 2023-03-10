package gregtech.common.tileentities.machines.multiblock;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static gregtech.api.multitileentity.multiblock.base.MultiBlockPart.NOTHING;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import gregtech.api.multitileentity.enums.GT_MultiTileCasing;
import gregtech.api.multitileentity.enums.GT_MultiTileRegistries;
import gregtech.api.multitileentity.multiblock.base.MultiBlockController;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;

public class MultiBlock_CokeOven extends MultiBlockController<MultiBlock_CokeOven> {

    private static IStructureDefinition<MultiBlock_CokeOven> STRUCTURE_DEFINITION = null;
    private static final Vec3Impl offset = new Vec3Impl(1, 1, 0);
    private static final String MAIN = "Main";

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        buildState.startBuilding(getStartingStructureOffset());
        buildPiece(MAIN, trigger, hintsOnly, buildState.stopBuilding());
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        buildState.startBuilding(getStartingStructureOffset());
        return survivalBuildPiece(MAIN, trigger, buildState.stopBuilding(), elementBudget, env, false);
    }

    @Override
    public short getCasingRegistryID() {
        return GT_MultiTileRegistries.CASING_REGISTRY_ID;
    }

    @Override
    public short getCasingMeta() {
        return GT_MultiTileCasing.CokeOven.getId();
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tooltip = new GT_Multiblock_Tooltip_Builder();
        tooltip.addMachineType("Coke Oven").addInfo("Used for charcoal").beginStructureBlock(3, 3, 3, true).addCasingInfoExactly("Coke Oven Bricks", 25, false);
        return tooltip;
    }

    @Override
    public Vec3Impl getStartingStructureOffset() {
        return offset;
    }

    @Override
    public IStructureDefinition<MultiBlock_CokeOven> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MultiBlock_CokeOven>builder().addShape(
                    MAIN,
                    new String[][] { { "AAA", "A~A", "AAA" }, { "AAA", "A-A", "AAA" }, { "AAA", "AAA", "AAA" } })
                    .addElement('A', ofChain(addMultiTileCasing(getCasingRegistryID(), getCasingMeta(), NOTHING)))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

}
