package gregtech.common.tools;

import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.GTValues.VN;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.factory.PlayerInventoryGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.gtnewhorizon.gtnhlib.item.ItemStackNBT;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Mods;
import gregtech.api.items.ItemTool;
import gregtech.common.gui.modularui.item.VajraGui;
import gregtech.crossmod.backhand.Backhand;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import thaumcraft.common.tiles.TileOwned;
import xonin.backhand.api.core.BackhandUtils;

public class ToolVajra extends ItemTool implements IElectricItem, IGuiHolder<PlayerInventoryGuiData> {

    private static final String SPEED_MODE_KEY = "VajraSpeedMode";
    private static final String CREATIVE_BREAK_COOLDOWN_KEY = "VajraCreativeBreakCooldown";
    private static final SpeedMode[] SPEED_MODES = SpeedMode.values();

    public int maxCharge = (int) 1e8;
    public int baseCost = 3333;
    public int tier = 5;
    public double transferLimit = V[tier];

    public ToolVajra(String aUnlocalized, String aEnglish, String aTooltip, int aMaxDamage, int aEntityDamage,
        boolean aSwingIfUsed) {
        super(aUnlocalized, aEnglish, aTooltip, aMaxDamage, aEntityDamage, aSwingIfUsed);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List<ItemStack> itemList) {
        ItemStack itemStack = new ItemStack(this, 1);
        if (getChargedItem(itemStack) == this) {
            ItemStack charged = new ItemStack(this, 1);
            ElectricItem.manager.charge(charged, 2147483647, 2147483647, true, false);
            itemList.add(charged);
        }
        if (getEmptyItem(itemStack) == this) {
            itemList.add(new ItemStack(this, 1, getMaxDamage()));
        }
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int par4, int par5, int par6,
        EntityLivingBase entityLiving) {
        ElectricItem.manager.use(stack, baseCost, entityLiving);
        if (world.isRemote && isCreativeBreakCooldownEnabled(stack)) {
            Minecraft.getMinecraft().playerController.blockHitDelay = 5;
        }
        return true;
    }

    @Override
    public boolean canHarvestBlock(Block block, ItemStack stack) {
        return ElectricItem.manager.canUse(stack, baseCost);
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass) {
        return Integer.MAX_VALUE;
    }

    @Override
    public float getDigSpeed(ItemStack stack, Block block, int meta) {
        if (!ElectricItem.manager.canUse(stack, baseCost)) {
            return 0.0F;
        }
        return getSpeedMode(stack).digSpeed;
    }

