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

    /**
     * create a new topic
     * */
    private void createTopic(String[] path, T object) {
        TreeNode<T> node=root;
        for (int i=0;i<path.length;i++){
            node=node.createIfNotExit(path[i]);
        }
        node.setObject(object);
    }

    /**
     * delete a topic
     * */
    private T deleteTopic(String[] path) {
        TreeNode<T> node=findNode(path);
        if (node!=null&&path.length-1>=0){
            TreeNode<T> removeNode=node.parent.children.remove(path[path.length-1]);
            return removeNode.object;
        }
        return null;
    }

    private TreeNode<T> findNode(String[] path){
        TreeNode<T> node=root;
        for (int i=0;i<path.length;i++){
            node=node.getChild(path[i]);
            if (node==null){
                break;
            }
        }
        return node;
    }

    private T findTopic(String[] path) {
        TreeNode<T> node=findNode(path);
        if (node!=null){
            return node.object;
        }
        return null;
    }


    private class TreeNode<N> {

        private final TreeNode<N> parent;

        private final String name;

        private N object;

        private final ConcurrentHashMap<String, TreeNode<N>> children = new ConcurrentHashMap<>();

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
        public TreeNode<N> createIfNotExit(String name) {
            TreeNode<N> child = children.get(name);
            if (child == null) {
                child = new TreeNode<N>(this, name);
                TreeNode<N> returnNode = this.children.putIfAbsent(name, child);
                if (returnNode != null) {
                    child = returnNode;
                }
            }
            return child;
        }

        public TreeNode<N> getChild(String name){
            return this.children.get(name);
        }


        public ConcurrentHashMap<String, TreeNode<N>> getChildren() {
            return children;
        }

    }
}
