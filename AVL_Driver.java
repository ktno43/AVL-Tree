import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class AVL_Driver {

	public static void main(String[] args) throws IOException {
		TreeAVL<Integer, Integer> tree = new TreeAVL<Integer, Integer>();

		try {
			OutputStream treeStream = new FileOutputStream("Tree.txt");
			OutputStreamWriter treeWriter = new OutputStreamWriter(treeStream);
			FileInputStream in = new FileInputStream("Tree.txt");
			int content = 0;

			System.out.println("Inserting values 10 to 20");
			for (int i = 1, j = 10; i <= 20; i++, j++) {
				System.out.println("Inserting " + j + " into the AVL Tree");
				tree.insert(i, j);

				tree.printTree2(treeWriter);
				treeWriter.flush();

				System.out.println();

				while ((content = in.read()) != -1) {
					System.out.print((char) content);
				}
				System.out.println();
				System.out.println();
			}
			treeWriter.close();
			in.close();
		}

		catch (Exception ex) {
			System.out.println("error");
			ex.printStackTrace();
		}

		ArrayList<Integer> mahList = tree.inorderTraversal();

		System.out.println(Arrays.toString(mahList.toArray()));

		System.out.print("Printing balance: ");
		tree.printBalance();
	}
}