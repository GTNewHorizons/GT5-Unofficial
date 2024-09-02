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

import static bartworks.util.BWTooltipReference.MULTIBLOCK_ADDED_BY_BARTWORKS;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.util.GTStructureUtility.ofHatchAdder;
import static gregtech.api.util.GTUtility.filterValidMTEs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

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
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;

public class MTEThoriumHighTempReactor extends MTEEnhancedMultiBlockBase<MTEThoriumHighTempReactor> {

    private static final int BASECASINGINDEX = 44;

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
        .addElement('c', onElementPass(x -> x.mCasing++, ofBlock(GregTechAPI.sBlockCasings3, 12)))
        .addElement(
            'b',
            ofChain(
                ofHatchAdder(MTEThoriumHighTempReactor::addOutputToMachineList, BASECASINGINDEX, 1),
                ofHatchAdder(MTEThoriumHighTempReactor::addMaintenanceToMachineList, BASECASINGINDEX, 1),
                ofHatchAdder(MTEThoriumHighTempReactor::addEnergyInputToMachineList, BASECASINGINDEX, 1),
                onElementPass(x -> x.mCasing++, ofBlock(GregTechAPI.sBlockCasings3, 12))))
        .addElement(
            'B',
            ofChain(
                ofHatchAdder(MTEThoriumHighTempReactor::addInputToMachineList, BASECASINGINDEX, 2),
                onElementPass(x -> x.mCasing++, ofBlock(GregTechAPI.sBlockCasings3, 12))))
        // ofHatchAdderOptional(GT_TileEntity_THTR::addInputToMachineList, BASECASINGINDEX, 2,
        // GregTechAPI.sBlockCasings3, 12))
        .build();

    private static final int HELIUM_NEEDED = 730000;
    private static final int powerUsage = (int) TierEU.RECIPE_IV / 2;
    private static final int maxcapacity = 675000;
    private static final int mincapacity = 100000;
    private int HeliumSupply;
    private int fuelsupply;
    private boolean empty;
    private int coolanttaking = 0;
    private int mCasing = 0;

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
            .addInfo("Controller block for the Thorium High Temperature Reactor (THTR)")
            .addInfo("Needs to be primed with " + GTUtility.formatNumbers(HELIUM_NEEDED) + " of helium")
            .addInfo("Needs a constant supply of coolant while running")
            .addInfo("Needs at least 100k Fuel pebbles to start operation (can hold up to 675k pebbles)")
            .addInfo("Consumes up to 0.5% of total Fuel Pellets per Operation depending on efficiency")
            .addInfo("Efficiency decreases exponentially if the internal buffer is not completely filled")
            .addInfo("Reactor will take 4 800L/t of coolant multiplied by efficiency")
            .addInfo("Uses " + GTUtility.formatNumbers(powerUsage) + " EU/t")
            .addInfo("One Operation takes 9 hours")
            .addSeparator()
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
            .toolTipFinisher(MULTIBLOCK_ADDED_BY_BARTWORKS);
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
        this.mCasing = 0;
        return this.checkPiece(STRUCTURE_PIECE_MAIN, 5, 11, 0) && this.mCasing >= 500
            && this.mMaintenanceHatches.size() == 1
            && this.mInputHatches.size() > 0
            && this.mOutputHatches.size() > 0
            && this.mInputBusses.size() > 0
            && this.mOutputBusses.size() > 0
            && this.mEnergyHatches.size() > 0;
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
            if (this.HeliumSupply < MTEThoriumHighTempReactor.HELIUM_NEEDED) {
                for (FluidStack fluidStack : this.getStoredFluids()) {
                    if (fluidStack.isFluidEqual(Materials.Helium.getGas(1))) {
                        int toget = Math
                            .min(MTEThoriumHighTempReactor.HELIUM_NEEDED - this.HeliumSupply, fluidStack.amount);
                        fluidStack.amount -= toget;
                        this.HeliumSupply += toget;
                        if (MTEThoriumHighTempReactor.HELIUM_NEEDED == this.HeliumSupply && fluidStack.amount == 0)
                            fluidStack = null;
                    }
                }
            }
            if (this.fuelsupply < maxcapacity) {
                this.startRecipeProcessing();
                for (ItemStack itemStack : this.getStoredInputs()) {
                    if (GTUtility.areStacksEqual(
                        itemStack,
                        new ItemStack(THTRMaterials.aTHTR_Materials, 1, THTRMaterials.MATERIAL_FUEL_INDEX))) {
                        int toget = Math.min(maxcapacity - this.fuelsupply, itemStack.stackSize);
                        if (toget == 0) continue;
                        itemStack.stackSize -= toget;
                        this.fuelsupply += toget;
                    }
                }
                this.endRecipeProcessing();
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
        if (this.HeliumSupply < MTEThoriumHighTempReactor.HELIUM_NEEDED || this.fuelsupply < mincapacity) return false;

        double eff = Math
            .min(Math.pow((this.fuelsupply - mincapacity) / ((maxcapacity - mincapacity) / 10D), 2D) + 1, 100D) / 100D
            - (this.getIdealStatus() - this.getRepairStatus()) / 10D;
        if (eff <= 0D) return false;

        int toReduce = MathUtils.floorInt(this.fuelsupply * 0.005D * eff);

        final int originalToReduce = toReduce;
        int burnedballs = toReduce / 64;
        if (burnedballs > 0) toReduce -= burnedballs * 64;

        int meta = THTRMaterials.MATERIAL_USED_FUEL_INDEX;

        ItemStack[] toOutput = { new ItemStack(THTRMaterials.aTHTR_Materials, burnedballs, meta),
            new ItemStack(THTRMaterials.aTHTR_Materials, toReduce, meta + 1) };
        if (!this.canOutputAll(toOutput)) return false;

        this.fuelsupply -= originalToReduce;
        this.mOutputItems = toOutput;

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

        int takecoolant = this.coolanttaking;
        int drainedamount = 0;

        for (MTEHatchInput tHatch : filterValidMTEs(mInputHatches)) {
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
        return new MTEThoriumHighTempReactor(this.mName);
    }

    @Override
    public String[] getInfoData() {
        return new String[] { "Progress:",
            GTUtility.formatNumbers(this.mProgresstime / 20) + "secs /"
                + GTUtility.formatNumbers(this.mMaxProgresstime / 20)
                + "secs",
            "TRISO-Pebbles:",
            GTUtility.formatNumbers(this.fuelsupply) + "pcs. / " + GTUtility.formatNumbers(this.fuelsupply) + "psc.",
            "Helium-Level:",
            GTUtility.formatNumbers(this.HeliumSupply) + "L / "
                + GTUtility.formatNumbers(MTEThoriumHighTempReactor.HELIUM_NEEDED)
                + "L",
            "Coolant/t:", GTUtility.formatNumbers(this.mProgresstime == 0 ? 0 : this.coolanttaking) + "L/t",
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
        this.empty = !this.empty;
        GTUtility.sendChatToPlayer(
            aPlayer,
            "THTR is now running in " + (this.empty ? "emptying mode." : "normal Operation"));
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
