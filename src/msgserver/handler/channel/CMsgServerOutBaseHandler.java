package msgserver.handler.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.apache.log4j.Logger;

import java.sql.Date;

/**
 * Created by Administrator on 2016/12/8 0008.
 */
public class CMsgServerOutBaseHandler extends ChannelOutboundHandlerAdapter {
    private static org.apache.log4j.Logger logger = Logger.getLogger(CMsgServerOutBaseHandler.class);
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        String currentTime = new Date(System.currentTimeMillis()).toString()+msg;
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.writeAndFlush(resp).addListener(new ChannelFutureListener(){
            @Override
            public void operationComplete(ChannelFuture future)
                    throws Exception {
                logger.info("下发成功！");
            }
        });  ;
    }
}
