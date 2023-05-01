package com.github.technus.tectech.thing.item;

import static com.github.technus.tectech.Reference.MODID;
import static com.github.technus.tectech.TecTech.creativeTabTecTech;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.github.technus.tectech.Reference;
import com.github.technus.tectech.util.CommonValues;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Cable;
import gregtech.api.util.GT_Utility;

public class EuMeterGT extends Item {

    public static EuMeterGT INSTANCE;

    private EuMeterGT() {
        setMaxStackSize(1);
        setUnlocalizedName("em.EuMeterGT");
        setTextureName(MODID + ":itemEuMeterGT");
        setCreativeTab(creativeTabTecTech);
    }

    @Override
    public boolean onItemUseFirst(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ,
            int ordinalSide, float hitX, float hitY, float hitZ) {
        final ForgeDirection side = ForgeDirection.getOrientation(ordinalSide);
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity == null || aPlayer instanceof FakePlayer) {
            return aPlayer instanceof EntityPlayerMP;
        }
        if (aPlayer instanceof EntityPlayerMP && !aPlayer.isSneaking() && tTileEntity instanceof IGregTechTileEntity) {
            String clientLocale = "en_US";
            try {
                EntityPlayerMP player = (EntityPlayerMP) aPlayer;
                clientLocale = (String) FieldUtils.readField(player, "translator", true);
            } catch (Exception e) {
                clientLocale = "en_US";
            }

            if (tTileEntity instanceof BaseMetaTileEntity) {
                GT_Utility.sendChatToPlayer(
                        aPlayer,
                        EnumChatFormatting.AQUA + "----- X:"
                                + aX
                                + " Y:"
                                + aY
                                + " Z:"
                                + aZ
                                + " D:"
                                + aWorld.provider.dimensionId
                                + " S:"
                                + ordinalSide
                                + " -----");
                GT_Utility.sendChatToPlayer(
                        aPlayer,
                        translateToLocalFormatted("tt.keyphrase.Stored_energy", clientLocale) + ": "
                                + EnumChatFormatting.YELLOW
                                + (((BaseMetaTileEntity) tTileEntity).getUniversalEnergyStored())
                                + EnumChatFormatting.RESET
                                + '/'
                                + EnumChatFormatting.GREEN
                                + (((BaseMetaTileEntity) tTileEntity).getUniversalEnergyCapacity()));
                GT_Utility.sendChatToPlayer(
                        aPlayer,
                        translateToLocalFormatted("tt.keyphrase.Stored_EU", clientLocale) + ": "
                                + EnumChatFormatting.YELLOW
                                + (((BaseMetaTileEntity) tTileEntity).getStoredEU())
                                + EnumChatFormatting.RESET
                                + '/'
                                + EnumChatFormatting.GREEN
                                + (((BaseMetaTileEntity) tTileEntity).getEUCapacity()));
                GT_Utility.sendChatToPlayer(
                        aPlayer,
                        translateToLocalFormatted("tt.keyphrase.Average_IO", clientLocale) + ": "
                                + EnumChatFormatting.YELLOW
                                + (((BaseMetaTileEntity) tTileEntity).getAverageElectricInput())
                                + EnumChatFormatting.RESET
                                + '/'
                                + EnumChatFormatting.YELLOW
                                + (((BaseMetaTileEntity) tTileEntity).getAverageElectricOutput()));
                GT_Utility.sendChatToPlayer(
                        aPlayer,
                        translateToLocalFormatted("tt.keyphrase.Average_IO_(max)", clientLocale) + ": "
                                + EnumChatFormatting.GOLD
                                + (((BaseMetaTileEntity) tTileEntity).getInputVoltage())
                                + EnumChatFormatting.RESET
                                + '/'
                                + EnumChatFormatting.GOLD
                                + (((BaseMetaTileEntity) tTileEntity).getOutputVoltage()));
                GT_Utility.sendChatToPlayer(
                        aPlayer,
                        translateToLocalFormatted("tt.keyphrase.Average_IO_max", clientLocale) + ": "
                                + EnumChatFormatting.RED
                                + (((BaseMetaTileEntity) tTileEntity).getMaxSafeInput())
                                + EnumChatFormatting.RESET
                                + '/'
                                + EnumChatFormatting.RED
                                + (((BaseMetaTileEntity) tTileEntity).getMaxEnergyOutput()));
                GT_Utility.sendChatToPlayer(
                        aPlayer,
                        translateToLocalFormatted("tt.keyphrase.Amperage_IO_(max)", clientLocale) + ": "
                                + EnumChatFormatting.GOLD
                                + (((BaseMetaTileEntity) tTileEntity).getInputAmperage())
                                + EnumChatFormatting.RESET
                                + '/'
                                + EnumChatFormatting.GOLD
                                + (((BaseMetaTileEntity) tTileEntity).getOutputAmperage()));
                GT_Utility.sendChatToPlayer(
                        aPlayer,
                        translateToLocalFormatted("tt.keyphrase.Side_capabilities", clientLocale) + ": "
                                + (((BaseMetaTileEntity) tTileEntity).inputEnergyFrom(side)
                                        ? translateToLocalFormatted("tt.keyword.input", clientLocale) + " "
                                        : "")
                                + (((BaseMetaTileEntity) tTileEntity).outputsEnergyTo(side)
                                        ? translateToLocalFormatted("tt.keyword.output", clientLocale)
                                        : ""));
                return true;
            } else if (tTileEntity instanceof BaseMetaPipeEntity) {
                if (((BaseMetaPipeEntity) tTileEntity).getMetaTileEntity() instanceof GT_MetaPipeEntity_Cable) {
                    ArrayList<String> tList = new ArrayList<>();
                    GT_Utility.getCoordinateScan(tList, aPlayer, aWorld, 1, aX, aY, aZ, side, hitX, hitY, hitZ);
                    for (String str : tList) {
                        GT_Utility.sendChatToPlayer(aPlayer, str);
                    }
                }
                return true;
            }
        }
        if (!(aPlayer instanceof EntityPlayerMP)) {
            GT_Utility.doSoundAtClient(new ResourceLocation(Reference.MODID, "fx_scan"), 1, 1.0F, aX, aY, aZ);
        }
        return false;
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List<String> aList, boolean boo) {
        aList.add(CommonValues.TEC_MARK_GENERAL);
        aList.add(translateToLocal("item.em.EuMeterGT.desc.0")); // Measures basic EU related stuff
        aList.add(EnumChatFormatting.BLUE + translateToLocal("item.em.EuMeterGT.desc.1")); // Just right click on
                                                                                           // blocks.
    }

    public static void run() {
        INSTANCE = new EuMeterGT();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
    }
}
