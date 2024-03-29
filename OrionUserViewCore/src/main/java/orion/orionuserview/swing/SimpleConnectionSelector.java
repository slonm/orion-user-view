/*
 * ConnectionSelector.java
 *
 * Created on Oct 11, 2010, 4:11:58 PM
 */
package orion.orionuserview.swing;

import java.sql.*;
import java.util.*;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import orion.orionuserview.DatabaseDef;
import orion.orionuserview.DatabaseDefFactory;
import orion.orionuserview.utils.DriverDef;
import orion.orionuserview.utils.DriversUtils;

/**
 *
 * @author sl
 */
public class SimpleConnectionSelector extends javax.swing.JPanel {

    private final static Map<String, Boolean> booleanValues = new HashMap<String, Boolean>();
    private Properties driverProperties = new Properties();
    private Globals globals;
    private static final ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(SimpleConnectionSelector.class);

    /** Creates new form ConnectionSelector */
    public SimpleConnectionSelector(Globals globals) {
        this.globals = globals;
        initComponents();
        driverChanged();
        //----Begin Debug Block----
        _URLControl.setText("jdbc:firebirdsql:172.16.1.1/3050:zismg?lc_ctype=WIN1251");
//        _URLControl.setText("jdbc:firebirdsql:172.16.1.4/3050:/var/lib/firebird/2.1/data/ouv.fdb");
        userControl.setText("SYSDBA");
        passwordControl.setText("ntktajy");
//        _URLControl.setText("jdbc:sqlserver://172.16.1.2;databaseName=abo2010");
//        userControl.setText("zav");
//        passwordControl.setText("zav");
        //----End Debug Block----
        globals.addHint("Настройте подключение к базе данных");
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        driverControl = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        _URLControl = new javax.swing.JTextField();
        userControl = new javax.swing.JTextField();
        passwordControl = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        driverControl.setModel(new javax.swing.DefaultComboBoxModel(DriversUtils.getAvailableDriverNames().toArray()));
        driverControl.setName("driverControl"); // NOI18N
        driverControl.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                driverControlItemStateChanged(evt);
            }
        });

        jLabel1.setText("Driver");
        jLabel1.setName("jLabel1"); // NOI18N

        _URLControl.setName("_URLControl"); // NOI18N

        userControl.setName("UserControl"); // NOI18N

        passwordControl.setName("PasswordControl"); // NOI18N

        jLabel3.setText("URL");
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setText("User");
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel5.setText("Password");
        jLabel5.setName("jLabel5"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(SimpleConnectionSelector.class, this);
        jButton1.setAction(actionMap.get("testConnection")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        jButton2.setAction(actionMap.get("connect")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N

        jButton3.setAction(actionMap.get("disconnect")); // NOI18N
        jButton3.setName("jButton3"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(driverControl, 0, 464, Short.MAX_VALUE)
                            .addComponent(userControl, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
                            .addComponent(_URLControl, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
                            .addComponent(passwordControl, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 200, Short.MAX_VALUE)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(driverControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(_URLControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 254, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void driverControlItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_driverControlItemStateChanged
        driverChanged();
}//GEN-LAST:event_driverControlItemStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField _URLControl;
    private javax.swing.JComboBox driverControl;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField passwordControl;
    private javax.swing.JTextField userControl;
    // End of variables declaration//GEN-END:variables

    private void driverChanged() {
        DriverDef dd = DriversUtils.getDriverDefs().get((String) driverControl.getSelectedItem());
        _URLControl.setText(dd.getURLFormat());
    }

    private void prepareDriverProperties() {
        driverProperties.setProperty("user", userControl.getText());
        driverProperties.setProperty("password", passwordControl.getText());
    }

    @Action
    public void testConnection() {
        prepareDriverProperties();
        try {
            DriverManager.getConnection(_URLControl.getText(), driverProperties);
            globals.addInfo(resourceMap.getString("MESSAGE.TestConnectonPassed"));
        } catch (Exception ex) {
            //TODO сделать более информативно
            globals.addError(resourceMap.getString("MESSAGE.TestConnectonFiled"));
        }
    }

    @Action
    public void connect() {
        prepareDriverProperties();
        try {
            DatabaseDef dd = DatabaseDefFactory.build(_URLControl.getText(), driverProperties);
        //----Begin Debug Block----
            dd.getHiddenAttrPatterns().add("CODE|Code|code");
            dd.getHiddenAttrPatterns().add("HASFILLED_NAME");
            dd.getHiddenAttrPatterns().add("HASFILLED_ROLE");
            dd.getHiddenAttrPatterns().add("ID");
            dd.getHiddenAttrPatterns().add("FILLER");
            dd.getHiddenAttrPatterns().add("INTERNAL_NAME");
        //----End Debug Block----
            //TODO индицировать выполняющуюся операцию
            dd.connect();
            globals.setDatabaseDef(dd);
            globals.addInfo(resourceMap.getString("MESSAGE.ConnectionSuccesful"));
        } catch (SQLException ex) {
            //TODO сделать более информативно
            globals.addError(resourceMap.getString("MESSAGE.ConnectionFiled"));
        }
    }

    @Action
    public void disconnect() {
        try {
            globals.getDatabaseDef().getConnection().close();
        } catch (Exception ex) {
            globals.addError(resourceMap.getString("MESSAGE.ConnectionFiled"));
        }
    }

}
