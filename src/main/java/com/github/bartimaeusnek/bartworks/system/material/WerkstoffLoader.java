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

import com.github.bartimaeusnek.bartworks.API.WerkstoffAdderRegistry;
import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.client.renderer.BW_Renderer_Block_Ores;
import com.github.bartimaeusnek.bartworks.util.Pair;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ProgressManager;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.GT_Mod;
import gregtech.api.enums.*;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static gregtech.api.enums.OrePrefixes.*;

public class WerkstoffLoader implements Runnable {

    private WerkstoffLoader() {}

    public static final WerkstoffLoader INSTANCE = new WerkstoffLoader();

    public static final Werkstoff Bismutite = new Werkstoff(
            new short[]{255, 233, 0, 0},
            "Bismutite",
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().addGems(),
            1,
            TextureSet.SET_FLINT,
            Arrays.asList(Materials.Bismuth),
            new Pair<ISubTagContainer, Integer>(Materials.Bismuth, 2),
            new Pair<ISubTagContainer, Integer>(Materials.Oxygen, 2),
            new Pair<ISubTagContainer, Integer>(Materials.CarbonDioxide, 2)
    );
    public static final Werkstoff Bismuthinit = new Werkstoff(
            new short[]{192, 192, 192, 0},
            "Bismuthinite",
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures(),
            2,
            TextureSet.SET_METALLIC,
            Arrays.asList(Materials.Bismuth, Materials.Sulfur),
            new Pair<ISubTagContainer, Integer>(Materials.Bismuth, 2),
            new Pair<ISubTagContainer, Integer>(Materials.Sulfur, 3)
    );
    public static final Werkstoff FluorBuergerit = new Werkstoff(
            new short[]{0x20, 0x20, 0x20, 0},
            "Fluor-Buergerit",
            "NaFe3Al6(Si6O18)(BO3)3O3F",
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().addGems(),
            5,
            TextureSet.SET_RUBY,
            Arrays.asList(Materials.Sodium, Materials.Boron, Materials.Silicon),
            new Pair<ISubTagContainer, Integer>(Materials.Sodium, 1),
            new Pair<ISubTagContainer, Integer>(Materials.Iron, 3),
            new Pair<ISubTagContainer, Integer>(Materials.Aluminium, 6),
            new Pair<ISubTagContainer, Integer>(Materials.Silicon, 6),
            new Pair<ISubTagContainer, Integer>(Materials.Boron, 3),
            new Pair<ISubTagContainer, Integer>(Materials.Oxygen, 30),
            new Pair<ISubTagContainer, Integer>(Materials.Fluorine, 1)
    );
    public static final Werkstoff ChromoAluminoPovondrait = new Werkstoff(
            new short[]{0, 0x79, 0x6A, 0},
            "Chromo-Alumino-Povondraite",
            "NaCr3(Al4Mg2)(Si6O18)(BO3)3(OH)3O",
            Werkstoff.Types.getDefaultStatForType(Werkstoff.Types.COMPOUND),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().addGems(),
            7,
            TextureSet.SET_RUBY,
            Arrays.asList(Materials.Sodium, Materials.Boron, Materials.Silicon),
            new Pair<ISubTagContainer, Integer>(Materials.Sodium, 1),
            new Pair<ISubTagContainer, Integer>(Materials.Chrome, 3),
            new Pair<ISubTagContainer, Integer>(Materials.Magnalium, 6),
            new Pair<ISubTagContainer, Integer>(Materials.Silicon, 6),
            new Pair<ISubTagContainer, Integer>(Materials.Boron, 3),
            new Pair<ISubTagContainer, Integer>(Materials.Oxygen, 31),
            new Pair<ISubTagContainer, Integer>(Materials.Hydrogen, 3)
    );
    public static final Werkstoff VanadioOxyDravit = new Werkstoff(
            new short[]{0x60, 0xA0, 0xA0, 0},
            "Vanadio-Oxy-Dravite",
            "NaV3(Al4Mg2)(Si6O18)(BO3)3(OH)3O",
            Werkstoff.Types.getDefaultStatForType(Werkstoff.Types.COMPOUND),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().addGems(),
            8,
            TextureSet.SET_RUBY,
            Arrays.asList(Materials.Sodium, Materials.Boron, Materials.Silicon),
            new Pair<ISubTagContainer, Integer>(Materials.Sodium, 1),
            new Pair<ISubTagContainer, Integer>(Materials.Vanadium, 3),
            new Pair<ISubTagContainer, Integer>(Materials.Magnalium, 6),
            new Pair<ISubTagContainer, Integer>(Materials.Silicon, 6),
            new Pair<ISubTagContainer, Integer>(Materials.Boron, 3),
            new Pair<ISubTagContainer, Integer>(Materials.Oxygen, 31),
            new Pair<ISubTagContainer, Integer>(Materials.Hydrogen, 3)
    );

