package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.auxiliary.TecTechConfig;
import com.github.technus.tectech.elementalMatter.classes.cElementalDefinitionStack;
import com.github.technus.tectech.elementalMatter.classes.cElementalInstanceStack;
import com.github.technus.tectech.elementalMatter.classes.cElementalInstanceStackMap;
import com.github.technus.tectech.elementalMatter.classes.tElementalException;
import com.github.technus.tectech.elementalMatter.definitions.dAtomDefinition;
import com.github.technus.tectech.elementalMatter.definitions.dHadronDefinition;
import com.github.technus.tectech.elementalMatter.definitions.eLeptonDefinition;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.casing.GT_Container_CasingsTT;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;

import static com.github.technus.tectech.Util.isInputEqual;
import static com.github.technus.tectech.elementalMatter.definitions.dAtomDefinition.getBestUnstableIsotope;
import static com.github.technus.tectech.elementalMatter.definitions.dAtomDefinition.getFirstStableIsotope;
import static gregtech.api.enums.GT_Values.V;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_quantizer extends GT_MetaTileEntity_MultiblockBase_EM {
    public static HashMap<Integer, cElementalDefinitionStack> itemBinds = new HashMap<>(32);
    public static HashMap<Integer, cElementalDefinitionStack> fluidBind = new HashMap<>(8);
    private static float refMass, refUnstableMass;

    public GT_MetaTileEntity_EM_quantizer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_quantizer(String aName) {
        super(aName);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_quantizer(this.mName);
    }

    @Override
    public boolean EM_checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        int xDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetX;
        int yDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetY;
        int zDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetZ;
        if (iGregTechTileEntity.getBlockOffset(xDir, yDir, zDir) != QuantumGlassBlock.INSTANCE) return false;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int h = -1; h < 2; h++) {
                    if ((i != 0 || j != 0 || h != 0)/*exclude center*/ && (xDir + i != 0 || yDir + h != 0 || zDir + j != 0)/*exclude this*/) {
                        IGregTechTileEntity tTileEntity = iGregTechTileEntity.getIGregTechTileEntityOffset(xDir + i, yDir + h, zDir + j);
                        if ((!addMaintenanceToMachineList(tTileEntity, 99)) &&
                                (!addClassicInputToMachineList(tTileEntity, 99)) &&
                                (!addElementalOutputToMachineList(tTileEntity, 99)) &&
                                (!addMufflerToMachineList(tTileEntity, 99)) &&
                                (!addEnergyIOToMachineList(tTileEntity, 99))) {
                            if (iGregTechTileEntity.getBlockOffset(xDir + i, yDir + h, zDir + j) != GT_Container_CasingsTT.sBlockCasingsTT ||
                                    iGregTechTileEntity.getMetaIDOffset(xDir + i, yDir + h, zDir + j) != 3) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.tecMark,
                "Conveniently convert regular stuff into quantum form.",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "To make it more inconvenient."
        };
    }

    @Override
    public boolean EM_checkRecipe(ItemStack itemStack) {
        if (GregTech_API.sPostloadFinished) {
            ItemStack[] inI = getStoredInputs().toArray(new ItemStack[0]);
            if (inI.length > 0) {
                for (ItemStack is : inI) {
                    int[] oreIDs = OreDictionary.getOreIDs(is);
                    if (TecTechConfig.DEBUG_MODE)
                        TecTech.Logger.info("Quantifier-recipe " + is.getItem().getUnlocalizedName() + "." + is.getItemDamage() + " " + is.getDisplayName());
                    for (int ID : oreIDs) {
                        if (TecTechConfig.DEBUG_MODE)
                            TecTech.Logger.info("Quantifier-recipe " + is.getItem().getUnlocalizedName() + "." + is.getItemDamage() + " " + OreDictionary.getOreName(ID));
                        cElementalDefinitionStack into = itemBinds.get(ID);
                        if (into != null && isInputEqual(true, false,
                                nothingF, new ItemStack[]{new ItemStack(is.getItem(), 1, is.getItemDamage())}, null, inI)) {
                            mMaxProgresstime = 20;
                            mEfficiencyIncrease = 10000;
                            float mass = into.getMass();
                            float euMult = mass / refMass;
                            eAmpereFlow = (int) Math.ceil(euMult);
                            if (mass > refUnstableMass) {
                                mEUt = (int) -V[9];
                            } else {
                                mEUt = (int) -V[8];
                            }
                            outputEM = new cElementalInstanceStackMap[1];
                            outputEM[0] = new cElementalInstanceStackMap(new cElementalInstanceStack(into));
                            return true;
                        }
                    }
                }
            }
            FluidStack[] inF = getStoredFluids().toArray(new FluidStack[0]);
            if (inF.length > 0) {
                for (FluidStack fs : inF) {
                    cElementalDefinitionStack into = fluidBind.get(fs.getFluid().getID());
                    if (into != null && fs.amount >= 144 && isInputEqual(true, false,
                            new FluidStack[]{new FluidStack(fs.getFluid(), 144)}, nothingI, inF, (ItemStack[]) null)) {
                        mMaxProgresstime = 20;
                        mEfficiencyIncrease = 10000;
                        float mass = into.getMass();
                        float euMult = mass / refMass;
                        eAmpereFlow = (int) Math.ceil(euMult);
                        if (mass > refUnstableMass) {
                            mEUt = (int) -V[9];
                        } else {
                            mEUt = (int) -V[8];
                        }
                        outputEM = new cElementalInstanceStackMap[1];
                        outputEM[0] = new cElementalInstanceStackMap(new cElementalInstanceStack(into));
                        return true;
                    }
                }
            }
        }
        mEfficiencyIncrease = 0;
        mMaxProgresstime = 0;
        return false;
    }

    @Override
    public void EM_outputFunction() {
        if (eOutputHatches.size() < 1) {
            stopMachine();
            return;
        }
        eOutputHatches.get(0).getContainerHandler().putUnifyAll(outputEM[0]);
    }

    private static int getID(OrePrefixes prefix, Materials material) {
        return OreDictionary.getOreID(prefix.name() + material.name());
    }

    public static void run() {
        refMass = getFirstStableIsotope(1).getMass() * 144F;
        fluidBind.put(Materials.Hydrogen.mGas.getID(), new cElementalDefinitionStack(getFirstStableIsotope(1), 144));
        fluidBind.put(Materials.Helium.mGas.getID(), new cElementalDefinitionStack(getFirstStableIsotope(2), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Lithium),
                new cElementalDefinitionStack(getFirstStableIsotope(3), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Beryllium),
                new cElementalDefinitionStack(getFirstStableIsotope(4), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Boron),
                new cElementalDefinitionStack(getFirstStableIsotope(5), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Carbon),
                new cElementalDefinitionStack(getFirstStableIsotope(6), 144));
        fluidBind.put(Materials.Nitrogen.mGas.getID(), new cElementalDefinitionStack(getFirstStableIsotope(7), 144));
        fluidBind.put(Materials.Oxygen.mGas.getID(), new cElementalDefinitionStack(getFirstStableIsotope(8), 144));
        fluidBind.put(Materials.Fluorine.mGas.getID(), new cElementalDefinitionStack(getFirstStableIsotope(9), 144));
        //fluidBind.put(Materials.Neon.mGas.getID(),new cElementalDefinitionStack(getFirstStableIsotope(10),144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Sodium),
                new cElementalDefinitionStack(getFirstStableIsotope(11), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Magnesium),
                new cElementalDefinitionStack(getFirstStableIsotope(12), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Aluminium),
                new cElementalDefinitionStack(getFirstStableIsotope(13), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Silicon),
                new cElementalDefinitionStack(getFirstStableIsotope(14), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Phosphorus),
                new cElementalDefinitionStack(getFirstStableIsotope(15), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Sulfur),
                new cElementalDefinitionStack(getFirstStableIsotope(16), 144));
        fluidBind.put(Materials.Chlorine.mFluid.getID(), new cElementalDefinitionStack(getFirstStableIsotope(17), 144));
        fluidBind.put(Materials.Argon.mGas.getID(), new cElementalDefinitionStack(getFirstStableIsotope(18), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Potassium),
                new cElementalDefinitionStack(getFirstStableIsotope(19), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Calcium),
                new cElementalDefinitionStack(getFirstStableIsotope(20), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Scandium),
                new cElementalDefinitionStack(getFirstStableIsotope(21), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Titanium),
                new cElementalDefinitionStack(getFirstStableIsotope(22), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Vanadium),
                new cElementalDefinitionStack(getFirstStableIsotope(23), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Chrome),
                new cElementalDefinitionStack(getFirstStableIsotope(24), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Manganese),
                new cElementalDefinitionStack(getFirstStableIsotope(25), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Iron),
                new cElementalDefinitionStack(getFirstStableIsotope(26), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.IronMagnetic),
                new cElementalDefinitionStack(getFirstStableIsotope(26), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Cobalt),
                new cElementalDefinitionStack(getFirstStableIsotope(27), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Nickel),
                new cElementalDefinitionStack(getFirstStableIsotope(28), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Copper),
                new cElementalDefinitionStack(getFirstStableIsotope(29), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Zinc),
                new cElementalDefinitionStack(getFirstStableIsotope(30), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Gallium),
                new cElementalDefinitionStack(getFirstStableIsotope(31), 144));
        //itemBinds.put(getID(OrePrefixes.dust, Materials.Germanium),
        //        new cElementalDefinitionStack(getFirstStableIsotope(32),144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Arsenic),
                new cElementalDefinitionStack(getFirstStableIsotope(33), 144));
        //itemBinds.put(getID(OrePrefixes.dust, Materials.Selenium),
        //        new cElementalDefinitionStack(getFirstStableIsotope(34),144));
        //itemBinds.put(getID(OrePrefixes.dust, Materials.Bromine),
        //        new cElementalDefinitionStack(getFirstStableIsotope(35),144));
        //itemBinds.put(getID(OrePrefixes.dust, Materials.Krypton),
        //        new cElementalDefinitionStack(getFirstStableIsotope(36),144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Rubidium),
                new cElementalDefinitionStack(getFirstStableIsotope(37), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Strontium),
                new cElementalDefinitionStack(getFirstStableIsotope(38), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Yttrium),
                new cElementalDefinitionStack(getFirstStableIsotope(39), 144));
        //itemBinds.put(getID(OrePrefixes.dust, Materials.Zirconium),
        //        new cElementalDefinitionStack(getFirstStableIsotope(40),144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Niobium),
                new cElementalDefinitionStack(getFirstStableIsotope(41), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Molybdenum),
                new cElementalDefinitionStack(getFirstStableIsotope(42), 144));
        //itemBinds.put(getID(OrePrefixes.dust, Materials.Technetium),
        //        new cElementalDefinitionStack(getFirstStableIsotope(43),144));
        //itemBinds.put(getID(OrePrefixes.dust, Materials.Ruthenium),
        //        new cElementalDefinitionStack(getFirstStableIsotope(44),144));
        //itemBinds.put(getID(OrePrefixes.dust, Materials.Rhodium),
        //        new cElementalDefinitionStack(getFirstStableIsotope(45),144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Palladium),
                new cElementalDefinitionStack(getFirstStableIsotope(46), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Silver),
                new cElementalDefinitionStack(getFirstStableIsotope(47), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Cadmium),
                new cElementalDefinitionStack(getFirstStableIsotope(48), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Indium),
                new cElementalDefinitionStack(getFirstStableIsotope(49), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Tin),
                new cElementalDefinitionStack(getFirstStableIsotope(50), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Antimony),
                new cElementalDefinitionStack(getFirstStableIsotope(51), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Tellurium),
                new cElementalDefinitionStack(getFirstStableIsotope(52), 144));
        //itemBinds.put(getID(OrePrefixes.dust, Materials.Iodine),
        //        new cElementalDefinitionStack(getFirstStableIsotope(53),144));
        //fluidBind.put(Materials.Xenon.mGas.getID(),new cElementalDefinitionStack(getFirstStableIsotope(54),144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Caesium),
                new cElementalDefinitionStack(getFirstStableIsotope(55), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Barium),
                new cElementalDefinitionStack(getFirstStableIsotope(56), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Lanthanum),
                new cElementalDefinitionStack(getFirstStableIsotope(57), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Cerium),
                new cElementalDefinitionStack(getFirstStableIsotope(58), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Praseodymium),
                new cElementalDefinitionStack(getFirstStableIsotope(59), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Neodymium),
                new cElementalDefinitionStack(getFirstStableIsotope(60), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.NeodymiumMagnetic),
                new cElementalDefinitionStack(getFirstStableIsotope(60), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Promethium),
                new cElementalDefinitionStack(getFirstStableIsotope(61), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Samarium),
                new cElementalDefinitionStack(getFirstStableIsotope(62), 144));
        //itemBinds.put(getID(OrePrefixes.dust, Materials.SamariumMagnetic),
        //        new cElementalDefinitionStack(getFirstStableIsotope(62),144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Europium),
                new cElementalDefinitionStack(getFirstStableIsotope(63), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Gadolinium),
                new cElementalDefinitionStack(getFirstStableIsotope(64), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Terbium),
                new cElementalDefinitionStack(getFirstStableIsotope(65), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Dysprosium),
                new cElementalDefinitionStack(getFirstStableIsotope(66), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Holmium),
                new cElementalDefinitionStack(getFirstStableIsotope(67), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Erbium),
                new cElementalDefinitionStack(getFirstStableIsotope(68), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Thulium),
                new cElementalDefinitionStack(getFirstStableIsotope(69), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Ytterbium),
                new cElementalDefinitionStack(getFirstStableIsotope(70), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Lutetium),
                new cElementalDefinitionStack(getFirstStableIsotope(71), 144));
        //itemBinds.put(getID(OrePrefixes.dust, Materials.Hafnum),
        //        new cElementalDefinitionStack(getFirstStableIsotope(72),144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Tantalum),
                new cElementalDefinitionStack(getFirstStableIsotope(73), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Tungsten),
                new cElementalDefinitionStack(getFirstStableIsotope(74), 144));
        //itemBinds.put(getID(OrePrefixes.dust, Materials.Rhenium),
        //        new cElementalDefinitionStack(getFirstStableIsotope(75),144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Osmium),
                new cElementalDefinitionStack(getFirstStableIsotope(76), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Iridium),
                new cElementalDefinitionStack(getFirstStableIsotope(77), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Platinum),
                new cElementalDefinitionStack(getFirstStableIsotope(78), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Gold),
                new cElementalDefinitionStack(getFirstStableIsotope(79), 144));
        fluidBind.put(Materials.Mercury.mFluid.getID(), new cElementalDefinitionStack(getFirstStableIsotope(80), 144));
        //itemBinds.put(getID(OrePrefixes.dust, Materials.Thalium),
        //        new cElementalDefinitionStack(getFirstStableIsotope(81),144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Lead),
                new cElementalDefinitionStack(getFirstStableIsotope(82), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Bismuth),
                new cElementalDefinitionStack(getFirstStableIsotope(83), 144));
        //UNSTABLE ATOMS
        refUnstableMass = getFirstStableIsotope(83).getMass() * 144F;
        //itemBinds.put(getID(OrePrefixes.dust, Materials.Polonium),
        //        new cElementalDefinitionStack(getBestUnstableIsotope(84),144));
        //fluidBind.put(Materials.Astatine.mPlasma.getID(),new cElementalDefinitionStack(getBestUnstableIsotope(85),144));
        fluidBind.put(Materials.Radon.mGas.getID(), new cElementalDefinitionStack(getBestUnstableIsotope(86), 144));
        //itemBinds.put(getID(OrePrefixes.dust, Materials.Francium),
        //        new cElementalDefinitionStack(getBestUnstableIsotope(87),144));
        //itemBinds.put(getID(OrePrefixes.dust, Materials.Radium),
        //        new cElementalDefinitionStack(getBestUnstableIsotope(88),144));
        //itemBinds.put(getID(OrePrefixes.dust, Materials.Actinium),
        //        new cElementalDefinitionStack(getBestUnstableIsotope(89),144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Thorium),
                new cElementalDefinitionStack(getBestUnstableIsotope(90), 144));
        //itemBinds.put(getID(OrePrefixes.dust, Materials.Protactinium),
        //        new cElementalDefinitionStack(getBestUnstableIsotope(91),144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Uranium),
                new cElementalDefinitionStack(getBestUnstableIsotope(92), 144));
        //itemBinds.put(getID(OrePrefixes.dust, Materials.Neptunium),
        //        new cElementalDefinitionStack(getBestUnstableIsotope(93),144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Plutonium),
                new cElementalDefinitionStack(getBestUnstableIsotope(94), 144));
        itemBinds.put(getID(OrePrefixes.dust, Materials.Americium),
                new cElementalDefinitionStack(getBestUnstableIsotope(95), 144));
        /* ... */
        itemBinds.put(getID(OrePrefixes.ingotHot, Materials.Neutronium),
                new cElementalDefinitionStack(dHadronDefinition.hadron_n, 100000));

        try {
            fluidBind.put(Materials.Deuterium.mGas.getID(), new cElementalDefinitionStack(
                    new dAtomDefinition(
                            eLeptonDefinition.lepton_e1,
                            dHadronDefinition.hadron_p1,
                            dHadronDefinition.hadron_n1
                    ), 144));
            fluidBind.put(Materials.Tritium.mGas.getID(), new cElementalDefinitionStack(
                    new dAtomDefinition(
                            eLeptonDefinition.lepton_e1,
                            dHadronDefinition.hadron_p1,
                            dHadronDefinition.hadron_n2
                    ), 144));
            fluidBind.put(Materials.Helium_3.mGas.getID(), new cElementalDefinitionStack(
                    new dAtomDefinition(
                            new cElementalDefinitionStack(eLeptonDefinition.lepton_e, 2),
                            dHadronDefinition.hadron_p2,
                            new cElementalDefinitionStack(dHadronDefinition.hadron_n, 3)
                    ), 144));
            itemBinds.put(getID(OrePrefixes.dust, Materials.Uranium235),
                    new cElementalDefinitionStack(new dAtomDefinition(
                            new cElementalDefinitionStack(eLeptonDefinition.lepton_e, 92),
                            new cElementalDefinitionStack(dHadronDefinition.hadron_p, 92),
                            new cElementalDefinitionStack(dHadronDefinition.hadron_n, 143)
                    ), 144));
            itemBinds.put(getID(OrePrefixes.dust, Materials.Plutonium241),
                    new cElementalDefinitionStack(new dAtomDefinition(
                            new cElementalDefinitionStack(eLeptonDefinition.lepton_e, 94),
                            new cElementalDefinitionStack(dHadronDefinition.hadron_p, 94),
                            new cElementalDefinitionStack(dHadronDefinition.hadron_n, 149)
                    ), 144));
        } catch (tElementalException e) {
            if (TecTechConfig.DEBUG_MODE) e.printStackTrace();
        }
    }
}
