package gregtech.common.tileentities.boilers;

import static mcp.mobius.waila.api.SpecialChars.GOLD;
import static mcp.mobius.waila.api.SpecialChars.RESET;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.SteamVariant;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.common.config.machinestats.ConfigBronzeSolarBoiler;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class GT_MetaTileEntity_Boiler_Solar extends GT_MetaTileEntity_Boiler {

    public static final String LPS_FMT = "%s L/s";
    private static final String localizedDescFormat = GT_LanguageManager.addStringLocalization(
        "gt.blockmachines.boiler.solar.desc.format",
        "Steam Power by the Sun%n" + "Produces %sL of Steam per second%n"
            + "Calcifies over time, reducing Steam output to %sL/s%n"
            + "Break and replace to descale");
    protected int calcificationTicks = ConfigBronzeSolarBoiler.calcificationTicks;
    protected int cooldownTicks = ConfigBronzeSolarBoiler.cooldownTicks;
    protected int maxOutputPerSecond = ConfigBronzeSolarBoiler.maxOutputPerSecond;
    protected int minOutputPerSecond = ConfigBronzeSolarBoiler.minOutputPerSecond;

    protected final int basicTemperatureMod = 5; // Base Celsius gain or loss
    private int mRunTimeTicks = 0;

    public GT_MetaTileEntity_Boiler_Solar(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, new String[0]);
    }

    public GT_MetaTileEntity_Boiler_Solar(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Boiler_Solar(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public int getMaxOutputPerSecond() {
        return maxOutputPerSecond;
    }

    @Override
    public String[] getDescription() {
        return String
            .format(
                localizedDescFormat,
                GT_Utility.formatNumbers(getMaxOutputPerSecond()),
                GT_Utility.formatNumbers(getMinOutputPerSecond()))
            .split("\\R");
    }

    public int getMinOutputPerSecond() {
        return minOutputPerSecond;
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[4][17][];
        for (int color = -1; color < 16; color++) {
            int i = color + 1;
            short[] colorModulation = Dyes.getModulation(color, Dyes._NULL.mRGBa);
            rTextures[0][i] = new ITexture[] {
                TextureFactory.of(BlockIcons.MACHINE_BRONZEBRICKS_BOTTOM, colorModulation) };
            rTextures[1][i] = new ITexture[] { TextureFactory.of(BlockIcons.MACHINE_BRONZEBRICKS_TOP, colorModulation),
                TextureFactory.of(BlockIcons.BOILER_SOLAR) };
            rTextures[2][i] = new ITexture[] {
                TextureFactory.of(BlockIcons.MACHINE_BRONZEBRICKS_SIDE, colorModulation) };
            rTextures[3][i] = new ITexture[] { TextureFactory.of(BlockIcons.MACHINE_BRONZEBRICKS_SIDE, colorModulation),
                TextureFactory.of(BlockIcons.OVERLAY_PIPE) };
        }
        return rTextures;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        final int i = colorIndex + 1;
        if ((sideDirection.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) == 0) { // Horizontal
            if (sideDirection != facingDirection) return mTextures[2][i];
            return mTextures[3][i];
        }
        return mTextures[sideDirection.ordinal()][i];
    }

    @Override
    public int maxProgresstime() {
        return 500;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mRunTime", mRunTimeTicks);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mRunTimeTicks = aNBT.getInteger("mRunTime");
    }

    @Override
    protected void produceSteam(int aAmount) {
        super.produceSteam(aAmount);
        // Disable calcification when using distilled water
        if (mFluid.isFluidEqual(GT_ModHandler.getWater(1))) {
            // produceSteam is getting called every 10 ticks
            if (mRunTimeTicks >= 0 && mRunTimeTicks < (Integer.MAX_VALUE - 10)) mRunTimeTicks += 10;
            else mRunTimeTicks = Integer.MAX_VALUE; // Prevent Integer overflow wrap
        }
    }

    @Override
    protected void pushSteamToInventories(IGregTechTileEntity aBaseMetaTileEntity) {
        if (mSteam == null || mSteam.amount == 0) return;
        pushSteamToSide(aBaseMetaTileEntity, aBaseMetaTileEntity.getFrontFacing());
    }

    @Override
    protected int getPollution() {
        return 0;
    }

    @Override
    public int getProductionPerSecond() {
        if (mTemperature < 100) {
            return 0;
        }
        if (mRunTimeTicks > getMaxRuntimeTicks()) {
            return getMinOutputPerSecond();
        } else if (mRunTimeTicks > getCalcificationTicks()) {
            /*
             * When reaching calcification ticks; discount the proportion of run-time spent on calcification from the
             * maximum output per second, and return this or the minimum output per second
             */
            return getMaxOutputPerSecond()
                - getMaxOutputPerSecond() * (mRunTimeTicks - getCalcificationTicks()) / getCalcificationTicks();
        } else {
            return getMaxOutputPerSecond();
        }
    }

    protected int getCalcificationTicks() {
        return calcificationTicks;
    }

    protected int getCooldownTicks() {
        return cooldownTicks;
    }

    protected int getMaxRuntimeTicks() {
        // After which min output is reached.
        return (getMaxOutputPerSecond() - getMinOutputPerSecond()) * getCalcificationTicks() / getMaxOutputPerSecond()
            + getCalcificationTicks();
    }

    @Override
    protected int getMaxTemperature() {
        return 500;
    }

    @Override
    protected int getEnergyConsumption() {
        return basicTemperatureMod;
    }

    @Override
    protected int getCooldownInterval() {
        return getCooldownTicks() / basicTemperatureMod;
    }

    @Override
    protected int getHeatUpAmount() {
        return basicTemperatureMod;
    }

    @Override
    protected void updateFuel(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        World world = aBaseMetaTileEntity.getWorld();
        // Heat-up every 12s (240 ticks), has to be multiple of 20 ticks
        if ((aTick % 240L != 0L) || (world.isThundering())) {
            return;
        }
        if (!aBaseMetaTileEntity.getSkyAtSide(ForgeDirection.UP)) {
            return;
        }
        boolean weatherClear = !world.isRaining() || aBaseMetaTileEntity.getBiome().rainfall == 0.0F;
        if (!weatherClear && world.skylightSubtracted >= 4) {
            return;
        }
        if (weatherClear) {
            if (world.isDaytime()) {
                mProcessingEnergy += 8 * basicTemperatureMod;
            } else {
                mProcessingEnergy += basicTemperatureMod;
            }
        } else {
            mProcessingEnergy += basicTemperatureMod;
        }
    }

    @Override
    public SteamVariant getSteamVariant() {
        return SteamVariant.BRONZE;
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return String
            .format(
                "Heat Capacity: " + EnumChatFormatting.GREEN
                    + "%s %%"
                    + EnumChatFormatting.RESET
                    + "    Hot time: "
                    + EnumChatFormatting.RED
                    + "%s s"
                    + EnumChatFormatting.RESET
                    + "%n"
                    + "Min output: "
                    + EnumChatFormatting.RED
                    + LPS_FMT
                    + EnumChatFormatting.RESET
                    + "    Max output: "
                    + EnumChatFormatting.RED
                    + LPS_FMT
                    + EnumChatFormatting.RESET
                    + "%n"
                    + "Current Output: "
                    + EnumChatFormatting.YELLOW
                    + LPS_FMT
                    + EnumChatFormatting.RESET,
                GT_Utility.formatNumbers(getHeatCapacityPercent()),
                GT_Utility.formatNumbers(getHotTimeSeconds()),
                GT_Utility.formatNumbers(getMinOutputPerSecond()),
                GT_Utility.formatNumbers(getMaxOutputPerSecond()),
                GT_Utility.formatNumbers(getProductionPerSecond()))
            .split("\\R");
    }

    public int getHeatCapacityPercent() {
        return mTemperature * 100 / maxProgresstime();
    }

    public int getHotTimeSeconds() {
        return mRunTimeTicks / 20;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Boiler_Solar(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    protected Widget createFuelSlot() {
        return null;
    }

    @Override
    protected SlotWidget createAshSlot() {
        return null;
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        final NBTTagCompound tag = accessor.getNBTData();
        currentTip.add(
            String.format(
                (GOLD + "Solar Boiler Output: " + RESET + "%d/%d L/s"),
                tag.getInteger("calcificationOutput"),
                tag.getInteger("maxCalcificationOutput")));

        super.getWailaBody(itemStack, currentTip, accessor, config);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("calcificationOutput", (getProductionPerSecond()));
        tag.setInteger("maxCalcificationOutput", (getMaxOutputPerSecond()));
    }
}
