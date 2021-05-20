package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GT_Values.V;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import gregtech.api.gui.GT_Container_3by3;
import gregtech.api.gui.GT_GUIContainer_3by3;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.minecraft.InventoryUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GT_MetaTileEntity_Hatch_Energy_RTG extends GT_MetaTileEntity_Hatch_Energy {

	public GT_MetaTileEntity_Hatch_Energy_RTG(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier);
	}

	public GT_MetaTileEntity_Hatch_Energy_RTG(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, aDescription, aTextures);
	}

	public GT_MetaTileEntity_Hatch_Energy_RTG(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, aDescription, aTextures);
	}
	
	@Override
	public String[] getDescription() {		
		return super.getDescription();
	}

	@Override
	public ITexture[] getTexturesActive(ITexture aBaseTexture) {
		return new ITexture[]{aBaseTexture, TexturesGtBlock.getTextureFromIcon(TexturesGtBlock.Overlay_Hatch_RTG_On, new short[] {220, 220, 220, 0})};
	}

	@Override
	public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
		return new ITexture[]{aBaseTexture, TexturesGtBlock.getTextureFromIcon(TexturesGtBlock.Overlay_Hatch_RTG_Off, new short[] {220, 220, 220, 0})};
	}

	@Override
	public boolean isSimpleMachine() {
		return true;
	}

	@Override
	public boolean isFacingValid(byte aFacing) {
		return true;
	}

	@Override
	public boolean isAccessAllowed(EntityPlayer aPlayer) {
		return true;
	}

	@Override
	public boolean isEnetInput() {
		return false;
	}

	@Override
	public boolean isInputFacing(byte aSide) {
		return aSide == getBaseMetaTileEntity().getFrontFacing();
	}

	@Override
	public boolean isValidSlot(int aIndex) {
		return true;
	}

	@Override
	public long getMinimumStoredEU() {
		return 0;
	}

	@Override
	public long maxEUInput() {
		return V[mTier];
	}

	@Override
	public long maxEUStore() {
		return Long.MAX_VALUE / (Short.MAX_VALUE * Byte.MAX_VALUE) ;
	}

	@Override
	public long maxAmperesIn() {
		return 0;
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		Constructor aCon = ReflectionUtils.getConstructor(this.getClass(), new Class[] {String.class, int.class, int.class, String[].class, ITexture[][][].class});
		Object aInst = ReflectionUtils.createNewInstanceFromConstructor(aCon, new Object[] {mName, mTier, 9, mDescriptionArray, mTextures});
		if (GT_MetaTileEntity_Hatch_Energy_RTG.class.isInstance(aInst)) {
			return (MetaTileEntity) aInst;
		}
		Logger.INFO("Created bad sized RTG hatch.");
		return new GT_MetaTileEntity_Hatch_Energy_RTG(mName, mTier, mDescriptionArray, mTextures);
	}

	@Override
	public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return false;
	}

	@Override
	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return true;
	}

	@Override
	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide()) return true;
		aBaseMetaTileEntity.openGUI(aPlayer);
		return true;
	}

	@Override
	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_Container_3by3(aPlayerInventory, aBaseMetaTileEntity);
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_3by3(aPlayerInventory, aBaseMetaTileEntity, "RTG Power Unit");
	}
	
	private static class Dat {

		protected final String mUniqueDataTag;
		private final ItemStack mStack;
		private final NBTTagCompound mNBT;
		
		public Dat(ItemStack aStack) {
			mStack = aStack;
			mNBT = (aStack.getTagCompound() != null ? aStack.getTagCompound() : new NBTTagCompound());
			mUniqueDataTag = ""+Item.getIdFromItem(aStack.getItem())+""+aStack.getItemDamage()+""+1+""+mNBT.getId();
		}
		
		public int getKey() {
			return Item.getIdFromItem(mStack.getItem())+mStack.getItemDamage();
		}
		
	}

	private static final HashMap<String, ItemStack> mFuelInstanceMap = new HashMap<String, ItemStack>();
	private static final HashMap<String, Long> mFuelValueMap = new HashMap<String, Long>();
	private static final HashMap<String, Integer> mFuelTypeMap = new HashMap<String, Integer>();
	private static final HashMap<Integer, String> mFuelTypeMapReverse = new HashMap<Integer, String>();
	
	public static boolean registerPelletForHatch(ItemStack aStack, long aFuelValue) {
		if (!ItemUtils.checkForInvalidItems(aStack)) {
			return false;
		}		
		ItemStack aTemp = aStack.copy();
		aTemp.stackSize = 1;		
		Dat aDat = new Dat(aTemp);
		String aKey = aDat.mUniqueDataTag;
		mFuelInstanceMap.put(aKey, aTemp);
		mFuelValueMap.put(aKey, aFuelValue);
		mFuelTypeMap.put(aKey, aDat.getKey());	
		mFuelTypeMapReverse.put(aDat.getKey(), aKey);		
		Logger.INFO("RTG Hatch: Registered Fuel Pellet: "+ItemUtils.getItemName(aTemp)+", Fuel Value: "+aFuelValue+", Key: "+aKey+", Key2: "+aDat.getKey());
		return true;
	}
	
	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
		if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.hasInventoryBeenModified()) {
			InventoryUtils.sortInventoryItems(this);
		}
		if (aTimer % 100 == 0 && aBaseMetaTileEntity.isServerSide()) {
			if (hasPellet(this)) {
				Logger.INFO("Has Pellet");						
				tryConsumePellet(this);				
			}
		}
	}

	private static void tryConsumePellet(GT_MetaTileEntity_Hatch_Energy_RTG aTile) {
		ItemStack aPellet = getPelletToConsume(aTile);
		if (aPellet != null) {
			Logger.INFO("Found Pellet");	
			long aFuel = getFuelValueOfPellet(aPellet);
			if (aFuel > 0) {
				Logger.INFO("Has Fuel Value: "+aFuel);	
				if (hasSpaceForEnergy(aTile, aFuel)) {
					Logger.INFO("Can buffer");	
					aPellet.stackSize = 0;
					Logger.INFO("Stack set to 0");	
					aPellet = null;
					Logger.INFO("null stack");	
					addEnergyToInternalStorage(aTile, aFuel);
					Logger.INFO("Consumed");	
				}
			}
		}
		aTile.updateSlots();
		Logger.INFO("updating slots");	
	}
	
	private static void addEnergyToInternalStorage(GT_MetaTileEntity_Hatch_Energy_RTG aTile, long aFuel) {
		aTile.getBaseMetaTileEntity().increaseStoredEnergyUnits(aFuel, true);
	}

	public static boolean hasSpaceForEnergy(GT_MetaTileEntity_Hatch_Energy_RTG aTile, long aAmount) {
		long aMax = aTile.maxEUStore();
		long aCurrent = aTile.getEUVar();
		if ((aMax - aCurrent) >= aAmount) {
			return true;
		}
		return false;
	}

	public void updateSlots() {
		for (int i = 0; i < mInventory.length; i++) {
			if (mInventory[i] != null && mInventory[i].stackSize <= 0) {
				mInventory[i] = null;
			}
		}
		InventoryUtils.sortInventoryItems(this);
	}

	public static boolean hasPellet(GT_MetaTileEntity_Hatch_Energy_RTG aTile) {
		for (ItemStack o : aTile.mInventory) {
			if (o != null ) {
				for (ItemStack i : mFuelInstanceMap.values()) {
					if (ItemUtils.areItemsEqual(o, i)) {
						return true;
					}
				}
			}
		}
		return false;		
	}

	public static String getPelletType(ItemStack o) {
		if (o == null) {
			return "error";
		}
		Dat aDat = new Dat(o);		
		return mFuelTypeMapReverse.get(aDat.getKey());		
	}

	public static long getFuelValueOfPellet(ItemStack aPellet) {
		String aType = getPelletType(aPellet);
		if (mFuelValueMap.containsKey(aType)) {
			return mFuelValueMap.get(aType);
		}
		return 0;
	}

	public static ItemStack getPelletToConsume(GT_MetaTileEntity_Hatch_Energy_RTG aTile) {
		for (ItemStack o : aTile.mInventory) {
			if (o != null ) {
				for (ItemStack i : mFuelInstanceMap.values()) {
					if (ItemUtils.areItemsEqual(o, i)) {
						return o;
					}
				}
			}
		}
		return null;		
	}



}