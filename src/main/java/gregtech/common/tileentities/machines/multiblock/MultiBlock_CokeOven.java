package gregtech.common.tileentities.machines.multiblock;

import static gregtech.api.multitileentity.multiblock.base.MultiBlockPart.NOTHING;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import gregtech.api.multitileentity.enums.GT_MultiTileCasing;
import gregtech.api.multitileentity.enums.GT_MultiTileRegistries;
import gregtech.api.multitileentity.multiblock.base.MultiBlockController;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OreDictUnificator;

public class MultiBlock_CokeOven extends MultiBlockController<MultiBlock_CokeOven> {

    private static IStructureDefinition<MultiBlock_CokeOven> STRUCTURE_DEFINITION = null;
    private static final int NORMAL_RECIPE_TIME = 1800;
    private static final int WOOD_ORE_ID = OreDictionary.getOreID("logWood");
    private static final int COAL_ORE_ID = OreDictionary.getOreID("coal");
    private static final int COAL_BLOCK_ORE_ID = OreDictionary.getOreID("blockCoal");
    private static final int SUGARCANE_ORE_ID = OreDictionary.getOreID("sugarcane");
    private static final int CACTUS_ORE_ID = OreDictionary.getOreID("blockCactus");
    private static final int CACTUS_CHARCOAL_ORE_ID = OreDictionary.getOreID("itemCharcoalCactus");
    private static final int SUGAR_CHARCOAL_ORE_ID = OreDictionary.getOreID("itemCharcoalSugar");
    private static final Vec3Impl offset = new Vec3Impl(1, 1, 0);
    private static final String MAIN = "Main";
    private int timeMultiplier = 1;

    public MultiBlock_CokeOven() {
        super();
        setElectric(false);
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
        return GT_MultiTileRegistries.CASING_REGISTRY_ID;
    }

    @Override
    public short getCasingMeta() {
        return GT_MultiTileCasing.CokeOven.getId();
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Coke Oven").addInfo("Used for charcoal").beginStructureBlock(3, 3, 3, true)
                .addCasingInfoExactly("Coke Oven Bricks", 25, false).toolTipFinisher("BlueWeabo");
        return tt;
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
                    .addElement('A', addMultiTileCasing("gt.multitileentity.casings", getCasingMeta(), NOTHING))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected boolean checkRecipe() {
        timeMultiplier = 1;
        ItemStack[] inputs = getAllItemInputs();
        if (inputs == null || inputs[0] == null) {
            return false;
        }
        ItemStack input = inputs[0];
        int originalStackSize = input.stackSize;
        if (startRecipe(input)) {
            setDuration(NORMAL_RECIPE_TIME * timeMultiplier);
        }

        return originalStackSize > input.stackSize;
    }

    protected boolean startRecipe(ItemStack input) {
        for (int oreId : OreDictionary.getOreIDs(input)) {
            if (oreId == COAL_ORE_ID) {
                input.stackSize -= 1;
                setItemOutputs(GT_OreDictUnificator.get("fuelCoke", null, 1));
                return true;
            } else if (oreId == COAL_BLOCK_ORE_ID) {
                timeMultiplier = 9;
                input.stackSize -= 1;
                setItemOutputs(GT_ModHandler.getModItem("Railcraft", "cube", 1, 0));
                return true;
            } else if (oreId == WOOD_ORE_ID) {
                input.stackSize -= 1;
                setItemOutputs(new ItemStack(Items.coal, 1, 1));
                return true;
            } else if (oreId == SUGARCANE_ORE_ID) {
                input.stackSize -= 1;
                setItemOutputs(GT_OreDictUnificator.get("itemCharcoalSugar", null, 1));
                return true;
            } else if (oreId == SUGAR_CHARCOAL_ORE_ID) {
                input.stackSize -= 1;
                setItemOutputs(GT_OreDictUnificator.get("itemCokeSugar", null, 1));
                return true;
            } else if (oreId == CACTUS_ORE_ID) {
                input.stackSize -= 1;
                setItemOutputs(GT_OreDictUnificator.get("itemCharcoalCactus", null, 1));
                return true;
            } else if (oreId == CACTUS_CHARCOAL_ORE_ID) {
                input.stackSize -= 1;
                setItemOutputs(GT_OreDictUnificator.get("itemCokeCactus", null, 1));
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean hasFluidInput() {
        return false;
    }
}
