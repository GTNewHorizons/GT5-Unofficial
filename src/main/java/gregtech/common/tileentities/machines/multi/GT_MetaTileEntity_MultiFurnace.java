package gregtech.common.tileentities.machines.multi;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_StructureUtility;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_Values.VN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;

public class GT_MetaTileEntity_MultiFurnace extends GT_MetaTileEntity_AbstractMultiFurnace<GT_MetaTileEntity_MultiFurnace> implements IConstructable {
    private int mLevel = 0;
    private int mCostDiscount = 1;

    private static final int CASING_INDEX = 11;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_MetaTileEntity_MultiFurnace> STRUCTURE_DEFINITION = StructureDefinition.<GT_MetaTileEntity_MultiFurnace>builder()
            .addShape(STRUCTURE_PIECE_MAIN, transpose(new String[][]{
                    {"ccc", "cmc", "ccc"},
                    {"CCC", "C-C", "CCC",},
                    {"b~b", "bbb", "bbb"}
            }))
            .addElement('c', ofBlock(GregTech_API.sBlockCasings1, CASING_INDEX))
            .addElement('m', ofHatchAdder(GT_MetaTileEntity_MultiFurnace::addMufflerToMachineList, CASING_INDEX, 2))
            .addElement('C', GT_StructureUtility.ofCoil(GT_MetaTileEntity_MultiFurnace::setCoilLevel, GT_MetaTileEntity_MultiFurnace::getCoilLevel))
            .addElement('b', ofHatchAdderOptional(GT_MetaTileEntity_MultiFurnace::addBottomHatch, CASING_INDEX, 1, GregTech_API.sBlockCasings1, CASING_INDEX))
            .build();

