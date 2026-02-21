package gtPlusPlus.core.item.base.itemblock;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import gregtech.api.interfaces.IOreMaterial;
import gregtech.common.WorldgenGTOreLayer;
import gregtech.common.config.Client;
import gtPlusPlus.core.block.base.BlockBaseOre;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialStack;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.core.util.sys.KeyboardUtils;
import gtneioreplugin.util.DimensionHelper;

public class ItemBlockOre extends ItemBlock {

    private final BlockBaseOre mThisOre;
    private final Material mThisMaterial;
    private final int mThisRadiation;

    public ItemBlockOre(final Block block) {
        super(block);
        if (block instanceof BlockBaseOre) {
            this.mThisOre = (BlockBaseOre) block;
            this.mThisMaterial = this.mThisOre.getMaterialEx();
            this.mThisRadiation = this.mThisMaterial.vRadiationLevel;
        } else {
            this.mThisOre = null;
            this.mThisMaterial = null;
            this.mThisRadiation = 0;
        }
    }

    private static final Map<Material, Set<String>> mMapOreBlockItemToDimName = new LinkedHashMap<>();
    private static boolean mInitOres_Everglades = false;

    private static void initVeinInfo() {
        for (WorldgenGTOreLayer oreLayer : WorldgenGTOreLayer.sList) {
            IOreMaterial[] mats = { oreLayer.mPrimary, oreLayer.mSecondary, oreLayer.mBetween, oreLayer.mSporadic };

            for (IOreMaterial mat : mats) {
                if (mat instanceof Material gtppMat) {
                    mMapOreBlockItemToDimName.put(gtppMat, oreLayer.getAllowedDimensions());
                }
            }
        }
    }

    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List<String> list,
        final boolean bool) {

        if (!mInitOres_Everglades) {
            initVeinInfo();
            mInitOres_Everglades = true;
        }

        if (Client.tooltip.showFormula) {
            if (this.mThisMaterial != null) {
                list.add(this.mThisMaterial.vChemicalFormula);
            }
        }

        if (Client.tooltip.showRadioactiveText) {
            // Radioactive?
            if (this.mThisRadiation > 0) {
                list.add(GTPPCore.GT_Tooltip_Radioactive.get());
            }
        }

        if (Client.tooltip.showOreContainsText) {
            if (this.mThisMaterial != null) {
                list.add(StatCollector.translateToLocal("GTPP.tooltip.ore.contains"));
                if (mThisMaterial.getComposites()
                    .isEmpty()) {
                    list.add("- " + mThisMaterial.getLocalizedName());
                } else {
                    for (MaterialStack m : mThisMaterial.getComposites()) {
                        list.add(
                            "- " + m.getStackMaterial()
                                .getLocalizedName() + " x" + m.getPartsPerOneHundred());
                    }
                }
            }
        }

        if (Client.tooltip.showCtrlText) {
            if (KeyboardUtils.isCtrlKeyDown()) {

                Block b = Block.getBlockFromItem(stack.getItem());
                if (b != null) {
                    int aMiningLevel1 = b.getHarvestLevel(stack.getItemDamage());
                    if (aMiningLevel1 != 0) {
                        list.add(
                            StatCollector.translateToLocalFormatted(
                                "GTPP.tooltip.ore.mining_level",
                                Math.min(Math.max(aMiningLevel1, 0), 5)));
                    }
                }

                Set<String> dims = mMapOreBlockItemToDimName.get(this.mThisMaterial);

                list.add(StatCollector.translateToLocal("GTPP.tooltip.ore.found"));
                if (dims != null && !dims.isEmpty()) {
                    for (String m : dims) {
                        list.add("- " + DimensionHelper.getDimLocalizedName(m));
                    }
                } else {
                    list.add(StatCollector.translateToLocal("GTPP.tooltip.ore.unknown"));
                }

            } else {
                list.add(EnumChatFormatting.DARK_GRAY + StatCollector.translateToLocal("GTPP.tooltip.hold_ctrl"));
            }
        }

        super.addInformation(stack, aPlayer, list, bool);
    }

    @Override
    public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_,
        final boolean p_77663_5_) {
        if (this.mThisMaterial != null && this.mThisRadiation > 0) {
            EntityUtils.applyRadiationDamageToEntity(
                iStack.stackSize,
                this.mThisMaterial.vRadiationLevel,
                world,
                entityHolding);
        }
    }
}
