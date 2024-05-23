package gregtech.api.util;

import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.enums.GT_Values.COMPASS_DIRECTIONS;
import static gregtech.api.enums.GT_Values.D1;
import static gregtech.api.enums.GT_Values.E;
import static gregtech.api.enums.GT_Values.GT;
import static gregtech.api.enums.GT_Values.L;
import static gregtech.api.enums.GT_Values.M;
import static gregtech.api.enums.GT_Values.NW;
import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.GT_Values.W;
import static gregtech.api.enums.Materials.FLUID_MAP;
import static gregtech.api.enums.Mods.Translocator;
import static gregtech.common.GT_UndergroundOil.undergroundOilReadInformation;
import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.UNKNOWN;
import static net.minecraftforge.common.util.ForgeDirection.UP;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTBase.NBTPrimitive;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.oredict.OreDictionary;

import com.google.auto.value.AutoValue;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.SetMultimap;
import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.alignment.IAlignmentProvider;
import com.mojang.authlib.GameProfile;

import buildcraft.api.transport.IPipeTile;
import cofh.api.energy.IEnergyReceiver;
import cofh.api.transport.IItemDuct;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.damagesources.GT_DamageSources;
import gregtech.api.damagesources.GT_DamageSources.DamageSourceHotItem;
import gregtech.api.enchants.Enchantment_Hazmat;
import gregtech.api.enchants.Enchantment_Radioactivity;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.Textures;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.events.BlockScanningEvent;
import gregtech.api.interfaces.IBlockContainer;
import gregtech.api.interfaces.IDebugableBlock;
import gregtech.api.interfaces.IHasIndexedTexture;
import gregtech.api.interfaces.IProjectileItem;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IBasicEnergyContainer;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.interfaces.tileentity.IUpgradableMachine;
import gregtech.api.items.GT_EnergyArmor_Item;
import gregtech.api.items.GT_Generic_Item;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.net.GT_Packet_Sound;
import gregtech.api.objects.CollectorUtils;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_ItemStack2;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.threads.GT_Runnable_Sound;
import gregtech.api.util.extensions.ArrayExt;
import gregtech.common.GT_Pollution;
import gregtech.common.blocks.GT_Block_Ores_Abstract;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.RecipeOutput;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * Just a few Utility Functions I use.
 */
public class GT_Utility {

    /**
     * Formats a number with group separator and at most 2 fraction digits.
     */
    private static final Map<Locale, DecimalFormat> decimalFormatters = new HashMap<>();

    /**
     * Forge screwed the Fluid Registry up again, so I make my own, which is also much more efficient than the stupid
     * Stuff over there.
     */
    private static final List<FluidContainerData> sFluidContainerList = new ArrayList<>();

    private static final Map<GT_ItemStack, FluidContainerData> sFilledContainerToData = new /* Concurrent */ HashMap<>();
    private static final Map<GT_ItemStack, Map<String, FluidContainerData>> sEmptyContainerToFluidToData = new HashMap<>();
    private static final Map<String, List<ItemStack>> sFluidToContainers = new HashMap<>();
    /**
     * Must use {@code Supplier} here because the ore prefixes have not yet been registered at class load time.
     */
    private static final Map<OrePrefixes, Supplier<ItemStack>> sOreToCobble = new HashMap<>();

    private static final Map<Integer, Boolean> sOreTable = new HashMap<>();
    public static boolean TE_CHECK = false, BC_CHECK = false, CHECK_ALL = true, RF_CHECK = false;
    public static Map<GT_PlayedSound, Integer> sPlayedSoundMap = new /* Concurrent */ HashMap<>();
    private static int sBookCount = 0;
    public static UUID defaultUuid = null; // maybe default non-null?
    // UUID.fromString("00000000-0000-0000-0000-000000000000");

    static {
        GregTech_API.sItemStackMappings.add(sFilledContainerToData);
        GregTech_API.sItemStackMappings.add(sEmptyContainerToFluidToData);

        // 1 is the magic index to get the cobblestone block.
        // See: GT_Block_Stones.java, GT_Block_Granites.java
        Function<Materials, Supplier<ItemStack>> materialToCobble = m -> Suppliers.memoize(
            () -> GT_OreDictUnificator.getOres(OrePrefixes.stone, m)
                .get(1))::get;
        sOreToCobble.put(OrePrefixes.oreBlackgranite, materialToCobble.apply(Materials.GraniteBlack));
        sOreToCobble.put(OrePrefixes.oreRedgranite, materialToCobble.apply(Materials.GraniteRed));
        sOreToCobble.put(OrePrefixes.oreMarble, materialToCobble.apply(Materials.Marble));
        sOreToCobble.put(OrePrefixes.oreBasalt, materialToCobble.apply(Materials.Basalt));
        sOreToCobble.put(OrePrefixes.oreNetherrack, () -> new ItemStack(Blocks.netherrack));
        sOreToCobble.put(OrePrefixes.oreEndstone, () -> new ItemStack(Blocks.end_stone));
    }

    public static int safeInt(long number, int margin) {
        return number > Integer.MAX_VALUE - margin ? Integer.MAX_VALUE - margin : (int) number;
    }

    public static int safeInt(long number) {
        return number > V[V.length - 1] ? safeInt(V[V.length - 1], 1)
            : number < Integer.MIN_VALUE ? Integer.MIN_VALUE : (int) number;
    }

    public static Field getPublicField(Object aObject, String aField) {
        Field rField = null;
        try {
            rField = aObject.getClass()
                .getDeclaredField(aField);
        } catch (Throwable e) {
            /* Do nothing */
        }
        return rField;
    }

    public static Field getField(Object aObject, String aField) {
        Field rField = null;
        try {
            rField = aObject.getClass()
                .getDeclaredField(aField);
            rField.setAccessible(true);
        } catch (Throwable e) {
            /* Do nothing */
        }
        return rField;
    }

    public static Field getField(Class<?> aObject, String aField) {
        Field rField = null;
        try {
            rField = aObject.getDeclaredField(aField);
            rField.setAccessible(true);
        } catch (Throwable e) {
            /* Do nothing */
        }
        return rField;
    }

    public static Method getMethod(Class<?> aObject, String aMethod, Class<?>... aParameterTypes) {
        Method rMethod = null;
        try {
            rMethod = aObject.getMethod(aMethod, aParameterTypes);
            rMethod.setAccessible(true);
        } catch (Throwable e) {
            /* Do nothing */
        }
        return rMethod;
    }

    public static Method getMethod(Object aObject, String aMethod, Class<?>... aParameterTypes) {
        Method rMethod = null;
        try {
            rMethod = aObject.getClass()
                .getMethod(aMethod, aParameterTypes);
            rMethod.setAccessible(true);
        } catch (Throwable e) {
            /* Do nothing */
        }
        return rMethod;
    }

    public static Field getField(Object aObject, String aField, boolean aPrivate, boolean aLogErrors) {
        try {
            Field tField = (aObject instanceof Class) ? ((Class<?>) aObject).getDeclaredField(aField)
                : (aObject instanceof String) ? Class.forName((String) aObject)
                    .getDeclaredField(aField)
                    : aObject.getClass()
                        .getDeclaredField(aField);
            if (aPrivate) tField.setAccessible(true);
            return tField;
        } catch (Throwable e) {
            if (aLogErrors) e.printStackTrace(GT_Log.err);
        }
        return null;
    }

    public static Object getFieldContent(Object aObject, String aField, boolean aPrivate, boolean aLogErrors) {
        try {
            Field tField = (aObject instanceof Class) ? ((Class<?>) aObject).getDeclaredField(aField)
                : (aObject instanceof String) ? Class.forName((String) aObject)
                    .getDeclaredField(aField)
                    : aObject.getClass()
                        .getDeclaredField(aField);
            if (aPrivate) tField.setAccessible(true);
            return tField.get(aObject instanceof Class || aObject instanceof String ? null : aObject);
        } catch (Throwable e) {
            if (aLogErrors) e.printStackTrace(GT_Log.err);
        }
        return null;
    }

    public static Object callPublicMethod(Object aObject, String aMethod, Object... aParameters) {
        return callMethod(aObject, aMethod, false, false, true, aParameters);
    }

    public static Object callPrivateMethod(Object aObject, String aMethod, Object... aParameters) {
        return callMethod(aObject, aMethod, true, false, true, aParameters);
    }

    public static Object callMethod(Object aObject, String aMethod, boolean aPrivate, boolean aUseUpperCasedDataTypes,
        boolean aLogErrors, Object... aParameters) {
        try {
            Class<?>[] tParameterTypes = new Class<?>[aParameters.length];
            for (byte i = 0; i < aParameters.length; i++) {
                if (aParameters[i] instanceof Class) {
                    tParameterTypes[i] = (Class<?>) aParameters[i];
                    aParameters[i] = null;
                } else {
                    tParameterTypes[i] = aParameters[i].getClass();
                }
                if (!aUseUpperCasedDataTypes) {
                    if (tParameterTypes[i] == Boolean.class) tParameterTypes[i] = boolean.class;
                    else if (tParameterTypes[i] == Byte.class) tParameterTypes[i] = byte.class;
                    else if (tParameterTypes[i] == Short.class) tParameterTypes[i] = short.class;
                    else if (tParameterTypes[i] == Integer.class) tParameterTypes[i] = int.class;
                    else if (tParameterTypes[i] == Long.class) tParameterTypes[i] = long.class;
                    else if (tParameterTypes[i] == Float.class) tParameterTypes[i] = float.class;
                    else if (tParameterTypes[i] == Double.class) tParameterTypes[i] = double.class;
                }
            }

            Method tMethod = (aObject instanceof Class) ? ((Class<?>) aObject).getMethod(aMethod, tParameterTypes)
                : aObject.getClass()
                    .getMethod(aMethod, tParameterTypes);
            if (aPrivate) tMethod.setAccessible(true);
            return tMethod.invoke(aObject, aParameters);
        } catch (Throwable e) {
            if (aLogErrors) e.printStackTrace(GT_Log.err);
        }
        return null;
    }

    public static Object callConstructor(String aClass, int aConstructorIndex, Object aReplacementObject,
        boolean aLogErrors, Object... aParameters) {
        try {
            return callConstructor(
                Class.forName(aClass),
                aConstructorIndex,
                aReplacementObject,
                aLogErrors,
                aParameters);
        } catch (Throwable e) {
            if (aLogErrors) e.printStackTrace(GT_Log.err);
        }
        return aReplacementObject;
    }

    public static Object callConstructor(Class<?> aClass, int aConstructorIndex, Object aReplacementObject,
        boolean aLogErrors, Object... aParameters) {
        if (aConstructorIndex < 0) {
            try {
                for (Constructor<?> tConstructor : aClass.getConstructors()) {
                    try {
                        return tConstructor.newInstance(aParameters);
                    } catch (Throwable ignored) {}
                }
            } catch (Throwable e) {
                if (aLogErrors) e.printStackTrace(GT_Log.err);
            }
        } else {
            try {
                return aClass.getConstructors()[aConstructorIndex].newInstance(aParameters);
            } catch (Throwable e) {
                if (aLogErrors) e.printStackTrace(GT_Log.err);
            }
        }
        return aReplacementObject;
    }

    public static String capitalizeString(String aString) {
        if (aString != null && aString.length() > 0) return aString.substring(0, 1)
            .toUpperCase() + aString.substring(1);
        return E;
    }

    public static boolean getPotion(EntityLivingBase aPlayer, int aPotionIndex) {
        try {
            Field tPotionHashmap = null;

            Field[] fields = EntityLiving.class.getDeclaredFields();

            for (Field field : fields) {
                if (field.getType() == HashMap.class) {
                    tPotionHashmap = field;
                    tPotionHashmap.setAccessible(true);
                    break;
                }
            }

            if (tPotionHashmap != null) return ((HashMap<?, ?>) tPotionHashmap.get(aPlayer)).get(aPotionIndex) != null;
        } catch (Throwable e) {
            if (D1) e.printStackTrace(GT_Log.err);
        }
        return false;
    }

    public static String getClassName(Object aObject) {
        if (aObject == null) return "null";
        return aObject.getClass()
            .getName()
            .substring(
                aObject.getClass()
                    .getName()
                    .lastIndexOf(".") + 1);
    }

    public static void removePotion(EntityLivingBase aPlayer, int aPotionIndex) {
        try {
            Field tPotionHashmap = null;

            Field[] fields = EntityLiving.class.getDeclaredFields();

            for (Field field : fields) {
                if (field.getType() == HashMap.class) {
                    tPotionHashmap = field;
                    tPotionHashmap.setAccessible(true);
                    break;
                }
            }

            if (tPotionHashmap != null) ((HashMap<?, ?>) tPotionHashmap.get(aPlayer)).remove(aPotionIndex);
        } catch (Throwable e) {
            if (D1) e.printStackTrace(GT_Log.err);
        }
    }

