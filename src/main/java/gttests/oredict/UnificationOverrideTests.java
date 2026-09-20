package gttests.oredict;

import static gregtech.GTLoggers.GT_FML_LOGGER;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.gtnewhorizons.horizonqa.api.GameTestHelper;
import com.gtnewhorizons.horizonqa.api.annotation.GameTest;
import com.gtnewhorizons.horizonqa.api.annotation.GameTestHolder;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Mods;
import gregtech.api.util.GTOreDictUnificator;

/// Pins the unification targets that `OreDictUnificationOverrides` assigns to GregTech. The override
/// table keys on the registering mod, and most competing registrations reach GregTech through
/// `GTProxy#catchUpPreExistingOreDictEntries` rather than a live event, so the replay's mod attribution
/// decides whether a foreign stack is able to claim a GregTech-preferred name.
@GameTestHolder(Mods.ModIDs.GREG_TECH)
public class UnificationOverrideTests {

    private static final String[] GREGTECH_PREFERRED = { "dustAlumina", "blockAmber", "blockLead", "blockMagnesium",
        "blockMithril", "blockPlatinum", "blockTungsten", "ingotBloodInfusedIron", "ingotIchorium", "nuggetIchorium",
        "rawOreMeteoricIron", "stickDesh", "cellCreosote" };

    @GameTest(timeoutTicks = 20, batch = "gt5.oredict")
    public static void gregTechPreferredNamesUnifyToGregTechItems(GameTestHelper helper) {
        List<String> foreignTargets = new ArrayList<>();
        for (String oreName : GREGTECH_PREFERRED) {
            if (OreDictionary.getOres(oreName)
                .isEmpty()) continue;
            ItemStack target = GTOreDictUnificator.get(oreName, 1);
            String owner = ownerOf(target);
            GT_FML_LOGGER.info("Unification target of {} is {} ({})", oreName, describe(target), owner);
            if (!"gregtech".equals(owner) && !"materiallib".equals(owner)) {
                foreignTargets.add(oreName + " -> " + describe(target) + " of " + owner + candidates(oreName));
            }
        }
        helper.assertTrue(
            foreignTargets.isEmpty(),
            "GregTech-preferred ore names unify to foreign items: " + foreignTargets);
        helper.succeed();
    }

    private static String ownerOf(ItemStack stack) {
        if (stack == null || stack.getItem() == null) return "none";
        GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(stack.getItem());
        return id == null ? "unknown" : id.modId;
    }

    private static String describe(ItemStack stack) {
        if (stack == null || stack.getItem() == null) return "null";
        GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(stack.getItem());
        return (id == null ? stack.getUnlocalizedName() : id.toString()) + "@" + stack.getItemDamage();
    }

    private static String candidates(String oreName) {
        List<String> entries = new ArrayList<>();
        for (ItemStack candidate : OreDictionary.getOres(oreName)) entries.add(describe(candidate));
        return " (registered: " + entries + ")";
    }
}
