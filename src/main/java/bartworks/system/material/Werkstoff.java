/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bartworks.system.material;

import static gregtech.api.enums.Mods.GalaxySpace;
import static net.minecraft.util.EnumChatFormatting.DARK_PURPLE;
import static net.minecraft.util.EnumChatFormatting.GREEN;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import bartworks.MainMod;
import bartworks.system.oredict.OreDictHandler;
import bartworks.util.BWColorUtil;
import bartworks.util.BWUtil;
import bartworks.util.MurmurHash3;
import bwcrossmod.BartWorksCrossmod;
import bwcrossmod.tgregworks.MaterialsInjector;
import cpw.mods.fml.common.Loader;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.FluidState;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.StoneType;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TCAspects;
import gregtech.api.enums.TextureSet;
import gregtech.api.interfaces.IColorModulationContainer;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.interfaces.IStoneType;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import thaumcraft.api.aspects.Aspect;

public class Werkstoff implements IColorModulationContainer, ISubTagContainer, IOreMaterial {

    public static final LinkedHashSet<Werkstoff> werkstoffHashSet = new LinkedHashSet<>();
    public static final Short2ObjectMap<Werkstoff> werkstoffHashMap = new Short2ObjectLinkedOpenHashMap<>();
    public static final LinkedHashMap<String, Werkstoff> werkstoffNameHashMap = new LinkedHashMap<>();
    public static final LinkedHashMap<String, Werkstoff> werkstoffVarNameHashMap = new LinkedHashMap<>();

    public static final Map<String, String> modNameOverrides = new HashMap<>() {

        private static final long serialVersionUID = 6399917619058898648L;

        {
            this.put(GalaxySpace.ID, DARK_PURPLE + "GalaxySpace");
        }
    };

    private static final List<String> BWModNames = Arrays
        .asList(MainMod.NAME, BartWorksCrossmod.NAME, MaterialsInjector.NAME);

    private static final HashSet<Short> idHashSet = new HashSet<>();

    private static final Werkstoff.Stats DEFAULT_NULL_STATS = new Werkstoff.Stats();
    private static final Werkstoff.GenerationFeatures DEFAULT_NULL_GENERATION_FEATURES = new Werkstoff.GenerationFeatures()
        .disable();
    public static Werkstoff default_null_Werkstoff;

    private final HashSet<String> additionalOredict = new HashSet<>();
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

