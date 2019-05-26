public class myLinkedList<T extends Comparable<T>,U extends Comparable<U>> {
    private Node<T> head;
    private int numItems;

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

    public boolean isEmpty() {
        return (head.getNext() == null);
    }

    public int size() {
        return numItems;
    }

    public void setSize(int numItems){
        this.numItems = numItems;
    }
    private Node<U> find(int index) {
        Node<U> curr = head.getNext();
        for (int i = 1; i < index; i++) {
            curr = curr.getNext();
        }
        return curr;
    }

    public U get(int index) {
        if (index >= 1 && index <= numItems) {
            Node<U> curr = find(index);
            return curr.getItem();
        } else {
            return null;
        }
    }

    public void remove(int index) {
        if (index == 1) {
           removeOne();
        } else if (index >= 1 && index <= numItems) {
            Node<U> prev = find(index - 1);
            Node<U> curr = prev.getNext();
            prev.setNext(curr.getNext());
            numItems--;
        } else {
            System.out.println("해당 인덱스에 값이 없습니다.");
        }
    }

    public void removeOne() {
        T item= head.getItem();
        head = head.getNext();
        head.setItem(item);
        numItems--;
    }
/*
    public void add(int index, T item) {
        if (index == 1) {
            addOne(item);
        } else if (index > 1 && index <= numItems + 1) {
            Node prev = find(index - 1);
            Node newNode = new Node(item, prev.getNext());
            prev.setNext(newNode);
            numItems++;
        } else {
            System.out.println("해당 위치에 값을 넣을 수 없습니다.");
        }
    }
*/
    public void addOne(U item) {
        //head.setNext(new Node(item, head.getNext()));
        Node ptr = getHead();
        for(;ptr.getNext()!=null; ptr = ptr.getNext()){}
        ptr.setNext(new Node(item));
        numItems++;
    }

    public myLinkedList<T,U> clone(){
        myLinkedList<T,U> cloneList = new myLinkedList<>(getHead().getItem());
        Node<U> ptr = getHead().getNext();
        /*Node curr = cloneList.getHead();
        for(;ptr!=null;ptr = ptr.getNext()){
            curr.setNext(new Node<U>(ptr.getItem()));
            curr = curr.getNext();
            cloneList.numItems++;
        }
        */
         for(;ptr!=null;ptr=ptr.getNext()){
             cloneList.addOne(ptr.getItem());
         }
        return cloneList;
    }

    public void print(Node curr){
        System.out.print(curr.getItem());
        if(curr.getNext() != null){
            System.out.print(" ");
            print(curr.getNext());
        }
    }
}

class Node<T extends Comparable<T>> implements Comparable<T>{
    private T data;
    private Node next;

    public Node(T data, Node next){
        this.data=data;
        this.next=next;
    }

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

    public void setItem(T data) {
        this.data = data;
    }

    @Override
    public int compareTo(T other){
        return getItem().compareTo(other);
    }
}