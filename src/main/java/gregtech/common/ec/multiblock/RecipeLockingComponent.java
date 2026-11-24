package gregtech.common.ec.multiblock;

import static gregtech.api.recipe.check.SingleRecipeCheck.getDisplayString;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.ec.component.IGuiSyncableComponent;
import gregtech.api.ec.component.IOnScrewdriverClickComponent;
import gregtech.api.ec.component.IRecipeLogicComponent;
import gregtech.api.ec.component.ISavableComponent;
import gregtech.api.ec.component.IWailaDataComponent;
import gregtech.api.ec.component.gui.IGuiProviderComponent;
import gregtech.api.ec.component.gui.MultiblockGuiBuilder;
import gregtech.api.ec.component.multiblock.IProcessingLogicSetupComponent;
import gregtech.api.ec.component.multiblock.IRecipeLockingComponent;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.check.SingleRecipeCheck;
import gregtech.api.util.GTUtility;
import gregtech.common.ec.multiblock.internal.BaseMultiblockComponent;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockECBaseGui;
import gregtech.common.tileentities.machines.RecipeCheckReason;

public class RecipeLockingComponent extends BaseMultiblockComponent
    implements IRecipeLockingComponent, ISavableComponent, IProcessingLogicSetupComponent, IWailaDataComponent,
    IOnScrewdriverClickComponent, IGuiProviderComponent, IGuiSyncableComponent {

    private boolean mLockedToSingleRecipe = false;
    private @Nullable SingleRecipeCheck mSingleRecipeCheck = null;
    private boolean allowScrewdriverToggle = true;

    @Override
    public boolean isRecipeLockingEnabled() {
        return mLockedToSingleRecipe;
    }

    @Override
    public void setRecipeLocking(boolean enabled) {
        mLockedToSingleRecipe = enabled;
        if (!enabled) {
            setSingleRecipeCheck(null);
        }
        var recipeLogic = tryGetComponent(IRecipeLogicComponent.KEY);
        if (recipeLogic != null) {
            recipeLogic.scheduleRecipeCheck(RecipeCheckReason.IMMEDIATE);
        }
    }

    @Override
    public SingleRecipeCheck getSingleRecipeCheck() {
        return mSingleRecipeCheck;
    }

    @Override
    public void setSingleRecipeCheck(@Nullable SingleRecipeCheck recipeCheck) {
        mSingleRecipeCheck = recipeCheck;
    }

    public RecipeLockingComponent setAllowScrewdriverToggle(boolean allow) {
        allowScrewdriverToggle = allow;
        return this;
    }

    @Override
    public @NotNull String getSaveKey() {
        return "lock";
    }

    @Override
    public @Nullable NBTTagCompound saveComponentData() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("mLockedToSingleRecipe", mLockedToSingleRecipe);
        if (mLockedToSingleRecipe && mSingleRecipeCheck != null)
            nbt.setTag("mSingleRecipeCheck", mSingleRecipeCheck.writeToNBT());
        return nbt;
    }

    protected SingleRecipeCheck loadSingleRecipeChecker(NBTTagCompound aNBT) {
        var recipeLogic = tryGetComponent(IRecipeLogicComponent.KEY);
        return recipeLogic != null ? SingleRecipeCheck.tryLoad(recipeLogic.getRecipeMap(), aNBT) : null;
    }

    @Override
    public void loadLegacyNBTData(@NotNull NBTTagCompound nbt) {
        mLockedToSingleRecipe = nbt.getBoolean("mLockedToSingleRecipe");
        if (mLockedToSingleRecipe && nbt.hasKey("mSingleRecipeCheck", Constants.NBT.TAG_COMPOUND)) {
            SingleRecipeCheck c = loadSingleRecipeChecker(nbt.getCompoundTag("mSingleRecipeCheck"));
            if (c != null) mSingleRecipeCheck = c;
            // the old recipe is gone. we disable the machine to prevent making garbage in case of shared inputs
            // maybe use a better way to inform player in the future.
            else getMachine().disableWorking();
        }
    }

    @Override
    public void loadComponentData(@NotNull NBTTagCompound nbt) {
        loadLegacyNBTData(nbt);
    }

    @Override
    public int getSavePriority() {
        return SavePriority.RECIPE_LOCKING.priority;
    }

    @Override
    public void setupProcessingLogic(ProcessingLogic logic) {
        logic.setRecipeLocking(getMachine(), isRecipeLockingEnabled());
    }

    @Override
    public void appendWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x,
        int y, int z) {
        tag.setBoolean("isLockedToRecipe", isRecipeLockingEnabled());
        SingleRecipeCheck lockedRecipe = getSingleRecipeCheck();
        tag.setString(
            "lockedRecipeName",
            lockedRecipe != null ? getDisplayString(lockedRecipe.getRecipe(), false, true, false, true) : "");
    }

    @Override
    public boolean onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (!allowScrewdriverToggle) return false;
        var state = isRecipeLockingEnabled();
        setRecipeLocking(!state);
        if (!state) {
            GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.mb.locking_on");
        } else {
            GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.mb.locking_off");
            setSingleRecipeCheck(null);
        }
        return true;
    }

    @Override
    public int getOnScrewdriverClickPriority() {
        return OnScrewdriverClickPriority.RECIPE_LOCKING.priority;
    }

    @Override
    public void registerGuiSyncValues(@NotNull PanelSyncManager syncManager) {
        BooleanSyncValue recipeLockSyncer = new BooleanSyncValue(this::isRecipeLockingEnabled, this::setRecipeLocking)
            .allowC2S();
        syncManager.syncValue("recipeLock", recipeLockSyncer);
    }

    @Override
    public void configureGui(@NotNull MultiblockGuiBuilder builder) {
        builder.leftGap()
            .replace(
                LeftGapSection.RECIPE_LOCK,
                (panel, syncManager) -> MTEMultiBlockECBaseGui.createActiveFeatureToggleButton(
                    syncManager.findSyncHandler("recipeLock", BooleanSyncValue.class),
                    GTGuiTextures.OVERLAY_BUTTON_RECIPE_UNLOCKED,
                    GTGuiTextures.OVERLAY_BUTTON_RECIPE_LOCKED,
                    "GT5U.gui.button.lock_recipe",
                    this::isRecipeLockingEnabled));
    }
}
