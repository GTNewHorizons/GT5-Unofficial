package gregtech.common.misc.spaceprojects.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import gregtech.common.misc.spaceprojects.SpaceProjectManager;
import gregtech.common.misc.spaceprojects.enums.UpgradeStatus;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceBody;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject;

public class SpaceProject implements ISpaceProject {

    // #region Variables

    protected String mSpaceProjectName;
    protected String mSpaceProjectUnlocalizedName;
    protected long mVoltage;
    protected int mBuildTime;
    protected int mProjectTier;
    protected int mCurrentStage;
    protected int mTotalStages;
    protected Map<String, ISP_Upgrade> mUpgradesAvailable;
    protected Map<String, ISP_Upgrade> mUpgradesInstalled;
    protected ISP_Requirements mRequirements;
    protected ISP_Upgrade mCurrentUpgrade;
    protected ItemStack[] mItemsCost;
    protected FluidStack[] mFluidsCost;
    protected ISpaceBody mLocation;

    // #endregion

    // #region Getters

    @Override
    public String getProjectName() {
        return mSpaceProjectName;
    }

    @Override
    public String getUnlocalizedName() {
        return mSpaceProjectUnlocalizedName;
    }

    public String getLocalizedName() {
        return StatCollector.translateToLocal(mSpaceProjectUnlocalizedName);
    }

    @Override
    public long getProjectVoltage() {
        return mVoltage;
    }

    @Override
    public int getProjectBuildTime() {
        return mBuildTime;
    }

    @Override
    public float getProjectCurrentProgress() {
        if (mCurrentUpgrade != null) {
            return mCurrentUpgrade.getCurrentProgress();
        }

        return mCurrentStage / mTotalStages * 100.0f;
    }

    @Override
    public int getProjectTier() {
        return mProjectTier;
    }

    @Override
    public int getCurrentStage() {
        return mCurrentStage;
    }

    @Override
    public int getTotalStages() {
        return mTotalStages;
    }

    @Override
    public List<ISP_Upgrade> getUpgradesAvailable() {
        List<ISP_Upgrade> tUpgrades = new ArrayList<>();
        for (ISP_Upgrade tUpgrade : mUpgradesAvailable.values()) {
            if (tUpgrade.getStatus() == UpgradeStatus.Unlocked) {
                tUpgrades.add(tUpgrade);
            }
        }

        return tUpgrades;
    }

    @Override
    public Map<String, ISP_Upgrade> getUpgradesBuilt() {
        return mUpgradesInstalled;
    }

    @Override
    public ItemStack[] getItemsCostPerStage() {
        if (mCurrentUpgrade != null) {
            return mCurrentUpgrade.getItemsCostPerStage();
        }

        return mItemsCost;
    }

    @Override
    public ItemStack getItemCostPerStage(int aIndex) {
        if (mItemsCost == null || aIndex < 0 || aIndex >= mItemsCost.length) {
            return null;
        }

        if (mCurrentUpgrade != null) {
            return mCurrentUpgrade.getItemCostPerStage(aIndex);
        }

        return mItemsCost[aIndex];
    }

    @Override
    public ItemStack[] getCurrentItemsProgress() {
        if (mCurrentUpgrade != null) {
            return mCurrentUpgrade.getCurrentItemsProgress();
        }

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
        if (mCurrentUpgrade != null) {
            return mCurrentUpgrade.getCurrentItemProgress(aIndex);
        }

        if (mItemsCost == null || aIndex < 0 || aIndex >= mItemsCost.length) {
            return null;
        }

        ItemStack tItem = mItemsCost[aIndex].copy();
        tItem.stackSize *= getCurrentStage();
        return tItem;
    }

    @Override
    public ItemStack[] getTotalItemsCost() {
        if (mCurrentUpgrade != null) {
            return mCurrentUpgrade.getTotalItemsCost();
        }

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
        if (mCurrentUpgrade != null) {
            return mCurrentUpgrade.getTotalItemCost(aIndex);
        }

        if (mItemsCost == null || aIndex < 0 || aIndex >= mItemsCost.length) {
            return null;
        }

        ItemStack tItem = mItemsCost[aIndex].copy();
        tItem.stackSize *= getTotalStages();
        return tItem;
    }

    @Override
    public FluidStack[] getFluidsCostPerStage() {
        if (mCurrentUpgrade != null) {
            return mCurrentUpgrade.getFluidsCostPerStage();
        }

        return mFluidsCost;
    }

    @Override
    public FluidStack getFluidCostPerStage(int aIndex) {
        if (mCurrentUpgrade != null) {
            return mCurrentUpgrade.getFluidCostPerStage(aIndex);
        }

        if (mFluidsCost == null || aIndex < 0 || aIndex >= mFluidsCost.length) {
            return null;
        }

        return mFluidsCost[aIndex];
    }

