$(function(){
    const data4Vue = {
        uri:'links',
        beans: [],
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
                const url = this.uri+"_of_page?start="+start;
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