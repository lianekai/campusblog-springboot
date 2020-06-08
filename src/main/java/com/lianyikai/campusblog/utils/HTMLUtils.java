package com.lianyikai.campusblog.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLUtils {
    /**
     * 从html中获取摘要。去除html代码和code片段
     * @param html
     * @param length 摘要长度
     * @return
     */
    public static String summary(String html, int length) {
        Document document = Jsoup.parse(html);
        document.select("code").remove();

        String onlyText = document.text(); // 只提取纯文本内容
        String desc = onlyText.substring(0, onlyText.length() > length ? length : onlyText.length());

        return desc;
    }

    /*
     * 抓取文章图片
     * */
    public static List<String> getImgSrc(String htmlStr) {
        String img = "";
        Pattern p_image;
        Matcher m_image;
        List<String> pics = new ArrayList<String>();
//		 String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            img = img + "," + m_image.group();
            // Matcher m =
            // Pattern.compile("src=\"?(.*?)(\"|>|\\s+)").matcher(img); //匹配src
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        return pics;
    }
}
