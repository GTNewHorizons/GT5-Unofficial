package com.elisis.gtnhlanth.common.register;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.elisis.gtnhlanth.common.beamline.TileBeamline;
import com.elisis.gtnhlanth.common.block.AntennaCasing;
import com.elisis.gtnhlanth.common.block.Casing;
import com.elisis.gtnhlanth.common.block.ShieldedAccGlass;
import com.elisis.gtnhlanth.common.hatch.TileBusInputFocus;
import com.elisis.gtnhlanth.common.hatch.TileHatchInputBeamline;
import com.elisis.gtnhlanth.common.hatch.TileHatchOutputBeamline;
import com.elisis.gtnhlanth.common.item.LanthItem;
import com.elisis.gtnhlanth.common.item.MaskList;
import com.elisis.gtnhlanth.common.item.ParticleItem;
import com.elisis.gtnhlanth.common.item.PhotolithographicMask;
import com.elisis.gtnhlanth.common.tileentity.Digester;
import com.elisis.gtnhlanth.common.tileentity.DissolutionTank;
import com.elisis.gtnhlanth.common.tileentity.LINAC;
import com.elisis.gtnhlanth.common.tileentity.SourceChamber;
import com.elisis.gtnhlanth.common.tileentity.Synchrotron;
import com.elisis.gtnhlanth.common.tileentity.TargetChamber;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GT_LanguageManager;

public final class LanthItemList {

    public static ItemStack DIGESTER;
    public static ItemStack DISSOLUTION_TANK;

    public static ItemStack LINAC;
    public static ItemStack SOURCE_CHAMBER;

    public static ItemStack SYNCHROTRON;

    public static ItemStack TARGET_CHAMBER;

    public static ItemStack BEAMLINE_PIPE;

    public static ItemStack LUV_BEAMLINE_INPUT_HATCH;
    public static ItemStack LUV_BEAMLINE_OUTPUT_HATCH;

    public static ItemStack BEAMLINE_FOCUS_INPUT_BUS;

    public static Item CAPILLARY_EXCHANGE = new LanthItem("capillary_exchange");

    public static Item MM_LATTICE = new LanthItem("mm_lattice");

    public static Item IRON_COATED_QUARTZ = new LanthItem("iron_quartz_plate");

    public static Item SUBSTRATE_PRECURSOR = new LanthItem("substrate_precursor");

    public static Item MASK_SUBSTRATE = new LanthItem("mask_substrate");

    public static Item MASKED_MASK = new LanthItem("masked_mask_substrate");

    public static Item ETCHED_MASK_1 = new LanthItem("etched_mask1");

    public static Item SILICON_NITRIDE_MEMBRANE = new LanthItem("nitride_gold_membrane");

    public static Item PARTICLE_ITEM = new ParticleItem().setUnlocalizedName("particle");

    public static final Block SHIELDED_ACCELERATOR_CASING = new Casing("shielded_accelerator");
    public static final Block SHIELDED_ACCELERATOR_GLASS = new ShieldedAccGlass();

    public static final Block ELECTRODE_CASING = new Casing("electrode");

    public static final Block COOLANT_DELIVERY_CASING = new Casing("coolant_delivery");

    // public static final Block ANTENNA_CASING_T1 = new Casing("antenna_t1");
    public static final Block ANTENNA_CASING_T1 = new AntennaCasing(1);
    public static final Block ANTENNA_CASING_T2 = new AntennaCasing(2);

    public static final Block NIOBIUM_CAVITY_CASING = new Casing("niobium_cavity");

    public static final Block FOCUS_MANIPULATION_CASING = new Casing("focus_manipulator");
    public static final Block FOCUS_HOLDER = new Casing("focus_holder");

    public static final Block TARGET_RECEPTACLE_CASING = new Casing("target_receptacle");
    public static final Block TARGET_HOLDER = new Casing("target_holder");

    public static HashMap<MaskList, Item> maskMap = new HashMap<>();

