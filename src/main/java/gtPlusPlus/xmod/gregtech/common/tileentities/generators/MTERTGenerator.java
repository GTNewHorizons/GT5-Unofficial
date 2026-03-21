package gtPlusPlus.xmod.gregtech.common.tileentities.generators;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicGenerator;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.common.pollution.Pollution;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import tectech.util.TTUtility;

public class MTERTGenerator extends MTEBasicGenerator {

    private long mTicksToBurnFor;
    private int mVoltage = 0;
    private GTRecipe mCurrentRecipe;
    private int mDaysRemaining = 0;
    private int mDayTick = 0;
    private byte mNewTier = 0;

    public int removeDayOfTime() {
        if (this.mDaysRemaining > 0) {
            return this.mDaysRemaining--;
        }
        return this.mDaysRemaining;
    }

    // Generates fuel value based on MC days
    public static int convertDaysToTicks(float days) {
        return MathUtils.roundToClosestInt(20 * 86400 * days);
    }

    public static long getTotalEUGenerated(int ticks, int voltage) {
        return (long) ticks * voltage;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setLong("mTicksToBurnFor", this.mTicksToBurnFor);
        aNBT.setInteger("mVoltage", this.mVoltage);
        aNBT.setInteger("mDaysRemaining", this.mDaysRemaining);
        aNBT.setInteger("mDayTick", this.mDayTick);
        aNBT.setByte("mNewTier", this.mNewTier);

        if (this.mCurrentRecipe != null) {
            final NBTTagList list = new NBTTagList();
            final ItemStack stack = this.mCurrentRecipe.mInputs[0];
            if (stack != null) {
                final NBTTagCompound data = new NBTTagCompound();
                stack.writeToNBT(data);
                data.setInteger("mSlot", 0);
                list.appendTag(data);
            }
            aNBT.setTag("mRecipeItem", list);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.mTicksToBurnFor = aNBT.getLong("mTicksToBurnFor");
        this.mVoltage = aNBT.getInteger("mVoltage");
        this.mDaysRemaining = aNBT.getInteger("mDaysRemaining");
        this.mDayTick = aNBT.getInteger("mDayTick");
        this.mNewTier = aNBT.getByte("mNewTier");
        TTUtility.setTier(this.mNewTier, this);
        final NBTTagList list = aNBT.getTagList("mRecipeItem", 10);
        final NBTTagCompound data = list.getCompoundTagAt(0);
        ItemStack lastUsedFuel = ItemStack.loadItemStackFromNBT(data);
        if (lastUsedFuel != null) {
            this.mCurrentRecipe = getRecipeMap().findRecipeQuery()
                .items(lastUsedFuel)
                .find();
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (this.mDayTick < 24000) {
                this.mDayTick++;
            } else {
                this.mDayTick = 0;
                this.mDaysRemaining = this.removeDayOfTime();
            }
        }

        if ((aBaseMetaTileEntity.isServerSide()) && (aBaseMetaTileEntity.isAllowedToWork()) && (aTick % 10L == 0L)) {
            long tProducedEU = 0L;
            if (this.mFluid == null) {
                if (aBaseMetaTileEntity.getUniversalEnergyStored() < maxEUOutput() + getMinimumStoredEU()) {
                    this.mInventory[getStackDisplaySlot()] = null;
                } else {
                    if (this.mInventory[getStackDisplaySlot()] == null)
                        this.mInventory[getStackDisplaySlot()] = new ItemStack(Blocks.fire, 1);
                    this.mInventory[getStackDisplaySlot()].setStackDisplayName(
                        "Generating: "
                            + formatNumber(aBaseMetaTileEntity.getUniversalEnergyStored() - getMinimumStoredEU())
                            + " EU");
                }
            } else {
                int tFuelValue = getFuelValue(this.mFluid);
                int tConsumed = consumedFluidPerOperation(this.mFluid);
                if ((tFuelValue > 0) && (tConsumed > 0) && (this.mFluid.amount > tConsumed)) {
                    long tFluidAmountToUse = Math.min(
                        this.mFluid.amount / tConsumed,
                        (maxEUStore() - aBaseMetaTileEntity.getUniversalEnergyStored()) / tFuelValue);
                    if ((tFluidAmountToUse > 0L)
                        && (aBaseMetaTileEntity.increaseStoredEnergyUnits(tFluidAmountToUse * tFuelValue, true))) {
                        tProducedEU = tFluidAmountToUse * tFuelValue;
                        FluidStack tmp260_257 = this.mFluid;
                        tmp260_257.amount = (int) (tmp260_257.amount - (tFluidAmountToUse * tConsumed));
                    }
                }
            }
            if ((this.mInventory[getInputSlot()] != null)
                && (aBaseMetaTileEntity.getUniversalEnergyStored() < maxEUOutput() * 20L + getMinimumStoredEU())
                && (GTUtility.getFluidForFilledItem(this.mInventory[getInputSlot()], true) == null)) {
                int tFuelValue = getFuelValue(this.mInventory[getInputSlot()]);
                if (tFuelValue > 0) {
                    ItemStack tEmptyContainer = getEmptyContainer(this.mInventory[getInputSlot()]);
                    if (aBaseMetaTileEntity.addStackToSlot(getOutputSlot(), tEmptyContainer)) {
                        aBaseMetaTileEntity.increaseStoredEnergyUnits(tFuelValue, true);
                        aBaseMetaTileEntity.decrStackSize(getInputSlot(), 1);
                        tProducedEU = tFuelValue;
                    }
                }
            }
            if ((tProducedEU > 0L) && (getPollution() > 0)) {
                Pollution
                    .addPollution(aBaseMetaTileEntity, (int) (tProducedEU * getPollution() / 500 * this.mTier + 1L));
            }
        }

        if (aBaseMetaTileEntity.isServerSide()) aBaseMetaTileEntity.setActive(
            (aBaseMetaTileEntity.isAllowedToWork())
                && (aBaseMetaTileEntity.getUniversalEnergyStored() >= maxEUOutput() + getMinimumStoredEU()));
    }

    @Override
    public String[] getDescription() {
        return ArrayUtils.addAll(
            this.mDescriptionArray,
            "Fuel is measured in minecraft days (Check with Scanner)",
            "RTG changes output voltage depending on fuel",
            "Generates power at " + addFormattedString(formatNumber(this.getEfficiency())) + "%% Efficiency per tick",
            "Output Voltage: " + addFormattedString(formatNumber(this.getOutputTier())) + " EU/t",
            GTPPCore.GT_Tooltip.get());
    }

    public MTERTGenerator(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, "Requires RTG Pellets");
    }