    //TODO: FREE ID RANGE: 3,4,6,19-32766
    public static final Werkstoff Olenit = new Werkstoff(
            new short[]{210, 210, 210, 0},
            "Olenite",
            "NaAl3Al6(Si6O18)(BO3)3O3OH",
            Werkstoff.Types.getDefaultStatForType(Werkstoff.Types.COMPOUND),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().addGems(),
            9,
            TextureSet.SET_RUBY,
            Arrays.asList(Materials.Sodium, Materials.Boron, Materials.Silicon),
            new Pair<ISubTagContainer, Integer>(Materials.Sodium, 1),
            new Pair<ISubTagContainer, Integer>(Materials.Aluminium, 9),
            new Pair<ISubTagContainer, Integer>(Materials.Silicon, 6),
            new Pair<ISubTagContainer, Integer>(Materials.Boron, 3),
            new Pair<ISubTagContainer, Integer>(Materials.Oxygen, 31),
            new Pair<ISubTagContainer, Integer>(Materials.Hydrogen, 1)
    );
    public static final Werkstoff Arsenopyrite = new Werkstoff(
            new short[]{0xB0, 0xB0, 0xB0, 0},
            "Arsenopyrite",
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures(),
            10,
            TextureSet.SET_METALLIC,
            Arrays.asList(Materials.Sulfur, Materials.Arsenic, Materials.Iron),
            new Pair<ISubTagContainer, Integer>(Materials.Iron, 1),
            new Pair<ISubTagContainer, Integer>(Materials.Arsenic, 1),
            new Pair<ISubTagContainer, Integer>(Materials.Sulfur, 1)
    );
    public static final Werkstoff Ferberite = new Werkstoff(
            new short[]{0xB0, 0xB0, 0xB0, 0},
            "Ferberite",
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures(),
            11,
            TextureSet.SET_METALLIC,
            Arrays.asList(Materials.Iron, Materials.Tungsten),
            new Pair<ISubTagContainer, Integer>(Materials.Iron, 1),
            new Pair<ISubTagContainer, Integer>(Materials.Tungsten, 1),
            new Pair<ISubTagContainer, Integer>(Materials.Oxygen, 3)
    );
    public static final Werkstoff Loellingit = new Werkstoff(
            new short[]{0xD0, 0xD0, 0xD0, 0},
            "Loellingite",
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures(),
            12,
            TextureSet.SET_METALLIC,
            Arrays.asList(Materials.Iron, Materials.Arsenic),
            new Pair<ISubTagContainer, Integer>(Materials.Iron, 1),
            new Pair<ISubTagContainer, Integer>(Materials.Arsenic, 2)
    );
    public static final Werkstoff Roquesit = new Werkstoff(
            new short[]{0xA0, 0xA0, 0xA0, 0},
            "Roquesite",
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures(),
            13,
            TextureSet.SET_METALLIC,
            Arrays.asList(Materials.Copper, Materials.Sulfur),
            new Pair<ISubTagContainer, Integer>(Materials.Copper, 1),
            new Pair<ISubTagContainer, Integer>(Materials.Indium, 1),
            new Pair<ISubTagContainer, Integer>(Materials.Sulfur, 2)
    );
    public static final Werkstoff Bornite = new Werkstoff(
            new short[]{0x97, 0x66, 0x2B, 0},
            "Bornite",
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures(),
            14,
            TextureSet.SET_METALLIC,
            Arrays.asList(Materials.Copper, Materials.Iron, Materials.Sulfur),
            new Pair<ISubTagContainer, Integer>(Materials.Copper, 5),
            new Pair<ISubTagContainer, Integer>(Materials.Iron, 1),
            new Pair<ISubTagContainer, Integer>(Materials.Sulfur, 4)
    );
    public static final Werkstoff Wittichenit = new Werkstoff(
            Materials.Copper.mRGBa,
            "Wittichenite",
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures(),
            15,
            TextureSet.SET_METALLIC,
            Arrays.asList(Materials.Copper, Materials.Bismuth, Materials.Sulfur),
            new Pair<ISubTagContainer, Integer>(Materials.Copper, 5),
            new Pair<ISubTagContainer, Integer>(Materials.Bismuth, 1),
            new Pair<ISubTagContainer, Integer>(Materials.Sulfur, 4)
    );
    public static final Werkstoff Djurleit = new Werkstoff(
            new short[]{0x60, 0x60, 0x60, 0},
            "Djurleite",
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures(),
            16,
            TextureSet.SET_METALLIC,
            Arrays.asList(Materials.Copper, Materials.Copper, Materials.Sulfur),
            new Pair<ISubTagContainer, Integer>(Materials.Copper, 31),
            new Pair<ISubTagContainer, Integer>(Materials.Sulfur, 16)
    );
    public static final Werkstoff Huebnerit = new Werkstoff(
            new short[]{0x80, 0x60, 0x60, 0},
            "Huebnerite",
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures(),
            17,
            TextureSet.SET_METALLIC,
            Arrays.asList(Materials.Manganese, Materials.Tungsten),
            new Pair<ISubTagContainer, Integer>(Materials.Manganese, 1),
            new Pair<ISubTagContainer, Integer>(Materials.Tungsten, 1),
            new Pair<ISubTagContainer, Integer>(Materials.Oxygen, 3)
    );
    public static final Werkstoff Thorianit = new Werkstoff(
            new short[]{0x30, 0x30, 0x30, 0},
            "Thorianit",
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures(),
            18,
            TextureSet.SET_METALLIC,
            Arrays.asList(Materials.Thorium),
            new Pair<ISubTagContainer, Integer>(Materials.Thorium, 1),
            new Pair<ISubTagContainer, Integer>(Materials.Oxygen, 2)
    );
    public static HashMap<OrePrefixes, BW_MetaGenerated_Items> items = new HashMap<>();
    public static Block BWOres;
    public boolean registered = false;

