package work;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by LIZIXUAN560 on 2020/11/3.
 *
 * @author LIZIXUAN560
 */
public class BaseDo {
    private static final long serialVersionUID = 1904075534879000161L;

    public BaseDo() {
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
