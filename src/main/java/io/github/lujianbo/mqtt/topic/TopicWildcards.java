package io.github.lujianbo.mqtt.topic;

import java.util.Observable;

/**
 * topic 通过 "/" 来定义和分割 topic的级别
 * 通配符号 "#" 意味着匹配任意topic，# 要么独立存在(订阅所有频道)，要么处于最后一个，其余情况属于非法行为
 * 通配符号 "+" 和 "#" 不同，"+" 只匹配当前级别
 * 通配符"$","$" 开头的情况下，将不会匹配 "#" 和 "+"
 */
public class TopicWildcards {

}
