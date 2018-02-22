import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NodeAVL<Key extends Comparable<Key>, Value> implements Comparable<NodeAVL<Key, Value>> {
	private Key key;
	private Value value;
	private NodeAVL<Key, Value> leftNode;
	private NodeAVL<Key, Value> rightNode;
	private NodeAVL<Key, Value> parentNode;
	private int height;
	private int balanceFactor;

	public NodeAVL(Key k, Value v) {
		this.key = k;
		this.value = v;
	}

	public NodeAVL(Key k, Value v, NodeAVL<Key, Value> parent) {
		this.key = k;
		this.value = v;
		this.parentNode = parent;
		this.height = 1;
	}

	// Hash Map
	public void setValue(Value v) {
		this.value = v;
	}

	public Value getValue() {
		return this.value;
	}

	public boolean hasSameKey(Key k) {
		return this.key == k;
	}

	public Key getKey() {
		return this.key;
	}

	// AVL operations
	public void setHeight(int h) {
		this.height = h;
	}

	public int getHeight() {
		return this.height;
	}

	public void setBalanceFactor(int bf) {
		this.balanceFactor = bf;
	}

	public int getBalanceFactor() {
		return this.balanceFactor;
	}

	// Parent Node
	public NodeAVL<Key, Value> getParentNode() {
		return this.parentNode;
	}

	public void setParentNode(NodeAVL<Key, Value> parentNode) {
		this.parentNode = parentNode;
	}

	// Left-Sub-Tree
	public boolean hasLeftNode() {
		return leftNode != null;
	}

	public void changeLeftNode(NodeAVL<Key, Value> leftNode) {
		this.leftNode = leftNode;
	}

	public NodeAVL<Key, Value> getLeftNode() {
		return this.leftNode;
	}

	// Right-Sub-Tree
	public boolean hasRightNode() {
		return rightNode != null;
	}

	public NodeAVL<Key, Value> getRightNode() {
		return this.rightNode;
	}

	public void changeRightNode(NodeAVL<Key, Value> rightNode) {
		this.rightNode = rightNode;
	}

	// Get the smallest descendant
	public Value getMin() {
		if (this.hasLeftNode()) {
			return this.leftNode.getMin();
		}

		else {
			return this.value;
		}
	}

	// Get the largest descendant
	public Value getMax() {
		if (this.hasRightNode()) {
			return this.rightNode.getMax();
		}

		else {
			return this.value;
		}
	}

	@Override
	public int compareTo(NodeAVL<Key, Value> o) {
		return this.key.compareTo(o.key);
	}

	/*******************************
	 * Print Tree
	 *******************************/
	public void printTree(NodeAVL<Key, Value> root) {
		printNode(root); // Print tree-like structure
	}

	/*******************************
	 * BST Printer Class @Michal Kreuzman
	 *******************************/
	private <Key extends Comparable<Key>> void printNode(NodeAVL<Key, Value> root) {
		int maxLevel = maxLevel(root) + 2;

		printNode(Collections.singletonList(root), 1, maxLevel);
	}

	private <Key extends Comparable<Key>> void printNode(List<NodeAVL<Key, Value>> nodes, int level, int maxLevel) {
		if (nodes.isEmpty() || isAllElementsNull(nodes))
			return;

		int floor = maxLevel - level;
		int endgeLines = (int) Math.pow(2, (Math.max(floor - 1, 0)));
		int firstSpaces = (int) Math.pow(2, (floor));
		int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 1;

		printWhitespaces(firstSpaces);

		List<NodeAVL<Key, Value>> newNodes = new ArrayList<NodeAVL<Key, Value>>();
		for (NodeAVL<Key, Value> node : nodes) {
			if (node != null) {
				System.out.print(node.getValue());
				newNodes.add(node.getLeftNode());
				newNodes.add(node.getRightNode());
			}

			else {
				newNodes.add(null);
				newNodes.add(null);
				System.out.print(" ");
			}

			printWhitespaces(betweenSpaces);
		}
		System.out.println("");

		for (int i = 1; i <= endgeLines; i++) {
			for (int j = 0; j < nodes.size(); j++) {
				printWhitespaces(firstSpaces - i);
				if (nodes.get(j) == null) {
					printWhitespaces(endgeLines + endgeLines + i + 1);
					continue;
				}

				if (nodes.get(j).getLeftNode() != null)
					System.out.print("/");

				else
					printWhitespaces(1);

				printWhitespaces(i + i - 1);

				if (nodes.get(j).getRightNode() != null)
					System.out.print("\\");

				else
					printWhitespaces(1);

				printWhitespaces(endgeLines + endgeLines - i);
			}
			System.out.println("");
		}
		printNode(newNodes, level + 1, maxLevel);
	}

	private void printWhitespaces(int count) {
		for (int i = 0; i < count; i++)
			System.out.print(" ");
	}

	private <Key extends Comparable<Key>> int maxLevel(NodeAVL<Key, Value> node) {
		if (node == null)
			return 0;

		else
			return Math.max(maxLevel(node.getLeftNode()), maxLevel(node.getRightNode())) + 1;
	}

	private <V> boolean isAllElementsNull(List<V> list) {
		for (Object object : list) {
			if (object != null)
				return false;
		}
		return true;
	}

	/*******************************
	 * Print Tree 2.0
	 *******************************/
	public void printTree(OutputStreamWriter out) throws IOException {
		if (this.getRightNode() != null) {
			this.getRightNode().printTree(out, true, "");
		}

		printNodeValue(out);

		if (this.getLeftNode() != null) {
			this.getLeftNode().printTree(out, false, "");
		}
	}

	private void printNodeValue(OutputStreamWriter out) throws IOException {
		if (this.getValue() == null) {
			out.write("<null>");
		}

		else {
			out.write(this.getValue().toString());
		}
		out.write('\n');
	}

	// use string and not stringbuffer on purpose as we need to change the indent at each recursion
	private void printTree(OutputStreamWriter out, boolean isRight, String indent) throws IOException {
		if (this.getRightNode() != null) {
			this.getRightNode().printTree(out, true, indent + (isRight ? "        " : " |      "));
		}

		out.write(indent);

		if (isRight) {
			out.write(" /");
		}

		else {
			out.write(" \\");
		}

		out.write("----- ");

		printNodeValue(out);

		if (this.getLeftNode() != null) {
			this.getLeftNode().printTree(out, false, indent + (isRight ? " |      " : "        "));
		}
	}
}