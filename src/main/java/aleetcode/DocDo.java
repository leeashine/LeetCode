package aleetcode;

/**
 * Created by LIZIXUAN560 on 2020/12/10.
 *
 * @author LIZIXUAN560
 */
public class DocDo {
    private int docSort;
    private String name;

    public DocDo(int docSort, String name) {
        this.docSort = docSort;
        this.name = name;
    }

    public int getDocSort() {
        return docSort;
    }

    public void setDocSort(int docSort) {
        this.docSort = docSort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
