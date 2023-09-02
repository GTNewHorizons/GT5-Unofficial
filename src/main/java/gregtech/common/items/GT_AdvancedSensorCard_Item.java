package gregtech.common.items;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.common.covers.GT_Cover_Metrics_Transmitter.CARD_STATE_KEY;
import static gregtech.common.covers.GT_Cover_Metrics_Transmitter.FREQUENCY_LSB_KEY;
import static gregtech.common.covers.GT_Cover_Metrics_Transmitter.FREQUENCY_MSB_KEY;

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
import net.minecraft.world.World;

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
    private static final ImmutableList<PanelString> SELF_DESTRUCTED_OUTPUT = ImmutableList
        .of(prebakePanelString(EnumChatFormatting.OBFUSCATED + "critical error" + EnumChatFormatting.RESET, true));
    private static final ImmutableList<PanelString> DECONSTRUCTED_OUTPUT = ImmutableList.of(
        prebakePanelString("-ERROR- Machine Deconstructed", true),
        prebakePanelString("Place machine again to re-enable", true));
    private static final ImmutableList<PanelString> NO_DATA_FOUND = ImmutableList
        .of(prebakePanelString("No data found", true));

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
        tooltip.add("Created by attaching a Metrics Transmitter cover, no standard recipe");
        getUUID(itemStack).ifPresent(uuid -> tooltip.add("Frequency: " + EnumChatFormatting.YELLOW + uuid));
        getCardState(itemStack).ifPresent(state -> {
            if (state == State.SELF_DESTRUCTED) {
                tooltip.add(
                    EnumChatFormatting.ITALIC.toString() + EnumChatFormatting.LIGHT_PURPLE
                        + "This thing looks completely fried...");
                tooltip.add(EnumChatFormatting.RED + "Destroyed due to metrics transmitter being removed from machine");
            }
        });
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
                case DECONSTRUCTED -> DECONSTRUCTED_OUTPUT.size();
                case OPERATIONAL -> data.getPayload()
                    .map(List::size)
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
            case DECONSTRUCTED -> new ArrayList<>(DECONSTRUCTED_OUTPUT);
            case OPERATIONAL -> getDataFromDatabase(card).flatMap(GlobalMetricsCoverDatabase.Data::getPayload)
                .filter(payload -> !payload.isEmpty())
                .map(
                    payload -> IntStream.range(0, payload.size())
                        .filter(i -> (displaySettings & (1 << i)) != 0)
                        .mapToObj(i -> prebakePanelString(payload.get(i)))
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
        if ((worldIn.getWorldTime() % 20) == 0) {
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

    private Optional<State> getCardState(ICardWrapper card) {
        return getCardState(card.getItemStack());
    }

    private Optional<State> getCardState(ItemStack itemStack) {
        if (itemStack.hasTagCompound() && itemStack.getTagCompound()
            .hasKey(CARD_STATE_KEY)) {
            return State.find(
                itemStack.getTagCompound()
                    .getInteger(CARD_STATE_KEY));
        }

        return Optional.empty();
    }

    private Optional<UUID> getUUID(ItemStack stack) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt.hasKey(FREQUENCY_LSB_KEY) && nbt.hasKey(FREQUENCY_MSB_KEY)) {
                return Optional.of(new UUID(nbt.getLong(FREQUENCY_MSB_KEY), nbt.getLong(FREQUENCY_LSB_KEY)));
            }
        }

        return Optional.empty();
    }

    private Optional<GlobalMetricsCoverDatabase.Data> getDataFromDatabase(ICardWrapper card) {
        return getDataFromDatabase(card.getItemStack());
    }

    private Optional<GlobalMetricsCoverDatabase.Data> getDataFromDatabase(ItemStack stack) {
        return getUUID(stack).flatMap(GlobalMetricsCoverDatabase::getData);
    }

    private static PanelString prebakePanelString(String info) {
        return prebakePanelString(info, false);
    }

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
