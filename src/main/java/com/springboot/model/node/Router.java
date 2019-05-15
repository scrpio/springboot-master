package com.springboot.model.node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Router implements Serializable {
    private String path;
    private Integer id;
    private String name;
    /**
     * 是否为叶子节点
     */
    private boolean leaf;
    private boolean menuShow;
    private Integer parentId;
    private String iconCls;
    private List<Router> children;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public boolean getMenuShow() {
        return menuShow;
    }

    public void setMenuShow(boolean menuShow) {
        this.menuShow = menuShow;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public List<Router> getChildren() {
        return children;
    }

    public void setChildren(List<Router> children) {
        this.children = children;
    }

    public static List<Router> buildList(List<Router> nodes, Integer idParam) {
        if (nodes == null) {
            return null;
        }
        List<Router> topNodes = new ArrayList<Router>();
        for (Router child : nodes) {
            Integer pid = child.getParentId();
            if (pid == null || idParam == pid) {
                topNodes.add(child);
                continue;
            }
            for (Router parent : nodes) {
                Integer id = parent.getId();
                if (id != null && id.equals(pid)) {
                    parent.getChildren().add(child);
                    // child.setHasParent(true);
                    // parent.setChildren(true);
                    //parent.setLeaf(false);
                    continue;
                } else {
                    // parent.setLeaf(true);
                }
            }
        }
        return topNodes;
    }
}
