package gtPlusPlus.xmod.forestry.bees.items;

import net.minecraft.item.EnumRarity;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

import cpw.mods.fml.common.Optional;
import gregtech.api.enums.Mods;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class FRItemRegistry {

    @Optional.Method(modid = Mods.ModIDs.FORESTRY)
    public static void Register() {

        GregtechItemList.HiveFrameVoid
            .set(new MBItemFrame(MBFrameType.USELESS, EnumRarity.common, "gtpp.tooltip.frame.void"));

        GregtechItemList.HiveFrameAccelerated
            .set(new MBItemFrame(MBFrameType.ACCELERATED, "gtpp.tooltip.frame.accelerated"));

        GregtechItemList.HiveFrameMutagenic
            .set(new MBItemFrame(MBFrameType.MUTAGENIC, EnumRarity.epic, "gtpp.tooltip.frame.mutagenic"));

        GregtechItemList.HiveFrameBusy.set(new MBItemFrame(MBFrameType.BUSY, "gtpp.tooltip.frame.busy"));

        GregtechItemList.HiveFrameDecay
            .set(new MBItemFrame(MBFrameType.DECAYING, EnumRarity.uncommon, "gtpp.tooltip.frame.decaying"));

        GregtechItemList.HiveFrameSlow
            .set(new MBItemFrame(MBFrameType.SLOWING, EnumRarity.common, "gtpp.tooltip.frame.slowing"));

        GregtechItemList.HiveFrameStabilize
            .set(new MBItemFrame(MBFrameType.STABILIZING, EnumRarity.rare, "gtpp.tooltip.frame.stabilizing"));

        GregtechItemList.HiveFrameArborist
            .set(new MBItemFrame(MBFrameType.ARBORISTS, EnumRarity.common, "gtpp.tooltip.frame.arborists"));

        ChestGenHooks.addItem(
            ChestGenHooks.STRONGHOLD_CORRIDOR,
            new WeightedRandomChestContent(GregtechItemList.HiveFrameVoid.get(1), 1, 1, 14));
        ChestGenHooks.addItem(
            ChestGenHooks.STRONGHOLD_LIBRARY,
            new WeightedRandomChestContent(GregtechItemList.HiveFrameVoid.get(1), 1, 3, 18));
        ChestGenHooks.addItem(
            ChestGenHooks.DUNGEON_CHEST,
            new WeightedRandomChestContent(GregtechItemList.HiveFrameVoid.get(1), 1, 1, 14));
        ChestGenHooks.addItem(
            ChestGenHooks.MINESHAFT_CORRIDOR,
            new WeightedRandomChestContent(GregtechItemList.HiveFrameVoid.get(1), 1, 1, 9));

        ChestGenHooks.addItem(
            ChestGenHooks.PYRAMID_DESERT_CHEST,
            new WeightedRandomChestContent(GregtechItemList.HiveFrameMutagenic.get(1), 1, 1, 9));
        ChestGenHooks.addItem(
            ChestGenHooks.PYRAMID_JUNGLE_CHEST,
            new WeightedRandomChestContent(GregtechItemList.HiveFrameMutagenic.get(1), 1, 3, 12));
        ChestGenHooks.addItem(
            ChestGenHooks.MINESHAFT_CORRIDOR,
            new WeightedRandomChestContent(GregtechItemList.HiveFrameMutagenic.get(1), 1, 3, 8));
        ChestGenHooks.addItem(
            ChestGenHooks.DUNGEON_CHEST,
            new WeightedRandomChestContent(GregtechItemList.HiveFrameMutagenic.get(1), 1, 3, 12));

        ChestGenHooks.addItem(
            ChestGenHooks.PYRAMID_JUNGLE_CHEST,
            new WeightedRandomChestContent(GregtechItemList.HiveFrameArborist.get(1), 1, 4, 24));

        ChestGenHooks.addItem(
            ChestGenHooks.STRONGHOLD_LIBRARY,
            new WeightedRandomChestContent(GregtechItemList.HiveFrameSlow.get(1), 1, 4, 24));
    }
}
