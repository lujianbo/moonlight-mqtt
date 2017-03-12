package io.github.lujianbo.sentinelmq.util;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jianbo on 2017/3/12.
 */
public class TopicTree<T> {

    /**
     * root of tree
     */
    private TreeNode<T> root = new TreeNode<T>(null, "");

    public TopicTree() {

    }

    private boolean createTopic(String path, T object) {
        return true;
    }

    private T deleteTopic(String path) {
        return null;
    }

    private T findTopic(String path) {

        return null;
    }


    private class TreeNode<N> {

        private final TreeNode<N> parent;

        private final String name;

        private N object;

        private final ConcurrentHashMap<String, TreeNode> children = new ConcurrentHashMap<>();

        TreeNode(TreeNode<N> parent, String name) {
            this.parent = parent;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public TreeNode<N> getParent() {
            return parent;
        }

        public N getObject() {
            return object;
        }

        public void setObject(N object) {
            this.object = object;
        }

        /**
         * 新增一个子节点
         */
        public void addChild(String name) {
            this.children.putIfAbsent(name, new TreeNode<N>(this, name));
        }

        public ConcurrentHashMap<String, TreeNode> getChildren() {
            return children;
        }

    }
}
