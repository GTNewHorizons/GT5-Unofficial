package gregtech.api.util;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.GTMod.GT_FML_LOGGER;
import static gregtech.api.enums.GTValues.COMPASS_DIRECTIONS;
import static gregtech.api.enums.GTValues.D1;
import static gregtech.api.enums.GTValues.E;
import static gregtech.api.enums.GTValues.M;
import static gregtech.api.enums.GTValues.NW;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.Materials.FLUID_MAP;
import static gregtech.api.enums.Mods.Translocator;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.WILDCARD;
import static gregtech.common.UndergroundOil.undergroundOilReadInformation;
import static net.minecraft.util.StatCollector.translateToLocal;
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
import java.util.AbstractList;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IllegalFormatException;
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
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
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
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
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

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.Contract;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3i;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.alignment.IAlignmentProvider;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.alignment.enumerable.Flip;
import com.gtnewhorizon.structurelib.alignment.enumerable.Rotation;
import com.mojang.authlib.GameProfile;

import buildcraft.api.transport.IPipeTile;
import codechicken.translocator.TileItemTranslocator;
import cofh.api.transport.IItemDuct;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ModAPIManager;
import cpw.mods.fml.common.registry.GameRegistry;
import forestry.api.arboriculture.ITree;
import forestry.arboriculture.tiles.TileLeaves;
import fox.spiteful.avaritia.items.ItemMatterCluster;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.damagesources.GTDamageSources;
import gregtech.api.damagesources.GTDamageSources.DamageSourceHotItem;
import gregtech.api.enchants.EnchantmentRadioactivity;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.Textures;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.events.BlockScanningEvent;
import gregtech.api.hazards.HazardProtection;
import gregtech.api.interfaces.IBlockContainer;
import gregtech.api.interfaces.IDebugableBlock;
import gregtech.api.interfaces.IHasIndexedTexture;
import gregtech.api.interfaces.IProjectileItem;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.item.ItemStackSizeCalculator;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IBasicEnergyContainer;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.interfaces.tileentity.IUpgradableMachine;
import gregtech.api.items.GTGenericItem;
import gregtech.api.items.ItemEnergyArmor;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.net.GTPacketSound;
import gregtech.api.objects.CollectorUtils;
import gregtech.api.objects.GTItemStack;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.XSTR;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.threads.RunnableSound;
import gregtech.common.items.ItemIntegratedCircuit;
import gregtech.common.ores.OreManager;
import gregtech.common.pollution.Pollution;
import ic2.api.crops.ICropTile;
import ic2.api.energy.tile.IEnergyConductor;
import ic2.api.reactor.IReactor;
import ic2.api.recipe.ICannerBottleRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.RecipeOutput;
import ic2.api.tile.IEnergyStorage;
import ic2.api.tile.IWrenchable;
import ic2.core.IC2Potion;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIntPair;
import it.unimi.dsi.fastutil.objects.Reference2LongOpenHashMap;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * Just a few Utility Functions I use.
 */
public class GTUtility {

    /**
     * Formats a number with group separator and at most 2 fraction digits.
     */
    private static final Map<Locale, DecimalFormat> decimalFormatters = new HashMap<>();

    /*
     * Forge screwed the Fluid Registry up again, so I make my own, which is also much more efficient than the stupid
     * Stuff over there.
     */

    /**
     * All catched fluid container data.
     */
    private static final List<FluidContainerData> sFluidContainerList = new ArrayList<>();

    /**
     * Associates the filled container item with the fluid container data.
     */
    private static final Map<GTItemStack, FluidContainerData> sFilledContainerToData = new /* Concurrent */ HashMap<>();

    /**
     * Associates the empty container item with a map mapping fluid names to the associated fluid container data.
     */
    private static final Map<GTItemStack, Map<String, FluidContainerData>> sEmptyContainerToFluidToData = new HashMap<>();

    /**
     * Associates the name of the fluid with all filled container items.
     */
    private static final Map<String, List<ItemStack>> sFluidToContainers = new HashMap<>();

    private static final Map<Integer, Boolean> sOreTable = new HashMap<>();
    public static boolean TE_CHECK = false, BC_CHECK = false, CHECK_ALL = true, RF_CHECK = false;
    public static Map<GTPlayedSound, Integer> sPlayedSoundMap = new /* Concurrent */ HashMap<>();
    private static int sBookCount = 0;
    public static UUID defaultUuid = null; // maybe default non-null?
    // UUID.fromString("00000000-0000-0000-0000-000000000000");

    public static Map<GTItemStack, FluidContainerData> getFilledContainerToData() {
        return sFilledContainerToData;
    }

    public static int safeInt(long number, int margin) {
        return number > Integer.MAX_VALUE - margin ? Integer.MAX_VALUE - margin : (int) number;
    }

    public static int safeInt(long number) {
        return number > V[V.length - 1] ? safeInt(V[V.length - 1], 1)
            : number < Integer.MIN_VALUE ? Integer.MIN_VALUE : (int) number;
    }

    public static int longToInt(long number) {
        return (int) Math.min(Integer.MAX_VALUE, number);
    }

    public static Field getField(Object aObject, String aField) {
        Field rField = null;
        try {
            rField = aObject.getClass()
                .getDeclaredField(aField);
            rField.setAccessible(true);
        } catch (Exception e) {
            if (D1) e.printStackTrace(GTLog.err);
        }
        return rField;
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        try {
            final Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (Exception e) {
            if (D1) e.printStackTrace(GTLog.err);
        }
        return null;
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
        } catch (Exception e) {
            if (aLogErrors) e.printStackTrace(GTLog.err);
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
        } catch (Exception e) {
            if (aLogErrors) e.printStackTrace(GTLog.err);
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
        } catch (Exception e) {
            if (aLogErrors) e.printStackTrace(GTLog.err);
        }
        return null;
    }

    public static Object callConstructor(Class<?> aClass, int aConstructorIndex, Object aReplacementObject,
        boolean aLogErrors, Object... aParameters) {
        if (aConstructorIndex < 0) {
            try {
                for (Constructor<?> tConstructor : aClass.getConstructors()) {
                    try {
                        return tConstructor.newInstance(aParameters);
                    } catch (Exception e) {
                        if (D1) e.printStackTrace(GTLog.err);
                    }
                }
            } catch (Exception e) {
                if (aLogErrors) e.printStackTrace(GTLog.err);
            }
        } else {
            try {
                return aClass.getConstructors()[aConstructorIndex].newInstance(aParameters);
            } catch (Exception e) {
                if (aLogErrors) e.printStackTrace(GTLog.err);
            }
        }
        return aReplacementObject;
    }

