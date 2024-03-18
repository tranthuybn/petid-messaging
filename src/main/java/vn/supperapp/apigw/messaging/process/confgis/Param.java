//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package vn.supperapp.apigw.messaging.process.confgis;

import java.io.Serializable;
import javax.swing.JComponent;

public abstract class Param implements Serializable {
    private String name = "";
    private boolean readOnly = false;

    public abstract String getValue();

    public Param() {
    }

    public Param(String name) {
        this.name = name;
        this.readOnly = false;
    }

    public Param(String name, boolean readOnly) {
        this.name = name;
        this.readOnly = readOnly;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isReadOnly() {
        return this.readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String toString() {
        return "Param name: " + this.name + "\nClass: " + this.getClass().getName();
    }

    public abstract boolean isDifferent(Param var1);

    public abstract JComponent getComponent();

    public abstract Param getCopy();
}
