package org.dementhium.net;

import org.dementhium.net.codec.DefaultGameEncoder;
import org.dementhium.net.codec.handshake.HandshakeDecoder;
import org.dementhium.net.handler.DementhiumHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.DefaultChannelPipeline;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class DementhiumPipelineFactory implements ChannelPipelineFactory {

    @Override
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = new DefaultChannelPipeline();
        pipeline.addLast("encoder", new DefaultGameEncoder());
        pipeline.addLast("decoder", new HandshakeDecoder());
        pipeline.addLast("handler", new DementhiumHandler());
        return pipeline;
    }

}
