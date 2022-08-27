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

package com.github.bartimaeusnek.crossmod.GTpp.loader;

import com.github.bartimaeusnek.bartworks.API.IRadMaterial;
import com.github.bartimaeusnek.bartworks.util.log.DebugLog;
import com.github.bartimaeusnek.crossmod.BartWorksCrossmod;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.RegistryNamespaced;
import net.minecraftforge.oredict.OreDictionary;

@SuppressWarnings({"unchecked", "rawtypes", "unused"})
public class RadioHatchCompat {

    private static Class intf;
    private static Class materialClass;
    private static Class enu;
    private static Class materialStackClass;

    private static Field isRadioactive;
    private static Field f;
    private static Field componentType;
    private static Field radlevel;
    private static Field protons;

    private static Field vMaterialInput;
    private static Field stackMaterial;
    private static Field RGBA;
    private static Field localizedName;
    private static Field unlocalizedName;

    private static Object rod;
    private static Object longRod;

    public static HashSet<String> TranslateSet = new HashSet<>();

    static {
        try {
            RadioHatchCompat.intf = Class.forName("gtPlusPlus.core.item.base.BaseItemComponent");
            RadioHatchCompat.enu = Class.forName("gtPlusPlus.core.item.base.BaseItemComponent$ComponentTypes");
            RadioHatchCompat.materialClass = Class.forName("gtPlusPlus.core.material.Material");
            RadioHatchCompat.materialStackClass = Class.forName("gtPlusPlus.core.material.MaterialStack");

            RadioHatchCompat.f = RadioHatchCompat.intf.getField("componentMaterial");
            RadioHatchCompat.isRadioactive = RadioHatchCompat.materialClass.getField("isRadioactive");
            RadioHatchCompat.componentType = RadioHatchCompat.intf.getDeclaredField("componentType");
            RadioHatchCompat.radlevel = RadioHatchCompat.materialClass.getField("vRadiationLevel");

            RadioHatchCompat.vMaterialInput = RadioHatchCompat.materialClass.getDeclaredField("vMaterialInput");
            RadioHatchCompat.stackMaterial = RadioHatchCompat.materialStackClass.getDeclaredField("stackMaterial");
            RadioHatchCompat.protons = RadioHatchCompat.materialClass.getDeclaredField("vProtons");
            RadioHatchCompat.RGBA = RadioHatchCompat.materialClass.getDeclaredField("RGBA");
            RadioHatchCompat.localizedName = RadioHatchCompat.materialClass.getDeclaredField("localizedName");
            RadioHatchCompat.unlocalizedName = RadioHatchCompat.materialClass.getDeclaredField("unlocalizedName");

            RadioHatchCompat.vMaterialInput.setAccessible(true);
            RadioHatchCompat.stackMaterial.setAccessible(true);
            RadioHatchCompat.protons.setAccessible(true);
            RadioHatchCompat.RGBA.setAccessible(true);
            RadioHatchCompat.localizedName.setAccessible(true);
            RadioHatchCompat.unlocalizedName.setAccessible(true);

            Object[] arr = RadioHatchCompat.enu.getEnumConstants();
            for (Object o : arr) {
                if (RadioHatchCompat.rod != null && RadioHatchCompat.longRod != null) break;
                else if (o.toString().equalsIgnoreCase("ROD")) RadioHatchCompat.rod = o;
                else if (o.toString().equalsIgnoreCase("RODLONG")) RadioHatchCompat.longRod = o;
            }

        } catch (NoSuchFieldException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void run() {
        DebugLog.log("Starting Generation of missing GT++ rods/longrods");
        try {
            Class rodclass = Class.forName("gtPlusPlus.core.item.base.rods.BaseItemRod");
            Class longrodclass = Class.forName("gtPlusPlus.core.item.base.rods.BaseItemRodLong");
            Constructor<? extends Item> c1 = rodclass.getConstructor(RadioHatchCompat.materialClass);
            Constructor<? extends Item> c2 = longrodclass.getConstructor(RadioHatchCompat.materialClass);
            Field cOwners = GameData.class.getDeclaredField("customOwners");
            cOwners.setAccessible(true);
            Field map = RegistryNamespaced.class.getDeclaredField("field_148758_b");
            map.setAccessible(true);
            Map<Item, String> UniqueIdentifierMap = (Map<Item, String>) map.get(GameData.getItemRegistry());

            Map<GameRegistry.UniqueIdentifier, ModContainer> ownerItems =
                    (Map<GameRegistry.UniqueIdentifier, ModContainer>) cOwners.get(null);
            ModContainer gtpp = null;
            ModContainer bartworks = null;

            for (ModContainer container : Loader.instance().getModList()) {
                if (gtpp != null && bartworks != null) break;
                else if (container.getModId().equalsIgnoreCase(BartWorksCrossmod.MOD_ID)) bartworks = container;
                else if (container.getModId().equalsIgnoreCase("miscutils")) gtpp = container;
            }

            for (Object mats : (Set)
                    RadioHatchCompat.materialClass.getField("mMaterialMap").get(null)) {
                if (RadioHatchCompat.isRadioactive.getBoolean(mats)) {

                    if (OreDictionary.getOres("stick" + RadioHatchCompat.unlocalizedName.get(mats))
                            .isEmpty()) {
                        Item it = c1.newInstance(mats);
                        UniqueIdentifierMap.replace(it, "miscutils:" + it.getUnlocalizedName());
                        GameRegistry.UniqueIdentifier ui = GameRegistry.findUniqueIdentifierFor(it);
                        ownerItems.replace(ui, bartworks, gtpp);

                        String tanslate =
                                it.getUnlocalizedName() + ".name=" + RadioHatchCompat.localizedName.get(mats) + " Rod";
                        RadioHatchCompat.TranslateSet.add(tanslate);

                        DebugLog.log(tanslate);
                        DebugLog.log("Generate: " + RadioHatchCompat.rod + RadioHatchCompat.unlocalizedName.get(mats));
                    }
                    if (OreDictionary.getOres("stickLong" + RadioHatchCompat.unlocalizedName.get(mats))
                            .isEmpty()) {
                        Item it2 = c2.newInstance(mats);
                        UniqueIdentifierMap.replace(it2, "miscutils:" + it2.getUnlocalizedName());
                        GameRegistry.UniqueIdentifier ui2 = GameRegistry.findUniqueIdentifierFor(it2);
                        ownerItems.replace(ui2, bartworks, gtpp);

                        DebugLog.log(
                                "Generate: " + RadioHatchCompat.longRod + RadioHatchCompat.unlocalizedName.get(mats));
                    }
                }
            }
        } catch (NoSuchFieldException
                | IllegalAccessException
                | NoSuchMethodException
                | InvocationTargetException
                | InstantiationException
                | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static IRadMaterial GTppRadChecker(ItemStack lStack) {
        try {
            if (RadioHatchCompat.intf.isAssignableFrom(lStack.getItem().getClass())) {
                if (!RadioHatchCompat.isRadioactive.getBoolean(RadioHatchCompat.f.get(lStack.getItem()))) return null;
                int amount = RadioHatchCompat.componentType
                                .get(lStack.getItem())
                                .equals(RadioHatchCompat.rod)
                        ? 1
                        : RadioHatchCompat.componentType.get(lStack.getItem()).equals(RadioHatchCompat.longRod) ? 2 : 0;
                if (amount == 0) return null;
                return new RadioHatchCompat.GTPPRadAdapter(amount, RadioHatchCompat.f.get(lStack.getItem()));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    static class GTPPRadAdapter implements IRadMaterial {

        static final HashMap<Object, Integer> BUFFER = new HashMap<>();

        GTPPRadAdapter(Object m) {
            this.m = m;
        }

        GTPPRadAdapter(int amount, Object m) {
            this.amount = (byte) amount;
            this.m = m;
        }

        byte amount;
        final Object m;

        private static ArrayList getMaterialInput(Object GTPPMaterial) throws IllegalAccessException {
            Object ret = RadioHatchCompat.vMaterialInput.get(GTPPMaterial);
            return ret instanceof ArrayList ? (ArrayList) ret : new ArrayList();
        }

        private static boolean isElement(Object GTPPMaterial) throws IllegalAccessException {
            return RadioHatchCompat.GTPPRadAdapter.getMaterialInput(GTPPMaterial)
                    .isEmpty();
        }

        private static List getElemets(Object GTPPMaterial) throws IllegalAccessException {
            ArrayList elements = new ArrayList();
            Queue toCheck = new LinkedList();
            ArrayList materialInputs = RadioHatchCompat.GTPPRadAdapter.getMaterialInput(GTPPMaterial);
            if (materialInputs.isEmpty()) return Collections.singletonList(GTPPMaterial);
            for (Object materialStack : materialInputs) {
                if (!RadioHatchCompat.GTPPRadAdapter.isElement(RadioHatchCompat.stackMaterial.get(materialStack)))
                    toCheck.add(RadioHatchCompat.stackMaterial.get(materialStack));
                else elements.add(RadioHatchCompat.stackMaterial.get(materialStack));
            }
            while (!toCheck.isEmpty()) {
                elements.addAll(GTPPRadAdapter.getElemets(toCheck.poll()));
            }
            return elements;
        }

        private static Integer calulateRad(Object m) {
            int ret = 0;
            try {
                List pureElements = RadioHatchCompat.GTPPRadAdapter.getElemets(m);
                for (Object materialObj : pureElements)
                    if (RadioHatchCompat.isRadioactive.getBoolean(materialObj))
                        ret += ((int) RadioHatchCompat.radlevel.getByte(m)
                                + RadioHatchCompat.GTPPRadAdapter.clampToZero(
                                        RadioHatchCompat.protons.getLong(materialObj)));
                    else ret += RadioHatchCompat.radlevel.getByte(m);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return ret;
        }

        @Override
        public int getRadiationLevel(ItemStack aStack) {
            return RadioHatchCompat.GTPPRadAdapter.BUFFER.computeIfAbsent(
                    this.m, radlvl -> RadioHatchCompat.GTPPRadAdapter.calulateRad(this.m));
        }

        private static long clampToZero(long number) {
            return number > 0 ? number : 0;
        }

        @Override
        public byte getAmountOfMaterial(ItemStack aStack) {
            return this.amount;
        }

        @Override
        public short[] getColorForGUI(ItemStack aStack) {
            short[] rgba = {0, 0, 0, 0};
            try {
                rgba = (short[]) RadioHatchCompat.RGBA.get(this.m);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return rgba;
        }

        @Override
        public String getNameForGUI(ItemStack aStack) {
            String ret = "";
            try {
                ret = (String) RadioHatchCompat.localizedName.get(this.m);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return ret;
        }
    }
}
