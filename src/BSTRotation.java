public class BSTRotation<T extends Comparable<T>> extends BinarySearchTree<T> {

      /**
     * Performs the rotation operation on the provided nodes within this tree.
     * When the provided child is a left child of the provided parent, this
     * method will perform a right rotation. When the provided child is a right
     * child of the provided parent, this method will perform a left rotation.
     *
     * @param child is the node being rotated from child to parent position 
     * @param parent is the node being rotated from parent to child position
     */
    protected void rotate(BinaryNode<T> child, BinaryNode<T> parent) {
        if (child == null || parent == null) {
            return;
        }

        BinaryNode<T> grandparent = parent.getUp();

        boolean parentWasRoot = (parent == root);

        // Right Rotation: Child is to the left of parent.
        if (child == parent.getLeft()) {

            // Move child's right child to parent's left.
            if (child.getRight() != null) {
                BinaryNode<T> rightGrandchild = child.getRight();
                parent.setLeft(rightGrandchild);
                rightGrandchild.setUp(parent);
            } else {
                parent.setLeft(null);
            }

            // Swap child and parent.
            child.setRight(parent);
            parent.setUp(child);
            
        // Left Rotation: Child is to the right of parent.
        } else if (child == parent.getRight()) {

            // Move child's left child to parent's right.
            if (child.getLeft() != null) {
                BinaryNode<T> leftGrandchild = child.getLeft();
                parent.setRight(leftGrandchild);
                leftGrandchild.setUp(parent);
            } else {
                parent.setRight(null);
            }

            // Swap child and parent.
            child.setLeft(parent);
            parent.setUp(child);

        } else {
            // Child isn't actually a subtree of parent, do nothing.
            System.err.println("ROTATION ERROR: Child is not a subtree of parent.");
            return;
        }

        // Reconnect child to grandparent.
        if (grandparent != null) {
            child.setUp(grandparent);

            if (grandparent.getLeft() == parent) {
                grandparent.setLeft(child);
            } else if (grandparent.getRight() == parent) {
                grandparent.setRight(child);
            }
        }

        // If parent was originally root, then child should become root.
        if (parentWasRoot) {
            root = child;
            child.setUp(null);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------------//
    //                                                          TEST METHODS                                                       //
    //-----------------------------------------------------------------------------------------------------------------------------//

    /**
     * Tests a parent child relationship where the child is to the right of the parent.
     * Should rotate to the left. 
     * Returns true if it does, false if it doesn't.
    */
    public boolean test1(){
        BSTRotation<Integer> tree = new BSTRotation<>();

        BinaryNode<Integer> parent = new BinaryNode<>(5);
        BinaryNode<Integer> child = new BinaryNode<>(10);

        parent.setRight(child);
        child.setUp(parent);

        tree.rotate(child, parent);

        boolean parentIsLeftChild = (child.getLeft() == parent);
        boolean childIsParent = (parent.getUp() == child);

        if (parentIsLeftChild && childIsParent) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests a parent child relationship where the child is to the left of the parent.
     * Should rotate to the right. 
     * Returns true if it does, false if it doesn't.
    */
    public boolean test2(){
        BSTRotation<Integer> tree = new BSTRotation<>();

        BinaryNode<Integer> parent = new BinaryNode<>(5);
        BinaryNode<Integer> child = new BinaryNode<>(3);

        parent.setLeft(child);
        child.setUp(parent);

        tree.rotate(child, parent);

        boolean parentIsRightChild = (child.getRight() == parent);
        boolean childIsParent = (parent.getUp() == child);

        if (parentIsRightChild && childIsParent) {
            return true;
        } else {
            return false;
        }
    }

     /**
     * Tests a parent child relationship where the parent was originally the root.
     * Should rotate left, and child should end up as root. 
     * Returns true if it does, false if it doesn't.
    */
    public boolean test3(){
        BSTRotation<Integer> tree = new BSTRotation<>();

        BinaryNode<Integer> parent = new BinaryNode<>(5);
        BinaryNode<Integer> child = new BinaryNode<>(10);

        parent.setRight(child);
        child.setUp(parent);

        tree.root = parent;

        tree.rotate(child, parent);

        boolean parentIsLeftChild = (child.getLeft() == parent);
        boolean childIsParent = (parent.getUp() == child);
        boolean childIsRoot = (tree.root == child);

        if (parentIsLeftChild && childIsParent && childIsRoot) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests a parent child relationship where the child has a left child.
     * Should rotate left, and left grandchild should end up as right child of parent. 
     * Returns true if it does, false if it doesn't.
    */
    public boolean test4(){
        BSTRotation<Integer> tree = new BSTRotation<>();

        BinaryNode<Integer> parent = new BinaryNode<>(5);
        BinaryNode<Integer> child = new BinaryNode<>(10);
        BinaryNode<Integer> grandchild = new BinaryNode<>(7);

        parent.setRight(child);
        child.setUp(parent);
        
        child.setLeft(grandchild);
        grandchild.setUp(child);

        tree.rotate(child, parent);

        boolean parentIsLeftChild = (child.getLeft() == parent);
        boolean childIsParent = (parent.getUp() == child);
        boolean grandchildInCorrectSpot = (grandchild.getUp() == parent && parent.getRight() == grandchild);

        if (parentIsLeftChild && childIsParent && grandchildInCorrectSpot) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests a parent child relationship where the child has a left and right child.
     * Should rotate left, so left grandchild will end up as right child of parent, right grandchild stays right child of child. 
     * Returns true if they do, false if they don't.
    */
    public boolean test5(){
        BSTRotation<Integer> tree = new BSTRotation<>();

        BinaryNode<Integer> parent = new BinaryNode<>(5);
        BinaryNode<Integer> child = new BinaryNode<>(10);
        BinaryNode<Integer> grandchild1 = new BinaryNode<>(7);
        BinaryNode<Integer> grandchild2 = new BinaryNode<>(13);

        parent.setRight(child);
        child.setUp(parent);
        
        child.setLeft(grandchild1);
        grandchild1.setUp(child);

        child.setRight(grandchild2);
        grandchild2.setUp(child);

        tree.rotate(child, parent);

        boolean parentIsLeftChild = (child.getLeft() == parent);
        boolean childIsParent = (parent.getUp() == child);
        boolean grandchild1InCorrectSpot = (grandchild1.getUp() == parent && parent.getRight() == grandchild1);
        boolean grandchild2InCorrectSpot = (grandchild2.getUp() == child && child.getRight() == grandchild2);

        if (parentIsLeftChild && childIsParent && grandchild1InCorrectSpot && grandchild2InCorrectSpot) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * Tests a parent child relationship where the child has a left and right child, and the parent has a left child.
     * Should rotate left, so left grandchild will end up as right child of parent, right grandchild stays right child of child,
     * and left child of parent stays left child of parent even after being rotated, i.e left grandchild of child. 
     * Returns true if they do, false if they don't.
    */
    public boolean test6(){
        BSTRotation<Integer> tree = new BSTRotation<>();

        BinaryNode<Integer> parent = new BinaryNode<>(5);
        BinaryNode<Integer> child = new BinaryNode<>(10);
        BinaryNode<Integer> child2= new BinaryNode<>(3);
        BinaryNode<Integer> grandchild1 = new BinaryNode<>(7);
        BinaryNode<Integer> grandchild2 = new BinaryNode<>(13);

        parent.setRight(child);
        child.setUp(parent);

        parent.setLeft(child2);
        child2.setUp(parent);
        
        child.setLeft(grandchild1);
        grandchild1.setUp(child);

        child.setRight(grandchild2);
        grandchild2.setUp(child);

        tree.rotate(child, parent);

        boolean parentIsLeftChild = (child.getLeft() == parent);
        boolean childIsParent = (parent.getUp() == child);
        boolean grandchild1InCorrectSpot = (grandchild1.getUp() == parent && parent.getRight() == grandchild1);
        boolean grandchild2InCorrectSpot = (grandchild2.getUp() == child && child.getRight() == grandchild2);
        boolean child2InCorrectSpot = (child2.getUp() == parent && child2.getUp().getUp() == child && parent.getLeft() == child2);

        if (parentIsLeftChild && childIsParent && grandchild1InCorrectSpot && grandchild2InCorrectSpot && child2InCorrectSpot) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        BSTRotation<Integer> tester = new BSTRotation<>();

        System.out.println("\n\n\u001b[1m\033[3mTesting Results (True means the test has passed.)\u001b[0m");
        System.out.println("-------------------------------------------------");
        System.out.println("- Test 1: \u001b[1m" + tester.test1() + "\u001b[0m\033[3m (checks left rotation) \u001b[0m");
        System.out.println("- Test 2: \u001b[1m" + tester.test2() + "\u001b[0m\033[3m (checks right rotation) \u001b[0m");
        System.out.println("- Test 3: \u001b[1m" + tester.test3() + "\u001b[0m\033[3m (checks rotation when parent is the root) \u001b[0m");
        System.out.println("- Test 4: \u001b[1m" + tester.test4() + "\u001b[0m\033[3m (checks rotation when child has a left child, i.e. 1 other child total) \u001b[0m");
        System.out.println("- Test 5: \u001b[1m" + tester.test5() + "\u001b[0m\033[3m (checks rotation when child has a left and right child, i.e. 2 other children total) \u001b[0m");
        System.out.println("- Test 6: \u001b[1m" + tester.test6() + "\u001b[0m\033[3m (checks rotation when child has a left and right child, and parent has a left child, i.e. 3 other children total) \u001b[0m");
        if (tester.test1() && tester.test2() && tester.test3() && tester.test4() && tester.test5() && tester.test6()) {
            System.out.println("\n--> All tests passed successfully!\n");
        }
    }
}
