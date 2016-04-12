package io.github.lujianbo.server;

/**
 * Created by jianbo on 2016/3/31.
 */
public abstract class AbstractLauncher {


    public AbstractLauncher() {

        /**
         * 配置shutdown的hook
         * */
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {

        }));
    }

}
