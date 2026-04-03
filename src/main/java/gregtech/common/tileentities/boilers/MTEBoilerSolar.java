package gregtech.common.tileentities.boilers;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static mcp.mobius.waila.api.SpecialChars.GOLD;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.modularui2.GTGuiTheme;
import gregtech.api.modularui2.GTGuiThemes;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTSplit;
import gregtech.common.config.MachineStats;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

@IMetaTileEntity.SkipGenerateDescription
public class MTEBoilerSolar extends MTEBoiler {

    public static final String LPS_FMT = "%s L/s";
    protected int calcificationTicks = MachineStats.bronzeSolarBoiler.calcificationTicks;
    protected int cooldownTicks = MachineStats.bronzeSolarBoiler.cooldownTicks;
    protected int maxOutputPerSecond = MachineStats.bronzeSolarBoiler.maxOutputPerSecond;
    protected int minOutputPerSecond = MachineStats.bronzeSolarBoiler.minOutputPerSecond;

    protected final int basicTemperatureMod = 5; // Base Celsius gain or loss
    private int mRunTimeTicks = 0;

    public MTEBoilerSolar(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, GTValues.emptyStringArray);
    }

    public MTEBoilerSolar(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public int getMaxOutputPerSecond() {
        return maxOutputPerSecond;
    }

    @Override
    public String[] getDescription() {
        return GTSplit.splitLocalizedFormatted(
            "gt.blockmachines.boiler.solar.desc",
            formatNumber(getMaxOutputPerSecond()),
            formatNumber(getMinOutputPerSecond()));
    }

    public int getMinOutputPerSecond() {
        return minOutputPerSecond;
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[5][17][];
        for (int color = -1; color < 16; color++) {
            int i = color + 1;
            short[] colorModulation = Dyes.getModulation(color);
            rTextures[0][i] = new ITexture[] {
                TextureFactory.of(BlockIcons.MACHINE_BRONZEBRICKS_BOTTOM, colorModulation) };
            rTextures[1][i] = new ITexture[] { TextureFactory.of(BlockIcons.MACHINE_BRONZEBRICKS_TOP, colorModulation),
                TextureFactory.of(BlockIcons.BOILER_SOLAR) };
            rTextures[2][i] = new ITexture[] {
                TextureFactory.of(BlockIcons.MACHINE_BRONZEBRICKS_SIDE, colorModulation) };
            rTextures[3][i] = new ITexture[] { TextureFactory.of(BlockIcons.MACHINE_BRONZEBRICKS_SIDE, colorModulation),
                TextureFactory.of(BlockIcons.OVERLAY_PIPE) };
            rTextures[4][i] = new ITexture[] { TextureFactory.of(BlockIcons.MACHINE_BRONZEBRICKS_TOP, colorModulation),
                TextureFactory.of(BlockIcons.BOILER_SOLAR_CALCIFIED) };
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
        if (sideDirection == ForgeDirection.UP) {
            if (isCalcified()) {
                return mTextures[4][i];
            }
            return mTextures[1][i];
        }
        return mTextures[sideDirection.ordinal()][i];
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
        if (mFluid.isFluidEqual(Materials.Water.getFluid(1))) {
            // produceSteam is getting called every 10 ticks
            if (mRunTimeTicks >= 0 && mRunTimeTicks < (Integer.MAX_VALUE - 10)) mRunTimeTicks += 10;
            else mRunTimeTicks = Integer.MAX_VALUE; // Prevent Integer overflow wrap
            getBaseMetaTileEntity().issueTileUpdate();
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

    protected boolean isCalcified() {
        return mRunTimeTicks > getCalcificationTicks();
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
    public NBTTagCompound getDescriptionData() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("RuntimeTicks", mRunTimeTicks);
        return tag;
    }

    @Override
    public void onDescriptionPacket(NBTTagCompound data) {
        super.onDescriptionPacket(data);
        mRunTimeTicks = data.getInteger("RuntimeTicks");
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[] { StatCollector.translateToLocalFormatted(
            "GT5U.infodata.boiler_solar.heat",
            String.format(
                EnumChatFormatting.GREEN + "%s %%" + EnumChatFormatting.RESET,
                formatNumber(getHeatCapacityPercent())),
            String
                .format(EnumChatFormatting.RED + "%s s" + EnumChatFormatting.RESET, formatNumber(getHotTimeSeconds()))),
            StatCollector.translateToLocalFormatted(
                "GT5U.infodata.boiler_solar.output",
                String.format(
                    EnumChatFormatting.RED + LPS_FMT + EnumChatFormatting.RESET,
                    formatNumber(getMinOutputPerSecond())),
                String.format(
                    EnumChatFormatting.RED + LPS_FMT + EnumChatFormatting.RESET,
                    formatNumber(getMaxOutputPerSecond()))),
            StatCollector.translateToLocalFormatted(
                "GT5U.infodata.boiler_solar.current_output",
                String.format(
                    EnumChatFormatting.YELLOW + LPS_FMT + EnumChatFormatting.RESET,
                    formatNumber(getProductionPerSecond()))) };
    }

    public int getHeatCapacityPercent() {
        return mTemperature * 100 / maxProgresstime();
    }

    public int getHotTimeSeconds() {
        return mRunTimeTicks / 20;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBoilerSolar(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    protected GTGuiTheme getGuiTheme() {
        return GTGuiThemes.BRONZE;
    }

    @Override
    public boolean doesAddFuelSlot() {
        return false;
    }

    @Override
    public boolean doesAddAshSlot() {
        return false;
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        final NBTTagCompound tag = accessor.getNBTData();
        currentTip.add(
            GOLD + StatCollector.translateToLocalFormatted(
                "GT5U.waila.boiler_solar.output",
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
