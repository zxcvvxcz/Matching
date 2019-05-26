 class AVLNode<T extends Comparable<T>,U extends Comparable<U>> {
    private myLinkedList<T,U> itemList;
    private AVLNode<T,U> leftChild;
    private AVLNode<T,U> rightChild;
    private int height;

    public AVLNode(T item){
        itemList= new myLinkedList<>(item);
        leftChild=null;
        rightChild=null;
        height=1;
    }

    public AVLNode<T,U> getLeftChild() {
        return leftChild;
    }

    public int getHeight(){
        return height;
    }
    public int leftHeight(){
        return hasLeftChild() ? getLeftChild().getHeight() : 0;
    }
    public int rightHeight(){
        return hasRightChild() ? getRightChild().getHeight() : 0;
    }
    public void setHeightFromChild(){
        height = (leftHeight() > rightHeight()) ? leftHeight() + 1 : rightHeight() + 1;
    }
    public void setHeight(int height){
        this.height = height;
    }

    public AVLNode<T,U> getRightChild() {
        return rightChild;
    }

    public myLinkedList<T,U> getItemList() {
        return itemList;
    }

    public void setLeftChild(AVLNode<T,U> leftChild) {
        this.leftChild = leftChild;
    }

    public void setRightChild(AVLNode<T,U> rightChild) {
        this.rightChild = rightChild;
    }

    public void setItemList(myLinkedList<T,U> itemList) {
        this.itemList = itemList;
    }

    public boolean hasLeftChild(){
        return leftChild != null;
    }
    public boolean hasRightChild(){
        return rightChild != null;
    }
}

public class AVLTree<T extends Comparable<T>,U extends Comparable<U>>{
    private AVLNode<T,U> root;

    private AVLNode<T,U> singleRightRotation(AVLNode<T,U> root){
        AVLNode<T,U> node1 = root.getLeftChild();
        AVLNode<T,U> node2 = node1.getRightChild();

        node1.setRightChild(root);
        root.setLeftChild(node2);
        root.setHeightFromChild();
        node1.setHeightFromChild();
        //System.out.print("last: ");
        //printPre(node1);
        //System.out.println();
        return node1;
    }
    private AVLNode<T,U> singleLeftRotation(AVLNode<T,U> root){
        AVLNode<T,U> node1 = root.getRightChild();
        AVLNode<T,U> node2 = node1.getLeftChild();

        node1.setLeftChild(root);
        root.setRightChild(node2);
        root.setHeightFromChild();
        node1.setHeightFromChild();
        //System.out.print("last: ");
        //printPre(node1);
        //System.out.println();
        return node1;
    }

    private AVLNode<T,U> doubleRightRotation(AVLNode<T,U> root){
        root.setLeftChild(singleLeftRotation(root.getLeftChild()));
        return singleRightRotation(root);
    }

    private AVLNode<T,U> doubleLeftRotation(AVLNode<T,U> root){
        root.setRightChild(singleRightRotation(root.getRightChild()));
        return singleLeftRotation(root);
    }
    public AVLTree(AVLNode<T,U> avlNode){
        root=avlNode;
    }

    public AVLNode<T, U> getRoot() {
        return root;
    }

    public void setRoot(AVLNode<T,U> root){
        this.root = root;
    }
    public AVLNode<T, U> insertNode(AVLNode<T,U> root, T item, U location){
        if(root != null) {
            if (item.equals(root.getItemList().getHead().getItem())) {
                root.getItemList().addOne(location);
                //System.out.print("Itemlist: ");
                //for(Node n = root.getItemList().getHead().getNext(); n != null; n = n.getNext())
                //    System.out.print(n.getItem()+" ");
                //System.out.println();
                return root;
            } else{
                int compare = item.compareTo(root.getItemList().getHead().getItem());
                if (compare > 0)
                    root.setRightChild(insertNode(root.getRightChild(), item, location));
                else
                    root.setLeftChild(insertNode(root.getLeftChild(), item, location));
            }
            //System.out.print("first: ");
            //printPre(root);

            root.setHeightFromChild();
            //System.out.println("height: "+ root.getHeight());
            int heightDifference =  root.leftHeight() - root.rightHeight();
            if (heightDifference > 1) {
                if (root.getLeftChild().rightHeight() > root.getLeftChild().leftHeight()) {
                    return doubleRightRotation(root);
                }
                else{
                    return singleRightRotation(root);
                }
            } else if (heightDifference < -1) {
                AVLNode<T,U> rotatedRoot;
                if (root.getRightChild().leftHeight() > root.getRightChild().rightHeight()){
                    return  doubleLeftRotation(root);
                }
                else{
                    return  singleLeftRotation(root);
                }
            }
            return root;
        }
        else{
            AVLNode<T,U> newNode = new AVLNode<>(item);
            newNode.getItemList().addOne(location);
            return newNode;
        }
/*
        AVLNode<T> curr = null;
        AVLNode<T> next = root;

        while(next != null){
            if(item.equals(next.getItemList().getHead().getItem())){        //이미 같은 값이 저장되어있을 때
                next.getItemList().addOne(item);
                break;
            }
            else if(item.compareTo(next.getItemList().getHead().getItem()) > 0){ //저장된 값이 더 작을 때
                curr = next;
                next = next.getRightChild();
            }
            else{   //저장된 값이 더 클 때
                curr = next;
                next = next.getLeftChild();
            }
        }

        if (next == null) {
            next = new AVLNode<>(item, curr.getHeight() + 1);
        }
*/
    }
    public void printPre(AVLNode<T,U> node){
        if(node == null) {
            return;
        }
        System.out.print(node.getItemList().getHead().getItem());
        if(node.hasLeftChild()) {
            System.out.print(" ");
            printPre(node.getLeftChild());
        }
        if(node.hasRightChild()) {
            System.out.print(" ");
            printPre(node.getRightChild());
        }
    }

    public AVLNode<T,U> treeSearch(AVLNode<T,U> root, T item){
        if(root == null){
            return null;
        }
        else {
            int compare = item.compareTo(root.getItemList().getHead().getItem());
            if (item.equals(root.getItemList().getHead().getItem())) {
                return root;
            } else if (compare > 0)
                return treeSearch(root.getRightChild(), item);
            else
                return treeSearch(root.getLeftChild(), item);
        }
    }
}