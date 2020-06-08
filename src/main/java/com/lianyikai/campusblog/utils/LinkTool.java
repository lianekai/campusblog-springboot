package com.lianyikai.campusblog.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;

public class LinkTool {
    private final static String PATH = getWebrootPath() + "hosts.properties";
    private final static Properties hosts = new Properties();

    private static final Logger log = LoggerFactory.getLogger(LinkTool.class);

    /**
     * 获取项目根路径
     *
     * @param key
     * @return
     */
    public final static String getWebrootPath() {
        return Thread.currentThread().getContextClassLoader().getResource("").getPath();
    }

    /**
     * 获取在 hosts.properties 中定义的各种类型文件对应的路径
     *
     * @param key
     * @return
     */
    public static String getHostOf(String key) {
        if (!StringUtils.isEmpty(key)) {
            BufferedReader bufferedReader;
            try {
                bufferedReader = new BufferedReader(new FileReader(PATH));
                hosts.load(bufferedReader);
                log.info("加载配置文件成功");
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 获取key对应的value值
            return hosts.getProperty(key);
        }
        return null;
    }
}
