/**
 * Дерево-2-3 образовано узлами, в которых хранятся элементы структуры.
 * Каждый узел содержит не более двух элементов и не менее одного.
 */
class Node<T extends Comparable<T>> {

    Node left;      // левый потомок
    Node mid;       // средний потомок
    Node right;     // правый потомок
    T leftElement;  // элемент слева
    T rightElement; // элемент справа

    public Node() {
        this.left = null;
        this.mid = null;
        this.right = null;
        this.leftElement = null;
        this.rightElement = null;
    }

    /**
     * Конструктор 3-х узлов без определенных потомков (нулевые ссылки).
     */
    public Node(T leftElement, T rightElement) {
        this.leftElement = leftElement;
        this.rightElement = rightElement;
        left = null;
        mid = null;
        right = null;
    }

    /**
     * Конструктор 3-х узлов с заданными левым и средним узлами/потомками.
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
     * @return true, если мы находимся на самом глубоком уровне дерева, иначе false
     */
    public boolean isLeaf() {
        return left == null && mid == null && right == null;
    }

    /**
     * @return true, если правого узла не существует, иначе false
     */
    public boolean is2Node() {
        return rightElement == null;
    }

    /**
     * @return true, если правый узел существует, иначе false
     */
    public boolean is3Node() {
        return rightElement != null;
    }

    /**
     * Метод, для проверки, хорошо ли сбалансировано дерево
     *
     * @return true если дерево хорошо сбалансировано, иначе false
     */
    boolean isBalanced() {

        boolean balanced = false;

        if (isLeaf()) { // Если мы находимся на самом глубоком уровне (лист), он точно сбалансирован
            balanced = true;

        } else if (left.getLeftElement() != null && mid.getLeftElement() != null) { // Есть два случая: 2 узла или 3 узла

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

        /* Тривиальный случай, мы находимся на самом глубоком уровне дерева */
        if (isLeaf()) {

            if (getRightElement() != null) {
                max = getRightElement();
                setRightElement(null);
                // Нам повезло, нам ничего не нужно перебалансировать
            } else {
                max = getLeftElement();
                setLeftElement(null);
                // На первом этапе рекурсивной функции произойдет перебалансировка
            }
        }

        /* Рекурсивный случай, мы не на самом глубоком уровне */
        else {

            // Если есть элемент справа, мы продолжаем справа
            if (getRightElement() != null) {
                max = (T) right.replaceMax();
            }

            // Иначе, продолжаем со средним
            else {
                max = (T) mid.replaceMax();
            }
        }

        /* Сохраняем баланс */
        if (!isBalanced()) {
            reBalance();
        }

        return max;
    }

    /**
     * @return минимальный элемент
     */
    T replaceMin() {

        T min;

        /* Тривиальный случай, мы находимся на самом глубоком уровне дерева */
        if (isLeaf()) {

            min = leftElement;
            leftElement = null;

            // Элемент был справа, мы пропустили его слева, и здесь ничего не произошло
            if (rightElement != null) {
                leftElement = rightElement;
                rightElement = null;
            }
        }

        /* Рекурсивный случай, пока мы не достигаем самого глубокого уровня, мы всегда спускаемся влево */
        else {
            min = (T) left.replaceMin();
        }

        // Сохраняем баланс
        if (!isBalanced()) {
            reBalance();
        }

        return min;
    }

    /**
     * Метод, для сохранение баланса, путём перебалансировки
     * самого глубокого уровня дерева начиная со второго самого глубокого
     */
    void reBalance() {

        while (!isBalanced()) {

            /*  Дисбаланс в левом потомке */
            if (getLeftNode().getLeftElement() == null) {

                // Мы ставим левый элемент текущего узла как левый элемент левого потомка
                getLeftNode().setLeftElement(getLeftElement());

                // Теперь мы заменяем левый элемент среднего потомка как левый элемент текущего узла
                setLeftElement((T) getMidNode().getLeftElement());

                // Если правый элемент на среднем дочернем элементе существует, мы сдвигаем его влево
                if (getMidNode().getRightElement() != null) {
                    getMidNode().setLeftElement(getMidNode().getRightElement());
                    getMidNode().setRightElement(null);
                }

                // Иначе, мы дадим среднему потомку "empty", так что следующая итерация может разрешить эту ситуацию,
                // если нет, то здесь начинается критический случай
                else {
                    getMidNode().setLeftElement(null);
                }

            }

            /* Дисбаланс в правом ребенке */
            else if (getMidNode().getLeftElement() == null) {

                // Критический случай, каждый узел (дочерний элемент) самого глубокого уровня
                // имеет только один элемент, алгоритм должен будет выполнить балансировку с более высокого уровня дерева
                if (getRightElement() == null) {

                    if (getLeftNode().getLeftElement() != null && getLeftNode().getRightElement() == null && getMidNode().getLeftElement() == null) {
                        setRightElement(getLeftElement());
                        setLeftElement((T) getLeftNode().getLeftElement());

                        // мы удаляем текущих потомков
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

                    // Мы ставим правый элемент текущего узла как левый элемент среднего сына
                    getMidNode().setLeftElement(getRightElement());

                    // Мы помещаем левый элемент правого потомка в качестве правого элемента текущего узла
                    setRightElement((T) getRightNode().getLeftElement());

                    // Если правый дочерний элемент, в котором мы взяли последний элемент,
                    // имеет правый элемент, мы перемещаем его слева от того же дочернего элемента.
                    if (getRightNode().getRightElement() != null) {
                        getRightNode().setLeftElement(getRightNode().getRightElement());
                        getRightNode().setRightElement(null);

                    }

                    // Иначе мы дадим нужному ребенку "empty"
                    else {
                        getRightNode().setLeftElement(null);
                    }
                }
            }

            /* Дисбаланс в правом потомке */
            else if (getRightElement() != null && getRightNode().getLeftElement() == null) {

                // *** Ситуация 1 ***
                // Средний ребенок существует, поэтому мы делаем сдвиг элементов вправо
                if (getMidNode().getRightElement() != null) {
                    getRightNode().setLeftElement(getRightElement());
                    setRightElement((T) getMidNode().getRightElement());
                    getMidNode().setRightElement(null);

                }

                // *** Ситуация 2 ***
                // Средний дочерний элемент имеет только левый элемент,
                // тогда мы должны поместить правый элемент текущего узла в качестве правого элемента среднего дочернего элемента.
                else {
                    getMidNode().setRightElement(getRightElement());
                    setRightElement(null);
                }
            }
        }
    }
}