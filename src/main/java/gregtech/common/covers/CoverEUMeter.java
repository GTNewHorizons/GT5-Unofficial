package gregtech.common.covers;

import static net.minecraft.util.StatCollector.translateToLocal;

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
import gregtech.common.gui.modularui.cover.CoverEUMeterGui;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import gregtech.common.gui.mui1.cover.EUMeterUIFactory;
import io.netty.buffer.ByteBuf;

public class CoverEUMeter extends Cover implements Invertable {

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
            case 0 -> GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.univ_normal");
            case 1 -> GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.univ_invert");
            case 2 -> GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.eu_normal");
            case 3 -> GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.eu_invert");
            case 4 -> GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.steam_normal");
            case 5 -> GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.steam_invert");
            case 6 -> GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.eu_avg_i_normal");
            case 7 -> GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.eu_avg_i_invert");
            case 8 -> GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.eu_avg_o_normal");
            case 9 -> GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.eu_avg_o_invert");
            case 10 -> GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.eu_batt_normal");
            case 11 -> GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.eu_batt_invert");
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
    protected @NotNull CoverBaseGui<?> getCoverGui() {
        return new CoverEUMeterGui(this);
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new EUMeterUIFactory(buildContext).createWindow();
    }

    // endregion

    public enum EnergyType {

        UNIVERSAL_STORAGE(translateToLocal("gt.interact.desc.Energy_Detector.universal"),
            translateToLocal("gt.interact.desc.Energy_Detector.universal.tooltip"),
            ICoverable::getUniversalEnergyStored, ICoverable::getUniversalEnergyCapacity),
        ELECTRICITY_STORAGE(translateToLocal("gt.interact.desc.Energy_Detector.electricity"),
            translateToLocal("gt.interact.desc.Energy_Detector.electricity.tooltip"), ICoverable::getStoredEU,
            ICoverable::getEUCapacity),
        STEAM_STORAGE(translateToLocal("gt.interact.desc.Energy_Detector.steam"),
            translateToLocal("gt.interact.desc.Energy_Detector.steam.tooltip"), ICoverable::getStoredSteam,
            ICoverable::getSteamCapacity),
        AVERAGE_ELECTRIC_INPUT(translateToLocal("gt.interact.desc.Energy_Detector.average_in"),
            translateToLocal("gt.interact.desc.Energy_Detector.average_in.tooltip"),
            ICoverable::getAverageElectricInput, (te) -> te.getInputVoltage() * te.getInputAmperage()),
        AVERAGE_ELECTRIC_OUTPUT(translateToLocal("gt.interact.desc.Energy_Detector.average_out"),
            translateToLocal("gt.interact.desc.Energy_Detector.average_out.tooltip"),
            ICoverable::getAverageElectricOutput, (te) -> te.getOutputVoltage() * te.getOutputAmperage()),
        ELECTRICITY_STORAGE_INCLUDING_BATTERIES(translateToLocal("gt.interact.desc.Energy_Detector.eu_stored"),
            translateToLocal("gt.interact.desc.Energy_Detector.eu_stored.tooltip"), (te) -> {
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