    public static void registerGTMTE() {

        DIGESTER = new Digester(10500, "Digester", "Digester").getStackForm(1L);
        DISSOLUTION_TANK = new DissolutionTank(10501, "Dissolution Tank", "Dissolution Tank").getStackForm(1L);

        BEAMLINE_PIPE = new TileBeamline(10502, "Beamline Pipe", "Beamline Pipe").getStackForm(1L);
        LUV_BEAMLINE_INPUT_HATCH = new TileHatchInputBeamline(
            10503,
            "LuV Beamline Input Hatch",
            "LuV Beamline Input Hatch",
            6).getStackForm(1L);
        LUV_BEAMLINE_OUTPUT_HATCH = new TileHatchOutputBeamline(
            10504,
            "LuV Beamline Output Hatch",
            "LuV Beamline Output Hatch",
            6).getStackForm(1L);

        BEAMLINE_FOCUS_INPUT_BUS = new TileBusInputFocus(10509, "Focus Input Bus", "Focus Input Bus").getStackForm(1L);

        LINAC = new LINAC(10505, "Linear Accelerator", "Linear Accelerator").getStackForm(1L);

        SOURCE_CHAMBER = new SourceChamber(10506, "Source Chamber", "Source Chamber").getStackForm(1L);

        SYNCHROTRON = new Synchrotron(10507, "Synchrotron", "Synchrotron").getStackForm(1L);

        TARGET_CHAMBER = new TargetChamber(10508, "Target Chamber", "Target Chamber").getStackForm(1L);
    }

    public static void registerTypical() {

        GameRegistry.registerItem(CAPILLARY_EXCHANGE, CAPILLARY_EXCHANGE.getUnlocalizedName());

        GameRegistry.registerItem(MM_LATTICE, MM_LATTICE.getUnlocalizedName());

        GameRegistry.registerItem(IRON_COATED_QUARTZ, IRON_COATED_QUARTZ.getUnlocalizedName());

        GameRegistry.registerItem(MASK_SUBSTRATE, MASK_SUBSTRATE.getUnlocalizedName());

        GameRegistry.registerItem(ETCHED_MASK_1, ETCHED_MASK_1.getUnlocalizedName());

        GameRegistry.registerItem(SUBSTRATE_PRECURSOR, SUBSTRATE_PRECURSOR.getUnlocalizedName());

        GameRegistry.registerItem(MASKED_MASK, MASKED_MASK.getUnlocalizedName());

        GameRegistry.registerItem(PARTICLE_ITEM, "particle");

        GameRegistry.registerBlock(SHIELDED_ACCELERATOR_CASING, SHIELDED_ACCELERATOR_CASING.getUnlocalizedName());

        GameRegistry.registerBlock(ELECTRODE_CASING, ELECTRODE_CASING.getUnlocalizedName());

        GameRegistry.registerBlock(COOLANT_DELIVERY_CASING, COOLANT_DELIVERY_CASING.getUnlocalizedName());

        GameRegistry.registerBlock(SHIELDED_ACCELERATOR_GLASS, SHIELDED_ACCELERATOR_GLASS.getUnlocalizedName());

        GameRegistry.registerBlock(ANTENNA_CASING_T1, ANTENNA_CASING_T1.getUnlocalizedName());

        GameRegistry.registerBlock(ANTENNA_CASING_T2, ANTENNA_CASING_T2.getUnlocalizedName());

        GameRegistry.registerBlock(NIOBIUM_CAVITY_CASING, NIOBIUM_CAVITY_CASING.getUnlocalizedName());

        GameRegistry.registerBlock(FOCUS_MANIPULATION_CASING, FOCUS_MANIPULATION_CASING.getUnlocalizedName());

        GameRegistry.registerBlock(FOCUS_HOLDER, FOCUS_HOLDER.getUnlocalizedName());

        GameRegistry.registerBlock(TARGET_RECEPTACLE_CASING, TARGET_RECEPTACLE_CASING.getUnlocalizedName());

        GameRegistry.registerBlock(TARGET_HOLDER, TARGET_HOLDER.getUnlocalizedName());

        for (MaskList mask : MaskList.values()) {

            String english = mask.getEnglishName();

            String descSpectrum = mask.getSpectrum();

            PhotolithographicMask maskItem = new PhotolithographicMask(mask.getName(), mask.getDamage(), descSpectrum);
            GameRegistry.registerItem(maskItem, maskItem.getUnlocalizedName());

            GT_LanguageManager.addStringLocalization(maskItem.getUnlocalizedName() + ".name", "Mask (" + english + ")");

            maskMap.put(mask, maskItem);

        }

    }
}
