/*
 * MainView.java
 */
package orion.orionuserview.swing;

import java.awt.Color;
import javax.swing.text.BadLocationException;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import orion.orionuserview.DatabaseDef;

/**
 * The application's main frame.
 */
public class MainView extends FrameView implements Globals {

    private final ResourceMap resourceMap;
    private AttributesSelector attributesSelector;
    private RelationsSelector relationsSelector;
    private SimpleConnectionSelector connectionSelector;
    private Preferences preferences;
    private DatabaseDef databaseDef;
    private StyledDocument outputDoc;

    public MainView(SingleFrameApplication app) {
        super(app);
        resourceMap = getResourceMap();
        JFrame frame = new JFrame();
        frame.setExtendedState(JFrame.NORMAL);
        this.setFrame(frame);
        initComponents();
        //Define styles
        Style def = StyleContext.getDefaultStyleContext().
                getStyle(StyleContext.DEFAULT_STYLE);
        outputDoc = outputControl.getStyledDocument();
        Style s = outputDoc.addStyle("hint", def);
        StyleConstants.setForeground(s, Color.BLUE);
        s = outputDoc.addStyle("info", def);
        StyleConstants.setForeground(s, Color.BLACK);
        s = outputDoc.addStyle("warning", def);
        StyleConstants.setForeground(s, Color.ORANGE);
        s = outputDoc.addStyle("error", def);
        StyleConstants.setForeground(s, Color.RED);
        //end Define styles
        connectionSelector = new SimpleConnectionSelector(this);
        tabbs.add(connectionSelector);
        tabbs.setTitleAt(tabbs.getTabCount() - 1, "Соединение");
        relationsSelector = new RelationsSelector(this);
        tabbs.add(relationsSelector);
        tabbs.setTitleAt(tabbs.getTabCount() - 1, "Таблицы");
        attributesSelector = new AttributesSelector(this);
        tabbs.add(attributesSelector);
        tabbs.setTitleAt(tabbs.getTabCount() - 1, "Поля");
        preferences = new Preferences();
        tabbs.add(preferences);
        tabbs.setTitleAt(tabbs.getTabCount() - 1, "Настройки");
        // status bar initialization - message timeout, idle icon and busy animation, etc
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = SWINGApp.getApplication().getMainFrame();
            aboutBox = new AboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        SWINGApp.getApplication().show(aboutBox);
    }

    @Action(enabledProperty = "connected")
    public void switchToRelationsSelector() {
        if (relationsSelector == null) {
            relationsSelector = new RelationsSelector(this);
        }
        setComponent(relationsSelector);
        getComponent().revalidate();
    }

    @Action(enabledProperty = "connected")
    public void switchToAttributesSelector() {
        if (attributesSelector == null) {
            attributesSelector = new AttributesSelector(this);
        }
        setComponent(attributesSelector);
        getComponent().revalidate();
    }

    @Action
    public void switchToConnectionSelector() {
        if (connectionSelector == null) {
            connectionSelector = new SimpleConnectionSelector(this);
        }
        setComponent(connectionSelector);
        getComponent().revalidate();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        jScrollPane1 = new javax.swing.JScrollPane();
        outputControl = new javax.swing.JTextPane();
        tabbs = new javax.swing.JTabbedPane();

        menuBar.setName("menuBar"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(MainView.class);
        jMenu1.setText(resourceMap.getString("jMenu1.text")); // NOI18N
        jMenu1.setName("jMenu1"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(MainView.class, this);
        jMenuItem1.setAction(actionMap.get("switchToConnectionSelector")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        jMenu1.add(jMenuItem1);

        jMenuItem2.setAction(actionMap.get("switchToRelationsSelector")); // NOI18N
        jMenuItem2.setName("jMenuItem2"); // NOI18N
        jMenu1.add(jMenuItem2);

        jMenuItem3.setAction(actionMap.get("switchToAttributesSelector")); // NOI18N
        jMenuItem3.setName("jMenuItem3"); // NOI18N
        jMenu1.add(jMenuItem3);

        menuBar.add(jMenu1);

        statusPanel.setName("statusPanel"); // NOI18N
        statusPanel.setPreferredSize(new java.awt.Dimension(808, 130));

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        outputControl.setEditable(false);
        outputControl.setName("outputControl"); // NOI18N
        jScrollPane1.setViewportView(outputControl);

        org.jdesktop.layout.GroupLayout statusPanelLayout = new org.jdesktop.layout.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusPanelLayout.createSequentialGroup()
                .add(214, 214, 214)
                .add(statusMessageLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 384, Short.MAX_VALUE)
                .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(statusAnimationLabel)
                .addContainerGap())
            .add(org.jdesktop.layout.GroupLayout.TRAILING, statusPanelSeparator, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 770, Short.MAX_VALUE)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 770, Short.MAX_VALUE)
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, statusPanelLayout.createSequentialGroup()
                .add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 84, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 12, Short.MAX_VALUE)
                .add(statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(statusMessageLabel)
                    .add(statusAnimationLabel)
                    .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(12, 12, 12))
        );

        tabbs.setName("tabbs"); // NOI18N

        setComponent(tabbs);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents
    private boolean connected = false;

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean b) {
        boolean old = isConnected();
        this.connected = b;
        firePropertyChange("connected", old, isConnected());
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JTextPane outputControl;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JTabbedPane tabbs;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;

    @Override
    public DatabaseDef getDatabaseDef() {
        return databaseDef;
    }

    @Override
    public void setDatabaseDef(DatabaseDef databaseDef) {
        if (databaseDef != null) {
            setConnected(databaseDef.isConnected());
        }
        this.databaseDef = databaseDef;
    }

    @Override
    public void addHint(String message) {
        try {
            outputDoc.insertString(outputDoc.getLength(), "Hint: " + message + "\n", outputDoc.getStyle("hint"));
        } catch (BadLocationException ex) {
            System.err.println("Couldn't insert initial text into text pane.");
        }
    }

    @Override
    public void addInfo(String message) {
        try {
            outputDoc.insertString(outputDoc.getLength(), "Info: " + message + "\n", outputDoc.getStyle("info"));
        } catch (BadLocationException ex) {
            System.err.println("Couldn't insert initial text into text pane.");
        }
    }

    @Override
    public void addWarning(String message) {
        try {
            outputDoc.insertString(outputDoc.getLength(), "Warning: " + message + "\n", outputDoc.getStyle("warning"));
        } catch (BadLocationException ex) {
            System.err.println("Couldn't insert initial text into text pane.");
        }
    }

    @Override
    public void addError(String message) {
        try {
            outputDoc.insertString(outputDoc.getLength(), "Error: " + message + "\n", outputDoc.getStyle("error"));
        } catch (BadLocationException ex) {
            System.err.println("Couldn't insert initial text into text pane.");
        }
    }

    @Override
    public <T> T getParametr(Class<T> clasz, String key) {
        return preferences.getParametr(clasz, key);
    }
}
