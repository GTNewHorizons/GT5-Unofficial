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

import com.github.bartimaeusnek.bartworks.util.MurmurHash3;
import com.github.bartimaeusnek.bartworks.util.Pair;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TextureSet;
import gregtech.api.interfaces.IColorModulationContainer;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import java.nio.ByteBuffer;
import java.util.*;

public class Werkstoff implements IColorModulationContainer, ISubTagContainer {

    public static final LinkedHashSet<Werkstoff> werkstoffHashSet = new LinkedHashSet<>();
    public static final LinkedHashMap<Short, Werkstoff> werkstoffHashMap = new LinkedHashMap<>();
    private static final HashSet<Short> idHashSet = new HashSet<>();

    public static final Werkstoff.Stats DEFAULT_NULL_STATS = new Werkstoff.Stats();
    public static final GenerationFeatures DEFAULT_NULL_GENERATION_FEATURES = new GenerationFeatures();
    public static Werkstoff default_null_Werkstoff;

    private final List<ISubTagContainer> mOreByProducts = new ArrayList<ISubTagContainer>();
    private final LinkedHashSet<Pair<ISubTagContainer, Integer>> contents = new LinkedHashSet<>();
    HashSet<SubTag> subtags = new HashSet<>();
    private byte[] rgb = new byte[3];
    private String defaultName;
    private String toolTip;
    private Fluid fluid;
    private Fluid gas;
    private Werkstoff.Stats stats;
    private Werkstoff.Types type;
    private GenerationFeatures generationFeatures;
    private short mID;
    private TextureSet texSet;

    public static void init(){
        default_null_Werkstoff = new Werkstoff(new short[3], "_NULL", "Default null Werkstoff", DEFAULT_NULL_STATS, Werkstoff.Types.UNDEFINED, DEFAULT_NULL_GENERATION_FEATURES, -1, TextureSet.SET_NONE);
    }


    public Werkstoff(short[] rgba, String defaultName, Werkstoff.Types type, GenerationFeatures generationFeatures, int mID, TextureSet texSet, List<ISubTagContainer> oreByProduct, Pair<ISubTagContainer, Integer>... contents) {
        this(rgba, defaultName, Types.getDefaultStatForType(type), type, generationFeatures, mID, texSet, oreByProduct, contents);
    }
    public Werkstoff(short[] rgba, String toolTip, String defaultName, Werkstoff.Types type, GenerationFeatures generationFeatures, int mID, TextureSet texSet, List<ISubTagContainer> oreByProduct, Pair<ISubTagContainer, Integer>... contents) {
        this(rgba, toolTip, defaultName, Types.getDefaultStatForType(type), type, generationFeatures, mID, texSet, oreByProduct, contents);
    }

    public Werkstoff(short[] rgba, String defaultName, Werkstoff.Stats stats, Werkstoff.Types type, GenerationFeatures generationFeatures, int mID, TextureSet texSet, List<ISubTagContainer> oreByProduct, Pair<ISubTagContainer, Integer>... contents) {
        this(rgba, defaultName, "", stats, type, generationFeatures, mID, texSet, contents);
        this.mOreByProducts.addAll(oreByProduct);
    }

    public Werkstoff(short[] rgba, String defaultName, Werkstoff.Stats stats, Werkstoff.Types type, GenerationFeatures generationFeatures, int mID, TextureSet texSet, Pair<ISubTagContainer, Integer>... contents) {
        this(rgba, defaultName, "", stats, type, generationFeatures, mID, texSet, contents);
    }

    public Werkstoff(short[] rgba, String defaultName, String toolTip, Werkstoff.Stats stats, Werkstoff.Types type, GenerationFeatures generationFeatures, int mID, TextureSet texSet, List<ISubTagContainer> oreByProduct, Pair<ISubTagContainer, Integer>... contents) {
        this(rgba, defaultName, toolTip, stats, type, generationFeatures, mID, texSet, contents);
        this.mOreByProducts.addAll(oreByProduct);
    }

