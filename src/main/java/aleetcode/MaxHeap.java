package aleetcode;

public class MaxHeap<T extends Comparable>{

    protected int count;
    protected T [] data;
    protected int capacity;

    // 构造函数, 构造一个空堆, 可容纳capacity个元素
    public MaxHeap(int capacity) {
        count=0;
        data=(T [] )new Comparable[capacity];
        this.capacity=capacity;
    }

    // 构造函数, 通过一个给定数组创建一个最大堆
    // 该构造堆的过程, 时间复杂度为O(n)
    public MaxHeap(T arr[]){

        int n=arr.length;
        data=(T [])new Comparable[n+1];
        this.capacity=n;
        count=n;
        for( int i = 0 ; i < n ; i ++ )
            data[i+1] = arr[i];
        for( int i = count/2 ; i >= 1 ; i -- )
            shiftDown(i);

    }
    // 向最大堆中插入一个新的元素 item
    public void insert(T item){

        assert count + 1 <= capacity;
        count++;
        data[count]=item;
        shiftUp(count);

    }
    // 从最大堆中取出堆顶元素, 即堆中所存储的最大数据
    public T extractMax(){
        assert count > 0;
        T ret = data[1];

        swap( 1 , count );
        count --;
        shiftDown(1);

        return ret;
    }

    //********************
    //* 最大堆核心辅助函数
    //********************

    private void shiftUp(int k) {
        while(k>1 && data[k/2].compareTo(data[k])>0){
            swap(k,k/2);
            k/=2;//向上找
        }
    }

    private void shiftDown(int i) {

        while(i*2<=count){
            int j=i*2;//左儿子
            // data[j] 是 data[2*k]和data[2*k+1]中的最大值
            if( j+1 <= count && data[j+1].compareTo(data[j]) > 0 )
                j ++;
            if( data[i].compareTo(data[j]) >= 0 ) break;
            swap(i, j);
            i = j;
        }

    }


    private void swap(int i, int j){
        T temp=data[i];
        data[i]=data[j];
        data[j]=temp;
    }



}
