package gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import com.gtnewhorizon.gtnhlib.util.map.ItemStackMap;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.CycleButtonWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicTank;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.gui.GTPP_UITextures;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import ic2.api.crops.CropCard;
import ic2.api.crops.ICropTile;
import ic2.core.item.DamageHandler;

public class GT_MetaTileEntity_CropHarvestor extends GT_MetaTileEntity_BasicTank {

    private static final int SLOT_WEEDEX_1 = 1;
    private static final int SLOT_WEEDEX_2 = 2;
    private static final int SLOT_FERT_1 = 3;
    private static final int SLOT_FERT_4 = 6;
    private static final int SLOT_OUTPUT_START = 7;

    public boolean mModeAlternative = false;
    public boolean mHarvestEnabled = true;

    public GT_MetaTileEntity_CropHarvestor(final int aID, final int aTier, final String aDescription) {
        super(
            aID,
            "basicmachine.cropharvester.0" + aTier,
            "Crop Manager (" + GT_Values.VN[aTier] + ")",
            aTier,
            21,
            aDescription);
    }

    public GT_MetaTileEntity_CropHarvestor(final String aName, final int aTier, final String[] aDescription,
        final ITexture[][][] aTextures) {
        super(aName, aTier, 21, aDescription, aTextures);
    }

    @Override
    public boolean isTransformerUpgradable() {
        return true;
    }

    @Override
    public boolean isOverclockerUpgradable() {
        return true;
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return true;
    }

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public boolean isElectric() {
        return true;
    }

    @Override
    public long maxAmperesIn() {
        return 8;
    }

    @Override
    public long getMinimumStoredEU() {
        return GT_Values.V[this.mTier];
    }

    @Override
    public long maxEUStore() {
        return GT_Values.V[this.mTier] * (this.mTier * GT_Values.V[this.mTier]);
    }

    @Override
    public long maxEUInput() {
        return GT_Values.V[this.mTier];
    }

    @Override
    public int getCapacity() {
        return 32000 * this.mTier;
    }

    @Override
    public int getTankPressure() {
        return -100;
    }

