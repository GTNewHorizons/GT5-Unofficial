package tectech.mechanics.pipe;

import javax.annotation.Nullable;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import tectech.mechanics.dataTransport.ALRecipeDataPacket;
import tectech.mechanics.dataTransport.QuantumDataPacket;
import tectech.thing.metaTileEntity.hatch.MTEHatchDataInput;
import tectech.thing.metaTileEntity.hatch.MTEHatchDataItemsInput;
import tectech.thing.metaTileEntity.hatch.MTEHatchDataItemsOutput;
import tectech.thing.metaTileEntity.hatch.MTEHatchDataOutput;
import tectech.thing.metaTileEntity.pipe.MTEPipeData;

/**
 * Created by Tec on 26.02.2017.
 */
public interface IConnectsToDataPipe<T> {

    int TICKS_CONNECTION_POLLING = 100;

    boolean canConnectData(ForgeDirection side);

    /**
     * The first caller is supposed to be a colored hatch. Pipes implement this interface need to overload this method!
     * @param source the calling {@link IConnectsToDataPipe}, for grabbing its colorization.
     * @return the neighboring {@link IConnectsToDataPipe} or {@code null} if none is found.
     */
    default @Nullable IConnectsToDataPipe<T> getNext(IConnectsToDataPipe<T> source) {
        if (canClear()) return null;

        if (!(source instanceof MTEHatch hatch)) return null;

        IGregTechTileEntity base = hatch.getBaseMetaTileEntity();
        byte color = source.getColorization();
        if (color < 0) {
            return null;
        }
        IGregTechTileEntity next = base.getIGregTechTileEntityAtSide(base.getFrontFacing());
        if (next == null) {
            return null;
        }
        IMetaTileEntity meta = next.getMetaTileEntity();
        return switch (meta) {
            case MTEPipeData pipe -> {
                pipe.markUsed();
                // noinspection rawtypes,unchecked
                yield (IConnectsToDataPipe) pipe;
            }
            case MTEHatchDataInput dataInput -> {
                if (dataInput.getColorization() == color && dataInput.canConnectData(base.getFrontFacing().getOpposite())) {
                    // noinspection unchecked
                    yield (IConnectsToDataPipe<T>) dataInput;
                }
                yield null;
            }
            case MTEHatchDataItemsInput stickInput -> {
                if (stickInput.getColorization() == color && stickInput.canConnectData(base.getFrontFacing().getOpposite())) {
                    // noinspection unchecked
                    yield (IConnectsToDataPipe<T>) stickInput;
                }
                yield null;
            }
            default -> null;
        };
    }

    /**
     * Checks for a connection from an output subclass like {@link MTEHatchDataOutput} or {@link MTEHatchDataItemsOutput}
     * to an input subclass like {@link MTEHatchDataInput} or {@link MTEHatchDataItemsInput}.
     * Keeps track of the connected input and updates the data if new data is pushed or the slow polling detects either a new connection or disconnection.
     * <p>
     * Non-hatches should return {@link CheckState#UNKNOWN}!
     *
     * @return a resulting check state, used for post moveAround processing of subclasses
     */
    default CheckState moveAround(IGregTechTileEntity aBaseMetaTileEntity, CheckState checkState) {
        // safety check
        if (checkState != CheckState.CONNECTION && checkState != CheckState.NEW_DATA) return CheckState.UNKNOWN;
        IConnectsToDataPipe<T> current = this, source = this, next;
        var oldConnected = getLastConnected();
        var result = CheckState.UNKNOWN;
        setLastConnected(null);
        int range = 0;
        boolean isDataOutput = this instanceof MTEHatchDataOutput;
        boolean isStickOutput = this instanceof MTEHatchDataItemsOutput;
        while ((next = current.getNext(source)) != null && range++ < 1000) {
            if (isStickOutput && next instanceof MTEHatchDataItemsInput dataItemsInput) {
                if (checkState == CheckState.NEW_DATA) {
                    result = CheckState.NEW_DATA;
                } else {
                    result = CheckState.CONNECTED;
                }
                dataItemsInput.setContent((ALRecipeDataPacket) this.getQueued());
                setLastConnected(next);
                break;
            } else if (isDataOutput && next instanceof MTEHatchDataInput dataInput) {
                if (checkState == CheckState.NEW_DATA) {
                    result = CheckState.NEW_DATA;
                } else {
                    result = CheckState.CONNECTED;
                }
                dataInput.setContent((QuantumDataPacket) this.getQueued());
                setLastConnected(next);
                break;
            }

            source = current;
            current = next;
        }

        // broken connection or redirected connection,
        // we need to remove the data from the old connected hatch if possible
        if (getLastConnected() == null || getLastConnected() != oldConnected) {
            switch (oldConnected) {
                case MTEHatchDataInput dataInput -> dataInput.setContent(null);
                case MTEHatchDataItemsInput dataItemsInput -> dataItemsInput.setContent(null);
                // oldConnected can be null, technically the default should catch it, but I got NPEs in some cases
                case null -> {}
                default -> {}
            }
            result = CheckState.DISCONNECTED;
        }
        return result;
    }

