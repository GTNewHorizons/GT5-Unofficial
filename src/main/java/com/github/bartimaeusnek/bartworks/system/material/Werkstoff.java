/*
 * Copyright (c) 2019 bartimaeusnek
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

import com.github.bartimaeusnek.bartworks.util.BW_ColorUtil;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.github.bartimaeusnek.bartworks.util.MurmurHash3;
import com.github.bartimaeusnek.bartworks.util.Pair;
import gregtech.api.enums.*;
import gregtech.api.interfaces.IColorModulationContainer;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.objects.GT_Fluid;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import java.nio.ByteBuffer;
import java.util.*;

public class Werkstoff implements IColorModulationContainer, ISubTagContainer {

    static final LinkedHashSet<Werkstoff> werkstoffHashSet = new LinkedHashSet<>();
    public static final LinkedHashMap<Short, Werkstoff> werkstoffHashMap = new LinkedHashMap<>();
    private static final HashSet<Short> idHashSet = new HashSet<>();

    private static final Werkstoff.Stats DEFAULT_NULL_STATS = new Werkstoff.Stats();
    private static final Werkstoff.GenerationFeatures DEFAULT_NULL_GENERATION_FEATURES = new Werkstoff.GenerationFeatures().disable();
    public static Werkstoff default_null_Werkstoff;

    private final List<ISubTagContainer> mOreByProducts = new ArrayList<ISubTagContainer>();
    private final LinkedHashSet<Pair<ISubTagContainer, Integer>> contents = new LinkedHashSet<>();
    HashSet<SubTag> subtags = new HashSet<>();
    private byte[] rgb = new byte[3];
    private final String defaultName;
    private String toolTip;
    private Fluid fluid;
    private Fluid gas;

    private Werkstoff.Stats stats;
    private final Werkstoff.Types type;
    private final Werkstoff.GenerationFeatures generationFeatures;
    private final short mID;
    private final TextureSet texSet;

    public static void init(){
        Werkstoff.default_null_Werkstoff = new Werkstoff(new short[3], "_NULL", "Default null Werkstoff", Werkstoff.DEFAULT_NULL_STATS, Werkstoff.Types.UNDEFINED, Werkstoff.DEFAULT_NULL_GENERATION_FEATURES, -1, TextureSet.SET_NONE);
    }

    public Werkstoff(short[] rgba, String defaultName, Werkstoff.Types type, Werkstoff.GenerationFeatures generationFeatures, int mID, TextureSet texSet, Pair<ISubTagContainer, Integer>... contents) {
        this(rgba, defaultName, Werkstoff.Types.getDefaultStatForType(type), type, generationFeatures, mID, texSet, contents);
    }
    public Werkstoff(short[] rgba, String defaultName, Werkstoff.Types type, Werkstoff.GenerationFeatures generationFeatures, int mID, TextureSet texSet, List<ISubTagContainer> oreByProduct, Pair<ISubTagContainer, Integer>... contents) {
        this(rgba, defaultName, Werkstoff.Types.getDefaultStatForType(type), type, generationFeatures, mID, texSet, oreByProduct, contents);
    }
    public Werkstoff(short[] rgba, String toolTip, String defaultName, Werkstoff.Types type, Werkstoff.GenerationFeatures generationFeatures, int mID, TextureSet texSet, List<ISubTagContainer> oreByProduct, Pair<ISubTagContainer, Integer>... contents) {
        this(rgba, toolTip, defaultName, Werkstoff.Types.getDefaultStatForType(type), type, generationFeatures, mID, texSet, oreByProduct, contents);
    }

    public Werkstoff(short[] rgba, String defaultName, Werkstoff.Stats stats, Werkstoff.Types type, Werkstoff.GenerationFeatures generationFeatures, int mID, TextureSet texSet, List<ISubTagContainer> oreByProduct, Pair<ISubTagContainer, Integer>... contents) {
        this(rgba, defaultName, "", stats, type, generationFeatures, mID, texSet, contents);
        this.mOreByProducts.addAll(oreByProduct);
    }

    public Werkstoff(short[] rgba, String defaultName, Werkstoff.Stats stats, Werkstoff.Types type, Werkstoff.GenerationFeatures generationFeatures, int mID, TextureSet texSet, Pair<ISubTagContainer, Integer>... contents) {
        this(rgba, defaultName, "", stats, type, generationFeatures, mID, texSet, contents);
    }

    public Werkstoff(short[] rgba, String defaultName, String toolTip, Werkstoff.Stats stats, Werkstoff.Types type, Werkstoff.GenerationFeatures generationFeatures, int mID, TextureSet texSet, List<ISubTagContainer> oreByProduct, Pair<ISubTagContainer, Integer>... contents) {
        this(rgba, defaultName, toolTip, stats, type, generationFeatures, mID, texSet, contents);
        this.mOreByProducts.addAll(oreByProduct);
    }

    public Werkstoff(short[] rgba, String defaultName, String toolTip, Werkstoff.Stats stats, Werkstoff.Types type, Werkstoff.GenerationFeatures generationFeatures, int mID, TextureSet texSet, Pair<ISubTagContainer, Integer>... contents) {

        if (Werkstoff.idHashSet.contains((short) mID))
            throw new UnsupportedOperationException("ID (" + mID + ") is already in use!");
        Werkstoff.idHashSet.add((short) mID);
        if (type == null)
            type = Werkstoff.Types.UNDEFINED;

        this.defaultName = defaultName;

        this.type = type;
        this.mID = (short) mID;
        this.generationFeatures = generationFeatures;
        this.setRgb(BW_ColorUtil.correctCorlorArray(rgba));
        this.contents.addAll(Arrays.asList(contents));
        this.toolTip = "";
        if (toolTip.isEmpty()) {
            for (Pair<ISubTagContainer, Integer> p : contents) {
                if (p.getKey() instanceof Materials) {
                    this.toolTip += ((Materials) p.getKey()).mChemicalFormula + (p.getValue() > 1 ? BW_Util.subscriptNumber(p.getValue()) : "");
                }
                if (p.getKey() instanceof Werkstoff)
                    this.toolTip += ((Werkstoff) p.getKey()).toolTip + (p.getValue() > 1 ? BW_Util.subscriptNumber(p.getValue()) : "");
            }
        } else
            this.toolTip = toolTip;
        long tmpprotons = 0;
        for (Pair<ISubTagContainer, Integer> p : contents) {
            if (p.getKey() instanceof Materials) {
                tmpprotons += ((Materials) p.getKey()).getProtons() * p.getValue();
            } else if (p.getKey() instanceof Werkstoff) {
                tmpprotons += ((Werkstoff) p.getKey()).getStats().protons * p.getValue();
            }
        }
        this.stats = stats.setProtons(tmpprotons);

        long tmpmass = 0;
        for (Pair<ISubTagContainer, Integer> p : contents) {
            if (p.getKey() instanceof Materials) {
                tmpmass += ((Materials) p.getKey()).getMass() * p.getValue();
            } else if (p.getKey() instanceof Werkstoff) {
                tmpprotons += ((Werkstoff) p.getKey()).getStats().mass * p.getValue();
            }
        }
        this.stats = stats.setMass(tmpmass);

        this.texSet = texSet;

        if (this.getStats().meltingPoint > 0) {
            this.fluid = new GT_Fluid("molten" + this.getDefaultName().replaceAll(" ", ""), "molten.autogenerated", this.getRGBA());
            this.getGenerationFeatures().toGenerate |= 16;
        }

        Werkstoff.werkstoffHashSet.add(this);
        Werkstoff.werkstoffHashMap.put(this.mID, this);
    }


    public void setTCAspects(Pair<Object,Integer>... pAspectsArr){
        this.stats.mTC_Aspects=pAspectsArr;
    }

    public Pair<Object,Integer>[] getTCAspects(int ratio){
        if (this.stats.mTC_Aspects == null) {
            HashSet<TC_Aspects.TC_AspectStack> tc_aspectStacks = new HashSet<>();
            HashSet<Pair<Object, Integer>> set = new HashSet<>();
            for (Pair p : this.getContents().getValue()) {
                if (p.getKey() instanceof Materials)
                    tc_aspectStacks.addAll(((Materials) p.getKey()).mAspects);
                if (p.getKey() instanceof Werkstoff)
                    set.addAll(Arrays.asList(((Werkstoff) p.getKey()).getTCAspects()));
            }
            tc_aspectStacks.forEach(tc_aspectStack -> set.add(new Pair<Object, Integer>(tc_aspectStack.mAspect.mAspect, (int) tc_aspectStack.mAmount)));
            this.stats.mTC_Aspects = set.toArray(new Pair[0]);
        }
        Pair<Object,Integer>[] ret = this.stats.mTC_Aspects.clone();
        for (int i = 0; i < ret.length; i++) {
            ret[i]=ret[i].copyWithNewValue(ret[i].getValue() * ratio);
        }
        return ret;
    }

    public Pair<Object,Integer>[] getTCAspects(){
        return this.getTCAspects(1);
    }

    public Werkstoff.Types getType() {
        return this.type;
    }

    public Pair<Integer, LinkedHashSet<Pair<ISubTagContainer, Integer>>> getContents() {
        int ret = 0;
        switch (this.type) {
            case COMPOUND:
            case BIOLOGICAL: {
                for (int i = 0; i < this.contents.toArray().length; i++) {
                    ret += ((Pair<ISubTagContainer, Integer>) this.contents.toArray()[i]).getValue();
                }
                break;
            }
            default:
                ret = 1;
                break;
        }
        return new Pair<>(ret, this.contents);
    }

    public int getNoOfByProducts() {
        return this.mOreByProducts.size();
    }

    public ISubTagContainer getOreByProductRaw(int aNumber){
        if (this.mOreByProducts.size() == 0)
            return null;
        if (aNumber < 0)
            aNumber = this.mOreByProducts.size() + aNumber;
        while (aNumber >= this.mOreByProducts.size())
            aNumber--;
        ISubTagContainer o = this.mOreByProducts.get(aNumber);
        if (o == null || o.equals(Werkstoff.default_null_Werkstoff) || o.equals(Materials._NULL))
            return this;
        return o;
    }

    public ItemStack getOreByProduct(int aNumber, OrePrefixes prefixes) {
        if (this.mOreByProducts.size() == 0)
            return null;
        if (aNumber < 0)
            aNumber = this.mOreByProducts.size() + aNumber;
        while (aNumber >= this.mOreByProducts.size())
            aNumber--;
        Object o = this.mOreByProducts.get(aNumber);
        if (o == null||o.equals(Werkstoff.default_null_Werkstoff) || o.equals(Materials._NULL))
            return this.get(prefixes);
        if (o instanceof Werkstoff)
            return WerkstoffLoader.getCorresopndingItemStack(prefixes, (Werkstoff) o);
        if (o instanceof Materials)
            return GT_OreDictUnificator.get(prefixes, o, 1L);
        return null;
    }

    public String getDefaultName() {
        return this.defaultName;
    }

    public String getToolTip() {
        return this.toolTip;
    }

    public Werkstoff.Stats getStats() {
        return this.stats;
    }

    public short getmID() {
        return this.mID;
    }

    public Werkstoff.GenerationFeatures getGenerationFeatures() {
        return this.generationFeatures;
    }

    public TextureSet getTexSet() {
        return this.texSet;
    }

    public void setRgb(short[] rgb) {
        this.rgb = new byte[]{(byte) (rgb[0] - 128), (byte) (rgb[1] - 128), (byte) (rgb[2] - 128)};
    }

    @Override
    public short[] getRGBA() {
        return new short[]{(short) (this.rgb[0] + 128), (short) (this.rgb[1] + 128), (short) (this.rgb[2] + 128), 0};
    }

    @Override
    public boolean contains(SubTag subTag) {
        for (Pair<ISubTagContainer, Integer> p : this.contents)
            if (p.getKey().contains(subTag))
                return true;
        return this.subtags.contains(subTag);
    }

    @Override
    public ISubTagContainer add(SubTag... subTags) {
        this.subtags.addAll(Arrays.asList(subTags));
        return this;
    }

    @Override
    public boolean remove(SubTag subTag) {
        return this.subtags.remove(subTag);
    }

    public void getAndAddToCollection(OrePrefixes prefixes,int amount,Collection<ItemStack> stacks){
        stacks.add(this.get(prefixes,amount));
    }

    public ItemStack get(OrePrefixes prefixes) {
        return WerkstoffLoader.getCorresopndingItemStack(prefixes, this);
    }

    public ItemStack get(OrePrefixes prefixes, int amount) {
        return WerkstoffLoader.getCorresopndingItemStack(prefixes, this, amount);
    }

    public enum Types {
        MATERIAL, COMPOUND, MIXTURE, BIOLOGICAL, ELEMENT, UNDEFINED;

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
        //logic gate shit
        /*
        dust 1
        metal 10
        gem 100
        ore 1000
        cell 10000
         */
        public byte toGenerate = 0b0001001;
        public byte blacklist;

        /*
         * Auto add Chemical Recipes 1
         * Auto add mixer Recipes 10
         * Auto add Sifter Recipe 100
         */
        public byte extraRecipes;

        public Werkstoff.GenerationFeatures setBlacklist(OrePrefixes p){
            if (p == OrePrefixes.dustTiny || p == OrePrefixes.dust || p == OrePrefixes.dustSmall || p == OrePrefixes.crateGtDust){
                this.blacklist |= 1;
            }else
                this.blacklist |= p.mMaterialGenerationBits;
            return this;
        }

        public boolean hasDusts() {
            return (this.toGenerate & 0b1) != 0;
        }
        public boolean hasGems() {
            return (this.toGenerate & 0b100) != 0;
        }
        public boolean hasOres() {
            return (this.toGenerate & 0b1000) != 0;
        }

        public Werkstoff.GenerationFeatures removeGems(){
            if (this.hasGems())
                this.toGenerate = (byte) (this.toGenerate ^ 0b100);
            return this;
        }

        public Werkstoff.GenerationFeatures removeDusts(){
            if (this.hasDusts())
                this.toGenerate = (byte) (this.toGenerate ^ 0b1);
            return this;
        }
        public Werkstoff.GenerationFeatures removeOres(){
            if (this.hasOres())
                this.toGenerate = (byte) (this.toGenerate ^ 0b1000);
            return this;
        }

        public Werkstoff.GenerationFeatures addChemicalRecipes(){
            this.extraRecipes = (byte) (this.extraRecipes | 1);
            return this;
        }
        public boolean hasChemicalRecipes() {
           return (this.extraRecipes & 1) != 0;
        }

        public Werkstoff.GenerationFeatures addMixerRecipes(){
            this.extraRecipes = (byte) (this.extraRecipes | 10);
            return this;
        }
        public boolean hasMixerRecipes() {
            return (this.extraRecipes & 10) != 0;
        }


        public Werkstoff.GenerationFeatures addSifterRecipes(){
            this.extraRecipes = (byte) (this.extraRecipes | 100);
            return this;
        }
        public boolean hasSifterRecipes() {
            return (this.extraRecipes & 100) != 0;
        }

        public Werkstoff.GenerationFeatures onlyDust() {
            this.toGenerate = (byte) (0b1);
            return this;
        }

        public Werkstoff.GenerationFeatures addMetalItems() {
            this.toGenerate = (byte) (this.toGenerate | 0b10);
            return this;
        }

        public Werkstoff.GenerationFeatures disable() {
            this.toGenerate = (byte) (0);
            return this;
        }

        public Werkstoff.GenerationFeatures addGems() {
            this.toGenerate = (byte) (this.toGenerate | 0x4);
            return this;
        }

    }

    public static class Stats {

        public static final int NULL_KELVIN = 0;

        int boilingPoint;

        public int getBoilingPoint() {
            return this.boilingPoint;
        }

        public Stats setBoilingPoint(int boilingPoint) {
            this.boilingPoint = boilingPoint;
            return this;
        }

        public int getMeltingPoint() {
            return this.meltingPoint;
        }

        public Stats setMeltingPoint(int meltingPoint) {
            this.meltingPoint = meltingPoint;
            return this;
        }

        int meltingPoint;
        long protons;
        long neutrons;
        long electrons;
        long mass;
        private Pair<Object,Integer>[] mTC_Aspects;
        //logic gate shit
        byte quality = ~0b111111;

        public Stats setmTC_AspectsArray(Pair<Object, Integer>[] mTC_Aspects) {
            this.mTC_Aspects = mTC_Aspects;
            return this;
        }

        public Stats setmTC_AspectsVarArg(Pair<Object, Integer>... mTC_Aspects) {
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
            return this.quality == that.quality;
        }

        @Override
        public int hashCode() {
            return MurmurHash3.murmurhash3_x86_32(ByteBuffer.allocate(49).put(this.quality).putInt(this.boilingPoint).putInt(this.meltingPoint).putLong(this.protons).putLong(this.neutrons).putLong(this.electrons).putLong(this.mass).array(), 0, 49, 31);
        }

        public Werkstoff.Stats setMass(long mass) {
            this.mass = this.protons;
            return this;
        }

        public Werkstoff.Stats setProtons(long protons) {
            this.protons = protons;
            return this;
        }

        public boolean isSublimation() {
            return (this.quality & 0b1) == 0b1;
        }

        public Werkstoff.Stats setSublimation(boolean sublimation) {
            if (sublimation)
                this.quality = (byte) (this.quality | 0b000001);
            else
                this.quality = (byte) (this.quality & 0b111110);
            return this;
        }

        public boolean isToxic() {
            return (this.quality >> 1 & 0b1) == 0b1;
        }

        public Werkstoff.Stats setToxic(boolean toxic) {
            if (toxic)
                this.quality = (byte) (this.quality | 0b000010);
            else
                this.quality = (byte) (this.quality & 0b111101);
            return this;
        }

        public boolean isRadioactive() {
            return (this.quality >> 2 & 0b1) == 0b1;
        }

        public Werkstoff.Stats setRadioactive(boolean radioactive) {
            if (radioactive)
                this.quality = (byte) (this.quality | 0b000100);
            else
                this.quality = (byte) (this.quality & 0b111011);
            return this;
        }

        public boolean isBlastFurnace() {
            return (this.quality >> 3 & 0b1) == 0b1;
        }

        public Werkstoff.Stats setBlastFurnace(boolean blastFurnace) {
            if (blastFurnace)
                this.quality = (byte) (this.quality | 0b001000);
            else
                this.quality = (byte) (this.quality & 0b110111);
            return this;
        }

        public boolean isElektrolysis() {
            return (this.quality >> 4 & 0b1) == 0b1;
        }

        public Werkstoff.Stats setElektrolysis(boolean elektrolysis) {
            if (elektrolysis)
                this.quality = (byte) (this.quality | 0b010000);
            else
                this.quality = (byte) (this.quality & 0b101111);
            return this;
        }

        public boolean isCentrifuge() {
            return (this.quality >> 5 & 0b1) == 0b1;
        }

        public Werkstoff.Stats setCentrifuge(boolean centrifuge) {
            if (centrifuge)
                this.quality = (byte) (this.quality | 0b100000);
            else
                this.quality = (byte) (this.quality & 0b011111);
            return this;
        }
    }

}
