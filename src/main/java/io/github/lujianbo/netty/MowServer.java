package io.github.lujianbo.netty;

import io.github.lujianbo.netty.handler.MowServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;


public class MowServer {

    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    private ChannelInitializer<SocketChannel> initializer;
    /**
     * SSL支持
     * */
    public static final boolean SSL = System.getProperty("ssl") != null;

    static final int PORT = Integer.parseInt(System.getProperty("port", SSL? "8443" : "8080"));

    public MowServer(ChannelInitializer<SocketChannel> initializer){
        this.initializer=initializer;
    }

    public void start() {
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(initializer);
            b.bind(PORT).sync().channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void stop(){
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public static void main(String[] args) {
       try {
           final SslContext sslCtx;
           if (SSL) {
               SelfSignedCertificate ssc = new SelfSignedCertificate();
               sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
           } else {
               sslCtx = null;
           }
           MowServer proxyServer=new MowServer(new MowServerInitializer(sslCtx));
           proxyServer.start();
       } catch (CertificateException | SSLException e) {
           e.printStackTrace();
       }
    }
}
