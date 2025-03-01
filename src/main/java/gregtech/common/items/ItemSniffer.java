package gregtech.common.items;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandlers;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.*;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import com.ibm.icu.impl.TextTrieMap;
import gregtech.api.GregTechAPI;
import gregtech.api.items.GTGenericItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class ItemSniffer extends GTGenericItem implements IGuiHolder<GuiData> {
    private String label = "Description";
    private String uuid;
    public ItemSniffer(String aUnlocalized, String aEnglish, String aEnglishTooltip) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
    }
    @Override
    public ModularPanel buildUI(GuiData guiData, PanelSyncManager guiSyncManager) {



        Map<Integer, Map<Long, Byte>> publicFreqs = GregTechAPI.sAdvancedWirelessRedstone.get("null");
        Map<Integer, Map<Long, Byte>> privateFreqs = GregTechAPI.sAdvancedWirelessRedstone.get(this.uuid);

        publicFreqs.forEach((frequency, map) -> {
            map.forEach((hash,str) -> {
            });

        });
        privateFreqs.forEach((frequency, map) -> {
            map.forEach((hash,str) -> {
            });

        });

        ModularPanel panel = ModularPanel.defaultPanel("redstone_sniffer");
        panel.flex()
                .sizeRel(0.5f, 0.5f)
                .align(Alignment.Center);

        ParentWidget data = new ParentWidget<>();
        data.sizeRel(1,0.8f).margin(10).coverChildren();

        AtomicInteger i = new AtomicInteger();
        GregTechAPI.sWirelessRedstone.forEach((freq,str) ->{

            boolean isPrivate = freq > 65535;
            int displayFreq = isPrivate ? freq - 65535 : freq;
            data.addChild(new Row()
                .sizeRel(1,0.1f)
                .posRel(0,i.get()*0.1f)
                .margin(10)
                .child(new TextWidget(String.valueOf(displayFreq)).widthRel(1.0f/3.0f))
                .child(new TextWidget(isPrivate ? "Yes" : "No").widthRel(1.0f/3.0f))
                .child(new TextFieldWidget()
                    .value(SyncHandlers.string(() -> this.label, label -> this.label = label))
                    .widthRel(1.0f/3.0f)), i.get());
            i.getAndIncrement();
        });

        panel.child(new Column()
            .child(new Row()
                .margin(10)
                .heightRel(0.1f)
                .child(new TextWidget("Frequency").widthRel(1.0f/3.0f).alignment(Alignment.Center))
                .child(new TextWidget("Private").widthRel(1.0f/3.0f).alignment(Alignment.Center))
                .child(new TextWidget("Label").widthRel(1.0f/3.0f).alignment(Alignment.Center)))
            .child(data)
            .margin(10));


        return panel;
    }
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

        if (!world.isRemote) {
            this.uuid = String.valueOf(player.getUniqueID());

            GuiFactories.item().open(player);
        }
        return super.onItemRightClick(stack,world,player);
    }
}
