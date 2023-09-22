package gregtech.common.items;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.common.covers.GT_Cover_Metrics_Transmitter.CARD_STATE_KEY;
import static gregtech.common.covers.GT_Cover_Metrics_Transmitter.FREQUENCY_LSB_KEY;
import static gregtech.common.covers.GT_Cover_Metrics_Transmitter.FREQUENCY_MSB_KEY;
import static gregtech.common.covers.GT_Cover_Metrics_Transmitter.MACHINE_KEY;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
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
public class GT_AdvancedSensorCard_Item extends Item implements IPanelDataSource {

    public static final UUID CARD_TYPE_ID = UUID.fromString("ff952e84-7608-4c4a-85af-dd6e1aa27fc7");

    // This has obfuscated formatting, so no need to localize it.
    private static final ImmutableList<PanelString> SELF_DESTRUCTED_OUTPUT = ImmutableList
        .of(prebakePanelString(EnumChatFormatting.OBFUSCATED + "critical error" + EnumChatFormatting.RESET, true));

    private static final ImmutableList<PanelString> DECONSTRUCTED_OUTPUT = ImmutableList.of(
        prebakePanelString(StatCollector.translateToLocal("gt.item.adv_sensor_card.error.deconstructed.1"), true),
        prebakePanelString(StatCollector.translateToLocal("gt.item.adv_sensor_card.error.deconstructed.2"), true));

    private static final ImmutableList<PanelString> NO_DATA_FOUND = ImmutableList
        .of(prebakePanelString(StatCollector.translateToLocal("gt.item.adv_sensor_card.error.no_data"), true));

    private int payloadSize = 0;

    @SideOnly(Side.CLIENT)
    private IIcon normalIcon;
    @SideOnly(Side.CLIENT)
    private IIcon selfDestructedIcon;

    @SuppressWarnings("unused")
    public GT_AdvancedSensorCard_Item() {
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
        getDataFromDatabase(card).ifPresent(data -> {
            reconcileSelfDestructedCard(card.getItemStack(), data.getState());
            card.setInt(
                CARD_STATE_KEY,
                data.getState()
                    .getType());
            payloadSize = switch (data.getState()) {
                case SELF_DESTRUCTED -> SELF_DESTRUCTED_OUTPUT.size();
                case HOST_DECONSTRUCTED -> DECONSTRUCTED_OUTPUT.size() + getMachineName(card.getItemStack()).map(x -> 1)
                    .orElse(0);
                case OPERATIONAL -> data.getPayload()
                    .map(List::size)
                    .orElse(0)
                    + getMachineName(card.getItemStack()).map(x -> 1)
                        .orElse(0)
                    + data.getCoordinates()
                        .map(x -> 2)
                        .orElse(0);
            };
        });
        return CardState.OK;
    }

    @Override
    public List<PanelString> getStringData(final int displaySettings, final ICardWrapper card,
        final boolean showLabels) {
        // This method needs to return a mutable list, since the calling routine in NuclearCraft appends an item to the
        // head of the list. Hence, all the array copying.

        return getCardState(card).map(state -> switch (state) {
            case SELF_DESTRUCTED -> new ArrayList<>(SELF_DESTRUCTED_OUTPUT);
            case HOST_DECONSTRUCTED -> {
                final ArrayList<PanelString> list = new ArrayList<>();
                getMachineName(card.getItemStack()).ifPresent(name -> list.add(prebakePanelString(name, true)));
                list.addAll(DECONSTRUCTED_OUTPUT);
                yield list;
            }
            case OPERATIONAL -> getDataFromDatabase(card).map(data -> {
                final ImmutableList.Builder<String> builder = ImmutableList.builder();

                getMachineName(card.getItemStack()).ifPresent(builder::add);
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

                return builder.build();
            })
                .filter(payload -> !payload.isEmpty())
                .map(
                    payload -> IntStream.range(0, payload.size())
                        .filter(i -> (displaySettings & (1 << i)) != 0)
                        .mapToObj(i -> prebakePanelString(payload.get(i), i == 0))
                        .collect(Collectors.toCollection(ArrayList::new)))
                .orElse(null);
        })
            .orElse(new ArrayList<>(NO_DATA_FOUND));
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
    private static PanelString prebakePanelString(String info) {
        return prebakePanelString(info, false);
    }

    @NotNull
    private static PanelString prebakePanelString(String info, boolean center) {
        final PanelString panelString = new PanelString();
        if (center) {
            panelString.textCenter = info;
        } else {
            panelString.textLeft = info;
        }
        return panelString;

    }
}