    private byte getTier() {
        int voltage = this.mVoltage;
        if (voltage >= 512) {
            return 4;
        } else if (voltage >= 128) {
            return 3;
        } else if (voltage >= 32) {
            return 2;
        } else if (voltage >= 8) {
            return 1;
        }
        return 0;
    }

    public MTERTGenerator(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return ((side.offsetY == 0) && (side != getBaseMetaTileEntity().getFrontFacing())
            && (side != getBaseMetaTileEntity().getBackFacing()));
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTERTGenerator(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.rtgFuels;
    }

    @Override
    public int getCapacity() {
        return 0;
    }

    @Override
    public int getEfficiency() {
        return 100;
    }

    @Override
    public ITexture[] getFront(byte aColor) {
        return new ITexture[] { TextureFactory.of(Textures.BlockIcons.OVERLAY_SIDE_RTG) };
    }

    @Override
    public ITexture[] getBack(byte aColor) {
        return new ITexture[] { TextureFactory.of(Textures.BlockIcons.OVERLAY_SIDE_RTG) };
    }

    @Override
    public ITexture[] getBottom(byte aColor) {
        return new ITexture[] { TextureFactory.of(Textures.BlockIcons.OVERLAY_TOP_RTG) };
    }

    @Override
    public ITexture[] getTop(byte aColor) {
        return new ITexture[] { TextureFactory.of(Textures.BlockIcons.OVERLAY_TOP_RTG) };
    }

    @Override
    public ITexture[] getSides(byte aColor) {
        return new ITexture[] { TextureFactory.of(Textures.BlockIcons.OVERLAY_SIDE_RTG),
            Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_2A[getTier() + 1] };
    }

    @Override
    public ITexture[] getFrontActive(byte aColor) {
        return new ITexture[] { TextureFactory.of(Textures.BlockIcons.OVERLAY_SIDE_RTG_ACTIVE), TextureFactory.builder()
            .addIcon(Textures.BlockIcons.OVERLAY_SIDE_RTG_ACTIVE_GLOW)
            .glow()
            .build() };
    }

    @Override
    public ITexture[] getBackActive(byte aColor) {
        return new ITexture[] { TextureFactory.of(Textures.BlockIcons.OVERLAY_SIDE_RTG_ACTIVE), TextureFactory.builder()
            .addIcon(Textures.BlockIcons.OVERLAY_SIDE_RTG_ACTIVE_GLOW)
            .glow()
            .build() };
    }

    @Override
    public ITexture[] getBottomActive(byte aColor) {
        return new ITexture[] { TextureFactory.of(Textures.BlockIcons.OVERLAY_TOP_RTG_ACTIVE), TextureFactory.builder()
            .addIcon(Textures.BlockIcons.OVERLAY_TOP_RTG_ACTIVE_GLOW)
            .glow()
            .build() };
    }

    @Override
    public ITexture[] getTopActive(byte aColor) {
        return new ITexture[] { TextureFactory.of(Textures.BlockIcons.OVERLAY_TOP_RTG_ACTIVE), TextureFactory.builder()
            .addIcon(Textures.BlockIcons.OVERLAY_TOP_RTG_ACTIVE_GLOW)
            .glow()
            .build() };
    }

    @Override
    public ITexture[] getSidesActive(byte aColor) {
        return new ITexture[] { TextureFactory.of(Textures.BlockIcons.OVERLAY_SIDE_RTG_ACTIVE), TextureFactory.builder()
            .addIcon(Textures.BlockIcons.OVERLAY_SIDE_RTG_ACTIVE_GLOW)
            .glow()
            .build(), Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_2A[getTier() + 1] };
    }

    @Override
    public int getPollution() {
        return 0;
    }

    @Override
    public int getFuelValue(ItemStack aStack) {
        if ((GTUtility.isStackInvalid(aStack)) || (getRecipeMap() == null)) return 0;
        GTRecipe tFuel = getRecipeMap().findRecipeQuery()
            .items(aStack)
            .find();
        if (tFuel != null) {
            this.mCurrentRecipe = tFuel;
            int voltage = tFuel.mEUt;
            this.mVoltage = voltage;

            // Do some voodoo.
            byte mTier2;
            try {
                if (ItemStack.areItemStacksEqual(tFuel.mInputs[0], GregtechItemList.Pellet_RTG_AM241.get(1))) {
                    mTier2 = 1;
                } else if (ItemStack.areItemStacksEqual(tFuel.mInputs[0], GregtechItemList.Pellet_RTG_PO210.get(1))) {
                    mTier2 = 3;
                } else if (ItemStack.areItemStacksEqual(tFuel.mInputs[0], GregtechItemList.Pellet_RTG_PU238.get(1))) {
                    mTier2 = 2;
                } else if (ItemStack.areItemStacksEqual(tFuel.mInputs[0], GregtechItemList.Pellet_RTG_SR90.get(1))) {
                    mTier2 = 1;
                } else {
                    mTier2 = 0;
                }
                TTUtility.setTier(mTier2, this);
                this.mNewTier = mTier2;
            } catch (Exception e) {
                Logger.WARNING("Failed setting mTier.");
                e.printStackTrace();
            }

            this.mTicksToBurnFor = getTotalEUGenerated(convertDaysToTicks(tFuel.mSpecialValue), voltage);
            if (mTicksToBurnFor >= Integer.MAX_VALUE) {
                mTicksToBurnFor = Integer.MAX_VALUE;
                Logger.WARNING("Fuel went over Int limit, setting to MAX_VALUE.");
            }
            this.mDaysRemaining = MathUtils.roundToClosestInt(mTicksToBurnFor / 20 / 60 / 3);
            Logger.WARNING("step | " + (int) (mTicksToBurnFor * getEfficiency() / 100L));
            return (int) (mTicksToBurnFor * getEfficiency() / 100L);
        }
        Logger.WARNING("Not sure");
        return 0;
    }

    @Override
    public long maxEUOutput() {
        return ((getBaseMetaTileEntity().isAllowedToWork()) ? this.mVoltage : 0L);
    }

    @Override
    public long getOutputTier() {
        if (this.mCurrentRecipe != null) {
            return this.mVoltage = this.mCurrentRecipe.mEUt;
        }
        return 0;
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[] { StatCollector.translateToLocalFormatted("gtpp.infodata.rtg.running_at", this.mTier),
            StatCollector.translateToLocalFormatted(
                "gtpp.infodata.rtg.active",
                this.getBaseMetaTileEntity()
                    .isActive()),
            StatCollector.translateToLocalFormatted("gtpp.infodata.rtg.output", formatNumber(mVoltage)),
            StatCollector.translateToLocalFormatted(
                "gtpp.infodata.rtg.remaining.days",
                formatNumber(mTicksToBurnFor / 20 / 60 / 20)),
            StatCollector.translateToLocalFormatted(
                "gtpp.infodata.rtg.remaining.hours",
                formatNumber(mTicksToBurnFor / 20 / 60 / 60)),
            StatCollector
                .translateToLocalFormatted("gtpp.infodata.rtg.remaining.ticks", this.mVoltage, mTicksToBurnFor),
            this.mCurrentRecipe.mInputs[0].getDisplayName() + " x1" };
    }
}
