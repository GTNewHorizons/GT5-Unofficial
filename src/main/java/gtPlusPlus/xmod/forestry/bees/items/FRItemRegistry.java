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
            .set(new MBItemFrame(MBFrameType.USELESS, EnumRarity.common, "No more cheaty frames for GTNH players."));

        GregtechItemList.HiveFrameAccelerated.set(
            new MBItemFrame(
                MBFrameType.ACCELERATED,
                "Longevity for bees isn't very common, especially if they're working harder."));

        GregtechItemList.HiveFrameMutagenic.set(
            new MBItemFrame(MBFrameType.MUTAGENIC, EnumRarity.epic, "Evolution of the fittest, finest and fastest."));

        GregtechItemList.HiveFrameBusy
            .set(new MBItemFrame(MBFrameType.BUSY, "Your bee will work harder and longer than you expected."));

        GregtechItemList.HiveFrameDecay
            .set(new MBItemFrame(MBFrameType.DECAYING, EnumRarity.uncommon, "Who really needs stable genetics?"));

        GregtechItemList.HiveFrameSlow
            .set(new MBItemFrame(MBFrameType.SLOWING, EnumRarity.common, "The journey is its own reward."));

        GregtechItemList.HiveFrameStabilize.set(
            new MBItemFrame(MBFrameType.STABILIZING, EnumRarity.rare, "If you wish your bees to keep their form."));

        GregtechItemList.HiveFrameArborist
            .set(new MBItemFrame(MBFrameType.ARBORISTS, EnumRarity.common, "Who need Bees when you can have Trees?"));

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
