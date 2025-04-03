package gregtech.common.covers;

import java.util.function.Function;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicBatteryBuffer;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.mui1.cover.EUMeterUIFactory;
import io.netty.buffer.ByteBuf;

public class CoverEUMeter extends Cover {

    private EnergyType type;
    private boolean inverted;
    /**
     * The special value {@code 0} means threshold check is disabled.
     */
    private long threshold;

    public CoverEUMeter(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
        type = EnergyType.UNIVERSAL_STORAGE;
        inverted = false;
        threshold = 0;
    }

    public EnergyType getType() {
        return this.type;
    }

    public CoverEUMeter setType(EnergyType type) {
        this.type = type;
        return this;
    }

    public boolean isInverted() {
        return this.inverted;
    }

    public CoverEUMeter setInverted(boolean inverted) {
        this.inverted = inverted;
        return this;
    }

    public long getThreshold() {
        return this.threshold;
    }

    public CoverEUMeter setThresdhold(long threshold) {
        this.threshold = threshold;
        return this;
    }

    public int getNum() {
        return type.ordinal() * 2 + (inverted ? 1 : 0);
    }

    public void setNum(int num) {
        type = EnergyType.getEnergyType(num / 2);
        inverted = num % 2 == 1;
    }

    @Override
    protected void readDataFromNbt(NBTBase nbt) {
        NBTTagCompound tag = (NBTTagCompound) nbt;
        int typeOrdinal = tag.getInteger("typeOrdinal");
        type = EnergyType.getEnergyType(typeOrdinal);
        inverted = tag.getBoolean("inverted");
        threshold = tag.getLong("threshold");
    }

