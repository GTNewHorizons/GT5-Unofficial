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

package bartworks.API;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import bartworks.system.material.WerkstoffLoader;
import bartworks.util.BWUtil;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;

public final class BioVatLogicAdder {

    public static class RadioHatch {

        public static void runBasicItemIntegration() {
            giveItemStackRadioHatchAbilites(ItemList.RodThorium.get(1), Materials.Thorium, 3);
            giveItemStackRadioHatchAbilites(ItemList.RodThorium2.get(1), Materials.Thorium, 6);
            giveItemStackRadioHatchAbilites(ItemList.RodThorium4.get(1), Materials.Thorium, 12);

            giveItemStackRadioHatchAbilites(ItemList.RodNaquadah.get(1), Materials.Naquadah, 3);
            giveItemStackRadioHatchAbilites(ItemList.RodNaquadah2.get(1), Materials.Naquadah, 6);
            giveItemStackRadioHatchAbilites(ItemList.RodNaquadah4.get(1), Materials.Naquadah, 12);

            giveItemStackRadioHatchAbilites(ItemList.RodMOX.get(1), Materials.Plutonium, 3);
            giveItemStackRadioHatchAbilites(ItemList.RodMOX2.get(1), Materials.Plutonium, 6);
            giveItemStackRadioHatchAbilites(ItemList.RodMOX4.get(1), Materials.Plutonium, 12);

            giveItemStackRadioHatchAbilites(ItemList.RodUranium.get(1), Materials.Uranium, 3);
            giveItemStackRadioHatchAbilites(ItemList.RodUranium2.get(1), Materials.Uranium, 6);
            giveItemStackRadioHatchAbilites(ItemList.RodUranium4.get(1), Materials.Uranium, 12);

            giveItemStackRadioHatchAbilites(
                ItemList.RodTiberium.get(1),
                WerkstoffLoader.Tiberium.getBridgeMaterial(),
                3);
            giveItemStackRadioHatchAbilites(
                ItemList.RodTiberium2.get(1),
                WerkstoffLoader.Tiberium.getBridgeMaterial(),
                6);
            giveItemStackRadioHatchAbilites(
                ItemList.RodTiberium4.get(1),
                WerkstoffLoader.Tiberium.getBridgeMaterial(),
                12);

            giveItemStackRadioHatchAbilites(ItemList.RodNaquadah32.get(1), Materials.Naquadah, 96);

            giveItemStackRadioHatchAbilites(ItemList.DepletedRodThorium.get(1), Materials.Thorium, 3, 10);
            giveItemStackRadioHatchAbilites(ItemList.DepletedRodThorium2.get(1), Materials.Thorium, 6, 10);
            giveItemStackRadioHatchAbilites(ItemList.DepletedRodThorium4.get(1), Materials.Thorium, 12, 10);

            giveItemStackRadioHatchAbilites(ItemList.DepletedRodNaquadah.get(1), Materials.Naquadah, 3, 10);
            giveItemStackRadioHatchAbilites(ItemList.DepletedRodNaquadah2.get(1), Materials.Naquadah, 6, 10);
            giveItemStackRadioHatchAbilites(ItemList.DepletedRodNaquadah4.get(1), Materials.Naquadah, 12, 10);

            giveItemStackRadioHatchAbilites(ItemList.DepletedRodMOX.get(1), Materials.Plutonium, 3, 10);
            giveItemStackRadioHatchAbilites(ItemList.DepletedRodMOX2.get(1), Materials.Plutonium, 6, 10);
            giveItemStackRadioHatchAbilites(ItemList.DepletedRodMOX4.get(1), Materials.Plutonium, 12, 10);

            giveItemStackRadioHatchAbilites(ItemList.DepletedRodUranium.get(1), Materials.Uranium, 3, 10);
            giveItemStackRadioHatchAbilites(ItemList.DepletedRodUranium2.get(1), Materials.Uranium, 6, 10);
            giveItemStackRadioHatchAbilites(ItemList.DepletedRodUranium4.get(1), Materials.Uranium, 12, 10);

            giveItemStackRadioHatchAbilites(
                ItemList.DepletedRodTiberium.get(1),
                WerkstoffLoader.Tiberium.getBridgeMaterial(),
                3,
                10);
            giveItemStackRadioHatchAbilites(
                ItemList.DepletedRodTiberium2.get(1),
                WerkstoffLoader.Tiberium.getBridgeMaterial(),
                6,
                10);
            giveItemStackRadioHatchAbilites(
                ItemList.DepletedRodTiberium4.get(1),
                WerkstoffLoader.Tiberium.getBridgeMaterial(),
                12,
                10);

            giveItemStackRadioHatchAbilites(ItemList.DepletedRodNaquadah32.get(1), Materials.Naquadah, 96, 10);

            giveItemStackRadioHatchAbilites(ItemList.RodNaquadria.get(1), Materials.Naquadria, 3);
            giveItemStackRadioHatchAbilites(ItemList.RodNaquadria2.get(1), Materials.Naquadria, 6);
            giveItemStackRadioHatchAbilites(ItemList.RodNaquadria4.get(1), Materials.Naquadria, 12);
            giveItemStackRadioHatchAbilites(ItemList.DepletedRodNaquadria.get(1), Materials.Naquadria, 3, 10);
            giveItemStackRadioHatchAbilites(ItemList.DepletedRodNaquadria2.get(1), Materials.Naquadria, 6, 10);
            giveItemStackRadioHatchAbilites(ItemList.DepletedRodNaquadria4.get(1), Materials.Naquadria, 12, 10);
        }

