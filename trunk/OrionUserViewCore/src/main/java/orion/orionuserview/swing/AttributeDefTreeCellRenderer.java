package orion.orionuserview.swing;

import orion.orionuserview.ForeignKeyType;
import java.awt.Component;
import java.sql.Types;
import java.util.*;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import orion.orionuserview.AttributeDef;
import orion.orionuserview.ForeignKeyDef;
import orion.orionuserview.RelationDef;
import orion.orionuserview.NuclearAttributeDef;
import orion.orionuserview.utils.Defense;

/**
 *
 * @author sl
 */
public class AttributeDefTreeCellRenderer extends DefaultTreeCellRenderer {

    private final Map<String, Icon> icons;
    private final Globals globals;

    public AttributeDefTreeCellRenderer(Map<String, Icon> icons, Globals globals) {
        this.icons = Defense.notNull(icons, "icons");
        this.globals = Defense.notNull(globals, "globals");
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean sel, boolean expanded, boolean leaf,
            int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if (value instanceof AttributeDefTreeNode) {
            AttributeDef ad = ((AttributeDefTreeNode) value).getAttributeDef();
            StringBuffer toolTop = new StringBuffer();
            toolTop.append("<html><b>Name: </b>");
            if (ad instanceof RelationDef) {
                RelationDef rd = (RelationDef) ad;
                setIcon(icons.get("OneTable"));
                toolTop.append(rd.getSchema() == null ? "" : rd.getSchema() + ".");
                toolTop.append(ad.getName());
                toolTop.append("<br><b>Type: </b>");
                toolTop.append(rd.getRelationSourceType());
                if (ad instanceof ForeignKeyDef) {
                    ForeignKeyDef fkd = (ForeignKeyDef) ad;
                    toolTop.append("<br><b>Foreign key: </b>");
                    toolTop.append(fkd.getForeignKeyType().toString());
                    toolTop.append("<br><b>Key map: </b>");
                    toolTop.append(fkd.getPkOnFk().toString());
                    if (fkd.getForeignKeyType() == ForeignKeyType.ONE_TO_MANY) {
                        setIcon(icons.get("ManyTables"));
                    }
                }
            } else if (ad instanceof NuclearAttributeDef) {
                NuclearAttributeDef sad = (NuclearAttributeDef) ad;
                toolTop.append(sad.getName());
                toolTop.append("<br><b>Data type: </b>");
                toolTop.append(sad.getDataType());
                setIcon(iconByDataType(sad.getDataType()));
            }
            setToolTipText(toolTop.toString());
            setText(UIUtils.attributeDefPresentableNameForAdmin(globals, ad));
        }
        return this;
    }

    private Icon iconByDataType(Integer dataType) {
        switch (dataType) {
            case Types.BIGINT:
            case Types.INTEGER:
            case Types.NUMERIC:
            case Types.SMALLINT:
            case Types.TINYINT:
                return icons.get("Number");
            case Types.BOOLEAN:
            case Types.BIT:
                return icons.get("Boolean");
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
                return icons.get("DateTime");
            case Types.DECIMAL:
            case Types.DOUBLE:
            case Types.FLOAT:
            case Types.REAL:
                return icons.get("Float");
            case Types.CHAR:
            case Types.LONGNVARCHAR:
            case Types.LONGVARCHAR:
            case Types.NCHAR:
            case Types.NVARCHAR:
            case Types.VARCHAR:
                return icons.get("Text");
            default:
                return icons.get("Unknown");
        }
    }
}
