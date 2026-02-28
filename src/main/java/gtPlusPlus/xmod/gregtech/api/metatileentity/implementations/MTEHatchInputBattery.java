package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GTValues.V;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.hatch.MTEHatchInputBatteryGui;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.api.gui.widget.ElectricSlotWidget;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEHatchInputBattery extends MTEHatch {

    public final RecipeMap<?> mRecipeMap = null;

    public MTEHatchInputBattery(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, getSlots(aTier), "Chargeable Item Bus for Multiblocks");
    }

    public MTEHatchInputBattery(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aTier < 1 ? 1 : aTier == 1 ? 4 : aTier == 2 ? 4 : 16, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        int mSlots = 0;
        if (this.mTier == 2) {
            mSlots = 4;
        } else if (this.mTier == 4) {
            mSlots = 16;
        } else {
            mSlots = 16;
        }
        return ArrayUtils.addAll(this.mDescriptionArray, "Capacity: " + mSlots + " slots", GTPPCore.GT_Tooltip.get());
    }

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
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
        return 512 + V[mTier + 1] * 16;
    }

    @Override
    public long maxAmperesIn() {
        return 4;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(TexturesGtBlock.Overlay_Hatch_Charger) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(TexturesGtBlock.Overlay_Hatch_Charger) };
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchInputBattery(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.hasInventoryBeenModified()) {
            fillStacksIntoFirstSlots();
        }
        if (aBaseMetaTileEntity.isServerSide()) {
            if (aBaseMetaTileEntity.getMetaTileEntity() instanceof MetaTileEntity mMetaTileEntity) {
                if (mMetaTileEntity.rechargerSlotCount() > 0 && aBaseMetaTileEntity.getStoredEU() > 0) {
                    for (int i = mMetaTileEntity.rechargerSlotStartIndex(),
                        k = mMetaTileEntity.rechargerSlotCount() + i; i < k; i++) {
                        if (aBaseMetaTileEntity.getStoredEU() > 0 && mMetaTileEntity.mInventory[i] != null) {
                            for (int u = 0; u < 10; u++) {
                                aBaseMetaTileEntity.decreaseStoredEnergyUnits(
                                    GTModHandler.chargeElectricItem(
                                        mMetaTileEntity.mInventory[i],
                                        (int) Math.min(V[this.mTier] * 15, aBaseMetaTileEntity.getStoredEU()),
                                        (int) Math.min(Integer.MAX_VALUE, GTValues.V[u]),
                                        false,
                                        false),
                                    true);
                                if (mMetaTileEntity.mInventory[i].stackSize <= 0) {
                                    mMetaTileEntity.mInventory[i] = null;
                                }
                            }
                        }
                    }
                }
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTimer);
    }

    public void updateSlots() {
        for (int i = 0; i < mInventory.length; i++)
            if (mInventory[i] != null && mInventory[i].stackSize <= 0) mInventory[i] = null;
        fillStacksIntoFirstSlots();
    }

    protected void fillStacksIntoFirstSlots() {
        GTUtility.compactInventory(this);
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return side == getBaseMetaTileEntity().getFrontFacing()
            && (mRecipeMap == null || mRecipeMap.containsInput(aStack));
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return side == getBaseMetaTileEntity().getFrontFacing()
            && (mRecipeMap == null || mRecipeMap.containsInput(aStack));
    }

    @Override
    public int rechargerSlotCount() {
        return mTier == 2 ? 4 : 16;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        if (mTier == 2) {
            for (int i = 0; i < 4; i++) {
                builder
                    .widget(new ElectricSlotWidget(inventoryHandler, i).setPos(70 + (i % 2) * 18, 25 + (i / 2) * 18));
            }
        } else {
            for (int i = 0; i < 16; i++) {
                builder.widget(new ElectricSlotWidget(inventoryHandler, i).setPos(52 + (i % 4) * 18, 7 + (i / 4) * 18));
            }
        }
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchInputBatteryGui(this).build(data, syncManager, uiSettings);
    }
}
