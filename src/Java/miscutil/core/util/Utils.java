package miscutil.core.util;

import static gregtech.api.enums.GT_Values.F;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import miscutil.MiscUtils;
import miscutil.core.lib.CORE;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.EntityRegistry;

public class Utils {

	public static final int WILDCARD_VALUE = Short.MAX_VALUE;

	static class ShortTimerTask extends TimerTask {
		@Override
		public void run() {
			Utils.LOG_WARNING("Timer expired.");
		}
	}

	/**
	 * Returns a psuedo-random number between min and max, inclusive.
	 * The difference between min and max can be at most
	 * Integer.MAX_VALUE - 1.
	 *
	 * @param min Minimim value
	 * @param max Maximim value.  Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	public static int randInt(int min, int max) {

		// Usually this can be a field rather than a method variable
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	public static long randLong(long min, long max) {
		// Usually this can be a field rather than a method variable
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		long randomNum = nextLong(rand,(max - min) + 1) + min;

		return randomNum;
	}

	private static long nextLong(Random rng, long n) {
		// error checking and 2^x checking removed for simplicity.
		long bits, val;
		do {
			bits = (rng.nextLong() << 1) >>> 1;
			val = bits % n;
		} while (bits-val+(n-1) < 0L);
		return val;
	}

	public static boolean containsMatch(boolean strict, ItemStack[] inputs, ItemStack... targets)
	{
		for (ItemStack input : inputs)
		{
			for (ItemStack target : targets)
			{
				if (itemMatches(target, input, strict))
				{
					return true;
				}
			}
		}
		return false;
	}

	public static boolean itemMatches(ItemStack target, ItemStack input, boolean strict)
	{
		if (input == null || target == null)
		{
			return false;
		}
		return (target.getItem() == input.getItem() && ((target.getItemDamage() == WILDCARD_VALUE && !strict) || target.getItemDamage() == input.getItemDamage()));
	}

	//TODO
	public static void registerEntityToBiomeSpawns(Class<EntityLiving> classy, EnumCreatureType EntityType, BiomeGenBase baseBiomeGen){
		EntityRegistry.addSpawn(classy, 6, 1, 5, EntityType, baseBiomeGen); //change the values to vary the spawn rarity, biome, etc. 
	}

	//Non-Dev Comments 
	public static void LOG_INFO(String s){
		//if (CORE.DEBUG){
		FMLLog.info("MiscUtils: "+s);
		//}
	}

	//Developer Comments
	public static void LOG_WARNING(String s){
		if (CORE.DEBUG){
			FMLLog.warning("MiscUtils: "+s);
		}
	}

	//Errors
	public static void LOG_ERROR(String s){
		if (CORE.DEBUG){
			FMLLog.severe("MiscUtils: "+s);
		}
	}

	//Developer Logger
	public static void LOG_SPECIFIC_WARNING(String whatToLog, String msg, int line){
		if (CORE.DEBUG){		
			FMLLog.warning("MiscUtils |"+line+"| "+whatToLog+" | "+msg);
		}			
	}

	public static void paintBox(Graphics g, int MinA, int MinB, int MaxA, int MaxB){
		g.drawRect (MinA, MinB, MaxA, MaxB);  
	}

	public static void messagePlayer(EntityPlayer P, String S){
		gregtech.api.util.GT_Utility.sendChatToPlayer(P, S);
	}

	/**
	 * Returns if that Liquid is IC2Steam.
	 */
	public static boolean isIC2Steam(FluidStack aFluid) {
		if (aFluid == null) return F;
		return aFluid.isFluidEqual(getIC2Steam(1));
	}

	/**
	 * Returns a Liquid Stack with given amount of IC2Steam.
	 */
	public static FluidStack getIC2Steam(long aAmount) {
		return FluidRegistry.getFluidStack("ic2steam", (int)aAmount);
	}



	/*public static void recipeBuilderBlock(ItemStack slot_1, ItemStack slot_2, ItemStack slot_3, ItemStack slot_4, ItemStack slot_5, ItemStack slot_6, ItemStack slot_7, ItemStack slot_8, ItemStack slot_9, Block resultBlock){
		GameRegistry.addRecipe(new ItemStack(resultBlock),
				new Object[] {"ABC", "DEF", "GHI",
			'A',slot_1,'B',slot_2,'C',slot_3,
			'D',slot_4,'E',slot_5,'F',slot_6,
			'G',slot_7,'H',slot_8,'I',slot_9
		});			
	}*/

	public static String checkCorrectMiningToolForBlock(Block currentBlock, World currentWorld){
		String correctTool = "";
		if (!currentWorld.isRemote){			
			try {
				correctTool = currentBlock.getHarvestTool(0);
				Utils.LOG_WARNING(correctTool);

			} catch (NullPointerException e){

			}
		}

		return correctTool;
	}

