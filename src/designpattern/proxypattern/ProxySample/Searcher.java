package designpattern.proxypattern.ProxySample;
//抽象查询类，充当抽象主题角色
public interface Searcher {
    String DoSearch(String userId, String keyword);
}
