package gregtech.api.material;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.ruling_0.materiallib.api.Property;

/// Pins each defaulted [GTMaterialProperties] key to the value its [MU]/[MUOre] accessor returns for a null
/// material. The two are the same constant expressed twice -- the accessors read the property directly and
/// rely on its default for a material that sets none -- so a key whose default drifts from its accessor
/// silently changes what every unset material reports.
public class GTMaterialPropertyDefaultsTest {

    private static Map<Property<?>, Object> declaredDefaults() {
        Map<Property<?>, Object> expected = new LinkedHashMap<>();
        expected.put(GTMaterialProperties.BLAST_REQUIRED, false);
        expected.put(GTMaterialProperties.CAN_BE_CRACKED, false);
        expected.put(GTMaterialProperties.HAS_CENTRIFUGE_RECIPE, false);
        expected.put(GTMaterialProperties.HAS_CORRESPONDING_FLUID, false);
        expected.put(GTMaterialProperties.HAS_CORRESPONDING_GAS, false);
        expected.put(GTMaterialProperties.HAS_ELECTROLYZER_RECIPE, false);
        expected.put(GTMaterialProperties.AUTO_BLAST_FURNACE_RECIPES, true);
        expected.put(GTMaterialProperties.AUTO_RECYCLE_RECIPES, true);
        expected.put(GTMaterialProperties.AUTO_VACUUM_FREEZER_RECIPES, true);
        expected.put(GTMaterialProperties.UNIFIABLE, true);
        expected.put(GTMaterialProperties.BLAST_TEMP, 0);
        expected.put(GTMaterialProperties.DURABILITY, 0);
        expected.put(GTMaterialProperties.FUEL_POWER, 0);
        expected.put(GTMaterialProperties.FUEL_TYPE, 0);
        expected.put(GTMaterialProperties.MELTING_POINT, 0);
        expected.put(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU, 0);
        expected.put(GTMaterialProperties.TIER, 0);
        expected.put(GTMaterialProperties.TOOL_QUALITY, 0);
        expected.put(GTMaterialProperties.BYPRODUCT_MULTIPLIER, 1);
        expected.put(GTMaterialProperties.ORE_MULTIPLIER, 1);
        expected.put(GTMaterialProperties.SMELTING_MULTIPLIER, 1);
        expected.put(GTMaterialProperties.VOLTAGE_MULTIPLIER, 16L);
        expected.put(GTMaterialProperties.TOOL_SPEED, 1.0f);
        expected.put(GTMaterialProperties.HEAT_DAMAGE, 0f);
        return expected;
    }

    @Test
    void keysDeclareTheDefaultTheirAccessorReturns() {
        declaredDefaults()
            .forEach((property, expected) -> assertEquals(expected, property.getDefaultValue(), property.toString()));
    }

    @Test
    void nullMaterialAgreesWithTheDeclaredDefault() {
        assertEquals(GTMaterialProperties.BLAST_REQUIRED.getDefaultValue(), MU.blastFurnaceRequired(null));
        assertEquals(GTMaterialProperties.CAN_BE_CRACKED.getDefaultValue(), MU.canBeCracked(null));
        assertEquals(GTMaterialProperties.HAS_CENTRIFUGE_RECIPE.getDefaultValue(), MU.hasCentrifugeRecipe(null));
        assertEquals(GTMaterialProperties.HAS_CORRESPONDING_FLUID.getDefaultValue(), MU.hasCorrespondingFluid(null));
        assertEquals(GTMaterialProperties.HAS_CORRESPONDING_GAS.getDefaultValue(), MU.hasCorrespondingGas(null));
        assertEquals(GTMaterialProperties.HAS_ELECTROLYZER_RECIPE.getDefaultValue(), MU.hasElectrolyzerRecipe(null));
        assertEquals(
            GTMaterialProperties.AUTO_BLAST_FURNACE_RECIPES.getDefaultValue(),
            MU.autoGenerateBlastFurnaceRecipes(null));
        assertEquals(GTMaterialProperties.AUTO_RECYCLE_RECIPES.getDefaultValue(), MU.autoGenerateRecycleRecipes(null));
        assertEquals(
            GTMaterialProperties.AUTO_VACUUM_FREEZER_RECIPES.getDefaultValue(),
            MU.autoGenerateVacuumFreezerRecipes(null));
        assertEquals(GTMaterialProperties.UNIFIABLE.getDefaultValue(), MU.unifiable(null));
        assertEquals(GTMaterialProperties.BLAST_TEMP.getDefaultValue(), MU.blastFurnaceTemp(null));
        assertEquals(GTMaterialProperties.DURABILITY.getDefaultValue(), MU.durability(null));
        assertEquals(GTMaterialProperties.FUEL_POWER.getDefaultValue(), MU.fuelPower(null));
        assertEquals(GTMaterialProperties.FUEL_TYPE.getDefaultValue(), MU.fuelType(null));
        assertEquals(GTMaterialProperties.MELTING_POINT.getDefaultValue(), MU.meltingPoint(null));
        assertEquals(
            GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU.getDefaultValue(),
            MU.processingMaterialTierEU(null));
        assertEquals(GTMaterialProperties.TIER.getDefaultValue(), MU.tier(null));
        assertEquals(GTMaterialProperties.TOOL_QUALITY.getDefaultValue(), MU.toolQuality(null));
        assertEquals(GTMaterialProperties.VOLTAGE_MULTIPLIER.getDefaultValue(), MU.voltageMultiplier(null));
        assertEquals(GTMaterialProperties.TOOL_SPEED.getDefaultValue(), MU.toolSpeed(null));
        assertEquals(GTMaterialProperties.HEAT_DAMAGE.getDefaultValue(), MU.heatDamage(null));
        assertEquals(GTMaterialProperties.BYPRODUCT_MULTIPLIER.getDefaultValue(), MUOre.byProductMultiplier(null));
        assertEquals(GTMaterialProperties.ORE_MULTIPLIER.getDefaultValue(), MUOre.oreMultiplier(null));
        assertEquals(GTMaterialProperties.SMELTING_MULTIPLIER.getDefaultValue(), MUOre.smeltingMultiplier(null));
    }

    /// `OLD_SUB_ID` must stay default-free: absence is meaningful for it, and
    /// [gregtech.api.enums.materials2.Materials2IDIndex] and
    /// [gregtech.api.enums.materials2.Materials2WerkstoffIndex] both branch on the property being unset. A
    /// default would make those tests unconditionally true. [MU#oldSubId]'s `-1` is its own sentinel.
    @Test
    void oldSubIdDeclaresNoDefault() {
        assertNull(GTMaterialProperties.OLD_SUB_ID.getDefaultValue());
        assertEquals(-1, MU.oldSubId(null));
    }
}