    public static ItemStack getCorresopndingItemStack(OrePrefixes orePrefixes, Werkstoff werkstoff) {
        return getCorresopndingItemStack(orePrefixes, werkstoff, 1);
    }

    public static ItemStack getCorresopndingItemStack(OrePrefixes orePrefixes, Werkstoff werkstoff, int amount) {
        if (orePrefixes == ore)
            return new ItemStack(BWOres, amount, werkstoff.getmID());
        return new ItemStack(items.get(orePrefixes), amount, werkstoff.getmID()).copy();
    }

    public void init() {
        if (INSTANCE == null)
            MainMod.LOGGER.error("INSTANCE IS NULL THIS SHOULD NEVER HAPPEN!");
    }

    public void runInit() {
        WerkstoffAdderRegistry.getINSTANCE().run();
        addSubTags();
        addItemsForGeneration();
    }

    @Override
    public void run() {
        if (!registered) {
            MainMod.LOGGER.info("Loading Processing Recipes for BW Materials");
            long timepre = System.nanoTime();
            ProgressManager.ProgressBar progressBar = ProgressManager.push("Register BW Materials", Werkstoff.werkstoffHashMap.size());

            for (short i = 0; i < Werkstoff.werkstoffHashMap.size(); i++) {
                Werkstoff werkstoff = Werkstoff.werkstoffHashMap.get(i);
                if (werkstoff == null || werkstoff.getmID() < 0) {
                    progressBar.step("");
                    continue;
                }
                addDustRecipes(werkstoff);
                addGemRecipes(werkstoff);
                addOreRecipes(werkstoff);
                addCrushedRecipes(werkstoff);
                progressBar.step(werkstoff.getDefaultName());
            }
            ProgressManager.pop(progressBar);
            long timepost = System.nanoTime();
            MainMod.LOGGER.info("Loading Processing Recipes for BW Materials took " + (timepost - timepre) + "ns/" + ((timepost - timepre) / 1000000) + "ms/" + ((timepost - timepre) / 1000000000) + "s!");
            registered = true;
        }
    }

