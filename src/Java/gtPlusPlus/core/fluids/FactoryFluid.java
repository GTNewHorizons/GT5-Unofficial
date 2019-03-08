package gtPlusPlus.core.fluids;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraftforge.fluids.Fluid;

public class FactoryFluid extends Fluid {


		public FactoryFluid(String fluidName, int luminosity, int density, int temp, int viscosity) {
			this(fluidName, null, luminosity, density, temp, viscosity, false, EnumRarity.common);
		}
		
		
		public FactoryFluid(String fluidName, Block aBlock, int luminosity, int density, int temp, int viscosity, boolean gas, EnumRarity aRarity) {
			super(fluidName);
			this.setBlock(aBlock);
			this.setLuminosity(luminosity);
			this.setDensity(density);
			this.setTemperature(temp);
			this.setViscosity(viscosity);
			this.setGaseous(gas);
			this.setRarity(aRarity);
		}
		
	}