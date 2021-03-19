package aleetcode;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by LIZIXUAN560 on 2020/12/10.
 *
 * @author LIZIXUAN560
 */
public class DocTreeProblem {

    public static void main(String[] args) {

        List<DocDo> list= Lists.newArrayList(new DocDo(1,"n1"),new DocDo(2,"n2"),new DocDo(896,"n3"),new DocDo(896,"n4"),new DocDo(700,"n6"));
        int max = list.stream().mapToInt(DocDo::getDocSort).max().orElse(0);
        int temp=0;
        for (DocDo docDO : list) {
            if (docDO.getDocSort() > temp) {
                temp = docDO.getDocSort();
            }
        }
        assert temp==max;
        System.out.println(max);
        System.out.println(temp);
    }
}
