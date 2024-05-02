package gregtech.loaders.preload;

import static gregtech.api.enums.FluidState.GAS;
import static gregtech.api.enums.FluidState.LIQUID;
import static gregtech.api.enums.FluidState.MOLTEN;
import static gregtech.api.enums.FluidState.SLURRY;
import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.PamsHarvestCraft;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.enums.Mods.TwilightForest;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidCannerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.thermalCentrifugeRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_RecipeBuilder.WILDCARD;

import java.util.Locale;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;

import codechicken.nei.api.API;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsKevlar;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.fluid.GT_FluidFactory;
import gregtech.api.items.GT_Block_LongDistancePipe;
import gregtech.api.items.GT_BreederCell_Item;
import gregtech.api.items.GT_Generic_Item;
import gregtech.api.items.GT_RadioactiveCellIC_Item;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings1;
import gregtech.common.blocks.GT_Block_Casings2;
import gregtech.common.blocks.GT_Block_Casings3;
import gregtech.common.blocks.GT_Block_Casings4;
import gregtech.common.blocks.GT_Block_Casings5;
import gregtech.common.blocks.GT_Block_Casings6;
import gregtech.common.blocks.GT_Block_Casings8;
import gregtech.common.blocks.GT_Block_Casings9;
import gregtech.common.blocks.GT_Block_Concretes;
import gregtech.common.blocks.GT_Block_Drone;
import gregtech.common.blocks.GT_Block_Glass1;
import gregtech.common.blocks.GT_Block_Granites;
import gregtech.common.blocks.GT_Block_Machines;
import gregtech.common.blocks.GT_Block_Metal;
import gregtech.common.blocks.GT_Block_Ores;
import gregtech.common.blocks.GT_Block_Reinforced;
import gregtech.common.blocks.GT_Block_Stones;
import gregtech.common.blocks.GT_Cyclotron_Coils;
import gregtech.common.blocks.GT_TileEntity_Ores;
import gregtech.common.items.GT_DepletetCell_Item;
import gregtech.common.items.GT_FluidDisplayItem;
import gregtech.common.items.GT_IntegratedCircuit_Item;
import gregtech.common.items.GT_MetaGenerated_Item_01;
import gregtech.common.items.GT_MetaGenerated_Item_02;
import gregtech.common.items.GT_MetaGenerated_Item_03;
import gregtech.common.items.GT_MetaGenerated_Item_98;
import gregtech.common.items.GT_MetaGenerated_Item_99;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gregtech.common.items.GT_NeutronReflector_Item;
import gregtech.common.items.GT_TierDrone;
import gregtech.common.items.GT_VolumetricFlask;
import gregtech.common.tileentities.render.TileDrone;

public class GT_Loader_Item_Block_And_Fluid implements Runnable {

