package io.github.lujianbo.mqtt.util;

import org.apache.commons.lang3.StringUtils;

/**
 * topic 通过 "/" 来定义和分割 topic的级别
 * 通配符号 "#" 意味着匹配任意topic，# 要么独立存在(订阅所有频道)，要么处于最后一个，其余情况属于非法行为，获取所有字节点的信息
 * 通配符号 "+" 和 "#" 不同，"+" 只匹配当前级别，获取所有兄弟结点的信息
 * 通配符"$","$" 开头的情况下，将不会匹配 "#" 和 "+",$ 是 特殊topic
 */
public class TopicWildcards {

    String topicName;

    public String[] process(String topicName) {
        String[] result = StringUtils.split(topicName, "/");
        if (result[0].startsWith("$")) {

        } else {
            for (String str : result) {
                if (str.equals("#")) {

                }
                if (str.equals("+")) {

                }
            }
        }
        return result;
    }
}
