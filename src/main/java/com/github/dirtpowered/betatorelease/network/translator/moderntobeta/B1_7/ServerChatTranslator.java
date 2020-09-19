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

package com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7;

import com.github.dirtpowered.betatorelease.BetaToRelease;
import com.github.dirtpowered.betatorelease.network.client.ModernClient;
import com.github.dirtpowered.betatorelease.network.session.ServerSession;
import com.github.dirtpowered.betatorelease.network.translator.model.ModernToBeta;
import com.github.steveice10.mc.protocol.data.game.MessageType;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.apache.commons.lang3.StringUtils;

public class ServerChatTranslator implements ModernToBeta<ServerChatPacket> {

    @Override
    public void translate(BetaToRelease main, ServerChatPacket packet, ServerSession session, ModernClient modernClient) {
        String messageJson = packet.getMessage().toJsonString();
        String formattedMessage = TextComponent.toLegacyText(ComponentSerializer.parse(messageJson));

        MessageType messageType = packet.getType();

        if (messageType == MessageType.CHAT || messageType == MessageType.SYSTEM) {
            String chat = StringUtils.substring(formattedMessage, 0, 119);
            String additionalChatMessage = null;

            if (formattedMessage.length() > 119) {
                additionalChatMessage = StringUtils.substring(formattedMessage, 119, chat.length());
            }

            session.getBetaPlayer().sendMessage(chat);
            if (additionalChatMessage != null) {
                session.getBetaPlayer().sendMessage(additionalChatMessage);
            }
        } else if (messageType == MessageType.NOTIFICATION) {
            // TODO: send on change or something
        }
    }
}
