package gregtech.common.tileentities.boilers;

import static gregtech.api.GregTech_API.sMachineFile;
import static gregtech.api.enums.ConfigCategories.machineconfig;
import static mcp.mobius.waila.api.SpecialChars.GOLD;
import static mcp.mobius.waila.api.SpecialChars.RESET;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.SteamVariant;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_Boiler_Solar extends GT_MetaTileEntity_Boiler {

    public static final String LPS_FMT = "%s L/s";
    private static final String localizedDescFormat = GT_LanguageManager.addStringLocalization(
        "gt.blockmachines.boiler.solar.desc.format",
        "Steam Power by the Sun%n" + "Produces %sL of Steam per second%n"
            + "Calcifies over time, reducing Steam output to %sL/s%n"
            + "Break and replace to descale");
    protected final Config mConfig;
    protected int basicTemperatureMod = 5; // Base Celsius gain or loss
    private int mRunTimeTicks = 0;

    public GT_MetaTileEntity_Boiler_Solar(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, new String[0]);
        mConfig = createConfig();
    }

    public GT_MetaTileEntity_Boiler_Solar(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        mConfig = createConfig();
    }

    public GT_MetaTileEntity_Boiler_Solar(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        mConfig = createConfig();
    }

    protected GT_MetaTileEntity_Boiler_Solar(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures,
        Config aConfig) {
        super(aName, aTier, aDescription, aTextures);
        mConfig = aConfig;
    }

    protected Config createConfig() {
        return new Config(machineconfig + ".boiler.solar.bronze", 1080000, 40, 120, 45);
    }

    /**
     * for WAILA
     *
     * @deprecated replaced by {@link #getMaxOutputPerSecond()}
     */
    @Deprecated
    public int getBasicOutput() {
        return (int) (getMaxOutputPerSecond() * 1.25F);
    }

    public int getMaxOutputPerSecond() {
        return mConfig.getMaxOutputPerSecond();
    }

    /**
     * for WAILA
     *
     * @deprecated replaced by {@link #getProductionPerSecond()}
     */
    @SuppressWarnings("unused")
    @Deprecated
    public int getCalcificationOutput() {
        return (int) (getProductionPerSecond() * 1.25F);
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
        return mConfig.getMinOutputPerSecond();
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
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex,
        boolean aActive, boolean aRedstone) {
        int i = aColorIndex + 1;
        if (aSide >= 2) {
            if (aSide != aFacing) return mTextures[2][i];
            return mTextures[3][i];
        }
        return mTextures[aSide][i];
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
        if (mRunTimeTicks > mConfig.getMaxRuntimeTicks()) {
            return mConfig.getMinOutputPerSecond();
        } else if (mRunTimeTicks > mConfig.getCalcificationTicks()) {
            /*
             * When reaching calcification ticks; discount the proportion of run-time spent on calcification from the
             * maximum output per second, and return this or the minimum output per second
             */
            return mConfig.getMaxOutputPerSecond()
                - mConfig.getMaxOutputPerSecond() * (mRunTimeTicks - mConfig.getCalcificationTicks())
                    / mConfig.getCalcificationTicks();
        } else {
            return mConfig.getMaxOutputPerSecond();
        }
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
        return mConfig.getCoolDownTicks() / basicTemperatureMod;
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
        if (!aBaseMetaTileEntity.getSkyAtSide((byte) ForgeDirection.UP.ordinal())) {
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
        return new GT_MetaTileEntity_Boiler_Solar(mName, mTier, mDescriptionArray, mTextures, mConfig);
    }

    @Override
    protected IDrawable[] getFuelSlotBackground() {
        return new IDrawable[] { GT_UITextures.TRANSPARENT };
    }

    @Override
    protected IDrawable[] getAshSlotBackground() {
        return new IDrawable[] { GT_UITextures.TRANSPARENT };
    }

    @Override
    protected SlotWidget createFuelSlot() {
        // todo: remove this slot after some time
        return super.createFuelSlot().setAccess(true, false);
    }

    @Override
    protected SlotWidget createAshSlot() {
        // todo: remove this slot after some time
        return super.createAshSlot().setAccess(true, false);
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

    protected static class Config {

        private final int calcificationTicks;
        private final int minOutputPerSecond;
        private final int maxOutputPerSecond;
        private final int coolDownTicks;
        private final int maxRuntimeTicks;

        public Config(String aCategory, int aDefaultCalcificationTicks, int aDefaultMinOutputPerSecond,
            int aDefaultMaxOutputPerSecond, int aDefaultCoolDownTicks) {
            calcificationTicks = get(
                aCategory,
                "CalcificationTicks",
                aDefaultCalcificationTicks,
                "Number of run-time ticks before boiler starts calcification.",
                "100% calcification and minimal output will be reached at 2 times this.");
            minOutputPerSecond = get(aCategory, "MinOutputPerSecond", aDefaultMinOutputPerSecond);
            maxOutputPerSecond = get(aCategory, "MaxOutputPerSecond", aDefaultMaxOutputPerSecond);
            coolDownTicks = get(
                aCategory,
                "CoolDownTicks",
                aDefaultCoolDownTicks,
                "Number of ticks it takes to lose 1°C.");
            // After which min output is reached.
            maxRuntimeTicks = (getMaxOutputPerSecond() - getMinOutputPerSecond()) * getCalcificationTicks()
                / getMaxOutputPerSecond() + getCalcificationTicks();
        }

        protected int get(final String aCategory, final String aKey, final int aDefaultValue,
            final String... aComments) {
            final StringBuilder tCommentBuilder = new StringBuilder();
            for (String tComment : aComments) tCommentBuilder.append(tComment)
                .append('\n');
            tCommentBuilder.append("Default: ")
                .append(aDefaultValue);
            return sMachineFile.mConfig.get(aCategory, aKey, aDefaultValue, tCommentBuilder.toString())
                .getInt();
        }

        public int getCalcificationTicks() {
            return calcificationTicks;
        }

        public int getMinOutputPerSecond() {
            return minOutputPerSecond;
        }

        public int getMaxOutputPerSecond() {
            return maxOutputPerSecond;
        }

        public int getCoolDownTicks() {
            return coolDownTicks;
        }

        public int getMaxRuntimeTicks() {
            return maxRuntimeTicks;
        }
    }
}
