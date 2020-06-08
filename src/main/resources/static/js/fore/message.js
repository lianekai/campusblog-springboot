$(function(){
    const data4Vue = {
        uri:'feed',
        countAll: 0,
        comments: [],
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
                const url = this.uri + "/count_message_all";
                axios.get(url).then(function(response) {
                    vue.countAll = response.data.data.feedCount;
                });
            },
            list:function(start){
                const url = this.uri+"/message_of_page?start="+start+"&size=15";
                axios.get(url).then(function(response) {
                    vue.pagination = response.data;
                    vue.comments = response.data.content;
                });
            },
            allOfRead:function(){
                const url = "feed/all_message_read";
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