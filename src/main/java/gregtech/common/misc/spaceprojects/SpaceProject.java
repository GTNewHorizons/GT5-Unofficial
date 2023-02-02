package gregtech.common.misc.spaceprojects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

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
    protected List<ISP_Upgrade> mUpgrades;
    protected ISP_Upgrade mCurrentUpgrade;
    protected ItemStack[] mItemCosts;
    protected FluidStack[] mFluidCosts;

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
        for (ISP_Upgrade tUpgrade : mUpgrades) {
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
        mUpgrades = Arrays.asList(aUpgrades);
        return this;
    }

    public void setCurrentUpgradeBeingBuilt(ISP_Upgrade aCurrentUpgrade) {
        mCurrentUpgrade = aCurrentUpgrade;
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
}
