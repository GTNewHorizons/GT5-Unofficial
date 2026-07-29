package gregtech.api.material;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.ruling_0.materiallib.api.Property;

/// Pins each defaulted [GTMaterialProperties] key to the value its [MaterialUtils] accessor returns for a null
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
        assertEquals(GTMaterialProperties.BLAST_REQUIRED.getDefaultValue(), MaterialUtils.blastFurnaceRequired(null));
        assertEquals(GTMaterialProperties.CAN_BE_CRACKED.getDefaultValue(), MaterialUtils.canBeCracked(null));
        assertEquals(
            GTMaterialProperties.HAS_CENTRIFUGE_RECIPE.getDefaultValue(),
            MaterialUtils.hasCentrifugeRecipe(null));
        assertEquals(
            GTMaterialProperties.HAS_CORRESPONDING_FLUID.getDefaultValue(),
            MaterialUtils.hasCorrespondingFluid(null));
        assertEquals(
            GTMaterialProperties.HAS_CORRESPONDING_GAS.getDefaultValue(),
            MaterialUtils.hasCorrespondingGas(null));
        assertEquals(
            GTMaterialProperties.HAS_ELECTROLYZER_RECIPE.getDefaultValue(),
            MaterialUtils.hasElectrolyzerRecipe(null));
        assertEquals(
            GTMaterialProperties.AUTO_BLAST_FURNACE_RECIPES.getDefaultValue(),
            MaterialUtils.autoGenerateBlastFurnaceRecipes(null));
        assertEquals(
            GTMaterialProperties.AUTO_RECYCLE_RECIPES.getDefaultValue(),
            MaterialUtils.autoGenerateRecycleRecipes(null));
        assertEquals(
            GTMaterialProperties.AUTO_VACUUM_FREEZER_RECIPES.getDefaultValue(),
            MaterialUtils.autoGenerateVacuumFreezerRecipes(null));
        assertEquals(GTMaterialProperties.UNIFIABLE.getDefaultValue(), MaterialUtils.unifiable(null));
        assertEquals(GTMaterialProperties.BLAST_TEMP.getDefaultValue(), MaterialUtils.blastFurnaceTemp(null));
        assertEquals(GTMaterialProperties.DURABILITY.getDefaultValue(), MaterialUtils.durability(null));
        assertEquals(GTMaterialProperties.FUEL_POWER.getDefaultValue(), MaterialUtils.fuelPower(null));
        assertEquals(GTMaterialProperties.FUEL_TYPE.getDefaultValue(), MaterialUtils.fuelType(null));
        assertEquals(GTMaterialProperties.MELTING_POINT.getDefaultValue(), MaterialUtils.meltingPoint(null));
        assertEquals(
            GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU.getDefaultValue(),
            MaterialUtils.processingMaterialTierEU(null));
        assertEquals(GTMaterialProperties.TIER.getDefaultValue(), MaterialUtils.tier(null));
        assertEquals(GTMaterialProperties.TOOL_QUALITY.getDefaultValue(), MaterialUtils.toolQuality(null));
        assertEquals(GTMaterialProperties.VOLTAGE_MULTIPLIER.getDefaultValue(), MaterialUtils.voltageMultiplier(null));
        assertEquals(GTMaterialProperties.TOOL_SPEED.getDefaultValue(), MaterialUtils.toolSpeed(null));
        assertEquals(GTMaterialProperties.HEAT_DAMAGE.getDefaultValue(), MaterialUtils.heatDamage(null));
        assertEquals(
            GTMaterialProperties.BYPRODUCT_MULTIPLIER.getDefaultValue(),
            MaterialUtils.byProductMultiplier(null));
        assertEquals(GTMaterialProperties.ORE_MULTIPLIER.getDefaultValue(), MaterialUtils.oreMultiplier(null));
        assertEquals(
            GTMaterialProperties.SMELTING_MULTIPLIER.getDefaultValue(),
            MaterialUtils.smeltingMultiplier(null));
    }

    /// `OLD_SUB_ID` must stay default-free: absence is meaningful for it, and
    /// [gregtech.api.enums.materials2.Materials2IDIndex] and
    /// [gregtech.api.enums.materials2.Materials2WerkstoffIndex] both branch on the property being unset. A
    /// default would make those tests unconditionally true. [MaterialUtils#oldSubId]'s `-1` is its own sentinel.
    @Test
    void oldSubIdDeclaresNoDefault() {
        assertNull(GTMaterialProperties.OLD_SUB_ID.getDefaultValue());
        assertEquals(-1, MaterialUtils.oldSubId(null));
    }
}
