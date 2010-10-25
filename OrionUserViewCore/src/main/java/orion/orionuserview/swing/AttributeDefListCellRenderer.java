/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.orionuserview.swing;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import orion.orionuserview.AttributeDef;
import orion.orionuserview.RelationDef;
import orion.orionuserview.RelationSourceType;
import orion.orionuserview.utils.Defense;

/**
 *
 * @author sl
 */
public class AttributeDefListCellRenderer extends DefaultListCellRenderer {

    private final Globals globals;

    public AttributeDefListCellRenderer(Globals globals) {
        this.globals = Defense.notNull(globals, "globals");
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value != null) {
            RelationDef rd = (RelationDef) value;
            rd.getRelationSourceType();
            if (RelationSourceType.VIEW.equals(rd.getRelationSourceType())) {
                setForeground(Color.GRAY);
            }
            setText(UIUtils.attributeDefPresentableNameForAdmin(globals, rd));
        }
        return this;
    }
}
