package java8.lambda;

import com.google.common.collect.Lists;
import java8.JourneyDO;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 * list去重
 */
public class IListTest {

    public static void main(String[] args) {
        JourneyDO journeyDO = new JourneyDO();
        journeyDO.setName("aaa");
        JourneyDO journeyDO2 = new JourneyDO();
        journeyDO2.setName("aaa");
        ArrayList<JourneyDO> journeyDOS = Lists.newArrayList(journeyDO,journeyDO2);
        Set<JourneyDO> uniqueSet = new TreeSet<>((o1, o2) -> o1.getName().compareTo(o2.getName()));
        uniqueSet.addAll(journeyDOS);
        for (JourneyDO aDo : uniqueSet) {
            System.out.println(aDo.getName());
        }
    }

}
