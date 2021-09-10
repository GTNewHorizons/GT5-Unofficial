package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.mechanics.elementalMatter.core.cElementalInstanceStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.cElementalInstanceStack;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_InputElemental;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.INameFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.Parameters;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.github.technus.tectech.util.CommonValues;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.util.IGT_HatchAdder;
import ic2.core.init.MainConfig;
import ic2.core.util.ConfigUtil;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.reflect.FieldUtils;

import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.bTransformationInfo.AVOGADRO_CONSTANT;
import static com.github.technus.tectech.mechanics.structure.Structure.adders;
import static com.github.technus.tectech.mechanics.structure.Structure.builder;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_OK;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_TOO_LOW;
import static com.github.technus.tectech.util.CommonValues.VN;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_decay extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    //region variables
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;

    public static final double URANIUM_INGOT_MASS_DIFF = 1.6114516E10* AVOGADRO_CONSTANT;
    private static final double URANIUM_MASS_TO_EU_PARTIAL = ConfigUtil.getFloat(MainConfig.get(), "balance/energy/generator/nuclear") * 3_000_000.0 / URANIUM_INGOT_MASS_DIFF;
    public static final double URANIUM_MASS_TO_EU_INSTANT = URANIUM_MASS_TO_EU_PARTIAL * 20;

    private String clientLocale = "en_US";
    //endregion

    //region structure
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
            translateToLocal("gt.blockmachines.multimachine.em.decay.hint.0"),//1 - Classic Hatches or High Power Casing
            translateToLocal("gt.blockmachines.multimachine.em.decay.hint.1"),//2 - Elemental Hatches or Molecular Casing
    };

    private static final IStructureDefinition<GT_MetaTileEntity_EM_decay> STRUCTURE_DEFINITION =
            StructureDefinition.<GT_MetaTileEntity_EM_decay>builder()
            .addShape("main",transpose(new String[][]{
                    {"A   A","AAAAA","A   A","ABBBA","ABCBA","ABBBA","A   A","AAAAA","A   A"},
                    {" FFF ","AAAAA"," EEE ","BDDDB","BDDDB","BDDDB"," EEE ","AAAAA"," FFF "},
                    {" F~F ","AAAAA"," EAE ","BDDDB","CDDDC","BDDDB"," EAE ","AAAAA"," FFF "},
                    {" FFF ","AAAAA"," EEE ","BDDDB","BDDDB","BDDDB"," EEE ","AAAAA"," FFF "},
                    {"A   A","AAAAA","A   A","ABBBA","ABCBA","ABBBA","A   A","AAAAA","A   A"}
            }))
            .addElement('A', ofBlock(sBlockCasingsTT, 4))
            .addElement('B', ofBlock(sBlockCasingsTT, 5))
            .addElement('C', ofBlock(sBlockCasingsTT, 6))
            .addElement('D', ofBlock(sBlockCasingsTT, 8))
            .addElement('E', ofHatchAdderOptional(GT_MetaTileEntity_EM_decay::addElementalToMachineList, textureOffset + 4, 2, sBlockCasingsTT, 4))
            .addElement('F', ofHatchAdderOptional(GT_MetaTileEntity_EM_decay::addClassicToMachineList, textureOffset, 1, sBlockCasingsTT, 0))
            .build();
    //endregion

    //region parameters
    protected Parameters.Group.ParameterIn ampereFlow;
    private static final INameFunction<GT_MetaTileEntity_EM_decay> FLOW_NAME = (base, p) -> translateToLocal("gt.blockmachines.multimachine.em.decay.conf");//Ampere divider
    private static final IStatusFunction<GT_MetaTileEntity_EM_decay> FLOW_STATUS = (base, p) -> {
        if (base.eAmpereFlow <= 0) {
            return STATUS_TOO_LOW;
        }
        return STATUS_OK;
    };
    //endregion

    public GT_MetaTileEntity_EM_decay(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_decay(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_decay(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return structureCheck_EM("main", 2, 2, 0);
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {
        cElementalInstanceStackMap map = getInputsClone_EM();
        if (map != null && map.hasStacks()) {
            for (GT_MetaTileEntity_Hatch_InputElemental i : eInputHatches) {
                i.getContainerHandler().clear();
            }
            return startRecipe(map);
        }
        return false;
    }

    private boolean startRecipe(cElementalInstanceStackMap input) {
        mMaxProgresstime = 20;
        mEfficiencyIncrease = 10000;
        outputEM = new cElementalInstanceStackMap[2];
        outputEM[0] = input;
        outputEM[1] = new cElementalInstanceStackMap();

        for (cElementalInstanceStack stack : outputEM[0].values()) {
            if (stack.getEnergy() == 0 && stack.definition.decayMakesEnergy(1) &&
                    getBaseMetaTileEntity().decreaseStoredEnergyUnits(
                            (long) (stack.getEnergySettingCost(1) * URANIUM_MASS_TO_EU_INSTANT), false)) {
                stack.setEnergy(1);
            } else if (!stack.definition.decayMakesEnergy(stack.getEnergy())) {
                outputEM[0].remove(stack.definition);
                outputEM[1].putReplace(stack);
            }
        }

        eAmpereFlow = (long) ampereFlow.get();
        if (eAmpereFlow <= 0) {
            mEUt = 0;
            return false;
        }
        double energyDose = -outputEM[0].tickContent(1, 0, 1) * URANIUM_MASS_TO_EU_PARTIAL;
        mEUt = (int) ( energyDose / eAmpereFlow);
        return outputEM[0].hasStacks();
    }

    @Override
    public void outputAfterRecipe_EM() {
        for (int i = 0; i < 2 && i < eOutputHatches.size(); i++) {
            eOutputHatches.get(i).getContainerHandler().putUnifyAll(outputEM[i]);
            outputEM[i] = null;
        }
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                translateToLocal("gt.blockmachines.multimachine.em.decay.desc.0"),//Is life time too long?
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + translateToLocal("gt.blockmachines.multimachine.em.decay.desc.1")//Make it half-life (3) instead!
        };
    }

    @Override
    public String[] getInfoData() {//TODO Do it
        long storedEnergy = 0;
        long maxEnergy = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                storedEnergy += tHatch.getBaseMetaTileEntity().getStoredEU();
                maxEnergy += tHatch.getBaseMetaTileEntity().getEUCapacity();
            }
        }
        for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : eEnergyMulti) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                storedEnergy += tHatch.getBaseMetaTileEntity().getStoredEU();
                maxEnergy += tHatch.getBaseMetaTileEntity().getEUCapacity();
            }
        }

        return new String[]{
                translateToLocalFormatted("tt.keyword.Progress", clientLocale) + ":",
                EnumChatFormatting.GREEN + Integer.toString(mProgresstime / 20) + EnumChatFormatting.RESET + " s / " +
                        EnumChatFormatting.YELLOW + mMaxProgresstime / 20 + EnumChatFormatting.RESET + " s",
                translateToLocalFormatted("tt.keyphrase.Energy_Hatches", clientLocale) + ":",
                EnumChatFormatting.GREEN + Long.toString(storedEnergy) + EnumChatFormatting.RESET + " EU / " +
                        EnumChatFormatting.YELLOW + maxEnergy + EnumChatFormatting.RESET + " EU",
                (mEUt <= 0 ? translateToLocalFormatted("tt.keyphrase.Probably_uses", clientLocale) + ": " : translateToLocalFormatted("tt.keyphrase.Probably_makes", clientLocale) + ": ") +
                        EnumChatFormatting.RED + Math.abs(mEUt) + EnumChatFormatting.RESET + " EU/t at " +
                        EnumChatFormatting.RED + eAmpereFlow + EnumChatFormatting.RESET + " A",
                translateToLocalFormatted("tt.keyphrase.Tier_Rating", clientLocale) + ": " + EnumChatFormatting.YELLOW + VN[getMaxEnergyInputTier_EM()] + EnumChatFormatting.RESET + " / " + EnumChatFormatting.GREEN + VN[getMinEnergyInputTier_EM()] + EnumChatFormatting.RESET +
                        " " + translateToLocalFormatted("tt.keyphrase.Amp_Rating", clientLocale) + ": " + EnumChatFormatting.GREEN + eMaxAmpereFlow + EnumChatFormatting.RESET + " A",
                translateToLocalFormatted("tt.keyword.Problems", clientLocale) + ": " + EnumChatFormatting.RED + (getIdealStatus() - getRepairStatus()) + EnumChatFormatting.RESET +
                        " " + translateToLocalFormatted("tt.keyword.Efficiency", clientLocale) + ": " + EnumChatFormatting.YELLOW + mEfficiency / 100.0F + EnumChatFormatting.RESET + " %",
                translateToLocalFormatted("tt.keyword.PowerPass", clientLocale) + ": " + EnumChatFormatting.BLUE + ePowerPass + EnumChatFormatting.RESET +
                        " " + translateToLocalFormatted("tt.keyword.SafeVoid", clientLocale) + ": " + EnumChatFormatting.BLUE + eSafeVoid,
                translateToLocalFormatted("tt.keyword.Computation", clientLocale) + ": " + EnumChatFormatting.GREEN + eAvailableData + EnumChatFormatting.RESET + " / " + EnumChatFormatting.YELLOW + eRequiredData + EnumChatFormatting.RESET,
        };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/EM_DECAY");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/EM_DECAY_ACTIVE");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][12], new TT_RenderedExtendedFacingTexture(aActive ? ScreenON : ScreenOFF)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][12]};
    }

    @Override
    protected void parametersInstantiation_EM() {
        Parameters.Group hatch_0 = parametrization.getGroup(0, true);
        ampereFlow = hatch_0.makeInParameter(0, 1, FLOW_NAME, FLOW_STATUS);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        super.onRightclick(aBaseMetaTileEntity, aPlayer);

        if (!aBaseMetaTileEntity.isClientSide() && aPlayer instanceof EntityPlayerMP) {
            try {
                EntityPlayerMP player = (EntityPlayerMP) aPlayer;
                clientLocale = (String) FieldUtils.readField(player, "translator", true);
            } catch (Exception e) {
                clientLocale = "en_US";
            }
        } else {
            return true;
        }
        return true;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM("main", 2, 2, 0, hintsOnly, stackSize);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_EM_decay> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }
}