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
    private final boolean foldOneLeaf;

    public AttributeDef getAttributeDef() {
        init();
        return attributeDef;
    }

    public AttributeDefTreeNode(AttributeDefTreeNode parent, AttributeDef attributeDef, boolean foldOneLeaf) {
        this.attributeDef = Defense.notNull(attributeDef, "attributeDef");
        this.parent = parent;
        this.foldOneLeaf = foldOneLeaf;
        relationDef = (attributeDef instanceof RelationDef) ? (RelationDef) attributeDef : null;
    }

    public AttributeDefTreeNode(EntityDef rootRelationDef, boolean foldOneLeaf) {
        this(null, rootRelationDef, foldOneLeaf);
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
        return !isLeaf();
    }

    @Override
    public boolean isLeaf() {
        if (foldOneLeaf) {
            init();
            if (children == null) {
                return true;
            } else if (children.size() == 1) {
                return children.get(0).isLeaf();
            } else {
                return false;
            }
        } else {
            return relationDef == null;
        }
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
                    //Бывает ситуация что в таблице нет нет видимых полей и зависимых таблиц
                    //Такую вырожденную ветку не будем добавлять вообще
                    //FIXME не обрабатываются вложенные вырожденные ветки,
                    //т.к это затрудняет ленивую инциализацию
                    RelationDef rd = null;
                    if (ad instanceof RelationDef) {
                        rd = (RelationDef) ad;
                        rd.init();
                    }
                    if (rd == null || rd.size() != 0) {
                        children.add(new AttributeDefTreeNode(this, ad, foldOneLeaf));
                    }
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
