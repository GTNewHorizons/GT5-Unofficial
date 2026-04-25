package tectech.mechanics.boseEinsteinCondensate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import appeng.api.storage.data.IAEFluidStack;
import appeng.util.item.AEFluidStack;
import gregtech.api.util.GTUtility;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongMaps;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;

public class CondensateList extends Object2LongOpenHashMap<Fluid> {

    public NBTTagCompound saveToNBT() {
        NBTTagCompound tag = new NBTTagCompound();

        Iterator<Object2LongMap.Entry<Fluid>> iter = this.object2LongEntrySet()
            .fastIterator();

        while (iter.hasNext()) {
            var e = iter.next();

            tag.setLong(FluidRegistry.getFluidName(e.getKey()), e.getLongValue());
        }

        return tag;
    }

    public void loadFromNBT(NBTTagCompound tag) {
        clear();

        // noinspection unchecked
        for (Map.Entry<String, NBTTagLong> e : (Set<Map.Entry<String, NBTTagLong>>) tag.tagMap.entrySet()) {
            Fluid fluid = FluidRegistry.getFluid(e.getKey());

            if (fluid == null) continue;

            this.put(
                fluid,
                e.getValue()
                    .func_150291_c());
        }
    }

    public void writeToPacketBuffer(PacketBuffer buffer) throws IOException {
        buffer.writeInt(this.size());

        Iterator<Object2LongMap.Entry<Fluid>> iter = this.object2LongEntrySet()
            .fastIterator();

        while (iter.hasNext()) {
            var e = iter.next();

            buffer.writeStringToBuffer(FluidRegistry.getFluidName(e.getKey()));
            buffer.writeLong(e.getLongValue());
        }
    }

    public void readFromPacketBuffer(PacketBuffer buffer) throws IOException {
        int len = buffer.readInt();

        this.clear();
        this.ensureCapacity(len);

        for (int i = 0; i < len; i++) {
            this.put(FluidRegistry.getFluid(buffer.readStringFromBuffer(4000)), buffer.readLong());
        }
    }

    public List<FluidStack> toFluidStacks() {
        ArrayList<FluidStack> out = new ArrayList<>();

        Object2LongMaps.fastForEach(this, e -> {
            for (long l = 0; l < e.getLongValue(); l += Integer.MAX_VALUE) {
                int amount = GTUtility.longToInt(e.getLongValue() - l);

                out.add(new FluidStack(e.getKey(), amount));
            }
        });

        return out;
    }

    public List<IAEFluidStack> toAEFluidStacks() {
        ArrayList<IAEFluidStack> out = new ArrayList<>();

        Object2LongMaps.fastForEach(
            this,
            e -> {
                out.add(
                    AEFluidStack.create(new FluidStack(e.getKey(), 1))
                        .setStackSize(e.getLongValue()));
            });

        return out;
    }

    @Override
    public CondensateList clone() {
        return (CondensateList) super.clone();
    }
}
