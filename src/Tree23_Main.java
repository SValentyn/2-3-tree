import java.util.Scanner;

/**
 * С помощью меню, имеем доступ к функциям по обработке 2-3-дерева.
 *
 * @author Syniuk Valentyn
 */
public class Tree23_Main {

    public static void main(String[] args) {

        /* создаём объект класса Tree23 */
        Tree23<Integer> tree = new Tree23<>();
        Scanner scanner = new Scanner(System.in);

        /* menu */
        while (true) {
            System.out.println("\nTree23 Operations:");
            System.out.println("1. insert");
            System.out.println("2. remove");
            System.out.println("3. search");
            System.out.println("4. size tree");
            System.out.println("5. check empty");
            System.out.println("6. clear tree");
            System.out.println("7. get minimum");
            System.out.println("8. get maximum");
            System.out.println("9. display tree");
            System.out.println("10. exit..\n");

            System.out.print("Enter your change: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1: {
                    System.out.print("Enter integer element to insert: ");
                    tree.add(scanner.nextInt());
                    break;
                }
                case 2: {
                    System.out.print("Enter integer element to remove: ");
                    System.out.println("Remove result: " + tree.remove(scanner.nextInt()));
                    break;
                }
                case 3: {
                    System.out.print("Enter integer element to search: ");
                    System.out.println("Search result: " + tree.search(scanner.nextInt()));
                    break;
                }
                case 4: {
                    System.out.println("Size tree: " + tree.size());
                    break;
                }
                case 5: {
                    System.out.println("Empty status: " + tree.isEmpty());
                    break;
                }
                case 6: {
                    System.out.println("\nTree Cleared!");
                    tree.clear();
                    break;
                }
                case 7: {
                    System.out.println("Minimum element: " + tree.findMin());
                    break;
                }
                case 8: {
                    System.out.println("Maximum element: " + tree.findMax());
                    break;
                }
                case 9: {
                    System.out.print("\nIn-order: ");
                    tree.inOrder();
                    System.out.print("\nPre-order: ");
                    tree.preOrder();
                    System.out.print("\nPost-order: ");
                    tree.postOrder();
                    System.out.println();
                    break;
                }
                case 10: { // exit
                    return;
                }
                default: {
                    System.out.println("\nWrong Entry!");
                    break;
                }
            }
        }
    }
}
