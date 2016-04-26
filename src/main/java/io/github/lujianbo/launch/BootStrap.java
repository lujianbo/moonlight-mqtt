package io.github.lujianbo.launch;

import io.github.lujianbo.context.impl.DefaultContextService;
import io.github.lujianbo.context.service.ContextService;
import io.github.lujianbo.engine.core.MQTTEngine;
import io.github.lujianbo.engine.wapper.SingleSentinelEngine;
import io.github.lujianbo.netty.NettyServer;
import io.github.lujianbo.netty.handler.HandlerContext;
import io.github.lujianbo.netty.handler.MQTTServerInitializer;
import io.github.lujianbo.sentinel.handler.MQTTProtocolHandler;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

/**
 * 启动器
 */
public class BootStrap {

    /**
     * 初始化容器资源
     * */
    private ContextService contextService;

    /**
     * MQTT的核心引擎
     * */
    private MQTTEngine engine;




    public BootStrap(){
        /**
         * 默认使用内存来实现context数据存储功能
         * */
        this.contextService=new DefaultContextService();

        /**
         * 初始化引擎
         * */
        this.engine= new SingleSentinelEngine(contextService);

        /**
         * 从exporter中获取service
         * */

        /**
         * 启动sentinel
         * */
        MQTTProtocolHandler protocolHandler=new MQTTProtocolHandler(engine);

        /**
         * 配置服务
         * */
        HandlerContext context=new HandlerContext();

        /**
         * 配置主要的处理器
         * */
        context.setHandler(protocolHandler);

        MQTTServerInitializer initializer=new MQTTServerInitializer(context);
        try {
            NettyServer mowServer = new NettyServer(initializer);
            mowServer.start();
        } catch (CertificateException | SSLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new BootStrap();
    }


    private void registerHook(){

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {


        }));
    }

}
