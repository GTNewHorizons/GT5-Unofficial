package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.modes.MachineProcessingCondition;
import gregtech.common.covers.modes.TransferMode;
import gregtech.common.gui.modularui.cover.CoverFluidRegulatorGui;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import gregtech.common.gui.mui1.cover.FluidRegulatorUIFactory;
import io.netty.buffer.ByteBuf;

public class CoverFluidRegulator extends Cover {

    public static final int TICK_RATE_MIN = 1;
    public static final int TICK_RATE_MAX = 2048;

    public final int mTransferRate;
    private boolean allowFluid = false;
    private int tickRate;
    private int speed;
    private Conditional condition;

    public CoverFluidRegulator(CoverContext context, int aTransferRate, ITexture coverTexture) {
        super(context, coverTexture);
        this.mTransferRate = aTransferRate;
        this.tickRate = TICK_RATE_MIN;
        this.speed = 0;
        this.condition = Conditional.Always;
    }

    public int getTransferRate() {
        return mTransferRate;
    }

    public int getTickRateForUi() {
        return tickRate;
    }

    public CoverFluidRegulator setTickRateForUi(int tickRate) {
        this.tickRate = tickRate;
        return this;
    }

    public TransferMode getIOMode() {
        return speed >= 0 ? TransferMode.EXPORT : TransferMode.IMPORT;
    }

    public void setIOMode(TransferMode mode) {
        if (switch (mode) {
            case EXPORT -> speed < 0;
            case IMPORT -> speed >= 0;
        }) {
            speed = -speed;
        }
    }

    public int getSpeed() {
        return speed;
    }

    public CoverFluidRegulator setSpeed(int speed) {
        this.speed = speed;
        return this;
    }

    public MachineProcessingCondition getMachineProcessingCondition() {
        return switch (condition) {
            case Always -> MachineProcessingCondition.ALWAYS;
            case Conditional -> MachineProcessingCondition.CONDITIONAL;
            case Inverted -> MachineProcessingCondition.INVERTED;
        };
    }

    public void setMachineProcessingCondition(MachineProcessingCondition mode) {
        condition = switch (mode) {
            case ALWAYS -> Conditional.Always;
            case CONDITIONAL -> Conditional.Conditional;
            case INVERTED -> Conditional.Inverted;
        };
    }

    public Conditional getCondition() {
        return condition;
    }

    public CoverFluidRegulator setCondition(Conditional condition) {
        this.condition = condition;
        return this;
    }

    @Override
    protected void readDataFromNbt(NBTBase nbt) {
        if (!(nbt instanceof NBTTagCompound tag)) return; // not very good...
        speed = tag.getInteger("mSpeed");
        tickRate = tag.getInteger("mTickRate");
        condition = Conditional.VALUES[tag.getByte("mCondition")];
    }

    @Override
    public void readDataFromPacket(ByteArrayDataInput byteData) {
        this.tickRate = byteData.readInt();
        this.speed = byteData.readInt();
        this.condition = Conditional.VALUES[byteData.readUnsignedByte()];
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("mSpeed", speed);
        tag.setInteger("mTickRate", tickRate);
        tag.setByte("mCondition", (byte) condition.ordinal());
        return tag;
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        byteBuf.writeInt(tickRate)
            .writeInt(speed)
            .writeByte(condition.ordinal());
    }

