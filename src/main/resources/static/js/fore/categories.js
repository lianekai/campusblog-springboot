$(function(){
    var data4Vue = {
        uri:'categories',
        pagination:{},
        categories:[],
        category:{id:0, name:''},
        articles:[],
        title:''
    };
    //ViewModel
    var vue = new Vue({
        el: '#app',
        data: data4Vue,
        mounted:function(){ //mounted　表示这个 Vue 对象加载成功了
            this.list_category();
        },
        methods: {
            list_category:function(){
                var cid = getUrlParms("cid");
                var name = getUrlParms("name");
                var start = getUrlParms("start");
                var url = "/all_categories";
                axios.get(url).then(function(response) {
                    vue.categories = response.data;
                    if (cid == null || cid <= 0) {
                        cid = vue.categories[0].id;
                        name = vue.categories[0].name;
                        start = 0;
                    }
                    vue.list_article(cid, name, start);
                });
            },
            list_article:function(id, name, start){
                vue.title = name;
                vue.category.id = id;
                vue.category.name = name;
                var url = "/articles/articles_of_category?cid="+id+"&start="+start;
                axios.get(url).then(function(response) {
                    vue.pagination = response.data;
                    vue.articles = response.data.content;
                });
            },
            list: function(start) {
                var url = "/articles/articles_of_category?cid="+vue.category.id+"&start="+start;
                axios.get(url).then(function(response) {
                    vue.pagination = response.data;
                    vue.articles = response.data.content;
                });
            },
            jump: function(page){
                jump(page, vue); //定义在categories.html 中
            },
            jumpByNumber: function(start){
                jumpByNumber(start, vue);
            }
        }
    });
});