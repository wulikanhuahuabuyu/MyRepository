package com.xpp.util;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.AttributeProviderContext;
import org.commonmark.renderer.html.AttributeProviderFactory;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.*;

/**
 * Markdown工具类
 */
public class MarkdownUtils {
    /**
     * 将 Markdown 格式转换成 HTML 格式
     * 解析并呈现为超文本标记语言
     *
     * @param markdown
     * @return
     */
    public static String markToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    /**
     * 增加扩展[标题锚点，表格生成]
     * Extension
     * Markdown转换成HTML
     *
     * @param markdown blog.content
     * @return
     */
    public static String markToHtmlExtensions(String markdown) {
        //html标题生成id
        Set<Extension> headingAnchorExtensions = Collections.singleton(HeadingAnchorExtension.create());
        //转换table的HTML
        List<Extension> tableExtension = Arrays.asList(TablesExtension.create());

        Parser parser = Parser.builder().extensions(tableExtension).build();
        Node document = parser.parse(markdown);

        HtmlRenderer renderer = HtmlRenderer.builder()
                .extensions(headingAnchorExtensions)
                .extensions(tableExtension)
                .attributeProviderFactory(new AttributeProviderFactory() {
                    @Override
                    public AttributeProvider create(AttributeProviderContext attributeProviderContext) {
                        return new CustomAttribute();
                    }
                }).build();
        return renderer.render(document);
    }

    /**
     * 自定义标签的属性
     */
    static class CustomAttribute implements AttributeProvider {
        @Override
        public void setAttributes(Node node, String tagName, Map<String, String> attributes) {
            //如果是链接的话
            if (node instanceof Link) {
                //改变a标签的target属性为_blank
                attributes.put("target", "_blank");
            }
            //如果是表格的话
            if (node instanceof TableBlock) {
                //使用Semantic UI的样式 <table class="ui celled table">
                attributes.put("class", "ui celled table");
            }

        }
    }

    //------------------------------------------------测试-----------------------------------------------------------------
    public static void main(String[] args) {
        String table = "| 姓名 | 年龄   | 性别   |\n" +
                "| ----- | ---- | ----- |\n" +
                "| 大哈  | 6  | 公    |\n" +
                "| 二哈  | 4 | 公 |\n" +
                "\n";
        String a = "[百度一下，你就知道！](https://www.baidu.com)";
        System.out.println(markToHtmlExtensions(a));
        System.out.println(markToHtmlExtensions(markToHtmlExtensions(table)));
    }

}
