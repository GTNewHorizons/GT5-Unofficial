package gregtech.common.covers;

import java.util.List;
import java.util.UUID;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.google.common.io.ByteArrayDataInput;

import gregtech.api.covers.CoverContext;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetricsExporter;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.util.ISerializableObject;
import gregtech.common.events.MetricsCoverDataEvent;
import gregtech.common.misc.GlobalMetricsCoverDatabase;
import gregtech.common.misc.GlobalMetricsCoverDatabase.State;
import io.netty.buffer.ByteBuf;

/**
 * Used to transmit Nuclear Control information across dimensions. The only reason this is a cover is to artificially
 * limit the number of total machines that transmit this data, for performance reasons.
 * <p>
 * This cover will retrieve information, preferentially, using {@link IMetricsExporter#reportMetrics()}. Absent this
 * method, it will resort to {@link BaseMetaTileEntity#getInfoData()} instead.
 */
public class CoverMetricsTransmitter extends CoverBehaviorBase<CoverMetricsTransmitter.MetricsTransmitterData> {

    @SuppressWarnings("SpellCheckingInspection")
    public static final String FREQUENCY_MSB_KEY = "gt.metricscover.freq_msb";
    @SuppressWarnings("SpellCheckingInspection")
    public static final String FREQUENCY_LSB_KEY = "gt.metricscover.freq_lsb";
    public static final String MACHINE_KEY = "machine_name";
    public static final String CARD_STATE_KEY = "card_state";

    public CoverMetricsTransmitter(CoverContext context, ITexture coverTexture) {
        super(context, MetricsTransmitterData.class, coverTexture);
    }

    @Override
    protected MetricsTransmitterData createDataObject() {
        return new CoverMetricsTransmitter.MetricsTransmitterData();
    }

    @Override
    public int getTickRate() {
        return 20;
    }

    @Override
    public CoverMetricsTransmitter.MetricsTransmitterData doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return coverData;
        }
        if (coverable instanceof final BaseMetaTileEntity baseMTE && baseMTE.isGivingInformation()) {
            final List<String> payload;

            if (baseMTE.getMetaTileEntity() instanceof final IMetricsExporter metricsExporter) {
                payload = metricsExporter.reportMetrics();
            } else {
                payload = ImmutableList.copyOf(baseMTE.getInfoData());
            }

            MinecraftForge.EVENT_BUS.post(new MetricsCoverDataEvent(
                coverData.frequency,
                payload,
                new GlobalMetricsCoverDatabase.Coordinates(
                    baseMTE.getWorld().provider.getDimensionName(),
                    baseMTE.getXCoord(),
                    baseMTE.getYCoord(),
                    baseMTE.getZCoord()
                )
            ));
        }

        return coverData;
    }

    @Override
    public void onPlayerAttach(EntityPlayer player, ItemStack cover) {
        final UUID newFrequency = UUID.randomUUID();
        final ItemStack cardStack = ItemList.NC_AdvancedSensorCard.get(1);
        ICoverable coverable = coveredTile.get();

        if (cardStack == null || coverable == null) {
            return;
        }

        final NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setLong(FREQUENCY_MSB_KEY, newFrequency.getMostSignificantBits());
        tagCompound.setLong(FREQUENCY_LSB_KEY, newFrequency.getLeastSignificantBits());
        tagCompound.setInteger(CARD_STATE_KEY, State.OPERATIONAL.getType());

        if (coverable instanceof final BaseMetaTileEntity baseMTE) {
            final ItemStack baseMTEStack = baseMTE.getStackForm(1);
            if (baseMTEStack != null) {
                tagCompound.setTag(MACHINE_KEY, baseMTEStack.writeToNBT(new NBTTagCompound()));
            }
        }

        cardStack.setTagCompound(tagCompound);
        coverable.getCoverAtSide(coverSide)
            .setCoverData(new MetricsTransmitterData(newFrequency));

        final EntityItem cardEntity = new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, cardStack);
        cardEntity.delayBeforeCanPickup = 0;
        player.worldObj.spawnEntityInWorld(cardEntity);
    }

    @Override
    public boolean allowsCopyPasteTool() {
        return false;
    }

    @NotNull
    @Override
    public List<String> getAdditionalTooltip() {
        return ImmutableList.of(
            StatCollector.translateToLocalFormatted(
                "gt.item.adv_sensor_card.tooltip.frequency",
                EnumChatFormatting.UNDERLINE.toString() + EnumChatFormatting.YELLOW + coverData.frequency.toString()));
    }

    public static class MetricsTransmitterData implements ISerializableObject {

        private UUID frequency;

        public MetricsTransmitterData() {
            this.frequency = UUID.randomUUID();
        }

        public MetricsTransmitterData(@NotNull UUID frequency) {
            this.frequency = frequency;
        }

        @NotNull
        public UUID getFrequency() {
            return frequency;
        }

        @NotNull
        @Override
        public ISerializableObject copy() {
            return new MetricsTransmitterData(frequency);
        }

        @NotNull
        @Override
        public NBTBase saveDataToNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setLong(FREQUENCY_MSB_KEY, frequency.getMostSignificantBits());
            tag.setLong(FREQUENCY_LSB_KEY, frequency.getLeastSignificantBits());
            return tag;
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            if (aNBT instanceof final NBTTagCompound tag) {
                frequency = new UUID(tag.getLong(FREQUENCY_MSB_KEY), tag.getLong(FREQUENCY_LSB_KEY));
            }
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            aBuf.writeLong(frequency.getMostSignificantBits());
            aBuf.writeLong(frequency.getLeastSignificantBits());
        }

        @NotNull
        @Override
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf) {
            return new MetricsTransmitterData(new UUID(aBuf.readLong(), aBuf.readLong()));
        }
    }
}
