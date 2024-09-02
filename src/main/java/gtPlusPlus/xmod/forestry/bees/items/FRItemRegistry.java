/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir. All rights reserved. This program and the accompanying materials are made
 * available under the terms of the GNU Lesser Public License v3 which accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to: SirSengir (original work), CovertJaguar, Player, Binnie,
 * MysteriousAges
 ******************************************************************************/
package gtPlusPlus.xmod.forestry.bees.items;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import forestry.core.utils.StringUtil;
import gregtech.api.enums.Mods;

public class FRItemRegistry {

    // ----- Apiary Frames ----------------------

    // Magic Bee Frame Items
    public static MBItemFrame hiveFrameAccelerated;
    public static MBItemFrame hiveFrameVoid;
    public static MBItemFrame hiveFrameMutagenic;
    public static MBItemFrame hiveFrameBusy;

    // Extra Bee Frame Items
    public static MBItemFrame hiveFrameCocoa;
    public static MBItemFrame hiveFrameCaged;
    public static MBItemFrame hiveFrameSoul;
    public static MBItemFrame hiveFrameClay;
    public static MBItemFrame hiveFrameNova;

    // Frame Items added by bartimaeusnek
    public static MBItemFrame hiveFrameDecay;
    public static MBItemFrame hiveFrameSlow;
    public static MBItemFrame hiveFrameStalilize;
    public static MBItemFrame hiveFrameArborist;

    @Optional.Method(modid = Mods.Names.FORESTRY)
    public static void Register() {

        // Forestry Frames
        // frameUntreated = registerItem(new FR_ItemHiveFrame(80, 0.9f), "frameUntreated");
        // frameImpregnated = registerItem(new FR_ItemHiveFrame(240, 0.4f), "frameImpregnated");
        // frameProven = registerItem(new FR_ItemHiveFrame(720, 0.3f), "frameProven");

        // Magic Bee like Frames

        hiveFrameVoid = new MBItemFrame(
            MBFrameType.USELESS,
            EnumRarity.common,
            "No more cheaty frames for GTNH players.");

        hiveFrameAccelerated = new MBItemFrame(
            MBFrameType.ACCELERATED,
            "Longevity for bees isn't very common, especially if they're working harder.");
        hiveFrameMutagenic = new MBItemFrame(
            MBFrameType.MUTAGENIC,
            EnumRarity.epic,
            "Evolution of the fittest, finest and fastest.");
        hiveFrameBusy = new MBItemFrame(MBFrameType.BUSY, "Your bee will work harder and longer than you expected.");
        ChestGenHooks.addItem(
            ChestGenHooks.STRONGHOLD_CORRIDOR,
            new WeightedRandomChestContent(new ItemStack(hiveFrameVoid), 1, 1, 14));
        ChestGenHooks.addItem(
            ChestGenHooks.STRONGHOLD_LIBRARY,
            new WeightedRandomChestContent(new ItemStack(hiveFrameVoid), 1, 3, 18));
        ChestGenHooks.addItem(
            ChestGenHooks.DUNGEON_CHEST,
            new WeightedRandomChestContent(new ItemStack(hiveFrameVoid), 1, 1, 14));
        ChestGenHooks.addItem(
            ChestGenHooks.MINESHAFT_CORRIDOR,
            new WeightedRandomChestContent(new ItemStack(hiveFrameVoid), 1, 1, 9));
        ChestGenHooks.addItem(
            ChestGenHooks.PYRAMID_DESERT_CHEST,
            new WeightedRandomChestContent(new ItemStack(hiveFrameMutagenic), 1, 1, 9));
        ChestGenHooks.addItem(
            ChestGenHooks.PYRAMID_JUNGLE_CHEST,
            new WeightedRandomChestContent(new ItemStack(hiveFrameMutagenic), 1, 3, 12));
        ChestGenHooks.addItem(
            ChestGenHooks.MINESHAFT_CORRIDOR,
            new WeightedRandomChestContent(new ItemStack(hiveFrameMutagenic), 1, 3, 8));
        ChestGenHooks.addItem(
            ChestGenHooks.DUNGEON_CHEST,
            new WeightedRandomChestContent(new ItemStack(hiveFrameMutagenic), 1, 3, 12));

        // Frame Items added by bartimaeusnek
        hiveFrameDecay = new MBItemFrame(
            MBFrameType.DECAYING,
            EnumRarity.uncommon,
            "Who really needs stable genetics?");
        hiveFrameSlow = new MBItemFrame(MBFrameType.SLOWING, EnumRarity.common, "The journey is its own reward.");
        hiveFrameStalilize = new MBItemFrame(
            MBFrameType.STABILIZING,
            EnumRarity.rare,
            "If you wish your bees to keep their form.");
        hiveFrameArborist = new MBItemFrame(
            MBFrameType.ARBORISTS,
            EnumRarity.common,
            "Who need Bees when you can have Trees?");
        ChestGenHooks.addItem(
            ChestGenHooks.PYRAMID_JUNGLE_CHEST,
            new WeightedRandomChestContent(new ItemStack(hiveFrameArborist), 1, 4, 24));
        ChestGenHooks.addItem(
            ChestGenHooks.STRONGHOLD_LIBRARY,
            new WeightedRandomChestContent(new ItemStack(hiveFrameSlow), 1, 4, 24));
    }

    protected static <T extends Item> T registerItem(final T item, final String name) {
        item.setUnlocalizedName(name);
        GameRegistry.registerItem(item, StringUtil.cleanItemName(item));
        return item;
    }
}
