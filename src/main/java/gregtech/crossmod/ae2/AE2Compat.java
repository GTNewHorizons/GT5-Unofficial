package gregtech.crossmod.ae2;

import net.minecraft.item.ItemStack;

import appeng.api.AEApi;
import appeng.api.storage.IExternalStorageRegistry;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.objects.AE2DigitalChestHandler;
import gregtech.api.objects.AE2NonconsumableHatchHandler;
import gregtech.common.covers.CoverFacadeAE;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputME;

public final class AE2Compat {

    public static void onPreInit() {
        AEApi.instance()
            .registries()
            .interfaceTerminal()
            .register(MTEHatchCraftingInputME.class);
    }

    public static void onPostInit() {
        registerCover();
        registerExternalStorage();
    }

    private static void registerCover() {
        ItemStack facade = AEApi.instance()
            .definitions()
            .items()
            .facade()
            .maybeItem()
            .transform(i -> new ItemStack(i, 1, GTValues.W))
            .orNull();
        if (facade != null) {
            CoverRegistry.registerCover(facade, null, new CoverFacadeAE());
        }
    }

    private static void registerExternalStorage() {
        IExternalStorageRegistry registry = AEApi.instance()
            .registries()
            .externalStorage();
        registry.addExternalStorageInterface(new AE2DigitalChestHandler());
        registry.addExternalStorageInterface(new AE2NonconsumableHatchHandler());
    }
}
