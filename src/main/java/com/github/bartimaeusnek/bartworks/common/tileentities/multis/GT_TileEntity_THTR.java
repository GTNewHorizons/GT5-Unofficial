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

package com.github.bartimaeusnek.bartworks.common.tileentities.multis;

import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.MULTIBLOCK_ADDED_BY_BARTWORKS;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

import com.github.bartimaeusnek.bartworks.common.items.SimpleSubItemClass;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.github.bartimaeusnek.bartworks.util.MathUtils;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import java.util.Arrays;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class GT_TileEntity_THTR extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_TileEntity_THTR> {

    private static final int BASECASINGINDEX = 44;

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_TileEntity_THTR> STRUCTURE_DEFINITION =
            StructureDefinition.<GT_TileEntity_THTR>builder()
                    .addShape(STRUCTURE_PIECE_MAIN, transpose(new String[][] {
                        {
                            "  BBBBBBB  ",
                            " BBBBBBBBB ",
                            "BBBBBBBBBBB",
                            "BBBBBBBBBBB",
                            "BBBBBBBBBBB",
                            "BBBBBBBBBBB",
                            "BBBBBBBBBBB",
                            "BBBBBBBBBBB",
                            "BBBBBBBBBBB",
                            " BBBBBBBBB ",
                            "  BBBBBBB  "
                        },
                        {
                            "  ccccccc  ",
                            " c-------c ",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            " c-------c ",
                            "  ccccccc  "
                        },
                        {
                            "  ccccccc  ",
                            " c-------c ",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            " c-------c ",
                            "  ccccccc  "
                        },
                        {
                            "  ccccccc  ",
                            " c-------c ",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            " c-------c ",
                            "  ccccccc  "
                        },
                        {
                            "  ccccccc  ",
                            " c-------c ",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            " c-------c ",
                            "  ccccccc  "
                        },
                        {
                            "  ccccccc  ",
                            " c-------c ",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            " c-------c ",
                            "  ccccccc  "
                        },
                        {
                            "  ccccccc  ",
                            " c-------c ",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            " c-------c ",
                            "  ccccccc  "
                        },
                        {
                            "  ccccccc  ",
                            " c-------c ",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            " c-------c ",
                            "  ccccccc  "
                        },
                        {
                            "  ccccccc  ",
                            " c-------c ",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            " c-------c ",
                            "  ccccccc  "
                        },
                        {
                            "  ccccccc  ",
                            " c-------c ",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            " c-------c ",
                            "  ccccccc  "
                        },
                        {
                            "  ccccccc  ",
                            " c-------c ",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            "c---------c",
                            " c-------c ",
                            "  ccccccc  "
                        },
                        {
                            "  bbb~bbb  ",
                            " bbbbbbbbb ",
                            "bbbbbbbbbbb",
                            "bbbbbbbbbbb",
                            "bbbbbbbbbbb",
                            "bbbbbbbbbbb",
                            "bbbbbbbbbbb",
                            "bbbbbbbbbbb",
                            "bbbbbbbbbbb",
                            " bbbbbbbbb ",
                            "  bbbbbbb  "
                        },
                    }))
                    .addElement('c', onElementPass(x -> x.mCasing++, ofBlock(GregTech_API.sBlockCasings3, 12)))
                    .addElement(
                            'b',
                            ofChain(
                                    ofHatchAdder(GT_TileEntity_THTR::addOutputToMachineList, BASECASINGINDEX, 1),
                                    ofHatchAdder(GT_TileEntity_THTR::addMaintenanceToMachineList, BASECASINGINDEX, 1),
                                    ofHatchAdder(GT_TileEntity_THTR::addEnergyInputToMachineList, BASECASINGINDEX, 1),
                                    onElementPass(x -> x.mCasing++, ofBlock(GregTech_API.sBlockCasings3, 12))))
                    .addElement(
                            'B',
                            ofChain(
                                    ofHatchAdder(GT_TileEntity_THTR::addInputToMachineList, BASECASINGINDEX, 2),
                                    onElementPass(x -> x.mCasing++, ofBlock(GregTech_API.sBlockCasings3, 12))))
                    // ofHatchAdderOptional(GT_TileEntity_THTR::addInputToMachineList, BASECASINGINDEX, 2,
                    // GregTech_API.sBlockCasings3, 12))
                    .build();

    private static final int HELIUM_NEEDED = 730000;
    private static final int powerUsage = BW_Util.getMachineVoltageFromTier(5) / 2;
    private static final int maxcapacity = 675000;
    private static final int mincapacity = 100000;
    private int HeliumSupply;
    private int fuelsupply;
    private boolean empty;
    private int coolanttaking = 0;
    private int mCasing = 0;

    public GT_TileEntity_THTR(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    private GT_TileEntity_THTR(String aName) {
        super(aName);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack itemStack) {
        return true;
    }

    @Override
    public IStructureDefinition<GT_TileEntity_THTR> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("High Temperature Reactor")
                .addInfo("Controller block for the Thorium High Temperature Reactor (THTR)")
                .addInfo("Needs a constant supply of coolant while running")
                .addInfo("Needs at least 100k Fuel pebbles to start operation (can hold up to 675k pebbles)")
                .addInfo("Consumes up to 0.5% of total Fuel Pellets per Operation depending on efficiency")
                .addInfo(
                        "Efficiency is calculated exponentially depending on the amount of pebbles in the internal buffer")
                .addInfo("Reactor will take 4 800L/t of coolant multiplied by efficiency")
                .addInfo("Uses " + GT_Utility.formatNumbers(powerUsage) + " EU/t")
                .addInfo("One Operation takes 9 hour")
                .addSeparator()
                .beginStructureBlock(11, 12, 11, true)
                .addController("Front bottom center")
                .addCasingInfo("Radiation Proof Casings", 500)
                .addStructureInfo("Corners and the 2 touching blocks are air (cylindric)")
                .addInputBus("Any top layer casing", 2)
                .addInputHatch("Any top layer casing", 2)
                .addOutputBus("Any bottom layer casing", 1)
                .addOutputHatch("Any bottom layer casing", 1)
                .addEnergyHatch("Any bottom layer casing", 1)
                .addMaintenanceHatch("Any bottom layer casing", 1)
                .toolTipFinisher(MULTIBLOCK_ADDED_BY_BARTWORKS);
        return tt;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && f.isNotFlipped();
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 5, 11, 0);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack itemStack) {
        this.mCasing = 0;
        return (checkPiece(STRUCTURE_PIECE_MAIN, 5, 11, 0)
                && this.mCasing >= 500
                && this.mMaintenanceHatches.size() == 1
                && this.mInputHatches.size() > 0
                && this.mOutputHatches.size() > 0
                && this.mInputBusses.size() > 0
                && this.mOutputBusses.size() > 0
                && this.mEnergyHatches.size() > 0);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.HeliumSupply = aNBT.getInteger("HeliumSupply");
        this.fuelsupply = aNBT.getInteger("fuelsupply");
        this.coolanttaking = aNBT.getInteger("coolanttaking");
        this.empty = aNBT.getBoolean("EmptyMode");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("HeliumSupply", this.HeliumSupply);
        aNBT.setInteger("fuelsupply", this.fuelsupply);
        aNBT.setInteger("coolanttaking", this.coolanttaking);
        aNBT.setBoolean("EmptyMode", this.empty);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && !this.empty) {
            if (this.HeliumSupply < GT_TileEntity_THTR.HELIUM_NEEDED) {
                for (FluidStack fluidStack : this.getStoredFluids()) {
                    if (fluidStack.isFluidEqual(Materials.Helium.getGas(1))) {
                        int toget = Math.min(GT_TileEntity_THTR.HELIUM_NEEDED - this.HeliumSupply, fluidStack.amount);
                        fluidStack.amount -= toget;
                        this.HeliumSupply += toget;
                        if (GT_TileEntity_THTR.HELIUM_NEEDED == this.HeliumSupply && fluidStack.amount == 0)
                            fluidStack = null;
                    }
                }
            }
            if (this.fuelsupply < maxcapacity) {
                startRecipeProcessing();
                for (ItemStack itemStack : this.getStoredInputs()) {
                    if (GT_Utility.areStacksEqual(
                            itemStack,
                            new ItemStack(THTRMaterials.aTHTR_Materials, 1, THTRMaterials.MATERIAL_FUEL_INDEX))) {
                        int toget = Math.min(maxcapacity - this.fuelsupply, itemStack.stackSize);
                        if (toget == 0) continue;
                        itemStack.stackSize -= toget;
                        this.fuelsupply += toget;
                    }
                }
                endRecipeProcessing();
                this.updateSlots();
            }
        }
    }

    @Override
    public boolean checkRecipe(ItemStack controllerStack) {

        if (this.empty) {
            if (this.HeliumSupply > 0 || this.fuelsupply > 0) {
                this.mEfficiency = 10000;
                this.mMaxProgresstime = 100;
                return true;
            }
            return false;
        }
        if (!(this.HeliumSupply >= GT_TileEntity_THTR.HELIUM_NEEDED && this.fuelsupply >= mincapacity)) return false;

        double eff = Math.min(
                                Math.pow(
                                                (double) (this.fuelsupply - mincapacity)
                                                        / ((maxcapacity - mincapacity) / 10D),
                                                2D)
                                        + 1,
                                100D)
                        / 100D
                - ((double) (getIdealStatus() - getRepairStatus()) / 10D);
        if (eff <= 0D) return false;

        int toReduce = MathUtils.floorInt((double) this.fuelsupply * 0.005D * eff);

        this.fuelsupply -= toReduce;
        int burnedballs = toReduce / 64;
        if (burnedballs > 0) toReduce -= burnedballs * 64;

        int meta = THTRMaterials.MATERIAL_USED_FUEL_INDEX;

        this.mOutputItems = new ItemStack[] {
            new ItemStack(THTRMaterials.aTHTR_Materials, burnedballs, meta),
            new ItemStack(THTRMaterials.aTHTR_Materials, toReduce, meta + 1)
        };

        // this.updateSlots(); not needed ?

        this.coolanttaking = (int) (4800D * eff);
        this.mEfficiency = (int) (eff * 10000D);
        this.mEUt = -powerUsage;
        this.mMaxProgresstime = 648000;
        return true;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {

        if (this.empty) {
            this.addOutput(Materials.Helium.getGas(this.HeliumSupply));
            this.addOutput(
                    new ItemStack(THTRMaterials.aTHTR_Materials, this.fuelsupply, THTRMaterials.MATERIAL_FUEL_INDEX));
            this.HeliumSupply = 0;
            this.fuelsupply = 0;
            this.updateSlots();
            return true;
        }

        if (!super.onRunningTick(aStack)) return false;

        int takecoolant = coolanttaking;
        int drainedamount = 0;

        for (GT_MetaTileEntity_Hatch_Input tHatch : this.mInputHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                FluidStack tLiquid = tHatch.getFluid();
                if (tLiquid != null && tLiquid.isFluidEqual(FluidRegistry.getFluidStack("ic2coolant", 1))) {
                    FluidStack drained = tHatch.drain(takecoolant, true);
                    takecoolant -= drained.amount;
                    drainedamount += drained.amount;
                    if (takecoolant <= 0) break;
                }
            }
        }

        if (drainedamount > 0) addOutput(FluidRegistry.getFluidStack("ic2hotcoolant", drainedamount));

        this.updateSlots();

        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack itemStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerTick(ItemStack itemStack) {
        return 0;
    }

    @Override
    public int getDamageToComponent(ItemStack itemStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack itemStack) {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_TileEntity_THTR(this.mName);
    }

    @Override
    public String[] getInfoData() {
        return new String[] {
            "Progress:",
                    GT_Utility.formatNumbers(this.mProgresstime / 20) + "secs /"
                            + GT_Utility.formatNumbers(this.mMaxProgresstime / 20) + "secs",
            "TRISO-Pebbles:",
                    GT_Utility.formatNumbers(this.fuelsupply) + "pcs. / " + GT_Utility.formatNumbers(this.fuelsupply)
                            + "psc.",
            "Helium-Level:",
                    GT_Utility.formatNumbers(this.HeliumSupply) + "L / "
                            + GT_Utility.formatNumbers(GT_TileEntity_THTR.HELIUM_NEEDED) + "L",
            "Coolant/t:", GT_Utility.formatNumbers(this.mProgresstime == 0 ? 0 : coolanttaking) + "L/t",
            "Problems:", String.valueOf(this.getIdealStatus() - this.getRepairStatus())
        };
    }

    @Override
    public ITexture[] getTexture(
            IGregTechTileEntity aBaseMetaTileEntity,
            byte aSide,
            byte aFacing,
            byte aColorIndex,
            boolean aActive,
            boolean aRedstone) {
        if (aSide == aFacing) {
            if (aActive)
                return new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(GT_TileEntity_THTR.BASECASINGINDEX),
                    TextureFactory.builder()
                            .addIcon(Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE)
                            .extFacing()
                            .build(),
                    TextureFactory.builder()
                            .addIcon(Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE_GLOW)
                            .extFacing()
                            .glow()
                            .build()
                };
            return new ITexture[] {
                Textures.BlockIcons.getCasingTextureForId(GT_TileEntity_THTR.BASECASINGINDEX),
                TextureFactory.builder()
                        .addIcon(Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER)
                        .extFacing()
                        .build(),
                TextureFactory.builder()
                        .addIcon(Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_GLOW)
                        .extFacing()
                        .glow()
                        .build()
            };
        }
        return new ITexture[] {Textures.BlockIcons.getCasingTextureForId(GT_TileEntity_THTR.BASECASINGINDEX)};
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (this.mMaxProgresstime > 0) {
            GT_Utility.sendChatToPlayer(aPlayer, "THTR mode cannot be changed while the machine is running.");
            return;
        }
        this.empty = !this.empty;
        GT_Utility.sendChatToPlayer(
                aPlayer, "THTR is now running in " + (this.empty ? "emptying mode." : "normal Operation"));
    }

    public static class THTRMaterials {
        static final SimpleSubItemClass aTHTR_Materials = new SimpleSubItemClass(
                "BISOPelletCompound", // 0
                "BISOPelletBall", // 1
                "TRISOPelletCompound", // 2
                "TRISOPelletBall", // 3
                "TRISOPellet", // 4
                "BurnedOutTRISOPelletBall", // 5
                "BurnedOutTRISOPellet" // 6
                );
        public static final int MATERIAL_FUEL_INDEX = 4;
        public static final int MATERIAL_USED_FUEL_INDEX = 5;

        public static void registeraTHR_Materials() {
            GameRegistry.registerItem(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, "bw.THTRMaterials");
        }

        public static void registerTHR_Recipes() {
            GT_Values.RA.addCentrifugeRecipe(
                    Materials.Thorium.getDust(1),
                    GT_Values.NI,
                    GT_Values.NF,
                    GT_Values.NF,
                    Materials.Thorium.getDust(1),
                    Materials.Thorium.getDust(1),
                    WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 1),
                    WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 1),
                    WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 1),
                    GT_Values.NI,
                    new int[] {800, 375, 22, 22, 5},
                    10000,
                    BW_Util.getMachineVoltageFromTier(4));
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] {
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Lead, 6),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1)
                    },
                    Materials.Concrete.getMolten(1296),
                    new ItemStack(GregTech_API.sBlockCasings3, 1, 12),
                    40,
                    BW_Util.getMachineVoltageFromTier(5));
            GT_Values.RA.addMixerRecipe(
                    WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 10),
                    Materials.Uranium235.getDust(1),
                    GT_Utility.getIntegratedCircuit(2),
                    null,
                    null,
                    null,
                    new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials),
                    400,
                    30);
            GT_Values.RA.addFormingPressRecipe(
                    new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials),
                    Materials.Graphite.getDust(64),
                    new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, 1, 1),
                    40,
                    30);
            GT_Values.RA.addFormingPressRecipe(
                    new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, 1, 1),
                    Materials.Silicon.getDust(64),
                    new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, 1, 2),
                    40,
                    30);
            GT_Values.RA.addFormingPressRecipe(
                    new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, 1, 2),
                    Materials.Graphite.getDust(64),
                    new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, 1, 3),
                    40,
                    30);
            ItemStack[] pellets = new ItemStack[6];
            Arrays.fill(pellets, new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, 64, 4));
            GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.addRecipe(
                    false,
                    new ItemStack[] {
                        new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, 1, 3),
                        GT_Utility.getIntegratedCircuit(17)
                    },
                    pellets,
                    null,
                    null,
                    null,
                    null,
                    48000,
                    30,
                    0);
            GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.addRecipe(
                    false,
                    new ItemStack[] {
                        new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, 1, 5),
                        GT_Utility.getIntegratedCircuit(17)
                    },
                    new ItemStack[] {new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, 64, 6)},
                    null,
                    null,
                    null,
                    null,
                    48000,
                    30,
                    0);
            GT_Values.RA.addCentrifugeRecipe(
                    new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, 1, 6),
                    GT_Values.NI,
                    GT_Values.NF,
                    GT_Values.NF,
                    Materials.Lead.getDust(1),
                    GT_Values.NI,
                    GT_Values.NI,
                    GT_Values.NI,
                    GT_Values.NI,
                    GT_Values.NI,
                    new int[] {300},
                    1200,
                    30);
        }
    }
}
