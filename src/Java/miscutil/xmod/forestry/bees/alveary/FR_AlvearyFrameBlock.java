package miscutil.xmod.forestry.bees.alveary;

import net.minecraft.util.ChunkCoordinates;

import com.mojang.authlib.GameProfile;

import forestry.api.multiblock.IAlvearyComponent;
import forestry.api.multiblock.IMultiblockComponent;
import forestry.api.multiblock.IMultiblockController;
import forestry.api.multiblock.IMultiblockLogicAlveary;

public class FR_AlvearyFrameBlock implements IAlvearyComponent, IMultiblockComponent{

	@Override
	public ChunkCoordinates getCoordinates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GameProfile getOwner() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onMachineAssembled(IMultiblockController arg0,
			ChunkCoordinates arg1, ChunkCoordinates arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMachineBroken() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IMultiblockLogicAlveary getMultiblockLogic() {
		// TODO Auto-generated method stub
		return null;
	}

}
