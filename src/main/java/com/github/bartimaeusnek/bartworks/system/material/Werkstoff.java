/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.system.material;

import static net.minecraft.util.EnumChatFormatting.DARK_PURPLE;
import static net.minecraft.util.EnumChatFormatting.GREEN;

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.common.loaders.StaticRecipeChangeLoaders;
import com.github.bartimaeusnek.bartworks.system.oredict.OreDictHandler;
import com.github.bartimaeusnek.bartworks.util.*;
import com.github.bartimaeusnek.crossmod.BartWorksCrossmod;
import com.github.bartimaeusnek.crossmod.tgregworks.MaterialsInjector;
import com.github.bartimaeusnek.crossmod.thaumcraft.util.ThaumcraftHandler;
import cpw.mods.fml.common.Loader;
import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.interfaces.IColorModulationContainer;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_OreDictUnificator;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

@SuppressWarnings("ALL")
public class Werkstoff implements IColorModulationContainer, ISubTagContainer {

    public static final LinkedHashSet<Werkstoff> werkstoffHashSet = new LinkedHashSet<>();
    public static final LinkedHashMap<Short, Werkstoff> werkstoffHashMap = new LinkedHashMap<>();
    public static final LinkedHashMap<String, Werkstoff> werkstoffNameHashMap = new LinkedHashMap<>();

    public static final Map<String, String> modNameOverrides = new HashMap() {
        {
            put("GalaxySpace", DARK_PURPLE + "GalaxySpace");
        }
    };

    private static final List<String> BWModNames =
            Arrays.asList(MainMod.NAME, BartWorksCrossmod.NAME, MaterialsInjector.NAME);

    private static final HashSet<Short> idHashSet = new HashSet<>();

    private static final Werkstoff.Stats DEFAULT_NULL_STATS = new Werkstoff.Stats();
    private static final Werkstoff.GenerationFeatures DEFAULT_NULL_GENERATION_FEATURES =
            new Werkstoff.GenerationFeatures().disable();
    public static Werkstoff default_null_Werkstoff;

    private final HashSet<String> ADDITIONAL_OREDICT = new HashSet<>();
    private final List<ISubTagContainer> mOreByProducts = new ArrayList<>();
    private final LinkedHashSet<Pair<ISubTagContainer, Integer>> CONTENTS = new LinkedHashSet<>();
    private final HashSet<SubTag> SUBTAGS = new HashSet<>();
    private byte[] rgb = new byte[3];
    private final String defaultName;
    private String toolTip;

    private Werkstoff.Stats stats;
    private final Werkstoff.Types type;
    private final Werkstoff.GenerationFeatures generationFeatures;
    private final short mID;
    private final TextureSet texSet;
    private Materials bridgeMaterial;
    private final String owner;

    public Materials getBridgeMaterial() {
        return this.bridgeMaterial;
    }

    public void setBridgeMaterial(Materials bridgeMaterial) {
        this.bridgeMaterial = bridgeMaterial;
    }

    public static void init() {
        Werkstoff.default_null_Werkstoff = new Werkstoff(
                new short[3],
                "_NULL",
                "Default null Werkstoff",
                Werkstoff.DEFAULT_NULL_STATS,
                Werkstoff.Types.UNDEFINED,
                Werkstoff.DEFAULT_NULL_GENERATION_FEATURES,
                -1,
                TextureSet.SET_NONE);
    }

    /**
     * GT Materials Bridge Constructor
     * @param materials a GT Materials
     * @param generationFeatures the new Types you want to add
     * @param type - self explainatory
     * @param mID > 31_766 && <= 32_767
     */
    public Werkstoff(Materials materials, Werkstoff.GenerationFeatures generationFeatures, Types type, int mID) {
        this(
                materials.mRGBa,
                materials.mDefaultLocalName,
                materials.getToolTip(),
                type == null ? materials.mElement != null ? Types.ELEMENT : Types.UNDEFINED : type,
                generationFeatures,
                mID,
                materials.mIconSet,
                (List) materials.mOreByProducts,
                new Pair<>(materials, 1));
        if (!(mID > 31_766 && mID <= 32_767)) throw new IllegalArgumentException();
        this.stats.mass = materials.getMass();
        this.stats.protons = materials.getProtons();
        this.stats.meltingPoint = materials.mMeltingPoint;
        this.stats.neutrons = materials.getNeutrons();
        this.stats.speedOverride = materials.mToolSpeed;
        this.stats.durOverride = materials.mDurability;
        this.stats.qualityOverride = materials.mToolQuality;
        this.stats.setGas(materials.mHasGas);
        this.stats.setRadioactive(materials.isRadioactive());
        this.stats.setBlastFurnace(materials.mBlastFurnaceRequired);
        this.stats.setMeltingVoltage(120);
        if (type == Types.COMPOUND) {
            this.stats.setElektrolysis(true);
            this.generationFeatures.addChemicalRecipes();
        } else if (type == Types.MIXTURE) {
            this.stats.setCentrifuge(true);
            this.generationFeatures.addMixerRecipes();
        }
    }

    public Werkstoff(
            short[] rgba,
            String defaultName,
            Werkstoff.Types type,
            int meltingpoint,
            Werkstoff.GenerationFeatures generationFeatures,
            int mID,
            TextureSet texSet,
            Pair<ISubTagContainer, Integer>... contents) {
        this(
                rgba,
                defaultName,
                Werkstoff.Types.getDefaultStatForType(type).setMeltingPoint(meltingpoint),
                type,
                generationFeatures,
                mID,
                texSet,
                contents);
    }

