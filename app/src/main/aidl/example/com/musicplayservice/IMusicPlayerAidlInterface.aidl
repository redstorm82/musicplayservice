// IMusicPlayerAidlInterface.aidl
package example.com.musicplayservice;

// Declare any non-default types here with import statements

interface IMusicPlayerAidlInterface {
    void start(int id,boolean loop);
    void stop();
    void pause();
    void resume();
    void reset();
}
