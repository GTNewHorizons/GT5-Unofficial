package gtPlusPlus.core.util;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.EnumUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.FMLRelaunchLog;
import gregtech.GT_Mod;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TC_Aspects;
import gregtech.api.enums.TC_Aspects.TC_AspectStack;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.proxy.ClientProxy;
import gtPlusPlus.core.util.array.Pair;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.nbt.NBTUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import ic2.core.Ic2Items;
import ic2.core.init.InternalName;
import ic2.core.item.resources.ItemCell;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class Utils {

	public static final int WILDCARD_VALUE = Short.MAX_VALUE;

	public static final boolean isServer() {
		return FMLCommonHandler.instance().getEffectiveSide().isServer();
	}

	static class ShortTimerTask extends TimerTask {
		@Override
		public void run() {
			Utils.LOG_WARNING("Timer expired.");
		}
	}

	public static boolean isModUpToDate() {
		if (CORE.MASTER_VERSION.toLowerCase().equals("offline")) {
			return false;
		}

		else if (CORE.MASTER_VERSION.toLowerCase().equals(CORE.VERSION.toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}

	public static TC_AspectStack getTcAspectStack(final TC_Aspects aspect, final long size) {
		return getTcAspectStack(aspect.name(), (int) size);
	}

	public static TC_AspectStack getTcAspectStack(final String aspect, final long size) {
		return getTcAspectStack(aspect, (int) size);
	}

	public static TC_AspectStack getTcAspectStack(final TC_Aspects aspect, final int size) {
		return getTcAspectStack(aspect.name(), size);
	}

	public static TC_AspectStack getTcAspectStack(final String aspect, final int size) {

		TC_AspectStack returnValue = null;

		if (aspect.toUpperCase().equals("COGNITIO")) {
			// Adds in Compat for older GT Versions which Misspell aspects.
			try {
				if (EnumUtils.isValidEnum(TC_Aspects.class, "COGNITIO")) {
					Utils.LOG_WARNING("TC Aspect found - " + aspect);
					returnValue = new TC_AspectStack(TC_Aspects.valueOf("COGNITIO"), size);
				} else {
					Utils.LOG_INFO("Fallback TC Aspect found - " + aspect
							+ " - PLEASE UPDATE GREGTECH TO A NEWER VERSION TO REMOVE THIS MESSAGE - THIS IS NOT AN ERROR");
					returnValue = new TC_AspectStack(TC_Aspects.valueOf("COGNITO"), size);
				}
			} catch (final NoSuchFieldError r) {
				Utils.LOG_INFO("Invalid Thaumcraft Aspects - Report this issue to Alkalus");
			}
		} else if (aspect.toUpperCase().equals("EXANIMUS")) {
			// Adds in Compat for older GT Versions which Misspell aspects.
			try {
				if (EnumUtils.isValidEnum(TC_Aspects.class, "EXANIMUS")) {
					Utils.LOG_WARNING("TC Aspect found - " + aspect);
					returnValue = new TC_AspectStack(TC_Aspects.valueOf("EXANIMUS"), size);
				} else {
					Utils.LOG_INFO("Fallback TC Aspect found - " + aspect
							+ " - PLEASE UPDATE GREGTECH TO A NEWER VERSION TO REMOVE THIS MESSAGE - THIS IS NOT AN ERROR");
					returnValue = new TC_AspectStack(TC_Aspects.valueOf("EXAMINIS"), size);
				}
			} catch (final NoSuchFieldError r) {
				Utils.LOG_INFO("Invalid Thaumcraft Aspects - Report this issue to Alkalus");
			}

		} else if (aspect.toUpperCase().equals("PRAECANTATIO")) {
			// Adds in Compat for older GT Versions which Misspell aspects.
			try {
				if (EnumUtils.isValidEnum(TC_Aspects.class, "PRAECANTATIO")) {
					Utils.LOG_WARNING("TC Aspect found - " + aspect);
					returnValue = new TC_AspectStack(TC_Aspects.valueOf("PRAECANTATIO"), size);
				} else {
					Utils.LOG_INFO("Fallback TC Aspect found - " + aspect
							+ " - PLEASE UPDATE GREGTECH TO A NEWER VERSION TO REMOVE THIS MESSAGE - THIS IS NOT AN ERROR");
					returnValue = new TC_AspectStack(TC_Aspects.valueOf("PRAECANTIO"), size);
				}
			} catch (final NoSuchFieldError r) {
				Utils.LOG_INFO("Invalid Thaumcraft Aspects - Report this issue to Alkalus");
			}
		} else {
			Utils.LOG_WARNING("TC Aspect found - " + aspect);
			returnValue = new TC_AspectStack(TC_Aspects.valueOf(aspect), size);
		}

		return returnValue;
	}

	public static boolean containsMatch(final boolean strict, final ItemStack[] inputs, final ItemStack... targets) {
		for (final ItemStack input : inputs) {
			for (final ItemStack target : targets) {
				if (itemMatches(target, input, strict)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean itemMatches(final ItemStack target, final ItemStack input, final boolean strict) {
		if ((input == null) || (target == null)) {
			return false;
		}
		return ((target.getItem() == input.getItem()) && (((target.getItemDamage() == WILDCARD_VALUE) && !strict)
				|| (target.getItemDamage() == input.getItemDamage())));
	}

	// Logging Functions
	private static final Logger modLogger = makeLogger();

	// Generate GT++ Logger
	public static Logger makeLogger() {
		final Logger gtPlusPlusLogger = LogManager.getLogger("GT++");
		return gtPlusPlusLogger;
	}

	// Non-Dev Comments
	public static void LOG_INFO(final String s) {
		// if (CORE.DEBUG){
		modLogger.info(s);
		// }
	}

	// Non-Dev Comments
	public static void LOG_MACHINE_INFO(final String s) {

		boolean localPlayer = false;
		try {
			if (ClientProxy.playerName != null){
				if (ClientProxy.playerName.toLowerCase().contains("draknyte1")){
					localPlayer = true;
				}
			}
		}
		catch (final Throwable t){

		}

		if (CORE.ConfigSwitches.MACHINE_INFO || localPlayer) {
			final String name1 = gtPlusPlus.core.util.reflect.ReflectionUtils.getMethodName(2);
			modLogger.info("Machine Info: " + s + " | " + name1);
		}
	}

	// Developer Comments
	public static void LOG_WARNING(final String s) {
		if (CORE.DEBUG) {
			modLogger.warn(s);
		}
	}

	// Errors
	public static void LOG_ERROR(final String s) {
		if (CORE.DEBUG) {
			modLogger.fatal(s);
		}
	}

	// Developer Logger
	public static void LOG_SPECIFIC_WARNING(final String whatToLog, final String msg, final int line) {
		// if (!CORE.DEBUG){
		FMLLog.warning("GT++ |" + line + "| " + whatToLog + " | " + msg);
		// }
	}

	// Non-Dev Comments
	public static void LOG_ASM(final String s) {
		FMLRelaunchLog.info("", s);
	}

	//Register an event to both busses.
	public static void registerEvent(Object o){
		MinecraftForge.EVENT_BUS.register(o);
		FMLCommonHandler.instance().bus().register(o);
	}

	public static void paintBox(final Graphics g, final int MinA, final int MinB, final int MaxA, final int MaxB) {
		g.drawRect(MinA, MinB, MaxA, MaxB);
	}

	// Send a message to all players on the server
	public static void sendServerMessage(final String translationKey) {
		sendServerMessage(new ChatComponentText(translationKey));
	}

	// Send a message to all players on the server
	public static void sendServerMessage(final IChatComponent chatComponent) {
		MinecraftServer.getServer().getConfigurationManager().sendChatMsg(chatComponent);
	}

	/**
	 * Returns if that Liquid is IC2Steam.
	 */
	public static boolean isIC2Steam(final FluidStack aFluid) {
		if (aFluid == null) {
			return false;
		}
		return aFluid.isFluidEqual(getIC2Steam(1));
	}

	/**
	 * Returns a Liquid Stack with given amount of IC2Steam.
	 */
	public static FluidStack getIC2Steam(final long aAmount) {
		return FluidRegistry.getFluidStack("ic2steam", (int) aAmount);
	}

	/*
	 * public static void recipeBuilderBlock(ItemStack slot_1, ItemStack slot_2,
	 * ItemStack slot_3, ItemStack slot_4, ItemStack slot_5, ItemStack slot_6,
	 * ItemStack slot_7, ItemStack slot_8, ItemStack slot_9, Block resultBlock){
	 * GameRegistry.addRecipe(new ItemStack(resultBlock), new Object[] {"ABC",
	 * "DEF", "GHI", 'A',slot_1,'B',slot_2,'C',slot_3,
	 * 'D',slot_4,'E',slot_5,'F',slot_6, 'G',slot_7,'H',slot_8,'I',slot_9 }); }
	 */

	public static String checkCorrectMiningToolForBlock(final Block currentBlock, final World currentWorld) {
		String correctTool = "";
		if (!currentWorld.isRemote) {
			try {
				correctTool = currentBlock.getHarvestTool(0);
				Utils.LOG_WARNING(correctTool);

			} catch (final NullPointerException e) {

			}
		}

		return correctTool;
	}

	/**
	 *
	 * @param colourStr
	 *            e.g. "#FFFFFF"
	 * @return String - formatted "rgb(0,0,0)"
	 */
	public static String hex2RgbFormatted(final String hexString) {
		final Color c = new Color(Integer.valueOf(hexString.substring(1, 3), 16),
				Integer.valueOf(hexString.substring(3, 5), 16), Integer.valueOf(hexString.substring(5, 7), 16));

		final StringBuffer sb = new StringBuffer();
		sb.append("rgb(");
		sb.append(c.getRed());
		sb.append(",");
		sb.append(c.getGreen());
		sb.append(",");
		sb.append(c.getBlue());
		sb.append(")");
		return sb.toString();
	}

	/**
	 *
	 * @param colourStr
	 *            e.g. "#FFFFFF"
	 * @return
	 */
	public static Color hex2Rgb(final String colorStr) {
		return new Color(Integer.valueOf(colorStr.substring(1, 3), 16), Integer.valueOf(colorStr.substring(3, 5), 16),
				Integer.valueOf(colorStr.substring(5, 7), 16));
	}

	/**
	 *
	 * @param colourInt
	 *            e.g. 0XFFFFFF
	 * @return Colour
	 */
	public static Color hex2Rgb(final int colourInt) {
		return Color.decode(String.valueOf(colourInt));
	}

	/**
	 *
	 * @param colourInt
	 *            e.g. 0XFFFFFF
	 * @return short[]
	 */
	public static short[] hex2RgbShort(final int colourInt) {
		final Color rgb = Color.decode(String.valueOf(colourInt));
		final short[] rgba = { (short) rgb.getRed(), (short) rgb.getGreen(), (short) rgb.getBlue(),
				(short) rgb.getAlpha() };
		return rgba;
	}

	public static Timer ShortTimer(final int seconds) {
		Timer timer;
		timer = new Timer();
		timer.schedule(new ShortTimerTask(), seconds * 1000);
		return timer;
	}

	public static String byteToHex(final byte b) {
		final int i = b & 0xFF;
		return Integer.toHexString(i);
	}

	public static Object[] convertListToArray(final List<Object> sourceList) {
		final Object[] targetArray = sourceList.toArray(new Object[sourceList.size()]);
		return targetArray;
	}

	public static List<Object> convertArrayToFixedSizeList(final Object[] sourceArray) {
		final List<Object> targetList = Arrays.asList(sourceArray);
		return targetList;
	}

	public static List<Object> convertArrayToList(final Object[] sourceArray) {
		final List<Object> targetList = new ArrayList<>(Arrays.asList(sourceArray));
		return targetList;
	}

	public static List<Object> convertArrayListToList(final ArrayList<Object> sourceArray) {
		final List<Object> targetList = new ArrayList<Object>(Arrays.asList(sourceArray));
		return targetList;
	}

	public static void spawnCustomParticle(final Entity entity) {
		GTplusplus.proxy.generateMysteriousParticles(entity);
	}

	public static void spawnFX(final World world, final int x, final int y, final int z, final String particleName,
			Object particleName2) {
		if (!world.isRemote) {
			if ((particleName2 == null) || particleName2.equals("")) {
				particleName2 = particleName;
			}
			final int l = MathUtils.randInt(0, 4);
			final double d0 = x + 0.5F;
			final double d1 = y + 0.7F;
			final double d2 = z + 0.5F;
			final double d3 = 0.2199999988079071D;
			final double d4 = 0.27000001072883606D;

			if (l == 1) {
				world.spawnParticle(particleName, d0 - d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D);
			} else if (l == 2) {
				world.spawnParticle((String) particleName2, d0 + d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D);
			} else if (l == 3) {
				world.spawnParticle(particleName, d0, d1 + d3, d2 - d4, 0.0D, 0.0D, 0.0D);
			} else if (l == 4) {
				world.spawnParticle((String) particleName2, d0, d1 + d3, d2 + d4, 0.0D, 0.0D, 0.0D);
			} else {
				world.spawnParticle(particleName, d0, d1, d2, 0.0D, 0.0D, 0.0D);
				if (particleName2 != null) {
					world.spawnParticle((String) particleName2, d0, d1, d2, 0.0D, 0.0D, 0.0D);
				}
			}
		}
	}

	public static int rgbtoHexValue(final int r, final int g, final int b) {
		if ((r > 255) || (g > 255) || (b > 255) || (r < 0) || (g < 0) || (b < 0)) {
			return 0;
		}
		final Color c = new Color(r, g, b);
		String temp = Integer.toHexString(c.getRGB() & 0xFFFFFF).toUpperCase();

		// System.out.println( "hex: " + Integer.toHexString( c.getRGB() &
		// 0xFFFFFF ) + " hex value:"+temp);
		temp = Utils.appenedHexNotationToString(String.valueOf(temp));
		Utils.LOG_WARNING("Made " + temp + " - Hopefully it's not a mess.");
		Utils.LOG_WARNING("It will decode into " + Integer.decode(temp) + ".");
		return Integer.decode(temp);
	}

	/*
	 * http://javadevnotes.com/java-left-pad-string-with-zeros-examples
	 */
	public static String leftPadWithZeroes(final String originalString, final int length) {
		final StringBuilder sb = new StringBuilder();
		while ((sb.length() + originalString.length()) < length) {
			sb.append('0');
		}
		sb.append(originalString);
		final String paddedString = sb.toString();
		return paddedString;
	}

	/*
	 * Original Code by Chandana Napagoda -
	 * https://cnapagoda.blogspot.com.au/2011/03/java-hex-color-code-generator.
	 * html
	 */
	public static Map<Integer, String> hexColourGenerator(final int colorCount) {
		final int maxColorValue = 16777215;
		// this is decimal value of the "FFFFFF"
		final int devidedvalue = maxColorValue / colorCount;
		int countValue = 0;
		final HashMap<Integer, String> hexColorMap = new HashMap<>();
		for (int a = 0; (a < colorCount) && (maxColorValue >= countValue); a++) {
			if (a != 0) {
				countValue += devidedvalue;
				hexColorMap.put(a, Integer.toHexString(0x10000 | countValue).substring(1).toUpperCase());
			} else {
				hexColorMap.put(a, Integer.toHexString(0x10000 | countValue).substring(1).toUpperCase());
			}
		}
		return hexColorMap;
	}

	/*
	 * Original Code by Chandana Napagoda -
	 * https://cnapagoda.blogspot.com.au/2011/03/java-hex-color-code-generator.
	 * html
	 */
	public static Map<Integer, String> hexColourGeneratorRandom(final int colorCount) {
		final HashMap<Integer, String> hexColorMap = new HashMap<>();
		for (int a = 0; a < colorCount; a++) {
			String code = "" + (int) (Math.random() * 256);
			code = code + code + code;
			final int i = Integer.parseInt(code);
			hexColorMap.put(a, Integer.toHexString(0x1000000 | i).substring(1).toUpperCase());
			Utils.LOG_WARNING("" + Integer.toHexString(0x1000000 | i).substring(1).toUpperCase());
		}
		return hexColorMap;
	}

	public static String appenedHexNotationToString(final Object hexAsStringOrInt) {
		final String hexChar = "0x";
		String result;
		if (hexAsStringOrInt.getClass() == String.class) {

			if (((String) hexAsStringOrInt).length() != 6) {
				final String temp = leftPadWithZeroes((String) hexAsStringOrInt, 6);
				result = temp;
			}
			result = hexChar + hexAsStringOrInt;
			return result;
		} else if (hexAsStringOrInt.getClass() == Integer.class) {
			if (((String) hexAsStringOrInt).length() != 6) {
				final String temp = leftPadWithZeroes((String) hexAsStringOrInt, 6);
				result = temp;
			}
			result = hexChar + String.valueOf(hexAsStringOrInt);
			return result;
		} else {
			return null;
		}
	}

	public static Integer appenedHexNotationToInteger(final int hexAsStringOrInt) {
		final String hexChar = "0x";
		String result;
		Utils.LOG_WARNING(String.valueOf(hexAsStringOrInt));
		result = hexChar + String.valueOf(hexAsStringOrInt);
		return Integer.getInteger(result);
	}

	public static boolean doesEntryExistAlreadyInOreDictionary(final String OreDictName) {
		if (OreDictionary.getOres(OreDictName).size() != 0) {
			return true;
		}
		return false;
	}

	public static boolean invertBoolean(final boolean booleans) {
		if (booleans == true) {
			return false;
		}
		return true;
	}

	public static File getMcDir() {
		if ((MinecraftServer.getServer() != null) && MinecraftServer.getServer().isDedicatedServer()) {
			return new File(".");
		}
		return Minecraft.getMinecraft().mcDataDir;
	}

	private static short cellID = 15;

	public static ItemStack createInternalNameAndFluidCell(final String s) {
		Utils.LOG_WARNING("1");
		final InternalName yourName = EnumHelper.addEnum(InternalName.class, s, new Class[0], new Object[0]);
		Utils.LOG_WARNING("2 " + yourName.name());
		final ItemCell item = (ItemCell) Ic2Items.cell.getItem();
		Utils.LOG_WARNING("3 " + item.getUnlocalizedName());
		try {
			Utils.LOG_WARNING("4");
			final Class<? extends ItemCell> clz = item.getClass();
			Utils.LOG_WARNING("5 " + clz.getSimpleName());
			final Method methode = clz.getDeclaredMethod("addCell", int.class, InternalName.class, Block[].class);
			Utils.LOG_WARNING("6 " + methode.getName());
			methode.setAccessible(true);
			Utils.LOG_WARNING("7 " + methode.isAccessible());
			final ItemStack temp = (ItemStack) methode.invoke(item, cellID++, yourName, new Block[0]);
			Utils.LOG_WARNING("Successfully created " + temp.getDisplayName() + "s.");
			FluidContainerRegistry.registerFluidContainer(FluidUtils.getFluidStack(s.toLowerCase(), 1000), temp.copy(),
					Ic2Items.cell.copy());
			ItemUtils.addItemToOreDictionary(temp.copy(), "cell" + s);
			return temp;
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ItemStack createInternalNameAndFluidCellNoOreDict(final String s) {
		Utils.LOG_WARNING("1");
		final InternalName yourName = EnumHelper.addEnum(InternalName.class, s, new Class[0], new Object[0]);
		Utils.LOG_WARNING("2 " + yourName.name());
		final ItemCell item = (ItemCell) Ic2Items.cell.getItem();
		Utils.LOG_WARNING("3 " + item.getUnlocalizedName());
		try {
			Utils.LOG_WARNING("4");
			final Class<? extends ItemCell> clz = item.getClass();
			Utils.LOG_WARNING("5 " + clz.getSimpleName());
			final Method methode = clz.getDeclaredMethod("addCell", int.class, InternalName.class, Block[].class);
			Utils.LOG_WARNING("6 " + methode.getName());
			methode.setAccessible(true);
			Utils.LOG_WARNING("7 " + methode.isAccessible());
			final ItemStack temp = (ItemStack) methode.invoke(item, cellID++, yourName, new Block[0]);
			Utils.LOG_WARNING("Successfully created " + temp.getDisplayName() + "s.");
			FluidContainerRegistry.registerFluidContainer(FluidUtils.getFluidStack(s.toLowerCase(), 1000), temp.copy(),
					Ic2Items.cell.copy());
			// ItemUtils.addItemToOreDictionary(temp.copy(), "cell"+s);
			return temp;
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String sanitizeString(final String input) {
		String temp;
		String output;

		temp = input.replace(" ", "");
		temp = temp.replace("-", "");
		temp = temp.replace("_", "");
		temp = temp.replace("?", "");
		temp = temp.replace("!", "");
		temp = temp.replace("@", "");
		temp = temp.replace("#", "");
		temp = temp.replace("(", "");
		temp = temp.replace(")", "");
		temp = temp.replace("{", "");
		temp = temp.replace("}", "");
		temp = temp.replace("[", "");
		temp = temp.replace("]", "");
		temp = temp.replace(" ", "");
		output = temp;
		return output;

	}

	public static String sanitizeStringKeepBrackets(final String input) {
		String temp;
		String output;

		temp = input.replace(" ", "");
		temp = temp.replace("-", "");
		temp = temp.replace("_", "");
		temp = temp.replace("?", "");
		temp = temp.replace("!", "");
		temp = temp.replace("@", "");
		temp = temp.replace("#", "");
		temp = temp.replace(" ", "");
		output = temp;
		return output;

	}

	public static int calculateVoltageTier(final int Voltage) {
		int V;
		if (Voltage == 8) {
			V = 0;
		} else if (Voltage == 32) {
			V = 1;
		} else if (Voltage == 128) {
			V = 2;
		} else if (Voltage == 512) {
			V = 3;
		} else if (Voltage == 2048) {
			V = 4;
		} else if (Voltage == 8196) {
			V = 5;
		} else if (Voltage == 32768) {
			V = 6;
		} else if (Voltage == 131072) {
			V = 7;
		} else if (Voltage == 524288) {
			V = 8;
		} else if (Voltage == Integer.MAX_VALUE) {
			V = 9;
		} else {
			V = -1;
		}
		return V;
	}

	public static String[] parseVersion(final String version) {
		return parseVersion(version, "//.");
	}

	public static String[] parseVersion(final String version, final String delimiter) {
		final String[] versionArray = version.split(delimiter);
		return versionArray;
	}

	public static Versioning compareModVersion(final String currentVersion, final String expectedVersion) {
		return compareModVersion(currentVersion, expectedVersion, "//.");
	}

	public static Versioning compareModVersion(final String currentVersion, final String expectedVersion,
			final String delimiter) {
		final String[] a = parseVersion(currentVersion, delimiter);
		final String[] b = parseVersion(expectedVersion, delimiter);
		final int[] c = new int[a.length];
		final int[] d = new int[b.length];
		for (int r = 0; r < a.length; r++) {
			c[r] = Integer.parseInt(a[r]);
		}
		for (int r = 0; r < b.length; r++) {
			d[r] = Integer.parseInt(b[r]);
		}
		final Versioning[] e = new Versioning[MathUtils.returnLargestNumber(c.length, d.length)];
		for (int r = 0; r < e.length; r++) {

			if (c[r] > d[r]) {
				e[r] = Versioning.NEWER;
			} else if (c[r] < d[r]) {
				e[r] = Versioning.OLDER;
			} else if (c[r] == d[r]) {
				e[r] = Versioning.EQUAL;
			}
		}

		for (int r = 0; r < e.length; r++) {
			if (e[0] == Versioning.NEWER) {
				return Versioning.NEWER;
			} else if (e[0] == Versioning.OLDER) {
				return Versioning.OLDER;
			} else {
				if (e[r] == Versioning.OLDER) {

				}

				return Versioning.NEWER;
			}
		}

		return null;
	}

	public static ToolMaterial generateToolMaterialFromGT(final Materials gtMaterial) {
		final String name = Utils.sanitizeString(gtMaterial.mDefaultLocalName);
		final int harvestLevel = gtMaterial.mToolQuality;
		final int durability = gtMaterial.mDurability;
		final float damage = gtMaterial.mToolQuality;
		final int efficiency = (int) gtMaterial.mToolSpeed;
		final int enchantability = gtMaterial.mEnchantmentToolsLevel;
		final ToolMaterial temp = EnumHelper.addToolMaterial(name, harvestLevel, durability, efficiency, damage,
				enchantability);
		return temp;

	}

	public static ToolMaterial generateToolMaterial(final Material material) {
		final String name = material.getLocalizedName();
		final int harvestLevel = material.vHarvestLevel;
		final int durability = (int) material.vDurability;
		final float damage = material.vToolQuality;
		final int efficiency = material.vToolQuality;
		// int enchantability = material.mEnchantmentToolsLevel;
		Utils.LOG_INFO("ToolMaterial stats for " + material.getLocalizedName() + " | harvestLevel:" + harvestLevel
				+ " | durability:" + durability + " | toolQuality:" + damage + " | toolSpeed:" + damage);
		final ToolMaterial temp = EnumHelper.addToolMaterial(name, harvestLevel, durability, efficiency, damage, 0);
		return temp;

	}

	public static enum Versioning {
		EQUAL(0), NEWER(1), OLDER(-1);
		private final int versioningInfo;

		private Versioning(final int versionStatus) {
			this.versioningInfo = versionStatus;
		}

		public int getTexture() {
			return this.versioningInfo;
		}
	}

	private static int sBookCount = 0;

	public static int getBookCount() {
		return sBookCount;
	}

	public static ItemStack getWrittenBook(final ItemStack aBook, final int aID, final String aMapping, final String aTitle, final String aAuthor,
			final String[] aPages) {
		if (GT_Utility.isStringInvalid(aMapping)) {
			return null;
		}
		ItemStack rStack = CORE.sBookList.get(aMapping);
		if (rStack != null) {
			return GT_Utility.copyAmount(1L, new Object[] { rStack });
		}
		if ((GT_Utility.isStringInvalid(aTitle)) || (GT_Utility.isStringInvalid(aAuthor)) || (aPages.length <= 0)) {
			return null;
		}
		sBookCount += 1;
		final int vMeta = (aID == -1 ? sBookCount : aID);
		rStack = (aBook == null ? new ItemStack(ModItems.itemCustomBook, 1, vMeta) : aBook);
		final NBTTagCompound tNBT = new NBTTagCompound();
		tNBT.setString("title", GT_LanguageManager.addStringLocalization(
				new StringBuilder().append("Book.").append(aTitle).append(".Name").toString(), aTitle));
		tNBT.setString("author", aAuthor);
		final NBTTagList tNBTList = new NBTTagList();
		for (byte i = 0; i < aPages.length; i = (byte) (i + 1)) {
			aPages[i] = GT_LanguageManager
					.addStringLocalization(new StringBuilder().append("Book.").append(aTitle).append(".Page")
							.append((i < 10) ? new StringBuilder().append("0").append(i).toString() : Byte.valueOf(i))
							.toString(), aPages[i]);
			if (i < 48) {
				if (aPages[i].length() < 256) {
					tNBTList.appendTag(new NBTTagString(aPages[i]));
				}
				else {
					GT_Log.err.println(new StringBuilder().append("WARNING: String for written Book too long! -> ")
							.append(aPages[i]).toString());
				}
			} else {
				GT_Log.err.println(new StringBuilder().append("WARNING: Too much Pages for written Book! -> ")
						.append(aTitle).toString());
				break;
			}
		}
		tNBTList.appendTag(new NBTTagString(new StringBuilder().append("Credits to ").append(aAuthor)
				.append(" for writing this Book. This was Book Nr. ").append(sBookCount)
				.append(" at its creation. Gotta get 'em all!").toString()));
		tNBT.setTag("pages", tNBTList);
		rStack.setTagCompound(tNBT);
		GT_Log.out.println(new StringBuilder().append("GT++_Mod: Added Book to Book++ List  -  Mapping: '")
				.append(aMapping).append("'  -  Name: '").append(aTitle).append("'  -  Author: '").append(aAuthor)
				.append("'").toString());
		NBTUtils.createIntegerTagCompound(rStack, "stats", "mMeta", vMeta);
		CORE.sBookList.put(aMapping, rStack);
		Utils.LOG_INFO("Creating book: " + aTitle + " by " + aAuthor + ". Using Meta " + vMeta + ".");
		return GT_Utility.copy(new Object[] { rStack });
	}

	@SuppressWarnings({ "unused", "unchecked" })
	public static Pair<Integer, Integer> getGregtechVersion(){
		Pair<Integer, Integer> version;
		if (GT_Mod.VERSION == 509){
			Class<GT_Mod> clazz;
			try {
				clazz = (Class<GT_Mod>) Class.forName("gregtech.GT_Mod");
				Field mSubversion = ReflectionUtils.getField(clazz, "SUBVERSION");
				if (mSubversion != null){
					int mSub = 0;
					mSub = mSubversion.getInt(clazz);
					if (mSub != 0){
						version = new Pair<Integer, Integer>(9, mSub);
						return version;
					}
				}
			}
			catch (Throwable t){}
		}
		//5.08.33
		else if (GT_Mod.VERSION == 508){
			version = new Pair<Integer, Integer>(8, 33);
			return version;

		}
		//5.07.07
		else if (GT_Mod.VERSION == 507){
			version = new Pair<Integer, Integer>(7, 7);
			return version;

		}
		//Returb Bad Value
		version = new Pair<Integer, Integer>(0, 0);
		return version;	
	}
	
	public static int getGregtechVersionAsInt(){
		Pair<Integer, Integer> ver = getGregtechVersion();
		return 50000+(ver.getKey()*100)+(ver.getValue());
	}
	
	public static String getGregtechVersionAsString(){
		Pair<Integer, Integer> ver = getGregtechVersion();
		return "5."+ver.getKey()+"."+ver.getValue();		
	}
	
	public static int getGregtechSubVersion(){
		Pair<Integer, Integer> ver = getGregtechVersion();		
		return ver.getValue();		
	}
	
	public static SecureRandom generateSecureRandom(){
		SecureRandom secRan;
		String secRanType;		
		
		if (SystemUtils.isWindows()){
			secRanType = "Windows-PRNG";
		}
		else {
			secRanType = "NativePRNG";
		}		
		try {
			secRan = SecureRandom.getInstance(secRanType);
			// Default constructor would have returned insecure SHA1PRNG algorithm, so make an explicit call.
			byte[] b = new byte[64] ;
			secRan.nextBytes(b);
			return secRan;
		}
		catch (NoSuchAlgorithmException e) {
			return null;
		} 
	}	
	

}