    public Werkstoff(
            short[] rgba,
            String defaultName,
            Werkstoff.Types type,
            Werkstoff.GenerationFeatures generationFeatures,
            int mID,
            TextureSet texSet,
            Pair<ISubTagContainer, Integer>... contents) {
        this(
                rgba,
                defaultName,
                Werkstoff.Types.getDefaultStatForType(type),
                type,
                generationFeatures,
                mID,
                texSet,
                contents);
    }

    public Werkstoff(
            short[] rgba,
            String defaultName,
            Werkstoff.Types type,
            int meltingpoint,
            Werkstoff.GenerationFeatures generationFeatures,
            int mID,
            TextureSet texSet,
            List<ISubTagContainer> oreByProduct,
            Pair<ISubTagContainer, Integer>... contents) {
        this(
                rgba,
                defaultName,
                Werkstoff.Types.getDefaultStatForType(type).setMeltingPoint(meltingpoint),
                type,
                generationFeatures,
                mID,
                texSet,
                oreByProduct,
                contents);
    }

    public Werkstoff(
            short[] rgba,
            String defaultName,
            Werkstoff.Types type,
            Werkstoff.GenerationFeatures generationFeatures,
            int mID,
            TextureSet texSet,
            List<ISubTagContainer> oreByProduct,
            Pair<ISubTagContainer, Integer>... contents) {
        this(
                rgba,
                defaultName,
                Werkstoff.Types.getDefaultStatForType(type),
                type,
                generationFeatures,
                mID,
                texSet,
                oreByProduct,
                contents);
    }

    public Werkstoff(
            short[] rgba,
            String toolTip,
            String defaultName,
            Werkstoff.Types type,
            Werkstoff.GenerationFeatures generationFeatures,
            int mID,
            TextureSet texSet,
            List<ISubTagContainer> oreByProduct,
            Pair<ISubTagContainer, Integer>... contents) {
        this(
                rgba,
                toolTip,
                defaultName,
                Werkstoff.Types.getDefaultStatForType(type),
                type,
                generationFeatures,
                mID,
                texSet,
                oreByProduct,
                contents);
    }

    public Werkstoff(
            short[] rgba,
            String defaultName,
            Werkstoff.Stats stats,
            Werkstoff.Types type,
            Werkstoff.GenerationFeatures generationFeatures,
            int mID,
            TextureSet texSet,
            List<ISubTagContainer> oreByProduct,
            Pair<ISubTagContainer, Integer>... contents) {
        this(rgba, defaultName, "", stats, type, generationFeatures, mID, texSet, contents);
        this.mOreByProducts.clear();
        this.mOreByProducts.addAll(oreByProduct);
    }

    public Werkstoff(
            short[] rgba,
            String defaultName,
            Werkstoff.Stats stats,
            Werkstoff.Types type,
            Werkstoff.GenerationFeatures generationFeatures,
            int mID,
            TextureSet texSet,
            Pair<ISubTagContainer, Integer>... contents) {
        this(rgba, defaultName, "", stats, type, generationFeatures, mID, texSet, contents);
    }

    public Werkstoff(
            short[] rgba,
            String defaultName,
            String toolTip,
            Werkstoff.Stats stats,
            Werkstoff.Types type,
            Werkstoff.GenerationFeatures generationFeatures,
            int mID,
            TextureSet texSet,
            List<ISubTagContainer> oreByProduct,
            Pair<ISubTagContainer, Integer>... contents) {
        this(rgba, defaultName, toolTip, stats, type, generationFeatures, mID, texSet, contents);
        this.mOreByProducts.clear();
        this.mOreByProducts.addAll(oreByProduct);
    }