    /**
     * Safely enqueue and move the specified content for output hatches by calling
     * {@link #setContent(Object)} and {@link #moveAround(IGregTechTileEntity, CheckState)} internally.
     * 
     * @param content
     */
    default void enqueueAndMove(@Nullable T content) {
        if (canClear()) return;
        if (!(this instanceof MTEHatch hatch)) return;
        setContent(content);
        moveAround(hatch.getBaseMetaTileEntity(), CheckState.NEW_DATA);
    }

    boolean isDataInputFacing(ForgeDirection side);

    default byte getColorization() {
        return switch (this) {
            case MTEHatch hatch -> hatch.getBaseMetaTileEntity().getColorization();
            case MTEPipeData pipe -> pipe.getBaseMetaTileEntity().getColorization();
            default -> -1;
        };
    }

    @Nullable
    IConnectsToDataPipe<T> getLastConnected();

    void setLastConnected(@Nullable IConnectsToDataPipe<T> connected);

    @Nullable
    T getQueued();

    /**
     * Sets the content for hatches, they should replace {@code null} with a non-null empty or dummy packet
     * depending on the scenario.
     * This packet is the {@link #getQueued()} packet.
     * <p>
     * If you want that an output hatch updates its content and sends it to the connected input,
     * you should call {@link #enqueueAndMove(Object)}.
     *
     * @param content the packet
     */
    void setContent(@Nullable T content);

    /**
     * Implementing input hatches should return {@code true}!
     * Default implementation returns {@code false}.
     * 
     * @return whether the implementation is an "input" and thereby can be cleared,
     *         as no multiblock is feeding the input content directly.
     */
    default boolean canClear() {
        return false;
    }

    enum CheckState {
        /**
         * Uses {@link #moveAround(IGregTechTileEntity, CheckState)} api, but doesn't pass data, because the DataInput
         * does
         */
        UNKNOWN,
        /**
         * Indicating that from the DataOutput to the DataInput is only checked for a connection,
         * this state should update the connected input's data on {@link #CONNECTED} or {@link #DISCONNECTED} usually
         * without modifying the DataOutput's data.
         * It's also never returned, only passed as an argument!
         */
        CONNECTION,
        /**
         * Indicating that from the DataOutput to the DataInput is checked for a connection and intentionally for
         * pushing new data.
         * This state is returned after it successfully pushed new data to the connected input.
         * It's returned and passed as an argument!
         */
        NEW_DATA,
        /**
         * This state is returned if the DataOutput is connected to a DataInput and {@link #CONNECTION} was passed as an
         * argument.
         * The DataInput's data has been updated.
         */
        CONNECTED,
        /**
         * This state is returned if the DataOutput is not connected to a DataInput no matter the passed argument.
         * The DataInput's data has been set to null.
         */
        DISCONNECTED
    }
}
