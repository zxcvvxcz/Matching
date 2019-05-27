public class myLinkedList<T extends Comparable<T>,U extends Comparable<U>> {
    private Node<T> head;   //첫 번째 노드
    private int numItems;   //헤드를 제외한 노드 개수

    public myLinkedList(T item) {
        numItems = 0;
        head=(new Node<>(item));
    }

    public Node<T> getHead() {
        return head;
    }

    public void setHead(Node<T> head) {
        this.head = head;
    }

    public boolean isEmpty() {  //linked list가 비었는지 확인
        return (head.getNext() == null);
    }

    public void shrinkSize(){
        numItems--;
    }   //노드 개수 재설정

    public void addOne(U item) {
        //linked list에 node 하나 추가
        Node ptr = getHead();
        for(;ptr.getNext()!=null; ptr = ptr.getNext()){}
        ptr.setNext(new Node(item));
        numItems++;
    }

    public myLinkedList<T,U> clone(){   //현재 linked list를 복사해 값은 같고 주소는 다른 노드를 가진 linked list 생성
        myLinkedList<T,U> cloneList = new myLinkedList<>(getHead().getItem());
        Node<U> ptr = getHead().getNext();
        for(;ptr!=null;ptr=ptr.getNext())
            cloneList.addOne(ptr.getItem());
        return cloneList;
    }

    public void print(Node curr){   //이 노드의 뒤에 있는 노드를 순서대로 출력
        StringBuilder sb = new StringBuilder();
        for(;curr.getNext()!= null; curr=curr.getNext()) {
            sb.append(curr.getItem());
            sb.append(" ");
        }
        sb.append(curr.getItem());
        System.out.println(sb.toString());
    }
}

class Node<T extends Comparable<T>> implements Comparable<T>{
    //node 내부 값을 비교하기 위해 Comparable<T>를 implement하고 CompareTo를 구현
    private T data;
    private Node next;

    public Node(T data){
        this.data=data;
        this.next=null;
    }

    public T getItem(){
        return data;
    }

    public Node getNext(){
        return next;
    }

    public void setNext(Node next){
        this.next=next;
    }

    @Override
    public int compareTo(T other){
        return getItem().compareTo(other);
    }
}