    public Werkstoff(
            short[] rgba,
            String defaultName,
            String toolTip,
            Werkstoff.Stats stats,
            Werkstoff.Types type,
            Werkstoff.GenerationFeatures generationFeatures,
            int mID,
            TextureSet texSet,
            Pair<ISubTagContainer, Integer>... contents) {

        if (Werkstoff.idHashSet.contains((short) mID))
            throw new UnsupportedOperationException("ID (" + mID + ") is already in use!");
        Werkstoff.idHashSet.add((short) mID);
        if (type == null) type = Werkstoff.Types.UNDEFINED;

        this.mID = (short) mID;
        this.defaultName = defaultName;
        // Ensure that localization key are written to the lang file
        GregTech_API.sAfterGTPreload.add(() -> {
            this.getLocalizedName();
        });
        this.stats = stats;
        this.type = type;
        this.generationFeatures = generationFeatures;
        this.setRgb(BW_ColorUtil.correctCorlorArray(rgba));
        this.CONTENTS.addAll(Arrays.asList(contents));
        this.toolTip = "";
        if (toolTip.isEmpty()) {
            for (Pair<ISubTagContainer, Integer> p : contents) {
                if (contents.length > 1) {
                    if (p.getKey() instanceof Materials) {
                        if (((Materials) p.getKey()).mMaterialList.size() > 1 && p.getValue() > 1)
                            this.toolTip += "(" + getFormula((Materials) p.getKey()) + ")"
                                    + (BW_Util.subscriptNumber(p.getValue()));
                        else
                            this.toolTip += getFormula((Materials) p.getKey())
                                    + (p.getValue() > 1 ? BW_Util.subscriptNumber(p.getValue()) : "");
                    }
                    if (p.getKey() instanceof Werkstoff) {
                        if (((Werkstoff) p.getKey()).CONTENTS.size() > 1 && p.getValue() > 1)
                            this.toolTip += "(" + getFormula((Werkstoff) p.getKey()) + ")"
                                    + (BW_Util.subscriptNumber(p.getValue()));
                        else
                            this.toolTip += getFormula((Werkstoff) p.getKey())
                                    + (p.getValue() > 1 ? BW_Util.subscriptNumber(p.getValue()) : "");
                    }
                } else {
                    if (p.getKey() instanceof Materials) {
                        this.toolTip += getFormula((Materials) p.getKey())
                                + (p.getValue() > 1 ? BW_Util.subscriptNumber(p.getValue()) : "");
                    } else if (p.getKey() instanceof Werkstoff)
                        this.toolTip += getFormula((Werkstoff) p.getKey())
                                + (p.getValue() > 1 ? BW_Util.subscriptNumber(p.getValue()) : "");
                }
            }
        } else this.toolTip = toolTip;

        //        if (this.toolTip.length() > 25)
        //            this.toolTip = "The formula is to long...";

        // Ensure that localization key are written to the lang file
        GregTech_API.sAfterGTPreload.add(() -> {
            this.getLocalizedToolTip();
        });

        if (this.stats.protons == 0) {
            long tmpprotons = 0;
            for (Pair<ISubTagContainer, Integer> p : contents) {
                if (p.getKey() instanceof Materials) {
                    tmpprotons += ((Materials) p.getKey()).getProtons() * p.getValue();
                } else if (p.getKey() instanceof Werkstoff) {
                    tmpprotons += ((Werkstoff) p.getKey()).getStats().protons * p.getValue();
                }
            }
            this.stats = stats.setProtons(tmpprotons);
        }
        if (this.stats.mass == 0) {
            long tmpmass = 0;
            int count = 0;
            for (Pair<ISubTagContainer, Integer> p : contents) {
                if (p.getKey() instanceof Materials) {
                    tmpmass += ((Materials) p.getKey()).getMass() * p.getValue();
                    count += p.getValue();
                } else if (p.getKey() instanceof Werkstoff) {
                    tmpmass += ((Werkstoff) p.getKey()).getStats().mass * p.getValue();
                    count += p.getValue();
                }
            }
            if (count > 0) this.stats = stats.setMass(tmpmass / count);
        }

        if (this.stats.meltingPoint == 0) this.stats.meltingPoint = 1123;

        if (this.stats.meltingVoltage == 0) this.stats.meltingVoltage = 120;

        this.texSet = texSet;

        switch (this.mOreByProducts.size()) {
            case 0:
                this.mOreByProducts.add(this);
                this.mOreByProducts.add(this);
                this.mOreByProducts.add(this);
                break;
            case 1:
                this.mOreByProducts.add(this);
                this.mOreByProducts.add(this);
                break;
            case 2:
                this.mOreByProducts.add(this);
                break;
        }

        Optional<Pair<ISubTagContainer, Integer>> firstContent;
        if (this.CONTENTS.size() == 1 && (firstContent = this.CONTENTS.stream().findFirst()).isPresent()) {
            ISubTagContainer firstContentSubTagContainer = firstContent.get().getKey();
            if (firstContent.get().getValue() == 1 && firstContentSubTagContainer instanceof Materials)
                this.getGenerationFeatures().setExtension();
        }

        Werkstoff.werkstoffHashSet.add(this);
        Werkstoff.werkstoffHashMap.put(this.mID, this);
        Werkstoff.werkstoffNameHashMap.put(this.defaultName, this);

        this.owner = getMaterialOwner();
    }

    private static String getFormula(Materials material) {
        return material.mChemicalFormula.isEmpty() ? "?" : material.mChemicalFormula;
    }

    private static String getFormula(Werkstoff material) {
        return material.toolTip.isEmpty() ? "?" : material.toolTip;
    }

    public Werkstoff addAdditionalOreDict(String s) {
        ADDITIONAL_OREDICT.add(s);
        return this;
    }

    public HashSet<String> getADDITIONAL_OREDICT() {
        return ADDITIONAL_OREDICT;
    }

    public void setTCAspects(Pair<Object, Integer>... pAspectsArr) {
        this.stats.mTC_Aspects = pAspectsArr;
    }

    public Pair<Object, Integer>[] getTCAspects(int ratio) {
        if (this.stats.mTC_Aspects == null) {
            HashSet<TC_Aspects.TC_AspectStack> tc_aspectStacks = new HashSet<>();
            HashSet<Pair<Object, Integer>> set = new HashSet<>();
            for (Pair p : this.getContents().getValue()) {
                if (p.getKey() instanceof Materials) tc_aspectStacks.addAll(((Materials) p.getKey()).mAspects);
                if (p.getKey() instanceof Werkstoff) set.addAll(Arrays.asList(((Werkstoff) p.getKey()).getTCAspects()));
            }
            tc_aspectStacks.forEach(tc_aspectStack ->
                    set.add(new Pair<>(tc_aspectStack.mAspect.mAspect, (int) tc_aspectStack.mAmount)));
            this.stats.mTC_Aspects = set.toArray(new Pair[0]);
        }
        Pair<Object, Integer>[] ret = this.stats.mTC_Aspects.clone();
        for (int i = 0; i < ret.length; i++) {
            ret[i] = ret[i].copyWithNewValue(ret[i].getValue() * ratio);
        }
        return ret;
    }

