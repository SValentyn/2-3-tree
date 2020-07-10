/**
 * Implements the "2-3-tree" data structure.
 * The structure stores elements in the form of a tree, but balanced.
 *
 * @author Syniuk Valentyn
 */
public class Operations<T extends Comparable<T>> {

    private static final int ROOT_IS_BIGGER = 1;
    private static final int ROOT_IS_SMALLER = -1;

    private Node root;      // Tree root
    private int size;       // The number of tree elements
    private boolean flag;   // Tracks if the last element was added correctly or not.

    Operations() {
        this.root = new Node();
        this.size = 0;
    }

    /**
     * @return true, the tree is empty, otherwise false
     */
    public boolean isEmpty() {
        if (root == null) return true;
        return root.getLeftElement() == null;
    }

    /**
     * Check if the given element is in the tree
     *
     * @param element the element to check
     * @return true, if the element is found, otherwise false
     */
    public boolean contains(T element) {
        return search(element);
    }

    /**
     * @return number of elements in the tree
     */
    public int size() {
        return size;
    }

    /**
     * Adds a new element to the tree, keeping it balanced
     *
     * @param element - element to add
     */
    public void add(T element) {
        flag = false;

        if (root == null || root.getLeftElement() == null) {
            flag = true;

            if (root == null) {
                root = new Node();
            }

            root.setLeftElement(element);
        } else {
            Node newRoot = add(root, element);
            if (newRoot != null) {
                root = newRoot;
            }
        }

        if (flag) size++;
    }

    /**
     * @param current node to add to
     * @param element - element to add
     */
    private Node add(Node current, T element) {

        Node newParent = null; // Node to be added

        // We are not yet at the deepest level
        if (!current.isLeaf()) {

            Node newNode;

            // Element already exists
            if (current.leftElement.compareTo(element) == 0 || (current.is3Node() && current.rightElement.compareTo(element) == 0)) {
            }

            // newNode < left element
            else if (current.leftElement.compareTo(element) == ROOT_IS_BIGGER) {
                newNode = add(current.leftChild, element);

                // newNode comes from the left branch
                if (newNode != null) {

                    // newNode < than current.left
                    if (current.is2Node()) {
                        current.rightElement = current.leftElement; // Move the current left element to the right
                        current.leftElement = newNode.leftElement;
                        current.rightChild = current.middleChild;
                        current.middleChild = newNode.middleChild;
                        current.leftChild = newNode.leftChild;
                    }

                    // We have a new division, so the current element on the left will rise
                    else {

                        // Copy the right side of the subtree
                        Node rightCopy = new Node(current.rightElement, null, current.middleChild, current.rightChild);

                        // Create a new "structure" by inserting the right side
                        newParent = new Node(current.leftElement, null, newNode, rightCopy);
                    }
                }
            }

            // newNode is > left and < right
            else if (current.is2Node() || (current.is3Node() && current.rightElement.compareTo(element) == ROOT_IS_BIGGER)) {

                newNode = add(current.middleChild, element);

                // New division
                if (newNode != null) {

                    // The right element is empty, so we can set newNode on the left, and the existing left element on the right
                    if (current.is2Node()) {
                        current.rightElement = newNode.leftElement;
                        current.rightChild = newNode.middleChild;
                        current.middleChild = newNode.leftChild;
                    }

                    // Another case where we have to split again
                    else {
                        Node left = new Node(current.leftElement, null, current.leftChild, newNode.leftChild);
                        Node mid = new Node(current.rightElement, null, newNode.middleChild, current.rightChild);
                        newParent = new Node(newNode.leftElement, null, left, mid);
                    }
                }
            }

            // newNode is larger than the right element
            else if (current.is3Node() && current.rightElement.compareTo(element) == ROOT_IS_SMALLER) {

                newNode = add(current.rightChild, element);

                // Divide -> the right element rises
                if (newNode != null) {
                    Node leftCopy = new Node(current.leftElement, null, current.leftChild, current.middleChild);
                    newParent = new Node(current.rightElement, null, leftCopy, newNode);
                }
            }
        }

        // We are at the deepest level
        else {
            flag = true;

            // Element already exists
            if (current.leftElement.compareTo(element) == 0 || (current.is3Node() && current.rightElement.compareTo(element) == 0)) {
                flag = false;
            }

            // The case when there is no right element
            else if (current.is2Node()) {

                // If the current left element is larger than newNode, we move the left element to the right
                if (current.leftElement.compareTo(element) == ROOT_IS_BIGGER) {
                    current.rightElement = current.leftElement;
                    current.leftElement = element;
                }

                // If newNode is larger, we add it to the right
                else if (current.leftElement.compareTo(element) == ROOT_IS_SMALLER) current.rightElement = element;
            }

            // The case when the node has 2 elements, and we want to add another one. To do this, we share the node
            else newParent = split(current, element);
        }

        return newParent;
    }

