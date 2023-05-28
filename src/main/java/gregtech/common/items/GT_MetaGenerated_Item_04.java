package gregtech.common.items;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.items.GT_MetaGenerated_Item_X32;

public class GT_MetaGenerated_Item_04 extends GT_MetaGenerated_Item_X32 {

    public static GT_MetaGenerated_Item_04 INSTANCE;

    public GT_MetaGenerated_Item_04() {
        super(
            "metaitem.04",
            OrePrefixes.crateGtDust,
            OrePrefixes.crateGtIngot,
            OrePrefixes.crateGtGem,
            OrePrefixes.crateGtPlate,
            OrePrefixes.nanite);
        INSTANCE = this;
        Object[] o = new Object[0];

        int luvIDs = 0;
        ItemList.LUV_Shape_Extruder_Plate.set(
            addItem(
                luvIDs++,
                "Rhodium Extruder Shape (Plate)",
                "Strong Shape for making Plates"));
        ItemList.LUV_Shape_Extruder_Rod.set(
            addItem(
                luvIDs++,
                "Rhodium Extruder Shape (Rod)",
                "Strong Shape for making Rods"));
        ItemList.LUV_Shape_Extruder_Bolt.set(
            addItem(
                luvIDs++,
                "Rhodium Extruder Shape (Bolt)",
                "Strong Shape for making Bolts"));
        ItemList.LUV_Shape_Extruder_Ring.set(
            addItem(
                luvIDs++,
                "Rhodium Extruder Shape (Ring)",
                "Strong Shape for making Rings"));
        ItemList.LUV_Shape_Extruder_Cell.set(
            addItem(
                luvIDs++,
                "Rhodium Extruder Shape (Cell)",
                "Strong Shape for making Cells"));
        ItemList.LUV_Shape_Extruder_Ingot.set(
            addItem(
                luvIDs++,
                "Rhodium Extruder Shape (Ingot)",
                "Strong Shape for, wait, can't we just use a Furnace?"));
        ItemList.LUV_Shape_Extruder_Wire.set(
            addItem(
                luvIDs++,
                "Rhodium Extruder Shape (Wire)",
                "Strong Shape for making Wires"));
        ItemList.LUV_Shape_Extruder_Casing.set(
            addItem(
                luvIDs++,
                "Rhodium Extruder Shape (Casing)",
                "Strong Shape for making Item Casings"));
        ItemList.LUV_Shape_Extruder_Pipe_Tiny.set(
            addItem(
                luvIDs++,
                "Rhodium Extruder Shape (Tiny Pipe)",
                "Strong Shape for making tiny Pipes"));
        ItemList.LUV_Shape_Extruder_Pipe_Small.set(
            addItem(
                luvIDs++,
                "Rhodium Extruder Shape (Small Pipe)",
                "Strong Shape for making small Pipes"));
        ItemList.LUV_Shape_Extruder_Pipe_Medium.set(
            addItem(
                luvIDs++,
                "Rhodium Extruder Shape (Normal Pipe)",
                "Strong Shape for making Pipes"));
        ItemList.LUV_Shape_Extruder_Pipe_Large.set(
            addItem(
                luvIDs++,
                "Rhodium Extruder Shape (Large Pipe)",
                "Strong Shape for making large Pipes"));
        ItemList.LUV_Shape_Extruder_Pipe_Huge.set(
            addItem(
                luvIDs++,
                "Rhodium Extruder Shape (Huge Pipe)",
                "Strong Shape for making full Block Pipes"));
        ItemList.LUV_Shape_Extruder_Block.set(
            addItem(
                luvIDs++,
                "Rhodium Extruder Shape (Block)",
                "Strong Shape for making Blocks"));
        ItemList.LUV_Shape_Extruder_Sword.set(
            addItem(
                luvIDs++,
                "Rhodium Extruder Shape (Sword Blade)",
                "Strong Shape for making Swords"));
        ItemList.LUV_Shape_Extruder_Pickaxe.set(
            addItem(
                luvIDs++,
                "Rhodium Extruder Shape (Pickaxe Head)",
                "Strong Shape for making Pickaxes"));
        ItemList.LUV_Shape_Extruder_Shovel.set(
            addItem(
                luvIDs++,
                "Rhodium Extruder Shape (Shovel Head)",
                "Strong Shape for making Shovels"));
        ItemList.LUV_Shape_Extruder_Axe.set(
            addItem(
                luvIDs++,
                "Rhodium Extruder Shape (Axe Head)",
                "Strong Shape for making Axes"));
        ItemList.LUV_Shape_Extruder_Hoe.set(
            addItem(
                luvIDs++,
                "Rhodium Extruder Shape (Hoe Head)",
                "Strong Shape for making Hoes"));
        ItemList.LUV_Shape_Extruder_Hammer.set(
            addItem(
                luvIDs++,
                "Rhodium Extruder Shape (Hammer Head)",
                "Strong Stellar for making Hammers"));
        ItemList.LUV_Shape_Extruder_File.set(
            addItem(
                luvIDs++,
                "Rhodium Extruder Shape (File Head)",
                "Strong Shape for making Files"));
        ItemList.LUV_Shape_Extruder_Saw.set(
            addItem(
                luvIDs++,
                "Rhodium Extruder Shape (Saw Blade)",
                "Strong Shape for making Saws"));
        ItemList.LUV_Shape_Extruder_Gear.set(
            addItem(
                luvIDs++,
                "Rhodium Extruder Shape (Gear)",
                "Strong Shape for making Gears"));
        ItemList.LUV_Shape_Extruder_Bottle.set(
            addItem(
                luvIDs++,
                "Rhodium Extruder Shape (Bottle)",
                "Strong Shape for making Bottles"));
        ItemList.LUV_Shape_Extruder_Rotor.set(
            addItem(
                luvIDs++,
                "Rhodium Extruder Shape (Rotor)",
                "Strong Shape for a Rotor"));
        ItemList.LUV_Shape_Extruder_Small_Gear.set(
            addItem(
                luvIDs++,
                "Rhodium Extruder Shape (Small Gear)",
                "Strong Shape for a Small Gear"));
        ItemList.LUV_Shape_Extruder_Turbine_Blade.set(
            addItem(
                luvIDs++,
                "Rhodium Extruder Shape (Turbine Blade)",
                "Strong Extruder Shape for a Turbine Blade"));
        ItemList.LUV_Shape_Extruder_ToolHeadDrill.set(
            addItem(
                luvIDs,
                "Rhodium Extruder Shape (Drill Head)",
                "Rhodium Extruder Shape for a Drill Head"));

        int uhvIDs = 28;
        ItemList.UHV_Shape_Extruder_Plate.set(
            addItem(
                uhvIDs++,
                "Black Plutonium Extruder Shape (Plate)",
                "Strong Shape for making Plates"));
        ItemList.UHV_Shape_Extruder_Rod.set(
            addItem(
                uhvIDs++,
                "Black Plutonium Extruder Shape (Rod)",
                "Very Strong Shape for making Rods"));
        ItemList.UHV_Shape_Extruder_Bolt.set(
            addItem(
                uhvIDs++,
                "Black Plutonium Extruder Shape (Bolt)",
                "Very Strong Shape for making Bolts"));
        ItemList.UHV_Shape_Extruder_Ring.set(
            addItem(
                uhvIDs++,
                "Black Plutonium Extruder Shape (Ring)",
                "Very Strong Shape for making Rings"));
        ItemList.UHV_Shape_Extruder_Cell.set(
            addItem(
                uhvIDs++,
                "Black Plutonium Extruder Shape (Cell)",
                "Very Strong Shape for making Cells"));
        ItemList.UHV_Shape_Extruder_Ingot.set(
            addItem(
                uhvIDs++,
                "Black Plutonium Extruder Shape (Ingot)",
                "Very Strong Shape for, wait, can't we just use a Furnace?"));
        ItemList.UHV_Shape_Extruder_Wire.set(
            addItem(
                uhvIDs++,
                "Black Plutonium Extruder Shape (Wire)",
                "Very Strong Shape for making Wires"));
        ItemList.UHV_Shape_Extruder_Casing.set(
            addItem(
                uhvIDs++,
                "Black Plutonium Extruder Shape (Casing)",
                "Very Strong Shape for making Item Casings"));
        ItemList.UHV_Shape_Extruder_Pipe_Tiny.set(
            addItem(
                uhvIDs++,
                "Black Plutonium Extruder Shape (Tiny Pipe)",
                "Very Strong Shape for making tiny Pipes"));
        ItemList.UHV_Shape_Extruder_Pipe_Small.set(
            addItem(
                uhvIDs++,
                "Black Plutonium Extruder Shape (Small Pipe)",
                "Very Strong Shape for making small Pipes"));
        ItemList.UHV_Shape_Extruder_Pipe_Medium.set(
            addItem(
                uhvIDs++,
                "Black Plutonium Extruder Shape (Normal Pipe)",
                "Very Strong Shape for making Pipes"));
        ItemList.UHV_Shape_Extruder_Pipe_Large.set(
            addItem(
                uhvIDs++,
                "Black Plutonium Extruder Shape (Large Pipe)",
                "Very Strong Shape for making large Pipes"));
        ItemList.UHV_Shape_Extruder_Pipe_Huge.set(
            addItem(
                uhvIDs++,
                "Black Plutonium Extruder Shape (Huge Pipe)",
                "Very Strong Shape for making full Block Pipes"));
        ItemList.UHV_Shape_Extruder_Block.set(
            addItem(
                uhvIDs++,
                "Black Plutonium Extruder Shape (Block)",
                "Very Strong Shape for making Blocks"));
        ItemList.UHV_Shape_Extruder_Sword.set(
            addItem(
                uhvIDs++,
                "Black Plutonium Extruder Shape (Sword Blade)",
                "Very Strong Shape for making Swords"));
        ItemList.UHV_Shape_Extruder_Pickaxe.set(
            addItem(
                uhvIDs++,
                "Black Plutonium Extruder Shape (Pickaxe Head)",
                "Very Strong Shape for making Pickaxes"));
        ItemList.UHV_Shape_Extruder_Shovel.set(
            addItem(
                uhvIDs++,
                "Black Plutonium Extruder Shape (Shovel Head)",
                "Very Strong Shape for making Shovels"));
        ItemList.UHV_Shape_Extruder_Axe.set(
            addItem(
                uhvIDs++,
                "Black Plutonium Extruder Shape (Axe Head)",
                "Very Strong Shape for making Axes"));
        ItemList.UHV_Shape_Extruder_Hoe.set(
            addItem(
                uhvIDs++,
                "Black Plutonium Extruder Shape (Hoe Head)",
                "Very Strong Shape for making Hoes"));
        ItemList.UHV_Shape_Extruder_Hammer.set(
            addItem(
                uhvIDs++,
                "Black Plutonium Extruder Shape (Hammer Head)",
                "Very Strong Stellar for making Hammers"));
        ItemList.UHV_Shape_Extruder_File.set(
            addItem(
                uhvIDs++,
                "Black Plutonium Extruder Shape (File Head)",
                "Very Strong Shape for making Files"));
        ItemList.UHV_Shape_Extruder_Saw.set(
            addItem(
                uhvIDs++,
                "Black Plutonium Extruder Shape (Saw Blade)",
                "Very Strong Shape for making Saws"));
        ItemList.UHV_Shape_Extruder_Gear.set(
            addItem(
                uhvIDs++,
                "Black Plutonium Extruder Shape (Gear)",
                "Very Strong Shape for making Gears"));
        ItemList.UHV_Shape_Extruder_Bottle.set(
            addItem(
                uhvIDs++,
                "Black Plutonium Extruder Shape (Bottle)",
                "Very Strong Shape for making Bottles"));
        ItemList.UHV_Shape_Extruder_Rotor.set(
            addItem(
                uhvIDs++,
                "Black Plutonium Extruder Shape (Rotor)",
                "Very Strong Shape for a Rotor"));
        ItemList.UHV_Shape_Extruder_Small_Gear.set(
            addItem(
                uhvIDs++,
                "Black Plutonium Extruder Shape (Small Gear)",
                "Very Strong Shape for a Small Gear"));
        ItemList.UHV_Shape_Extruder_Turbine_Blade.set(
            addItem(
                uhvIDs++,
                "Black Plutonium Extruder Shape (Turbine Blade)",
                "Very Strong Extruder Shape for a Turbine Blade"));
        ItemList.UHV_Shape_Extruder_ToolHeadDrill.set(
            addItem(
                uhvIDs,
                "Black Plutonium Extruder Shape (Drill Head)",
                "Black Plutonium Extruder Shape for a Drill Head"));
    }
}
