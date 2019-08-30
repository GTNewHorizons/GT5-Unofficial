package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.IHatchAdder;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedTexture;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import static com.github.technus.tectech.Util.StructureBuilderExtreme;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_decay.URANIUM_INGOT_MASS_DIFF;
import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_decay.URANIUM_MASS_TO_EU_INSTANT;
import static gregtech.api.enums.GT_Values.E;

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
    private static final String[][] shape = new String[][]{
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
    };
    private static final Block[] blockType = new Block[]{sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMeta = new byte[]{12, 13, 14, 10, 11};
    private final IHatchAdder[] addingMethods = new IHatchAdder[]{this::addClassicToMachineList, this::addElementalToMachineList};
    private static final short[] casingTextures = new short[]{textureOffset, textureOffset + 4};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMetaFallback = new byte[]{0, 4};
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA + "Hint Details:",
            "1 - Classic Hatches or High Power Casing",
            "2 - Elemental Hatches or Molecular Casing",
    };
    //endregion

    //region structure dank - glass sphere for the looks
    private static final String[][] shape2 = new String[][]{
            {"\u000B", "M0000000", "L00     00", "L0       0", "L0  !!!  0", "L0  !.!  0", "L0  !!!  0", "L0       0", "L00     00", "M0000000",},
            {"\u0008", "O0A0", "M110A011", "L1110A0111", "K11110A01111", "J1111222221111", "J1112202022111", "I000020020020000", "M2222222", "I000020020020000", "J1112202022111", "J1111222221111", "K11110A01111", "L1110A0111", "M110A011", "O0A0",},
            {"\u0006", "O0A0", "M110A011", "K11110A01111", "J111C2C111", "I111D2D111", "I11B2222222B11", "H11B22E22B11", "H11B2B333B2B11", "G000B2A34443A2B000", "J222A34443A222", "G000B2A34443A2B000", "H11B2B333B2B11", "H11B22E22B11", "I11B2222222B11", "I111D2D111", "J111C2C111", "K11110A01111", "M110A011", "O0A0",},
            {"\u0005", "O0A0", "L1110A0111", "J111C2C111", "I11E2E11", "H11M11", "H1O1", "G11O11", "G1Q1", "G1Q1", "F00Q00", "H22M22", "F00Q00", "G1Q1", "G1Q1", "G11O11", "H1O1", "H11M11", "I11E2E11", "J111C2C111", "L1110A0111", "O0A0",},
            {"\u0004", "O0A0", "K11100000111", "I111D2D111", "H11F5F11", "G11G5G11", "G1Q1", "F11Q11", "F1S1", "F1S1", "F0S0", "E00S00", "F0255M5520", "E00S00", "F0S0", "F1S1", "F1S1", "F11Q11", "G1Q1", "G11G5G11", "H11F5F11", "I111D2D111", "K11100000111", "O0A0",},
            {"\u0003", "O0A0", "J111110A011111", "H111E2E111", "G11D3335333D11", "F11Q11", "F1S1", "E11S11", "E1U1", "E1U1", "E1A3Q3A1", "E1A3Q3A1", "D00A3Q3A00", "F25Q52", "D00A3Q3A00", "E1A3Q3A1", "E1A3Q3A1", "E1U1", "E1U1", "E11S11", "F1S1", "F11Q11", "G11D3335333D11", "H111E2E111", "J111110A011111", "O0A0",},
            {"\u0002", "O0A0", "K11100000111", "H111E2E111", "G11G5G11", "F1S1", "E11S11", "E1U1", "E1U1", "D1W1", "D1W1", "D1W1", "D0W0", "C00W00", "D025S520", "C00W00", "D0W0", "D1W1", "D1W1", "D1W1", "E1U1", "E1U1", "E11S11", "F1S1", "G11G5G11", "H111E2E111", "K11100000111", "O0A0",},
            {"\u0001", "O0A0", "L1110A0111", "I111D2D111", "G11D3335333D11", "F1S1", "E1U1", "E1U1", "D1W1", "D1W1", "D1W1", "C1Y1", "C1A3U3A1", "C1A3U3A1", "B00A3U3A00", "D25U52", "B00A3U3A00", "C1A3U3A1", "C1A3U3A1", "C1Y1", "D1W1", "D1W1", "D1W1", "E1U1", "E1U1", "F1S1", "G11D3335333D11", "I111D2D111", "L1110A0111", "O0A0",},
            {"\u0001", "M110A011", "J111C2C111", "H11F5F11", "F11Q11", "E11S11", "E1U1", "D1W1", "D1W1", "C1Y1", "C1Y1", "C1Y1", "B1[1", "B1[1", "B0[0", "C25W52", "B0[0", "B1[1", "B1[1", "C1Y1", "C1Y1", "C1Y1", "D1W1", "D1W1", "E1U1", "E11S11", "F11Q11", "H11F5F11", "J111C2C111", "M110A011",},
            {E, "O0A0", "K11110A01111", "I11E2E11", "G11G5G11", "F1S1", "E1U1", "D1W1", "D1W1", "C1Y1", "C1Y1", "B1[1", "B1[1", "B1[1", "B1[1", "A00[00", "C25W52", "A00[00", "B1[1", "B1[1", "B1[1", "B1[1", "C1Y1", "C1Y1", "D1W1", "D1W1", "E1U1", "F1S1", "G11G5G11", "I11E2E11", "K11110A01111", "O0A0",},
            {E, "M110A011", "J111C2C111", "H11M11", "G1Q1", "E11S11", "E1U1", "D1W1", "C1Y1", "C1Y1", "B1[1", "B1[1", "B1[1", "A1]1", "A1]1", "A0]0", "B2[2", "A0]0", "A1]1", "A1]1", "B1[1", "B1[1", "B1[1", "C1Y1", "C1Y1", "D1W1", "E1U1", "E11S11", "G1Q1", "H11M11", "J111C2C111", "M110A011",},
            {E, "L1110A0111", "I111D2D111", "H1O1", "F11Q11", "E1U1", "D1W1", "D1W1", "C1Y1", "B1[1", "B1[1", "B1[1", "A1]1", "A1]1", "A1]1", "A0]0", "B2[2", "A0]0", "A1]1", "A1]1", "A1]1", "B1[1", "B1[1", "B1[1", "C1Y1", "D1W1", "D1W1", "E1U1", "F11Q11", "H1O1", "I111D2D111", "L1110A0111",},
            {"O0A0", "K11110A01111", "I11B2222222B11", "G11O11", "F1S1", "E1U1", "D1W1", "C1Y1", "C1Y1", "B1[1", "B1[1", "A1]1", "A1]1", "A12[21", "A12[21", "002[200", "B2[2", "002[200", "A12[21", "A12[21", "A1]1", "A1]1", "B1[1", "B1[1", "C1Y1", "C1Y1", "D1W1", "E1U1", "F1S1", "G11O11", "I11B2222222B11", "K11110A01111", "O0A0",},
            {"O0A0", "J1111222221111", "H11B22E22B11", "G1Q1", "F1S1", "E1A3Q3A1", "D1W1", "C1A3U3A1", "B1[1", "B1[1", "A1]1", "A1]1", "A12[21", "A12[21", "A2]2", "02]20", "A2]2", "02]20", "A2]2", "A12[21", "A12[21", "A1]1", "A1]1", "B1[1", "B1[1", "C1A3U3A1", "D1W1", "E1A3Q3A1", "F1S1", "G1Q1", "H11B22E22B11", "J1111222221111", "O0A0",},
            {"O0A0", "J1112202022111", "H11B2B333B2B11", "G1Q1", "F0S0", "E1A3Q3A1", "D0W0", "C1A3U3A1", "B1[1", "B1[1", "A1]1", "A1]1", "A12[21", "A2]2", "A2]2", "003[300", "A23[32", "003[300", "A2]2", "A2]2", "A12[21", "A1]1", "A1]1", "B1[1", "B1[1", "C1A3U3A1", "D0W0", "E1A3Q3A1", "F0S0", "G1Q1", "H11B2B333B2B11", "J1112202022111", "O0A0",},
            {"L000000000", "I000020020020000", "G000B2A34443A2B000", "F00Q00", "E00S00", "D00A3Q3A00", "C00W00", "B00A3U3A00", "B0[0", "A00[00", "A0]0", "A0]0", "002[200", "02]20", "003[300", "004[400", "024[420", "004[400", "003[300", "02]20", "002[200", "A0]0", "A0]0", "A00[00", "B0[0", "B00A3U3A00", "C00W00", "D00A3Q3A00", "E00S00", "F00Q00", "G000B2A34443A2B000", "I000020020020000", "L000000000",},
            {"O0A0", "M2222222", "J222A34443A222", "H22M22", "F0255M5520", "F25Q52", "D025S520", "D25U52", "C25W52", "C25W52", "B2[2", "B2[2", "B2[2", "A2]2", "A23[32", "024[420", "A24[42", "024[420", "A23[32", "A2]2", "B2[2", "B2[2", "B2[2", "C25W52", "C25W52", "D25U52", "D025S520", "F25Q52", "F0255M5520", "H22M22", "J222A34443A222", "M2222222", "O0A0",},
            {"L000000000", "I000020020020000", "G000B2A34443A2B000", "F00Q00", "E00S00", "D00A3Q3A00", "C00W00", "B00A3U3A00", "B0[0", "A00[00", "A0]0", "A0]0", "002[200", "02]20", "003[300", "004[400", "024[420", "004[400", "003[300", "02]20", "002[200", "A0]0", "A0]0", "A00[00", "B0[0", "B00A3U3A00", "C00W00", "D00A3Q3A00", "E00S00", "F00Q00", "G000B2A34443A2B000", "I000020020020000", "L000000000",},
            {"O0A0", "J1112202022111", "H11B2B333B2B11", "G1Q1", "F0S0", "E1A3Q3A1", "D0W0", "C1A3U3A1", "B1[1", "B1[1", "A1]1", "A1]1", "A12[21", "A2]2", "A2]2", "003[300", "A23[32", "003[300", "A2]2", "A2]2", "A12[21", "A1]1", "A1]1", "B1[1", "B1[1", "C1A3U3A1", "D0W0", "E1A3Q3A1", "F0S0", "G1Q1", "H11B2B333B2B11", "J1112202022111", "O0A0",},
            {"O0A0", "J1111222221111", "H11B22E22B11", "G1Q1", "F1S1", "E1A3Q3A1", "D1W1", "C1A3U3A1", "B1[1", "B1[1", "A1]1", "A1]1", "A12[21", "A12[21", "A2]2", "02]20", "A2]2", "02]20", "A2]2", "A12[21", "A12[21", "A1]1", "A1]1", "B1[1", "B1[1", "C1A3U3A1", "D1W1", "E1A3Q3A1", "F1S1", "G1Q1", "H11B22E22B11", "J1111222221111", "O0A0",},
            {"O0A0", "K11110A01111", "I11B2222222B11", "G11O11", "F1S1", "E1U1", "D1W1", "C1Y1", "C1Y1", "B1[1", "B1[1", "A1]1", "A1]1", "A12[21", "A12[21", "002[200", "B2[2", "002[200", "A12[21", "A12[21", "A1]1", "A1]1", "B1[1", "B1[1", "C1Y1", "C1Y1", "D1W1", "E1U1", "F1S1", "G11O11", "I11B2222222B11", "K11110A01111", "O0A0",},
            {E, "L1110A0111", "I111D2D111", "H1O1", "F11Q11", "E1U1", "D1W1", "D1W1", "C1Y1", "B1[1", "B1[1", "B1[1", "A1]1", "A1]1", "A1]1", "A0]0", "B2[2", "A0]0", "A1]1", "A1]1", "A1]1", "B1[1", "B1[1", "B1[1", "C1Y1", "D1W1", "D1W1", "E1U1", "F11Q11", "H1O1", "I111D2D111", "L1110A0111",},
            {E, "M110A011", "J111C2C111", "H11M11", "G1Q1", "E11S11", "E1U1", "D1W1", "C1Y1", "C1Y1", "B1[1", "B1[1", "B1[1", "A1]1", "A1]1", "A0]0", "B2[2", "A0]0", "A1]1", "A1]1", "B1[1", "B1[1", "B1[1", "C1Y1", "C1Y1", "D1W1", "E1U1", "E11S11", "G1Q1", "H11M11", "J111C2C111", "M110A011",},
            {E, "O0A0", "K11110A01111", "I11E2E11", "G11G5G11", "F1S1", "E1U1", "D1W1", "D1W1", "C1Y1", "C1Y1", "B1[1", "B1[1", "B1[1", "B1[1", "A00[00", "C25W52", "A00[00", "B1[1", "B1[1", "B1[1", "B1[1", "C1Y1", "C1Y1", "D1W1", "D1W1", "E1U1", "F1S1", "G11G5G11", "I11E2E11", "K11110A01111", "O0A0",},
            {"\u0001", "M110A011", "J111C2C111", "H11F5F11", "F11Q11", "E11S11", "E1U1", "D1W1", "D1W1", "C1Y1", "C1Y1", "C1Y1", "B1[1", "B1[1", "B0[0", "C25W52", "B0[0", "B1[1", "B1[1", "C1Y1", "C1Y1", "C1Y1", "D1W1", "D1W1", "E1U1", "E11S11", "F11Q11", "H11F5F11", "J111C2C111", "M110A011",},
            {"\u0001", "O0A0", "L1110A0111", "I111D2D111", "G11D3335333D11", "F1S1", "E1U1", "E1U1", "D1W1", "D1W1", "D1W1", "C1Y1", "C1A3U3A1", "C1A3U3A1", "B00A3U3A00", "D25U52", "B00A3U3A00", "C1A3U3A1", "C1A3U3A1", "C1Y1", "D1W1", "D1W1", "D1W1", "E1U1", "E1U1", "F1S1", "G11D3335333D11", "I111D2D111", "L1110A0111", "O0A0",},
            {"\u0002", "O0A0", "K11100000111", "H111E2E111", "G11G5G11", "F1S1", "E11S11", "E1U1", "E1U1", "D1W1", "D1W1", "D1W1", "D0W0", "C00W00", "D025S520", "C00W00", "D0W0", "D1W1", "D1W1", "D1W1", "E1U1", "E1U1", "E11S11", "F1S1", "G11G5G11", "H111E2E111", "K11100000111", "O0A0",},
            {"\u0003", "O0A0", "J111110A011111", "H111E2E111", "G11D3335333D11", "F11Q11", "F1S1", "E11S11", "E1U1", "E1U1", "E1A3Q3A1", "E1A3Q3A1", "D00A3Q3A00", "F25Q52", "D00A3Q3A00", "E1A3Q3A1", "E1A3Q3A1", "E1U1", "E1U1", "E11S11", "F1S1", "F11Q11", "G11D3335333D11", "H111E2E111", "J111110A011111", "O0A0",},
            {"\u0004", "O0A0", "K11100000111", "I111D2D111", "H11F5F11", "G11G5G11", "G1Q1", "F11Q11", "F1S1", "F1S1", "F0S0", "E00S00", "F0255M5520", "E00S00", "F0S0", "F1S1", "F1S1", "F11Q11", "G1Q1", "G11G5G11", "H11F5F11", "I111D2D111", "K11100000111", "O0A0",},
            {"\u0005", "O0A0", "L1110A0111", "J111C2C111", "I11E2E11", "H11M11", "H1O1", "G11O11", "G1Q1", "G1Q1", "F00Q00", "H22M22", "F00Q00", "G1Q1", "G1Q1", "G11O11", "H1O1", "H11M11", "I11E2E11", "J111C2C111", "L1110A0111", "O0A0",},
            {"\u0006", "O0A0", "M110A011", "K11110A01111", "J111C2C111", "I111D2D111", "I11B2222222B11", "H11B22E22B11", "H11B2B333B2B11", "G000B2A34443A2B000", "J222A34443A222", "G000B2A34443A2B000", "H11B2B333B2B11", "H11B22E22B11", "I11B2222222B11", "I111D2D111", "J111C2C111", "K11110A01111", "M110A011", "O0A0",},
            {"\u0008", "O0A0", "M110A011", "L1110A0111", "K11110A01111", "J1111222221111", "J1112202022111", "I000020020020000", "M2222222", "I000020020020000", "J1112202022111", "J1111222221111", "K11110A01111", "L1110A0111", "M110A011", "O0A0",},
            {"\u000B", "O0A0", "O0A0", "O0A0", "L000000000", "O0A0", "L000000000", "O0A0", "O0A0", "O0A0",},
    };
    private static final Block[] blockType2 = new Block[]{sBlockCasingsTT, QuantumGlassBlock.INSTANCE, sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMeta2 = new byte[]{12, 0, 13, 14, 10, 11};
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
        if (structureCheck_EM(shape2, blockType2, blockMeta2, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 16, 16, 0)) {
            glassDome = true;
            return true;
        }
        if (structureCheck_EM(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 16, 16, 0)) {
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
                "Singularity based power generation.",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Super unstable!!!"
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
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][12], new TT_RenderedTexture(aActive ? ScreenON : ScreenOFF)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][12]};
    }

    @Override
    public void construct(int stackSize, boolean hintsOnly) {
        if ((stackSize & 1) == 1) {
            StructureBuilderExtreme(shape, blockType, blockMeta, 16, 16, 0, getBaseMetaTileEntity(), this, hintsOnly);
        } else {
            StructureBuilderExtreme(shape2, blockType2, blockMeta2, 16, 16, 0, getBaseMetaTileEntity(), this, hintsOnly);
        }
    }

    @Override
    public String[] getStructureDescription(int stackSize) {
        return description;
    }
}