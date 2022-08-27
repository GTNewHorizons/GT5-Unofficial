/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.API;

import static cpw.mods.fml.common.registry.GameRegistry.findBlock;

import com.github.bartimaeusnek.bartworks.system.material.BW_NonMeta_MaterialItems;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_ModHandler;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

@SuppressWarnings("ALL")
public final class BioVatLogicAdder {

    public static class RadioHatch {

        public static void runBasicItemIntegration() {
            giveItemStackRadioHatchAbilites(ItemList.ThoriumCell_1.get(1), Materials.Thorium, 3);
            giveItemStackRadioHatchAbilites(ItemList.ThoriumCell_2.get(1), Materials.Thorium, 6);
            giveItemStackRadioHatchAbilites(ItemList.ThoriumCell_4.get(1), Materials.Thorium, 12);

            giveItemStackRadioHatchAbilites(ItemList.NaquadahCell_1.get(1), Materials.Naquadah, 3);
            giveItemStackRadioHatchAbilites(ItemList.NaquadahCell_2.get(1), Materials.Naquadah, 6);
            giveItemStackRadioHatchAbilites(ItemList.NaquadahCell_4.get(1), Materials.Naquadah, 12);

            giveItemStackRadioHatchAbilites(ItemList.Moxcell_1.get(1), Materials.Plutonium, 3);
            giveItemStackRadioHatchAbilites(ItemList.Moxcell_2.get(1), Materials.Plutonium, 6);
            giveItemStackRadioHatchAbilites(ItemList.Moxcell_4.get(1), Materials.Plutonium, 12);

            giveItemStackRadioHatchAbilites(ItemList.Uraniumcell_1.get(1), Materials.Uranium, 3);
            giveItemStackRadioHatchAbilites(ItemList.Uraniumcell_2.get(1), Materials.Uranium, 6);
            giveItemStackRadioHatchAbilites(ItemList.Uraniumcell_4.get(1), Materials.Uranium, 12);

            giveItemStackRadioHatchAbilites(
                    BW_NonMeta_MaterialItems.TiberiumCell_1.get(1), WerkstoffLoader.Tiberium.getBridgeMaterial(), 3);
            giveItemStackRadioHatchAbilites(
                    BW_NonMeta_MaterialItems.TiberiumCell_2.get(1), WerkstoffLoader.Tiberium.getBridgeMaterial(), 6);
            giveItemStackRadioHatchAbilites(
                    BW_NonMeta_MaterialItems.TiberiumCell_4.get(1), WerkstoffLoader.Tiberium.getBridgeMaterial(), 12);

            giveItemStackRadioHatchAbilites(BW_NonMeta_MaterialItems.TheCoreCell.get(1), Materials.Naquadah, 96);

            giveItemStackRadioHatchAbilites(ItemList.Depleted_Thorium_1.get(1), Materials.Thorium, 3, 10);
            giveItemStackRadioHatchAbilites(ItemList.Depleted_Thorium_2.get(1), Materials.Thorium, 6, 10);
            giveItemStackRadioHatchAbilites(ItemList.Depleted_Thorium_4.get(1), Materials.Thorium, 12, 10);

            giveItemStackRadioHatchAbilites(ItemList.Depleted_Naquadah_1.get(1), Materials.Naquadah, 3, 10);
            giveItemStackRadioHatchAbilites(ItemList.Depleted_Naquadah_2.get(1), Materials.Naquadah, 6, 10);
            giveItemStackRadioHatchAbilites(ItemList.Depleted_Naquadah_4.get(1), Materials.Naquadah, 12, 10);

            giveItemStackRadioHatchAbilites(
                    GT_ModHandler.getModItem("IC2", "reactorMOXSimpledepleted", 1), Materials.Plutonium, 3, 10);
            giveItemStackRadioHatchAbilites(
                    GT_ModHandler.getModItem("IC2", "reactorMOXDualdepleted", 1), Materials.Plutonium, 6, 10);
            giveItemStackRadioHatchAbilites(
                    GT_ModHandler.getModItem("IC2", "reactorMOXQuaddepleted", 1), Materials.Plutonium, 12, 10);

            giveItemStackRadioHatchAbilites(
                    GT_ModHandler.getModItem("IC2", "reactorUraniumSimpledepleted", 1), Materials.Uranium, 3, 10);
            giveItemStackRadioHatchAbilites(
                    GT_ModHandler.getModItem("IC2", "reactorUraniumDualdepleted", 1), Materials.Uranium, 6, 10);
            giveItemStackRadioHatchAbilites(
                    GT_ModHandler.getModItem("IC2", "reactorUraniumQuaddepleted", 1), Materials.Uranium, 12, 10);

            giveItemStackRadioHatchAbilites(
                    BW_NonMeta_MaterialItems.Depleted_Tiberium_1.get(1),
                    WerkstoffLoader.Tiberium.getBridgeMaterial(),
                    3,
                    10);
            giveItemStackRadioHatchAbilites(
                    BW_NonMeta_MaterialItems.Depleted_Tiberium_2.get(1),
                    WerkstoffLoader.Tiberium.getBridgeMaterial(),
                    6,
                    10);
            giveItemStackRadioHatchAbilites(
                    BW_NonMeta_MaterialItems.Depleted_Tiberium_4.get(1),
                    WerkstoffLoader.Tiberium.getBridgeMaterial(),
                    12,
                    10);

            giveItemStackRadioHatchAbilites(
                    BW_NonMeta_MaterialItems.Depleted_TheCoreCell.get(1), Materials.Naquadah, 96, 10);

            if (WerkstoffLoader.gtnhGT) {
                giveItemStackRadioHatchAbilites(ItemList.MNqCell_1.get(1), Materials.Naquadria, 3);
                giveItemStackRadioHatchAbilites(ItemList.MNqCell_2.get(1), Materials.Naquadria, 6);
                giveItemStackRadioHatchAbilites(ItemList.MNqCell_4.get(1), Materials.Naquadria, 12);
                giveItemStackRadioHatchAbilites(ItemList.Depleted_MNq_1.get(1), Materials.Naquadria, 3, 10);
                giveItemStackRadioHatchAbilites(ItemList.Depleted_MNq_2.get(1), Materials.Naquadria, 6, 10);
                giveItemStackRadioHatchAbilites(ItemList.Depleted_MNq_4.get(1), Materials.Naquadria, 12, 10);
            }
        }

