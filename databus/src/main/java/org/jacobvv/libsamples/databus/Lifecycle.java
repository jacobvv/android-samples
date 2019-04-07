package org.jacobvv.libsamples.databus;

/**
 * 生命周期回调接口
 * @author jacob
 * @date 19-3-29
 */
public interface Lifecycle {

    /**
     * 对应Activity的onCreate
     * @param hash 对应Activity的hashcode
     */
    void onCreate(int hash);

    /**
     * 对应Activity的onStart
     * @param hash 对应Activity的hashcode
     */
    void onStart(int hash);

    /**
     * 对应Activity的onResume
     * @param hash 对应Activity的hashcode
     */
    void onResume(int hash);

    /**
     * 对应Activity的onPause
     * @param hash 对应Activity的hashcode
     */
    void onPause(int hash);

    /**
     * 对应Activity的onStop
     * @param hash 对应Activity的hashcode
     */
    void onStop(int hash);

    /**
     * 对应Activity的onDestroy
     * @param hash 对应Activity的hashcode
     */
    void onDestroy(int hash);
}
