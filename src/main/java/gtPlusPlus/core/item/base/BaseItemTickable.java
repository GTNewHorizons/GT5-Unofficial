package gtPlusPlus.core.item.base;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.common.config.Client;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.util.Utils;

public class BaseItemTickable extends CoreItem {

    public final String[] descriptionString;
    public final int itemColour;
    public final int maxTicks;
    public final boolean twoRenderPasses;

    public IIcon[] mIcon = new IIcon[2];

    public BaseItemTickable(boolean containerTick, boolean twoPass, final String unlocalName, final int colour,
        final int maxTicks, final String[] Description) {
        super(unlocalName, AddToCreativeTab.tabMisc, 1, 999999999, Description, EnumRarity.epic, true, null);
        this.itemColour = colour;
        this.descriptionString = Description;
        this.maxTicks = maxTicks;
        this.twoRenderPasses = twoPass;
        this.maxStackSize = 1;
        // setGregtechItemList();
    }

    @Override
    public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_,
        final boolean p_77663_5_) {
        if (world == null || iStack == null) {
            return;
        }
        if (world.isRemote) {
            return;
        }

        boolean active = isTicking(iStack);
        if (active) {
            tickItemTag(iStack);
        }
    }

    /**
     * Handle Custom Rendering
     */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return this.twoRenderPasses;
    }

    @Override
    public int getColorFromItemStack(final ItemStack stack, final int renderPass) {
        if (renderPass == 1 && this.twoRenderPasses) {
            return Utils.rgbtoHexValue(255, 255, 255);
        }
        return this.itemColour;
    }

    @Override
    public IIcon getIconFromDamageForRenderPass(final int damage, final int pass) {
        if (this.twoRenderPasses) {
            if (pass == 0) {
                return this.mIcon[0];
            }
            return this.mIcon[1];
        }
        return this.mIcon[0];
    }

    @Override
    public void registerIcons(final IIconRegister i) {
        if (this.twoRenderPasses) {
            this.mIcon[0] = i.registerIcon(GTPlusPlus.ID + ":" + this.getUnlocalizedName());
            this.mIcon[1] = i.registerIcon(GTPlusPlus.ID + ":" + this.getUnlocalizedName() + "_OVERLAY");
        } else {
            this.mIcon[0] = i.registerIcon(GTPlusPlus.ID + ":" + this.getUnlocalizedName());
            // this.overlay = i.registerIcon(getCorrectTextures() + "_OVERLAY");
        }
    }

    private int getMaxTicks() {
        return maxTicks;
    }

    private boolean createNBT(ItemStack rStack) {
        final NBTTagCompound tagMain = new NBTTagCompound();
        final NBTTagCompound tagNBT = new NBTTagCompound();
        tagNBT.setLong("Tick", 0);
        tagNBT.setLong("maxTick", getMaxTicks());
        tagNBT.setBoolean("isActive", true);
        tagMain.setTag("TickableItem", tagNBT);
        rStack.setTagCompound(tagMain);
        return true;
    }

    public final long getTicks(final ItemStack aStack) {
        NBTTagCompound aNBT = aStack.getTagCompound();
        if (aNBT != null) {
            aNBT = aNBT.getCompoundTag("TickableItem");
            if (aNBT != null) {
                return aNBT.getLong("Tick");
            }
        } else {
            createNBT(aStack);
        }
        return 0L;
    }

    public final boolean isTicking(final ItemStack aStack) {
        NBTTagCompound aNBT = aStack.getTagCompound();
        if (aNBT != null) {
            aNBT = aNBT.getCompoundTag("TickableItem");
            if (aNBT != null) {
                return aNBT.getBoolean("isActive");
            }
        } else {
            return createNBT(aStack);
        }
        return true;
    }

    public final void setTicking(final ItemStack aStack, final boolean active) {
        NBTTagCompound aNBT = aStack.getTagCompound();
        if (aNBT != null) {
            aNBT = aNBT.getCompoundTag("TickableItem");
            if (aNBT != null) {
                aNBT.setBoolean("isActive", active);
            }
        } else {
            createNBT(aStack);
        }
    }

    public final boolean tickItemTag(ItemStack aStack) {
        NBTTagCompound aNBT = aStack.getTagCompound();
        if (aNBT != null) {
            if (aNBT.hasKey("TickableItem")) {
                aNBT = aNBT.getCompoundTag("TickableItem");
                // Done Ticking
                if (getMaxTicks() - getTicks(aStack) <= 0) {
                    setTicking(aStack, false);
                    return false;
                }
                if (isTicking(aStack)) {
                    if (aNBT != null) {
                        aNBT.setLong("Tick", getTicks(aStack) + 1);
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return createNBT(aStack);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (stack.getTagCompound() == null) {
            // createNBT(null, stack);
            return 0;
        }
        double currentDamage = getTicks(stack);
        return currentDamage / getMaxTicks();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, @SuppressWarnings("rawtypes") List list,
        boolean bool) {
        if (Client.tooltip.showFormula) {
            if (this.descriptionString.length > 0) {
                list.add(EnumChatFormatting.GRAY + this.descriptionString[0]);
            }
        }
        long maxTicks = getMaxTicks();
        long ticks = 0;
        if (stack.hasTagCompound()) {
            ticks = getTicks(stack);
        }
        EnumChatFormatting durability = EnumChatFormatting.GRAY;
        if (maxTicks - ticks > (maxTicks * 0.8)) {
            durability = EnumChatFormatting.GRAY;
        } else if (maxTicks - ticks > (maxTicks * 0.6)) {
            durability = EnumChatFormatting.GREEN;
        } else if (maxTicks - ticks > (maxTicks * 0.4)) {
            durability = EnumChatFormatting.YELLOW;
        } else if (maxTicks - ticks > (maxTicks * 0.2)) {
            durability = EnumChatFormatting.GOLD;
        } else if (maxTicks - ticks > 0) {
            durability = EnumChatFormatting.RED;
        }
        list.add(
            durability + StatCollector
                .translateToLocalFormatted("gtpp.tooltip.tickable.decay_seconds", (maxTicks - ticks) / 20));

        if (this.descriptionString.length > 1) {
            for (int h = 1; h < this.descriptionString.length; h++) {
                list.add(EnumChatFormatting.GRAY + this.descriptionString[h]);
            }
        }

        // super.addInformation(stack, player, list, bool);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
    }
}