    private void addSubTags() {
        for (Werkstoff W : Werkstoff.werkstoffHashMap.values()) {
            for (Pair<ISubTagContainer, Integer> pair : W.getContents().getValue().toArray(new Pair[0])) {

                if (pair.getKey() instanceof Materials && ((Materials) pair.getKey()) == Materials.Neodymium) {
                    W.add(SubTag.ELECTROMAGNETIC_SEPERATION_NEODYMIUM);
                    break;
                } else if (pair.getKey() instanceof Materials && ((Materials) pair.getKey()) == Materials.Iron) {
                    W.add(SubTag.ELECTROMAGNETIC_SEPERATION_IRON);
                    break;
                } else if (pair.getKey() instanceof Materials && ((Materials) pair.getKey()) == Materials.Gold) {
                    W.add(SubTag.ELECTROMAGNETIC_SEPERATION_GOLD);
                    break;
                }
            }
            if (W.getGenerationFeatures().hasGems()) {
                W.add(SubTag.CRYSTAL);
                W.add(SubTag.CRYSTALLISABLE);
            }
        }


    }

    private void addItemsForGeneration() {
        int toGenerateGlobal = 0b0000000;

        for (Werkstoff werkstoff : Werkstoff.werkstoffHashSet)
            toGenerateGlobal = (toGenerateGlobal | werkstoff.getGenerationFeatures().toGenerate);

        if ((toGenerateGlobal & 0b1) != 0) {
            items.put(dust, new BW_MetaGenerated_Items(dust));
            items.put(dustTiny, new BW_MetaGenerated_Items(dustTiny));
            items.put(dustSmall, new BW_MetaGenerated_Items(dustSmall));
        }
        if ((toGenerateGlobal & 0b10) != 0) {
            items.put(ingot, new BW_MetaGenerated_Items(ingot));

        }
        if ((toGenerateGlobal & 0b100) != 0) {
            items.put(gem, new BW_MetaGenerated_Items(gem));
            items.put(gemChipped, new BW_MetaGenerated_Items(gemChipped));
            items.put(gemExquisite, new BW_MetaGenerated_Items(gemExquisite));
            items.put(gemFlawed, new BW_MetaGenerated_Items(gemFlawed));
            items.put(gemFlawless, new BW_MetaGenerated_Items(gemFlawless));
        }
        if ((toGenerateGlobal & 0b1000) != 0) {
            if (FMLCommonHandler.instance().getSide().isClient())
                RenderingRegistry.registerBlockHandler(BW_Renderer_Block_Ores.INSTANCE);
            GameRegistry.registerTileEntity(BW_MetaGeneratedOreTE.class, "bw.blockoresTE");
            BWOres = new BW_MetaGenerated_Ores(Material.rock, BW_MetaGeneratedOreTE.class, "bw.blockores");
            GameRegistry.registerBlock(BWOres, BW_MetaGeneratedOre_Item.class, "bw.blockores.01");

            items.put(crushed, new BW_MetaGenerated_Items(crushed));
            items.put(crushedPurified, new BW_MetaGenerated_Items(crushedPurified));
            items.put(crushedCentrifuged, new BW_MetaGenerated_Items(crushedCentrifuged));
            items.put(dustPure, new BW_MetaGenerated_Items(dustPure));
            items.put(dustImpure, new BW_MetaGenerated_Items(dustImpure));
        }
    }

