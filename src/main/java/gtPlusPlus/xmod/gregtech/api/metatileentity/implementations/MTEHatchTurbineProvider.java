package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.hatch.base.MTETurbineHousingGui;
import gregtech.common.tileentities.machines.multi.MTELargeTurbine;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.sys.KeyboardUtils;

public class MTEHatchTurbineProvider extends MTEHatchInputBus {

    public MTEHatchTurbineProvider(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEHatchTurbineProvider(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchTurbineProvider(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTETurbineHousingGui(this).build(data, syncManager, uiSettings);
    }

    @Override
    public String[] getDescription() {
        return new String[] { "An automation port for Large Turbines",
            "Will attempt once per 1200 ticks to fill the turbine slot of it's parent turbine",
            "You may adjust this with a screwdriver", "Hold shift to adjust in finer amounts",
            "Hold control to adjust direction", "Left Click with Screwdriver to reset",
            "This module assumes the entire turbine is in the same Chunk", GTPPCore.GT_Tooltip.get() };
    }

    private MTELargeTurbine mParent = null;

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        super.onPostTick(aBaseMetaTileEntity, aTimer);
        if (aTimer % mRefreshTime == 0 && this.getBaseMetaTileEntity()
            .isServerSide()) {
            tryRefillTurbine();
        }
    }

    private void tryFindParentTurbine() {
        Logger.INFO("This turbine housing has no parent, searching world.");
        IGregTechTileEntity T = this.getBaseMetaTileEntity();
        World W = T.getWorld();
        Chunk C = W.getChunkFromBlockCoords(T.getXCoord(), T.getZCoord());
        for (Object o : C.chunkTileEntityMap.values()) {
            if (o instanceof IGregTechTileEntity G) {
                final IMetaTileEntity aMetaTileEntity = G.getMetaTileEntity();
                if (aMetaTileEntity == null) {
                    continue;
                }
                if (aMetaTileEntity instanceof MTELargeTurbine aTurb) {
                    for (MTEHatchInputBus ee : aTurb.mInputBusses) {
                        if (ee.equals(this)) {
                            mParent = aTurb;
                            Logger.INFO("Found a Parent to attach to this housing.");
                            return;
                        }
                    }
                }
            }
        }
    }

    private void tryRefillTurbine() {
        if (mParent == null) {
            tryFindParentTurbine();
        }
        if (mParent != null && mParent.mInventory[1] == null) {
            for (ItemStack aStack : this.mInventory) {
                if (isItemStackTurbine(aStack)) {
                    setGUIItemStack(aStack);
                }
            }
        }
    }

    protected boolean setGUIItemStack(ItemStack aNewGuiSlotContents) {
        boolean result = false;
        if (mParent.mInventory[1] == null) {
            mParent.mInventory[1] = aNewGuiSlotContents != null ? aNewGuiSlotContents.copy() : null;
            mParent.depleteInput(aNewGuiSlotContents);
            mParent.updateSlots();
            this.updateSlots();
            result = true;
        }
        return result;
    }

    public boolean isItemStackTurbine(ItemStack aStack) {
        if (aStack.getItem() instanceof MetaGeneratedTool) {
            return aStack.getItemDamage() >= 170 && aStack.getItemDamage() <= 176;
        }
        return false;
    }

    public boolean isItemStackScrewdriver(ItemStack aStack) {
        if (aStack.getItem() instanceof MetaGeneratedTool) {
            return aStack.getItemDamage() == 22 || aStack.getItemDamage() == 150;
        }
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return isItemStackTurbine(aStack);
    }

    private int mRefreshTime = 1200;
    private boolean mDescending = true;

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mRefreshTime", mRefreshTime);
        aNBT.setBoolean("mDescending", mDescending);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mRefreshTime = aNBT.getInteger("mRefreshTime");
        mDescending = aNBT.getBoolean("mDescending");
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (aPlayer != null) {
            if (KeyboardUtils.isCtrlKeyDown()) {
                mDescending = !mDescending;
                GTUtility.sendChatToPlayer(aPlayer, "Direction: " + (mDescending ? "DOWN" : "UP"));
            } else {
                int aAmount = 0;
                if (KeyboardUtils.isShiftKeyDown()) {
                    aAmount = 10;
                } else {
                    aAmount = 100;
                }
                if (mDescending) {
                    mRefreshTime -= aAmount;
                    if (mRefreshTime < 0) {
                        mRefreshTime = 1200;
                    }
                } else {
                    mRefreshTime += aAmount;
                    if (mRefreshTime > 1200) {
                        mRefreshTime = 0;
                    }
                }
                GTUtility.sendChatToPlayer(aPlayer, "Set check time to be every " + mRefreshTime + " ticks.");
            }
        }
    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        boolean aDidScrewdriver = false;
        if (aPlayer != null) {
            if (aPlayer.getHeldItem() != null) {
                if (isItemStackScrewdriver(aPlayer.getHeldItem())) {
                    aDidScrewdriver = true;
                    mRefreshTime = 1200;
                    GTUtility.sendChatToPlayer(aPlayer, "Reset check time to " + mRefreshTime + " ticks.");
                }
            }
        }
        if (!aDidScrewdriver) {
            super.onLeftclick(aBaseMetaTileEntity, aPlayer);
        }
    }

    @Override
    public boolean allowSelectCircuit() {
        return false;
    }

}
