/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.orionuserview.swing;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import javax.swing.tree.TreeNode;
import orion.orionuserview.AttributeDef;
import orion.orionuserview.EntityDef;
import orion.orionuserview.RelationDef;
import orion.orionuserview.utils.Defense;

/**
 *
 * @author sl
 */
public class AttributeDefTreeNode implements TreeNode {

    private final AttributeDef attributeDef;
    private final RelationDef relationDef;
    private final AttributeDefTreeNode parent;
    private List<AttributeDefTreeNode> children;

    public AttributeDef getAttributeDef() {
        init();
        return attributeDef;
    }

    public AttributeDefTreeNode(AttributeDefTreeNode parent, AttributeDef attributeDef) {
        this.attributeDef = Defense.notNull(attributeDef, "attributeDef");
        this.parent = parent;
        relationDef = (attributeDef instanceof RelationDef) ? (RelationDef) attributeDef : null;
    }

    public AttributeDefTreeNode(EntityDef rootRelationDef) {
        this(null, rootRelationDef);
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        if (relationDef != null) {
            init();
            return children.get(childIndex);
        }
        return null;
    }

    @Override
    public int getChildCount() {
        if (relationDef != null) {
            init();
            return children.size();
        }
        return 0;
    }

    @Override
    public TreeNode getParent() {
        return parent;
    }

    //Почему-то в аргументе приходит копия TreeNode, поэтому для поиска определен equals
    @Override
    public int getIndex(TreeNode node) {
        if (relationDef != null) {
            init();
            return children.indexOf(node);
        }
        return -1;
    }

    @Override
    public boolean getAllowsChildren() {
        return relationDef != null;
    }

    @Override
    public boolean isLeaf() {
        return relationDef == null;
    }

    @Override
    public Enumeration children() {
        init();
        if (children == null) {
            return Collections.enumeration(Collections.EMPTY_LIST);
        } else {
            return Collections.enumeration(children);
        }
    }

    private void init() {
        if (relationDef != null) {
            try {
                relationDef.init();
                children = new ArrayList<AttributeDefTreeNode>();
                for (AttributeDef ad : relationDef) {
                    children.add(new AttributeDefTreeNode(this, ad));
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
    }

    @Override
    public String toString() {
        init();
        return attributeDef.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AttributeDefTreeNode other = (AttributeDefTreeNode) obj;
        if (this.attributeDef != other.attributeDef && (this.attributeDef == null || !this.attributeDef.equals(other.attributeDef))) {
            return false;
        }
        if (this.parent != other.parent && (this.parent == null || !this.parent.equals(other.parent))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

}
