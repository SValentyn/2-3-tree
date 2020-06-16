
/**
 * Implements the "2-3-tree" data structure.
 * The structure stores elements in the form of a tree, but balanced.
 */
 class Tree23<T extends Comparable<T>> {

    private static final int ROOT_IS_BIGGER = 1;
    private static final int ROOT_IS_SMALLER = -1;

    private Node root;         // Tree root
    private int size;          // The number of tree elements
    private boolean addition;  // Tracks if the last item was added correctly or not.

    Tree23() {
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
     * @return number of elements in the tree */
    public int size() {
        return size;
    }

    /**
     * Adds a new element to the tree, keeping it balanced
     *
     * @param element item to add
     */
    public void add(T element) {

        size++;
        addition = false;

        if (root == null || root.getLeftElement() == null) { // first case
            if (root == null) {
                root = new Node();
            }
            root.setLeftElement(element);
            addition = true;

        } else {
            Node newRoot = add(root, element);
            if (newRoot != null) {
                root = newRoot;
            }
        }

        if (!addition) size--;
    }

    /**
     * @param current node to add to
     * @param element item to add
     */
    private Node add(Node current, T element) {

        Node newParent = null; // узел, который будет добавлен

        /* we are not yet at the deepest level */
        if (!current.isLeaf()) {

            Node newNode;

            /* element already exists */
            if (current.leftElement.compareTo(element) == 0 || (current.is3Node() && current.rightElement.compareTo(element) == 0)) {
            }

            // newNode < left element
            else if (current.leftElement.compareTo(element) == ROOT_IS_BIGGER) {
                newNode = add(current.left, element);

                // newNode comes from the left branch
                if (newNode != null) {

                    // newNode < than current.left
                    if (current.is2Node()) {
                        current.rightElement = current.leftElement; // сдвинуть текущий левый элемент вправо
                        current.leftElement = newNode.leftElement;
                        current.right = current.mid;
                        current.mid = newNode.mid;
                        current.left = newNode.left;
                    }

                    // we have a new division, so the current element on the left will rise
                    else {

                        // copy the right side of the subtree
                        Node rightCopy = new Node(current.rightElement, null, current.mid, current.right);

                        // create a new “structure” by inserting the right side
                        newParent = new Node(current.leftElement, null, newNode, rightCopy);
                    }
                }
            }

            // newNode is > left and < right
            else if (current.is2Node() || (current.is3Node() && current.rightElement.compareTo(element) == ROOT_IS_BIGGER)) {

                newNode = add(current.mid, element);

                // новое разделение
                if (newNode != null) {

                    // правый элемент пуст, поэтому мы можем установить newNode слева, а существующий левый элемент - справа
                    if (current.is2Node()) {
                        current.rightElement = newNode.leftElement;
                        current.right = newNode.mid;
                        current.mid = newNode.left;
                    }

                    // еще один случай, когда мы должны снова разделить
                    else {
                        Node left = new Node(current.leftElement, null, current.left, newNode.left);
                        Node mid = new Node(current.rightElement, null, newNode.mid, current.right);
                        newParent = new Node(newNode.leftElement, null, left, mid);
                    }
                }
            }

            // newNode больше, чем правый элемент
            else if (current.is3Node() && current.rightElement.compareTo(element) == ROOT_IS_SMALLER) {

                newNode = add(current.right, element);

                // разделение -> правый элемент поднимается
                if (newNode != null) {
                    Node leftCopy = new Node(current.leftElement, null, current.left, current.mid);
                    newParent = new Node(current.rightElement, null, leftCopy, newNode);
                }
            }
        }

        /* Мы на самом глубоком уровне */
        else {

            addition = true;

            /* такой элемент уже существует */
            if (current.leftElement.compareTo(element) == 0 || (current.is3Node() && current.rightElement.compareTo(element) == 0)) {
                addition = false;
            }

            /* случай, когда нет правильного элемента */
            else if (current.is2Node()) {

                // если текущий левый элемент больше чем newNode, мы сдвигаем левый элемент вправо
                if (current.leftElement.compareTo(element) == ROOT_IS_BIGGER) {
                    current.rightElement = current.leftElement;
                    current.leftElement = element;
                }

                // если newNode больше, мы добавляем его справа
                else if (current.leftElement.compareTo(element) == ROOT_IS_SMALLER) current.rightElement = element;
            }

            /* Случай когда в узле 2 элемента, и мы хотим добавить еще один. Для этого мы разделяем узел */
            else newParent = split(current, element);
        }

        return newParent;
    }

    /**
     * Создает новую структуру узла, которая будет присоединена в нижней части метода add
     *
     * @param current узел, где происходит разделение
     * @param element элемент для вставки
     * @return двухузловая структура с ненулевым левым и средним узлом
     */
    private Node split(Node current, T element) {

        Node newParent = null;

        /* левый элемент больше, поэтому он будет подниматься, позволяя встать newParent слева */
        if (current.leftElement.compareTo(element) == ROOT_IS_BIGGER) {
            Node<T> left = new Node<>(element, null);
            Node right = new Node(current.rightElement, null);
            newParent = new Node(current.leftElement, null, left, right);

        } else if (current.leftElement.compareTo(element) == ROOT_IS_SMALLER) {

            // newParent больше текущего справа и меньше правого элемента
            // newParent поднимается
            if (current.rightElement.compareTo(element) == ROOT_IS_BIGGER) {

                Node left = new Node(current.leftElement, null);
                Node right = new Node(current.rightElement, null);
                newParent = new Node(element, null, left, right);

            }

            // newParent самый большой, поэтому текущий правый элемент поднимается
            else {

                Node left = new Node(current.leftElement, null);
                Node<T> right = new Node<>(element, null);
                newParent = new Node(current.rightElement, null, left, right);
            }
        }

        return newParent;
    }

    /**
     * Метод, для удаления элемента из дерева
     *
     * @param element элемента для удаления
     * @return true, если элемент был удалён, иначе false
     */
    public boolean remove(T element) {
        boolean ifRemoved;

        // уменьшаем кол-во уровней в начале
        this.size--;

        ifRemoved = remove(root, element);

        root.reBalance();

        // если удалили последний элемент дерева
        if (root.getLeftElement() == null) root = null;

        // если элемент не был удалён, увеличиваем кол-во уровней
        if (!ifRemoved) this.size++;

        return ifRemoved;
    }

    /**
     * @param current узел из которого нужно удалить
     * @param element элемент который нужно удалить
     * @return true, если элемент был удалён, иначе false
     */
    private boolean remove(Node current, T element) {
        boolean ifRemoved = true;

        /* Случай, когда мы находимся в самом глубоком уровне дерева, но мы не нашли элемент (он не существует) */
        if (current == null) {
            ifRemoved = false;
            return false;
        }

        /* Рекурсивный случай, мы все еще находим элемент для удаления */
        else {

            if (!current.getLeftElement().equals(element)) {

                // Если справа нет элемента или удаляемый элемент меньше правого элемента
                if (current.getRightElement() == null || current.getRightElement().compareTo(element) == ROOT_IS_BIGGER) {

                    // Левый элемент больше, чем удаляемый элемент, поэтому мы проходим левый дочерний элемент
                    if (current.getLeftElement().compareTo(element) == ROOT_IS_BIGGER) {
                        ifRemoved = remove(current.left, element);
                    }

                    // Иначе проходим средний дочерний элемент
                    else {
                        ifRemoved = remove(current.mid, element);
                    }

                } else {

                    // Если удаляемый элемент не равен нужному элементу, мы проходим правого потомка
                    if (!current.getRightElement().equals(element)) {
                        ifRemoved = remove(current.right, element);
                    }

                    // Иначе, мы нашли элемент
                    else {

                        // *** Ситуация 1 ***
                        // Элемент равен правому элементу листа, поэтому мы просто удаляем его
                        if (current.isLeaf()) {
                            current.setRightElement(null);
                        }

                        // *** Ситуация 2 ***
                        // Мы нашли элемент, но его нет в листе
                        else {

                            // Мы получаем элемент "min" правой ветви, удаляем его из текущей позиции и помещаем туда,
                            // где нашли элемент для удаления
                            T replacement = (T) current.getRightNode().replaceMin();

                            current.setRightElement(replacement);
                        }
                    }
                }
            }

            /* Левый элемент равен элементу для удаления */
            else {

                // *** Ситуация 1 ***
                if (current.isLeaf()) {

                    // Левый элемент, элемент для удаления, заменяется правым элементом
                    if (current.getRightElement() != null) {
                        current.setLeftElement(current.getRightElement());
                        current.setRightElement(null);

                    }

                    // Если справа нет элемента, потребуется перебалансировка
                    else {
                        current.setLeftElement(null); // Отпускаем узел
                        return true;
                    }
                }

                // *** Ситуация 2 ***
                else {

                    // Перемещаем элемент "max" левой ветви, где мы нашли элемент
                    T replacement = (T) current.getLeftNode().replaceMax();
                    current.setLeftElement(replacement);
                }
            }
        }

        // Нижний уровень должен быть сбалансирован
        if (current != null && !current.isBalanced()) {
            current.reBalance();
        } else if (current != null && !current.isLeaf()) {

            boolean isBalanced = false;

            while (!isBalanced) {

                if (current.getRightNode() == null) {

                    // Критический случай ситуации 2 у левого потомка
                    if (current.getLeftNode().isLeaf() && !current.getMidNode().isLeaf()) {
                        T replacement = (T) current.getMidNode().replaceMin();
                        T tempLeft = (T) current.getLeftElement();
                        current.setLeftElement(replacement);

                        add(tempLeft);
                    }

                    // Критический случай ситуации 2 у правого потомка
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
     * Метод, для удаления всех элементов из дерева
     */
    public void clear() {
        this.size = 0;
        this.root = null;
    }

    /**
     * Метод, для поиска элемента в дереве
     *
     * @param element элемент, который нужно найти
     * @return true, если элемент был найден, иначе false
     */
    public boolean search(T element) {
        if (root == null) return false;
        return search(root, element);
    }

    private boolean search(Node current, T element) {
        boolean ifFound = false;

        if (current != null) {

            /* В тривиальном случае -> мы нашли элемент */
            if (current.leftElement != null && current.leftElement.equals(element)) {
                ifFound = true;
            }

            /* Мы еще не на самом глубоком уровне */
            else {

                // Элемент для поиска равен правому элементу
                if (current.rightElement != null && current.rightElement.equals(element)) {
                    ifFound = true;
                }

                // иначе -> рекурсивные вызовы
                else {
                    if (current.leftElement.compareTo(element) == ROOT_IS_BIGGER) {
                        ifFound = search(current.left, element);

                    } else if (current.right == null || current.rightElement.compareTo(element) == ROOT_IS_BIGGER) {
                        ifFound = search(current.mid, element);

                    } else if (current.rightElement.compareTo(element) == ROOT_IS_SMALLER) {
                        ifFound = search(current.right, element);

                    } else return false;
                }
            }
        }

        return ifFound;
    }

    /**
     * Метод, для поиска минимального значения
     *
     * @return найденное минимальное значение, иначе null
     */
    public T findMin() {
        if (isEmpty()) return null;
        return findMin(root);
    }

    private T findMin(Node current) {

        // получаем минимальный элемент
        if (current.getLeftNode() == null) {
            return (T) current.leftElement;
        }

        // иначе -> рекурсивные вызовы
        else {
            return findMin(current.getLeftNode());
        }
    }

    /**
     * Метод, для поиска максимального значения
     *
     * @return найденное максимальное значение, иначе null
     */
    public T findMax() {
        if (isEmpty()) return null;
        return findMax(root);
    }

    private T findMax(Node current) {

        // рекурсивные вызовы
        if (current.rightElement != null && current.getRightNode() != null) {
            return findMax(current.getRightNode());
        } else if (current.getMidNode() != null) {
            return findMax(current.getMidNode());
        }

        // получаем максимальный элемент
        if (current.rightElement != null) return (T) current.rightElement;
        else return (T) current.leftElement;
    }

    /**
     * Метод, для вывода элементов дерева в порядке способа - "in-order"
     */
    public void inOrder() {

        if (!isEmpty()) inOrder(root);
        else System.out.print("The tree is empty...");
    }

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

    /**
     * Метод, для вывода элементов дерева в порядке способа - "pre-order"
     */
    public void preOrder() {

        if (!isEmpty()) {

            preOrder(root);
        } else System.out.print("The tree is empty...");
    }

    private void preOrder(Node current) {

        if (current != null) {

            System.out.print(current.leftElement.toString() + " ");
            preOrder(current.left);
            preOrder(current.mid);

            if (current.rightElement != null) {

                System.out.print(current.rightElement.toString() + " ");
                preOrder(current.right);
            }
        }
    }

    /**
     * Метод, для вывода элементов дерева в порядке способа - "post-order"
     */
    public void postOrder() {

        if (!isEmpty()) {

            postOrder(root);
        } else System.out.print("The tree is empty...");
    }

    private void postOrder(Node current) {

        if (current != null) {

            postOrder(current.left);
            postOrder(current.mid);
            System.out.print(current.leftElement.toString() + " ");

            if (current.rightElement != null) {

                System.out.print(current.rightElement.toString() + " ");
                postOrder(current.right);
            }
        }
    }

}
