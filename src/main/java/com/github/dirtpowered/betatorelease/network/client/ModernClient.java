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

package com.github.dirtpowered.betatorelease.network.client;

import com.github.dirtpowered.betatorelease.BetaToRelease;
import com.github.dirtpowered.betatorelease.configuration.B2RConfiguration;
import com.github.dirtpowered.betatorelease.network.ping.PingMessage;
import com.github.dirtpowered.betatorelease.network.session.ServerSession;
import com.github.dirtpowered.betatorelease.network.translator.model.ModernToBeta;
import com.github.dirtpowered.betatorelease.utils.interfaces.Callback;
import com.github.steveice10.mc.protocol.MinecraftConstants;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.data.SubProtocol;
import com.github.steveice10.mc.protocol.data.status.handler.ServerInfoHandler;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.event.session.ConnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;

import java.net.Proxy;

public class ModernClient {

    private ServerSession serverSession;
    private BetaToRelease main;
    private Client client;

    public ModernClient(BetaToRelease main, ServerSession serverSession) {
        this.main = main;
        this.serverSession = serverSession;
    }

    public void connect() {
        String username = serverSession.getBetaPlayer().getUsername();

        MinecraftProtocol protocol = new MinecraftProtocol(username);
        client = new Client(B2RConfiguration.remoteAddress, B2RConfiguration.remotePort, protocol, new TcpSessionFactory());
        client.getSession().addListener(new SessionAdapter() {
            @Override
            public void packetReceived(PacketReceivedEvent event) {
                processPacket(event.getPacket());
            }

            @Override
            public void connected(ConnectedEvent event) {
                main.getLogger().info("[" + username + "] connected to remote server");
            }

            @Override
            public void disconnected(DisconnectedEvent event) {
                main.getLogger().info("[" + username + "] disconnected from remote server: " + event.getReason());
                disconnectAll(event.getReason());
            }
        });

        client.getSession().connect();
    }

    private void disconnectAll(String message) {
        serverSession.disconnect(message);

        if (client != null && client.getSession().isConnected()) {
            client.getSession().disconnect(message);
        }
    }

    public void disconnectRemote() {
        if (client != null) {
            client.getSession().disconnect("connection closed");
        }
    }

    @SuppressWarnings("unchecked")
    private void processPacket(Packet packet) {
        ModernToBeta handler = main.getModernToBetaTranslatorRegistry().getByPacket(packet);
        String username = serverSession.getBetaPlayer().getUsername();

        if (handler != null) {
            handler.translate(main, packet, serverSession, this);
            if (B2RConfiguration.debug)
                main.getLogger().info("[" + username + "] translating " + packet.getClass().getSimpleName());

        } else {
            if (B2RConfiguration.debug)
                main.getLogger().error("[" + username + "] missing 'ModernToBeta' translator for " + packet.getClass().getSimpleName());
        }
    }

    public void getStatus(Callback<PingMessage> callback) {
        MinecraftProtocol protocol = new MinecraftProtocol(SubProtocol.STATUS);
        Client client = new Client(B2RConfiguration.remoteAddress, B2RConfiguration.remotePort, protocol, new TcpSessionFactory());

        client.getSession().setFlag(MinecraftConstants.AUTH_PROXY_KEY, Proxy.NO_PROXY);
        client.getSession().setFlag(MinecraftConstants.SERVER_INFO_HANDLER_KEY, (ServerInfoHandler) (session, info) -> {
            callback.onComplete(new PingMessage(info.getDescription().getText(), info.getPlayerInfo().getOnlinePlayers(), info.getPlayerInfo().getMaxPlayers()));
        });

        client.getSession().connect();
    }

    public void sendPacket(Packet modernPacket) {
        client.getSession().send(modernPacket);
    }
}
