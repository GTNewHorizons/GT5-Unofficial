package gtPlusPlus.xmod.forestry.bees.alveary.gui;

import com.mojang.authlib.GameProfile;

import forestry.api.apiculture.*;
import forestry.api.core.*;
import forestry.core.inventory.InventoryAdapterTile;
import forestry.core.utils.ItemStackUtil;
import gtPlusPlus.xmod.forestry.bees.alveary.IAlvearyFrameHousing;
import gtPlusPlus.xmod.forestry.bees.alveary.TileAlvearyFrameHousing;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class InventoryFrameHousing extends InventoryAdapterTile<TileAlvearyFrameHousing> implements IAlvearyFrameHousing
{

	TileAlvearyFrameHousing alvearyFrame;

	public InventoryFrameHousing(final TileAlvearyFrameHousing alvearyFrame)
	{
		super(alvearyFrame, 1, "FrameHousingInv");
		this.alvearyFrame = alvearyFrame;
	}

	@Override
	public boolean canSlotAccept(final int slotIndex, final ItemStack itemStack)
	{
		return ItemStackUtil.containsItemStack(BeeManager.inducers.keySet(), itemStack);
	}

	@Override
	public boolean canBlockSeeTheSky() {
		return this.alvearyFrame.canBlockSeeTheSky();
	}

	@Override
	public Vec3 getBeeFXCoordinates() {
		return this.alvearyFrame.getBeeFXCoordinates();
	}

	@Override
	public IBeeHousingInventory getBeeInventory() {
		return this.alvearyFrame.getBeeInventory();
	}

	@Override
	public Iterable<IBeeListener> getBeeListeners() {
		return this.alvearyFrame.getBeeListeners();
	}

	@Override
	public Iterable<IBeeModifier> getBeeModifiers() {
		return this.alvearyFrame.getBeeModifiers();
	}

	@Override
	public IBeekeepingLogic getBeekeepingLogic() {
		return this.alvearyFrame.getBeekeepingLogic();
	}

	@Override
	public BiomeGenBase getBiome() {
		return this.alvearyFrame.getBiome();
	}

	@Override
	public int getBlockLightValue() {
		return this.alvearyFrame.getBlockLightValue();
	}

	@Override
	public EnumHumidity getHumidity() {
		return this.alvearyFrame.getHumidity();
	}

	@Override
	public GameProfile getOwner() {
		return this.alvearyFrame.getOwner();
	}

	@Override
	public EnumTemperature getTemperature() {
		return this.alvearyFrame.getTemperature();
	}

	@Override
	public World getWorld() {
		return this.alvearyFrame.getWorld();
	}

	@Override
	public ChunkCoordinates getCoordinates() {
		return this.alvearyFrame.getCoordinates();
	}

	@Override
	public IErrorLogic getErrorLogic() {
		return this.alvearyFrame.getErrorLogic();
	}

	@Override
	public InventoryFrameHousing getAlvearyInventory() {
		return this;
	}

	@Override
	public void wearOutFrames(final IBeeHousing beeHousing, final int amount) {
		this.alvearyFrame.wearOutFrames(beeHousing, amount);
	}
}