    /**
     * The method creates a new node structure that will be attached at the bottom of the add() method
     *
     * @param current - the node where the separation occurs
     * @param element - element to insert
     * @return two-node structure with a nonzero left and middle node
     */
    private Node split(Node current, T element) {

        Node newParent = null;

        // The left element is larger, so it will rise, allowing newParent to stand on the left
        if (current.leftElement.compareTo(element) == ROOT_IS_BIGGER) {
            Node<T> left = new Node<>(element, null);
            Node right = new Node(current.rightElement, null);
            newParent = new Node(current.leftElement, null, left, right);

        } else if (current.leftElement.compareTo(element) == ROOT_IS_SMALLER) {
            Node left = new Node(current.leftElement, null);

            // newParent is greater than the current on the right and smaller than the right. newParent rises.
            if (current.rightElement.compareTo(element) == ROOT_IS_BIGGER) {
                Node right = new Node(current.rightElement, null);
                newParent = new Node(element, null, left, right);
            }

            // newParent is the largest, so the current right element is raised
            else {
                Node<T> right = new Node<>(element, null);
                newParent = new Node(current.rightElement, null, left, right);
            }
        }

        return newParent;
    }

    /**
     * Method for removing an element from the tree
     *
     * @param element - element to remove
     * @return true, if the element was removed, otherwise false
     */
    public boolean remove(T element) {

        // Reduce the number of levels at the beginning
        this.size--;

        boolean ifRemoved = remove(root, element);

        root.reBalance();

        // If you deleted the last element of the tree
        if (root.getLeftElement() == null) root = null;

        // If the element could not be deleted, then increase the number of levels
        if (!ifRemoved) this.size++;

        return ifRemoved;
    }

    /**
     * @param current - node to be deleted
     * @param element - element to be deleted
     * @return true, if the element was deleted, otherwise false
     */
    private boolean remove(Node current, T element) {
        boolean ifRemoved = true;

        // The case when we are at the deepest level of the tree, but we did not find the element (it does not exist)
        if (current == null) {
            ifRemoved = false;
            return false;
        }

        // Recursive case, we still find the element to delete
        else {

            if (!current.getLeftElement().equals(element)) {

                // If there is no element on the right or the element to be deleted is smaller than the right element
                if (current.getRightElement() == null || current.getRightElement().compareTo(element) == ROOT_IS_BIGGER) {

                    // The left element is larger than the element to be deleted, so we go through the left child element
                    if (current.getLeftElement().compareTo(element) == ROOT_IS_BIGGER) {
                        ifRemoved = remove(current.leftChild, element);
                    }

                    // Otherwise -> try to remove the middle child
                    else {
                        ifRemoved = remove(current.middleChild, element);
                    }

                } else {

                    // If the element to be deleted is not equal to the desired element, we pass the right child
                    if (!current.getRightElement().equals(element)) {
                        ifRemoved = remove(current.rightChild, element);
                    }

                    // Otherwise, we found an element
                    else {

                        // *** Situation 1 ***
                        // The element is equal to the right element of the sheet, so we just delete it
                        if (current.isLeaf()) {
                            current.setRightElement(null);
                        }

                        // *** Situation 2 ***
                        // We found the element, but it is not in the sheet
                        else {

                            // We get the min element of the right branch,
                            // delete it from the current position and place it where we found the element to delete.
                            T replacement = (T) current.getRightNode().replaceMin();
                            current.setRightElement(replacement);
                        }
                    }
                }
            }

            // The left element is equal to the element to be deleted.
            else {

                // *** Situation 1 ***
                if (current.isLeaf()) {

                    // The left element, the element to delete, is replaced by the right element
                    if (current.getRightElement() != null) {
                        current.setLeftElement(current.getRightElement());
                        current.setRightElement(null);

                    }

                    // If there is no element on the right, then balancing is required
                    else {
                        current.setLeftElement(null); // Release the node
                        return true;
                    }
                }

                // *** Situation 2 ***
                else {

                    // Move the "max" element of the left branch, where we found the element
                    T replacement = (T) current.getLeftNode().replaceMax();
                    current.setLeftElement(replacement);
                }
            }
        }

        // The lower level must be balanced
        if (!current.isBalanced()) {
            current.reBalance();

        } else if (!current.isLeaf()) {
            boolean isBalanced = false;

            while (!isBalanced) {
                if (current.getRightNode() == null) {

                    // A critical case of situation 2 for the left child
                    if (current.getLeftNode().isLeaf() && !current.getMidNode().isLeaf()) {
                        T replacement = (T) current.getMidNode().replaceMin();
                        T tempLeft = (T) current.getLeftElement();
                        current.setLeftElement(replacement);

                        add(tempLeft);
                    }

                    // A critical case of situation 2 for the right child
                    else if (!current.getLeftNode().isLeaf() && current.getMidNode().isLeaf()) {
                        if (current.getRightElement() == null) {
                            T replacement = (T) current.getLeftNode().replaceMax();
                            T tempLeft = (T) current.getLeftElement();
                            current.setLeftElement(replacement);

                            add(tempLeft);
                        }
                    }
                }

                if (current.getRightNode() != null) {
                    if (current.getMidNode().isLeaf() && !current.getRightNode().isLeaf()) {
                        current.getRightNode().reBalance();
                    }

                    if (current.getMidNode().isLeaf() && !current.getRightNode().isLeaf()) {
                        T replacement = (T) current.getRightNode().replaceMin();
                        T tempRight = (T) current.getRightElement();
                        current.setRightElement(replacement);

                        add(tempRight);
                    } else {
                        isBalanced = true;
                    }
                }

                if (current.isBalanced()) isBalanced = true;
            }
        }

        return ifRemoved;
    }