    @Override
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase target, EntityLivingBase source) {
        if (source instanceof EntityPlayer player) {
            target.attackEntityFrom(DamageSource.causePlayerDamage(player), this.attackDamage);
        } else {
            target.attackEntityFrom(DamageSource.causeMobDamage(source), this.attackDamage);
        }
        return true;
    }

    @Override
    public boolean isRepairable() {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack itemstack1, ItemStack itemstack2) {
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List<String> list, boolean par4) {
        list.add(EnumChatFormatting.WHITE + StatCollector.translateToLocal("gt.vajra.tooltip.flavor"));
        list.add(
            EnumChatFormatting.WHITE + StatCollector.translateToLocalFormatted("gt.vajra.tooltip.charge", VN[tier]));
        list.add(
            EnumChatFormatting.YELLOW + StatCollector.translateToLocalFormatted(
                "gt.vajra.tooltip.speed",
                StatCollector.translateToLocal(getSpeedMode(stack).translationKey)));
        list.add(
            EnumChatFormatting.YELLOW + StatCollector
                .translateToLocalFormatted("gt.vajra.tooltip.silk_touch", getStateName(isSilkTouchEnabled(stack))));
        list.add(
            EnumChatFormatting.YELLOW + StatCollector.translateToLocalFormatted(
                "gt.vajra.tooltip.creative_break_cooldown",
                getStateName(isCreativeBreakCooldownEnabled(stack))));
        list.add(EnumChatFormatting.YELLOW + StatCollector.translateToLocal("gt.vajra.tooltip.configure"));
    }

    private static String getStateName(boolean enabled) {
        return StatCollector
            .translateToLocal(enabled ? "GT5U.gui.button.feature_enabled" : "GT5U.gui.button.feature_disabled");
    }

    public static SpeedMode getSpeedMode(ItemStack stack) {
        if (!ItemStackNBT.hasKey(stack, SPEED_MODE_KEY)) return SpeedMode.FAST;
        int mode = ItemStackNBT.getByte(stack, SPEED_MODE_KEY) & 0xFF;
        return mode < SPEED_MODES.length ? SPEED_MODES[mode] : SpeedMode.FAST;
    }

    public static void setSpeedMode(ItemStack stack, SpeedMode mode) {
        ItemStackNBT.setByte(stack, SPEED_MODE_KEY, (byte) mode.ordinal());
    }

    public static boolean isSilkTouchEnabled(ItemStack stack) {
        return ItemStackNBT.hasKey(stack, "ench");
    }

    public static void setSilkTouchEnabled(ItemStack stack, boolean enabled) {
        if (enabled) {
            if (!isSilkTouchEnabled(stack)) stack.addEnchantment(Enchantment.silkTouch, 1);
        } else {
            ItemStackNBT.removeTag(stack, "ench");
        }
    }

    public static boolean isCreativeBreakCooldownEnabled(ItemStack stack) {
        return ItemStackNBT.getBoolean(stack, CREATIVE_BREAK_COOLDOWN_KEY);
    }

    public static void setCreativeBreakCooldownEnabled(ItemStack stack, boolean enabled) {
        ItemStackNBT.setBoolean(stack, CREATIVE_BREAK_COOLDOWN_KEY, enabled);
    }

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return false;
    }

    @Override
    public double getMaxCharge(ItemStack itemStack) {
        return maxCharge;
    }

    @Override
    public int getTier(ItemStack itemStack) {
        return tier;
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return transferLimit;
    }

    @Override
    public Item getChargedItem(ItemStack itemStack) {
        return this;
    }

    @Override
    public Item getEmptyItem(ItemStack itemStack) {
        return this;
    }

    @Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {
        // GTGenericItem overrode this to true, we return false here so that sneak right click never trigger any block
        // activations, as shift right click should always break the target block, and not any sneaky right click
        // interactions.
        return false;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            if (!world.isRemote) openConfigurationGui(stack, player);
            return true;
        }

        Block target = world.getBlock(x, y, z);
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        int metaData = world.getBlockMetadata(x, y, z);

        if (target.blockHardness < 0) return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
        if (!ElectricItem.manager.canUse(stack, baseCost))
            return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
        if (blockThaumcraftHarvest(tileEntity, player))
            return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);

        if (world.isRemote) {
            Minecraft.getMinecraft().playerController.onPlayerDestroyBlock(x, y, z, side);
            player.swingItem();
        } else {
            target.onBlockHarvested(world, x, y, z, metaData, player);
            if (target.removedByPlayer(world, player, x, y, z, true)) {
                target.onBlockDestroyedByPlayer(world, x, y, z, metaData);
                target.harvestBlock(world, player, x, y, z, metaData);
            }
        }
        // FMP & Backhand interaction: don't place if removal doesn't actually succeed
        if (Mods.Backhand.isModLoaded() && world.isAirBlock(x, y, z)) {
            BackhandUtils.useOffhandItem(player, () -> {
                ItemStack offhand = player.getHeldItem();
                if (offhand != null && offhand.getItem() instanceof ItemBlock itemBlock) {
                    int damage = offhand.getItemDamage();
                    int stackSize = offhand.stackSize;
                    itemBlock.onItemUse(offhand, player, world, x, y, z, side, hitX, hitY, hitZ);
                    if (player.capabilities.isCreativeMode) {
                        offhand.setItemDamage(damage);
                        offhand.stackSize = stackSize;
                    } else {
                        if (offhand.stackSize <= 0) {
                            MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(player, offhand));
                            player.inventory.mainInventory[player.inventory.currentItem] = null;
                        }
                    }
                }
            });
        }
        ElectricItem.manager.use(stack, baseCost, player);
        return true;
    }

    private boolean blockThaumcraftHarvest(TileEntity tileEntity, EntityPlayer player) {
        if (!Mods.Thaumcraft.isModLoaded()) return false;
        if (!(tileEntity instanceof TileOwned owned)) return false;
        return !owned.owner.equals(player.getDisplayName());
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World worldIn, EntityPlayer player) {
        if (!worldIn.isRemote && player.isSneaking()) openConfigurationGui(stack, player);
        return super.onItemRightClick(stack, worldIn, player);
    }

    private static void openConfigurationGui(ItemStack stack, EntityPlayer player) {
        if (stack == Backhand.getOffhandItem(player)) {
            GuiFactories.playerInventory()
                .openFromPlayerInventory(player, Backhand.getOffhandSlot(player));
        } else {
            GuiFactories.playerInventory()
                .openFromMainHand(player);
        }
    }

    @Override
    public ModularPanel buildUI(PlayerInventoryGuiData data, PanelSyncManager syncManager, UISettings settings) {
        return new VajraGui(data, syncManager).build();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModularScreen createScreen(PlayerInventoryGuiData data, ModularPanel mainPanel) {
        return new ModularScreen(Mods.GregTech.ID, mainPanel);
    }

    public enum SpeedMode {

        SLOW(8.0F, "gt.vajra.speed.slow"),
        MEDIUM(64.0F, "gt.vajra.speed.medium"),
        FAST(Integer.MAX_VALUE, "gt.vajra.speed.fast");

        private final float digSpeed;
        private final String translationKey;

        SpeedMode(float digSpeed, String translationKey) {
            this.digSpeed = digSpeed;
            this.translationKey = translationKey;
        }

        public String getTranslationKey() {
            return translationKey;
        }
    }
}
