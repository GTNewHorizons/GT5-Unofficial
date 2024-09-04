package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.LARGETURBINE_ST5;
import static gregtech.api.enums.Textures.BlockIcons.LARGETURBINE_ST_ACTIVE5;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.gui.modularui.GTUIInfos;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.objects.GTItemStack;
import gregtech.api.objects.GTRenderedTexture;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines.MTELargerTurbineBase;

@SuppressWarnings("deprecation")
public class MTEHatchTurbine extends MTEHatch {

    public boolean mHasController = false;
    public boolean mUsingAnimation = true;
    private String mControllerLocation;
    public int mEUt = 0;

    public MTEHatchTurbine(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 16, "Turbine Rotor holder for XL Turbines");
    }

    public MTEHatchTurbine(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures);
    }

    public MTEHatchTurbine(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription[0], aTextures);
    }

    @Override
    public String[] getDescription() {
        return ArrayUtils.addAll(
            this.mDescriptionArray,
            "Right Click with a soldering iron to reset controller link",
            "Right Click with a wrench to remove turbine",
            "Right Click with a screwdriver for technical information",
            "Sneak + Right Click with a wrench to rotate",
            "Sneak + Right Click with a screwdriver to disable animations",
            GTPPCore.GT_Tooltip.get());
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, getFrontFacingTurbineTexture() };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, getFrontFacingTurbineTexture() };
    }

    public int getEU() {
        return this.mEUt;
    }

    public void setEU(int aEU) {
        this.mEUt = aEU;
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return facing.offsetY == 0;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    public boolean hasTurbine() {
        ItemStack aStack = this.mInventory[0];
        boolean aIsValid = MTELargerTurbineBase.isValidTurbine(aStack);
        return aIsValid;
    }

    public ItemStack getTurbine() {
        if (hasTurbine()) {
            return this.mInventory[0];
        }
        return null;
    }

    public boolean canWork() {
        return hasTurbine();
    }

    public boolean insertTurbine(ItemStack aTurbine) {
        if (MTELargerTurbineBase.isValidTurbine(aTurbine)) {
            this.mInventory[0] = aTurbine;
            return true;
        }
        return false;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchTurbine(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        GTUIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    public void damageTurbine(int aEUt, int damageFactorLow, float damageFactorHigh) {
        damageTurbine((long) aEUt, damageFactorLow, damageFactorHigh);
    }

    public void damageTurbine(long aEUt, int damageFactorLow, float damageFactorHigh) {
        if (hasTurbine() && MathUtils.randInt(0, 1) == 0) {
            ItemStack aTurbine = getTurbine();
            ((MetaGeneratedTool) aTurbine.getItem()).doDamage(
                aTurbine,
                (long) getDamageToComponent(aTurbine)
                    * (long) Math.min((float) aEUt / (float) damageFactorLow, Math.pow(aEUt, damageFactorHigh)));
        }
    }

    private int getDamageToComponent(ItemStack aStack) {
        return 1;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("mHasController", mHasController);
        aNBT.setBoolean("mUsingAnimation", mUsingAnimation);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mHasController = aNBT.getBoolean("mHasController");
        mUsingAnimation = aNBT.getBoolean("mUsingAnimation");
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (this.mHasController) {
            if (aTick % 20 == 0) {
                if (isControllerActive()) {
                    this.getBaseMetaTileEntity()
                        .setActive(true);
                } else {
                    this.getBaseMetaTileEntity()
                        .setActive(false);
                }
            }
        } else if (!this.mHasController && this.mControllerLocation != null) {
            // Weird Invalid State
            if (setController(BlockPos.generateBlockPos(mControllerLocation))) {
                // Valid
            }
        } else {
            // No Controller
        }
        if (this.mInventory[0] != null && this.mInventory[0].stackSize <= 0) this.mInventory[0] = null;
    }

    public boolean isControllerActive() {
        MTELargerTurbineBase x = getController();
        if (x != null) {
            // Logger.INFO("Checking Status of Controller. Running? "+(x.mEUt > 0));
            return x.lEUt > 0;
        }
        // Logger.INFO("Status of Controller failed, controller is null.");
        return false;
    }

    public MTELargerTurbineBase getController() {
        if (this.mHasController && this.mControllerLocation != null && this.mControllerLocation.length() > 0) {
            BlockPos p = BlockPos.generateBlockPos(mControllerLocation);
            if (p != null) {
                // Logger.INFO(p.getLocationString());
                IGregTechTileEntity tTileEntity = getBaseMetaTileEntity()
                    .getIGregTechTileEntity(p.xPos, p.yPos, p.zPos);
                if (tTileEntity != null && tTileEntity.getMetaTileEntity() instanceof MTELargerTurbineBase) {
                    return (MTELargerTurbineBase) tTileEntity.getMetaTileEntity();
                } else {
                    if (tTileEntity == null) {
                        Logger.INFO("Controller MTE is null, somehow?");
                    } else {
                        Logger.INFO("Controller is a different MTE to expected");
                    }
                }
            }
        }
        // Logger.INFO("Failed to Get Controller.");
        return null;
    }

    public boolean canSetNewController() {
        if ((mControllerLocation != null && mControllerLocation.length() > 0) || this.mHasController) {
            return false;
        }
        return true;
    }

    public boolean setController(BlockPos aPos) {
        clearController();
        if (canSetNewController()) {
            mControllerLocation = aPos.getUniqueIdentifier();
            mHasController = true;
            Logger.INFO("Successfully injected controller into this Turbine Assembly Hatch.");
        }
        return mHasController;
    }

    public void clearController() {
        this.mControllerLocation = null;
        this.mHasController = false;
    }

    public boolean usingAnimations() {
        return mUsingAnimation;
    }

    private ITexture getFrontFacingTurbineTexture() {
        if (!mHasController) {
            return this.getBaseMetaTileEntity()
                .isActive() ? new GTRenderedTexture(LARGETURBINE_ST_ACTIVE5) : new GTRenderedTexture(LARGETURBINE_ST5);
        } else {
            if (usingAnimations()) {
                if (isControllerActive()) {
                    return getController().frontFaceActive;
                }
            }
            return getController().frontFace;
        }
    }

    @Override
    public long getMinimumStoredEU() {
        return 0;
    }

    @Override
    public boolean isItemValidForSlot(int aIndex, ItemStack aStack) {
        return false;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int ordinalSide) {
        return new int[] {};
    }

    @Override
    public boolean canInsertItem(int aIndex, ItemStack aStack, int ordinalSide) {
        return false;
    }

    public void setActive(boolean b) {
        this.getBaseMetaTileEntity()
            .setActive(b);
    }

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, GTItemStack aStack) {
        return false;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (!aPlayer.isSneaking()) {
            PlayerUtils.messagePlayer(aPlayer, "Using Animations? " + usingAnimations());
            PlayerUtils.messagePlayer(aPlayer, "Has Controller? " + this.mHasController);
            if (mHasController) {
                PlayerUtils.messagePlayer(
                    aPlayer,
                    "Controller Location: " + BlockPos.generateBlockPos(mControllerLocation)
                        .getLocationString());
                PlayerUtils.messagePlayer(aPlayer, "Controller Active? " + this.isControllerActive());
            }
            PlayerUtils.messagePlayer(
                aPlayer,
                "Active? " + this.getBaseMetaTileEntity()
                    .isActive());
            PlayerUtils.messagePlayer(aPlayer, "Has Turbine inserted? " + this.hasTurbine());
            if (this.hasTurbine()) {
                Materials aMat = MetaGeneratedTool.getPrimaryMaterial(getTurbine());
                String aSize = MTELargerTurbineBase
                    .getTurbineSizeString(MTELargerTurbineBase.getTurbineSize(getTurbine()));
                PlayerUtils.messagePlayer(aPlayer, "Using: " + aMat.mLocalizedName + " " + aSize);
            }
        } else {
            this.mUsingAnimation = !mUsingAnimation;
            if (this.mUsingAnimation) {
                PlayerUtils.messagePlayer(aPlayer, "Using Animated Turbine Texture.");
            } else {
                PlayerUtils.messagePlayer(aPlayer, "Using Static Turbine Texture.");
            }
        }
    }

    @Override
    public boolean onWrenchRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer, float aX,
        float aY, float aZ) {
        if (this.getBaseMetaTileEntity()
            .isServerSide() && !aPlayer.isSneaking()) {
            ItemStack tCurrentItem = aPlayer.inventory.getCurrentItem();
            if (tCurrentItem != null) {
                if (tCurrentItem.getItem() instanceof MetaGeneratedTool) {
                    return onToolClick(tCurrentItem, aPlayer, wrenchingSide);
                }
            }
        }
        return super.onWrenchRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ);
    }

    @Override
    public boolean onSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ) {
        if (this.getBaseMetaTileEntity()
            .isServerSide()) {
            ItemStack tCurrentItem = aPlayer.inventory.getCurrentItem();
            if (tCurrentItem != null) {
                if (tCurrentItem.getItem() instanceof MetaGeneratedTool) {
                    return onToolClick(tCurrentItem, aPlayer, wrenchingSide);
                }
            }
        }
        return false;
    }

    public boolean onToolClick(ItemStack tCurrentItem, EntityPlayer aPlayer, ForgeDirection side) {
        if (GTUtility.isStackInList(tCurrentItem, GregTechAPI.sWrenchList)) {
            boolean aHasTurbine = this.hasTurbine();
            if (aPlayer.inventory.getFirstEmptyStack() >= 0 && aHasTurbine) {
                if (PlayerUtils.isCreative(aPlayer)
                    || GTModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer)) {
                    aPlayer.inventory.addItemStackToInventory((this.getTurbine()));
                    this.mInventory[0] = null;
                    GTUtility.sendChatToPlayer(aPlayer, "Removed turbine with wrench.");
                    return true;
                }
            } else {
                GTUtility.sendChatToPlayer(
                    aPlayer,
                    aHasTurbine ? "Cannot remove turbine, no free inventory space." : "No turbine to remove.");
            }
        } else if (GTUtility.isStackInList(tCurrentItem, GregTechAPI.sSolderingToolList)) {
            if (mControllerLocation != null && mControllerLocation.length() > 0) {
                if (setController(BlockPos.generateBlockPos(mControllerLocation))) {
                    if (PlayerUtils.isCreative(aPlayer)
                        || GTModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer)) {
                        String tChat = "Trying to Reset linked Controller";
                        IGregTechTileEntity g = this.getBaseMetaTileEntity();
                        GTUtility.sendChatToPlayer(aPlayer, tChat);
                        GTUtility.sendSoundToPlayers(
                            g.getWorld(),
                            SoundResource.IC2_TOOLS_RUBBER_TRAMPOLINE,
                            1.0F,
                            -1,
                            g.getXCoord(),
                            g.getYCoord(),
                            g.getZCoord());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new SlotWidget(inventoryHandler, 0).setFilter(MTELargerTurbineBase::isValidTurbine)
                .setAccess(false, true)
                .setPos(79, 34));
    }
}
