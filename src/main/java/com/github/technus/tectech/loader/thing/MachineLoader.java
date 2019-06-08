package com.github.technus.tectech.loader.thing;

import com.github.technus.tectech.Reference;
import com.github.technus.tectech.compatibility.dreamcraft.NoDreamCraftMachineLoader;
import com.github.technus.tectech.compatibility.thaumcraft.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_essentiaDequantizer;
import com.github.technus.tectech.compatibility.thaumcraft.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_essentiaQuantizer;
import com.github.technus.tectech.thing.metaTileEntity.hatch.*;
import com.github.technus.tectech.thing.metaTileEntity.multi.*;
import com.github.technus.tectech.thing.metaTileEntity.multi.em_machine.GT_MetaTileEntity_EM_machine;
import com.github.technus.tectech.thing.metaTileEntity.pipe.GT_MetaTileEntity_Pipe_Data;
import com.github.technus.tectech.thing.metaTileEntity.pipe.GT_MetaTileEntity_Pipe_EM;
import com.github.technus.tectech.thing.metaTileEntity.pipe.GT_MetaTileEntity_Pipe_Energy;
import com.github.technus.tectech.thing.metaTileEntity.single.*;
import cpw.mods.fml.common.Loader;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import static com.github.technus.tectech.CommonValues.V;
import static com.github.technus.tectech.thing.CustomItemList.*;
import static com.github.technus.tectech.thing.CustomItemList.eM_dynamotunnel9001;

/**
 * Created by danie_000 on 16.11.2016.
 */
