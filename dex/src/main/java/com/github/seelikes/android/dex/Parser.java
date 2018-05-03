package com.github.seelikes.android.dex;

public interface Parser {
    /**
     * 基础包
     * 只有在此包下的类才会参与加载以作进一步的判定
     * @param basePackage 基础包
     * @return U know
     */
    Parser basePackage(String basePackage);

    /**
     * 是否使用了instantRun
     * @param instantRun true 使用了instantRun false 没有使用instantRun
     * @return U know
     */
    Parser instantRun(boolean instantRun);

    /**
     * 遍历资源包中的类
     * @param checker 用于接收通过的类
     * @return U know
     */
    Parser getClass(Action<Class<?>> checker);
}
