package detrav.items.processing;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import detrav.DetravScannerMod;
import detrav.items.DetravToolItems;
import detrav.items.tools.DetravElectricProspectorItem;
import detrav.items.tools.DetravProspectorItem;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TCAspects;
import gregtech.api.util.GTModHandler;

/**
 * Created by wital_000 on 18.03.2016.
 */
public class ProcessingDetravToolProspector implements gregtech.api.interfaces.IOreRecipeRegistrator {

    private static final long RECIPE_BITS = GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
        | GTModHandler.RecipeBits.BUFFERED;

    public ProcessingDetravToolProspector() {
        OrePrefixes.toolHeadDrill.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials material, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (!aPrefix.doGenerateItem(material)) return;
        if (DetravScannerMod.DEBUG_ENABLED) return;

        addScannerRecipe(
            DetravToolItems.PROSPECTOR_LV,
            material,
            "cellSaltWater",
            "cellSulfuricAcid",
            Materials.LV,
            ItemList.Sensor_LV);
        addScannerRecipe(
            DetravToolItems.PROSPECTOR_MV,
            material,
            "cellSaltWater",
            "cellSulfuricAcid",
            Materials.MV,
            ItemList.Sensor_MV);
        addScannerRecipe(
            DetravToolItems.PROSPECTOR_HV,
            material,
            "cellSodiumPersulfate",
            "cellNitricAcid",
            Materials.HV,
            ItemList.Sensor_HV);
        addScannerRecipe(
            DetravToolItems.PROSPECTOR_EV,
            material,
            "cellSodiumPersulfate",
            "cellNitricAcid",
            Materials.EV,
            ItemList.Sensor_EV);
        addScannerRecipe(
            DetravToolItems.PROSPECTOR_IV,
            material,
            "cellSodiumPersulfate",
            "cellNitricAcid",
            Materials.IV,
            ItemList.Sensor_IV);
        addScannerRecipe(
            DetravToolItems.PROSPECTOR_LUV,
            material,
            "cellLithiumPeroxide",
            "cellHydrofluoricAcid",
            Materials.LuV,
            ItemList.Sensor_LuV);
        addScannerRecipe(
            DetravToolItems.PROSPECTOR_ZPM,
            material,
            "cellLithiumPeroxide",
            "cellHydrofluoricAcid",
            Materials.ZPM,
            ItemList.Sensor_ZPM);
        addScannerRecipe(
            DetravToolItems.PROSPECTOR_UV,
            material,
            "cellLithiumPeroxide",
            "cellHydrofluoricAcid",
            Materials.UV,
            ItemList.Sensor_UV);
        addScannerRecipe(
            DetravToolItems.PROSPECTOR_UHV,
            material,
            "cellHydrogenPeroxide",
            "cellHydrofluoricAcid",
            Materials.UHV,
            ItemList.Sensor_UHV);

        // The electric scanners are built from a fixed material per tier: the one the game used to hand out for that
        // tier. Registering them from inside this handler keeps them to materials that really do have a drill head
        // and a plate. There is no UHV battery in this repo, so the UHV scanner takes the UV one and the circuit and
        // sensor are what mark its tier.
        if (material == Materials.Iridium) {
            addElectricScannerRecipe(
                DetravToolItems.ELECTRIC_PROSPECTOR_LUV,
                material,
                Materials.LuV,
                ItemList.Sensor_LuV,
                OrePrefixes.battery.get(Materials.LuV));
        }
        if (material == Materials.Neutronium) {
            addElectricScannerRecipe(
                DetravToolItems.ELECTRIC_PROSPECTOR_ZPM,
                material,
                Materials.ZPM,
                ItemList.Sensor_ZPM,
                OrePrefixes.battery.get(Materials.ZPM));
            addElectricScannerRecipe(
                DetravToolItems.ELECTRIC_PROSPECTOR_UHV,
                material,
                Materials.UHV,
                ItemList.Sensor_UHV,
                OrePrefixes.battery.get(Materials.UV));
        }
        if (material == Materials.InfinityCatalyst) {
            addElectricScannerRecipe(
                DetravToolItems.ELECTRIC_PROSPECTOR_UV,
                material,
                Materials.UV,
                ItemList.Sensor_UV,
                OrePrefixes.battery.get(Materials.UV));
        }
        if (material == Materials.Infinity) {
            addElectricScannerRecipe(
                DetravToolItems.ELECTRIC_PROSPECTOR_UHV,
                material,
                Materials.UHV,
                ItemList.Sensor_UHV,
                OrePrefixes.battery.get(Materials.UV));
        }
    }

    /**
     * The hand scanner recipe, unchanged from the one every tier spelled out before: two chemical cells, a drill head
     * of the scanner's own material, two circuits, three plates and the tier's sensor.
     */
    private static void addScannerRecipe(DetravProspectorItem scanner, Materials material, String leftCell,
        String rightCell, Materials circuitTier, ItemList sensor) {
        ItemStack stack = scanner.registerMaterial(
            material,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
            new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));
        if (stack == null) return;
        GTModHandler.addCraftingRecipe(
            stack,
            RECIPE_BITS,
            new Object[] { "SHE", "CPC", "PXP", 'E', OreDictionary.getOres(rightCell)
                .get(0), 'S',
                OreDictionary.getOres(leftCell)
                    .get(0),
                'H', OrePrefixes.toolHeadDrill.get(material), 'P', OrePrefixes.plate.get(material), 'C',
                OrePrefixes.circuit.get(circuitTier), 'X', sensor });
    }

    /**
     * The electric scanner recipe. The old ones were never registered in this repo -- they came from the modpack's
     * own recipe mod, which cannot craft these items any more -- so this is the hand scanner's layout with the two
     * chemical cells dropped and the tier's battery worked in.
     */
    private static void addElectricScannerRecipe(DetravElectricProspectorItem scanner, Materials material,
        Materials circuitTier, ItemList sensor, Object battery) {
        ItemStack stack = scanner.registerMaterial(
            material,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
            new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L));
        if (stack == null) return;
        GTModHandler.addCraftingRecipe(
            stack,
            RECIPE_BITS,
            new Object[] { "CHC", "PBP", "PXP", 'H', OrePrefixes.toolHeadDrill.get(material), 'P',
                OrePrefixes.plate.get(material), 'C', OrePrefixes.circuit.get(circuitTier), 'B', battery, 'X',
                sensor });
    }
}
