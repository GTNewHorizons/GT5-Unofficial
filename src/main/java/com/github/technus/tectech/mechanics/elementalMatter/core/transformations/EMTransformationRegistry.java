package com.github.technus.tectech.mechanics.elementalMatter.core.transformations;

import static java.lang.Math.pow;

import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.IEMStack;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by Tec on 26.05.2017.
 */
public class EMTransformationRegistry {
    /**
     * Atom count per Mol
     */
    public static final double AVOGADRO_CONSTANT = 6.02214076e23D;
    /**
     * Scale to 1m^3 of C-12
     */
    public static final double EM_COUNT_PER_CUBE = AVOGADRO_CONSTANT * 1650_000D / 0.012;

    public static final double EM_COUNT_PER_MATERIAL_AMOUNT = EM_COUNT_PER_CUBE / 144 / 9;
    public static final double EM_COUNT_PER_ITEM = EM_COUNT_PER_CUBE * 9;
    public static final double EM_COUNT_PER_1k = EM_COUNT_PER_MATERIAL_AMOUNT * 1000;

    public static final double EM_COUNT_MINIMUM = 1 / EM_COUNT_PER_CUBE;
    /**
     * Quantity considered to be indifferent when computing stuff
     */
    public static final double EM_COUNT_EPSILON = EM_COUNT_PER_CUBE / pow(2, 40);

    public static final double EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED =
            EM_COUNT_PER_MATERIAL_AMOUNT - EM_COUNT_EPSILON;
    public static final double EM_COUNT_PER_CUBE_DIMINISHED = EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED * 144 * 9;
    public static final double EM_COUNT_PER_ITEM_DIMINISHED = EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED * 144;
    public static final double EM_COUNT_PER_1k_DIMINISHED = EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED * 1000;

    private final Map<Integer, EMFluidQuantizationInfo> fluidQuantization;
    private final Map<EMItemQuantizationInfo, EMItemQuantizationInfo> itemQuantization;
    private final Map<Integer, EMOredictQuantizationInfo> oredictQuantization;

    private final Map<IEMDefinition, EMDequantizationInfo> infoMap;
    private final Function<IEMStack, EMDequantizationInfo> creator;

    public EMTransformationRegistry() {
        this(EMDequantizationInfo::new);
    }

    public EMTransformationRegistry(Function<IEMStack, EMDequantizationInfo> creator) {
        this(creator, new HashMap<>(16), new HashMap<>(16), new HashMap<>(64), new HashMap<>(256));
    }

    public EMTransformationRegistry(
            Function<IEMStack, EMDequantizationInfo> creator,
            Map<Integer, EMFluidQuantizationInfo> fluidQuantization,
            Map<EMItemQuantizationInfo, EMItemQuantizationInfo> itemQuantization,
            Map<Integer, EMOredictQuantizationInfo> oredictQuantization,
            Map<IEMDefinition, EMDequantizationInfo> infoMap) {
        this.creator = creator;
        this.fluidQuantization = fluidQuantization;
        this.itemQuantization = itemQuantization;
        this.oredictQuantization = oredictQuantization;
        this.infoMap = infoMap;
    }

    protected EMDequantizationInfo compute(IEMStack em) {
        return infoMap.computeIfAbsent(em.getDefinition(), stack -> creator.apply(em));
    }

    public void addFluid(IEMStack em, FluidStack fluidStack) {
        getFluidQuantization().put(fluidStack.getFluidID(), new EMFluidQuantizationInfo(fluidStack, em));
        compute(em).setFluid(fluidStack);
    }

    public void addFluid(IEMStack em, Fluid fluid, int fluidAmount) {
        addFluid(em, new FluidStack(fluid, fluidAmount));
    }

    protected void addItemQuantization(EMItemQuantizationInfo aIQI) {
        getItemQuantization().put(aIQI, aIQI);
    }

    public void addItem(IEMStack em, ItemStack itemStack, boolean skipNBT) {
        addItemQuantization(new EMItemQuantizationInfo(itemStack, skipNBT, em));
        compute(em).setItem(itemStack);
    }

    public void addItem(IEMStack em, OrePrefixes prefix, Materials material, int amount, boolean skipNBT) {
        addItem(em, GT_OreDictUnificator.get(prefix, material, amount), skipNBT);
    }

    public void addOredict(IEMStack em, int id, int qty) {
        getOredictQuantization().put(id, new EMOredictQuantizationInfo(id, qty, em));
        compute(em).setOre(new OreDictionaryStack(qty, id));
    }

    public void addOredict(IEMStack em, String name, int qty) {
        addOredict(em, OreDictionary.getOreID(name), qty);
    }

    public void addOredict(IEMStack em, OrePrefixes prefix, Materials material, int qty) {
        addOredict(em, prefix, material.mName, qty);
    }

    public void addOredict(IEMStack em, OrePrefixes prefix, String materialName, int qty) {
        addOredict(em, OreDictionary.getOreID(prefix.name() + materialName), qty);
    }

    public Map<Integer, EMFluidQuantizationInfo> getFluidQuantization() {
        return fluidQuantization;
    }

    public Map<EMItemQuantizationInfo, EMItemQuantizationInfo> getItemQuantization() {
        return itemQuantization;
    }

    public Map<Integer, EMOredictQuantizationInfo> getOredictQuantization() {
        return oredictQuantization;
    }

    public Map<IEMDefinition, EMDequantizationInfo> getInfoMap() {
        return infoMap;
    }
}
