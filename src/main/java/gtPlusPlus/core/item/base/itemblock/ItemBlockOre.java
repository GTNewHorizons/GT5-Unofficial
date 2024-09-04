package gtPlusPlus.core.item.base.itemblock;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.block.base.BlockBaseOre;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialStack;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.core.util.sys.KeyboardUtils;
import gtPlusPlus.everglades.gen.gt.WorldGen_GT_Ore_Layer;

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

    private static Map<String, AutoMap<String>> mMapOreBlockItemToDimName = new LinkedHashMap<>();
    private static boolean mInitOres_Everglades = false;
    private AutoMap<String> mDimsForThisOre = new AutoMap<>();

    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {

        if (!mInitOres_Everglades) {
            for (WorldGen_GT_Ore_Layer f : gtPlusPlus.everglades.gen.gt.WorldGen_Ores.validOreveins.values()) {
                Material[] m2 = new Material[] { f.mPrimary, f.mSecondary, f.mBetween, f.mSporadic };
                for (Material m1 : m2) {
                    AutoMap<String> aMap = mMapOreBlockItemToDimName.get(
                        m1.getUnlocalizedName()
                            .toLowerCase());
                    if (aMap == null) {
                        aMap = new AutoMap<>();
                    }
                    String aDimName = "Everglades";
                    if (!aMap.containsValue(aDimName)) {
                        aMap.put(aDimName);
                    }
                    mMapOreBlockItemToDimName.put(
                        m1.getUnlocalizedName()
                            .toLowerCase(),
                        aMap);
                }
            }
            mInitOres_Everglades = true;
        }

        if (this.mThisMaterial != null) {
            list.add(this.mThisMaterial.vChemicalFormula);
        }

        // Radioactive?
        if (this.mThisRadiation > 0) {
            list.add(GTPPCore.GT_Tooltip_Radioactive.get());
        }

        if (this.mThisMaterial != null) {
            list.add("Ore contains:    ");
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

        if (KeyboardUtils.isCtrlKeyDown()) {

            Block b = Block.getBlockFromItem(stack.getItem());
            if (b != null) {
                int aMiningLevel1 = b.getHarvestLevel(stack.getItemDamage());
                if (aMiningLevel1 != 0) {
                    list.add("Mining Level: " + Math.min(Math.max(aMiningLevel1, 0), 5));
                }
            }

            if (mDimsForThisOre.isEmpty()) {
                AutoMap<String> A = mMapOreBlockItemToDimName.get(
                    this.mThisMaterial.getUnlocalizedName()
                        .toLowerCase());
                if (A != null) {
                    mDimsForThisOre = A;
                }
            }

            list.add("Found:    ");
            if (!mDimsForThisOre.isEmpty()) {
                for (String m : mDimsForThisOre) {
                    list.add("- " + m);
                }
            } else {
                list.add("- Unknown");
            }

        } else {
            list.add(EnumChatFormatting.DARK_GRAY + "Hold Ctrl to show additional info.");
        }

        super.addInformation(stack, aPlayer, list, bool);
    }

    @Override
    public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_,
        final boolean p_77663_5_) {
        if (this.mThisMaterial != null) {
            if (this.mThisRadiation > 0) {
                if (entityHolding instanceof EntityPlayer) {
                    if (!((EntityPlayer) entityHolding).capabilities.isCreativeMode) {
                        EntityUtils.applyRadiationDamageToEntity(
                            iStack.stackSize,
                            this.mThisMaterial.vRadiationLevel,
                            world,
                            entityHolding);
                    }
                }
            }
        }
    }
}
