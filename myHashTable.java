import java.lang.reflect.Array;

public class myHashTable<K extends Comparable<K>,T extends Comparable<T>> {
    private int size = 100;
    private AVLTree<K,T>[] hashSet;

    public myHashTable(){
        @SuppressWarnings("unchecked")
        final AVLTree<K,T>[] hashSet= (AVLTree<K,T>[]) Array.newInstance(AVLTree.class, size);
        this.hashSet = hashSet;
    }

    public int hashCode(K key) {
        //key를 String으로 변환하고 각 자리의 ascii code 합을 구한 뒤, 그 값을 100으로 나눈 나머지 반환
        String keyString = key.toString();
        int hCode = keyString.charAt(0);
        for(int i=1;i<6;i++){
            hCode += (int)keyString.charAt(i);
        }
        return hCode % 100 ;
    }

    public void tableInsert(K key, T value){
        //hash table 내부 주어진 key에 해당하는 AVLTree에 value 삽입
        int hCode = hashCode(key);
        if(hashSet[hCode] == null)
            hashSet[hCode] = new AVLTree<>(new AVLNode<K, T>(key));
        hashSet[hCode].setRoot(hashSet[hCode].insertNode(hashSet[hCode].getRoot(),key,value));
    }

    public void printValues(int index){
        //특정 위치에 있는 AVLTree의 value를 preorder traversal 방식으로 출력
        if(hashSet[index] != null)
            System.out.println(hashSet[index].printPre(hashSet[index].getRoot()));
        else
            System.out.println("EMPTY");
    }

    public AVLNode<K,T> search(K key){  //key에 해당하는 AVLTree를 검색
        int hCode = hashCode(key);
        AVLTree<K,T> hTree = hashSet[hCode];
        if(hTree == null)
            return null;
        else
            return hTree.treeSearch(hTree.getRoot(),key);
    }

    public void clear(){
        hashSet = (AVLTree<K,T>[]) Array.newInstance(AVLTree.class, size);
    }
    // hash table에 저장된 값 모두 삭제
}
