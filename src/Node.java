/**
 * Tree-2-3 is formed by nodes in which structural elements are stored.
 * Each node contains no more than two elements and at least one.
 */
class Node<T extends Comparable<T>> {

    Node left;      // left child
    Node mid;       // middle child
    Node right;     // right
    T leftElement;  // left element
    T rightElement; // right element

    public Node() {
        this.left = null;
        this.mid = null;
        this.right = null;
        this.leftElement = null;
        this.rightElement = null;
    }

    /**
     * Constructor of 3 nodes without specific descendants (null references).
     */
    public Node(T leftElement, T rightElement) {
        this.leftElement = leftElement;
        this.rightElement = rightElement;
        left = null;
        mid = null;
        right = null;
    }

    /**
     * Constructor of 3 nodes with given left and middle nodes / descendants.
     */
    public Node(T leftElement, T rightElement, Node left, Node mid) {
        this.leftElement = leftElement;
        this.rightElement = rightElement;
        this.left = left;
        this.mid = mid;
    }

    public T getLeftElement() {
        return leftElement;
    }

    public void setLeftElement(T element) {
        this.leftElement = element;
    }

    public T getRightElement() {
        return rightElement;
    }

    public void setRightElement(T element) {
        this.rightElement = element;
    }

    private void setLeftNode(Node left) {
        this.left = left;
    }

    public Node getLeftNode() {
        return left;
    }

    private void setMidNode(Node mid) {
        this.mid = mid;
    }

    public Node getMidNode() {
        return mid;
    }

    private void setRightNode(Node right) {
        this.right = right;
    }

    public Node getRightNode() {
        return right;
    }

    /**
     * @return true, if we are at the deepest level of a tree, otherwise false
     */
    public boolean isLeaf() {
        return left == null && mid == null && right == null;
    }

    /**
     * @return true, if the right node does not exist, otherwise false
     */
    public boolean is2Node() {
        return rightElement == null;
    }

    /**
     * @return true, if the right node exists, otherwise false
     */
    public boolean is3Node() {
        return rightElement != null;
    }

    /**
     * Method for checking if a tree is well balanced
     *
     * @return true if the tree is well balanced, otherwise false
     */
    boolean isBalanced() {

        boolean balanced = false;

        if (isLeaf()) { // If we are at the deepest level (leaf), it is balanced
            balanced = true;

        } else if (left.getLeftElement() != null && mid.getLeftElement() != null) { // There are two cases: 2 nodes or 3 nodes

            if (rightElement != null) { // 3 узла
                if (right.getLeftElement() != null) {
                    balanced = true;
                }
            } else {  // 2 узла
                balanced = true;
            }
        }

        return balanced;
    }

    public T replaceMax() {

        T max;

        /* Trivial case, we are at the deepest level of the tree */
        if (isLeaf()) {

            if (getRightElement() != null) {
                max = getRightElement();
                setRightElement(null);
                // We are lucky, we do not need to rebalance anything
            } else {
                max = getLeftElement();
                setLeftElement(null);
                // At the first stage of the recursive function, rebalancing will occur
            }
        }

        /* Recursive case, we are not at the deepest level */
        else {

            // 
            if (getRightElement() != null) {
                max = (T) right.replaceMax();
            }

            //If there is an element on the right, we continue on the right
            else {
                max = (T) mid.replaceMax();
            }
        }

        /* Keep balance */
        if (!isBalanced()) {
            reBalance();
        }

        return max;
    }

    /**
     * @return minimum element
     */
    T replaceMin() {

        T min;

        /* Trivial case, we are at the deepest level of the tree */
        if (isLeaf()) {

            min = leftElement;
            leftElement = null;

            // The element was on the right, we skipped it on the left, and nothing happened here
            if (rightElement != null) {
                leftElement = rightElement;
                rightElement = null;
            }
        }

        /* A recursive case, until we reach the deepest level, we always go down to the left */
        else {
            min = (T) left.replaceMin();
        }

        // Keep balance
        if (!isBalanced()) {
            reBalance();
        }

        return min;
    }

    /**
     * Method for maintaining balance by rebalancing
     * the deepest level of the tree starting from the second deepest 
     */
    void reBalance() {

        while (!isBalanced()) {

            /* Imbalance in the left child  */
            if (getLeftNode().getLeftElement() == null) {

                // We put the left element of the current node as the left element of the left child
                getLeftNode().setLeftElement(getLeftElement());

                // Now we replace the left element of the middle descendant as the left element of the current node
                setLeftElement((T) getMidNode().getLeftElement());

                // If the right element on the middle child exists, we move it to the left
                if (getMidNode().getRightElement() != null) {
                    getMidNode().setLeftElement(getMidNode().getRightElement());
                    getMidNode().setRightElement(null);
                }

                // Otherwise, we will make the middle descendant "empty", so the next iteration can resolve this situation,
                // if not, then the critical case begins
                else {
                    getMidNode().setLeftElement(null);
                }

            }

            /* Imbalance in the right child */
            else if (getMidNode().getLeftElement() == null) {

                // Critical case, each node (child) of the deepest level
                // has only one element, the algorithm will have to perform balancing from a higher tree level
                if (getRightElement() == null) {

                    if (getLeftNode().getLeftElement() != null && getLeftNode().getRightElement() == null && getMidNode().getLeftElement() == null) {
                        setRightElement(getLeftElement());
                        setLeftElement((T) getLeftNode().getLeftElement());

                        // we delete current descendants
                        setLeftNode(null);
                        setMidNode(null);
                        setRightNode(null);

                    } else {
                        getMidNode().setLeftElement(getLeftElement());
                        if (getLeftNode().getRightElement() == null) {
                            setLeftElement((T) getLeftNode().getLeftElement());
                            getLeftNode().setLeftElement(null);

                        } else {
                            setLeftElement((T) getLeftNode().getRightElement());
                            getLeftNode().setRightElement(null);
                        }

                        if (getLeftNode().getLeftElement() == null && getMidNode().getLeftElement() == null) {
                            setLeftNode(null);
                            setMidNode(null);
                            setRightNode(null);
                        }
                    }

                } else {

                    // We put the right element of the current node as the left element of the middle child
                    getMidNode().setLeftElement(getRightElement());

                    // We put the left element of the right child as the right element of the current node
                    setRightElement((T) getRightNode().getLeftElement());

                    // If the right child in which we took the last element
                    // has the right element, we move it to the left of the same child element.
                    if (getRightNode().getRightElement() != null) {
                        getRightNode().setLeftElement(getRightNode().getRightElement());
                        getRightNode().setRightElement(null);

                    }

                    // Otherwise, we will make the right child "empty"
                    else {
                        getRightNode().setLeftElement(null);
                    }
                }
            }

            /* Imbalance on the right */
            else if (getRightElement() != null && getRightNode().getLeftElement() == null) {

                // *** Situation 1 ***
                // The middle child exists, so we shift the elements to the right
                if (getMidNode().getRightElement() != null) {
                    getRightNode().setLeftElement(getRightElement());
                    setRightElement((T) getMidNode().getRightElement());
                    getMidNode().setRightElement(null);

                }

                // *** Situation 2 ***
                // The middle child has only the left element,
                // then we need to put the right element of the current node as the right element of the middle child.
                else {
                    getMidNode().setRightElement(getRightElement());
                    setRightElement(null);
                }
            }
        }
    }
}
