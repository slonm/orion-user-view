/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package orion.orionuserview.utils;

import java.sql.Driver;

/**
 *
 * @author sl
 */
public class DriverDef {
    private final String name;
    private final String className;
    private final String urlFormat;
    private Driver driver;

    public DriverDef(String name, String className, String urlFormat) {
        this.name = name;
        this.className = className;
        this.urlFormat = urlFormat;
    }

    public String getClassName() {
        return className;
    }

    public String getName() {
        return name;
    }

    public String getURLFormat() {
        return urlFormat;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
