package aleetcode;

import java.util.*;

public class Contest185 {
    public static void main(String[] args) {

//        String s="covid2019";
//        reformat(s);
        List<List<String>> orders=new ArrayList<>();
        List<String> list=new ArrayList<>();
        list.add("David");
        list.add("3");
        list.add("Ceviche");
        orders.add(list);
        list=new ArrayList<>();
        list.add("Corina");
        list.add("10");
        list.add("Beef Burrito");
        orders.add(list);
        list=new ArrayList<>();
        list.add("David");
        list.add("3");
        list.add("Fried Chicken");
        orders.add(list);
        list=new ArrayList<>();
        list.add("Carla");
        list.add("5");
        list.add("Water");
        orders.add(list);
        list=new ArrayList<>();
        list.add("Carla");
        list.add("5");
        list.add("Ceviche");
        orders.add(list);
        list=new ArrayList<>();
        list.add("Rous");
        list.add("3");
        list.add("Ceviche");
        orders.add(list);
        new Contest185().displayTable(orders);
    }

//    集合操作题  本体特殊在要填充没点的菜即为0
//    输入：orders = [["David","3","Ceviche"],["Corina","10","Beef Burrito"],["David","3","Fried Chicken"],["Carla","5","Water"],["Carla","5","Ceviche"],["Rous","3","Ceviche"]]
//    输出：[["Table","Beef Burrito","Ceviche","Fried Chicken","Water"],["3","0","2","1","0"],["5","0","1","0","1"],["10","1","0","0","0"]]
    public List<List<String>> displayTable(List<List<String>> orders) {

        List res=new ArrayList();
        Map<String,List<Order>> map=new TreeMap<>();
        //桌号 菜单和个数
        Map<Integer,Map<String,Integer>> countMap=new TreeMap<>();
        //菜品名菜单
        List<String> nameList=new ArrayList();

        for (List<String> order:orders){
            String customerName=order.get(0);
            String tableNumber=order.get(1);
            String foodItem=order.get(2);
            if(!nameList.contains(foodItem)){
                nameList.add(foodItem);
            }
            Order orde=new Order(customerName,tableNumber,foodItem);
            List<Order> foodAndName = map.getOrDefault(tableNumber, new ArrayList<>());
            foodAndName.add(orde);
            map.put(tableNumber,foodAndName);

        }
        //先排序
        Collections.sort(nameList);
        nameList.add(0,"Table");
        res.add(nameList);
        //桌号 菜单和个数进行统计
        for(Map.Entry<String,List<Order>> entry : map.entrySet()){
            List<Order> orderList = entry.getValue();
            Map<String, Integer> foodItemCnt =  new HashMap<>();
            for(Order order:orderList){
                foodItemCnt.put(order.foodItem,foodItemCnt.getOrDefault(order.foodItem,0)+1);
            }
            countMap.put(Integer.parseInt(entry.getKey()),foodItemCnt);
        }
        //再对没点的菜填充0
        for(Map.Entry<Integer,Map<String,Integer>> entry:countMap.entrySet()){
            List<String> list1=new ArrayList<>();
            Integer tableNum = entry.getKey();
            Map<String, Integer> map1 = entry.getValue();
            for(String name:nameList){
                if(name.equals("Table"))
                    list1.add(tableNum+"");
                else{
                    int cnt=map1.getOrDefault(name,0);
                    list1.add(cnt+"");
                }
            }
            res.add(list1);
        }

        return res;

    }
    public class Order{
        String customerName;
        String tableNumber;
        String foodItem;
        public Order(String customerName, String tableNumber, String foodItem) {
            this.customerName = customerName;
            this.tableNumber = tableNumber;
            this.foodItem = foodItem;
        }
    }

//    s = "a0b1c2"
//    格式化字符串，数字和字母相邻
//    思路：统计数字和字母出现次数 要考虑数字比字母多一个或少一个的情况
    public static String reformat(String s) {
        if(s==null||s.length()==0)
            return "";
        String res="";
        String a="";
        String b="";
        for(char c:s.toCharArray()){
            if(c>='0'&&c<='9')
                a+=c;
            else
                b+=c;
        }
        if(Math.abs(a.length()-b.length())>1)
            res="";
        else{
            char []ca=a.toCharArray();
            char []cb=b.toCharArray();
            int length=ca.length>cb.length?cb.length:ca.length;
            int i=0;
            while(i<length){
                if(ca.length>=cb.length){
                    res+=ca[i];
                    res+=cb[i];
                }else {
                    res+=cb[i];
                    res+=ca[i];
                }
                i++;
            }
            if(ca.length>cb.length){
                res+=ca[i];
            }else if(ca.length<cb.length){
                res+=cb[i];
            }

        }

        return res;

    }
}
