package gregtech.common.items;

import com.cleanroommc.modularui.api.GuiAxis;
import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandlers;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.ScrollWidget;
import com.cleanroommc.modularui.widget.scroll.ScrollArea;
import com.cleanroommc.modularui.widget.scroll.ScrollData;
import com.cleanroommc.modularui.widget.scroll.VerticalScrollData;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class ItemSniffer extends GTGenericItem implements IGuiHolder<GuiData> {
    private final Map<Integer, String> regularWirelessLabels = new ConcurrentHashMap<>();
    private String uuid;

    public ItemSniffer(String aUnlocalized, String aEnglish, String aEnglishTooltip) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
    }

    @Override
    public ModularPanel buildUI(GuiData guiData, PanelSyncManager guiSyncManager) {
        PagedWidget.Controller controller = new PagedWidget.Controller();



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

        PagedWidget data = new PagedWidget();
        data.sizeRel(1,0.65f);
        data.controller(controller);

        ScrollWidget wirelessPage = new ScrollWidget(new VerticalScrollData());
        wirelessPage.sizeRel(1);
        wirelessPage.getScrollArea().getScrollY().setScrollSize(35*GregTechAPI.sWirelessRedstone.size());

        AtomicInteger i = new AtomicInteger(0);

        //Process regular wireless redstone frequencies
        GregTechAPI.sWirelessRedstone.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry ->{
            int freq = entry.getKey();
            boolean isPrivate = freq > 65535;
            int displayFreq = isPrivate ? freq - 65536 : freq;
            wirelessPage.child(
                new Row()
                .sizeRel(1f,0.2f)
                .pos(0,i.get()*35)
                .child(new TextWidget(String.valueOf(displayFreq)).widthRel(1.0f/3.0f).alignment(Alignment.Center))
                .child(new TextWidget(isPrivate ? "Yes" : "No").widthRel(1.0f/3.0f).alignment(Alignment.Center))
                .child(new TextFieldWidget()
                    .align(Alignment.CenterRight)
                    .sizeRel(1.0f/3.25f, 1)
                    .value(SyncHandlers.string(() -> this.regularWirelessLabels.getOrDefault(freq, "Description"),
                        label -> this.regularWirelessLabels.put(freq, label)))
                    ));
            i.getAndIncrement();
        });
        data.addPage(wirelessPage);

        //Process advanced wireless redstone frequencies
        panel.child(new Column()
            .child(new Row()
                .heightRel(0.1f)
                .margin(10)
                .child(new PageButton(0, controller)
                    .widthRel(0.5f)
                    .align(Alignment.CenterLeft)
                    .overlay(IKey.dynamic(() -> "Regular Wireless")))
                .child(new PageButton(1, controller)
                    .widthRel(0.5f)
                    .align(Alignment.CenterRight)
                    .overlay(IKey.dynamic(() -> "Advanced Wireless"))))
            .child(new Row()
                .margin(5)
                .heightRel(0.1f)
                .child(new TextWidget("Frequency").widthRel(1.0f/3.0f).alignment(Alignment.Center))
                .child(new TextWidget("Private").widthRel(1.0f/3.0f).alignment(Alignment.Center))
                .child(new TextWidget("Label").widthRel(1.0f/3.0f).alignment(Alignment.Center)))
            .child(data)
            .margin(10));
        System.out.println(wirelessPage.getChildren().size());

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
