package gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.ArrayUtils;

import com.gtnewhorizon.gtnhlib.util.map.ItemStackMap;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.CycleButtonWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicTank;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.gui.GTPPUITextures;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import ic2.api.crops.CropCard;
import ic2.api.crops.Crops;
import ic2.api.crops.ICropTile;
import ic2.core.item.DamageHandler;

public class MTECropHarvestor extends MTEBasicTank {

    private static final int SLOT_WEEDEX_1 = 1;
    private static final int SLOT_WEEDEX_2 = 2;
    private static final int SLOT_FERT_1 = 3;
    private static final int SLOT_FERT_4 = 6;
    private static final int SLOT_OUTPUT_START = 7;

    public boolean mModeAlternative = false;
    public boolean mHarvestEnabled = true;
    public boolean harvestFullGrowth = true;

    public MTECropHarvestor(final int aID, final int aTier, final String aDescription) {
        super(
            aID,
            "basicmachine.cropharvester.0" + aTier,
            "Crop Manager (" + GTValues.VN[aTier] + ")",
            aTier,
            21,
            aDescription);
    }

    public MTECropHarvestor(final String aName, final int aTier, final String[] aDescription,
        final ITexture[][][] aTextures) {
        super(aName, aTier, 21, aDescription, aTextures);
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
    public long maxAmperesIn() {
        return 8;
    }

    @Override
    public long getMinimumStoredEU() {
        return GTValues.V[this.mTier];
    }

    @Override
    public long maxEUStore() {
        return GTValues.V[this.mTier] * (this.mTier * GTValues.V[this.mTier]);
    }

    @Override
    public long maxEUInput() {
        return GTValues.V[this.mTier];
    }

    @Override
    public int getCapacity() {
        return 32000 * this.mTier;
    }

    @Override
    public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTECropHarvestor(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
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

    private final HashSet<ICropTile> mCropCache = new HashSet<>();
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

        int aRadius = 10 + getRange(this.mTier);
        int aSide = (aRadius - 1) / 2;
        Map<ItemStack, Integer> aAllDrops = new ItemStackMap<>(true);

        if (this.mCropCache.isEmpty() || aTick % 1200 == 0 || this.mInvalidCache) {
            if (!this.mCropCache.isEmpty()) {
                this.mCropCache.clear();
            }
            lookForCrops(aSide);
        }

        // Process Cache
        // Note: A full inventory would also prevent processSecondaryFunctions (e.g. weed-ex)
        if (!doesInventoryHaveSpace()) return;

        for (ICropTile tCrop : this.mCropCache) {
            if (tCrop == null) {
                this.mInvalidCache = true;
                break;
            }
            CropCard aCrop = tCrop.getCrop();

            if (this.mModeAlternative) processSecondaryFunctions(tCrop);

            if (aCrop == null) continue;
            if (!this.mHarvestEnabled) continue;

            if (aCrop.canBeHarvested(tCrop)) {
                if (!getBaseMetaTileEntity().decreaseStoredEnergyUnits(powerUsage(), true)) continue;
                ItemStack[] aHarvest = tCrop.harvest_automated(this.harvestFullGrowth);
                if (aHarvest == null) continue;

                for (ItemStack aStack : aHarvest) {
                    if (aStack == null) continue;
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

        for (var dropEntry : aAllDrops.entrySet()) {
            ItemStack dropItem = dropEntry.getKey();
            int dropAmount = dropEntry.getValue();

            // how this can happen, idk
            if (dropItem == null) continue;

            for (int i = SLOT_OUTPUT_START; i < this.getSizeInventory() && dropAmount > 0; i++) {
                ItemStack invStack = mInventory[i];

                // If the slot is empty, create a new stack for the drop item, else check if it is the same as the drop
                // and merge if possible
                if (invStack == null || GTUtility.isStackInvalid(invStack) || invStack.stackSize == 0) {
                    int stackSize = Math.min(dropAmount, dropItem.getMaxStackSize());
                    getBaseMetaTileEntity().setInventorySlotContents(i, GTUtility.copyAmount(stackSize, dropItem));
                    dropAmount -= stackSize;
                } else if (GTUtility.areStacksEqual(invStack, dropItem)) {
                    int space = Math.min(invStack.getMaxStackSize(), getInventoryStackLimit()) - invStack.stackSize;
                    if (dropAmount <= space) {
                        // if the drop amount fits
                        getBaseMetaTileEntity().addStackToSlot(i, invStack, dropAmount);
                        dropAmount = 0;
                    } else {
                        // fill the slot
                        getBaseMetaTileEntity().addStackToSlot(i, invStack, space);
                        dropAmount -= space;
                    }
                }
            }
        }
    }

    private void lookForCrops(int aSide) {
        for (int y = -2; y <= 2; y++) {
            for (int x = (-aSide); x <= aSide; x++) {
                for (int z = (-aSide); z <= aSide; z++) {
                    TileEntity tTileEntity = getBaseMetaTileEntity().getTileEntityOffset(x, y, z);
                    if (tTileEntity instanceof ICropTile tCrop) {
                        this.mCropCache.add(tCrop);
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

    public boolean consumeWeedEX(boolean aSimulate) {
        for (int i = SLOT_WEEDEX_1; i <= SLOT_WEEDEX_2; i++) {
            if (this.mInventory[i] != null) {
                damage(i, 1, aSimulate);
                return true;
            }
        }
        return false;
    }

    public void processSecondaryFunctions(ICropTile aCrop) {
        if (!this.mModeAlternative) {
            return;
        }
        if (consumeWeedEX(true) && this.getBaseMetaTileEntity()
            .getUniversalEnergyStored() >= getMinimumStoredEU()
            && getBaseMetaTileEntity().decreaseStoredEnergyUnits(powerUsageSecondary(), true)
            && applyWeedEx(aCrop)) {
            consumeWeedEX(false);
        }
        if (aCrop.getCrop() == null || aCrop.getCrop()
            .equals(Crops.weed)) return;
        if (hasFertilizer() && consumeFertilizer(true)
            && this.getBaseMetaTileEntity()
                .getUniversalEnergyStored() >= getMinimumStoredEU()
            && getBaseMetaTileEntity().decreaseStoredEnergyUnits(powerUsageSecondary(), true)
            && applyFertilizer(aCrop)) {
            consumeFertilizer(false);
        }
        if (this.getFluidAmount() > 0 && this.getBaseMetaTileEntity()
            .getUniversalEnergyStored() >= getMinimumStoredEU()
            && getBaseMetaTileEntity().decreaseStoredEnergyUnits(powerUsageSecondary(), true)
            && applyHydration(aCrop)) {
            // Logger.INFO("Consumed Water.");
        }
    }

    public boolean applyWeedEx(ICropTile aCrop) {
        boolean applyDose = aCrop.getWeedExStorage() < 150;
        if (applyDose) aCrop.setWeedExStorage(aCrop.getWeedExStorage() + 50);

        boolean triggerDecline = aCrop.getWorld().rand.nextInt(3) == 0;
        CropCard cropType = aCrop.getCrop();
        if (!triggerDecline || cropType == null) return applyDose;

        // Should there already be a weed (an actual weed, not a weed-like plant) present, either
        // because Weed-EX got added late or it spawned on new untracked crop sticks, slowly wither it
        if (cropType.equals(Crops.weed)) {
            if (aCrop.getSize() == 1) {
                aCrop.reset();
            } else {
                aCrop.setSize((byte) (aCrop.getSize() - 1));
            }
            return true;
        }

        // Affect weed-like crops when we apply a dose of Weed-EX that crosses the threshold
        if (applyDose && cropType.isWeed(aCrop) && aCrop.getWeedExStorage() >= 75) {
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
        return applyDose;
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
            if (ItemList.IC2_Fertilizer.isStackEqual(aStack)) {
                return aIndex >= SLOT_FERT_1 && aIndex <= SLOT_FERT_4;
            } else if (ItemList.IC2_Spray_WeedEx.isStackEqual(aStack, true, true)) {
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
            "You can set the mode to harvest any growth stage crop or only fully mature ones",
            "Consumes " + powerUsage() + "eu per harvest",
            "Consumes " + powerUsageSecondary() + "eu per secondary operation",
            "Can harvest 2 block levels above and below itself",
            "Range: " + aRadius + "x5x" + aRadius + " blocks",
            "Has " + (this.mTier * 5) + "% chance for extra drops",
            "Holds " + this.getCapacity() + "L of Water",
            GTPPCore.GT_Tooltip.get());
    }

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
    }

    public ITexture[] getFront(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_CropHarvester_Cutter) };
    }

    public ITexture[] getBack(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_CropHarvester_Cutter) };
    }

    public ITexture[] getBottom(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_CropHarvester_Boxes) };
    }

    public ITexture[] getTop(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_CropHarvester_Boxes) };
    }

    public ITexture[] getSides(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_CropHarvester_Cutter) };
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return aFluid != null && aFluid.getFluid() == FluidRegistry.WATER;
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
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("mModeAlternative", this.mModeAlternative);
        aNBT.setBoolean("mHarvestEnabled", this.mHarvestEnabled);
        aNBT.setBoolean("harvestFullGrowth", this.harvestFullGrowth);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.mModeAlternative = aNBT.getBoolean("mModeAlternative");
        if (aNBT.hasKey("mHarvestEnabled")) {
            this.mHarvestEnabled = aNBT.getBoolean("mHarvestEnabled");
        }
        if (aNBT.hasKey("harvestFullGrowth")) {
            this.harvestFullGrowth = aNBT.getBoolean("harvestFullGrowth");
        }
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new CycleButtonWidget().setToggle(() -> mModeAlternative, val -> mModeAlternative = val)
                .setTexture(GTPPUITextures.OVERLAY_BUTTON_HARVESTER_MODE)
                .addTooltip(0, "Enable Hydration/Fertilizing/Weed-EX")
                .addTooltip(1, "Disable Hydration/Fertilizing/Weed-EX")
                .setBackground(GTUITextures.BUTTON_STANDARD)
                .setPos(47, 63)
                .setSize(18, 18));
        builder.widget(
            new CycleButtonWidget().setToggle(() -> mHarvestEnabled, val -> mHarvestEnabled = val)
                .setTexture(GTPPUITextures.OVERLAY_BUTTON_HARVESTER_TOGGLE)
                .addTooltip(0, "Enable Harvest")
                .addTooltip(1, "Disable Harvest")
                .setBackground(GTUITextures.BUTTON_STANDARD)
                .setPos(67, 63)
                .setSize(18, 18));
        builder.widget(
            new CycleButtonWidget().setToggle(() -> harvestFullGrowth, val -> harvestFullGrowth = val)
                .setTexture(GTPPUITextures.OVERLAY_BUTTON_HARVESTER_GROWTH_TOGGLE)
                .addTooltip(0, "Enable Full Growth Harvest")
                .addTooltip(1, "Disable Full Growth Harvest")
                .setBackground(GTUITextures.BUTTON_STANDARD)
                .setPos(87, 63)
                .setSize(18, 18));
        builder.widget(
            SlotGroup.ofItemHandler(inventoryHandler, 2)
                .startFromSlot(SLOT_WEEDEX_1)
                .endAtSlot(SLOT_WEEDEX_2)
                .applyForWidget(
                    widget -> widget.setFilter(x -> ItemList.IC2_Spray_WeedEx.isStackEqual(x, true, true))
                        .setBackground(getGUITextureSet().getItemSlot(), GTPPUITextures.OVERLAY_SLOT_WEED_EX))
                .build()
                .setPos(7, 13))
            .widget(
                SlotGroup.ofItemHandler(inventoryHandler, 2)
                    .startFromSlot(SLOT_FERT_1)
                    .endAtSlot(SLOT_FERT_4)
                    .applyForWidget(
                        widget -> widget.setFilter(ItemList.IC2_Fertilizer::isStackEqual)
                            .setBackground(getGUITextureSet().getItemSlot(), GTPPUITextures.OVERLAY_SLOT_FERTILIZER))
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
                    .setTexture(GTPPUITextures.PROGRESSBAR_BOILER_EMPTY, GTUITextures.PROGRESSBAR_BOILER_WATER, 54)
                    .setDirection(ProgressBar.Direction.UP)
                    .setProgress(() -> (float) getFluidAmount() / getCapacity())
                    .setSynced(false, false)
                    .dynamicTooltip(
                        () -> Collections.singletonList(
                            StatCollector.translateToLocalFormatted(
                                "gtpp.gui.crop_harvestor.tooltip.water",
                                getFluidAmount(),
                                getCapacity())))
                    .setPos(47, 7)
                    .setSize(10, 54))
            .widget(new FakeSyncWidget.FluidStackSyncer(this::getDrainableStack, this::setDrainableStack));
    }
}
