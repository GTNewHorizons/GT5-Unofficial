package tectech.thing.item;

import static net.minecraft.util.StatCollector.translateToLocal;

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

import com.gtnewhorizon.gtnhlib.chat.customcomponents.ChatComponentEnergy;
import com.gtnewhorizon.gtnhlib.chat.customcomponents.ChatComponentNumber;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTECable;
import gregtech.api.util.GTUtility;
import gregtech.api.util.scanner.ScannerHelper;
import tectech.Reference;
import tectech.TecTech;
import tectech.util.CommonValues;

public class ItemEuMeterGT extends Item {

    public static ItemEuMeterGT INSTANCE;

    private ItemEuMeterGT() {
        setMaxStackSize(1);
        setUnlocalizedName("em.EuMeterGT");
        setTextureName(Reference.MODID + ":itemEuMeterGT");
        setCreativeTab(TecTech.creativeTabTecTech);
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
            if (tTileEntity instanceof BaseMetaTileEntity metaTileEntity) {
                GTUtility.sendChatTrans(
                    aPlayer,
                    "tt.chat.eu_meter.position",
                    new ChatComponentNumber(aX),
                    new ChatComponentNumber(aY),
                    new ChatComponentNumber(aZ),
                    aWorld.provider.dimensionId,
                    ordinalSide);
                GTUtility.sendChatTrans(
                    aPlayer,
                    "tt.chat.eu_meter.stored_energy",
                    new ChatComponentEnergy(metaTileEntity.getUniversalEnergyStored()),
                    new ChatComponentEnergy(metaTileEntity.getUniversalEnergyCapacity()));
                GTUtility.sendChatTrans(
                    aPlayer,
                    "tt.chat.eu_meter.stored_eu",
                    new ChatComponentEnergy(metaTileEntity.getStoredEU()),
                    new ChatComponentEnergy(metaTileEntity.getEUCapacity()));
                GTUtility.sendChatTrans(
                    aPlayer,
                    "tt.chat.eu_meter.average_io",
                    new ChatComponentNumber(metaTileEntity.getAverageElectricInput()),
                    new ChatComponentNumber(metaTileEntity.getAverageElectricOutput()));
                GTUtility.sendChatTrans(
                    aPlayer,
                    "tt.chat.eu_meter.average_io_(max)",
                    new ChatComponentNumber(metaTileEntity.getInputVoltage()),
                    new ChatComponentNumber(metaTileEntity.getOutputVoltage()));
                GTUtility.sendChatTrans(
                    aPlayer,
                    "tt.chat.eu_meter.average_io_max",
                    new ChatComponentNumber(metaTileEntity.getMaxSafeInput()),
                    new ChatComponentNumber(metaTileEntity.getMaxEnergyOutput()));
                GTUtility.sendChatTrans(
                    aPlayer,
                    "tt.chat.eu_meter.amperage_io_(max)",
                    new ChatComponentNumber(metaTileEntity.getInputAmperage()),
                    new ChatComponentNumber(metaTileEntity.getOutputAmperage()));
                boolean allowEnergyInput = metaTileEntity.inputEnergyFrom(side);
                boolean allowEnergyOutput = metaTileEntity.outputsEnergyTo(side);
                if (allowEnergyInput && allowEnergyOutput) {
                    GTUtility.sendChatTrans(aPlayer, "tt.chat.eu_meter.side_capabilities.io");
                } else if (allowEnergyInput) {
                    GTUtility.sendChatTrans(aPlayer, "tt.chat.eu_meter.side_capabilities.input");
                } else if (allowEnergyOutput) {
                    GTUtility.sendChatTrans(aPlayer, "tt.chat.eu_meter.side_capabilities.output");
                }
                return true;
            } else if (tTileEntity instanceof BaseMetaPipeEntity) {
                if (((BaseMetaPipeEntity) tTileEntity).getMetaTileEntity() instanceof MTECable) {
                    List<String> list = new ArrayList<>();
                    ScannerHelper.scan(list, aPlayer, aWorld, 1, aX, aY, aZ, side, hitX, hitY, hitZ);
                    for (String str : list) {
                        GTUtility.sendChatToPlayer(aPlayer, str);
                    }
                }
                return true;
            }
        }
        if (!(aPlayer instanceof EntityPlayerMP)) {
            GTUtility.doSoundAtClient(new ResourceLocation(Reference.MODID, "fx_scan"), 1, 1.0F, aX, aY, aZ);
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
        INSTANCE = new ItemEuMeterGT();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
    }
}
