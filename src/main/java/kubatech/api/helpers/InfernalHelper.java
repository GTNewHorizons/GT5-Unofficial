/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2023  kuba6000
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <https://www.gnu.org/licenses/>.
 * spotless:on
 */

package kubatech.api.helpers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import atomicstryker.infernalmobs.common.InfernalMobsCore;
import atomicstryker.infernalmobs.common.mods.api.ModifierLoader;

@SuppressWarnings("unchecked")
public class InfernalHelper {

    private static Method isClassAllowed = null;

    public static boolean isClassAllowed(EntityLivingBase e) {
        try {
            if (isClassAllowed == null) {
                isClassAllowed = InfernalMobsCore.class.getDeclaredMethod("isClassAllowed", EntityLivingBase.class);
                isClassAllowed.setAccessible(true);
            }
            return (boolean) isClassAllowed.invoke(InfernalMobsCore.instance(), e);
        } catch (Throwable exception) {
            exception.printStackTrace();
        }
        return false;
    }

    private static Method checkEntityClassForced = null;

    public static boolean checkEntityClassForced(EntityLivingBase e) {
        try {
            if (checkEntityClassForced == null) {
                checkEntityClassForced = InfernalMobsCore.class
                    .getDeclaredMethod("checkEntityClassForced", EntityLivingBase.class);
                checkEntityClassForced.setAccessible(true);
            }
            return (boolean) checkEntityClassForced.invoke(InfernalMobsCore.instance(), e);
        } catch (Throwable exception) {
            exception.printStackTrace();
        }
        return false;
    }

    private static Field modifierLoaders = null;

    public static ArrayList<ModifierLoader<?>> getModifierLoaders() {
        try {
            if (modifierLoaders == null) {
                modifierLoaders = InfernalMobsCore.class.getDeclaredField("modifierLoaders");
                modifierLoaders.setAccessible(true);
            }
            return (ArrayList<ModifierLoader<?>>) modifierLoaders.get(InfernalMobsCore.instance());
        } catch (Throwable exception) {
            exception.printStackTrace();
        }
        return new ArrayList<>();
    }

    private static Field eliteRarity;

    public static int getEliteRarity() {
        try {
            if (eliteRarity == null) {
                eliteRarity = InfernalMobsCore.class.getDeclaredField("eliteRarity");
                eliteRarity.setAccessible(true);
            }
            return eliteRarity.getInt(InfernalMobsCore.instance());
        } catch (Throwable exception) {
            exception.printStackTrace();
        }
        return 15;
    }

    private static Field ultraRarity;

    public static int getUltraRarity() {
        try {
            if (ultraRarity == null) {
                ultraRarity = InfernalMobsCore.class.getDeclaredField("ultraRarity");
                ultraRarity.setAccessible(true);
            }
            return ultraRarity.getInt(InfernalMobsCore.instance());
        } catch (Throwable exception) {
            exception.printStackTrace();
        }
        return 15;
    }

    private static Field infernoRarity;

    public static int getInfernoRarity() {
        try {
            if (infernoRarity == null) {
                infernoRarity = InfernalMobsCore.class.getDeclaredField("infernoRarity");
                infernoRarity.setAccessible(true);
            }
            return infernoRarity.getInt(InfernalMobsCore.instance());
        } catch (Throwable exception) {
            exception.printStackTrace();
        }
        return 15;
    }

    private static Field minEliteModifiers;

    public static int getMinEliteModifiers() {
        try {
            if (minEliteModifiers == null) {
                minEliteModifiers = InfernalMobsCore.class.getDeclaredField("minEliteModifiers");
                minEliteModifiers.setAccessible(true);
            }
            return minEliteModifiers.getInt(InfernalMobsCore.instance());
        } catch (Throwable exception) {
            exception.printStackTrace();
        }
        return 15;
    }

    private static Field minUltraModifiers;

    public static int getMinUltraModifiers() {
        try {
            if (minUltraModifiers == null) {
                minUltraModifiers = InfernalMobsCore.class.getDeclaredField("minUltraModifiers");
                minUltraModifiers.setAccessible(true);
            }
            return minUltraModifiers.getInt(InfernalMobsCore.instance());
        } catch (Throwable exception) {
            exception.printStackTrace();
        }
        return 15;
    }

    private static Field minInfernoModifiers;

    public static int getMinInfernoModifiers() {
        try {
            if (minInfernoModifiers == null) {
                minInfernoModifiers = InfernalMobsCore.class.getDeclaredField("minInfernoModifiers");
                minInfernoModifiers.setAccessible(true);
            }
            return minInfernoModifiers.getInt(InfernalMobsCore.instance());
        } catch (Throwable exception) {
            exception.printStackTrace();
        }
        return 15;
    }

    private static Field dimensionBlackList;

    public static ArrayList<Integer> getDimensionBlackList() {
        try {
            if (dimensionBlackList == null) {
                dimensionBlackList = InfernalMobsCore.class.getDeclaredField("dimensionBlackList");
                dimensionBlackList.setAccessible(true);
            }
            return (ArrayList<Integer>) dimensionBlackList.get(InfernalMobsCore.instance());
        } catch (Throwable exception) {
            exception.printStackTrace();
        }
        return new ArrayList<>();
    }

    private static Field dropIdListElite;

    public static ArrayList<ItemStack> getDropIdListElite() {
        try {
            if (dropIdListElite == null) {
                dropIdListElite = InfernalMobsCore.class.getDeclaredField("dropIdListElite");
                dropIdListElite.setAccessible(true);
            }
            return (ArrayList<ItemStack>) dropIdListElite.get(InfernalMobsCore.instance());
        } catch (Throwable exception) {
            exception.printStackTrace();
        }
        return new ArrayList<>();
    }

    private static Field dropIdListUltra;

    public static ArrayList<ItemStack> getDropIdListUltra() {
        try {
            if (dropIdListUltra == null) {
                dropIdListUltra = InfernalMobsCore.class.getDeclaredField("dropIdListUltra");
                dropIdListUltra.setAccessible(true);
            }
            return (ArrayList<ItemStack>) dropIdListUltra.get(InfernalMobsCore.instance());
        } catch (Throwable exception) {
            exception.printStackTrace();
        }
        return new ArrayList<>();
    }

    private static Field dropIdListInfernal;

    public static ArrayList<ItemStack> getDropIdListInfernal() {
        try {
            if (dropIdListInfernal == null) {
                dropIdListInfernal = InfernalMobsCore.class.getDeclaredField("dropIdListInfernal");
                dropIdListInfernal.setAccessible(true);
            }
            return (ArrayList<ItemStack>) dropIdListInfernal.get(InfernalMobsCore.instance());
        } catch (Throwable exception) {
            exception.printStackTrace();
        }
        return new ArrayList<>();
    }
}
