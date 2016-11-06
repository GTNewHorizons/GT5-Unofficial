package gtPlusPlus.xmod.forestry.bees.alveary;

import java.io.IOException;
import java.util.List;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.biome.BiomeGenBase;
import forestry.api.apiculture.*;
import forestry.api.core.*;
import forestry.api.multiblock.IAlvearyComponent;
import forestry.api.multiblock.IMultiblockController;
import forestry.apiculture.multiblock.MultiblockLogicAlveary;
import forestry.core.access.*;
import forestry.core.config.Config;
import forestry.core.gui.IGuiHandlerForestry;
import forestry.core.gui.IHintSource;
import forestry.core.inventory.IInventoryAdapter;
import forestry.core.multiblock.MultiblockTileEntityForestry;
import forestry.core.network.*;
import forestry.core.tiles.IClimatised;
import forestry.core.tiles.ITitled;

public abstract class FR_TileAlveary
extends MultiblockTileEntityForestry<MultiblockLogicAlveary>
implements IBeeHousing, IAlvearyComponent, IRestrictedAccess, IStreamableGui, ITitled, IClimatised, IHintSource, IGuiHandlerForestry
{
	private final String unlocalizedTitle;

	protected FR_TileAlveary()
	{
		this(FR_BlockAlveary.Type.ERROR);
	}

	protected FR_TileAlveary(FR_BlockAlveary.Type type)
	{
		super(new MultiblockLogicAlveary());
		this.unlocalizedTitle = ("advanced.tile.for.alveary." + type.ordinal() + ".name");
		
	}

	public int getIcon(int side)
	{
		return 0;
	}

	@Override
	public void onMachineAssembled(IMultiblockController multiblockController, ChunkCoordinates minCoord, ChunkCoordinates maxCoord)
	{
		if (this.worldObj.isRemote) {
			this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		}
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, getBlockType());
		markDirty();
	}

	@Override
	public void onMachineBroken()
	{
		if (this.worldObj.isRemote) {
			this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		}
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, getBlockType());
		markDirty();
	}

	@Override
	public BiomeGenBase getBiome()
	{
		return ((MultiblockLogicAlveary)getMultiblockLogic()).getController().getBiome();
	}

	@Override
	public Iterable<IBeeModifier> getBeeModifiers()
	{
		return ((MultiblockLogicAlveary)getMultiblockLogic()).getController().getBeeModifiers();
	}

	@Override
	public Iterable<IBeeListener> getBeeListeners()
	{
		return ((MultiblockLogicAlveary)getMultiblockLogic()).getController().getBeeListeners();
	}

	@Override
	public IBeeHousingInventory getBeeInventory()
	{
		return ((MultiblockLogicAlveary)getMultiblockLogic()).getController().getBeeInventory();
	}

	@Override
	public IBeekeepingLogic getBeekeepingLogic()
	{
		return ((MultiblockLogicAlveary)getMultiblockLogic()).getController().getBeekeepingLogic();
	}

	@Override
	public Vec3 getBeeFXCoordinates()
	{
		return ((MultiblockLogicAlveary)getMultiblockLogic()).getController().getBeeFXCoordinates();
	}

	@Override
	public EnumTemperature getTemperature()
	{
		return ((MultiblockLogicAlveary)getMultiblockLogic()).getController().getTemperature();
	}

	@Override
	public EnumHumidity getHumidity()
	{
		return ((MultiblockLogicAlveary)getMultiblockLogic()).getController().getHumidity();
	}

	@Override
	public int getBlockLightValue()
	{
		return ((MultiblockLogicAlveary)getMultiblockLogic()).getController().getBlockLightValue();
	}

	@Override
	public boolean canBlockSeeTheSky()
	{
		return ((MultiblockLogicAlveary)getMultiblockLogic()).getController().canBlockSeeTheSky();
	}

	@Override
	public IErrorLogic getErrorLogic()
	{
		return ((MultiblockLogicAlveary)getMultiblockLogic()).getController().getErrorLogic();
	}

	@Override
	public IAccessHandler getAccessHandler()
	{
		return ((MultiblockLogicAlveary)getMultiblockLogic()).getController().getAccessHandler();
	}

	@Override
	public void onSwitchAccess(EnumAccess oldAccess, EnumAccess newAccess)
	{
		((MultiblockLogicAlveary)getMultiblockLogic()).getController().onSwitchAccess(oldAccess, newAccess);
	}

	@Override
	public IInventoryAdapter getInternalInventory()
	{
		return ((MultiblockLogicAlveary)getMultiblockLogic()).getController().getInternalInventory();
	}

	@Override
	public String getUnlocalizedTitle()
	{
		return this.unlocalizedTitle;
	}

	@Override
	public List<String> getHints()
	{
		return Config.hints.get("apiary");
	}

	@Override
	public float getExactTemperature()
	{
		return ((MultiblockLogicAlveary)getMultiblockLogic()).getController().getExactTemperature();
	}

	@Override
	public float getExactHumidity()
	{
		return ((MultiblockLogicAlveary)getMultiblockLogic()).getController().getExactHumidity();
	}

	@Override
	public void writeGuiData(DataOutputStreamForestry data)
			throws IOException
	{
		((MultiblockLogicAlveary)getMultiblockLogic()).getController().writeGuiData(data);
	}

	@Override
	public void readGuiData(DataInputStreamForestry data)
			throws IOException
	{
		((MultiblockLogicAlveary)getMultiblockLogic()).getController().readGuiData(data);
	}

}
