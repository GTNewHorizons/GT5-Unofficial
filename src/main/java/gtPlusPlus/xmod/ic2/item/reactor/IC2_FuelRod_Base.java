package gtPlusPlus.xmod.ic2.item.reactor;

import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import net.minecraft.item.ItemStack;

public class IC2_FuelRod_Base implements IReactorComponent {

	@Override
	public void processChamber(IReactor var1, ItemStack var2, int var3, int var4, boolean var5) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean acceptUraniumPulse(IReactor var1, ItemStack var2, ItemStack var3, int var4, int var5, int var6,
			int var7, boolean var8) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canStoreHeat(IReactor var1, ItemStack var2, int var3, int var4) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getMaxHeat(IReactor var1, ItemStack var2, int var3, int var4) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCurrentHeat(IReactor var1, ItemStack var2, int var3, int var4) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int alterHeat(IReactor var1, ItemStack var2, int var3, int var4, int var5) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float influenceExplosion(IReactor var1, ItemStack var2) {
		// TODO Auto-generated method stub
		return 0;
	}

}