public class MachineLoader implements Runnable {
    @Override
    public void run() {
        // ===================================================================================================
        // eM IN
        // ===================================================================================================

        eM_in_UV.set(new GT_MetaTileEntity_Hatch_InputElemental(
                15000, "hatch.emin.tier.08", "UV Elemental Input Hatch", 8).getStackForm(1L));

        eM_in_UHV.set(new GT_MetaTileEntity_Hatch_InputElemental(
                15001, "hatch.emin.tier.09", "UHV Elemental Input Hatch", 9).getStackForm(1L));

        eM_in_UEV.set(new GT_MetaTileEntity_Hatch_InputElemental(
                15002, "hatch.emin.tier.10", "UEV Elemental Input Hatch", 10).getStackForm(1L));

        eM_in_UIV.set(new GT_MetaTileEntity_Hatch_InputElemental(
                15003, "hatch.emin.tier.11", "UIV Elemental Input Hatch", 11).getStackForm(1L));

        eM_in_UMV.set(new GT_MetaTileEntity_Hatch_InputElemental(
                15004, "hatch.emin.tier.12", "UMV Elemental Input Hatch", 12).getStackForm(1L));

        eM_in_UXV.set(new GT_MetaTileEntity_Hatch_InputElemental(
                15005, "hatch.emin.tier.13", "UXV Elemental Input Hatch", 13).getStackForm(1L));

        // ===================================================================================================
        // eM OUT
        // ===================================================================================================

        eM_out_UV.set(new GT_MetaTileEntity_Hatch_OutputElemental(
                15010, "hatch.emout.tier.08", "UV Elemental Output Hatch", 8).getStackForm(1L));

        eM_out_UHV.set(new GT_MetaTileEntity_Hatch_OutputElemental(
                15011, "hatch.emout.tier.09", "UHV Elemental Output Hatch", 9).getStackForm(1L));

        eM_out_UEV.set(new GT_MetaTileEntity_Hatch_OutputElemental(
                15012, "hatch.emout.tier.10", "UEV Elemental Output Hatch", 10).getStackForm(1L));

        eM_out_UIV.set(new GT_MetaTileEntity_Hatch_OutputElemental(
                15013, "hatch.emout.tier.11", "UIV Elemental Output Hatch", 11).getStackForm(1L));

        eM_out_UMV.set(new GT_MetaTileEntity_Hatch_OutputElemental(
                15014, "hatch.emout.tier.12", "UMV Elemental Output Hatch", 12).getStackForm(1L));

        eM_out_UXV.set(new GT_MetaTileEntity_Hatch_OutputElemental(
                15015, "hatch.emout.tier.13", "UXV Elemental Output Hatch", 13).getStackForm(1L));

        // ===================================================================================================
        // eM Waste OUT
        // ===================================================================================================

        eM_muffler_UV.set(new GT_MetaTileEntity_Hatch_OverflowElemental(
                15020, "hatch.emmuffler.tier.08", "UV Overflow Output Hatch", 8, 1e10f).getStackForm(1L));

        eM_muffler_UHV.set(new GT_MetaTileEntity_Hatch_OverflowElemental(
                15021, "hatch.emmuffler.tier.09", "UHV Overflow Output Hatch", 9, 5e10f).getStackForm(1L));

        eM_muffler_UEV.set(new GT_MetaTileEntity_Hatch_OverflowElemental(
                15022, "hatch.emmuffler.tier.10", "UEV Overflow Output Hatch", 10, 25e10f).getStackForm(1L));

        eM_muffler_UIV.set(new GT_MetaTileEntity_Hatch_OverflowElemental(
                15023, "hatch.emmuffler.tier.11", "UIV Overflow Output Hatch", 11, 125e10f).getStackForm(1L));

        eM_muffler_UMV.set(new GT_MetaTileEntity_Hatch_OverflowElemental(
                15024, "hatch.emmuffler.tier.12", "UMV Overflow Output Hatch", 12, 125e11f).getStackForm(1L));

        eM_muffler_UXV.set(new GT_MetaTileEntity_Hatch_OverflowElemental(
                15025, "hatch.emmuffler.tier.13", "UXV Overflow Output Hatch", 13, 125e12f).getStackForm(1L));

        // ===================================================================================================
        // Multi AMP Power INPUTS
        // ===================================================================================================

        eM_energymulti4_IV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                15100, "hatch.energymulti04.tier.05", "IV 4A Energy Hatch", 5, 4).getStackForm(1L));
        eM_energymulti16_IV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                15110, "hatch.energymulti16.tier.05", "IV 16A Energy Hatch", 5, 16).getStackForm(1L));
        eM_energymulti64_IV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                15120, "hatch.energymulti64.tier.05", "IV 64A Energy Hatch", 5, 64).getStackForm(1L));

        eM_energymulti4_LuV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                15101, "hatch.energymulti04.tier.06", "LuV 4A Energy Hatch", 6, 4).getStackForm(1L));
        eM_energymulti16_LuV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                15111, "hatch.energymulti16.tier.06", "LuV 16A Energy Hatch", 6, 16).getStackForm(1L));
        eM_energymulti64_LuV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                15121, "hatch.energymulti64.tier.06", "LuV 64A Energy Hatch", 6, 64).getStackForm(1L));

        eM_energymulti4_ZPM.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                15102, "hatch.energymulti04.tier.07", "ZPM 4A Energy Hatch", 7, 4).getStackForm(1L));
        eM_energymulti16_ZPM.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                15112, "hatch.energymulti16.tier.07", "ZPM 16A Energy Hatch", 7, 16).getStackForm(1L));
        eM_energymulti64_ZPM.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                15122, "hatch.energymulti64.tier.07", "ZPM 64A Energy Hatch", 7, 64).getStackForm(1L));

        eM_energymulti4_UV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                15103, "hatch.energymulti04.tier.08", "UV 4A Energy Hatch", 8, 4).getStackForm(1L));
        eM_energymulti16_UV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                15113, "hatch.energymulti16.tier.08", "UV 16A Energy Hatch", 8, 16).getStackForm(1L));
        eM_energymulti64_UV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                15123, "hatch.energymulti64.tier.08", "UV 64A Energy Hatch", 8, 64).getStackForm(1L));

        eM_energymulti4_UHV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                15104, "hatch.energymulti04.tier.09", "UHV 4A Energy Hatch", 9, 4).getStackForm(1L));
        eM_energymulti16_UHV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                15114, "hatch.energymulti16.tier.09", "UHV 16A Energy Hatch", 9, 16).getStackForm(1L));
        eM_energymulti64_UHV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                15124, "hatch.energymulti64.tier.09", "UHV 64A Energy Hatch", 9, 64).getStackForm(1L));

        eM_energymulti4_UEV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                15105, "hatch.energymulti04.tier.10", "UEV 4A Energy Hatch", 10, 4).getStackForm(1L));
        eM_energymulti16_UEV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                15115, "hatch.energymulti16.tier.10", "UEV 16A Energy Hatch", 10, 16).getStackForm(1L));
        eM_energymulti64_UEV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                15125, "hatch.energymulti64.tier.10", "UEV 64A Energy Hatch", 10, 64).getStackForm(1L));

        eM_energymulti4_UIV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                15106, "hatch.energymulti04.tier.11", "UIV 4A Energy Hatch", 11, 4).getStackForm(1L));
        eM_energymulti16_UIV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                15116, "hatch.energymulti16.tier.11", "UIV 16A Energy Hatch", 11, 16).getStackForm(1L));
        eM_energymulti64_UIV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                15126, "hatch.energymulti64.tier.11", "UIV 64A Energy Hatch", 11, 64).getStackForm(1L));

        eM_energymulti4_UMV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                15107, "hatch.energymulti04.tier.12", "UMV 4A Energy Hatch", 12, 4).getStackForm(1L));
        eM_energymulti16_UMV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                15117, "hatch.energymulti16.tier.12", "UMV 16A Energy Hatch", 12, 16).getStackForm(1L));
        eM_energymulti64_UMV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                15127, "hatch.energymulti64.tier.12", "UMV 64A Energy Hatch", 12, 64).getStackForm(1L));

        eM_energymulti4_UXV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                15108, "hatch.energymulti04.tier.13", "UXV 4A Energy Hatch", 13, 4).getStackForm(1L));
        eM_energymulti16_UXV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                15118, "hatch.energymulti16.tier.13", "UXV 16A Energy Hatch", 13, 16).getStackForm(1L));
        eM_energymulti64_UXV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                15128, "hatch.energymulti64.tier.13", "UXV 64A Energy Hatch", 13, 64).getStackForm(1L));


        // ===================================================================================================
        // Multi AMP Laser INPUTS
        // ===================================================================================================

        eM_energytunnel1_IV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15130, "hatch.energytunnel1.tier.05", "IV 256/t Laser Target Hatch", 5, 256).getStackForm(1L));
        eM_energytunnel2_IV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15140, "hatch.energytunnel2.tier.05", "IV 1024/t Laser Target Hatch", 5, 1024).getStackForm(1L));
        eM_energytunnel3_IV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15150, "hatch.energytunnel3.tier.05", "IV 4096/t Laser Target Hatch", 5, 4096).getStackForm(1L));
        eM_energytunnel4_IV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15160, "hatch.energytunnel4.tier.05", "IV 16384/t Laser Target Hatch", 5, 16384).getStackForm(1L));
        eM_energytunnel5_IV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15170, "hatch.energytunnel5.tier.05", "IV 65536/t Laser Target Hatch", 5, 65536).getStackForm(1L));
        eM_energytunnel6_IV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15180, "hatch.energytunnel6.tier.05", "IV 262144/t Laser Target Hatch", 5, 262144).getStackForm(1L));
        eM_energytunnel7_IV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15190, "hatch.energytunnel7.tier.05", "IV 1048576/t Laser Target Hatch", 5, 1048576).getStackForm(1L));

        eM_energytunnel1_LuV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15131, "hatch.energytunnel1.tier.06", "LuV 256/t Laser Target Hatch", 6, 256).getStackForm(1L));
        eM_energytunnel2_LuV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15141, "hatch.energytunnel2.tier.06", "LuV 1024/t Laser Target Hatch", 6, 1024).getStackForm(1L));
        eM_energytunnel3_LuV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15151, "hatch.energytunnel3.tier.06", "LuV 4096/t Laser Target Hatch", 6, 4096).getStackForm(1L));
        eM_energytunnel4_LuV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15161, "hatch.energytunnel4.tier.06", "LuV 16384/t Laser Target Hatch", 6, 16384).getStackForm(1L));
        eM_energytunnel5_LuV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15171, "hatch.energytunnel5.tier.06", "LuV 65536/t Laser Target Hatch", 6, 65536).getStackForm(1L));
        eM_energytunnel6_LuV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15181, "hatch.energytunnel6.tier.06", "LuV 262144/t Laser Target Hatch", 6, 262144).getStackForm(1L));
        eM_energytunnel7_LuV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15191, "hatch.energytunnel7.tier.06", "LuV 1048576/t Laser Target Hatch", 6, 1048576).getStackForm(1L));

        eM_energytunnel1_ZPM.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15132, "hatch.energytunnel1.tier.07", "ZPM 256/t Laser Target Hatch", 7, 256).getStackForm(1L));
        eM_energytunnel2_ZPM.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15142, "hatch.energytunnel2.tier.07", "ZPM 1024/t Laser Target Hatch", 7, 1024).getStackForm(1L));
        eM_energytunnel3_ZPM.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15152, "hatch.energytunnel3.tier.07", "ZPM 4096/t Laser Target Hatch", 7, 4096).getStackForm(1L));
        eM_energytunnel4_ZPM.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15162, "hatch.energytunnel4.tier.07", "ZPM 16384/t Laser Target Hatch", 7, 16384).getStackForm(1L));
        eM_energytunnel5_ZPM.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15172, "hatch.energytunnel5.tier.07", "ZPM 65536/t Laser Target Hatch", 7, 65536).getStackForm(1L));
        eM_energytunnel6_ZPM.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15182, "hatch.energytunnel6.tier.07", "ZPM 262144/t Laser Target Hatch", 7, 262144).getStackForm(1L));
        eM_energytunnel7_ZPM.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15192, "hatch.energytunnel7.tier.07", "ZPM 1048576/t Laser Target Hatch", 7, 1048576).getStackForm(1L));

        eM_energytunnel1_UV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15133, "hatch.energytunnel1.tier.08", "UV 256/t Laser Target Hatch", 8, 256).getStackForm(1L));
        eM_energytunnel2_UV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15143, "hatch.energytunnel2.tier.08", "UV 1024/t Laser Target Hatch", 8, 1024).getStackForm(1L));
        eM_energytunnel3_UV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15153, "hatch.energytunnel3.tier.08", "UV 4096/t Laser Target Hatch", 8, 4096).getStackForm(1L));
        eM_energytunnel4_UV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15163, "hatch.energytunnel4.tier.08", "UV 16384/t Laser Target Hatch", 8, 16384).getStackForm(1L));
        eM_energytunnel5_UV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15173, "hatch.energytunnel5.tier.08", "UV 65536/t Laser Target Hatch", 8, 65536).getStackForm(1L));
        eM_energytunnel6_UV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15183, "hatch.energytunnel6.tier.08", "UV 262144/t Laser Target Hatch", 8, 262144).getStackForm(1L));
        eM_energytunnel7_UV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15193, "hatch.energytunnel7.tier.08", "UV 1048576/t Laser Target Hatch", 8, 1048576).getStackForm(1L));

        eM_energytunnel1_UHV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15134, "hatch.energytunnel1.tier.09", "UHV 256/t Laser Target Hatch", 9, 256).getStackForm(1L));
        eM_energytunnel2_UHV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15144, "hatch.energytunnel2.tier.09", "UHV 1024/t Laser Target Hatch", 9, 1024).getStackForm(1L));
        eM_energytunnel3_UHV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15154, "hatch.energytunnel3.tier.09", "UHV 4096/t Laser Target Hatch", 9, 4096).getStackForm(1L));
        eM_energytunnel4_UHV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15164, "hatch.energytunnel4.tier.09", "UHV 16384/t Laser Target Hatch", 9, 16384).getStackForm(1L));
        eM_energytunnel5_UHV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15174, "hatch.energytunnel5.tier.09", "UHV 65536/t Laser Target Hatch", 9, 65536).getStackForm(1L));
        eM_energytunnel6_UHV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15184, "hatch.energytunnel6.tier.09", "UHV 262144/t Laser Target Hatch", 9, 262144).getStackForm(1L));
        eM_energytunnel7_UHV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15194, "hatch.energytunnel7.tier.09", "UHV 1048576/t Laser Target Hatch", 9, 1048576).getStackForm(1L));

        eM_energytunnel1_UEV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15135, "hatch.energytunnel1.tier.10", "UEV 256/t Laser Target Hatch", 10, 256).getStackForm(1L));
        eM_energytunnel2_UEV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15145, "hatch.energytunnel2.tier.10", "UEV 1024/t Laser Target Hatch", 10, 1024).getStackForm(1L));
        eM_energytunnel3_UEV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15155, "hatch.energytunnel3.tier.10", "UEV 4096/t Laser Target Hatch", 10, 4096).getStackForm(1L));
        eM_energytunnel4_UEV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15165, "hatch.energytunnel4.tier.10", "UEV 16384/t Laser Target Hatch", 10, 16384).getStackForm(1L));
        eM_energytunnel5_UEV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15175, "hatch.energytunnel5.tier.10", "UEV 65536/t Laser Target Hatch", 10, 65536).getStackForm(1L));
        eM_energytunnel6_UEV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15185, "hatch.energytunnel6.tier.10", "UEV 262144/t Laser Target Hatch", 10, 262144).getStackForm(1L));
        eM_energytunnel7_UEV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15195, "hatch.energytunnel7.tier.10", "UEV 1048576/t Laser Target Hatch", 10, 1048576).getStackForm(1L));

        eM_energytunnel1_UIV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15136, "hatch.energytunnel1.tier.11", "UIV 256/t Laser Target Hatch", 11, 256).getStackForm(1L));
        eM_energytunnel2_UIV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15146, "hatch.energytunnel2.tier.11", "UIV 1024/t Laser Target Hatch", 11, 1024).getStackForm(1L));
        eM_energytunnel3_UIV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15156, "hatch.energytunnel3.tier.11", "UIV 4096/t Laser Target Hatch", 11, 4096).getStackForm(1L));
        eM_energytunnel4_UIV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15166, "hatch.energytunnel4.tier.11", "UIV 16384/t Laser Target Hatch", 11, 16384).getStackForm(1L));
        eM_energytunnel5_UIV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15176, "hatch.energytunnel5.tier.11", "UIV 65536/t Laser Target Hatch", 11, 65536).getStackForm(1L));
        eM_energytunnel6_UIV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15186, "hatch.energytunnel6.tier.11", "UIV 262144/t Laser Target Hatch", 11, 262144).getStackForm(1L));
        eM_energytunnel7_UIV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15196, "hatch.energytunnel7.tier.11", "UIV 1048576/t Laser Target Hatch", 11, 1048576).getStackForm(1L));

        eM_energytunnel1_UMV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15137, "hatch.energytunnel1.tier.12", "UMV 256/t Laser Target Hatch", 12, 256).getStackForm(1L));
        eM_energytunnel2_UMV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15147, "hatch.energytunnel2.tier.12", "UMV 1024/t Laser Target Hatch", 12, 1024).getStackForm(1L));
        eM_energytunnel3_UMV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15157, "hatch.energytunnel3.tier.12", "UMV 4096/t Laser Target Hatch", 12, 4096).getStackForm(1L));
        eM_energytunnel4_UMV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15167, "hatch.energytunnel4.tier.12", "UMV 16384/t Laser Target Hatch", 12, 16384).getStackForm(1L));
        eM_energytunnel5_UMV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15177, "hatch.energytunnel5.tier.12", "UMV 65536/t Laser Target Hatch", 12, 65536).getStackForm(1L));
        eM_energytunnel6_UMV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15187, "hatch.energytunnel6.tier.12", "UMV 262144/t Laser Target Hatch", 12, 262144).getStackForm(1L));
        eM_energytunnel7_UMV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15197, "hatch.energytunnel7.tier.12", "UMV 1048576/t Laser Target Hatch", 12, 1048576).getStackForm(1L));

        eM_energytunnel1_UXV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15138, "hatch.energytunnel1.tier.13", "UXV 256/t Laser Target Hatch", 13, 256).getStackForm(1L));
        eM_energytunnel2_UXV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15148, "hatch.energytunnel2.tier.13", "UXV 1024/t Laser Target Hatch", 13, 1024).getStackForm(1L));
        eM_energytunnel3_UXV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15158, "hatch.energytunnel3.tier.13", "UXV 4096/t Laser Target Hatch", 13, 4096).getStackForm(1L));
        eM_energytunnel4_UXV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15168, "hatch.energytunnel4.tier.13", "UXV 16384/t Laser Target Hatch", 13, 16384).getStackForm(1L));
        eM_energytunnel5_UXV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15178, "hatch.energytunnel5.tier.13", "UXV 65536/t Laser Target Hatch", 13, 65536).getStackForm(1L));
        eM_energytunnel6_UXV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15188, "hatch.energytunnel6.tier.13", "UXV 262144/t Laser Target Hatch", 13, 262144).getStackForm(1L));
        eM_energytunnel7_UXV.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15198, "hatch.energytunnel7.tier.13", "UXV 1048576/t Laser Target Hatch", 13, 1048576).getStackForm(1L));
        eM_energytunnel9001.set(new GT_MetaTileEntity_Hatch_EnergyTunnel(
                15199, "hatch.energytunnel.tier.14", "Legendary Laser Target Hatch", 14, (int)V[14]).getStackForm(1L));

        // ===================================================================================================
        // Multi AMP Power OUTPUTS
        // ===================================================================================================


        eM_dynamomulti4_IV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                15200, "hatch.dynamomulti04.tier.05", "IV 4A Dynamo Hatch", 5, 4).getStackForm(1L));
        eM_dynamomulti16_IV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                15210, "hatch.dynamomulti16.tier.05", "IV 16A Dynamo Hatch", 5, 16).getStackForm(1L));
        eM_dynamomulti64_IV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                15220, "hatch.dynamomulti64.tier.05", "IV 64A Dynamo Hatch", 5, 64).getStackForm(1L));

        eM_dynamomulti4_LuV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                15201, "hatch.dynamomulti04.tier.06", "LuV 4A Dynamo Hatch", 6, 4).getStackForm(1L));
        eM_dynamomulti16_LuV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                15211, "hatch.dynamomulti16.tier.06", "LuV 16A Dynamo Hatch", 6, 16).getStackForm(1L));
        eM_dynamomulti64_LuV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                15221, "hatch.dynamomulti64.tier.06", "LuV 64A Dynamo Hatch", 6, 64).getStackForm(1L));

        eM_dynamomulti4_ZPM.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                15202, "hatch.dynamomulti04.tier.07", "ZPM 4A Dynamo Hatch", 7, 4).getStackForm(1L));
        eM_dynamomulti16_ZPM.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                15212, "hatch.dynamomulti16.tier.07", "ZPM 16A Dynamo Hatch", 7, 16).getStackForm(1L));
        eM_dynamomulti64_ZPM.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                15222, "hatch.dynamomulti64.tier.07", "ZPM 64A Dynamo Hatch", 7, 64).getStackForm(1L));

        eM_dynamomulti4_UV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                15203, "hatch.dynamomulti04.tier.08", "UV 4A Dynamo Hatch", 8, 4).getStackForm(1L));
        eM_dynamomulti16_UV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                15213, "hatch.dynamomulti16.tier.08", "UV 16A Dynamo Hatch", 8, 16).getStackForm(1L));
        eM_dynamomulti64_UV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                15223, "hatch.dynamomulti64.tier.08", "UV 64A Dynamo Hatch", 8, 64).getStackForm(1L));

        eM_dynamomulti4_UHV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                15204, "hatch.dynamomulti04.tier.09", "UHV 4A Dynamo Hatch", 9, 4).getStackForm(1L));
        eM_dynamomulti16_UHV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                15214, "hatch.dynamomulti16.tier.09", "UHV 16A Dynamo Hatch", 9, 16).getStackForm(1L));
        eM_dynamomulti64_UHV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                15224, "hatch.dynamomulti64.tier.09", "UHV 64A Dynamo Hatch", 9, 64).getStackForm(1L));

        eM_dynamomulti4_UEV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                15205, "hatch.dynamomulti04.tier.10", "UEV 4A Dynamo Hatch", 10, 4).getStackForm(1L));
        eM_dynamomulti16_UEV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                15215, "hatch.dynamomulti16.tier.10", "UEV 16A Dynamo Hatch", 10, 16).getStackForm(1L));
        eM_dynamomulti64_UEV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                15225, "hatch.dynamomulti64.tier.10", "UEV 64A Dynamo Hatch", 10, 64).getStackForm(1L));

        eM_dynamomulti4_UIV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                15206, "hatch.dynamomulti04.tier.11", "UIV 4A Dynamo Hatch", 11, 4).getStackForm(1L));
        eM_dynamomulti16_UIV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                15216, "hatch.dynamomulti16.tier.11", "UIV 16A Dynamo Hatch", 11, 16).getStackForm(1L));
        eM_dynamomulti64_UIV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                15226, "hatch.dynamomulti64.tier.11", "UIV 64A Dynamo Hatch", 11, 64).getStackForm(1L));

        eM_dynamomulti4_UMV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                15207, "hatch.dynamomulti04.tier.12", "UMV 4A Dynamo Hatch", 12, 4).getStackForm(1L));
        eM_dynamomulti16_UMV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                15217, "hatch.dynamomulti16.tier.12", "UMV 16A Dynamo Hatch", 12, 16).getStackForm(1L));
        eM_dynamomulti64_UMV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                15227, "hatch.dynamomulti64.tier.12", "UMV 64A Dynamo Hatch", 12, 64).getStackForm(1L));

        eM_dynamomulti4_UXV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                15208, "hatch.dynamomulti04.tier.13", "UXV 4A Dynamo Hatch", 13, 4).getStackForm(1L));
        eM_dynamomulti16_UXV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                15218, "hatch.dynamomulti16.tier.13", "UXV 16A Dynamo Hatch", 13, 16).getStackForm(1L));
        eM_dynamomulti64_UXV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                15228, "hatch.dynamomulti64.tier.13", "UXV 64A Dynamo Hatch", 13, 64).getStackForm(1L));

        // ===================================================================================================
        // Multi AMP Laser OUTPUTS
        // ===================================================================================================

        eM_dynamotunnel1_IV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15230, "hatch.dynamotunnel1.tier.05", "IV 256/t Laser Source Hatch", 5, 256).getStackForm(1L));
        eM_dynamotunnel2_IV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15240, "hatch.dynamotunnel2.tier.05", "IV 1024/t Laser Source Hatch", 5, 1024).getStackForm(1L));
        eM_dynamotunnel3_IV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15250, "hatch.dynamotunnel3.tier.05", "IV 4096/t Laser Source Hatch", 5, 4096).getStackForm(1L));
        eM_dynamotunnel4_IV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15260, "hatch.dynamotunnel4.tier.05", "IV 16384/t Laser Source Hatch", 5, 16384).getStackForm(1L));
        eM_dynamotunnel5_IV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15270, "hatch.dynamotunnel5.tier.05", "IV 65536/t Laser Source Hatch", 5, 65536).getStackForm(1L));
        eM_dynamotunnel6_IV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15280, "hatch.dynamotunnel6.tier.05", "IV 262144/t Laser Source Hatch", 5, 262144).getStackForm(1L));
        eM_dynamotunnel7_IV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15290, "hatch.dynamotunnel7.tier.05", "IV 1048576/t Laser Source Hatch", 5, 1048576).getStackForm(1L));

        eM_dynamotunnel1_LuV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15231, "hatch.dynamotunnel1.tier.06", "LuV 256/t Laser Source Hatch", 6, 256).getStackForm(1L));
        eM_dynamotunnel2_LuV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15241, "hatch.dynamotunnel2.tier.06", "LuV 1024/t Laser Source Hatch", 6, 1024).getStackForm(1L));
        eM_dynamotunnel3_LuV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15251, "hatch.dynamotunnel3.tier.06", "LuV 4096/t Laser Source Hatch", 6, 4096).getStackForm(1L));
        eM_dynamotunnel4_LuV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15261, "hatch.dynamotunnel4.tier.06", "LuV 16384/t Laser Source Hatch", 6, 16384).getStackForm(1L));
        eM_dynamotunnel5_LuV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15271, "hatch.dynamotunnel5.tier.06", "LuV 65536/t Laser Source Hatch", 6, 65536).getStackForm(1L));
        eM_dynamotunnel6_LuV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15281, "hatch.dynamotunnel6.tier.06", "LuV 262144/t Laser Source Hatch", 6, 262144).getStackForm(1L));
        eM_dynamotunnel7_LuV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15291, "hatch.dynamotunnel7.tier.06", "LuV 1048576/t Laser Source Hatch", 6, 1048576).getStackForm(1L));

        eM_dynamotunnel1_ZPM.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15232, "hatch.dynamotunnel1.tier.07", "ZPM 256/t Laser Source Hatch", 7, 256).getStackForm(1L));
        eM_dynamotunnel2_ZPM.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15242, "hatch.dynamotunnel2.tier.07", "ZPM 1024/t Laser Source Hatch", 7, 1024).getStackForm(1L));
        eM_dynamotunnel3_ZPM.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15252, "hatch.dynamotunnel3.tier.07", "ZPM 4096/t Laser Source Hatch", 7, 4096).getStackForm(1L));
        eM_dynamotunnel4_ZPM.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15262, "hatch.dynamotunnel4.tier.07", "ZPM 16384/t Laser Source Hatch", 7, 16384).getStackForm(1L));
        eM_dynamotunnel5_ZPM.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15272, "hatch.dynamotunnel5.tier.07", "ZPM 65536/t Laser Source Hatch", 7, 65536).getStackForm(1L));
        eM_dynamotunnel6_ZPM.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15282, "hatch.dynamotunnel6.tier.07", "ZPM 262144/t Laser Source Hatch", 7, 262144).getStackForm(1L));
        eM_dynamotunnel7_ZPM.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15292, "hatch.dynamotunnel7.tier.07", "ZPM 1048576/t Laser Source Hatch", 7, 1048576).getStackForm(1L));

        eM_dynamotunnel1_UV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15233, "hatch.dynamotunnel1.tier.08", "UV 256/t Laser Source Hatch", 8, 256).getStackForm(1L));
        eM_dynamotunnel2_UV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15243, "hatch.dynamotunnel2.tier.08", "UV 1024/t Laser Source Hatch", 8, 1024).getStackForm(1L));
        eM_dynamotunnel3_UV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15253, "hatch.dynamotunnel3.tier.08", "UV 4096/t Laser Source Hatch", 8, 4096).getStackForm(1L));
        eM_dynamotunnel4_UV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15263, "hatch.dynamotunnel4.tier.08", "UV 16384/t Laser Source Hatch", 8, 16384).getStackForm(1L));
        eM_dynamotunnel5_UV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15273, "hatch.dynamotunnel5.tier.08", "UV 65536/t Laser Source Hatch", 8, 65536).getStackForm(1L));
        eM_dynamotunnel6_UV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15283, "hatch.dynamotunnel6.tier.08", "UV 262144/t Laser Source Hatch", 8, 262144).getStackForm(1L));
        eM_dynamotunnel7_UV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15293, "hatch.dynamotunnel7.tier.08", "UV 1048576/t Laser Source Hatch", 8, 1048576).getStackForm(1L));

        eM_dynamotunnel1_UHV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15234, "hatch.dynamotunnel1.tier.09", "UHV 256/t Laser Source Hatch", 9, 256).getStackForm(1L));
        eM_dynamotunnel2_UHV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15244, "hatch.dynamotunnel2.tier.09", "UHV 1024/t Laser Source Hatch", 9, 1024).getStackForm(1L));
        eM_dynamotunnel3_UHV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15254, "hatch.dynamotunnel3.tier.09", "UHV 4096/t Laser Source Hatch", 9, 4096).getStackForm(1L));
        eM_dynamotunnel4_UHV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15264, "hatch.dynamotunnel4.tier.09", "UHV 16384/t Laser Source Hatch", 9, 16384).getStackForm(1L));
        eM_dynamotunnel5_UHV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15274, "hatch.dynamotunnel5.tier.09", "UHV 65536/t Laser Source Hatch", 9, 65536).getStackForm(1L));
        eM_dynamotunnel6_UHV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15284, "hatch.dynamotunnel6.tier.09", "UHV 262144/t Laser Source Hatch", 9, 262144).getStackForm(1L));
        eM_dynamotunnel7_UHV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15294, "hatch.dynamotunnel7.tier.09", "UHV 1048576/t Laser Source Hatch", 9, 1048576).getStackForm(1L));

        eM_dynamotunnel1_UEV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15235, "hatch.dynamotunnel1.tier.10", "UEV 256/t Laser Source Hatch", 10, 256).getStackForm(1L));
        eM_dynamotunnel2_UEV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15245, "hatch.dynamotunnel2.tier.10", "UEV 1024/t Laser Source Hatch", 10, 1024).getStackForm(1L));
        eM_dynamotunnel3_UEV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15255, "hatch.dynamotunnel3.tier.10", "UEV 4096/t Laser Source Hatch", 10, 4096).getStackForm(1L));
        eM_dynamotunnel4_UEV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15265, "hatch.dynamotunnel4.tier.10", "UEV 16384/t Laser Source Hatch", 10, 16384).getStackForm(1L));
        eM_dynamotunnel5_UEV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15275, "hatch.dynamotunnel5.tier.10", "UEV 65536/t Laser Source Hatch", 10, 65536).getStackForm(1L));
        eM_dynamotunnel6_UEV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15285, "hatch.dynamotunnel6.tier.10", "UEV 262144/t Laser Source Hatch", 10, 262144).getStackForm(1L));
        eM_dynamotunnel7_UEV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15295, "hatch.dynamotunnel7.tier.10", "UEV 1048576/t Laser Source Hatch", 10, 1048576).getStackForm(1L));

        eM_dynamotunnel1_UIV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15236, "hatch.dynamotunnel1.tier.11", "UIV 256/t Laser Source Hatch", 11, 256).getStackForm(1L));
        eM_dynamotunnel2_UIV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15246, "hatch.dynamotunnel2.tier.11", "UIV 1024/t Laser Source Hatch", 11, 1024).getStackForm(1L));
        eM_dynamotunnel3_UIV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15256, "hatch.dynamotunnel3.tier.11", "UIV 4096/t Laser Source Hatch", 11, 4096).getStackForm(1L));
        eM_dynamotunnel4_UIV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15266, "hatch.dynamotunnel4.tier.11", "UIV 16384/t Laser Source Hatch", 11, 16384).getStackForm(1L));
        eM_dynamotunnel5_UIV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15276, "hatch.dynamotunnel5.tier.11", "UIV 65536/t Laser Source Hatch", 11, 65536).getStackForm(1L));
        eM_dynamotunnel6_UIV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15286, "hatch.dynamotunnel6.tier.11", "UIV 262144/t Laser Source Hatch", 11, 262144).getStackForm(1L));
        eM_dynamotunnel7_UIV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15296, "hatch.dynamotunnel7.tier.11", "UIV 1048576/t Laser Source Hatch", 11, 1048576).getStackForm(1L));

        eM_dynamotunnel1_UMV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15237, "hatch.dynamotunnel1.tier.12", "UMV 256/t Laser Source Hatch", 12, 256).getStackForm(1L));
        eM_dynamotunnel2_UMV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15247, "hatch.dynamotunnel2.tier.12", "UMV 1024/t Laser Source Hatch", 12, 1024).getStackForm(1L));
        eM_dynamotunnel3_UMV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15257, "hatch.dynamotunnel3.tier.12", "UMV 4096/t Laser Source Hatch", 12, 4096).getStackForm(1L));
        eM_dynamotunnel4_UMV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15267, "hatch.dynamotunnel4.tier.12", "UMV 16384/t Laser Source Hatch", 12, 16384).getStackForm(1L));
        eM_dynamotunnel5_UMV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15277, "hatch.dynamotunnel5.tier.12", "UMV 65536/t Laser Source Hatch", 12, 65536).getStackForm(1L));
        eM_dynamotunnel6_UMV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15287, "hatch.dynamotunnel6.tier.12", "UMV 262144/t Laser Source Hatch", 12, 262144).getStackForm(1L));
        eM_dynamotunnel7_UMV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15297, "hatch.dynamotunnel7.tier.12", "UMV 1048576/t Laser Source Hatch", 12, 1048576).getStackForm(1L));

        eM_dynamotunnel1_UXV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15238, "hatch.dynamotunnel1.tier.13", "UXV 256/t Laser Source Hatch", 13, 256).getStackForm(1L));
        eM_dynamotunnel2_UXV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15248, "hatch.dynamotunnel2.tier.13", "UXV 1024/t Laser Source Hatch", 13, 1024).getStackForm(1L));
        eM_dynamotunnel3_UXV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15258, "hatch.dynamotunnel3.tier.13", "UXV 4096/t Laser Source Hatch", 13, 4096).getStackForm(1L));
        eM_dynamotunnel4_UXV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15268, "hatch.dynamotunnel4.tier.13", "UXV 16384/t Laser Source Hatch", 13, 16384).getStackForm(1L));
        eM_dynamotunnel5_UXV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15278, "hatch.dynamotunnel5.tier.13", "UXV 65536/t Laser Source Hatch", 13, 65536).getStackForm(1L));
        eM_dynamotunnel6_UXV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15288, "hatch.dynamotunnel6.tier.13", "UXV 262144/t Laser Source Hatch", 13, 262144).getStackForm(1L));
        eM_dynamotunnel7_UXV.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15298, "hatch.dynamotunnel7.tier.13", "UXV 1048576/t Laser Source Hatch", 13, 1048576).getStackForm(1L));
        eM_dynamotunnel9001.set(new GT_MetaTileEntity_Hatch_DynamoTunnel(
                15299, "hatch.dynamotunnel.tier.14", "Legendary Laser Source Hatch", 14, (int)V[14]).getStackForm(1L));
        
        // ===================================================================================================
        // MULTIBLOCKS
        // ===================================================================================================

        Machine_Multi_Transformer.set(new GT_MetaTileEntity_EM_transformer(15300, "multimachine.em.transformer", "Active Transformer").getStackForm(1L));

        Machine_Multi_Switch.set(new GT_MetaTileEntity_EM_switch(15310, "multimachine.em.switch", "Network Switch With QoS").getStackForm(1L));
        Machine_Multi_Computer.set(new GT_MetaTileEntity_EM_computer(15311, "multimachine.em.computer", "Quantum Computer").getStackForm(1L));
        Machine_Multi_Microwave.set(new GT_MetaTileEntity_TM_microwave(15312, "multimachine.tm.microwave", "Microwave Grinder").getStackForm(1L));
        Machine_Multi_DataBank.set(new GT_MetaTileEntity_EM_dataBank(15313, "multimachine.em.databank", "Data Bank").getStackForm(1L));
        Machine_Multi_teslaCoil.set(new GT_MetaTileEntity_TM_teslaCoil(15314, "multimachine.tm.teslaCoil", "Tesla Coil").getStackForm(1L));

        Machine_Multi_EMjunction.set(new GT_MetaTileEntity_EM_junction(15320, "multimachine.em.junction", "Matter Junction").getStackForm(1L));
        Machine_Multi_MatterToEM.set(new GT_MetaTileEntity_EM_quantizer(15321, "multimachine.em.mattertoem", "Matter Quantizer").getStackForm(1L));
        Machine_Multi_EMToMatter.set(new GT_MetaTileEntity_EM_dequantizer(15322, "multimachine.em.emtomatter", "Matter Dequantizer").getStackForm(1L));

        // COMPAT
        Machine_Multi_EssentiaToEM.set(new GT_MetaTileEntity_EM_essentiaQuantizer(15323, "multimachine.em.essentiatoem", "Essentia Quantizer").getStackForm(1L));
        Machine_Multi_EMToEssentia.set(new GT_MetaTileEntity_EM_essentiaDequantizer(15324, "multimachine.em.emtoessentia", "Essentia Dequantizer").getStackForm(1L));

        Machine_Multi_Scanner.set(new GT_MetaTileEntity_EM_scanner(15330, "multimachine.em.scanner", "Elemental Scanner").getStackForm(1L));
        Machine_Multi_Research.set(new GT_MetaTileEntity_EM_research(15331, "multimachine.em.research", "Research station").getStackForm(1L));

        Machine_Multi_Collider.set(new GT_MetaTileEntity_EM_collider(15340, "multimachine.em.collider", "Matter Collider").getStackForm(1L));

        Machine_Multi_Infuser.set(new GT_MetaTileEntity_EM_infuser(15350, "multimachine.em.infuser", "Energy Infuser").getStackForm(1));

        Machine_Multi_EMmachine.set(new GT_MetaTileEntity_EM_machine(15360, "multimachine.em.processing", "Quantum Processing Machine").getStackForm(1L));

        Machine_Multi_EMCrafter.set(new GT_MetaTileEntity_EM_crafting(15370, "multimachine.em.crafter", "Matter Assembler").getStackForm(1L));

        Machine_Multi_Stabilizer.set(new GT_MetaTileEntity_EM_stabilizer(15380, "multimachine.em.stabilizer", "Elemental Stabilizer").getStackForm(1L));

        Machine_Multi_Wormhole.set(new GT_MetaTileEntity_EM_wormhole(15390, "multimachine.em.wormhole", "Wormhole").getStackForm(1L));

        Machine_Multi_Decay.set(new GT_MetaTileEntity_EM_decay(15400, "multimachine.em.decay", "Decay Generator").getStackForm(1L));
        Machine_Multi_Annihilation.set(new GT_MetaTileEntity_EM_annihilation(15405, "multimachine.em.annihilation", "Annihilation Generator").getStackForm(1L));
        Machine_Multi_BHG.set(new GT_MetaTileEntity_EM_bhg(15410, "multimachine.em.blackholegenerator", "Black Hole Generator").getStackForm(1L));

        // ===================================================================================================
        // Hatches
        // ===================================================================================================

        Parametrizer_Hatch.set(new GT_MetaTileEntity_Hatch_Param(15420, "hatch.param.tier.07", "Parametrizer", 7).getStackForm(1L));
        ParametrizerX_Hatch.set(new GT_MetaTileEntity_Hatch_Param(15421, "hatch.param.tier.10", "Parametrizer X", 10).getStackForm(1L));

        Uncertainty_Hatch.set(new GT_MetaTileEntity_Hatch_Uncertainty(15430, "hatch.certain.tier.07", "Uncertainty Resolver", 7).getStackForm(1L));
        UncertaintyX_Hatch.set(new GT_MetaTileEntity_Hatch_Uncertainty(15431, "hatch.certain.tier.10", "Uncertainty Resolver X", 10).getStackForm(1L));

        dataIn_Hatch.set(new GT_MetaTileEntity_Hatch_InputData(15440, "hatch.datain.tier.07", "Optical Slave Connector", 7).getStackForm(1L));
        dataOut_Hatch.set(new GT_MetaTileEntity_Hatch_OutputData(15441, "hatch.dataout.tier.07", "Optical Master Connector", 7).getStackForm(1L));
        dataInAss_Hatch.set(new GT_MetaTileEntity_Hatch_InputDataItems(15442, "hatch.datainass.tier.07", "Assembly line Slave Connector", 7).getStackForm(1L));
        dataOutAss_Hatch.set(new GT_MetaTileEntity_Hatch_OutputDataItems(15443, "hatch.dataoutass.tier.07", "Data Bank Master Connector", 7).getStackForm(1L));

        rack_Hatch.set(new GT_MetaTileEntity_Hatch_Rack(15450, "hatch.rack.tier.08", "Computer Rack", 8, "4 Slot Rack").getStackForm(1L));
        holder_Hatch.set(new GT_MetaTileEntity_Hatch_Holder(15451, "hatch.holder.tier.09", "Object Holder", 8, "For Research Station").getStackForm(1L));

        capacitor_Hatch.set(new GT_MetaTileEntity_Hatch_Capacitor(15452, "hatch.capacitor.tier.05", "Capacitor Hatch", 5, "For Tesla Coil").getStackForm(1L));

        // ===================================================================================================
        // Pipes
        // ===================================================================================================

        EMpipe.set(new GT_MetaTileEntity_Pipe_EM(15460, "pipe.elementalmatter", "Quantum \"Tunnel\"").getStackForm(1L));
        LASERpipe.set(new GT_MetaTileEntity_Pipe_Energy(15465, "pipe.energystream", "Laser Vacuum Pipe").getStackForm(1L));
        DATApipe.set(new GT_MetaTileEntity_Pipe_Data(15470, "pipe.datastream", "Optical Fiber Cable").getStackForm(1L));

        // ===================================================================================================
        // Single Blocks
        // ===================================================================================================

        Machine_OwnerDetector.set(new GT_MetaTileEntity_OwnerDetector(15480, "machine.tt.ownerdetector", "Owner detector", 3).getStackForm(1L));
        Machine_DataReader.set(new GT_MetaTileEntity_DataReader(15481, "machine.tt.datareader", "Data Reader", 5).getStackForm(1L));

        Machine_BuckConverter_IV.set(new GT_MetaTileEntity_BuckConverter(15485, "machine.tt.buck.05", "Insane Buck Converter", 5).getStackForm(1L));
        Machine_BuckConverter_LuV.set(new GT_MetaTileEntity_BuckConverter(15486, "machine.tt.buck.06", "Ludicrous Buck Converter", 6).getStackForm(1L));
        Machine_BuckConverter_ZPM.set(new GT_MetaTileEntity_BuckConverter(15487, "machine.tt.buck.07", "ZPM Voltage Buck Converter", 7).getStackForm(1L));
        Machine_BuckConverter_UV.set(new GT_MetaTileEntity_BuckConverter(15488, "machine.tt.buck.08", "Ultimate Power Buck Converter", 8).getStackForm(1L));
        Machine_BuckConverter_UHV.set(new GT_MetaTileEntity_BuckConverter(15489, "machine.tt.buck.09", "Highly Ultimate Buck Converter", 9).getStackForm(1L));
        Machine_BuckConverter_UEV.set(new GT_MetaTileEntity_BuckConverter(15490, "machine.tt.buck.10", "Extremely Ultimate Buck Converter", 10).getStackForm(1L));
        Machine_BuckConverter_UIV.set(new GT_MetaTileEntity_BuckConverter(15491, "machine.tt.buck.11", "Insanely Ultimate Buck Converter", 11).getStackForm(1L));
        Machine_BuckConverter_UMV.set(new GT_MetaTileEntity_BuckConverter(15492, "machine.tt.buck.12", "Mega Ultimate Buck Converter", 12).getStackForm(1L));
        Machine_BuckConverter_UXV.set(new GT_MetaTileEntity_BuckConverter(15493, "machine.tt.buck.13", "Extended Mega Ultimate Buck Converter", 13).getStackForm(1L));

        // ===================================================================================================
        // Debug Stuff
        // ===================================================================================================
        hatch_CreativeData.set(new GT_MetaTileEntity_Hatch_CreativeData(15496,"debug.tt.data","Debug Data Hatch",15).getStackForm(1));
        hatch_CreativeMaitenance.set(new GT_MetaTileEntity_Hatch_CreativeMaintenance(15497, "debug.tt.maintenance", "Debug Maintenance Hatch", 15).getStackForm(1L));
        Machine_DebugGenny.set(new GT_MetaTileEntity_DebugPowerGenerator(15498, "debug.tt.genny", "Debug Power Generator", 15).getStackForm(1L));
        Machine_DebugWriter.set(new GT_MetaTileEntity_DebugStructureWriter(15499, "debug.tt.writer", "Debug Structure Writer", 15).getStackForm(1L));
        UnusedStuff.set(new ItemStack(Blocks.air));

        // ===================================================================================================
        // MetaTE init
        // ===================================================================================================

        GT_MetaTileEntity_Hatch_Rack.run();
        GT_MetaTileEntity_DataReader.run();

        if (!Loader.isModLoaded(Reference.DREAMCRAFT)) {
            new NoDreamCraftMachineLoader().run();
        }
    }
}
