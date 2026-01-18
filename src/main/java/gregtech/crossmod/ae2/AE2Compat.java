package gregtech.crossmod.ae2;

import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.WILDCARD;

import net.minecraft.item.ItemStack;

import appeng.api.AEApi;
import appeng.api.features.IBlockingModeIgnoreItemRegistry;
import appeng.api.storage.IExternalStorageRegistry;
import bartworks.system.material.WerkstoffLoader;
import gregtech.api.covers.CoverPlacer;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.AE2DigitalChestHandler;
import gregtech.api.objects.AE2NonconsumableHatchHandler;
import gregtech.common.covers.CoverFacadeAE;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputME;

public final class AE2Compat {

    public static void onPreInit() {
        AEApi.instance()
            .registries()
            .interfaceTerminal()
            .register(MTEHatchCraftingInputME.class);
    }

    public static void onPostInit() {
        registerCover();
        registerExternalStorage();
        registerBlockingModeIgnoreItems();
    }

    private static void registerCover() {
        ItemStack facade = AEApi.instance()
            .definitions()
            .items()
            .facade()
            .maybeItem()
            .transform(i -> new ItemStack(i, 1, WILDCARD))
            .orNull();
        if (facade != null) {
            CoverRegistry.registerCover(
                facade,
                null,
                CoverFacadeAE::new,
                CoverPlacer.builder()
                    .allowOnPrimitiveBlock()
                    .onlyPlaceIf(CoverFacadeAE.isCoverPlaceable)
                    .build());
        }
    }

    private static void registerExternalStorage() {
        IExternalStorageRegistry registry = AEApi.instance()
            .registries()
            .externalStorage();
        registry.addExternalStorageInterface(new AE2DigitalChestHandler());
        registry.addExternalStorageInterface(new AE2NonconsumableHatchHandler());
    }

    private static void registerBlockingModeIgnoreItems() {
        IBlockingModeIgnoreItemRegistry registry = AEApi.instance()
            .registries()
            .blockingModeIgnoreItem();

        ItemList[] nonConsumables = new ItemList[] { ItemList.Shape_Mold_Bottle, ItemList.Shape_Mold_Plate,
            ItemList.Shape_Mold_Ingot, ItemList.Shape_Mold_Casing, ItemList.Shape_Mold_Gear,
            ItemList.Shape_Mold_Gear_Small, ItemList.Shape_Mold_Credit, ItemList.Shape_Mold_Nugget,
            ItemList.Shape_Mold_Block, ItemList.Shape_Mold_Ball, ItemList.Shape_Mold_Bun, ItemList.Shape_Mold_Bread,
            ItemList.Shape_Mold_Baguette, ItemList.Shape_Mold_Cylinder, ItemList.Shape_Mold_Anvil,
            ItemList.Shape_Mold_Arrow, ItemList.Shape_Mold_Name, ItemList.Shape_Mold_Rod, ItemList.Shape_Mold_Bolt,
            ItemList.Shape_Mold_Round, ItemList.Shape_Mold_Screw, ItemList.Shape_Mold_Ring,
            ItemList.Shape_Mold_Rod_Long, ItemList.Shape_Mold_Rotor, ItemList.Shape_Mold_Turbine_Blade,
            ItemList.Shape_Mold_Pipe_Tiny, ItemList.Shape_Mold_Pipe_Small, ItemList.Shape_Mold_Pipe_Medium,
            ItemList.Shape_Mold_Pipe_Large, ItemList.Shape_Mold_Pipe_Huge, ItemList.Shape_Mold_ToolHeadDrill,

            ItemList.Shape_Slicer_Flat, ItemList.Shape_Slicer_Stripes, ItemList.Shape_Extruder_Bottle,
            ItemList.Shape_Extruder_Plate, ItemList.Shape_Extruder_Cell, ItemList.Shape_Extruder_Ring,
            ItemList.Shape_Extruder_Rod, ItemList.Shape_Extruder_Bolt, ItemList.Shape_Extruder_Ingot,
            ItemList.Shape_Extruder_Wire, ItemList.Shape_Extruder_Casing, ItemList.Shape_Extruder_Pipe_Tiny,
            ItemList.Shape_Extruder_Pipe_Small, ItemList.Shape_Extruder_Pipe_Medium, ItemList.Shape_Extruder_Pipe_Large,
            ItemList.Shape_Extruder_Pipe_Huge, ItemList.Shape_Extruder_Block, ItemList.Shape_Extruder_Sword,
            ItemList.Shape_Extruder_Pickaxe, ItemList.Shape_Extruder_Shovel, ItemList.Shape_Extruder_Axe,
            ItemList.Shape_Extruder_Hoe, ItemList.Shape_Extruder_Hammer, ItemList.Shape_Extruder_File,
            ItemList.Shape_Extruder_Saw, ItemList.Shape_Extruder_Gear, ItemList.Shape_Extruder_Rotor,
            ItemList.Shape_Extruder_Turbine_Blade, ItemList.Shape_Extruder_Small_Gear,
            ItemList.Shape_Extruder_ToolHeadDrill, };
        for (ItemList item : nonConsumables) {
            registry.register(item.get(1));
        }

        registry.register(ItemList.Circuit_Integrated.getItem());
        // GT lenses are registered by ProcessingLens
        registry.register(WerkstoffLoader.items.get(OrePrefixes.lens));
        if (NewHorizonsCoreMod.isModLoaded()) {
            registry.register(getModItem(NewHorizonsCoreMod.ID, "ReinforcedGlassLense"));
            registry.register(getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens"));
            registry.register(getModItem(NewHorizonsCoreMod.ID, "RadoxPolymerLens"));
            registry.register(getModItem(NewHorizonsCoreMod.ID, "ChromaticLens"));
        }
    }
}