    public List<TC_Aspects.TC_AspectStack> getGTWrappedTCAspects() {
        final List<TC_Aspects.TC_AspectStack> ret = new ArrayList<>();
        Arrays.stream(getTCAspects()).forEach(objectIntegerPair -> {
            try {
                new TC_Aspects.TC_AspectStack(
                                TC_Aspects.valueOf(((String) ThaumcraftHandler.AspectAdder.getName.invoke(
                                                objectIntegerPair.getKey()))
                                        .toUpperCase(Locale.US)),
                                objectIntegerPair.getValue())
                        .addToAspectList(ret);
            } catch (IllegalAccessException | InvocationTargetException ignored) {
            }
        });
        return ret;
    }

    public Pair<Object, Integer>[] getTCAspects() {
        return this.getTCAspects(1);
    }

    public Werkstoff.Types getType() {
        return this.type;
    }

    public boolean containsStuff(ISubTagContainer stuff) {
        for (Pair<ISubTagContainer, Integer> pair : this.CONTENTS) {
            if (pair.getKey().equals(stuff)) return true;
        }
        return false;
    }

    public Pair<Integer, LinkedHashSet<Pair<ISubTagContainer, Integer>>> getContents() {
        int ret = 0;
        switch (this.type) {
            case COMPOUND:
            case MIXTURE:
            case BIOLOGICAL: {
                for (int i = 0; i < this.CONTENTS.toArray().length; i++) {
                    ret += ((Pair<ISubTagContainer, Integer>) this.CONTENTS.toArray()[i]).getValue();
                }
                break;
            }
            default:
                ret = 1;
                break;
        }
        return new Pair<>(ret, this.CONTENTS);
    }

    public int getNoOfByProducts() {
        return this.mOreByProducts.size();
    }

    public ISubTagContainer getOreByProductRaw(int aNumber) {
        if (this.mOreByProducts.size() == 0) return null;
        if (aNumber < 0) aNumber = this.mOreByProducts.size() + aNumber;
        while (aNumber >= this.mOreByProducts.size()) aNumber--;
        ISubTagContainer o = this.mOreByProducts.get(aNumber);
        if (o == null || o.equals(Werkstoff.default_null_Werkstoff) || o.equals(Materials._NULL)) return this;
        return o;
    }

    public ItemStack getOreByProduct(int aNumber, OrePrefixes prefixes) {
        if (this.mOreByProducts.size() == 0) return null;
        if (aNumber < 0) aNumber = this.mOreByProducts.size() + aNumber;
        while (aNumber >= this.mOreByProducts.size()) aNumber--;
        Object o = this.mOreByProducts.get(aNumber);
        if (o == null || o.equals(Werkstoff.default_null_Werkstoff) || o.equals(Materials._NULL))
            return this.get(prefixes);
        if (o instanceof Werkstoff) return WerkstoffLoader.getCorrespondingItemStack(prefixes, (Werkstoff) o);
        if (o instanceof Materials) return GT_OreDictUnificator.get(prefixes, o, 1L);
        return null;
    }

    public String getDefaultName() {
        return this.defaultName;
    }

    public String getLocalizedName() {
        return GT_LanguageManager.addStringLocalization(
                String.format("bw.werkstoff.%05d.name", this.mID), defaultName, !GregTech_API.sPostloadFinished);
    }

    public String getVarName() {
        return this.defaultName.replaceAll(" ", "");
    }

    public String getToolTip() {
        return this.toolTip;
    }

    public String getLocalizedToolTip() {
        return GT_LanguageManager.addStringLocalization(
                String.format("bw.werkstoff.%05d.tooltip", this.mID), toolTip, !GregTech_API.sPostloadFinished);
    }

    public Werkstoff.Stats getStats() {
        return this.stats;
    }

    public short getmID() {
        return this.mID;
    }

    public short getMixCircuit() {
        return this.getGenerationFeatures().mixCircuit;
    }

    public Werkstoff.GenerationFeatures getGenerationFeatures() {
        return this.generationFeatures;
    }

    public TextureSet getTexSet() {
        return this.texSet;
    }

    public void setRgb(short[] rgb) {
        this.rgb = new byte[] {(byte) (rgb[0] - 128), (byte) (rgb[1] - 128), (byte) (rgb[2] - 128)};
    }

    @Override
    public short[] getRGBA() {
        return new short[] {(short) (this.rgb[0] + 128), (short) (this.rgb[1] + 128), (short) (this.rgb[2] + 128), 0};
    }

    @Override
    public boolean contains(SubTag subTag) {
        if (!subTag.equals(WerkstoffLoader.NOBLE_GAS)
                && !subTag.equals(WerkstoffLoader.ANAEROBE_GAS)
                && !subTag.equals(WerkstoffLoader.NO_BLAST))
            for (Pair<ISubTagContainer, Integer> p : this.CONTENTS)
                if (p.getKey().contains(subTag)) return true;
        return this.SUBTAGS.contains(subTag);
    }

    @Override
    public ISubTagContainer add(SubTag... subTags) {
        this.SUBTAGS.addAll(Arrays.asList(subTags));
        return this;
    }

    @Override
    public boolean remove(SubTag subTag) {
        return this.SUBTAGS.remove(subTag);
    }

    public void getAndAddToCollection(OrePrefixes prefixes, int amount, Collection<ItemStack> stacks) {
        stacks.add(this.get(prefixes, amount));
    }

