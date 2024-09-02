package gtnhlanth.common.register;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GTLanguageManager;
import gtnhlanth.common.beamline.MTEBeamlinePipe;
import gtnhlanth.common.block.BlockAntennaCasing;
import gtnhlanth.common.block.BlockCasing;
import gtnhlanth.common.block.BlockShieldedAccGlass;
import gtnhlanth.common.hatch.MTEBusInputFocus;
import gtnhlanth.common.hatch.MTEHatchInputBeamline;
import gtnhlanth.common.hatch.MTEHatchOutputBeamline;
import gtnhlanth.common.item.ItemLanth;
import gtnhlanth.common.item.ItemParticle;
import gtnhlanth.common.item.ItemPhotolithographicMask;
import gtnhlanth.common.item.MaskList;
import gtnhlanth.common.tileentity.MTEDigester;
import gtnhlanth.common.tileentity.MTEDissolutionTank;
import gtnhlanth.common.tileentity.MTELINAC;
import gtnhlanth.common.tileentity.MTESourceChamber;
import gtnhlanth.common.tileentity.MTESynchrotron;
import gtnhlanth.common.tileentity.MTETargetChamber;

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

    public static Item CAPILLARY_EXCHANGE = new ItemLanth("capillary_exchange");

    public static Item MM_LATTICE = new ItemLanth("mm_lattice");

    public static Item IRON_COATED_QUARTZ = new ItemLanth("iron_quartz_plate");

    public static Item SUBSTRATE_PRECURSOR = new ItemLanth("substrate_precursor");

    public static Item MASK_SUBSTRATE = new ItemLanth("mask_substrate");

    public static Item MASKED_MASK = new ItemLanth("masked_mask_substrate");

    public static Item ETCHED_MASK_1 = new ItemLanth("etched_mask1");

    public static Item SILICON_NITRIDE_MEMBRANE = new ItemLanth("nitride_gold_membrane");

    public static Item PARTICLE_ITEM = new ItemParticle().setUnlocalizedName("particle");

    public static final Block SHIELDED_ACCELERATOR_CASING = new BlockCasing("shielded_accelerator");
    public static final Block SHIELDED_ACCELERATOR_GLASS = new BlockShieldedAccGlass();

    public static final Block ELECTRODE_CASING = new BlockCasing("electrode");

    public static final Block COOLANT_DELIVERY_CASING = new BlockCasing("coolant_delivery");

    // public static final Block ANTENNA_CASING_T1 = new Casing("antenna_t1");
    public static final Block ANTENNA_CASING_T1 = new BlockAntennaCasing(1);
    public static final Block ANTENNA_CASING_T2 = new BlockAntennaCasing(2);

    public static final Block NIOBIUM_CAVITY_CASING = new BlockCasing("niobium_cavity");

    public static final Block FOCUS_MANIPULATION_CASING = new BlockCasing("focus_manipulator");
    public static final Block FOCUS_HOLDER = new BlockCasing("focus_holder");

    public static final Block TARGET_RECEPTACLE_CASING = new BlockCasing("target_receptacle");
    public static final Block TARGET_HOLDER = new BlockCasing("target_holder");

    public static HashMap<MaskList, Item> maskMap = new HashMap<>();

    public static void registerGTMTE() {

        DIGESTER = new MTEDigester(10500, "Digester", "Digester").getStackForm(1L);
        DISSOLUTION_TANK = new MTEDissolutionTank(10501, "Dissolution Tank", "Dissolution Tank").getStackForm(1L);

        BEAMLINE_PIPE = new MTEBeamlinePipe(10502, "Beamline Pipe", "Beamline Pipe").getStackForm(1L);
        LUV_BEAMLINE_INPUT_HATCH = new MTEHatchInputBeamline(
            10503,
            "LuV Beamline Input Hatch",
            "LuV Beamline Input Hatch",
            6).getStackForm(1L);
        LUV_BEAMLINE_OUTPUT_HATCH = new MTEHatchOutputBeamline(
            10504,
            "LuV Beamline Output Hatch",
            "LuV Beamline Output Hatch",
            6).getStackForm(1L);

        BEAMLINE_FOCUS_INPUT_BUS = new MTEBusInputFocus(10509, "Focus Input Bus", "Focus Input Bus").getStackForm(1L);

        LINAC = new MTELINAC(10505, "Linear Accelerator", "Linear Accelerator").getStackForm(1L);

        SOURCE_CHAMBER = new MTESourceChamber(10506, "Source Chamber", "Source Chamber").getStackForm(1L);

        SYNCHROTRON = new MTESynchrotron(10507, "Synchrotron", "Synchrotron").getStackForm(1L);

        TARGET_CHAMBER = new MTETargetChamber(10508, "Target Chamber", "Target Chamber").getStackForm(1L);
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

            ItemPhotolithographicMask maskItem = new ItemPhotolithographicMask(
                mask.getName(),
                mask.getDamage(),
                descSpectrum);
            GameRegistry.registerItem(maskItem, maskItem.getUnlocalizedName());

            GTLanguageManager.addStringLocalization(maskItem.getUnlocalizedName() + ".name", "Mask (" + english + ")");

            maskMap.put(mask, maskItem);

        }

    }
}