    public GT_MetaTileEntity_MultiFurnace(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, "smelting");
    }

    public GT_MetaTileEntity_MultiFurnace(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_MultiFurnace(this.mName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Furnace")
                .addInfo("Controller Block for the Multi Smelter")
                .addInfo("Smelts up to 8-128 items at once")
                .addInfo("Items smelted increases with coil tier")
                .addPollutionAmount(getPollutionPerSecond(null))
                .addSeparator()
                .beginStructureBlock(3, 3, 3, true)
                .addController("Front bottom")
                .addCasingInfo("Heat Proof Machine Casing", 8)
                .addOtherStructurePart("Heating Coils", "Middle layer")
                .addEnergyHatch("Any bottom casing", 1)
                .addMaintenanceHatch("Any bottom casing", 1)
                .addMufflerHatch("Top Middle", 2)
                .addInputBus("Any bottom casing", 1)
                .addOutputBus("Any bottom casing", 1)
                .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide != aFacing) return new ITexture[]{casingTexturePages[0][CASING_INDEX]};
        if (aActive) return new ITexture[]{
                casingTexturePages[0][CASING_INDEX],
                TextureFactory.builder().addIcon(OVERLAY_FRONT_MULTI_SMELTER_ACTIVE).extFacing().build(),
                TextureFactory.builder().addIcon(OVERLAY_FRONT_MULTI_SMELTER_ACTIVE_GLOW).extFacing().glow().build()};
        return new ITexture[]{
                casingTexturePages[0][CASING_INDEX],
                TextureFactory.builder().addIcon(OVERLAY_FRONT_MULTI_SMELTER).extFacing().build(),
                TextureFactory.builder().addIcon(OVERLAY_FRONT_MULTI_SMELTER_GLOW).extFacing().glow().build()};
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "MultiFurnace.png");
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sFurnaceRecipes;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack){
        return GT_Mod.gregtechproxy.mPollutionMultiSmelterPerSecond;
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        ArrayList<ItemStack> tInputList = getStoredInputs();
        if (tInputList.isEmpty())
            return false;

        int mVolatage = GT_Utility.safeInt(getMaxInputVoltage());
        int tMaxParrallel = 8 * this.mLevel;
        int tCurrenParrallel = 0;
        ItemStack tSmeltStack = tInputList.get(0);
        ItemStack tOutputStack = GT_ModHandler.getSmeltingOutput(tSmeltStack,false,null);
        if (tOutputStack == null)
                return false;
        for (ItemStack item : tInputList)
            if (tSmeltStack.isItemEqual(item)) if (item.stackSize < (tMaxParrallel - tCurrenParrallel)) {
                tCurrenParrallel += item.stackSize;
                item.stackSize = 0;
            } else {
                item.stackSize = (tCurrenParrallel + item.stackSize) - tMaxParrallel;
                tCurrenParrallel = tMaxParrallel;
                break;
            }

        tCurrenParrallel *= tOutputStack.stackSize;
        this.mOutputItems = new ItemStack[(tCurrenParrallel/64)+1];
        for (int i = 0; i < this.mOutputItems.length; i++) {
            ItemStack tNewStack = tOutputStack.copy();
            int size = Math.min(tCurrenParrallel, 64);
            tNewStack.stackSize = size;
            tCurrenParrallel -= size;
            this.mOutputItems[i] = tNewStack;
        }

        if (this.mOutputItems.length > 0) {
            this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
            this.mEfficiencyIncrease = 10000;
            calculateOverclockedNessMulti(4, 512, 1, mVolatage);
            //In case recipe is too OP for that machine
            if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                return false;

            this.mEUt = GT_Utility.safeInt(((long)mEUt) * this.mLevel / (long)this.mCostDiscount,1);
            if (mEUt == Integer.MAX_VALUE - 1)
                return false;

            if (this.mEUt > 0)
                this.mEUt = (-this.mEUt);
        }
        updateSlots();
        return true;
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_MultiFurnace> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack){
        this.mLevel = 0;
        this.mCostDiscount = 1;

        replaceDeprecatedCoils(aBaseMetaTileEntity);

        setCoilLevel(HeatingCoilLevel.None);

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 1, 2, 0))
            return false;

        if (getCoilLevel() == HeatingCoilLevel.None)
            return false;

        if (mMaintenanceHatches.size() != 1)
            return false;

        this.mLevel = getCoilLevel().getLevel();
        this.mCostDiscount = getCoilLevel().getCostDiscount();
        return true;
    }

    private void replaceDeprecatedCoils(IGregTechTileEntity aBaseMetaTileEntity) {
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
        int tX = aBaseMetaTileEntity.getXCoord() + xDir;
        int tY = aBaseMetaTileEntity.getYCoord();
        int tZ = aBaseMetaTileEntity.getZCoord() + zDir;
        int tUsedMeta;
        for (int xPos = tX - 1; xPos <= tX + 1; xPos++)
            for (int zPos = tZ - 1; zPos <= tZ + 1; zPos++) {
                if ((xPos == tX) && (zPos == tZ)) continue;
                tUsedMeta = aBaseMetaTileEntity.getMetaID(xPos, tY + 1, zPos);
                if (tUsedMeta >= 12 && tUsedMeta <= 14 && aBaseMetaTileEntity.getBlock(xPos, tY + 1, zPos) == GregTech_API.sBlockCasings1)
                    aBaseMetaTileEntity.getWorld().setBlock(xPos, tY + 1, zPos, GregTech_API.sBlockCasings5, tUsedMeta - 12, 3);
            }
    }


    @Override
    public String[] getInfoData() {
        int mPollutionReduction=0;
        for (GT_MetaTileEntity_Hatch_Muffler tHatch : mMufflerHatches)
            if (isValidMetaTileEntity(tHatch))
                mPollutionReduction = Math.max(tHatch.calculatePollutionReduction(100), mPollutionReduction);

        long storedEnergy=0;
        long maxEnergy=0;
        for(GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches)
            if (isValidMetaTileEntity(tHatch)) {
                storedEnergy += tHatch.getBaseMetaTileEntity().getStoredEU();
                maxEnergy += tHatch.getBaseMetaTileEntity().getEUCapacity();
            }

        return new String[]{
                StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": " +
                        EnumChatFormatting.GREEN + GT_Utility.formatNumbers(mProgresstime / 20) + EnumChatFormatting.RESET + " s / " +
                        EnumChatFormatting.YELLOW + GT_Utility.formatNumbers(mMaxProgresstime / 20) + EnumChatFormatting.RESET + " s",
                StatCollector.translateToLocal("GT5U.multiblock.energy") + ": " +
                        EnumChatFormatting.GREEN + GT_Utility.formatNumbers(storedEnergy) + EnumChatFormatting.RESET + " EU / " +
                        EnumChatFormatting.YELLOW + GT_Utility.formatNumbers(maxEnergy) + EnumChatFormatting.RESET + " EU",
                StatCollector.translateToLocal("GT5U.multiblock.usage") + ": " +
                        EnumChatFormatting.RED + GT_Utility.formatNumbers(-mEUt) + EnumChatFormatting.RESET + " EU/t",
                StatCollector.translateToLocal("GT5U.multiblock.mei") + ": " +
                        EnumChatFormatting.YELLOW + GT_Utility.formatNumbers(getMaxInputVoltage()) + EnumChatFormatting.RESET + " EU/t(*2A) " +
                        StatCollector.translateToLocal("GT5U.machines.tier") + ": " +
                        EnumChatFormatting.YELLOW + VN[GT_Utility.getTier(getMaxInputVoltage())] + EnumChatFormatting.RESET,
                StatCollector.translateToLocal("GT5U.multiblock.problems") + ": " +
                        EnumChatFormatting.RED + (getIdealStatus() - getRepairStatus()) + EnumChatFormatting.RESET + " " +
                        StatCollector.translateToLocal("GT5U.multiblock.efficiency") + ": " +
                        EnumChatFormatting.YELLOW + mEfficiency / 100.0F + EnumChatFormatting.RESET + " %",
                StatCollector.translateToLocal("GT5U.MS.multismelting") + ": " +
                        EnumChatFormatting.GREEN + mLevel * 8 + EnumChatFormatting.RESET +
                        " Discount: (EU/t) / " + EnumChatFormatting.GREEN + GT_Utility.formatNumbers(mCostDiscount) + EnumChatFormatting.RESET,
                StatCollector.translateToLocal("GT5U.multiblock.pollution") + ": " +
                        EnumChatFormatting.GREEN + mPollutionReduction + EnumChatFormatting.RESET + " %"
        };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 2, 0);
    }
}
