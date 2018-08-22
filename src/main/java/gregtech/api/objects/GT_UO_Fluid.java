package gregtech.api.objects;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.Random;

import static gregtech.common.GT_UndergroundOil.DIVIDER;

public class GT_UO_Fluid {
	public String Registry = "null";
	public int MaxAmount = 0;
	public int MinAmount = 0;
	public int Chance = 0;
	public int DecreasePerOperationAmount = 5;

	public GT_UO_Fluid(ConfigCategory aConfigCategory) {
		if (aConfigCategory.containsKey("Registry"))
		{
			aConfigCategory.get("Registry").comment = "Fluid registry name";
			Registry = aConfigCategory.get("Registry").getString();
		}
		if (aConfigCategory.containsKey("MaxAmount"))
		{
			aConfigCategory.get("MaxAmount").comment = "Max amount generation (per operation, sets the VeinData) 80000 MAX";
			MaxAmount = aConfigCategory.get("MaxAmount").getInt(0);
		}
		if (aConfigCategory.containsKey("MinAmount"))
		{
			aConfigCategory.get("MinAmount").comment = "Min amount generation (per operation, sets the VeinData) 0 MIN";
			MinAmount = aConfigCategory.get("MinAmount").getInt(0);
		}
		if (aConfigCategory.containsKey("Chance"))
		{
			aConfigCategory.get("Chance").comment = "Chance generating (weighted chance!, there will be a fluid in chunk always!)";
			Chance = aConfigCategory.get("Chance").getInt(0);
		}
		if (aConfigCategory.containsKey("DecreasePerOperationAmount"))
		{
			aConfigCategory.get("DecreasePerOperationAmount").comment = "Decrease per operation (actual fluid gained works like (Litre)VeinData/5000)";
			DecreasePerOperationAmount = aConfigCategory.get("DecreasePerOperationAmount").getInt(5);
		}
		//System.out.println("GT UO "+aConfigCategory.getName()+" Fluid:"+Registry+" Max:"+MaxAmount+" Min:"+MinAmount+" Chance:"+Chance);
	}
	
	public Fluid getFluid(){
		try {
			return FluidRegistry.getFluid(this.Registry);
		} catch (Exception e) {
			return null;
		}
	}
	
	public int getRandomAmount(Random aRandom){//generates some random ass number that correlates to extraction speeds
		int div = (int)Math.floor(Math.pow((MaxAmount-MinAmount)*100.d*DIVIDER, 0.2d));
		int min = (int)Math.floor(Math.pow(MinAmount*100.d*DIVIDER, 0.2d));
        double amount = min+aRandom.nextInt(div)+aRandom.nextDouble();
        return (int) (Math.pow(amount, 5) / 100);//reverses the computation above
	}
}