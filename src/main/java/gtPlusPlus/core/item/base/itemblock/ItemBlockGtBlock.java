package gtPlusPlus.core.item.base.itemblock;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import gregtech.api.util.GTLog;
import gregtech.common.config.Client;
import gtPlusPlus.core.block.base.BasicBlock.BlockTypes;
import gtPlusPlus.core.block.base.BlockBaseModular;
import gtPlusPlus.core.block.base.BlockBaseOre;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialStack;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.core.util.sys.KeyboardUtils;

public class ItemBlockGtBlock extends ItemBlock {

    protected final int blockColour;
    private int sRadiation;

    private Material mMaterial;
    protected BlockTypes thisBlockType;

    private final Block thisBlock;
    private boolean isOre = false;
    private boolean isModular = false;

    public ItemBlockGtBlock(final Block block) {
        super(block);
        this.thisBlock = block;
        if (block instanceof BlockBaseOre) {
            this.isOre = true;
        } else if (block instanceof BlockBaseModular) {
            this.isModular = true;
        }
        final BlockBaseModular baseBlock = (BlockBaseModular) block;
        if (isModular) {
            this.blockColour = baseBlock.getRenderColor(0);
        } else if (isOre) {
            this.blockColour = block.getBlockColor();
        } else {
            this.blockColour = block.getBlockColor();
        }
        if (block instanceof BlockBaseModular g) {
            this.mMaterial = g.getMaterialEx();
            this.thisBlockType = g.thisBlock;
        } else {
            this.mMaterial = null;
            this.thisBlockType = BlockTypes.STANDARD;
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return this.thisBlock.getLocalizedName();
    }

    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {

        if (Client.tooltip.showFormula) {
            if (this.mMaterial != null) {
                list.add(this.mMaterial.vChemicalFormula);
            } else {
                try {
                    BlockBaseModular g = (BlockBaseModular) thisBlock;
                    this.mMaterial = g.getMaterialEx();
                } catch (Exception e) {
                    e.printStackTrace(GTLog.err);
                }
                // list.add("Material is Null.");
            }
        }

        if (this.isOre) {
            if (Client.tooltip.showCtrlText) {
                if (KeyboardUtils.isCtrlKeyDown()) {
                    Block b = Block.getBlockFromItem(stack.getItem());
                    if (b != null) {
                        int aMiningLevel1 = b.getHarvestLevel(stack.getItemDamage());
                        if (this.mMaterial != null) {
                            list.add(
                                StatCollector.translateToLocalFormatted(
                                    "GTPP.tooltip.block.mining_level",
                                    Math.min(Math.max(aMiningLevel1, 0), 5)));
                            list.add(StatCollector.translateToLocal("GTPP.tooltip.block.contains"));
                            if (mMaterial.getComposites()
                                .isEmpty()) {
                                list.add("- " + mMaterial.getLocalizedName());
                            } else {
                                for (MaterialStack m : mMaterial.getComposites()) {
                                    list.add(
                                        "- " + m.getStackMaterial()
                                            .getLocalizedName() + " x" + m.getPartsPerOneHundred());
                                }
                            }
                        }
                    }
                } else {
                    list.add(EnumChatFormatting.DARK_GRAY + StatCollector.translateToLocal("GTPP.tooltip.hold_ctrl"));
                }
            }
        } else {
            if (Client.tooltip.showMiningLevelText) {
                Block b = Block.getBlockFromItem(stack.getItem());
                if (b != null) {
                    int aMiningLevel1 = b.getHarvestLevel(stack.getItemDamage());
                    list.add("Mining Level: " + Math.min(Math.max(aMiningLevel1, 0), 5));
                }
            }
        }

        if (Client.tooltip.showRadioactiveText) {
            if (this.mMaterial != null) {
                if (this.mMaterial.vRadiationLevel > 0) {
                    list.add(GTPPCore.GT_Tooltip_Radioactive.get());
                }
            }
        }

        super.addInformation(stack, aPlayer, list, bool);
    }

    @Override
    public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_,
        final boolean p_77663_5_) {

        if (!isModular && !isOre) {
            mMaterial = null;
        } else {
            if (this.mMaterial == null) {
                Block b = Block.getBlockFromItem(iStack.getItem());
                if (isOre) {
                    mMaterial = ((BlockBaseOre) b).getMaterialEx();
                } else {
                    mMaterial = ((BlockBaseModular) b).getMaterialEx();
                }
                if (mMaterial != null) {
                    this.sRadiation = mMaterial.vRadiationLevel;
                } else {
                    this.sRadiation = 0;
                }
            }
            if (this.sRadiation > 0) {
                EntityUtils.applyRadiationDamageToEntity(iStack.stackSize, this.sRadiation, world, entityHolding);
            }
        }
    }
}
