package gtPlusPlus.core.handler;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.github.bsideup.jabel.Desugar;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.ModItems;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class BookHandler {

    public static Int2ObjectMap<BookTemplate> mBookMap = new Int2ObjectOpenHashMap<>();

    public static BookTemplate book_ThermalBoiler;
    public static BookTemplate book_MultiPowerStation;
    public static BookTemplate book_ModularBauble;
    public static BookTemplate book_NuclearManual;

    public static void run() {

        Logger.INFO("Writing books.");

        // Thermal Boiler
        book_ThermalBoiler = writeBookTemplate(
            0,
            "Manual_Thermal_Boiler",
            "gtpp.book.thermal_boiler.name",
            "GregoriusT",
            new String[] { "gtpp.book.thermal_boiler.page00", "gtpp.book.thermal_boiler.page01",
                "gtpp.book.thermal_boiler.page02", "gtpp.book.thermal_boiler.page03", "gtpp.book.thermal_boiler.page04",
                "gtpp.book.thermal_boiler.page05", "gtpp.book.thermal_boiler.page06", "gtpp.book.thermal_boiler.page07",
                "gtpp.book.thermal_boiler.page08", "gtpp.book.thermal_boiler.page09", "gtpp.book.thermal_boiler.page10",
                "gtpp.book.thermal_boiler.page11", "gtpp.book.thermal_boiler.page12", "gtpp.book.thermal_boiler.page13",
                "gtpp.book.thermal_boiler.page14", "gtpp.book.thermal_boiler.page15" });

        // Test Novel
        book_MultiPowerStation = writeBookTemplate(
            1,
            "Manual_Multi_PowerStation",
            "gtpp.book.power_storage_v064.name",
            "Alkalus",
            new String[] { "gtpp.book.power_storage_v064.page00", "gtpp.book.power_storage_v064.page01",
                "gtpp.book.power_storage_v064.page02", "gtpp.book.power_storage_v064.page03",
                "gtpp.book.power_storage_v064.page04", "gtpp.book.power_storage_v064.page05",
                "gtpp.book.power_storage_v064.page06", "gtpp.book.power_storage_v064.page07" });

        // Test Novel
        book_ModularBauble = writeBookTemplate(
            2,
            "Manual_Modular_Bauble",
            "gtpp.book.modular_baubles.name",
            "Alkalus",
            new String[] { "gtpp.book.modular_baubles.page00", "gtpp.book.modular_baubles.page01",
                "gtpp.book.modular_baubles.page02", "gtpp.book.modular_baubles.page03",
                "gtpp.book.modular_baubles.page04" });

        book_NuclearManual = writeBookTemplate(
            4,
            "Manual_NuclearStuff_1",
            "gtpp.book.nuclear_chemistry_ffpp.name",
            "Alkalus",
            new String[] { "gtpp.book.nuclear_chemistry_ffpp.page00", "gtpp.book.nuclear_chemistry_ffpp.page01",
                "gtpp.book.nuclear_chemistry_ffpp.page02", "gtpp.book.nuclear_chemistry_ffpp.page03",
                "gtpp.book.nuclear_chemistry_ffpp.page04" });
    }

    public static void runLater() {
        // Multiblock Manuals

        // Thermal Boiler
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(ModItems.itemCustomBook, 1, 0),
            new ItemStack[] { new ItemStack(Items.writable_book), new ItemStack(Items.lava_bucket) });

        // Power Substation
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(ModItems.itemCustomBook, 1, 1),
            new ItemStack[] { new ItemStack(Items.writable_book),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.Tin, 1) });

        // Nuclear Manual
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(ModItems.itemCustomBook, 1, 4),
            new ItemStack[] { new ItemStack(Items.writable_book),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Uranium, 1) });

        for (int meta : mBookMap.keySet()) {
            ItemStack bookstack = new ItemStack(ModItems.itemCustomBook, 1, meta);
            GTOreDictUnificator.registerOre("bookWritten", bookstack);
            GTOreDictUnificator.registerOre("craftingBook", bookstack);
        }
    }

    public static BookTemplate writeBookTemplate(int meta, String aMapping, String aTitle, String aAuthor,
        String[] aPages) {
        BookTemplate mTemp = new BookTemplate(meta, aMapping, aTitle, aAuthor, aPages);
        mBookMap.put(meta, mTemp);
        return mTemp;
    }

    @Desugar
    public record BookTemplate(int mMeta, String mMapping, String mTitle, String mAuthor, String[] mPages) {}
}
