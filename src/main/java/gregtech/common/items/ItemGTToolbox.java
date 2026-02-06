package gregtech.common.items;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.GTValues.V;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.interfaces.IToolStats;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.factory.PlayerInventoryGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.gtnewhorizon.gtnhlib.GTNHLib;
import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import appeng.api.implementations.items.IAEWrench;
import buildcraft.api.tools.IToolWrench;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.InterfaceList;
import crazypants.enderio.api.tool.ITool;
import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Mods;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.ToolboxSlot;
import gregtech.api.interfaces.IDamagableItem;
import gregtech.api.interfaces.item.IMiddleClickItem;
import gregtech.api.items.GTGenericItem;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.modularui2.ToolboxSelectGuiFactory;
import gregtech.api.net.GTPacketToolboxEvent;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.item.ToolboxInventoryGui;
import gregtech.common.items.toolbox.ToolboxElectricManager;
import gregtech.common.items.toolbox.ToolboxItemStackHandler;
import gregtech.common.items.toolbox.ToolboxUtil;
import gregtech.crossmod.backhand.Backhand;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import mods.railcraft.api.core.items.IToolCrowbar;
import mrtjp.projectred.api.IScrewdriver;
import net.minecraftforge.event.world.BlockEvent;

@InterfaceList(
    value = { @Interface(iface = "forestry.api.arboriculture.IToolGrafter", modid = Mods.ModIDs.FORESTRY),
        @Interface(iface = "mods.railcraft.api.core.items.IToolCrowbar", modid = Mods.ModIDs.RAILCRAFT),
        @Interface(iface = "buildcraft.api.tools.IToolWrench", modid = Mods.ModIDs.BUILD_CRAFT_CORE),
        @Interface(iface = "crazypants.enderio.api.tool.ITool", modid = Mods.ModIDs.ENDER_I_O),
        @Interface(iface = "mrtjp.projectred.api.IScrewdriver", modid = Mods.ModIDs.PROJECT_RED_CORE), })
