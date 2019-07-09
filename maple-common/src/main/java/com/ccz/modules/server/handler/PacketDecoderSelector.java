package com.ccz.modules.server.handler;

import com.ccz.modules.domain.constant.EProtocolHeader;
import com.ccz.modules.common.utils.StrUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class PacketDecoderSelector extends ByteToMessageDecoder {

    private WebProtocolSelector webProtocolSelector;
    private StringBasedDataServerHandler stringBasedDataServerHandler;
    private BinaryBasedDataServerHandler binaryBasedDataServerHandler;

    public PacketDecoderSelector(WebProtocolSelector webProtocolSelector, StringBasedDataServerHandler stringBasedDataServerHandler,
                                 BinaryBasedDataServerHandler binaryBasedDataServerHandler) {
        this.webProtocolSelector = webProtocolSelector;
        this.stringBasedDataServerHandler = stringBasedDataServerHandler;
        this.binaryBasedDataServerHandler = binaryBasedDataServerHandler;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ByteBuf buffer = in;
        int eol = StrUtils.findEndOfLine(buffer);
        if(eol<1)
            eol = in.readableBytes();
        byte[] dest = new byte[eol];
        buffer.getBytes(0, dest);
        EProtocolHeader protocol = EProtocolHeader.getType(new String(dest));

        ChannelPipeline pipeline = ctx.pipeline();

        if(protocol == EProtocolHeader.http) {
            pipeline.addLast(new HttpServerCodec(), new HttpObjectAggregator(65535));
            pipeline.addLast("protocolSelector", webProtocolSelector);
        }else if(protocol == EProtocolHeader.iotp1 || protocol == EProtocolHeader.iotp2){
            pipeline.addLast("delimiterBasedFrameDecoder", new DelimiterBasedFrameDecoder(65535, Delimiters.lineDelimiter()));
            pipeline.addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8));
            pipeline.addLast(stringBasedDataServerHandler);
        }else { //binary type
            pipeline.addLast("byteArrayDecoder", new ByteArrayDecoder());
            pipeline.addLast(binaryBasedDataServerHandler);
        }
        out.add(buffer);
        pipeline.remove("PacketDecoderSelector");
    }
}