	/**
	 * 
	 * @param colorStr e.g. "#FFFFFF"
	 * @return String - formatted "rgb(0,0,0)"
	 */
	public static String hex2Rgb(String hexString) {
		Color c = new Color(
				Integer.valueOf(hexString.substring(1, 3), 16), 
				Integer.valueOf(hexString.substring(3, 5), 16), 
				Integer.valueOf(hexString.substring(5, 7), 16));

		StringBuffer sb = new StringBuffer();
		sb.append("rgb(");
		sb.append(c.getRed());
		sb.append(",");
		sb.append(c.getGreen());
		sb.append(",");
		sb.append(c.getBlue());
		sb.append(")");
		return sb.toString();
	}

	//Smooth Rounding Function
	public static double decimalRounding(double d) {
		return Math.round(d * 2) / 2.0;
	}

	//Smooth Rounding Function (Nearest 5)
	public static double decimalRoundingToWholes(double d) {
		return 5*(Math.round(d/5));
	}

	//Can be divided by
	public static boolean divideXintoY(int x, int y){
		if ((x % y) == 0)
		{
			return true;
		}
		return false;
	}

	//Converts temps for GT machines, then rounds for ease of use.
	public static float celsiusToKelvin(int i){
		double f = i + 273.15F;
		return (int)decimalRoundingToWholes(f);
	}

	public static Timer ShortTimer(int seconds) {
		Timer timer;
		timer = new Timer();
		timer.schedule(new ShortTimerTask(), seconds * 1000);
		return timer;
	}

	public static String byteToHex(byte b) {
		int i = b & 0xFF;
		return Integer.toHexString(i);
	}

	public static Object[] convertListToArray(List<Object> sourceList) {	
		Object[] targetArray = sourceList.toArray(new Object[sourceList.size()]);
		return targetArray;
	}

	public static List<Object> convertArrayToListFixed(Object[] sourceArray) {
		List<Object> targetList = Arrays.asList(sourceArray);
		return targetList;
	}

	public static List<Object> convertArrayToList(Object[] sourceArray) {
		List<Object> targetList = new ArrayList<Object>(Arrays.asList(sourceArray));
		return targetList;
	}

	public static EntityPlayer getPlayerOnServerFromUUID(UUID parUUID){
		if (parUUID == null) 
		{
			return null;
		}
		List<EntityPlayerMP> allPlayers = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		for (EntityPlayerMP player : allPlayers) 
		{
			if (player.getUniqueID().equals(parUUID)) 
			{
				return player;
			}
		}
		return null;
	}

	@Deprecated
	public static Block findBlockUnderEntityNonBoundingBox(Entity parEntity){
		int blockX = MathHelper.floor_double(parEntity.posX);
		int blockY = MathHelper.floor_double(parEntity.posY-0.2D - (double)parEntity.yOffset);
		int blockZ = MathHelper.floor_double(parEntity.posZ);
		return parEntity.worldObj.getBlock(blockX, blockY, blockZ);
	}

	public static Block findBlockUnderEntity(Entity parEntity){
		int blockX = MathHelper.floor_double(parEntity.posX);
		int blockY = MathHelper.floor_double(parEntity.boundingBox.minY)-1;
		int blockZ = MathHelper.floor_double(parEntity.posZ);
		return parEntity.worldObj.getBlock(blockX, blockY, blockZ);
	}

