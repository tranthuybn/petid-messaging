package vn.supperapp.apigw.messaging.process;

public interface ProcessManagerMBean {
    void stop(Integer var1);

    String listProcess();

    String getProcessState(Integer var1);

    void kill(Integer var1);

    void cleanup();

    void stopAll();
}
