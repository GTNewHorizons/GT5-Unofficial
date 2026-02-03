package kubatech.tileentity.gregtech.multiblock.modularui2;

import com.cleanroommc.modularui.utils.serialization.IByteBufDeserializer;
import com.cleanroommc.modularui.utils.serialization.IByteBufSerializer;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

//some code from PR#5320 DroneConnection.java
//this one is just basicly a
public class KubaDisplayInvetorySyncHandler extends GenericListSyncHandler<ItemStack> {
    public KubaDisplayInvetorySyncHandler(@NotNull Supplier<List<ItemStack>> getter, @NotNull IByteBufDeserializer<ItemStack> deserializer, @NotNull IByteBufSerializer<ItemStack> serializer) {
        super(getter, null, deserializer, serializer, null, null);
    }

    // Todo. fix this
    public static class EasyKubaDisplayInvetorySyncValue extends EasyListSyncValue<KubaDisplayInvetorySyncHandler, ItemStack> {
        private final Supplier<List<ItemStack>> getter;
        public EasyKubaDisplayInvetorySyncValue(String name, Supplier<List<ItemStack>> getter) {
            super(name);
            this.getter = getter;
        }

        @Override
        protected void registerHandler() {
            this.handler = new KubaDisplayInvetorySyncHandler(getter, EasyKubaDisplayInvetorySyncValue::deserialize, EasyKubaDisplayInvetorySyncValue::serialize);
        }

        public static void serialize(PacketBuffer buf, ItemStack stack) throws IOException {
            buf.writeNBTTagCompoundToBuffer(stack.writeToNBT(new NBTTagCompound()));
        }

        public static ItemStack deserialize(PacketBuffer buf) throws IOException {
            NBTTagCompound tag = buf.readNBTTagCompoundFromBuffer();
            return ItemStack.loadItemStackFromNBT(tag);
        }
    }
}
