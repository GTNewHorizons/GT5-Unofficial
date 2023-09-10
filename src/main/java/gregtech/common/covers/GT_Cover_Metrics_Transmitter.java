package gregtech.common.covers;

import java.util.List;
import java.util.UUID;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.io.ByteArrayDataInput;

import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.covers.IPlayerAttachHandler;
import gregtech.api.interfaces.metatileentity.IMetricsExporter;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.ISerializableObject;
import gregtech.common.events.MetricsCoverDataEvent;
import gregtech.common.events.MetricsCoverHostDeconstructedEvent;
import gregtech.common.events.MetricsCoverSelfDestructEvent;
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
public class GT_Cover_Metrics_Transmitter
    extends GT_CoverBehaviorBase<GT_Cover_Metrics_Transmitter.MetricsTransmitterData> implements IPlayerAttachHandler {

    public static final String FREQUENCY_MSB_KEY = "frequency_msb";
    public static final String FREQUENCY_LSB_KEY = "frequency_lsb";
    public static final String MACHINE_NAME_KEY = "machine_name";
    public static final String CARD_STATE_KEY = "card_state";

    @SuppressWarnings("unused")
    public GT_Cover_Metrics_Transmitter() {
        this(null);
    }

    public GT_Cover_Metrics_Transmitter(ITexture coverTexture) {
        super(MetricsTransmitterData.class, coverTexture);
    }

    @Override
    public MetricsTransmitterData createDataObject(int aLegacyData) {
        // As a new cover, this shouldn't fire.
        return new MetricsTransmitterData();
    }

    @Override
    public MetricsTransmitterData createDataObject() {
        return new MetricsTransmitterData();
    }

    @Override
    protected int getTickRateImpl(ForgeDirection side, int aCoverID, MetricsTransmitterData aCoverVariable,
        ICoverable aTileEntity) {
        return 20;
    }

    @Override
    public boolean isCoverPlaceable(ForgeDirection side, ItemStack aStack, ICoverable aTileEntity) {
        return (aTileEntity instanceof final BaseMetaTileEntity baseMTE && baseMTE.isGivingInformation());
    }

    @Override
    protected MetricsTransmitterData doCoverThingsImpl(ForgeDirection side, byte aInputRedstone, int aCoverID,
        MetricsTransmitterData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        if (aTileEntity instanceof final BaseMetaTileEntity baseMTE && baseMTE.isGivingInformation()) {
            final List<String> payload;

            if (baseMTE.getMetaTileEntity() instanceof final IMetricsExporter metricsExporter) {
                payload = metricsExporter.reportMetrics();
            } else {
                payload = ImmutableList.copyOf(baseMTE.getInfoData());
            }

            MinecraftForge.EVENT_BUS.post(new MetricsCoverDataEvent(
                aCoverVariable.frequency,
                payload,
                new GlobalMetricsCoverDatabase.Coordinates(
                    baseMTE.getWorld().provider.getDimensionName(),
                    baseMTE.getXCoord(),
                    baseMTE.getYCoord(),
                    baseMTE.getZCoord()
                )
            ));
        }

        return aCoverVariable;
    }

    @Override
    protected void onDroppedImpl(ForgeDirection side, int aCoverID, MetricsTransmitterData aCoverVariable,
        ICoverable aTileEntity) {
        MinecraftForge.EVENT_BUS.post(new MetricsCoverSelfDestructEvent(aCoverVariable.frequency));
    }

    @Override
    protected void onBaseTEDestroyedImpl(ForgeDirection side, int aCoverID, MetricsTransmitterData aCoverVariable,
        ICoverable aTileEntity) {
        MinecraftForge.EVENT_BUS.post(new MetricsCoverHostDeconstructedEvent(aCoverVariable.frequency));
    }

    @Override
    public void onPlayerAttach(EntityPlayer player, ItemStack aCover, ICoverable aTileEntity, ForgeDirection side) {
        final UUID newFrequency = UUID.randomUUID();
        final ItemStack cardStack = ItemList.NC_AdvancedSensorCard.get(1);

        if (!cardStack.hasTagCompound()) {
            cardStack.setTagCompound(new NBTTagCompound());
        }

        final NBTTagCompound tagCompound = cardStack.getTagCompound();
        tagCompound.setLong(FREQUENCY_MSB_KEY, newFrequency.getMostSignificantBits());
        tagCompound.setLong(FREQUENCY_LSB_KEY, newFrequency.getLeastSignificantBits());
        tagCompound.setInteger(CARD_STATE_KEY, State.OPERATIONAL.getType());

        if (aTileEntity instanceof final BaseMetaTileEntity baseMTE) {
            tagCompound.setString(MACHINE_NAME_KEY, baseMTE.getLocalName());
        }

        aTileEntity.getCoverInfoAtSide(side)
            .setCoverData(new MetricsTransmitterData(newFrequency));

        final EntityItem cardEntity = new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, cardStack);
        cardEntity.delayBeforeCanPickup = 0;
        player.worldObj.spawnEntityInWorld(cardEntity);
    }

    @Override
    public boolean allowsCopyPasteTool() {
        return false;
    }

    @Override
    public List<String> getAdditionalTooltipImpl(MetricsTransmitterData data) {
        return ImmutableList.of(
            StatCollector.translateToLocalFormatted(
                "gt.item.adv_sensor_card.tooltip.frequency",
                EnumChatFormatting.UNDERLINE.toString() + EnumChatFormatting.YELLOW + data.frequency.toString()));
    }

    public static class MetricsTransmitterData implements ISerializableObject {

        private UUID frequency;

        public MetricsTransmitterData() {
            this.frequency = UUID.randomUUID();
        }

        public MetricsTransmitterData(UUID frequency) {
            this.frequency = frequency;
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
        public void writeToByteBuf(ByteBuf aBuf) {
            aBuf.writeLong(frequency.getMostSignificantBits());
            aBuf.writeLong(frequency.getLeastSignificantBits());
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            if (aNBT instanceof final NBTTagCompound tag) {
                frequency = new UUID(tag.getLong(FREQUENCY_MSB_KEY), tag.getLong(FREQUENCY_LSB_KEY));
            }
        }

        @NotNull
        @Override
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, @Nullable EntityPlayerMP aPlayer) {
            return new MetricsTransmitterData(new UUID(aBuf.readLong(), aBuf.readLong()));
        }
    }
}
