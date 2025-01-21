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

package bartworks.common.tileentities.multis;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.util.GTStructureUtility.ofHatchAdder;
import static gregtech.api.util.GTUtility.validMTEList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.common.items.SimpleSubItemClass;
import bartworks.util.MathUtils;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;

public class MTEThoriumHighTempReactor extends MTEEnhancedMultiBlockBase<MTEThoriumHighTempReactor> {

    private static final int BASECASINGINDEX = 44;
    private int mCasingAmount = 0;

    private static final int HELIUM_NEEDED = 730000;
    private static final int powerUsage = (int) TierEU.RECIPE_IV / 2;
    private static final int maxCapacity = 675000;
    private static final int minCapacityToStart = 100000;
    private int HeliumSupply;
    private int fuelSupply;
    private boolean emptyingMode;
    private int coolingPerTick = 0;

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEThoriumHighTempReactor> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEThoriumHighTempReactor>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] {
                    { "  BBBBBBB  ", " BBBBBBBBB ", "BBBBBBBBBBB", "BBBBBBBBBBB", "BBBBBBBBBBB", "BBBBBBBBBBB",
                        "BBBBBBBBBBB", "BBBBBBBBBBB", "BBBBBBBBBBB", " BBBBBBBBB ", "  BBBBBBB  " },
                    { "  ccccccc  ", " c-------c ", "c---------c", "c---------c", "c---------c", "c---------c",
                        "c---------c", "c---------c", "c---------c", " c-------c ", "  ccccccc  " },
                    { "  ccccccc  ", " c-------c ", "c---------c", "c---------c", "c---------c", "c---------c",
                        "c---------c", "c---------c", "c---------c", " c-------c ", "  ccccccc  " },
                    { "  ccccccc  ", " c-------c ", "c---------c", "c---------c", "c---------c", "c---------c",
                        "c---------c", "c---------c", "c---------c", " c-------c ", "  ccccccc  " },
                    { "  ccccccc  ", " c-------c ", "c---------c", "c---------c", "c---------c", "c---------c",
                        "c---------c", "c---------c", "c---------c", " c-------c ", "  ccccccc  " },
                    { "  ccccccc  ", " c-------c ", "c---------c", "c---------c", "c---------c", "c---------c",
                        "c---------c", "c---------c", "c---------c", " c-------c ", "  ccccccc  " },
                    { "  ccccccc  ", " c-------c ", "c---------c", "c---------c", "c---------c", "c---------c",
                        "c---------c", "c---------c", "c---------c", " c-------c ", "  ccccccc  " },
                    { "  ccccccc  ", " c-------c ", "c---------c", "c---------c", "c---------c", "c---------c",
                        "c---------c", "c---------c", "c---------c", " c-------c ", "  ccccccc  " },
                    { "  ccccccc  ", " c-------c ", "c---------c", "c---------c", "c---------c", "c---------c",
                        "c---------c", "c---------c", "c---------c", " c-------c ", "  ccccccc  " },
                    { "  ccccccc  ", " c-------c ", "c---------c", "c---------c", "c---------c", "c---------c",
                        "c---------c", "c---------c", "c---------c", " c-------c ", "  ccccccc  " },
                    { "  ccccccc  ", " c-------c ", "c---------c", "c---------c", "c---------c", "c---------c",
                        "c---------c", "c---------c", "c---------c", " c-------c ", "  ccccccc  " },
                    { "  bbb~bbb  ", " bbbbbbbbb ", "bbbbbbbbbbb", "bbbbbbbbbbb", "bbbbbbbbbbb", "bbbbbbbbbbb",
                        "bbbbbbbbbbb", "bbbbbbbbbbb", "bbbbbbbbbbb", " bbbbbbbbb ", "  bbbbbbb  " }, }))
        .addElement('c', onElementPass(x -> x.mCasingAmount++, ofBlock(GregTechAPI.sBlockCasings3, 12)))
        .addElement(
            'b',
            ofChain(
                ofHatchAdder(MTEThoriumHighTempReactor::addOutputToMachineList, BASECASINGINDEX, 1),
                ofHatchAdder(MTEThoriumHighTempReactor::addMaintenanceToMachineList, BASECASINGINDEX, 1),
                ofHatchAdder(MTEThoriumHighTempReactor::addEnergyInputToMachineList, BASECASINGINDEX, 1),
                onElementPass(x -> x.mCasingAmount++, ofBlock(GregTechAPI.sBlockCasings3, 12))))
        .addElement(
            'B',
            ofChain(
                ofHatchAdder(MTEThoriumHighTempReactor::addInputToMachineList, BASECASINGINDEX, 2),
                onElementPass(x -> x.mCasingAmount++, ofBlock(GregTechAPI.sBlockCasings3, 12))))
        // ofHatchAdderOptional(GT_TileEntity_THTR::addInputToMachineList, BASECASINGINDEX, 2,
        // GregTechAPI.sBlockCasings3, 12))
        .build();

    public MTEThoriumHighTempReactor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    private MTEThoriumHighTempReactor(String aName) {
        super(aName);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack itemStack) {
        return true;
    }

    @Override
    public IStructureDefinition<MTEThoriumHighTempReactor> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("High Temperature Reactor")
            .addInfo("Needs to be primed with " + GTUtility.formatNumbers(HELIUM_NEEDED) + " of helium")
            .addInfo("Needs a constant supply of coolant while running")
            .addInfo("Needs at least 100k Fuel pebbles to start operation (can hold up to 675k pebbles)")
            .addInfo("Consumes up to 0.5% of total Fuel Pellets per Operation depending on efficiency")
            .addInfo("Efficiency decreases exponentially if the internal buffer is not completely filled")
            .addInfo("Reactor will take 4 800L/t of coolant multiplied by efficiency")
            .addInfo("Uses " + GTUtility.formatNumbers(powerUsage) + " EU/t")
            .addInfo("One Operation takes 9 hours")
            .beginStructureBlock(11, 12, 11, true)
            .addController("Front bottom center")
            .addCasingInfoMin("Radiation Proof Casings", 500, false)
            .addStructureInfo("Corners and the 2 touching blocks are air (cylindric)")
            .addInputBus("Any top layer casing", 2)
            .addInputHatch("Any top layer casing", 2)
            .addOutputBus("Any bottom layer casing", 1)
            .addOutputHatch("Any bottom layer casing", 1)
            .addEnergyHatch("Any bottom layer casing", 1)
            .addMaintenanceHatch("Any bottom layer casing", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && f.isNotFlipped();
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 5, 11, 0);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack itemStack) {
        this.mCasingAmount = 0;
        return this.checkPiece(STRUCTURE_PIECE_MAIN, 5, 11, 0) && this.mCasingAmount >= 500
            && this.mMaintenanceHatches.size() == 1
            && !this.mInputHatches.isEmpty()
            && !this.mOutputHatches.isEmpty()
            && !this.mInputBusses.isEmpty()
            && !this.mOutputBusses.isEmpty()
            && !this.mEnergyHatches.isEmpty();
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.HeliumSupply = aNBT.getInteger("HeliumSupply");
        this.fuelSupply = aNBT.getInteger("fuelsupply");
        this.coolingPerTick = aNBT.getInteger("coolanttaking");
        this.emptyingMode = aNBT.getBoolean("EmptyMode");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("HeliumSupply", this.HeliumSupply);
        aNBT.setInteger("fuelsupply", this.fuelSupply);
        aNBT.setInteger("coolanttaking", this.coolingPerTick);
        aNBT.setBoolean("EmptyMode", this.emptyingMode);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && !this.emptyingMode) {
            if (this.HeliumSupply < MTEThoriumHighTempReactor.HELIUM_NEEDED) {
                for (FluidStack fluidStack : this.getStoredFluids()) {
                    if (fluidStack.isFluidEqual(Materials.Helium.getGas(1))) {
                        int toget = Math
                            .min(MTEThoriumHighTempReactor.HELIUM_NEEDED - this.HeliumSupply, fluidStack.amount);
                        fluidStack.amount -= toget;
                        this.HeliumSupply += toget;
                    }
                }
            }
            if (this.fuelSupply < maxCapacity) {
                this.startRecipeProcessing();
                for (ItemStack itemStack : this.getStoredInputs()) {
                    if (GTUtility.areStacksEqual(
                        itemStack,
                        new ItemStack(THTRMaterials.aTHTR_Materials, 1, THTRMaterials.MATERIAL_FUEL_INDEX))) {
                        int toget = Math.min(maxCapacity - this.fuelSupply, itemStack.stackSize);
                        if (toget == 0) continue;
                        itemStack.stackSize -= toget;
                        this.fuelSupply += toget;
                    }
                }
                this.endRecipeProcessing();
                this.updateSlots();
            }
        }
    }

    private double getEfficiency() {
        return Math.min(
            Math.pow((this.fuelSupply - minCapacityToStart) / ((maxCapacity - minCapacityToStart) / 10D), 2D) + 1,
            100D) / 100D - (this.getIdealStatus() - this.getRepairStatus()) / 10D;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        if (emptyingMode) {
            if (!(HeliumSupply > 0 || fuelSupply > 0)) return CheckRecipeResultRegistry.NO_RECIPE;
            this.mEfficiency = 10000;
            this.mMaxProgresstime = 100;
        } else {
            if (this.HeliumSupply < MTEThoriumHighTempReactor.HELIUM_NEEDED || this.fuelSupply < minCapacityToStart)
                return CheckRecipeResultRegistry.NO_RECIPE;

            double efficiency = getEfficiency();
            if (efficiency <= 0.0) return CheckRecipeResultRegistry.NO_RECIPE;

            int toReduce = MathUtils.floorInt(this.fuelSupply * 0.005D * efficiency);

            final int originalToReduce = toReduce;
            int burnedBalls = toReduce / 64;
            if (burnedBalls > 0) toReduce -= burnedBalls * 64;

            int meta = THTRMaterials.MATERIAL_USED_FUEL_INDEX;

            ItemStack[] toOutput = { new ItemStack(THTRMaterials.aTHTR_Materials, burnedBalls, meta),
                new ItemStack(THTRMaterials.aTHTR_Materials, toReduce, meta + 1) };
            if (!this.canOutputAll(toOutput)) return CheckRecipeResultRegistry.NO_RECIPE;

            this.fuelSupply -= originalToReduce;
            this.mOutputItems = toOutput;

            this.coolingPerTick = (int) (4800.0 * efficiency);
            this.mEfficiency = (int) (efficiency * 10000.0);
            this.mEUt = -powerUsage;
            this.mMaxProgresstime = 648000;
        }
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {

        if (this.emptyingMode) {
            this.addOutput(Materials.Helium.getGas(this.HeliumSupply));
            this.addOutput(
                new ItemStack(THTRMaterials.aTHTR_Materials, this.fuelSupply, THTRMaterials.MATERIAL_FUEL_INDEX));
            this.HeliumSupply = 0;
            this.fuelSupply = 0;
            this.updateSlots();
            return true;
        }

        if (!super.onRunningTick(aStack)) return false;

        int takecoolant = this.coolingPerTick;
        int drainedamount = 0;

        for (MTEHatchInput tHatch : validMTEList(mInputHatches)) {
            FluidStack tLiquid = tHatch.getFluid();
            if (tLiquid != null && tLiquid.isFluidEqual(FluidRegistry.getFluidStack("ic2coolant", 1))) {
                FluidStack drained = tHatch.drain(takecoolant, true);
                takecoolant -= drained.amount;
                drainedamount += drained.amount;
                if (takecoolant <= 0) break;
            }
        }

        if (drainedamount > 0) this.addOutput(FluidRegistry.getFluidStack("ic2hotcoolant", drainedamount));

        this.updateSlots();

        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack itemStack) {
        return 10000;
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
        return new MTEThoriumHighTempReactor(this.mName);
    }

    @Override
    public String[] getInfoData() {
        return new String[] { "Progress:",
            GTUtility.formatNumbers(this.mProgresstime / 20) + "secs /"
                + GTUtility.formatNumbers(this.mMaxProgresstime / 20)
                + "secs",
            "TRISO-Pebbles:",
            GTUtility.formatNumbers(this.fuelSupply) + "pcs. / " + GTUtility.formatNumbers(this.fuelSupply) + "psc.",
            "Helium-Level:",
            GTUtility.formatNumbers(this.HeliumSupply) + "L / "
                + GTUtility.formatNumbers(MTEThoriumHighTempReactor.HELIUM_NEEDED)
                + "L",
            "Coolant/t:", GTUtility.formatNumbers(this.mProgresstime == 0 ? 0 : this.coolingPerTick) + "L/t",
            "Problems:", String.valueOf(this.getIdealStatus() - this.getRepairStatus()) };
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] {
                Textures.BlockIcons.getCasingTextureForId(MTEThoriumHighTempReactor.BASECASINGINDEX),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] {
                Textures.BlockIcons.getCasingTextureForId(MTEThoriumHighTempReactor.BASECASINGINDEX),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(MTEThoriumHighTempReactor.BASECASINGINDEX) };
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (this.mMaxProgresstime > 0) {
            GTUtility.sendChatToPlayer(aPlayer, "THTR mode cannot be changed while the machine is running.");
            return;
        }
        this.emptyingMode = !this.emptyingMode;
        GTUtility.sendChatToPlayer(
            aPlayer,
            "THTR is now running in " + (this.emptyingMode ? "emptying mode." : "normal Operation"));
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    public static class THTRMaterials {

        public static final SimpleSubItemClass aTHTR_Materials = new SimpleSubItemClass(
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
            GameRegistry.registerItem(MTEThoriumHighTempReactor.THTRMaterials.aTHTR_Materials, "bw.THTRMaterials");
        }
    }
}