    public static <T> T getFieldValue(Class<?> clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            // noinspection unchecked
            return (T) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static String capitalizeString(String s) {
        if (s != null && !s.isEmpty()) {
            return s.substring(0, 1)
                .toUpperCase() + s.substring(1);
        }
        return "";
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

    public static boolean getFullInvisibility(EntityPlayer player) {
        if (!player.isInvisible()) return false;
        for (ItemStack stack : player.inventory.armorInventory) {
            if (stack == null || !(stack.getItem() instanceof ItemEnergyArmor)) continue;
            if ((((ItemEnergyArmor) stack.getItem()).mSpecials & 512) != 0) {
                if (GTModHandler.canUseElectricItem(stack, 10000)) {
                    return true;
                }
            }
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

    /**
     * Gets the voltage tier corresponding to an amount of EU, capped to 15 (MAX+)
     *
     * @param l The amount of EU
     * @return Corresponding voltage tier in the range 0-15
     */
    public static byte getTier(long l) {
        if (l > V[14]) return 15;
        if (l <= V[0]) return 0;

        // numberOfLeadingZeros is implemented in hardware by x86 LZCNT
        // and is extremely efficient (takes only a couple of hardware cycles)
        // (64 - numberOfLeadingZeros(l - 1)) = ceil(log_2(l))
        int log2L = 64 - Long.numberOfLeadingZeros(l - 1);

        return (byte) ((log2L - 2) / 2);
    }

    /**
     * Gets the voltage tier corresponding to an amount of EU
     *
     * @param l The amount of EU
     * @return Corresponding voltage tier
     */
    public static int getTierExtended(long l) {
        if (l <= V[0]) return 0;
        int log2L = 64 - Long.numberOfLeadingZeros(l - 1);
        return ((log2L - 2) / 2);
    }

    public static long getAmperageForTier(long voltage, byte tier) {
        return ceilDiv(voltage, GTValues.V[tier]);
    }

    /**
     * Rounds up partial voltage that exceeds tiered voltage, e.g. 4,096 -> 8,192(IV)
     */
    public static long roundUpVoltage(long voltage) {
        if (voltage > V[V.length - 1]) {
            return voltage;
        }
        return V[GTUtility.getTier(voltage)];
    }

    public static String getColoredTierNameFromVoltage(long voltage) {
        return getColoredTierNameFromTier(getTier(voltage));
    }

    public static String getColoredTierNameFromTier(byte tier) {
        return GTValues.TIER_COLORS[tier] + GTValues.VN[tier] + EnumChatFormatting.RESET;
    }

    /**
     * @return e.g. {@code " (LV)"}
     */
    @Nonnull
    public static String getTierNameWithParentheses(long voltage) {
        byte tier = getTier(voltage);
        tier = tier < 1 ? 1 : tier;
        String color = GTValues.TIER_COLORS[tier];
        return "(" + color + GTValues.VN[tier] + EnumChatFormatting.RESET + ")";
    }

    /**
     * @deprecated Use {@link #sendChatTrans} instead.
     */
    @Deprecated
    public static void sendChatToPlayer(EntityPlayer player, String message) {
        if (message != null) {
            message = processFormatStacks(message);
            player.addChatComponentMessage(new ChatComponentText(message));
        }
    }

    /**
     * Send a translated chat message to the player.
     *
     * @param player     The player who will receive the message.
     * @param messageKey The lang key of the translation. The text corresponding to the key must only contain
     *                   placeholder '%s'; otherwise, it cannot be translated.
     * @param args       Substitutions for `%s` in the translation. `IChatComponent` will be handled properly, others
     *                   will be converted to String
     */
    public static void sendChatTrans(EntityPlayer player, @Nonnull String messageKey, Object... args) {
        // FIXME：
        // should have a better translation component to:
        // 1. process format stacks;
        // 2. accept placeholders other than '%s', at least positional ones like '%1$s'
        player.addChatComponentMessage(new ChatComponentTranslation(messageKey, args));
    }

    /**
     * Send a chat component to the player.
     * We use this method to ensure future compatibility.
     * When we have a better translation component, we can modify the chat component sent to the player through this
     * function.
     *
     * @param player    The player who will receive the message.
     * @param component The chat component to send.
     */
    public static void sendChatComp(EntityPlayer player, @Nonnull IChatComponent component) {
        player.addChatComponentMessage(component);
    }

    /**
     * Send a message to all players on the server
     */
    public static void sendServerMessage(String message) {
        sendServerMessage(new ChatComponentText(message));
    }

    /**
     * Send a message to all players on the server
     */
    public static void sendServerMessage(IChatComponent chatComponent) {
        MinecraftServer.getServer()
            .getConfigurationManager()
            .sendChatMsg(chatComponent);
    }

    /** Uses thread analysis, works on dedicated servers. */
    public static boolean isServer() {
        return FMLCommonHandler.instance()
            .getEffectiveSide()
            .isServer();
    }

    /** Uses thread analysis, works on dedicated servers. */
    public static boolean isClient() {
        return FMLCommonHandler.instance()
            .getEffectiveSide()
            .isClient();
    }

    private static final char FORMAT_ESCAPE = '§';
    public static final String FORMAT_PUSH_STACK = FORMAT_ESCAPE + "s";
    public static final String FORMAT_POP_STACK = FORMAT_ESCAPE + "t";

    /**
     * Pre-processes a localized chat message with custom format push/pop instructions. This allows nested localizations
     * to set their own format without clobbering whatever came before, without needing any extra context info.
     * <p>
     * </p>
     * Example: {@code §a§lHello §s§eWorld§t!} is transformed into {@code §a§lHello §eWorld§a§l!}
     */
    public static String processFormatStacks(String message) {
        // Short circuit if there aren't any pops, because pops mutate while pushes don't.
        // Invalid codes are ignored by the font renderer so it won't cause problems if we ignore erroneous pushes.
        if (!message.contains(FORMAT_POP_STACK)) return message;

        StringBuilder out = new StringBuilder();
        out.ensureCapacity(message.length() * 6 / 5);

        int len = message.length();

        ArrayDeque<String> stack = new ArrayDeque<>();
        String currentFormat = "";

        int start = 0;

        while (start < len) {
            // Find the next format escape char
            int end = message.indexOf(FORMAT_ESCAPE, start);

            // If we hit the end of the string, push the rest of the input and stop
            if (end == -1) {
                out.append(message, start, len);
                break;
            }

            // If there was any text from the end of the previous code to the start of this one, add it to the output
            // buffer
            if (end > start) {
                out.append(message, start, end);
            }

            // If the current format escape char has a code after it, check it
            if (end < len - 1) {
                char code = message.charAt(end + 1);

                if (code >= '0' && code <= '9' || code >= 'a' && code <= 'f') {
                    // Colours, use as-is and erase the previous format
                    currentFormat = "" + FORMAT_ESCAPE + code;
                    out.append(FORMAT_ESCAPE)
                        .append(code);
                } else if (code >= 'k' && code <= 'o') {
                    // Styles, use as-is and append to the colour style (note: this may act up with repeated style
                    // codes but it shouldn't break anything).
                    currentFormat = currentFormat + FORMAT_ESCAPE + code;
                    out.append(FORMAT_ESCAPE)
                        .append(code);
                } else if (code == 'r') {
                    // Reset, use as-is and clear the format
                    currentFormat = "";
                    out.append(FORMAT_ESCAPE)
                        .append(code);
                } else if (code == 's') {
                    // Push, save the current format and don't emit to the output buffer
                    stack.push(currentFormat);
                } else if (code == 't') {
                    // Pop, restore the top format and don't emit to the output buffer
                    currentFormat = stack.isEmpty() ? "" : stack.pop();
                    out.append(currentFormat);
                }

                // Skip the format escape along with its code
                start = end + 2;
            }
        }

        return out.toString();
    }

    public static void checkAvailabilities() {
        if (CHECK_ALL) {
            TE_CHECK = ModAPIManager.INSTANCE.hasAPI("CoFHAPI|transport");
            BC_CHECK = ModAPIManager.INSTANCE.hasAPI("BuildCraftAPI|transport");
            RF_CHECK = ModAPIManager.INSTANCE.hasAPI("CoFHAPI|energy");
            CHECK_ALL = false;
        }
    }

    public static boolean isConnectableNonInventoryPipe(TileEntity tileEntity, ForgeDirection side) {
        if (tileEntity == null) return false;
        checkAvailabilities();
        if (TE_CHECK && tileEntity instanceof IItemDuct) return true;
        if (BC_CHECK && tileEntity instanceof IPipeTile pipeTile) return pipeTile.isPipeConnected(side);
        return Translocator.isModLoaded() && tileEntity instanceof TileItemTranslocator;
    }

    public static List<ItemStack> wrapInventory(IInventory inv) {
        int sizeInventory = inv.getSizeInventory();

        return new AbstractList<>() {

            @Override
            public ItemStack get(int index) {
                return inv.getStackInSlot(index);
            }

            @Override
            public ItemStack set(int index, ItemStack element) {
                ItemStack existing = inv.getStackInSlot(index);
                inv.setInventorySlotContents(index, element);
                return existing;
            }

            @Override
            public int size() {
                return sizeInventory;
            }
        };
    }

    public static void compactStandardInventory(IInventory inv) {
        inv.markDirty();

        ItemStackSizeCalculator stackSizes = (slot, stack) -> Math
            .min(inv.getInventoryStackLimit(), stack == null ? 64 : stack.getMaxStackSize());

        compactInventory(wrapInventory(inv), stackSizes);
    }

    public static void compactInventory(IMetaTileEntity imte) {
        compactInventory(imte, 0, imte.getSizeInventory());
    }

    public static boolean compactInventory(IMetaTileEntity imte, int start, int end) {
        ItemStackSizeCalculator stackSizes = (slot, stack) -> imte.getStackSizeLimit(slot + start, stack);

        if (compactInventory(wrapInventory(imte).subList(start, end), stackSizes)) {
            imte.markDirty();
            return true;
        } else {
            return false;
        }
    }

    public static boolean compactInventory(List<ItemStack> inv, ItemStackSizeCalculator stackSizes) {
        int len = inv.size();

        // Filter each ItemStack into their own lists (grouped by Item, meta, and NBT).
        Map<ItemStack, ObjectArrayList<ObjectIntPair<ItemStack>>> slots = new Object2ObjectOpenCustomHashMap<>(
            GTItemStack.ITEMSTACK_HASH_STRATEGY_NBT_SENSITIVE);

        for (int i = 0; i < len; i++) {
            ItemStack stack = inv.get(i);

            if (stack == null) continue;

            slots.computeIfAbsent(stack, ignored -> new ObjectArrayList<>())
                .add(ObjectIntPair.of(stack, i));
        }

        MutableBoolean didSomething = new MutableBoolean(false);

        // For each ItemStack, merge stacks from the end of the list to the front
        slots.forEach((ignored, stacks) -> {
            int stackLen = stacks.size();

            int insert = 0;
            int extract = stackLen - 1;

            while (insert < stackLen && insert < extract) {
                // Grab the next stack from the front of the list, to insert into if possible
                var toInflate = stacks.get(insert);
                ItemStack inflateStack = toInflate.left();

                int maxStack = stackSizes.getSlotStackLimit(toInflate.rightInt(), inflateStack);
                int remaining = maxStack - inflateStack.stackSize;

                // Scan from the end of the list to the current stack, and try to move items from those stacks into the
                // current stack
                while (insert < extract) {
                    var toBeExtracted = stacks.get(extract);

                    int toTransfer = Math.min(toBeExtracted.left().stackSize, remaining);

                    toBeExtracted.left().stackSize -= toTransfer;
                    inflateStack.stackSize += toTransfer;
                    remaining -= toTransfer;

                    didSomething.setValue(true);

                    if (toBeExtracted.left().stackSize <= 0) {
                        inv.set(toBeExtracted.rightInt(), null);
                        extract--;
                    }

                    if (inflateStack.stackSize >= maxStack) {
                        break;
                    }
                }

                insert++;
            }
        });

        int insert = 0;

        // Put all stacks into the first slots, contiguously
        while (insert < len) {
            if (inv.get(insert) == null) {
                ItemStack stack = null;

                int extract = insert + 1;

                while (extract < len && (stack = inv.get(extract)) == null) {
                    extract++;
                }

                if (stack != null) {
                    inv.set(insert, stack);
                    inv.set(extract, null);
                    didSomething.setValue(true);
                } else {
                    break;
                }
            }

            insert++;
        }

        return didSomething.getValue();
    }

    public static void swapSlots(IInventory inv, int a, int b) {
        ItemStack stackA = inv.getStackInSlot(a);
        ItemStack stackB = inv.getStackInSlot(b);

        inv.setInventorySlotContents(a, stackB);
        inv.setInventorySlotContents(b, stackA);

        inv.markDirty();
    }

    public static boolean cleanInventory(IInventory inv) {
        if (cleanInventory(wrapInventory(inv))) {
            inv.markDirty();
            return true;
        } else {
            return false;
        }
    }

    public static boolean cleanInventory(List<ItemStack> inv) {
        boolean didSomething = false;

        for (int i = 0, invSize = inv.size(); i < invSize; i++) {
            ItemStack stack = inv.get(i);

            if (stack != null && (stack.getItem() == null || stack.stackSize <= 0)) {
                inv.set(i, null);
                didSomething = true;
            }
        }

        return didSomething;
    }

    /**
     * Drops the item to the world.
     * <p>
     * NOTE: the stack is directly passed to the entity, so you should not continue using it.
     *
     * @param world    the world to spawn the item
     * @param x        the x coord to spawn the item
     * @param y        the y coord to spawn the item
     * @param z        the z coord to spawn the item
     * @param stack    the stack to spawn
     * @param noMotion {@code true} to remove the initial motion when spawned to the world
     */
    public static void dropItem(World world, double x, double y, double z, @Nullable ItemStack stack,
        boolean noMotion) {
        if (isStackInvalid(stack)) return;
        EntityItem entity = new EntityItem(world, x, y, z, stack);
        if (noMotion) {
            // remove initial motion
            entity.motionX = 0;
            entity.motionY = 0;
            entity.motionZ = 0;
        }
        world.spawnEntityInWorld(entity);
    }

    /**
     * @see #dropItem(World, double, double, double, ItemStack, boolean)
     */
    public static void dropItem(World world, double x, double y, double z, @Nullable ItemStack stack) {
        dropItem(world, x, y, z, stack, false);
    }

    /**
     * Drops the item to the world at the block pos.
     * <p>
     * NOTE: the stack is directly passed to the entity, so you should not continue using it.
     *
     * @param world         the world to spawn the item
     * @param x             the x coord to spawn the item
     * @param y             the y coord to spawn the item
     * @param z             the z coord to spawn the item
     * @param stack         the stack to spawn
     * @param positionShift {@code true} to add a small random shift to the position
     * @param noMotion      {@code true} to remove the initial motion when spawned to the world
     */
    public static void dropItemToBlockPos(World world, int x, int y, int z, @Nullable ItemStack stack,
        boolean positionShift, boolean noMotion) {
        if (isStackInvalid(stack)) return;
        double x1, y1, z1;
        if (positionShift) {
            // shift the position to drop, but always inside the block pos
            // equivalent to x = x + (0.1..0.9).random()
            x1 = x + XSTR.XSTR_INSTANCE.nextDouble() * 0.8 + 0.1;
            y1 = y + XSTR.XSTR_INSTANCE.nextDouble() * 0.8 + 0.1;
            z1 = z + XSTR.XSTR_INSTANCE.nextDouble() * 0.8 + 0.1;
        } else {
            x1 = x + 0.5;
            y1 = y + 0.5;
            z1 = z + 0.5;
        }
        dropItem(world, x1, y1, z1, stack, noMotion);
    }

    /**
     * @see #dropItemToBlockPos(World, int, int, int, ItemStack, boolean, boolean)
     */
    public static void dropItemToBlockPos(World world, int x, int y, int z, @Nullable ItemStack stack) {
        dropItemToBlockPos(world, x, y, z, stack, false, false);
    }

    public static void dropItemsOrClusters(World world, float x, float y, float z, List<ItemStack> stacks) {
        if (Mods.AvaritiaAddons.isModLoaded()) {
            dropMatterClusters(world, x, y, z, stacks);
        } else {
            for (ItemStack stack : stacks) {
                int maxStack = stack.getMaxStackSize();

                while (stack.stackSize > 0) {
                    int inStack = Math.min(stack.stackSize, maxStack);
                    stack.stackSize -= inStack;

                    EntityItem item = new EntityItem(world, x, y, z, GTUtility.copyAmountUnsafe(inStack, stack));

                    item.motionX = 0;
                    item.motionY = 0;
                    item.motionZ = 0;

                    world.spawnEntityInWorld(item);
                }
            }
        }
    }

    @cpw.mods.fml.common.Optional.Method(modid = Mods.ModIDs.AVARITIA)
    public static void dropMatterClusters(World world, float x, float y, float z, List<ItemStack> stacks) {
        for (ItemStack cluster : ItemMatterCluster.makeClusters(stacks)) {
            EntityItem item = new EntityItem(world, x, y, z, cluster);

            item.motionX = 0;
            item.motionY = 0;
            item.motionZ = 0;

            world.spawnEntityInWorld(item);
        }
    }

    /**
     * Move up to maxAmount amount of fluid from source to dest, with optional filtering via allowMove. note that this
     * filter cannot bypass filtering done by IFluidHandlers themselves.
     * <p>
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
                    || Items.feather.getDamage(aStack1) == WILDCARD
                    || Items.feather.getDamage(aStack2) == WILDCARD);
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
            && (Items.feather.getDamage(aStack1) == Items.feather.getDamage(aStack2)
                || Items.feather.getDamage(aStack1) == WILDCARD
                || Items.feather.getDamage(aStack2) == WILDCARD)
            && (aIgnoreNBT || (((aStack1.getTagCompound() == null) == (aStack2.getTagCompound() == null))
                && (aStack1.getTagCompound() == null || aStack1.getTagCompound()
                    .equals(aStack2.getTagCompound()))));
    }

    public static boolean areStacksEqualOrNull(ItemStack stack1, ItemStack stack2) {
        return (stack1 == null && stack2 == null) || GTUtility.areStacksEqual(stack1, stack2);
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
            GTOreDictUnificator.get_nocopy(aStack1),
            GTOreDictUnificator.get_nocopy(aStack2),
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
        for (FluidContainerData data : sFluidContainerList) {
            String fluidName = data.fluid.getFluid()
                .getName();
            sFilledContainerToData.put(new GTItemStack(data.filledContainer), data);
            sEmptyContainerToFluidToData.computeIfAbsent(new GTItemStack(data.emptyContainer), $ -> new HashMap<>())
                .put(fluidName, data);
            sFluidToContainers.computeIfAbsent(fluidName, $ -> new ArrayList<>())
                .add(data.filledContainer);
        }
    }

    public static void addFluidContainerData(FluidContainerData data) {
        String fluidName = data.fluid.getFluid()
            .getName();
        sFluidContainerList.add(data);
        sFilledContainerToData.put(new GTItemStack(data.filledContainer), data);
        sEmptyContainerToFluidToData.computeIfAbsent(new GTItemStack(data.emptyContainer), $ -> new HashMap<>())
            .put(fluidName, data);
        sFluidToContainers.computeIfAbsent(fluidName, $ -> new ArrayList<>())
            .add(data.filledContainer);
    }

    public static boolean isEmptyFluidContainer(ItemStack itemStack) {
        return sEmptyContainerToFluidToData.containsKey(new GTItemStack(itemStack));
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
        if (GTModHandler.isWater(aFluid) && ItemList.Bottle_Empty.isStackEqual(aStack)) {
            if (aFluid.amount >= 1000) {
                return new ItemStack(Items.potionitem, 1, 0);
            }
            return null;
        }
        if (aCheckIFluidContainerItems && aStack.getItem() instanceof IFluidContainerItem fluidContainerItem
            && fluidContainerItem.getFluid(aStack) == null
            && fluidContainerItem.getCapacity(aStack) > 0
            && fluidContainerItem.getCapacity(aStack) <= aFluid.amount) {
            if (aRemoveFluidDirectly) {
                aFluid.amount -= fluidContainerItem.fill(aStack = copyAmount(1, aStack), aFluid, true);
            } else {
                fluidContainerItem.fill(aStack = copyAmount(1, aStack), aFluid, true);
            }
            return aStack;
        }
        Map<String, FluidContainerData> tFluidToContainer = sEmptyContainerToFluidToData.get(new GTItemStack(aStack));
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
            e.printStackTrace();
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
        FluidContainerData tData = sFilledContainerToData.get(new GTItemStack(aStack));
        return tData != null && tData.fluid.isFluidEqual(aFluid);
    }

    public static FluidStack getFluidForFilledItem(ItemStack aStack, boolean aCheckIFluidContainerItems) {
        if (isStackInvalid(aStack)) return null;
        if (aCheckIFluidContainerItems && aStack.getItem() instanceof IFluidContainerItem
            && ((IFluidContainerItem) aStack.getItem()).getCapacity(aStack) > 0)
            return ((IFluidContainerItem) aStack.getItem()).drain(copyAmount(1, aStack), Integer.MAX_VALUE, false);
        FluidContainerData tData = sFilledContainerToData.get(new GTItemStack(aStack));
        return tData == null ? null : tData.fluid.copy();
    }

    /**
     * Get empty fluid container from filled one.
     */
    public static ItemStack getContainerForFilledItem(ItemStack aStack, boolean aCheckIFluidContainerItems) {
        if (isStackInvalid(aStack)) return null;
        FluidContainerData tData = sFilledContainerToData.get(new GTItemStack(aStack));
        if (tData != null) return copyAmount(1, tData.emptyContainer);
        if (aCheckIFluidContainerItems && aStack.getItem() instanceof IFluidContainerItem
            && ((IFluidContainerItem) aStack.getItem()).getCapacity(aStack) > 0) {
            ((IFluidContainerItem) aStack.getItem()).drain(aStack = copyAmount(1, aStack), Integer.MAX_VALUE, true);
            return aStack;
        }
        return null;
    }

    /**
     * This is NOT meant for fluid manipulation! It's for getting item container, which is generally used for crafting
     * recipes. While it also works for many of the fluid containers, some don't.
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

        int tCapsuleCount = GTModHandler.getCapsuleCellContainerCount(aStack);
        if (tCapsuleCount > 0) return ItemList.Cell_Empty.get(tCapsuleCount);

        if (ItemList.IC2_ForgeHammer.isStackEqual(aStack) || ItemList.IC2_WireCutter.isStackEqual(aStack))
            return copyMetaData(Items.feather.getDamage(aStack) + 1, aStack);
        return null;
    }

    public static FluidStack getFluidFromContainerOrFluidDisplay(ItemStack stack) {
        FluidStack fluidStack = GTUtility.getFluidForFilledItem(stack, true);
        if (fluidStack == null) {
            fluidStack = GTUtility.getFluidFromDisplayStack(stack);
        }
        return fluidStack;
    }

    public static Object2LongOpenHashMap<ItemId> getItemStackHistogram(Iterable<ItemStack> stacks) {
        Object2LongOpenHashMap<ItemId> histogram = new Object2LongOpenHashMap<>();

        if (stacks == null) return histogram;

        for (ItemStack stack : stacks) {
            if (stack == null || stack.getItem() == null) continue;
            histogram.addTo(ItemId.create(stack), stack.stackSize);
        }

        return histogram;
    }

    public static Iterable<NBTTagCompound> getCompoundTagList(NBTTagCompound tag, String name) {
        NBTTagList list = tag.getTagList(name, Constants.NBT.TAG_COMPOUND);

        return list.tagList;
    }

    public static synchronized boolean removeIC2BottleRecipe(ItemStack aContainer, ItemStack aInput,
        Map<ICannerBottleRecipeManager.Input, RecipeOutput> aRecipeList, ItemStack aOutput) {
        if ((isStackInvalid(aInput) && isStackInvalid(aOutput) && isStackInvalid(aContainer)) || aRecipeList == null)
            return false;
        boolean rReturn = false;
        Iterator<Map.Entry<ICannerBottleRecipeManager.Input, RecipeOutput>> tIterator = aRecipeList.entrySet()
            .iterator();
        aOutput = GTOreDictUnificator.get(aOutput);
        while (tIterator.hasNext()) {
            Map.Entry<ICannerBottleRecipeManager.Input, RecipeOutput> tEntry = tIterator.next();
            if (aInput == null || tEntry.getKey()
                .matches(aContainer, aInput)) {
                List<ItemStack> tList = tEntry.getValue().items;
                if (tList != null) for (ItemStack tOutput : tList)
                    if (aOutput == null || areStacksEqual(GTOreDictUnificator.get(tOutput), aOutput)) {
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
        aOutput = GTOreDictUnificator.get(aOutput);
        while (tIterator.hasNext()) {
            Map.Entry<IRecipeInput, RecipeOutput> tEntry = tIterator.next();
            if (aInput == null || tEntry.getKey()
                .matches(aInput)) {
                List<ItemStack> tList = tEntry.getValue().items;
                if (tList != null) for (ItemStack tOutput : tList)
                    if (aOutput == null || areStacksEqual(GTOreDictUnificator.get(tOutput), aOutput)) {
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
        final Map<ItemStack, ItemStack> finalToRemove = Maps.transformValues(toRemove, GTOreDictUnificator::get_nocopy);

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
                                    || areStacksEqual(GTOreDictUnificator.get(tOutput), aOutput)));
                    }));
    }

    public static boolean addSimpleIC2MachineRecipe(ItemStack aInput, Map<IRecipeInput, RecipeOutput> aRecipeList,
        NBTTagCompound aNBT, Object... aOutput) {
        if (isStackInvalid(aInput) || aOutput.length == 0 || aRecipeList == null) return false;
        ItemData tOreName = GTOreDictUnificator.getAssociation(aInput);
        for (Object o : aOutput) {
            if (o == null) {
                GT_FML_LOGGER.info("EmptyIC2Output!" + aInput.getUnlocalizedName());
                return false;
            }
        }
        ItemStack[] tStack = GTOreDictUnificator.getStackArray(true, aOutput);
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
        ItemStack rStack = GregTechAPI.sBookList.get(aMapping);
        if (rStack == null) return aStackToPutNBT;
        if (aStackToPutNBT != null) {
            aStackToPutNBT.setTagCompound(rStack.getTagCompound());
            return aStackToPutNBT;
        }
        return copyAmount(1, rStack);
    }

    public static ItemStack getWrittenBook(String aMapping, String aTitle, String aAuthor, String... aPages) {
        if (isStringInvalid(aMapping)) return null;
        ItemStack rStack = GregTechAPI.sBookList.get(aMapping);
        if (rStack != null) return copyAmount(1, rStack);
        if (isStringInvalid(aTitle) || isStringInvalid(aAuthor) || aPages.length == 0) return null;
        sBookCount++;
        rStack = new ItemStack(Items.written_book, 1);
        NBTTagCompound tNBT = new NBTTagCompound();
        tNBT.setString("title", GTLanguageManager.addStringLocalization("Book." + aTitle + ".Name", aTitle));
        tNBT.setString("author", aAuthor);
        NBTTagList tNBTList = new NBTTagList();
        for (byte i = 0; i < aPages.length; i++) {
            aPages[i] = GTLanguageManager
                .addStringLocalization("Book." + aTitle + ".Page" + ((i < 10) ? "0" + i : i), aPages[i]);
            if (i < 48) {
                if (aPages[i].length() < 256) tNBTList.appendTag(new NBTTagString(aPages[i]));
                else GTLog.err.println("WARNING: String for written Book too long! -> " + aPages[i]);
            } else {
                GTLog.err.println("WARNING: Too much Pages for written Book! -> " + aTitle);
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
        GTLog.out.println(
            "GTMod: Added Book to Book List  -  Mapping: '" + aMapping
                + "'  -  Name: '"
                + aTitle
                + "'  -  Author: '"
                + aAuthor
                + "'");
        GregTechAPI.sBookList.put(aMapping, rStack);
        return copyOrNull(rStack);
    }

    public static boolean doSoundAtClient(String aSoundName, int aTimeUntilNextSound, float aSoundStrength) {
        if (aSoundName == null) return false;
        return doSoundAtClient(aSoundName, aTimeUntilNextSound, aSoundStrength, GTMod.GT.getThePlayer());
    }

    public static boolean doSoundAtClient(SoundResource sound, int aTimeUntilNextSound, float aSoundStrength) {
        return doSoundAtClient(sound.resourceLocation, aTimeUntilNextSound, aSoundStrength, GTMod.GT.getThePlayer());
    }

    public static boolean doSoundAtClient(ResourceLocation aSoundResourceLocation, int aTimeUntilNextSound,
        float aSoundStrength) {
        return doSoundAtClient(aSoundResourceLocation, aTimeUntilNextSound, aSoundStrength, GTMod.GT.getThePlayer());
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
            .isClient() || GTMod.GT.getThePlayer() == null || !GTMod.GT.getThePlayer().worldObj.isRemote) return false;
        if (GregTechAPI.sMultiThreadedSounds) new Thread(
            new RunnableSound(
                GTMod.GT.getThePlayer().worldObj,
                aX,
                aY,
                aZ,
                aTimeUntilNextSound,
                aSoundResourceLocation,
                aSoundStrength,
                aSoundModulation),
            "Sound Effect").start();
        else new RunnableSound(
            GTMod.GT.getThePlayer().worldObj,
            aX,
            aY,
            aZ,
            aTimeUntilNextSound,
            aSoundResourceLocation,
            aSoundStrength,
            aSoundModulation).run();
        return true;
    }

    public static boolean sendSoundToPlayers(World aWorld, String aSoundName, float aSoundStrength,
        float aSoundModulation, double aX, double aY, double aZ) {
        if (isStringInvalid(aSoundName) || aWorld == null || aWorld.isRemote) return false;
        NW.sendPacketToAllPlayersInRange(
            aWorld,
            new GTPacketSound(aSoundName, aSoundStrength, aSoundModulation, aX, aY, aZ),
            MathHelper.floor_double(aX),
            MathHelper.floor_double(aZ));
        return true;
    }

    public static boolean sendSoundToPlayers(World aWorld, SoundResource sound, float aSoundStrength,
        float aSoundModulation, double aX, double aY, double aZ) {
        if (aWorld == null || aWorld.isRemote) return false;
        NW.sendPacketToAllPlayersInRange(
            aWorld,
            new GTPacketSound(sound.resourceLocation.toString(), aSoundStrength, aSoundModulation, aX, aY, aZ),
            MathHelper.floor_double(aX),
            MathHelper.floor_double(aZ));
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
        return Item.getIdFromItem(aStack.getItem()) | (WILDCARD << 16);
    }

    public static ItemStack intToStack(int aStack) {
        int tID = aStack & (~0 >>> 16), tMeta = aStack >>> 16;
        Item tItem = Item.getItemById(tID);
        if (tItem != null) return new ItemStack(tItem, 1, tMeta);
        return null;
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

    public static <A, B> int indexOf(A[] array, B value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) return i;
        }

        return -1;
    }

    public static <A, B> boolean contains(A[] array, B value) {
        return indexOf(array, value) != -1;
    }

    public static <T> T getIndexSafe(T[] array, int index) {
        return index < 0 || index >= array.length ? null : array[index];
    }

    public static <T> T getIndexSafe(List<T> list, int index) {
        return index < 0 || index >= list.size() ? null : list.get(index);
    }

    public static Block getBlockFromStack(ItemStack itemStack) {
        if (isStackInvalid(itemStack)) return Blocks.air;
        return getBlockFromItem(itemStack.getItem());
    }

    public static Block getBlockFromItem(Item item) {
        return Block.getBlockFromItem(item);
    }

    public static boolean isStringValid(Object aString) {
        return aString != null && !aString.toString()
            .isEmpty();
    }

    public static boolean isStringValid(String aString) {
        return aString != null && !aString.isEmpty();
    }

    public static boolean isStringInvalid(Object aString) {
        return aString == null || aString.toString()
            .isEmpty();
    }

    public static boolean isStringInvalid(String aString) {
        return aString == null || aString.isEmpty();
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
            GTModHandler.getIC2Item("debug", 1),
            aStack,
            true);
    }

    public static ItemStack updateItemStack(ItemStack aStack) {
        if (isStackValid(aStack) && aStack.getItem() instanceof GTGenericItem)
            ((GTGenericItem) aStack.getItem()).isItemStackUsable(aStack);
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
    public static int getTextureId(Block blockFromBlock, int metaFromBlock) {
        for (int page = 0; page < Textures.BlockIcons.casingTexturePages.length; page++) {
            ITexture[] casingTexturePage = Textures.BlockIcons.casingTexturePages[page];
            if (casingTexturePage != null) {
                for (int index = 0; index < casingTexturePage.length; index++) {
                    ITexture iTexture = casingTexturePage[index];
                    if (iTexture instanceof IBlockContainer) {
                        Block block = ((IBlockContainer) iTexture).getBlock();
                        int meta = ((IBlockContainer) iTexture).getMeta();
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

    public static float getHeatDamageFromItem(ItemStack aStack) {
        ItemData tData = GTOreDictUnificator.getItemData(aStack);
        return tData == null ? 0
            : (tData.mPrefix == null ? 0 : tData.mPrefix.mHeatDamage)
                + (tData.hasValidMaterialData() ? tData.mMaterial.mMaterial.mHeatDamage : 0);
    }

    public static int getRadioactivityLevel(ItemStack aStack) {
        ItemData tData = GTOreDictUnificator.getItemData(aStack);
        if (tData != null && tData.hasValidMaterialData()) {
            if (tData.mMaterial.mMaterial.mArmorEnchantment instanceof EnchantmentRadioactivity)
                return tData.mMaterial.mMaterial.mArmorEnchantmentLevel;
            if (tData.mMaterial.mMaterial.mToolEnchantment instanceof EnchantmentRadioactivity)
                return tData.mMaterial.mMaterial.mToolEnchantmentLevel;
        }
        return EnchantmentHelper.getEnchantmentLevel(EnchantmentRadioactivity.INSTANCE.effectId, aStack);
    }

    public static boolean applyHeatDamage(EntityLivingBase entity, float damage) {
        return applyHeatDamage(entity, damage, GTDamageSources.getHeatDamage());
    }

    public static boolean applyHeatDamageFromItem(EntityLivingBase entity, float damage, ItemStack item) {
        return applyHeatDamage(entity, damage, new DamageSourceHotItem(item));
    }

    private static boolean applyHeatDamage(EntityLivingBase aEntity, float aDamage, DamageSource source) {
        if (aDamage > 0 && aEntity != null
            && !aEntity.isImmuneToFire()
            && !HazardProtection.isWearingFullHeatHazmat(aEntity)) {
            try {
                return aEntity.attackEntityFrom(source, aDamage);
            } catch (Exception t) {
                GTMod.GT_FML_LOGGER.error("Error damaging entity", t);
            }
        }
        return false;
    }

    public static boolean applyFrostDamage(EntityLivingBase aEntity, float aDamage) {
        if (aDamage > 0 && aEntity != null && !HazardProtection.isWearingFullFrostHazmat(aEntity)) {
            return aEntity.attackEntityFrom(GTDamageSources.getFrostDamage(), aDamage);
        }
        return false;
    }

    public static boolean applyElectricityDamage(EntityLivingBase aEntity, long aVoltage, long aAmperage) {
        long aDamage = getTier(aVoltage) * aAmperage * 4;
        if (aDamage > 0 && aEntity != null && !HazardProtection.isWearingFullElectroHazmat(aEntity)) {
            return aEntity.attackEntityFrom(GTDamageSources.getElectricDamage(), aDamage);
        }
        return false;
    }

    public static boolean applyRadioactivity(EntityLivingBase aEntity, int aLevel, int aAmountOfItems) {
        if (aLevel > 0 && aEntity != null
            && aEntity.getCreatureAttribute() != EnumCreatureAttribute.UNDEAD
            && aEntity.getCreatureAttribute() != EnumCreatureAttribute.ARTHROPOD
            && !HazardProtection.isWearingFullRadioHazmat(aEntity)) {
            PotionEffect tEffect;
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
                    IC2Potion.radiation.id,
                    aLevel * 180 * aAmountOfItems + Math.max(
                        0,
                        ((tEffect = aEntity.getActivePotionEffect(IC2Potion.radiation)) == null ? 0
                            : tEffect.getDuration())),
                    Math.max(0, (5 * aLevel) / 7)));
            return true;
        }
        return false;
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

    @Contract("null -> null")
    public static ItemStack copyOrNull(ItemStack stack) {
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

    @Contract("_, null -> null")
    public static ItemStack copyAmount(int aAmount, ItemStack aStack) {
        ItemStack rStack = copy(aStack);
        if (isStackInvalid(rStack)) return null;
        if (aAmount > 64) aAmount = 64;
        else if (aAmount == -1) aAmount = 111;
        else if (aAmount < 0) aAmount = 0;
        rStack.stackSize = (byte) aAmount;
        return rStack;
    }

    @Contract("_, null -> null")
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
     * Unlike {@link #copyAmount(int, ItemStack)}, this method does not restrict stack size by 64.
     */
    public static ItemStack copyAmountUnsafe(int aAmount, ItemStack aStack) {
        ItemStack rStack = copy(aStack);
        if (isStackInvalid(rStack)) return null;
        else if (aAmount < 0) aAmount = 0;
        rStack.stackSize = aAmount;
        return rStack;
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
        ItemStack tRet = GTOreDictUnificator.get(true, tRawStack);
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

    public static NBTTagList saveItemList(List<ItemStack> stacks) {
        NBTTagList list = new NBTTagList();

        if (stacks != null) {
            for (ItemStack stack : stacks) {
                if (isStackInvalid(stack)) continue;

                list.appendTag(stack.writeToNBT(new NBTTagCompound()));
            }
        }

        return list;
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<ItemStack> loadItemList(NBTTagList list) {
        ArrayList<ItemStack> stacks = new ArrayList<>();

        for (NBTTagCompound tag : (List<NBTTagCompound>) list.tagList) {
            ItemStack stack = ItemStack.loadItemStackFromNBT(tag);

            if (isStackInvalid(stack)) continue;

            stacks.add(stack);
        }

        return stacks;
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

        return aSet.contains(GTItemStack.internalCopyStack(aStack, true));
    }

    public static boolean isStackInList(ItemStack aStack, Collection<GTItemStack> aList) {
        if (aStack == null) {
            return false;
        }
        return isStackInList(new GTItemStack(aStack), aList);
    }

    public static boolean isStackInList(@Nonnull GTItemStack aStack, @Nonnull Collection<GTItemStack> aList) {
        return aList.contains(aStack) || aList.contains(new GTItemStack(aStack.mItem, aStack.mStackSize, WILDCARD));
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
        return translateMaterialToAmount(aMaterialAmount, 1 * INGOTS, aRoundUp);
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
        if (aDimensionID <= 1 && aDimensionID >= -1 && !GregTechAPI.sDimensionalList.contains(aDimensionID))
            return true;
        return !GregTechAPI.sDimensionalList.contains(aDimensionID)
            && DimensionManager.isDimensionRegistered(aDimensionID);
    }

    private static final Int2ObjectOpenHashMap<String> DIMENSION_NAMES = new Int2ObjectOpenHashMap<>();

    public static String getDimensionName(int dimId) {
        if (!DIMENSION_NAMES.containsKey(dimId)) {
            WorldProvider p = DimensionManager.createProviderFor(dimId);

            if (p != null) {
                DIMENSION_NAMES.put(dimId, p.getDimensionName());
            } else {
                DIMENSION_NAMES.put(dimId, "[unknown dimension]");
            }
        }

        String name = DIMENSION_NAMES.get(dimId);

        String key = "gtnop.world." + name;

        return tryTranslate(key, name);
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

            if (tTileEntity instanceof ic2.api.reactor.IReactorChamber chamber) {
                rEUAmount += 500;
                // Redirect the rest of the scans to the reactor itself
                tTileEntity = (TileEntity) chamber.getReactor();
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
            EnumChatFormatting.STRIKETHROUGH + "-----"
                + EnumChatFormatting.RESET
                + " X: "
                + EnumChatFormatting.AQUA
                + formatNumber(aX)
                + EnumChatFormatting.RESET
                + " Y: "
                + EnumChatFormatting.AQUA
                + formatNumber(aY)
                + EnumChatFormatting.RESET
                + " Z: "
                + EnumChatFormatting.AQUA
                + formatNumber(aZ)
                + EnumChatFormatting.RESET
                + " D: "
                + EnumChatFormatting.AQUA
                + aWorld.provider.dimensionId
                + EnumChatFormatting.RESET
                + " "
                + EnumChatFormatting.STRIKETHROUGH
                + "-----");
        try {
            tList.add(
                GTUtility.trans("162", "Name: ") + EnumChatFormatting.BLUE
                    + ((tTileEntity instanceof IInventory inv) ? inv.getInventoryName() : tBlock.getUnlocalizedName())
                    + EnumChatFormatting.RESET
                    + GTUtility.trans("163", " MetaData: ")
                    + EnumChatFormatting.AQUA
                    + aWorld.getBlockMetadata(aX, aY, aZ)
                    + EnumChatFormatting.RESET);
            tList.add(
                GTUtility.trans("164", "Hardness: ") + EnumChatFormatting.YELLOW
                    + tBlock.getBlockHardness(aWorld, aX, aY, aZ)
                    + EnumChatFormatting.RESET
                    + GTUtility.trans("165", " Blast Resistance: ")
                    + EnumChatFormatting.YELLOW
                    + tBlock
                        .getExplosionResistance(aPlayer, aWorld, aX, aY, aZ, aPlayer.posX, aPlayer.posY, aPlayer.posZ)
                    + EnumChatFormatting.RESET);
            if (tBlock.isBeaconBase(aWorld, aX, aY, aZ, aX, aY + 1, aZ)) tList.add(
                EnumChatFormatting.GOLD + GTUtility.trans("166", "Is valid Beacon Pyramid Material")
                    + EnumChatFormatting.RESET);
        } catch (Exception e) {
            tList.add("§cAn exception was thrown while fetching this block's info.§r");
            if (D1) e.printStackTrace(GTLog.err);
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
                        GTUtility.trans("167", "Tank ") + i
                            + ": "
                            + EnumChatFormatting.GREEN
                            + formatNumber((tTanks[i].fluid == null ? 0 : tTanks[i].fluid.amount))
                            + EnumChatFormatting.RESET
                            + " L / "
                            + EnumChatFormatting.YELLOW
                            + formatNumber(tTanks[i].capacity)
                            + EnumChatFormatting.RESET
                            + " L "
                            + EnumChatFormatting.GOLD
                            + getFluidName(tTanks[i].fluid, true)
                            + EnumChatFormatting.RESET);
                }
            }
        } catch (Exception e) {
            tList.add("§cAn exception was thrown while fetching this tile's fluid tank info.§r");
            if (D1) e.printStackTrace(GTLog.err);
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
        } catch (Exception e) {
            tList.add("§cAn exception was thrown while fetching this block's debug info.§r");
            if (D1) e.printStackTrace(GTLog.err);
        }
        return rEUAmount;
    }

    private static void addPollutionInfo(ArrayList<String> tList, Chunk currentChunk) {
        if (Pollution.hasPollution(currentChunk)) {
            tList.add(
                GTUtility.trans("202", "Pollution in Chunk: ") + EnumChatFormatting.RED
                    + formatNumber(Pollution.getPollution(currentChunk))
                    + EnumChatFormatting.RESET
                    + GTUtility.trans("203", " gibbl"));
        } else {
            tList.add(
                EnumChatFormatting.GREEN + GTUtility.trans("204", "No Pollution in Chunk! HAYO!")
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
                    + formatNumber(tFluid.amount)
                    + EnumChatFormatting.RESET
                    + " L");
            else tList.add(
                EnumChatFormatting.GOLD + GTUtility.trans("201", "Nothing")
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
            if (Mods.Forestry.isModLoaded() && tTileEntity instanceof TileLeaves tileLeaves) {
                final ITree tree = tileLeaves.getTree();
                if (tree != null) {
                    rEUAmount += 1000;
                    if (!tree.isAnalyzed()) tree.analyze();
                    tree.addTooltip(tList);
                }
            }
        } catch (Exception e) {
            tList.add("§cAn exception was thrown while fetching this leaves' info.§r");
            if (D1) e.printStackTrace(GTLog.err);
        }
        return rEUAmount;
    }

    private static int addIC2CropInfo(ArrayList<String> tList, TileEntity tTileEntity) {
        int rEUAmount = 0;
        try {
            if (tTileEntity instanceof ICropTile crop) {
                rEUAmount += 1000;
                if (crop.getScanLevel() < 4) crop.setScanLevel((byte) 4);
                if (crop.getCrop() != null) {
                    tList.add(
                        GTUtility.trans("187", "Type -- Crop-Name: ") + crop.getCrop()
                            .name()
                            + GTUtility.trans("188", "  Growth: ")
                            + crop.getGrowth()
                            + GTUtility.trans("189", "  Gain: ")
                            + crop.getGain()
                            + GTUtility.trans("190", "  Resistance: ")
                            + crop.getResistance());
                }
                tList.add(
                    GTUtility.trans("191", "Plant -- Fertilizer: ") + crop.getNutrientStorage()
                        + GTUtility.trans("192", "  Water: ")
                        + crop.getHydrationStorage()
                        + GTUtility.trans("193", "  Weed-Ex: ")
                        + crop.getWeedExStorage()
                        + GTUtility.trans("194", "  Scan-Level: ")
                        + crop.getScanLevel());
                tList.add(
                    GTUtility.trans("195", "Environment -- Nutrients: ") + crop.getNutrients()
                        + GTUtility.trans("196", "  Humidity: ")
                        + crop.getHumidity()
                        + GTUtility.trans("197", "  Air-Quality: ")
                        + crop.getAirQuality());
                if (crop.getCrop() != null) {
                    final StringBuilder tStringB = new StringBuilder();
                    for (final String tAttribute : crop.getCrop()
                        .attributes()) {
                        tStringB.append(", ")
                            .append(tAttribute);
                    }
                    final String tString = tStringB.toString();
                    tList.add(GTUtility.trans("198", "Attributes:") + tString.replaceFirst(",", E));
                    tList.add(
                        GTUtility.trans("199", "Discovered by: ") + crop.getCrop()
                            .discoveredBy());
                }
            }
        } catch (Exception e) {
            tList.add("§cAn exception was thrown while fetching this crop's info.§r");
            if (D1) e.printStackTrace(GTLog.err);
        }
        return rEUAmount;
    }

    private static void addDeviceInfo(ArrayList<String> tList, TileEntity tTileEntity) {
        try {
            if (tTileEntity instanceof IGregTechDeviceInformation info && info.isGivingInformation()) {
                tList.addAll(Arrays.asList(info.getInfoData()));
            }
        } catch (Exception e) {
            tList.add("§cAn exception was thrown while fetching this device's info.§r");
            if (D1) e.printStackTrace(GTLog.err);
        }
    }

    private static void addOwnerInfo(ArrayList<String> tList, TileEntity tTileEntity) {
        try {
            if (tTileEntity instanceof IGregTechTileEntity gtTE) {
                tList.add(
                    GTUtility.trans("186", "Owned by: ") + EnumChatFormatting.BLUE
                        + gtTE.getOwnerName()
                        + EnumChatFormatting.RESET);
            }
        } catch (Exception e) {
            tList.add("§cAn exception was thrown while fetching this device's owner.§r");
            if (D1) e.printStackTrace(GTLog.err);
        }
    }

    private static void addEnergyContainerInfo(ArrayList<String> tList, TileEntity tTileEntity) {
        try {
            if (tTileEntity instanceof IBasicEnergyContainer energyContainer && energyContainer.getEUCapacity() > 0) {
                tList.add(
                    GTUtility.trans("179", "Max IN: ") + EnumChatFormatting.RED
                        + formatNumber(energyContainer.getInputVoltage())
                        + " ("
                        + GTValues.VN[getTier(energyContainer.getInputVoltage())]
                        + ") "
                        + EnumChatFormatting.RESET
                        + GTUtility.trans("182", " EU at ")
                        + EnumChatFormatting.RED
                        + formatNumber(energyContainer.getInputAmperage())
                        + EnumChatFormatting.RESET
                        + GTUtility.trans("183", " A"));
                tList.add(
                    GTUtility.trans("181", "Max OUT: ") + EnumChatFormatting.RED
                        + formatNumber(energyContainer.getOutputVoltage())
                        + " ("
                        + GTValues.VN[getTier(energyContainer.getOutputVoltage())]
                        + ") "
                        + EnumChatFormatting.RESET
                        + GTUtility.trans("182", " EU at ")
                        + EnumChatFormatting.RED
                        + formatNumber(energyContainer.getOutputAmperage())
                        + EnumChatFormatting.RESET
                        + GTUtility.trans("183", " A"));
                tList.add(
                    GTUtility.trans("184", "Energy: ") + EnumChatFormatting.GREEN
                        + formatNumber(energyContainer.getStoredEU())
                        + EnumChatFormatting.RESET
                        + " EU / "
                        + EnumChatFormatting.YELLOW
                        + formatNumber(energyContainer.getEUCapacity())
                        + EnumChatFormatting.RESET
                        + " EU");
            }
        } catch (Exception e) {
            tList.add("§cAn exception was thrown while fetching this device's energy info.§r");
            if (D1) e.printStackTrace(GTLog.err);
        }
    }

    private static int addCoverableInfo(ForgeDirection side, ArrayList<String> tList, TileEntity tTileEntity) {
        int rEUAmount = 0;
        try {
            if (tTileEntity instanceof ICoverable coverable) {
                rEUAmount += 300;
                final String tString = coverable.getCoverAtSide(side)
                    .getDescription();
                if (tString != null && !tString.equals(E)) tList.add(tString);
            }
        } catch (Exception e) {
            tList.add("§cAn exception was thrown while fetching this device's covers.§r");
            if (D1) e.printStackTrace(GTLog.err);
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
                    GTUtility.trans("178", "Progress/Load: ") + EnumChatFormatting.GREEN
                        + formatNumber(progress.getProgress())
                        + EnumChatFormatting.RESET
                        + " / "
                        + EnumChatFormatting.YELLOW
                        + formatNumber(tValue)
                        + EnumChatFormatting.RESET);
            }
        } catch (Exception e) {
            tList.add("§cAn exception was thrown while fetching this device's progress.§r");
            if (D1) e.printStackTrace(GTLog.err);
        }
        return rEUAmount;
    }

    private static int addUpgradableMachineInfo(ArrayList<String> tList, TileEntity tTileEntity) {
        int rEUAmount = 0;
        try {
            if (tTileEntity instanceof IUpgradableMachine upgradableMachine) {
                rEUAmount += 500;
                if (upgradableMachine.isMuffled()) tList
                    .add(EnumChatFormatting.GREEN + GTUtility.trans("177", "Is Muffled") + EnumChatFormatting.RESET);
            }
        } catch (Exception e) {
            tList.add("§cAn exception was thrown while fetching this device's upgrades.§r");
            if (D1) e.printStackTrace(GTLog.err);
        }
        return rEUAmount;
    }

    private static int addIC2EnergyStorageInfo(ArrayList<String> tList, TileEntity tTileEntity) {
        int rEUAmount = 0;
        try {
            if (tTileEntity instanceof IEnergyStorage storage) {
                rEUAmount += 200;
                tList.add(
                    GTUtility.trans("176", "Contained Energy: ") + EnumChatFormatting.YELLOW
                        + formatNumber(storage.getStored())
                        + EnumChatFormatting.RESET
                        + " EU / "
                        + EnumChatFormatting.YELLOW
                        + formatNumber(storage.getCapacity())
                        + EnumChatFormatting.RESET
                        + " EU");
            }
        } catch (Exception e) {
            tList.add("§cAn exception was thrown while fetching this device's IC2 energy info.§r");
            if (D1) e.printStackTrace(GTLog.err);
        }
        return rEUAmount;
    }

    private static int addIC2EnergyConductorInfo(ArrayList<String> tList, TileEntity tTileEntity) {
        int rEUAmount = 0;
        try {
            if (tTileEntity instanceof IEnergyConductor conductor) {
                rEUAmount += 200;
                tList.add(
                    GTUtility.trans("175", "Conduction Loss: ") + EnumChatFormatting.YELLOW
                        + conductor.getConductionLoss()
                        + EnumChatFormatting.RESET);
            }
        } catch (Exception e) {
            tList.add("§cAn exception was thrown while fetching this device's EU conduction info.§r");
            if (D1) e.printStackTrace(GTLog.err);
        }
        return rEUAmount;
    }

    private static int addIC2WrenchableInfo(EntityPlayer aPlayer, ArrayList<String> tList, TileEntity tTileEntity) {
        int rEUAmount = 0;
        try {
            if (tTileEntity instanceof IWrenchable wrenchable) {
                rEUAmount += 100;
                tList.add(
                    GTUtility.trans("171", "Facing: ") + EnumChatFormatting.GREEN
                        + wrenchable.getFacing()
                        + EnumChatFormatting.RESET
                        + GTUtility.trans("172", " / Chance: ")
                        + EnumChatFormatting.YELLOW
                        + (wrenchable.getWrenchDropRate() * 100)
                        + EnumChatFormatting.RESET
                        + "%");
                tList.add(
                    wrenchable.wrenchCanRemove(aPlayer)
                        ? EnumChatFormatting.GREEN + GTUtility.trans("173", "You can remove this with a Wrench")
                            + EnumChatFormatting.RESET
                        : EnumChatFormatting.RED + GTUtility.trans("174", "You can NOT remove this with a Wrench")
                            + EnumChatFormatting.RESET);
            }
        } catch (Exception e) {
            tList.add("§cAn exception was thrown while fetching this device's IC@ wrenchability.§r");
            if (D1) e.printStackTrace(GTLog.err);
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
                        GTUtility.trans("219", "Extended Facing: ") + EnumChatFormatting.GREEN
                            + tAlignment.getExtendedFacing()
                            + EnumChatFormatting.RESET);
                }
            }
        } catch (Exception e) {
            tList.add("§cAn exception was thrown while fetching this device's alignment info.§r");
            if (D1) e.printStackTrace(GTLog.err);
        }
        return rEUAmount;
    }

    private static int addReactorInfo(ArrayList<String> tList, TileEntity tTileEntity) {
        int rEUAmount = 0;
        try {
            if (tTileEntity instanceof IReactor reactor) {
                rEUAmount += 500;
                tList.add(
                    GTUtility.trans("168", "Heat: ") + EnumChatFormatting.GREEN
                        + formatNumber(reactor.getHeat())
                        + EnumChatFormatting.RESET
                        + " / "
                        + EnumChatFormatting.YELLOW
                        + formatNumber(reactor.getMaxHeat())
                        + EnumChatFormatting.RESET);
                tList.add(
                    GTUtility.trans("169", "HEM: ") + EnumChatFormatting.YELLOW
                        + reactor.getHeatEffectModifier()
                        + EnumChatFormatting.RESET);
            }
        } catch (Exception e) {
            tList.add("§cAn exception was thrown while fetching this reactor's info.§r");
            if (D1) e.printStackTrace(GTLog.err);
        }
        return rEUAmount;
    }

    /**
     * @deprecated Use {@link StatCollector}
     */
    @Deprecated
    public static String trans(String aKey, String aEnglish) {
        return GTLanguageManager.addStringLocalization("Interaction_DESCRIPTION_Index_" + aKey, aEnglish);
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
     * could never happen. Normalizes values into the range [0.0f, 1.0f].
     */
    public static ForgeDirection determineWrenchingSide(ForgeDirection side, float aX, float aY, float aZ) {
        float modX = (aX % 1.0f + 1.0f) % 1.0f;
        float modY = (aY % 1.0f + 1.0f) % 1.0f;
        float modZ = (aZ % 1.0f + 1.0f) % 1.0f;
        ForgeDirection tBack = side.getOpposite();
        // The = here is necessary; Since the hitVec only has a precision of 1/16th on MP and gets rounded down,
        // a value of 0.8 would be 0.75 on MP, which is not > 0.75, returning false.
        switch (side) {
            case DOWN, UP -> {
                if (modX < 0.25) {
                    if (modZ < 0.25) return tBack;
                    if (modZ >= 0.75) return tBack;
                    return WEST;
                }
                if (modX >= 0.75) {
                    if (modZ < 0.25) return tBack;
                    if (modZ >= 0.75) return tBack;
                    return EAST;
                }
                if (modZ < 0.25) return NORTH;
                if (modZ >= 0.75) return SOUTH;
                return side;
            }
            case NORTH, SOUTH -> {
                if (modX < 0.25) {
                    if (modY < 0.25) return tBack;
                    if (modY >= 0.75) return tBack;
                    return WEST;
                }
                if (modX >= 0.75) {
                    if (modY < 0.25) return tBack;
                    if (modY >= 0.75) return tBack;
                    return EAST;
                }
                if (modY < 0.25) return DOWN;
                if (modY >= 0.75) return UP;
                return side;
            }
            case WEST, EAST -> {
                if (modZ < 0.25) {
                    if (modY < 0.25) return tBack;
                    if (modY >= 0.75) return tBack;
                    return NORTH;
                }
                if (modZ >= 0.75) {
                    if (modY < 0.25) return tBack;
                    if (modY >= 0.75) return tBack;
                    return SOUTH;
                }
                if (modY < 0.25) return DOWN;
                if (modY >= 0.75) return UP;
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

    public static String scientificFormat(long aNumber) {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
        dfs.setExponentSeparator("e");
        DecimalFormat format = new DecimalFormat("0.00E0", dfs);
        return format.format(aNumber);
    }

    public static String scientificFormat(BigInteger aNumber) {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
        dfs.setExponentSeparator("e");
        DecimalFormat format = new DecimalFormat("0.00E0", dfs);
        return format.format(aNumber);
    }

    /**
     * {@link String#format} without throwing exception. Falls back to {@code format} without {@code args}. Since it
     * suppresses errors, it should be used only when inputs are unreliable, e.g. processing text input by player, or
     * processing placeholders in localization entries.
     */
    @Nonnull
    public static String formatStringSafe(@Nonnull String format, Object... args) {
        try {
            return String.format(format, args);
        } catch (IllegalFormatException ignored) {
            return format;
        }
    }

    public static String translate(String key, Object... parameters) {
        return parameters.length == 0 ? StatCollector.translateToLocal(key)
            : StatCollector.translateToLocalFormatted(key, parameters);
    }

    public static String tryTranslate(String key, String fallback, Object... parameters) {
        return StatCollector.canTranslate(key) ? translate(key, parameters) : fallback;
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
    public static boolean consumeItems(EntityPlayer player, ItemStack stack, Materials mat, int count) {
        if (stack != null && GTOreDictUnificator.getItemData(stack).mMaterial.mMaterial == mat
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

    /**
     * @return A list of every integrated circuit, excluding zero. Do not modify the ItemStacks!
     */
    public static List<ItemStack> getAllIntegratedCircuits() {
        return ItemIntegratedCircuit.NON_ZERO_VARIANTS;
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

    public static int hashCode(int... values) {
        return Arrays.hashCode(values);
    }

    public static long hashCode(long... values) {
        return Arrays.hashCode(values);
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

    public static Map<GTUtility.ItemId, Long> convertItemListToMap(Collection<ItemStack> itemStacks) {
        Map<GTUtility.ItemId, Long> result = new Object2LongOpenHashMap<>();
        for (ItemStack itemStack : itemStacks) {
            if (itemStack != null && itemStack.stackSize > 0) {
                GTUtility.ItemId itemId = GTUtility.ItemId.createNoCopy(itemStack);
                result.merge(itemId, (long) itemStack.stackSize, Long::sum);
            }
        }
        return result;
    }

    public static Map<Fluid, Long> convertFluidListToMap(Collection<FluidStack> fluidStacks) {
        Map<Fluid, Long> result = new Reference2LongOpenHashMap<>();
        for (FluidStack fluidStack : fluidStacks) {
            if (fluidStack != null && fluidStack.amount > 0) {
                result.merge(fluidStack.getFluid(), (long) fluidStack.amount, Long::sum);
            }
        }
        return result;
    }

    /**
     * @return Supplied collection that doesn't contain invalid MetaTileEntities.
     */
    public static <T extends Collection<E>, E extends MetaTileEntity> T filterValidMTEs(T metaTileEntities) {
        metaTileEntities.removeIf(mte -> mte == null || !mte.isValid());
        return metaTileEntities;
    }

    /**
     * @return Supplied collection that removes invalid MTEs from the collection while it is being iterated
     */
    public static <T extends Collection<E>, E extends MetaTileEntity> ValidMTEList<T, E> validMTEList(
        T metaTileEntities) {
        return new ValidMTEList<>(metaTileEntities);
    }

    /**
     * Filters a list of MTEs into a list of a subclass
     *
     * @param mtes     The original list of MTEs
     * @param mteClass The MTE subclass to filter
     * @return The filtered list of valid MTEs
     * @param <MTESuper> The MTE superclass
     * @param <MTEImpl>  The MTE implementation class/subclass
     */
    public static <MTESuper extends MetaTileEntity, MTEImpl extends MTESuper> List<MTEImpl> getMTEsOfType(
        List<MTESuper> mtes, Class<MTEImpl> mteClass) {
        List<MTEImpl> out = new ArrayList<>(mtes.size());

        for (int i = 0, mtesSize = mtes.size(); i < mtesSize; i++) {
            MTESuper mte = mtes.get(i);

            if (mte != null && mte.isValid()) {
                if (mteClass.isInstance(mte)) {
                    out.add(mteClass.cast(mte));
                }
            }
        }

        return out;
    }

    @Nullable
    public static IMetaTileEntity getMetaTileEntity(TileEntity tileEntity) {
        if (tileEntity instanceof IGregTechTileEntity gtTE && gtTE.canAccessData()) {
            return gtTE.getMetaTileEntity();
        }
        return null;
    }

    public static ForgeDirection getSideFromPlayerFacing(Entity player) {
        if (player == null) return ForgeDirection.UNKNOWN;
        if (player.rotationPitch >= 65) return ForgeDirection.UP;
        if (player.rotationPitch <= -65) return ForgeDirection.DOWN;
        final byte facing = COMPASS_DIRECTIONS[MathHelper.floor_double(0.5D + 4.0F * player.rotationYaw / 360.0F)
            & 0x3];
        return ForgeDirection.getOrientation(facing);
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
                .append(",");
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

                if (tPageText.length() != 0) {
                    String tPageCounter = tTotalPages > 1 ? String.format(aPageFormatter, tPage + 1, tTotalPages) : "";
                    NBTTagString tPageTag = new NBTTagString(String.format(aPageHeader, tPageCounter) + tPageText);
                    aBook.appendTag(tPageTag);
                }

                ++tPage;
            } while (tPageText.length() != 0);
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

    public static String toSubscript(long no) {
        char[] chars = Long.toString(no)
            .toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] = switch (chars[i]) {
                case '0' -> CustomGlyphs.SUBSCRIPT0.charAt(0);
                case '1' -> '\u2081';
                case '2' -> '\u2082';
                case '3' -> '\u2083';
                case '4' -> '\u2084';
                case '5' -> '\u2085';
                case '6' -> '\u2086';
                case '7' -> '\u2087';
                case '8' -> '\u2088';
                case '9' -> '\u2089';
                case '?' -> CustomGlyphs.SUBSCRIPT_QUESTION_MARK.charAt(0);
                default -> chars[i];
            };
        }
        return new String(chars);
    }

    public static boolean isPartOfMaterials(ItemStack aStack, Materials aMaterials) {
        return GTOreDictUnificator.getAssociation(aStack) != null
            && GTOreDictUnificator.getAssociation(aStack).mMaterial.mMaterial.equals(aMaterials);
    }

    public static boolean isPartOfOrePrefix(ItemStack aStack, OrePrefixes aPrefix) {
        return GTOreDictUnificator.getAssociation(aStack) != null
            && GTOreDictUnificator.getAssociation(aStack).mPrefix.equals(aPrefix);
    }

    public static boolean isOre(Block block, int meta) {
        OptionalBoolean isOre = OreManager.isOre(block, meta);

        if (isOre != OptionalBoolean.NONE) return isOre.getAsBoolean();

        return isOre(new ItemStack(block, 1, meta));
    }

    public static boolean isOre(ItemStack aStack) {
        int tItem = GTUtility.stackToInt(aStack);
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

    public static Optional<GTRecipe> reverseShapelessRecipe(ItemStack output, Object... aRecipe) {
        if (output == null) {
            return Optional.empty();
        }

        List<ItemStack> inputs = new ArrayList<>();

        for (Object o : aRecipe) {
            if (o instanceof ItemStack) {
                ItemStack toAdd = ((ItemStack) o).copy();
                inputs.add(toAdd);
            } else if (o instanceof String) {
                ItemStack stack = GTOreDictUnificator.get(o, 1);
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

        inputs.removeIf(x -> x.getItem() instanceof MetaGeneratedTool);

        return Optional.of(
            new GTRecipe(
                new ItemStack[] { output },
                inputs.toArray(new ItemStack[0]),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                300,
                30,
                0));
    }

    public static Optional<GTRecipe> reverseShapedRecipe(ItemStack output, Object... aRecipe) {
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
                ItemStack stack = GTOreDictUnificator.get(dictName, null, amount, false, true);
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
        inputs.removeIf(x -> x.getItem() instanceof MetaGeneratedTool);

        return Optional.of(
            new GTRecipe(
                new ItemStack[] { output },
                inputs.toArray(new ItemStack[0]),
                null,
                null,
                null,
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

    public static long clamp(long val, long lo, long hi) {
        return Math.min(hi, Math.max(val, lo));
    }

    public static int clamp(int val, int lo, int hi) {
        return Math.min(hi, Math.max(val, lo));
    }

    public static float clamp(float val, float lo, float hi) {
        return Math.min(hi, Math.max(val, lo));
    }

    public static double clamp(double val, double lo, double hi) {
        return Math.min(hi, Math.max(val, lo));
    }

    public static int map(int x, int in_min, int in_max, int out_min, int out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public static long map(long x, long in_min, long in_max, long out_min, long out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public static float map(float x, float in_min, float in_max, float out_min, float out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public static double map(double x, double in_min, double in_max, double out_min, double out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public static double linearCurve(double x, double x1, double y1, double x2, double y2) {
        x = GTUtility.clamp(x, Math.min(x1, x2), Math.max(x1, x2));

        return map(x, x1, x2, y1, y2);
    }

    public static int min(int a, int b, int c) {
        return Math.min(a, Math.min(b, c));
    }

    public static int min(int a, int b, int c, int d) {
        return Math.min(a, Math.min(b, Math.min(c, b)));
    }

    public static int min(int first, int... rest) {
        for (int i = 0; i < rest.length; i++) {
            int l = rest[i];
            if (l < first) first = l;
        }
        return first;
    }

    public static long min(long first, long... rest) {
        for (int i = 0; i < rest.length; i++) {
            long l = rest[i];
            if (l < first) first = l;
        }
        return first;
    }

    public static int max(int first, int... rest) {
        for (int i = 0; i < rest.length; i++) {
            int l = rest[i];
            if (l > first) first = l;
        }
        return first;
    }

    public static long max(long first, long... rest) {
        for (int i = 0; i < rest.length; i++) {
            long l = rest[i];
            if (l > first) first = l;
        }
        return first;
    }

    public static int ceilDiv(int lhs, int rhs) {
        return (lhs + rhs - 1) / rhs;
    }

    /** Handles negatives properly, but it's slower than {@link #ceilDiv(int, int)}. */
    public static int ceilDiv2(int lhs, int rhs) {
        int sign = Integer.signum(lhs) * Integer.signum(rhs);

        if (lhs == 0) return 0;
        if (rhs == 0) throw new ArithmeticException("/ by zero");

        lhs = Math.abs(lhs);
        rhs = Math.abs(rhs);

        int unsigned = 1 + ((lhs - 1) / rhs);

        return unsigned * sign;
    }

    public static long ceilDiv(long lhs, long rhs) {
        return (lhs + rhs - 1) / rhs;
    }

    /** @deprecated Use {@link Integer#signum(int)} instead.} */
    @Deprecated
    public static int signum(int x) {
        return Integer.signum(x);
    }

    /** @deprecated Use {@link Long#signum(long)} instead.} */
    @Deprecated
    public static long signum(long x) {
        return Long.signum(x);
    }

    public static Vector3i signum(Vector3i v) {
        v.x = Integer.signum(v.x);
        v.y = Integer.signum(v.y);
        v.z = Integer.signum(v.z);

        return v;
    }

    public static int mod(int value, int divisor) {
        return ((value % divisor) + divisor) % divisor;
    }

    /**
     * Computes base raised to the power of an integer exponent. Typically faster than
     * {@link java.lang.Math#pow(double, double)} when {@code exp} is an integer.
     */
    public static double powInt(double base, int exp) {
        if (exp > 0) return powBySquaring(base, exp);
        if (exp < 0) return 1.0 / powBySquaring(base, -exp);
        return 1.0;
    }

    /**
     * Computes base raised to non-negative integer exponent.
     */
    private static double powBySquaring(double base, int exp) {
        // IEEE 754 double: 1 sign bit, 11 exponent bits, 52 mantissa bits. Exponent is stored with offset 1023.
        // The result is directly constructed for bases 2 and 4 from the exponent bits.
        if (base == 2) return exp > 1023 ? Double.POSITIVE_INFINITY : Double.longBitsToDouble(exp + 1023L << 52);
        if (base == 4) return exp > 511 ? Double.POSITIVE_INFINITY : Double.longBitsToDouble(exp * 2L + 1023L << 52);
        double result = 1.0;
        while (exp > 0) {
            if ((exp & 1) == 1) result *= base;
            base *= base;
            exp >>= 1;
        }
        return result;
    }

    /**
     * Computes base raised to the power of a long exponent. Typically faster than
     * {@link java.lang.Math#pow(double, double)} when {@code exp} is a long.
     */
    public static double powInt(double base, long exp) {
        if (exp > 0) return powBySquaring(base, exp);
        if (exp < 0) return 1.0 / powBySquaring(base, -exp);
        return 1.0;
    }

    /**
     * Computes base raised to non-negative long exponent.
     */
    private static double powBySquaring(double base, long exp) {
        // IEEE 754 double: 1 sign bit, 11 exponent bits, 52 mantissa bits. Exponent is stored with offset 1023.
        // The result is directly constructed for bases 2 and 4 from the exponent bits.
        if (base == 2) return exp > 1023 ? Double.POSITIVE_INFINITY : Double.longBitsToDouble(exp + 1023L << 52);
        if (base == 4) return exp > 511 ? Double.POSITIVE_INFINITY : Double.longBitsToDouble(exp * 2L + 1023L << 52);
        double result = 1.0;
        while (exp > 0) {
            if ((exp & 1) == 1) result *= base;
            base *= base;
            exp >>= 1;
        }
        return result;
    }

    /**
     * Computes the floor of log base 2 for a positive integer. Uses bitwise operations for fast calculation.
     */
    public static int log2(int a) {
        if (a <= 1) return 0;
        return 31 - Integer.numberOfLeadingZeros(a);
    }

    /**
     * Computes the ceiling of log base 2 for a positive integer. Uses bitwise operations for fast calculation.
     */
    public static int log2ceil(int a) {
        if (a <= 1) return 0;
        return 32 - Integer.numberOfLeadingZeros(a - 1);
    }

    /**
     * Computes the floor of log base 4 for a positive integer. Uses bitwise operations for fast calculation.
     */
    public static int log4(int a) {
        if (a <= 1) return 0;
        return 31 - Integer.numberOfLeadingZeros(a) >> 1;
    }

    /**
     * Computes the ceil of log base 4 for a positive integer. Uses bitwise operations for fast calculation.
     */
    public static int log4ceil(int a) {
        if (a <= 1) return 0;
        return 33 - Integer.numberOfLeadingZeros(a - 1) >> 1;
    }

    /**
     * Computes the floor of log base 2 for a positive long. Uses bitwise operations for fast calculation.
     */
    public static long log2(long a) {
        if (a <= 1) return 0;
        return 63 - Long.numberOfLeadingZeros(a);
    }

    /**
     * Computes the ceiling of log base 2 for a positive long. Uses bitwise operations for fast calculation.
     */
    public static long log2ceil(long a) {
        if (a <= 1) return 0;
        return 64 - Long.numberOfLeadingZeros(a - 1);
    }

    /**
     * Computes the floor of log base 4 for a positive long. Uses bitwise operations for fast calculation.
     */
    public static long log4(long a) {
        if (a <= 1) return 0;
        return 63 - Long.numberOfLeadingZeros(a) >> 1;
    }

    /**
     * Computes the ceil of log base 4 for a positive long. Uses bitwise operations for fast calculation.
     */
    public static long log4ceil(long a) {
        if (a <= 1) return 0;
        return 65 - Long.numberOfLeadingZeros(a - 1) >> 1;
    }

    public static int addSafe(int a, int b) {
        int result = a + b;

        if (a > 0 && b > 0 && result <= 0) {
            return Integer.MAX_VALUE;
        }

        if (a < 0 && b < 0 && result >= 0) {
            return Integer.MIN_VALUE;
        }

        return result;
    }

    public static long addSafe(long a, long b) {
        long result = a + b;

        if (a > 0 && b > 0 && result <= 0) {
            return Long.MAX_VALUE;
        }

        if (a < 0 && b < 0 && result >= 0) {
            return Long.MIN_VALUE;
        }

        return result;
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
        return GTUtility.areStacksEqual(itemStack, tStack);
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

    public static <NBT extends NBTBase> Collector<NBT, ?, NBTTagList> toNBTTagList() {
        return new Collector<NBT, NBTTagList, NBTTagList>() {

            @Override
            public Supplier<NBTTagList> supplier() {
                return NBTTagList::new;
            }

            @Override
            public BiConsumer<NBTTagList, NBT> accumulator() {
                return NBTTagList::appendTag;
            }

            @Override
            public BinaryOperator<NBTTagList> combiner() {
                return (from, to) -> {
                    to.tagList.addAll(from.tagList);

                    return to;
                };
            }

            @Override
            public Function<NBTTagList, NBTTagList> finisher() {
                return Function.identity();
            }

            @Override
            public Set<Characteristics> characteristics() {
                return new HashSet<>(Arrays.asList(Characteristics.IDENTITY_FINISH));
            }
        };
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

    public static String[] breakLines(String... lines) {
        return Arrays.stream(lines)
            .flatMap(s -> Arrays.stream(s.split("\\\\n")))
            .toArray(String[]::new);
    }

    @AutoValue
    public abstract static class ItemId {

        public static ItemId create(NBTTagCompound tag) {
            return new AutoValue_GTUtility_ItemId(
                Item.getItemById(tag.getShort("item")),
                tag.getShort("meta"),
                tag.hasKey("tag", Constants.NBT.TAG_COMPOUND) ? tag.getCompoundTag("tag") : null,
                tag.hasKey("stackSize", Constants.NBT.TAG_INT) ? tag.getInteger("stackSize") : null);
        }

        /**
         * This method copies NBT, as it is mutable.
         */
        public static ItemId create(ItemStack itemStack) {
            NBTTagCompound nbt = itemStack.getTagCompound();
            if (nbt != null) {
                nbt = (NBTTagCompound) nbt.copy();
            }

            return new AutoValue_GTUtility_ItemId(itemStack.getItem(), Items.feather.getDamage(itemStack), nbt, null);
        }

        /**
         * This method copies NBT, as it is mutable.
         */
        public static ItemId createWithStackSize(ItemStack itemStack) {
            NBTTagCompound nbt = itemStack.getTagCompound();
            if (nbt != null) {
                nbt = (NBTTagCompound) nbt.copy();
            }

            return new AutoValue_GTUtility_ItemId(
                itemStack.getItem(),
                Items.feather.getDamage(itemStack),
                nbt,
                itemStack.stackSize);
        }

        /**
         * This method copies NBT, as it is mutable.
         */
        public static ItemId create(Item item, int metaData, @Nullable NBTTagCompound nbt) {
            if (nbt != null) {
                nbt = (NBTTagCompound) nbt.copy();
            }
            return new AutoValue_GTUtility_ItemId(item, metaData, nbt, null);
        }

        /**
         * This method copies NBT, as it is mutable.
         */
        public static ItemId create(Item item, int metaData, @Nullable NBTTagCompound nbt,
            @Nullable Integer stackSize) {
            if (nbt != null) {
                nbt = (NBTTagCompound) nbt.copy();
            }
            return new AutoValue_GTUtility_ItemId(item, metaData, nbt, stackSize);
        }

        /**
         * This method stores metadata as wildcard and NBT as null.
         */
        public static ItemId createAsWildcard(ItemStack itemStack) {
            return new AutoValue_GTUtility_ItemId(itemStack.getItem(), WILDCARD, null, null);
        }

        public static ItemId createAsWildcardWithNBT(ItemStack itemStack) {
            NBTTagCompound nbt = itemStack.getTagCompound();
            if (nbt != null) {
                nbt = (NBTTagCompound) nbt.copy();
            }
            return new AutoValue_GTUtility_ItemId(itemStack.getItem(), WILDCARD, nbt, null);
        }

        /**
         * This method stores NBT as null.
         */
        public static ItemId createWithoutNBT(ItemStack itemStack) {
            return new AutoValue_GTUtility_ItemId(itemStack.getItem(), Items.feather.getDamage(itemStack), null, null);
        }

        /**
         * This method does not copy NBT in order to save time. Make sure not to mutate it!
         */
        public static ItemId createNoCopy(ItemStack itemStack) {
            return new AutoValue_GTUtility_ItemId(
                itemStack.getItem(),
                Items.feather.getDamage(itemStack),
                itemStack.getTagCompound(),
                null);
        }

        /**
         * This method does not copy NBT in order to save time. Make sure not to mutate it!
         */
        public static ItemId createNoCopyWithStackSize(ItemStack itemStack) {
            return new AutoValue_GTUtility_ItemId(
                itemStack.getItem(),
                Items.feather.getDamage(itemStack),
                itemStack.getTagCompound(),
                itemStack.stackSize);
        }

        /**
         * This method does not copy NBT in order to save time. Make sure not to mutate it!
         */
        public static ItemId createNoCopy(Item item, int metaData, @Nullable NBTTagCompound nbt) {
            return new AutoValue_GTUtility_ItemId(item, metaData, nbt, null);
        }

        public abstract Item item();

        public abstract int metaData();

        @Nullable
        public abstract NBTTagCompound nbt();

        @Nullable
        public abstract Integer stackSize();

        public NBTTagCompound writeToNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setShort("item", (short) Item.getIdFromItem(item()));
            tag.setShort("meta", (short) metaData());
            if (nbt() != null) tag.setTag("tag", nbt());
            if (stackSize() != null) tag.setInteger("stackSize", stackSize());
            return tag;
        }

        @Nonnull
        public ItemStack getItemStack() {
            ItemStack itemStack = new ItemStack(item(), 1, metaData());
            NBTTagCompound nbt = nbt();
            itemStack.setTagCompound(nbt == null ? null : (NBTTagCompound) nbt.copy());
            return itemStack;
        }

        @Nonnull
        public ItemStack getItemStack(int stackSize) {
            ItemStack itemStack = new ItemStack(item(), stackSize, metaData());
            NBTTagCompound nbt = nbt();
            itemStack.setTagCompound(nbt == null ? null : (NBTTagCompound) nbt.copy());
            return itemStack;
        }

        public boolean matches(ItemStack stack) {
            if (item() != stack.getItem()) return false;
            if (metaData() != Items.feather.getDamage(stack)) return false;

            return Objects.equals(nbt(), stack.getTagCompound());
        }
    }

    @AutoValue
    public abstract static class FluidId {

        public static FluidId create(NBTTagCompound tag) {
            return new AutoValue_GTUtility_FluidId(
                FluidRegistry.getFluid(tag.getString("FluidName")),
                tag.hasKey("Tag", Constants.NBT.TAG_COMPOUND) ? tag.getCompoundTag("Tag") : null,
                tag.hasKey("Amount", Constants.NBT.TAG_INT) ? tag.getInteger("Amount") : null);
        }

        public NBTTagCompound writeToNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("FluidName", fluid().getName());
            if (nbt() != null) tag.setTag("Tag", nbt());
            Integer amount = amount();
            if (amount != null) tag.setInteger("Amount", amount);
            return tag;
        }

        public static FluidId create(FluidStack fluidStack) {
            return createWithCopy(fluidStack.getFluid(), null, fluidStack.tag);
        }

        public static FluidId createWithAmount(FluidStack fluidStack) {
            return createWithCopy(fluidStack.getFluid(), fluidStack.amount, fluidStack.tag);
        }

        public static FluidId create(Fluid fluid) {
            return createNoCopy(fluid, null, null);
        }

        public static FluidId createWithCopy(Fluid fluid, Integer amount, @Nullable NBTTagCompound nbt) {
            if (nbt != null) {
                nbt = (NBTTagCompound) nbt.copy();
            }
            return new AutoValue_GTUtility_FluidId(fluid, nbt, amount);
        }

        /**
         * This method does not copy the NBT tag.
         */
        public static FluidId createNoCopy(Fluid fluid, Integer amount, @Nullable NBTTagCompound nbt) {
            return new AutoValue_GTUtility_FluidId(fluid, nbt, amount);
        }

        protected abstract Fluid fluid();

        @Nullable
        protected abstract NBTTagCompound nbt();

        @Nullable
        protected abstract Integer amount();

        @Nonnull
        public FluidStack getFluidStack() {
            NBTTagCompound nbt = nbt();
            return new FluidStack(fluid(), 1, nbt != null ? (NBTTagCompound) nbt.copy() : null);
        }

        @Nonnull
        public FluidStack getFluidStack(int amount) {
            NBTTagCompound nbt = nbt();
            return new FluidStack(fluid(), amount, nbt != null ? (NBTTagCompound) nbt.copy() : null);
        }
    }

    public static int getPlasmaFuelValueInEUPerLiterFromMaterial(Materials material) {
        return getPlasmaFuelValueInEUPerLiterFromFluid(material.getPlasma(1));
    }

    public static int getPlasmaFuelValueInEUPerLiterFromFluid(FluidStack aLiquid) {
        if (aLiquid == null) return 0;
        GTRecipe tFuel = RecipeMaps.plasmaFuels.getBackend()
            .findFuel(aLiquid);
        if (tFuel != null) return tFuel.mSpecialValue;
        return 0;
    }

    public static MovingObjectPosition getPlayerLookingTarget(EntityPlayer viewpoint) {
        double reachDistance = viewpoint instanceof EntityPlayerMP mp ? mp.theItemInWorldManager.getBlockReachDistance()
            : getClientReachDistance();
        Vec3 posVec = Vec3.createVectorHelper(
            viewpoint.posX,
            viewpoint.posY + (viewpoint.getEyeHeight() - viewpoint.getDefaultEyeHeight()),
            viewpoint.posZ);
        Vec3 lookVec = viewpoint.getLook(0);
        Vec3 modifiedPosVec = posVec
            .addVector(lookVec.xCoord * reachDistance, lookVec.yCoord * reachDistance, lookVec.zCoord * reachDistance);

        return viewpoint.worldObj.rayTraceBlocks(posVec, modifiedPosVec);
    }

    public static float getClientReachDistance() {
        return Minecraft.getMinecraft().playerController.getBlockReachDistance();
    }

    public static String formatShortenedLong(long number) {
        if (number < 1000) {
            return String.valueOf(number);
        }

        int exp = (int) (Math.log(number) / Math.log(1000));
        char suffix = "kMGTPE".charAt(exp - 1);
        double shortened = number / GTUtility.powInt(1000, exp);

        if (shortened == (long) shortened) {
            return String.format("%d%c", (long) shortened, suffix);
        } else {
            return String.format("%.1f%c", shortened, suffix);
        }
    }

    public static String truncateText(String text, int limit) {
        if (limit < 0) limit = 1;
        if (text == null) {
            return null;
        }
        if (text.length() <= limit) {
            return text;
        } else {
            return text.substring(0, limit) + "...";
        }
    }

    // helper function (from MultiblockBase that creates a string of timed dates
    public static String appendRate(boolean isLiquid, Long amount, boolean isFormatShortened, int maxProgressTicks) {
        final StringBuffer ret = new StringBuffer();
        final DecimalFormat df = new DecimalFormat("0.00");
        final double progressTime = (double) maxProgressTicks / 20;
        double perTick = amount / (double) maxProgressTicks;
        double perSecond = amount / progressTime;
        double perMinute = perSecond * 60;
        double perHour = perSecond * 3_600;
        double perDay = perSecond * 86_400;

        final String amountText = translateToLocal("GT5U.gui.text.amount") + " ";
        final String perTickText = translateToLocal("GT5U.gui.text.per_tick") + " ";
        final String perSecondText = translateToLocal("GT5U.gui.text.per_second") + " ";
        final String perMinuteText = translateToLocal("GT5U.gui.text.per_minute") + " ";
        final String perHourText = translateToLocal("GT5U.gui.text.per_hour") + " ";
        final String perDayText = translateToLocal("GT5U.gui.text.per_day") + " ";

        final Function<Double, Double> roundNumber = (number) -> {
            if (Math.abs(number) < 10) {
                return Math.round(number * 100) / 100.0;
            } else {
                return Math.floor(number);
            }
        };

        if (isFormatShortened) {
            ret.append(" (");
            ret.append(EnumChatFormatting.GRAY);
            if (perSecond <= 1) {
                ret.append(df.format(progressTime / amount));
                ret.append("s/each");
            } else {
                ret.append(formatShortenedLong((long) perSecond));
                ret.append("/s");
            }
            ret.append(EnumChatFormatting.WHITE);
            ret.append(")");
        } else {
            ret.append(EnumChatFormatting.RESET);
            ret.append(
                amountText + EnumChatFormatting.GOLD
                    + formatNumber(amount)
                    + (isLiquid ? "L" : "")
                    + EnumChatFormatting.RESET);
            ret.append("\n");
            ret.append(
                perTickText + EnumChatFormatting.GOLD
                    + formatNumber(roundNumber.apply(perTick))
                    + (isLiquid ? "L" : "")
                    + (perSecond > 1_000_000
                        ? EnumChatFormatting.WHITE + " ["
                            + EnumChatFormatting.GRAY
                            + formatShortenedLong((long) perTick)
                            + EnumChatFormatting.WHITE
                            + "]"
                        : "")
                    + EnumChatFormatting.RESET);
            ret.append("\n");
            ret.append(
                perSecondText + EnumChatFormatting.GOLD
                    + formatNumber(roundNumber.apply(perSecond))
                    + (isLiquid ? "L" : "")
                    + (perSecond > 1_000_000
                        ? EnumChatFormatting.WHITE + " ["
                            + EnumChatFormatting.GRAY
                            + formatShortenedLong((long) perSecond)
                            + EnumChatFormatting.WHITE
                            + "]"
                        : "")
                    + EnumChatFormatting.RESET);
            ret.append("\n");
            ret.append(
                perMinuteText + EnumChatFormatting.GOLD
                    + formatNumber(roundNumber.apply(perMinute))
                    + (isLiquid ? "L" : "")
                    + (perMinute > 1_000_000
                        ? EnumChatFormatting.WHITE + " ["
                            + EnumChatFormatting.GRAY
                            + formatShortenedLong((long) perMinute)
                            + EnumChatFormatting.WHITE
                            + "]"
                        : "")
                    + EnumChatFormatting.RESET);
            ret.append("\n");
            ret.append(
                perHourText + EnumChatFormatting.GOLD
                    + formatNumber(roundNumber.apply(perHour))
                    + (isLiquid ? "L" : "")
                    + (perHour > 1_000_000
                        ? EnumChatFormatting.WHITE + " ["
                            + EnumChatFormatting.GRAY
                            + formatShortenedLong((long) perHour)
                            + EnumChatFormatting.WHITE
                            + "]"
                        : "")
                    + EnumChatFormatting.RESET);
            ret.append("\n");
            ret.append(
                perDayText + EnumChatFormatting.GOLD
                    + formatNumber(roundNumber.apply(perDay))
                    + (isLiquid ? "L" : "")
                    + (perDay > 1_000_000
                        ? EnumChatFormatting.WHITE + " ["
                            + EnumChatFormatting.GRAY
                            + formatShortenedLong((long) perDay)
                            + EnumChatFormatting.WHITE
                            + "]"
                        : "")
                    + EnumChatFormatting.RESET);
        }
        return ret.toString();
    }

    public static boolean isRealPlayer(EntityLivingBase entity) {
        return entity instanceof EntityPlayer p && !p.getClass()
            .getName()
            .contains("Fake");
    }

    /**
     * ONLY used in GT MTE tooltips<br>
     *
     * Different from translateToLocalFormatted, it's to make sure<br>
     * nothing gets hardcoded on startup<br>
     * for a seamless translation experience
     */
    public static String nestParams(String locKey, Object... params) {
        if (params == null || params.length == 0) {
            return locKey;
        }

        JsonObject json = new JsonObject();
        json.addProperty("k", locKey);

        JsonArray paramsArray = new JsonArray();
        for (Object param : params) {
            if (param == null) {
                paramsArray.add(JsonNull.INSTANCE);
            } else {
                paramsArray.add(new JsonPrimitive(param.toString()));
            }
        }
        json.add("p", paramsArray);

        return json.toString();
    }

    /**
     * A helper method that does rotation calculations for renderering.
     *
     * @param extendedFacing - extendedFacing value of the MTE
     * @return AxisAngle4f that can be used directly with glRotate calls
     */
    public static AxisAngle4f getRotationAxisAngle4f(ExtendedFacing extendedFacing) {
        ForgeDirection direction = extendedFacing.getDirection();
        Rotation rotation = extendedFacing.getRotation();
        Flip flip = extendedFacing.getFlip();

        float faceAngleDeg = switch (direction) {
            case DOWN -> 90f;
            case UP -> -90f;
            case SOUTH -> 180f;
            case WEST -> 90f;
            case EAST -> -90f;
            default -> 0f; // NORTH
        };

        Vector3f faceAxis = switch (direction) {
            case UP, DOWN -> new Vector3f(1, 0, 0);
            default -> new Vector3f(0, 1, 0);
        };

        Quaternionf finalQuat = new Quaternionf().fromAxisAngleRad(faceAxis, (float) Math.toRadians(faceAngleDeg));

        // Local rotation
        float localAngleDeg = switch (rotation) {
            case CLOCKWISE -> -90f;
            case COUNTER_CLOCKWISE -> 90f;
            case UPSIDE_DOWN -> 180f;
            default -> 0f; // NORMAL
        };

        float angleSign = switch (direction) {
            case WEST, EAST, NORTH -> -1.0f;
            default -> 1.0f; // UP, DOWN, SOUTH
        };

        if (flip == Flip.HORIZONTAL || flip == Flip.VERTICAL) {
            angleSign *= -1.0f;
        }

        float angleOffset = (direction == ForgeDirection.DOWN) ? 180.0f : 0.0f;
        localAngleDeg = (localAngleDeg * angleSign) + angleOffset;

        // Apply the local rotation
        Quaternionf localQuat = new Quaternionf()
            .fromAxisAngleRad(new Vector3f(0, 0, 1), (float) Math.toRadians(localAngleDeg));
        finalQuat.mul(localQuat);

        // Extract axis-angle form
        AxisAngle4f axisAngle = new AxisAngle4f();
        finalQuat.get(axisAngle);

        // Convert to degrees for consistency
        axisAngle.angle = (float) Math.toDegrees(axisAngle.angle);
        return axisAngle;
    }
}