    public ItemStack get(OrePrefixes prefixes) {
        return WerkstoffLoader.getCorrespondingItemStack(prefixes, this);
    }

    public FluidStack getFluidOrGas(int fluidAmount) {
        return new FluidStack(Objects.requireNonNull(WerkstoffLoader.fluids.get(this)), fluidAmount);
    }

    public FluidStack getMolten(int fluidAmount) {
        return new FluidStack(Objects.requireNonNull(WerkstoffLoader.molten.get(this)), fluidAmount);
    }

    public ItemStack get(OrePrefixes prefixes, int amount) {
        return WerkstoffLoader.getCorrespondingItemStack(prefixes, this, amount);
    }

    public byte getToolQuality() {
        return this.stats.getQualityOverride() > 0
                ? this.stats.getQualityOverride()
                : (byte) ((15f
                                * (((float) this.getStats().getProtons() / 188f)
                                        + (float) this.getStats().getMeltingPoint() / 10801f))
                        / (float) this.getContents().getKey());
    }

    public float getToolSpeed() {
        return this.stats.getSpeedOverride() > 0f
                ? this.stats.getSpeedOverride()
                : Math.max(
                        1f,
                        2f
                                * ((float) -this.getStats().getMass()
                                        + 0.1f * (float) this.getStats().getMeltingPoint()
                                        + (float) this.getStats().getProtons())
                                * 0.1f
                                / (float) this.getContents().getKey()
                                * 0.1f
                                * (float) this.getToolQuality());
    }

    public int getDurability() {
        return this.stats.getDurOverride() > 0
                ? this.stats.getDurOverride()
                : (int) (this.stats.durMod
                        * ((0.01f
                                        * (float) this.getStats().getMeltingPoint()
                                        * (float) this.getStats().getMass())
                                / (float) this.getContents().getKey()));
    }

    /**
     * Checks if the generation feature is enabled and if its not in the blacklist
     */
    public boolean hasItemType(OrePrefixes prefixes) {
        int unpacked = Werkstoff.GenerationFeatures.getPrefixDataRaw(prefixes);
        return (this.getGenerationFeatures().toGenerate & unpacked) != 0
                && (this.getGenerationFeatures().blacklist & unpacked) == 0;
    }

    /**
     *  DOES NOT CHECK BLACKLIST!
     */
    public boolean hasGenerationFeature(OrePrefixes prefixes) {
        int unpacked = Werkstoff.GenerationFeatures.getPrefixDataRaw(prefixes);
        return (this.getGenerationFeatures().toGenerate & unpacked) != 0;
    }

    /**
     * Checks if the Actual Stack exists in the OreDict
     */
    public boolean doesOreDictedItemExists(OrePrefixes prefixes) {
        return OreDictHandler.getItemStack(this.getDefaultName(), prefixes, 1) != null;
    }

    public String getOwner() {
        return owner;
    }

    private String getMaterialOwner() {
        String modName = Loader.instance().activeModContainer().getName();
        if (modNameOverrides.get(modName) != null) {
            return modNameOverrides.get(modName);
        }
        if (BWModNames.contains(modName)) {
            return null;
        }
        return GREEN + modName;
    }

    public enum Types {
        MATERIAL,
        COMPOUND,
        MIXTURE,
        BIOLOGICAL,
        ELEMENT,
        ISOTOPE,
        UNDEFINED;

        public static Werkstoff.Stats getDefaultStatForType(Werkstoff.Types T) {
            switch (T) {
                case COMPOUND:
                case BIOLOGICAL:
                    return new Werkstoff.Stats().setElektrolysis(true);
                case MIXTURE:
                    return new Werkstoff.Stats().setCentrifuge(true);
                default:
                    return new Werkstoff.Stats();
            }
        }
    }

    public static class GenerationFeatures {
        public static final GenerationFeatures DISABLED = new GenerationFeatures().disable();
        long toGenerate = 0b0001001;
        // logic gate shit
        /*
        dust 1
        metal 10 (ingot, nugget)
        gem 100
        ore 1000
        cell 10000
        plasma 100000
        molten 1000000
        crafting metal 10000000 (sticks, plates)
        meta crafting metal 100000000 (gears, screws, bolts, springs)
        multiple ingotWorth stuff 1000000000 (double, triple, quadruple, ingot/plates)
         */
        private boolean isExtension;
        private static final NonNullWrappedHashMap<OrePrefixes, Integer> prefixLogic = new NonNullWrappedHashMap<>(0);

        public GenerationFeatures() {}

