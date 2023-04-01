package gregtech.common;

import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;
import java.util.WeakHashMap;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;

import gregtech.GT_Mod;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_UO_Dimension;
import gregtech.api.objects.GT_UO_Fluid;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GT_ChunkAssociatedData;

/**
 * Created by Tec on 29.04.2017.
 */
public class GT_UndergroundOil {

    public static final short DIVIDER = 5000;
    private static final GT_UndergroundOilStore STORAGE = new GT_UndergroundOilStore();
    private static final ChunkData NIL_FLUID_STACK = new ChunkData(-1, null, null, false);

    /**
     * Effectively just call {@code undergroundOil(te, -1)} for you
     * 
     * @see #undergroundOil(World, int, int, float)
     */
    public static FluidStack undergroundOilReadInformation(IGregTechTileEntity te) {
        return undergroundOil(
                te.getWorld()
                  .getChunkFromBlockCoords(te.getXCoord(), te.getZCoord()),
                -1);
    }

    /**
     * Effectively just call {@code undergroundOil(chunk, -1)} for you
     * 
     * @see #undergroundOil(World, int, int, float)
     */
    public static FluidStack undergroundOilReadInformation(Chunk chunk) {
        return undergroundOil(chunk, -1);
    }

    /** @see #undergroundOil(World, int, int, float) */
    public static FluidStack undergroundOil(IGregTechTileEntity te, float readOrDrainCoefficient) {
        return undergroundOil(
                te.getWorld()
                  .getChunkFromBlockCoords(te.getXCoord(), te.getZCoord()),
                readOrDrainCoefficient);
    }

    // Returns whole content for information purposes -> when drainSpeedCoefficient < 0
    // Else returns extracted fluidStack if amount > 0, or null otherwise
    /** @see #undergroundOil(World, int, int, float) */
    public static FluidStack undergroundOil(Chunk chunk, float readOrDrainCoefficient) {
        return undergroundOil(chunk.worldObj, chunk.xPosition, chunk.zPosition, readOrDrainCoefficient);
    }

    /**
     * Pump fluid or read info.
     * 
     * @param w                      a remote World. For a WorldClient it will always tell you null
     * @param chunkX                 chunk coordinate X, i.e. blockX >> 4
     * @param chunkZ                 chunk coordinate Z, i.e. blockZ >> 4
     * @param readOrDrainCoefficient how fast to pump. The higher the faster. use negative to read expected current
     *                               output
     * @return null if nothing here, or depleted already, or a client side world
     */
    public static FluidStack undergroundOil(World w, int chunkX, int chunkZ, float readOrDrainCoefficient) {
        if (w.isRemote) return null; // troublemakers go away
        ChunkData chunkData = STORAGE.get(w, chunkX, chunkZ);
        if (chunkData.getVein() == null || chunkData.getFluid() == null) // nothing here...
            return null;
        // do stuff on it if needed
        FluidStack fluidInChunk = new FluidStack(chunkData.getFluid(), 0);
        if (readOrDrainCoefficient >= 0) {
            int fluidExtracted = (int) Math.floor(chunkData.getAmount() * (double) readOrDrainCoefficient / DIVIDER);
            double averageDecrease = chunkData.getVein().DecreasePerOperationAmount * (double) readOrDrainCoefficient;
            int decrease = (int) Math.ceil(averageDecrease);
            if (fluidExtracted <= 0 || chunkData.amount <= decrease) { // decrease - here it is max value of extraction
                                                                       // for easy check
                chunkData.setAmount(0);
            } else {
                fluidInChunk.amount = fluidExtracted; // give appropriate amount
                if (XSTR_INSTANCE.nextFloat() < (decrease - averageDecrease)) decrease--; // use XSTR_INSTANCE to
                                                                                          // "subtract double from int"
                // ex.
                // averageDecrease=3.9
                // decrease= ceil from 3.9 = 4
                // decrease-averageDecrease=0.1 -> chance to subtract 1
                // if XSTR_INSTANCE is < chance then subtract 1
                chunkData.changeAmount(-decrease); // diminish amount, "randomly" adjusted to double value
                                                   // (averageDecrease)
            }
        } else { // just get info
            if (chunkData.amount <= DIVIDER) {
                chunkData.setAmount(0);
            } else {
                // get the expected current output
                fluidInChunk.amount = (int) Math.floor(
                        chunkData.getAmount() * (double) -readOrDrainCoefficient / DIVIDER);
            }
        }
        return fluidInChunk;
    }

