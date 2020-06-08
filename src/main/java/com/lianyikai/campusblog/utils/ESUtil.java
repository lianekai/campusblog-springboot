package com.lianyikai.campusblog.utils;

import com.lianyikai.campusblog.service.ArticleService;
import com.lianyikai.campusblog.pojo.Article;
import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Component
public class ESUtil {
    public final static int SORT_METHOD_RELEVANCY = 1;    //按相关度排序
    public final static int SORT_METHOD_POPULARITY = 2;    //按热门程度排序
    public final static int SORT_METHOD_PUB_DATE = 3;    //按发布时间排序

    private static RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("localhost", 9200, "http")
            ));
    private static final String indexName = "myblog";
    private static ArticleService articleService;

    @Autowired
    public void setArticleService(ArticleService articleService) {
        ESUtil.articleService = articleService;
    }

    /*
    * 添加es文档
    * */
    public static void addESDocument(Article article) throws IOException {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("id", article.getId());
//        jsonMap.put("author", article.getAuthor());
//        jsonMap.put("category", article.getCategory());
        jsonMap.put("content", article.getContent());
        jsonMap.put("description", article.getDescription());
        jsonMap.put("originalAuthor", article.getOriginalAuthor());
        jsonMap.put("originalUrl", article.getOriginalUrl());
        jsonMap.put("publishDate", fmt.format(article.getPublishDate()));
        jsonMap.put("tags", article.getTags());
        jsonMap.put("title", article.getTitle());
        jsonMap.put("type", article.getType());
        jsonMap.put("updateDate", fmt.format(article.getUpdateDate()));
        IndexRequest indexRequest = new IndexRequest(indexName, "article", String.valueOf(article.getId()))
                .source(jsonMap);
        client.index(indexRequest, RequestOptions.DEFAULT);
    }

    public static void delESDocument(int id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest (indexName,"article", String.valueOf(id));
        client.delete(deleteRequest, RequestOptions.DEFAULT);
    }

    public static void updateESDocument(Article article) throws IOException {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        UpdateRequest updateRequest = new UpdateRequest(indexName, "article", String.valueOf(article.getId()))
                .doc(jsonBuilder()
                .startObject()
                .field("title", article.getTitle())
//                .field("author", article.getAuthor().getId())
                .field("originalAuthor", article.getOriginalAuthor())
                .field("originalUrl", article.getOriginalUrl())
                .field("content", article.getContent())
                .field("tags", article.getTags())
                .field("type", article.getType())
//                .field("category", article.getCategory().getId())
                .field("updateDate", fmt.format(article.getUpdateDate()))
                .field("description", article.getDescription())
                .endObject());
        client.update(updateRequest, RequestOptions.DEFAULT);
    }

    public static SearchHits search(String keyword, int start, int size, int sort) throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //关键字匹配
        MultiMatchQueryBuilder mb;
        Map<String, Float> fields = new HashMap<>();
        //模糊匹配
        fields.put("title", 6.0f);
        fields.put("description", 4.0f);
        fields.put("content", 1.0f);
        mb = QueryBuilders.multiMatchQuery(keyword.toLowerCase()).fields(fields);
        sourceBuilder.query(mb);
        //第几页
        sourceBuilder.from(start*size);
        //第几条
        sourceBuilder.size(size);
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("*");
        highlightBuilder.preTags("<span style=\"color:#db2828\">");
        highlightBuilder.postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);
        searchRequest.source(sourceBuilder);
        //匹配度从高到低
        sourceBuilder.sort(generateSorter(sort));
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();

        return hits;
    }

    /*
     * from database import document to es
     * */
    public void initDatabase2ES() throws IOException {
//		Pageable pageable = new PageRequest(0, 5);
//		Page<Product> page =productESDAO.findAll(pageable);
//        SearchRequest searchRequest = new SearchRequest(indexName);
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        //第几页
//        sourceBuilder.from(0);
//        //第几条
//        sourceBuilder.size(5);
//        searchRequest.source(sourceBuilder);
//        //匹配度从高到低
//        sourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
//
//        SearchResponse searchResponse = client.search(searchRequest);
//
//        SearchHits hits = searchResponse.getHits();
//        if(hits.getTotalHits() <= 0) {
//            List<Article> articles = articleDao.findAll();
//            for (Article article : articles) {
//                addESDocument(article);
//            }
//        }

        List<Article> articles = articleService.listOrderPubDesc();
        if (!CollectionUtils.isEmpty(articles)) {
            for (Article article : articles) {
                addESDocument(article);
            }
        }
    }

    ///////////////////////////////////// 私有方法 /////////////////////////////////////
    /**
     * 生成排序规则
     *
     * @param sortType
     * @return
     */
    private static SortBuilder generateSorter(int sortType) {
        SortBuilder sorter;
        switch (sortType) {
            case SORT_METHOD_RELEVANCY:
                //相关读排序
                sorter = SortBuilders.scoreSort();
                sorter.order(SortOrder.DESC);
                break;
            case SORT_METHOD_PUB_DATE:
                //发布日期排序
                sorter = SortBuilders.fieldSort("publishDate");
                sorter.order(SortOrder.DESC);
                break;
            default:
                sorter = SortBuilders.scoreSort();
                sorter.order(SortOrder.DESC);
                break;
        }
        return sorter;
    }
}
