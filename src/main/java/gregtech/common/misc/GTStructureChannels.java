package gregtech.common.misc;

import com.gtnewhorizon.gtnhlib.util.data.BlockMeta;
import com.gtnewhorizon.structurelib.structure.AutoPlaceEnvironment;
import com.gtnewhorizon.structurelib.structure.ICustomBlockSetting;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.ISpecialItemBlock;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.IStructureElementCheckOnly;
import com.gtnewhorizon.structurelib.structure.ITierConverter;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import com.gtnewhorizon.structurelib.util.ItemStackPredicate;
import gregtech.api.enums.GTValues;
import gregtech.api.util.GlassTier;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.StructureLibAPI;

import gregtech.api.enums.Mods;
import gregtech.api.structure.IStructureChannels;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;

/*
 * To unofficial addon authors: Do not add to this enum with EnumHelper or equivalent. Just copy this class into your
 * namespace, and replace the constants
 */
/*
 * Dev notes: Q1: central manage indicator item or in each blocks' constructor? A1: before this is merged #4067 happens.
 * we can build on this info and central manage it EDIT: I ended up with a registerAsIndicator() method here Q2: default
 * tooltip in MBTT builder? A2: Yes Q3: multi specific tier managed here or in individual controller? A3: here, because
 * it needs to be registered to a central location, so it would be nice to have a central location with an easy
 * overview. Plus, it's possible these multi-specific tiers would be become reused by others as development carries on,
 * e.g. PRASS_UNIT_CASING
 */
public enum GTStructureChannels implements IStructureChannels {

    // Order of enum constants does not matter
    QFT_MANIPULATOR("manipulator", "Manipulator Tier"),
    QFT_SHIELDING("shielding", "Shielding Tier"),
    HEATING_COIL("coil", "Heating Coil Tier"),
    BOROGLASS("glass", "Glass Tier"),
    PRASS_UNIT_CASING("unit_casing", "Precise Electronic Unit Casing Tier"),
    METAL_MACHINE_CASING("casing", "Metal Machine Casing Tier"),
    TIER_MACHINE_CASING("machine_casing", "Machine Casing Tier"),
    TIER_CASING("casing", "Machine Casing Tier"),
    SOLENOID("solenoid", "Solenoid Tier"),
    LSC_CAPACITOR("capacitor", "Capacitor Tier"),
    STRUCTURE_HEIGHT("height", "Structure Height"),
    STRUCTURE_LENGTH("length", "Structure Length"),
    PIPE_CASING("pipe", "Pipe Casing Tier"),
    ITEM_PIPE_CASING("item_pipe", "Item Pipe Casing Tier"),
    PSS_CELL("cell", "Vanadium Redox Power Cell Tier"),
    SYNCHROTRON_ANTENNA("antenna", "Antenna Casing Tier"),
    SE_MOTOR("motor", "Space Elevator Motor Tier"),
    EOH_COMPRESSION("spacetime_compression", "Spacetime Compression Field Generator Tier"),
    EOH_STABILISATION("stabilisation", "Stabilisation Field Generator Tier"),
    EOH_DILATION("time_dilation", "Time Dilation Field Generator Tier"),
    HATCH("gt_hatch", "Hatch placement"),
    TFFT_FIELD("field", "Storage Field Tier"),
    EIC_PISTON("piston_block", "Metal Block Tier"),
    ALCHEMICAL_CASING("casing", "Alchemical Casing Tier"),
    ALCHEMICAL_CONSTRUCT("construct", "Alchemical Construct Tier"),
    SUPER_CHEST("super_chest", "Super Chest Tier"),
    MAGNETIC_CHASSIS("chassis", "Magnetic Chassis Tier"),
    COMPONENT_ASSEMBLYLINE_CASING("component_casing", "Component Assembly Line Casing Tier"),
    LES_ESSENTIA_CELL("essentia_cell", "Large Essentia Smeltery Essentia Diffusion Cell Tier"),
    COKE_OVEN_CASING("coke_oven_casing", "Coke Oven Casing Tier");
    //
    ;

    private final String channel;
    private final String defaultTooltip;

    GTStructureChannels(String aChannel, String defaultTooltip) {
        channel = aChannel;
        this.defaultTooltip = defaultTooltip;
    }

