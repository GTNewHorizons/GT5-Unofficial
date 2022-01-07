package gtPlusPlus.core.tileentities.machines;

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
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

public class TileEntityPooCollector extends TileEntityBaseFluidCollector {
	
	public TileEntityPooCollector() {	
		super(9, 8000);
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
			if (aChance<= 100) {
				aPoop = ItemUtils.getItemStackOfAmountFromOreDict("dustManureByproducts", 1);
			}
			else if (aChance <= 500) {
				aPoop = ItemUtils.getItemStackOfAmountFromOreDict("dustSmallManureByproducts", 1);				
			}
			else if (aChance <= 1250) {
				aPoop = ItemUtils.getItemStackOfAmountFromOreDict("dustTinyManureByproducts", 1);				
			}
			else {
				return false;
			}			
			if (!ItemUtils.checkForInvalidItems(aPoop)) {
				return false;
			}
			
			//Add poop to world
			//Logger.INFO("Adding animal waste for "+aPooMaker.getCommandSenderName());
			
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
					else {
						aPooAmount = MathUtils.randInt(1, 10);
					}					
				}				
				aPooAmount = Math.max((Math.min(this.tank.getCapacity()-this.tank.getFluidAmount(), aPooAmount)/10), 1);				
				return aPooAmount;
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
	public ItemStack itemToSpawnInWorldIfTankIsFull() {
		int a = MathUtils.randInt(0, 100);
		ItemStack aItem = null;		
		if (a <= 30) {
			aItem = ItemUtils.getSimpleStack(AgriculturalChem.dustDirt);
		}
		else if (a <= 40) {
			aItem = ItemUtils.getItemStackOfAmountFromOreDict("dustSmallManureByproducts", 1);
		}
		else if (a <= 55) {
			aItem = ItemUtils.getItemStackOfAmountFromOreDict("dustTinyManureByproducts", 1);
		}		
		return aItem;
	}
	
	
	

}
