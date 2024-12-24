package gregtech.common.tileentities.machines.multiblock.coke_oven;

import net.minecraft.item.ItemStack;

import com.badlogic.ashley.core.Entity;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import gregtech.api.GregTechAPI;
import gregtech.api.multitileentity.StructureHandler;

public class CokeOvenStructureHandler extends StructureHandler {

    public static final IStructureDefinition<Entity> STRUCTURE = StructureDefinition.<Entity>builder()
        .addShape("main", new String[][] { { "AAA", "A~A", "AAA" }, { "AAA", "A-A", "AAA" }, { "AAA", "AAA", "AAA" } })
        .addElement('A', StructureUtility.ofBlock(GregTechAPI.sBlockCasings8, 1))
        .build();

    public CokeOvenStructureHandler(Entity entity) {
        super(entity);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        construct(stackSize, "main", 1, 1, 0, hintsOnly);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivalConstruct(stackSize, "main", 1, 1, 0, elementBudget, env);
    }

    @Override
    public IStructureDefinition<Entity> getStructureDefinition() {
        return STRUCTURE;
    }
}
