$(function(){
    var data4VueAll = {
        uri:'/count_visit_all',
        uriYesterday:'count_visit_yesterday',
        uriUser:'count_user_all',
        uriArticle:'count_article_all',
        countAll: 0,
        countYesterday: 0,
        countUserAll: 0,
        countArticleAll: 0
    };
    //ViewModel
    var vue = new Vue({
        el: '#app',
        data: data4VueAll,
        mounted:function(){ //mounted　表示这个 Vue 对象加载成功了
            this.list();
        },
        methods: {
            list:function(){
                var url =  this.uri;
                axios.get(url).then(function(response) {
                    vue.countAll = response.data.data.countAll;
                });
                axios.get(this.uriYesterday).then(function(response) {
                    vue.countYesterday = response.data.data.countYesterday;
                });
                axios.get(this.uriUser).then(function(response) {
                    vue.countUserAll = response.data.data.countUserAll;
                });
                axios.get(this.uriArticle).then(function(response) {
                    vue.countArticleAll = response.data.data.countArticleAll;
                });
            }
        }
    });
});