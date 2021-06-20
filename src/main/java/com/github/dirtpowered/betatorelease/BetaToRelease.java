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

package com.github.dirtpowered.betatorelease;

import com.github.dirtpowered.betaprotocollib.BetaLib;
import com.github.dirtpowered.betaprotocollib.data.version.MinecraftVersion;
import com.github.dirtpowered.betatorelease.bootstrap.AbstractBootstrap;
import com.github.dirtpowered.betatorelease.configuration.B2RConfiguration;
import com.github.dirtpowered.betatorelease.data.mappings.PreFlatteningData;
import com.github.dirtpowered.betatorelease.logger.AbstractLogger;
import com.github.dirtpowered.betatorelease.network.codec.PipelineFactory;
import com.github.dirtpowered.betatorelease.network.protocol.B1_7;
import com.github.dirtpowered.betatorelease.network.session.ServerSession;
import com.github.dirtpowered.betatorelease.network.session.SessionRegistry;
import com.github.dirtpowered.betatorelease.network.translator.registry.BetaToModernTranslatorRegistry;
import com.github.dirtpowered.betatorelease.network.translator.registry.ModernToBetaTranslatorRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;

public class BetaToRelease {
    private final AbstractBootstrap bootstrap;
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private final BetaToRelease instance;

    @Getter
    private final BetaToModernTranslatorRegistry betaToModernTranslatorRegistry;

    @Getter
    private final ModernToBetaTranslatorRegistry modernToBetaTranslatorRegistry;

    @Getter
    private final SessionRegistry sessionRegistry;

    public BetaToRelease(AbstractBootstrap bootstrap) {
        long startTime = System.nanoTime();

        this.bootstrap = bootstrap;
        this.instance = this;

        this.sessionRegistry = new SessionRegistry();
        this.betaToModernTranslatorRegistry = new BetaToModernTranslatorRegistry();
        this.modernToBetaTranslatorRegistry = new ModernToBetaTranslatorRegistry();

        BetaLib.inject(MinecraftVersion.B_1_7_3); //TODO: multi-version support
        new B1_7(betaToModernTranslatorRegistry, modernToBetaTranslatorRegistry);

        PreFlatteningData.setInstance(this);
        PreFlatteningData.loadMappings();

        long endTime = System.nanoTime();
        getLogger().info("Ready for connections (" + ((endTime - startTime) / 1000000L) + "ms)");

        bind(B2RConfiguration.bindAddress, B2RConfiguration.bindPort);
    }

    public AbstractLogger getLogger() {
        return bootstrap.getAppLogger();
    }

    private void bind(String address, int port) {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel channel) {
                        channel.pipeline().addLast("mc_pipeline", new PipelineFactory(instance));
                        channel.pipeline().addLast("user_session", new ServerSession(instance, channel));
                    }
                })
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        ChannelFuture future;
        try {
            future = b.bind(address, port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            getLogger().error("address already in use: " + e.getLocalizedMessage());
            stop();
        }
    }

    public void stop() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();

        sessionRegistry.getSessions().clear();
        PreFlatteningData.cleanup();
    }
}
