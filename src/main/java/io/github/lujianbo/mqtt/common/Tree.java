package io.github.lujianbo.mqtt.common;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class Tree<T> {

    public final TreeNode<T> root;

    public Tree() {
        root = new TreeNode<>();
    }

    /**
     * 树结点
     */
    public static class TreeNode<T> {
        private TreeNode<T> parent = null;
        private T data;
        private ConcurrentHashMap<String, TreeNode<T>> children = new ConcurrentHashMap<>();

        public TreeNode(TreeNode<T> parent, T data) {
            this.parent = parent;
            this.data = data;
        }

        public TreeNode() {
        }

        public TreeNode getParent() {
            return parent;
        }

        public void setParent(TreeNode parent) {
            this.parent = parent;
        }

        public ConcurrentHashMap<String, TreeNode<T>> getChildren() {
            return children;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        /**
         * 对于任意child进行操作
         */
        public void foreachChild(Consumer<TreeNode<T>> consumer) {

        }
    }

    public TreeNode<T> getRoot() {
        return root;
    }
}
