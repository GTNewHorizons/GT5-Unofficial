package gregtech.common.items;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.common.covers.CoverMetricsTransmitter.CARD_STATE_KEY;
import static gregtech.common.covers.CoverMetricsTransmitter.FREQUENCY_LSB_KEY;
import static gregtech.common.covers.CoverMetricsTransmitter.FREQUENCY_MSB_KEY;
import static gregtech.common.covers.CoverMetricsTransmitter.MACHINE_KEY;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.common.misc.GlobalMetricsCoverDatabase;
import gregtech.common.misc.GlobalMetricsCoverDatabase.State;
import shedar.mods.ic2.nuclearcontrol.api.CardState;
import shedar.mods.ic2.nuclearcontrol.api.ICardWrapper;
import shedar.mods.ic2.nuclearcontrol.api.IPanelDataSource;
import shedar.mods.ic2.nuclearcontrol.api.PanelSetting;
import shedar.mods.ic2.nuclearcontrol.api.PanelString;

@SuppressWarnings("unused")
public class ItemAdvancedSensorCard extends Item implements IPanelDataSource {

    public static final UUID CARD_TYPE_ID = UUID.fromString("ff952e84-7608-4c4a-85af-dd6e1aa27fc7");

    // This has obfuscated formatting, so no need to localize it.
    private static final String SELF_DESTRUCTED_OUTPUT = EnumChatFormatting.OBFUSCATED + "critical error"
        + EnumChatFormatting.RESET;

    private static final ImmutableList<String> DECONSTRUCTED_OUTPUT = ImmutableList.of(
        StatCollector.translateToLocal("gt.item.adv_sensor_card.error.deconstructed.1"),
        StatCollector.translateToLocal("gt.item.adv_sensor_card.error.deconstructed.2"));

    private static final String NO_DATA_FOUND = StatCollector.translateToLocal("gt.item.adv_sensor_card.error.no_data");

    private static final String MACHINE_NAME_KEY = "client_machine_name";
    private static final String OUTPUT_ENTRY_KEY = "client_entry_%d";
    private static final String OUTPUT_ENTRY_LENGTH_KEY = "client_entry_length";

    private int payloadSize = 0;

    @SideOnly(Side.CLIENT)
    private IIcon normalIcon;
    @SideOnly(Side.CLIENT)
    private IIcon selfDestructedIcon;

    @SuppressWarnings("unused")
    public ItemAdvancedSensorCard() {
        super();

        GameRegistry.registerItem(this, "gt.advancedsensorcard", GregTech.ID);
        setUnlocalizedName("gt.advancedsensorcard");
        setMaxStackSize(1);
        setNoRepair();
    }

    @Override
    public void addInformation(final ItemStack itemStack, final EntityPlayer player, final List<String> tooltip,
        final boolean p_77624_4_) {
        super.addInformation(itemStack, player, tooltip, p_77624_4_);

        final Optional<State> cardState = getCardState(itemStack);
        if (cardState.isPresent()) {
            final State state = cardState.get();

            if (state == State.SELF_DESTRUCTED) {
                tooltip.add(StatCollector.translateToLocal("gt.item.adv_sensor_card.tooltip.fried.1"));
                tooltip.add(StatCollector.translateToLocal("gt.item.adv_sensor_card.tooltip.fried.2"));
                tooltip.add(StatCollector.translateToLocal("gt.item.adv_sensor_card.tooltip.fried.3"));
            } else {
                getMachineName(itemStack).ifPresent(
                    machineName -> tooltip.add(
                        StatCollector
                            .translateToLocalFormatted("gt.item.adv_sensor_card.tooltip.machine", machineName)));
                getUUID(itemStack).ifPresent(
                    uuid -> tooltip.add(
                        StatCollector
                            .translateToLocalFormatted("gt.item.adv_sensor_card.tooltip.frequency", uuid.toString())));
            }
        } else {
            tooltip.add(StatCollector.translateToLocal("gt.item.adv_sensor_card.tooltip.recipe_hint"));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item aItem, CreativeTabs aCreativeTab, List<ItemStack> aOutputSubItems) {}

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister aIconRegister) {
        super.registerIcons(aIconRegister);
        itemIcon = aIconRegister.registerIcon(GregTech.ID + ":gt.advancedsensorcard");
        normalIcon = itemIcon;
        selfDestructedIcon = aIconRegister.registerIcon(GregTech.ID + ":gt.advancedsensorcardburned");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final ItemStack stack, final int renderPass) {
        return getIconIndex(stack);
    }

