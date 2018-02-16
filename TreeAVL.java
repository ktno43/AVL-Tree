import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class TreeAVL<Key extends Comparable<Key>, Value> {
	private NodeAVL<Key, Value> root;

	public TreeAVL() {
		this.root = null;
	}

	// Get height for node n
	public int getHeight(NodeAVL<Key, Value> n) {
		if (n == null)
			return 0;

		return n.getHeight();
	}

	public Value getMin() {
		return this.root.getMin();
	}

	public Value getMax() {
		return this.root.getMax();
	}

	// Recalculate height for node n
	private void reHeight(NodeAVL<Key, Value> n) {
		if (n != null) {
			n.setHeight(1 + Math.max(getHeight(n.getLeftNode()), getHeight(n.getRightNode())));
		}

	}

	// Set Balance factor of node n
	private void setBalance(NodeAVL<Key, Value> n) {
		reHeight(n);
		n.setBalanceFactor(getHeight(n.getRightNode()) - getHeight(n.getLeftNode()));
	}

	// Insert
	public void insert(Key k, Value v) {
		if (this.root == null) {
			this.root = new NodeAVL<Key, Value>(k, v, null);
			return;
		}

		NodeAVL<Key, Value> currentNode = this.root;

		while (true) {
			if (currentNode.hasSameKey(k)) {
				currentNode.setValue(v);
			}

			NodeAVL<Key, Value> parentNode = currentNode;
			boolean goLeft = k.compareTo(currentNode.getKey()) < 0;
			currentNode = goLeft ? currentNode.getLeftNode() : currentNode.getRightNode();

			if (currentNode == null) {
				if (goLeft) {
					parentNode.changeLeftNode(new NodeAVL<Key, Value>(k, v, parentNode));
				}

				else {
					parentNode.changeRightNode(new NodeAVL<Key, Value>(k, v, parentNode));
				}
				reBalance(parentNode);
				break;
			}
		}
	}

	private void delete(NodeAVL<Key, Value> n) {
		if (n.getLeftNode() == null && n.getRightNode() == null) {
			if (n.getParentNode() == null) {
				this.root = null;
			}
			else {
				NodeAVL<Key, Value> parentNode = n.getParentNode();
				if (parentNode.getLeftNode() == n) {
					parentNode.changeLeftNode(null);
				}
				else {
					parentNode.changeRightNode(null);
				}
				reBalance(parentNode);
			}
			return;
		}

		if (n.getLeftNode() != null) {
			NodeAVL<Key, Value> child = n.getLeftNode();
			while (child.getRightNode() != null)
				child = child.getRightNode();
			n.hasSameKey(child.getKey());
			delete(child);
		}
		else {
			NodeAVL<Key, Value> child = n.getRightNode();
			while (child.getLeftNode() != null)
				child = child.getLeftNode();
			n.hasSameKey(child.getKey());
			delete(child);
		}
	}

	public void delete(Key k) {
		if (root == null)
			return;

		NodeAVL<Key, Value> child = this.root;
		while (child != null) {
			NodeAVL<Key, Value> node = child;
			boolean cmpValue = k.compareTo(node.getKey()) >= 0;
			child = cmpValue ? node.getRightNode() : node.getLeftNode();
			if (node.hasSameKey(k)) {
				delete(node);
				return;
			}
		}
	}

	private void reBalance(NodeAVL<Key, Value> n) {
		setBalance(n);

		if (getBalance(n) == -2) {
			if (getHeight(n.getLeftNode().getLeftNode()) >= getHeight(n.getLeftNode().getRightNode())) {
				n = rightRotate(n);
			}

			else
				n = leftRightRotate(n);
		}

		else if (getBalance(n) == 2) {
			if (getHeight(n.getRightNode().getRightNode()) >= getHeight(n.getRightNode().getLeftNode())) {
				n = leftRotate(n);
			}

			else
				n = rightLeftRotate(n);
		}

		if (n.getParentNode() != null) {
			reBalance(n.getParentNode());
		}

		else {
			this.root = n;
		}
	}

	// Get Balance factor of node n
	private int getBalance(NodeAVL<Key, Value> n) {
		return n.getBalanceFactor();
	}

	/*-
	 * LL Imbalance: Right Rotate
	 * T1, T2, T3 and T4 are subtrees.
	 *          a                                      b 
	 *         / \                                   /   \  
	 *        b   T4      Right Rotate (a)          c      a            
	 *       / \          - - - - - - - - ->      /  \    /  \
	 *      c   T3                               T1  T2  T3  T4        
	 *     / \             
	 *   T1   T2   
	 */

	private NodeAVL<Key, Value> rightRotate(NodeAVL<Key, Value> a) {
		NodeAVL<Key, Value> b = a.getLeftNode();
		b.setParentNode(a.getParentNode());

		a.changeLeftNode(b.getRightNode());

		if (a.getLeftNode() != null)
			a.getLeftNode().setParentNode(a);

		b.changeRightNode(a);
		a.setParentNode(b);

		if (b.getParentNode() != null) {
			if (b.getParentNode().getRightNode() == a) {
				b.getParentNode().changeRightNode(b);
			}

			else {
				b.getParentNode().changeLeftNode(b);
			}
		}

		setBalance(a);
		setBalance(b);

		return b;
	}

	/*-
	 * RR Imbalance: Left Rotate
	 * T1, T2, T3 and T4 are subtrees.
	 *   a                                b
	 *  /  \                            /   \ 
	 * T1   b     Left Rotate(a)       a      c       
	 *     /  \   - - - - - - - ->    / \    / \
	 *    T2   c                     T1  T2 T3  T4
	 *        / \  
	 *       T3  T4        
	 */
	private NodeAVL<Key, Value> leftRotate(NodeAVL<Key, Value> a) {
		NodeAVL<Key, Value> b = a.getRightNode();
		b.setParentNode(a.getParentNode());

		a.changeRightNode(b.getLeftNode());

		if (a.getRightNode() != null)
			a.getRightNode().setParentNode(a);

		b.changeLeftNode(a);
		a.setParentNode(b);

		if (b.getParentNode() != null) {
			if (b.getParentNode().getRightNode() == a) {
				b.getParentNode().changeRightNode(b);
			}

			else {
				b.getParentNode().changeLeftNode(b);
			}
		}

		setBalance(a);
		setBalance(b);

		return b;
	}

	/*-
	 * LR Imbalance: Left Rotate -> Right Rotate
	 * T1, T2, T3 and T4 are subtrees. 
	 *      a                               a                           c 
	 *     / \                            /   \                        /  \  
	 *    b   T4  Left Rotate (b)        c    T4  Right Rotate(a)    b      a 
	 *   / \      - - - - - - - - ->    /  \      - - - - - - - ->  / \    / \ 
	 * T1   c                          b    T3                    T1  T2 T3  T4 
	 *     / \                        / \ 
	 *   T2   T3                    T1   T2    
	 */
	private NodeAVL<Key, Value> leftRightRotate(NodeAVL<Key, Value> a) {
		a.changeLeftNode(leftRotate(a.getLeftNode()));
		return rightRotate(a);
	}

	/*-
	* RL Imbalance: Right Rotate -> Left Rotate
	* T1, T2, T3 and T4 are subtrees. 
	*    a                            a                            c
	*   / \                          / \                          /  \ 
	* T1   b   Right Rotate (b)    T1   c      Left Rotate(a)   a      b
	*     / \  - - - - - - - - ->     /  \   - - - - - - - ->  / \    / \
	*    c   T4                      T2   b                  T1  T2  T3  T4
	*   / \                              /  \
	* T2   T3                           T3   T4
	*/
	private NodeAVL<Key, Value> rightLeftRotate(NodeAVL<Key, Value> a) {
		a.changeRightNode(rightRotate(a.getRightNode()));
		return leftRotate(a);
	}

	public ArrayList<Value> preorderTraversal() {
		ArrayList<Value> resultList = new ArrayList<Value>();
		preorderIterator(this.root, resultList);

		return resultList;
	}

	public void preorderIterator(NodeAVL<Key, Value> node, ArrayList<Value> preorderList) {
		if (node != null) {
			// add node to preorderList
			preorderList.add(node.getValue());
			// traverse left subTreeAVL
			preorderIterator(node.getLeftNode(), preorderList);
			// traverse right subTreeAVL
			preorderIterator(node.getRightNode(), preorderList);
		}
	}

	public ArrayList<Value> inorderTraversal() {
		ArrayList<Value> resultList = new ArrayList<Value>();
		inorderIterator(this.root, resultList);

		return resultList;
	}

	public void inorderIterator(NodeAVL<Key, Value> node, ArrayList<Value> inorderList) {
		if (node != null) {
			// traverse left subTreeAVL
			inorderIterator(node.getLeftNode(), inorderList);
			// add the node to the inorderList
			inorderList.add(node.getValue());
			// traverse right subTreeAVL
			inorderIterator(node.getRightNode(), inorderList);
		}
	}

	public ArrayList<Value> postorderTraversal() {
		ArrayList<Value> resultList = new ArrayList<Value>();
		postorderIterator(this.root, resultList);

		return resultList;
	}

	public void postorderIterator(NodeAVL<Key, Value> node, ArrayList<Value> postorderList) {
		if (node != null) {
			// traverse the left subTreeAVL
			postorderIterator(node.getLeftNode(), postorderList);
			// traverse the right subTreeAVL
			postorderIterator(node.getRightNode(), postorderList);
			// output node data
			postorderList.add(node.getValue());
		}
	}

	public void printBalance() {
		printBalance(root);
	}

	public void printTree2(OutputStreamWriter out) throws IOException {
		this.root.printTree(out);
	}

	public void printTree() {
		this.root.printTree(this.root);
	}

	private void printBalance(NodeAVL<Key, Value> n) {
		if (n != null) {
			printBalance(n.getLeftNode());
			System.out.printf("%s ", n.getBalanceFactor());
			printBalance(n.getRightNode());
		}
	}
}