    @Override
    public void readDataFromPacket(ByteArrayDataInput byteData) {
        int typeOrdinal = byteData.readInt();
        type = EnergyType.getEnergyType(typeOrdinal);
        inverted = byteData.readBoolean();
        threshold = byteData.readLong();
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("typeOrdinal", type.ordinal());
        tag.setBoolean("inverted", inverted);
        tag.setLong("threshold", threshold);
        return tag;
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        byteBuf.writeInt(type.ordinal());
        byteBuf.writeBoolean(inverted);
        byteBuf.writeLong(threshold);
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        {
            ICoverable coverable = coveredTile.get();
            if (coverable == null) {
                return;
            }
            final long stored = type.getTileEntityStoredEnergy(coverable);
            final long capacity = type.getTileEntityEnergyCapacity(coverable);

            byte redstoneSignal;

            if (stored == 0L) {
                // nothing
                redstoneSignal = 0;
            } else if (stored >= capacity) {
                // full
                redstoneSignal = 15;
            } else {
                // 1-14 range
                redstoneSignal = (byte) (1 + (14 * stored) / capacity);
            }

            if (inverted) {
                redstoneSignal = (byte) (15 - redstoneSignal);
            }

            if (threshold > 0) {
                if (inverted && stored >= threshold) {
                    redstoneSignal = 0;
                } else if (!inverted && stored < threshold) {
                    redstoneSignal = 0;
                }
            }

            coverable.setOutputRedstoneSignal(coverSide, redstoneSignal);
        }
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int num = (getNum() + (aPlayer.isSneaking() ? -1 : 1) + EnergyType.values().length * 2)
            % (EnergyType.values().length * 2);
        switch (num) {
            case 0 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("031", "Normal Universal Storage"));
            case 1 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("032", "Inverted Universal Storage"));
            case 2 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("033", "Normal Electricity Storage"));
            case 3 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("034", "Inverted Electricity Storage"));
            case 4 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("035", "Normal Steam Storage"));
            case 5 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("036", "Inverted Steam Storage"));
            case 6 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("037", "Normal Average Electric Input"));
            case 7 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("038", "Inverted Average Electric Input"));
            case 8 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("039", "Normal Average Electric Output"));
            case 9 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("040", "Inverted Average Electric Output"));
            case 10 -> GTUtility
                .sendChatToPlayer(aPlayer, GTUtility.trans("041", "Normal Electricity Storage(Including Batteries)"));
            case 11 -> GTUtility
                .sendChatToPlayer(aPlayer, GTUtility.trans("042", "Inverted Electricity Storage(Including Batteries)"));
        }
        setNum(num);
    }

    // region Static Result Methods
    @Override
    public boolean isRedstoneSensitive(long aTimer) {
        return false;
    }

    @Override
    public boolean letsEnergyIn() {
        return true;
    }

    @Override
    public boolean letsEnergyOut() {
        return true;
    }

    @Override
    public boolean letsFluidIn(Fluid aFluid) {
        return true;
    }

    @Override
    public boolean letsFluidOut(Fluid aFluid) {
        return true;
    }

    @Override
    public boolean letsItemsIn(int aSlot) {
        return true;
    }

    @Override
    public boolean letsItemsOut(int aSlot) {
        return true;
    }

    @Override
    public boolean manipulatesSidedRedstoneOutput() {
        return true;
    }

    @Override
    public int getMinimumTickRate() {
        return 20;
    }
    // endregion

    // region GUI Stuff
    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new EUMeterUIFactory(buildContext).createWindow();
    }

    // endregion

    public enum EnergyType {

        UNIVERSAL_STORAGE(GTUtility.trans("301", "Universal"), GTUtility.trans("256", "Universal Storage"),
            ICoverable::getUniversalEnergyStored, ICoverable::getUniversalEnergyCapacity),
        ELECTRICITY_STORAGE(GTUtility.trans("302", "Int. EU"), GTUtility.trans("257", "Electricity Storage"),
            ICoverable::getStoredEU, ICoverable::getEUCapacity),
        STEAM_STORAGE(GTUtility.trans("303", "Steam"), GTUtility.trans("258", "Steam Storage"),
            ICoverable::getStoredSteam, ICoverable::getSteamCapacity),
        AVERAGE_ELECTRIC_INPUT(GTUtility.trans("304", "Avg. Input"), GTUtility.trans("259", "Average Electric Input"),
            ICoverable::getAverageElectricInput, (te) -> te.getInputVoltage() * te.getInputAmperage()),
        AVERAGE_ELECTRIC_OUTPUT(GTUtility.trans("305", "Avg. Output"),
            GTUtility.trans("260", "Average Electric Output"), ICoverable::getAverageElectricOutput,
            (te) -> te.getOutputVoltage() * te.getOutputAmperage()),
        ELECTRICITY_STORAGE_INCLUDING_BATTERIES(GTUtility.trans("306", "EU stored"),
            GTUtility.trans("261", "Electricity Storage(Including Batteries)"), (te) -> {
                if (te instanceof IGregTechTileEntity) {
                    IMetaTileEntity mte = ((IGregTechTileEntity) te).getMetaTileEntity();
                    if (mte instanceof MTEBasicBatteryBuffer buffer) {
                        return buffer.getStoredEnergy()[0];
                    }
                }
                return te.getStoredEU();
            }, (te) -> {
                if (te instanceof IGregTechTileEntity) {
                    IMetaTileEntity mte = ((IGregTechTileEntity) te).getMetaTileEntity();
                    if (mte instanceof MTEBasicBatteryBuffer buffer) {
                        return buffer.getStoredEnergy()[1];
                    }
                }
                return te.getEUCapacity();
            });

        private final String title;
        private final String tooltip;
        private final Function<ICoverable, Long> getTileEntityStoredEnergyFunc;
        private final Function<ICoverable, Long> getTileEntityEnergyCapacityFunc;

        EnergyType(String title, String tooltip, Function<ICoverable, Long> getTileEntityStoredEnergyFunc,
            Function<ICoverable, Long> getTileEntityEnergyCapacityFunc) {
            this.title = title;
            this.tooltip = tooltip;
            this.getTileEntityStoredEnergyFunc = getTileEntityStoredEnergyFunc;
            this.getTileEntityEnergyCapacityFunc = getTileEntityEnergyCapacityFunc;
        }

        public String getTitle() {
            return title;
        }

        public String getTooltip() {
            return tooltip;
        }

        public long getTileEntityStoredEnergy(ICoverable coverable) {
            return getTileEntityStoredEnergyFunc.apply(coverable);
        }

        public long getTileEntityEnergyCapacity(ICoverable coverable) {
            return getTileEntityEnergyCapacityFunc.apply(coverable);
        }

        public EnergyType getNext() {
            return values()[(ordinal() + 1) % values().length];
        }

        public static EnergyType getEnergyType(int ordinal) {
            if (ordinal < 0 || values().length <= ordinal) {
                ordinal = 0;
            }
            return values()[ordinal];
        }
    }
}
