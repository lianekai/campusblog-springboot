$(function(){
    const data4Vue = {
        uri:'categories',
        countAll: 0,
        countArticle: 0,
        message: '',
        show: false,
        beans: [],
        category: {id:0,categoryName:''},
        pagination:{}
    };
    //ViewModel
    const vue = new Vue({
        el: '#app',
        data: data4Vue,
        mounted:function(){ //mounted　表示这个 Vue 对象加载成功了
            this.list(0);
            this.count();
        },
        methods: {
            count:function(){
                var url = "admin_category/count_all";
                axios.get(url).then(function(response) {
                    vue.countAll = response.data.data.countAll;
                });
                url = "/count_article_all";
                axios.get(url).then(function(response) {
                    vue.countArticle = response.data.data.countArticleAll;
                });
            },
            list:function(start){
                const url =  "admin_category/"+this.uri+"_of_page?start="+start;
                axios.get(url).then(function(response) {
                    vue.pagination = response.data;
                    vue.beans = response.data.content;
                });
            },
            addBean: function(name) {
                const url = this.uri+"?name="+name;
                axios.post(url,this.category).then(function(response){
                    if (response.data.id > 0) {
                        location.href = "/admin_category";
                    } else if (response.data.id == -1) {
                        vue.message = "\"博客分类\"已存在!";
                        vue.show = true;
                    } else {
                        vue.message = "\"博客分类\"添加失败!";
                        vue.show = true;
                    }
                }).catch(function (error) {
                    vue.message = error;
                    vue.show = true;
                });
            },
            deleteBean: function (id, articleCount) {
                if (articleCount > 0) {
                    vue.message = "\"博客分类\"下没有文章才能执行删除操作!";
                    vue.show = true;
                    return;
                }
                const url = this.uri+"/"+id;
                axios.delete(url).then(function(response){
                    if(1 === response.data.code) {
                        vue.list(0);
                        vue.count();
                    }
                    else {
                        vue.message = "删除失败!";
                        vue.show = true;
                    }
                });
            },
            closeAlert: function() {
                vue.show = false;
            },
            jump: function(page){
                jump(page,vue); //定义在adminHeader.html 中
            },
            jumpByNumber: function(start){
                jumpByNumber(start,vue);
            }
        },
        computed:{
            percent() {
                return function (articleCount) {
                    return ((articleCount / this.countArticle) * 100).toFixed(2);
                }
            }
        }
    });

    $('#add-category').on('click', function() {
        $('#my-prompt').modal({
            relatedElement: this,
            onConfirm: function() {
                vue.addBean(document.getElementById("name").value);
            }
        });
    });
});