    private void addGemRecipes(Werkstoff werkstoff) {
        if (werkstoff.getGenerationFeatures().hasGems()) {

            GT_Values.RA.addSifterRecipe(
                    getCorresopndingItemStack(crushedPurified, werkstoff),
                    new ItemStack[]{
                            getCorresopndingItemStack(gemExquisite, werkstoff),
                            getCorresopndingItemStack(gemFlawless, werkstoff),
                            getCorresopndingItemStack(gem, werkstoff),
                            getCorresopndingItemStack(gemFlawed, werkstoff),
                            getCorresopndingItemStack(gemChipped, werkstoff),
                            getCorresopndingItemStack(dust, werkstoff)
                    },
                    new int[]{
                            100, 400, 1500, 2000, 4000, 5000
                    },
                    800,
                    16
            );
            GT_ModHandler.addPulverisationRecipe(werkstoff.get(gemExquisite), werkstoff.get(dust, 4));
            GT_ModHandler.addPulverisationRecipe(werkstoff.get(gemFlawless), werkstoff.get(dust, 2));
            GT_ModHandler.addPulverisationRecipe(werkstoff.get(gem), werkstoff.get(dust));
            GT_ModHandler.addPulverisationRecipe(werkstoff.get(gemFlawed), werkstoff.get(dustSmall, 1));
            GT_ModHandler.addPulverisationRecipe(werkstoff.get(gemChipped), werkstoff.get(dustTiny));

            GT_Values.RA.addForgeHammerRecipe(werkstoff.get(gemExquisite), werkstoff.get(gemFlawless, 2), 64, 16);
            GT_Values.RA.addForgeHammerRecipe(werkstoff.get(gemFlawless), werkstoff.get(gem, 2), 64, 16);
            GT_Values.RA.addForgeHammerRecipe(werkstoff.get(gem), werkstoff.get(gemFlawed, 2), 64, 16);
            GT_Values.RA.addForgeHammerRecipe(werkstoff.get(gemFlawed), werkstoff.get(gemChipped, 2), 64, 16);
            GT_Values.RA.addForgeHammerRecipe(werkstoff.get(gemChipped), werkstoff.get(dustTiny), 64, 16);

            GT_Values.RA.addImplosionRecipe(werkstoff.get(gemFlawless, 3), 8, werkstoff.get(gemExquisite), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2));
            GT_Values.RA.addImplosionRecipe(werkstoff.get(gem, 3), 8, werkstoff.get(gemFlawless), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2));
            GT_Values.RA.addImplosionRecipe(werkstoff.get(gemFlawed, 3), 8, werkstoff.get(gem), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2));
            GT_Values.RA.addImplosionRecipe(werkstoff.get(gemChipped, 3), 8, werkstoff.get(gemFlawed), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2));

            GT_Values.RA.addImplosionRecipe(werkstoff.get(dust, 4), 24, werkstoff.get(gem, 3), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 8));

        }
    }

    private void addDustRecipes(Werkstoff werkstoff) {
        if ((werkstoff.getGenerationFeatures().toGenerate & 0b1) != 0) {

            List<FluidStack> flOutputs = new ArrayList<>();
            List<ItemStack> stOutputs = new ArrayList<>();
            HashMap<ISubTagContainer, Pair<Integer, Integer>> tracker = new HashMap<>();
            int cells = 0;

            if (werkstoff.getStats().isElektrolysis() || werkstoff.getStats().isCentrifuge()) {
                for (Pair<ISubTagContainer, Integer> container : werkstoff.getContents().getValue().toArray(new Pair[0])) {
                    if (container.getKey() instanceof Materials) {
                        if (((Materials) container.getKey()).hasCorrespondingGas() || ((Materials) container.getKey()).hasCorrespondingFluid() || ((Materials) container.getKey()).mIconSet == TextureSet.SET_FLUID) {
                            FluidStack tmpFl = ((Materials) container.getKey()).getGas(1000 * container.getValue());
                            if (tmpFl == null || tmpFl.getFluid() == null) {
                                tmpFl = ((Materials) container.getKey()).getFluid(1000 * container.getValue());
                            }
                            flOutputs.add(tmpFl);
                            if (flOutputs.size() > 1) {
                                if (!tracker.containsKey(container.getKey())) {
                                    stOutputs.add(((Materials) container.getKey()).getCells(container.getValue()));
                                    tracker.put(container.getKey(), new Pair<>(container.getValue(), stOutputs.size() - 1));
                                } else {
                                    stOutputs.add(((Materials) container.getKey()).getCells(tracker.get(container.getKey()).getKey() + container.getValue()));
                                    stOutputs.remove(tracker.get(container.getKey()).getValue() + 1);
                                }
                                cells += container.getValue();
                            }
                        } else {
                            if (!tracker.containsKey(container.getKey())) {
                                stOutputs.add(((Materials) container.getKey()).getDust(container.getValue()));
                                tracker.put(container.getKey(), new Pair<>(container.getValue(), stOutputs.size() - 1));
                            } else {
                                stOutputs.add(((Materials) container.getKey()).getDust(tracker.get(container.getKey()).getKey() + container.getValue()));
                                stOutputs.remove(tracker.get(container.getKey()).getValue() + 1);
                            }
                        }
                    } else if (container.getKey() instanceof Werkstoff) {
                        if (((Werkstoff) container.getKey()).getTexSet() == TextureSet.SET_FLUID) {
                            //not yet implemented no fluids from me...
                        } else {
                            if (!tracker.containsKey(container.getKey())) {
                                stOutputs.add(((Werkstoff) container.getKey()).get(dust, container.getValue()));
                                tracker.put(container.getKey(), new Pair<>(container.getValue(), stOutputs.size() - 1));
                            } else {
                                stOutputs.add(((Werkstoff) container.getKey()).get(dust, (tracker.get(container.getKey()).getKey() + container.getValue())));
                                stOutputs.remove(tracker.get(container.getKey()).getValue() + 1);
                            }
                        }
                    }
                }
                ItemStack input = getCorresopndingItemStack(dust, werkstoff);
                input.stackSize = werkstoff.getContents().getKey();
                if (werkstoff.getStats().isElektrolysis())
                    GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes.addRecipe(true, new ItemStack[]{input, cells > 0 ? Materials.Empty.getCells(cells) : null}, stOutputs.toArray(new ItemStack[0]), (Object) null, null, new FluidStack[]{null}, new FluidStack[]{flOutputs.size() > 0 ? flOutputs.get(0) : null}, (int) Math.max(1L, Math.abs(werkstoff.getStats().protons * werkstoff.getContents().getValue().size())), Math.min(4, werkstoff.getContents().getValue().size()) * 30, 0);
                if (werkstoff.getStats().isCentrifuge())
                    GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.addRecipe(true, new ItemStack[]{input, cells > 0 ? Materials.Empty.getCells(cells) : null}, stOutputs.toArray(new ItemStack[0]), (Object) null, null, new FluidStack[]{null}, new FluidStack[]{flOutputs.size() > 0 ? flOutputs.get(0) : null}, (int) Math.max(1L, Math.abs(werkstoff.getStats().mass * werkstoff.getContents().getValue().size())), Math.min(4, werkstoff.getContents().getValue().size()) * 5, 0);
            }

            GT_ModHandler.addShapelessCraftingRecipe(getCorresopndingItemStack(dust, werkstoff), 0, new Object[]{
                    getCorresopndingItemStack(dustTiny, werkstoff),
                    getCorresopndingItemStack(dustTiny, werkstoff),
                    getCorresopndingItemStack(dustTiny, werkstoff),
                    getCorresopndingItemStack(dustTiny, werkstoff),
                    getCorresopndingItemStack(dustTiny, werkstoff),
                    getCorresopndingItemStack(dustTiny, werkstoff),
                    getCorresopndingItemStack(dustTiny, werkstoff),
                    getCorresopndingItemStack(dustTiny, werkstoff),
                    getCorresopndingItemStack(dustTiny, werkstoff)
            });
            GT_ModHandler.addShapelessCraftingRecipe(getCorresopndingItemStack(dust, werkstoff), 0, new Object[]{
                    getCorresopndingItemStack(dustSmall, werkstoff),
                    getCorresopndingItemStack(dustSmall, werkstoff),
                    getCorresopndingItemStack(dustSmall, werkstoff),
                    getCorresopndingItemStack(dustSmall, werkstoff)
            });
            GT_ModHandler.addCraftingRecipe(getCorresopndingItemStack(dustSmall, werkstoff, 4), new Object[]{
                    " T ", 'T', getCorresopndingItemStack(dust, werkstoff)
            });
            GT_ModHandler.addCraftingRecipe(getCorresopndingItemStack(dustTiny, werkstoff, 9), new Object[]{
                    "T  ", 'T', getCorresopndingItemStack(dust, werkstoff)
            });

            if ((werkstoff.getGenerationFeatures().toGenerate & 2) != 0 && !werkstoff.getStats().isBlastFurnace()) {
                GT_ModHandler.addSmeltingRecipe(getCorresopndingItemStack(dust, werkstoff), getCorresopndingItemStack(ingot, werkstoff));
                GT_ModHandler.addSmeltingRecipe(getCorresopndingItemStack(dustTiny, werkstoff), getCorresopndingItemStack(nugget, werkstoff));
            }

            if (werkstoff.contains(SubTag.CRYSTALLISABLE)) {
                GT_Values.RA.addAutoclaveRecipe(werkstoff.get(dustPure), Materials.Water.getFluid(200L), werkstoff.get(gem), 9000, 2000, 24);
                GT_Values.RA.addAutoclaveRecipe(werkstoff.get(dustImpure), Materials.Water.getFluid(200L), werkstoff.get(gem), 9000, 2000, 24);
                GT_Values.RA.addAutoclaveRecipe(werkstoff.get(dustPure), gregtech.api.util.GT_ModHandler.getDistilledWater(200L), werkstoff.get(gem), 9500, 1500, 24);
                GT_Values.RA.addAutoclaveRecipe(werkstoff.get(dustImpure), gregtech.api.util.GT_ModHandler.getDistilledWater(200L), werkstoff.get(gem), 9500, 1500, 24);
            }

        }
    }

    private void addOreRecipes(Werkstoff werkstoff) {
        if ((werkstoff.getGenerationFeatures().toGenerate & 2) != 0 && !werkstoff.getStats().isBlastFurnace())
            GT_ModHandler.addSmeltingRecipe(getCorresopndingItemStack(ore, werkstoff), getCorresopndingItemStack(ingot, werkstoff));

        if ((werkstoff.getGenerationFeatures().toGenerate & 0b1000) != 0) {
            GT_Values.RA.addForgeHammerRecipe(werkstoff.get(ore), werkstoff.getGenerationFeatures().hasGems() ? werkstoff.get(gem) : werkstoff.get(crushed), 16, 10);
            GT_ModHandler.addPulverisationRecipe(
                    werkstoff.get(ore),
                    werkstoff.get(crushed, 2),
                    werkstoff.getOreByProduct(0, gem) != null ? werkstoff.getOreByProduct(0, gem) : werkstoff.getOreByProduct(0, dust),
                    werkstoff.getNoOfByProducts() > 0 ? 10 : 0,
                    Materials.Stone.getDust(1),
                    50,
                    true);
        }
    }

    private void addCrushedRecipes(Werkstoff werkstoff) {
        if ((werkstoff.getGenerationFeatures().toGenerate & 0b1000) == 0 || (werkstoff.getGenerationFeatures().toGenerate & 0b1) == 0)
            return;

        GT_Values.RA.addForgeHammerRecipe(werkstoff.get(crushed), werkstoff.get(dustImpure), 10, 16);
        GT_ModHandler.addPulverisationRecipe(werkstoff.get(crushed), werkstoff.get(dustImpure), werkstoff.getOreByProduct(0, dust), 10, false);
        GT_ModHandler.addOreWasherRecipe(werkstoff.get(crushed), 1000, werkstoff.get(crushedPurified), werkstoff.getOreByProduct(0, dustTiny), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1L));
        GT_ModHandler.addThermalCentrifugeRecipe(werkstoff.get(crushed), (int) Math.min(5000L, Math.abs(werkstoff.getStats().protons * 20L)), werkstoff.get(crushedCentrifuged), werkstoff.getOreByProduct(1, dustTiny), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1L));

        GT_Values.RA.addForgeHammerRecipe(werkstoff.get(crushedPurified), werkstoff.get(dustPure), 10, 16);
        GT_ModHandler.addPulverisationRecipe(werkstoff.get(crushedPurified), werkstoff.get(dustPure), werkstoff.getOreByProduct(1, dust), 10, false);
        GT_ModHandler.addThermalCentrifugeRecipe(werkstoff.get(crushedPurified), (int) Math.min(5000L, Math.abs(werkstoff.getStats().protons * 20L)), werkstoff.get(crushedCentrifuged), werkstoff.getOreByProduct(1, dustTiny));

        GT_Values.RA.addForgeHammerRecipe(werkstoff.get(crushedCentrifuged), werkstoff.get(dust), 10, 16);
        GT_ModHandler.addPulverisationRecipe(werkstoff.get(crushedCentrifuged), werkstoff.get(dust), werkstoff.getOreByProduct(2, dust), 10, false);

        GT_Values.RA.addCentrifugeRecipe(werkstoff.get(dustImpure), 0, werkstoff.get(dust), werkstoff.getOreByProduct(0, dustTiny), null, null, null, null, (int) Math.max(1L, werkstoff.getStats().mass * 8L));
        GT_Values.RA.addCentrifugeRecipe(werkstoff.get(dustPure), 0, werkstoff.get(dust), werkstoff.getOreByProduct(1, dustTiny), null, null, null, null, (int) Math.max(1L, werkstoff.getStats().mass * 8L));


        if (werkstoff.contains(SubTag.WASHING_MERCURY))
            GT_Values.RA.addChemicalBathRecipe(werkstoff.get(crushed), Materials.Mercury.getFluid(1000L), werkstoff.get(crushedPurified), werkstoff.getOreByProduct(1, dust), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1L), new int[]{10000, 7000, 4000}, 800, 8);
        if (werkstoff.contains(SubTag.WASHING_SODIUMPERSULFATE))
            GT_Values.RA.addChemicalBathRecipe(werkstoff.get(crushed), Materials.SodiumPersulfate.getFluid(GT_Mod.gregtechproxy.mDisableOldChemicalRecipes ? 1000L : 100L), werkstoff.get(crushedPurified), werkstoff.getOreByProduct(1, dust), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1L), new int[]{10000, 7000, 4000}, 800, 8);
        if (werkstoff.contains(SubTag.ELECTROMAGNETIC_SEPERATION_GOLD))
            GT_Values.RA.addElectromagneticSeparatorRecipe(werkstoff.get(dustPure), werkstoff.get(dust), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Gold, 1L), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Gold, 1L), new int[]{10000, 4000, 2000}, 400, 24);
        else if (werkstoff.contains(SubTag.ELECTROMAGNETIC_SEPERATION_IRON))
            GT_Values.RA.addElectromagneticSeparatorRecipe(werkstoff.get(dustPure), werkstoff.get(dust), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Iron, 1L), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Iron, 1L), new int[]{10000, 4000, 2000}, 400, 24);
        else if (werkstoff.contains(SubTag.ELECTROMAGNETIC_SEPERATION_NEODYMIUM))
            GT_Values.RA.addElectromagneticSeparatorRecipe(werkstoff.get(dustPure), werkstoff.get(dust), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Neodymium, 1L), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Neodymium, 1L), new int[]{10000, 4000, 2000}, 400, 24);
    }

}