    public Werkstoff(short[] rgba, String defaultName, String toolTip, Werkstoff.Stats stats, Werkstoff.Types type, GenerationFeatures generationFeatures, int mID, TextureSet texSet, Pair<ISubTagContainer, Integer>... contents) {

        if (idHashSet.contains((short) mID))
            throw new UnsupportedOperationException("ID (" + mID + ") is already in use!");
        idHashSet.add((short) mID);
        if (type == null)
            type = Werkstoff.Types.UNDEFINED;

        this.defaultName = defaultName;

        this.type = type;
        this.mID = (short) mID;
        this.generationFeatures = generationFeatures;
        this.setRgb(rgba);
        this.contents.addAll(Arrays.asList(contents));
        this.toolTip = "";
        if (toolTip.isEmpty()) {
            for (Pair<ISubTagContainer, Integer> p : contents) {
                if (p.getKey() instanceof Materials) {
                    this.toolTip += ((Materials) p.getKey()).mChemicalFormula + (p.getValue() > 1 ? p.getValue() : "");
                }
                if (p.getKey() instanceof Werkstoff)
                    this.toolTip += ((Werkstoff) p.getKey()).toolTip + (p.getValue() > 1 ? p.getValue() : "");
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
        werkstoffHashSet.add(this);
        werkstoffHashMap.put(this.mID, this);
    }

    public Pair<Integer, LinkedHashSet<Pair<ISubTagContainer, Integer>>> getContents() {
        int ret = 0;
        switch (this.type) {
            case COMPOUND:
            case BIOLOGICAL: {
                for (int i = 0; i < contents.toArray().length; i++) {
                    ret += ((Pair<ISubTagContainer, Integer>) contents.toArray()[i]).getValue();
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
        return mOreByProducts.size();
    }

    public ItemStack getOreByProduct(int aNumber, OrePrefixes prefixes) {
        if (mOreByProducts.size() == 0)
            return null;
        if (aNumber < 0)
            aNumber = mOreByProducts.size() + aNumber;
        while (aNumber >= mOreByProducts.size())
            aNumber--;
        Object o = mOreByProducts.get(aNumber);
        if (o == null || o.equals(default_null_Werkstoff) || o.equals(Materials._NULL))
            return null;
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

    public GenerationFeatures getGenerationFeatures() {
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
        return new short[]{(short) (rgb[0] + 128), (short) (rgb[1] + 128), (short) (rgb[2] + 128), 0};
    }

    @Override
    public boolean contains(SubTag subTag) {
        for (Pair<ISubTagContainer, Integer> p : contents)
            if (p.getKey().contains(subTag))
                return true;
        if (subtags.contains(subTag))
            return true;
        return false;
    }

    @Override
    public ISubTagContainer add(SubTag... subTags) {
        subtags.addAll(Arrays.asList(subTags));
        return this;
    }

    @Override
    public boolean remove(SubTag subTag) {
        return subtags.remove(subTag);
    }

    public ItemStack get(OrePrefixes prefixes) {
        return WerkstoffLoader.getCorresopndingItemStack(prefixes, this);
    }

    public ItemStack get(OrePrefixes prefixes, int amount) {
        return WerkstoffLoader.getCorresopndingItemStack(prefixes, this, amount);
    }

    public enum Types {
        MATERIAL, COMPOUND, MIXTURE, BIOLOGICAL, UNDEFINED;

        public static Stats getDefaultStatForType(Types T) {
            switch (T) {
                case COMPOUND:
                case BIOLOGICAL:
                    return new Stats().setElektrolysis(true);
                case MIXTURE:
                    return new Stats().setCentrifuge(true);
                default:
                    return new Stats();
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
         */
        public byte toGenerate = 0b0001001;

        public boolean hasGems() {
            return (toGenerate & 4) != 0;
        }

        public GenerationFeatures onlyDust() {
            toGenerate = (byte) (1);
            return this;
        }

        public GenerationFeatures addGems() {
            toGenerate = (byte) (toGenerate | 0x4);
            return this;
        }


    }

    public static class Stats {

        int boilingPoint;
        int meltingPoint;
        long protons;
        long neutrons;
        long electrons;
        long mass;
        //logic gate shit
        byte quality = ~0b111111;

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
            return MurmurHash3.murmurhash3_x86_32(ByteBuffer.allocate(49).put(quality).putInt(boilingPoint).putInt(meltingPoint).putLong(protons).putLong(neutrons).putLong(electrons).putLong(mass).array(), 0, 49, 31);
        }

        public Stats setMass(long mass) {
            this.mass = protons;
            return this;
        }

        public Stats setProtons(long protons) {
            this.protons = protons;
            return this;
        }

        public boolean isSublimation() {
            return (quality & 0b1) == 0b1;
        }

        public Werkstoff.Stats setSublimation(boolean sublimation) {
            if (sublimation)
                quality = (byte) (quality | 0b000001);
            else
                quality = (byte) (quality & 0b111110);
            return this;
        }

        public boolean isToxic() {
            return (quality >> 1 & 0b1) == 0b1;
        }

        public Werkstoff.Stats setToxic(boolean toxic) {
            if (toxic)
                quality = (byte) (quality | 0b000010);
            else
                quality = (byte) (quality & 0b111101);
            return this;
        }

        public boolean isRadioactive() {
            return (quality >> 2 & 0b1) == 0b1;
        }

        public Werkstoff.Stats setRadioactive(boolean radioactive) {
            if (radioactive)
                quality = (byte) (quality | 0b000100);
            else
                quality = (byte) (quality & 0b111011);
            return this;
        }

        public boolean isBlastFurnace() {
            return (quality >> 3 & 0b1) == 0b1;
        }

        public Werkstoff.Stats setBlastFurnace(boolean blastFurnace) {
            if (blastFurnace)
                quality = (byte) (quality | 0b001000);
            else
                quality = (byte) (quality & 0b110111);
            return this;
        }

        public boolean isElektrolysis() {
            return (quality >> 4 & 0b1) == 0b1;
        }

        public Werkstoff.Stats setElektrolysis(boolean elektrolysis) {
            if (elektrolysis)
                quality = (byte) (quality | 0b010000);
            else
                quality = (byte) (quality & 0b101111);
            return this;
        }

        public boolean isCentrifuge() {
            return (quality >> 5 & 0b1) == 0b1;
        }

        public Werkstoff.Stats setCentrifuge(boolean centrifuge) {
            if (centrifuge)
                quality = (byte) (quality | 0b100000);
            else
                quality = (byte) (quality & 0b011111);
            return this;
        }
    }

}
