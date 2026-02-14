public class RedBlackTree<T extends Comparable<T>> extends BSTRotation<T> {

    /**
     * Inserts a new data value into the sorted collection.
     * @param data the new value being inserted
     * @throws NullPointerException if data argument is null, we do not allow
     * null values to be stored within a SortedCollection
     */
    @Override
    public void insert(T data) throws NullPointerException {
        if (root == null) {
            RedBlackNode<T> newNode = new RedBlackNode<>(data);

            // Ensure new root node is a black node.
            if (!newNode.isBlackNode()) {
                newNode.flipColor();
            }

            // Set newNode as the new root node.
            root = newNode;
        } else {
            RedBlackNode<T> newNode = new RedBlackNode<>(data);

            // Ensure newly added node is red.
            if (newNode.isBlackNode()) {
                newNode.flipColor();
            }

            // Utilize BinarySearchTree's insertHelper() method to insert the new red node.
            insertHelper(newNode, root);

            // Check and repair any red property violations in the tree after insertion.
            ensureRedProperty(newNode);

            // Ensure root is a black node.
            if (((RedBlackNode<T>)this.root).isBlackNode() == false) {
                ((RedBlackNode<T>)this.root).flipColor();
            }
        }
    }

    /**
     * Checks if a new red node in the RedBlackTree causes a red property violation
     * by having a red parent. If this is not the case, the method terminates without
     * making any changes to the tree. If a red property violation is detected, then
     * the method repairs this violation and any additional red property violations
     * that are generated as a result of the applied repair operation.
     * Using this method might cause nodes with a value equal to the value of one of
     * their ancestors to appear within the left and the right subtree of that ancestor,
     * even if the original insertion procedure consistently inserts such nodes into only
     * the left or the right subtree. But it will preserve the ordering of nodes within
     * the tree.
     * @param newNode a newly inserted red node, or a node turned red by previous repair
     */
    protected void ensureRedProperty(RedBlackNode<T> newNode) {
        // TODO: Implement this method.
    }

}
