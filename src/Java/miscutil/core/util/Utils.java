package miscutil.core.util;

import gregtech.api.enums.TC_Aspects;
import gregtech.api.enums.TC_Aspects.TC_AspectStack;
import ic2.core.IC2Potion;
import ic2.core.Ic2Items;
import ic2.core.init.InternalName;
import ic2.core.item.armor.ItemArmorHazmat;
import ic2.core.item.resources.ItemCell;

import java.awt.Color;
import java.awt.Graphics;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import miscutil.MiscUtils;
import miscutil.core.lib.CORE;
import miscutil.core.util.fluid.FluidUtils;
import miscutil.core.util.item.UtilsItems;
import miscutil.core.util.math.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
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

	public static TC_AspectStack getTcAspectStack (TC_Aspects aspect, int size){

		TC_AspectStack returnValue = null;

		if (aspect.name().toUpperCase() == "COGNITIO"){
			//Adds in Compat for older GT Versions which Misspell aspects.
			try {
				returnValue = new TC_AspectStack(TC_Aspects.valueOf("COGNITIO"), size);
			} catch (NoSuchFieldError r){
				Utils.LOG_INFO("Fallback TC Aspect found - "+aspect.name()+" - PLEASE UPDATE GREGTECH TO A NEWER VERSION TO REMOVE THIS MESSAGE - THIS IS NOT AN ERROR");
				returnValue = new TC_AspectStack(TC_Aspects.valueOf("COGNITO"), size);

			}
		}
		else if (aspect.name().toUpperCase() == "EXANIMUS"){
			//Adds in Compat for older GT Versions which Misspell aspects.
			try {
				returnValue = new TC_AspectStack(TC_Aspects.valueOf("EXANIMUS"), size);
			} catch (NoSuchFieldError r){
				Utils.LOG_INFO("Fallback TC Aspect found - "+aspect.name()+" - PLEASE UPDATE GREGTECH TO A NEWER VERSION TO REMOVE THIS MESSAGE - THIS IS NOT AN ERROR");
				returnValue = new TC_AspectStack(TC_Aspects.valueOf("EXAMINIS"), size);
			}
		}
		else if (aspect.name().toUpperCase() == "PRAECANTATIO"){
			//Adds in Compat for older GT Versions which Misspell aspects.
			try {
				returnValue = new TC_AspectStack(TC_Aspects.valueOf("PRAECANTATIO"), size);
			} catch (NoSuchFieldError r){
				Utils.LOG_INFO("Fallback TC Aspect found - "+aspect.name()+" - PLEASE UPDATE GREGTECH TO A NEWER VERSION TO REMOVE THIS MESSAGE - THIS IS NOT AN ERROR");
				returnValue = new TC_AspectStack(TC_Aspects.valueOf("PRAECANTIO"), size);
			}
		}
		else {
			returnValue = new TC_AspectStack(aspect, size);
		}

		return returnValue;
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
		FMLLog.info("GT++: "+s);
		//}
	}

	//Developer Comments
	public static void LOG_WARNING(String s){
		if (CORE.DEBUG){
			FMLLog.warning("GT++: "+s);
		}
	}

	//Errors
	public static void LOG_ERROR(String s){
		if (CORE.DEBUG){
			FMLLog.severe("GT++: "+s);
		}
	}

	//Developer Logger
	public static void LOG_SPECIFIC_WARNING(String whatToLog, String msg, int line){
		if (CORE.DEBUG){		
			FMLLog.warning("GT++ |"+line+"| "+whatToLog+" | "+msg);
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
		if (aFluid == null) return false;
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
			int l = MathUtils.randInt(0, 4);
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

	public static int rgbtoHexValue(int r, int g, int b){		    
		Color c = new Color(r,g,b);
		String temp = Integer.toHexString( c.getRGB() & 0xFFFFFF ).toUpperCase();	

		//System.out.println( "hex: " + Integer.toHexString( c.getRGB() & 0xFFFFFF ) + " hex value:"+temp); 
		temp = Utils.appenedHexNotationToString(String.valueOf(temp));
		Utils.LOG_WARNING("Made "+temp+" - Hopefully it's not a mess.");
		Utils.LOG_WARNING("It will decode into "+Integer.decode(temp)+".");
		return Integer.decode(temp);
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

			if (((String) hexAsStringOrInt).length() != 6){
				String temp = leftPadWithZeroes((String) hexAsStringOrInt, 6);
				result = temp;
			}
			result = hexChar+hexAsStringOrInt;
			return result;
		}
		else if (hexAsStringOrInt.getClass() == Integer.class){
			;
			if (((String) hexAsStringOrInt).length() != 6){
				String temp = leftPadWithZeroes((String) hexAsStringOrInt, 6);
				result = temp;
			}
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

	public static boolean doesEntryExistAlreadyInOreDictionary(String OreDictName){
		if (OreDictionary.getOres(OreDictName).size() != 0) {
			return true;
		}
		return false;
	}

	public static boolean invertBoolean(boolean booleans){
		if (booleans == true){
			return false;
		}
		return true;
	}

	public static boolean applyRadiationDamageToEntity(int damage, World world, Entity entityHolding){
		if (!world.isRemote){				
			if (damage > 0 && (entityHolding instanceof EntityLivingBase)) {
				EntityLivingBase entityLiving = (EntityLivingBase) entityHolding;
				if (!ItemArmorHazmat.hasCompleteHazmat(entityLiving)) {
					int duration;
					if (entityLiving.getActivePotionEffect(IC2Potion.radiation) != null){
						//Utils.LOG_INFO("t");
						duration = (damage*5)+entityLiving.getActivePotionEffect(IC2Potion.radiation).getDuration();
					}
					else {
						//Utils.LOG_INFO("f");
						duration = damage*30;
					}					
					IC2Potion.radiation.applyTo(entityLiving, duration, damage * 15);
				}
			}
			return true;
		}
		return false;		
	}

	private static short cellID = 15;
	public static ItemStack createInternalNameAndFluidCell(String s){
		Utils.LOG_WARNING("1");
		InternalName yourName = EnumHelper.addEnum(InternalName.class, s, new Class[0], new Object[0]);
		Utils.LOG_WARNING("2 "+yourName.name());
		ItemCell item = (ItemCell)Ic2Items.cell.getItem();
		Utils.LOG_WARNING("3 "+item.getUnlocalizedName());
		try
		{
			Utils.LOG_WARNING("4");
			Class<? extends ItemCell> clz = item.getClass();
			Utils.LOG_WARNING("5 "+clz.getSimpleName());
			Method methode = clz.getDeclaredMethod("addCell", int.class, InternalName.class, Block[].class);
			Utils.LOG_WARNING("6 "+methode.getName());
			methode.setAccessible(true);
			Utils.LOG_WARNING("7 "+methode.isAccessible());
			ItemStack temp = (ItemStack) methode.invoke(item, cellID++, yourName, new Block[0]);
			Utils.LOG_INFO("Successfully created "+temp.getDisplayName()+"s.");
			FluidContainerRegistry.registerFluidContainer(FluidUtils.getFluidStack(s.toLowerCase(), 0), temp.copy(), Ic2Items.cell.copy());
			UtilsItems.addItemToOreDictionary(temp.copy(), "cell"+s);
			return temp;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}


}


