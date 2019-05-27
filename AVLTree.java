class AVLNode<T extends Comparable<T>,U extends Comparable<U>> {
    private myLinkedList<T,U> itemList; //해당 노드의 값에 해당하는 linked list
    private AVLNode<T,U> leftChild;     //왼쪽 child
    private AVLNode<T,U> rightChild;    //오른쪽 child
    private int height;                 //노드의 높이

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
    public void setHeightFromChild(){       //양쪽의 child의 높이 중 더 큰 값에 1을 더해 현재 노드의 높이를 얻는다
        height = (leftHeight() > rightHeight()) ? leftHeight() + 1 : rightHeight() + 1;
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

    public boolean hasLeftChild(){
        return leftChild != null;
    }
    public boolean hasRightChild(){
        return rightChild != null;
    }
}

public class AVLTree<T extends Comparable<T>,U extends Comparable<U>>{
    //AVLNode를 노드로 갖는 Tree
    private AVLNode<T,U> root;  //Tree의 가장 위쪽 노드

    private AVLNode<T,U> singleRightRotation(AVLNode<T,U> root){
        AVLNode<T,U> node1 = root.getLeftChild();
        AVLNode<T,U> node2 = node1.getRightChild();

        node1.setRightChild(root);
        root.setLeftChild(node2);
        //노드 시계방향으로 회전
        root.setHeightFromChild();
        node1.setHeightFromChild();
        //높이 재설정
        return node1;   //회전 후 root가 된 노드 반환
    }
    private AVLNode<T,U> singleLeftRotation(AVLNode<T,U> root){
        AVLNode<T,U> node1 = root.getRightChild();
        AVLNode<T,U> node2 = node1.getLeftChild();

        node1.setLeftChild(root);
        root.setRightChild(node2);
        //노드 반시계방향 회전
        root.setHeightFromChild();
        node1.setHeightFromChild();
        //높이 재설정
        return node1;   //회전 후 root가 된 노드 반환
    }

    private AVLNode<T,U> doubleRightRotation(AVLNode<T,U> root){
        root.setLeftChild(singleLeftRotation(root.getLeftChild()));// root의 왼쪽 child를 기준으로 반시계방향 회전
        return singleRightRotation(root);   //root를 기준으로 시계방향 회전
    }

    private AVLNode<T,U> doubleLeftRotation(AVLNode<T,U> root){
        root.setRightChild(singleRightRotation(root.getRightChild()));  // root의 왼쪽 child를 기준으로 반시계방향 회전
        return singleLeftRotation(root);    //root를 기준으로 시계방향 회전

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
    public AVLNode<T, U> insertNode(AVLNode<T,U> root, T item, U location) {
        //새로운 노드를 트리에 삽입하는 메소드
        if (root != null) {
            int compare = item.compareTo(root.getItemList().getHead().getItem());
            if (compare == 0) {//현재 노드의 키 값이 item과 일치할 때
                root.getItemList().addOne(location);//현재 노드에 location 삽입
                return root;
            } //현재 노드의 키 값이 item과 다르면 키 값과 item을 비교해 적절한 child에 대해 insertNode 실행
            else if (compare > 0)
                root.setRightChild(insertNode(root.getRightChild(), item, location));
            else
                root.setLeftChild(insertNode(root.getLeftChild(), item, location));

            root.setHeightFromChild();            //child가 바뀌었으므로 높이 재 조정
            int heightDifference = root.leftHeight() - root.rightHeight();
            if (heightDifference > 1) {//왼쪽 tree의 높이가 오른쪽보다 2 이상 크면
                if (root.getLeftChild().rightHeight() > root.getLeftChild().leftHeight())
                    return doubleRightRotation(root);
                    //왼쪽 child의 오른쪽 child가 더 높을 때는 한 번 회전해도 높이 차이가 줄어들지 않으므로 두번 회전
                else
                    return singleRightRotation(root);
                //왼쪽 child의 왼쪽 child가 더 높을 때는 한 번만 회전해도 높이 차이 해소

            } else if (heightDifference < -1) {
                if (root.getRightChild().leftHeight() > root.getRightChild().rightHeight())
                    return doubleLeftRotation(root);
                    //오른쪽 child의 왼쪽 child가 더 높을 때는 한 번 회전해도 높이 차이가 줄어들지 않으므로 두번 회전
                else
                    return singleLeftRotation(root);
                //오른쪽 child의 오른쪽 child가 더 높을 때는 한 번만 회전해도 높이 차이 해소
            }
            return root;
            //높이 차가 최대 1 이하일 때
        }
        else{
                //현재 노드가 비어있으면 새 노드 삽입
                AVLNode<T, U> newNode = new AVLNode<>(item);
                newNode.getItemList().addOne(location);
                return newNode;
            }

    }
    public String printPre(AVLNode<T,U> node){    //node를 root로 하는 tree에 대해 preorder traversal 순서대로 노드 값 출력
        if(node == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(node.getItemList().getHead().getItem());  //현재 노드 출력
        if(node.hasLeftChild()) {
            sb.append(" ");
            sb.append(printPre(node.getLeftChild()));
            //현재 노드 출력 후 왼쪽 child부터 출력
        }
        if(node.hasRightChild()) {
            sb.append(" ");
            sb.append(printPre(node.getRightChild()));
            //왼쪽 child 출력이 끝나면 오른쪽 child 출력
        }
        return sb.toString();
    }

    public AVLNode<T,U> treeSearch(AVLNode<T,U> root, T item){
        //tree 내에서 item에 해당하는 AVLNode를 검색
        if(root == null){
            return null;
        }
        else {
            int compare = item.compareTo(root.getItemList().getHead().getItem());
            if (compare == 0) {
                return root;
            } else if (compare > 0)
                return treeSearch(root.getRightChild(), item);
            else
                return treeSearch(root.getLeftChild(), item);
        }
    }
}