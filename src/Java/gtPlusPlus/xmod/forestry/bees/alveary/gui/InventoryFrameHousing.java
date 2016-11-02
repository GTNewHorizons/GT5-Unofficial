package gtPlusPlus.xmod.forestry.bees.alveary.gui;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

import com.mojang.authlib.GameProfile;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeHousingInventory;
import forestry.api.apiculture.IBeeListener;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.apiculture.IBeekeepingLogic;
import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;
import forestry.api.core.IErrorLogic;
import forestry.core.inventory.InventoryAdapterTile;
import forestry.core.utils.ItemStackUtil;
import gtPlusPlus.xmod.forestry.bees.alveary.IAlvearyFrameHousing;
import gtPlusPlus.xmod.forestry.bees.alveary.TileAlvearyFrameHousing;

public class InventoryFrameHousing extends InventoryAdapterTile<TileAlvearyFrameHousing> implements IAlvearyFrameHousing
{
	
	TileAlvearyFrameHousing alvearyFrame;
	
	public InventoryFrameHousing(TileAlvearyFrameHousing alvearyFrame)
	{
		super(alvearyFrame, 1, "FrameHousingInv");
		this.alvearyFrame = alvearyFrame;
	}

	@Override
	public boolean canSlotAccept(int slotIndex, ItemStack itemStack)
	{
		return ItemStackUtil.containsItemStack(BeeManager.inducers.keySet(), itemStack);
	}

	@Override
	public boolean canBlockSeeTheSky() {
		return alvearyFrame.canBlockSeeTheSky();
	}

	@Override
	public Vec3 getBeeFXCoordinates() {
		return alvearyFrame.getBeeFXCoordinates();
	}

	@Override
	public IBeeHousingInventory getBeeInventory() {
		return alvearyFrame.getBeeInventory();
	}

	@Override
	public Iterable<IBeeListener> getBeeListeners() {
		return alvearyFrame.getBeeListeners();
	}

	@Override
	public Iterable<IBeeModifier> getBeeModifiers() {
		return alvearyFrame.getBeeModifiers();
	}

	@Override
	public IBeekeepingLogic getBeekeepingLogic() {
		return alvearyFrame.getBeekeepingLogic();
	}

	@Override
	public BiomeGenBase getBiome() {
		return alvearyFrame.getBiome();
	}

	@Override
	public int getBlockLightValue() {
		return alvearyFrame.getBlockLightValue();
	}

	@Override
	public EnumHumidity getHumidity() {
		return alvearyFrame.getHumidity();
	}

	@Override
	public GameProfile getOwner() {
		return alvearyFrame.getOwner();
	}

	@Override
	public EnumTemperature getTemperature() {
		return alvearyFrame.getTemperature();
	}

	@Override
	public World getWorld() {
		return alvearyFrame.getWorld();
	}

	@Override
	public ChunkCoordinates getCoordinates() {
		return alvearyFrame.getCoordinates();
	}

	@Override
	public IErrorLogic getErrorLogic() {
		return alvearyFrame.getErrorLogic();
	}

	@Override
	public InventoryFrameHousing getAlvearyInventory() {
		return this;
	}

	@Override
	public void wearOutFrames(IBeeHousing beeHousing, int amount) {
		alvearyFrame.wearOutFrames(beeHousing, amount);		
	}
}
