package gregtech.loaders.postload.postea;

import static gregtech.loaders.postload.postea.Transformers.replaceBlock;
import static gregtech.loaders.postload.postea.Transformers.replaceItem;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.materials.BlockShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.MaterialParts;
import gregtech.common.items.MetaGeneratedItem98;

final class IC2Transformers {

    private IC2Transformers() {}

    static void register() {
        registerItems();
        registerBlocks();
    }

    // spotless:off
    private static void registerItems() {
        replaceItem("IC2:itemUran238", MaterialLibAPI.getStack(Materials.Uranium, Shapes.ingot, 1));
        replaceItem("IC2:itemUran235", MaterialLibAPI.getStack(Materials.Uranium235, Shapes.ingot, 1));
        replaceItem("IC2:itemUran235small", MaterialLibAPI.getStack(Materials.Uranium235, Shapes.nugget, 1));
        replaceItem("IC2:itemPlutonium", MaterialLibAPI.getStack(Materials.Plutonium, Shapes.ingot, 1));
        replaceItem("IC2:itemPlutoniumsmall", MaterialLibAPI.getStack(Materials.Plutonium, Shapes.nugget, 1));
        replaceItem("IC2:itemFuelPlantBall", ItemList.IC2_Plantball.get(1));
        replaceItem("IC2:itemBiochaff", ItemList.IC2_BioChaff.get(1));
        replaceItem("IC2:itemDynamite", ItemList.Dynamite.get(1));

        // IC2's empty and filled cells share one id. The IC2 air cell holds 2000 mB and the MaterialLib cell 1000 mB,
        // and so does the meta-15 cell
        replaceItem("IC2:itemCellEmpty", 0, ItemList.Cell_Empty.get(1));
        replaceItem("IC2:itemCellEmpty", 1, MaterialParts.cell(Materials.Water, 1));
        replaceItem("IC2:itemCellEmpty", 2, MaterialParts.cell(Materials.Lava, 1));
        replaceItem("IC2:itemCellEmpty", 3, MaterialParts.cell(Materials.UUMatter, 1));
        replaceItem("IC2:itemCellEmpty", 4, MaterialParts.cell(Materials.ConstructionFoam, 1));
        replaceItem("IC2:itemCellEmpty", 5, MaterialParts.cell(Materials.Air, 1));
        replaceItem("IC2:itemCellEmpty", 6, MaterialParts.cell(Materials.Biomass, 1));
        replaceItem("IC2:itemCellEmpty", 7, MaterialParts.cell(Materials.Biogas, 1));
        replaceItem("IC2:itemCellEmpty", 9, MaterialParts.cell(Materials.Coolant, 1));
        replaceItem("IC2:itemCellEmpty", 10, MaterialParts.cell(Materials.HotCoolant, 1));
        replaceItem("IC2:itemCellEmpty", 11, MaterialParts.cell(Materials.PahoehoeLava, 1));
        replaceItem("IC2:itemCellEmpty", 12, MaterialParts.cell(Materials.DistilledWater, 1));
        replaceItem("IC2:itemCellEmpty", 13, MaterialParts.cell(Materials.SuperheatedSteam, 1));
        replaceItem("IC2:itemCellEmpty", 14, MetaGeneratedItem98.FluidCell.STEAM.get());
        // metas 15-20 are the cells gtPlusPlus registered on IC2's cell item
        replaceItem("IC2:itemCellEmpty", 15, MaterialParts.cell(Materials.IndustrialStrengthHydrofluoricAcid, 1));
        replaceItem("IC2:itemCellEmpty", 16, MaterialParts.cell(Materials.SulfurousAcid, 1));
        replaceItem("IC2:itemCellEmpty", 17, MaterialParts.cell(Materials.SulfuricApatiteMix, 1));
        replaceItem("IC2:itemCellEmpty", 18, MaterialParts.cell(Materials.IndustrialStrengthHydrogenChloride, 1));
        replaceItem("IC2:itemCellEmpty", 19, MaterialParts.cell(Materials.SulfuricLithiumMix, 1));
        replaceItem("IC2:itemCellEmpty", 20, MaterialParts.cell(Materials.LithiumHydroxide, 1));

        // IC2 still registers its casing item, so these run on the live id
        replaceItem("IC2:itemCasing", 0, MaterialLibAPI.getStack(Materials.Copper, Shapes.itemCasing, 1));
        replaceItem("IC2:itemCasing", 1, MaterialLibAPI.getStack(Materials.Tin, Shapes.itemCasing, 1));
        replaceItem("IC2:itemCasing", 2, MaterialLibAPI.getStack(Materials.Bronze, Shapes.itemCasing, 1));
        replaceItem("IC2:itemCasing", 3, MaterialLibAPI.getStack(Materials.Gold, Shapes.itemCasing, 1));
        replaceItem("IC2:itemCasing", 4, MaterialLibAPI.getStack(Materials.Iron, Shapes.itemCasing, 1));
        replaceItem("IC2:itemCasing", 5, MaterialLibAPI.getStack(Materials.Steel, Shapes.itemCasing, 1));
        replaceItem("IC2:itemCasing", 6, MaterialLibAPI.getStack(Materials.Lead, Shapes.itemCasing, 1));
    }
    // spotless:on

    // spotless:off
    private static void registerBlocks() {
        replaceBlock("IC2:blockAlloyGlass", GregTechAPI.sBlockGlass1, 10);
        replaceBlock("IC2:blockRubber", ItemList.PadBouncy.getBlock(), 0);
        replaceBlock("IC2:blockAlloy", ItemList.Block_ReinforcedConcrete.getBlock(), 13);
        replaceBlock("IC2:blockFenceIron", Casings.IronFence.getBlock(), 0);
        replaceBlock("IC2:blockMachine", 0, ItemList.Casing_LV.getBlock(), 1);
        replaceBlock("IC2:blockMetal", 0, MaterialLibAPI.getStack(Materials.Copper, BlockShapes.block, 1));
        replaceBlock("IC2:blockMetal", 1, MaterialLibAPI.getStack(Materials.Tin, BlockShapes.block, 1));
        replaceBlock("IC2:blockMetal", 2, MaterialLibAPI.getStack(Materials.Bronze, BlockShapes.block, 1));
        replaceBlock("IC2:blockMetal", 3, MaterialLibAPI.getStack(Materials.Uranium, BlockShapes.block, 1));
        replaceBlock("IC2:blockMetal", 4, MaterialLibAPI.getStack(Materials.Lead, BlockShapes.block, 1));
        replaceBlock("IC2:blockMetal", 5, MaterialLibAPI.getStack(Materials.Steel, BlockShapes.block, 1));
    }
    // spotless:on
}
