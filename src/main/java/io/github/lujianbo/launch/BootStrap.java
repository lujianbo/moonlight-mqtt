package io.github.lujianbo.launch;

/**
 * 启动器
 */
public class BootStrap {




    public static void main(String[] args) {


    }

    private void registerHook(){

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {


        }));
    }

}
