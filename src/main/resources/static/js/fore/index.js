$(function(){
    var data4Vue = {
        uri:'foreindex',
        pagination:{},
        articles: [],
        mostComment: [],
        mostVisit: [],
        tags: [],
        articlesCount: 0,
        commentsCount: 0,
        visitCount: 0,
        visitorCount: 0,
        tagsCount: 0,
        keyword: '',
        articles_read_count: {},
        articles_like_count: {},
        read_count_map: {},
        like_count_map: {},
        comment_count_map: {}
    };
    //ViewModel
    var vue = new Vue({
        el: '#app',
        data: data4Vue,
        mounted:function(){ //mounted　表示这个 Vue 对象加载成功了
            this.list(0);
        },
        methods: {
            list:function(start){
                var url =  this.uri+"?start="+start;
                axios.get(url).then(function(response) {
                    vue.pagination = response.data.data.articlePage;
                    vue.articles = response.data.data.articlePage.content;
                    vue.mostComment = response.data.data.mostComment;
                    vue.mostVisit = response.data.data.mostVisit;
                    vue.tags = response.data.data.tags;
                    vue.articlesCount = response.data.data.articlesCount;
                    vue.commentsCount = response.data.data.commentsCount;
                    vue.visitCount = response.data.data.visitCount;
                    vue.visitorCount = response.data.data.visitorCount;

                    vue.read_count_map = response.data.data.readCountMap;
                    vue.like_count_map = response.data.data.likeCountMap;
                    vue.comment_count_map = response.data.data.commentCountMap;
                    // for (let i = 0; i < vue.articles.length; ++i) {
                    //     vue.$set(vue.articles_read_count, vue.articles[i].id, 0);
                    //     vue.$set(vue.articles_like_count, vue.articles[i].id, 0);
                    //     const url = "/articles/read_count";
                    //     axios.post(url, vue.articles[i]).then(function(response) {
                    //         vue.$set(vue.articles_read_count, vue.articles[i].id, response.data.data.read_count);
                    //     });
                    //     const url1 = "/articles/like_count";
                    //     axios.post(url1, vue.articles[i]).then(function(response) {
                    //         vue.$set(vue.articles_like_count, vue.articles[i].id, response.data.data.like_count);
                    //     });
                    // }
                });
            },
            jump: function(page){
                jump(page,vue); //定义在adminHeader.html 中
            },
            jumpByNumber: function(start){
                jumpByNumber(start,vue);
            },
            // countByArticle: async function(id) {
            //     var url =  "/comments/count_by_article/"+id;
            //     axios.get(url).then(function(response) {
            //         // 转换了下id，直接获取id会莫名其妙的添加背景色
            //         document.getElementById("index_comment_"+id).textContent = " " + response.data.data.count + " ";
            //     });
            // },
            // forCommentId:function(id){
            //     return "index_comment_" +id;
            // },
            forTagId:function(id){
                return "index_tag_" +id;
            },
            changeColor: function (count) {
                var value = 999;
                value -= count*10;
                return "#"+value;
            },
            search: function () {
                window.location.href = "/search?keyword="+vue.keyword;
            }
        }
    });
});