    @Override
    public FluidStack[] getCurrentFluidsProgress() {
        if (mCurrentUpgrade != null) {
            return mCurrentUpgrade.getCurrentFluidsProgress();
        }

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
        if (mCurrentUpgrade != null) {
            return mCurrentUpgrade.getCurrentFluidProgress(aIndex);
        }

        if (mFluidsCost == null || aIndex < 0 || aIndex >= mFluidsCost.length) {
            return null;
        }

        FluidStack tFluid = mFluidsCost[aIndex].copy();
        tFluid.amount *= getCurrentStage();
        return tFluid;
    }

    @Override
    public FluidStack[] getTotalFluidsCost() {
        if (mCurrentUpgrade != null) {
            return mCurrentUpgrade.getTotalFluidsCost();
        }

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
        if (mCurrentUpgrade != null) {
            return mCurrentUpgrade.getTotalFluidCost(aIndex);
        }

        if (mFluidsCost == null || aIndex < 0 || aIndex >= mFluidsCost.length) {
            return null;
        }

        FluidStack tFluid = mFluidsCost[aIndex].copy();
        tFluid.amount *= getTotalStages();
        return tFluid;
    }

    @Override
    public ISP_Upgrade getUpgradeBeingBuilt() {
        return mCurrentUpgrade;
    }

    // #endregion

    // #region Setter/Builder

    public SpaceProject setProjectName(String aSpaceProjectName) {
        mSpaceProjectName = aSpaceProjectName;
        return this;
    }

    public SpaceProject setProjectUnlocalizedName(String aSpaceProjectUnlocalizedName) {
        mSpaceProjectUnlocalizedName = aSpaceProjectUnlocalizedName;
        return this;
    }

    public SpaceProject setProjectVoltage(long aSpaceProjectVoltage) {
        mVoltage = aSpaceProjectVoltage;
        return this;
    }

    public SpaceProject setProjectBuildTime(int aSpaceProjectBuildTime) {
        mBuildTime = aSpaceProjectBuildTime;
        return this;
    }

    public SpaceProject setTotalStages(int aSpaceProjectTotalStages) {
        mTotalStages = aSpaceProjectTotalStages;
        return this;
    }

    public SpaceProject setItemCosts(ItemStack... aItemCosts) {
        mItemsCost = aItemCosts;
        return this;
    }

    public SpaceProject setFluidCosts(FluidStack... aFluidCosts) {
        mFluidsCost = aFluidCosts;
        return this;
    }

    public SpaceProject setUpgrades(ISP_Upgrade... aUpgrades) {
        for (ISP_Upgrade tUpgrade : aUpgrades) {
            mUpgradesAvailable.put(tUpgrade.getUpgradeName(), tUpgrade);
        }
        return this;
    }

    @Override
    public void setCurrentUpgradeBeingBuilt(ISP_Upgrade aCurrentUpgrade) {
        if (mTotalStages == mCurrentStage) {
            mCurrentUpgrade = aCurrentUpgrade;
        }
    }

    @Override
    public void setProjectStage(int aStage) {
        mCurrentStage = aStage;
    }

    // #endregion

    // #region Other

    @Override
    public SpaceProject copy() {
        SpaceProject aCopy = new SpaceProject().setProjectName(mSpaceProjectName)
                .setProjectUnlocalizedName(mSpaceProjectUnlocalizedName).setProjectVoltage(mVoltage)
                .setProjectBuildTime(mBuildTime).setItemCosts(mItemsCost).setFluidCosts(mFluidsCost)
                .setTotalStages(mTotalStages);
        if (mUpgradesAvailable != null) {
            ISP_Upgrade[] tUpgrades = new ISP_Upgrade[mUpgradesAvailable.size()];
            int tIndex = 0;
            for (ISP_Upgrade tUpgrade : mUpgradesAvailable.values()) {
                tUpgrades[tIndex++] = tUpgrade.copy();
            }
            aCopy.setUpgrades(tUpgrades);
        }
        return aCopy;
    }

    @Override
    public void goToNextStage() {
        if (getCurrentStage() == getTotalStages()) {
            if (mCurrentUpgrade != null) {
                mUpgradesInstalled.put(mCurrentUpgrade.getUpgradeName(), mCurrentUpgrade);
                mCurrentUpgrade = null;
            }
            return;
        }
        if (mCurrentUpgrade != null) {
            mCurrentUpgrade.goToNextStage();
            return;
        }
        mCurrentStage++;
    }

    @Override
    public boolean meetsRequirements(UUID aTeam, ISpaceBody aLocation) {
        if (mRequirements == null) {
            return true;
        }

        if (mRequirements.getBodyType() != null) {
            if (!mRequirements.getBodyType().equals(mLocation.getType())) {
                return false;
            }
        }

        if (mRequirements.getStarType() != null) {
            if (!mRequirements.getStarType().equals(mLocation.getStarType())) {
                return false;
            }
        }

        if (mRequirements.getProjects() != null) {
            for (SpaceProject tProject : mRequirements.getProjects()) {
                if (!SpaceProjectManager.teamHasProject(aTeam, tProject)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SpaceProject)) {
            return false;
        }
        return getProjectName().equals(((SpaceProject) obj).getProjectName());
    }

    @Override
    public boolean isFinished() {
        return mCurrentStage == mTotalStages;
    }

    // #endregion
}
