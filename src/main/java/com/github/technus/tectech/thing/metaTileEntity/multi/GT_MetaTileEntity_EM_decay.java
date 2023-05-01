package com.github.technus.tectech.thing.metaTileEntity.multi;

import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry.EM_COUNT_PER_MATERIAL_AMOUNT;
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

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMInstanceStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMInstanceStack;
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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import ic2.core.init.MainConfig;
import ic2.core.util.ConfigUtil;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_decay extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {

    // region variables
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;

    public static final double URANIUM_INGOT_MASS_DIFF = 1.6114516E10 * EM_COUNT_PER_MATERIAL_AMOUNT;
    private static final double URANIUM_MASS_TO_EU_PARTIAL = ConfigUtil
            .getDouble(MainConfig.get(), "balance/energy/generator/nuclear") * 3_000_000.0
            / URANIUM_INGOT_MASS_DIFF;
    public static final double URANIUM_MASS_TO_EU_INSTANT = URANIUM_MASS_TO_EU_PARTIAL * 20;

    private String clientLocale = "en_US";
    // endregion

    // region structure
    private static final String[] description = new String[] {
            EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
            translateToLocal("gt.blockmachines.multimachine.em.decay.hint.0"), // 1 - Classic Hatches or High Power
                                                                               // Casing
            translateToLocal("gt.blockmachines.multimachine.em.decay.hint.1"), // 2 - Elemental Hatches or Molecular
                                                                               // Casing
    };

    private static final IStructureDefinition<GT_MetaTileEntity_EM_decay> STRUCTURE_DEFINITION = IStructureDefinition
            .<GT_MetaTileEntity_EM_decay>builder()
            .addShape(
                    "main",
                    transpose(
                            new String[][] {
                                    { "A   A", "AAAAA", "A   A", "ABBBA", "ABCBA", "ABBBA", "A   A", "AAAAA", "A   A" },
                                    { " FFF ", "AAAAA", " EEE ", "BDDDB", "BDDDB", "BDDDB", " EEE ", "AAAAA", " FFF " },
                                    { " F~F ", "AAAAA", " EAE ", "BDDDB", "CDDDC", "BDDDB", " EAE ", "AAAAA", " FFF " },
                                    { " FFF ", "AAAAA", " EEE ", "BDDDB", "BDDDB", "BDDDB", " EEE ", "AAAAA", " FFF " },
                                    { "A   A", "AAAAA", "A   A", "ABBBA", "ABCBA", "ABBBA", "A   A", "AAAAA",
                                            "A   A" } }))
            .addElement('A', ofBlock(sBlockCasingsTT, 4)).addElement('B', ofBlock(sBlockCasingsTT, 5))
            .addElement('C', ofBlock(sBlockCasingsTT, 6)).addElement('D', ofBlock(sBlockCasingsTT, 8))
            .addElement(
                    'F',
                    ofHatchAdderOptional(
                            GT_MetaTileEntity_EM_decay::addClassicToMachineList,
                            textureOffset,
                            1,
                            sBlockCasingsTT,
                            0))
            .addElement(
                    'E',
                    ofHatchAdderOptional(
                            GT_MetaTileEntity_EM_decay::addElementalToMachineList,
                            textureOffset + 4,
                            2,
                            sBlockCasingsTT,
                            4))
            .build();
    // endregion

    // region parameters
    protected Parameters.Group.ParameterIn ampereFlow;
    private static final INameFunction<GT_MetaTileEntity_EM_decay> FLOW_NAME = (base,
            p) -> translateToLocal("gt.blockmachines.multimachine.em.decay.conf"); // Ampere divider
    private static final IStatusFunction<GT_MetaTileEntity_EM_decay> FLOW_STATUS = (base, p) -> {
        if (base.eAmpereFlow <= 0) {
            return STATUS_TOO_LOW;
        }
        return STATUS_OK;
    };
    // endregion

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
        EMInstanceStackMap map = getInputsClone_EM();
        if (map != null && map.hasStacks()) {
            for (GT_MetaTileEntity_Hatch_InputElemental i : eInputHatches) {
                i.getContentHandler().clear();
            }
            return startRecipe(map);
        }
        return false;
    }

    private boolean startRecipe(EMInstanceStackMap input) {
        mMaxProgresstime = 20;
        mEfficiencyIncrease = 10000;
        outputEM = new EMInstanceStackMap[2];
        outputEM[0] = input;
        outputEM[1] = new EMInstanceStackMap();

        for (EMInstanceStack stack : outputEM[0].valuesToArray()) {
            if (stack.getEnergy() == 0 && stack.getDefinition().decayMakesEnergy(1)
                    && getBaseMetaTileEntity().decreaseStoredEnergyUnits(
                            (long) (stack.getEnergySettingCost(1) * URANIUM_MASS_TO_EU_INSTANT),
                            false)) {
                stack.setEnergy(1);
            } else if (!stack.getDefinition().decayMakesEnergy(stack.getEnergy())) {
                outputEM[0].removeKey(stack.getDefinition());
                outputEM[1].putReplace(stack);
            }
        }

        eAmpereFlow = (long) ampereFlow.get();
        if (eAmpereFlow <= 0) {
            mEUt = 0;
            return false;
        }
        double energyDose = -outputEM[0].tickContent(1, 0, 1) * URANIUM_MASS_TO_EU_PARTIAL;
        mEUt = (int) (energyDose / eAmpereFlow);
        return outputEM[0].hasStacks();
    }

    @Override
    public void outputAfterRecipe_EM() {
        for (int i = 0; i < 2 && i < eOutputHatches.size(); i++) {
            eOutputHatches.get(i).getContentHandler().putUnifyAll(outputEM[i]);
            outputEM[i] = null;
        }
    }

    @Override
    public GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(translateToLocal("gt.blockmachines.multimachine.em.decay.name")) // Machine Type: Decay
                                                                                           // Generator
                .addInfo(translateToLocal("gt.blockmachines.multimachine.em.decay.desc.0")) // Controller block of the
                                                                                            // Decay Generator
                .addInfo(translateToLocal("gt.blockmachines.multimachine.em.decay.desc.1")) // Decays elemental matter
                                                                                            // to generate power
                .addInfo(translateToLocal("tt.keyword.Structure.StructureTooComplex")) // The structure is too complex!
                .addSeparator().beginStructureBlock(5, 5, 9, false)
                .addOtherStructurePart(
                        translateToLocal("tt.keyword.Structure.Elemental"),
                        translateToLocal("tt.keyword.Structure.AnyMolecularCasing2D"),
                        2) // Elemental Hatch: Any Molecular Casing with 2 dot
                .addOtherStructurePart(
                        translateToLocal("gt.blockmachines.hatch.param.tier.05.name"),
                        translateToLocal("tt.keyword.Structure.Optional") + " "
                                + translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"),
                        1) // Parametrizer: (optional) Any High Power Casing
                .addEnergyHatch(translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1) // Energy Hatch: Any
                                                                                                // High Power Casing
                .addMaintenanceHatch(translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1) // Maintenance
                                                                                                     // Hatch: Any High
                                                                                                     // Power Casing
                .toolTipFinisher(CommonValues.TEC_MARK_EM);
        return tt;
    }

    @Override
    public String[] getInfoData() { // TODO Do it
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

        return new String[] { translateToLocalFormatted("tt.keyword.Progress", clientLocale) + ":",
                EnumChatFormatting.GREEN + GT_Utility.formatNumbers(mProgresstime / 20)
                        + EnumChatFormatting.RESET
                        + " s / "
                        + EnumChatFormatting.YELLOW
                        + GT_Utility.formatNumbers(mMaxProgresstime / 20)
                        + EnumChatFormatting.RESET
                        + " s",
                translateToLocalFormatted("tt.keyphrase.Energy_Hatches", clientLocale) + ":",
                EnumChatFormatting.GREEN + GT_Utility.formatNumbers(storedEnergy)
                        + EnumChatFormatting.RESET
                        + " EU / "
                        + EnumChatFormatting.YELLOW
                        + GT_Utility.formatNumbers(maxEnergy)
                        + EnumChatFormatting.RESET
                        + " EU",
                (mEUt <= 0 ? translateToLocalFormatted("tt.keyphrase.Probably_uses", clientLocale) + ": "
                        : translateToLocalFormatted("tt.keyphrase.Probably_makes", clientLocale) + ": ")
                        + EnumChatFormatting.RED
                        + GT_Utility.formatNumbers(Math.abs(mEUt))
                        + EnumChatFormatting.RESET
                        + " EU/t at "
                        + EnumChatFormatting.RED
                        + GT_Utility.formatNumbers(eAmpereFlow)
                        + EnumChatFormatting.RESET
                        + " A",
                translateToLocalFormatted("tt.keyphrase.Tier_Rating", clientLocale) + ": "
                        + EnumChatFormatting.YELLOW
                        + VN[getMaxEnergyInputTier_EM()]
                        + EnumChatFormatting.RESET
                        + " / "
                        + EnumChatFormatting.GREEN
                        + VN[getMinEnergyInputTier_EM()]
                        + EnumChatFormatting.RESET
                        + " "
                        + translateToLocalFormatted("tt.keyphrase.Amp_Rating", clientLocale)
                        + ": "
                        + EnumChatFormatting.GREEN
                        + GT_Utility.formatNumbers(eMaxAmpereFlow)
                        + EnumChatFormatting.RESET
                        + " A",
                translateToLocalFormatted("tt.keyword.Problems", clientLocale) + ": "
                        + EnumChatFormatting.RED
                        + (getIdealStatus() - getRepairStatus())
                        + EnumChatFormatting.RESET
                        + " "
                        + translateToLocalFormatted("tt.keyword.Efficiency", clientLocale)
                        + ": "
                        + EnumChatFormatting.YELLOW
                        + mEfficiency / 100.0F
                        + EnumChatFormatting.RESET
                        + " %",
                translateToLocalFormatted("tt.keyword.PowerPass", clientLocale) + ": "
                        + EnumChatFormatting.BLUE
                        + ePowerPass
                        + EnumChatFormatting.RESET
                        + " "
                        + translateToLocalFormatted("tt.keyword.SafeVoid", clientLocale)
                        + ": "
                        + EnumChatFormatting.BLUE
                        + eSafeVoid,
                translateToLocalFormatted("tt.keyword.Computation", clientLocale) + ": "
                        + EnumChatFormatting.GREEN
                        + GT_Utility.formatNumbers(eAvailableData)
                        + EnumChatFormatting.RESET
                        + " / "
                        + EnumChatFormatting.YELLOW
                        + GT_Utility.formatNumbers(eRequiredData)
                        + EnumChatFormatting.RESET, };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/EM_DECAY");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/EM_DECAY_ACTIVE");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
            int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[texturePage][12],
                    new TT_RenderedExtendedFacingTexture(aActive ? ScreenON : ScreenOFF) };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[texturePage][12] };
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
        structureBuild_EM("main", 2, 2, 0, stackSize, hintsOnly);
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
