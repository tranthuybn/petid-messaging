package vn.supperapp.apigw.messaging.process;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationEmitter;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import vn.supperapp.apigw.messaging.process.confgis.Configuration;

public abstract class ProcessThreadMX extends ProcessThread implements DynamicMBean, NotificationEmitter {
    public static final String TYPE = "process.status";
    protected String dClassName = this.getClass().getName();
    protected String dDescription = null;
    protected String objectName = "";
    protected MBeanAttributeInfo[] dAttributes;
    protected MBeanConstructorInfo[] dConstructors;
    protected MBeanOperationInfo[] dOperations;
    protected MBeanNotificationInfo[] dNotifications;
    protected MBeanInfo dMBeanInfo;
    protected NotificationBroadcasterSupport notificationHandler;
    protected ArrayList<ProcessThreadMX> children = null;
    private String configFile = "";

    public ProcessThreadMX(String threadName) throws NotCompliantMBeanException {
        super(threadName);
        this.children = new ArrayList();
        this.buildDynamicMBeanInfo();
        this.notificationHandler = new NotificationBroadcasterSupport();
    }

    public ProcessThreadMX(String threadName, String description) throws NotCompliantMBeanException {
        super(threadName);
        this.children = new ArrayList();
        this.dDescription = description;
        this.buildDynamicMBeanInfo();
        this.notificationHandler = new NotificationBroadcasterSupport();
    }

    public void start() {
        super.start();
        Notification notification = new Notification("process.status", this.getProcessStatus(), 0L, this.getThreadName() + " started");
        this.notificationHandler.sendNotification(notification);
    }

    public void stop() {
        super.stop();
        Notification notification = new Notification("process.status", this.getProcessStatus(), 0L, this.getThreadName() + " stopped");
        this.notificationHandler.sendNotification(notification);
    }

    /** @deprecated */
    public String loadParams() {
        return "";
    }

    /** @deprecated */
    public String saveParams(String newConfig) {
        return "";
    }

    public Configuration loadConfig() {
        return null;
    }

    public boolean saveConfig(Configuration newConfig) {
        return false;
    }

