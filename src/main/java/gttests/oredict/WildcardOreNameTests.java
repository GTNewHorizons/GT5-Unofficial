package gttests.oredict;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.gtnewhorizons.horizonqa.api.GameTestHelper;
import com.gtnewhorizons.horizonqa.api.annotation.GameTest;
import com.gtnewhorizons.horizonqa.api.annotation.GameTestHolder;
import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Wildcard;
import gregtech.api.material.MaterialParts;
import gregtech.api.util.GTUtility;

/// Pins that every member's part is registered under its wildcard's ore name, including the `gear` names that only
/// the `gearGt` alias produces.
@GameTestHolder(Mods.ModIDs.GREG_TECH)
public class WildcardOreNameTests {

    private static final OrePrefixes[] PREFIXES = { OrePrefixes.plate, OrePrefixes.stick, OrePrefixes.ring,
        OrePrefixes.screw, OrePrefixes.dust, OrePrefixes.ingot, OrePrefixes.foil, OrePrefixes.gearGt };

    @GameTest(timeoutTicks = 20, batch = "gt5.oredict")
    public static void membersPartsCarryTheWildcardNames(GameTestHelper helper) {
        List<String> missing = new ArrayList<>();
        for (Wildcard wildcard : Wildcard.values()) {
            for (Material member : wildcard.members()) {
                for (OrePrefixes prefix : PREFIXES) {
                    ItemStack part = MaterialParts.stack(prefix, member, 1);
                    if (part == null) continue;
                    check(missing, wildcard.oreDictName(prefix), part);
                    if (prefix == OrePrefixes.gearGt) check(missing, wildcard.oreDictName(OrePrefixes.gear), part);
                }
            }
        }
        helper.assertTrue(missing.isEmpty(), "Wildcard ore names lack member parts: " + missing);
        helper.succeed();
    }

    private static void check(List<String> missing, String oreName, ItemStack part) {
        for (ItemStack ore : OreDictionary.getOres(oreName)) {
            if (GTUtility.areStacksEqual(ore, part, true)) return;
        }
        missing.add(oreName + " -> " + part.getDisplayName());
    }
}
