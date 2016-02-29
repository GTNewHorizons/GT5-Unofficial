package miscutil.core.util;

import static gregtech.api.enums.GT_Values.F;

import java.awt.Graphics;

import miscutil.core.lib.Strings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.common.FMLLog;

public class Utils {

	public static final int WILDCARD_VALUE = Short.MAX_VALUE;
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
    	//if (Strings.DEBUG){
			FMLLog.info("MiscUtils: "+s);
		//}
    }
    
    //Developer Comments
    public static void LOG_WARNING(String s){
    	if (Strings.DEBUG){
			FMLLog.warning("MiscUtils: "+s);
		}
    }
    
    //Errors
    public static void LOG_ERROR(String s){
    	if (Strings.DEBUG){
			FMLLog.severe("MiscUtils: "+s);
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
    
}