        private static final HashSet<BioVatLogicAdder.MaterialSvPair> MaSv = new HashSet<>();
        private static final HashMap<ItemStack, Integer> IsSv = new HashMap<>();
        private static final HashMap<ItemStack, Integer> IsKg = new HashMap<>();
        private static final HashMap<ItemStack, short[]> IsColor = new HashMap<>();

        public static @NotNull HashSet<BioVatLogicAdder.MaterialSvPair> getMaSv() {
            return RadioHatch.MaSv;
        }

        public static @NotNull HashMap<ItemStack, Integer> getIsKg() {
            return IsKg;
        }

        public static @NotNull HashMap<ItemStack, Integer> getIsSv() {
            return RadioHatch.IsSv;
        }

        public static @NotNull HashMap<ItemStack, short[]> getIsColor() {
            return IsColor;
        }

        public static void setOverrideSvForMaterial(Materials m, int sv) {
            MaSv.add(new BioVatLogicAdder.MaterialSvPair(m, sv));
        }

        public static void giveItemStackRadioHatchAbilites(ItemStack stack, int sv) {
            IsSv.put(stack, sv);
        }

        public static void giveItemStackRadioHatchAbilites(ItemStack stack, Materials materials) {
            IsSv.put(stack, BWUtil.calculateSv(materials));
        }

        public static void giveItemStackRadioHatchAbilites(ItemStack stack, int sv, int kg) {
            IsSv.put(stack, sv);
            IsKg.put(stack, kg);
        }

        public static void giveItemStackRadioHatchAbilites(ItemStack stack, @NotNull Materials materials, int kg) {
            giveItemStackRadioHatchAbilites(stack, BWUtil.calculateSv(materials), kg);
            IsColor.put(stack, materials.getRGBA());
        }

        public static void giveItemStackRadioHatchAbilites(ItemStack stack, @NotNull Materials materials, int kg,
            int divider) {
            giveItemStackRadioHatchAbilites(stack, BWUtil.calculateSv(materials) / divider, kg);
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

    public static class MaterialSvPair {

        final Materials materials;
        final Integer sievert;

        public MaterialSvPair(Materials materials, Integer sievert) {
            this.materials = materials;
            this.sievert = sievert;
        }

        @Override
        public boolean equals(@Nullable Object o) {
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

}