    @Override
    public void run() {
        Materials.Water.mFluid = (Materials.Ice.mFluid = GT_ModHandler.getWater(1000L)
            .getFluid());
        Materials.Lava.mFluid = GT_ModHandler.getLava(1000L)
            .getFluid();

        GT_Log.out.println("GT_Mod: Register Books.");

        GT_Utility.getWrittenBook(
            "Manual_Printer",
            "Printer Manual V2.0",
            "Gregorius Techneticies",
            "This Manual explains the different Functionalities the GregTech Printing Factory has built in, which are not in NEI. Most got NEI Support now, but there is still some left without it.",
            "1. Coloring Items and Blocks: You know those Crafting Recipes, which have a dye surrounded by 8 Item to dye them? Or the ones which have just one Item and one Dye in the Grid? Those two Recipe Types can be cheaply automated using the Printer.",
            "The Colorization Functionality even optimizes the Recipes, which normally require 8 Items + 1 Dye to 1 Item and an 8th of the normally used Dye in Fluid Form, isn't that awesome?",
            "2. Copying Books: This Task got slightly harder. The first Step is putting the written and signed Book inside the Scanner with a Data Stick ready to receive the Data.",
            "Now insert the Stick into the Data Slot of the Printer and add 3 pieces of Paper together with 144 Liters of actual Ink Fluid. Water mixed and chemical Dyes won't work on Paper without messing things up!",
            "You got a stack of Pages for your new Book, just put them into the Assembler with some Glue and a piece of Leather for the Binding, and you receive an identical copy of the Book, which would stack together with the original.",
            "3. Renaming Items: This Functionality is no longer Part of the Printer. There is now a Name Mold for the Forming Press to imprint a Name into an Item, just rename the Mold in an Anvil and use it in the Forming Press on any Item.",
            "4. Crafting of Books, Maps, Nametags etc etc etc: Those Recipes moved to other Machines, just look them up in NEI.");

        GT_Utility.getWrittenBook(
            "Manual_Punch_Cards",
            "Punch Card Manual V0.0",
            "Gregorius Techneticies",
            "This Manual will explain the Functionality of the Punch Cards, once they are fully implemented. And no, they won't be like the IRL Punch Cards. This is just a current Idea Collection.",
            "(i1&&i2)?o1=15:o1=0;=10",
            "ignore all Whitespace Characters, use Long for saving the Numbers",
            "&& || ^^ & | ^ ! ++ -- + - % / // * ** << >> >>> < > <= >= == !=  ~ ( ) ?: , ; ;= ;=X; = i0 i1 i2 i3 i4 i5 o0 o1 o2 o3 o4 o5 v0 v1 v2 v3 v4 v5 v6 v7 v8 v9 m0 m1 m2 m3 m4 m5 m6 m7 m8 m9 A B C D E F",
            "'0' = false, 'everything but 0' = true, '!' turns '0' into '1' and everything else into '0'",
            "',' is just a separator for multiple executed Codes in a row.",
            "';' means that the Program waits until the next tick before continuing. ';=10' and ';=10;' both mean that it will wait 10 Ticks instead of 1. And ';=0' or anything < 0 will default to 0.",
            "If the '=' Operator is used within Brackets, it returns the value the variable has been set to.",
            "The Program saves the Char Index of the current Task, the 10 Variables (which reset to 0 as soon as the Program Loop stops), the 10 Member Variables and the remaining waiting Time in its NBT.",
            "A = 10, B = 11, C = 12, D = 13, E = 14, F = 15, just for Hexadecimal Space saving, since Redstone has only 4 Bits.",
            "For implementing Loops you just need 1 Punch Card per Loop, these Cards can restart once they are finished, depending on how many other Cards there are in the Program Loop you inserted your Card into, since it will process them procedurally.",
            "A Punch Card Processor can run up to four Loops, each with the length of seven Punch Cards, parallel.",
            "Why does the Punch Card need Ink to be made, you ask? Because the empty one needs to have some lines on, and the for the punched one it prints the Code to execute in a human readable format on the Card.");

        GT_Utility.getWrittenBook(
            "Manual_Microwave",
            "Microwave Oven Manual",
            "Kitchen Industries",
            "Congratulations, you inserted a random seemingly empty Book into the Microwave and these Letters appeared out of nowhere.",
            "You just got a Microwave Oven and asked yourself 'why do I even need it?'. It's simple, the Microwave can cook for just 128 EU and at an insane speed. Not even a normal E-furnace can do it that fast and cheap!",
            "This is the cheapest and fastest way to cook for you. That is why the Microwave Oven can be found in almost every Kitchen (see www.youwannabuyakitchen.ly).",
            "Long time exposure to Microwaves can cause Cancer, but we doubt Steve lives long enough to die because of that.",
            "Do not insert any Metals. It might result in an Explosion.",
            "Do not dry Animals with it. It will result in a Hot Dog, no matter which Animal you put into it.",
            "Do not insert inflammable Objects. The Oven will catch on Fire.",
            "Do not insert Explosives such as Eggs. Just don't.");

        GT_Log.out.println("GT_Mod: Register Items.");

        new GT_IntegratedCircuit_Item();
        new GT_MetaGenerated_Item_01();
        new GT_MetaGenerated_Item_02();
        new GT_MetaGenerated_Item_03();
        // GT_MetaGenerated_Item_98 is initialized in GT_Proxy.onPostLoad() because we need to wait for fluids to be
        // registered.
        // Pre-initialization needs to happen before then, though, because the cell icons get deleted at some point
        // between load and post-load.
        GT_MetaGenerated_Item_98.preInit();
        new GT_MetaGenerated_Item_99();
        new GT_MetaGenerated_Tool_01();
        new GT_FluidDisplayItem();

        // Tiered recipe materials actually appear to be set in GT_MetaTileEntity_BasicMachine_GT_Recipe, making these
        // unused
        ItemList.Rotor_LV.set(GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Tin, 1L));
        ItemList.Rotor_MV.set(GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Bronze, 1L));
        ItemList.Rotor_HV.set(GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Steel, 1L));
        ItemList.Rotor_EV.set(GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.StainlessSteel, 1L));
        ItemList.Rotor_IV.set(GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.TungstenSteel, 1L));

        ItemList.VOLUMETRIC_FLASK.set(new GT_VolumetricFlask("Volumetric_Flask", "Volumetric flask", 1000));

        Item tItem = (Item) GT_Utility.callConstructor(
            "gregtech.common.items.GT_SensorCard_Item",
            0,
            null,
            false,
            new Object[] { "sensorcard", "GregTech Sensor Card" });
        ItemList.NC_SensorCard.set(
            tItem == null ? new GT_Generic_Item("sensorcard", "GregTech Sensor Card", "Nuclear Control not installed")
                : tItem);

        Item advSensorCard = (Item) GT_Utility
            .callConstructor("gregtech.common.items.GT_AdvancedSensorCard_Item", 0, null, false);
        ItemList.NC_AdvancedSensorCard.set(
            advSensorCard == null
                ? new GT_Generic_Item(
                    "advancedsensorcard",
                    "GregTech Advanced Sensor Card",
                    "Nuclear Control not installed")
                : advSensorCard);

        ItemList.Neutron_Reflector
            .set(new GT_NeutronReflector_Item("neutronreflector", "Iridium Neutron Reflector", 0));
        ItemList.Reactor_Coolant_He_1
            .set(GregTech_API.constructCoolantCellItem("60k_Helium_Coolantcell", "60k He Coolant Cell", 60000));
        ItemList.Reactor_Coolant_He_3
            .set(GregTech_API.constructCoolantCellItem("180k_Helium_Coolantcell", "180k He Coolant Cell", 180000));
        ItemList.Reactor_Coolant_He_6
            .set(GregTech_API.constructCoolantCellItem("360k_Helium_Coolantcell", "360k He Coolant Cell", 360000));
        ItemList.Reactor_Coolant_NaK_1
            .set(GregTech_API.constructCoolantCellItem("60k_NaK_Coolantcell", "60k NaK Coolantcell", 60000));
        ItemList.Reactor_Coolant_NaK_3
            .set(GregTech_API.constructCoolantCellItem("180k_NaK_Coolantcell", "180k NaK Coolantcell", 180000));
        ItemList.Reactor_Coolant_NaK_6
            .set(GregTech_API.constructCoolantCellItem("360k_NaK_Coolantcell", "360k NaK Coolantcell", 360000));

        ItemList.Reactor_Coolant_Sp_1
            .set(GregTech_API.constructCoolantCellItem("180k_Space_Coolantcell", "180k Sp Coolant Cell", 180000));

        ItemList.Reactor_Coolant_Sp_2
            .set(GregTech_API.constructCoolantCellItem("360k_Space_Coolantcell", "360k Sp Coolant Cell", 360000));

        ItemList.Reactor_Coolant_Sp_3
            .set(GregTech_API.constructCoolantCellItem("540k_Space_Coolantcell", "540k Sp Coolant Cell", 540000));

        ItemList.Reactor_Coolant_Sp_6
            .set(GregTech_API.constructCoolantCellItem("1080k_Space_Coolantcell", "1080k Sp Coolant Cell", 1080000));

        ItemList.GlowstoneCell.set(
            new GT_BreederCell_Item(
                "glowstoneCell",
                "Glowstone Fuel Rod",
                "Source of sunnarium",
                3000,
                1,
                10000,
                () -> ItemList.SunnariumCell.get(1)));
        ItemList.SunnariumCell.set(new GT_DepletetCell_Item("sunnariumCell", "Sunnarium Fuel Rod", 1));

        ItemList.neutroniumHeatCapacitor.set(
            GregTech_API
                .constructCoolantCellItem("neutroniumHeatCapacitor", "1G Neutronium Heat Capacitor", 1000000000));

        ItemList.Depleted_Thorium_1.set(new GT_DepletetCell_Item("ThoriumcellDep", "Fuel Rod (Depleted Thorium)", 1));
        ItemList.Depleted_Thorium_2
            .set(new GT_DepletetCell_Item("Double_ThoriumcellDep", "Dual Fuel Rod (Depleted Thorium)", 1)); // TODO
                                                                                                            // CHECK
                                                                                                            // num
        ItemList.Depleted_Thorium_4
            .set(new GT_DepletetCell_Item("Quad_ThoriumcellDep", "Quad Fuel Rod (Depleted Thorium)", 1)); // TODO
                                                                                                          // CHECK
                                                                                                          // num
        ItemList.ThoriumCell_1.set(
            new GT_RadioactiveCellIC_Item(
                "Thoriumcell",
                "Fuel Rod (Thorium)",
                1,
                50000,
                0.4F,
                0,
                0.25F,
                ItemList.Depleted_Thorium_1.get(1),
                false));
        ItemList.ThoriumCell_2.set(
            new GT_RadioactiveCellIC_Item(
                "Double_Thoriumcell",
                "Dual Fuel Rod (Thorium)",
                2,
                50000,
                0.4F,
                0,
                0.25F,
                ItemList.Depleted_Thorium_2.get(1),
                false));
        ItemList.ThoriumCell_4.set(
            new GT_RadioactiveCellIC_Item(
                "Quad_Thoriumcell",
                "Quad Fuel Rod (Thorium)",
                4,
                50000,
                0.4F,
                0,
                0.25F,
                ItemList.Depleted_Thorium_4.get(1),
                false));

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Depleted_Thorium_1.get(1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Lutetium, 2L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Thorium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1L))
            .duration(25 * SECONDS)
            .eut(48)
            .addTo(thermalCentrifugeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Depleted_Thorium_2.get(1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lutetium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Thorium, 2L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 3L))
            .duration(25 * SECONDS)
            .eut(48)
            .addTo(thermalCentrifugeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Depleted_Thorium_4.get(1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lutetium, 2L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Thorium, 4L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 6L))
            .duration(25 * SECONDS)
            .eut(48)
            .addTo(thermalCentrifugeRecipes);

        ItemList.Depleted_Naquadah_1
            .set(new GT_DepletetCell_Item("NaquadahcellDep", "Fuel Rod (Depleted Naquadah)", 1));
        ItemList.Depleted_Naquadah_2
            .set(new GT_DepletetCell_Item("Double_NaquadahcellDep", "Dual Fuel Rod (Depleted Naquadah)", 1));
        ItemList.Depleted_Naquadah_4
            .set(new GT_DepletetCell_Item("Quad_NaquadahcellDep", "Quad Fuel Rod (Depleted Naquadah)", 1));
        ItemList.NaquadahCell_1.set(
            new GT_RadioactiveCellIC_Item(
                "Naquadahcell",
                "Fuel Rod (Naquadah)",
                1,
                100000,
                4F,
                1,
                1F,
                ItemList.Depleted_Naquadah_1.get(1),
                false));
        ItemList.NaquadahCell_2.set(
            new GT_RadioactiveCellIC_Item(
                "Double_Naquadahcell",
                "Dual Fuel Rod (Naquadah)",
                2,
                100000,
                4F,
                1,
                1F,
                ItemList.Depleted_Naquadah_2.get(1),
                false));
        ItemList.NaquadahCell_4.set(
            new GT_RadioactiveCellIC_Item(
                "Quad_Naquadahcell",
                "Quad Fuel Rod (Naquadah)",
                4,
                100000,
                4F,
                1,
                1F,
                ItemList.Depleted_Naquadah_4.get(1),
                false));

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Depleted_Naquadah_1.get(1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Naquadria, 2L),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.NaquadahEnriched, 2L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TungstenSteel, 8L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Platinum, 1L))
            .outputChances(10_000, 5_000, 5_000, 2_500, 10_000, 10_000)
            .duration(25 * SECONDS)
            .eut(2000)
            .addTo(centrifugeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Depleted_Naquadah_2.get(1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 2L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 2L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadria, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.NaquadahEnriched, 4L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TungstenSteel, 18L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Platinum, 2L))
            .outputChances(10_000, 5_000, 5_000, 2_500, 10_000, 10_000)
            .duration(50 * SECONDS)
            .eut(2000)
            .addTo(centrifugeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Depleted_Naquadah_4.get(1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 4L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 4L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadria, 2L),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.NaquadahEnriched, 8L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TungstenSteel, 38L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Platinum, 4L))
            .outputChances(10_000, 5_000, 5_000, 2_500, 10_000, 10_000)
            .duration(100 * SECONDS)
            .eut(2000)
            .addTo(centrifugeRecipes);

        ItemList.Depleted_MNq_1.set(new GT_DepletetCell_Item("MNqCellDep", "Fuel Rod (Depleted Nq*)", 1));
        ItemList.Depleted_MNq_2.set(new GT_DepletetCell_Item("Double_MNqCellDep", "Dual Fuel Rod (Depleted Nq*)", 1));
        ItemList.Depleted_MNq_4.set(new GT_DepletetCell_Item("Quad_MNqCellDep", "Quad Fuel Rod (Depleted Nq*)", 1));
        ItemList.MNqCell_1.set(
            new GT_RadioactiveCellIC_Item(
                "MNqCell",
                "Fuel Rod (Nq* - MOX like behaviour)",
                1,
                100000,
                4F,
                1,
                1F,
                ItemList.Depleted_MNq_1.get(1),
                true));
        ItemList.MNqCell_2.set(
            new GT_RadioactiveCellIC_Item(
                "Double_MNqCell",
                "Dual Fuel Rod (Nq* - MOX like behaviour)",
                2,
                100000,
                4F,
                1,
                1F,
                ItemList.Depleted_MNq_2.get(1),
                true));
        ItemList.MNqCell_4.set(
            new GT_RadioactiveCellIC_Item(
                "Quad_MNqCell",
                "Quad Fuel Rod (Nq* - MOX like behaviour)",
                4,
                100000,
                4F,
                1,
                1F,
                ItemList.Depleted_MNq_4.get(1),
                true));

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Depleted_MNq_1.get(1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.NaquadahEnriched, 2L),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Naquadria, 2L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TungstenSteel, 8L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Platinum, 1L))
            .outputChances(10_000, 5_000, 5_000, 2_500, 10_000, 10_000)
            .duration(25 * SECONDS)
            .eut(2000)
            .addTo(centrifugeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Depleted_MNq_2.get(1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 2L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 2L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NaquadahEnriched, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Naquadria, 4L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TungstenSteel, 18L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Platinum, 2L))
            .outputChances(10_000, 5_000, 5_000, 2_500, 10_000, 10_000)
            .duration(50 * SECONDS)
            .eut(2000)
            .addTo(centrifugeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Depleted_MNq_4.get(1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 4L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 4L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NaquadahEnriched, 2L),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Naquadria, 8L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TungstenSteel, 38L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Platinum, 4L))
            .outputChances(10_000, 5_000, 5_000, 2_500, 10_000, 10_000)
            .duration(100 * SECONDS)
            .eut(2000)
            .addTo(centrifugeRecipes);

        ItemList.Uraniumcell_1.set(
            new GT_RadioactiveCellIC_Item(
                "reactorUraniumSimple",
                "Fuel Rod (Uranium)",
                1,
                20000,
                2F,
                1,
                1F,
                GT_ModHandler.getIC2Item("reactorDepletedUraniumSimple", 1),
                false));
        ItemList.Uraniumcell_2.set(
            new GT_RadioactiveCellIC_Item(
                "reactorUraniumDual",
                "Dual Fuel Rod (Uranium)",
                2,
                20000,
                2F,
                1,
                1F,
                GT_ModHandler.getIC2Item("reactorDepletedUraniumDual", 1),
                false));
        ItemList.Uraniumcell_4.set(
            new GT_RadioactiveCellIC_Item(
                "reactorUraniumQuad",
                "Quad Fuel Rod (Uranium)",
                4,
                20000,
                2F,
                1,
                1F,
                GT_ModHandler.getIC2Item("reactorDepletedUraniumQuad", 1),
                false));
        ItemList.Moxcell_1.set(
            new GT_RadioactiveCellIC_Item(
                "reactorMOXSimple",
                "Fuel Rod (Mox)",
                1,
                10000,
                2F,
                1,
                1F,
                GT_ModHandler.getIC2Item("reactorDepletedMOXSimple", 1),
                true));
        ItemList.Moxcell_2.set(
            new GT_RadioactiveCellIC_Item(
                "reactorMOXDual",
                "Dual Fuel Rod (Mox)",
                2,
                10000,
                2F,
                1,
                1F,
                GT_ModHandler.getIC2Item("reactorDepletedMOXDual", 1),
                true));
        ItemList.Moxcell_4.set(
            new GT_RadioactiveCellIC_Item(
                "reactorMOXQuad",
                "Quad Fuel Rod (Mox)",
                4,
                10000,
                2F,
                1,
                1F,
                GT_ModHandler.getIC2Item("reactorDepletedMOXQuad", 1),
                true));

        GT_Log.out.println("GT_Mod: Adding Blocks.");
        GregTech_API.sBlockMachines = new GT_Block_Machines();
        GregTech_API.sBlockCasings1 = new GT_Block_Casings1();
        GregTech_API.sBlockCasings2 = new GT_Block_Casings2();
        GregTech_API.sBlockCasings3 = new GT_Block_Casings3();
        GregTech_API.sBlockCasings4 = new GT_Block_Casings4();
        GregTech_API.sBlockCasings5 = new GT_Block_Casings5();
        GregTech_API.sBlockCasings6 = new GT_Block_Casings6();
        GregTech_API.sBlockCasings8 = new GT_Block_Casings8();
        GregTech_API.sBlockCasings9 = new GT_Block_Casings9();
        GregTech_API.sBlockGranites = new GT_Block_Granites();
        GregTech_API.sBlockLongDistancePipes = new GT_Block_LongDistancePipe();
        GregTech_API.sBlockConcretes = new GT_Block_Concretes();
        GregTech_API.sBlockStones = new GT_Block_Stones();
        GregTech_API.sBlockOres1 = new GT_Block_Ores();
        GregTech_API.sDroneRender = new GT_Block_Drone();
        GregTech_API.sBlockGlass1 = new GT_Block_Glass1();
        // meta ID order, DO NOT CHANGE ORDER

        GregTech_API.sBlockMetal1 = new GT_Block_Metal(
            "gt.blockmetal1",
            new Materials[] { Materials.Adamantium, Materials.Aluminium, Materials.Americium, Materials.AnnealedCopper,
                Materials.Antimony, Materials.Arsenic, Materials.AstralSilver, Materials.BatteryAlloy,
                Materials.Beryllium, Materials.Bismuth, Materials.BismuthBronze, Materials.BlackBronze,
                Materials.BlackSteel, Materials.BlueAlloy, Materials.BlueSteel, Materials.Brass },
            OrePrefixes.block,
            gregtech.api.enums.Textures.BlockIcons.STORAGE_BLOCKS1);

        GregTech_API.sBlockMetal2 = new GT_Block_Metal(
            "gt.blockmetal2",
            new Materials[] { Materials.Bronze, Materials.Caesium, Materials.Cerium, Materials.Chrome,
                Materials.ChromiumDioxide, Materials.Cobalt, Materials.CobaltBrass, Materials.Copper,
                Materials.Cupronickel, Materials.DamascusSteel, Materials.DarkIron, Materials.DeepIron, Materials.Desh,
                Materials.Duranium, Materials.Dysprosium, Materials.Electrum },
            OrePrefixes.block,
            gregtech.api.enums.Textures.BlockIcons.STORAGE_BLOCKS2);

        GregTech_API.sBlockMetal3 = new GT_Block_Metal(
            "gt.blockmetal3",
            new Materials[] { Materials.ElectrumFlux, Materials.Enderium, Materials.Erbium, Materials.Europium,
                Materials.FierySteel, Materials.Gadolinium, Materials.Gallium, Materials.Holmium, Materials.HSLA,
                Materials.Indium, Materials.InfusedGold, Materials.Invar, Materials.Iridium, Materials.IronMagnetic,
                Materials.IronWood, Materials.Kanthal },
            OrePrefixes.block,
            gregtech.api.enums.Textures.BlockIcons.STORAGE_BLOCKS3);

        GregTech_API.sBlockMetal4 = new GT_Block_Metal(
            "gt.blockmetal4",
            new Materials[] { Materials.Knightmetal, Materials.Lanthanum, Materials.Lead, Materials.Lutetium,
                Materials.Magnalium, Materials.Magnesium, Materials.Manganese, Materials.MeteoricIron,
                Materials.MeteoricSteel, Materials.Trinium, Materials.Mithril, Materials.Molybdenum, Materials.Naquadah,
                Materials.NaquadahAlloy, Materials.NaquadahEnriched, Materials.Naquadria },
            OrePrefixes.block,
            gregtech.api.enums.Textures.BlockIcons.STORAGE_BLOCKS4);

        GregTech_API.sBlockMetal5 = new GT_Block_Metal(
            "gt.blockmetal5",
            new Materials[] { Materials.Neodymium, Materials.NeodymiumMagnetic, Materials.Neutronium,
                Materials.Nichrome, Materials.Nickel, Materials.Niobium, Materials.NiobiumNitride,
                Materials.NiobiumTitanium, Materials.Osmiridium, Materials.Osmium, Materials.Palladium,
                Materials.PigIron, Materials.Platinum, Materials.Plutonium, Materials.Plutonium241,
                Materials.Praseodymium },
            OrePrefixes.block,
            gregtech.api.enums.Textures.BlockIcons.STORAGE_BLOCKS5);

        GregTech_API.sBlockMetal6 = new GT_Block_Metal(
            "gt.blockmetal6",
            new Materials[] { Materials.Promethium, Materials.RedAlloy, Materials.RedSteel, Materials.RoseGold,
                Materials.Rubidium, Materials.Samarium, Materials.Scandium, Materials.ShadowIron, Materials.ShadowSteel,
                Materials.Silicon, Materials.Silver, Materials.SolderingAlloy, Materials.StainlessSteel,
                Materials.Steel, Materials.SteelMagnetic, Materials.SterlingSilver },
            OrePrefixes.block,
            gregtech.api.enums.Textures.BlockIcons.STORAGE_BLOCKS6);

        GregTech_API.sBlockMetal7 = new GT_Block_Metal(
            "gt.blockmetal7",
            new Materials[] { Materials.Sunnarium, Materials.Tantalum, Materials.Tellurium, Materials.Terbium,
                Materials.Thaumium, Materials.Thorium, Materials.Thulium, Materials.Tin, Materials.TinAlloy,
                Materials.Titanium, Materials.Tritanium, Materials.Tungsten, Materials.TungstenSteel, Materials.Ultimet,
                Materials.Uranium, Materials.Uranium235 },
            OrePrefixes.block,
            gregtech.api.enums.Textures.BlockIcons.STORAGE_BLOCKS7);

        GregTech_API.sBlockMetal8 = new GT_Block_Metal(
            "gt.blockmetal8",
            new Materials[] { Materials.Vanadium, Materials.VanadiumGallium, Materials.WroughtIron, Materials.Ytterbium,
                Materials.Yttrium, Materials.YttriumBariumCuprate, Materials.Zinc, Materials.TungstenCarbide,
                Materials.VanadiumSteel, Materials.HSSG, Materials.HSSE, Materials.HSSS, Materials.Steeleaf,
                Materials.Ichorium, Materials.Firestone, Materials.Shadow },
            OrePrefixes.block,
            gregtech.api.enums.Textures.BlockIcons.STORAGE_BLOCKS8);

        GregTech_API.sBlockGem1 = new GT_Block_Metal(
            "gt.blockgem1",
            new Materials[] { Materials.InfusedAir, Materials.Amber, Materials.Amethyst, Materials.InfusedWater,
                Materials.BlueTopaz, Materials.CertusQuartz, Materials.Dilithium, Materials.EnderEye,
                Materials.EnderPearl, Materials.FoolsRuby, Materials.Force, Materials.Forcicium, Materials.Forcillium,
                Materials.GreenSapphire, Materials.InfusedFire, Materials.Jasper },
            OrePrefixes.block,
            gregtech.api.enums.Textures.BlockIcons.STORAGE_BLOCKS9);

        GregTech_API.sBlockGem2 = new GT_Block_Metal(
            "gt.blockgem2",
            new Materials[] { Materials.Lazurite, Materials.Lignite, Materials.Monazite, Materials.Niter,
                Materials.Olivine, Materials.Opal, Materials.InfusedOrder, Materials.InfusedEntropy,
                Materials.TricalciumPhosphate, Materials.Quartzite, Materials.GarnetRed, Materials.Ruby,
                Materials.Sapphire, Materials.Sodalite, Materials.Tanzanite, Materials.InfusedEarth },
            OrePrefixes.block,
            gregtech.api.enums.Textures.BlockIcons.STORAGE_BLOCKS10);

        GregTech_API.sBlockGem3 = new GT_Block_Metal(
            "gt.blockgem3",
            new Materials[] { Materials.Topaz, Materials.Vinteum, Materials.GarnetYellow, Materials.NetherStar,
                Materials.Charcoal, Materials.Blaze },
            OrePrefixes.block,
            gregtech.api.enums.Textures.BlockIcons.STORAGE_BLOCKS11);

        GregTech_API.sBlockMetal9 = new GT_Block_Metal(
            "gt.blockmetal9",
            new Materials[] { Materials.Cryolite, Materials.SiliconSG, MaterialsKevlar.NickelAluminide,
                MaterialsUEVplus.SpaceTime, MaterialsUEVplus.TranscendentMetal, Materials.Oriharukon,
                MaterialsUEVplus.WhiteDwarfMatter, MaterialsUEVplus.BlackDwarfMatter, MaterialsUEVplus.Universium,
                MaterialsUEVplus.Eternity, MaterialsUEVplus.MagMatter },
            OrePrefixes.block,
            gregtech.api.enums.Textures.BlockIcons.STORAGE_BLOCKS12);

        GregTech_API.sBlockReinforced = new GT_Block_Reinforced("gt.blockreinforced");

        GT_Log.out.println("GT_Mod: Register TileEntities.");

        BaseMetaTileEntity tBaseMetaTileEntity = GregTech_API.constructBaseMetaTileEntity();

        GT_Log.out.println("GT_Mod: Registering the BaseMetaTileEntity.");
        GameRegistry.registerTileEntity(tBaseMetaTileEntity.getClass(), "BaseMetaTileEntity");
        FMLInterModComms.sendMessage(
            AppliedEnergistics2.ID,
            "whitelist-spatial",
            tBaseMetaTileEntity.getClass()
                .getName());

        GT_Log.out.println("GT_Mod: Registering the DroneRender.");
        GameRegistry.registerTileEntity(TileDrone.class, "DroneRender");

        GT_Log.out.println("GT_Mod: Registering the BaseMetaPipeEntity.");
        GameRegistry.registerTileEntity(BaseMetaPipeEntity.class, "BaseMetaPipeEntity");
        FMLInterModComms.sendMessage(AppliedEnergistics2.ID, "whitelist-spatial", BaseMetaPipeEntity.class.getName());

        GT_Log.out.println("GT_Mod: Registering the Ore TileEntity.");
        GameRegistry.registerTileEntity(GT_TileEntity_Ores.class, "GT_TileEntity_Ores");
        FMLInterModComms.sendMessage(AppliedEnergistics2.ID, "whitelist-spatial", GT_TileEntity_Ores.class.getName());

        GT_Log.out.println("GT_Mod: Registering Fluids.");
        Materials.ConstructionFoam.mFluid = GT_Utility
            .getFluidForFilledItem(GT_ModHandler.getIC2Item("CFCell", 1L), true)
            .getFluid();
        Materials.UUMatter.mFluid = GT_Utility.getFluidForFilledItem(GT_ModHandler.getIC2Item("uuMatterCell", 1L), true)
            .getFluid();

        GT_FluidFactory.builder("Air")
            .withLocalizedName("Air")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Air)
            .registerContainers(ItemList.Cell_Air.get(1L), ItemList.Cell_Empty.get(1L), 2000);
        GT_FluidFactory.builder("LiquidOxygen")
            .withLocalizedName("Liquid Oxygen")
            .withStateAndTemperature(GAS, 60)
            .buildAndRegister()
            .configureMaterials(Materials.LiquidOxygen)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.LiquidOxygen, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("LiquidNitrogen")
            .withLocalizedName("Liquid Nitrogen")
            .withStateAndTemperature(GAS, 77)
            .buildAndRegister()
            .configureMaterials(Materials.LiquidNitrogen)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.LiquidNitrogen, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("LiquidAir")
            .withLocalizedName("Liquid Air")
            .withStateAndTemperature(LIQUID, 77)
            .buildAndRegister()
            .configureMaterials(Materials.LiquidAir)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.LiquidAir, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("Oxygen")
            .withLocalizedName("Oxygen")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Oxygen)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("Hydrogen")
            .withLocalizedName("Hydrogen")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Hydrogen)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("Deuterium")
            .withLocalizedName("Deuterium")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Deuterium)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Deuterium, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("Tritium")
            .withLocalizedName("Tritium")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Tritium)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Tritium, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("Helium")
            .withLocalizedName("Helium")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Helium)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Helium, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("Argon")
            .withLocalizedName("Argon")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Argon)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Argon, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("Radon")
            .withLocalizedName("Radon")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Radon)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Radon, 1L),
                ItemList.Cell_Empty.get(1L));

        GT_FluidFactory.builder("Fluorine")
            .withLocalizedName("Fluorine")
            .withStateAndTemperature(GAS, 53)
            .buildAndRegister()
            .configureMaterials(Materials.Fluorine)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Fluorine, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("Titaniumtetrachloride")
            .withLocalizedName("Titaniumtetrachloride")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Titaniumtetrachloride)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Titaniumtetrachloride, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("Helium-3")
            .withLocalizedName("Helium-3")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Helium_3)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Helium_3, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("Methane")
            .withLocalizedName("Methane")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Methane)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Methane, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("Nitrogen")
            .withLocalizedName("Nitrogen")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Nitrogen)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Nitrogen, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("NitrogenDioxide")
            .withLocalizedName("Nitrogen Dioxide")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.NitrogenDioxide)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.NitrogenDioxide, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("Steam")
            .withLocalizedName("Steam")
            .withStateAndTemperature(GAS, 375)
            .buildAndRegister()
            .configureMaterials(Materials.Water)
            .registerBContainers(GT_ModHandler.getIC2Item("steamCell", 1), Materials.Empty.getCells(1));

        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1))
            .itemOutputs(GT_ModHandler.getIC2Item("steamCell", 1))
            .fluidInputs(GT_ModHandler.getSteam(1000))
            .duration(16 * TICKS)
            .eut(1)
            .addTo(fluidCannerRecipes);

        Materials.Ice.mGas = Materials.Water.mGas;
        Materials.Water.mGas.setTemperature(375)
            .setGaseous(true);

        ItemList.sOilExtraHeavy = GT_FluidFactory.of("liquid_extra_heavy_oil", "Very Heavy Oil", LIQUID, 295);
        ItemList.sEpichlorhydrin = GT_FluidFactory.builder("liquid_epichlorhydrin")
            .withLocalizedName("Epichlorohydrin")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Epichlorohydrin)
            .registerBContainers(Materials.Epichlorohydrin.getCells(1), Materials.Empty.getCells(1))
            .asFluid();
        ItemList.sDrillingFluid = GT_FluidFactory.of("liquid_drillingfluid", "Drilling Fluid", LIQUID, 295);
        ItemList.sToluene = GT_FluidFactory.builder("liquid_toluene")
            .withLocalizedName("Toluene")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Toluene)
            .registerBContainers(Materials.Toluene.getCells(1), Materials.Empty.getCells(1))
            .asFluid();
        ItemList.sNitrationMixture = GT_FluidFactory.builder("liquid_nitrationmixture")
            .withLocalizedName("Nitration Mixture")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.NitrationMixture)
            .registerBContainers(Materials.NitrationMixture.getCells(1), Materials.Empty.getCells(1))
            .asFluid();

        GT_FluidFactory.builder("liquid_heavy_oil")
            .withLocalizedName("Heavy Oil")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.OilHeavy)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.OilHeavy, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("liquid_medium_oil")
            .withLocalizedName("Raw Oil")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.OilMedium)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.OilMedium, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("liquid_light_oil")
            .withLocalizedName("Light Oil")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.OilLight)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.OilLight, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("gas_natural_gas")
            .withLocalizedName("Natural Gas")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.NatruralGas)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.NatruralGas, 1L),
                ItemList.Cell_Empty.get(1L));
        ItemList.sHydricSulfur = GT_FluidFactory.builder("liquid_hydricsulfur")
            .withLocalizedName("Hydrogen Sulfide")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.HydricSulfide)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.HydricSulfide, 1L),
                ItemList.Cell_Empty.get(1L))
            .asFluid();
        GT_FluidFactory.builder("gas_sulfuricgas")
            .withLocalizedName("Sulfuric Gas")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.SulfuricGas)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.SulfuricGas, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("gas_gas")
            .withLocalizedName("Refinery Gas")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Gas)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Gas, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("liquid_sulfuricnaphtha")
            .withLocalizedName("Sulfuric Naphtha")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.SulfuricNaphtha)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.SulfuricNaphtha, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("liquid_sufluriclight_fuel")
            .withLocalizedName("Sulfuric Light Fuel")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.SulfuricLightFuel)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.SulfuricLightFuel, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("liquid_sulfuricheavy_fuel")
            .withLocalizedName("Sulfuric Heavy Fuel")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.SulfuricHeavyFuel)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.SulfuricHeavyFuel, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("liquid_naphtha")
            .withLocalizedName("Naphtha")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Naphtha)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Naphtha, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("liquid_light_fuel")
            .withLocalizedName("Light Fuel")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.LightFuel)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.LightFuel, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("liquid_heavy_fuel")
            .withLocalizedName("Heavy Fuel")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.HeavyFuel)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.HeavyFuel, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("liquid_lpg")
            .withLocalizedName("LPG")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.LPG)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.LPG, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("charcoal_byproducts")
            .withTextureName("molten.autogenerated")
            .withLocalizedName("Charcoal Byproducts")
            .withColorRGBA(Materials.CharcoalByproducts.mRGBa)
            .withStateAndTemperature(GAS, 775)
            .buildAndRegister()
            .configureMaterials(Materials.CharcoalByproducts)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.CharcoalByproducts, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("molten.bisphenol_a")
            .withTextureName("molten.autogenerated")
            .withLocalizedName("Molten Bisphenol A")
            .withColorRGBA(Materials.BisphenolA.mRGBa)
            .withStateAndTemperature(LIQUID, 432)
            .buildAndRegister()
            .configureMaterials(Materials.BisphenolA)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.BisphenolA, 1L),
                ItemList.Cell_Empty.get(1L));

        GT_FluidFactory.builder("UUAmplifier")
            .withLocalizedName("UU Amplifier")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.UUAmplifier)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.UUAmplifier, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("Chlorine")
            .withLocalizedName("Chlorine")
            .withStateAndTemperature(GAS, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Chlorine)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Chlorine, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("Mercury")
            .withLocalizedName("Mercury")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Mercury)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Mercury, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("NitroFuel")
            .withLocalizedName("Cetane-Boosted Diesel")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.NitroFuel)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.NitroFuel, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("SodiumPersulfate")
            .withLocalizedName("Sodium Persulfate")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.SodiumPersulfate)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.SodiumPersulfate, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("Glyceryl")
            .withLocalizedName("Glyceryl Trinitrate")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Glyceryl)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Glyceryl, 1L),
                ItemList.Cell_Empty.get(1L));

        GT_FluidFactory.builder("lubricant")
            .withLocalizedName("Lubricant")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Lubricant)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Lubricant, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("creosote")
            .withLocalizedName("Creosote Oil")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Creosote)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Creosote, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("seedoil")
            .withLocalizedName("Seed Oil")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.SeedOil)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.SeedOil, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("fishoil")
            .withLocalizedName("Fish Oil")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.FishOil)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.FishOil, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("oil")
            .withLocalizedName("Oil")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Oil)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oil, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("fuel")
            .withLocalizedName("Diesel")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Fuel)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Fuel, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("for.honey")
            .withLocalizedName("Honey")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Honey)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Honey, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("biomass")
            .withLocalizedName("Biomass")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Biomass)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Biomass, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("bioethanol")
            .withLocalizedName("Bio Ethanol")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Ethanol)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Ethanol, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("sulfuricacid")
            .withLocalizedName("Sulfuric Acid")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.SulfuricAcid)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.SulfuricAcid, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("milk")
            .withLocalizedName("Milk")
            .withStateAndTemperature(LIQUID, 290)
            .buildAndRegister()
            .configureMaterials(Materials.Milk)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Milk, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("mcguffium")
            .withLocalizedName("Mc Guffium 239")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.McGuffium239)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.McGuffium239, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("refinedGlue")
            .withLocalizedName("Refined Glue")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Glue)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Glue, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("hotfryingoil")
            .withLocalizedName("Hot Frying Oil")
            .withStateAndTemperature(LIQUID, 400)
            .buildAndRegister()
            .configureMaterials(Materials.FryingOilHot)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.FryingOilHot, 1L),
                ItemList.Cell_Empty.get(1L));

        GT_FluidFactory.builder("DimensionallyTranscendentResidue")
            .withLocalizedName("Dimensionally Transcendent Residue")
            .withStateAndTemperature(LIQUID, 2000000000)
            .buildAndRegister()
            .configureMaterials(MaterialsUEVplus.DimensionallyTranscendentResidue)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, MaterialsUEVplus.DimensionallyTranscendentResidue, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("ExcitedDTCC")
            .withLocalizedName("Excited Dimensionally Transcendent Crude Catalyst")
            .withStateAndTemperature(LIQUID, 500000000)
            .buildAndRegister()
            .configureMaterials(MaterialsUEVplus.ExcitedDTCC)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, MaterialsUEVplus.ExcitedDTCC, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("ExcitedDTPC")
            .withLocalizedName("Excited Dimensionally Transcendent Prosaic Catalyst")
            .withStateAndTemperature(LIQUID, 500000000)
            .buildAndRegister()
            .configureMaterials(MaterialsUEVplus.ExcitedDTPC)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, MaterialsUEVplus.ExcitedDTPC, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("ExcitedDTRC")
            .withLocalizedName("Excited Dimensionally Transcendent Resplendent Catalyst")
            .withStateAndTemperature(LIQUID, 500000000)
            .buildAndRegister()
            .configureMaterials(MaterialsUEVplus.ExcitedDTRC)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, MaterialsUEVplus.ExcitedDTRC, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("ExcitedDTEC")
            .withLocalizedName("Excited Dimensionally Transcendent Exotic Catalyst")
            .withStateAndTemperature(LIQUID, 500000000)
            .buildAndRegister()
            .configureMaterials(MaterialsUEVplus.ExcitedDTEC)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, MaterialsUEVplus.ExcitedDTEC, 1L),
                ItemList.Cell_Empty.get(1L));
        GT_FluidFactory.builder("ExcitedDTSC")
            .withLocalizedName("Excited Dimensionally Transcendent Stellar Catalyst")
            .withStateAndTemperature(LIQUID, 500000000)
            .buildAndRegister()
            .configureMaterials(MaterialsUEVplus.ExcitedDTSC)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, MaterialsUEVplus.ExcitedDTSC, 1L),
                ItemList.Cell_Empty.get(1L));

        GT_FluidFactory.builder(MaterialsUEVplus.RawStarMatter.mName)
            .withLocalizedName(MaterialsUEVplus.RawStarMatter.mLocalizedName)
            .withStateAndTemperature(LIQUID, 10_000_000)
            .buildAndRegister()
            .configureMaterials(MaterialsUEVplus.RawStarMatter)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, MaterialsUEVplus.RawStarMatter, 1L),
                ItemList.Cell_Empty.get(1L));

        GT_FluidFactory.builder(MaterialsUEVplus.Space.mName)
            .withLocalizedName(MaterialsUEVplus.Space.mLocalizedName)
            .withStateAndTemperature(MOLTEN, 0)
            .buildAndRegister()
            .configureMaterials(MaterialsUEVplus.Space)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, MaterialsUEVplus.Space, 1L),
                ItemList.Cell_Empty.get(1L));

        GT_FluidFactory.builder(MaterialsUEVplus.Time.mName)
            .withLocalizedName(MaterialsUEVplus.Time.mLocalizedName)
            .withStateAndTemperature(MOLTEN, 0)
            .buildAndRegister()
            .configureMaterials(MaterialsUEVplus.Time)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, MaterialsUEVplus.Time, 1L),
                ItemList.Cell_Empty.get(1L));

        GT_FluidFactory.builder("PrimordialMatter")
            .withLocalizedName(MaterialsUEVplus.PrimordialMatter.mLocalizedName)
            .withStateAndTemperature(LIQUID, 2_000_000_000)
            .buildAndRegister()
            .configureMaterials(MaterialsUEVplus.PrimordialMatter)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, MaterialsUEVplus.PrimordialMatter, 1L),
                ItemList.Cell_Empty.get(1L));

        GT_FluidFactory.builder("QuarkGluonPlasma")
            .withLocalizedName(MaterialsUEVplus.QuarkGluonPlasma.mLocalizedName)
            .withStateAndTemperature(LIQUID, 2_000_000_000)
            .buildAndRegister()
            .configureMaterials(MaterialsUEVplus.QuarkGluonPlasma)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, MaterialsUEVplus.QuarkGluonPlasma, 1L),
                ItemList.Cell_Empty.get(1L));

        GT_FluidFactory.builder("fieryblood")
            .withLocalizedName("Fiery Blood")
            .withStateAndTemperature(LIQUID, 6400)
            .buildAndRegister()
            .configureMaterials(Materials.FierySteel)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.FierySteel, 1L),
                ItemList.Cell_Empty.get(1L));

        GT_FluidFactory.builder("holywater")
            .withLocalizedName("Holy Water")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.HolyWater)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.HolyWater, 1L),
                ItemList.Cell_Empty.get(1L));
        if (ItemList.TF_Vial_FieryBlood.get(1L) != null) {
            FluidContainerRegistry.registerFluidContainer(
                new FluidContainerRegistry.FluidContainerData(
                    Materials.FierySteel.getFluid(250L),
                    ItemList.TF_Vial_FieryBlood.get(1L),
                    ItemList.Bottle_Empty.get(1L)));
        }

        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                Materials.Milk.getFluid(1000L),
                GT_OreDictUnificator.get(OrePrefixes.bucket, Materials.Milk, 1L),
                GT_OreDictUnificator.get(OrePrefixes.bucket, Materials.Empty, 1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                Materials.Milk.getFluid(250L),
                ItemList.Bottle_Milk.get(1L),
                ItemList.Bottle_Empty.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                Materials.HolyWater.getFluid(250L),
                ItemList.Bottle_Holy_Water.get(1L),
                ItemList.Bottle_Empty.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                Materials.McGuffium239.getFluid(250L),
                ItemList.McGuffium_239.get(1L),
                ItemList.Bottle_Empty.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                Materials.Fuel.getFluid(100L),
                ItemList.Tool_Lighter_Invar_Full.get(1L),
                ItemList.Tool_Lighter_Invar_Empty.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                Materials.Fuel.getFluid(1000L),
                ItemList.Tool_Lighter_Platinum_Full.get(1L),
                ItemList.Tool_Lighter_Platinum_Empty.get(1L)));

        Dyes.dyeBlack.addFluidDye(GT_FluidFactory.of("squidink", "Squid Ink", LIQUID, 295));
        Dyes.dyeBlue.addFluidDye(GT_FluidFactory.of("indigo", "Indigo Dye", LIQUID, 295));
        for (byte i = 0; i < Dyes.VALUES.length; i = (byte) (i + 1)) {
            Dyes tDye = Dyes.VALUES[i];
            tDye.addFluidDye(
                GT_FluidFactory.builder(
                    "dye.watermixed." + tDye.name()
                        .toLowerCase(Locale.ENGLISH))
                    .withTextureName("dyes")
                    .withLocalizedName("Water Mixed " + tDye.mName + " Dye")
                    .withColorRGBA(tDye.getRGBA())
                    .withStateAndTemperature(LIQUID, 295)
                    .buildAndRegister()
                    .asFluid());
            tDye.addFluidDye(
                GT_FluidFactory.builder(
                    "dye.chemical." + tDye.name()
                        .toLowerCase(Locale.ENGLISH))
                    .withTextureName("dyes")
                    .withLocalizedName("Chemical " + tDye.mName + " Dye")
                    .withColorRGBA(tDye.getRGBA())
                    .withStateAndTemperature(LIQUID, 295)
                    .buildAndRegister()
                    .registerContainers(ItemList.SPRAY_CAN_DYES[i].get(1L), ItemList.Spray_Empty.get(1L), 2304)
                    .asFluid());
        }
        GT_FluidFactory.builder("ice")
            .withLocalizedName("Crushed Ice")
            .withStateAndTemperature(SLURRY, 270)
            .buildAndRegister()
            .configureMaterials(Materials.Ice)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Ice, 1L),
                ItemList.Cell_Empty.get(1L));
        Materials.Water.mSolid = Materials.Ice.mSolid;

        GT_FluidFactory.builder("molten.glass")
            .withLocalizedName("Molten Glass")
            .withStateAndTemperature(MOLTEN, 1500)
            .buildAndRegister()
            .configureMaterials(Materials.Glass)
            .registerContainers(
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Glass, 1L),
                ItemList.Cell_Empty.get(1L),
                144);
        GT_FluidFactory.builder("molten.redstone")
            .withLocalizedName("Molten Redstone")
            .withStateAndTemperature(MOLTEN, 500)
            .buildAndRegister()
            .configureMaterials(Materials.Redstone)
            .registerContainers(
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Redstone, 1L),
                ItemList.Cell_Empty.get(1L),
                144);
        GT_FluidFactory.builder("molten.blaze")
            .withLocalizedName("Molten Blaze")
            .withStateAndTemperature(MOLTEN, 6400)
            .buildAndRegister()
            .configureMaterials(Materials.Blaze)
            .registerContainers(
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Blaze, 1L),
                ItemList.Cell_Empty.get(1L),
                144);
        GT_FluidFactory.builder("wet.concrete")
            .withLocalizedName("Wet Concrete")
            .withStateAndTemperature(MOLTEN, 300)
            .buildAndRegister()
            .configureMaterials(Materials.Concrete)
            .registerContainers(
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Concrete, 1L),
                ItemList.Cell_Empty.get(1L),
                144);

        for (Materials tMaterial : Materials.values()) {
            if ((tMaterial.mStandardMoltenFluid == null) && (tMaterial.contains(SubTag.SMELTING_TO_FLUID))
                && (!tMaterial.contains(SubTag.NO_SMELTING))) {
                GT_Mod.gregtechproxy.addAutogeneratedMoltenFluid(tMaterial);
                if ((tMaterial.mSmeltInto != tMaterial) && (tMaterial.mSmeltInto.mStandardMoltenFluid == null)) {
                    GT_Mod.gregtechproxy.addAutogeneratedMoltenFluid(tMaterial.mSmeltInto);
                }
            }
            if (tMaterial.mElement != null) {
                GT_Mod.gregtechproxy.addAutogeneratedPlasmaFluid(tMaterial);
            }
            if (tMaterial.hasCorrespondingFluid()) {
                GT_Mod.gregtechproxy.addAutoGeneratedCorrespondingFluid(tMaterial);
            }
            if (tMaterial.hasCorrespondingGas()) {
                GT_Mod.gregtechproxy.addAutoGeneratedCorrespondingGas(tMaterial);
            }
            if (tMaterial.canBeCracked()) {
                GT_Mod.gregtechproxy.addAutoGeneratedHydroCrackedFluids(tMaterial);
                GT_Mod.gregtechproxy.addAutoGeneratedSteamCrackedFluids(tMaterial);
            }
        }

        GT_FluidFactory.builder("potion.awkward")
            .withLocalizedName("Awkward Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.thick")
            .withLocalizedName("Thick Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 32), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.mundane")
            .withLocalizedName("Mundane Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 64), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.damage")
            .withLocalizedName("Harming Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8204), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.damage.strong")
            .withLocalizedName("Strong Harming Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8236), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.damage.splash")
            .withLocalizedName("Splash Harming Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16396), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.damage.strong.splash")
            .withLocalizedName("Strong Splash Harming Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16428), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.health")
            .withLocalizedName("Healing Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8197), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.health.strong")
            .withLocalizedName("Strong Healing Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8229), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.health.splash")
            .withLocalizedName("Splash Healing Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16389), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.health.strong.splash")
            .withLocalizedName("Strong Splash Healing Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16421), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.speed")
            .withLocalizedName("Swiftness Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8194), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.speed.strong")
            .withLocalizedName("Strong Swiftness Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8226), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.speed.long")
            .withLocalizedName("Stretched Swiftness Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8258), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.speed.splash")
            .withLocalizedName("Splash Swiftness Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16386), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.speed.strong.splash")
            .withLocalizedName("Strong Splash Swiftness Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16418), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.speed.long.splash")
            .withLocalizedName("Stretched Splash Swiftness Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16450), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.strength")
            .withLocalizedName("Strength Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8201), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.strength.strong")
            .withLocalizedName("Strong Strength Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8233), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.strength.long")
            .withLocalizedName("Stretched Strength Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8265), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.strength.splash")
            .withLocalizedName("Splash Strength Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16393), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.strength.strong.splash")
            .withLocalizedName("Strong Splash Strength Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16425), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.strength.long.splash")
            .withLocalizedName("Stretched Splash Strength Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16457), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.regen")
            .withLocalizedName("Regenerating Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8193), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.regen.strong")
            .withLocalizedName("Strong Regenerating Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8225), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.regen.long")
            .withLocalizedName("Stretched Regenerating Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8257), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.regen.splash")
            .withLocalizedName("Splash Regenerating Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16385), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.regen.strong.splash")
            .withLocalizedName("Strong Splash Regenerating Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16417), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.regen.long.splash")
            .withLocalizedName("Stretched Splash Regenerating Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16449), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.poison")
            .withLocalizedName("Poisonous Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8196), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.poison.strong")
            .withLocalizedName("Strong Poisonous Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8228), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.poison.long")
            .withLocalizedName("Stretched Poisonous Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8260), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.poison.splash")
            .withLocalizedName("Splash Poisonous Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16388), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.poison.strong.splash")
            .withLocalizedName("Strong Splash Poisonous Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16420), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.poison.long.splash")
            .withLocalizedName("Stretched Splash Poisonous Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16452), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.fireresistance")
            .withLocalizedName("Fire Resistant Brew")
            .withStateAndTemperature(LIQUID, 375)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8195), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.fireresistance.long")
            .withLocalizedName("Stretched Fire Resistant Brew")
            .withStateAndTemperature(LIQUID, 375)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8259), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.fireresistance.splash")
            .withLocalizedName("Splash Fire Resistant Brew")
            .withStateAndTemperature(LIQUID, 375)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16387), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.fireresistance.long.splash")
            .withLocalizedName("Stretched Splash Fire Resistant Brew")
            .withStateAndTemperature(LIQUID, 375)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16451), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.nightvision")
            .withLocalizedName("Night Vision Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8198), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.nightvision.long")
            .withLocalizedName("Stretched Night Vision Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8262), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.nightvision.splash")
            .withLocalizedName("Splash Night Vision Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16390), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.nightvision.long.splash")
            .withLocalizedName("Stretched Splash Night Vision Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16454), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.weakness")
            .withLocalizedName("Weakening Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8200), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.weakness.long")
            .withLocalizedName("Stretched Weakening Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8264), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.weakness.splash")
            .withLocalizedName("Splash Weakening Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16392), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.weakness.long.splash")
            .withLocalizedName("Stretched Splash Weakening Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16456), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.slowness")
            .withLocalizedName("Lame Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8202), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.slowness.long")
            .withLocalizedName("Stretched Lame Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8266), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.slowness.splash")
            .withLocalizedName("Splash Lame Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16394), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.slowness.long.splash")
            .withLocalizedName("Stretched Splash Lame Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16458), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.waterbreathing")
            .withLocalizedName("Fishy Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8205), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.waterbreathing.long")
            .withLocalizedName("Stretched Fishy Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8269), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.waterbreathing.splash")
            .withLocalizedName("Splash Fishy Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16397), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.waterbreathing.long.splash")
            .withLocalizedName("Stretched Splash Fishy Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16461), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.invisibility")
            .withLocalizedName("Invisible Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8206), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.invisibility.long")
            .withLocalizedName("Stretched Invisible Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 8270), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.invisibility.splash")
            .withLocalizedName("Splash Invisible Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16398), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.invisibility.long.splash")
            .withLocalizedName("Stretched Splash Invisible Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(new ItemStack(Items.potionitem, 1, 16462), ItemList.Bottle_Empty.get(1L));

        GT_FluidFactory.builder("potion.purpledrink")
            .withLocalizedName("Purple Drink")
            .withStateAndTemperature(LIQUID, 275)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Purple_Drink.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.grapejuice")
            .withLocalizedName("Grape Juice")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Grape_Juice.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.wine")
            .withLocalizedName("Wine")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Wine.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.vinegar")
            .withLocalizedName("Vinegar")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .configureMaterials(Materials.Vinegar)
            .registerPContainers(ItemList.Bottle_Vinegar.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.potatojuice")
            .withLocalizedName("Potato Juice")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Potato_Juice.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.vodka")
            .withLocalizedName("Vodka")
            .withStateAndTemperature(LIQUID, 275)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Vodka.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.leninade")
            .withLocalizedName("Leninade")
            .withStateAndTemperature(LIQUID, 275)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Leninade.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.mineralwater")
            .withLocalizedName("Mineral Water")
            .withStateAndTemperature(LIQUID, 275)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Mineral_Water.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.saltywater")
            .withLocalizedName("Salty Water")
            .withStateAndTemperature(LIQUID, 275)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Salty_Water.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.reedwater")
            .withLocalizedName("Reed Water")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Reed_Water.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.rum")
            .withLocalizedName("Rum")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Rum.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.piratebrew")
            .withLocalizedName("Pirate Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Pirate_Brew.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.hopsjuice")
            .withLocalizedName("Hops Juice")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Hops_Juice.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.darkbeer")
            .withLocalizedName("Dark Beer")
            .withStateAndTemperature(LIQUID, 275)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Dark_Beer.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.dragonblood")
            .withLocalizedName("Dragon Blood")
            .withStateAndTemperature(LIQUID, 375)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Dragon_Blood.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.wheatyjuice")
            .withLocalizedName("Wheaty Juice")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Wheaty_Juice.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.scotch")
            .withLocalizedName("Scotch")
            .withStateAndTemperature(LIQUID, 275)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Scotch.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.glenmckenner")
            .withLocalizedName("Glen McKenner")
            .withStateAndTemperature(LIQUID, 275)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Glen_McKenner.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.wheatyhopsjuice")
            .withLocalizedName("Wheaty Hops Juice")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Wheaty_Hops_Juice.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.beer")
            .withLocalizedName("Beer")
            .withStateAndTemperature(LIQUID, 275)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Beer.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.chillysauce")
            .withLocalizedName("Chilly Sauce")
            .withStateAndTemperature(LIQUID, 375)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Chilly_Sauce.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.hotsauce")
            .withLocalizedName("Hot Sauce")
            .withStateAndTemperature(LIQUID, 380)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Hot_Sauce.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.diabolosauce")
            .withLocalizedName("Diabolo Sauce")
            .withStateAndTemperature(LIQUID, 385)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Diabolo_Sauce.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.diablosauce")
            .withLocalizedName("Diablo Sauce")
            .withStateAndTemperature(LIQUID, 390)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Diablo_Sauce.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.diablosauce.strong")
            .withLocalizedName("Old Man Snitches glitched Diablo Sauce")
            .withStateAndTemperature(LIQUID, 999)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Snitches_Glitch_Sauce.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.applejuice")
            .withLocalizedName("Apple Juice")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Apple_Juice.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.cider")
            .withLocalizedName("Cider")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Cider.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.goldenapplejuice")
            .withLocalizedName("Golden Apple Juice")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Golden_Apple_Juice.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.goldencider")
            .withLocalizedName("Golden Cider")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Golden_Cider.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.idunsapplejuice")
            .withLocalizedName("Idun's Apple Juice")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Iduns_Apple_Juice.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.notchesbrew")
            .withLocalizedName("Notches Brew")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Notches_Brew.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.lemonjuice")
            .withLocalizedName("Lemon Juice")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Lemon_Juice.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.limoncello")
            .withLocalizedName("Limoncello")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Limoncello.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.lemonade")
            .withLocalizedName("Lemonade")
            .withStateAndTemperature(LIQUID, 275)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Lemonade.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.alcopops")
            .withLocalizedName("Alcopops")
            .withStateAndTemperature(LIQUID, 275)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Alcopops.get(1L), ItemList.Bottle_Empty.get(1L));
        GT_FluidFactory.builder("potion.cavejohnsonsgrenadejuice")
            .withLocalizedName("Cave Johnsons Grenade Juice")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(ItemList.Bottle_Cave_Johnsons_Grenade_Juice.get(1L), ItemList.Bottle_Empty.get(1L));

        GT_FluidFactory.builder("potion.darkcoffee")
            .withLocalizedName("Dark Coffee")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(ItemList.ThermosCan_Dark_Coffee.get(1L), ItemList.ThermosCan_Empty.get(1L));
        GT_FluidFactory.builder("potion.darkcafeaulait")
            .withLocalizedName("Dark Cafe au lait")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(ItemList.ThermosCan_Dark_Cafe_au_lait.get(1L), ItemList.ThermosCan_Empty.get(1L));
        GT_FluidFactory.builder("potion.coffee")
            .withLocalizedName("Coffee")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(ItemList.ThermosCan_Coffee.get(1L), ItemList.ThermosCan_Empty.get(1L));
        GT_FluidFactory.builder("potion.cafeaulait")
            .withLocalizedName("Cafe au lait")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(ItemList.ThermosCan_Cafe_au_lait.get(1L), ItemList.ThermosCan_Empty.get(1L));
        GT_FluidFactory.builder("potion.laitaucafe")
            .withLocalizedName("Lait au cafe")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(ItemList.ThermosCan_Lait_au_cafe.get(1L), ItemList.ThermosCan_Empty.get(1L));
        GT_FluidFactory.builder("potion.darkchocolatemilk")
            .withLocalizedName("Bitter Chocolate Milk")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(ItemList.ThermosCan_Dark_Chocolate_Milk.get(1L), ItemList.ThermosCan_Empty.get(1L));
        GT_FluidFactory.builder("potion.chocolatemilk")
            .withLocalizedName("Chocolate Milk")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(ItemList.ThermosCan_Chocolate_Milk.get(1L), ItemList.ThermosCan_Empty.get(1L));
        GT_FluidFactory.builder("potion.tea")
            .withLocalizedName("Tea")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(ItemList.ThermosCan_Tea.get(1L), ItemList.ThermosCan_Empty.get(1L));
        GT_FluidFactory.builder("potion.sweettea")
            .withLocalizedName("Sweet Tea")
            .withStateAndTemperature(LIQUID, 295)
            .buildAndRegister()
            .registerPContainers(ItemList.ThermosCan_Sweet_Tea.get(1L), ItemList.ThermosCan_Empty.get(1L));
        GT_FluidFactory.builder("potion.icetea")
            .withLocalizedName("Ice Tea")
            .withStateAndTemperature(LIQUID, 255)
            .buildAndRegister()
            .registerPContainers(ItemList.ThermosCan_Ice_Tea.get(1L), ItemList.ThermosCan_Empty.get(1L));
        GT_FluidFactory.builder("liquid_sodium")
            .withLocalizedName("Liquid Sodium")
            .withStateAndTemperature(LIQUID, 495)
            .buildAndRegister()
            .configureMaterials(Materials.Sodium)
            .registerBContainers(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Sodium, 1L),
                ItemList.Cell_Empty.get(1L));

        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                FluidRegistry.getFluidStack("potion.poison.strong", 750),
                ItemList.IC2_Spray_WeedEx.get(1L),
                ItemList.Spray_Empty.get(1L)));

        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                FluidRegistry.getFluidStack("potion.poison", 125),
                ItemList.Arrow_Head_Glass_Poison.get(1L),
                ItemList.Arrow_Head_Glass_Emtpy.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                FluidRegistry.getFluidStack("potion.poison.long", 125),
                ItemList.Arrow_Head_Glass_Poison_Long.get(1L),
                ItemList.Arrow_Head_Glass_Emtpy.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                FluidRegistry.getFluidStack("potion.poison.strong", 125),
                ItemList.Arrow_Head_Glass_Poison_Strong.get(1L),
                ItemList.Arrow_Head_Glass_Emtpy.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                FluidRegistry.getFluidStack("potion.slowness", 125),
                ItemList.Arrow_Head_Glass_Slowness.get(1L),
                ItemList.Arrow_Head_Glass_Emtpy.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                FluidRegistry.getFluidStack("potion.slowness.long", 125),
                ItemList.Arrow_Head_Glass_Slowness_Long.get(1L),
                ItemList.Arrow_Head_Glass_Emtpy.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                FluidRegistry.getFluidStack("potion.weakness", 125),
                ItemList.Arrow_Head_Glass_Weakness.get(1L),
                ItemList.Arrow_Head_Glass_Emtpy.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                FluidRegistry.getFluidStack("potion.weakness.long", 125),
                ItemList.Arrow_Head_Glass_Weakness_Long.get(1L),
                ItemList.Arrow_Head_Glass_Emtpy.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                FluidRegistry.getFluidStack("holywater", 125),
                ItemList.Arrow_Head_Glass_Holy_Water.get(1L),
                ItemList.Arrow_Head_Glass_Emtpy.get(1L)));

        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                FluidRegistry.getFluidStack("potion.poison", 125),
                ItemList.Arrow_Wooden_Glass_Poison.get(1L),
                ItemList.Arrow_Wooden_Glass_Emtpy.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                FluidRegistry.getFluidStack("potion.poison.long", 125),
                ItemList.Arrow_Wooden_Glass_Poison_Long.get(1L),
                ItemList.Arrow_Wooden_Glass_Emtpy.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                FluidRegistry.getFluidStack("potion.poison.strong", 125),
                ItemList.Arrow_Wooden_Glass_Poison_Strong.get(1L),
                ItemList.Arrow_Wooden_Glass_Emtpy.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                FluidRegistry.getFluidStack("potion.slowness", 125),
                ItemList.Arrow_Wooden_Glass_Slowness.get(1L),
                ItemList.Arrow_Wooden_Glass_Emtpy.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                FluidRegistry.getFluidStack("potion.slowness.long", 125),
                ItemList.Arrow_Wooden_Glass_Slowness_Long.get(1L),
                ItemList.Arrow_Wooden_Glass_Emtpy.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                FluidRegistry.getFluidStack("potion.weakness", 125),
                ItemList.Arrow_Wooden_Glass_Weakness.get(1L),
                ItemList.Arrow_Wooden_Glass_Emtpy.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                FluidRegistry.getFluidStack("potion.weakness.long", 125),
                ItemList.Arrow_Wooden_Glass_Weakness_Long.get(1L),
                ItemList.Arrow_Wooden_Glass_Emtpy.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                FluidRegistry.getFluidStack("holywater", 125),
                ItemList.Arrow_Wooden_Glass_Holy_Water.get(1L),
                ItemList.Arrow_Wooden_Glass_Emtpy.get(1L)));

        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                FluidRegistry.getFluidStack("potion.poison", 125),
                ItemList.Arrow_Plastic_Glass_Poison.get(1L),
                ItemList.Arrow_Plastic_Glass_Emtpy.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                FluidRegistry.getFluidStack("potion.poison.long", 125),
                ItemList.Arrow_Plastic_Glass_Poison_Long.get(1L),
                ItemList.Arrow_Plastic_Glass_Emtpy.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                FluidRegistry.getFluidStack("potion.poison.strong", 125),
                ItemList.Arrow_Plastic_Glass_Poison_Strong.get(1L),
                ItemList.Arrow_Plastic_Glass_Emtpy.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                FluidRegistry.getFluidStack("potion.slowness", 125),
                ItemList.Arrow_Plastic_Glass_Slowness.get(1L),
                ItemList.Arrow_Plastic_Glass_Emtpy.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                FluidRegistry.getFluidStack("potion.slowness.long", 125),
                ItemList.Arrow_Plastic_Glass_Slowness_Long.get(1L),
                ItemList.Arrow_Plastic_Glass_Emtpy.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                FluidRegistry.getFluidStack("potion.weakness", 125),
                ItemList.Arrow_Plastic_Glass_Weakness.get(1L),
                ItemList.Arrow_Plastic_Glass_Emtpy.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                FluidRegistry.getFluidStack("potion.weakness.long", 125),
                ItemList.Arrow_Plastic_Glass_Weakness_Long.get(1L),
                ItemList.Arrow_Plastic_Glass_Emtpy.get(1L)));
        FluidContainerRegistry.registerFluidContainer(
            new FluidContainerRegistry.FluidContainerData(
                FluidRegistry.getFluidStack("holywater", 125),
                ItemList.Arrow_Plastic_Glass_Holy_Water.get(1L),
                ItemList.Arrow_Plastic_Glass_Emtpy.get(1L)));
        if (!GT_Values.D1) {
            try {
                Class.forName("codechicken.nei.api.API");
                GT_Log.out.println("GT_Mod: Hiding certain Items from NEI.");
                API.hideItem(ItemList.Display_Fluid.getWildcard(1L));
            } catch (Throwable e) {
                if (GT_Values.D1) {
                    e.printStackTrace(GT_Log.err);
                }
            }
        }

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.cobblestone, 1, WILDCARD))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1L))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.gravel, 1, WILDCARD))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1L), new ItemStack(Items.flint, 1))
            .outputChances(10000, 1000)
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.furnace, 1, WILDCARD))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 8L))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.lit_furnace, 1, WILDCARD))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 8L))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        GT_OreDictUnificator.set(
            OrePrefixes.ingot,
            Materials.FierySteel,
            GT_ModHandler.getModItem(TwilightForest.ID, "item.fieryIngot", 1L, 0));
        GT_OreDictUnificator.set(
            OrePrefixes.ingot,
            Materials.Knightmetal,
            GT_ModHandler.getModItem(TwilightForest.ID, "item.knightMetal", 1L, 0));
        GT_OreDictUnificator.set(
            OrePrefixes.ingot,
            Materials.Steeleaf,
            GT_ModHandler.getModItem(TwilightForest.ID, "item.steeleafIngot", 1L, 0));
        GT_OreDictUnificator.set(
            OrePrefixes.ingot,
            Materials.IronWood,
            GT_ModHandler.getModItem(TwilightForest.ID, "item.ironwoodIngot", 1L, 0));
        GT_OreDictUnificator
            .set(OrePrefixes.gem, Materials.InfusedAir, GT_ModHandler.getModItem(Thaumcraft.ID, "ItemShard", 1L, 0));
        GT_OreDictUnificator
            .set(OrePrefixes.gem, Materials.InfusedFire, GT_ModHandler.getModItem(Thaumcraft.ID, "ItemShard", 1L, 1));
        GT_OreDictUnificator
            .set(OrePrefixes.gem, Materials.InfusedWater, GT_ModHandler.getModItem(Thaumcraft.ID, "ItemShard", 1L, 2));
        GT_OreDictUnificator
            .set(OrePrefixes.gem, Materials.InfusedEarth, GT_ModHandler.getModItem(Thaumcraft.ID, "ItemShard", 1L, 3));
        GT_OreDictUnificator
            .set(OrePrefixes.gem, Materials.InfusedOrder, GT_ModHandler.getModItem(Thaumcraft.ID, "ItemShard", 1L, 4));
        GT_OreDictUnificator.set(
            OrePrefixes.gem,
            Materials.InfusedEntropy,
            GT_ModHandler.getModItem(Thaumcraft.ID, "ItemShard", 1L, 5));
        GT_OreDictUnificator
            .set(OrePrefixes.nugget, Materials.Mercury, GT_ModHandler.getModItem(Thaumcraft.ID, "ItemNugget", 1L, 5));
        GT_OreDictUnificator
            .set(OrePrefixes.nugget, Materials.Thaumium, GT_ModHandler.getModItem(Thaumcraft.ID, "ItemNugget", 1L, 6));
        GT_OreDictUnificator
            .set(OrePrefixes.ingot, Materials.Thaumium, GT_ModHandler.getModItem(Thaumcraft.ID, "ItemResource", 1L, 2));
        GT_OreDictUnificator
            .set(OrePrefixes.gem, Materials.Mercury, GT_ModHandler.getModItem(Thaumcraft.ID, "ItemResource", 1L, 3));
        GT_OreDictUnificator
            .set(OrePrefixes.gem, Materials.Amber, GT_ModHandler.getModItem(Thaumcraft.ID, "ItemResource", 1L, 6));
        GT_OreDictUnificator
            .set(OrePrefixes.gem, Materials.Firestone, GT_ModHandler.getModItem(Railcraft.ID, "firestone.raw", 1L));

        GT_OreDictUnificator
            .set(OrePrefixes.nugget, Materials.Void, GT_ModHandler.getModItem(Thaumcraft.ID, "ItemNugget", 1L, 7));
        GT_OreDictUnificator
            .set(OrePrefixes.ingot, Materials.Void, GT_ModHandler.getModItem(Thaumcraft.ID, "ItemResource", 1L, 16));

        if (GregTech_API.sUnification
            .get(ConfigCategories.specialunificationtargets + "." + "railcraft", "plateIron", true)) {
            GT_OreDictUnificator
                .set(OrePrefixes.plate, Materials.Iron, GT_ModHandler.getModItem(Railcraft.ID, "part.plate", 1L, 0));
        } else {
            GT_OreDictUnificator.set(
                OrePrefixes.plate,
                Materials.Iron,
                GT_ModHandler.getModItem(Railcraft.ID, "part.plate", 1L, 0),
                false,
                false);
        }

        if (GregTech_API.sUnification
            .get(ConfigCategories.specialunificationtargets + "." + "railcraft", "plateSteel", true)) {
            GT_OreDictUnificator
                .set(OrePrefixes.plate, Materials.Steel, GT_ModHandler.getModItem(Railcraft.ID, "part.plate", 1L, 1));
        } else {
            GT_OreDictUnificator.set(
                OrePrefixes.plate,
                Materials.Steel,
                GT_ModHandler.getModItem(Railcraft.ID, "part.plate", 1L, 1),
                false,
                false);
        }

        if (GregTech_API.sUnification
            .get(ConfigCategories.specialunificationtargets + "." + "railcraft", "plateTinAlloy", true)) {
            GT_OreDictUnificator.set(
                OrePrefixes.plate,
                Materials.TinAlloy,
                GT_ModHandler.getModItem(Railcraft.ID, "part.plate", 1L, 2));
        } else {
            GT_OreDictUnificator.set(
                OrePrefixes.plate,
                Materials.TinAlloy,
                GT_ModHandler.getModItem(Railcraft.ID, "part.plate", 1L, 2),
                false,
                false);
        }

        if (GregTech_API.sUnification
            .get(ConfigCategories.specialunificationtargets + "." + "railcraft", "plateCopper", true)) {
            GT_OreDictUnificator
                .set(OrePrefixes.plate, Materials.Copper, GT_ModHandler.getModItem(Railcraft.ID, "part.plate", 1L, 3));
        } else {
            GT_OreDictUnificator.set(
                OrePrefixes.plate,
                Materials.Copper,
                GT_ModHandler.getModItem(Railcraft.ID, "part.plate", 1L, 3),
                false,
                false);
        }

        GT_OreDictUnificator.set(
            OrePrefixes.dust,
            Materials.Cocoa,
            GT_ModHandler.getModItem(PamsHarvestCraft.ID, "cocoapowderItem", 1L, 0));
        GT_OreDictUnificator.set(OrePrefixes.dust, Materials.Coffee, ItemList.IC2_CoffeePowder.get(1L));

        GregTech_API
            .registerMachineBlock(GT_Utility.getBlockFromStack(GT_ModHandler.getIC2Item("reinforcedGlass", 0)), 0);

        GregTech_API.sSolenoidCoilCasings = new GT_Cyclotron_Coils();
        ItemList.TierdDrone0
            .set(new GT_TierDrone("tierdDrone0", "Drone (Level 1)", "Quadcopter Stable Small Aircraft", 1));
        ItemList.TierdDrone1
            .set(new GT_TierDrone("tierdDrone1", "Drone (Level 2)", "Dual Turbo High-Ejection Medium Aircraft", 2));
        ItemList.TierdDrone2
            .set(new GT_TierDrone("tierdDrone2", "Drone (Level 3)", "Single Engine Anti-Gravity Large Aircraft", 3));

    }
}
