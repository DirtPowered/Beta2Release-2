/*
 * Copyright (c) 2020 Dirt Powered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.dirtpowered.betatorelease.network.translator.betatomodern.B1_7;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.EntityActionPacketData;
import com.github.dirtpowered.betatorelease.BetaToRelease;
import com.github.dirtpowered.betatorelease.network.client.ModernClient;
import com.github.dirtpowered.betatorelease.network.session.ServerSession;
import com.github.dirtpowered.betatorelease.network.translator.model.BetaToModern;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerState;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerStatePacket;

public class EntityActionTranslator implements BetaToModern<EntityActionPacketData> {

    @Override
    public void translate(BetaToRelease main, EntityActionPacketData packet, ServerSession session, ModernClient modernClient) {
        int entityId = packet.getEntityId();
        int state = packet.getState();
        PlayerState playerState;

        switch (state) {
            case 1:
                playerState = PlayerState.START_SNEAKING;
                break;
            case 2:
                playerState = PlayerState.STOP_SNEAKING;
                break;
            case 3:
                playerState = PlayerState.LEAVE_BED;
                break;
            default:
                playerState = null;
                break;
        }

        if (playerState == null)
            return;

        modernClient.sendPacket(new ClientPlayerStatePacket(entityId, playerState));
    }
}
