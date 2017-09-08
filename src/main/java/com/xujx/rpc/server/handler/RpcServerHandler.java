package com.xujx.rpc.server.handler;

import com.xujx.rpc.dto.request.RpcRequest;
import com.xujx.rpc.dto.response.RpcResponse;
import io.netty.channel.*;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created by xujinxin on 2017/9/7.
 * 消息处理器： 获取请求 cglib动态调用
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServerHandler.class);
    private final Map<String, Object> handlerMap;

    public RpcServerHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest request) throws Exception {
        System.out.println("======================");
        RpcResponse response = new RpcResponse();
        try{
            Object result = handle(request);
            response.setResult(result);
        }catch (Exception e){
            response.setError(e.toString());
        }

        channelHandlerContext.writeAndFlush(response);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("服务器捕获异常,{}", cause);
        ctx.close();
    }

    private Object handle(RpcRequest request) throws InvocationTargetException {

        String className= request.getClassName();
        /*
         * 获取spring托管的单例
         */
        Object bean = handlerMap.get(className);

        /*
         * 获取对象的class对象
         */
        Class<?> clazz = bean.getClass();

        /*
         * 获取远程调用的方法名
         */
        String methodName= request.getMethodName();

        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        /*
         * 通过cglib反射动态调用指定的方法
         */
        FastClass fastClass = FastClass.create(clazz);
        FastMethod fastMethod = fastClass.getMethod(methodName,parameterTypes);
        return fastMethod.invoke(bean,parameters);
    }
}
