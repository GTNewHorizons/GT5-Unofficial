package gtPlusPlus.core.tileentities.machines;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.item.chemistry.AgriculturalChem;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

public class TileEntityAdvPooCollector extends TileEntityBaseFluidCollector {
	
	public TileEntityAdvPooCollector() {	
		super(18, 128000);
		Logger.INFO("Created");
	}


	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return true;
	}
	
	public void onPreLogicTick() {
		
	}
		
	public <V> boolean addDrop(V aPooMaker) {
		int aChance = MathUtils.randInt(0, 50000);
		if (aChance > 0) {
			ItemStack aPoop;
			if (aChance<= 200) {
				aPoop = ItemUtils.getItemStackOfAmountFromOreDict("dustManureByproducts", 1);
			}
			else if (aChance <= 1000) {
				aPoop = ItemUtils.getItemStackOfAmountFromOreDict("dustSmallManureByproducts", 1);				
			}
			else if (aChance <= 2000) {
				aPoop = ItemUtils.getItemStackOfAmountFromOreDict("dustTinyManureByproducts", 1);				
			}
			else {
				return false;
			}
			
			//Add to inventory if not full, else espawn in world
			if (!this.mInventory.addItemStack(aPoop)) {
				EntityItem entity = new EntityItem(worldObj, xCoord, yCoord+1.5, zCoord, aPoop);
				worldObj.spawnEntityInWorld(entity);
			}			
			
		}
		
		
		return false;
	}

	private static AutoMap<Class> aEntityToDrain = new AutoMap<Class>();	
	
	@Override
	public AutoMap<Class> aThingsToLookFor() {
		if (aEntityToDrain.isEmpty()) {
			aEntityToDrain.add(EntityAnimal.class);
			aEntityToDrain.add(IAnimals.class);
			aEntityToDrain.add(EntityVillager.class);
			aEntityToDrain.add(EntityPlayer.class);
		}
		return aEntityToDrain;
	}

	@Override
	public <V> int onPostTick(V aPooMaker) {
			if (this.tank.getFluidAmount() < this.tank.getCapacity()) {				
				int aPooAmount = 0;				
				// Vanilla Animals
				if (aPooMaker instanceof EntityChicken) {
					aPooAmount = MathUtils.randInt(1, 40);
				}
				else if (aPooMaker instanceof EntityHorse) {
					aPooAmount = MathUtils.randInt(20, 40);
				}
				else if (aPooMaker instanceof EntityCow) {
					aPooAmount = MathUtils.randInt(18, 45);
				}
				else if (aPooMaker instanceof EntityMooshroom) {
					aPooAmount = 17;
				}
				else if (aPooMaker instanceof EntitySheep) {
					aPooAmount = MathUtils.randInt(8, 30);
				}				
				
				else {					
					if (aPooMaker instanceof EntityAnimal || aPooMaker instanceof IAnimals) {
						aPooAmount = MathUtils.randInt(5, 35);						
					}
					else if (aPooMaker instanceof EntityVillager) {
						aPooAmount = MathUtils.randInt(25, 30);
					}
					else if (aPooMaker instanceof EntityPlayer) {
						aPooAmount = MathUtils.randInt(1, 3);
					}
					else {
						aPooAmount = MathUtils.randInt(1, 10);
					}					
				}				
				aPooAmount = Math.max(Math.min(this.tank.getCapacity()-this.tank.getFluidAmount(), aPooAmount), 1);				
				return aPooAmount * 4;
			}
			else {
				return 0;
			}	
	}

	@Override
	public Fluid fluidToProvide() {
		return AgriculturalChem.PoopJuice;
	}

	@Override
	public Item itemToSpawnInWorldIfTankIsFull() {
		int a = MathUtils.randInt(0, 75);
		Item aItem = null;		
		if (a <= 30) {
			aItem = AgriculturalChem.dustDirt;
		}
		else if (a <= 40) {
			aItem = ItemUtils.getItemStackOfAmountFromOreDict("dustManureByproducts", 1).getItem();
		}
		else if (a <= 55) {
			aItem = ItemUtils.getItemStackOfAmountFromOreDict("dustSmallManureByproducts", 1).getItem();
		}		
		return aItem;
	}
	
	
	

}
