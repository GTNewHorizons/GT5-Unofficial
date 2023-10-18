package gtPlusPlus.xmod.tinkers.util;

import static gregtech.api.enums.Mods.TinkerConstruct;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.Materials;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class TinkersUtils {

    private static final Class<?> mClass_Smeltery;
    private static final Class<?> mClass_TConstructRegistry;
    private static final Class<?> mClass_ToolMaterial;
    private static final Class<?> mClass_IPattern;
    private static final Class<?> mClass_DynamicToolPart;
    private static final Class<?> mClass_FluidType;
    private static final Class<?> mClass_CastingRecipe;
    private static final Class<?> mClass_TinkerSmeltery;

    private static final Field mField_MoltenIronFluid;

    private static final Method mMethod_getFluidType;
    private static final Method mMethod_getCastingRecipes;

    private static Object mSmelteryInstance;
    private static Object mTinkersRegistryInstance;

    private static final HashMap<String, Method> mMethodCache = new LinkedHashMap<>();

    static {
        mClass_Smeltery = ReflectionUtils.getClass("tconstruct.library.crafting.Smeltery");
        mClass_TConstructRegistry = ReflectionUtils.getClass("tconstruct.library.TConstructRegistry");

        mClass_ToolMaterial = ReflectionUtils.getClass("tconstruct.library.tools.ToolMaterial");
        mClass_IPattern = ReflectionUtils.getClass("tconstruct.library.util.IPattern");
        mClass_DynamicToolPart = ReflectionUtils.getClass("tconstruct.library.tools.DynamicToolPart");
        mClass_FluidType = ReflectionUtils.getClass("tconstruct.library.crafting.FluidType");
        mClass_CastingRecipe = ReflectionUtils.getClass("tconstruct.library.crafting.CastingRecipe");
        mClass_TinkerSmeltery = ReflectionUtils.getClass("tconstruct.smeltery.TinkerSmeltery");

        mField_MoltenIronFluid = ReflectionUtils.getField(mClass_TinkerSmeltery, "moltenIronFluid");

        mMethod_getFluidType = ReflectionUtils.getMethod(mClass_FluidType, "getFluidType", String.class);
        mMethod_getCastingRecipes = ReflectionUtils
                .getMethod(getCastingInstance(0), "getCastingRecipes", new Class[] {});
    }

    private static void setTiConDataInstance() {
        if (!TinkerConstruct.isModLoaded()) {
            return;
        }

        if (mSmelteryInstance == null) {
            if (mClass_Smeltery != null) {
                try {
                    mSmelteryInstance = ReflectionUtils.getField(mClass_Smeltery, "instance").get(null);
                } catch (IllegalArgumentException | IllegalAccessException ignored) {}
            }
        }
        if (mTinkersRegistryInstance == null) {
            if (mClass_TConstructRegistry != null) {
                try {
                    mTinkersRegistryInstance = ReflectionUtils.getField(mClass_TConstructRegistry, "instance")
                            .get(null);
                } catch (IllegalArgumentException | IllegalAccessException ignored) {}
            }
        }
    }

    /**
     * Add a new fluid as a valid Smeltery fuel.
     * 
     * @param fluid    The fluid.
     * @param power    The temperature of the fluid. This also influences the melting speed. Lava is 1000.
     * @param duration How long one "portion" of liquid fuels the smeltery. Lava is 10.
     */
    public static void addSmelteryFuel(Fluid fluid, int power, int duration) {
        setTiConDataInstance();
        ReflectionUtils.invokeVoid(
                mSmelteryInstance,
                "addSmelteryFuel",
                new Class[] { Fluid.class, int.class, int.class },
                new Object[] { fluid, power, duration });
    }

    public static boolean registerFluidType(String name, Block block, int meta, int baseTemperature, Fluid fluid,
            boolean isToolpart) {
        if (mMethodCache.get("registerFluidType") == null) {
            Method m = ReflectionUtils.getMethod(
                    ReflectionUtils.getClass("tconstruct.library.crafting.FluidType"),
                    "registerFluidType",
                    String.class,
                    Block.class,
                    int.class,
                    int.class,
                    Fluid.class,
                    boolean.class);
            mMethodCache.put("registerFluidType", m);
        }
        try {
            mMethodCache.get("registerFluidType").invoke(null, name, block, meta, baseTemperature, fluid, isToolpart);
            return true;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            return false;
        }
    }

    public static void addMelting(ItemStack input, Block block, int metadata, int temperature, FluidStack liquid) {
        if (mMethodCache.get("addMelting") == null) {
            Method m = ReflectionUtils.getMethod(
                    mClass_Smeltery,
                    "addMelting",
                    ItemStack.class,
                    Block.class,
                    int.class,
                    int.class,
                    FluidStack.class);
            mMethodCache.put("addMelting", m);
        }
        try {
            mMethodCache.get("addMelting").invoke(null, input, block, metadata, temperature, liquid);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ignored) {}
    }

    public static void addMelting(Object type, ItemStack input, int temperatureDifference, int fluidAmount) {
        if (mMethodCache.get("addMelting") == null) {
            Method m = ReflectionUtils
                    .getMethod(mClass_Smeltery, "addMelting", mClass_FluidType, ItemStack.class, int.class, int.class);
            mMethodCache.put("addMelting", m);
        }
        try {
            mMethodCache.get("addMelting").invoke(null, type, input, temperatureDifference, fluidAmount);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ignored) {}
    }

    public static void addBasinRecipe(ItemStack output, FluidStack metal, ItemStack cast, boolean consume, int delay) {
        if (mMethodCache.get("addBasinRecipe") == null) {
            Method m = ReflectionUtils.getMethod(
                    ReflectionUtils.getClass("tconstruct.library.crafting.LiquidCasting"),
                    "addCastingRecipe",
                    ItemStack.class,
                    FluidStack.class,
                    ItemStack.class,
                    boolean.class,
                    int.class);
            mMethodCache.put("addBasinRecipe", m);
        }
        try {
            mMethodCache.get("addBasinRecipe").invoke(getCastingInstance(0), output, metal, cast, consume, delay);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ignored) {}
    }

    public static void addCastingTableRecipe(ItemStack output, FluidStack metal, ItemStack cast, boolean consume,
            int delay) {
        if (mMethodCache.get("addCastingTableRecipe") == null) {
            Method m = ReflectionUtils.getMethod(
                    ReflectionUtils.getClass("tconstruct.library.crafting.LiquidCasting"),
                    "addCastingRecipe",
                    ItemStack.class,
                    FluidStack.class,
                    ItemStack.class,
                    boolean.class,
                    int.class);
            mMethodCache.put("addCastingTableRecipe", m);
        }
        try {
            mMethodCache.get("addCastingTableRecipe")
                    .invoke(getCastingInstance(1), output, metal, cast, consume, delay);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ignored) {}
    }

    /**
     * 0 For Table, 1 For Basin.
     * 
     * @param aType - Casting Type
     * @return - The casting instance.
     */
    public static Object getCastingInstance(int aType) {

        setTiConDataInstance();

        Method m = null;
        if (aType == 0) {
            m = ReflectionUtils.getMethod(mTinkersRegistryInstance, "getTableCasting", new Class[] {});
        } else if (aType == 1) {
            m = ReflectionUtils.getMethod(mTinkersRegistryInstance, "getBasinCasting", new Class[] {});
        } // return null;

        if (m != null) {
            try {
                return m.invoke(mTinkersRegistryInstance, new Object[] {});
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static Item mTinkerMetalPattern;

    public static ItemStack getPattern(int aType) {
        if (mTinkerMetalPattern == null) {
            Field m = ReflectionUtils.getField(mClass_TinkerSmeltery, "metalPattern");
            if (m != null) {
                try {
                    mTinkerMetalPattern = (Item) m.get(null);
                } catch (IllegalArgumentException | IllegalAccessException ignored) {}
            }
        }
        if (mTinkerMetalPattern != null) {
            return new ItemStack(mTinkerMetalPattern, aType, 0);
        }
        return ItemUtils.getErrorStack(1, "Bad Tinkers Pattern");
    }

    private static AutoMap<?> mDryingRackRecipes;

    public static List<?> getDryingRecipes() {
        if (mDryingRackRecipes != null) {
            return mDryingRackRecipes;
        }
        AutoMap<Object> aData = new AutoMap<>();
        int aCount = 0;
        try {
            ArrayList<?> recipes = (ArrayList<?>) ReflectionUtils
                    .getField(ReflectionUtils.getClass("tconstruct.library.crafting.DryingRackRecipes"), "recipes")
                    .get(null);
            if (recipes != null) {
                for (Object o : recipes) {
                    aData.put(o);
                    aCount++;
                }
                Logger.INFO("Found " + aCount + " Tinkers drying rack recipes.");
            } else {
                Logger.INFO("Failed to find any Tinkers drying rack recipes.");
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            Logger.INFO("Failed to find any Tinkers drying rack recipes.");
        }
        mDryingRackRecipes = aData;
        return aData;
    }

    public static Object generateToolMaterial(String name, String localizationString, int level, int durability,
            int speed, int damage, float handle, int reinforced, float stonebound, String style, int primaryColor) {
        try {
            Constructor<?> constructor = mClass_ToolMaterial.getConstructor(
                    String.class,
                    String.class,
                    int.class,
                    int.class,
                    int.class,
                    int.class,
                    float.class,
                    int.class,
                    float.class,
                    String.class,
                    int.class);
            return constructor.newInstance(
                    name,
                    localizationString,
                    level,
                    durability,
                    speed,
                    damage,
                    handle,
                    reinforced,
                    stonebound,
                    style,
                    primaryColor);
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    public static List<?> getTableCastingRecipes() {
        Object aCastingTableHandlerInstance = getCastingInstance(0);
        List<?> aTemp;
        try {
            aTemp = (List<?>) mMethod_getCastingRecipes.invoke(aCastingTableHandlerInstance, new Object[] {});
            return aTemp;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static void generateCastingRecipes(Material aMaterial, int aID) {

        List<CastingRecipeHandler> newRecipies = new LinkedList<>();

        Iterator<?> iterator1 = getTableCastingRecipes().iterator();
        Fluid aMoltenIron = null;
        if (aMoltenIron == null) {
            try {
                aMoltenIron = (Fluid) mField_MoltenIronFluid.get(null);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
                aMoltenIron = Materials.Iron.getMolten(0).getFluid();
            }
        }
        while (iterator1.hasNext()) {
            CastingRecipeHandler recipe = new CastingRecipeHandler(iterator1.next());
            if (recipe == null || !recipe.valid) {
                continue;
            }
            try {
                if (recipe.castingMetal.getFluid() == aMoltenIron && recipe.cast != null
                        && mClass_IPattern.isInstance(recipe.cast.getItem())
                        && mClass_DynamicToolPart.isInstance(recipe.getResult().getItem())) {
                    newRecipies.add(recipe);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return;
            }
        }

        Object ft;
        try {
            ft = mMethod_getFluidType.invoke(null, aMaterial.getLocalizedName());
            for (CastingRecipeHandler newRecipy : newRecipies) {
                CastingRecipeHandler recipe = new CastingRecipeHandler(newRecipy);
                if (!recipe.valid) {
                    continue;
                }
                // CastingRecipe recipe = (CastingRecipe) i$.next();
                ItemStack output = recipe.getResult().copy();
                output.setItemDamage(aID);
                FluidStack liquid2 = new FluidStack(aMaterial.getFluidStack(0).getFluid(), recipe.castingMetal.amount);
                addCastingTableRecipe(output, liquid2, recipe.cast, recipe.consumeCast, recipe.coolTime);
                addMelting(ft, output, 0, liquid2.amount / 2);
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private static class CastingRecipeHandler {

        public ItemStack output;
        public FluidStack castingMetal;
        public ItemStack cast;
        public boolean consumeCast;
        public int coolTime;

        public boolean valid;

        public CastingRecipeHandler(Object aCastingRecipe) {
            if (mClass_CastingRecipe.isInstance(aCastingRecipe)) {
                try {
                    Field aF_output = ReflectionUtils.getField(mClass_CastingRecipe, "output");
                    Field aF_castingMetal = ReflectionUtils.getField(mClass_CastingRecipe, "castingMetal");
                    Field aF_cast = ReflectionUtils.getField(mClass_CastingRecipe, "cast");
                    Field aF_consumeCast = ReflectionUtils.getField(mClass_CastingRecipe, "consumeCast");
                    Field aF_coolTime = ReflectionUtils.getField(mClass_CastingRecipe, "coolTime");

                    output = (ItemStack) aF_output.get(aCastingRecipe);
                    castingMetal = (FluidStack) aF_castingMetal.get(aCastingRecipe);
                    cast = (ItemStack) aF_cast.get(aCastingRecipe);
                    consumeCast = (boolean) aF_consumeCast.get(aCastingRecipe);
                    coolTime = (int) aF_coolTime.get(aCastingRecipe);
                    valid = true;
                } catch (Throwable t) {
                    t.printStackTrace();
                    valid = false;
                }
            } else {
                valid = false;
            }
        }

        public ItemStack getResult() {
            return this.output.copy();
        }
    }
}
