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

package com.github.bartimaeusnek.bartworks.common.tileentities.multis.mega;

import com.github.bartimaeusnek.bartworks.API.LoaderReference;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.util.*;
import com.github.bartimaeusnek.crossmod.tectech.helper.TecTechUtils;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import cpw.mods.fml.common.Optional;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.MULTIBLOCK_ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS;
import static com.github.bartimaeusnek.bartworks.util.RecipeFinderForParallel.getMultiOutput;
import static com.github.bartimaeusnek.bartworks.util.RecipeFinderForParallel.handleParallelRecipe;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

@Optional.Interface(iface = "com.github.bartimaeusnek.crossmod.tectech.TecTechEnabledMulti", modid = "tectech", striprefs = true)
public class GT_TileEntity_MegaDistillTower extends GT_TileEntity_MegaMultiBlockBase<GT_TileEntity_MegaDistillTower> {
    protected static final int CASING_INDEX = 49;
    protected static final String STRUCTURE_PIECE_BASE = "base";
    protected static final String STRUCTURE_PIECE_LAYER = "layer";
    private static final IStructureDefinition<GT_TileEntity_MegaDistillTower> STRUCTURE_DEFINITION = StructureDefinition.<GT_TileEntity_MegaDistillTower>builder()
            .addShape(STRUCTURE_PIECE_BASE, transpose(new String[][]{
                    {"bbbbbbb~bbbbbbb", "bbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbb"},
            }))
            .addShape(STRUCTURE_PIECE_LAYER, transpose(new String[][]{
                    {"lllllllllllllll", "lcccccccccccccl", "lcccccccccccccl", "lcccccccccccccl", "lcccccccccccccl", "lcccccccccccccl", "lcccccccccccccl", "lcccccccccccccl", "lcccccccccccccl", "lcccccccccccccl", "lcccccccccccccl", "lcccccccccccccl", "lcccccccccccccl", "lcccccccccccccl", "lllllllllllllll"},
                    {"lllllllllllllll", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "lllllllllllllll"},
                    {"lllllllllllllll", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "lllllllllllllll"},
                    {"lllllllllllllll", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "lllllllllllllll"},
                    {"lllllllllllllll", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "l-------------l", "lllllllllllllll"},
            }))
            .addElement('b', ofChain(
                    ofHatchAdder(GT_TileEntity_MegaDistillTower::addEnergyInputToMachineList, CASING_INDEX, 1),
                    ofHatchAdder(GT_TileEntity_MegaDistillTower::addOutputToMachineList, CASING_INDEX, 1),
                    ofHatchAdder(GT_TileEntity_MegaDistillTower::addInputToMachineList, CASING_INDEX, 1),
                    ofHatchAdder(GT_TileEntity_MegaDistillTower::addMaintenanceToMachineList, CASING_INDEX, 1),
                    onElementPass(GT_TileEntity_MegaDistillTower::onCasingFound, ofBlock(GregTech_API.sBlockCasings4, 1))
            ))
            .addElement('l', ofChain(
                    ofHatchAdder(GT_TileEntity_MegaDistillTower::addEnergyInputToMachineList, CASING_INDEX, 1),
                    ofHatchAdder(GT_TileEntity_MegaDistillTower::addLayerOutputHatch, CASING_INDEX, 2),
                    ofHatchAdder(GT_TileEntity_MegaDistillTower::addMaintenanceToMachineList, CASING_INDEX, 1),
                    onElementPass(GT_TileEntity_MegaDistillTower::onCasingFound, ofBlock(GregTech_API.sBlockCasings4, 1))
            ))
			// this one is too complex and cannot be expressed with a combination of existing IStructureElement
			.addElement('c', new IStructureElement<GT_TileEntity_MegaDistillTower>() {
						@Override
						public boolean check(GT_TileEntity_MegaDistillTower t, World world, int x, int y, int z) {
							if (world.isAirBlock(x, y, z)) {
								if (t.mTopState < 1) {
									t.mTopState = 0;
									return true;
								}
								// definitely top - cannot be air
								return false;
							}
							// from here on we must be looking at a top layer, since it's not air
							if (t.mTopState == 0)
								// must be air but failed, so no
								return false;
							t.mTopState = 1;
							// hatch adder
							TileEntity tileEntity = world.getTileEntity(x, y, z);
							if (tileEntity instanceof IGregTechTileEntity) {
								IGregTechTileEntity entity = (IGregTechTileEntity) tileEntity;
								if (t.addLayerOutputHatch(entity, CASING_INDEX)) {
									t.onTopLayerFound(false);
									return true;
								}
							}
							// block adder
							if(world.getBlock(x, y, z) == GregTech_API.sBlockCasings4 && world.getBlockMetadata(x, y, z) == 1) {
								t.onTopLayerFound(true);
								return true;
							} else {
								return false;
							}
						}

						@Override
						public boolean spawnHint(GT_TileEntity_MegaDistillTower t, World world, int x, int y, int z, ItemStack trigger) {
							if (trigger.stackSize == 1)
								StructureLibAPI.hintParticle(world, x, y, z, GregTech_API.sBlockCasings4, 1);
							return true;
						}

						@Override
						public boolean placeBlock(GT_TileEntity_MegaDistillTower t, World world, int x, int y, int z, ItemStack trigger) {
							if (trigger.stackSize == 1) {
								world.setBlock(x, y, z, GregTech_API.sBlockCasings4, 1, 3);
								return true;
							}
							world.setBlockToAir(x, y, z);
							return false;
						}
					}
			)
            .build();
    protected final List<List<GT_MetaTileEntity_Hatch_Output>> mOutputHatchesByLayer = new ArrayList<>();
    protected int mHeight;
    protected int mCasing;
    protected boolean mTopLayerFound;

