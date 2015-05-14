import java.util.ArrayList;
import java.util.List;

public class Tree {

    private Integer key;
    private int nodes;
    private Tree left;
    private Tree right;

    public Tree(Integer key, int nodes, Tree left, Tree right){
        this.key = key;
        this.nodes = nodes;
        this.left = left;
        this.right = right;
    }

    public Tree(){
    }


    public boolean isEmpty() {
        if( this.key == null) {
            return true;
        } else { return false; }
    }

    public int size() {
        return size(this);
    }

    private int size(Tree t) {
        if (t == null || t.isEmpty()) return(0);
        else {
            return(size(t.left) + 1 + size(t.right));
        }
    }

    public boolean exists(int val) {
        if(this.key == val){
            return true;
        } else {
            while(this.getNodes() > 0){
                if(val < this.key){
                    return this.left.exists(val);
                } else {return this.right.exists(val);}
            }
        }


        return false;
    }

    public int height() {
        if(this.key == null){
            return -1;
        } else if(this.nodes == 0){
            return 0;
        } else if(this.left == null){
            return this.right.height();
        } else if(this.right == null){
            return this.left.height();
        } else{
            int left = this.left.height();
            int right = this.right.height();

            return left > right ? left+1 : right+1;

        }
    }

    public void insert(int val) {

        if(!this.contains(val)) {
            Tree res = insert(this, val);

            this.setKey(res.getKey());
            this.setNodes(res.getNodes());
            this.setLeft(res.getLeft());
            this.setRight(res.getRight());
        }

    }

    private Tree insert(Tree in, int val){
        if (in == null || in.isEmpty()){
            return  new Tree(val, 0, null, null);
        } else {
            in.setNodes(in.getNodes() + 1);
            if (val < in.getKey()){
                in.setLeft(insert(in.getLeft(), val));
            } else {
                in.setRight(insert(in.getRight(), val));
            }
        }
        return in;
    }


    private boolean contains(int x){
        return contains(x, this);
    }

    private boolean contains(int x, Tree t){
        if(t == null || t.isEmpty()){
            return false;
        }
        if(x == t.key){
            return true;
        } else if (x < t.key){
            return contains(x, t.left);
        } else {
            return contains(x, t.right);
        }
    }

    public void delete(int value) {
        Tree res = remove(this, value);
        this.key = res.key;
        this.nodes = res.nodes;
        this.left = res.left;
        this.right = res.right;
    }


    private Tree remove(Tree node, int value) {
        if (node == null) {
        } else if (node.key < value) {
            node.right = remove(node.right, value);
        } else if (node.key > value) {
            node.left = remove(node.left, value);
        } else {
            if (node.left == null && node.right == null) {
                node = null;
            } else if (node.right == null) {
                node = node.left;
            } else if (node.left == null) {
                node = node.right;
            } else {
                int min = min(node.right).key;
                node.right = remove(node.right, min);
                node.key = min;
            }
        }

        return node;
    }

    private Integer successor(Integer val){
        Tree t = successor(this, val, null);
        if( t != null){
            return t.key;
        }
        return null;
    }

    private Tree successor(Tree t, Integer val, Tree p){
        if( t == null || t.isEmpty()){
            return null;
        }

        if( val < t.key){
            return successor(t.left, val, t);
        } else if ( val > t.key){
            return successor(t.right, val, t);
        }
        if( t.right != null){
            return min(t.right);
        }

        return t;
    }

    private Tree min(Tree t){
        if( t == null || t.isEmpty()){
            return null;
        }
        while( t.left != null){
            t = t.left;
        }
        return t;
    }

    public int valueAtPosition(int k){
        List<Integer> res = new ArrayList<Integer>();
        res = getInorder(this, res);
        try {
            return res.get(k);
        }catch (IndexOutOfBoundsException e){
            throw new IllegalArgumentException("The value k is invalid.");
        }
    }

    public int position(int val) {
        List<Integer> res = new ArrayList<Integer>();
        res = getInorder(this, res);
        if(res.contains(val)){
            return res.indexOf(val);
        } else {
            Tree test = this;
            List<Integer> result = new ArrayList<Integer>();
            test.insert(val);
            result = getInorder(test, result);
            return result.indexOf(val);
        }
    }

    private List<Integer> getInorder(Tree t, List<Integer> res){
        if(t == null || t.isEmpty()){
            return res;
        }
        getInorder(t.getLeft(), res);
        res.add(t.getKey());
        getInorder(t.getRight(), res);
        return res;
    }

    public Iterable<Integer> values(int lo, int hi) {
        return null;
    }

    public void simpleBalance() {

    }

    // ########################### GETTER AND SETTER ##############################################
    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public int getNodes() {
        return nodes;
    }

    public void setNodes(int nodes) {
        this.nodes = nodes;
    }

    public Tree getLeft() {
        return left;
    }

    public void setLeft(Tree left) {
        this.left = left;
    }

    public Tree getRight() {
        return right;
    }

    public void setRight(Tree right) {
        this.right = right;
    }

    public String toString(){
        return "<" +toString(this) + "> \nNodes: " + this.nodes + "\nSize: " + this.size() +"\n";
    }

    private static String toString(Tree r){
        if(r==null)
            return "";
        else
            return toString(r.left) + r.key  + toString(r.right);
    }
}