    @Override
    public @Nullable Materials getGTMaterial() {
        return bridgeMaterial;
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
     *
     * @param materials          a GT Materials
     * @param generationFeatures the new Types you want to add
     * @param type               - self explainatory
     * @param mID                > 31_766 && <= 32_767
     */
    public Werkstoff(Materials materials, Werkstoff.GenerationFeatures generationFeatures, Types type, int mID) {
        this(
            materials.mRGBa,
            materials.mDefaultLocalName,
            materials.getChemicalTooltip(),
            type == null ? materials.mElement != null ? Types.ELEMENT : Types.UNDEFINED : type,
            generationFeatures,
            mID,
            materials.mIconSet,
            (List) materials.mOreByProducts,
            Pair.of(materials, 1));
        if (mID <= 31_766 || mID > 32_767) throw new IllegalArgumentException();
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
        this.stats.isProxy = true;
        if (type == Types.COMPOUND) {
            this.stats.setElektrolysis(true);
            this.generationFeatures.addChemicalRecipes();
        } else if (type == Types.MIXTURE) {
            this.stats.setCentrifuge(true);
            this.generationFeatures.addMixerRecipes();
        }
    }

    @SafeVarargs
    public Werkstoff(short[] rgba, String defaultName, Werkstoff.Types type, int meltingpoint,
        Werkstoff.GenerationFeatures generationFeatures, int mID, TextureSet texSet,
        Pair<ISubTagContainer, Integer>... contents) {
        this(
            rgba,
            defaultName,
            Werkstoff.Types.getDefaultStatForType(type)
                .setMeltingPoint(meltingpoint),
            type,
            generationFeatures,
            mID,
            texSet,
            contents);
    }

    @SafeVarargs
    public Werkstoff(short[] rgba, String defaultName, Werkstoff.Types type,
        Werkstoff.GenerationFeatures generationFeatures, int mID, TextureSet texSet,
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

    @SafeVarargs
    public Werkstoff(short[] rgba, String defaultName, Werkstoff.Types type, int meltingpoint,
        Werkstoff.GenerationFeatures generationFeatures, int mID, TextureSet texSet,
        List<ISubTagContainer> oreByProduct, Pair<ISubTagContainer, Integer>... contents) {
        this(
            rgba,
            defaultName,
            Werkstoff.Types.getDefaultStatForType(type)
                .setMeltingPoint(meltingpoint),
            type,
            generationFeatures,
            mID,
            texSet,
            oreByProduct,
            contents);
    }

    @SafeVarargs
    public Werkstoff(short[] rgba, String defaultName, Werkstoff.Types type,
        Werkstoff.GenerationFeatures generationFeatures, int mID, TextureSet texSet,
        List<ISubTagContainer> oreByProduct, Pair<ISubTagContainer, Integer>... contents) {
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

    @SafeVarargs
    public Werkstoff(short[] rgba, String toolTip, String defaultName, Werkstoff.Types type,
        Werkstoff.GenerationFeatures generationFeatures, int mID, TextureSet texSet,
        List<ISubTagContainer> oreByProduct, Pair<ISubTagContainer, Integer>... contents) {
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

    @SafeVarargs
    public Werkstoff(short[] rgba, String defaultName, Werkstoff.Stats stats, Werkstoff.Types type,
        Werkstoff.GenerationFeatures generationFeatures, int mID, TextureSet texSet,
        List<ISubTagContainer> oreByProduct, Pair<ISubTagContainer, Integer>... contents) {
        this(rgba, defaultName, "", stats, type, generationFeatures, mID, texSet, contents);
        this.mOreByProducts.clear();
        this.mOreByProducts.addAll(oreByProduct);
    }

    @SafeVarargs
    public Werkstoff(short[] rgba, String defaultName, Werkstoff.Stats stats, Werkstoff.Types type,
        Werkstoff.GenerationFeatures generationFeatures, int mID, TextureSet texSet,
        Pair<ISubTagContainer, Integer>... contents) {
        this(rgba, defaultName, "", stats, type, generationFeatures, mID, texSet, contents);
    }

    @SafeVarargs
    public Werkstoff(short[] rgba, String defaultName, String toolTip, Werkstoff.Stats stats, Werkstoff.Types type,
        Werkstoff.GenerationFeatures generationFeatures, int mID, TextureSet texSet,
        List<ISubTagContainer> oreByProduct, Pair<ISubTagContainer, Integer>... contents) {
        this(rgba, defaultName, toolTip, stats, type, generationFeatures, mID, texSet, contents);
        this.mOreByProducts.clear();
        this.mOreByProducts.addAll(oreByProduct);
    }

    @SafeVarargs
    public Werkstoff(short[] rgba, String defaultName, String toolTip, Werkstoff.Stats stats, Werkstoff.Types type,
        Werkstoff.GenerationFeatures generationFeatures, int mID, TextureSet texSet,
        Pair<ISubTagContainer, Integer>... contents) {

        if (Werkstoff.idHashSet.contains((short) mID))
            throw new UnsupportedOperationException("ID (" + mID + ") is already in use!");
        Werkstoff.idHashSet.add((short) mID);
        if (type == null) type = Werkstoff.Types.UNDEFINED;

        this.mID = (short) mID;
        this.defaultName = defaultName;
        // Ensure that localization key are written to the lang file
        GregTechAPI.sAfterGTPreload.add(() -> this.getLocalizedName());
        this.stats = stats;
        this.type = type;
        this.generationFeatures = generationFeatures;
        this.setRgb(BWColorUtil.correctCorlorArray(rgba));
        this.CONTENTS.addAll(Arrays.asList(contents));
        this.toolTip = "";
        if (toolTip.isEmpty()) {
            for (Pair<ISubTagContainer, Integer> p : contents) {
                if (contents.length > 1) {
                    if (p.getKey() instanceof Materials) {
                        if (((Materials) p.getKey()).mMaterialList.size() > 1 && p.getValue() > 1)
                            this.toolTip += "(" + getFormula((Materials) p.getKey())
                                + ")"
                                + BWUtil.subscriptNumber(p.getValue());
                        else this.toolTip += getFormula((Materials) p.getKey())
                            + (p.getValue() > 1 ? BWUtil.subscriptNumber(p.getValue()) : "");
                    }
                    if (p.getKey() instanceof Werkstoff) {
                        if (((Werkstoff) p.getKey()).CONTENTS.size() > 1 && p.getValue() > 1)
                            this.toolTip += "(" + getFormula((Werkstoff) p.getKey())
                                + ")"
                                + BWUtil.subscriptNumber(p.getValue());
                        else this.toolTip += getFormula((Werkstoff) p.getKey())
                            + (p.getValue() > 1 ? BWUtil.subscriptNumber(p.getValue()) : "");
                    }
                } else if (p.getKey() instanceof Materials) {
                    this.toolTip += getFormula((Materials) p.getKey())
                        + (p.getValue() > 1 ? BWUtil.subscriptNumber(p.getValue()) : "");
                } else if (p.getKey() instanceof Werkstoff) this.toolTip += getFormula((Werkstoff) p.getKey())
                    + (p.getValue() > 1 ? BWUtil.subscriptNumber(p.getValue()) : "");
            }
        } else this.toolTip = toolTip;

        // if (this.toolTip.length() > 25)
        // this.toolTip = "The formula is to long...";

        // Ensure that localization key are written to the lang file
        GregTechAPI.sAfterGTPreload.add(() -> this.getLocalizedToolTip());

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
        if (this.CONTENTS.size() == 1 && (firstContent = this.CONTENTS.stream()
            .findFirst()).isPresent()) {
            ISubTagContainer firstContentSubTagContainer = firstContent.get()
                .getKey();
            if (firstContent.get()
                .getValue() == 1 && firstContentSubTagContainer instanceof Materials) this.getGenerationFeatures()
                    .setExtension();
        }

        Werkstoff.werkstoffHashSet.add(this);
        Werkstoff.werkstoffHashMap.put(this.mID, this);
        Werkstoff.werkstoffNameHashMap.put(this.defaultName, this);
        Werkstoff.werkstoffVarNameHashMap.put(this.getVarName(), this);

        this.owner = this.getMaterialOwner();
    }

    private static String getFormula(Materials material) {
        return material.mChemicalFormula.isEmpty() ? "?" : material.mChemicalFormula;
    }

    private static String getFormula(Werkstoff material) {
        return material.toolTip.isEmpty() ? "?" : material.toolTip;
    }

    public Werkstoff addAdditionalOreDict(String s) {
        this.additionalOredict.add(s);
        return this;
    }

    public Set<String> getAdditionalOredict() {
        return this.additionalOredict;
    }

    public void setTCAspects(Pair<Object, Integer>... pAspectsArr) {
        this.stats.mTC_Aspects = pAspectsArr;
    }

    @SuppressWarnings("unchecked")
    public Pair<Object, Integer>[] getTCAspects(int ratio) {
        if (this.stats.mTC_Aspects == null) {
            HashSet<TCAspects.TC_AspectStack> tc_aspectStacks = new HashSet<>();
            HashSet<Pair<Object, Integer>> set = new HashSet<>();
            for (Pair<?, ?> p : this.getContents()
                .getValue()) {
                if (p.getKey() instanceof Materials) tc_aspectStacks.addAll(((Materials) p.getKey()).mAspects);
                if (p.getKey() instanceof Werkstoff) set.addAll(Arrays.asList(((Werkstoff) p.getKey()).getTCAspects()));
            }
            tc_aspectStacks.forEach(
                tc_aspectStack -> set.add(Pair.of(tc_aspectStack.mAspect.mAspect, (int) tc_aspectStack.mAmount)));
            this.stats.mTC_Aspects = set.toArray(new Pair[0]);
        }
        Pair<Object, Integer>[] ret = this.stats.mTC_Aspects.clone();
        for (int i = 0; i < ret.length; i++) {
            ret[i] = Pair.of(ret[i].getKey(), ret[i].getValue() * ratio);
        }
        return ret;
    }

    public List<TCAspects.TC_AspectStack> getGTWrappedTCAspects() {
        final List<TCAspects.TC_AspectStack> ret = new ArrayList<>();
        Arrays.stream(this.getTCAspects())
            .forEach(
                objectIntegerPair -> new TCAspects.TC_AspectStack(
                    TCAspects.valueOf(
                        ((Aspect) objectIntegerPair.getKey()).getName()
                            .toUpperCase(Locale.US)),
                    objectIntegerPair.getValue()).addToAspectList(ret));
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
            if (pair.getKey()
                .equals(stuff)) return true;
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
                    ret += (int) this.CONTENTS.toArray(new Pair[0])[i].getValue();
                }
                break;
            }
            default:
                ret = 1;
                break;
        }
        return Pair.of(ret, this.CONTENTS);
    }

    public int getNoOfByProducts() {
        return this.mOreByProducts.size();
    }

    public ISubTagContainer getOreByProductRaw(int aNumber) {
        if (this.mOreByProducts.isEmpty()) return null;
        if (aNumber < 0) aNumber = this.mOreByProducts.size() + aNumber;
        while (aNumber >= this.mOreByProducts.size()) aNumber--;
        ISubTagContainer o = this.mOreByProducts.get(aNumber);
        if (o == null || o.equals(Werkstoff.default_null_Werkstoff) || o.equals(Materials._NULL)) return this;
        return o;
    }

    public ItemStack getOreByProduct(int aNumber, OrePrefixes prefixes) {
        if (this.mOreByProducts.isEmpty()) return null;
        if (aNumber < 0) aNumber = this.mOreByProducts.size() + aNumber;
        while (aNumber >= this.mOreByProducts.size()) aNumber--;
        Object o = this.mOreByProducts.get(aNumber);
        if (o == null || o.equals(Werkstoff.default_null_Werkstoff) || o.equals(Materials._NULL))
            return this.get(prefixes);
        if (o instanceof Werkstoff) return WerkstoffLoader.getCorrespondingItemStack(prefixes, (Werkstoff) o);
        if (o instanceof Materials) return GTOreDictUnificator.get(prefixes, o, 1L);
        return null;
    }

    public String getDefaultName() {
        return this.defaultName;
    }

    @Override
    public String getLocalizedName() {
        return GTLanguageManager.addStringLocalization(
            String.format("bw.werkstoff.%05d.name", this.mID),
            this.defaultName,
            !GregTechAPI.sPostloadFinished);
    }

    public String getVarName() {
        return this.defaultName.replace(" ", "");
    }

    public String getToolTip() {
        return this.toolTip;
    }

    public String getLocalizedToolTip() {
        return GTLanguageManager.addStringLocalization(
            String.format("bw.werkstoff.%05d.tooltip", this.mID),
            this.toolTip,
            !GregTechAPI.sPostloadFinished);
    }

    public Werkstoff.Stats getStats() {
        return this.stats;
    }

    public short getmID() {
        return this.mID;
    }

    @Override
    public int getId() {
        return mID;
    }

    @Override
    public List<IStoneType> getValidStones() {
        return StoneType.STONE_ONLY;
    }

    @Override
    public String getInternalName() {
        return getVarName();
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
        this.rgb = new byte[] { (byte) (rgb[0] - 128), (byte) (rgb[1] - 128), (byte) (rgb[2] - 128) };
    }

    @Override
    public short[] getRGBA() {
        return new short[] { (short) (this.rgb[0] + 128), (short) (this.rgb[1] + 128), (short) (this.rgb[2] + 128), 0 };
    }

    @Override
    public TextureSet getTextureSet() {
        return texSet;
    }

    @Override
    public boolean contains(SubTag subTag) {
        if (!subTag.equals(WerkstoffLoader.NOBLE_GAS) && !subTag.equals(WerkstoffLoader.ANAEROBE_GAS)
            && !subTag.equals(WerkstoffLoader.NO_BLAST))
            for (Pair<ISubTagContainer, Integer> p : this.CONTENTS) if (p.getKey()
                .contains(subTag)) return true;
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

    @Override
    public ItemStack getPart(OrePrefixes prefix, int amount) {
        return GTUtility.copyAmountUnsafe(amount, get(prefix));
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
        return this.stats.getQualityOverride() > 0 ? this.stats.getQualityOverride()
            : (byte) (15f * (this.getStats()
                .getProtons() / 188f
                + this.getStats()
                    .getMeltingPoint() / 10801f)
                / (float) this.getContents()
                    .getKey());
    }

    public float getToolSpeed() {
        return this.stats.getSpeedOverride() > 0f ? this.stats.getSpeedOverride()
            : Math.max(
                1f,
                2f * (-this.getStats()
                    .getMass() + 0.1f
                        * this.getStats()
                            .getMeltingPoint()
                    + this.getStats()
                        .getProtons())
                    * 0.1f
                    / (float) this.getContents()
                        .getKey()
                    * 0.1f
                    * this.getToolQuality());
    }

    public int getDurability() {
        return this.stats.getDurOverride() > 0 ? this.stats.getDurOverride()
            : (int) (this.stats.durMod * (0.01f * this.getStats()
                .getMeltingPoint()
                * this.getStats()
                    .getMass()
                / (float) this.getContents()
                    .getKey()));
    }

    @Override
    public boolean generatesPrefix(OrePrefixes prefix) {
        return hasItemType(prefix);
    }

    /**
     * Checks if the generation feature is enabled and if its not in the blacklist
     */
    public boolean hasItemType(OrePrefixes prefix) {
        // Proxy materials are only used to generate (re)bolted casings, so we don't want to generate sheetmetals for
        // them. There's no good way to blacklist this, so we just add a dedicated check for it.
        if (prefix == OrePrefixes.sheetmetal && stats.isProxy) return false;

        // Explicit overrides take priority over the bitmask
        if (this.getGenerationFeatures().disabledPrefixes.contains(prefix)) return false;
        if (this.getGenerationFeatures().enablePrefixes.contains(prefix)) return true;

        int unpacked = Werkstoff.GenerationFeatures.getPrefixDataRaw(prefix);
        return (this.getGenerationFeatures().toGenerate & unpacked) != 0;
    }

    @Deprecated
    public boolean hasGenerationFeature(OrePrefixes prefix) {
        return hasItemType(prefix);
    }

    /**
     * Checks if the Actual Stack exists in the OreDict
     */
    public boolean doesOreDictedItemExists(OrePrefixes prefixes) {
        return OreDictHandler.getItemStack(this.getDefaultName(), prefixes, 1) != null;
    }

    public String getOwner() {
        return this.owner;
    }

    private String getMaterialOwner() {
        String modName = Loader.instance()
            .activeModContainer()
            .getName();
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
            return switch (T) {
                case COMPOUND, BIOLOGICAL -> new Werkstoff.Stats().setElektrolysis(true);
                case MIXTURE -> new Werkstoff.Stats().setCentrifuge(true);
                default -> new Werkstoff.Stats();
            };
        }
    }

    public static class GenerationFeatures {

        public static final GenerationFeatures DISABLED = new GenerationFeatures().disable();

        public static final int DUSTS = 0b1;
        public static final int METALS = 0b1 << 1;
        public static final int GEMS = 0b1 << 2;
        public static final int ORES = 0b1 << 3;
        public static final int LIQUID_CELLS = 0b1 << 4;
        /// Unused
        public static final int BOTTLES = 0b1 << 5;
        public static final int MOLTEN_CELLS = 0b1 << 6;
        public static final int SIMPLE_METALWORKING = 0b1 << 7;
        public static final int CRAFTING_METALWORKING = 0b1 << 8;
        /// Double and dense plates
        public static final int DOUBLE_DENSE_PLATES = 0b1 << 9;
        /// Triple, quadruple, and quintuple plates
        public static final int MULTI_PLATES = 0b1 << 10;

        long toGenerate = DUSTS | ORES;

        private boolean isExtension;
        private static final Object2IntOpenHashMap<OrePrefixes> prefixLogic = new Object2IntOpenHashMap<>(0);
        private final HashSet<OrePrefixes> disabledPrefixes = new HashSet<>(0);
        private final HashSet<OrePrefixes> enablePrefixes = new HashSet<>(0);

        public boolean enforceUnification;

        private final EnumSet<ExtraRecipes> extraRecipes = EnumSet.noneOf(ExtraRecipes.class);

        public short mixCircuit = -1;

        private enum ExtraRecipes {
            /// Adds a chemical reactor recipe that synthesizes this material from its contained parts.
            ChemicalSynthesis,
            /// Adds solidification recipes for metal crafting parts (see [#initPrefixLogic()] and
            /// [#CRAFTING_METALWORKING]).
            MetalCraftingSolidification,
            /// Adds solidification recipes for metal parts (see [#initPrefixLogic()] and [#SIMPLE_METALWORKING]).
            MetalSolidification,
            /// Adds mixing recipe that creates this material from its contained parts.
            Mixing,
            /// Adds a sifting oreproc recipe for this material's ore. Also adds a 9x gem -> block compressor recipe for
            /// some reason.
            Sifting;
        }

        public GenerationFeatures() {}

        public static void initPrefixLogic() {
            prefixLogic.defaultReturnValue(0);

            prefixLogic.put(OrePrefixes.dust, DUSTS);
            prefixLogic.put(OrePrefixes.dustTiny, DUSTS);
            prefixLogic.put(OrePrefixes.dustSmall, DUSTS);

            prefixLogic.put(OrePrefixes.ingot, METALS);
            prefixLogic.put(OrePrefixes.ingotHot, METALS);
            prefixLogic.put(OrePrefixes.nugget, METALS);

            prefixLogic.put(OrePrefixes.gem, GEMS);
            prefixLogic.put(OrePrefixes.gemFlawed, GEMS);
            prefixLogic.put(OrePrefixes.gemExquisite, GEMS);
            prefixLogic.put(OrePrefixes.gemChipped, GEMS);
            prefixLogic.put(OrePrefixes.gemFlawless, GEMS);
            prefixLogic.put(OrePrefixes.lens, GEMS);

            prefixLogic.put(OrePrefixes.block, METALS | GEMS);

            prefixLogic.put(OrePrefixes.ore, ORES);
            prefixLogic.put(OrePrefixes.dustImpure, ORES);
            prefixLogic.put(OrePrefixes.dustPure, ORES);
            prefixLogic.put(OrePrefixes.crushed, ORES);
            prefixLogic.put(OrePrefixes.crushedPurified, ORES);
            prefixLogic.put(OrePrefixes.crushedCentrifuged, ORES);
            prefixLogic.put(OrePrefixes.rawOre, ORES);

            prefixLogic.put(OrePrefixes.cell, LIQUID_CELLS);
            if (Mods.Forestry.isModLoaded()) {
                prefixLogic.put(OrePrefixes.capsule, LIQUID_CELLS);
                prefixLogic.put(OrePrefixes.capsuleMolten, MOLTEN_CELLS);
            }
            // prefixLogic.put(OrePrefixes.bottle, BOTTLES);

            prefixLogic.put(OrePrefixes.cellMolten, MOLTEN_CELLS);

            prefixLogic.put(OrePrefixes.plate, SIMPLE_METALWORKING);
            prefixLogic.put(OrePrefixes.foil, SIMPLE_METALWORKING);
            prefixLogic.put(OrePrefixes.stick, SIMPLE_METALWORKING);
            prefixLogic.put(OrePrefixes.stickLong, SIMPLE_METALWORKING);
            prefixLogic.put(OrePrefixes.toolHeadHammer, SIMPLE_METALWORKING);
            prefixLogic.put(OrePrefixes.toolHeadWrench, SIMPLE_METALWORKING);
            prefixLogic.put(OrePrefixes.toolHeadSaw, SIMPLE_METALWORKING);
            prefixLogic.put(OrePrefixes.turbineBlade, SIMPLE_METALWORKING);

            prefixLogic.put(OrePrefixes.screw, CRAFTING_METALWORKING);
            prefixLogic.put(OrePrefixes.gearGt, CRAFTING_METALWORKING);
            prefixLogic.put(OrePrefixes.gearGtSmall, CRAFTING_METALWORKING);
            prefixLogic.put(OrePrefixes.bolt, CRAFTING_METALWORKING);
            prefixLogic.put(OrePrefixes.ring, CRAFTING_METALWORKING);
            prefixLogic.put(OrePrefixes.spring, CRAFTING_METALWORKING);
            prefixLogic.put(OrePrefixes.springSmall, CRAFTING_METALWORKING);
            prefixLogic.put(OrePrefixes.rotor, CRAFTING_METALWORKING);
            prefixLogic.put(OrePrefixes.wireFine, CRAFTING_METALWORKING);
            prefixLogic.put(OrePrefixes.sheetmetal, CRAFTING_METALWORKING);

            prefixLogic.put(OrePrefixes.plateDouble, DOUBLE_DENSE_PLATES);
            prefixLogic.put(OrePrefixes.plateDense, DOUBLE_DENSE_PLATES);

            prefixLogic.put(OrePrefixes.plateTriple, MULTI_PLATES);
            prefixLogic.put(OrePrefixes.plateQuadruple, MULTI_PLATES);
            prefixLogic.put(OrePrefixes.plateQuintuple, MULTI_PLATES);

            prefixLogic.put(OrePrefixes.blockCasing, SIMPLE_METALWORKING | CRAFTING_METALWORKING | DOUBLE_DENSE_PLATES);
            prefixLogic.put(
                OrePrefixes.blockCasingAdvanced,
                SIMPLE_METALWORKING | CRAFTING_METALWORKING | DOUBLE_DENSE_PLATES);
        }

        public void setExtension() {
            this.isExtension = !this.isExtension;
        }

        public static int getPrefixDataRaw(OrePrefixes prefixes) {
            if (prefixes == null) throw new IllegalArgumentException("OrePrefixes is NULL!");
            return prefixLogic.getInt(prefixes);
        }

        public boolean isExtension() {
            return this.isExtension;
        }

        /// Removes a specific prefix without interacting with other prefixes.
        public Werkstoff.GenerationFeatures removePrefix(OrePrefixes p) {
            this.disabledPrefixes.add(p);
            return this;
        }

        /// Adds a specific prefix without interacting with other prefixes.
        public Werkstoff.GenerationFeatures addPrefix(OrePrefixes p) {
            this.enablePrefixes.add(p);
            return this;
        }

        public Werkstoff.GenerationFeatures enforceUnification() {
            this.enforceUnification = true;
            return this;
        }

        /// Adds a chemical reactor recipe that synthesizes this material from its contained parts.
        public Werkstoff.GenerationFeatures addChemicalRecipes() {
            this.extraRecipes.add(ExtraRecipes.ChemicalSynthesis);
            return this;
        }

        public boolean hasChemicalRecipes() {
            return this.extraRecipes.contains(ExtraRecipes.ChemicalSynthesis);
        }

        /// Adds solidification recipes for metal crafting parts (see [#initPrefixLogic()] and
        /// [#CRAFTING_METALWORKING]).
        public Werkstoff.GenerationFeatures addMetalCraftingSolidifierRecipes() {
            this.extraRecipes.add(ExtraRecipes.MetalCraftingSolidification);
            return this;
        }

        public boolean hasMetalCraftingSolidifierRecipes() {
            return this.extraRecipes.contains(ExtraRecipes.MetalCraftingSolidification);
        }

        /// Adds solidification recipes for metal parts (see [#initPrefixLogic()] and [#SIMPLE_METALWORKING]).
        public Werkstoff.GenerationFeatures addMetaSolidifierRecipes() {
            this.extraRecipes.add(ExtraRecipes.MetalSolidification);
            return this;
        }

        public boolean hasMetaSolidifierRecipes() {
            return this.extraRecipes.contains(ExtraRecipes.MetalSolidification);
        }

        /// Adds mixing recipe that creates this material from its contained parts.
        public Werkstoff.GenerationFeatures addMixerRecipes() {
            this.extraRecipes.add(ExtraRecipes.Mixing);
            return this;
        }

        /// Adds mixing recipe that creates this material from its contained parts.
        public Werkstoff.GenerationFeatures addMixerRecipes(short aCircuit) {
            this.extraRecipes.add(ExtraRecipes.Mixing);
            if (aCircuit >= 1 && aCircuit <= 24) this.mixCircuit = aCircuit;
            return this;
        }

        public boolean hasMixerRecipes() {
            return this.extraRecipes.contains(ExtraRecipes.Mixing);
        }

        /// Adds a sifting oreproc recipe for this material's ore. Also adds a 9x gem -> block compressor recipe for
        /// some reason.
        public Werkstoff.GenerationFeatures addSifterRecipes() {
            this.extraRecipes.add(ExtraRecipes.Sifting);
            return this;
        }

        public boolean hasSifterRecipes() {
            return this.extraRecipes.contains(ExtraRecipes.Sifting);
        }

        public Werkstoff.GenerationFeatures disable() {
            this.toGenerate = 0;
            return this;
        }

        public Werkstoff.GenerationFeatures onlyDust() {
            this.toGenerate = DUSTS;
            return this;
        }

        /**
         * Automatically adds Simple Metal Working Items
         */
        public Werkstoff.GenerationFeatures addMetalItems() {
            this.toGenerate |= METALS;
            this.toGenerate |= SIMPLE_METALWORKING;
            return this;
        }

        public Werkstoff.GenerationFeatures addCells() {
            this.toGenerate |= LIQUID_CELLS;
            return this;
        }

        public Werkstoff.GenerationFeatures addMolten() {
            this.toGenerate |= MOLTEN_CELLS;
            return this;
        }

        /**
         * Automatically adds Simple Metal Working Items
         */
        public Werkstoff.GenerationFeatures addGems() {
            this.toGenerate |= SIMPLE_METALWORKING;
            this.toGenerate |= GEMS;
            return this;
        }

        public Werkstoff.GenerationFeatures addSimpleMetalWorkingItems() {
            this.toGenerate |= SIMPLE_METALWORKING;
            return this;
        }

        public Werkstoff.GenerationFeatures addCasings() {
            this.toGenerate |= SIMPLE_METALWORKING;
            this.toGenerate |= CRAFTING_METALWORKING;
            this.toGenerate |= DOUBLE_DENSE_PLATES;
            this.toGenerate |= METALS;
            return this;
        }

        public Werkstoff.GenerationFeatures addCraftingMetalWorkingItems() {
            this.toGenerate |= CRAFTING_METALWORKING;
            return this;
        }

        public Werkstoff.GenerationFeatures addDoubleAndDensePlates() {
            this.toGenerate |= DOUBLE_DENSE_PLATES;
            return this;
        }

        /**
         * Due to rebolted casings, double plates had to be excluded from this
         */
        public Werkstoff.GenerationFeatures addMultiPlates() {
            this.toGenerate |= MULTI_PLATES;
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
         * The generated EBF recipes using this gas will have their duration multiplied by this number. If set to a
         * negative value, the default proton count-based logic is used. See also
         * {@link gregtech.api.util.BlastFurnaceGasStat}
         */
        public Werkstoff.Stats setEbfGasRecipeTimeMultiplier(double timeMultiplier) {
            this.ebfGasRecipeTimeMultiplier = timeMultiplier;
            return this;
        }

        public double getEbfGasRecipeConsumedAmountMultiplier() {
            return this.ebfGasRecipeConsumedAmountMultiplier;
        }

        /**
         * The generated EBF recipes using this gas will have the amount of gas consumed multiplied by this number. See
         * also {@link gregtech.api.util.BlastFurnaceGasStat}
         */
        public Werkstoff.Stats setEbfGasRecipeConsumedAmountMultiplier(double amountMultiplier) {
            this.ebfGasRecipeConsumedAmountMultiplier = amountMultiplier;
            return this;
        }

        public int getDurOverride() {
            return this.durOverride;
        }

        public Werkstoff.Stats setDurOverride(int durOverride) {
            this.durOverride = durOverride;
            return this;
        }

        public float getSpeedOverride() {
            return this.speedOverride;
        }

        public Werkstoff.Stats setSpeedOverride(float speedOverride) {
            this.speedOverride = speedOverride;
            return this;
        }

        public byte getQualityOverride() {
            return this.qualityOverride;
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
        /// Whether this material is a proxy for a GT material. See [BWGTMaterialReference].
        private boolean isProxy = false;

        private boolean autoGenerateBlastFurnaceRecipes = true;
        private boolean autoGenerateVacuumFreezerRecipes = true;

        float durMod = 1f;

        public boolean autoGenerateBlastFurnaceRecipes() {
            return autoGenerateBlastFurnaceRecipes;
        }

        public boolean autoGenerateVacuumFreezerRecipes() {
            return autoGenerateVacuumFreezerRecipes;
        }

        public Werkstoff.Stats disableAutoGeneratedBlastFurnaceRecipes() {
            autoGenerateBlastFurnaceRecipes = false;
            return this;
        }

        public Werkstoff.Stats disableAutoGeneratedVacuumFreezerRecipes() {
            autoGenerateVacuumFreezerRecipes = false;
            return this;
        }

        public float getDurMod() {
            return this.durMod;
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
            if (!(o instanceof Werkstoff.Stats that)) return false;

            if (this.boilingPoint != that.boilingPoint || this.meltingPoint != that.meltingPoint
                || this.mass != that.mass
                || this.protons != that.protons) return false;
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
            return this.enchantmentlvl;
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
            return this.meltingVoltage;
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
            }
            return FluidState.LIQUID;
        }

        public Werkstoff.Stats setGas(boolean gas) {
            if (gas) this.quality = (byte) (this.quality | 0x40);
            else this.quality = (byte) (this.quality & 0b0111111);
            return this;
        }
    }
}