    // -1 => maybe top, maybe not, 0 => definitely not top, 1 => definitely top
    private int mTopState = -1;

    public GT_TileEntity_MegaDistillTower(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    private GT_TileEntity_MegaDistillTower(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_MegaDistillTower(this.mName);
    }

    protected void onCasingFound() {
        mCasing++;
    }

    protected boolean addLayerOutputHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null || aTileEntity.isDead() || !(aTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_Output))
            return false;
        while (mOutputHatchesByLayer.size() < mHeight)
            mOutputHatchesByLayer.add(new ArrayList<>());
        GT_MetaTileEntity_Hatch_Output tHatch = (GT_MetaTileEntity_Hatch_Output) aTileEntity.getMetaTileEntity();
        tHatch.updateTexture(aBaseCasingIndex);
        return mOutputHatchesByLayer.get(mHeight - 1).add(tHatch);
    }

    protected void onTopLayerFound(boolean aIsCasing) {
        mTopLayerFound = true;
        if (aIsCasing)
            onCasingFound();
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            if (aActive)
                return new ITexture[]{
                    Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE).extFacing().build(),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW).extFacing().glow().build()};
            return new ITexture[]{
                Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                TextureFactory.builder().addIcon(OVERLAY_FRONT_DISTILLATION_TOWER).extFacing().build(),
                TextureFactory.builder().addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_GLOW).extFacing().glow().build()};
        }
        return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(CASING_INDEX)};
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "DistillationTower.png");
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sDistillationRecipes;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Distillery")
                .addInfo("Controller block for the Distillation Tower")
                .addInfo("Fluids are only put out at the correct height")
                .addInfo("The correct height equals the slot number in the NEI recipe")
                .addSeparator()
                .beginVariableStructureBlock(15, 15, 16, 56, 15, 15, true)
                .addController("Front bottom")
                .addOtherStructurePart("Clean Stainless Steel Machine Casing", "15 x h - 5 (minimum)")
                .addEnergyHatch("Any casing")
                .addMaintenanceHatch("Any casing")
                .addInputHatch("Any bottom layer casing")
                .addOutputBus("Any bottom layer casing")
                .addOutputHatch("2-11x Output Hatches (One per Output Layer except bottom layer)")
                .addStructureInfo("An \"Output Layer\" consists of 5 layers!")
                .addStructureHint("This Mega Multiblock is too big to have its structure hologram displayed fully.")
                .toolTipFinisher(MULTIBLOCK_ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS);
        return tt;
    }

    @Override
    public IStructureDefinition<GT_TileEntity_MegaDistillTower> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (LoaderReference.tectech) {
            this.getTecTechEnergyMultis().clear();
            this.getTecTechEnergyTunnels().clear();
        }
        // reset
        mOutputHatchesByLayer.forEach(List::clear);
        mHeight = 1;
        mTopLayerFound = false;
        mTopState = -1;

        // check base
        if (!checkPiece(STRUCTURE_PIECE_BASE, 7, 0, 0))
            return false;

        // check each layer
        while (mHeight < 12 && checkPiece(STRUCTURE_PIECE_LAYER, 7, mHeight * 5, 0) && !mTopLayerFound) {
			if (mOutputHatchesByLayer.get(mHeight - 1).isEmpty())
				// layer without output hatch
				return false;
			mTopState = -1;
			// not top
			mHeight++;
		}

        // validate final invariants...
        return mCasing >= 75 * mHeight + 10 && mHeight >= 2 && mTopLayerFound && mMaintenanceHatches.size() == 1;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        // no op, to big to be displayed
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {

        ItemStack[] tItems = getCompactedInputs();

        ArrayList<FluidStack> tFluidList = this.getStoredFluids();

        for (int i = 0; i < tFluidList.size() - 1; ++i) {
            for (int j = i + 1; j < tFluidList.size(); ++j) {
                if (GT_Utility.areFluidsEqual(tFluidList.get(i), tFluidList.get(j))) {
                    if (tFluidList.get(i).amount < tFluidList.get(j).amount) {
                        tFluidList.remove(i--);
                        break;
                    }
                    tFluidList.remove(j--);
                }
            }
        }

        long tVoltage = this.getMaxInputVoltage();
        byte tTier = (byte) Math.max(0, Math.min(GT_Utility.getTier(tVoltage), V.length - 1));

        long nominalV = LoaderReference.tectech ? TecTechUtils.getnominalVoltageTT(this) : BW_Util.getnominalVoltage(this);
        FluidStack[] tFluids = tFluidList.toArray(new FluidStack[0]);
        if (tFluids.length > 0) {
            for (FluidStack tFluid : tFluids) {
                ArrayList<FluidStack> outputFluids = new ArrayList<>();
                ArrayList<ItemStack> outputItems = new ArrayList<>();
                Pair<ArrayList<FluidStack>, ArrayList<ItemStack>> Outputs;
                int processed = 0;
                boolean found_Recipe = false;
                GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sDistillationRecipes.findRecipe(this.getBaseMetaTileEntity(), false, GT_Values.V[tTier], new FluidStack[]{tFluid}, tItems);

                if (tRecipe != null) {
                    found_Recipe = true;
                    long tMaxPara = Math.min(ConfigHandler.megaMachinesMax, nominalV / tRecipe.mEUt);
                    int tCurrentPara = handleParallelRecipe(tRecipe, new FluidStack[]{tFluid}, null, (int) tMaxPara);
                    this.updateSlots();
                    processed = tCurrentPara;
                    Outputs = getMultiOutput(tRecipe, tCurrentPara);
                    outputFluids = Outputs.getKey();
                    outputItems = Outputs.getValue();
                }

                if (!found_Recipe)
                    continue;
                this.mEfficiency = (10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000);
                this.mEfficiencyIncrease = 10000;
                long actualEUT = ((long)tRecipe.mEUt) * processed;
                calculateOverclockedNessMulti(actualEUT, tRecipe.mDuration, nominalV);
                //In case recipe is too OP for that machine
                if (this.mMaxProgresstime == Integer.MAX_VALUE - 1 && this.lEUt == Integer.MAX_VALUE - 1)
                    return false;
                if (this.lEUt > 0) {
                    this.lEUt = (-this.lEUt);
                }
                this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
                this.mOutputFluids = outputFluids.toArray(new FluidStack[0]);
                if (!outputItems.isEmpty())
                    this.mOutputItems = outputItems.toArray(new ItemStack[0]);
                else
                    this.mOutputItems = null;
                return true;
            }
        }
        return false;
    }

    @Override
    protected void addFluidOutputs(FluidStack[] mOutputFluids2) {
        for (int i = 0; i < mOutputFluids2.length && i < mOutputHatchesByLayer.size(); i++) {
            FluidStack tStack = mOutputFluids2[i].copy();
            if (!dumpFluid(mOutputHatchesByLayer.get(i), tStack, true))
                dumpFluid(mOutputHatchesByLayer.get(i), tStack, false);
        }
    }

}