    public void removeNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback) throws ListenerNotFoundException {
        this.notificationHandler.removeNotificationListener(listener, filter, handback);
    }

    public void addNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback) {
        this.notificationHandler.addNotificationListener(listener, filter, handback);
    }

    public MBeanNotificationInfo[] getNotificationInfo() {
        return this.dNotifications;
    }

    public void removeNotificationListener(NotificationListener listener) throws ListenerNotFoundException {
        this.notificationHandler.removeNotificationListener(listener);
    }

    protected void registerAgent(String objName) throws MalformedObjectNameException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        this.logger.info("register MBeanServer:" + objName);
        MBeanServer mbs = MMbeanServer.getInstance();
        ObjectName mbeanName = new ObjectName(objName);
        mbs.registerMBean(this, mbeanName);
        this.objectName = objName;
        this.logger = Logger.getLogger(objName);
    }



    public Object getAttribute(String attributeName) throws AttributeNotFoundException, MBeanException, ReflectionException {
        if (attributeName == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"), "Cannot invoke a getter of " + this.dClassName + " with null attribute name");
        } else if (attributeName.equals("ThreadName")) {
            return this.getThreadName();
        } else if (attributeName.equals("Running")) {
            return this.isRunning();
        } else if (attributeName.equals("Status")) {
            return this.getProcessStatus();
        } else if (attributeName.equals("Priority")) {
            return this.getPriority();
        } else if (attributeName.equals("pingable")) {
            return this.pingable();
        } else {
            throw new AttributeNotFoundException("Cannot find " + attributeName + " attribute in " + this.dClassName);
        }
    }

    public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
        if (attribute == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute cannot be null"), "Cannot invoke a setter of " + this.dClassName + " with null attribute");
        } else {
            String name = attribute.getName();
            Object value = attribute.getValue();
            if (name == null) {
                throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"), "Cannot invoke the setter of " + this.dClassName + " with null attribute name");
            } else {
                if (name.equals("Priority")) {
                    try {
                        if (!Class.forName("java.lang.Integer").isAssignableFrom(value.getClass())) {
                            throw new InvalidAttributeValueException("Cannot set attribute " + name + " to a " + value.getClass().getName() + " object, String expected");
                        }

                        this.setPriority((Integer)value);
                    } catch (ClassNotFoundException var5) {
                        this.logger.error("Error in SetAttribue");
                        this.logger.error(var5);
                    }
                } else {
                    if (!name.equals("ThreadName")) {
                        if (name.equals("Running")) {
                            throw new AttributeNotFoundException("Cannot set attribute " + name + " because it is read-only");
                        }

                        if (name.equals("Status")) {
                            throw new AttributeNotFoundException("Cannot set attribute " + name + " because it is read-only");
                        }

                        if (name.equals("pingable")) {
                            throw new AttributeNotFoundException("Cannot set attribute " + name + " because it is read-only");
                        }

                        throw new AttributeNotFoundException("Attribute " + name + " not found in " + this.getClass().getName());
                    }

                    try {
                        if (!Class.forName("java.lang.String").isAssignableFrom(value.getClass())) {
                            throw new InvalidAttributeValueException("Cannot set attribute " + name + " to a " + value.getClass().getName() + " object, String expected");
                        }

                        this.setThreadName((String)value);
                    } catch (ClassNotFoundException var6) {
                        this.logger.error("Error in SetAttribue");
                        this.logger.error(var6);
                    }
                }

            }
        }
    }

    public AttributeList getAttributes(String[] attributeNames) {
        if (attributeNames == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("attributeNames[] cannot be null"), "Cannot invoke a getter of " + this.dClassName);
        } else {
            AttributeList resultList = new AttributeList();
            if (attributeNames.length == 0) {
                return resultList;
            } else {
                for(int i = 0; i < attributeNames.length; ++i) {
                    try {
                        Object value = this.getAttribute(attributeNames[i]);
                        resultList.add(new Attribute(attributeNames[i], value));
                    } catch (AttributeNotFoundException var5) {
                        this.logger.error("Error in Get Attributes");
                        this.logger.error(var5);
                    } catch (MBeanException var6) {
                        this.logger.error("Error in Get Attributes");
                        this.logger.error(var6);
                    } catch (ReflectionException var7) {
                        this.logger.error("Error in Get Attributes");
                        this.logger.error(var7);
                    }
                }

                return resultList;
            }
        }
    }

    public AttributeList setAttributes(AttributeList attributes) {
        if (attributes == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("AttributeList attributes cannot be null"), "Cannot invoke a setter of " + this.dClassName);
        } else {
            AttributeList resultList = new AttributeList();
            if (attributes.isEmpty()) {
                return resultList;
            } else {
                Iterator i = attributes.iterator();

                while(i.hasNext()) {
                    Attribute attr = (Attribute)i.next();

                    try {
                        this.setAttribute(attr);
                        String name = attr.getName();
                        Object value = this.getAttribute(name);
                        resultList.add(new Attribute(name, value));
                    } catch (AttributeNotFoundException var7) {
                        this.logger.error("Error in SetAttributes");
                        this.logger.error(var7);
                    } catch (InvalidAttributeValueException var8) {
                        this.logger.error("Error in SetAttributes");
                        this.logger.error(var8);
                    } catch (MBeanException var9) {
                        this.logger.error("Error in SetAttributes");
                        this.logger.error(var9);
                    } catch (ReflectionException var10) {
                        this.logger.error("Error in SetAttributes");
                        this.logger.error(var10);
                    }
                }

                return resultList;
            }
        }
    }

    public Object invoke(String operationName, Object[] params, String[] signature) throws MBeanException, ReflectionException {
        if (operationName == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Operation name cannot be null"), "Cannot invoke a null operation in " + this.dClassName);
        } else if (operationName.equals("start")) {
            this.start();
            return null;
        } else if (operationName.equals("stop")) {
            this.stop();
            return null;
        } else if (operationName.equals("restart")) {
            this.restart();
            return null;
        } else if (operationName.equals("readConfig")) {
            return this.readConfig();
        } else if (operationName.equals("addChild")) {
            this.addChild((String)params[0], (String)params[1], (String)params[2], (String)params[3], (String)params[4]);
            return null;
        } else if (operationName.equals("getInfor")) {
            return this.getInfor();
        } else if (operationName.equals("ping")) {
            return this.ping();
        } else if (operationName.equals("loadParams")) {
            return this.loadParams();
        } else if (operationName.equals("saveParams")) {
            String newConfig = (String)params[0];
            return this.saveParams(newConfig);
        } else if (operationName.equals("setDump")) {
            Boolean b = (Boolean)params[0];
            this.setDumpThread(b);
            return null;
        } else if (operationName.equals("loadLoggerName")) {
            return this.loadLoggerName();
        } else if (operationName.equals("loadConfig")) {
            return this.loadConfig();
        } else if (operationName.equals("saveConfig")) {
            Configuration config = (Configuration)params[0];
            return this.saveConfig(config);
        } else {
            throw new ReflectionException(new NoSuchMethodException(operationName), "Cannot find the operation " + operationName + " in " + this.dClassName);
        }
    }

    public MBeanInfo getMBeanInfo() {
        return this.dMBeanInfo;
    }

    protected MBeanAttributeInfo[] buildAttributes() {
        ArrayList<MBeanAttributeInfo> v = new ArrayList();
        v.add(new MBeanAttributeInfo("ThreadName", "java.lang.String", "The name of thread", true, true, false));
        v.add(new MBeanAttributeInfo("Status", "java.lang.Integer", "The status of thread, 0 - running 1-stopping 2- stopped", true, false, false));
        v.add(new MBeanAttributeInfo("Running", "java.lang.Boolean", "The running status", true, false, true));
        v.add(new MBeanAttributeInfo("Priority", "java.lang.Integer", "Priority of thread value range 1-10", true, true, false));
        v.add(new MBeanAttributeInfo("pingable", "java.lang.Boolean", "Get pingable property of thread", true, false, true));
        return (MBeanAttributeInfo[])v.toArray(new MBeanAttributeInfo[v.size()]);
    }

    protected MBeanOperationInfo[] buildOperations() {
        ArrayList<MBeanOperationInfo> v = new ArrayList();
        MBeanParameterInfo[] params = new MBeanParameterInfo[0];
        v.add(new MBeanOperationInfo("start", "start service", params, "void", 1));
        v.add(new MBeanOperationInfo("stop", "stop service", params, "void", 1));
        v.add(new MBeanOperationInfo("restart", "stop service", params, "void", 1));
        v.add(new MBeanOperationInfo("getInfor", "get configuration information and runtime state of this service", params, "java.lang.String", 1));
        v.add(new MBeanOperationInfo("ping", "get the period between the time ping() called and the lastest transation start time", params, "java.lang.Long", 1));
        v.add(new MBeanOperationInfo("loadLoggerName", "get logger name of services", params, "java.lang.String", 1));
        v.add(new MBeanOperationInfo("readConfig", "get Document represent the config file for create new child Thread", params, "org.w3c.dom.Document", 1));
        params = new MBeanParameterInfo[]{new MBeanParameterInfo("childName", "java.lang.String", "Child's name that in form of key=value"), new MBeanParameterInfo("className", "java.lang.String", "An extend class of ProcessThreadMX class that is used to create an instance"), new MBeanParameterInfo("constructParam", "java.lang.String", "The String that is used to provide parammeters for constructing new instance of className"), new MBeanParameterInfo("variable", "java.lang.String", "variable assigned to new instance"), new MBeanParameterInfo("followingThread", "java.lang.String", "Name of thread that run following this child")};
        v.add(new MBeanOperationInfo("addChild", "create a ProcessThreadMX thread that is assigned to be a child of this thread", params, "void", 1));
        params = new MBeanParameterInfo[]{new MBeanParameterInfo("dump", "java.lang.Boolean", "boolean value indicate track thread or not")};
        v.add(new MBeanOperationInfo("setDump", "set dumping status of current thread", params, "void", 1));
        params = new MBeanParameterInfo[0];
        v.add(new MBeanOperationInfo("loadParams", "load params", params, "java.lang.String", 1));
        params = new MBeanParameterInfo[]{new MBeanParameterInfo("newConfig", "java.lang.String", "The String express new config")};
        v.add(new MBeanOperationInfo("saveParams", "save params", params, "java.lang.String", 1));
        params = new MBeanParameterInfo[0];
        v.add(new MBeanOperationInfo("loadConfig", "load Configuration", params, "vn.supperapp.apigw.messaging.process.confgis", 1));
        params = new MBeanParameterInfo[]{new MBeanParameterInfo("newConfig", "vn.supperapp.apigw.messaging.process.confgis", "new configuration")};
        v.add(new MBeanOperationInfo("saveConfig", "save Configuration", params, "java.lang.Boolean", 1));
        return (MBeanOperationInfo[])v.toArray(new MBeanOperationInfo[v.size()]);
    }

    protected MBeanConstructorInfo[] buildConstructors() {
        ArrayList<MBeanConstructorInfo> v = new ArrayList();
        Constructor[] constructors = this.getClass().getConstructors();
        if (constructors != null && constructors.length > 0) {
            v.add(new MBeanConstructorInfo("Constructs a service object", constructors[0]));
        }

        return (MBeanConstructorInfo[])v.toArray(new MBeanConstructorInfo[v.size()]);
    }

    protected MBeanNotificationInfo[] buildNotifications() {
        this.dNotifications = new MBeanNotificationInfo[1];
        this.dNotifications[0] = new MBeanNotificationInfo(new String[]{"Process Status"}, Notification.class.getName(), "notification when the status of process changed");
        return this.dNotifications;
    }

    protected void buildDynamicMBeanInfo() {
        if (this.dDescription == null) {
            this.dDescription = "The process which run in a separator thread";
        }

        this.dAttributes = this.buildAttributes();
        this.dConstructors = this.buildConstructors();
        this.dOperations = this.buildOperations();
        this.dNotifications = this.buildNotifications();
        this.dMBeanInfo = new MBeanInfo(this.dClassName, this.dDescription, this.dAttributes, this.dConstructors, this.dOperations, this.dNotifications);
    }

    protected void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    protected Document readConfig() {
        if (this.configFile.equals("")) {
            return null;
        } else {
            try {
                File file = new File(this.configFile);
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(file);
                return doc;
            } catch (Exception var5) {
                this.logger.warn(var5.toString());
                return null;
            }
        }
    }

    protected void addChild(String childName, String className, String constructParam, String variable, String followingThread) {
    }
}