    @Override
    public IIcon getIconIndex(final ItemStack itemStack) {
        return getCardState(itemStack).filter(Predicate.isEqual(State.SELF_DESTRUCTED))
            .map(ignored -> selfDestructedIcon)
            .orElse(normalIcon);
    }

    @Override
    public CardState update(TileEntity tileEntity, ICardWrapper card, int maxRange) {
        return update(tileEntity.getWorldObj(), card, maxRange);
    }

    @Override
    public CardState update(World world, ICardWrapper card, int maxRange) {
        final Optional<GlobalMetricsCoverDatabase.Data> optionalData = getDataFromDatabase(card);

        optionalData.ifPresent(data -> {
            final State machineState = data.getState();
            reconcileSelfDestructedCard(card.getItemStack(), machineState);
            card.setInt(CARD_STATE_KEY, machineState.getType());

            getMachineName(card.getItemStack())
                .ifPresent(name -> card.setString(MACHINE_NAME_KEY, machineState == State.SELF_DESTRUCTED ? "" : name));

            final ImmutableList.Builder<String> builder = ImmutableList.builder();
            switch (machineState) {
                case SELF_DESTRUCTED -> builder.add(SELF_DESTRUCTED_OUTPUT);
                case HOST_DECONSTRUCTED -> builder.addAll(DECONSTRUCTED_OUTPUT);
                case OPERATIONAL -> {
                    data.getCoordinates()
                        .ifPresent(
                            coordinates -> builder.add(
                                StatCollector.translateToLocalFormatted(
                                    "gt.item.adv_sensor_card.dimension",
                                    coordinates.getDimension()),
                                StatCollector.translateToLocalFormatted(
                                    "gt.item.adv_sensor_card.coords",
                                    coordinates.getLocalizedCoordinates())));

                    data.getPayload()
                        .ifPresent(builder::addAll);
                }
                default -> builder.add(NO_DATA_FOUND);
            }

            final List<String> payload = builder.build();
            card.setInt(OUTPUT_ENTRY_LENGTH_KEY, payload.size());
            for (int i = 0; i < payload.size(); i++) {
                final String payloadItem = payload.get(i);
                if (!payloadItem.isEmpty()) {
                    card.setString(String.format(OUTPUT_ENTRY_KEY, i), payloadItem);
                }
            }
        });

        return CardState.OK;
    }

    @Override
    public List<PanelString> getStringData(final int displaySettings, final ICardWrapper card,
        final boolean showLabels) {
        final List<PanelString> returned = new ArrayList<>();
        final String machineName = card.getString(MACHINE_NAME_KEY);
        final int bitmaskOffset;

        payloadSize = card.getInt(OUTPUT_ENTRY_LENGTH_KEY);

        if (!machineName.isEmpty() && (displaySettings & 1) != 0) {
            returned.add(panelString(machineName, true));
            payloadSize += 1;
            bitmaskOffset = 1;
        } else {
            bitmaskOffset = 0;
        }

        // Not reusing payloadSize here because it can be conditionally mutated.
        IntStream.range(0, card.getInt(OUTPUT_ENTRY_LENGTH_KEY))
            .forEach(i -> {
                if ((displaySettings & 1 << (i + bitmaskOffset)) != 0) {
                    returned.add(panelString(card.getString(String.format(OUTPUT_ENTRY_KEY, i))));
                }
            });

        return returned;
    }