    @Override
    public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
        GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_CropHarvestor(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public int getSizeInventory() {
        return 21;
    }

    private static int getRange(int aTier) {
        return switch (aTier) {
            case 1 -> 1;
            case 2 -> 5;
            case 3 -> 9;
            case 4 -> 13;
            case 5 -> 17;
            case 6 -> 21;
            case 7 -> 25;
            case 8 -> 29;
            case 9 -> 33;
            default -> 0;
        };
    }

    private HashSet<ICropTile> mCropCache = new HashSet<>();
    private boolean mInvalidCache = false;

    public boolean doesInventoryHaveSpace() {
        for (int i = SLOT_OUTPUT_START; i < this.getSizeInventory(); i++) {
            if (this.mInventory[i] == null || this.mInventory[i].stackSize < 64) {
                return true;
            }
        }
        return false;
    }

    public long powerUsage() {
        return this.maxEUInput() / 8;
    }

    public long powerUsageSecondary() {
        return this.maxEUInput() / 32;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (!getBaseMetaTileEntity().isServerSide() || !getBaseMetaTileEntity().isAllowedToWork()
            || (!getBaseMetaTileEntity().hasWorkJustBeenEnabled() && aTick % 100 != 0)) return;

        if (this.getBaseMetaTileEntity()
            .getUniversalEnergyStored() < getMinimumStoredEU()) return;

        int aTileX = this.getBaseMetaTileEntity()
            .getXCoord();
        int aTileY = this.getBaseMetaTileEntity()
            .getXCoord();
        int aTileZ = this.getBaseMetaTileEntity()
            .getXCoord();

        int aRadius = 10 + getRange(this.mTier);
        int aSide = (aRadius - 1) / 2;
        Map<ItemStack, Integer> aAllDrops = new ItemStackMap<>(true);

        if (this.mCropCache.isEmpty() || aTick % 1200 == 0 || this.mInvalidCache) {
            if (!this.mCropCache.isEmpty()) {
                this.mCropCache.clear();
            }
            // Logger.INFO("Looking for crops.");
            for (int y = -2; y <= 2; y++) {
                for (int x = (-aSide); x <= aSide; x++) {
                    for (int z = (-aSide); z <= aSide; z++) {
                        TileEntity tTileEntity = getBaseMetaTileEntity().getTileEntityOffset(x, y, z);
                        if (tTileEntity != null && tTileEntity instanceof ICropTile tCrop) {
                            this.mCropCache.add(tCrop);
                        }
                    }
                }
            }
        }

        // Process Cache
        if (!doesInventoryHaveSpace()) return;

        for (ICropTile tCrop : this.mCropCache) {
            if (tCrop == null) {
                this.mInvalidCache = true;
                break;
            }
            CropCard aCrop = tCrop.getCrop();
            if (aCrop == null) continue;

            if (this.mModeAlternative) processSecondaryFunctions(tCrop);
            if (!this.mHarvestEnabled) continue;

            if (aCrop.canBeHarvested(tCrop) && tCrop.getSize() == aCrop.getOptimalHavestSize(tCrop)) {
                if (!getBaseMetaTileEntity().decreaseStoredEnergyUnits(powerUsage(), true)) continue;
                ItemStack[] aHarvest = tCrop.harvest_automated(true);
                if (aHarvest == null) continue;

                for (ItemStack aStack : aHarvest) {
                    if (!ItemUtils.checkForInvalidItems(aStack)) continue;
                    if (this.mTier * 5 > MathUtils.randInt(1, 100)) {
                        aStack.stackSize += Math.floor(tCrop.getGain() / 10);
                        Logger.INFO("Bonus output given for " + aCrop.displayName());
                    }
                    Logger.INFO("Harvested " + aCrop.displayName());
                    aAllDrops.merge(aStack, aStack.stackSize, Integer::sum);
                }
            }
        }

        if (aAllDrops.isEmpty()) return;

        Logger.INFO("Handling " + aAllDrops.size() + " Harvests");
        for (int i = SLOT_OUTPUT_START; i < this.getSizeInventory() && !aAllDrops.isEmpty(); i++) {
            ItemStack invStack = mInventory[i];
            if (invStack == null || GT_Utility.isStackInvalid(invStack) || invStack.stackSize == 0) {
                Iterator<Entry<ItemStack, Integer>> iter = aAllDrops.entrySet()
                    .iterator();
                if (!iter.hasNext()) return;
                Entry<ItemStack, Integer> e = iter.next();
                int toAdd = e.getValue();
                int toAddThisSlot = Math.min(
                    toAdd,
                    e.getKey()
                        .getMaxStackSize());
                getBaseMetaTileEntity().setInventorySlotContents(i, GT_Utility.copyAmount(toAddThisSlot, e.getKey()));
                toAdd -= toAddThisSlot;
                if (toAdd <= toAddThisSlot) {
                    iter.remove();
                } else {
                    e.setValue(toAdd);
                }
            } else {
                Integer toAddMaybeNull = aAllDrops.get(invStack);
                if (toAddMaybeNull != null) {
                    int toAdd = toAddMaybeNull;
                    int space = Math.min(invStack.getMaxStackSize(), getInventoryStackLimit()) - invStack.stackSize;
                    if (toAdd <= space) {
                        getBaseMetaTileEntity().addStackToSlot(i, invStack, toAdd);
                        aAllDrops.remove(invStack);
                    } else {
                        getBaseMetaTileEntity().addStackToSlot(i, invStack, space);
                        aAllDrops.put(invStack, toAdd - space);
                    }
                }
            }
        }
    }

    public boolean hasFertilizer() {
        for (int i = SLOT_FERT_1; i <= SLOT_FERT_4; i++) {
            if (this.mInventory[i] != null) {
                return true;
            }
        }
        return false;
    }

    public boolean consumeFertilizer(boolean aSimulate) {
        if (hasFertilizer()) {
            for (int i = SLOT_FERT_1; i <= SLOT_FERT_4; i++) {
                if (this.mInventory[i] != null) {
                    consume(i, 1, aSimulate);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasWeedEX() {
        for (int i = SLOT_WEEDEX_1; i <= SLOT_WEEDEX_2; i++) {
            if (this.mInventory[i] != null) {
                return true;
            }
        }
        return false;
    }

    public boolean consumeWeedEX(boolean aSimulate) {
        if (hasWeedEX()) {
            for (int i = SLOT_WEEDEX_1; i <= SLOT_WEEDEX_2; i++) {
                if (this.mInventory[i] != null) {
                    damage(i, 1, aSimulate);
                    return true;
                }
            }
        }
        return false;
    }

    public void processSecondaryFunctions(ICropTile aCrop) {
        if (!this.mModeAlternative) {
            return;
        }
        if (hasFertilizer() && consumeFertilizer(true)
            && this.getBaseMetaTileEntity()
                .getUniversalEnergyStored() >= getMinimumStoredEU()
            && getBaseMetaTileEntity().decreaseStoredEnergyUnits(powerUsageSecondary(), true)
            && applyFertilizer(aCrop)) {
            if (consumeFertilizer(false)) {
                // Logger.INFO("Consumed Fert.");
            }
        }
        if (this.getFluidAmount() > 0 && this.getBaseMetaTileEntity()
            .getUniversalEnergyStored() >= getMinimumStoredEU()
            && getBaseMetaTileEntity().decreaseStoredEnergyUnits(powerUsageSecondary(), true)
            && applyHydration(aCrop)) {
            // Logger.INFO("Consumed Water.");
        }
        if (hasWeedEX() && consumeWeedEX(true)
            && this.getBaseMetaTileEntity()
                .getUniversalEnergyStored() >= getMinimumStoredEU()
            && getBaseMetaTileEntity().decreaseStoredEnergyUnits(powerUsageSecondary(), true)
            && applyWeedEx(aCrop)) {
            if (consumeWeedEX(false)) {
                // Logger.INFO("Consumed Weed-EX.");
            }
        }
    }

    public boolean applyWeedEx(ICropTile aCrop) {
        if (aCrop.getWeedExStorage() < 150) {
            aCrop.setWeedExStorage(aCrop.getWeedExStorage() + 50);
            boolean triggerDecline;
            triggerDecline = aCrop.getWorld().rand.nextInt(3) == 0;
            if (aCrop.getCrop() != null && aCrop.getCrop()
                .isWeed(aCrop) && aCrop.getWeedExStorage() >= 75 && triggerDecline) {
                switch (aCrop.getWorld().rand.nextInt(5)) {
                    case 0:
                        if (aCrop.getGrowth() > 0) {
                            aCrop.setGrowth((byte) (aCrop.getGrowth() - 1));
                        }
                    case 1:
                        if (aCrop.getGain() > 0) {
                            aCrop.setGain((byte) (aCrop.getGain() - 1));
                        }
                    default:
                        if (aCrop.getResistance() > 0) {
                            aCrop.setResistance((byte) (aCrop.getResistance() - 1));
                        }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean applyFertilizer(ICropTile aCrop) {
        if (aCrop.getNutrientStorage() >= 100) {
            return false;
        } else {
            // Logger.INFO("Current Nutrient: "+aCrop.getNutrientStorage()+" for "+aCrop.getCrop().displayName());
            aCrop.setNutrientStorage(aCrop.getNutrientStorage() + 100);
            return true;
        }
    }

    public boolean applyHydration(ICropTile aCrop) {
        if (aCrop.getHydrationStorage() >= 200 || this.getFluidAmount() == 0) {
            // Logger.INFO("Hydration Max");
            return false;
        } else {
            int apply = 200 - aCrop.getHydrationStorage();
            if (this.getFluidAmount() >= 0) {
                int drain = 0;
                if (this.getFluidAmount() >= apply) {
                    drain = apply;
                } else {
                    drain = this.getFluidAmount();
                }
                this.mFluid.amount -= drain;
                if (this.mFluid.amount <= 0) {
                    this.mFluid = null;
                }
                // Logger.INFO("Did Hydrate");
                aCrop.setHydrationStorage(aCrop.getHydrationStorage() + drain);
                return true;
            } else {
                // Logger.INFO("No water?");
                return false;
            }
        }
    }

    public boolean consume(int aSlot, int amount, boolean simulate) {
        ItemStack stack = this.mInventory[aSlot];
        if (stack != null && stack.stackSize >= amount) {
            int currentAmount = Math.min(amount, stack.stackSize);
            amount -= currentAmount;
            if (!simulate) {
                if (stack.stackSize == currentAmount) {
                    this.mInventory[aSlot] = null;
                } else {
                    stack.stackSize -= currentAmount;
                }
            } else {
                return amount >= 0;
            }
            return true;
        }
        return false;
    }

    public ItemStack damage(int aSlot, int amount, boolean simulate) {
        ItemStack ret = null;
        int damageApplied = 0;
        ItemStack stack = this.mInventory[aSlot];
        Item item = stack.getItem();
        if (stack != null && item.isDamageable()
            && (ret == null || stack.getItem() == ret.getItem() && ItemStack.areItemStackTagsEqual(stack, ret))) {
            if (simulate) {
                stack = stack.copy();
            }
            int maxDamage = DamageHandler.getMaxDamage(stack);
            while (amount > 0 && stack.stackSize > 0) {
                int currentAmount = Math.min(amount, maxDamage - DamageHandler.getDamage(stack));
                DamageHandler.damage(stack, currentAmount, null);
                damageApplied += currentAmount;
                amount -= currentAmount;
                if (DamageHandler.getDamage(stack) >= maxDamage) {
                    --stack.stackSize;
                    DamageHandler.setDamage(stack, 0);
                }

                if (ret == null) {
                    ret = stack.copy();
                }
            }
            if (stack.stackSize == 0 && !simulate) {
                this.mInventory[aSlot] = null;
            }
        }

        if (ret != null) {
            int i = DamageHandler.getMaxDamage(ret);
            ret.stackSize = damageApplied / i;
            DamageHandler.setDamage(ret, damageApplied % i);
        }
        return ret;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return aStack != null && aIndex >= SLOT_OUTPUT_START && aIndex < this.getSizeInventory();
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (aStack != null) {
            if (aStack.getItem()
                .getUnlocalizedName()
                .equals("ic2.itemFertilizer")) {
                return aIndex >= SLOT_FERT_1 && aIndex <= SLOT_FERT_4;
            } else if (aStack.getItem()
                .getUnlocalizedName()
                .equals("ic2.itemWeedEx")) {
                    return aIndex >= SLOT_WEEDEX_1 && aIndex <= SLOT_WEEDEX_2;
                }
        }
        return false;
    }

    @Override
    public String[] getDescription() {
        int aRadius = 10 + getRange(this.mTier);
        int aSide = (aRadius - 1) / 2;
        return ArrayUtils.addAll(
            this.mDescriptionArray,
            "Secondary mode can Hydrate/Fertilize/Weed-EX",
            "Consumes " + powerUsage() + "eu per harvest",
            "Consumes " + powerUsageSecondary() + "eu per secondary operation",
            "Can harvest 2 block levels above and below itself",
            "Radius: " + aSide + " blocks each side (" + aRadius + "x3x" + aRadius + ")",
            "Has " + (this.mTier * 5) + "% chance for extra drops",
            "Holds " + this.getCapacity() + "L of Water",
            CORE.GT_Tooltip.get());
    }

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, GT_ItemStack aStack) {
        return true;
    }

    /*
     * @Override public int getTextureIndex(byte aSide, byte aFacing, boolean aActive, boolean aRedstone) { if (aSide ==
     * aFacing) return 118+(aRedstone?8:0); if (GT_Utility.getOppositeSide(aSide) == aFacing) return
     * 113+(aRedstone?8:0); int tIndex = 128+(aRedstone?8:0); switch (aFacing) { case 0: return tIndex+64; case 1:
     * return tIndex+32; case 2: switch (aSide) { case 0: return tIndex+32; case 1: return tIndex+32; case 4: return
     * tIndex+16; case 5: return tIndex+48; } case 3: switch (aSide) { case 0: return tIndex+64; case 1: return
     * tIndex+64; case 4: return tIndex+48; case 5: return tIndex+16; } case 4: switch (aSide) { case 0: return
     * tIndex+16; case 1: return tIndex+16; case 2: return tIndex+48; case 3: return tIndex+16; } case 5: switch (aSide)
     * { case 0: return tIndex+48; case 1: return tIndex+48; case 2: return tIndex+16; case 3: return tIndex+48; } }
     * return tIndex; }
     */

    @Override
    public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
        final ITexture[][][] rTextures = new ITexture[10][17][];
        for (byte i = -1; i < 16; i++) {
            rTextures[0][i + 1] = this.getFront(i);
            rTextures[1][i + 1] = this.getBack(i);
            rTextures[2][i + 1] = this.getBottom(i);
            rTextures[3][i + 1] = this.getTop(i);
            rTextures[4][i + 1] = this.getSides(i);
            rTextures[5][i + 1] = this.getFront(i);
            rTextures[6][i + 1] = this.getBack(i);
            rTextures[7][i + 1] = this.getBottom(i);
            rTextures[8][i + 1] = this.getTop(i);
            rTextures[9][i + 1] = this.getSides(i);
        }
        return rTextures;
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
        final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {
        if (side == ForgeDirection.DOWN || side == ForgeDirection.UP) {
            return this.mTextures[3][aColorIndex + 1];
        } else {
            return this.mTextures[4][aColorIndex + 1];
        }
        /*
         * return this.mTextures[(aActive ? 5 : 0) + (side == facing ? 0 : aSide == GT_Utility.getOppositeSide(aFacing)
         * ? 1 : side == ForgeDirection.DOWN ? 2 : side == ForgeDirection.UP ? 3 : 4)][aColorIndex + 1];
         */
    }

    public ITexture[] getFront(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            new GT_RenderedTexture(TexturesGtBlock.Casing_CropHarvester_Cutter) };
    }

    public ITexture[] getBack(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            new GT_RenderedTexture(TexturesGtBlock.Casing_CropHarvester_Cutter) };
    }

    public ITexture[] getBottom(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            new GT_RenderedTexture(TexturesGtBlock.Casing_CropHarvester_Boxes) };
    }

    public ITexture[] getTop(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            new GT_RenderedTexture(TexturesGtBlock.Casing_CropHarvester_Boxes) };
    }

    public ITexture[] getSides(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            new GT_RenderedTexture(TexturesGtBlock.Casing_CropHarvester_Cutter) };
    }

    @Override
    public boolean doesFillContainers() {
        return false;
    }

    @Override
    public boolean doesEmptyContainers() {
        return false;
    }

    @Override
    public boolean canTankBeFilled() {
        return true;
    }

    @Override
    public boolean canTankBeEmptied() {
        return false;
    }

    @Override
    public boolean displaysItemStack() {
        return false;
    }

    @Override
    public boolean displaysStackSize() {
        return false;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("mModeAlternative", this.mModeAlternative);
        aNBT.setBoolean("mHarvestEnabled", this.mHarvestEnabled);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.mModeAlternative = aNBT.getBoolean("mModeAlternative");
        if (aNBT.hasKey("mHarvestEnabled")) {
            this.mHarvestEnabled = aNBT.getBoolean("mHarvestEnabled");
        }
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new CycleButtonWidget().setToggle(() -> mModeAlternative, val -> mModeAlternative = val)
                .setTexture(GTPP_UITextures.OVERLAY_BUTTON_HARVESTER_MODE)
                .addTooltip(0, "Enable Hydration/Fertilizing/Weed-EX")
                .addTooltip(1, "Disable Hydration/Fertilizing/Weed-EX")
                .setBackground(GT_UITextures.BUTTON_STANDARD)
                .setPos(47, 63)
                .setSize(18, 18));
        builder.widget(
            new CycleButtonWidget().setToggle(() -> mHarvestEnabled, val -> mHarvestEnabled = val)
                .setTexture(GTPP_UITextures.OVERLAY_BUTTON_HARVESTER_TOGGLE)
                .addTooltip(0, "Enable Harvest")
                .addTooltip(1, "Disable Harvest")
                .setBackground(GT_UITextures.BUTTON_STANDARD)
                .setPos(67, 63)
                .setSize(18, 18));
        builder.widget(
            SlotGroup.ofItemHandler(inventoryHandler, 2)
                .startFromSlot(SLOT_WEEDEX_1)
                .endAtSlot(SLOT_WEEDEX_2)
                .applyForWidget(
                    widget -> widget.setFilter(
                        stack -> stack != null && stack.getItem()
                            .getUnlocalizedName()
                            .equals("ic2.itemWeedEx"))
                        .setBackground(getGUITextureSet().getItemSlot(), GTPP_UITextures.OVERLAY_SLOT_WEED_EX))
                .build()
                .setPos(7, 13))
            .widget(
                SlotGroup.ofItemHandler(inventoryHandler, 2)
                    .startFromSlot(SLOT_FERT_1)
                    .endAtSlot(SLOT_FERT_4)
                    .applyForWidget(
                        widget -> widget.setFilter(
                            stack -> stack != null && stack.getItem()
                                .getUnlocalizedName()
                                .equals("ic2.itemFertilizer"))
                            .setBackground(getGUITextureSet().getItemSlot(), GTPP_UITextures.OVERLAY_SLOT_FERTILIZER))
                    .build()
                    .setPos(7, 31))
            .widget(
                SlotGroup.ofItemHandler(inventoryHandler, 6)
                    .startFromSlot(SLOT_OUTPUT_START)
                    .endAtSlot(SLOT_OUTPUT_START + 6 * 3)
                    .canInsert(false)
                    .build()
                    .setPos(61, 7));
        builder
            .widget(
                new ProgressBar()
                    .setTexture(GTPP_UITextures.PROGRESSBAR_BOILER_EMPTY, GT_UITextures.PROGRESSBAR_BOILER_WATER, 54)
                    .setDirection(ProgressBar.Direction.UP)
                    .setProgress(() -> (float) getFluidAmount() / getCapacity())
                    .setSynced(false, false)
                    .dynamicTooltip(
                        () -> Collections.singletonList("Water: " + getFluidAmount() + "L / " + getCapacity() + "L"))
                    .setPos(47, 7)
                    .setSize(10, 54))
            .widget(new FakeSyncWidget.FluidStackSyncer(this::getDrainableStack, this::setDrainableStack));
    }
}