        public static void initPrefixLogic() {
            Arrays.stream(OrePrefixes.values()).forEach(e -> prefixLogic.put(e, 0));
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.dust, 0b1);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.dustTiny, 0b1);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.dustSmall, 0b1);

            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.ingot, 0b10);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.ingotHot, 0b10);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.nugget, 0b10);

            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.gem, 0b100);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.gemFlawed, 0b100);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.gemExquisite, 0b100);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.gemChipped, 0b100);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.gemFlawless, 0b100);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.lens, 0b100);

            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.block, 0b110);

            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.ore, 0b1000);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.dustImpure, 0b1000);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.dustPure, 0b1000);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.crushed, 0b1000);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.crushedPurified, 0b1000);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.crushedCentrifuged, 0b1000);

            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.cell, 0b10000);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.capsule, 0b10000);
            // Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.bottle,0b10000);

            Werkstoff.GenerationFeatures.prefixLogic.put(WerkstoffLoader.capsuleMolten, 0b1000000);
            Werkstoff.GenerationFeatures.prefixLogic.put(WerkstoffLoader.cellMolten, 0b1000000);

            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.plate, 0b10000000);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.foil, 0b10000000);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.stick, 0b10000000);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.stickLong, 0b10000000);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.toolHeadHammer, 0b10000000);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.toolHeadWrench, 0b10000000);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.toolHeadSaw, 0b10000000);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.turbineBlade, 0b10000000);

            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.screw, 0b100000000);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.gearGt, 0b100000000);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.gearGtSmall, 0b100000000);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.bolt, 0b100000000);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.ring, 0b100000000);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.spring, 0b100000000);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.springSmall, 0b100000000);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.rotor, 0b100000000);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.wireFine, 0b100000000);

            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.plateDouble, 0x200);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.plateTriple, 0x200);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.plateQuadruple, 0x200);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.plateQuintuple, 0x200);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.plateDense, 0x200);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.ingotDouble, 0x200);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.ingotTriple, 0x200);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.ingotQuadruple, 0x200);
            Werkstoff.GenerationFeatures.prefixLogic.put(OrePrefixes.ingotQuintuple, 0x200);

            Werkstoff.GenerationFeatures.prefixLogic.put(WerkstoffLoader.blockCasing, 0x380);
            Werkstoff.GenerationFeatures.prefixLogic.put(WerkstoffLoader.blockCasingAdvanced, 0x380);
        }

        public void setExtension() {
            isExtension = !isExtension;
        }

        public static int getPrefixDataRaw(OrePrefixes prefixes) {
            if (prefixes == null) throw new IllegalArgumentException("OrePrefixes is NULL!");
            return GenerationFeatures.prefixLogic.get(prefixes);
        }

        public boolean isExtension() {
            return isExtension;
        }

        // public byte toGenerateSecondary = 0b0000000;
        public byte blacklist;

        public boolean enforceUnification;

        /*
         * Auto add Chemical Recipes 1
         * Auto add mixer Recipes 10
         * Auto add Sifter Recipe 100
         * Auto add MetalWorking(sticks, plates) Recipe 1000
         * Auto add MetalWorking(crafting components) Recipe 10000
         */
        public byte extraRecipes;

        /*
         * Here so that new recipes don't fuck with existing functionality
         * Auto add Crafting Metal Solidifier recipes 1
         * Auto add Meta Crafting Metal Solidifier recipes 10
         * Auto add Multiple Ingot Metal Solidifier recipes 100 (Unused)
         */
        public byte extraRecipes2;

        public short mixCircuit = -1;

        public Werkstoff.GenerationFeatures setBlacklist(OrePrefixes p) {
            this.blacklist |= getPrefixDataRaw(p);
            return this;
        }

        @Deprecated
        public boolean hasDusts() {
            return (this.toGenerate & 0b1) != 0;
        }

        @Deprecated
        public boolean hasGems() {
            return (this.toGenerate & 0b100) != 0;
        }

        @Deprecated
        public boolean hasOres() {
            return (this.toGenerate & 0b1000) != 0;
        }

        public Werkstoff.GenerationFeatures enforceUnification() {
            this.enforceUnification = true;
            return this;
        }

        @Deprecated
        public Werkstoff.GenerationFeatures removeGems() {
            if (this.hasGems()) this.toGenerate = (long) (this.toGenerate ^ 0b100);
            return this;
        }

        @Deprecated
        public Werkstoff.GenerationFeatures removeDusts() {
            if (this.hasDusts()) this.toGenerate = (long) (this.toGenerate ^ 0b1);
            return this;
        }

        @Deprecated
        public Werkstoff.GenerationFeatures removeOres() {
            if (this.hasOres()) this.toGenerate = (long) (this.toGenerate ^ 0b1000);
            return this;
        }

        public Werkstoff.GenerationFeatures addChemicalRecipes() {
            this.extraRecipes = (byte) (this.extraRecipes | 1);
            return this;
        }

        public boolean hasChemicalRecipes() {
            return (this.extraRecipes & 1) != 0;
        }

        public Werkstoff.GenerationFeatures addMetalCraftingSolidifierRecipes() {
            this.extraRecipes2 = (byte) (this.extraRecipes2 | 1);
            return this;
        }

        public boolean hasMetalCraftingSolidifierRecipes() {
            return (this.extraRecipes2 & 1) != 0;
        }

        public Werkstoff.GenerationFeatures addMetaSolidifierRecipes() {
            this.extraRecipes2 = (byte) (this.extraRecipes2 | 10);
            return this;
        }

        public boolean hasMetaSolidifierRecipes() {
            return (this.extraRecipes2 & 10) != 0;
        }

        public Werkstoff.GenerationFeatures addMultipleMetalSolidifierRecipes() {
            this.extraRecipes2 = (byte) (this.extraRecipes2 | 100);
            return this;
        }

        public boolean hasMultipleMetalSolidifierRecipes() {
            return (this.extraRecipes2 & 100) != 0;
        }

        public Werkstoff.GenerationFeatures addMixerRecipes() {
            this.extraRecipes = (byte) (this.extraRecipes | 10);
            return this;
        }

        public Werkstoff.GenerationFeatures addMixerRecipes(short aCircuit) {
            this.extraRecipes = (byte) (this.extraRecipes | 10);
            if (aCircuit >= 1 && aCircuit <= 24) this.mixCircuit = aCircuit;
            return this;
        }

        public boolean hasMixerRecipes() {
            return (this.extraRecipes & 10) != 0;
        }

        public Werkstoff.GenerationFeatures addSifterRecipes() {
            this.extraRecipes = (byte) (this.extraRecipes | 100);
            return this;
        }

        public boolean hasSifterRecipes() {
            return (this.extraRecipes & 100) != 0;
        }

        public Werkstoff.GenerationFeatures onlyDust() {
            this.toGenerate = (long) (0b1);
            return this;
        }

        /**
         * Automatically adds Simple Metal Working Items
         */
        public Werkstoff.GenerationFeatures addMetalItems() {
            this.toGenerate = (long) (this.addSimpleMetalWorkingItems().toGenerate | 0b10);
            return this;
        }

        public Werkstoff.GenerationFeatures disable() {
            this.toGenerate = (long) (0);
            return this;
        }

        public Werkstoff.GenerationFeatures addCells() {
            this.toGenerate = (long) (this.toGenerate | 0b10000);
            return this;
        }

        @Deprecated
        public boolean hasCells() {
            return (this.toGenerate & 0b10000) != 0;
        }

        @Deprecated
        public boolean hasMolten() {
            return (this.toGenerate & 0b1000000) != 0;
        }

        public Werkstoff.GenerationFeatures addMolten() {
            this.toGenerate = (long) (this.toGenerate | 0b1000000);
            return this;
        }

        /**
         * Automatically adds Simple Metal Working Items
         */
        public Werkstoff.GenerationFeatures addGems() {
            this.toGenerate = (long) (this.addSimpleMetalWorkingItems().toGenerate | 0x4);
            return this;
        }

        public Werkstoff.GenerationFeatures addSimpleMetalWorkingItems() {
            this.toGenerate = (long) (this.toGenerate | 0b10000000);
            return this;
        }

        public Werkstoff.GenerationFeatures addCasings() {
            this.toGenerate = (long) (this.toGenerate | 0x382);
            return this;
        }

        @Deprecated
        public boolean hasSimpleMetalWorkingItems() {
            return (this.toGenerate & 0b10000000) != 0;
        }

        public Werkstoff.GenerationFeatures addCraftingMetalWorkingItems() {
            this.toGenerate = (long) (this.toGenerate | 0x100);
            return this;
        }

        public Werkstoff.GenerationFeatures addMultipleIngotMetalWorkingItems() {
            this.toGenerate = (long) (this.toGenerate | 0x200);
            return this;
        }

        public Werkstoff.GenerationFeatures addPrefix(OrePrefixes prefixes) {
            this.toGenerate = (long) (this.toGenerate | this.getPrefixDataRaw(prefixes));
            return this;
        }

        public Werkstoff.GenerationFeatures removePrefix(OrePrefixes prefixes) {
            this.toGenerate = (long) (this.toGenerate ^ this.getPrefixDataRaw(prefixes));
            return this;
        }
    }

    public static class Stats {

        public static final int NULL_KELVIN = 0;

        int boilingPoint;

        public int getBoilingPoint() {
            return this.boilingPoint;
        }

        public Werkstoff.Stats setBoilingPoint(int boilingPoint) {
            this.boilingPoint = boilingPoint;
            return this;
        }

        public long getMass() {
            return this.mass;
        }

        public long getProtons() {
            return this.protons;
        }

        public int getMeltingPoint() {
            return this.meltingPoint;
        }

        public Werkstoff.Stats setMeltingPoint(int meltingPoint) {
            this.meltingPoint = meltingPoint;
            return this;
        }

        public double getEbfGasRecipeTimeMultiplier() {
            return this.ebfGasRecipeTimeMultiplier;
        }

        /**
         * The generated EBF recipes using this gas will have their duration multiplied by this number.
         * If set to a negative value, the default proton count-based logic is used.
         * For GT Materials gases, add the overrides to {@link StaticRecipeChangeLoaders#addEBFGasRecipes()}
         */
        public Werkstoff.Stats setEbfGasRecipeTimeMultiplier(double timeMultiplier) {
            this.ebfGasRecipeTimeMultiplier = timeMultiplier;
            return this;
        }

        public double getEbfGasRecipeConsumedAmountMultiplier() {
            return this.ebfGasRecipeConsumedAmountMultiplier;
        }

        /**
         * The generated EBF recipes using this gas will have the amount of gas consumed multiplied by this number.
         * For GT Materials gases, add the overrides to {@link StaticRecipeChangeLoaders#addEBFGasRecipes()}
         */
        public Werkstoff.Stats setEbfGasRecipeConsumedAmountMultiplier(double amountMultiplier) {
            this.ebfGasRecipeConsumedAmountMultiplier = amountMultiplier;
            return this;
        }

        public int getDurOverride() {
            return durOverride;
        }

        public Werkstoff.Stats setDurOverride(int durOverride) {
            this.durOverride = durOverride;
            return this;
        }

        public float getSpeedOverride() {
            return speedOverride;
        }

        public Werkstoff.Stats setSpeedOverride(float speedOverride) {
            this.speedOverride = speedOverride;
            return this;
        }

        public byte getQualityOverride() {
            return qualityOverride;
        }

        public Werkstoff.Stats setQualityOverride(byte qualityOverride) {
            this.qualityOverride = qualityOverride;
            return this;
        }

        private byte qualityOverride;
        private int durOverride;
        private float speedOverride;
        private int meltingPoint;
        private int meltingVoltage;
        private long protons;
        private long neutrons;
        private long electrons;
        private long mass;
        private double ebfGasRecipeTimeMultiplier = -1.0;
        private double ebfGasRecipeConsumedAmountMultiplier = 1.0;

        float durMod = 1f;

        public float getDurMod() {
            return durMod;
        }

        public void setDurMod(float durMod) {
            this.durMod = durMod;
        }

        private Pair<Object, Integer>[] mTC_Aspects;
        // logic gate shit
        byte quality = ~0b1111111;

        public Werkstoff.Stats setmTC_AspectsArray(Pair<Object, Integer>[] mTC_Aspects) {
            this.mTC_Aspects = mTC_Aspects;
            return this;
        }

        @SafeVarargs
        public final Werkstoff.Stats setmTC_AspectsVarArg(Pair<Object, Integer>... mTC_Aspects) {
            this.mTC_Aspects = mTC_Aspects;
            return this;
        }

        Pair<Object, Integer>[] getmTC_Aspects() {
            return this.mTC_Aspects;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Werkstoff.Stats)) return false;

            Werkstoff.Stats that = (Werkstoff.Stats) o;

            if (this.boilingPoint != that.boilingPoint) return false;
            if (this.meltingPoint != that.meltingPoint) return false;
            if (this.mass != that.mass) return false;
            if (this.protons != that.protons) return false;
            if (this.neutrons != that.neutrons) return false;
            if (this.electrons != that.electrons) return false;
            if (Math.abs(this.ebfGasRecipeTimeMultiplier - that.ebfGasRecipeTimeMultiplier) > 1.0e-6D) return false;
            if (Math.abs(this.ebfGasRecipeConsumedAmountMultiplier - that.ebfGasRecipeConsumedAmountMultiplier)
                    > 1.0e-6D) return false;
            return this.quality == that.quality;
        }

        @Override
        public int hashCode() {
            return MurmurHash3.murmurhash3_x86_32(
                    ByteBuffer.allocate(49)
                            .put(this.quality)
                            .putInt(this.boilingPoint)
                            .putInt(this.meltingPoint)
                            .putLong(this.protons)
                            .putLong(this.neutrons)
                            .putLong(this.electrons)
                            .putLong(this.mass)
                            .putDouble(this.ebfGasRecipeTimeMultiplier)
                            .putDouble(this.ebfGasRecipeConsumedAmountMultiplier)
                            .array(),
                    0,
                    49,
                    31);
        }

        public Werkstoff.Stats setMass(long mass) {
            this.mass = mass;
            return this;
        }

        public Werkstoff.Stats setProtons(long protons) {
            this.protons = protons;
            return this;
        }

        public boolean isSublimation() {
            return (this.quality & 0b1) != 0;
        }

        public Werkstoff.Stats setSublimation(boolean sublimation) {
            if (sublimation) this.quality = (byte) (this.quality | 0b000001);
            else this.quality = (byte) (this.quality & 0b1111110);
            return this;
        }

        public boolean isToxic() {
            return (this.quality & 0b10) != 0;
        }

        public Werkstoff.Stats setToxic(boolean toxic) {
            if (toxic) this.quality = (byte) (this.quality | 0b000010);
            else this.quality = (byte) (this.quality & 0b1111101);
            return this;
        }

        byte enchantmentlvl = 3;

        public byte getEnchantmentlvl() {
            return enchantmentlvl;
        }

        public Werkstoff.Stats setEnchantmentlvl(byte enchantmentlvl) {
            this.enchantmentlvl = enchantmentlvl;
            return this;
        }

        public boolean isRadioactive() {
            return (this.quality & 0b100) != 0;
        }

        public Werkstoff.Stats setRadioactive(boolean radioactive) {
            if (radioactive) this.quality = (byte) (this.quality | 0b000100);
            else this.quality = (byte) (this.quality & 0b1111011);
            return this;
        }

        public boolean isBlastFurnace() {
            return (this.quality & 0b1000) != 0;
        }

        public Werkstoff.Stats setBlastFurnace(boolean blastFurnace) {
            if (blastFurnace) this.quality = (byte) (this.quality | 0b001000);
            else this.quality = (byte) (this.quality & 0b1110111);
            return this;
        }

        public Werkstoff.Stats setMeltingVoltage(int meltingVoltage) {
            this.meltingVoltage = meltingVoltage;
            return this;
        }

        public int getMeltingVoltage() {
            return meltingVoltage;
        }

        public boolean isElektrolysis() {
            return (this.quality & 0x10) != 0;
        }

        public Werkstoff.Stats setElektrolysis(boolean elektrolysis) {
            if (elektrolysis) this.quality = (byte) (this.quality | 0x10);
            else this.quality = (byte) (this.quality & 0b1101111);
            return this;
        }

        public boolean isCentrifuge() {
            return (this.quality & 0x20) != 0;
        }

        public Werkstoff.Stats setCentrifuge(boolean centrifuge) {
            if (centrifuge) this.quality = (byte) (this.quality | 0x20);
            else this.quality = (byte) (this.quality & 0b1011111);
            return this;
        }

        public boolean isGas() {
            return (this.quality & 0x40) != 0;
        }

        public FluidState getFluidState() {
            if ((this.quality & 0x40) != 0) {
                return FluidState.GAS;
            } else {
                return FluidState.LIQUID;
            }
        }

        public Werkstoff.Stats setGas(boolean gas) {
            if (gas) this.quality = (byte) (this.quality | 0x40);
            else this.quality = (byte) (this.quality & 0b0111111);
            return this;
        }
    }
}
