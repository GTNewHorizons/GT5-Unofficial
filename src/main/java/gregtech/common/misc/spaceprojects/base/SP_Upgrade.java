package gregtech.common.misc.spaceprojects.base;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import gregtech.common.misc.spaceprojects.enums.UpgradeStatus;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject.ISP_Requirements;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject.ISP_Upgrade;

public class SP_Upgrade implements ISP_Upgrade {

    // #region Variables

    private String mUpgradeName;
    private String mUpgradeUnlocalizedName;
    private ItemStack[] mItemsCost;
    private FluidStack[] mFluidsCost;
    private int mTotalStages;
    private int mCurrentStage;
    private int mBuildTime;
    private long mVoltage;

    // #endregion

    // #region Getters

    @Override
    public String getUpgradeName() {
        return mUpgradeName;
    }

    @Override
    public String getUnlocalizedName() {
        return mUpgradeUnlocalizedName;
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal(mUpgradeUnlocalizedName);
    }

    @Override
    public ItemStack[] getItemsCostPerStage() {
        return mItemsCost;
    }

    @Override
    public ItemStack getItemCostPerStage(int aIndex) {
        if (mItemsCost == null || aIndex < 0 || aIndex >= mItemsCost.length) {
            return null;
        }

        return mItemsCost[aIndex];
    }

    @Override
    public ItemStack[] getCurrentItemsProgress() {
        ItemStack[] tCurrentItemProgress = new ItemStack[mItemsCost.length];
        int index = 0;
        for (ItemStack tItem : mItemsCost) {
            ItemStack tCopy = tItem.copy();
            tCopy.stackSize *= getCurrentStage();
            tCurrentItemProgress[index++] = tCopy;
        }

        return tCurrentItemProgress;
    }

    @Override
    public ItemStack getCurrentItemProgress(int aIndex) {
        if (mItemsCost == null || aIndex < 0 || aIndex >= mItemsCost.length) {
            return null;
        }

        ItemStack tItem = mItemsCost[aIndex].copy();
        tItem.stackSize *= getCurrentStage();
        return tItem;
    }

    @Override
    public ItemStack[] getTotalItemsCost() {
        ItemStack[] tTotalItemCost = new ItemStack[mItemsCost.length];
        int index = 0;
        for (ItemStack tItem : mItemsCost) {
            ItemStack tCopy = tItem.copy();
            tCopy.stackSize *= getTotalStages();
            tTotalItemCost[index++] = tCopy;
        }

        return tTotalItemCost;
    }

    @Override
    public ItemStack getTotalItemCost(int aIndex) {
        if (mItemsCost == null || aIndex < 0 || aIndex >= mItemsCost.length) {
            return null;
        }

        ItemStack tItem = mItemsCost[aIndex].copy();
        tItem.stackSize *= getTotalStages();
        return tItem;
    }

    @Override
    public FluidStack[] getFluidsCostPerStage() {
        return mFluidsCost;
    }

    @Override
    public FluidStack getFluidCostPerStage(int aIndex) {
        if (mFluidsCost == null || aIndex < 0 || aIndex >= mFluidsCost.length) {
            return null;
        }

        return mFluidsCost[aIndex];
    }

    @Override
    public FluidStack[] getCurrentFluidsProgress() {
        if (mFluidsCost == null) {
            return null;
        }

        FluidStack[] tCurrentFluidProgress = new FluidStack[mFluidsCost.length];
        int index = 0;
        for (FluidStack tFluid : mFluidsCost) {
            FluidStack tCopy = tFluid.copy();
            tCopy.amount *= getCurrentStage();
            tCurrentFluidProgress[index++] = tCopy;
        }

        return tCurrentFluidProgress;
    }

    @Override
    public FluidStack getCurrentFluidProgress(int aIndex) {
        if (mFluidsCost == null || aIndex < 0 || aIndex >= mFluidsCost.length) {
            return null;
        }

        FluidStack tFluid = mFluidsCost[aIndex].copy();
        tFluid.amount *= getCurrentStage();
        return tFluid;
    }

    @Override
    public FluidStack[] getTotalFluidsCost() {
        if (mFluidsCost == null) {
            return null;
        }

        FluidStack[] tTotalFluidCost = new FluidStack[mFluidsCost.length];
        int index = 0;
        for (FluidStack tFluid : mFluidsCost) {
            FluidStack tCopy = tFluid.copy();
            tCopy.amount *= getTotalStages();
            tTotalFluidCost[index++] = tCopy;
        }

        return tTotalFluidCost;
    }

    @Override
    public FluidStack getTotalFluidCost(int aIndex) {
        if (mFluidsCost == null || aIndex < 0 || aIndex >= mFluidsCost.length) {
            return null;
        }

        FluidStack tFluid = mFluidsCost[aIndex].copy();
        tFluid.amount *= getTotalStages();
        return tFluid;
    }

    @Override
    public int getTotalStages() {
        return mTotalStages;
    }

    @Override
    public int getTotalBuildTime() {
        return mBuildTime;
    }

    @Override
    public int getCurrentStage() {
        return mCurrentStage;
    }

    @Override
    public float getCurrentProgress() {
        return mCurrentStage / mTotalStages * 100.0f;
    }

    @Override
    public long getVoltage() {
        return mVoltage;
    }

    @Override
    public UpgradeStatus getStatus() {
        if (getUpgradeRequirements() == null) {
            return UpgradeStatus.Unlocked;
        }
        return null;
    }

    @Override
    public ISP_Requirements getUpgradeRequirements() {
        // TODO Auto-generated method stub
        return null;
    }

    // #endregion

    // #region Other

    @Override
    public boolean meetsRequirements() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public ISP_Upgrade copy() {
        return null;
    }

    @Override
    public void goToNextStage() {
        mCurrentStage++;
    }

    // #endregion

}
