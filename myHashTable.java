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
        String keyString = key.toString();
        int hCode = keyString.charAt(0);
        for(int i=1;i<6;i++){
            hCode += (int)keyString.charAt(i);
        }
        return hCode % 100 ;
    }

    public void tableInsert(K key, T value){
        int hCode = hashCode(key);
        if(hashSet[hCode] == null) {
            hashSet[hCode] = new AVLTree<>(new AVLNode<K, T>(key));
        }
        hashSet[hCode].setRoot(hashSet[hCode].insertNode(hashSet[hCode].getRoot(),key,value));
        //hashSet[hCode].printPre(hashSet[hCode].getRoot());
        //System.out.println("Inserted Key: "+ key +", Value: "+ value + ", Hash:" + hCode);
    }

    public void printValues(int index){
        if(hashSet[index] != null) {
            AVLTree<K, T> tree = hashSet[index];
            tree.printPre(tree.getRoot());
            System.out.println();
        }
        else
            System.out.println("EMPTY");
    }

    public AVLNode<K,T> search(K key){
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
}
