package work.domain;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.Map;

public class Request {

    @JSONField(name = "company_id")
    private String companyId;

    @JSONField(name = "auth_status")
    private Integer authStatus;

    @JSONField(name = "money")
    private Float money;

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Integer getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(Integer authStatus) {
        this.authStatus = authStatus;
    }

    public static void main(String[] args) {
        Request request = new Request();
        request.setCompanyId("172637");
        request.setAuthStatus(1);
        request.setMoney(6665.32F);
        String jsonString = JSONObject.toJSONString(request);
        System.out.println(jsonString);

        Map map = (Map) JSONObject.toJSON(request);

        System.out.println(map);

    }
}
