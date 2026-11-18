package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.*;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.render.RenderOverlay;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtilityClient;
import gregtech.common.gui.modularui.hatch.MTEHatchTurbineGui;
import gtPlusPlus.core.lib.GTPPCore;

public class MTEHatchTurbine extends MTEHatch {

    public boolean mUsingAnimation = true;

    protected final List<RenderOverlay.OverlayTicket> overlayTickets = new ArrayList<>();
    private boolean mHasTurbine;

    public MTEHatchTurbine(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 16, "Turbine Rotor holder");
    }

    public MTEHatchTurbine(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        return ArrayUtils.addAll(
            this.mDescriptionArray,
            "Right Click with a wrench to remove turbine",
            "Right Click with a screwdriver for technical information",
            "Sneak + Right Click with a wrench to rotate",
            "Sneak + Right Click with a screwdriver to disable animations",
            GTPPCore.GT_Tooltip.get());
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(LARGETURBINE_NEW_ACTIVE5) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        if (hasTurbine()) return new ITexture[] { aBaseTexture, TextureFactory.of(LARGETURBINE_NEW_EMPTY5) };
        return new ITexture[] { aBaseTexture, TextureFactory.of(LARGETURBINE_NEW5) };
    }

    public int getEU() {
        return 0;
    }

    public void setEU(int aEU) {}

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return facing.offsetY == 0;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    public boolean hasTurbine() {
        if (getBaseMetaTileEntity().isServerSide()) {
            ItemStack aStack = this.mInventory[0];
            return mHasTurbine = isValidTurbine(aStack);
        }
        return mHasTurbine;
    }

    public ItemStack getTurbine() {
        if (hasTurbine()) {
            return this.mInventory[0];
        }
        return null;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchTurbine(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
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

    private int getDamageToComponent(ItemStack aStack) {
        return 1;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("mUsingAnimation", mUsingAnimation);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mUsingAnimation = aNBT.getBoolean("mUsingAnimation");
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (!aBaseMetaTileEntity.isServerSide()) return;
        if (this.mInventory[0] != null && this.mInventory[0].stackSize <= 0) this.mInventory[0] = null;
    }

    public IIconContainer[] getTurbineTextureActive() {
        return TURBINE_NEW_ACTIVE;
    }

    public IIconContainer[] getTurbineTextureFull() {
        return TURBINE_NEW;
    }

    public IIconContainer[] getTurbineTextureEmpty() {
        return TURBINE_NEW_EMPTY;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        setTurbineOverlay();
    }

    protected void setTurbineOverlay() {
        IGregTechTileEntity tile = getBaseMetaTileEntity();
        if (tile.isServerSide()) return;

        IIconContainer[] tTextures;
        if (tile.isActive()) tTextures = getTurbineTextureActive();
        else if (hasTurbine()) tTextures = getTurbineTextureFull();
        else tTextures = getTurbineTextureEmpty();

        GTUtilityClient.setTurbineOverlay(
            tile.getWorld(),
            tile.getXCoord(),
            tile.getYCoord(),
            tile.getZCoord(),
            ExtendedFacing.of(getBaseMetaTileEntity().getFrontFacing()),
            tTextures,
            overlayTickets);
    }

    @Override
    public void onTextureUpdate() {
        setTurbineOverlay();
    }

    public boolean usingAnimations() {
        return mUsingAnimation;
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
        return GTValues.emptyIntArray;
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
    public boolean allowCoverOnSide(ForgeDirection side, ItemStack coverItem) {
        return false;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (!aPlayer.isSneaking()) {
            GTUtility.sendChatToPlayer(aPlayer, "Using Animations? " + usingAnimations());
            GTUtility.sendChatToPlayer(
                aPlayer,
                "Active? " + this.getBaseMetaTileEntity()
                    .isActive());
            boolean hasTurbine = this.hasTurbine();
            GTUtility.sendChatToPlayer(aPlayer, "Has Turbine inserted? " + hasTurbine);
            if (hasTurbine) {
                Materials aMat = MetaGeneratedTool.getPrimaryMaterial(getTurbine());
                String aSize = getTurbineSizeString(getTurbineSize(getTurbine()));
                GTUtility.sendChatToPlayer(aPlayer, "Using: " + aMat.getLocalizedName() + " " + aSize);
            }
        } else {
            this.mUsingAnimation = !mUsingAnimation;
            if (this.mUsingAnimation) {
                GTUtility.sendChatToPlayer(aPlayer, "Using Animated Turbine Texture.");
            } else {
                GTUtility.sendChatToPlayer(aPlayer, "Using Static Turbine Texture.");
            }
        }
    }

    @Override
    public boolean onWrenchRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer, float aX,
        float aY, float aZ, ItemStack aTool) {
        if (this.getBaseMetaTileEntity()
            .isServerSide() && !aPlayer.isSneaking()) {
            if (aTool == null) {
                return super.onWrenchRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ, aTool);
            }
            if (!(aTool.getItem() instanceof MetaGeneratedTool)) {
                return super.onWrenchRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ, aTool);
            }
            boolean aHasTurbine = this.hasTurbine();
            if (aPlayer.inventory.getFirstEmptyStack() >= 0 && aHasTurbine) {
                if (aPlayer.capabilities.isCreativeMode || GTModHandler.damageOrDechargeItem(aTool, 1, 1000, aPlayer)) {
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
            return false;
        }
        return super.onWrenchRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ, aTool);
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchTurbineGui(this).build(data, syncManager, uiSettings);
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        if (getBaseMetaTileEntity().isClientSide()) GTUtilityClient.clearTurbineOverlay(overlayTickets);
    }

    public static boolean isValidTurbine(ItemStack aTurbine) {
        return (aTurbine != null && aTurbine.getItem() instanceof MetaGeneratedTool
            && aTurbine.getItemDamage() >= 170
            && aTurbine.getItemDamage() <= 176);
    }

    public static String getTurbineSizeString(int aSize) {
        return switch (aSize) {
            case 1 -> "Small Turbine";
            case 2 -> "Turbine";
            case 3 -> "Large Turbine";
            case 4 -> "Huge Turbine";
            default -> "";
        };
    }

    public static int getTurbineSize(ItemStack aTurbine) {
        if (isValidTurbine(aTurbine)) {
            if (aTurbine.getItemDamage() >= 170 && aTurbine.getItemDamage() < 172) {
                return 1;
            } else if (aTurbine.getItemDamage() >= 172 && aTurbine.getItemDamage() < 174) {
                return 2;
            } else if (aTurbine.getItemDamage() >= 174 && aTurbine.getItemDamage() < 176) {
                return 3;
            } else if (aTurbine.getItemDamage() >= 176 && aTurbine.getItemDamage() < 178) {
                return 4;
            }
        }
        return 0;
    }
}
