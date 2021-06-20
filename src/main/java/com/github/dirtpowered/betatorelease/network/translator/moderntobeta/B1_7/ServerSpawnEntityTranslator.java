package com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7;

import com.github.dirtpowered.betatorelease.BetaToRelease;
import com.github.dirtpowered.betatorelease.data.entity.Entity;
import com.github.dirtpowered.betatorelease.data.entity.EntityArmorStand;
import com.github.dirtpowered.betatorelease.network.client.ModernClient;
import com.github.dirtpowered.betatorelease.network.session.ServerSession;
import com.github.dirtpowered.betatorelease.network.translator.model.ModernToBeta;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnEntityPacket;

public class ServerSpawnEntityTranslator implements ModernToBeta<ServerSpawnEntityPacket> {

    @Override
    public void translate(BetaToRelease main, ServerSpawnEntityPacket packet, ServerSession session, ModernClient modernClient) {
        Entity e = new EntityArmorStand(packet.getEntityId());
        System.out.println(packet.getType());
        e.setX(packet.getX());
        e.setY(packet.getY());
        e.setZ(packet.getZ());

        session.getEntityCache().addEntity(packet.getEntityId(), e);
    }
}
