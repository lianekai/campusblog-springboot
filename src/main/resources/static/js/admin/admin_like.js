$(function(){
    const data4Vue = {
        uri:'likes',
        beans: [],
        notRead: 0,
        pagination:{}
    };
    //ViewModel
    const vue = new Vue({
        el: '#app',
        data: data4Vue,
        mounted:function(){ //mounted　表示这个 Vue 对象加载成功了
            this.list(0);
            this.countNotRead();
        },
        methods: {
            list:function(start){
                const url =  "admin_like/"+this.uri+"?start="+start;
                axios.get(url).then(function(response) {
                    vue.pagination = response.data;
                    vue.beans = response.data.content;
                });
            },
            countNotRead:function(){
                const url = "admin_like/count_not_read";
                axios.get(url).then(function(response) {
                    vue.notRead = response.data.data.notRead;
                });
            },
            allOfRead:function(){
                const url = "admin_like/all_of_read";
                axios.get(url).then(function(response) {
                    if (1 === response.data.code) {
                        window.location.reload();
                    }
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