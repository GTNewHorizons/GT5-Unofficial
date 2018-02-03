package com.github.technus.tectech.network;

import com.github.technus.tectech.auxiliary.Reference;
import eu.usrv.yamcore.network.PacketDispatcher;

/**
 * Created by Bass on 25/07/2017.
 */
public class Dispatcher extends PacketDispatcher {
    public Dispatcher() {
        super(Reference.MODID);
    }

    @Override
    public void registerPackets() {
        //this.registerMessage(SpawnParticleFXMessage.SpawnParticleFXMessageHandler.class, SpawnParticleFXMessage.class);
    }
}