    @Override
    public boolean isRedstoneSensitive(long aTimer) {
        return condition.isRedstoneSensitive();
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null || speed == 0 || !condition.isAllowedToWork(coverSide, coverID, coverable)) {
            return;
        }
        if ((coverable instanceof IFluidHandler fluidHandler)) {
            final IFluidHandler tTank1;
            final IFluidHandler tTank2;
            final ForgeDirection directionFrom;
            if (speed > 0) {
                tTank2 = coverable.getITankContainerAtSide(coverSide);
                tTank1 = fluidHandler;
                directionFrom = coverSide;
            } else {
                tTank1 = coverable.getITankContainerAtSide(coverSide);
                tTank2 = fluidHandler;
                directionFrom = coverSide.getOpposite();
            }
            if (tTank1 != null && tTank2 != null) {
                allowFluid = true;
                GTUtility.moveFluid(tTank1, tTank2, directionFrom, Math.abs(speed), this::canTransferFluid);
                allowFluid = false;
            }
        }
    }

    private String getScredriverClickChat() {
        if (Math.abs(speed) == getMaxSpeed()) {
            return GTUtility.trans("316", "Pump speed limit reached!");
        }
        if (tickRate == 1) {
            return GTUtility.trans("048", "Pump speed: ") + speed
                + GTUtility.trans("049", "L/tick ")
                + speed * 20
                + GTUtility.trans("050", "L/sec");
        }
        return String.format(
            GTUtility.trans("207", "Pump speed: %dL every %d ticks, %.2f L/sec on average"),
            speed,
            tickRate,
            speed * 20d / tickRate);
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int step = aPlayer.isSneaking() ? 256 : 16;
        if (faceClickedOnRightSide(aX, aY, aZ)) {
            speed += step;
        } else {
            speed -= step;
        }
        if (Math.abs(speed) > getMaxSpeed()) {
            speed = getMaxSpeed() * (speed > 0 ? 1 : -1);
        }
        GTUtility.sendChatToPlayer(aPlayer, getScredriverClickChat());
    }

    public int getMaxSpeed() {
        if ((long) mTransferRate * tickRate > Integer.MAX_VALUE) return Integer.MAX_VALUE;
        return mTransferRate * tickRate;
    }

    public int getMinSpeed() {
        return -getMaxSpeed();
    }

    private boolean faceClickedOnRightSide(float aX, float aY, float aZ) {
        return GTUtility.getClickedFacingCoords(coverSide, aX, aY, aZ)[0] >= 0.5F;
    }

    protected boolean canTransferFluid(FluidStack fluid) {
        return true;
    }

    @Override
    public boolean letsRedstoneGoIn() {
        return true;
    }

    @Override
    public boolean letsRedstoneGoOut() {
        return true;
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
    public boolean letsItemsIn(int aSlot) {
        return true;
    }

    @Override
    public boolean letsItemsOut(int aSlot) {
        return true;
    }

    @Override
    public boolean letsFluidIn(Fluid aFluid) {
        return allowFluid;
    }

    @Override
    public boolean letsFluidOut(Fluid aFluid) {
        return allowFluid;
    }

    @Override
    public boolean alwaysLookConnected() {
        return true;
    }

    @Override
    public int getMinimumTickRate() {
        return tickRate;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    protected @NotNull CoverBaseGui<?> getCoverGui() {
        return new CoverFluidRegulatorGui(this);
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new FluidRegulatorUIFactory(buildContext).createWindow();
    }

    @Override
    public boolean allowsTickRateAddition() {
        return false;
    }

    public enum Conditional {

        Always(false) {

            @Override
            boolean isAllowedToWork(ForgeDirection coverSide, int coverID, ICoverable coverable) {
                return true;
            }
        },
        Conditional(true) {

            @Override
            boolean isAllowedToWork(ForgeDirection coverSide, int coverID, ICoverable coverable) {
                return !(coverable instanceof IMachineProgress) || ((IMachineProgress) coverable).isAllowedToWork();
            }
        },
        Inverted(true) {

            @Override
            boolean isAllowedToWork(ForgeDirection coverSide, int coverID, ICoverable coverable) {
                return !(coverable instanceof IMachineProgress) || !((IMachineProgress) coverable).isAllowedToWork();
            }
        };

        static final Conditional[] VALUES = values();
        private final boolean redstoneSensitive;

        Conditional(boolean redstoneSensitive) {
            this.redstoneSensitive = redstoneSensitive;
        }

        abstract boolean isAllowedToWork(ForgeDirection coverSide, int coverID, ICoverable coverable);

        boolean isRedstoneSensitive() {
            return redstoneSensitive;
        }
    }
}
