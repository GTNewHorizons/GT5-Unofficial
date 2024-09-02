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
import static gregtech.api.enums.Mods.EnderIO;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.enums.Mods.Thaumcraft;

import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import bartworks.API.BorosilicateGlass;
import bartworks.API.GlassTier;
import tectech.thing.block.BlockQuantumGlass;

// Register all your glasses here.
public class RegisterGlassTiers {

    public static void run() {
        registerGlassAsTiered();
        registerGlassOreDicts();
    }

    private static void registerGlassAsTiered() {

        // ---------------------------------------------------------------------
        // Register glass.
        // ---------------------------------------------------------------------

        GlassTier.addCustomGlass(BorosilicateGlass.getGlassBlock(), 0, 3);
        GlassTier.addCustomGlass(BorosilicateGlass.getGlassBlock(), 1, 4);
        GlassTier.addCustomGlass(BorosilicateGlass.getGlassBlock(), 2, 5);
        GlassTier.addCustomGlass(BorosilicateGlass.getGlassBlock(), 3, 6);
        GlassTier.addCustomGlass(BorosilicateGlass.getGlassBlock(), 4, 7);
        GlassTier.addCustomGlass(BorosilicateGlass.getGlassBlock(), 5, 8);

        // Stained boro glass
        GlassTier.addCustomGlass(BorosilicateGlass.getGlassBlock(), 6, 3);
        GlassTier.addCustomGlass(BorosilicateGlass.getGlassBlock(), 7, 3);
        GlassTier.addCustomGlass(BorosilicateGlass.getGlassBlock(), 8, 3);
        GlassTier.addCustomGlass(BorosilicateGlass.getGlassBlock(), 9, 3);
        GlassTier.addCustomGlass(BorosilicateGlass.getGlassBlock(), 10, 3);
        GlassTier.addCustomGlass(BorosilicateGlass.getGlassBlock(), 11, 3);

        // Incrementing tiers
        GlassTier.addCustomGlass(BorosilicateGlass.getGlassBlock(), 12, 5);
        GlassTier.addCustomGlass(BorosilicateGlass.getGlassBlock(), 13, 9);
        GlassTier.addCustomGlass(BorosilicateGlass.getGlassBlock(), 14, 10);
        GlassTier.addCustomGlass(BorosilicateGlass.getGlassBlock(), 15, 11);

        // Glass block 2 for transcendent (Really?)
        GlassTier.addCustomGlass(BorosilicateGlass.getGlassBlock2(), 0, 12);

        // Other mods.
        GlassTier.addCustomGlass(IndustrialCraft2.ID, "blockAlloyGlass", 0, 4);
        GlassTier.addCustomGlass(BlockQuantumGlass.INSTANCE, 0, 8);

        if (EnderIO.isModLoaded()) {
            for (int i = 0; i < 6; ++i) {
                GlassTier.addCustomGlass(EnderIO.ID, "blockFusedQuartz", i, 3);
            }
        }

        if (Railcraft.isModLoaded()) {
            for (int i = 0; i < 16; i++) {
                GlassTier.addCustomGlass(Railcraft.ID, "glass", i, 3);
            }
        }

        // Magic alternatives.
        if (BloodArsenal.isModLoaded()) {
            GlassTier.addCustomGlass(BloodArsenal.ID, "blood_stained_glass", 0, 4);
        }

        if (Botania.isModLoaded()) {
            GlassTier.addCustomGlass(Botania.ID, "manaGlass", 0, 4);
            GlassTier.addCustomGlass(Botania.ID, "elfGlass", 0, 5);
        }

        if (Thaumcraft.isModLoaded()) {
            // Warded glass
            GlassTier.addCustomGlass(Thaumcraft.ID, "blockCosmeticOpaque", 2, 3);
        }
    }

    private static void registerGlassOreDicts() {

        // Register glass ore dict entries.
        for (Map.Entry<GlassTier.BlockMetaPair, Integer> pair : GlassTier.getGlassMap()
            .entrySet()) {
            String oreName = "blockGlass" + VN[pair.getValue()];
            ItemStack itemStack = new ItemStack(
                pair.getKey()
                    .getBlock(),
                1,
                pair.getKey()
                    .getMeta());
            OreDictionary.registerOre(oreName, itemStack);
        }
    }
}