public class ItemGTToolbox extends GTGenericItem implements IGuiHolder<PlayerInventoryGuiData>, ISpecialElectricItem,
    IMiddleClickItem, IDamagableItem, IToolCrowbar, IToolWrench, ITool, IScrewdriver, IAEWrench {

    public static final String CONTENTS_NBT_KEY = "gt5u.toolbox:Contents";
    public static final String TOOLBOX_OPEN_NBT_KEY = "gt5u.toolbox:ToolboxOpen";
    public static final String CURRENT_TOOL_NBT_KEY = "gt5u.toolbox:SelectedSlot";
    public static final int NO_TOOL_SELECTED = -1;

    private static final int CHARGE_TICK = 20;

    public ItemGTToolbox(final String aUnlocalized, final String aEnglish, final String aEnglishTooltip) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
        setMaxStackSize(1);
    }

    @Override
    public void onUpdate(final ItemStack stack, final World world, final Entity entity, final int timer, final boolean isInHand) {
        if (world.isRemote) {
            return;
        }

        if (isInHand && entity instanceof final EntityPlayer player) {
            final ToolboxItemStackHandler handler = new ToolboxItemStackHandler(stack);
            boolean shouldUpdate = false;

            if (player.ticksExisted % CHARGE_TICK == 0 && stack.hasTagCompound() && !stack.getTagCompound().getBoolean(TOOLBOX_OPEN_NBT_KEY)) {
                // If the toolbox is open, don't charge items to prevent syncing issues.

                final ItemStack battery = handler.extractItem(ToolboxSlot.BATTERY.getSlotID(), 1, true);
                if (battery != null && battery.getItem() instanceof final IElectricItem batteryItem) {
                    shouldUpdate = getElectricManager(battery).map(batteryManager -> {
                        boolean dirty = false;
                        double remainingCharge = batteryManager.discharge(
                            battery,
                            getMaxVoltage(batteryItem.getTier(battery)) * CHARGE_TICK,
                            Integer.MAX_VALUE,
                            true,
                            true,
                            true
                        );

                        for (final ToolboxSlot slot : ToolboxSlot.values()) {
                            if (slot == ToolboxSlot.BATTERY) {
                                continue;
                            }

                            if (remainingCharge <= 0) {
                                break;
                            }
                            final double availableCharge = remainingCharge;

                            final ItemStack slotStack = handler.extractItem(slot.getSlotID(), 1, true);
                            if (slotStack == null || !(slotStack.getItem() instanceof final IElectricItem slotItem)) {
                                continue;
                            }

                            final double powerUsed = getElectricManager(slotStack).map(slotManager -> slotManager.charge(
                                slotStack,
                                (int) Math.min(availableCharge, getMaxVoltage(slotItem.getTier(slotStack)) * CHARGE_TICK),
                                Integer.MAX_VALUE,
                                true,
                                false)).orElse(0d);

                            if (powerUsed > 0) {
                                batteryManager.discharge(battery, powerUsed, Integer.MAX_VALUE, true, true, false);
                                remainingCharge -= powerUsed;
                                dirty = true;
                                handler.setStackInSlot(slot.getSlotID(), slotStack);
                            }
                        }

                        if (dirty) {
                            handler.setStackInSlot(ToolboxSlot.BATTERY.getSlotID(), battery);
                        }

                        return dirty;
                    }).orElse(false);
                }
            }

            if (shouldUpdate) {
                ToolboxUtil.saveToolbox(stack, handler);
            }
        }
    }

    @Override
    public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer player) {
        if (!world.isRemote && !ToolboxUtil.getSelectedToolType(itemStack).isPresent()) {
            if (itemStack == Backhand.getOffhandItem(player)) {
                GuiFactories.playerInventory()
                    .openFromPlayerInventory(player, Backhand.getOffhandSlot(player));
            } else {
                GuiFactories.playerInventory()
                    .openFromMainHand(player);
            }
        }

        return super.onItemRightClick(itemStack, world, player);
    }

    @Override
    public String getItemStackDisplayName(final ItemStack toolbox) {
        final String base = super.getItemStackDisplayName(toolbox);

        return ToolboxUtil.getSelectedToolType(toolbox)
            .map(slot -> {
                final ToolboxItemStackHandler handler = new ToolboxItemStackHandler(toolbox);
                final String toolName = StatCollector.translateToLocal("GT5U.gui.text.toolbox.slot_title." + slot.getSlotID());
                final Optional<ItemStack> potentialTool = handler.getCurrentTool();
                final byte toolMode = potentialTool.map(MetaGeneratedTool::getToolMode).orElse((byte) 0);

                //noinspection SimplifyOptionalCallChains
                return toolMode > 0
                    ? StatCollector.translateToLocalFormatted(
                    "GT5U.item.toolbox.name_template.mode",
                    base,
                    toolName,
                    potentialTool.map(currentTool -> currentTool.getItem() instanceof final MetaGeneratedTool mgToolItem
                        ? mgToolItem.getToolModeName(currentTool)
                        : "").orElse(""))
                    : StatCollector
                    .translateToLocalFormatted("GT5U.item.toolbox.name_template", base, toolName);
            })
            .orElse(base);

    }

    @Override
    public void addInformation(final ItemStack toolbox, final EntityPlayer player, final List<String> tooltipList,
        final boolean f3mode) {
        final boolean hasCurrentTool = ToolboxUtil.getSelectedToolType(toolbox).isPresent();

        // TODO: Add information and author byline

        final GameSettings settings = Minecraft.getMinecraft().gameSettings;

        if (!hasCurrentTool) {
            tooltipList.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.item.toolbox.tooltip.open_toolbox",
                    GameSettings.getKeyDisplayString(settings.keyBindUseItem.getKeyCode())));
        }

        tooltipList.add(
            StatCollector.translateToLocalFormatted(
                "GT5U.item.toolbox.tooltip.select_tool",
                GameSettings.getKeyDisplayString(settings.keyBindPickBlock.getKeyCode())));

        if (hasCurrentTool) {
            tooltipList.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.item.toolbox.tooltip.deselect_tool",
                    GameSettings.getKeyDisplayString(settings.keyBindPickBlock.getKeyCode()))
                );
            tooltipList.add(
                StatCollector.translateToLocalFormatted(
                    "gt.behaviour.switch_mode.tooltip",
                    GameSettings.getKeyDisplayString(GTMod.proxy.TOOL_MODE_SWITCH_KEYBIND.getKeyCode())));
        }

        ToolboxUtil.withBatteryAndManager(toolbox, (battery, manager) -> {
            final IElectricItem batteryItem = Objects.requireNonNull((IElectricItem) battery.getItem());
            final int voltageTier = GTUtility.clamp(batteryItem.getTier(battery), 0, V.length - 1);

            tooltipList.add(
                EnumChatFormatting.AQUA + StatCollector.translateToLocalFormatted(
                    "gt.item.desc.eu_info",
                    formatNumber(manager.getCharge(battery)),
                    formatNumber(batteryItem.getMaxCharge(battery)),
                    formatNumber(V[voltageTier])));
        });
    }

    @Override
    public ModularPanel buildUI(final PlayerInventoryGuiData data, final PanelSyncManager syncManager,
        final UISettings settings) {
        final int slot = data.getSlotIndex();
        final ToolboxItemStackHandler stackHandler = new ToolboxItemStackHandler(data.getPlayer(), slot);

        if (data.getUsedItemStack() != null) {
            syncManager.addOpenListener(player -> {
                // Despite the Javadoc's insistence, this function only runs on the client.
                // Keeping this check in here just in case it gets fixed upstream, so it doesn't break later.
                if (player.worldObj.isRemote) {
                    GTValues.NW.sendToServer(new GTPacketToolboxEvent(GTPacketToolboxEvent.Action.UI_OPEN, slot));
                }
            })
                .addCloseListener(player -> {
                    if (!player.worldObj.isRemote) {
                        // Retrieve stack from player again. Persist the toolbox contents and allow charging again.
                        final ItemStack toolbox = player.inventory.getStackInSlot(slot);

                        ToolboxUtil.saveToolbox(toolbox, stackHandler, tag -> {
                            tag.setBoolean(TOOLBOX_OPEN_NBT_KEY, false);

                            // Unselect the active tool if it was removed from the toolbox.
                            if (tag.hasKey(CURRENT_TOOL_NBT_KEY)) {
                                final int selectedToolSlot = tag.getInteger(CURRENT_TOOL_NBT_KEY);
                                if (selectedToolSlot >= 0 && selectedToolSlot < stackHandler.getSlots()
                                    && stackHandler.getStackInSlot(selectedToolSlot) == null) {
                                    tag.removeTag(CURRENT_TOOL_NBT_KEY);
                                }
                            }
                        });

                        player.inventory.setInventorySlotContents(data.getSlotIndex(), toolbox);

                        GTUtility.sendSoundToPlayers(
                            player.worldObj,
                            SoundResource.GT_TOOLBOX_CLOSE,
                            1.0F,
                            1,
                            player.posX,
                            player.posY,
                            player.posZ);
                    }
                });
        }
        return new ToolboxInventoryGui(syncManager, data, stackHandler).build();
    }

    @Override
    public boolean doDamageToItem(final ItemStack toolbox, final int vanillaDamage) {
        if (toolbox == null || !(toolbox.getItem() instanceof ItemGTToolbox)) {
            return false;
        }

        return ToolboxUtil.getSelectedTool(toolbox).map(toolStack -> {
            if (toolStack.getItem() instanceof final MetaGeneratedTool tool) {
                final ToolboxItemStackHandler handler = new ToolboxItemStackHandler(toolbox);

                if (tool.doDamageToItem(toolStack, vanillaDamage)) {
                    handler.setCurrentTool(toolStack.stackSize == 0 ? null : toolStack);
                    ToolboxUtil.saveToolbox(toolbox, handler);

                    return true;
                }
            }
            return false;
        }).orElse(false);
    }

    //region Event Handlers
    @Override
    public boolean onMiddleClick(final ItemStack toolbox, final EntityPlayer player) {
        // TODO: Switch tool if you pick block a machine?

        if (player.isSneaking() && ToolboxUtil.getSelectedToolType(toolbox).isPresent()) {
            GTValues.NW.sendToServer(
                new GTPacketToolboxEvent(
                    GTPacketToolboxEvent.Action.CHANGE_ACTIVE_TOOL,
                    player.inventory.getCurrentItem() == toolbox ? player.inventory.currentItem : Backhand.getOffhandSlot(player),
                    NO_TOOL_SELECTED));
            return true;
        }

        final ToolboxItemStackHandler handler = new ToolboxItemStackHandler(toolbox);

        int toolCount = 0;
        int lastSlot = -1;
        for (ToolboxSlot slot : ToolboxSlot.TOOL_SLOTS) {
            if (handler.getStackInSlot(slot.getSlotID()) != null) {
                toolCount++;
                lastSlot = slot.getSlotID();
            }
        }

        switch (toolCount) {
            case 0:
                GTNHLib.proxy.printMessageAboveHotbar(
                    StatCollector.translateToLocal("GT5U.gui.text.toolbox.error.no_tools"),
                    120,
                    true,
                    true);
            case 1:
                GTValues.NW.sendToServer(
                    new GTPacketToolboxEvent(
                        GTPacketToolboxEvent.Action.CHANGE_ACTIVE_TOOL,
                        player.inventory.getCurrentItem() == toolbox ? player.inventory.currentItem : Backhand.getOffhandSlot(player),
                        ToolboxUtil.getSelectedToolType(toolbox).isPresent() ? lastSlot : NO_TOOL_SELECTED));
                return true;
            default:
                if (player instanceof final EntityClientPlayerMP playerMP) {
                    ToolboxSelectGuiFactory.INSTANCE.open(playerMP);
                    return true;
                }
        }

        return false;
    }

    /**
     * Handler for tool mode switch keybind. The toolbox delegates this action to the currently selected tool, switching
     * its mode while still inside.
     *
     * @param player  The player doing the switching
     * @param keybind The keybind responsible for triggering this action
     * @param keyDown true if the key is depressed
     */
    public static void switchToolMode(EntityPlayerMP player, @SuppressWarnings("unused") SyncedKeybind keybind,
        boolean keyDown) {
        if (!keyDown) {
            return;
        }

        getToolboxIfEquipped(player).ifPresent(toolboxStack -> {
            if (toolboxStack.hasTagCompound()) {
                ToolboxItemStackHandler handler = new ToolboxItemStackHandler(toolboxStack);
                handler.mutateCurrentTool(MetaGeneratedTool::switchToolMode);
                ToolboxUtil.saveToolbox(toolboxStack, handler);
            }
        });
    }

    @SubscribeEvent
    public void onBlockBreakingEvent(BlockEvent.BreakEvent event) {
        getToolboxIfEquipped(event.getPlayer()).flatMap(ToolboxUtil::getSelectedTool).ifPresent(tool -> {
            if (tool.getItem() instanceof final MetaGeneratedTool toolItem) {
                IToolStats stats = toolItem.getToolStats(tool);
                if (stats != null) {
                    TileEntity tile = event.world.getTileEntity(event.x, event.y, event.z);
                    stats.onBreakBlock(event.getPlayer(), event.x, event.y, event.z, event.block, event.blockMetadata, tile, event);
                }
            }
        });
    }

    @SubscribeEvent
    public void onBlockHarvestingEvent(BlockEvent.HarvestDropsEvent aEvent) {
        getToolboxIfEquipped(aEvent.harvester).flatMap(ToolboxUtil::getSelectedTool).ifPresent(tool -> {
            if ((tool.getItem() instanceof MetaGeneratedTool toolItem)) {
                toolItem.onHarvestBlockEvent(
                    aEvent.drops,
                    tool,
                    aEvent.harvester,
                    aEvent.block,
                    aEvent.x,
                    aEvent.y,
                    aEvent.z,
                    aEvent.blockMetadata,
                    aEvent.fortuneLevel,
                    aEvent.isSilkTouching,
                    aEvent);
            }
        });
    }

    //endregion

    /**
     * Gets the currently equipped toolbox if the player is holding it in their main hand or offhand.
     *
     * @param player The player to interrogate
     * @return An optional with the toolbox's item stack, or empty if the user is not wielding a toolbox
     */
    private static Optional<ItemStack> getToolboxIfEquipped(EntityPlayer player) {
        if (player != null) {
            for (ItemStack stack : new ItemStack[] { player.inventory.getCurrentItem(), Backhand.getOffhandItem(player) }) {
                if (stack != null && stack.getItem() instanceof ItemGTToolbox) {
                    return Optional.of(stack);
                }
            }
        }

        return Optional.empty();
    }

    //region Vanilla Tool Harvesting Methods
    @Override
    public boolean canHarvestBlock(final Block block, final ItemStack toolbox) {
        return ToolboxUtil.getSelectedTool(toolbox).map(
            tool -> tool.getItem() instanceof final MetaGeneratedTool toolItem && toolItem.canHarvestBlock(block, tool))
            .orElse(false);
    }

    @Override
    public float getDigSpeed(final ItemStack toolbox, final Block block, final int metadata) {
        return ToolboxUtil.getSelectedTool(toolbox).filter(tool -> tool.getItem() instanceof MetaGeneratedTool)
            .map(
                tool -> tool.getItem()
                    .getDigSpeed(tool, block, metadata))
            .orElse(0f);
    }

    @Override
    public EnumAction getItemUseAction(final ItemStack toolbox) {
        return ToolboxUtil.getSelectedTool(toolbox).filter(tool -> tool.getItem() != null)
            .map(
                tool -> tool.getItem()
                    .getItemUseAction(tool))
            .orElse(EnumAction.none);
    }

    @Override
    public boolean onBlockDestroyed(final ItemStack toolbox, final World worldIn, final Block blockIn, final int x,
        final int y, final int z, final EntityLivingBase entity) {

        return ToolboxUtil.getSelectedToolType(toolbox).map(
            toolboxSlot -> ToolboxUtil.getSelectedTool(toolbox).filter(tool -> tool.getItem() != null)
                .map(tool -> {
                    final boolean result = tool.getItem()
                        .onBlockDestroyed(tool, worldIn, blockIn, x, y, z, entity);
                    if (result) {
                        ToolboxUtil.saveItemInside(toolbox, tool, toolboxSlot);
                    }

                    return result;
                })
                .orElse(false))
            .orElse(false);

    }

    @Override
    public boolean onBlockStartBreak(final ItemStack toolbox, final int x, final int y, final int z,
        final EntityPlayer player) {

        return ToolboxUtil.getSelectedTool(toolbox).filter(tool -> tool.getItem() != null)
            .map(
                tool -> tool.getItem()
                    .onBlockStartBreak(tool, x, y, z, player))
            .orElse(false);
    }

    @Override
    public int getHarvestLevel(final ItemStack toolbox, final String toolClass) {
        return ToolboxUtil.getSelectedTool(toolbox).filter(tool -> tool.getItem() instanceof MetaGeneratedTool)
            .map(tool -> tool.getItem().getHarvestLevel(tool, toolClass)).orElse(-1);
    }
    //endregion

    // region Electric Item Functions

    public static Optional<IElectricItemManager> getElectricManager(final ItemStack itemStack) {
        if (itemStack == null) {
            return Optional.empty();
        }

        final Item item = itemStack.getItem();

        IElectricItemManager manager = null;
        if (item instanceof final ISpecialElectricItem special) {
            manager = special.getManager(itemStack);
        } else if (item instanceof IElectricItem) {
            manager = ic2.api.item.ElectricItem.manager;
        }

        return Optional.ofNullable(manager);
    }

    private static long getMaxVoltage(int tier) {
        if (tier >= GTValues.V.length) {
            tier = GTValues.V.length - 1;
        }

        return GTValues.V[tier];
    }

    @Override
    public IElectricItemManager getManager(final ItemStack toolbox) {
        return new ToolboxElectricManager();
    }

    @Override
    public boolean canProvideEnergy(final ItemStack itemStack) {
        return false;
    }

    @Override
    public Item getChargedItem(final ItemStack itemStack) {
        return this;
    }

    @Override
    public Item getEmptyItem(final ItemStack itemStack) {
        return this;
    }

    @Override
    public double getMaxCharge(final ItemStack toolbox) {
        return ToolboxUtil.getBattery(toolbox).map(battery -> battery.getItem() instanceof final IElectricItem item ? item.getMaxCharge(battery) : 0d).orElse(0d);
    }

    @Override
    public double getTransferLimit(final ItemStack toolbox) {
        return ToolboxUtil.getBattery(toolbox).map(battery -> battery.getItem() instanceof final IElectricItem item ? item.getTransferLimit(battery) : 0d).orElse(0d);
    }

    @Override
    public int getTier(final ItemStack toolbox) {
        return ToolboxUtil.getBattery(toolbox)
            .map(battery -> {
                if (GTModHandler.isElectricItem(battery)) {
                    final IElectricItem item = (IElectricItem) battery.getItem();
                    return item == null ? null : item.getTier(battery);
                }

                return null;
            })
            .orElse(0);

    }

    // endregion

    // region Cross-mod Compatibility
    @Override
    public boolean canWrench(final ItemStack toolbox, final EntityPlayer player, final int x, final int y,
        final int z) {
        return ToolboxUtil.isToolOfType(toolbox, ToolboxSlot.WRENCH);
    }

    @Override
    public boolean canWrench(final EntityPlayer entityPlayer, final int x, final int y, final int z) {
        return getToolboxIfEquipped(entityPlayer).map(toolbox -> ToolboxUtil.isToolOfType(toolbox, ToolboxSlot.WRENCH))
            .orElse(false);
    }

    @Override
    public void wrenchUsed(final EntityPlayer entityPlayer, final int x, final int y, final int z) {}

    @Override
    public boolean canUse(final ItemStack toolbox, final EntityPlayer player, final int x, final int y, final int z) {
        return ToolboxUtil.isToolOfType(toolbox, ToolboxSlot.WRENCH);
    }

    @Override
    public void used(final ItemStack stack, final EntityPlayer player, final int x, final int y, final int z) {}

    @Override
    public boolean shouldHideFacades(final ItemStack toolbox, final EntityPlayer player) {
        return ToolboxUtil.isToolOfType(toolbox, ToolboxSlot.WRENCH);
    }

    @Override
    public boolean canWhack(final EntityPlayer player, final ItemStack toolbox, final int x, final int y, final int z) {
        return ToolboxUtil.isToolOfType(toolbox, ToolboxSlot.CROWBAR);
    }

    @Override
    public void onWhack(final EntityPlayer player, final ItemStack toolbox, final int x, final int y, final int z) {
        ToolboxUtil.damageSelectedTool(toolbox);
    }

    @Override
    public boolean canLink(final EntityPlayer player, final ItemStack toolbox, final EntityMinecart cart) {
        return player.isSneaking() && ToolboxUtil.isToolOfType(toolbox, ToolboxSlot.CROWBAR);
    }

    @Override
    public void onLink(final EntityPlayer player, final ItemStack toolbox, final EntityMinecart cart) {
        ToolboxUtil.damageSelectedTool(toolbox);
    }

    @Override
    public boolean canBoost(final EntityPlayer player, final ItemStack toolbox, final EntityMinecart cart) {
        return !player.isSneaking() && ToolboxUtil.isToolOfType(toolbox, ToolboxSlot.CROWBAR);
    }

    @Override
    public void onBoost(final EntityPlayer player, final ItemStack toolbox, final EntityMinecart cart) {
        ToolboxUtil.damageSelectedTool(toolbox);
    }

    @Override
    public boolean canUse(final EntityPlayer player, final ItemStack stack) {
        return ToolboxUtil.isToolOfType(stack, ToolboxSlot.SCREWDRIVER);
    }

    @Override
    public void damageScrewdriver(final EntityPlayer player, final ItemStack toolbox) {
        ToolboxUtil.damageSelectedTool(toolbox);
    }
    // endregion
}
