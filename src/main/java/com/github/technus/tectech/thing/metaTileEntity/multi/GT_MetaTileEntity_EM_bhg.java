package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.mechanics.constructable.IConstructable;
import com.github.technus.tectech.mechanics.structure.IStructureDefinition;
import com.github.technus.tectech.mechanics.structure.StructureDefinition;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.github.technus.tectech.util.CommonValues;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import static com.github.technus.tectech.mechanics.structure.StructureUtility.ofBlock;
import static com.github.technus.tectech.mechanics.structure.StructureUtility.ofHatchAdderOptional;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_decay.URANIUM_INGOT_MASS_DIFF;
import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_decay.URANIUM_MASS_TO_EU_INSTANT;
import static gregtech.api.enums.GT_Values.E;
import static net.minecraft.util.StatCollector.translateToLocal;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_bhg extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    //region variables
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;

    private static final double NEUTRONIUM_BLOCK_MASS = 4.1E17;
    private static final double NEUTRONIUM_BLOCK_ATOM_COUNT = 2.4478671E44;
    private static final double NEUTRONIUM_BLOCK_TO_EU_INSTANT = URANIUM_MASS_TO_EU_INSTANT * NEUTRONIUM_BLOCK_MASS / (URANIUM_INGOT_MASS_DIFF * 1.78266191e-36);//~ 5.314e40
    private static final double NEUTRON_TO_EU_INSTANT = NEUTRONIUM_BLOCK_TO_EU_INSTANT / NEUTRONIUM_BLOCK_ATOM_COUNT;//~ 0.00021708694

    public boolean glassDome = false;
    //endregion

    //Time dillatation - to slow down the explosion thing but REALLY REDUCE POWER OUTPUT
    //Startcodes to startup
    //per dim disable thingies

    //region structure actual
    private static final IStructureDefinition<GT_MetaTileEntity_EM_bhg> STRUCTURE_DEFINITION= StructureDefinition
            .<GT_MetaTileEntity_EM_bhg>builder()
            .addShapeOldApi("t1",new String[][]{
                    {"\u000B", "M0000000", "L00     00", "L0       0", "L0  !!!  0", "L0  !.!  0", "L0  !!!  0", "L0       0", "L00     00", "M0000000",},
                    {"\u0008", "O0A0", "O0A0", "O0A0", "O0A0", "N11111", "M1101011", "I000010010010000", "M1111111", "I000010010010000", "M1101011", "N11111", "O0A0", "O0A0", "O0A0", "O0A0",},
                    {"\u0006", "O0A0", "O0A0", "O0A0", "P1", "P1", "M1111111", "L11E11", "L1B222B1", "G000B1A23332A1B000", "J111A23332A111", "G000B1A23332A1B000", "L1B222B1", "L11E11", "M1111111", "P1", "P1", "O0A0", "O0A0", "O0A0",},
                    {"\u0005", "O0A0", "O0A0", "P1", "P1", "\u0004", "F00Q00", "H11M11", "F00Q00", "\u0004", "P1", "P1", "O0A0", "O0A0",},
                    {"\u0004", "O0A0", "N00000", "P1", "P4", "P4", "\u0003", "F0S0", "E00S00", "F0144M4410", "E00S00", "F0S0", "\u0003", "P4", "P4", "P1", "N00000", "O0A0",},
                    {"\u0003", "O0A0", "O0A0", "P1", "M2224222", "\u0004", "G2Q2", "G2Q2", "D00A2Q2A00", "F14Q41", "D00A2Q2A00", "G2Q2", "G2Q2", "\u0004", "M2224222", "P1", "O0A0", "O0A0",},
                    {"\u0002", "O0A0", "N00000", "P1", "P4", "\u0006", "D0W0", "C00W00", "D014S410", "C00W00", "D0W0", "\u0006", "P4", "P1", "N00000", "O0A0",},
                    {"\u0001", "O0A0", "O0A0", "P1", "M2224222", "\u0006", "E2U2", "E2U2", "B00A2U2A00", "D14U41", "B00A2U2A00", "E2U2", "E2U2", "\u0006", "M2224222", "P1", "O0A0", "O0A0",},
                    {"\u0001", "O0A0", "P1", "P4", "\u0009", "B0[0", "C14W41", "B0[0", "\u0009", "P4", "P1", "O0A0",},
                    {E, "O0A0", "O0A0", "P1", "P4", "\u0009", "A00[00", "C14W41", "A00[00", "\u0009", "P4", "P1", "O0A0", "O0A0",},
                    {E, "O0A0", "P1", "\u000B", "A0]0", "B1[1", "A0]0", "\u000B", "P1", "O0A0",},
                    {E, "O0A0", "P1", "\u000B", "A0]0", "B1[1", "A0]0", "\u000B", "P1", "O0A0",},
                    {"O0A0", "O0A0", "M1111111", "\u0009", "B1[1", "B1[1", "001[100", "B1[1", "001[100", "B1[1", "B1[1", "\u0009", "M1111111", "O0A0", "O0A0",},
                    {"O0A0", "N11111", "L11E11", "\u0001", "G2Q2", E, "E2U2", "\u0003", "B1[1", "B1[1", "A1]1", "01]10", "A1]1", "01]10", "A1]1", "B1[1", "B1[1", "\u0003", "E2U2", E, "G2Q2", "\u0001", "L11E11", "N11111", "O0A0",},
                    {"O0A0", "M1101011", "L1B222B1", E, "F0S0", "G2Q2", "D0W0", "E2U2", "\u0003", "B1[1", "A1]1", "A1]1", "002[200", "A12[21", "002[200", "A1]1", "A1]1", "B1[1", "\u0003", "E2U2", "D0W0", "G2Q2", "F0S0", E, "L1B222B1", "M1101011", "O0A0",},
                    {"L000000000", "I000010010010000", "G000B1A23332A1B000", "F00Q00", "E00S00", "D00A2Q2A00", "C00W00", "B00A2U2A00", "B0[0", "A00[00", "A0]0", "A0]0", "001[100", "01]10", "002[200", "003[300", "013[310", "003[300", "002[200", "01]10", "001[100", "A0]0", "A0]0", "A00[00", "B0[0", "B00A2U2A00", "C00W00", "D00A2Q2A00", "E00S00", "F00Q00", "G000B1A23332A1B000", "I000010010010000", "L000000000",},
                    {"O0A0", "M1111111", "J111A23332A111", "H11M11", "F0144M4410", "F14Q41", "D014S410", "D14U41", "C14W41", "C14W41", "B1[1", "B1[1", "B1[1", "A1]1", "A12[21", "013[310", "A13[31", "013[310", "A12[21", "A1]1", "B1[1", "B1[1", "B1[1", "C14W41", "C14W41", "D14U41", "D014S410", "F14Q41", "F0144M4410", "H11M11", "J111A23332A111", "M1111111", "O0A0",},
                    {"L000000000", "I000010010010000", "G000B1A23332A1B000", "F00Q00", "E00S00", "D00A2Q2A00", "C00W00", "B00A2U2A00", "B0[0", "A00[00", "A0]0", "A0]0", "001[100", "01]10", "002[200", "003[300", "013[310", "003[300", "002[200", "01]10", "001[100", "A0]0", "A0]0", "A00[00", "B0[0", "B00A2U2A00", "C00W00", "D00A2Q2A00", "E00S00", "F00Q00", "G000B1A23332A1B000", "I000010010010000", "L000000000",},
                    {"O0A0", "M1101011", "L1B222B1", E, "F0S0", "G2Q2", "D0W0", "E2U2", "\u0003", "B1[1", "A1]1", "A1]1", "002[200", "A12[21", "002[200", "A1]1", "A1]1", "B1[1", "\u0003", "E2U2", "D0W0", "G2Q2", "F0S0", E, "L1B222B1", "M1101011", "O0A0",},
                    {"O0A0", "N11111", "L11E11", "\u0001", "G2Q2", E, "E2U2", "\u0003", "B1[1", "B1[1", "A1]1", "01]10", "A1]1", "01]10", "A1]1", "B1[1", "B1[1", "\u0003", "E2U2", E, "G2Q2", "\u0001", "L11E11", "N11111", "O0A0",},
                    {"O0A0", "O0A0", "M1111111", "\u0009", "B1[1", "B1[1", "001[100", "B1[1", "001[100", "B1[1", "B1[1", "\u0009", "M1111111", "O0A0", "O0A0",},
                    {E, "O0A0", "P1", "\u000B", "A0]0", "B1[1", "A0]0", "\u000B", "P1", "O0A0",},
                    {E, "O0A0", "P1", "\u000B", "A0]0", "B1[1", "A0]0", "\u000B", "P1", "O0A0",},
                    {E, "O0A0", "O0A0", "P1", "P4", "\u0009", "A00[00", "C14W41", "A00[00", "\u0009", "P4", "P1", "O0A0", "O0A0",},
                    {"\u0001", "O0A0", "P1", "P4", "\u0009", "B0[0", "C14W41", "B0[0", "\u0009", "P4", "P1", "O0A0",},
                    {"\u0001", "O0A0", "O0A0", "P1", "M2224222", "\u0006", "E2U2", "E2U2", "B00A2U2A00", "D14U41", "B00A2U2A00", "E2U2", "E2U2", "\u0006", "M2224222", "P1", "O0A0", "O0A0",},
                    {"\u0002", "O0A0", "N00000", "P1", "P4", "\u0006", "D0W0", "C00W00", "D014S410", "C00W00", "D0W0", "\u0006", "P4", "P1", "N00000", "O0A0",},
                    {"\u0003", "O0A0", "O0A0", "P1", "M2224222", "\u0004", "G2Q2", "G2Q2", "D00A2Q2A00", "F14Q41", "D00A2Q2A00", "G2Q2", "G2Q2", "\u0004", "M2224222", "P1", "O0A0", "O0A0",},
                    {"\u0004", "O0A0", "N00000", "P1", "P4", "P4", "\u0003", "F0S0", "E00S00", "F0144M4410", "E00S00", "F0S0", "\u0003", "P4", "P4", "P1", "N00000", "O0A0",},
                    {"\u0005", "O0A0", "O0A0", "P1", "P1", "\u0004", "F00Q00", "H11M11", "F00Q00", "\u0004", "P1", "P1", "O0A0", "O0A0",},
                    {"\u0006", "O0A0", "O0A0", "O0A0", "P1", "P1", "M1111111", "L11E11", "L1B222B1", "G000B1A23332A1B000", "J111A23332A111", "G000B1A23332A1B000", "L1B222B1", "L11E11", "M1111111", "P1", "P1", "O0A0", "O0A0", "O0A0",},
                    {"\u0008", "O0A0", "O0A0", "O0A0", "O0A0", "N11111", "M1101011", "I000010010010000", "M1111111", "I000010010010000", "M1101011", "N11111", "O0A0", "O0A0", "O0A0", "O0A0",},
                    {"\u000B", "O0A0", "O0A0", "O0A0", "L000000000", "O0A0", "L000000000", "O0A0", "O0A0", "O0A0",},
            })
            .addShapeOldApi("t2",new String[][]{
                    {"\u000B", "M0000000", "L00     00", "L0       0", "L0  !!!  0", "L0  !.!  0", "L0  !!!  0", "L0       0", "L00     00", "M0000000",},
                    {"\u0008", "O0A0", "M550A055", "L5550A0555", "K55550A05555", "J5555111115555", "J5551101011555", "I000010010010000", "M1111111", "I000010010010000", "J5551101011555", "J5555111115555", "K55550A05555", "L5550A0555", "M550A055", "O0A0",},
                    {"\u0006", "O0A0", "M550A055", "K55550A05555", "J555C1C555", "I555D1D555", "I55B1111111B55", "H55B11E11B55", "H55B1B222B1B55", "G000B1A23332A1B000", "J111A23332A111", "G000B1A23332A1B000", "H55B1B222B1B55", "H55B11E11B55", "I55B1111111B55", "I555D1D555", "J555C1C555", "K55550A05555", "M550A055", "O0A0",},
                    {"\u0005", "O0A0", "L5550A0555", "J555C1C555", "I55E1E55", "H55M55", "H5O5", "G55O55", "G5Q5", "G5Q5", "F00Q00", "H11M11", "F00Q00", "G5Q5", "G5Q5", "G55O55", "H5O5", "H55M55", "I55E1E55", "J555C1C555", "L5550A0555", "O0A0",},
                    {"\u0004", "O0A0", "K55500000555", "I555D1D555", "H55F4F55", "G55G4G55", "G5Q5", "F55Q55", "F5S5", "F5S5", "F0S0", "E00S00", "F0144M4410", "E00S00", "F0S0", "F5S5", "F5S5", "F55Q55", "G5Q5", "G55G4G55", "H55F4F55", "I555D1D555", "K55500000555", "O0A0",},
                    {"\u0003", "O0A0", "J555550A055555", "H555E1E555", "G55D2224222D55", "F55Q55", "F5S5", "E55S55", "E5U5", "E5U5", "E5A2Q2A5", "E5A2Q2A5", "D00A2Q2A00", "F14Q41", "D00A2Q2A00", "E5A2Q2A5", "E5A2Q2A5", "E5U5", "E5U5", "E55S55", "F5S5", "F55Q55", "G55D2224222D55", "H555E1E555", "J555550A055555", "O0A0",},
                    {"\u0002", "O0A0", "K55500000555", "H555E1E555", "G55G4G55", "F5S5", "E55S55", "E5U5", "E5U5", "D5W5", "D5W5", "D5W5", "D0W0", "C00W00", "D014S410", "C00W00", "D0W0", "D5W5", "D5W5", "D5W5", "E5U5", "E5U5", "E55S55", "F5S5", "G55G4G55", "H555E1E555", "K55500000555", "O0A0",},
                    {"\u0001", "O0A0", "L5550A0555", "I555D1D555", "G55D2224222D55", "F5S5", "E5U5", "E5U5", "D5W5", "D5W5", "D5W5", "C5Y5", "C5A2U2A5", "C5A2U2A5", "B00A2U2A00", "D14U41", "B00A2U2A00", "C5A2U2A5", "C5A2U2A5", "C5Y5", "D5W5", "D5W5", "D5W5", "E5U5", "E5U5", "F5S5", "G55D2224222D55", "I555D1D555", "L5550A0555", "O0A0",},
                    {"\u0001", "M550A055", "J555C1C555", "H55F4F55", "F55Q55", "E55S55", "E5U5", "D5W5", "D5W5", "C5Y5", "C5Y5", "C5Y5", "B5[5", "B5[5", "B0[0", "C14W41", "B0[0", "B5[5", "B5[5", "C5Y5", "C5Y5", "C5Y5", "D5W5", "D5W5", "E5U5", "E55S55", "F55Q55", "H55F4F55", "J555C1C555", "M550A055",},
                    {E, "O0A0", "K55550A05555", "I55E1E55", "G55G4G55", "F5S5", "E5U5", "D5W5", "D5W5", "C5Y5", "C5Y5", "B5[5", "B5[5", "B5[5", "B5[5", "A00[00", "C14W41", "A00[00", "B5[5", "B5[5", "B5[5", "B5[5", "C5Y5", "C5Y5", "D5W5", "D5W5", "E5U5", "F5S5", "G55G4G55", "I55E1E55", "K55550A05555", "O0A0",},
                    {E, "M550A055", "J555C1C555", "H55M55", "G5Q5", "E55S55", "E5U5", "D5W5", "C5Y5", "C5Y5", "B5[5", "B5[5", "B5[5", "A5]5", "A5]5", "A0]0", "B1[1", "A0]0", "A5]5", "A5]5", "B5[5", "B5[5", "B5[5", "C5Y5", "C5Y5", "D5W5", "E5U5", "E55S55", "G5Q5", "H55M55", "J555C1C555", "M550A055",},
                    {E, "L5550A0555", "I555D1D555", "H5O5", "F55Q55", "E5U5", "D5W5", "D5W5", "C5Y5", "B5[5", "B5[5", "B5[5", "A5]5", "A5]5", "A5]5", "A0]0", "B1[1", "A0]0", "A5]5", "A5]5", "A5]5", "B5[5", "B5[5", "B5[5", "C5Y5", "D5W5", "D5W5", "E5U5", "F55Q55", "H5O5", "I555D1D555", "L5550A0555",},
                    {"O0A0", "K55550A05555", "I55B1111111B55", "G55O55", "F5S5", "E5U5", "D5W5", "C5Y5", "C5Y5", "B5[5", "B5[5", "A5]5", "A5]5", "A51[15", "A51[15", "001[100", "B1[1", "001[100", "A51[15", "A51[15", "A5]5", "A5]5", "B5[5", "B5[5", "C5Y5", "C5Y5", "D5W5", "E5U5", "F5S5", "G55O55", "I55B1111111B55", "K55550A05555", "O0A0",},
                    {"O0A0", "J5555111115555", "H55B11E11B55", "G5Q5", "F5S5", "E5A2Q2A5", "D5W5", "C5A2U2A5", "B5[5", "B5[5", "A5]5", "A5]5", "A51[15", "A51[15", "A1]1", "01]10", "A1]1", "01]10", "A1]1", "A51[15", "A51[15", "A5]5", "A5]5", "B5[5", "B5[5", "C5A2U2A5", "D5W5", "E5A2Q2A5", "F5S5", "G5Q5", "H55B11E11B55", "J5555111115555", "O0A0",},
                    {"O0A0", "J5551101011555", "H55B1B222B1B55", "G5Q5", "F0S0", "E5A2Q2A5", "D0W0", "C5A2U2A5", "B5[5", "B5[5", "A5]5", "A5]5", "A51[15", "A1]1", "A1]1", "002[200", "A12[21", "002[200", "A1]1", "A1]1", "A51[15", "A5]5", "A5]5", "B5[5", "B5[5", "C5A2U2A5", "D0W0", "E5A2Q2A5", "F0S0", "G5Q5", "H55B1B222B1B55", "J5551101011555", "O0A0",},
                    {"L000000000", "I000010010010000", "G000B1A23332A1B000", "F00Q00", "E00S00", "D00A2Q2A00", "C00W00", "B00A2U2A00", "B0[0", "A00[00", "A0]0", "A0]0", "001[100", "01]10", "002[200", "003[300", "013[310", "003[300", "002[200", "01]10", "001[100", "A0]0", "A0]0", "A00[00", "B0[0", "B00A2U2A00", "C00W00", "D00A2Q2A00", "E00S00", "F00Q00", "G000B1A23332A1B000", "I000010010010000", "L000000000",},
                    {"O0A0", "M1111111", "J111A23332A111", "H11M11", "F0144M4410", "F14Q41", "D014S410", "D14U41", "C14W41", "C14W41", "B1[1", "B1[1", "B1[1", "A1]1", "A12[21", "013[310", "A13[31", "013[310", "A12[21", "A1]1", "B1[1", "B1[1", "B1[1", "C14W41", "C14W41", "D14U41", "D014S410", "F14Q41", "F0144M4410", "H11M11", "J111A23332A111", "M1111111", "O0A0",},
                    {"L000000000", "I000010010010000", "G000B1A23332A1B000", "F00Q00", "E00S00", "D00A2Q2A00", "C00W00", "B00A2U2A00", "B0[0", "A00[00", "A0]0", "A0]0", "001[100", "01]10", "002[200", "003[300", "013[310", "003[300", "002[200", "01]10", "001[100", "A0]0", "A0]0", "A00[00", "B0[0", "B00A2U2A00", "C00W00", "D00A2Q2A00", "E00S00", "F00Q00", "G000B1A23332A1B000", "I000010010010000", "L000000000",},
                    {"O0A0", "J5551101011555", "H55B1B222B1B55", "G5Q5", "F0S0", "E5A2Q2A5", "D0W0", "C5A2U2A5", "B5[5", "B5[5", "A5]5", "A5]5", "A51[15", "A1]1", "A1]1", "002[200", "A12[21", "002[200", "A1]1", "A1]1", "A51[15", "A5]5", "A5]5", "B5[5", "B5[5", "C5A2U2A5", "D0W0", "E5A2Q2A5", "F0S0", "G5Q5", "H55B1B222B1B55", "J5551101011555", "O0A0",},
                    {"O0A0", "J5555111115555", "H55B11E11B55", "G5Q5", "F5S5", "E5A2Q2A5", "D5W5", "C5A2U2A5", "B5[5", "B5[5", "A5]5", "A5]5", "A51[15", "A51[15", "A1]1", "01]10", "A1]1", "01]10", "A1]1", "A51[15", "A51[15", "A5]5", "A5]5", "B5[5", "B5[5", "C5A2U2A5", "D5W5", "E5A2Q2A5", "F5S5", "G5Q5", "H55B11E11B55", "J5555111115555", "O0A0",},
                    {"O0A0", "K55550A05555", "I55B1111111B55", "G55O55", "F5S5", "E5U5", "D5W5", "C5Y5", "C5Y5", "B5[5", "B5[5", "A5]5", "A5]5", "A51[15", "A51[15", "001[100", "B1[1", "001[100", "A51[15", "A51[15", "A5]5", "A5]5", "B5[5", "B5[5", "C5Y5", "C5Y5", "D5W5", "E5U5", "F5S5", "G55O55", "I55B1111111B55", "K55550A05555", "O0A0",},
                    {E, "L5550A0555", "I555D1D555", "H5O5", "F55Q55", "E5U5", "D5W5", "D5W5", "C5Y5", "B5[5", "B5[5", "B5[5", "A5]5", "A5]5", "A5]5", "A0]0", "B1[1", "A0]0", "A5]5", "A5]5", "A5]5", "B5[5", "B5[5", "B5[5", "C5Y5", "D5W5", "D5W5", "E5U5", "F55Q55", "H5O5", "I555D1D555", "L5550A0555",},
                    {E, "M550A055", "J555C1C555", "H55M55", "G5Q5", "E55S55", "E5U5", "D5W5", "C5Y5", "C5Y5", "B5[5", "B5[5", "B5[5", "A5]5", "A5]5", "A0]0", "B1[1", "A0]0", "A5]5", "A5]5", "B5[5", "B5[5", "B5[5", "C5Y5", "C5Y5", "D5W5", "E5U5", "E55S55", "G5Q5", "H55M55", "J555C1C555", "M550A055",},
                    {E, "O0A0", "K55550A05555", "I55E1E55", "G55G4G55", "F5S5", "E5U5", "D5W5", "D5W5", "C5Y5", "C5Y5", "B5[5", "B5[5", "B5[5", "B5[5", "A00[00", "C14W41", "A00[00", "B5[5", "B5[5", "B5[5", "B5[5", "C5Y5", "C5Y5", "D5W5", "D5W5", "E5U5", "F5S5", "G55G4G55", "I55E1E55", "K55550A05555", "O0A0",},
                    {"\u0001", "M550A055", "J555C1C555", "H55F4F55", "F55Q55", "E55S55", "E5U5", "D5W5", "D5W5", "C5Y5", "C5Y5", "C5Y5", "B5[5", "B5[5", "B0[0", "C14W41", "B0[0", "B5[5", "B5[5", "C5Y5", "C5Y5", "C5Y5", "D5W5", "D5W5", "E5U5", "E55S55", "F55Q55", "H55F4F55", "J555C1C555", "M550A055",},
                    {"\u0001", "O0A0", "L5550A0555", "I555D1D555", "G55D2224222D55", "F5S5", "E5U5", "E5U5", "D5W5", "D5W5", "D5W5", "C5Y5", "C5A2U2A5", "C5A2U2A5", "B00A2U2A00", "D14U41", "B00A2U2A00", "C5A2U2A5", "C5A2U2A5", "C5Y5", "D5W5", "D5W5", "D5W5", "E5U5", "E5U5", "F5S5", "G55D2224222D55", "I555D1D555", "L5550A0555", "O0A0",},
                    {"\u0002", "O0A0", "K55500000555", "H555E1E555", "G55G4G55", "F5S5", "E55S55", "E5U5", "E5U5", "D5W5", "D5W5", "D5W5", "D0W0", "C00W00", "D014S410", "C00W00", "D0W0", "D5W5", "D5W5", "D5W5", "E5U5", "E5U5", "E55S55", "F5S5", "G55G4G55", "H555E1E555", "K55500000555", "O0A0",},
                    {"\u0003", "O0A0", "J555550A055555", "H555E1E555", "G55D2224222D55", "F55Q55", "F5S5", "E55S55", "E5U5", "E5U5", "E5A2Q2A5", "E5A2Q2A5", "D00A2Q2A00", "F14Q41", "D00A2Q2A00", "E5A2Q2A5", "E5A2Q2A5", "E5U5", "E5U5", "E55S55", "F5S5", "F55Q55", "G55D2224222D55", "H555E1E555", "J555550A055555", "O0A0",},
                    {"\u0004", "O0A0", "K55500000555", "I555D1D555", "H55F4F55", "G55G4G55", "G5Q5", "F55Q55", "F5S5", "F5S5", "F0S0", "E00S00", "F0144M4410", "E00S00", "F0S0", "F5S5", "F5S5", "F55Q55", "G5Q5", "G55G4G55", "H55F4F55", "I555D1D555", "K55500000555", "O0A0",},
                    {"\u0005", "O0A0", "L5550A0555", "J555C1C555", "I55E1E55", "H55M55", "H5O5", "G55O55", "G5Q5", "G5Q5", "F00Q00", "H11M11", "F00Q00", "G5Q5", "G5Q5", "G55O55", "H5O5", "H55M55", "I55E1E55", "J555C1C555", "L5550A0555", "O0A0",},
                    {"\u0006", "O0A0", "M550A055", "K55550A05555", "J555C1C555", "I555D1D555", "I55B1111111B55", "H55B11E11B55", "H55B1B222B1B55", "G000B1A23332A1B000", "J111A23332A111", "G000B1A23332A1B000", "H55B1B222B1B55", "H55B11E11B55", "I55B1111111B55", "I555D1D555", "J555C1C555", "K55550A05555", "M550A055", "O0A0",},
                    {"\u0008", "O0A0", "M550A055", "L5550A0555", "K55550A05555", "J5555111115555", "J5551101011555", "I000010010010000", "M1111111", "I000010010010000", "J5551101011555", "J5555111115555", "K55550A05555", "L5550A0555", "M550A055", "O0A0",},
                    {"\u000B", "O0A0", "O0A0", "O0A0", "L000000000", "O0A0", "L000000000", "O0A0", "O0A0", "O0A0",},
            })
            .addElement('0', ofBlock(sBlockCasingsTT,12))
            .addElement('1', ofBlock(sBlockCasingsTT,13))
            .addElement('2', ofBlock(sBlockCasingsTT,14))
            .addElement('3', ofBlock(sBlockCasingsTT,10))
            .addElement('4', ofBlock(sBlockCasingsTT,11))
            .addElement('5', ofBlock(QuantumGlassBlock.INSTANCE,0))
            .addElement(' ', ofHatchAdderOptional(GT_MetaTileEntity_EM_bhg::addClassicToMachineList,
                    textureOffset,1,sBlockCasingsTT,0))
            .addElement('!', ofHatchAdderOptional(GT_MetaTileEntity_EM_bhg::addElementalToMachineList,
                    textureOffset + 4,2,sBlockCasingsTT,4))
            .build();

    @Override
    public IStructureDefinition<? extends GT_MetaTileEntity_MultiblockBase_EM> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
            translateToLocal("gt.blockmachines.multimachine.em.blackholegenerator.hint.0"),//1 - Classic Hatches or High Power Casing
            translateToLocal("gt.blockmachines.multimachine.em.blackholegenerator.hint.1"),//2 - Elemental Hatches or Molecular Casing
    };
    //endregion

    public GT_MetaTileEntity_EM_bhg(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_bhg(String aName) {
        super(aName);
    }

    /**
     * Black hole event horizon radius calculator
     *
     * @param massKg mass in kg
     * @return radius in meters
     */
    private static double getSchwarzschildRadius(double massKg) {
        return massKg * 1.48523238761875E-27;
    }

    /**
     * Black hole event horizon surface area calculator
     *
     * @param massKg mass in kg
     * @return area in meters^2
     */
    private static double getSchwarzschildArea(double massKg) {
        return Math.pow(getSchwarzschildRadius(massKg), 2) * 12.566370614359172;
    }

    /**
     * Black hole event horizon temperature calculator
     *
     * @param massKg mass in kg
     * @return temperature in K
     */
    private static double getTemperature(double massKg) {
        return 2.841438513199716E-9 / (2.3159488515170722E-32 * massKg);
    }

    /**
     * Black hole luminosity calculator
     *
     * @param massKg mass in kg
     * @return luminosity in watts
     */
    private static double getLuminosity(double massKg) {
        return getSchwarzschildArea(massKg) * 5.670373e-8 * Math.pow(getTemperature(massKg), 4);
    }

    /**
     * Black hole acretion disk luminosity calculator
     *
     * @param massKgPer1s mass injection kg per s
     * @return luminosity in watts
     */
    private static double getAcretionDiskLuminosity(double massKgPer1s) {
        return massKgPer1s * 7.48962648947348E15;
    }

    /**
     * Black hole gravity field calculator, should be used for gravity blasting
     *
     * @param massKg     mass in kg
     * @param distanceSq distance squared in meters
     * @return gravity field
     */
    private static double getGravityField(double massKg, double distanceSq) {
        return massKg * 6.6743015e-11 / distanceSq;
    }

    /**
     * Black hole containment force calculator
     *
     * @param massKg   mass in kg
     * @param radiusSq radius squared in meters
     * @return force in newtons
     */
    private static double getContainmentForce(double massKg, double radiusSq) {
        return Math.pow(massKg, 2) * 6.6743015e-11 / radiusSq;
    }

    /**
     * Black hole containment pressure calculator F/s, should be used for bhg initial release explosion?
     *
     * @param massKg   mass in kg
     * @param radiusSq radius squared in meters
     * @return pressure in pascals
     */
    private static double getContainmentPressure(double massKg, double radiusSq) {
        return getContainmentForce(massKg, radiusSq) / (12.566370614359172 * radiusSq);
    }

    /**
     * Black hole containment energy calculator, assuming F*s, and 100% efficient gravity force field
     *
     * @param massKg mass in kg
     * @return power in watts
     */
    private static double getContainmentPower(double massKg) {
        return Math.pow(massKg, 2) * 8.387174624097334E-10;
    }

    /**
     * Black hole power balance, zero at mass ~= 2.5525e10 (T~=4.8067e12)
     *
     * @param massKg      mass in kg
     * @param massKgPer1s mass injection kg per s
     * @return power in watts
     */
    @Deprecated
    private static double getContainmentPowerBalance(double massKg, double massKgPer1s) {
        return getLuminosity(massKg) + getAcretionDiskLuminosity(massKgPer1s) - getContainmentPower(massKg);
    }

    //todo compaction energy 8 * Long.MAx_VALUE?

    //todo neutronium decay gen? 0.0007186885 mass diff - actually compute hydrogen amount...

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_bhg(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        if (structureCheck_EM("t2", 16, 16, 0)) {
            glassDome = true;
            return true;
        }
        if (structureCheck_EM("t1", 16, 16, 0)) {
            glassDome = false;
            return true;
        }
        //todo check tiers of hatches!!!!
        return false;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                translateToLocal("gt.blockmachines.multimachine.em.blackholegenerator.desc.0"),//Singularity based power generation.
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + translateToLocal("gt.blockmachines.multimachine.em.blackholegenerator.desc.1")//Super unstable!!!
        };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/EM_BHG");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/EM_BHG_ACTIVE");
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
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM((stackSize.stackSize&1)==1?"t1":"t2", 16, 16, 0, hintsOnly,stackSize);
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }
}