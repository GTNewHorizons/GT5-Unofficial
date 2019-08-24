package gtPlusPlus.xmod.gregtech.api.gui;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * The Container I use for all my Basic Machines
 */
public class CONTAINER_MultiMachine extends GT_ContainerMetaTile_Machine {

	public String[] mTileDescription;
	private String[] oTileDescription;
	
	private GregtechMeta_MultiBlockBase mMCTEI;
	private boolean mControllerSet = false;


	public CONTAINER_MultiMachine(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity) {
		super(aInventoryPlayer, aTileEntity);
	}

	public CONTAINER_MultiMachine(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity, final boolean bindInventory) {
		super(aInventoryPlayer, aTileEntity, bindInventory);
	}
	
	public static void setControllerInstance(CONTAINER_MultiMachine aContainer, IGregTechTileEntity aTile) {		
		if (aTile == null) {
			return;
		}
		final IMetaTileEntity aMetaTileEntity = aTile.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return;
		}
		if (aMetaTileEntity instanceof GregtechMeta_MultiBlockBase) {
			aContainer.mMCTEI = (GregtechMeta_MultiBlockBase) aMetaTileEntity;
		}		
	}

	public int aTotalTickTime = 0;
	public int aMaxParallel = 0;
	public int aPollutionTick = 0;
	public int aMaxInputVoltage = 0;
	public int aInputTier = 0;
	public int aOutputTier = 0;
	public int aRecipeDuration = 0;
	public int aRecipeEU = 0;
	public int aRecipeSpecial = 0;	
	public int aPollutionReduction = 0;
	public int aStoredEnergy = 0;
	public int aMaxEnergy = 0;
	public int aEfficiency = 0;

	private int oTotalTickTime = 0;
	private int oMaxParallel = 0;
	private int oPollutionTick = 0;
	private int oMaxInputVoltage = 0;
	private int oInputTier = 0;
	private int oOutputTier = 0;
	private int oRecipeDuration = 0;
	private int oRecipeEU = 0;
	private int oRecipeSpecial = 0;	
	private int oPollutionReduction = 0;
	private int oStoredEnergy = 0;
	private int oMaxEnergy = 0;
	private int oEfficiency = 0;

	private static Field timer;
	//private static Field crafters;

	@Override
	public final void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (this.mTileEntity.isClientSide() || this.mTileEntity.getMetaTileEntity() == null) {
			return;
		}
		if (!mControllerSet) {
			setControllerInstance(this, this.mTileEntity);
		}		
		mControllerSet = (mMCTEI != null);		
		if (timer == null) {
			timer = ReflectionUtils.getField(getClass(), "mTimer");
		}
		if (crafters == null) {
			//crafters = ReflectionUtils.getField(getClass(), "crafters");
		}
		if (timer != null && crafters != null && mControllerSet) {
			Logger.INFO("Trying to update clientside GUI data");
			try {
				Logger.INFO("0");		
				int aTimer = (int) ReflectionUtils.getFieldValue(timer, this);				
				
				//List crafters1List = (List) crafters1;
				List<ICrafting> crafters2 = new ArrayList<ICrafting>();
				Logger.INFO("1");		
				for (Object o : crafters) {
					if (o instanceof ICrafting) {
						crafters2.add((ICrafting) o);
					}
				}
				Logger.INFO("2");		
				if (!crafters2.isEmpty()) {
					Logger.INFO("3");		
					handleInitialFieldSetting();

					try {
						Logger.INFO("4");		
						for (final ICrafting var3 : crafters2) {
							handleCraftingEvent(aTimer, var3);
						}
						Logger.INFO("5");		
						handleInternalFieldSetting();
						Logger.INFO("6");		
					} catch (Throwable t) {

					}
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		else {
			Logger.INFO("Failed.");			
		}
	}

	public void handleInitialFieldSetting() {
		this.aTotalTickTime = MathUtils.balance((int) mMCTEI.getTotalRuntimeInTicks(), Integer.MIN_VALUE, Integer.MAX_VALUE);
		this.aMaxParallel = mMCTEI.getMaxParallelRecipes();
		this.aPollutionTick = mMCTEI.getPollutionPerTick(null);
		this.aMaxInputVoltage = MathUtils.balance((int) mMCTEI.getMaxInputVoltage(), Integer.MIN_VALUE, Integer.MAX_VALUE);
		this.aInputTier = MathUtils.balance((int) mMCTEI.getInputTier(), Integer.MIN_VALUE, Integer.MAX_VALUE);
		this.aOutputTier = MathUtils.balance((int) mMCTEI.getOutputTier(), Integer.MIN_VALUE, Integer.MAX_VALUE);		
		if (mMCTEI.mLastRecipe != null) {
			GT_Recipe aRecipe = mMCTEI.mLastRecipe;
			this.aRecipeDuration = MathUtils.balance(aRecipe.mDuration, Integer.MIN_VALUE, Integer.MAX_VALUE);
			this.aRecipeEU = MathUtils.balance(aRecipe.mEUt, Integer.MIN_VALUE, Integer.MAX_VALUE);
			this.aRecipeSpecial = MathUtils.balance(aRecipe.mSpecialValue, Integer.MIN_VALUE, Integer.MAX_VALUE);			
		}		
		this.aPollutionReduction = mMCTEI.getPollutionReductionForAllMufflers();
		
		this.aStoredEnergy = MathUtils.balance((int) mMCTEI.getStoredEnergyInAllEnergyHatches(), Integer.MIN_VALUE, Integer.MAX_VALUE);
		this.aMaxEnergy = MathUtils.balance((int) mMCTEI.getMaxEnergyStorageOfAllEnergyHatches(), Integer.MIN_VALUE, Integer.MAX_VALUE);
		this.aEfficiency = MathUtils.balance(mMCTEI.mEfficiency, Integer.MIN_VALUE, Integer.MAX_VALUE);		
	}

	public void handleCraftingEvent(int aTimer, ICrafting aCrafter) {
		int aID = 750;
		if (aTimer % 500 == 10 || this.oTotalTickTime != this.aTotalTickTime) {
			aCrafter.sendProgressBarUpdate((Container) this, aID++, this.aTotalTickTime);
		}
		if (aTimer % 500 == 10 || this.oMaxParallel != this.aMaxParallel) {
			aCrafter.sendProgressBarUpdate((Container) this, aID++, this.aMaxParallel);
		}
		if (aTimer % 500 == 10 || this.oPollutionTick != this.aPollutionTick) {
			aCrafter.sendProgressBarUpdate((Container) this, aID++, this.aPollutionTick);
		}
		if (aTimer % 500 == 10 || this.oMaxInputVoltage != this.aMaxInputVoltage) {
			aCrafter.sendProgressBarUpdate((Container) this, aID++, this.aMaxInputVoltage);
		}
		if (aTimer % 500 == 10 || this.oInputTier != this.aInputTier) {
			aCrafter.sendProgressBarUpdate((Container) this, aID++, this.aInputTier);
		}
		if (aTimer % 500 == 10 || this.oOutputTier != this.aOutputTier) {
			aCrafter.sendProgressBarUpdate((Container) this, aID++, this.aOutputTier);
		}
		if (aTimer % 500 == 10 || this.oRecipeDuration != this.aRecipeDuration) {
			aCrafter.sendProgressBarUpdate((Container) this, aID++, this.aRecipeDuration);
		}
		if (aTimer % 500 == 10 || this.oRecipeEU != this.aRecipeEU) {
			aCrafter.sendProgressBarUpdate((Container) this, aID++, this.aRecipeEU);
		}
		if (aTimer % 500 == 10 || this.oRecipeSpecial != this.aRecipeSpecial) {
			aCrafter.sendProgressBarUpdate((Container) this, aID++, this.aRecipeSpecial);
		}
		if (aTimer % 500 == 10 || this.oPollutionReduction != this.aPollutionReduction) {
			aCrafter.sendProgressBarUpdate((Container) this, aID++, this.aPollutionReduction);
		}
		if (aTimer % 500 == 10 || this.oStoredEnergy != this.aStoredEnergy) {
			aCrafter.sendProgressBarUpdate((Container) this, aID++, this.aStoredEnergy);
		}
		if (aTimer % 500 == 10 || this.oMaxEnergy != this.aMaxEnergy) {
			aCrafter.sendProgressBarUpdate((Container) this, aID++, this.aMaxEnergy);
		}
		if (aTimer % 500 == 10 || this.oEfficiency != this.aEfficiency) {
			aCrafter.sendProgressBarUpdate((Container) this, aID++, this.aEfficiency);
		}
	}

	public void handleInternalFieldSetting() {
		this.oTotalTickTime = this.aTotalTickTime;
		this.oMaxParallel = this.aMaxParallel;
		this.oPollutionTick = this.aPollutionTick;
		this.oMaxInputVoltage = this.aMaxInputVoltage;
		this.oInputTier = this.aInputTier;
		this.oOutputTier = this.aOutputTier;
		this.oRecipeDuration = this.aRecipeDuration;
		this.oRecipeEU = this.aRecipeEU;
		this.oRecipeSpecial = this.aRecipeSpecial;
		this.oPollutionReduction = this.aPollutionReduction;
		this.oStoredEnergy = this.aStoredEnergy;
		this.oMaxEnergy = this.aMaxEnergy;
		this.oEfficiency = this.aEfficiency;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(final int par1, final int par2) {
		super.updateProgressBar(par1, par2);		
		int shiftedSwitch = par1 - 750;		
		switch (shiftedSwitch) {
		case 0: {
			this.aTotalTickTime = par2;
			break;
		}
		case 1: {
			this.aMaxParallel = par2;
			break;
		}
		case 2: {
			this.aPollutionTick = par2;
			break;
		}
		case 3: {
			this.aMaxInputVoltage = par2;
			break;
		}
		case 4: {
			this.aInputTier = par2;
			break;
		}
		case 5: {
			this.aOutputTier = par2;
			break;
		}
		case 6: {
			this.aRecipeDuration = par2;
			break;
		}
		case 7: {
			this.aRecipeEU = par2;
			break;
		}
		case 8: {
			this.aRecipeSpecial = par2;
			break;
		}
		case 9: {
			this.aPollutionReduction = par2;
			break;
		}
		case 10: {
			this.aStoredEnergy = par2;
			break;
		}
		case 11: {
			this.aMaxEnergy = par2;
			break;
		}
		case 12: {
			this.aEfficiency = par2;
			break;
		}
		}
	}

}
