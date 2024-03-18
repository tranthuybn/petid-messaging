package vn.supperapp.apigw.messaging.process.confgis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Configuration implements Serializable {
    private ArrayList<Param> params = new ArrayList();

    public Configuration() {
    }

    public Param getParamByName(String paramName) {
        Iterator i$ = this.params.iterator();

        Param p;
        do {
            if (!i$.hasNext()) {
                return null;
            }

            p = (Param)i$.next();
        } while(!p.getName().equals(paramName));

        return p;
    }

    public void addParams(ArrayList<Param> params) {
        this.params.addAll(params);
    }

    public void addParam(Param param) {
        this.params.add(param);
    }

    public ArrayList<Param> getParams() {
        return this.params;
    }

    public void setParams(ArrayList<Param> params) {
        this.params = params;
    }

    public String toString() {
        String r = "";

        for(Iterator i$ = this.params.iterator(); i$.hasNext(); r = r + "\n") {
            Param p = (Param)i$.next();
            r = r + p.toString();
        }

        return r;
    }

    public Configuration getCopy() {
        Configuration copy = new Configuration();
        ArrayList<Param> copyParam = new ArrayList();
        Iterator i$ = this.params.iterator();

        while(i$.hasNext()) {
            Param p = (Param)i$.next();
            copyParam.add(p.getCopy());
        }

        copy.params = copyParam;
        return copy;
    }

    public boolean isDifferent(Configuration cfg) {
        if (cfg.params.size() != this.params.size()) {
            return true;
        } else {
            for(int i = 0; i < this.params.size(); ++i) {
                Param p = (Param)this.params.get(i);
                if (p.isDifferent((Param)cfg.params.get(i))) {
                    return true;
                }
            }

            return false;
        }
    }
}
