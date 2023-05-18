package java8;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 重写hashcode和equals方法
 */
public class JourneyDO {

    private String name;

    private List<String> journeyList;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getJourneyList() {
        return journeyList;
    }

    public void setJourneyList(List<String> journeyList) {
        this.journeyList = journeyList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JourneyDO journeyDO = (JourneyDO) o;
        // 这里Objects.equals两个集合为什么能比较？ 因为是两个string的 已经重写过string的equals方法了
        return Objects.equals(name, journeyDO.name) && Objects.equals(journeyList, journeyDO.journeyList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, journeyList);
    }

    public static void main(String[] args) {
        List<JourneyDO> journeyDOList = new ArrayList<>();
        JourneyDO journeyDO = new JourneyDO();
        journeyDO.setName("jack");
        journeyDO.setJourneyList(Lists.newArrayList("1","2"));

        JourneyDO journeyDO2 = new JourneyDO();
        journeyDO2.setName("jack");
        journeyDO2.setJourneyList(Lists.newArrayList("1","2"));
        journeyDOList.add(journeyDO);
        journeyDOList.add(journeyDO2);

        List<JourneyDO> collect = journeyDOList.stream().distinct().collect(Collectors.toList());
        System.out.println(JSON.toJSONString(collect));

        System.out.println(Objects.equals(Lists.newArrayList("1","2","3","3"),Lists.newArrayList("1","2","3","3")));

    }
}
