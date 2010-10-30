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
 * //TODO Вынести выбор иконы в AttributeDefTreeNode
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
            AttributeDefTreeNode node = (AttributeDefTreeNode) value;
            AttributeDef ad = node.getAttributeDef();
            StringBuilder toolTop = new StringBuilder();
            toolTop.append("<html><b>Name: </b>");
            if (ad instanceof RelationDef) {
                RelationDef rd = (RelationDef) ad;
                if (rd.isArray()) {
                    setIcon(icons.get("ManyTables"));
                } else {
                    setIcon(icons.get("OneTable"));
                }
                toolTop.append(rd.getSchema() == null ? "" : rd.getSchema() + ".");
                toolTop.append(ad.getName());
                toolTop.append("<br><b>Type: </b>");
                toolTop.append(rd.getRelationSourceType());
                toolTop.append("<br><b>Alias: </b>");
                toolTop.append(rd.getAlias());
                if (ad instanceof ForeignKeyDef) {
                    ForeignKeyDef fkd = (ForeignKeyDef) ad;
                    toolTop.append("<br><b>Foreign key: </b>");
                    toolTop.append(fkd.getForeignKeyType().toString());
                    toolTop.append("<br><b>Key map: </b>");
                    toolTop.append(fkd.getPkOnFk().toString());
                }
                if (node.isLeaf()) {
                    setIcon(iconByLeafRelation(rd));
                }
            } else if (ad instanceof NuclearAttributeDef) {
                NuclearAttributeDef sad = (NuclearAttributeDef) ad;
                toolTop.append(sad.getName());
                toolTop.append("<br><b>Data type: </b>");
                toolTop.append(sad.getDataType());
                setIcon(iconByDataType(sad.getDataType(), sad.isArray()));
            }
            setToolTipText(toolTop.toString());
            setText(UIUtils.attributeDefPresentableNameForAdmin(globals, ad));
        }
        return this;
    }

    private Icon iconByLeafRelation(RelationDef rd) {
        AttributeDef childAd = rd;
        while (!(childAd instanceof NuclearAttributeDef)) {
            childAd = ((RelationDef) childAd).get(0);
        }
        NuclearAttributeDef nad = (NuclearAttributeDef) childAd;
        return iconByDataType(nad.getDataType(), nad.isArray());
    }

    private Icon iconByDataType(Integer dataType, Boolean isArray) {
        String iconName;
        switch (dataType) {
            case Types.BIGINT:
            case Types.INTEGER:
            case Types.NUMERIC:
            case Types.SMALLINT:
            case Types.TINYINT:
                iconName = "Number";
                break;
            case Types.BOOLEAN:
            case Types.BIT:
                iconName = "Boolean";
                break;
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
                iconName = "DateTime";
                break;
            case Types.DECIMAL:
            case Types.DOUBLE:
            case Types.FLOAT:
            case Types.REAL:
                iconName = "Float";
                break;
            case Types.CHAR:
            case Types.LONGNVARCHAR:
            case Types.LONGVARCHAR:
            case Types.NCHAR:
            case Types.NVARCHAR:
            case Types.VARCHAR:
                iconName = "Text";
                break;
            default:
                iconName = "Unknown";
                break;
        }
        if (isArray) {
            iconName = "Many" + iconName + "s";
        }
        return icons.get(iconName);
    }
}
