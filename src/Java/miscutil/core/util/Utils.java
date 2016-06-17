package miscutil.core.util;

import static gregtech.api.enums.GT_Values.F;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import miscutil.core.lib.CORE;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.common.FMLLog;

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
	 * <code>Integer.MAX_VALUE - 1</code>.
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
		if (input == null && target != null || input != null && target == null)
		{
			return false;
		}
		return (target.getItem() == input.getItem() && ((target.getItemDamage() == WILDCARD_VALUE && !strict) || target.getItemDamage() == input.getItemDamage()));
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

	
}