        private static final HashSet<BioVatLogicAdder.MaterialSvPair> MaSv = new HashSet<>();
        private static final HashMap<ItemStack, Integer> IsSv = new HashMap<>();
        private static final HashMap<ItemStack, Integer> IsKg = new HashMap<>();
        private static final HashMap<ItemStack, short[]> IsColor = new HashMap<>();

        public static HashSet<BioVatLogicAdder.MaterialSvPair> getMaSv() {
            return RadioHatch.MaSv;
        }

        public static HashMap<ItemStack, Integer> getIsKg() {
            return IsKg;
        }

        public static HashMap<ItemStack, Integer> getIsSv() {
            return RadioHatch.IsSv;
        }

        public static HashMap<ItemStack, short[]> getIsColor() {
            return IsColor;
        }

        public static void setOverrideSvForMaterial(Materials m, int sv) {
            MaSv.add(new BioVatLogicAdder.MaterialSvPair(m, sv));
        }

        public static void giveItemStackRadioHatchAbilites(ItemStack stack, int sv) {
            IsSv.put(stack, sv);
        }

        public static void giveItemStackRadioHatchAbilites(ItemStack stack, Materials materials) {
            IsSv.put(stack, BW_Util.calculateSv(materials));
        }

        public static void giveItemStackRadioHatchAbilites(ItemStack stack, int sv, int kg) {
            IsSv.put(stack, sv);
            IsKg.put(stack, kg);
        }

        public static void giveItemStackRadioHatchAbilites(ItemStack stack, Materials materials, int kg) {
            giveItemStackRadioHatchAbilites(stack, BW_Util.calculateSv(materials), kg);
            IsColor.put(stack, materials.getRGBA());
        }

        public static void giveItemStackRadioHatchAbilites(ItemStack stack, Materials materials, int kg, int divider) {
            giveItemStackRadioHatchAbilites(stack, BW_Util.calculateSv(materials) / divider, kg);
            IsColor.put(stack, materials.getRGBA());
        }

