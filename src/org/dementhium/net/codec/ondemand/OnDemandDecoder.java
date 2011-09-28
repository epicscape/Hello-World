package org.dementhium.net.codec.ondemand;

import org.dementhium.cache.CacheManager;
import org.dementhium.net.message.Message;
import org.dementhium.util.DementhiumThreadFactory;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class OnDemandDecoder extends FrameDecoder {

    private static ExecutorService worker = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new DementhiumThreadFactory("JS5-Worker"));

    @Override
    protected Object decode(ChannelHandlerContext ctx, final Channel channel, ChannelBuffer buffer) throws Exception {
        while (buffer.readableBytes() >= 4) {
            final int priority = buffer.readByte() & 0xFF;
            final int container = buffer.readByte() & 0xFF;
            final int file = buffer.readShort() & 0xFFFF;
            switch (priority) {
                case 1:
                case 0:
                    worker.submit(new Runnable() {
                        @Override
                        public void run() {
                            if (channel.isConnected()) {
								Message response = CacheManager.generateFile(container, file, priority);
								if (response != null) {
									channel.write(response);
								}
                            }
                        }
                    });
                    break;
            }
        }
        return null;
    }

}