    /**
     * Method for removing all elements from a tree
     */
    public void clear() {
        this.size = 0;
        this.root = null;
    }

    /**
     * Method for finding an element in a tree
     *
     * @param element - element to find
     * @return true, if the element was found, otherwise false
     */
    public boolean search(T element) {
        if (root == null) {
            return false;
        } else {
            return search(root, element);
        }
    }

    private boolean search(Node current, T element) {
        boolean ifFound = false;

        if (current != null) {

            // In the trivial case -> found an element
            if (current.leftElement != null && current.leftElement.equals(element)) {
                ifFound = true;
            }

            // Otherwise -> not yet at the deepest level
            else {

                // Search element equals right element
                if (current.rightElement != null && current.rightElement.equals(element)) {
                    ifFound = true;
                }

                // Otherwise -> recursive calls
                else {
                    if (current.leftElement.compareTo(element) == ROOT_IS_BIGGER) {
                        ifFound = search(current.leftChild, element);

                    } else if (current.rightChild == null || current.rightElement.compareTo(element) == ROOT_IS_BIGGER) {
                        ifFound = search(current.middleChild, element);

                    } else if (current.rightElement.compareTo(element) == ROOT_IS_SMALLER) {
                        ifFound = search(current.rightChild, element);

                    } else return false;
                }
            }
        }

        return ifFound;
    }

    /**
     * Method for finding the minimum value
     *
     * @return minimum value, otherwise null
     */
    public T findMin() {
        if (isEmpty()) return null;
        return findMin(root);
    }

    private T findMin(Node current) {

        // Get the minimum element
        if (current.getLeftNode() == null) {
            return (T) current.leftElement;
        }

        // Otherwise -> recursive calls
        else {
            return findMin(current.getLeftNode());
        }
    }

    /**
     * Method for finding the maximum value
     *
     * @return maximum value, otherwise null
     */
    public T findMax() {
        if (isEmpty()) {
            return null;
        } else {
            return findMax(root);
        }
    }

    private T findMax(Node current) {

        // Recursive calls
        if (current.rightElement != null && current.getRightNode() != null) {
            return findMax(current.getRightNode());
        } else if (current.getMidNode() != null) {
            return findMax(current.getMidNode());
        }

        // Get the maximum element
        if (current.rightElement != null) {
            return (T) current.rightElement;
        } else {
            return (T) current.leftElement;
        }
    }

    public void inOrder() {
        if (!isEmpty()) {
            inOrder(root);
        } else {
            System.out.print("The tree is empty...");
        }
    }

    /**
     * Method for displaying tree elements in the order of the method - "in-order"
     */
    private void inOrder(Node current) {
        if (current != null) {

            if (current.isLeaf()) {
                System.out.print(current.getLeftElement().toString() + " ");
                if (current.getRightElement() != null) {
                    System.out.print(current.getRightElement().toString() + " ");
                }

            } else {
                inOrder(current.getLeftNode());
                System.out.print(current.getLeftElement().toString() + " ");
                inOrder(current.getMidNode());

                if (current.getRightElement() != null) {
                    if (!current.isLeaf()) {
                        System.out.print(current.getRightElement().toString() + " ");
                    }
                    inOrder(current.getRightNode());
                }
            }
        }
    }

    public void preOrder() {
        if (!isEmpty()) {
            preOrder(root);
        } else {
            System.out.print("The tree is empty...");
        }
    }

    /**
     * Method for displaying tree elements in the order of the method - "pre-order"
     */
    private void preOrder(Node current) {
        if (current != null) {
            System.out.print(current.leftElement.toString() + " ");
            preOrder(current.leftChild);
            preOrder(current.middleChild);

            if (current.rightElement != null) {
                System.out.print(current.rightElement.toString() + " ");
                preOrder(current.rightChild);
            }
        }
    }

    public void postOrder() {
        if (!isEmpty()) {
            postOrder(root);
        } else {
            System.out.print("The tree is empty...");
        }
    }

    /**
     * Method for displaying tree elements in the order of the method - "post-order"
     */
    private void postOrder(Node current) {
        if (current != null) {
            postOrder(current.leftChild);
            postOrder(current.middleChild);
            System.out.print(current.leftElement.toString() + " ");

            if (current.rightElement != null) {
                System.out.print(current.rightElement.toString() + " ");
                postOrder(current.rightChild);
            }
        }
    }

}
