package java8;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by LIZIXUAN560 on 2020/12/11.
 *
 * @author LIZIXUAN560
 */
public class BaseDO {
    private static final long serialVersionUID = 1904075534879000161L;

    public BaseDO() {
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
