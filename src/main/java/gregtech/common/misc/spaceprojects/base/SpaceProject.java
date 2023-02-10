package gregtech.common.misc.spaceprojects.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import gregtech.common.misc.spaceprojects.SpaceProjectManager;
import gregtech.common.misc.spaceprojects.enums.UpgradeStatus;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceBody;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject;

public class SpaceProject implements ISpaceProject {

    /*
     * Variable Section
     */
    protected String mSpaceProjectName;
    protected String mSpaceProjectUnlocalizedName;
    protected long mVoltage;
    protected int mBuildTime;
    protected int mProjectTier;
    protected int mCurrentStage;
    protected int mTotalStages;
    protected Map<String, ISP_Upgrade> mUpgrades;
    protected ISP_Requirements mRequirements;
    protected ISP_Upgrade mCurrentUpgrade;
    protected ItemStack[] mItemCosts;
    protected FluidStack[] mFluidCosts;
    protected ISpaceBody mLocation;

    /*
     * Getter Section
     */
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
    public int getProjectCurrentProgress() {
        return mCurrentStage;
    }

    @Override
    public int getProjectTier() {
        return mProjectTier;
    }

    @Override
    public int getTotalStages() {
        return mTotalStages;
    }

    @Override
    public List<ISP_Upgrade> getUpgradesAvailable() {
        List<ISP_Upgrade> tUpgrades = new ArrayList<>();
        for (ISP_Upgrade tUpgrade : mUpgrades.values()) {
            if (tUpgrade.getStatus() == UpgradeStatus.Unlocked) {
                tUpgrades.add(tUpgrade);
            }
        }
        return tUpgrades;
    }

    @Override
    public int getProgressForUpgrade() {
        return mCurrentUpgrade.getCurrentStage();
    }

    @Override
    public ItemStack[] getItemCostPerStage() {
        return mItemCosts;
    }

    @Override
    public ItemStack[] getCurrentItemProgress() {
        ItemStack[] tCurrentItemProgress = new ItemStack[mItemCosts.length];
        int index = 0;
        for (ItemStack tItem : mItemCosts) {
            ItemStack tCopy = tItem.copy();
            tCopy.stackSize *= getCurrentStage();
            tCurrentItemProgress[index++] = tCopy;
        }
        return tCurrentItemProgress;
    }

    @Override
    public ItemStack[] getTotalItemCost() {
        ItemStack[] tTotalItemCost = new ItemStack[mItemCosts.length];
        int index = 0;
        for (ItemStack tItem : mItemCosts) {
            ItemStack tCopy = tItem.copy();
            tCopy.stackSize *= getTotalStages();
            tTotalItemCost[index++] = tCopy;
        }
        return tTotalItemCost;
    }

    @Override
    public FluidStack[] getFluidCostPerStage() {
        return mFluidCosts;
    }

    @Override
    public FluidStack[] getCurrentFluidProgress() {
        FluidStack[] tCurrentFluidProgress = new FluidStack[mFluidCosts.length];
        int index = 0;
        for (FluidStack tFluid : mFluidCosts) {
            FluidStack tCopy = tFluid.copy();
            tCopy.amount *= getCurrentStage();
            tCurrentFluidProgress[index++] = tCopy;
        }
        return tCurrentFluidProgress;
    }

    @Override
    public FluidStack[] getTotalFluidCost() {
        FluidStack[] tTotalFluidCost = new FluidStack[mFluidCosts.length];
        int index = 0;
        for (FluidStack tFluid : mFluidCosts) {
            FluidStack tCopy = tFluid.copy();
            tCopy.amount *= getTotalStages();
            tTotalFluidCost[index++] = tCopy;
        }
        return tTotalFluidCost;
    }

    @Override
    public ISP_Upgrade getUpgradeBeingBuilt() {
        return mCurrentUpgrade;
    }

    @Override
    public int getCurrentStage() {
        return mCurrentStage;
    }

    /*
     * Setter/Builder Section
     */
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
        mItemCosts = aItemCosts;
        return this;
    }

    public SpaceProject setFluidCosts(FluidStack... aFluidCosts) {
        mFluidCosts = aFluidCosts;
        return this;
    }

    public SpaceProject setUpgrades(ISP_Upgrade... aUpgrades) {
        for (ISP_Upgrade tUpgrade : aUpgrades) {
            mUpgrades.put(tUpgrade.getUpgradeName(), tUpgrade);
        }
        return this;
    }

    @Override
    public void setCurrentUpgradeBeingBuilt(ISP_Upgrade aCurrentUpgrade) {
        mCurrentUpgrade = aCurrentUpgrade;
    }

    @Override
    public void setProjectStage(int aStage) {
        mCurrentStage = aStage;
    }

    /*
     * Other
     */
    @Override
    public void saveExtraSavedWorldData(NBTTagCompound aNBT) {
        // TODO Do things /s

    }

    @Override
    public void loadExtraSavedWorldData(NBTTagCompound aNBT) {
        // TODO Do things /s

    }

    @Override
    public ISpaceProject copy() {
        SpaceProject aCopy = new SpaceProject().setProjectName(mSpaceProjectName)
                .setProjectUnlocalizedName(mSpaceProjectUnlocalizedName).setProjectVoltage(mVoltage)
                .setProjectBuildTime(mBuildTime).setItemCosts(mItemCosts).setFluidCosts(mFluidCosts)
                .setTotalStages(mTotalStages);
        if (mUpgrades != null) {
            aCopy.setUpgrades(mUpgrades.values().toArray(new ISP_Upgrade[0]));
        }
        return aCopy;
    }

    @Override
    public void goToNextStage() {
        if (mCurrentStage == mTotalStages) {
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
            for (ISpaceProject tProject : mRequirements.getProjects()) {
                if (!SpaceProjectManager.teamHasProject(aTeam, tProject)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ISpaceProject)) {
            return false;
        }
        return getProjectName().equals(((ISpaceProject) obj).getProjectName());
    }
}
