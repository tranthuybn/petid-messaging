package vn.supperapp.apigw.messaging.apps;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import io.dropwizard.Configuration;
import io.dropwizard.bundles.assets.AssetsBundleConfiguration;
import io.dropwizard.bundles.assets.AssetsConfiguration;
import io.dropwizard.db.DataSourceFactory;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Map;

public class EApplicationConfiguration extends Configuration implements AssetsBundleConfiguration {
    @NotEmpty
    @JsonProperty
    private String appName;

    @NotEmpty
    @JsonProperty
    private String[] controllerPackages;

    @NotNull
    private Map<String, Map<String, String>> viewRendererConfiguration = Collections.emptyMap();

    @Valid
    @NotNull
    @JsonProperty
    private final AssetsConfiguration assets = AssetsConfiguration.builder().build();

    @Valid
    @NotNull
    private DataSourceFactory dbApp = new DataSourceFactory();
    private boolean dbAppEncrypted;

//    @Valid
//    @NotNull
//    private DataSourceFactory dbApp1 = new DataSourceFactory();

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String[] getControllerPackages() {
        return controllerPackages;
    }

    public void setControllerPackages(String[] controllerPackages) {
        this.controllerPackages = controllerPackages;
    }


    @JsonProperty("viewRendererConfiguration")
    public Map<String, Map<String, String>> getViewRendererConfiguration() {
        return viewRendererConfiguration;
    }

    @JsonProperty("viewRendererConfiguration")
    public void setViewRendererConfiguration(Map<String, Map<String, String>> viewRendererConfiguration) {
        final ImmutableMap.Builder<String, Map<String, String>> builder = ImmutableMap.builder();
        for (Map.Entry<String, Map<String, String>> entry : viewRendererConfiguration.entrySet()) {
            builder.put(entry.getKey(), ImmutableMap.copyOf(entry.getValue()));
        }
        this.viewRendererConfiguration = builder.build();
    }

    @Override
    public AssetsConfiguration getAssetsConfiguration() {
        return assets;
    }

    public DataSourceFactory getDbApp() {
        return dbApp;
    }

    @JsonProperty("dbApp")
    public void setDbApp(DataSourceFactory dbApp) {
        //Init decrypted module here
        if (dbAppEncrypted) {
//            dbApp.setUrl(AppCryptUtils.decrypt(dbApp.getUrl()));
//            dbApp.setUser(AppCryptUtils.decrypt(dbApp.getUser()));
//            dbApp.setPassword(AppCryptUtils.decrypt(dbApp.getPassword()));
        }
        this.dbApp = dbApp;
    }

//    public DataSourceFactory getDbApp1() {
//        return dbApp1;
//    }
//
//    @JsonProperty("dbTest1")
//    public void setDbApp1(DataSourceFactory dbApp1) {
//        //Init decrypted module here
//        dbApp1.setUrl(AppCryptUtils.decrypt(dbApp1.getUrl()));
//        dbApp1.setUser(AppCryptUtils.decrypt(dbApp1.getUser()));
//        dbApp1.setPassword(AppCryptUtils.decrypt(dbApp1.getPassword()));
//        this.dbApp1 = dbApp1;
//    }

    public boolean isDbAppEncrypted() {
        return dbAppEncrypted;
    }

    public void setDbAppEncrypted(boolean dbAppEncrypted) {
        this.dbAppEncrypted = dbAppEncrypted;
    }
}
