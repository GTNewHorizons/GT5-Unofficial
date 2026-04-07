package gregtech.api.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.function.ToIntFunction;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import gregtech.api.casing.ICasing;
import gregtech.api.enums.HatchElement;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.chars.CharArrayList;
import it.unimi.dsi.fastutil.chars.CharComparator;
import it.unimi.dsi.fastutil.chars.CharList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;

/**
 * An extended tooltip builder with some compat for the structure wrapper system.
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
public class StructureWrapperTooltipBuilder<MTE extends MTEEnhancedMultiBlockBase<?> & IStructureProvider<MTE>>
    extends MultiblockTooltipBuilder {

    public final StructureWrapper<MTE> structure;

    private final Object2ObjectArrayMap<ICasing, String> casingNameOverrides = new Object2ObjectArrayMap<>();
    private final Object2ObjectArrayMap<IHatchElement<? super MTE>, String> hatchNameOverrides = new Object2ObjectArrayMap<>();
    private final Object2ObjectArrayMap<IHatchElement<? super MTE>, String> hatchInfoOverrides = new Object2ObjectArrayMap<>();
    private boolean hasExoticHatches = false, hasMultiAmpHatches = false, printMultiampSupport = true;

    public StructureWrapperTooltipBuilder(StructureWrapper<MTE> structure) {
        this.structure = structure;
    }

    /**
     * Adds the structure size info.
     */
    public StructureWrapperTooltipBuilder<MTE> beginStructureBlock(boolean hollow) {
        if (!structure.minSize.equals(structure.maxSize)) {
            super.beginVariableStructureBlock(
                structure.minSize.get0(),
                structure.minSize.get1(),
                structure.minSize.get2(),
                structure.maxSize.get0(),
                structure.maxSize.get1(),
                structure.maxSize.get2(),
                hollow);
        } else {
            super.beginStructureBlock(
                structure.minSize.get0(),
                structure.minSize.get1(),
                structure.minSize.get2(),
                hollow);
        }

        return this;
    }

    /**
     * Adds the structure size info.
     */
    public StructureWrapperTooltipBuilder<MTE> beginStructureBlock() {
        if (!structure.minSize.equals(structure.maxSize)) {
            super.beginVariableStructureBlock(
                structure.minSize.get0(),
                structure.minSize.get1(),
                structure.minSize.get2(),
                structure.maxSize.get0(),
                structure.maxSize.get1(),
                structure.maxSize.get2(),
                false);
        } else {
            super.beginStructureBlock(
                structure.minSize.get0(),
                structure.minSize.get1(),
                structure.minSize.get2(),
                false);
        }

        return this;
    }

    /**
     * Sets the name for a hatch instead of using the default name.
     */
    public StructureWrapperTooltipBuilder<MTE> addCasingNameOverride(ICasing casing, String newName) {
        casingNameOverrides.put(casing, newName);
        return this;
    }

    public StructureWrapperTooltipBuilder<MTE> addCasingInfoAuto(ICasing casing) {
        if (structure.getCasingMax(casing) != structure.getCasingMin(casing)) {
            addCasingInfoRange(casing);
        } else {
            addCasingInfoExact(casing);
        }

        return this;
    }

    public StructureWrapperTooltipBuilder<MTE> addCasingInfoExact(ICasing casing) {
        addCasingInfoExactly(casing.getLocalizedName(), structure.getCasingMax(casing), casing.isTiered());
        return this;
    }

    public StructureWrapperTooltipBuilder<MTE> addCasingInfoRange(ICasing casing) {
        String name;

        if (casingNameOverrides.containsKey(casing)) {
            name = casingNameOverrides.get(casing);
        } else {
            name = casing.getLocalizedName();
        }

        addCasingInfoRange(name, structure.getCasingMin(casing), structure.getCasingMax(casing), casing.isTiered());

        return this;
    }

    /**
     * Sets the name for a hatch instead of using the default name.
     */
    public StructureWrapperTooltipBuilder<MTE> addHatchNameOverride(IHatchElement<? super MTE> hatch, String newName) {
        hatchNameOverrides.put(hatch, newName);
        return this;
    }

    /**
     * Sets the name for a hatch instead of using the default name.
     */
    public StructureWrapperTooltipBuilder<MTE> addHatchNameOverride(IHatchElement<? super MTE> hatch, ItemStack stack) {
        hatchNameOverrides.put(hatch, stack.getDisplayName());
        return this;
    }

    /**
     * Sets the location/casing for a hatch instead of using the default location.
     */
    public StructureWrapperTooltipBuilder<MTE> addHatchLocationOverride(IHatchElement<? super MTE> hatch,
        String newLocation) {
        hatchInfoOverrides.put(hatch, newLocation);
        return this;
    }

    /**
     * Sets the location/casing for a hatch instead of using the default location.
     */
    public StructureWrapperTooltipBuilder<MTE> addHatchLocationOverride(Collection<IHatchElement<? super MTE>> hatches,
        String newLocation) {
        for (var hatch : hatches) {
            hatchInfoOverrides.put(hatch, newLocation);
        }
        return this;
    }

    public StructureWrapperTooltipBuilder<MTE> disableMultiAmpHatchLine() {
        printMultiampSupport = false;
        return this;
    }

    /**
     * Add a hatch line manually.
     */
    public StructureWrapperTooltipBuilder<MTE> addHatch(ICasing casing, IHatchElement<? super MTE> hatch,
        int... hintNumbers) {
        String nameOverride = hatchNameOverrides.get(hatch);

        String info = hatchInfoOverrides.get(hatch);

        // if we were given a hatch info override, use it
        if (info == null) info = GTUtility.translate("GT5U.MBTT.HatchInfo", casing.getLocalizedName());

        // add hintNumbers to the info if possible
        if (hintNumbers.length > 0) {
            info += GTUtility.translate(
                "GT5U.MBTT.HatchHint",
                String.join(", ", GTDataUtils.mapToList(new IntArrayList(hintNumbers), Object::toString)));
        }

        if (nameOverride != null) {
            addStructurePart(nameOverride, info, hintNumbers);
        } else {
            // try to use an existing addXHatch method if possible
            if (hatch instanceof HatchElement gtHatch) {
                switch (gtHatch) {
                    case Dynamo:
                        addDynamoHatch(info, hintNumbers);
                        break;
                    case Energy:
                        addEnergyHatch(info, hintNumbers);
                        break;
                    case ExoticEnergy:
                        addStructurePart("GT5U.MBTT.MultiampEnergyHatch", info, hintNumbers);
                        addStructurePart("GT5U.MBTT.LaserTargetHatch", info, hintNumbers);
                        hasExoticHatches = true;
                        break;
                    case MultiAmpEnergy:
                        addStructurePart("GT5U.MBTT.MultiampEnergyHatch", info, hintNumbers);
                        hasMultiAmpHatches = true;
                        break;
                    case InputBus:
                        addInputBus(info, hintNumbers);
                        break;
                    case InputHatch:
                        addInputHatch(info, hintNumbers);
                        break;
                    case Maintenance:
                        addMaintenanceHatch(info, hintNumbers);
                        break;
                    case Muffler:
                        addMufflerHatch(info, hintNumbers);
                        break;
                    case OutputBus:
                        addOutputBus(info, hintNumbers);
                        break;
                    case OutputHatch:
                        addOutputHatch(info, hintNumbers);
                        break;
                    default:
                        break;
                }
            } else if (hatch instanceof TTMultiblockBase.HatchElement ttHatch) {
                switch (ttHatch) {
                    case EnergyMulti -> {
                        addStructurePart("GT5U.MBTT.MultiampEnergyHatch", info, hintNumbers);
                        addStructurePart("GT5U.MBTT.LaserTargetHatch", info, hintNumbers);
                        hasExoticHatches = true;
                    }
                    case DynamoMulti -> {
                        addStructurePart("GT5U.MBTT.MultiampEnergyDynamo", info, hintNumbers);
                        addStructurePart("GT5U.MBTT.LaserSourceHatch", info, hintNumbers);
                        hasExoticHatches = true;
                    }
                    default -> addStructurePart(ttHatch.getDisplayName(), info, hintNumbers);
                }
            } else {
                // fallback for custom hatches
                addStructurePart(hatch.getDisplayName(), info, hintNumbers);
            }
        }

        return this;
    }

    /**
     * Automatically adds casing and hatch lines.
     */
    public StructureWrapperTooltipBuilder<MTE> addAllCasingInfo() {
        addAllCasingInfo(null, null);
        return this;
    }

    /**
     * Automatically adds casing and hatch lines. If this doesn't do something you need, add it if it's simple. If it's
     * something cursed or complex don't bother, just call the proper methods manually. This method should only cover
     * normal use cases.
     *
     * @see StructureWrapperTooltipBuilder#addCasingInfoAuto(ICasing)
     * @see StructureWrapperTooltipBuilder#addHatch(ICasing, IHatchElement, int...)
     */
    public StructureWrapperTooltipBuilder<MTE> addAllCasingInfo(@Nullable List<ICasing> casingOrder,
        @Nullable List<IHatchElement<? super MTE>> hatchOrder) {
        ObjectArraySet<ICasing> addedCasings = new ObjectArraySet<>();
        ObjectArraySet<IHatchElement<?>> addedHatches = new ObjectArraySet<>();

        // make a list containing the casing chars so that we can sort it properly
        CharList casings = new CharArrayList(structure.casings.keySet());

        if (casingOrder != null && !casingOrder.isEmpty()) {
            // if we were given a casing order, use it
            CharComparator comparator = (char a, char b) -> {
                int i1 = casingOrder.indexOf(structure.casings.get(a).casing);
                int i2 = casingOrder.indexOf(structure.casings.get(b).casing);

                if (i1 == -1 || i2 == -1) {
                    return -Integer.compare(i1, i2);
                } else {
                    return Integer.compare(i1, i2);
                }
            };

            casings.sort(comparator);
        } else {
            // otherwise, sort naturally (by the casing char, which isn't very useful, but it's deterministic)
            casings.sort(null);
        }

        Multimap<Pair<ICasing, IHatchElement<? super MTE>>, Integer> hatches = ArrayListMultimap.create();

        for (char c : casings) {
            CasingInfo<MTE> casingInfo = structure.casings.get(c);

            // add a line for casings as we see them, but don't add them twice if the same ICasing was used multiple
            // times
            if (addedCasings.add(casingInfo.casing)) {
                addCasingInfoAuto(casingInfo.casing);
            }

            // keep track of any hatches in this structure element and their hint numbers
            if (casingInfo.hatches != null) {
                for (var hatch : casingInfo.hatches) {
                    hatches.put(Pair.of(casingInfo.casing, hatch), casingInfo.dot);
                }
            }
        }

        List<Pair<ICasing, IHatchElement<? super MTE>>> hatchesSorted = new ArrayList<>(new HashSet<>(hatches.keys()));

        if (hatchOrder != null && !hatchOrder.isEmpty()) {
            // if we were given a hatch order, use it
            hatchesSorted.sort((p1, p2) -> {
                int i1 = hatchOrder.indexOf(p1.right());
                int i2 = hatchOrder.indexOf(p2.right());

                if (i1 == -1 || i2 == -1) {
                    return -Integer.compare(i1, i2);
                } else {
                    return Integer.compare(i1, i2);
                }
            });
        } else {
            // otherwise sort by the hatch type, followed by the hatch display name
            ToIntFunction<Pair<ICasing, IHatchElement<? super MTE>>> categoryComparator = p -> {
                if (p.right() instanceof HatchElement gtHatch) {
                    return gtHatch.ordinal();
                } else if (p.right() instanceof TTMultiblockBase.HatchElement ttHatch) {
                    return ttHatch.ordinal() + 100;
                } else {
                    return 200;
                }
            };

            // this is only relevant for special hatches
            Comparator<Pair<ICasing, IHatchElement<? super MTE>>> nameComparator = Comparator.nullsFirst(
                Comparator.comparing(
                    p -> hatchNameOverrides.getOrDefault(
                        p.right(),
                        p.right()
                            .getDisplayName())));

            hatchesSorted.sort(
                Comparator.comparingInt(categoryComparator)
                    .thenComparing(nameComparator));
        }

        for (var hatch : hatchesSorted) {
            // dedupe the hint numbers and sort them
            IntArrayList hintNumbers = new IntArrayList(new IntArraySet(hatches.get(hatch)));
            hintNumbers.sort(null);

            // finally add the hatch
            addHatch(hatch.left(), hatch.right(), hintNumbers.toIntArray());
        }

        // add the tectech multi amp hatch info line if it should be added
        if (printMultiampSupport && hasExoticHatches) {
            addTecTechHatchInfo();
        }

        if (printMultiampSupport && hasMultiAmpHatches) {
            addMultiAmpHatchInfo();
        }

        return this;
    }
}
