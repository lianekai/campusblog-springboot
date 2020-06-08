$(function(){
    const data4Vue = {
        uri:'articles',
        beans: [],
        keyword: '',
        total: 0,
        sort: 1,
        pagination:{}
    };
    //ViewModel
    const vue = new Vue({
        el: '#app',
        data: data4Vue,
        mounted:function(){ //mounted　表示这个 Vue 对象加载成功了
            this.list(0);
        },
        methods: {
            list:function(start){
                var keyword = getUrlParms("keyword");
                const url = this.uri+"/search_result?keyword="+keyword+"&start="+start+"&sort="+this.sort;
                axios.get(url).then(function(response) {
                    vue.pagination = response.data;
                    vue.beans = response.data.content;
                    vue.total = vue.pagination.totalElements;
                });
            },
            jump: function(page){
                jump(page,vue); //定义在adminHeader.html 中
            },
            jumpByNumber: function(start){
                jumpByNumber(start,vue);
            },
            search: function () {
                window.location.href = "/search?keyword="+vue.keyword;
            },
            sortResult: function (sort) {
                vue.sort = sort;
                vue.list(0);
            }
        }
    });
});