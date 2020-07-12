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

package com.github.dirtpowered.betatorelease.network.session;

import com.github.dirtpowered.betaprotocollib.model.Packet;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.KickDisconnectPacketData;
import com.github.dirtpowered.betatorelease.BetaToRelease;
import com.github.dirtpowered.betatorelease.configuration.B2RConfiguration;
import com.github.dirtpowered.betatorelease.data.ProtocolState;
import com.github.dirtpowered.betatorelease.data.player.BetaPlayer;
import com.github.dirtpowered.betatorelease.network.client.ModernClient;
import com.github.dirtpowered.betatorelease.network.translator.model.BetaToModern;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.internal.StringUtil;
import lombok.Getter;
import lombok.Setter;

public class ServerSession extends SimpleChannelInboundHandler<Packet> {

    private BetaToRelease main;
    private SocketChannel channel;
    private ModernClient modernClient;

    @Getter
    private BetaPlayer betaPlayer;

    @Getter
    @Setter
    private ProtocolState protocolState;

    public ServerSession(BetaToRelease main, SocketChannel channel) {
        this.main = main;
        this.channel = channel;
        this.modernClient = new ModernClient(main, this);

        this.protocolState = ProtocolState.HANDSHAKE;

        this.betaPlayer = new BetaPlayer(this);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) {
        processPacket(packet);
    }

    @Override
    public void channelActive(ChannelHandlerContext context) {
        main.getLogger().info("[" + getPrefix() + "] connected to B2R bridge");
        main.getSessionRegistry().addSession(this);
    }

    @Override
    public void channelInactive(ChannelHandlerContext context) {
        main.getLogger().info("[" + getPrefix() + "] disconnected from B2R bridge");
        main.getSessionRegistry().removeSession(this);
        if (protocolState != ProtocolState.HANDSHAKE) {
            modernClient.disconnectRemote();
        }

        disconnect();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        cause.printStackTrace();
    }

    @SuppressWarnings("unchecked")
    private void processPacket(Packet packet) {
        BetaToModern handler = main.getBetaToModernTranslatorRegistry().getByPacket(packet);

        if (handler != null) {
            handler.translate(main, packet, this, modernClient);
        } else {
            if (B2RConfiguration.debug)
                main.getLogger().warning("[" + getPrefix() + "]" + " missing 'BetaToModern' translator for: " + packet.getClass().getSimpleName());
        }
    }

    public String getAddress() {
        return channel.remoteAddress().toString();
    }

    public void sendPacket(Packet packet) {
        channel.writeAndFlush(packet);
    }

    private void disconnect() {
        disconnect(StringUtil.EMPTY_STRING);
    }

    public void disconnect(String message) {
        if (message.isEmpty()) {
            channel.close();
        } else {
            sendPacket(new KickDisconnectPacketData(message));
        }
    }

    private String getPrefix() {
        return protocolState == ProtocolState.HANDSHAKE ? getAddress() : betaPlayer.getUsername();
    }
}