    @Override
    public List<PanelSetting> getSettingsList() {
        return payloadSize == 0 ? ImmutableList.of()
            : ImmutableList.copyOf(
                IntStream.range(0, Math.min(payloadSize, 31))
                    .mapToObj(i -> new PanelSetting(String.valueOf(i + 1), 1 << i, getCardType()))
                    .iterator());
    }

    @Override
    public UUID getCardType() {
        return CARD_TYPE_ID;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int slot, boolean isHeld) {
        super.onUpdate(stack, worldIn, entityIn, slot, isHeld);
        // At the time of this comment's writing, there are 52 matches of the regex:
        // /% \d+0 \)?\s*== 0/ in the code base, indicating an over-reliance on events happening on either the 10th or
        // 20th tick. Let's tick on something slightly off of that. A prime number will do nicely.
        if ((worldIn.getWorldTime() % 20) == 13) {
            getDataFromDatabase(stack).ifPresent(data -> {
                reconcileSelfDestructedCard(stack, data.getState());
                if (!stack.hasTagCompound()) {
                    stack.setTagCompound(new NBTTagCompound());
                }

                stack.getTagCompound()
                    .setInteger(
                        CARD_STATE_KEY,
                        data.getState()
                            .getType());
            });
        }
    }

    private void reconcileSelfDestructedCard(ItemStack stack, State newState) {
        getUUID(stack).ifPresent(uuid -> getCardState(stack).ifPresent(oldState -> {
            if (newState == State.SELF_DESTRUCTED && oldState != State.SELF_DESTRUCTED) {
                GlobalMetricsCoverDatabase.clearSelfDestructedFrequency(uuid);
            }
        }));

    }

    @NotNull
    private Optional<State> getCardState(ICardWrapper card) {
        return getCardState(card.getItemStack());
    }

    @NotNull
    private Optional<State> getCardState(ItemStack itemStack) {
        if (itemStack.hasTagCompound() && itemStack.getTagCompound()
            .hasKey(CARD_STATE_KEY)) {
            return State.find(
                itemStack.getTagCompound()
                    .getInteger(CARD_STATE_KEY));
        }

        return Optional.empty();
    }

    @NotNull
    private Optional<UUID> getUUID(ItemStack stack) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt.hasKey(FREQUENCY_LSB_KEY) && nbt.hasKey(FREQUENCY_MSB_KEY)) {
                return Optional.of(new UUID(nbt.getLong(FREQUENCY_MSB_KEY), nbt.getLong(FREQUENCY_LSB_KEY)));
            }
        }

        return Optional.empty();
    }

    @NotNull
    private Optional<String> getMachineName(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound()
            .hasKey(MACHINE_KEY)) {
            try {
                final ItemStack machine = ItemStack.loadItemStackFromNBT(
                    stack.getTagCompound()
                        .getCompoundTag(MACHINE_KEY));
                if (machine != null) {
                    return Optional.of(machine.getDisplayName());
                }
            } catch (Exception ignored) {}
        }

        return Optional.empty();
    }

    @NotNull
    private Optional<GlobalMetricsCoverDatabase.Data> getDataFromDatabase(ICardWrapper card) {
        return getDataFromDatabase(card.getItemStack());
    }

    @NotNull
    private Optional<GlobalMetricsCoverDatabase.Data> getDataFromDatabase(ItemStack stack) {
        return getUUID(stack).flatMap(GlobalMetricsCoverDatabase::getData);
    }

    @NotNull
    private static PanelString panelString(String info) {
        return panelString(info, false);
    }

    @NotNull
    private static PanelString panelString(String info, boolean center) {
        final PanelString panelString = new PanelString();
        if (center) {
            panelString.textCenter = info;
        } else {
            panelString.textLeft = info;
        }
        return panelString;

    }
}
