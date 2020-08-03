package pres.hjc.community.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    public static final String REPLACE = "***";

    private TreeNode rootNode = new TreeNode();

    /**
     * 初始化 过滤树
     */
    @PostConstruct
    private void init(){

        try (
                InputStream stream = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(stream)));
                ) {
            String key;
            while (((key = reader.readLine()) != null)){
                    // 添加到 前缀树
                this.addKeyWord(key);
            }
        } catch (IOException e) {
            log.error("loader file fail -> {}" , e.getMessage());
        }

    }

    /**
     * 添加 敏感字
     * 到前缀树
     * 检索 词组
     * @param key key
     */
    private void addKeyWord(String key) {
        TreeNode node = rootNode;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            // 是否 存在 次节点
            TreeNode node1 = node.getNode(c);
            if (null == node1){
                // 初始化 子节点
                node1 = new TreeNode();
                // 记录子节点
                node.addNode(c  , node1);
            }

            // 指针 指向当前 节点
            node = node1;

            // 敏感词
            if (i == key.length() - 1){
                node.setSensitive(true);
            }
        }

    }


    /**
     * 过滤
     * @param text
     * @return
     */
    public String filter(String text){
        if (StringUtils.isBlank(text)){
            return null;
        }

        //双指针 指向 节点

        //root
        TreeNode temp = this.rootNode;

        int p1 = 0;
        int p2 = 0;

        StringBuilder builder = new StringBuilder();
        while (p2 < text.length()){
            char c = text.charAt(p2);

            // 排除 特殊符号
            if(isSymbol(c)){
                // 头节点是符号， 记录并移动指针
                // 跳过 特殊符号
                if (temp == this.rootNode){
                    builder.append(c);
                    p1++;
                }

                p2++;
                continue;
            }

            // 下节点
            temp = temp.getNode(c);
            if (temp == null){
                builder.append(text.charAt(p1));
                // 下一个位置
                p2 = ++p1;
                // 重新 指向头
                temp =  this.rootNode;
            }else if ( temp.isSensitive()){
                // 处理敏感词汇
                // 敏感词汇替换
                builder.append(REPLACE);
                // 下一方向
                p1 = ++p2;


            }else {
                p2 ++;
            }
        }
        // 处理剩余字符
        builder.append(text.substring(p1));
        return builder.toString();
    }

    /**
     * 符号验证
     * @param character
     * @return
     */
    private boolean isSymbol(Character character){
        // CharUtils.isAsciiAlphanumeric(character) //
        // ( character < 0x2E80 || character > 0x9FFF) // 东亚文字范围外
        return !CharUtils.isAsciiAlphanumeric(character) && ( character < 0x2E80 || character > 0x9FFF);
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