    /**
     * Get the deposit as if it is never exploited
     * 
     * @return UO fluid kind and amount, or null if nothing here.
     */
    public static Pair<GT_UO_Fluid, Integer> getPristineAmount(World world, int chunkX, int chunkZ) {
        int dimensionId = world.provider.dimensionId;
        GT_UO_Dimension dimension = GT_Mod.gregtechproxy.mUndergroundOil.GetDimension(dimensionId);
        if (dimension == null) return null;
        // prepare RNG
        final XSTR tVeinRNG = new XSTR(world.getSeed() + dimensionId * 2L + (chunkX >> 3) + 8267L * (chunkZ >> 3));
        GT_UO_Fluid uoFluid = dimension.getRandomFluid(tVeinRNG);
        // nothing here :(
        if (uoFluid == null || uoFluid.getFluid() == null) return null;
        // offset each chunk's fluid amount by +-25%
        // discard random values not for current chunk
        int veinAverage = uoFluid.getRandomAmount(tVeinRNG);
        for (int i = 0; i < (((chunkX & 0x7) << 3) | chunkZ & 0x7); i++) {
            tVeinRNG.next(24);
        }
        int amount = (int) ((float) veinAverage * (0.75f + (tVeinRNG.nextFloat() / 2f)));
        return Pair.of(uoFluid, amount);
    }

    static void migrate(ChunkDataEvent.Load e) {
        if (e.getData()
             .hasKey("GTOIL")
                && e.getData()
                    .hasKey("GTOILFLUID")) {
            ChunkData chunkData = STORAGE.get(e.getChunk());
            Fluid fluid = chunkData.getFluid();
            if (fluid != null && fluid.getID() == e.getData()
                                                   .getInteger("GTOILFLUID"))
                chunkData.setAmount(
                        Math.min(
                                chunkData.getAmount(),
                                e.getData()
                                 .getInteger("GTOIL")));
        }
    }

    /**
     * Revamped UO store.
     * <p>
     * Primary functionality:
     *
     * <ul>
     * <li>Decouple data storage with chunk, making it possible to pump oil from unloaded chunks</li>
     * <li>Regen detection. If fluid generation config is changed, chunk fluid will be regenerated.</li>
     * </ul>
     *
     * <h2>Serialized form</h2>
     * <p>
     * Since the exact file layout is controlled by the super class, here we only concern how each chunk's data is
     * written.
     * <h3>Form A: Empty Chunk</h3>
     * <ol>
     * <li>4 bytes of 0</li>
     * </ol>
     *
     * <h3>Form B: Normal Chunk</h3>
     * <ol>
     * <li>4 bytes unsigned integer. Vein Hash.</li>
     * <li>UTF string. Vein Key.</li>
     * <li>4 bytes signed integer. Fluid amount.</li>
     * </ol>
     *
     * @author glease
     */
    @ParametersAreNonnullByDefault
    private static class GT_UndergroundOilStore extends GT_ChunkAssociatedData<ChunkData> {

        private static final WeakHashMap<GT_UO_Fluid, Integer> hashes = new WeakHashMap<>();

        private GT_UndergroundOilStore() {
            super("UO", GT_UndergroundOil.ChunkData.class, 64, (byte) 0, false);
        }

        @Override
        protected void writeElement(DataOutput output, ChunkData element, World world, int chunkX, int chunkZ)
                throws IOException {
            /* see class javadoc for explanation */
            output.writeInt(element.getVeinHash());
            if (element.getVeinKey() == null) return;
            output.writeUTF(element.getVeinKey());
            if (element.getAmount() > 0 && element.getFluid() != null) {
                output.writeInt(element.getAmount());
            } else {
                output.writeInt(-1);
            }
        }

