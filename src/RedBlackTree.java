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
        if (newNode != null && newNode.getUp() != null) {
            RedBlackNode<T> parent = newNode.getUp();

            // Check if there is a red property violation.
            if (!newNode.isBlackNode() && !parent.isBlackNode()) {
                RedBlackNode<T> aunt = null;

                // Discover which node is newNode's aunt.
                if (parent.getUp() != null) {
                    RedBlackNode<T> grandparent = parent.getUp();

                    // Check if parent is the right or left child of grandparent.
                    if (grandparent.getRight() == parent) {     // Parent is right child, aunt should be left child.
                        aunt = grandparent.getLeft();
                        //System.out.println("Aunt of " + newNode + " = " + aunt);
                    } else if (grandparent.getLeft() == parent) {       // Parent is left child, aunt should be right child.
                        aunt = grandparent.getRight();
                        //System.out.println("Aunt of " + newNode + " = " + aunt);
                    }

                    // Check if aunt is red or black (or null).
                    if (aunt == null || aunt.isBlackNode()) {
                        // Aunt is black or null:

                        /* 
                        If child and parent are not aligned, rotate child up over parent first, then rotate child up 
                        over grandparent. For aligned case, rotate parent up over grandparent.
                        */ 
                        RedBlackNode<T> promoted = null;
                        if ((grandparent.getRight() == parent && parent.getLeft() == newNode) || (grandparent.getLeft() == parent && parent.getRight() == newNode)) {
                            // Zig-zag case: Rotate child up over parent, then rotate child up over grandparent.
                            this.rotate(newNode, parent);

                            if (newNode.getUp() != null) {
                                this.rotate(newNode, newNode.getUp());
                                promoted = newNode;
                            }

                        } else {
                            // Aligned case: rotate parent up over grandparent.
                            this.rotate(parent, grandparent);
                            promoted = parent;
                        }

                        // After rotation, set promoted node to black and original grandparent to red.
                        if (promoted != null) {
                            if (!promoted.isBlackNode()) {
                                promoted.flipColor();
                            }
                            if (grandparent.isBlackNode()) {
                                grandparent.flipColor();
                            }
                        }

                    } else {
                        // Aunt is red:

                        // Recolor grandparent and its children to repair.
                        recolor(grandparent);

                        // Now check grandparent to see if we created a new red property violation.
                        ensureRedProperty(grandparent);
                    }
                }
            }
        }

        RedBlackNode<T> redBlackRoot = (RedBlackNode<T>)this.root;

        if (!redBlackRoot.isBlackNode()) {
            redBlackRoot.flipColor();
        }
    }

    /**
     * Recursively flips the color of all nodes in the tree except the one we want (the one we just inserted).
     * @param node current node to flip (start with root node)
     * @param newNode node to ignore, pass newly inserted node.
     */
    private void recolor(RedBlackNode<T> grandparent) {
        if (grandparent == null) {
            return;
        }

        // Standard recolor: make grandparent red and its two children black.
        if (grandparent.isBlackNode()) {
            grandparent.flipColor();
        }

        RedBlackNode<T> left = grandparent.getLeft();
        RedBlackNode<T> right = grandparent.getRight();

        if (left != null && !left.isBlackNode()) {
            left.flipColor();
        }
        if (right != null && !right.isBlackNode()) {
            right.flipColor();
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------------//
    //                                                          TEST METHODS                                                       //
    //-----------------------------------------------------------------------------------------------------------------------------//

    /**
     * Tests a RedBlackTree with the insertion order (14, 7, 18, 23).
     * This case was gone over during lecture.
     * In this case, the program will identify and repair a red property violation when 18 is inserted,
     * and then once again when 23 is inserted.
     * Tests the repair case for when the aunt is red.
     * @return true if the final tree has the correct expected level-order with the correct colors 
     * following two red property violation repair jobs.
     */
    public static boolean test1() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();

        int[] data = {14, 7, 18, 23};

        for (int dataPoint : data) {
            tree.insert(dataPoint);
            System.out.println(tree);
        }

        if (!tree.toString().equalsIgnoreCase("[ \u001B[0m14\u001B[0m, \u001B[0m7\u001B[0m, \u001B[0m18\u001B[0m, \u001B[31m23\u001B[0m ]")) {
            return false;
        }

        return true;
    }

    /**
     * Tests a RedBlackTree with the insertion order (14, 7, 18, 23, 1, 11, 20).
     * This case was gone over during lecture.
     * In this case, the program will identify and repair a red property violation when 18 is inserted,
     * when 23 is inserted, and once again when 20 is inserted.
     * Tests both the repair cases, both when the aunt is red, and when the aunt is null.
     * @return true if the final tree has the correct expected level-order with the correct colors 
     * following three red property violation repair jobs.
     */
    public static boolean test2() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();

        int[] data = {14, 7, 18, 23, 1, 11, 20};

        for (int dataPoint : data) {
            tree.insert(dataPoint);
            System.out.println(tree);
        }

        if (!tree.toString().equalsIgnoreCase("[ \u001B[0m14\u001B[0m, \u001B[0m7\u001B[0m, \u001B[0m20\u001B[0m, \u001B[31m1\u001B[0m, \u001B[31m11\u001B[0m, \u001B[31m18\u001B[0m, \u001B[31m23\u001B[0m ]")) {
            return false;
        }

        return true;
    }

    /**
     * Tests a RedBlackTree with the insertion order (14, 7, 18, 23, 1, 11, 20, 29, 25, 27).
     * This case was gone over during lecture.
     * In this case, the program will identify and repair a red property violation when 18, 23, 20, and now 
     * also when 29, 25, and 27 are inserted. Tests both the repair cases, both when the aunt is red, 
     * and when the aunt is null, as well as both when the parent, child, and grandparent are aligned vs.
     * when they are in a zig-zag formation.
     * @return true if the final tree has the correct expected level-order with the correct colors 
     * following six red property violation repair jobs, and almost full tree rearrangement.
     */
    public static boolean test3() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();

        int[] data = {14, 7, 18, 23, 1, 11, 20, 29, 25, 27};

        for (int dataPoint : data) {
            tree.insert(dataPoint);
            System.out.println(tree);
        }

        if (!tree.toString().equalsIgnoreCase("[ \u001B[0m20\u001B[0m, \u001B[31m14\u001B[0m, \u001B[31m25\u001B[0m, \u001B[0m7\u001B[0m, \u001B[0m18\u001B[0m, \u001B[0m23\u001B[0m, \u001B[0m29\u001B[0m, \u001B[31m1\u001B[0m, \u001B[31m11\u001B[0m, \u001B[31m27\u001B[0m ]")) {
            return false;
        }

        return true;
    }
    
    public static void main (String[] args) {
        System.out.println("Test 1: " + test1() + "\n");
        System.out.println("Test 2: " + test2() + "\n");
        System.out.println("Test 3: " + test3() + "\n");
    }

}
