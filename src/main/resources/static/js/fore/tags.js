$(function(){
    const data4Vue = {
        uri:'tags',
        countAll: 0,
        beans: [],
        tag: {id:0,categoryName:''},
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
                const url = this.uri + "/count_all";
                axios.get(url).then(function(response) {
                    vue.countAll = response.data.data.countAll;
                });
            },
            list:function(start){
                const url = this.uri+"_of_page?start="+start+"&size=50";
                axios.get(url).then(function(response) {
                    vue.pagination = response.data;
                    vue.beans = response.data.content;
                });
            },
            jump: function(page){
                jump(page,vue); //定义在adminHeader.html 中
            },
            jumpByNumber: function(start){
                jumpByNumber(start,vue);
            }
        }
    });
});