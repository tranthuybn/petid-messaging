//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package vn.supperapp.apigw.messaging.process;

import java.lang.Thread.State;
import java.util.Hashtable;
import java.util.Iterator;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.StandardMBean;

public class ProcessManagerMX extends StandardMBean implements ProcessManagerMBean {
    private static ProcessManagerMX instance;
    private final Hashtable<Integer, ProcessThread> mmProcesses = new Hashtable();

    private ProcessManagerMX() throws NotCompliantMBeanException {
        super(ProcessManagerMBean.class);

        try {
            MMbeanServer.getInstance().registerMBean(this, new ObjectName("Tools:name=ProcessManagerMX"));
        } catch (Exception var2) {
            Log.warn("Register JMX error", var2);
        }

    }

    public static synchronized ProcessManagerMX getInstance() throws NotCompliantMBeanException {
        if (instance == null) {
            instance = new ProcessManagerMX();
        }

        return instance;
    }

    public void addMmProcess(ProcessThread process) {
        if (process != null) {
            synchronized(this.mmProcesses) {
                this.mmProcesses.put(process.getId(), process);
            }
        }

    }

    public ProcessThread getMmProcess(Integer pid) {
        synchronized(this.mmProcesses) {
            return (ProcessThread)this.mmProcesses.get(pid);
        }
    }

    public void kill(Integer pid) {
        synchronized(this.mmProcesses) {
            ProcessThread process = (ProcessThread)this.mmProcesses.get(pid);
            if (process != null) {
                process.stop();
                process = null;
            }

            this.mmProcesses.remove(pid);
        }
    }

    public void cleanup() {
        Iterator i$ = this.mmProcesses.values().iterator();

        while(i$.hasNext()) {
            ProcessThread process = (ProcessThread)i$.next();
            if (process.getState() == State.TERMINATED) {
                this.stop(process.getId());
            }
        }

        this.mmProcesses.clear();
    }

    public Hashtable<Integer, ProcessThread> getMmProcess() {
        return this.mmProcesses;
    }

    public String getProcessState(Integer pid) {
        ProcessThread process = this.getMmProcess(pid);
        return process != null ? process.toString() : "process with pid " + pid + " not exist";
    }

    public String listProcess() {
        StringBuilder sb = new StringBuilder("\n[MmProcess List]");
        Iterator i$ = this.mmProcesses.values().iterator();

        while(i$.hasNext()) {
            ProcessThread process = (ProcessThread)i$.next();
            sb.append("\n" + process.toString());
        }

        sb.append("\n[End List]");
        return sb.toString();
    }

    public void unManageProcess(Integer pid) {
        synchronized(this.mmProcesses) {
            this.mmProcesses.remove(pid);
        }
    }

    public void stop(Integer pid) {
        synchronized(this.mmProcesses) {
            ProcessThread process = (ProcessThread)this.mmProcesses.get(pid);
            if (process != null) {
                process.stop();
                process = null;
            }

        }
    }

    public void stopAll() {
        Integer[] ids = (Integer[])this.mmProcesses.keySet().toArray(new Integer[this.mmProcesses.size()]);
        Integer[] arr$ = ids;
        int len$ = ids.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Integer i = arr$[i$];
            ProcessThread process = (ProcessThread)this.mmProcesses.get(i);
            if (process != null) {
                process.stop();
                process = null;
            }
        }

    }

    protected String getDescription(MBeanInfo info) {
        return "Tiến trình quản lý các Mm Process";
    }

    protected String getDescription(MBeanOperationInfo info) {
        MBeanParameterInfo[] params = info.getSignature();
        String[] signature = new String[params.length];

        for(int i = 0; i < params.length; ++i) {
            signature[i] = params[i].getType();
        }

        if (info.getName().equals("cleanup")) {
            return "cleanup deadth process on system";
        } else if (info.getName().equals("getProcessState")) {
            return "Trạng thái của internal thread";
        } else if (info.getName().equals("kill")) {
            return "Hủy tiến trình MmProcess, yêu cần tiến trình dừng, nếu hết thời gian timeout, sẽ kill";
        } else if (info.getName().equals("stop")) {
            return "Stop tiến trình MmProcess";
        } else if (info.getName().equals("stopAll")) {
            return "Dừng tất cả các tiến trình xử lý nghiệp vụ";
        } else if (info.getName().equals("resume")) {
            return "Tiếp tục chạy tiến trình nếu đang suspend";
        } else if (info.getName().equals("suspend")) {
            return "Suspend tiến trình";
        } else {
            return info.getName().equals("listProcess") ? "Liệt kê trạng thái các tiến trình MmProcess trong hệ thống" : null;
        }
    }

    protected String getDescription(MBeanOperationInfo op, MBeanParameterInfo param, int sequence) {
        if (!op.getName().equals("getProcessState") && !op.getName().equals("kill") && !op.getName().equals("listProcess") && !op.getName().equals("resume") && !op.getName().equals("stop") && !op.getName().equals("suspend") && !op.getName().equals("start")) {
            return null;
        } else {
            switch (sequence) {
                case 0:
                    return "Pid của MmProcess cần thao tác";
                default:
                    return null;
            }
        }
    }

    protected String getParameterName(MBeanOperationInfo op, MBeanParameterInfo param, int sequence) {
        if (!op.getName().equals("getProcessState") && !op.getName().equals("kill") && !op.getName().equals("listProcess") && !op.getName().equals("resume") && !op.getName().equals("stop") && !op.getName().equals("suspend") && !op.getName().equals("start")) {
            return null;
        } else {
            switch (sequence) {
                case 0:
                    return "process id";
                default:
                    return null;
            }
        }
    }
}
