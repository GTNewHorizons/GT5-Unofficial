/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bartworks.common.loaders;

import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.Mods.BloodArsenal;
import static gregtech.api.enums.Mods.Botania;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.enums.Mods.Thaumcraft;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.tuple.Pair;

import gregtech.api.GregTechAPI;
import gregtech.api.util.GlassTier;
import tectech.thing.block.BlockQuantumGlass;

// Register all your glasses here.
public class RegisterGlassTiers {

    public static void run() {
        registerGlassAsTiered();
        registerGlassOreDicts();
    }

    private static void registerGlassAsTiered() {

        // --- HV ---
        GlassTier.addCustomGlass(ItemRegistry.bw_realglas, 0, 3, 0);
        // Stained boro glass
        for (int i = 0; i < 6; ++i) {
            GlassTier.addCustomGlass(ItemRegistry.bw_realglas, i + 6, 3, i + 1);
        }
        if (Thaumcraft.isModLoaded()) {
            // Warded glass
            GlassTier.addCustomGlass(Thaumcraft.ID, "blockCosmeticOpaque", 2, 3, 12);
        }

        // --- EV ---
        GlassTier.addCustomGlass(ItemRegistry.bw_realglas, 1, 4, 0);
        for (int i = 0; i < 4; i++) {
            GlassTier.addCustomGlass(GregTechAPI.sBlockTintedGlass, i, 4, i + 1);
        }
        GlassTier.addCustomGlass(GregTechAPI.sBlockGlass1, 0, 4, 1);
        GlassTier.addCustomGlass(IndustrialCraft2.ID, "blockAlloyGlass", 0, 4, 5);
        if (BloodArsenal.isModLoaded()) {
            GlassTier.addCustomGlass(BloodArsenal.ID, "blood_stained_glass", 0, 4, 6);
        }
        if (Botania.isModLoaded()) {
            GlassTier.addCustomGlass(Botania.ID, "manaGlass", 0, 4, 7);
        }

        // --- IV ---
        GlassTier.addCustomGlass(ItemRegistry.bw_realglas, 2, 5, 0);
        // Thorium-Yttrium
        GlassTier.addCustomGlass(ItemRegistry.bw_realglas, 12, 5, 1);
        if (Botania.isModLoaded()) {
            GlassTier.addCustomGlass(Botania.ID, "elfGlass", 0, 5, 2);
        }

        // --- LuV ---
        GlassTier.addCustomGlass(ItemRegistry.bw_realglas, 3, 6, 0);

        // --- ZPM ---
        GlassTier.addCustomGlass(ItemRegistry.bw_realglas, 4, 7, 0);

        // --- UV ---
        GlassTier.addCustomGlass(ItemRegistry.bw_realglas, 5, 8, 0);
        GlassTier.addCustomGlass(BlockQuantumGlass.INSTANCE, 0, 8, 1);

        // --- UHV ---
        GlassTier.addCustomGlass(ItemRegistry.bw_realglas, 13, 9, 0);
        GlassTier.addCustomGlass(GregTechAPI.sBlockGlass1, 1, 9, 1);
        GlassTier.addCustomGlass(GregTechAPI.sBlockGlass1, 2, 9, 2);

        // --- UEV ---
        GlassTier.addCustomGlass(ItemRegistry.bw_realglas, 14, 10, 0);
        GlassTier.addCustomGlass(GregTechAPI.sBlockGlass1, 3, 10, 1);

        // --- UIV ---
        GlassTier.addCustomGlass(ItemRegistry.bw_realglas, 15, 11, 0);
        GlassTier.addCustomGlass(GregTechAPI.sBlockGlass1, 4, 11, 1);

        // --- UMV ---
        GlassTier.addCustomGlass(ItemRegistry.bw_realglas2, 0, 12, 0);
    }

    private static void registerGlassOreDicts() {

        // Register glass ore dict entries.
        for (Map.Entry<Pair<Block, Integer>, Integer> pair : GlassTier.getGlassMap()
            .entrySet()) {
            String oreName = "blockGlass" + VN[pair.getValue()];
            ItemStack itemStack = new ItemStack(
                pair.getKey()
                    .getLeft(),
                1,
                pair.getKey()
                    .getRight());
            OreDictionary.registerOre(oreName, itemStack);
        }
    }
}
