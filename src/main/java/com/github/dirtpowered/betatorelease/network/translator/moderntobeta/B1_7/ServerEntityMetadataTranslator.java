package com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.NamedEntitySpawnPacketData;
import com.github.dirtpowered.betatorelease.BetaToRelease;
import com.github.dirtpowered.betatorelease.data.entity.Entity;
import com.github.dirtpowered.betatorelease.network.client.ModernClient;
import com.github.dirtpowered.betatorelease.network.session.ServerSession;
import com.github.dirtpowered.betatorelease.network.translator.model.ModernToBeta;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.MetadataType;
import com.github.steveice10.mc.protocol.data.message.TextMessage;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityMetadataPacket;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.apache.commons.lang3.StringUtils;

public class ServerEntityMetadataTranslator  implements ModernToBeta<ServerEntityMetadataPacket> {

    @Override
    public void translate(BetaToRelease main, ServerEntityMetadataPacket packet, ServerSession session, ModernClient modernClient) {
        int entityId = packet.getEntityId();
        EntityMetadata[] metadata = packet.getMetadata();

        // TODO: holograms
        for (EntityMetadata entityMetadata : metadata) {
            MetadataType type = entityMetadata.getType();

            if (type == MetadataType.OPTIONAL_CHAT) {
                TextMessage val = (TextMessage) entityMetadata.getValue();
                if (entityMetadata.getValue() != null) {
                    Entity e = session.getEntityCache().getEntityById(entityId);
                    if (e != null) {

                        int x = Utils.toAbsolutePos(e.getX());
                        int y = Utils.toAbsolutePos(e.getY());
                        int z = Utils.toAbsolutePos(e.getZ());

                        String formattedMessage = TextComponent.toLegacyText(ComponentSerializer.parse(val.toJsonString()));

                        session.sendPacket(new NamedEntitySpawnPacketData(entityId, StringUtils.substring(formattedMessage, 0, 16),
                                x, y, z, (byte) 0, (byte) 0, 0));

                        System.out.println(val.getText());
                    }
                }
            }
        }
    }
}