    public static boolean getFullInvisibility(EntityPlayer aPlayer) {
        try {
            if (aPlayer.isInvisible()) {
                for (int i = 0; i < 4; i++) {
                    if (aPlayer.inventory.armorInventory[i] != null) {
                        if (aPlayer.inventory.armorInventory[i].getItem() instanceof GT_EnergyArmor_Item) {
                            if ((((GT_EnergyArmor_Item) aPlayer.inventory.armorInventory[i].getItem()).mSpecials & 512)
                                != 0) {
                                if (GT_ModHandler.canUseElectricItem(aPlayer.inventory.armorInventory[i], 10000)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable e) {
            if (D1) e.printStackTrace(GT_Log.err);
        }
        return false;
    }

    public static ItemStack suckOneItemStackAt(World aWorld, double aX, double aY, double aZ, double aL, double aH,
        double aW) {
        for (EntityItem tItem : aWorld.getEntitiesWithinAABB(
            EntityItem.class,
            AxisAlignedBB.getBoundingBox(aX, aY, aZ, aX + aL, aY + aH, aZ + aW))) {
            if (!tItem.isDead) {
                aWorld.removeEntity(tItem);
                tItem.setDead();
                return tItem.getEntityItem();
            }
        }
        return null;
    }

    public static byte getOppositeSide(ForgeDirection side) {
        return (byte) side.getOpposite()
            .ordinal();
    }

    public static byte getTier(long l) {
        byte i = -1;
        while (++i < V.length) if (l <= V[i]) return i;
        return (byte) (V.length - 1);
    }

    public static long getAmperageForTier(long voltage, byte tier) {
        return ceilDiv(voltage, GT_Values.V[tier]);
    }

    /**
     * Rounds up partial voltage that exceeds tiered voltage, e.g. 4,096 -> 8,192(IV)
     */
    public static long roundUpVoltage(long voltage) {
        if (voltage > V[V.length - 1]) {
            return voltage;
        }
        return V[GT_Utility.getTier(voltage)];
    }

    public static String getColoredTierNameFromVoltage(long voltage) {
        return getColoredTierNameFromTier(getTier(voltage));
    }

    public static String getColoredTierNameFromTier(byte tier) {
        return GT_Values.TIER_COLORS[tier] + GT_Values.VN[tier] + EnumChatFormatting.RESET;
    }

    /**
     * @return e.g. {@code " (LV)"}
     */
    @Nonnull
    public static String getTierNameWithParentheses(long voltage) {
        byte tier = getTier(voltage);
        if (tier < 0) {
            return "";
        } else if (tier >= GT_Values.VN.length - 1) {
            return " (MAX+)";
        }
        return " (" + GT_Values.VN[tier] + ")";
    }

    public static void sendChatToPlayer(EntityPlayer aPlayer, String aChatMessage) {
        if (aPlayer instanceof EntityPlayerMP && aChatMessage != null) {
            aPlayer.addChatComponentMessage(new ChatComponentText(aChatMessage));
        }
    }

    public static void checkAvailabilities() {
        if (CHECK_ALL) {
            try {
                Class<IItemDuct> tClass = IItemDuct.class;
                tClass.getCanonicalName();
                TE_CHECK = true;
            } catch (Throwable e) {
                /**/
            }
            try {
                Class<IPipeTile> tClass = buildcraft.api.transport.IPipeTile.class;
                tClass.getCanonicalName();
                BC_CHECK = true;
            } catch (Throwable e) {
                /**/
            }
            try {
                Class<IEnergyReceiver> tClass = cofh.api.energy.IEnergyReceiver.class;
                tClass.getCanonicalName();
                RF_CHECK = true;
            } catch (Throwable e) {
                /**/
            }
            CHECK_ALL = false;
        }
    }

    public static boolean isConnectableNonInventoryPipe(TileEntity tileEntity, ForgeDirection side) {
        if (tileEntity == null) return false;
        checkAvailabilities();
        if (TE_CHECK && tileEntity instanceof IItemDuct) return true;
        if (BC_CHECK && tileEntity instanceof buildcraft.api.transport.IPipeTile pipeTile)
            return pipeTile.isPipeConnected(side);
        return Translocator.isModLoaded() && tileEntity instanceof codechicken.translocator.TileItemTranslocator;
    }

    /**
     * Moves Stack from Inv-Slot to Inv-Slot, without checking if its even allowed.
     *
     * @return the Amount of moved Items
     */
    public static byte moveStackIntoPipe(IInventory aTileEntity1, Object aTileEntity2, int[] aGrabSlots,
        ForgeDirection fromSide, ForgeDirection putSide, List<ItemStack> aFilter, boolean aInvertFilter,
        byte aMaxTargetStackSize, byte aMinTargetStackSize, byte aMaxMoveAtOnce, byte aMinMoveAtOnce) {
        return moveStackIntoPipe(
            aTileEntity1,
            aTileEntity2,
            aGrabSlots,
            fromSide,
            putSide,
            aFilter,
            aInvertFilter,
            aMaxTargetStackSize,
            aMinTargetStackSize,
            aMaxMoveAtOnce,
            aMinMoveAtOnce,
            true);
    }

    /**
     * Moves Stack from Inv-Slot to Inv-Slot, without checking if it is even allowed.
     *
     * @return the Amount of moved Items
     */
    public static byte moveStackIntoPipe(IInventory fromInventory, Object toObject, int[] fromSlots,
        ForgeDirection fromSide, ForgeDirection putSide, List<ItemStack> aFilter, boolean aInvertFilter,
        byte aMaxTargetStackSize, byte aMinTargetStackSize, byte aMaxMoveAtOnce, byte aMinMoveAtOnce,
        boolean dropItem) {
        if (fromInventory == null || aMaxTargetStackSize <= 0
            || aMinTargetStackSize <= 0
            || aMinTargetStackSize > aMaxTargetStackSize
            || aMaxMoveAtOnce <= 0
            || aMinMoveAtOnce > aMaxMoveAtOnce) return 0;
        if (toObject != null) {
            checkAvailabilities();
            if (TE_CHECK && toObject instanceof IItemDuct itemDuct) {
                for (final int aGrabSlot : fromSlots) {
                    if (listContainsItem(aFilter, fromInventory.getStackInSlot(aGrabSlot), true, aInvertFilter)) {
                        if (isAllowedToTakeFromSlot(
                            fromInventory,
                            aGrabSlot,
                            fromSide,
                            fromInventory.getStackInSlot(aGrabSlot))) {
                            if (Math.max(aMinMoveAtOnce, aMinTargetStackSize)
                                <= fromInventory.getStackInSlot(aGrabSlot).stackSize) {
                                ItemStack tStack = copyAmount(
                                    Math.min(
                                        fromInventory.getStackInSlot(aGrabSlot).stackSize,
                                        Math.min(aMaxMoveAtOnce, aMaxTargetStackSize)),
                                    fromInventory.getStackInSlot(aGrabSlot));
                                ItemStack rStack = itemDuct.insertItem(putSide, copyOrNull(tStack));
                                byte tMovedItemCount = (byte) (tStack.stackSize
                                    - (rStack == null ? 0 : rStack.stackSize));
                                if (tMovedItemCount >= 1 /* Math.max(aMinMoveAtOnce, aMinTargetStackSize) */) {
                                    fromInventory.decrStackSize(aGrabSlot, tMovedItemCount);
                                    fromInventory.markDirty();
                                    return tMovedItemCount;
                                }
                            }
                        }
                    }
                }
                return 0;
            }
            if (BC_CHECK && toObject instanceof buildcraft.api.transport.IPipeTile bcPipe) {
                for (int fromSlot : fromSlots) {
                    if (listContainsItem(aFilter, fromInventory.getStackInSlot(fromSlot), true, aInvertFilter)) {
                        if (isAllowedToTakeFromSlot(
                            fromInventory,
                            fromSlot,
                            fromSide,
                            fromInventory.getStackInSlot(fromSlot))) {
                            if (Math.max(aMinMoveAtOnce, aMinTargetStackSize)
                                <= fromInventory.getStackInSlot(fromSlot).stackSize) {
                                ItemStack tStack = copyAmount(
                                    Math.min(
                                        fromInventory.getStackInSlot(fromSlot).stackSize,
                                        Math.min(aMaxMoveAtOnce, aMaxTargetStackSize)),
                                    fromInventory.getStackInSlot(fromSlot));
                                byte tMovedItemCount = (byte) bcPipe.injectItem(copyOrNull(tStack), false, putSide);
                                if (tMovedItemCount >= Math.max(aMinMoveAtOnce, aMinTargetStackSize)) {
                                    tMovedItemCount = (byte) (bcPipe
                                        .injectItem(copyAmount(tMovedItemCount, tStack), true, putSide));
                                    fromInventory.decrStackSize(fromSlot, tMovedItemCount);
                                    fromInventory.markDirty();
                                    return tMovedItemCount;
                                }
                            }
                        }
                    }
                }
                return 0;
            }
        }

        if (fromInventory instanceof TileEntity fromTileEntity && fromSide != ForgeDirection.UNKNOWN
            && fromSide.getOpposite() == ForgeDirection.getOrientation(putSide.ordinal())) {
            int tX = fromTileEntity.xCoord + fromSide.offsetX, tY = fromTileEntity.yCoord + fromSide.offsetY,
                tZ = fromTileEntity.zCoord + fromSide.offsetZ;
            if (!hasBlockHitBox(((TileEntity) fromInventory).getWorldObj(), tX, tY, tZ) && dropItem) {
                for (final int fromSlot : fromSlots) {
                    if (listContainsItem(aFilter, fromInventory.getStackInSlot(fromSlot), true, aInvertFilter)) {
                        if (isAllowedToTakeFromSlot(
                            fromInventory,
                            fromSlot,
                            fromSide,
                            fromInventory.getStackInSlot(fromSlot))) {
                            if (Math.max(aMinMoveAtOnce, aMinTargetStackSize)
                                <= fromInventory.getStackInSlot(fromSlot).stackSize) {
                                final ItemStack tStack = copyAmount(
                                    Math.min(
                                        fromInventory.getStackInSlot(fromSlot).stackSize,
                                        Math.min(aMaxMoveAtOnce, aMaxTargetStackSize)),
                                    fromInventory.getStackInSlot(fromSlot));
                                final EntityItem tEntity = new EntityItem(
                                    ((TileEntity) fromInventory).getWorldObj(),
                                    tX + 0.5,
                                    tY + 0.5,
                                    tZ + 0.5,
                                    tStack);
                                tEntity.motionX = tEntity.motionY = tEntity.motionZ = 0;
                                ((TileEntity) fromInventory).getWorldObj()
                                    .spawnEntityInWorld(tEntity);
                                assert tStack != null;
                                fromInventory.decrStackSize(fromSlot, tStack.stackSize);
                                fromInventory.markDirty();
                                return (byte) tStack.stackSize;
                            }
                        }
                    }
                }
            }
        }
        return 0;
    }

    /**
     * Moves Stack from Inv-Slot to Inv-Slot, without checking if its even allowed. (useful for internal Inventory
     * Operations)
     *
     * @return the Amount of moved Items
     */
    public static byte moveStackFromSlotAToSlotB(IInventory aTileEntity1, IInventory aTileEntity2, int aGrabFrom,
        int aPutTo, byte aMaxTargetStackSize, byte aMinTargetStackSize, byte aMaxMoveAtOnce, byte aMinMoveAtOnce) {
        if (aTileEntity1 == null || aTileEntity2 == null
            || aMinTargetStackSize <= 0
            || aMinTargetStackSize > aMaxTargetStackSize
            || aMaxMoveAtOnce <= 0
            || aMinMoveAtOnce > aMaxMoveAtOnce) return 0;

        ItemStack tStack1 = aTileEntity1.getStackInSlot(aGrabFrom), tStack2 = aTileEntity2.getStackInSlot(aPutTo),
            tStack3;
        if (tStack1 != null) {
            if (tStack2 != null && !areStacksEqual(tStack1, tStack2)) return 0;
            tStack3 = copyOrNull(tStack1);
            aMaxTargetStackSize = (byte) Math.min(
                aMaxTargetStackSize,
                Math.min(
                    tStack3.getMaxStackSize(),
                    Math.min(
                        tStack2 == null ? Integer.MAX_VALUE : tStack2.getMaxStackSize(),
                        aTileEntity2.getInventoryStackLimit())));
            tStack3.stackSize = Math
                .min(tStack3.stackSize, aMaxTargetStackSize - (tStack2 == null ? 0 : tStack2.stackSize));
            if (tStack3.stackSize > aMaxMoveAtOnce) tStack3.stackSize = aMaxMoveAtOnce;
            if (tStack3.stackSize + (tStack2 == null ? 0 : tStack2.stackSize)
                >= Math.min(tStack3.getMaxStackSize(), aMinTargetStackSize) && tStack3.stackSize >= aMinMoveAtOnce) {
                tStack3 = aTileEntity1.decrStackSize(aGrabFrom, tStack3.stackSize);
                aTileEntity1.markDirty();
                if (tStack3 != null) {
                    if (tStack2 == null) {
                        aTileEntity2.setInventorySlotContents(aPutTo, copyOrNull(tStack3));
                    } else {
                        tStack2.stackSize += tStack3.stackSize;
                    }
                    aTileEntity2.markDirty();
                    return (byte) tStack3.stackSize;
                }
            }
        }
        return 0;
    }

    public static boolean isAllowedToTakeFromSlot(IInventory aTileEntity, int aSlot, ForgeDirection side,
        ItemStack aStack) {
        if (side == ForgeDirection.UNKNOWN) {
            return Arrays.stream(ForgeDirection.VALID_DIRECTIONS)
                .anyMatch(d -> isAllowedToTakeFromSlot(aTileEntity, aSlot, d, aStack));
        }
        if (aTileEntity instanceof ISidedInventory sided) return sided.canExtractItem(aSlot, aStack, side.ordinal());
        return true;
    }

    public static boolean isAllowedToPutIntoSlot(IInventory aTileEntity, int aSlot, ForgeDirection side,
        ItemStack aStack, byte aMaxStackSize) {
        ItemStack tStack = aTileEntity.getStackInSlot(aSlot);
        if (tStack != null && (!areStacksEqual(tStack, aStack) || tStack.stackSize >= tStack.getMaxStackSize()))
            return false;
        if (side == ForgeDirection.UNKNOWN) {
            return Arrays.stream(ForgeDirection.VALID_DIRECTIONS)
                .anyMatch(d -> isAllowedToPutIntoSlot(aTileEntity, aSlot, d, aStack, aMaxStackSize));
        }
        if (aTileEntity instanceof ISidedInventory
            && !((ISidedInventory) aTileEntity).canInsertItem(aSlot, aStack, side.ordinal())) return false;
        return aSlot < aTileEntity.getSizeInventory() && aTileEntity.isItemValidForSlot(aSlot, aStack);
    }

    /**
     * moves multiple stacks from Inv-Side to Inv-Side
     *
     * @return the Amount of moved Items
     */
    public static int moveMultipleItemStacks(Object aTileEntity1, Object aTileEntity2, ForgeDirection fromSide,
        ForgeDirection putSide, List<ItemStack> aFilter, boolean aInvertFilter, byte aMaxTargetStackSize,
        byte aMinTargetStackSize, byte aMaxMoveAtOnce, byte aMinMoveAtOnce, int aStackAmount) {
        if (aTileEntity1 instanceof IInventory) return moveMultipleItemStacks(
            (IInventory) aTileEntity1,
            aTileEntity2,
            fromSide,
            putSide,
            aFilter,
            aInvertFilter,
            aMaxTargetStackSize,
            aMinTargetStackSize,
            aMaxMoveAtOnce,
            aMinMoveAtOnce,
            aStackAmount,
            true);
        return 0;
    }

    public static int moveMultipleItemStacks(IInventory fromInventory, Object toObject, ForgeDirection fromSide,
        ForgeDirection putSide, List<ItemStack> aFilter, boolean aInvertFilter, byte aMaxTargetStackSize,
        byte aMinTargetStackSize, byte aMaxMoveAtOnce, byte aMinMoveAtOnce, int aMaxStackTransfer,
        boolean aDoCheckChests) {
        if (fromInventory == null || aMaxTargetStackSize <= 0
            || aMinTargetStackSize <= 0
            || aMaxMoveAtOnce <= 0
            || aMinTargetStackSize > aMaxTargetStackSize
            || aMinMoveAtOnce > aMaxMoveAtOnce
            || aMaxStackTransfer == 0) return 0;

        // find where to take from
        final int[] tGrabSlots = new int[fromInventory.getSizeInventory()];
        int tGrabSlotsSize = 0;
        if (fromInventory instanceof ISidedInventory) {
            for (int i : ((ISidedInventory) fromInventory).getAccessibleSlotsFromSide(fromSide.ordinal())) {
                final ItemStack s = fromInventory.getStackInSlot(i);
                if (s == null || !isAllowedToTakeFromSlot(fromInventory, i, fromSide, s)
                    || s.stackSize < aMinMoveAtOnce
                    || !listContainsItem(aFilter, s, true, aInvertFilter)) continue;
                tGrabSlots[tGrabSlotsSize++] = i;
            }
        } else {
            for (int i = 0; i < tGrabSlots.length; i++) {
                ItemStack s = fromInventory.getStackInSlot(i);
                if (s == null || s.stackSize < aMinMoveAtOnce || !listContainsItem(aFilter, s, true, aInvertFilter))
                    continue;
                tGrabSlots[tGrabSlotsSize++] = i;
            }
        }

        // no source, bail out
        if (tGrabSlotsSize == 0) {
            // maybe source is a double chest. check it
            if (aDoCheckChests && fromInventory instanceof TileEntityChest chest) return moveFromAdjacentChests(
                chest,
                toObject,
                fromSide,
                putSide,
                aFilter,
                aInvertFilter,
                aMaxTargetStackSize,
                aMinTargetStackSize,
                aMaxMoveAtOnce,
                aMinMoveAtOnce,
                aMaxStackTransfer);
            return 0;
        }

        // if target is an inventory, e.g. chest, machine, drawers...
        if (toObject instanceof IInventory toInventory) {

            // partially filled slot spare space mapping.
            // value is the sum of all spare space left not counting completely empty slot
            final HashMap<ItemId, Integer> tPutItems = new HashMap<>(toInventory.getSizeInventory());
            // partially filled slot contents
            final HashMap<ItemId, List<ItemStack>> tPutItemStacks = new HashMap<>(toInventory.getSizeInventory());
            // completely empty slots
            final List<Integer> tPutFreeSlots = new ArrayList<>(toInventory.getSizeInventory());

            // find possible target slots
            int[] accessibleSlots = null;
            if (toObject instanceof ISidedInventory sided)
                accessibleSlots = sided.getAccessibleSlotsFromSide(putSide.ordinal());
            for (int i = 0; i < toInventory.getSizeInventory(); i++) {
                int slot = i;
                if (accessibleSlots != null) {
                    if (accessibleSlots.length <= i) break;
                    slot = accessibleSlots[slot];
                }
                ItemStack s = toInventory.getStackInSlot(slot);
                if (s == null) {
                    tPutFreeSlots.add(slot);
                } else if ((s.stackSize < s.getMaxStackSize() && s.stackSize < toInventory.getInventoryStackLimit())
                    && aMinMoveAtOnce <= s.getMaxStackSize() - s.stackSize
                    && isAllowedToPutIntoSlot(toInventory, slot, putSide, s, (byte) 64)) {
                        ItemId sID = ItemId.createNoCopy(s);
                        tPutItems.merge(
                            sID,
                            (Math.min(s.getMaxStackSize(), toInventory.getInventoryStackLimit()) - s.stackSize),
                            Integer::sum);
                        tPutItemStacks.computeIfAbsent(sID, k -> new ArrayList<>())
                            .add(s);
                    }
            }

            // target completely filled, bail out
            if (tPutItems.isEmpty() && tPutFreeSlots.isEmpty()) {
                // maybe target is a double chest. check it.
                if (aDoCheckChests && toObject instanceof TileEntityChest chest) return moveToAdjacentChests(
                    fromInventory,
                    chest,
                    fromSide,
                    putSide,
                    aFilter,
                    aInvertFilter,
                    aMaxTargetStackSize,
                    aMinTargetStackSize,
                    aMaxMoveAtOnce,
                    aMinMoveAtOnce,
                    aMaxStackTransfer);
                return 0;
            }

            // go over source stacks one by one
            int tStacksMoved = 0, tTotalItemsMoved = 0;
            for (int j = 0; j < tGrabSlotsSize; j++) {
                final int grabSlot = tGrabSlots[j];
                int tMovedItems;
                int tStackSize;
                do {
                    tMovedItems = 0;
                    final ItemStack tGrabStack = fromInventory.getStackInSlot(grabSlot);
                    if (tGrabStack == null) break;
                    tStackSize = tGrabStack.stackSize;
                    final ItemId sID = ItemId.createNoCopy(tGrabStack);

                    if (tPutItems.containsKey(sID)) {
                        // there is a partially filled slot, try merging
                        final int canPut = Math.min(tPutItems.get(sID), aMaxMoveAtOnce);
                        if (canPut >= aMinMoveAtOnce) {
                            final List<ItemStack> putStack = tPutItemStacks.get(sID);
                            if (!putStack.isEmpty()) {
                                // can move, do merge
                                int toPut = Math.min(canPut, tStackSize);
                                tMovedItems = toPut;
                                for (int i = 0; i < putStack.size(); i++) {
                                    final ItemStack s = putStack.get(i);
                                    final int sToPut = Math.min(
                                        Math.min(
                                            Math.min(toPut, s.getMaxStackSize() - s.stackSize),
                                            toInventory.getInventoryStackLimit() - s.stackSize),
                                        aMaxTargetStackSize - s.stackSize);
                                    if (sToPut <= 0) continue;
                                    if (sToPut < aMinMoveAtOnce) continue;
                                    if (s.stackSize + sToPut < aMinTargetStackSize) continue;
                                    toPut -= sToPut;
                                    s.stackSize += sToPut;
                                    if (s.stackSize == s.getMaxStackSize()
                                        || s.stackSize == toInventory.getInventoryStackLimit()) {
                                        // this slot is full. remove this stack from candidate list
                                        putStack.remove(i);
                                        i--;
                                    }
                                    if (toPut == 0) break;
                                }
                                tMovedItems -= toPut;
                                if (tMovedItems > 0) {
                                    tStackSize -= tMovedItems;
                                    tTotalItemsMoved += tMovedItems;
                                    // deduct spare space
                                    tPutItems.merge(sID, tMovedItems, (a, b) -> a.equals(b) ? null : a - b);

                                    if (tStackSize == 0) fromInventory.setInventorySlotContents(grabSlot, null);
                                    else tGrabStack.stackSize = tStackSize;

                                    fromInventory.markDirty();
                                    toInventory.markDirty();
                                }
                            }
                        }
                    }
                    // still stuff to move & have completely empty slots
                    if (tStackSize > 0 && !tPutFreeSlots.isEmpty()) {
                        for (int i = 0; i < tPutFreeSlots.size(); i++) {
                            final int tPutSlot = tPutFreeSlots.get(i);
                            if (isAllowedToPutIntoSlot(toInventory, tPutSlot, putSide, tGrabStack, (byte) 64)) {
                                // allowed, now do moving
                                final int tMoved = moveStackFromSlotAToSlotB(
                                    fromInventory,
                                    toInventory,
                                    grabSlot,
                                    tPutSlot,
                                    aMaxTargetStackSize,
                                    aMinTargetStackSize,
                                    (byte) (aMaxMoveAtOnce - tMovedItems),
                                    aMinMoveAtOnce);
                                if (tMoved > 0) {
                                    final ItemStack s = toInventory.getStackInSlot(tPutSlot);
                                    if (s != null) {
                                        // s might be null if tPutInventory is very special, e.g. infinity chest
                                        // if s is null, we will not mark this slot as target candidate for anything
                                        final int spare = Math
                                            .min(s.getMaxStackSize(), toInventory.getInventoryStackLimit())
                                            - s.stackSize;
                                        if (spare > 0) {
                                            final ItemId ssID = ItemId.createNoCopy(s);
                                            // add back to spare space count
                                            tPutItems.merge(ssID, spare, Integer::sum);
                                            // add to partially filled slot list
                                            tPutItemStacks.computeIfAbsent(ssID, k -> new ArrayList<>())
                                                .add(s);
                                        }
                                        // this is no longer free
                                        tPutFreeSlots.remove(i);
                                        i--;
                                    }
                                    // else -> noop
                                    // this is still a free slot. no need to do anything.
                                    tTotalItemsMoved += tMoved;
                                    tMovedItems += tMoved;
                                    tStackSize -= tMoved;
                                    if (tStackSize == 0) break;
                                }
                            }
                        }
                    }

                    if (tMovedItems > 0) {
                        // check if we have moved enough stacks
                        if (++tStacksMoved >= aMaxStackTransfer) return tTotalItemsMoved;
                    }
                } while (tMovedItems > 0 && tStackSize > 0); // support inventories that store more than a stack in a
                // slot
            }

            // check if source is a double chest, if yes, try move from the adjacent as well
            if (aDoCheckChests && fromInventory instanceof TileEntityChest chest) {
                final int tAmount = moveFromAdjacentChests(
                    chest,
                    toObject,
                    fromSide,
                    putSide,
                    aFilter,
                    aInvertFilter,
                    aMaxTargetStackSize,
                    aMinTargetStackSize,
                    aMaxMoveAtOnce,
                    aMinMoveAtOnce,
                    aMaxStackTransfer - tStacksMoved);
                if (tAmount != 0) return tAmount + tTotalItemsMoved;
            }

            // check if target is a double chest, if yes, try move to the adjacent as well
            if (aDoCheckChests && toObject instanceof TileEntityChest chest) {
                final int tAmount = moveToAdjacentChests(
                    fromInventory,
                    chest,
                    fromSide,
                    putSide,
                    aFilter,
                    aInvertFilter,
                    aMaxTargetStackSize,
                    aMinTargetStackSize,
                    aMaxMoveAtOnce,
                    aMinMoveAtOnce,
                    aMaxStackTransfer - tStacksMoved);
                if (tAmount != 0) return tAmount + tTotalItemsMoved;
            }

            return tTotalItemsMoved;
        }
        // there should be a function to transfer more than 1 stack in a pipe
        // however I do not see any ways to improve it. too much work for what it is worth
        int tTotalItemsMoved = 0;
        final int tGrabInventorySize = tGrabSlots.length;
        for (int i = 0; i < tGrabInventorySize; i++) {
            final int tMoved = moveStackIntoPipe(
                fromInventory,
                toObject,
                tGrabSlots,
                fromSide,
                putSide,
                aFilter,
                aInvertFilter,
                aMaxTargetStackSize,
                aMinTargetStackSize,
                aMaxMoveAtOnce,
                aMinMoveAtOnce,
                aDoCheckChests);
            if (tMoved == 0) return tTotalItemsMoved;
            else tTotalItemsMoved += tMoved;
        }
        return 0;
    }

    private static int moveToAdjacentChests(IInventory aTileEntity1, TileEntityChest aTargetChest,
        ForgeDirection fromSide, ForgeDirection putSide, List<ItemStack> aFilter, boolean aInvertFilter,
        byte aMaxTargetStackSize, byte aMinTargetStackSize, byte aMaxMoveAtOnce, byte aMinMoveAtOnce,
        int aMaxStackTransfer) {
        if (aTargetChest.adjacentChestChecked) {
            if (aTargetChest.adjacentChestXNeg != null) {
                return moveMultipleItemStacks(
                    aTileEntity1,
                    aTargetChest.adjacentChestXNeg,
                    fromSide,
                    putSide,
                    aFilter,
                    aInvertFilter,
                    aMaxTargetStackSize,
                    aMinTargetStackSize,
                    aMaxMoveAtOnce,
                    aMinMoveAtOnce,
                    aMaxStackTransfer,
                    false);
            } else if (aTargetChest.adjacentChestZNeg != null) {
                return moveMultipleItemStacks(
                    aTileEntity1,
                    aTargetChest.adjacentChestZNeg,
                    fromSide,
                    putSide,
                    aFilter,
                    aInvertFilter,
                    aMaxTargetStackSize,
                    aMinTargetStackSize,
                    aMaxMoveAtOnce,
                    aMinMoveAtOnce,
                    aMaxStackTransfer,
                    false);
            } else if (aTargetChest.adjacentChestXPos != null) {
                return moveMultipleItemStacks(
                    aTileEntity1,
                    aTargetChest.adjacentChestXPos,
                    fromSide,
                    putSide,
                    aFilter,
                    aInvertFilter,
                    aMaxTargetStackSize,
                    aMinTargetStackSize,
                    aMaxMoveAtOnce,
                    aMinMoveAtOnce,
                    aMaxStackTransfer,
                    false);
            } else if (aTargetChest.adjacentChestZPos != null) {
                return moveMultipleItemStacks(
                    aTileEntity1,
                    aTargetChest.adjacentChestZPos,
                    fromSide,
                    putSide,
                    aFilter,
                    aInvertFilter,
                    aMaxTargetStackSize,
                    aMinTargetStackSize,
                    aMaxMoveAtOnce,
                    aMinMoveAtOnce,
                    aMaxStackTransfer,
                    false);
            }
        }
        return 0;
    }

    private static int moveFromAdjacentChests(TileEntityChest fromTileEntityChest, Object toObject,
        ForgeDirection fromSide, ForgeDirection putSide, List<ItemStack> aFilter, boolean aInvertFilter,
        byte aMaxTargetStackSize, byte aMinTargetStackSize, byte aMaxMoveAtOnce, byte aMinMoveAtOnce,
        int aMaxStackTransfer) {
        if (fromTileEntityChest.adjacentChestXNeg != null) {
            return moveMultipleItemStacks(
                fromTileEntityChest.adjacentChestXNeg,
                toObject,
                fromSide,
                putSide,
                aFilter,
                aInvertFilter,
                aMaxTargetStackSize,
                aMinTargetStackSize,
                aMaxMoveAtOnce,
                aMinMoveAtOnce,
                aMaxStackTransfer,
                false);
        } else if (fromTileEntityChest.adjacentChestZNeg != null) {
            return moveMultipleItemStacks(
                fromTileEntityChest.adjacentChestZNeg,
                toObject,
                fromSide,
                putSide,
                aFilter,
                aInvertFilter,
                aMaxTargetStackSize,
                aMinTargetStackSize,
                aMaxMoveAtOnce,
                aMinMoveAtOnce,
                aMaxStackTransfer,
                false);
        } else if (fromTileEntityChest.adjacentChestXPos != null) {
            return moveMultipleItemStacks(
                fromTileEntityChest.adjacentChestXPos,
                toObject,
                fromSide,
                putSide,
                aFilter,
                aInvertFilter,
                aMaxTargetStackSize,
                aMinTargetStackSize,
                aMaxMoveAtOnce,
                aMinMoveAtOnce,
                aMaxStackTransfer,
                false);
        } else if (fromTileEntityChest.adjacentChestZPos != null) {
            return moveMultipleItemStacks(
                fromTileEntityChest.adjacentChestZPos,
                toObject,
                fromSide,
                putSide,
                aFilter,
                aInvertFilter,
                aMaxTargetStackSize,
                aMinTargetStackSize,
                aMaxMoveAtOnce,
                aMinMoveAtOnce,
                aMaxStackTransfer,
                false);
        }
        return 0;
    }

    /**
     * Moves Stack from Inv-Side to Inv-Side.
     *
     * @return the Amount of moved Items
     */
    public static byte moveOneItemStack(Object fromObject, Object toObject, ForgeDirection fromSide,
        ForgeDirection putSide, List<ItemStack> aFilter, boolean aInvertFilter, byte aMaxTargetStackSize,
        byte aMinTargetStackSize, byte aMaxMoveAtOnce, byte aMinMoveAtOnce) {
        if (fromObject instanceof IInventory inv) return moveOneItemStack(
            inv,
            toObject,
            fromSide,
            putSide,
            aFilter,
            aInvertFilter,
            aMaxTargetStackSize,
            aMinTargetStackSize,
            aMaxMoveAtOnce,
            aMinMoveAtOnce,
            true);
        return 0;
    }

    /**
     * This is only because I needed an additional Parameter for the Double Chest Check.
     */
    private static byte moveOneItemStack(IInventory fromInventory, Object toObject, ForgeDirection fromSide,
        ForgeDirection putSide, List<ItemStack> aFilter, boolean aInvertFilter, byte aMaxTargetStackSize,
        byte aMinTargetStackSize, byte aMaxMoveAtOnce, byte aMinMoveAtOnce, boolean aDoCheckChests) {
        if (fromInventory == null || aMaxTargetStackSize <= 0
            || aMinTargetStackSize <= 0
            || aMaxMoveAtOnce <= 0
            || aMinTargetStackSize > aMaxTargetStackSize
            || aMinMoveAtOnce > aMaxMoveAtOnce) return 0;

        int[] tGrabSlots = null;
        if (fromInventory instanceof ISidedInventory)
            tGrabSlots = ((ISidedInventory) fromInventory).getAccessibleSlotsFromSide(fromSide.ordinal());
        if (tGrabSlots == null) {
            tGrabSlots = new int[fromInventory.getSizeInventory()];
            for (int i = 0; i < tGrabSlots.length; i++) tGrabSlots[i] = i;
        }

        if (toObject instanceof IInventory inv) {
            int[] tPutSlots = null;
            if (toObject instanceof ISidedInventory sided)
                tPutSlots = sided.getAccessibleSlotsFromSide(putSide.ordinal());

            if (tPutSlots == null) {
                tPutSlots = new int[inv.getSizeInventory()];
                for (int i = 0; i < tPutSlots.length; i++) tPutSlots[i] = i;
            }

            for (final int tGrabSlot : tGrabSlots) {
                byte tMovedItemCount = 0;
                final ItemStack tGrabStack = fromInventory.getStackInSlot(tGrabSlot);
                if (listContainsItem(aFilter, tGrabStack, true, aInvertFilter)
                    && (tGrabStack.stackSize >= aMinMoveAtOnce
                        && isAllowedToTakeFromSlot(fromInventory, tGrabSlot, fromSide, tGrabStack))) {
                    for (final int tPutSlot : tPutSlots) {
                        if (isAllowedToPutIntoSlot(inv, tPutSlot, putSide, tGrabStack, aMaxTargetStackSize)) {
                            tMovedItemCount += moveStackFromSlotAToSlotB(
                                fromInventory,
                                inv,
                                tGrabSlot,
                                tPutSlot,
                                aMaxTargetStackSize,
                                aMinTargetStackSize,
                                (byte) (aMaxMoveAtOnce - tMovedItemCount),
                                aMinMoveAtOnce);
                            if (tMovedItemCount >= aMaxMoveAtOnce || (tMovedItemCount > 0 && aMaxTargetStackSize < 64))
                                return tMovedItemCount;
                        }
                    }

                }
                if (tMovedItemCount > 0) return tMovedItemCount;
            }

            if (aDoCheckChests && fromInventory instanceof TileEntityChest fromChest
                && (fromChest.adjacentChestChecked)) {
                byte tAmount = 0;
                if (fromChest.adjacentChestXNeg != null) {
                    tAmount = moveOneItemStack(
                        fromChest.adjacentChestXNeg,
                        toObject,
                        fromSide,
                        putSide,
                        aFilter,
                        aInvertFilter,
                        aMaxTargetStackSize,
                        aMinTargetStackSize,
                        aMaxMoveAtOnce,
                        aMinMoveAtOnce,
                        false);
                } else if (fromChest.adjacentChestZNeg != null) {
                    tAmount = moveOneItemStack(
                        fromChest.adjacentChestZNeg,
                        toObject,
                        fromSide,
                        putSide,
                        aFilter,
                        aInvertFilter,
                        aMaxTargetStackSize,
                        aMinTargetStackSize,
                        aMaxMoveAtOnce,
                        aMinMoveAtOnce,
                        false);
                } else if (fromChest.adjacentChestXPos != null) {
                    tAmount = moveOneItemStack(
                        fromChest.adjacentChestXPos,
                        toObject,
                        fromSide,
                        putSide,
                        aFilter,
                        aInvertFilter,
                        aMaxTargetStackSize,
                        aMinTargetStackSize,
                        aMaxMoveAtOnce,
                        aMinMoveAtOnce,
                        false);
                } else if (fromChest.adjacentChestZPos != null) {
                    tAmount = moveOneItemStack(
                        fromChest.adjacentChestZPos,
                        toObject,
                        fromSide,
                        putSide,
                        aFilter,
                        aInvertFilter,
                        aMaxTargetStackSize,
                        aMinTargetStackSize,
                        aMaxMoveAtOnce,
                        aMinMoveAtOnce,
                        false);
                }
                if (tAmount != 0) return tAmount;

            }
            if (aDoCheckChests && toObject instanceof TileEntityChest toChest && (toChest.adjacentChestChecked)) {
                byte tAmount = 0;
                if (toChest.adjacentChestXNeg != null) {
                    tAmount = moveOneItemStack(
                        fromInventory,
                        toChest.adjacentChestXNeg,
                        fromSide,
                        putSide,
                        aFilter,
                        aInvertFilter,
                        aMaxTargetStackSize,
                        aMinTargetStackSize,
                        aMaxMoveAtOnce,
                        aMinMoveAtOnce,
                        false);
                } else if (toChest.adjacentChestZNeg != null) {
                    tAmount = moveOneItemStack(
                        fromInventory,
                        toChest.adjacentChestZNeg,
                        fromSide,
                        putSide,
                        aFilter,
                        aInvertFilter,
                        aMaxTargetStackSize,
                        aMinTargetStackSize,
                        aMaxMoveAtOnce,
                        aMinMoveAtOnce,
                        false);
                } else if (toChest.adjacentChestXPos != null) {
                    tAmount = moveOneItemStack(
                        fromInventory,
                        toChest.adjacentChestXPos,
                        fromSide,
                        putSide,
                        aFilter,
                        aInvertFilter,
                        aMaxTargetStackSize,
                        aMinTargetStackSize,
                        aMaxMoveAtOnce,
                        aMinMoveAtOnce,
                        false);
                } else if (toChest.adjacentChestZPos != null) {
                    tAmount = moveOneItemStack(
                        fromInventory,
                        toChest.adjacentChestZPos,
                        fromSide,
                        putSide,
                        aFilter,
                        aInvertFilter,
                        aMaxTargetStackSize,
                        aMinTargetStackSize,
                        aMaxMoveAtOnce,
                        aMinMoveAtOnce,
                        false);
                }
                if (tAmount != 0) return tAmount;

            }
        }

        return moveStackIntoPipe(
            fromInventory,
            toObject,
            tGrabSlots,
            fromSide,
            putSide,
            aFilter,
            aInvertFilter,
            aMaxTargetStackSize,
            aMinTargetStackSize,
            aMaxMoveAtOnce,
            aMinMoveAtOnce,
            aDoCheckChests);
    }

    /**
     * Moves Stack from Inv-Side to Inv-Slot.
     *
     * @return the Amount of moved Items
     */
    public static byte moveOneItemStackIntoSlot(Object fromTileEntity, Object toTileEntity, ForgeDirection fromSide,
        int putSlot, List<ItemStack> aFilter, boolean aInvertFilter, byte aMaxTargetStackSize, byte aMinTargetStackSize,
        byte aMaxMoveAtOnce, byte aMinMoveAtOnce) {
        if (!(fromTileEntity instanceof IInventory fromInv) || aMaxTargetStackSize <= 0
            || aMinTargetStackSize <= 0
            || aMaxMoveAtOnce <= 0
            || aMinTargetStackSize > aMaxTargetStackSize
            || aMinMoveAtOnce > aMaxMoveAtOnce) return 0;

        int[] tGrabSlots = null;
        if (fromTileEntity instanceof ISidedInventory sided)
            tGrabSlots = sided.getAccessibleSlotsFromSide(fromSide.ordinal());
        if (tGrabSlots == null) {
            tGrabSlots = new int[fromInv.getSizeInventory()];
            for (int i = 0; i < tGrabSlots.length; i++) tGrabSlots[i] = i;
        }

        if (toTileEntity instanceof IInventory toInv) {
            for (final int tGrabSlot : tGrabSlots) {
                if (listContainsItem(aFilter, fromInv.getStackInSlot(tGrabSlot), true, aInvertFilter)) {
                    if (isAllowedToTakeFromSlot(fromInv, tGrabSlot, fromSide, fromInv.getStackInSlot(tGrabSlot))) {
                        if (isAllowedToPutIntoSlot(
                            toInv,
                            putSlot,
                            ForgeDirection.UNKNOWN,
                            fromInv.getStackInSlot(tGrabSlot),
                            aMaxTargetStackSize)) {
                            byte tMovedItemCount = moveStackFromSlotAToSlotB(
                                fromInv,
                                toInv,
                                tGrabSlot,
                                putSlot,
                                aMaxTargetStackSize,
                                aMinTargetStackSize,
                                aMaxMoveAtOnce,
                                aMinMoveAtOnce);
                            if (tMovedItemCount > 0) return tMovedItemCount;
                        }
                    }
                }
            }
        }

        final ForgeDirection toSide = fromSide.getOpposite();
        moveStackIntoPipe(
            fromInv,
            toTileEntity,
            tGrabSlots,
            fromSide,
            ForgeDirection.UNKNOWN,
            aFilter,
            aInvertFilter,
            aMaxTargetStackSize,
            aMinTargetStackSize,
            aMaxMoveAtOnce,
            aMinMoveAtOnce);
        return 0;
    }

    /**
     * Moves Stack from Inv-Slot to Inv-Slot.
     *
     * @return the Amount of moved Items
     */
    public static byte moveFromSlotToSlot(IInventory fromInv, IInventory toInv, int aGrabFrom, int aPutTo,
        List<ItemStack> aFilter, boolean aInvertFilter, byte aMaxTargetStackSize, byte aMinTargetStackSize,
        byte aMaxMoveAtOnce, byte aMinMoveAtOnce) {
        if (fromInv == null || toInv == null
            || aGrabFrom < 0
            || aPutTo < 0
            || aMinTargetStackSize <= 0
            || aMaxMoveAtOnce <= 0
            || aMinTargetStackSize > aMaxTargetStackSize
            || aMinMoveAtOnce > aMaxMoveAtOnce) return 0;
        if (listContainsItem(aFilter, fromInv.getStackInSlot(aGrabFrom), true, aInvertFilter)) {
            if (isAllowedToTakeFromSlot(
                fromInv,
                aGrabFrom,
                ForgeDirection.UNKNOWN,
                fromInv.getStackInSlot(aGrabFrom))) {
                if (isAllowedToPutIntoSlot(
                    toInv,
                    aPutTo,
                    ForgeDirection.UNKNOWN,
                    fromInv.getStackInSlot(aGrabFrom),
                    aMaxTargetStackSize)) {
                    byte tMovedItemCount = moveStackFromSlotAToSlotB(
                        fromInv,
                        toInv,
                        aGrabFrom,
                        aPutTo,
                        aMaxTargetStackSize,
                        aMinTargetStackSize,
                        aMaxMoveAtOnce,
                        aMinMoveAtOnce);
                    if (tMovedItemCount > 0) return tMovedItemCount;
                }
            }
        }
        return 0;
    }

    /**
     * Moves Stack from Inv-Side to Inv-Slot.
     *
     * @return the Amount of moved Items
     */
    public static byte moveFromSlotToSide(IInventory fromTile, Object toTile, int fromSlot, ForgeDirection putSide,
        List<ItemStack> aFilter, boolean aInvertFilter, byte aMaxTargetStackSize, byte aMinTargetStackSize,
        byte aMaxMoveAtOnce, byte aMinMoveAtOnce, boolean aDoCheckChests) {
        if (fromTile == null || fromSlot < 0
            || aMinTargetStackSize <= 0
            || aMaxMoveAtOnce <= 0
            || aMinTargetStackSize > aMaxTargetStackSize
            || aMinMoveAtOnce > aMaxMoveAtOnce) return 0;

        if (!listContainsItem(aFilter, fromTile.getStackInSlot(fromSlot), true, aInvertFilter)
            || !isAllowedToTakeFromSlot(fromTile, fromSlot, ForgeDirection.UNKNOWN, fromTile.getStackInSlot(fromSlot)))
            return 0;

        if (toTile instanceof IInventory) {
            int[] tPutSlots = null;
            if (toTile instanceof ISidedInventory sided)
                tPutSlots = sided.getAccessibleSlotsFromSide(putSide.ordinal());

            if (tPutSlots == null) {
                tPutSlots = new int[((IInventory) toTile).getSizeInventory()];
                for (int i = 0; i < tPutSlots.length; i++) tPutSlots[i] = i;
            }

            byte tMovedItemCount = 0;
            for (final int tPutSlot : tPutSlots) {
                if (isAllowedToPutIntoSlot(
                    (IInventory) toTile,
                    tPutSlot,
                    putSide,
                    fromTile.getStackInSlot(fromSlot),
                    aMaxTargetStackSize)) {
                    tMovedItemCount += moveStackFromSlotAToSlotB(
                        fromTile,
                        (IInventory) toTile,
                        fromSlot,
                        tPutSlot,
                        aMaxTargetStackSize,
                        aMinTargetStackSize,
                        (byte) (aMaxMoveAtOnce - tMovedItemCount),
                        aMinMoveAtOnce);
                    if (tMovedItemCount >= aMaxMoveAtOnce) {
                        return tMovedItemCount;
                    }
                }
            }
            if (tMovedItemCount > 0) return tMovedItemCount;

            if (aDoCheckChests && toTile instanceof TileEntityChest tTileEntity2) {
                if (tTileEntity2.adjacentChestChecked) {
                    if (tTileEntity2.adjacentChestXNeg != null) {
                        tMovedItemCount = moveFromSlotToSide(
                            fromTile,
                            tTileEntity2.adjacentChestXNeg,
                            fromSlot,
                            putSide,
                            aFilter,
                            aInvertFilter,
                            aMaxTargetStackSize,
                            aMinTargetStackSize,
                            aMaxMoveAtOnce,
                            aMinMoveAtOnce,
                            false);
                    } else if (tTileEntity2.adjacentChestZNeg != null) {
                        tMovedItemCount = moveFromSlotToSide(
                            fromTile,
                            tTileEntity2.adjacentChestZNeg,
                            fromSlot,
                            putSide,
                            aFilter,
                            aInvertFilter,
                            aMaxTargetStackSize,
                            aMinTargetStackSize,
                            aMaxMoveAtOnce,
                            aMinMoveAtOnce,
                            false);
                    } else if (tTileEntity2.adjacentChestXPos != null) {
                        tMovedItemCount = moveFromSlotToSide(
                            fromTile,
                            tTileEntity2.adjacentChestXPos,
                            fromSlot,
                            putSide,
                            aFilter,
                            aInvertFilter,
                            aMaxTargetStackSize,
                            aMinTargetStackSize,
                            aMaxMoveAtOnce,
                            aMinMoveAtOnce,
                            false);
                    } else if (tTileEntity2.adjacentChestZPos != null) {
                        tMovedItemCount = moveFromSlotToSide(
                            fromTile,
                            tTileEntity2.adjacentChestZPos,
                            fromSlot,
                            putSide,
                            aFilter,
                            aInvertFilter,
                            aMaxTargetStackSize,
                            aMinTargetStackSize,
                            aMaxMoveAtOnce,
                            aMinMoveAtOnce,
                            false);
                    }
                    if (tMovedItemCount > 0) return tMovedItemCount;
                }
            }
        }
        return moveStackIntoPipe(
            fromTile,
            toTile,
            new int[] { fromSlot },
            ForgeDirection.UNKNOWN,
            putSide,
            aFilter,
            aInvertFilter,
            aMaxTargetStackSize,
            aMinTargetStackSize,
            aMaxMoveAtOnce,
            aMinMoveAtOnce,
            aDoCheckChests);
    }

    public static byte moveFromSlotToSide(IInventory fromTile, Object toTile, int fromSlot, ForgeDirection putSide,
        List<ItemStack> aFilter, boolean aInvertFilter, byte aMaxTargetStackSize, byte aMinTargetStackSize,
        byte aMaxMoveAtOnce, byte aMinMoveAtOnce) {
        return moveFromSlotToSide(
            fromTile,
            toTile,
            fromSlot,
            putSide,
            aFilter,
            aInvertFilter,
            aMaxTargetStackSize,
            aMinTargetStackSize,
            aMaxMoveAtOnce,
            aMinMoveAtOnce,
            true);
    }

    /**
     * Move up to maxAmount amount of fluid from source to dest, with optional filtering via allowMove. note that this
     * filter cannot bypass filtering done by IFluidHandlers themselves.
     *
     * this overload will assume the fill side is the opposite of drainSide
     *
     * @param source    tank to drain from. method become noop if this is null
     * @param dest      tank to fill to. method become noop if this is null
     * @param drainSide side used during draining operation
     * @param maxAmount max amount of fluid to transfer. method become noop if this is not a positive integer
     * @param allowMove filter. can be null to signal all fluids are accepted
     */
    public static void moveFluid(IFluidHandler source, IFluidHandler dest, ForgeDirection drainSide, int maxAmount,
        @Nullable Predicate<FluidStack> allowMove) {
        moveFluid(source, dest, drainSide, drainSide.getOpposite(), maxAmount, allowMove);
    }

    /**
     * Move up to maxAmount amount of fluid from source to dest, with optional filtering via allowMove. note that this
     * filter cannot bypass filtering done by IFluidHandlers themselves.
     *
     * @param source    tank to drain from. method become noop if this is null
     * @param dest      tank to fill to. method become noop if this is null
     * @param drainSide side used during draining operation
     * @param fillSide  side used during filling operation
     * @param maxAmount max amount of fluid to transfer. method become noop if this is not a positive integer
     * @param allowMove filter. can be null to signal all fluids are accepted
     */
    public static void moveFluid(IFluidHandler source, IFluidHandler dest, ForgeDirection drainSide,
        ForgeDirection fillSide, int maxAmount, @Nullable Predicate<FluidStack> allowMove) {
        if (source == null || dest == null || maxAmount <= 0) return;
        FluidStack liquid = source.drain(drainSide, maxAmount, false);
        if (liquid == null) return;
        liquid = liquid.copy();
        liquid.amount = dest.fill(fillSide, liquid, false);
        if (liquid.amount > 0 && (allowMove == null || allowMove.test(liquid))) {
            dest.fill(fillSide, source.drain(drainSide, liquid.amount, true), true);
        }
    }

    public static boolean listContainsItem(Collection<ItemStack> aList, ItemStack aStack, boolean aTIfListEmpty,
        boolean aInvertFilter) {
        if (aStack == null || aStack.stackSize < 1) return false;
        if (aList == null) return aTIfListEmpty;
        boolean tEmpty = true;
        for (ItemStack tStack : aList) {
            if (tStack != null) {
                tEmpty = false;
                if (areStacksEqual(aStack, tStack)) {
                    return !aInvertFilter;
                }
            }
        }
        return tEmpty ? aTIfListEmpty : aInvertFilter;
    }

    public static boolean areStacksOrToolsEqual(ItemStack aStack1, ItemStack aStack2) {
        if (aStack1 != null && aStack2 != null && aStack1.getItem() == aStack2.getItem()) {
            if (aStack1.getItem()
                .isDamageable()) return true;
            return ((aStack1.getTagCompound() == null) == (aStack2.getTagCompound() == null))
                && (aStack1.getTagCompound() == null || aStack1.getTagCompound()
                    .equals(aStack2.getTagCompound()))
                && (Items.feather.getDamage(aStack1) == Items.feather.getDamage(aStack2)
                    || Items.feather.getDamage(aStack1) == W
                    || Items.feather.getDamage(aStack2) == W);
        }
        return false;
    }

    public static boolean areFluidsEqual(FluidStack aFluid1, FluidStack aFluid2) {
        return areFluidsEqual(aFluid1, aFluid2, false);
    }

    public static boolean areFluidsEqual(FluidStack aFluid1, FluidStack aFluid2, boolean aIgnoreNBT) {
        return aFluid1 != null && aFluid2 != null
            && aFluid1.getFluid() == aFluid2.getFluid()
            && (aIgnoreNBT || ((aFluid1.tag == null) == (aFluid2.tag == null))
                && (aFluid1.tag == null || aFluid1.tag.equals(aFluid2.tag)));
    }

    public static boolean areStacksEqual(ItemStack aStack1, ItemStack aStack2) {
        return areStacksEqual(aStack1, aStack2, false);
    }

    public static boolean areStacksEqual(ItemStack aStack1, ItemStack aStack2, boolean aIgnoreNBT) {
        return aStack1 != null && aStack2 != null
            && aStack1.getItem() == aStack2.getItem()
            && (aIgnoreNBT || (((aStack1.getTagCompound() == null) == (aStack2.getTagCompound() == null))
                && (aStack1.getTagCompound() == null || aStack1.getTagCompound()
                    .equals(aStack2.getTagCompound()))))
            && (Items.feather.getDamage(aStack1) == Items.feather.getDamage(aStack2)
                || Items.feather.getDamage(aStack1) == W
                || Items.feather.getDamage(aStack2) == W);
    }

    public static boolean areStacksEqualOrNull(ItemStack stack1, ItemStack stack2) {
        return (stack1 == null && stack2 == null) || GT_Utility.areStacksEqual(stack1, stack2);
    }

    /**
     * Treat both null list, or both null item stack at same list position as equal.
     * <p>
     * Since ItemStack doesn't override equals and hashCode, you cannot just use Objects.equals
     */
    public static boolean areStackListsEqual(List<ItemStack> lhs, List<ItemStack> rhs, boolean ignoreStackSize,
        boolean ignoreNBT) {
        if (lhs == null) return rhs == null;
        if (rhs == null) return false;
        if (lhs.size() != rhs.size()) return false;
        for (Iterator<ItemStack> it1 = lhs.iterator(), it2 = rhs.iterator(); it1.hasNext() && it2.hasNext();) {
            if (!areStacksEqualExtended(it1.next(), it2.next(), ignoreStackSize, ignoreNBT)) return false;
        }
        return true;
    }

    private static boolean areStacksEqualExtended(ItemStack lhs, ItemStack rhs, boolean ignoreStackSize,
        boolean ignoreNBT) {
        if (lhs == null) return rhs == null;
        if (rhs == null) return false;
        return lhs.getItem() == rhs.getItem()
            && (ignoreNBT || Objects.equals(lhs.stackTagCompound, rhs.stackTagCompound))
            && (ignoreStackSize || lhs.stackSize == rhs.stackSize);
    }

    public static boolean areUnificationsEqual(ItemStack aStack1, ItemStack aStack2) {
        return areUnificationsEqual(aStack1, aStack2, false);
    }

    public static boolean areUnificationsEqual(ItemStack aStack1, ItemStack aStack2, boolean aIgnoreNBT) {
        return areStacksEqual(
            GT_OreDictUnificator.get_nocopy(aStack1),
            GT_OreDictUnificator.get_nocopy(aStack2),
            aIgnoreNBT);
    }

    public static String getFluidName(Fluid aFluid, boolean aLocalized) {
        if (aFluid == null) return E;
        String rName = aLocalized ? aFluid.getLocalizedName(new FluidStack(aFluid, 0)) : aFluid.getUnlocalizedName();
        if (rName.contains("fluid.") || rName.contains("tile.")) return capitalizeString(
            rName.replaceAll("fluid.", E)
                .replaceAll("tile.", E));
        return rName;
    }

    public static String getFluidName(FluidStack aFluid, boolean aLocalized) {
        if (aFluid == null) return E;
        return getFluidName(aFluid.getFluid(), aLocalized);
    }

    public static void reInit() {
        sFilledContainerToData.clear();
        sEmptyContainerToFluidToData.clear();
        sFluidToContainers.clear();
        for (FluidContainerData tData : sFluidContainerList) {
            String fluidName = tData.fluid.getFluid()
                .getName();
            sFilledContainerToData.put(new GT_ItemStack(tData.filledContainer), tData);
            Map<String, FluidContainerData> tFluidToContainer = sEmptyContainerToFluidToData
                .get(new GT_ItemStack(tData.emptyContainer));
            List<ItemStack> tContainers = sFluidToContainers.get(fluidName);
            if (tFluidToContainer == null) {
                sEmptyContainerToFluidToData
                    .put(new GT_ItemStack(tData.emptyContainer), tFluidToContainer = new /* Concurrent */ HashMap<>());
            }
            tFluidToContainer.put(fluidName, tData);
            if (tContainers == null) {
                tContainers = new ArrayList<>();
                tContainers.add(tData.filledContainer);
                sFluidToContainers.put(fluidName, tContainers);
            } else tContainers.add(tData.filledContainer);
        }
    }

    public static void addFluidContainerData(FluidContainerData aData) {
        String fluidName = aData.fluid.getFluid()
            .getName();
        sFluidContainerList.add(aData);
        sFilledContainerToData.put(new GT_ItemStack(aData.filledContainer), aData);
        Map<String, FluidContainerData> tFluidToContainer = sEmptyContainerToFluidToData
            .get(new GT_ItemStack(aData.emptyContainer));
        List<ItemStack> tContainers = sFluidToContainers.get(fluidName);
        if (tFluidToContainer == null) {
            sEmptyContainerToFluidToData
                .put(new GT_ItemStack(aData.emptyContainer), tFluidToContainer = new /* Concurrent */ HashMap<>());
        }
        tFluidToContainer.put(fluidName, aData);
        if (tContainers == null) {
            tContainers = new ArrayList<>();
            tContainers.add(aData.filledContainer);
            sFluidToContainers.put(fluidName, tContainers);
        } else tContainers.add(aData.filledContainer);
    }

    public static List<ItemStack> getContainersFromFluid(FluidStack tFluidStack) {
        if (tFluidStack != null) {
            List<ItemStack> tContainers = sFluidToContainers.get(
                tFluidStack.getFluid()
                    .getName());
            if (tContainers == null) return new ArrayList<>();
            return tContainers;
        }
        return new ArrayList<>();
    }

    public static ItemStack fillFluidContainer(FluidStack aFluid, ItemStack aStack, boolean aRemoveFluidDirectly,
        boolean aCheckIFluidContainerItems) {
        if (isStackInvalid(aStack) || aFluid == null) return null;
        if (GT_ModHandler.isWater(aFluid) && ItemList.Bottle_Empty.isStackEqual(aStack)) {
            if (aFluid.amount >= 1000) {
                return new ItemStack(Items.potionitem, 1, 0);
            }
            return null;
        }
        if (aCheckIFluidContainerItems && aStack.getItem() instanceof IFluidContainerItem
            && ((IFluidContainerItem) aStack.getItem()).getFluid(aStack) == null
            && ((IFluidContainerItem) aStack.getItem()).getCapacity(aStack) <= aFluid.amount) {
            if (aRemoveFluidDirectly) aFluid.amount -= ((IFluidContainerItem) aStack.getItem())
                .fill(aStack = copyAmount(1, aStack), aFluid, true);
            else((IFluidContainerItem) aStack.getItem()).fill(aStack = copyAmount(1, aStack), aFluid, true);
            return aStack;
        }
        Map<String, FluidContainerData> tFluidToContainer = sEmptyContainerToFluidToData.get(new GT_ItemStack(aStack));
        if (tFluidToContainer == null) return null;
        FluidContainerData tData = tFluidToContainer.get(
            aFluid.getFluid()
                .getName());
        if (tData == null || tData.fluid.amount > aFluid.amount) return null;
        if (aRemoveFluidDirectly) aFluid.amount -= tData.fluid.amount;
        return copyAmount(1, tData.filledContainer);
    }

    public static int calculateRecipeEU(Materials aMaterial, int defaultRecipeEUPerTick) {
        return aMaterial.getProcessingMaterialTierEU() == 0 ? defaultRecipeEUPerTick
            : aMaterial.getProcessingMaterialTierEU();
    }

    public static ItemStack getFluidDisplayStack(Fluid aFluid) {
        return aFluid == null ? null : getFluidDisplayStack(new FluidStack(aFluid, 0), false);
    }

    public static ItemStack getFluidDisplayStack(FluidStack aFluid, boolean aUseStackSize) {
        return getFluidDisplayStack(aFluid, aUseStackSize, false);
    }

    public static ItemStack getFluidDisplayStack(FluidStack aFluid, boolean aUseStackSize, boolean aHideStackSize) {
        if (aFluid == null || aFluid.getFluid() == null) return null;
        int tmp = 0;
        try {
            tmp = aFluid.getFluid()
                .getID();
        } catch (Exception e) {
            System.err.println(e);
        }
        ItemStack rStack = ItemList.Display_Fluid.getWithDamage(1, tmp);
        NBTTagCompound tNBT = new NBTTagCompound();
        tNBT.setLong("mFluidDisplayAmount", aUseStackSize ? aFluid.amount : 0);
        tNBT.setLong(
            "mFluidDisplayHeat",
            aFluid.getFluid()
                .getTemperature(aFluid));
        tNBT.setBoolean(
            "mFluidState",
            aFluid.getFluid()
                .isGaseous(aFluid));
        tNBT.setBoolean("mHideStackSize", aHideStackSize);
        try {
            tNBT.setString("mFluidMaterialName", FLUID_MAP.get(aFluid.getFluid()).mName);
        } catch (Exception ignored) {}
        rStack.setTagCompound(tNBT);
        return rStack;
    }

    public static FluidStack getFluidFromDisplayStack(ItemStack aDisplayStack) {
        if (!isStackValid(aDisplayStack) || aDisplayStack.getItem() != ItemList.Display_Fluid.getItem()
            || !aDisplayStack.hasTagCompound()) return null;
        Fluid tFluid = FluidRegistry.getFluid(
            ItemList.Display_Fluid.getItem()
                .getDamage(aDisplayStack));
        return new FluidStack(
            tFluid,
            (int) aDisplayStack.getTagCompound()
                .getLong("mFluidDisplayAmount"));
    }

    public static boolean containsFluid(ItemStack aStack, FluidStack aFluid, boolean aCheckIFluidContainerItems) {
        if (isStackInvalid(aStack) || aFluid == null) return false;
        if (aCheckIFluidContainerItems && aStack.getItem() instanceof IFluidContainerItem
            && ((IFluidContainerItem) aStack.getItem()).getCapacity(aStack) > 0)
            return aFluid
                .isFluidEqual(((IFluidContainerItem) aStack.getItem()).getFluid(aStack = copyAmount(1, aStack)));
        FluidContainerData tData = sFilledContainerToData.get(new GT_ItemStack(aStack));
        return tData != null && tData.fluid.isFluidEqual(aFluid);
    }

    public static FluidStack getFluidForFilledItem(ItemStack aStack, boolean aCheckIFluidContainerItems) {
        if (isStackInvalid(aStack)) return null;
        if (aCheckIFluidContainerItems && aStack.getItem() instanceof IFluidContainerItem
            && ((IFluidContainerItem) aStack.getItem()).getCapacity(aStack) > 0)
            return ((IFluidContainerItem) aStack.getItem()).drain(copyAmount(1, aStack), Integer.MAX_VALUE, true);
        FluidContainerData tData = sFilledContainerToData.get(new GT_ItemStack(aStack));
        return tData == null ? null : tData.fluid.copy();
    }

    /**
     * Get empty fluid container from filled one.
     */
    public static ItemStack getContainerForFilledItem(ItemStack aStack, boolean aCheckIFluidContainerItems) {
        if (isStackInvalid(aStack)) return null;
        FluidContainerData tData = sFilledContainerToData.get(new GT_ItemStack(aStack));
        if (tData != null) return copyAmount(1, tData.emptyContainer);
        if (aCheckIFluidContainerItems && aStack.getItem() instanceof IFluidContainerItem
            && ((IFluidContainerItem) aStack.getItem()).getCapacity(aStack) > 0) {
            ((IFluidContainerItem) aStack.getItem()).drain(aStack = copyAmount(1, aStack), Integer.MAX_VALUE, true);
            return aStack;
        }
        return null;
    }

    /**
     * This is NOT meant for fluid manipulation! It's for getting item container, which is generally used for
     * crafting recipes. While it also works for many of the fluid containers, some don't.
     * <p>
     * Use {@link #getContainerForFilledItem} for getting empty fluid container.
     */
    public static ItemStack getContainerItem(ItemStack aStack, boolean aCheckIFluidContainerItems) {
        if (isStackInvalid(aStack)) return null;
        if (aStack.getItem()
            .hasContainerItem(aStack))
            return aStack.getItem()
                .getContainerItem(aStack);
        /*
         * These are all special Cases, in which it is intended to have only GT Blocks outputting those Container Items
         */
        if (ItemList.Cell_Empty.isStackEqual(aStack, false, true)) return null;
        if (aStack.getItem() == Items.potionitem || aStack.getItem() == Items.experience_bottle
            || ItemList.TF_Vial_FieryBlood.isStackEqual(aStack)
            || ItemList.TF_Vial_FieryTears.isStackEqual(aStack)) return ItemList.Bottle_Empty.get(1);

        if (aCheckIFluidContainerItems && aStack.getItem() instanceof IFluidContainerItem
            && ((IFluidContainerItem) aStack.getItem()).getCapacity(aStack) > 0) {
            ItemStack tStack = copyAmount(1, aStack);
            ((IFluidContainerItem) aStack.getItem()).drain(tStack, Integer.MAX_VALUE, true);
            if (!areStacksEqual(aStack, tStack)) return tStack;
            return null;
        }

        int tCapsuleCount = GT_ModHandler.getCapsuleCellContainerCount(aStack);
        if (tCapsuleCount > 0) return ItemList.Cell_Empty.get(tCapsuleCount);

        if (ItemList.IC2_ForgeHammer.isStackEqual(aStack) || ItemList.IC2_WireCutter.isStackEqual(aStack))
            return copyMetaData(Items.feather.getDamage(aStack) + 1, aStack);
        return null;
    }

    public static FluidStack getFluidFromContainerOrFluidDisplay(ItemStack stack) {
        FluidStack fluidStack = GT_Utility.getFluidForFilledItem(stack, true);
        if (fluidStack == null) {
            fluidStack = GT_Utility.getFluidFromDisplayStack(stack);
        }
        return fluidStack;
    }

    public static synchronized boolean removeIC2BottleRecipe(ItemStack aContainer, ItemStack aInput,
        Map<ic2.api.recipe.ICannerBottleRecipeManager.Input, RecipeOutput> aRecipeList, ItemStack aOutput) {
        if ((isStackInvalid(aInput) && isStackInvalid(aOutput) && isStackInvalid(aContainer)) || aRecipeList == null)
            return false;
        boolean rReturn = false;
        Iterator<Map.Entry<ic2.api.recipe.ICannerBottleRecipeManager.Input, RecipeOutput>> tIterator = aRecipeList
            .entrySet()
            .iterator();
        aOutput = GT_OreDictUnificator.get(aOutput);
        while (tIterator.hasNext()) {
            Map.Entry<ic2.api.recipe.ICannerBottleRecipeManager.Input, RecipeOutput> tEntry = tIterator.next();
            if (aInput == null || tEntry.getKey()
                .matches(aContainer, aInput)) {
                List<ItemStack> tList = tEntry.getValue().items;
                if (tList != null) for (ItemStack tOutput : tList)
                    if (aOutput == null || areStacksEqual(GT_OreDictUnificator.get(tOutput), aOutput)) {
                        tIterator.remove();
                        rReturn = true;
                        break;
                    }
            }
        }
        return rReturn;
    }

    public static synchronized boolean removeSimpleIC2MachineRecipe(ItemStack aInput,
        Map<IRecipeInput, RecipeOutput> aRecipeList, ItemStack aOutput) {
        if ((isStackInvalid(aInput) && isStackInvalid(aOutput)) || aRecipeList == null) return false;
        boolean rReturn = false;
        Iterator<Map.Entry<IRecipeInput, RecipeOutput>> tIterator = aRecipeList.entrySet()
            .iterator();
        aOutput = GT_OreDictUnificator.get(aOutput);
        while (tIterator.hasNext()) {
            Map.Entry<IRecipeInput, RecipeOutput> tEntry = tIterator.next();
            if (aInput == null || tEntry.getKey()
                .matches(aInput)) {
                List<ItemStack> tList = tEntry.getValue().items;
                if (tList != null) for (ItemStack tOutput : tList)
                    if (aOutput == null || areStacksEqual(GT_OreDictUnificator.get(tOutput), aOutput)) {
                        tIterator.remove();
                        rReturn = true;
                        break;
                    }
            }
        }
        return rReturn;
    }

    public static synchronized void bulkRemoveSimpleIC2MachineRecipe(Map<ItemStack, ItemStack> toRemove,
        Map<IRecipeInput, RecipeOutput> aRecipeList) {
        if (aRecipeList == null || aRecipeList.isEmpty()) return;
        toRemove.entrySet()
            .removeIf(aEntry -> (isStackInvalid(aEntry.getKey()) && isStackInvalid(aEntry.getValue())));
        final Map<ItemStack, ItemStack> finalToRemove = Maps
            .transformValues(toRemove, GT_OreDictUnificator::get_nocopy);

        aRecipeList.entrySet()
            .removeIf(
                tEntry -> finalToRemove.entrySet()
                    .stream()
                    .anyMatch(aEntry -> {
                        final ItemStack aInput = aEntry.getKey(), aOutput = aEntry.getValue();
                        final List<ItemStack> tList = tEntry.getValue().items;

                        if (tList == null) return false;
                        if (aInput != null && !tEntry.getKey()
                            .matches(aInput)) return false;

                        return tList.stream()
                            .anyMatch(
                                tOutput -> (aOutput == null
                                    || areStacksEqual(GT_OreDictUnificator.get(tOutput), aOutput)));
                    }));
    }

    public static boolean addSimpleIC2MachineRecipe(ItemStack aInput, Map<IRecipeInput, RecipeOutput> aRecipeList,
        NBTTagCompound aNBT, Object... aOutput) {
        if (isStackInvalid(aInput) || aOutput.length == 0 || aRecipeList == null) return false;
        ItemData tOreName = GT_OreDictUnificator.getAssociation(aInput);
        for (Object o : aOutput) {
            if (o == null) {
                GT_FML_LOGGER.info("EmptyIC2Output!" + aInput.getUnlocalizedName());
                return false;
            }
        }
        ItemStack[] tStack = GT_OreDictUnificator.getStackArray(true, aOutput);
        if (tStack.length > 0 && areStacksEqual(aInput, tStack[0])) return false;
        if (tOreName != null) {
            if (tOreName.toString()
                .equals("dustAsh")
                && tStack[0].getUnlocalizedName()
                    .equals("tile.volcanicAsh"))
                return false;
            aRecipeList
                .put(new RecipeInputOreDict(tOreName.toString(), aInput.stackSize), new RecipeOutput(aNBT, tStack));
        } else {
            aRecipeList
                .put(new RecipeInputItemStack(copyOrNull(aInput), aInput.stackSize), new RecipeOutput(aNBT, tStack));
        }
        return true;
    }

    public static ItemStack getWrittenBook(String aMapping, ItemStack aStackToPutNBT) {
        if (isStringInvalid(aMapping)) return null;
        ItemStack rStack = GregTech_API.sBookList.get(aMapping);
        if (rStack == null) return aStackToPutNBT;
        if (aStackToPutNBT != null) {
            aStackToPutNBT.setTagCompound(rStack.getTagCompound());
            return aStackToPutNBT;
        }
        return copyAmount(1, rStack);
    }

    public static ItemStack getWrittenBook(String aMapping, String aTitle, String aAuthor, String... aPages) {
        if (isStringInvalid(aMapping)) return null;
        ItemStack rStack = GregTech_API.sBookList.get(aMapping);
        if (rStack != null) return copyAmount(1, rStack);
        if (isStringInvalid(aTitle) || isStringInvalid(aAuthor) || aPages.length == 0) return null;
        sBookCount++;
        rStack = new ItemStack(Items.written_book, 1);
        NBTTagCompound tNBT = new NBTTagCompound();
        tNBT.setString("title", GT_LanguageManager.addStringLocalization("Book." + aTitle + ".Name", aTitle));
        tNBT.setString("author", aAuthor);
        NBTTagList tNBTList = new NBTTagList();
        for (byte i = 0; i < aPages.length; i++) {
            aPages[i] = GT_LanguageManager
                .addStringLocalization("Book." + aTitle + ".Page" + ((i < 10) ? "0" + i : i), aPages[i]);
            if (i < 48) {
                if (aPages[i].length() < 256) tNBTList.appendTag(new NBTTagString(aPages[i]));
                else GT_Log.err.println("WARNING: String for written Book too long! -> " + aPages[i]);
            } else {
                GT_Log.err.println("WARNING: Too much Pages for written Book! -> " + aTitle);
                break;
            }
        }
        tNBTList.appendTag(
            new NBTTagString(
                "Credits to " + aAuthor
                    + " for writing this Book. This was Book Nr. "
                    + sBookCount
                    + " at its creation. Gotta get 'em all!"));
        tNBT.setTag("pages", tNBTList);
        rStack.setTagCompound(tNBT);
        GT_Log.out.println(
            "GT_Mod: Added Book to Book List  -  Mapping: '" + aMapping
                + "'  -  Name: '"
                + aTitle
                + "'  -  Author: '"
                + aAuthor
                + "'");
        GregTech_API.sBookList.put(aMapping, rStack);
        return copyOrNull(rStack);
    }

    public static boolean doSoundAtClient(String aSoundName, int aTimeUntilNextSound, float aSoundStrength) {
        if (aSoundName == null) return false;
        return doSoundAtClient(aSoundName, aTimeUntilNextSound, aSoundStrength, GT.getThePlayer());
    }

    public static boolean doSoundAtClient(SoundResource sound, int aTimeUntilNextSound, float aSoundStrength) {
        return doSoundAtClient(sound.resourceLocation, aTimeUntilNextSound, aSoundStrength, GT.getThePlayer());
    }

    public static boolean doSoundAtClient(ResourceLocation aSoundResourceLocation, int aTimeUntilNextSound,
        float aSoundStrength) {
        return doSoundAtClient(aSoundResourceLocation, aTimeUntilNextSound, aSoundStrength, GT.getThePlayer());
    }

    public static boolean doSoundAtClient(String aSoundName, int aTimeUntilNextSound, float aSoundStrength,
        Entity aEntity) {
        if (aEntity == null || aSoundName == null) return false;
        return doSoundAtClient(
            aSoundName,
            aTimeUntilNextSound,
            aSoundStrength,
            aEntity.posX,
            aEntity.posY,
            aEntity.posZ);
    }

    public static boolean doSoundAtClient(ResourceLocation aSoundResourceLocation, int aTimeUntilNextSound,
        float aSoundStrength, Entity aEntity) {
        if (aEntity == null) return false;
        return doSoundAtClient(
            aSoundResourceLocation.toString(),
            aTimeUntilNextSound,
            aSoundStrength,
            aEntity.posX,
            aEntity.posY,
            aEntity.posZ);
    }

    public static boolean doSoundAtClient(ResourceLocation aSoundResourceLocation, int aTimeUntilNextSound,
        float aSoundStrength, double aX, double aY, double aZ) {
        return doSoundAtClient(aSoundResourceLocation, aTimeUntilNextSound, aSoundStrength, 1.01818028F, aX, aY, aZ);
    }

    /**
     * @inheritDoc
     * @deprecated Use {@link #doSoundAtClient(ResourceLocation, int, float, double, double, double)}
     */
    @Deprecated
    public static boolean doSoundAtClient(String aSoundName, int aTimeUntilNextSound, float aSoundStrength, double aX,
        double aY, double aZ) {
        if (aSoundName == null) return false;
        return doSoundAtClient(
            new ResourceLocation(aSoundName),
            aTimeUntilNextSound,
            aSoundStrength,
            1.01818028F,
            aX,
            aY,
            aZ);
    }

    public static boolean doSoundAtClient(SoundResource aSound, int aTimeUntilNextSound, float aSoundStrength,
        double aX, double aY, double aZ) {
        return doSoundAtClient(aSound.resourceLocation, aTimeUntilNextSound, aSoundStrength, aX, aY, aZ);
    }

    public static boolean doSoundAtClient(SoundResource aSound, int aTimeUntilNextSound, float aSoundStrength,
        float aSoundModulation, double aX, double aY, double aZ) {
        return doSoundAtClient(
            aSound.resourceLocation,
            aTimeUntilNextSound,
            aSoundStrength,
            aSoundModulation,
            aX,
            aY,
            aZ);
    }

    public static boolean doSoundAtClient(ResourceLocation aSoundResourceLocation, int aTimeUntilNextSound,
        float aSoundStrength, float aSoundModulation, double aX, double aY, double aZ) {
        if (!FMLCommonHandler.instance()
            .getEffectiveSide()
            .isClient() || GT.getThePlayer() == null || !GT.getThePlayer().worldObj.isRemote) return false;
        if (GregTech_API.sMultiThreadedSounds) new Thread(
            new GT_Runnable_Sound(
                GT.getThePlayer().worldObj,
                aX,
                aY,
                aZ,
                aTimeUntilNextSound,
                aSoundResourceLocation,
                aSoundStrength,
                aSoundModulation),
            "Sound Effect").start();
        else new GT_Runnable_Sound(
            GT.getThePlayer().worldObj,
            aX,
            aY,
            aZ,
            aTimeUntilNextSound,
            aSoundResourceLocation,
            aSoundStrength,
            aSoundModulation).run();
        return true;
    }

    /**
     * @inheritDoc
     * @Deprecated Use {@link #doSoundAtClient(ResourceLocation, int, float, float, double, double, double)}
     */
    @Deprecated
    public static boolean doSoundAtClient(String aSoundName, int aTimeUntilNextSound, float aSoundStrength,
        float aSoundModulation, double aX, double aY, double aZ) {
        if (isStringInvalid(aSoundName)) return false;
        return doSoundAtClient(
            new ResourceLocation(aSoundName),
            aTimeUntilNextSound,
            aSoundStrength,
            aSoundModulation,
            aX,
            aY,
            aZ);
    }

    public static boolean sendSoundToPlayers(World aWorld, String aSoundName, float aSoundStrength,
        float aSoundModulation, int aX, int aY, int aZ) {
        if (isStringInvalid(aSoundName) || aWorld == null || aWorld.isRemote) return false;
        NW.sendPacketToAllPlayersInRange(
            aWorld,
            new GT_Packet_Sound(aSoundName, aSoundStrength, aSoundModulation, aX, (short) aY, aZ),
            aX,
            aZ);
        return true;
    }

    public static boolean sendSoundToPlayers(World aWorld, SoundResource sound, float aSoundStrength,
        float aSoundModulation, int aX, int aY, int aZ) {
        if (aWorld == null || aWorld.isRemote) return false;
        NW.sendPacketToAllPlayersInRange(
            aWorld,
            new GT_Packet_Sound(
                sound.resourceLocation.toString(),
                aSoundStrength,
                aSoundModulation,
                aX,
                (short) aY,
                aZ),
            aX,
            aZ);
        return true;
    }

    public static int stackToInt(ItemStack aStack) {
        if (isStackInvalid(aStack)) return 0;
        return itemToInt(aStack.getItem(), Items.feather.getDamage(aStack));
    }

    public static int itemToInt(Item aItem, int aMeta) {
        return Item.getIdFromItem(aItem) | (aMeta << 16);
    }

    public static int stackToWildcard(ItemStack aStack) {
        if (isStackInvalid(aStack)) return 0;
        return Item.getIdFromItem(aStack.getItem()) | (W << 16);
    }

    public static ItemStack intToStack(int aStack) {
        int tID = aStack & (~0 >>> 16), tMeta = aStack >>> 16;
        Item tItem = Item.getItemById(tID);
        if (tItem != null) return new ItemStack(tItem, 1, tMeta);
        return null;
    }

    public static Integer[] stacksToIntegerArray(ItemStack... aStacks) {
        Integer[] rArray = new Integer[aStacks.length];
        for (int i = 0; i < rArray.length; i++) {
            rArray[i] = stackToInt(aStacks[i]);
        }
        return rArray;
    }

    public static int[] stacksToIntArray(ItemStack... aStacks) {
        int[] rArray = new int[aStacks.length];
        for (int i = 0; i < rArray.length; i++) {
            rArray[i] = stackToInt(aStacks[i]);
        }
        return rArray;
    }

    public static boolean arrayContains(Object aObject, Object... aObjects) {
        return listContains(aObject, Arrays.asList(aObjects));
    }

    public static boolean listContains(Object aObject, Collection<?> aObjects) {
        if (aObjects == null) return false;
        return aObjects.contains(aObject);
    }

    @SafeVarargs
    public static <T> boolean arrayContainsNonNull(T... aArray) {
        if (aArray != null) for (Object tObject : aArray) if (tObject != null) return true;
        return false;
    }

    /**
     * Note: use {@link ArrayExt#withoutNulls(Object[], IntFunction)} if you want an array as a result.
     */
    @SafeVarargs
    public static <T> ArrayList<T> getArrayListWithoutNulls(T... aArray) {
        if (aArray == null) return new ArrayList<>();
        ArrayList<T> rList = new ArrayList<>(Arrays.asList(aArray));
        for (int i = 0; i < rList.size(); i++) if (rList.get(i) == null) rList.remove(i--);
        return rList;
    }

    /**
     * Note: use {@link ArrayExt#withoutTrailingNulls(Object[], IntFunction)} if you want an array as a result.
     */
    @SafeVarargs
    public static <T> ArrayList<T> getArrayListWithoutTrailingNulls(T... aArray) {
        if (aArray == null) return new ArrayList<>();
        ArrayList<T> rList = new ArrayList<>(Arrays.asList(aArray));
        for (int i = rList.size() - 1; i >= 0 && rList.get(i) == null;) rList.remove(i--);
        return rList;
    }

    @Deprecated // why do you use Objects?
    public static Block getBlock(Object aBlock) {
        return (Block) aBlock;
    }

    public static Block getBlockFromStack(ItemStack itemStack) {
        if (isStackInvalid(itemStack)) return Blocks.air;
        return getBlockFromItem(itemStack.getItem());
    }

    public static Block getBlockFromItem(Item item) {
        return Block.getBlockFromItem(item);
    }

    @Deprecated // why do you use Objects? And if you want to check your block to be not null, check it directly!
    public static boolean isBlockValid(Object aBlock) {
        return (aBlock instanceof Block);
    }

    @Deprecated // why do you use Objects? And if you want to check your block to be null, check it directly!
    public static boolean isBlockInvalid(Object aBlock) {
        return !(aBlock instanceof Block);
    }

    public static boolean isStringValid(Object aString) {
        return aString != null && !aString.toString()
            .isEmpty();
    }

    public static boolean isStringInvalid(Object aString) {
        return aString == null || aString.toString()
            .isEmpty();
    }

    @Deprecated
    public static boolean isStackValid(Object aStack) {
        return (aStack instanceof ItemStack stack) && isStackValid(stack);
    }

    public static boolean isStackValid(ItemStack aStack) {
        return (aStack != null) && aStack.getItem() != null && aStack.stackSize >= 0;
    }

    @Deprecated
    public static boolean isStackInvalid(Object aStack) {
        return !(aStack instanceof ItemStack stack) || isStackInvalid(stack);
    }

    public static boolean isStackInvalid(ItemStack aStack) {
        return aStack == null || aStack.getItem() == null || aStack.stackSize < 0;
    }

    public static boolean isDebugItem(ItemStack aStack) {
        return /* ItemList.Armor_Cheat.isStackEqual(aStack, T, T) || */ areStacksEqual(
            GT_ModHandler.getIC2Item("debug", 1),
            aStack,
            true);
    }

    public static ItemStack updateItemStack(ItemStack aStack) {
        if (isStackValid(aStack) && aStack.getItem() instanceof GT_Generic_Item)
            ((GT_Generic_Item) aStack.getItem()).isItemStackUsable(aStack);
        return aStack;
    }

    public static boolean isOpaqueBlock(World aWorld, int aX, int aY, int aZ) {
        return aWorld.getBlock(aX, aY, aZ)
            .isOpaqueCube();
    }

    public static boolean isBlockAir(World aWorld, int aX, int aY, int aZ) {
        return aWorld.getBlock(aX, aY, aZ)
            .isAir(aWorld, aX, aY, aZ);
    }

    public static boolean hasBlockHitBox(World aWorld, int aX, int aY, int aZ) {
        return aWorld.getBlock(aX, aY, aZ)
            .getCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ) != null;
    }

    public static void setCoordsOnFire(World aWorld, int aX, int aY, int aZ, boolean aReplaceCenter) {
        if (aReplaceCenter) if (aWorld.getBlock(aX, aY, aZ)
            .getCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ) == null) aWorld.setBlock(aX, aY, aZ, Blocks.fire);
        if (aWorld.getBlock(aX + 1, aY, aZ)
            .getCollisionBoundingBoxFromPool(aWorld, aX + 1, aY, aZ) == null)
            aWorld.setBlock(aX + 1, aY, aZ, Blocks.fire);
        if (aWorld.getBlock(aX - 1, aY, aZ)
            .getCollisionBoundingBoxFromPool(aWorld, aX - 1, aY, aZ) == null)
            aWorld.setBlock(aX - 1, aY, aZ, Blocks.fire);
        if (aWorld.getBlock(aX, aY + 1, aZ)
            .getCollisionBoundingBoxFromPool(aWorld, aX, aY + 1, aZ) == null)
            aWorld.setBlock(aX, aY + 1, aZ, Blocks.fire);
        if (aWorld.getBlock(aX, aY - 1, aZ)
            .getCollisionBoundingBoxFromPool(aWorld, aX, aY - 1, aZ) == null)
            aWorld.setBlock(aX, aY - 1, aZ, Blocks.fire);
        if (aWorld.getBlock(aX, aY, aZ + 1)
            .getCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ + 1) == null)
            aWorld.setBlock(aX, aY, aZ + 1, Blocks.fire);
        if (aWorld.getBlock(aX, aY, aZ - 1)
            .getCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ - 1) == null)
            aWorld.setBlock(aX, aY, aZ - 1, Blocks.fire);
    }

    public static ItemStack getProjectile(SubTag aProjectileType, IInventory aInventory) {
        if (aInventory != null) for (int i = 0, j = aInventory.getSizeInventory(); i < j; i++) {
            ItemStack rStack = aInventory.getStackInSlot(i);
            if (isStackValid(rStack) && rStack.getItem() instanceof IProjectileItem
                && ((IProjectileItem) rStack.getItem()).hasProjectile(aProjectileType, rStack))
                return updateItemStack(rStack);
        }
        return null;
    }

    public static void removeNullStacksFromInventory(IInventory aInventory) {
        if (aInventory != null) for (int i = 0, j = aInventory.getSizeInventory(); i < j; i++) {
            ItemStack tStack = aInventory.getStackInSlot(i);
            if (tStack != null && (tStack.stackSize == 0 || tStack.getItem() == null))
                aInventory.setInventorySlotContents(i, null);
        }
    }

    /**
     * Initializes new empty texture page for casings page 0 is old CASING_BLOCKS
     * <p>
     * Then casings should be registered like this: for (byte i = MIN_USED_META; i < MAX_USED_META; i = (byte) (i + 1))
     * { Textures.BlockIcons.casingTexturePages[PAGE][i+START_INDEX] = new GT_CopiedBlockTexture(this, 6, i); }
     *
     * @param page 0 to 127
     * @return true if it made empty page, false if one already existed...
     */
    public static boolean addTexturePage(byte page) {
        if (Textures.BlockIcons.casingTexturePages[page] == null) {
            Textures.BlockIcons.casingTexturePages[page] = new ITexture[128];
            return true;
        }
        return false;
    }

    /**
     * Return texture id from page and index, for use when determining hatches, but can also be precomputed from:
     * (page<<7)+index
     *
     * @param page  0 to 127 page
     * @param index 0 to 127 texture index
     * @return casing texture 0 to 16383
     */
    public static int getTextureId(byte page, byte index) {
        if (page >= 0 && index >= 0) {
            return (page << 7) + index;
        }
        throw new RuntimeException("Index out of range: [" + page + "][" + index + "]");
    }

    /**
     * Return texture id from page and index, for use when determining hatches, but can also be precomputed from:
     * (page<<7)+index
     *
     * @param page       0 to 127 page
     * @param startIndex 0 to 127 start texture index
     * @param blockMeta  meta of the block
     * @return casing texture 0 to 16383
     */
    public static int getTextureId(byte page, byte startIndex, byte blockMeta) {
        if (page >= 0 && startIndex >= 0 && blockMeta >= 0 && (startIndex + blockMeta) <= 127) {
            return (page << 7) + (startIndex + blockMeta);
        }
        throw new RuntimeException(
            "Index out of range: [" + page
                + "]["
                + startIndex
                + "+"
                + blockMeta
                + "="
                + (startIndex + blockMeta)
                + "]");
    }

    /**
     * Return texture id from item stack, unoptimized but readable?
     *
     * @return casing texture 0 to 16383
     */
    @Deprecated
    public static int getTextureId(ItemStack stack) {
        return getTextureId(Block.getBlockFromItem(stack.getItem()), (byte) stack.getItemDamage());
    }

    /**
     * Return texture id from item stack, unoptimized but readable?
     *
     * @return casing texture 0 to 16383
     */
    public static int getTextureId(Block blockFromBlock, byte metaFromBlock) {
        for (int page = 0; page < Textures.BlockIcons.casingTexturePages.length; page++) {
            ITexture[] casingTexturePage = Textures.BlockIcons.casingTexturePages[page];
            if (casingTexturePage != null) {
                for (int index = 0; index < casingTexturePage.length; index++) {
                    ITexture iTexture = casingTexturePage[index];
                    if (iTexture instanceof IBlockContainer) {
                        Block block = ((IBlockContainer) iTexture).getBlock();
                        byte meta = ((IBlockContainer) iTexture).getMeta();
                        if (meta == metaFromBlock && blockFromBlock == block) {
                            return (page << 7) + index;
                        }
                    }
                }
            }
        }
        throw new RuntimeException(
            "Probably missing mapping or different texture class used for: " + blockFromBlock.getUnlocalizedName()
                + " meta:"
                + metaFromBlock);
    }

    /**
     * Converts a Number to a String
     */
    public static String parseNumberToString(int aNumber) {
        boolean temp = true, negative = false;

        if (aNumber < 0) {
            aNumber *= -1;
            negative = true;
        }

        StringBuilder tStringB = new StringBuilder();
        for (int i = 1000000000; i > 0; i /= 10) {
            int tDigit = (aNumber / i) % 10;
            if (temp && tDigit != 0) temp = false;
            if (!temp) {
                tStringB.append(tDigit);
                if (i != 1) for (int j = i; j > 0; j /= 1000) if (j == 1) tStringB.append(",");
            }
        }

        String tString = tStringB.toString();

        if (tString.equals(E)) tString = "0";

        return negative ? "-" + tString : tString;
    }

    public static NBTTagCompound getNBTContainingBoolean(NBTTagCompound aNBT, Object aTag, boolean aValue) {
        if (aNBT == null) aNBT = new NBTTagCompound();
        aNBT.setBoolean(aTag.toString(), aValue);
        return aNBT;
    }

    public static NBTTagCompound getNBTContainingByte(NBTTagCompound aNBT, Object aTag, byte aValue) {
        if (aNBT == null) aNBT = new NBTTagCompound();
        aNBT.setByte(aTag.toString(), aValue);
        return aNBT;
    }

    public static NBTTagCompound getNBTContainingShort(NBTTagCompound aNBT, Object aTag, short aValue) {
        if (aNBT == null) aNBT = new NBTTagCompound();
        aNBT.setShort(aTag.toString(), aValue);
        return aNBT;
    }

    public static NBTTagCompound getNBTContainingInteger(NBTTagCompound aNBT, Object aTag, int aValue) {
        if (aNBT == null) aNBT = new NBTTagCompound();
        aNBT.setInteger(aTag.toString(), aValue);
        return aNBT;
    }

    public static NBTTagCompound getNBTContainingFloat(NBTTagCompound aNBT, Object aTag, float aValue) {
        if (aNBT == null) aNBT = new NBTTagCompound();
        aNBT.setFloat(aTag.toString(), aValue);
        return aNBT;
    }

    public static NBTTagCompound getNBTContainingDouble(NBTTagCompound aNBT, Object aTag, double aValue) {
        if (aNBT == null) aNBT = new NBTTagCompound();
        aNBT.setDouble(aTag.toString(), aValue);
        return aNBT;
    }

    public static NBTTagCompound getNBTContainingString(NBTTagCompound aNBT, Object aTag, Object aValue) {
        if (aNBT == null) aNBT = new NBTTagCompound();
        if (aValue == null) return aNBT;
        aNBT.setString(aTag.toString(), aValue.toString());
        return aNBT;
    }

    public static boolean isWearingFullFrostHazmat(EntityLivingBase aEntity) {
        for (byte i = 1; i < 5; i++) {
            ItemStack tStack = aEntity.getEquipmentInSlot(i);

            if (!isStackInList(tStack, GregTech_API.sFrostHazmatList) && !hasHazmatEnchant(tStack)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isWearingFullHeatHazmat(EntityLivingBase aEntity) {
        for (byte i = 1; i < 5; i++) {
            ItemStack tStack = aEntity.getEquipmentInSlot(i);

            if (!isStackInList(tStack, GregTech_API.sHeatHazmatList) && !hasHazmatEnchant(tStack)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isWearingFullBioHazmat(EntityLivingBase aEntity) {
        for (byte i = 1; i < 5; i++) {
            ItemStack tStack = aEntity.getEquipmentInSlot(i);

            if (!isStackInList(tStack, GregTech_API.sBioHazmatList) && !hasHazmatEnchant(tStack)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isWearingFullRadioHazmat(EntityLivingBase aEntity) {
        for (byte i = 1; i < 5; i++) {
            ItemStack tStack = aEntity.getEquipmentInSlot(i);

            if (!isStackInList(tStack, GregTech_API.sRadioHazmatList) && !hasHazmatEnchant(tStack)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isWearingFullElectroHazmat(EntityLivingBase aEntity) {
        for (byte i = 1; i < 5; i++) {
            ItemStack tStack = aEntity.getEquipmentInSlot(i);

            if (!isStackInList(tStack, GregTech_API.sElectroHazmatList) && !hasHazmatEnchant(tStack)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isWearingFullGasHazmat(EntityLivingBase aEntity) {
        for (byte i = 1; i < 5; i++) {
            ItemStack tStack = aEntity.getEquipmentInSlot(i);

            if (!isStackInList(tStack, GregTech_API.sGasHazmatList) && !hasHazmatEnchant(tStack)) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasHazmatEnchant(ItemStack aStack) {
        if (aStack == null) return false;
        Map<Integer, Integer> tEnchantments = EnchantmentHelper.getEnchantments(aStack);
        Integer tLevel = tEnchantments.get(Enchantment_Hazmat.INSTANCE.effectId);

        return tLevel != null && tLevel >= 1;
    }

    public static float getHeatDamageFromItem(ItemStack aStack) {
        ItemData tData = GT_OreDictUnificator.getItemData(aStack);
        return tData == null ? 0
            : (tData.mPrefix == null ? 0 : tData.mPrefix.mHeatDamage)
                + (tData.hasValidMaterialData() ? tData.mMaterial.mMaterial.mHeatDamage : 0);
    }

    public static int getRadioactivityLevel(ItemStack aStack) {
        ItemData tData = GT_OreDictUnificator.getItemData(aStack);
        if (tData != null && tData.hasValidMaterialData()) {
            if (tData.mMaterial.mMaterial.mEnchantmentArmors instanceof Enchantment_Radioactivity)
                return tData.mMaterial.mMaterial.mEnchantmentArmorsLevel;
            if (tData.mMaterial.mMaterial.mEnchantmentTools instanceof Enchantment_Radioactivity)
                return tData.mMaterial.mMaterial.mEnchantmentToolsLevel;
        }
        return EnchantmentHelper.getEnchantmentLevel(Enchantment_Radioactivity.INSTANCE.effectId, aStack);
    }

    public static boolean isImmuneToBreathingGasses(EntityLivingBase aEntity) {
        return isWearingFullGasHazmat(aEntity);
    }

    public static boolean applyHeatDamage(EntityLivingBase entity, float damage) {
        return applyHeatDamage(entity, damage, GT_DamageSources.getHeatDamage());
    }

    public static boolean applyHeatDamageFromItem(EntityLivingBase entity, float damage, ItemStack item) {
        return applyHeatDamage(entity, damage, new DamageSourceHotItem(item));
    }

    private static boolean applyHeatDamage(EntityLivingBase aEntity, float aDamage, DamageSource source) {
        if (aDamage > 0 && aEntity != null && !isWearingFullHeatHazmat(aEntity)) {
            return aEntity.attackEntityFrom(source, aDamage);
        }
        return false;
    }

    public static boolean applyFrostDamage(EntityLivingBase aEntity, float aDamage) {
        if (aDamage > 0 && aEntity != null && !isWearingFullFrostHazmat(aEntity)) {
            return aEntity.attackEntityFrom(GT_DamageSources.getFrostDamage(), aDamage);
        }
        return false;
    }

    public static boolean applyElectricityDamage(EntityLivingBase aEntity, long aVoltage, long aAmperage) {
        long aDamage = getTier(aVoltage) * aAmperage * 4;
        if (aDamage > 0 && aEntity != null && !isWearingFullElectroHazmat(aEntity)) {
            return aEntity.attackEntityFrom(GT_DamageSources.getElectricDamage(), aDamage);
        }
        return false;
    }

    public static boolean applyRadioactivity(EntityLivingBase aEntity, int aLevel, int aAmountOfItems) {
        if (aLevel > 0 && aEntity != null
            && aEntity.getCreatureAttribute() != EnumCreatureAttribute.UNDEAD
            && aEntity.getCreatureAttribute() != EnumCreatureAttribute.ARTHROPOD
            && !isWearingFullRadioHazmat(aEntity)) {
            PotionEffect tEffect = null;
            aEntity.addPotionEffect(
                new PotionEffect(
                    Potion.moveSlowdown.id,
                    aLevel * 140 * aAmountOfItems + Math.max(
                        0,
                        ((tEffect = aEntity.getActivePotionEffect(Potion.moveSlowdown)) == null ? 0
                            : tEffect.getDuration())),
                    Math.max(0, (5 * aLevel) / 7)));
            aEntity.addPotionEffect(
                new PotionEffect(
                    Potion.digSlowdown.id,
                    aLevel * 150 * aAmountOfItems + Math.max(
                        0,
                        ((tEffect = aEntity.getActivePotionEffect(Potion.digSlowdown)) == null ? 0
                            : tEffect.getDuration())),
                    Math.max(0, (5 * aLevel) / 7)));
            aEntity.addPotionEffect(
                new PotionEffect(
                    Potion.confusion.id,
                    aLevel * 130 * aAmountOfItems + Math.max(
                        0,
                        ((tEffect = aEntity.getActivePotionEffect(Potion.confusion)) == null ? 0
                            : tEffect.getDuration())),
                    Math.max(0, (5 * aLevel) / 7)));
            aEntity.addPotionEffect(
                new PotionEffect(
                    Potion.weakness.id,
                    aLevel * 150 * aAmountOfItems + Math.max(
                        0,
                        ((tEffect = aEntity.getActivePotionEffect(Potion.weakness)) == null ? 0
                            : tEffect.getDuration())),
                    Math.max(0, (5 * aLevel) / 7)));
            aEntity.addPotionEffect(
                new PotionEffect(
                    Potion.hunger.id,
                    aLevel * 130 * aAmountOfItems + Math.max(
                        0,
                        ((tEffect = aEntity.getActivePotionEffect(Potion.hunger)) == null ? 0 : tEffect.getDuration())),
                    Math.max(0, (5 * aLevel) / 7)));
            aEntity.addPotionEffect(
                new PotionEffect(
                    24 /* IC2 Radiation */,
                    aLevel * 180 * aAmountOfItems + Math.max(
                        0,
                        ((tEffect = aEntity.getActivePotionEffect(Potion.potionTypes[24])) == null ? 0
                            : tEffect.getDuration())),
                    Math.max(0, (5 * aLevel) / 7)));
            return true;
        }
        return false;
    }

    @Deprecated
    public static ItemStack setStack(Object aSetStack, Object aToStack) {
        if (aSetStack instanceof ItemStack setStack && aToStack instanceof ItemStack toStack)
            return setStack(setStack, toStack);
        return null;
    }

    public static ItemStack setStack(ItemStack aSetStack, ItemStack aToStack) {
        if (isStackInvalid(aSetStack) || isStackInvalid(aToStack)) return null;
        aSetStack.func_150996_a(aToStack.getItem());
        aSetStack.stackSize = aToStack.stackSize;
        Items.feather.setDamage(aSetStack, Items.feather.getDamage(aToStack));
        aSetStack.setTagCompound(aToStack.getTagCompound());
        return aSetStack;
    }

    public static FluidStack[] copyFluidArray(FluidStack... aStacks) {
        if (aStacks == null) return null;
        FluidStack[] rStacks = new FluidStack[aStacks.length];
        for (int i = 0; i < aStacks.length; i++) if (aStacks[i] != null) rStacks[i] = aStacks[i].copy();
        return rStacks;
    }

    public static ItemStack[] copyItemArray(ItemStack... aStacks) {
        if (aStacks == null) return null;
        ItemStack[] rStacks = new ItemStack[aStacks.length];
        for (int i = 0; i < aStacks.length; i++) rStacks[i] = copy(aStacks[i]);
        return rStacks;
    }

    /**
     * @deprecated use {@link #copy(ItemStack)} instead
     */
    @Deprecated
    public static ItemStack copy(Object... aStacks) {
        for (Object tStack : aStacks) if (isStackValid(tStack)) return ((ItemStack) tStack).copy();
        return null;
    }

    public static ItemStack copy(ItemStack aStack) {
        if (isStackValid(aStack)) return aStack.copy();
        return null;
    }

    @Deprecated
    private static ItemStack firstStackOrNull(Object... aStacks) {
        for (Object tStack : aStacks) if (tStack instanceof ItemStack stack) return stack;
        return null;
    }

    @Nullable
    public static ItemStack copyOrNull(@Nullable ItemStack stack) {
        if (isStackValid(stack)) return stack.copy();
        return null;
    }

    public static FluidStack copyAmount(int aAmount, FluidStack aStack) {
        if (aStack == null) return null;
        FluidStack rStack = aStack.copy();
        rStack.amount = aAmount;
        return rStack;
    }

    /**
     * @deprecated use {@link #copyAmount(int, ItemStack)} instead
     */
    @Deprecated
    public static ItemStack copyAmount(long aAmount, Object... aStacks) {
        return copyAmount(aAmount, firstStackOrNull(aStacks));
    }

    /**
     * @deprecated use {@link #copyAmount(int, ItemStack)} instead
     */
    @Deprecated
    public static ItemStack copyAmount(long aAmount, ItemStack aStack) {
        return copyAmount((int) aAmount, aStack);
    }

    public static ItemStack copyAmount(int aAmount, ItemStack aStack) {
        ItemStack rStack = copy(aStack);
        if (isStackInvalid(rStack)) return null;
        if (aAmount > 64) aAmount = 64;
        else if (aAmount == -1) aAmount = 111;
        else if (aAmount < 0) aAmount = 0;
        rStack.stackSize = (byte) aAmount;
        return rStack;
    }

    /**
     * @deprecated use {@link #multiplyStack(int, ItemStack)} instead
     */
    @Deprecated
    public static ItemStack multiplyStack(long aMultiplier, Object... aStacks) {
        return multiplyStack((int) aMultiplier, firstStackOrNull(aStacks));
    }

    public static ItemStack multiplyStack(int aMultiplier, ItemStack aStack) {
        ItemStack rStack = copy(aStack);
        if (isStackInvalid(rStack)) return null;
        int tAmount = rStack.stackSize * aMultiplier;
        if (tAmount > 64) tAmount = 64;
        else if (tAmount == -1) tAmount = 111;
        else if (tAmount < 0) tAmount = 0;
        rStack.stackSize = (byte) tAmount;
        return rStack;
    }

    /**
     * @deprecated use {@link #copyAmountUnsafe(int, ItemStack)} instead
     */
    @Deprecated
    public static ItemStack copyAmountUnsafe(long aAmount, Object... aStacks) {
        return copyAmountUnsafe((int) aAmount, firstStackOrNull(aStacks));
    }

    /**
     * Unlike {@link #copyAmount(int, ItemStack)}, this method does not restrict stack size by 64.
     */
    public static ItemStack copyAmountUnsafe(int aAmount, ItemStack aStack) {
        ItemStack rStack = copy(aStack);
        if (isStackInvalid(rStack)) return null;
        else if (aAmount < 0) aAmount = 0;
        rStack.stackSize = (int) aAmount;
        return rStack;
    }

    /**
     * @deprecated use {@link #copyMetaData(int, ItemStack)} instead
     */
    @Deprecated
    public static ItemStack copyMetaData(long aMetaData, Object... aStacks) {
        return copyMetaData((int) aMetaData, firstStackOrNull(aStacks));
    }

    public static ItemStack copyMetaData(int aMetaData, ItemStack aStack) {
        ItemStack rStack = copy(aStack);
        if (isStackInvalid(rStack)) return null;
        Items.feather.setDamage(rStack, aMetaData);
        return rStack;
    }

    /**
     * @deprecated use {@link #copyAmountAndMetaData(int, int, ItemStack)} instead
     */
    @Deprecated
    public static ItemStack copyAmountAndMetaData(long aAmount, long aMetaData, Object... aStacks) {
        return copyAmountAndMetaData(aAmount, aMetaData, firstStackOrNull(aStacks));
    }

    /**
     * @deprecated use {@link #copyAmountAndMetaData(int, int, ItemStack)} instead
     */
    @Deprecated
    public static ItemStack copyAmountAndMetaData(long aAmount, long aMetaData, ItemStack aStack) {
        return copyAmountAndMetaData((int) aAmount, (int) aMetaData, aStack);
    }

    public static ItemStack copyAmountAndMetaData(int aAmount, int aMetaData, ItemStack aStack) {
        ItemStack rStack = copyAmount(aAmount, aStack);
        if (isStackInvalid(rStack)) return null;
        Items.feather.setDamage(rStack, aMetaData);
        return rStack;
    }

    /**
     * @deprecated use {@link #mul(int, ItemStack)} instead
     */
    @Deprecated
    public static ItemStack mul(long aMultiplier, Object... aStacks) {
        return mul((int) aMultiplier, firstStackOrNull(aStacks));
    }

    /**
     * returns a copy of an ItemStack with its Stacksize being multiplied by aMultiplier
     */
    public static ItemStack mul(int aMultiplier, ItemStack aStack) {
        ItemStack rStack = copy(aStack);
        if (rStack == null) return null;
        rStack.stackSize *= aMultiplier;
        return rStack;
    }

    /**
     * Loads an ItemStack properly.
     */
    public static ItemStack loadItem(NBTTagCompound aNBT, String aTagName) {
        return loadItem(aNBT.getCompoundTag(aTagName));
    }

    public static FluidStack loadFluid(NBTTagCompound aNBT, String aTagName) {
        return loadFluid(aNBT.getCompoundTag(aTagName));
    }

    /**
     * Loads an ItemStack properly.
     */
    public static ItemStack loadItem(NBTTagCompound aNBT) {
        if (aNBT == null) return null;
        ItemStack tRawStack = ItemStack.loadItemStackFromNBT(aNBT);
        int tRealStackSize = 0;
        if (tRawStack != null && aNBT.hasKey("Count", Constants.NBT.TAG_INT)) {
            tRealStackSize = aNBT.getInteger("Count");
            tRawStack.stackSize = tRealStackSize;
        } else if (tRawStack != null) {
            tRealStackSize = tRawStack.stackSize;
        }
        ItemStack tRet = GT_OreDictUnificator.get(true, tRawStack);
        if (tRet != null) tRet.stackSize = tRealStackSize;
        return tRet;
    }

    public static void saveItem(NBTTagCompound aParentTag, String aTagName, ItemStack aStack) {
        if (aStack != null) aParentTag.setTag(aTagName, saveItem(aStack));
    }

    public static NBTTagCompound saveItem(ItemStack aStack) {
        if (aStack == null) return new NBTTagCompound();
        NBTTagCompound t = new NBTTagCompound();
        aStack.writeToNBT(t);
        if (aStack.stackSize > Byte.MAX_VALUE) t.setInteger("Count", aStack.stackSize);
        return t;
    }

    /**
     * Loads an FluidStack properly.
     */
    public static FluidStack loadFluid(NBTTagCompound aNBT) {
        if (aNBT == null) return null;
        return FluidStack.loadFluidStackFromNBT(aNBT);
    }

    public static <E> E selectItemInList(int aIndex, E aReplacement, List<E> aList) {
        if (aList == null || aList.isEmpty()) return aReplacement;
        if (aList.size() <= aIndex) return aList.get(aList.size() - 1);
        if (aIndex < 0) return aList.get(0);
        return aList.get(aIndex);
    }

    @SafeVarargs
    public static <E> E selectItemInList(int aIndex, E aReplacement, E... aList) {
        if (aList == null || aList.length == 0) return aReplacement;
        if (aList.length <= aIndex) return aList[aList.length - 1];
        if (aIndex < 0) return aList[0];
        return aList[aIndex];
    }

    public static boolean isStackInStackSet(ItemStack aStack, Set<ItemStack> aSet) {
        if (aStack == null) return false;
        if (aSet.contains(aStack)) return true;

        return aSet.contains(GT_ItemStack.internalCopyStack(aStack, true));
    }

    public static boolean isStackInList(ItemStack aStack, Collection<GT_ItemStack> aList) {
        if (aStack == null) {
            return false;
        }
        return isStackInList(new GT_ItemStack(aStack), aList);
    }

    public static boolean isStackInList(ItemStack aStack, Set<GT_ItemStack2> aList) {
        if (aStack == null) {
            return false;
        }
        return isStackInList(new GT_ItemStack2(aStack), aList);
    }

    public static boolean isStackInList(GT_ItemStack aStack, Collection<GT_ItemStack> aList) {
        return aStack != null
            && (aList.contains(aStack) || aList.contains(new GT_ItemStack(aStack.mItem, aStack.mStackSize, W)));
    }

    public static boolean isStackInList(GT_ItemStack2 aStack, Set<GT_ItemStack2> aList) {
        return aStack != null
            && (aList.contains(aStack) || aList.contains(new GT_ItemStack2(aStack.mItem, aStack.mStackSize, W)));
    }

    /**
     * re-maps all Keys of a Map after the Keys were weakened.
     */
    public static <X, Y> Map<X, Y> reMap(Map<X, Y> aMap) {
        Map<X, Y> tMap = null;
        // We try to clone the Map first (most Maps are Cloneable) in order to retain as much state of the Map as
        // possible when rehashing. For example, "Custom" HashMaps from fastutil may have a custom hash function which
        // would not be used to rehash if we just create a new HashMap.
        if (aMap instanceof Cloneable) {
            try {
                tMap = (Map<X, Y>) aMap.getClass()
                    .getMethod("clone")
                    .invoke(aMap);
            } catch (Throwable e) {
                GT_Log.err.println("Failed to clone Map of type " + aMap.getClass());
                e.printStackTrace(GT_Log.err);
            }
        }

        if (tMap == null) {
            tMap = new HashMap<>(aMap);
        }

        aMap.clear();
        aMap.putAll(tMap);
        return aMap;
    }

    /**
     * re-maps all Keys of a Map after the Keys were weakened.
     */
    public static <X, Y> SetMultimap<X, Y> reMap(SetMultimap<X, Y> aMap) {
        @SuppressWarnings("unchecked")
        Map.Entry<X, Y>[] entries = aMap.entries()
            .toArray(new Map.Entry[0]);
        aMap.clear();
        for (Map.Entry<X, Y> entry : entries) {
            aMap.put(entry.getKey(), entry.getValue());
        }
        return aMap;
    }

    public static <X, Y extends Comparable<Y>> LinkedHashMap<X, Y> sortMapByValuesAcending(Map<X, Y> map) {
        return map.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue())
            .collect(CollectorUtils.entriesToMap(LinkedHashMap::new));
    }

    /**
     * Why the fuck do neither Java nor Guava have a Function to do this?
     */
    public static <X, Y extends Comparable<Y>> LinkedHashMap<X, Y> sortMapByValuesDescending(Map<X, Y> aMap) {
        List<Map.Entry<X, Y>> tEntrySet = new LinkedList<>(aMap.entrySet());
        tEntrySet.sort((aValue1, aValue2) -> {
            return aValue2.getValue()
                .compareTo(aValue1.getValue()); // FB: RV - RV_NEGATING_RESULT_OF_COMPARETO
        });
        LinkedHashMap<X, Y> rMap = new LinkedHashMap<>();
        for (Map.Entry<X, Y> tEntry : tEntrySet) rMap.put(tEntry.getKey(), tEntry.getValue());
        return rMap;
    }

    /**
     * Translates a Material Amount into an Amount of Fluid in Fluid Material Units.
     */
    public static long translateMaterialToFluidAmount(long aMaterialAmount, boolean aRoundUp) {
        return translateMaterialToAmount(aMaterialAmount, L, aRoundUp);
    }

    /**
     * Translates a Material Amount into an Amount of Fluid. Second Parameter for things like Bucket Amounts (1000) and
     * similar
     */
    public static long translateMaterialToAmount(long aMaterialAmount, long aAmountPerUnit, boolean aRoundUp) {
        return Math.max(
            0,
            ((aMaterialAmount * aAmountPerUnit) / M)
                + (aRoundUp && (aMaterialAmount * aAmountPerUnit) % M > 0 ? 1 : 0));
    }

    /**
     * This checks if the Dimension is really a Dimension and not another Planet or something. Used for my Teleporter.
     */
    public static boolean isRealDimension(int aDimensionID) {
        if (aDimensionID <= 1 && aDimensionID >= -1 && !GregTech_API.sDimensionalList.contains(aDimensionID))
            return true;
        return !GregTech_API.sDimensionalList.contains(aDimensionID)
            && DimensionManager.isDimensionRegistered(aDimensionID);
    }

    public static boolean moveEntityToDimensionAtCoords(Entity entity, int aDimension, double aX, double aY,
        double aZ) {
        // Credit goes to BrandonCore Author :!:

        if (entity == null || entity.worldObj.isRemote) return false;
        if (entity.ridingEntity != null) entity.mountEntity(null);
        if (entity.riddenByEntity != null) entity.riddenByEntity.mountEntity(null);

        World startWorld = entity.worldObj;
        WorldServer destinationWorld = FMLCommonHandler.instance()
            .getMinecraftServerInstance()
            .worldServerForDimension(aDimension);

        if (destinationWorld == null) {
            return false;
        }

        boolean interDimensional = startWorld.provider.dimensionId != destinationWorld.provider.dimensionId;
        if (!interDimensional) return false;
        startWorld.updateEntityWithOptionalForce(entity, false); // added

        if (entity instanceof EntityPlayerMP player) {
            player.closeScreen(); // added
            player.dimension = aDimension;
            player.playerNetServerHandler.sendPacket(
                new S07PacketRespawn(
                    player.dimension,
                    player.worldObj.difficultySetting,
                    destinationWorld.getWorldInfo()
                        .getTerrainType(),
                    player.theItemInWorldManager.getGameType()));
            ((WorldServer) startWorld).getPlayerManager()
                .removePlayer(player);

            startWorld.playerEntities.remove(player);
            startWorld.updateAllPlayersSleepingFlag();
            int i = entity.chunkCoordX;
            int j = entity.chunkCoordZ;
            if ((entity.addedToChunk) && (startWorld.getChunkProvider()
                .chunkExists(i, j))) {
                startWorld.getChunkFromChunkCoords(i, j)
                    .removeEntity(entity);
                startWorld.getChunkFromChunkCoords(i, j).isModified = true;
            }
            startWorld.loadedEntityList.remove(entity);
            startWorld.onEntityRemoved(entity);
        }

        entity.setLocationAndAngles(aX, aY, aZ, entity.rotationYaw, entity.rotationPitch);

        destinationWorld.theChunkProviderServer.loadChunk((int) aX >> 4, (int) aZ >> 4);

        destinationWorld.theProfiler.startSection("placing");
        if (!(entity instanceof EntityPlayer)) {
            NBTTagCompound entityNBT = new NBTTagCompound();
            entity.isDead = false;
            entityNBT.setString("id", EntityList.getEntityString(entity));
            entity.writeToNBT(entityNBT);
            entity.isDead = true;
            entity = EntityList.createEntityFromNBT(entityNBT, destinationWorld);
            if (entity == null) {
                return false;
            }
            entity.dimension = destinationWorld.provider.dimensionId;
        }
        destinationWorld.spawnEntityInWorld(entity);
        entity.setWorld(destinationWorld);
        entity.setLocationAndAngles(aX, aY, aZ, entity.rotationYaw, entity.rotationPitch);

        destinationWorld.updateEntityWithOptionalForce(entity, false);
        entity.setLocationAndAngles(aX, aY, aZ, entity.rotationYaw, entity.rotationPitch);

        if ((entity instanceof EntityPlayerMP player)) {
            player.mcServer.getConfigurationManager()
                .func_72375_a(player, destinationWorld);
            player.playerNetServerHandler.setPlayerLocation(aX, aY, aZ, player.rotationYaw, player.rotationPitch);
        }

        destinationWorld.updateEntityWithOptionalForce(entity, false);

        if (entity instanceof EntityPlayerMP player) {
            player.theItemInWorldManager.setWorld(destinationWorld);
            player.mcServer.getConfigurationManager()
                .updateTimeAndWeatherForPlayer(player, destinationWorld);
            player.mcServer.getConfigurationManager()
                .syncPlayerInventory(player);

            for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                player.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(player.getEntityId(), potionEffect));
            }

            player.playerNetServerHandler.sendPacket(
                new S1FPacketSetExperience(player.experience, player.experienceTotal, player.experienceLevel));
            FMLCommonHandler.instance()
                .firePlayerChangedDimensionEvent(
                    player,
                    startWorld.provider.dimensionId,
                    destinationWorld.provider.dimensionId);
        }
        entity.setLocationAndAngles(aX, aY, aZ, entity.rotationYaw, entity.rotationPitch);

        destinationWorld.theProfiler.endSection();
        entity.fallDistance = 0;
        return true;
    }

    public static int getScaleCoordinates(double aValue, int aScale) {
        return (int) Math.floor(aValue / aScale);
    }

    public static int getCoordinateScan(ArrayList<String> aList, EntityPlayer aPlayer, World aWorld, int aScanLevel,
        int aX, int aY, int aZ, ForgeDirection side, float aClickX, float aClickY, float aClickZ) {
        if (aList == null) return 0;

        ArrayList<String> tList = new ArrayList<>();
        int rEUAmount = 0;

        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);

        final Block tBlock = aWorld.getBlock(aX, aY, aZ);

        addBaseInfo(aPlayer, aWorld, aX, aY, aZ, tList, tTileEntity, tBlock);

        if (tTileEntity != null) {
            rEUAmount += addFluidHandlerInfo(side, tList, tTileEntity);

            try {
                if (tTileEntity instanceof ic2.api.reactor.IReactorChamber chamber) {
                    rEUAmount += 500;
                    // Redirect the rest of the scans to the reactor itself
                    tTileEntity = (TileEntity) chamber.getReactor();
                }
            } catch (Throwable e) {
                if (D1) e.printStackTrace(GT_Log.err);
            }
            rEUAmount += addReactorInfo(tList, tTileEntity);
            rEUAmount += addAlignmentInfo(tList, tTileEntity);
            rEUAmount += addIC2WrenchableInfo(aPlayer, tList, tTileEntity);
            rEUAmount += addIC2EnergyConductorInfo(tList, tTileEntity);
            rEUAmount += addIC2EnergyStorageInfo(tList, tTileEntity);
            rEUAmount += addUpgradableMachineInfo(tList, tTileEntity);
            rEUAmount += addMachineProgressInfo(tList, tTileEntity);
            rEUAmount += addCoverableInfo(side, tList, tTileEntity);
            addEnergyContainerInfo(tList, tTileEntity);
            addOwnerInfo(tList, tTileEntity);
            addDeviceInfo(tList, tTileEntity);

            rEUAmount += addIC2CropInfo(tList, tTileEntity);

            rEUAmount += addForestryLeavesInfo(tList, tTileEntity);
        }

        final Chunk currentChunk = aWorld.getChunkFromBlockCoords(aX, aZ);
        addUndergroundFluidInfo(aPlayer, tList, currentChunk);
        addPollutionInfo(tList, currentChunk);

        rEUAmount += addDebuggableBlockInfo(aPlayer, aX, aY, aZ, tList, tBlock);

        final BlockScanningEvent tEvent = new BlockScanningEvent(
            aWorld,
            aPlayer,
            aX,
            aY,
            aZ,
            side,
            aScanLevel,
            tBlock,
            tTileEntity,
            tList,
            aClickX,
            aClickY,
            aClickZ);
        tEvent.mEUCost = rEUAmount;
        MinecraftForge.EVENT_BUS.post(tEvent);
        if (!tEvent.isCanceled()) aList.addAll(tList);
        return tEvent.mEUCost;
    }

    private static void addBaseInfo(EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, ArrayList<String> tList,
        TileEntity tTileEntity, Block tBlock) {
        tList.add(
            "----- X: " + EnumChatFormatting.AQUA
                + formatNumbers(aX)
                + EnumChatFormatting.RESET
                + " Y: "
                + EnumChatFormatting.AQUA
                + formatNumbers(aY)
                + EnumChatFormatting.RESET
                + " Z: "
                + EnumChatFormatting.AQUA
                + formatNumbers(aZ)
                + EnumChatFormatting.RESET
                + " D: "
                + EnumChatFormatting.AQUA
                + aWorld.provider.dimensionId
                + EnumChatFormatting.RESET
                + " -----");
        try {
            tList.add(
                GT_Utility.trans("162", "Name: ") + EnumChatFormatting.BLUE
                    + ((tTileEntity instanceof IInventory inv) ? inv.getInventoryName() : tBlock.getUnlocalizedName())
                    + EnumChatFormatting.RESET
                    + GT_Utility.trans("163", " MetaData: ")
                    + EnumChatFormatting.AQUA
                    + aWorld.getBlockMetadata(aX, aY, aZ)
                    + EnumChatFormatting.RESET);
            tList.add(
                GT_Utility.trans("164", "Hardness: ") + EnumChatFormatting.YELLOW
                    + tBlock.getBlockHardness(aWorld, aX, aY, aZ)
                    + EnumChatFormatting.RESET
                    + GT_Utility.trans("165", " Blast Resistance: ")
                    + EnumChatFormatting.YELLOW
                    + tBlock
                        .getExplosionResistance(aPlayer, aWorld, aX, aY, aZ, aPlayer.posX, aPlayer.posY, aPlayer.posZ)
                    + EnumChatFormatting.RESET);
            if (tBlock.isBeaconBase(aWorld, aX, aY, aZ, aX, aY + 1, aZ)) tList.add(
                EnumChatFormatting.GOLD + GT_Utility.trans("166", "Is valid Beacon Pyramid Material")
                    + EnumChatFormatting.RESET);
        } catch (Throwable e) {
            if (D1) e.printStackTrace(GT_Log.err);
        }
    }

    private static int addFluidHandlerInfo(ForgeDirection side, ArrayList<String> tList, TileEntity tTileEntity) {
        int rEUAmount = 0;
        try {
            if (tTileEntity instanceof IFluidHandler fluidHandler) {
                rEUAmount += 500;
                final FluidTankInfo[] tTanks = fluidHandler.getTankInfo(side);
                if (tTanks != null) for (byte i = 0; i < tTanks.length; i++) {
                    tList.add(
                        GT_Utility.trans("167", "Tank ") + i
                            + ": "
                            + EnumChatFormatting.GREEN
                            + formatNumbers((tTanks[i].fluid == null ? 0 : tTanks[i].fluid.amount))
                            + EnumChatFormatting.RESET
                            + " L / "
                            + EnumChatFormatting.YELLOW
                            + formatNumbers(tTanks[i].capacity)
                            + EnumChatFormatting.RESET
                            + " L "
                            + EnumChatFormatting.GOLD
                            + getFluidName(tTanks[i].fluid, true)
                            + EnumChatFormatting.RESET);
                }
            }
        } catch (Throwable e) {
            if (D1) e.printStackTrace(GT_Log.err);
        }
        return rEUAmount;
    }

    private static int addDebuggableBlockInfo(EntityPlayer aPlayer, int aX, int aY, int aZ, ArrayList<String> tList,
        Block tBlock) {
        int rEUAmount = 0;
        try {
            if (tBlock instanceof IDebugableBlock debugableBlock) {
                rEUAmount += 500;
                final ArrayList<String> temp = debugableBlock.getDebugInfo(aPlayer, aX, aY, aZ, 3);
                if (temp != null) tList.addAll(temp);
            }
        } catch (Throwable e) {
            if (D1) e.printStackTrace(GT_Log.err);
        }
        return rEUAmount;
    }

    private static void addPollutionInfo(ArrayList<String> tList, Chunk currentChunk) {
        if (GT_Pollution.hasPollution(currentChunk)) {
            tList.add(
                GT_Utility.trans("202", "Pollution in Chunk: ") + EnumChatFormatting.RED
                    + formatNumbers(GT_Pollution.getPollution(currentChunk))
                    + EnumChatFormatting.RESET
                    + GT_Utility.trans("203", " gibbl"));
        } else {
            tList.add(
                EnumChatFormatting.GREEN + GT_Utility.trans("204", "No Pollution in Chunk! HAYO!")
                    + EnumChatFormatting.RESET);
        }
    }

    private static void addUndergroundFluidInfo(EntityPlayer aPlayer, ArrayList<String> tList, Chunk currentChunk) {
        if (aPlayer.capabilities.isCreativeMode) {
            final FluidStack tFluid = undergroundOilReadInformation(currentChunk); // -# to only read
            if (tFluid != null) tList.add(
                EnumChatFormatting.GOLD + tFluid.getLocalizedName()
                    + EnumChatFormatting.RESET
                    + ": "
                    + EnumChatFormatting.YELLOW
                    + formatNumbers(tFluid.amount)
                    + EnumChatFormatting.RESET
                    + " L");
            else tList.add(
                EnumChatFormatting.GOLD + GT_Utility.trans("201", "Nothing")
                    + EnumChatFormatting.RESET
                    + ": "
                    + EnumChatFormatting.YELLOW
                    + '0'
                    + EnumChatFormatting.RESET
                    + " L");
        }
    }

    private static int addForestryLeavesInfo(ArrayList<String> tList, TileEntity tTileEntity) {
        int rEUAmount = 0;
        try {
            if (tTileEntity instanceof forestry.arboriculture.tiles.TileLeaves tileLeaves) {
                final forestry.api.arboriculture.ITree tree = tileLeaves.getTree();
                if (tree != null) {
                    rEUAmount += 1000;
                    if (!tree.isAnalyzed()) tree.analyze();
                    tree.addTooltip(tList);
                }
            }
        } catch (Throwable e) {
            if (D1) e.printStackTrace(GT_Log.err);
        }
        return rEUAmount;
    }

    private static int addIC2CropInfo(ArrayList<String> tList, TileEntity tTileEntity) {
        int rEUAmount = 0;
        try {
            if (tTileEntity instanceof ic2.api.crops.ICropTile crop) {
                rEUAmount += 1000;
                if (crop.getScanLevel() < 4) crop.setScanLevel((byte) 4);
                if (crop.getCrop() != null) {
                    tList.add(
                        GT_Utility.trans("187", "Type -- Crop-Name: ") + crop.getCrop()
                            .name()
                            + GT_Utility.trans("188", "  Growth: ")
                            + crop.getGrowth()
                            + GT_Utility.trans("189", "  Gain: ")
                            + crop.getGain()
                            + GT_Utility.trans("190", "  Resistance: ")
                            + crop.getResistance());
                }
                tList.add(
                    GT_Utility.trans("191", "Plant -- Fertilizer: ") + crop.getNutrientStorage()
                        + GT_Utility.trans("192", "  Water: ")
                        + crop.getHydrationStorage()
                        + GT_Utility.trans("193", "  Weed-Ex: ")
                        + crop.getWeedExStorage()
                        + GT_Utility.trans("194", "  Scan-Level: ")
                        + crop.getScanLevel());
                tList.add(
                    GT_Utility.trans("195", "Environment -- Nutrients: ") + crop.getNutrients()
                        + GT_Utility.trans("196", "  Humidity: ")
                        + crop.getHumidity()
                        + GT_Utility.trans("197", "  Air-Quality: ")
                        + crop.getAirQuality());
                if (crop.getCrop() != null) {
                    final StringBuilder tStringB = new StringBuilder();
                    for (final String tAttribute : crop.getCrop()
                        .attributes()) {
                        tStringB.append(", ")
                            .append(tAttribute);
                    }
                    final String tString = tStringB.toString();
                    tList.add(GT_Utility.trans("198", "Attributes:") + tString.replaceFirst(",", E));
                    tList.add(
                        GT_Utility.trans("199", "Discovered by: ") + crop.getCrop()
                            .discoveredBy());
                }
            }
        } catch (Throwable e) {
            if (D1) e.printStackTrace(GT_Log.err);
        }
        return rEUAmount;
    }

    private static void addDeviceInfo(ArrayList<String> tList, TileEntity tTileEntity) {
        try {
            if (tTileEntity instanceof IGregTechDeviceInformation info && info.isGivingInformation()) {
                tList.addAll(Arrays.asList(info.getInfoData()));
            }
        } catch (Throwable e) {
            if (D1) e.printStackTrace(GT_Log.err);
        }
    }

    private static void addOwnerInfo(ArrayList<String> tList, TileEntity tTileEntity) {
        try {
            if (tTileEntity instanceof IGregTechTileEntity gtTE) {
                tList.add(
                    GT_Utility.trans("186", "Owned by: ") + EnumChatFormatting.BLUE
                        + gtTE.getOwnerName()
                        + EnumChatFormatting.RESET);
            }
        } catch (Throwable e) {
            if (D1) e.printStackTrace(GT_Log.err);
        }
    }

    private static void addEnergyContainerInfo(ArrayList<String> tList, TileEntity tTileEntity) {
        try {
            if (tTileEntity instanceof IBasicEnergyContainer energyContainer && energyContainer.getEUCapacity() > 0) {
                tList.add(
                    GT_Utility.trans("179", "Max IN: ") + EnumChatFormatting.RED
                        + formatNumbers(energyContainer.getInputVoltage())
                        + " ("
                        + GT_Values.VN[getTier(energyContainer.getInputVoltage())]
                        + ") "
                        + EnumChatFormatting.RESET
                        + GT_Utility.trans("182", " EU at ")
                        + EnumChatFormatting.RED
                        + formatNumbers(energyContainer.getInputAmperage())
                        + EnumChatFormatting.RESET
                        + GT_Utility.trans("183", " A"));
                tList.add(
                    GT_Utility.trans("181", "Max OUT: ") + EnumChatFormatting.RED
                        + formatNumbers(energyContainer.getOutputVoltage())
                        + " ("
                        + GT_Values.VN[getTier(energyContainer.getOutputVoltage())]
                        + ") "
                        + EnumChatFormatting.RESET
                        + GT_Utility.trans("182", " EU at ")
                        + EnumChatFormatting.RED
                        + formatNumbers(energyContainer.getOutputAmperage())
                        + EnumChatFormatting.RESET
                        + GT_Utility.trans("183", " A"));
                tList.add(
                    GT_Utility.trans("184", "Energy: ") + EnumChatFormatting.GREEN
                        + formatNumbers(energyContainer.getStoredEU())
                        + EnumChatFormatting.RESET
                        + " EU / "
                        + EnumChatFormatting.YELLOW
                        + formatNumbers(energyContainer.getEUCapacity())
                        + EnumChatFormatting.RESET
                        + " EU");
            }
        } catch (Throwable e) {
            if (D1) e.printStackTrace(GT_Log.err);
        }
    }

    private static int addCoverableInfo(ForgeDirection side, ArrayList<String> tList, TileEntity tTileEntity) {
        int rEUAmount = 0;
        try {
            if (tTileEntity instanceof ICoverable coverable) {
                rEUAmount += 300;
                final String tString = coverable.getCoverInfoAtSide(side)
                    .getBehaviorDescription();
                if (tString != null && !tString.equals(E)) tList.add(tString);
            }
        } catch (Throwable e) {
            if (D1) e.printStackTrace(GT_Log.err);
        }
        return rEUAmount;
    }

    private static int addMachineProgressInfo(ArrayList<String> tList, TileEntity tTileEntity) {
        int rEUAmount = 0;
        try {
            if (tTileEntity instanceof IMachineProgress progress) {
                if (progress.isAllowedToWork() && !progress.hasThingsToDo()) {
                    tList.add(EnumChatFormatting.RED + "Disabled." + EnumChatFormatting.RESET);
                }
                if (progress.wasShutdown() && isStringValid(
                    progress.getLastShutDownReason()
                        .getDisplayString())) {
                    tList.add(
                        progress.getLastShutDownReason()
                            .getDisplayString());
                }
                rEUAmount += 400;
                int tValue = 0;
                if (0 < (tValue = progress.getMaxProgress())) tList.add(
                    GT_Utility.trans("178", "Progress/Load: ") + EnumChatFormatting.GREEN
                        + formatNumbers(progress.getProgress())
                        + EnumChatFormatting.RESET
                        + " / "
                        + EnumChatFormatting.YELLOW
                        + formatNumbers(tValue)
                        + EnumChatFormatting.RESET);
            }
        } catch (Throwable e) {
            if (D1) e.printStackTrace(GT_Log.err);
        }
        return rEUAmount;
    }

    private static int addUpgradableMachineInfo(ArrayList<String> tList, TileEntity tTileEntity) {
        int rEUAmount = 0;
        try {
            if (tTileEntity instanceof IUpgradableMachine upgradableMachine) {
                rEUAmount += 500;
                if (upgradableMachine.hasMufflerUpgrade()) tList.add(
                    EnumChatFormatting.GREEN + GT_Utility.trans("177", "Has Muffler Upgrade")
                        + EnumChatFormatting.RESET);
            }
        } catch (Throwable e) {
            if (D1) e.printStackTrace(GT_Log.err);
        }
        return rEUAmount;
    }

    private static int addIC2EnergyStorageInfo(ArrayList<String> tList, TileEntity tTileEntity) {
        int rEUAmount = 0;
        try {
            if (tTileEntity instanceof ic2.api.tile.IEnergyStorage storage) {
                rEUAmount += 200;
                tList.add(
                    GT_Utility.trans("176", "Contained Energy: ") + EnumChatFormatting.YELLOW
                        + formatNumbers(storage.getStored())
                        + EnumChatFormatting.RESET
                        + " EU / "
                        + EnumChatFormatting.YELLOW
                        + formatNumbers(storage.getCapacity())
                        + EnumChatFormatting.RESET
                        + " EU");
            }
        } catch (Throwable e) {
            if (D1) e.printStackTrace(GT_Log.err);
        }
        return rEUAmount;
    }

    private static int addIC2EnergyConductorInfo(ArrayList<String> tList, TileEntity tTileEntity) {
        int rEUAmount = 0;
        try {
            if (tTileEntity instanceof ic2.api.energy.tile.IEnergyConductor conductor) {
                rEUAmount += 200;
                tList.add(
                    GT_Utility.trans("175", "Conduction Loss: ") + EnumChatFormatting.YELLOW
                        + conductor.getConductionLoss()
                        + EnumChatFormatting.RESET);
            }
        } catch (Throwable e) {
            if (D1) e.printStackTrace(GT_Log.err);
        }
        return rEUAmount;
    }

    private static int addIC2WrenchableInfo(EntityPlayer aPlayer, ArrayList<String> tList, TileEntity tTileEntity) {
        int rEUAmount = 0;
        try {
            if (tTileEntity instanceof ic2.api.tile.IWrenchable wrenchable) {
                rEUAmount += 100;
                tList.add(
                    GT_Utility.trans("171", "Facing: ") + EnumChatFormatting.GREEN
                        + wrenchable.getFacing()
                        + EnumChatFormatting.RESET
                        + GT_Utility.trans("172", " / Chance: ")
                        + EnumChatFormatting.YELLOW
                        + (wrenchable.getWrenchDropRate() * 100)
                        + EnumChatFormatting.RESET
                        + "%");
                tList.add(
                    wrenchable.wrenchCanRemove(aPlayer)
                        ? EnumChatFormatting.GREEN + GT_Utility.trans("173", "You can remove this with a Wrench")
                            + EnumChatFormatting.RESET
                        : EnumChatFormatting.RED + GT_Utility.trans("174", "You can NOT remove this with a Wrench")
                            + EnumChatFormatting.RESET);
            }
        } catch (Throwable e) {
            if (D1) e.printStackTrace(GT_Log.err);
        }
        return rEUAmount;
    }

    private static int addAlignmentInfo(ArrayList<String> tList, TileEntity tTileEntity) {
        int rEUAmount = 0;
        try {
            if (tTileEntity instanceof IAlignmentProvider alignmentProvider) {
                final IAlignment tAlignment = alignmentProvider.getAlignment();
                if (tAlignment != null) {
                    rEUAmount += 100;
                    tList.add(
                        GT_Utility.trans("219", "Extended Facing: ") + EnumChatFormatting.GREEN
                            + tAlignment.getExtendedFacing()
                            + EnumChatFormatting.RESET);
                }
            }
        } catch (Throwable e) {
            if (D1) e.printStackTrace(GT_Log.err);
        }
        return rEUAmount;
    }

    private static int addReactorInfo(ArrayList<String> tList, TileEntity tTileEntity) {
        int rEUAmount = 0;
        try {
            if (tTileEntity instanceof ic2.api.reactor.IReactor reactor) {
                rEUAmount += 500;
                tList.add(
                    GT_Utility.trans("168", "Heat: ") + EnumChatFormatting.GREEN
                        + formatNumbers(reactor.getHeat())
                        + EnumChatFormatting.RESET
                        + " / "
                        + EnumChatFormatting.YELLOW
                        + formatNumbers(reactor.getMaxHeat())
                        + EnumChatFormatting.RESET);
                tList.add(
                    GT_Utility.trans("169", "HEM: ") + EnumChatFormatting.YELLOW
                        + reactor.getHeatEffectModifier()
                        + EnumChatFormatting.RESET);
            }
        } catch (Throwable e) {
            if (D1) e.printStackTrace(GT_Log.err);
        }
        return rEUAmount;
    }

    public static String trans(String aKey, String aEnglish) {
        return GT_LanguageManager.addStringLocalization("Interaction_DESCRIPTION_Index_" + aKey, aEnglish);
    }

    public static String getTrans(String aKey) {
        return GT_LanguageManager.getTranslation("Interaction_DESCRIPTION_Index_" + aKey);
    }

    /**
     * @return an Array containing the X and the Y Coordinate of the clicked Point, with the top left Corner as Origin,
     *         like on the Texture Sheet. return values should always be between [0.0F and 0.99F].
     */
    // TODO: use clamp()
    public static float[] getClickedFacingCoords(ForgeDirection side, float aX, float aY, float aZ) {
        return switch (side) {
            case DOWN -> new float[] { Math.min(0.99F, Math.max(0, 1 - aX)), Math.min(0.99F, Math.max(0, aZ)) };
            case UP -> new float[] { Math.min(0.99F, Math.max(0, aX)), Math.min(0.99F, Math.max(0, aZ)) };
            case NORTH -> new float[] { Math.min(0.99F, Math.max(0, 1 - aX)), Math.min(0.99F, Math.max(0, 1 - aY)) };
            case SOUTH -> new float[] { Math.min(0.99F, Math.max(0, aX)), Math.min(0.99F, Math.max(0, 1 - aY)) };
            case WEST -> new float[] { Math.min(0.99F, Math.max(0, aZ)), Math.min(0.99F, Math.max(0, 1 - aY)) };
            case EAST -> new float[] { Math.min(0.99F, Math.max(0, 1 - aZ)), Math.min(0.99F, Math.max(0, 1 - aY)) };
            default -> new float[] { 0.5F, 0.5F };
        };
    }

    /**
     * This Function determines the direction a Block gets when being Wrenched. returns -1 if invalid. Even though that
     * could never happen.
     */
    public static ForgeDirection determineWrenchingSide(ForgeDirection side, float aX, float aY, float aZ) {
        ForgeDirection tBack = side.getOpposite();
        switch (side) {
            case DOWN, UP -> {
                if (aX < 0.25) {
                    if (aZ < 0.25) return tBack;
                    if (aZ > 0.75) return tBack;
                    return WEST;
                }
                if (aX > 0.75) {
                    if (aZ < 0.25) return tBack;
                    if (aZ > 0.75) return tBack;
                    return EAST;
                }
                if (aZ < 0.25) return NORTH;
                if (aZ > 0.75) return SOUTH;
                return side;
            }
            case NORTH, SOUTH -> {
                if (aX < 0.25) {
                    if (aY < 0.25) return tBack;
                    if (aY > 0.75) return tBack;
                    return WEST;
                }
                if (aX > 0.75) {
                    if (aY < 0.25) return tBack;
                    if (aY > 0.75) return tBack;
                    return EAST;
                }
                if (aY < 0.25) return DOWN;
                if (aY > 0.75) return UP;
                return side;
            }
            case WEST, EAST -> {
                if (aZ < 0.25) {
                    if (aY < 0.25) return tBack;
                    if (aY > 0.75) return tBack;
                    return NORTH;
                }
                if (aZ > 0.75) {
                    if (aY < 0.25) return tBack;
                    if (aY > 0.75) return tBack;
                    return SOUTH;
                }
                if (aY < 0.25) return DOWN;
                if (aY > 0.75) return UP;
                return side;
            }
        }
        return UNKNOWN;
    }

    private static DecimalFormat getDecimalFormat() {
        return decimalFormatters.computeIfAbsent(Locale.getDefault(Locale.Category.FORMAT), locale -> {
            DecimalFormat numberFormat = new DecimalFormat(); // uses the necessary locale inside anyway
            numberFormat.setGroupingUsed(true);
            numberFormat.setMaximumFractionDigits(2);
            numberFormat.setRoundingMode(RoundingMode.HALF_UP);
            DecimalFormatSymbols decimalFormatSymbols = numberFormat.getDecimalFormatSymbols();
            decimalFormatSymbols.setGroupingSeparator(','); // Use sensible separator for best clarity.
            numberFormat.setDecimalFormatSymbols(decimalFormatSymbols);
            return numberFormat;
        });
    }

    public static String formatNumbers(BigInteger aNumber) {
        return getDecimalFormat().format(aNumber);
    }

    public static String formatNumbers(long aNumber) {
        return getDecimalFormat().format(aNumber);
    }

    public static String formatNumbers(double aNumber) {
        return getDecimalFormat().format(aNumber);
    }

    /*
     * Check if stack has enough items of given type and subtract from stack, if there's no creative or 111 stack.
     */
    public static boolean consumeItems(EntityPlayer player, ItemStack stack, Item item, int count) {
        if (stack != null && stack.getItem() == item && stack.stackSize >= count) {
            if ((!player.capabilities.isCreativeMode) && (stack.stackSize != 111)) stack.stackSize -= count;
            return true;
        }
        return false;
    }

    /*
     * Check if stack has enough items of given gregtech material (will be oredicted) and subtract from stack, if
     * there's no creative or 111 stack.
     */
    public static boolean consumeItems(EntityPlayer player, ItemStack stack, gregtech.api.enums.Materials mat,
        int count) {
        if (stack != null && GT_OreDictUnificator.getItemData(stack).mMaterial.mMaterial == mat
            && stack.stackSize >= count) {
            if ((!player.capabilities.isCreativeMode) && (stack.stackSize != 111)) stack.stackSize -= count;
            return true;
        }
        return false;
    }

    public static ArrayList<String> sortByValueToList(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(map.entrySet());
        list.sort((o1, o2) -> o2.getValue() - o1.getValue());

        ArrayList<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> e : list) result.add(e.getKey());
        return result;
    }

    public static String joinListToString(List<String> list) {
        StringBuilder result = new StringBuilder(32);
        for (String s : list) result.append(result.length() == 0 ? s : '|' + s);
        return result.toString();
    }

    public static ItemStack getIntegratedCircuit(int config) {
        return ItemList.Circuit_Integrated.getWithDamage(0, config);
    }

    public static float getBlockHardnessAt(World aWorld, int aX, int aY, int aZ) {
        return aWorld.getBlock(aX, aY, aZ)
            .getBlockHardness(aWorld, aX, aY, aZ);
    }

    public static FakePlayer getFakePlayer(IGregTechTileEntity aBaseMetaTileEntity) {
        if (aBaseMetaTileEntity.getWorld() instanceof WorldServer) {
            return FakePlayerFactory.get(
                (WorldServer) aBaseMetaTileEntity.getWorld(),
                new GameProfile(aBaseMetaTileEntity.getOwnerUuid(), aBaseMetaTileEntity.getOwnerName()));
        }
        return null;
    }

    public static boolean eraseBlockByFakePlayer(FakePlayer aPlayer, int aX, int aY, int aZ, boolean isSimulate) {
        if (aPlayer == null) return false;
        World aWorld = aPlayer.worldObj;
        BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(
            aX,
            aY,
            aZ,
            aWorld,
            aWorld.getBlock(aX, aY, aZ),
            aWorld.getBlockMetadata(aX, aY, aZ),
            aPlayer);
        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) {
            if (!isSimulate) return aWorld.setBlockToAir(aX, aY, aZ);
            return true;
        }
        return false;
    }

    public static boolean setBlockByFakePlayer(FakePlayer aPlayer, int aX, int aY, int aZ, Block aBlock, int aMeta,
        boolean isSimulate) {
        if (aPlayer == null) return false;
        World aWorld = aPlayer.worldObj;
        BlockEvent.PlaceEvent event = ForgeEventFactory
            .onPlayerBlockPlace(aPlayer, new BlockSnapshot(aWorld, aX, aY, aZ, aBlock, aMeta), UNKNOWN);
        if (!event.isCanceled()) {
            if (!isSimulate) return aWorld.setBlock(aX, aY, aZ, aBlock, aMeta, 3);
            return true;
        }
        return false;
    }

    public static int findMatchingStackInList(List<ItemStack> aStacks, ItemStack aStack) {
        if (isStackInvalid(aStack)) return -1;
        for (int i = 0, aStacksSize = aStacks.size(); i < aStacksSize; i++) {
            ItemStack tStack = aStacks.get(i);
            if (areStacksEqual(aStack, tStack)) return i;
        }
        return -1;
    }

    /**
     * @return Supplied collection that doesn't contain invalid MetaTileEntities
     */
    public static <T extends Collection<E>, E extends MetaTileEntity> T filterValidMTEs(T metaTileEntities) {
        metaTileEntities.removeIf(mte -> mte == null || !mte.isValid());
        return metaTileEntities;
    }

    public static class ItemNBT {

        public static void setNBT(ItemStack aStack, NBTTagCompound aNBT) {
            if (aNBT == null) {
                aStack.setTagCompound(null);
                return;
            }
            ArrayList<String> tTagsToRemove = new ArrayList<>();
            for (String tKey : aNBT.func_150296_c()) {
                NBTBase tValue = aNBT.getTag(tKey);
                if (tValue == null || (tValue instanceof NBTPrimitive && ((NBTPrimitive) tValue).func_150291_c() == 0)
                    || (tValue instanceof NBTTagString && isStringInvalid(((NBTTagString) tValue).func_150285_a_())))
                    tTagsToRemove.add(tKey);
            }
            for (String tKey : tTagsToRemove) aNBT.removeTag(tKey);
            aStack.setTagCompound(aNBT.hasNoTags() ? null : aNBT);
        }

        public static NBTTagCompound getNBT(ItemStack aStack) {
            NBTTagCompound rNBT = aStack.getTagCompound();
            return rNBT == null ? new NBTTagCompound() : rNBT;
        }

        public static void setPunchCardData(ItemStack aStack, String aPunchCardData) {
            NBTTagCompound tNBT = getNBT(aStack);
            tNBT.setString("GT.PunchCardData", aPunchCardData);
            setNBT(aStack, tNBT);
        }

        public static String getPunchCardData(ItemStack aStack) {
            NBTTagCompound tNBT = getNBT(aStack);
            return tNBT.getString("GT.PunchCardData");
        }

        public static void setLighterFuel(ItemStack aStack, long aFuel) {
            NBTTagCompound tNBT = getNBT(aStack);
            tNBT.setLong("GT.LighterFuel", aFuel);
            setNBT(aStack, tNBT);
        }

        public static long getLighterFuel(ItemStack aStack) {
            NBTTagCompound tNBT = getNBT(aStack);
            return tNBT.getLong("GT.LighterFuel");
        }

        public static void setMapID(ItemStack aStack, short aMapID) {
            NBTTagCompound tNBT = getNBT(aStack);
            tNBT.setShort("map_id", aMapID);
            setNBT(aStack, tNBT);
        }

        public static short getMapID(ItemStack aStack) {
            NBTTagCompound tNBT = getNBT(aStack);
            if (!tNBT.hasKey("map_id")) return -1;
            return tNBT.getShort("map_id");
        }

        public static void setBookTitle(ItemStack aStack, String aTitle) {
            NBTTagCompound tNBT = getNBT(aStack);
            tNBT.setString("title", aTitle);
            setNBT(aStack, tNBT);
        }

        public static String getBookTitle(ItemStack aStack) {
            NBTTagCompound tNBT = getNBT(aStack);
            return tNBT.getString("title");
        }

        public static void setBookAuthor(ItemStack aStack, String aAuthor) {
            NBTTagCompound tNBT = getNBT(aStack);
            tNBT.setString("author", aAuthor);
            setNBT(aStack, tNBT);
        }

        public static String getBookAuthor(ItemStack aStack) {
            NBTTagCompound tNBT = getNBT(aStack);
            return tNBT.getString("author");
        }

        public static void setProspectionData(ItemStack aStack, int aX, int aY, int aZ, int aDim, FluidStack aFluid,
            String... aOres) {
            NBTTagCompound tNBT = getNBT(aStack);
            StringBuilder tData = new StringBuilder(aX + "," + aY + "," + aZ + "," + aDim + ",");
            if (aFluid != null) tData.append(aFluid.amount)
                .append(",")
                .append(aFluid.getLocalizedName())
                .append(","); // TODO
            // CHECK
            // IF
            // THAT
            // /5000
            // is
            // needed
            // (Not
            // needed)
            for (String tString : aOres) {
                tData.append(tString)
                    .append(",");
            }
            tNBT.setString("prospection", tData.toString());
            setNBT(aStack, tNBT);
        }

        public static void setAdvancedProspectionData(byte aTier, ItemStack aStack, int aX, short aY, int aZ, int aDim,
            ArrayList<String> aOils, ArrayList<String> aOres, int aRadius) {

            setBookTitle(aStack, "Raw Prospection Data");

            NBTTagCompound tNBT = getNBT(aStack);

            tNBT.setByte("prospection_tier", aTier);
            tNBT.setString("prospection_pos", "Dim: " + aDim + "\nX: " + aX + " Y: " + aY + " Z: " + aZ);

            // ores
            Collections.sort(aOres);
            tNBT.setString("prospection_ores", joinListToString(aOres));

            // oils
            ArrayList<String> tOilsTransformed = new ArrayList<>(aOils.size());
            for (String aStr : aOils) {
                String[] aStats = aStr.split(",");
                tOilsTransformed.add(aStats[0] + ": " + aStats[1] + "L " + aStats[2]);
            }

            tNBT.setString("prospection_oils", joinListToString(tOilsTransformed));

            String tOilsPosStr = "X: " + Math.floorDiv(aX, 16 * 8) * 16 * 8
                + " Z: "
                + Math.floorDiv(aZ, 16 * 8) * 16 * 8
                + "\n";
            int xOff = aX - Math.floorDiv(aX, 16 * 8) * 16 * 8;
            xOff = xOff / 16;
            int xOffRemain = 7 - xOff;

            int zOff = aZ - Math.floorDiv(aZ, 16 * 8) * 16 * 8;
            zOff = zOff / 16;
            int zOffRemain = 7 - zOff;

            for (; zOff > 0; zOff--) {
                tOilsPosStr = tOilsPosStr.concat("--------\n");
            }
            for (; xOff > 0; xOff--) {
                tOilsPosStr = tOilsPosStr.concat("-");
            }

            tOilsPosStr = tOilsPosStr.concat("P");

            for (; xOffRemain > 0; xOffRemain--) {
                tOilsPosStr = tOilsPosStr.concat("-");
            }
            tOilsPosStr = tOilsPosStr.concat("\n");
            for (; zOffRemain > 0; zOffRemain--) {
                tOilsPosStr = tOilsPosStr.concat("--------\n");
            }
            tOilsPosStr = tOilsPosStr.concat(
                "            X: " + (Math.floorDiv(aX, 16 * 8) + 1) * 16 * 8
                    + " Z: "
                    + (Math.floorDiv(aZ, 16 * 8) + 1) * 16 * 8); // +1 oilfied to find bottomright of [5]

            tNBT.setString("prospection_oils_pos", tOilsPosStr);

            tNBT.setString("prospection_radius", String.valueOf(aRadius));

            setNBT(aStack, tNBT);
        }

        public static void convertProspectionData(ItemStack aStack) {
            NBTTagCompound tNBT = getNBT(aStack);
            byte tTier = tNBT.getByte("prospection_tier");

            if (tTier == 0) { // basic prospection data
                String tData = tNBT.getString("prospection");
                String[] tDataArray = tData.split(",");
                if (tDataArray.length > 6) {
                    tNBT.setString(
                        "author",
                        " Dim: " + tDataArray[3]
                            + "X: "
                            + tDataArray[0]
                            + " Y: "
                            + tDataArray[1]
                            + " Z: "
                            + tDataArray[2]);
                    NBTTagList tNBTList = new NBTTagList();
                    StringBuilder tOres = new StringBuilder(" Prospected Ores: ");
                    for (int i = 6; tDataArray.length > i; i++) {
                        tOres.append(tDataArray[i])
                            .append(" ");
                    }
                    tNBTList.appendTag(
                        new NBTTagString(
                            "Tier " + tTier
                                + " Prospecting Data From: X"
                                + tDataArray[0]
                                + " Z:"
                                + tDataArray[2]
                                + " Dim:"
                                + tDataArray[3]
                                + " Produces "
                                + tDataArray[4]
                                + "L "
                                + tDataArray[5]
                                + " "
                                + tOres));
                    tNBT.setTag("pages", tNBTList);
                }
            } else { // advanced prospection data
                String tPos = tNBT.getString("prospection_pos");
                String tRadius = tNBT.getString("prospection_radius");

                String tOresStr = tNBT.getString("prospection_ores");
                String tOilsStr = tNBT.getString("prospection_oils");
                String tOilsPosStr = tNBT.getString("prospection_oils_pos");

                String[] tOres = tOresStr.isEmpty() ? null : tOresStr.split("\\|");
                String[] tOils = tOilsStr.isEmpty() ? null : tOilsStr.split("\\|");

                NBTTagList tNBTList = new NBTTagList();

                String tPageText = "Prospector report\n" + tPos
                    + "\n\n"
                    + "Oils: "
                    + (tOils != null ? tOils.length : 0)
                    + "\n\n"
                    + "Ores within "
                    + tRadius
                    + " blocks\n\n"
                    + "Location is center of orevein\n\n"
                    + "Check NEI to confirm orevein type";
                tNBTList.appendTag(new NBTTagString(tPageText));

                if (tOres != null) fillBookWithList(tNBTList, "Ores Found %s\n\n", "\n", 7, tOres);

                if (tOils != null) fillBookWithList(tNBTList, "Oils%s\n\n", "\n", 9, tOils);

                tPageText = """
                    Oil notes

                    Prospects from NW to SE 576 chunks(9 8x8 oilfields)
                     around and gives min-max amount

                    [1][2][3]
                    [4][5][6]
                    [7][8][9]

                    [5] - Prospector in this 8x8 area""";
                tNBTList.appendTag(new NBTTagString(tPageText));

                tPageText = "Corners of [5] are \n" + tOilsPosStr + "\n" + "P - Prospector in 8x8 field";
                tNBTList.appendTag(new NBTTagString(tPageText));

                tNBT.setString("author", tPos.replace("\n", " "));
                tNBT.setTag("pages", tNBTList);
            }
            setNBT(aStack, tNBT);
        }

        public static void fillBookWithList(NBTTagList aBook, String aPageHeader, String aListDelimiter,
            int aItemsPerPage, String[] list) {
            String aPageFormatter = " %d/%d";
            int tTotalPages = list.length / aItemsPerPage + (list.length % aItemsPerPage > 0 ? 1 : 0);
            int tPage = 0;
            StringBuilder tPageText;
            do {
                tPageText = new StringBuilder();
                for (int i = tPage * aItemsPerPage; i < (tPage + 1) * aItemsPerPage && i < list.length; i += 1)
                    tPageText.append((tPageText.length() == 0) ? "" : aListDelimiter)
                        .append(list[i]);

                if (tPageText.length() > 0) {
                    String tPageCounter = tTotalPages > 1 ? String.format(aPageFormatter, tPage + 1, tTotalPages) : "";
                    NBTTagString tPageTag = new NBTTagString(String.format(aPageHeader, tPageCounter) + tPageText);
                    aBook.appendTag(tPageTag);
                }

                ++tPage;
            } while (tPageText.length() > 0);
        }

        public static void addEnchantment(ItemStack aStack, Enchantment aEnchantment, int aLevel) {
            NBTTagCompound tNBT = getNBT(aStack), tEnchantmentTag;
            if (!tNBT.hasKey("ench", 9)) tNBT.setTag("ench", new NBTTagList());
            NBTTagList tList = tNBT.getTagList("ench", 10);

            boolean temp = true;

            for (int i = 0; i < tList.tagCount(); i++) {
                tEnchantmentTag = tList.getCompoundTagAt(i);
                if (tEnchantmentTag.getShort("id") == aEnchantment.effectId) {
                    tEnchantmentTag.setShort("id", (short) aEnchantment.effectId);
                    tEnchantmentTag.setShort("lvl", (byte) aLevel);
                    temp = false;
                    break;
                }
            }

            if (temp) {
                tEnchantmentTag = new NBTTagCompound();
                tEnchantmentTag.setShort("id", (short) aEnchantment.effectId);
                tEnchantmentTag.setShort("lvl", (byte) aLevel);
                tList.appendTag(tEnchantmentTag);
            }
            aStack.setTagCompound(tNBT);
        }
    }

    /**
     * THIS IS BULLSHIT!!! WHY DO I HAVE TO DO THIS SHIT JUST TO HAVE ENCHANTS PROPERLY!?!
     */
    public static class GT_EnchantmentHelper {

        private static final BullshitIteratorA mBullshitIteratorA = new BullshitIteratorA();
        private static final BullshitIteratorB mBullshitIteratorB = new BullshitIteratorB();

        private static void applyBullshit(IBullshit aBullshitModifier, ItemStack aStack) {
            if (aStack != null) {
                NBTTagList nbttaglist = aStack.getEnchantmentTagList();
                if (nbttaglist != null) {
                    try {
                        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                            short short1 = nbttaglist.getCompoundTagAt(i)
                                .getShort("id");
                            short short2 = nbttaglist.getCompoundTagAt(i)
                                .getShort("lvl");
                            if (Enchantment.enchantmentsList[short1] != null)
                                aBullshitModifier.calculateModifier(Enchantment.enchantmentsList[short1], short2);
                        }
                    } catch (Throwable e) {
                        /**/
                    }
                }
            }
        }

        private static void applyArrayOfBullshit(IBullshit aBullshitModifier, ItemStack[] aStacks) {
            for (ItemStack itemstack : aStacks) {
                applyBullshit(aBullshitModifier, itemstack);
            }
        }

        public static void applyBullshitA(EntityLivingBase aPlayer, Entity aEntity, ItemStack aStack) {
            mBullshitIteratorA.mPlayer = aPlayer;
            mBullshitIteratorA.mEntity = aEntity;
            if (aPlayer != null) applyArrayOfBullshit(mBullshitIteratorA, aPlayer.getLastActiveItems());
            if (aStack != null) applyBullshit(mBullshitIteratorA, aStack);
        }

        public static void applyBullshitB(EntityLivingBase aPlayer, Entity aEntity, ItemStack aStack) {
            mBullshitIteratorB.mPlayer = aPlayer;
            mBullshitIteratorB.mEntity = aEntity;
            if (aPlayer != null) applyArrayOfBullshit(mBullshitIteratorB, aPlayer.getLastActiveItems());
            if (aStack != null) applyBullshit(mBullshitIteratorB, aStack);
        }

        interface IBullshit {

            void calculateModifier(Enchantment aEnchantment, int aLevel);
        }

        static final class BullshitIteratorA implements IBullshit {

            public EntityLivingBase mPlayer;
            public Entity mEntity;

            BullshitIteratorA() {}

            @Override
            public void calculateModifier(Enchantment aEnchantment, int aLevel) {
                aEnchantment.func_151367_b(mPlayer, mEntity, aLevel);
            }
        }

        static final class BullshitIteratorB implements IBullshit {

            public EntityLivingBase mPlayer;
            public Entity mEntity;

            BullshitIteratorB() {}

            @Override
            public void calculateModifier(Enchantment aEnchantment, int aLevel) {
                aEnchantment.func_151368_a(mPlayer, mEntity, aLevel);
            }
        }
    }

    public static String toSubscript(long no) {
        char[] chars = Long.toString(no)
            .toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] += 8272;
        }
        return new String(chars);
    }

    public static boolean isPartOfMaterials(ItemStack aStack, Materials aMaterials) {
        return GT_OreDictUnificator.getAssociation(aStack) != null
            && GT_OreDictUnificator.getAssociation(aStack).mMaterial.mMaterial.equals(aMaterials);
    }

    public static boolean isPartOfOrePrefix(ItemStack aStack, OrePrefixes aPrefix) {
        return GT_OreDictUnificator.getAssociation(aStack) != null
            && GT_OreDictUnificator.getAssociation(aStack).mPrefix.equals(aPrefix);
    }

    public static final ImmutableSet<String> ORE_BLOCK_CLASSES = ImmutableSet.of(
        "com.github.bartimaeusnek.bartworks.system.material.BW_MetaGenerated_Ores",
        "com.github.bartimaeusnek.bartworks.system.material.BW_MetaGenerated_SmallOres",
        "gtPlusPlus.core.block.base.BlockBaseOre");

    public static boolean isOre(Block aBlock, int aMeta) {
        return (aBlock instanceof GT_Block_Ores_Abstract) || isOre(new ItemStack(aBlock, 1, aMeta))
            || ORE_BLOCK_CLASSES.contains(
                aBlock.getClass()
                    .getName());
    }

    public static boolean isOre(ItemStack aStack) {
        int tItem = GT_Utility.stackToInt(aStack);
        if (sOreTable.containsKey(tItem)) {
            return sOreTable.get(tItem);
        }
        for (int id : OreDictionary.getOreIDs(aStack)) {
            if (OreDictionary.getOreName(id)
                .startsWith("ore")) {
                sOreTable.put(tItem, true);
                return true;
            }
        }
        sOreTable.put(tItem, false);
        return false;
    }

    /**
     * Do <b>NOT</b> mutate the returned {@code ItemStack}! We return {@code ItemStack} instead of {@code Block} so that
     * we can include metadata.
     */
    public static ItemStack getCobbleForOre(Block ore, short metaData) {
        // We need to convert small ores to regular ores because small ores don't have associated ItemData.
        // We take the modulus of the metadata by 16000 because that is the magic number to convert small ores to
        // regular ores.
        // See: GT_TileEntity_Ores.java
        ItemData association = GT_OreDictUnificator
            .getAssociation(new ItemStack(Item.getItemFromBlock(ore), 1, metaData % 16000));
        if (association != null) {
            Supplier<ItemStack> supplier = sOreToCobble.get(association.mPrefix);
            if (supplier != null) {
                return supplier.get();
            }
        }
        return new ItemStack(Blocks.cobblestone);
    }

    public static Optional<GT_Recipe> reverseShapelessRecipe(ItemStack output, Object... aRecipe) {
        if (output == null) {
            return Optional.empty();
        }

        List<ItemStack> inputs = new ArrayList<>();

        for (Object o : aRecipe) {
            if (o instanceof ItemStack) {
                ItemStack toAdd = ((ItemStack) o).copy();
                inputs.add(toAdd);
            } else if (o instanceof String) {
                ItemStack stack = GT_OreDictUnificator.get(o, 1);
                if (stack == null) {
                    Optional<ItemStack> oStack = OreDictionary.getOres((String) o)
                        .stream()
                        .findAny();
                    if (oStack.isPresent()) {
                        ItemStack copy = oStack.get()
                            .copy();
                        inputs.add(copy);
                    }
                } else {
                    ItemStack copy = stack.copy();
                    inputs.add(copy);
                }
            } else if (o instanceof Item) inputs.add(new ItemStack((Item) o));
            else if (o instanceof Block) inputs.add(new ItemStack((Block) o));
            else throw new IllegalStateException("A Recipe contains an invalid input! Output: " + output);
        }

        inputs.removeIf(x -> x.getItem() instanceof GT_MetaGenerated_Tool);

        return Optional.of(
            new GT_Recipe(
                false,
                new ItemStack[] { output },
                inputs.toArray(new ItemStack[0]),
                null,
                null,
                null,
                null,
                300,
                30,
                0));
    }

    public static Optional<GT_Recipe> reverseShapedRecipe(ItemStack output, Object... aRecipe) {
        if (output == null) {
            return Optional.empty();
        }

        Map<Object, Integer> recipeAsMap = new HashMap<>();
        Map<Character, Object> ingridients = new HashMap<>();
        Map<Character, Integer> amounts = new HashMap<>();
        boolean startFound = false;
        for (int i = 0, aRecipeLength = aRecipe.length; i < aRecipeLength; i++) {
            Object o = aRecipe[i];
            if (!startFound) {
                if (o instanceof String) {
                    for (Character c : ((String) o).toCharArray()) amounts.merge(c, 1, (a, b) -> ++a);
                } else if (o instanceof Character) startFound = true;
            } else if (!(o instanceof Character)) ingridients.put((Character) aRecipe[i - 1], o);
        }
        for (Map.Entry<Character, Object> characterObjectEntry : ingridients.entrySet()) {
            for (Map.Entry<Character, Integer> characterIntegerEntry : amounts.entrySet()) {
                if (characterObjectEntry.getKey() != characterIntegerEntry.getKey()) continue;
                recipeAsMap.put(characterObjectEntry.getValue(), characterIntegerEntry.getValue());
            }
        }
        List<ItemStack> inputs = new ArrayList<>();

        for (Map.Entry<Object, Integer> o : recipeAsMap.entrySet()) {
            final int amount = o.getValue();
            if (o.getKey() instanceof ItemStack) {
                ItemStack toAdd = ((ItemStack) o.getKey()).copy();
                toAdd.stackSize = amount;
                inputs.add(toAdd);
            } else if (o.getKey() instanceof String dictName) {
                // Do not register tools dictName in inputs
                if (ToolDictNames.contains(dictName)) continue;
                ItemStack stack = GT_OreDictUnificator.get(dictName, null, amount, false, true);
                if (stack == null) {
                    Optional<ItemStack> oStack = OreDictionary.getOres(dictName)
                        .stream()
                        .findAny();
                    if (oStack.isPresent()) {
                        ItemStack copy = oStack.get()
                            .copy();
                        copy.stackSize = amount;
                        inputs.add(copy);
                    }
                } else {
                    ItemStack copy = stack.copy();
                    copy.stackSize = amount;
                    inputs.add(copy);
                }
            } else if (o.getKey() instanceof Item) inputs.add(new ItemStack((Item) o.getKey(), amount));
            else if (o.getKey() instanceof Block) inputs.add(new ItemStack((Block) o.getKey(), amount));
            else throw new IllegalStateException("A Recipe contains an invalid input! Output: " + output);
        }

        // Remove tools from inputs in case a recipe has one as a direct Item or ItemStack reference
        inputs.removeIf(x -> x.getItem() instanceof GT_MetaGenerated_Tool);

        return Optional.of(
            new GT_Recipe(
                false,
                new ItemStack[] { output },
                inputs.toArray(new ItemStack[0]),
                null,
                null,
                null,
                null,
                300,
                30,
                0));
    }

    /**
     * Add an itemstack to player inventory, or drop on ground if full. Can be called on client but it probably won't
     * work very well.
     */
    public static void addItemToPlayerInventory(EntityPlayer aPlayer, ItemStack aStack) {
        if (isStackInvalid(aStack)) return;
        if (!aPlayer.inventory.addItemStackToInventory(aStack) && !aPlayer.worldObj.isRemote) {
            EntityItem dropItem = aPlayer.entityDropItem(aStack, 0);
            dropItem.delayBeforeCanPickup = 0;
        }
    }

    public static long getNonnullElementCount(Object[] tArray) {
        return Arrays.stream(tArray)
            .filter(Objects::nonNull)
            .count();
    }

    public static int clamp(int val, int lo, int hi) {
        return MathHelper.clamp_int(val, lo, hi);
    }

    public static int ceilDiv(int lhs, int rhs) {
        return (lhs + rhs - 1) / rhs;
    }

    public static long ceilDiv(long lhs, long rhs) {
        return (lhs + rhs - 1) / rhs;
    }

    /**
     * Hash an item stack for the purpose of storing hash across launches
     */
    public static int persistentHash(ItemStack aStack, boolean aUseStackSize, boolean aUseNBT) {
        if (aStack == null) return 0;
        int result = Objects.hashCode(GameRegistry.findUniqueIdentifierFor(aStack.getItem()));
        result = result * 31 + Items.feather.getDamage(aStack);

        if (aUseStackSize) result = result * 31 + aStack.stackSize;
        if (aUseNBT) result = result * 31 + Objects.hashCode(aStack.stackTagCompound);
        return result;
    }

    /**
     * Hash an item stack for the purpose of storing hash across launches
     */
    public static int persistentHash(FluidStack aStack, boolean aUseStackSize, boolean aUseNBT) {
        if (aStack == null) return 0;
        int base = Objects.hashCode(
            aStack.getFluid()
                .getName());

        if (aUseStackSize) base = base * 31 + aStack.amount;
        if (aUseNBT) base = base * 31 + Objects.hashCode(aStack.tag);
        return base;
    }

    public static int getCasingTextureIndex(Block block, int meta) {
        if (block instanceof IHasIndexedTexture) return ((IHasIndexedTexture) block).getTextureIndex(meta);
        return Textures.BlockIcons.ERROR_TEXTURE_INDEX;
    }

    public static boolean isCellEmpty(ItemStack itemStack) {
        if (itemStack == null) return false;
        ItemStack tStack = ItemList.Cell_Empty.get(1);
        tStack.stackSize = itemStack.stackSize;
        return GT_Utility.areStacksEqual(itemStack, tStack);
    }

    /**
     * Convert a cell to fluid. If given itemstack does not contain any fluid, return null. Will correctly multiple
     * output fluid amount if input stack size is greater than 1.
     */
    public static FluidStack convertCellToFluid(ItemStack itemStack) {
        if (itemStack == null) return null;
        if (getFluidForFilledItem(itemStack, true) != null) {
            FluidStack fluidStack = getFluidForFilledItem(itemStack, true);
            if (fluidStack != null) fluidStack.amount = fluidStack.amount * itemStack.stackSize;
            return fluidStack;
        }
        return null;
    }

    /**
     * @deprecated typo in method name. use {@link #isAnyIntegratedCircuit(ItemStack)} instead.
     */
    @Deprecated
    public static boolean checkIfSameIntegratedCircuit(ItemStack itemStack) {
        if (itemStack == null) return false;
        for (int i = 0; i < 25; i++) if (itemStack.isItemEqual(GT_Utility.getIntegratedCircuit(i))) return true;
        return false;
    }

    public static boolean isAnyIntegratedCircuit(ItemStack itemStack) {
        if (itemStack == null) return false;
        return itemStack.getItem() == ItemList.Circuit_Integrated.getItem() && 0 <= itemStack.getItemDamage()
            && itemStack.getItemDamage() < 25;
    }

    public static byte convertRatioToRedstone(long used, long max, int threshold, boolean inverted) {
        byte signal;
        if (used <= 0) { // Empty
            signal = 0;
        } else if (used >= max) { // Full
            signal = 15;
        } else { // Range 1-14
            signal = (byte) (1 + (14 * used) / max);
        }

        if (inverted) {
            signal = (byte) (15 - signal);
        }

        if (threshold > 0) {
            if (inverted && used >= threshold) {
                return 0;
            } else if (!inverted && used < threshold) {
                return 0;
            }
        }

        return signal;
    }

    public static ItemStack getNaniteAsCatalyst(Materials material) {
        ItemStack aItem = material.getNanite(1);
        return new ItemStack(aItem.getItem(), 0, aItem.getItemDamage());
    }

    public static Stream<NBTTagCompound> streamCompounds(NBTTagList list) {
        if (list == null) return Stream.empty();
        return IntStream.range(0, list.tagCount())
            .mapToObj(list::getCompoundTagAt);
    }

    public static boolean equals(ItemStack[] a, ItemStack[] b) {
        // because stupid mojang didn't override equals for us
        if (a == null && b == null) return true;
        if ((a == null) != (b == null)) return false;
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) {
            if (!areStacksEqual(a[i], b[i], false)) return false;
        }
        return true;
    }

    /**
     * Guava ImmutableMap variant of Collectors.toMap. Optimized for serial streams.
     */
    public static <T, K, U> Collector<T, ?, ImmutableMap<K, U>> toImmutableMapSerial(
        Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper) {
        // petty type inference cannot work out the correct type parameter
        return Collector.<T, ImmutableMap.Builder<K, U>, ImmutableMap<K, U>>of(
            ImmutableMap::builder,
            (b, t) -> b.put(keyMapper.apply(t), valueMapper.apply(t)),
            (b1, b2) -> b1.putAll(b2.build()),
            ImmutableMap.Builder::build);
    }

    public static boolean isArrayEmptyOrNull(Object[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean isArrayOfLength(Object[] arr, int expectedLength) {
        return arr != null && arr.length == expectedLength;
    }

    @SafeVarargs
    public static <E> Collection<E> concat(Collection<E>... colls) {
        return concat(Arrays.asList(colls));
    }

    public static <E> Collection<E> concat(Collection<Collection<E>> colls) {
        return new ConcatCollection<>(colls);
    }

    private static class ConcatCollection<E> extends AbstractCollection<E> {

        private final Collection<Collection<E>> colls;
        private final int size;

        public ConcatCollection(Collection<Collection<E>> lists) {
            Collection<Collection<E>> colls1 = null;
            for (Collection<E> list : lists) {
                if (list == null || list.isEmpty()) {
                    colls1 = lists.stream()
                        .filter(c -> c != null && !c.isEmpty())
                        .collect(Collectors.toList());
                    break;
                }
            }
            if (colls1 == null) colls1 = lists;
            colls = colls1;
            int sum = 0;
            for (Collection<E> list : colls) {
                sum += list.size();
            }
            size = sum;
        }

        @Nonnull
        @Override
        public Iterator<E> iterator() {
            return colls.stream()
                .flatMap(Collection::stream)
                .iterator();
        }

        @Override
        public int size() {
            return size;
        }
    }

    @AutoValue
    public abstract static class ItemId {

        public static ItemId create(NBTTagCompound tag) {
            return new AutoValue_GT_Utility_ItemId(
                Item.getItemById(tag.getShort("item")),
                tag.getShort("meta"),
                tag.hasKey("tag", Constants.NBT.TAG_COMPOUND) ? tag.getCompoundTag("tag") : null);
        }

        /**
         * This method copies NBT, as it is mutable.
         */
        public static ItemId create(ItemStack itemStack) {
            NBTTagCompound nbt = itemStack.getTagCompound();
            if (nbt != null) {
                nbt = (NBTTagCompound) nbt.copy();
            }

            return new AutoValue_GT_Utility_ItemId(itemStack.getItem(), Items.feather.getDamage(itemStack), nbt);
        }

        /**
         * This method copies NBT, as it is mutable.
         */
        public static ItemId create(Item item, int metaData, @Nullable NBTTagCompound nbt) {
            if (nbt != null) {
                nbt = (NBTTagCompound) nbt.copy();
            }
            return new AutoValue_GT_Utility_ItemId(item, metaData, nbt);
        }

        /**
         * This method stores metadata as wildcard and NBT as null.
         */
        public static ItemId createAsWildcard(ItemStack itemStack) {
            return new AutoValue_GT_Utility_ItemId(itemStack.getItem(), W, null);
        }

        /**
         * This method stores NBT as null.
         */
        public static ItemId createWithoutNBT(ItemStack itemStack) {
            return new AutoValue_GT_Utility_ItemId(itemStack.getItem(), Items.feather.getDamage(itemStack), null);
        }

        /**
         * This method does not copy NBT in order to save time. Make sure not to mutate it!
         */
        public static ItemId createNoCopy(ItemStack itemStack) {
            return new AutoValue_GT_Utility_ItemId(
                itemStack.getItem(),
                Items.feather.getDamage(itemStack),
                itemStack.getTagCompound());
        }

        /**
         * This method does not copy NBT in order to save time. Make sure not to mutate it!
         */
        public static ItemId createNoCopy(Item item, int metaData, @Nullable NBTTagCompound nbt) {
            return new AutoValue_GT_Utility_ItemId(item, metaData, nbt);
        }

        protected abstract Item item();

        protected abstract int metaData();

        @Nullable
        protected abstract NBTTagCompound nbt();

        public NBTTagCompound writeToNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setShort("item", (short) Item.getIdFromItem(item()));
            tag.setShort("meta", (short) metaData());
            if (nbt() != null) tag.setTag("tag", nbt());
            return tag;
        }

        public ItemStack getItemStack() {
            ItemStack itemStack = new ItemStack(item(), 1, metaData());
            itemStack.setTagCompound(nbt());
            return itemStack;
        }
    }

    public static int getPlasmaFuelValueInEUPerLiterFromMaterial(Materials material) {
        return getPlasmaFuelValueInEUPerLiterFromFluid(material.getPlasma(1));
    }

    public static int getPlasmaFuelValueInEUPerLiterFromFluid(FluidStack aLiquid) {
        if (aLiquid == null) return 0;
        GT_Recipe tFuel = RecipeMaps.plasmaFuels.getBackend()
            .findFuel(aLiquid);
        if (tFuel != null) return tFuel.mSpecialValue;
        return 0;
    }

    public static MovingObjectPosition getPlayerLookingTarget() {
        // Basically copied from waila, thanks Caedis for such challenge
        Minecraft mc = Minecraft.getMinecraft();
        EntityLivingBase viewpoint = mc.renderViewEntity;
        if (viewpoint == null) return null;

        float reachDistance = mc.playerController.getBlockReachDistance();
        Vec3 posVec = viewpoint.getPosition(0);
        Vec3 lookVec = viewpoint.getLook(0);
        Vec3 modifiedPosVec = posVec
            .addVector(lookVec.xCoord * reachDistance, lookVec.yCoord * reachDistance, lookVec.zCoord * reachDistance);

        return viewpoint.worldObj.rayTraceBlocks(posVec, modifiedPosVec);
    }

    public static ForgeDirection getSideFromPlayerFacing(Entity player) {
        if (player == null) return ForgeDirection.UNKNOWN;
        if (player.rotationPitch >= 65) return ForgeDirection.UP;
        if (player.rotationPitch <= -65) return ForgeDirection.DOWN;
        final byte facing = COMPASS_DIRECTIONS[MathHelper.floor_double(0.5D + 4.0F * player.rotationYaw / 360.0F)
            & 0x3];
        return ForgeDirection.getOrientation(facing);
    }
}
