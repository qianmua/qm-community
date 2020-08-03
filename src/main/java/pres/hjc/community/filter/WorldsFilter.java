package pres.hjc.community.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/3  16:46
 * @description :
 */
@Component
@Slf4j
public class WorldsFilter {

    /**
     * 替换 符号
     */
    public static final String REPLACE = "**";

    private TreeNode rootNode = new TreeNode();

    /**
     * 初始化 过滤树
     */
    @PostConstruct
    public void init(){

    }


    /**
     * 前缀 树
     */
    private class TreeNode{

        @Getter
        @Setter
        private boolean isSensitive = false;

        // next key(word) v next Key
        private Map<Character , TreeNode> next = new HashMap<>();

        /**
         * 添加 子节点
         * 够成树
         * @param c
         * @param t
         */
        public void addNode(Character c , TreeNode t){
            next.put(c , t);
        }

        /**
         * 获取 树 v
         * @param c
         * @return
         */
        public TreeNode getNode(Character c){
            return next.get(c);
        }
    }
}