	public static int getFacingDirection(Entity entity){
		int d = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360) + 0.50) & 3;
		return d;
	}

	public static boolean isPlayerOP(EntityPlayer player){
		if (player.canCommandSenderUseCommand(2, "")){
			return true;
		}
		return false;
	}

	public static void setEntityOnFire(Entity entity, int length){
		entity.setFire(length);
	}

	public static void spawnCustomParticle(Entity entity){
		MiscUtils.proxy.generateMysteriousParticles(entity);
	}	

	public static void spawnFX(World world, int x, int y, int z, String particleName, Object particleName2){
		if (!world.isRemote){
			if (particleName2 == null || particleName2.equals("")){
				particleName2 = particleName;
			}
			int l = randInt(0, 4);
			double d0 = (double)((float)x + 0.5F);
			double d1 = (double)((float)y + 0.7F);
			double d2 = (double)((float)z + 0.5F);
			double d3 = 0.2199999988079071D;
			double d4 = 0.27000001072883606D;

			if (l == 1)
			{
				world.spawnParticle(particleName, d0 - d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D);
			}
			else if (l == 2)
			{
				world.spawnParticle((String) particleName2, d0 + d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D);
			}
			else if (l == 3)
			{
				world.spawnParticle(particleName, d0, d1 + d3, d2 - d4, 0.0D, 0.0D, 0.0D);
			}
			else if (l == 4)
			{
				world.spawnParticle((String) particleName2, d0, d1 + d3, d2 + d4, 0.0D, 0.0D, 0.0D);
			}
			else
			{
				world.spawnParticle(particleName, d0, d1, d2, 0.0D, 0.0D, 0.0D);
				if (particleName2 != null){
					world.spawnParticle((String) particleName2, d0, d1, d2, 0.0D, 0.0D, 0.0D);
				}
			}
		}
	}

	public static int getHexNumberFromInt(int myRandomNumber){
		String result = Integer.toHexString(myRandomNumber);
		int resultINT = Integer.getInteger(result);
		return resultINT;
	}

	public static int generateRandomHexValue(int min, int max){
		int result = getHexNumberFromInt(randInt(min, max));
		return result;
	}

	/*
	 * http://javadevnotes.com/java-left-pad-string-with-zeros-examples
	 */
	public static String leftPadWithZeroes(String originalString, int length) {
		StringBuilder sb = new StringBuilder();
		while (sb.length() + originalString.length() < length) {
			sb.append('0');
		}
		sb.append(originalString);
		String paddedString = sb.toString();
		return paddedString;
	}

	/*
	 * Original Code by Chandana Napagoda - https://cnapagoda.blogspot.com.au/2011/03/java-hex-color-code-generator.html
	 */	
	public static Map<Integer, String> hexColourGenerator(int colorCount){
		int maxColorValue = 16777215;
		// this is decimal value of the "FFFFFF"
		int devidedvalue = maxColorValue/colorCount;
		int countValue = 0;
		HashMap<Integer, String> hexColorMap = new HashMap<Integer, String>();
		for(int a=0; a < colorCount && maxColorValue >= countValue ; a++){
			if(a != 0){
				countValue+=devidedvalue;
				hexColorMap.put(a,Integer.toHexString( 0x10000 | countValue).substring(1).toUpperCase());
			}
			else {
				hexColorMap.put(a,Integer.toHexString( 0x10000 | countValue).substring(1).toUpperCase());
			}
		}
		return hexColorMap;
	}

	/*
	 * Original Code by Chandana Napagoda - https://cnapagoda.blogspot.com.au/2011/03/java-hex-color-code-generator.html
	 */
	public static Map<Integer, String> hexColourGeneratorRandom(int colorCount){
		HashMap<Integer, String> hexColorMap = new HashMap<Integer, String>();
		for(int a=0;a < colorCount; a++){
			String code = ""+(int)(Math.random()*256);
			code = code+code+code;
			int  i = Integer.parseInt(code);
			hexColorMap.put(a,Integer.toHexString( 0x1000000 | i).substring(1).toUpperCase());
			Utils.LOG_INFO(""+Integer.toHexString( 0x1000000 | i).substring(1).toUpperCase());
		}
		return hexColorMap;
	}

	public static String appenedHexNotationToString(Object hexAsStringOrInt){
		String hexChar = "0x";
		String result;
		if (hexAsStringOrInt.getClass() == String.class){
			result = hexChar+hexAsStringOrInt;
			if (result.length() != 6){
				String temp = leftPadWithZeroes(result, 6);
				result = temp;
			}
			return result;
		}
		else if (hexAsStringOrInt.getClass() == Integer.class){
			result = hexChar+String.valueOf(hexAsStringOrInt);
			return result;
		}
		else {
			return null;
		}
	}

	public static Integer appenedHexNotationToInteger(int hexAsStringOrInt){
		String hexChar = "0x";
		String result;
		Utils.LOG_INFO(String.valueOf(hexAsStringOrInt));
		result = hexChar+String.valueOf(hexAsStringOrInt);
		return Integer.getInteger(result);
	}

	public static int generateSingularRandomHexValue(){
		String temp;
		int usefuleNumber = 0;
		int tDecided = randInt(1, 5);
		final Map<Integer, String> colours = Utils.hexColourGeneratorRandom(5);

			if (colours.get(tDecided) != null && colours.size() > 0){				
					usefuleNumber = Integer.getInteger(colours.get(tDecided));		
			}
			else {
				usefuleNumber = 123456;	
			}

		Utils.LOG_INFO("Operating with "+usefuleNumber);	
		temp = Utils.appenedHexNotationToString(String.valueOf(usefuleNumber));
		Utils.LOG_INFO("Made "+temp+" - Hopefully it's not a mess.");	
		return Integer.decode(temp);
	}

}