        @Override
        protected GT_UndergroundOil.ChunkData readElement(DataInput input, int version, World world, int chunkX,
                int chunkZ) throws IOException {
            /* see class javadoc for explanation */
            if (version != 0) throw new IOException("Region file corrupted");
            GT_UndergroundOil.ChunkData pristine = createElement(world, chunkX, chunkZ);
            int hash = input.readInt();
            String veinKey = hash != 0 ? input.readUTF() : null;
            int amount = hash != 0 ? input.readInt() : -1;
            if (hash != pristine.veinHash || !Objects.equals(veinKey, pristine.getVeinKey())) {
                // vein config changed. use regen-ed data.
                return pristine;
            }
            if (hash == 0) return NIL_FLUID_STACK;
            return new GT_UndergroundOil.ChunkData(
                    amount,
                    GT_Mod.gregtechproxy.mUndergroundOil.GetDimension(world.provider.dimensionId)
                                                        .getUOFluid(veinKey),
                    veinKey);
        }

        @Override
        protected GT_UndergroundOil.ChunkData createElement(World world, int chunkX, int chunkZ) {
            Pair<GT_UO_Fluid, Integer> pristine = getPristineAmount(world, chunkX, chunkZ);
            if (pristine == null) return NIL_FLUID_STACK;
            int dimensionId = world.provider.dimensionId;
            GT_UO_Dimension dimension = GT_Mod.gregtechproxy.mUndergroundOil.GetDimension(dimensionId);
            return new GT_UndergroundOil.ChunkData(
                    pristine.getRight(),
                    pristine.getLeft(),
                    dimension.getUOFluidKey(pristine.getLeft()),
                    false);
        }

        private static int hash(@Nullable GT_UO_Fluid fluid) {
            if (fluid == null) return 0;
            int result = fluid.Registry.hashCode();
            result = 31 * result + fluid.MaxAmount;
            result = 31 * result + fluid.MinAmount;
            result = 31 * result + fluid.Chance;
            result = 31 * result + fluid.DecreasePerOperationAmount;
            return result == 0 ? 1 : result;
        }
    }

    /**
     * Represent the amount of fluid in a given chunk.
     */
    private static final class ChunkData implements GT_ChunkAssociatedData.IData {

        private final Fluid fluid;

        @Nullable
        private final GT_UO_Fluid vein;

        private final String veinKey;
        private final int veinHash;
        private int amount;
        private boolean dirty;

        private ChunkData(int amount, GT_UO_Fluid veinKey, String veinID) {
            this(amount, veinKey, veinID, true);
        }

        private ChunkData(int amount, @Nullable GT_UO_Fluid vein, @Nullable String veinKey, boolean dirty) {
            this.amount = amount;
            this.vein = vein;
            this.dirty = dirty;
            if (vein == null) {
                fluid = null;
                this.veinKey = null;
                veinHash = 0;
            } else {
                fluid = vein.getFluid();
                this.veinKey = veinKey;
                veinHash = GT_UndergroundOilStore.hashes.computeIfAbsent(vein, GT_UndergroundOilStore::hash);
            }
        }

        /**
         * The current fluid type. {@code null} if vein is generated to be empty.
         */
        @Nullable
        public Fluid getFluid() {
            return fluid;
        }

        /**
         * Current fluid amount. Might be 0 if empty. Cannot be negative
         */
        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            if (this.amount != amount) dirty = true;
            this.amount = Math.max(0, amount);
        }

        public void changeAmount(int delta) {
            if (delta != 0) dirty = true;
            this.amount = Math.max(amount + delta, 0);
        }

        @Nullable
        public GT_UO_Fluid getVein() {
            return vein;
        }

        /**
         * The vein ID. Might be null if generated to be empty.
         */
        @Nullable
        public String getVeinKey() {
            return veinKey;
        }

        public int getVeinHash() {
            return veinHash;
        }

        @Override
        public boolean isSameAsDefault() {
            return !dirty;
        }
    }
}
