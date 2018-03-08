package msgserver.handler.channel;

import base.pdu.PduBase;
import base.pdu.PduHandler;
import base.pdu.PduHeader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import msgserver.global.MsgGlobal;
import org.apache.log4j.Logger;

/**
 * 消息接入的handler
 * Created by Administrator on 2016/12/8 0008.
 */
public class CMsgServerInBaseHandler extends ChannelInboundHandlerAdapter {
	private static Logger logger = Logger.getLogger(CMsgServerInBaseHandler.class);

	public CMsgServerInBaseHandler() {
		// 初始化HandlerMap
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/*		// msg是经过PduDecoder从字节流中封装成的PduBase包
		PduBase pduBase = (PduBase) msg;
		PduHeader header = pduBase.getHeader();
		PduHandler handler = MsgGlobal.handlerMap.get((int) header.getCommandId());
		if (handler != null) {
			handler.handlePdu(pduBase);
		} else {
			logger.info("没有找到合适的handler");
		}*/
		logger.info("响应 client");
		ByteBuf result = (ByteBuf) msg;
		byte[] result1 = new byte[result.readableBytes()];
		// msg中存储的是ByteBuf类型的数据，把数据读取到byte[]中
		result.readBytes(result1);
		String resultStr = new String(result1);
		// 接收并打印客户端的信息
		System.out.println("Client said:" + resultStr);
		// 释放资源，这行很关键
		result.release();

		// 向客户端发送消息
		String response = "I am ok!";
		// 在当前场景下，发送的数据必须转换成ByteBuf数组
		ByteBuf encoded = ctx.alloc().buffer(4 * response.length());
		encoded.writeBytes(response.getBytes());
		ctx.write(encoded);
		ctx.flush();
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("server channelReadComplete..");
		// 第一种方法：写一个空的buf，并刷新写出区域。完成后关闭sock channel连接。
		ctx.writeAndFlush("server write over").addListener(ChannelFutureListener.CLOSE);
		//ctx.flush(); // 第二种方法：在client端关闭channel连接，这样的话，会触发两次channelReadComplete方法。
		//ctx.flush().close().sync(); // 第三种：改成这种写法也可以，但是这中写法，没有第一种方法的好。
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
}