        public static void giveItemStackRadioHatchAbilites(ItemStack stack, int sv, int kg, short[] color) {
            giveItemStackRadioHatchAbilites(stack, sv, kg);
            IsColor.put(stack, color);
        }

        public static int MaxSV = 150;

        public static int getMaxSv() {
            int ret = MaxSV;
            for (MaterialSvPair pair : RadioHatch.getMaSv()) {
                if (pair.getSievert() > ret) ret = pair.getSievert();
            }
            for (ItemStack is : RadioHatch.IsSv.keySet()) {
                if (RadioHatch.IsSv.get(is) > ret) ret = RadioHatch.IsSv.get(is);
            }
            return ret;
        }
    }

    public static class BioVatGlass {

        private static final HashMap<BlockMetaPair, Byte> glasses = new HashMap<>();

        /**
         * @param sModname        The modid owning the block
         * @param sUnlocBlockName The name of the block itself
         * @param meta            The meta of the block
         * @param tier            the glasses Tier = Voltage tier (MIN 3)
         * @return if the block was found in the Block registry
         */
        public static boolean addCustomGlass(String sModname, String sUnlocBlockName, int meta, int tier) {
            Block block = findBlock(sModname, sUnlocBlockName);
            boolean ret = block != null;
            if (ret) BioVatGlass.glasses.put(new BlockMetaPair(block, (byte) meta), (byte) tier);
            else
                new IllegalArgumentException(
                                "Block: " + sUnlocBlockName + " of the Mod: " + sModname + " was NOT found!")
                        .printStackTrace();
            block = null;
            return ret;
        }

        /**
         * @param block the block to add
         * @param meta  the meta of the block (0-15)
         * @param tier  the glasses Tier = Voltage tier (MIN 3)
         */
        public static void addCustomGlass(@Nonnull Block block, @Nonnegative int meta, @Nonnegative int tier) {
            BioVatGlass.glasses.put(new BlockMetaPair(block, (byte) meta), (byte) tier);
        }

        /**
         * @param block the block to add
         * @param tier  the glasses Tier = Voltage tier (MIN 3)
         */
        public static void addCustomGlass(@Nonnull Block block, @Nonnegative int tier) {
            BioVatGlass.glasses.put(new BlockMetaPair(block, (byte) 0), (byte) tier);
        }

        /**
         * @param blockBytePair the block to add and its meta as a javafx.util Pair
         * @param tier          the glasses Tier = Voltage tier (MIN 3)
         */
        public static void addCustomGlass(@Nonnull BlockMetaPair blockBytePair, @Nonnegative byte tier) {
            BioVatGlass.glasses.put(blockBytePair, tier);
        }

        public static HashMap<BlockMetaPair, Byte> getGlassMap() {
            return BioVatGlass.glasses;
        }
    }

    public static class MaterialSvPair {
        final Materials materials;
        final Integer sievert;

        public MaterialSvPair(Materials materials, Integer sievert) {
            this.materials = materials;
            this.sievert = sievert;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || this.getClass() != o.getClass()) return false;
            BioVatLogicAdder.MaterialSvPair that = (BioVatLogicAdder.MaterialSvPair) o;
            return Objects.equals(this.getMaterials(), that.getMaterials())
                    && Objects.equals(this.getSievert(), that.getSievert());
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.getMaterials(), this.getSievert());
        }

        public Materials getMaterials() {
            return this.materials;
        }

        public Integer getSievert() {
            return this.sievert;
        }
    }

    public static class BlockMetaPair {
        final Block block;
        final Byte aByte;

        public BlockMetaPair(Block block, Byte aByte) {
            this.block = block;
            this.aByte = aByte;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || this.getClass() != o.getClass()) return false;
            BioVatLogicAdder.BlockMetaPair that = (BioVatLogicAdder.BlockMetaPair) o;
            return Objects.equals(this.getBlock(), that.getBlock()) && Objects.equals(this.getaByte(), that.getaByte());
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.getBlock(), this.getaByte());
        }

        public Block getBlock() {
            return this.block;
        }

        public Byte getaByte() {
            return this.aByte;
        }
    }
}
