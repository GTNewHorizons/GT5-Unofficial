package gtPlusPlus.xmod.forestry.bees.alveary;

import java.io.IOException;
import java.util.List;

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
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.biome.BiomeGenBase;

public abstract class FR_TileAlveary
extends MultiblockTileEntityForestry<MultiblockLogicAlveary>
implements IBeeHousing, IAlvearyComponent, IRestrictedAccess, IStreamableGui, ITitled, IClimatised, IHintSource, IGuiHandlerForestry
{
	private final String unlocalizedTitle;

	protected FR_TileAlveary()
	{
		this(FR_BlockAlveary.Type.ERROR);
	}

	protected FR_TileAlveary(final FR_BlockAlveary.Type type)
	{
		super(new MultiblockLogicAlveary());
		this.unlocalizedTitle = ("advanced.tile.for.alveary." + type.ordinal() + ".name");

	}

	public int getIcon(final int side)
	{
		return 0;
	}

	@Override
	public void onMachineAssembled(final IMultiblockController multiblockController, final ChunkCoordinates minCoord, final ChunkCoordinates maxCoord)
	{
		if (this.worldObj.isRemote) {
			this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		}
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
		this.markDirty();
	}

	@Override
	public void onMachineBroken()
	{
		if (this.worldObj.isRemote) {
			this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		}
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
		this.markDirty();
	}

	@Override
	public BiomeGenBase getBiome()
	{
		return this.getMultiblockLogic().getController().getBiome();
	}

	@Override
	public Iterable<IBeeModifier> getBeeModifiers()
	{
		return this.getMultiblockLogic().getController().getBeeModifiers();
	}

	@Override
	public Iterable<IBeeListener> getBeeListeners()
	{
		return this.getMultiblockLogic().getController().getBeeListeners();
	}

	@Override
	public IBeeHousingInventory getBeeInventory()
	{
		return this.getMultiblockLogic().getController().getBeeInventory();
	}

	@Override
	public IBeekeepingLogic getBeekeepingLogic()
	{
		return this.getMultiblockLogic().getController().getBeekeepingLogic();
	}

	@Override
	public Vec3 getBeeFXCoordinates()
	{
		return this.getMultiblockLogic().getController().getBeeFXCoordinates();
	}

	@Override
	public EnumTemperature getTemperature()
	{
		return this.getMultiblockLogic().getController().getTemperature();
	}

	@Override
	public EnumHumidity getHumidity()
	{
		return this.getMultiblockLogic().getController().getHumidity();
	}

	@Override
	public int getBlockLightValue()
	{
		return this.getMultiblockLogic().getController().getBlockLightValue();
	}

	@Override
	public boolean canBlockSeeTheSky()
	{
		return this.getMultiblockLogic().getController().canBlockSeeTheSky();
	}

	@Override
	public IErrorLogic getErrorLogic()
	{
		return this.getMultiblockLogic().getController().getErrorLogic();
	}

	@Override
	public IAccessHandler getAccessHandler()
	{
		return this.getMultiblockLogic().getController().getAccessHandler();
	}

	@Override
	public void onSwitchAccess(final EnumAccess oldAccess, final EnumAccess newAccess)
	{
		this.getMultiblockLogic().getController().onSwitchAccess(oldAccess, newAccess);
	}

	@Override
	public IInventoryAdapter getInternalInventory()
	{
		return this.getMultiblockLogic().getController().getInternalInventory();
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
		return this.getMultiblockLogic().getController().getExactTemperature();
	}

	@Override
	public float getExactHumidity()
	{
		return this.getMultiblockLogic().getController().getExactHumidity();
	}

	@Override
	public void writeGuiData(final DataOutputStreamForestry data)
			throws IOException
	{
		this.getMultiblockLogic().getController().writeGuiData(data);
	}

	@Override
	public void readGuiData(final DataInputStreamForestry data)
			throws IOException
	{
		this.getMultiblockLogic().getController().readGuiData(data);
	}

}