    public static class TiredGlassStructureElement {

        /** support all Bart, Botania, Ic2, Thaumcraft glasses for multiblock structure **/
        public static <T> IStructureElement<T> chainAllGlasses(int notSet, BiConsumer<T, Integer> setter,
                                                               Function<T, Integer> getter) {
            return GTStructureChannels.BOROGLASS.use(
                lazy(
                    t -> ofTiredGlass(
                        GlassTier::getGlassBlockTier,
                        notSet,
                        setter,
                        getter,
                        Collections.singletonList("GT5U.structure.tiered_glass"))));
        }

        private static abstract class StructureElement_Bridge<T> implements IStructureElement<T> {

            // seal the deprecated method to prevent accidental overrides
            @Deprecated
            @Override
            public final PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger, IItemSource s,
                                                        EntityPlayerMP actor, Consumer<IChatComponent> chatter) {
                return survivalPlaceBlock(t, world, x, y, z, trigger, AutoPlaceEnvironment.fromLegacy(s, actor, chatter));
            }

            // clear the default implementation. we don't need that.
            public abstract PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger,
                                                           AutoPlaceEnvironment env);
        }

        private static ItemStackPredicate getBlockMetaPredicate(BlockMeta blockMeta) {
            int meta = blockMeta.getBlockMeta();
            Item itemBlock = Item.getItemFromBlock(blockMeta.getBlock());
            if (itemBlock instanceof ISpecialItemBlock) {
                meta = ((ISpecialItemBlock) itemBlock).getItemMetaFromBlockMeta(blockMeta.getBlock(), meta);
            }
            return ItemStackPredicate.from(itemBlock).setMeta(meta);
        }

        public static <T, TIER> IStructureElement<T> ofTiredGlass(
            ITierConverter<TIER> tierExtractor,
            @Nullable TIER notSet,
            BiConsumer<T, TIER> setter,
            Function<T, TIER> getter,
            @Nullable List<String> description
        ) {
            final BlockMeta[][] channelToGlasses = GlassTier.getGlassesByChannel();
            // validate glass list
            if (channelToGlasses.length <= 0) throw new IllegalArgumentException();
            if (Arrays.stream(channelToGlasses).anyMatch(x -> Objects.isNull(x) || x.length <= 0)) throw new IllegalArgumentException();

            final List<Predicate<ItemStack>> preComputedItemStackPredicates = Arrays.stream(channelToGlasses).map(x -> {
                Predicate<ItemStack> predicate = getBlockMetaPredicate(x[0]);
                for (int i = 1; i < x.length; i++) {
                    predicate = predicate.or(getBlockMetaPredicate(x[i]));
                }
                return predicate;
            }).collect(Collectors.toList());

            final IStructureElementCheckOnly<T> check = StructureUtility.ofBlocksTiered(tierExtractor, notSet, setter, getter);
            return new StructureElement_Bridge<T>() {

                @Override
                public boolean check(T t, World world, int x, int y, int z) {
                    return check.check(t, world, x, y, z);
                }

                private Predicate<ItemStack> getPredicate(ItemStack trigger) {
                    int triggerValue = GTStructureChannels.BOROGLASS.hasValue(trigger) ? GTStructureChannels.BOROGLASS.getValue(trigger) : Math.min(trigger.stackSize, GlassTier.getNumGlassTiers() - 1);
                    int idx = Math.min(Math.max(triggerValue, 1), preComputedItemStackPredicates.size()) - 1;
                    return preComputedItemStackPredicates.get(idx);
                }

                private BlockMeta[] getHints(ItemStack trigger) {
                    int triggerValue = GTStructureChannels.BOROGLASS.hasValue(trigger) ? GTStructureChannels.BOROGLASS.getValue(trigger) : Math.min(trigger.stackSize, GlassTier.getNumGlassTiers() - 1);
                    int idx = Math.min(Math.max(triggerValue, 1), channelToGlasses.length) - 1;
                    return channelToGlasses[idx];
                }

                private BlockMeta getHint(ItemStack trigger) {
                    // we pre-check the sizes when this is called
                    // so we should be good to just assume there is always at least 1 elem
                    return getHints(trigger)[0];
                }

                @Override
                public boolean couldBeValid(T t, World world, int x, int y, int z, ItemStack trigger) {
                    // get potential hints
                    BlockMeta[] hints = getHints(trigger);

                    // pre-compute the tier of the block at the current location
                    Block worldBlock = world.getBlock(x, y, z);
                    int worldMeta = world.getBlockMetadata(x, y, z);
                    TIER worldTier = tierExtractor.convert(worldBlock, worldMeta);

                    // check against all potential hints
                    for (BlockMeta hint : hints) {
                        TIER hintTier = tierExtractor.convert(hint.getBlock(), hint.getBlockMeta());
                        // if the block is in the same tier as the hint block, this could be valid
                        if (Objects.equals(hintTier, worldTier)) return true;
                    }

                    // no matches, abort
                    return false;
                }

                @Override
                public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                    BlockMeta hint = getHint(trigger);
                    StructureLibAPI.hintParticle(world, x, y, z, hint.getBlock(), hint.getBlockMeta());
                    return true;
                }

                @Override
                public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                    BlockMeta hint = getHint(trigger);
                    if (hint == null) return false;
                    if (hint.getBlock() instanceof ICustomBlockSetting block) {
                        block.setBlock(world, x, y, z, hint.getBlockMeta());
                    } else {
                        world.setBlock(x, y, z, hint.getBlock(), hint.getBlockMeta(), 2);
                    }
                    return true;
                }

                @Override
                public BlocksToPlace getBlocksToPlace(T t, World world, int x, int y, int z, ItemStack trigger,
                                                      AutoPlaceEnvironment env) {
                    var predicate = getPredicate(trigger);
                    return BlocksToPlace.create(predicate);
                }

                @Override
                public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger,
                                                      AutoPlaceEnvironment env) {
                    BlockMeta[] hints = getHints(trigger);

                    Block worldBlock = world.getBlock(x, y, z);
                    int worldMeta = world.getBlockMetadata(x, y, z);
                    TIER worldTier = tierExtractor.convert(worldBlock, worldMeta);

                    // since all glasses in the same channel should always have the same tier we can just check against the
                    // first one to see if there is already a compatible block in that position
                    if (Objects.equals(worldTier, tierExtractor.convert(hints[0].getBlock(), hints[0].getBlockMeta())))
                        return PlaceResult.SKIP;

                    for (BlockMeta hint : hints) {
                        PlaceResult result = StructureUtility.survivalPlaceBlock(
                            hint.getBlock(),
                            hint.getBlockMeta(),
                            world,
                            x,
                            y,
                            z,
                            env.getSource(),
                            env.getActor(),
                            env.getChatter());
                        if (result != PlaceResult.REJECT) {
                            return result;
                        }
                    }
                    return PlaceResult.REJECT;
                }

                @Nullable
                @Override
                public List<String> getDescription(T context) {
                    TIER currentTier = getter.apply(context);
                    if (Objects.equals(currentTier, notSet)) return description;

                    final int numGlassTier = GlassTier.getNumGlassTiers();
                    for (int i = 0; i < numGlassTier && i < channelToGlasses.length && i + 3 < GTValues.VN.length; i++) {
                        BlockMeta hint = channelToGlasses[i][0];
                        TIER pairTier = tierExtractor.convert(hint.getBlock(), hint.getBlockMeta());
                        if (!Objects.equals(pairTier, currentTier)) continue;
                        return Collections.singletonList("Any " + GTValues.VN[i + 3] + " Glass");
                    }
                    return description;
                }

            };
        }
    }

    @Override
    public String get() {
        return channel;
    }

    @Override
    public String getDefaultTooltip() {
        return defaultTooltip;
    }

    @Override
    public void registerAsIndicator(ItemStack indicator, int channelValue) {
        StructureLibAPI.registerChannelItem(get(), Mods.ModIDs.GREG_TECH, channelValue, indicator);
    }

    public static void register() {
        for (GTStructureChannels value : values()) {
            StructureLibAPI.registerChannelDescription(
                value.get(),
                Mods.ModIDs.GREG_TECH,
                "channels." + Mods.GregTech.ID + "." + value.get());
        }
    }
}
