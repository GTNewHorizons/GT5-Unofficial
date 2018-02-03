package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import static com.github.technus.tectech.Util.StructureBuilder;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static gregtech.api.enums.GT_Values.E;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_bhg extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;

    public boolean glassDome=false;

    //Time dillatation - to slow down the explosion thing but REALLY REDUCE POWER OUTPUT
    //Startcodes to startup
    //per dim disable thingies

    //region Structure actual
    private static final String[][] shape = new String[][]{
            {"\u000B","M0000000","L00     00","L0       0","L0  !!!  0","L0  !.!  0","L0  !!!  0","L0       0","L00     00","M0000000",},
            {"\u0008","O0A0","O0A0","O0A0","O0A0","N11111","M1101011","I000010010010000","M1111111","I000010010010000","M1101011","N11111","O0A0","O0A0","O0A0","O0A0",},
            {"\u0006","O0A0","O0A0","O0A0","P1","P1","M1111111","L11E11","L1B222B1","G000B1A23332A1B000","J111A23332A111","G000B1A23332A1B000","L1B222B1","L11E11","M1111111","P1","P1","O0A0","O0A0","O0A0",},
            {"\u0005","O0A0","O0A0","P1","P1","\u0004","F00Q00","H11M11","F00Q00","\u0004","P1","P1","O0A0","O0A0",},
            {"\u0004","O0A0","N00000","P1","P4","P4","\u0003","F0S0","E00S00","F0144M4410","E00S00","F0S0","\u0003","P4","P4","P1","N00000","O0A0",},
            {"\u0003","O0A0","O0A0","P1","M2224222","\u0004","G2Q2","G2Q2","D00A2Q2A00","F14Q41","D00A2Q2A00","G2Q2","G2Q2","\u0004","M2224222","P1","O0A0","O0A0",},
            {"\u0002","O0A0","N00000","P1","P4","\u0006","D0W0","C00W00","D014S410","C00W00","D0W0","\u0006","P4","P1","N00000","O0A0",},
            {"\u0001","O0A0","O0A0","P1","M2224222","\u0006","E2U2","E2U2","B00A2U2A00","D14U41","B00A2U2A00","E2U2","E2U2","\u0006","M2224222","P1","O0A0","O0A0",},
            {"\u0001","O0A0","P1","P4","\u0009","B0[0","C14W41","B0[0","\u0009","P4","P1","O0A0",},
            {E,"O0A0","O0A0","P1","P4","\u0009","A00[00","C14W41","A00[00","\u0009","P4","P1","O0A0","O0A0",},
            {E,"O0A0","P1","\u000B","A0]0","B1[1","A0]0","\u000B","P1","O0A0",},
            {E,"O0A0","P1","\u000B","A0]0","B1[1","A0]0","\u000B","P1","O0A0",},
            {"O0A0","O0A0","M1111111","\u0009","B1[1","B1[1","001[100","B1[1","001[100","B1[1","B1[1","\u0009","M1111111","O0A0","O0A0",},
            {"O0A0","N11111","L11E11","\u0001","G2Q2",E,"E2U2","\u0003","B1[1","B1[1","A1]1","01]10","A1]1","01]10","A1]1","B1[1","B1[1","\u0003","E2U2",E,"G2Q2","\u0001","L11E11","N11111","O0A0",},
            {"O0A0","M1101011","L1B222B1",E,"F0S0","G2Q2","D0W0","E2U2","\u0003","B1[1","A1]1","A1]1","002[200","A12[21","002[200","A1]1","A1]1","B1[1","\u0003","E2U2","D0W0","G2Q2","F0S0",E,"L1B222B1","M1101011","O0A0",},
            {"L000000000","I000010010010000","G000B1A23332A1B000","F00Q00","E00S00","D00A2Q2A00","C00W00","B00A2U2A00","B0[0","A00[00","A0]0","A0]0","001[100","01]10","002[200","003[300","013[310","003[300","002[200","01]10","001[100","A0]0","A0]0","A00[00","B0[0","B00A2U2A00","C00W00","D00A2Q2A00","E00S00","F00Q00","G000B1A23332A1B000","I000010010010000","L000000000",},
            {"O0A0","M1111111","J111A23332A111","H11M11","F0144M4410","F14Q41","D014S410","D14U41","C14W41","C14W41","B1[1","B1[1","B1[1","A1]1","A12[21","013[310","A13[31","013[310","A12[21","A1]1","B1[1","B1[1","B1[1","C14W41","C14W41","D14U41","D014S410","F14Q41","F0144M4410","H11M11","J111A23332A111","M1111111","O0A0",},
            {"L000000000","I000010010010000","G000B1A23332A1B000","F00Q00","E00S00","D00A2Q2A00","C00W00","B00A2U2A00","B0[0","A00[00","A0]0","A0]0","001[100","01]10","002[200","003[300","013[310","003[300","002[200","01]10","001[100","A0]0","A0]0","A00[00","B0[0","B00A2U2A00","C00W00","D00A2Q2A00","E00S00","F00Q00","G000B1A23332A1B000","I000010010010000","L000000000",},
            {"O0A0","M1101011","L1B222B1",E,"F0S0","G2Q2","D0W0","E2U2","\u0003","B1[1","A1]1","A1]1","002[200","A12[21","002[200","A1]1","A1]1","B1[1","\u0003","E2U2","D0W0","G2Q2","F0S0",E,"L1B222B1","M1101011","O0A0",},
            {"O0A0","N11111","L11E11","\u0001","G2Q2",E,"E2U2","\u0003","B1[1","B1[1","A1]1","01]10","A1]1","01]10","A1]1","B1[1","B1[1","\u0003","E2U2",E,"G2Q2","\u0001","L11E11","N11111","O0A0",},
            {"O0A0","O0A0","M1111111","\u0009","B1[1","B1[1","001[100","B1[1","001[100","B1[1","B1[1","\u0009","M1111111","O0A0","O0A0",},
            {E,"O0A0","P1","\u000B","A0]0","B1[1","A0]0","\u000B","P1","O0A0",},
            {E,"O0A0","P1","\u000B","A0]0","B1[1","A0]0","\u000B","P1","O0A0",},
            {E,"O0A0","O0A0","P1","P4","\u0009","A00[00","C14W41","A00[00","\u0009","P4","P1","O0A0","O0A0",},
            {"\u0001","O0A0","P1","P4","\u0009","B0[0","C14W41","B0[0","\u0009","P4","P1","O0A0",},
            {"\u0001","O0A0","O0A0","P1","M2224222","\u0006","E2U2","E2U2","B00A2U2A00","D14U41","B00A2U2A00","E2U2","E2U2","\u0006","M2224222","P1","O0A0","O0A0",},
            {"\u0002","O0A0","N00000","P1","P4","\u0006","D0W0","C00W00","D014S410","C00W00","D0W0","\u0006","P4","P1","N00000","O0A0",},
            {"\u0003","O0A0","O0A0","P1","M2224222","\u0004","G2Q2","G2Q2","D00A2Q2A00","F14Q41","D00A2Q2A00","G2Q2","G2Q2","\u0004","M2224222","P1","O0A0","O0A0",},
            {"\u0004","O0A0","N00000","P1","P4","P4","\u0003","F0S0","E00S00","F0144M4410","E00S00","F0S0","\u0003","P4","P4","P1","N00000","O0A0",},
            {"\u0005","O0A0","O0A0","P1","P1","\u0004","F00Q00","H11M11","F00Q00","\u0004","P1","P1","O0A0","O0A0",},
            {"\u0006","O0A0","O0A0","O0A0","P1","P1","M1111111","L11E11","L1B222B1","G000B1A23332A1B000","J111A23332A111","G000B1A23332A1B000","L1B222B1","L11E11","M1111111","P1","P1","O0A0","O0A0","O0A0",},
            {"\u0008","O0A0","O0A0","O0A0","O0A0","N11111","M1101011","I000010010010000","M1111111","I000010010010000","M1101011","N11111","O0A0","O0A0","O0A0","O0A0",},
            {"\u000B","O0A0","O0A0","O0A0","L000000000","O0A0","L000000000","O0A0","O0A0","O0A0",},
    };
    private static final Block[] blockType = new Block[]{sBlockCasingsTT,sBlockCasingsTT,sBlockCasingsTT,sBlockCasingsTT,sBlockCasingsTT};
    private static final byte[] blockMeta = new byte[]{12, 13, 14, 10, 11};
    private static final String[] addingMethods = new String[]{"addClassicToMachineList", "addElementalToMachineList"};
    private static final short[] casingTextures = new short[]{textureOffset, textureOffset + 4};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMetaFallback = new byte[]{0, 4};
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA+"Hint Details:",
            "1 - Classic Hatches or High Power Casing",
            "2 - Elemental Hatches or Molecular Casing",
    };
    //endregion

    //region Structure dank - glass sphere for the looks
    private static final String[][] shape2 = new String[][]{
            {"\u000B","M0000000","L00     00","L0       0","L0  !!!  0","L0  !.!  0","L0  !!!  0","L0       0","L00     00","M0000000",},
            {"\u0008","O0A0","M110A011","L1110A0111","K11110A01111","J1111222221111","J1112202022111","I000020020020000","M2222222","I000020020020000","J1112202022111","J1111222221111","K11110A01111","L1110A0111","M110A011","O0A0",},
            {"\u0006","O0A0","M110A011","K11110A01111","J111C2C111","I111D2D111","I11B2222222B11","H11B22E22B11","H11B2B333B2B11","G000B2A34443A2B000","J222A34443A222","G000B2A34443A2B000","H11B2B333B2B11","H11B22E22B11","I11B2222222B11","I111D2D111","J111C2C111","K11110A01111","M110A011","O0A0",},
            {"\u0005","O0A0","L1110A0111","J111C2C111","I11E2E11","H11M11","H1O1","G11O11","G1Q1","G1Q1","F00Q00","H22M22","F00Q00","G1Q1","G1Q1","G11O11","H1O1","H11M11","I11E2E11","J111C2C111","L1110A0111","O0A0",},
            {"\u0004","O0A0","K11100000111","I111D2D111","H11F5F11","G11G5G11","G1Q1","F11Q11","F1S1","F1S1","F0S0","E00S00","F0255M5520","E00S00","F0S0","F1S1","F1S1","F11Q11","G1Q1","G11G5G11","H11F5F11","I111D2D111","K11100000111","O0A0",},
            {"\u0003","O0A0","J111110A011111","H111E2E111","G11D3335333D11","F11Q11","F1S1","E11S11","E1U1","E1U1","E1A3Q3A1","E1A3Q3A1","D00A3Q3A00","F25Q52","D00A3Q3A00","E1A3Q3A1","E1A3Q3A1","E1U1","E1U1","E11S11","F1S1","F11Q11","G11D3335333D11","H111E2E111","J111110A011111","O0A0",},
            {"\u0002","O0A0","K11100000111","H111E2E111","G11G5G11","F1S1","E11S11","E1U1","E1U1","D1W1","D1W1","D1W1","D0W0","C00W00","D025S520","C00W00","D0W0","D1W1","D1W1","D1W1","E1U1","E1U1","E11S11","F1S1","G11G5G11","H111E2E111","K11100000111","O0A0",},
            {"\u0001","O0A0","L1110A0111","I111D2D111","G11D3335333D11","F1S1","E1U1","E1U1","D1W1","D1W1","D1W1","C1Y1","C1A3U3A1","C1A3U3A1","B00A3U3A00","D25U52","B00A3U3A00","C1A3U3A1","C1A3U3A1","C1Y1","D1W1","D1W1","D1W1","E1U1","E1U1","F1S1","G11D3335333D11","I111D2D111","L1110A0111","O0A0",},
            {"\u0001","M110A011","J111C2C111","H11F5F11","F11Q11","E11S11","E1U1","D1W1","D1W1","C1Y1","C1Y1","C1Y1","B1[1","B1[1","B0[0","C25W52","B0[0","B1[1","B1[1","C1Y1","C1Y1","C1Y1","D1W1","D1W1","E1U1","E11S11","F11Q11","H11F5F11","J111C2C111","M110A011",},
            {E,"O0A0","K11110A01111","I11E2E11","G11G5G11","F1S1","E1U1","D1W1","D1W1","C1Y1","C1Y1","B1[1","B1[1","B1[1","B1[1","A00[00","C25W52","A00[00","B1[1","B1[1","B1[1","B1[1","C1Y1","C1Y1","D1W1","D1W1","E1U1","F1S1","G11G5G11","I11E2E11","K11110A01111","O0A0",},
            {E,"M110A011","J111C2C111","H11M11","G1Q1","E11S11","E1U1","D1W1","C1Y1","C1Y1","B1[1","B1[1","B1[1","A1]1","A1]1","A0]0","B2[2","A0]0","A1]1","A1]1","B1[1","B1[1","B1[1","C1Y1","C1Y1","D1W1","E1U1","E11S11","G1Q1","H11M11","J111C2C111","M110A011",},
            {E,"L1110A0111","I111D2D111","H1O1","F11Q11","E1U1","D1W1","D1W1","C1Y1","B1[1","B1[1","B1[1","A1]1","A1]1","A1]1","A0]0","B2[2","A0]0","A1]1","A1]1","A1]1","B1[1","B1[1","B1[1","C1Y1","D1W1","D1W1","E1U1","F11Q11","H1O1","I111D2D111","L1110A0111",},
            {"O0A0","K11110A01111","I11B2222222B11","G11O11","F1S1","E1U1","D1W1","C1Y1","C1Y1","B1[1","B1[1","A1]1","A1]1","A12[21","A12[21","002[200","B2[2","002[200","A12[21","A12[21","A1]1","A1]1","B1[1","B1[1","C1Y1","C1Y1","D1W1","E1U1","F1S1","G11O11","I11B2222222B11","K11110A01111","O0A0",},
            {"O0A0","J1111222221111","H11B22E22B11","G1Q1","F1S1","E1A3Q3A1","D1W1","C1A3U3A1","B1[1","B1[1","A1]1","A1]1","A12[21","A12[21","A2]2","02]20","A2]2","02]20","A2]2","A12[21","A12[21","A1]1","A1]1","B1[1","B1[1","C1A3U3A1","D1W1","E1A3Q3A1","F1S1","G1Q1","H11B22E22B11","J1111222221111","O0A0",},
            {"O0A0","J1112202022111","H11B2B333B2B11","G1Q1","F0S0","E1A3Q3A1","D0W0","C1A3U3A1","B1[1","B1[1","A1]1","A1]1","A12[21","A2]2","A2]2","003[300","A23[32","003[300","A2]2","A2]2","A12[21","A1]1","A1]1","B1[1","B1[1","C1A3U3A1","D0W0","E1A3Q3A1","F0S0","G1Q1","H11B2B333B2B11","J1112202022111","O0A0",},
            {"L000000000","I000020020020000","G000B2A34443A2B000","F00Q00","E00S00","D00A3Q3A00","C00W00","B00A3U3A00","B0[0","A00[00","A0]0","A0]0","002[200","02]20","003[300","004[400","024[420","004[400","003[300","02]20","002[200","A0]0","A0]0","A00[00","B0[0","B00A3U3A00","C00W00","D00A3Q3A00","E00S00","F00Q00","G000B2A34443A2B000","I000020020020000","L000000000",},
            {"O0A0","M2222222","J222A34443A222","H22M22","F0255M5520","F25Q52","D025S520","D25U52","C25W52","C25W52","B2[2","B2[2","B2[2","A2]2","A23[32","024[420","A24[42","024[420","A23[32","A2]2","B2[2","B2[2","B2[2","C25W52","C25W52","D25U52","D025S520","F25Q52","F0255M5520","H22M22","J222A34443A222","M2222222","O0A0",},
            {"L000000000","I000020020020000","G000B2A34443A2B000","F00Q00","E00S00","D00A3Q3A00","C00W00","B00A3U3A00","B0[0","A00[00","A0]0","A0]0","002[200","02]20","003[300","004[400","024[420","004[400","003[300","02]20","002[200","A0]0","A0]0","A00[00","B0[0","B00A3U3A00","C00W00","D00A3Q3A00","E00S00","F00Q00","G000B2A34443A2B000","I000020020020000","L000000000",},
            {"O0A0","J1112202022111","H11B2B333B2B11","G1Q1","F0S0","E1A3Q3A1","D0W0","C1A3U3A1","B1[1","B1[1","A1]1","A1]1","A12[21","A2]2","A2]2","003[300","A23[32","003[300","A2]2","A2]2","A12[21","A1]1","A1]1","B1[1","B1[1","C1A3U3A1","D0W0","E1A3Q3A1","F0S0","G1Q1","H11B2B333B2B11","J1112202022111","O0A0",},
            {"O0A0","J1111222221111","H11B22E22B11","G1Q1","F1S1","E1A3Q3A1","D1W1","C1A3U3A1","B1[1","B1[1","A1]1","A1]1","A12[21","A12[21","A2]2","02]20","A2]2","02]20","A2]2","A12[21","A12[21","A1]1","A1]1","B1[1","B1[1","C1A3U3A1","D1W1","E1A3Q3A1","F1S1","G1Q1","H11B22E22B11","J1111222221111","O0A0",},
            {"O0A0","K11110A01111","I11B2222222B11","G11O11","F1S1","E1U1","D1W1","C1Y1","C1Y1","B1[1","B1[1","A1]1","A1]1","A12[21","A12[21","002[200","B2[2","002[200","A12[21","A12[21","A1]1","A1]1","B1[1","B1[1","C1Y1","C1Y1","D1W1","E1U1","F1S1","G11O11","I11B2222222B11","K11110A01111","O0A0",},
            {E,"L1110A0111","I111D2D111","H1O1","F11Q11","E1U1","D1W1","D1W1","C1Y1","B1[1","B1[1","B1[1","A1]1","A1]1","A1]1","A0]0","B2[2","A0]0","A1]1","A1]1","A1]1","B1[1","B1[1","B1[1","C1Y1","D1W1","D1W1","E1U1","F11Q11","H1O1","I111D2D111","L1110A0111",},
            {E,"M110A011","J111C2C111","H11M11","G1Q1","E11S11","E1U1","D1W1","C1Y1","C1Y1","B1[1","B1[1","B1[1","A1]1","A1]1","A0]0","B2[2","A0]0","A1]1","A1]1","B1[1","B1[1","B1[1","C1Y1","C1Y1","D1W1","E1U1","E11S11","G1Q1","H11M11","J111C2C111","M110A011",},
            {E,"O0A0","K11110A01111","I11E2E11","G11G5G11","F1S1","E1U1","D1W1","D1W1","C1Y1","C1Y1","B1[1","B1[1","B1[1","B1[1","A00[00","C25W52","A00[00","B1[1","B1[1","B1[1","B1[1","C1Y1","C1Y1","D1W1","D1W1","E1U1","F1S1","G11G5G11","I11E2E11","K11110A01111","O0A0",},
            {"\u0001","M110A011","J111C2C111","H11F5F11","F11Q11","E11S11","E1U1","D1W1","D1W1","C1Y1","C1Y1","C1Y1","B1[1","B1[1","B0[0","C25W52","B0[0","B1[1","B1[1","C1Y1","C1Y1","C1Y1","D1W1","D1W1","E1U1","E11S11","F11Q11","H11F5F11","J111C2C111","M110A011",},
            {"\u0001","O0A0","L1110A0111","I111D2D111","G11D3335333D11","F1S1","E1U1","E1U1","D1W1","D1W1","D1W1","C1Y1","C1A3U3A1","C1A3U3A1","B00A3U3A00","D25U52","B00A3U3A00","C1A3U3A1","C1A3U3A1","C1Y1","D1W1","D1W1","D1W1","E1U1","E1U1","F1S1","G11D3335333D11","I111D2D111","L1110A0111","O0A0",},
            {"\u0002","O0A0","K11100000111","H111E2E111","G11G5G11","F1S1","E11S11","E1U1","E1U1","D1W1","D1W1","D1W1","D0W0","C00W00","D025S520","C00W00","D0W0","D1W1","D1W1","D1W1","E1U1","E1U1","E11S11","F1S1","G11G5G11","H111E2E111","K11100000111","O0A0",},
            {"\u0003","O0A0","J111110A011111","H111E2E111","G11D3335333D11","F11Q11","F1S1","E11S11","E1U1","E1U1","E1A3Q3A1","E1A3Q3A1","D00A3Q3A00","F25Q52","D00A3Q3A00","E1A3Q3A1","E1A3Q3A1","E1U1","E1U1","E11S11","F1S1","F11Q11","G11D3335333D11","H111E2E111","J111110A011111","O0A0",},
            {"\u0004","O0A0","K11100000111","I111D2D111","H11F5F11","G11G5G11","G1Q1","F11Q11","F1S1","F1S1","F0S0","E00S00","F0255M5520","E00S00","F0S0","F1S1","F1S1","F11Q11","G1Q1","G11G5G11","H11F5F11","I111D2D111","K11100000111","O0A0",},
            {"\u0005","O0A0","L1110A0111","J111C2C111","I11E2E11","H11M11","H1O1","G11O11","G1Q1","G1Q1","F00Q00","H22M22","F00Q00","G1Q1","G1Q1","G11O11","H1O1","H11M11","I11E2E11","J111C2C111","L1110A0111","O0A0",},
            {"\u0006","O0A0","M110A011","K11110A01111","J111C2C111","I111D2D111","I11B2222222B11","H11B22E22B11","H11B2B333B2B11","G000B2A34443A2B000","J222A34443A222","G000B2A34443A2B000","H11B2B333B2B11","H11B22E22B11","I11B2222222B11","I111D2D111","J111C2C111","K11110A01111","M110A011","O0A0",},
            {"\u0008","O0A0","M110A011","L1110A0111","K11110A01111","J1111222221111","J1112202022111","I000020020020000","M2222222","I000020020020000","J1112202022111","J1111222221111","K11110A01111","L1110A0111","M110A011","O0A0",},
            {"\u000B","O0A0","O0A0","O0A0","L000000000","O0A0","L000000000","O0A0","O0A0","O0A0",},
    };
    private static final Block[] blockType2 = new Block[]{sBlockCasingsTT, QuantumGlassBlock.INSTANCE,sBlockCasingsTT,sBlockCasingsTT,sBlockCasingsTT,sBlockCasingsTT};
    private static final byte[] blockMeta2 = new byte[]{12, 0, 13, 14, 10, 11};
    //endregion

    public GT_MetaTileEntity_EM_bhg(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_bhg(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_bhg(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        if(structureCheck_EM(shape2, blockType2, blockMeta2, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 16, 16, 0)){
            glassDome=true;
            return true;
        }
        if(structureCheck_EM(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 16, 16, 0)){
            glassDome=false;
            return true;
        }
        //todo check tiers of hatches!!!!
        return false;
    }

    @Override
    public void construct(int stackSize, boolean hintsOnly) {
        if((stackSize &1)==1) {
            StructureBuilder(shape, blockType, blockMeta, 16, 16, 0, getBaseMetaTileEntity(), hintsOnly);
        } else {
            StructureBuilder(shape2, blockType2, blockMeta2, 16, 16, 0, getBaseMetaTileEntity(), hintsOnly);
        }
    }

    @Override
    public String[] getStructureDescription(int stackSize) {
        return description;
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
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][12], new GT_RenderedTexture(aActive ? ScreenON : ScreenOFF)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][12]};
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                "Singularity based power generation.",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Super unstable!!!"
        };
    }
}
