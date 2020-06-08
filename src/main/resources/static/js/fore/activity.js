$(function(){
    const data4Vue = {
        uri:'feed',
        countAll: 0,
        comments: [],
        likes: [],
        pagination:{},
        pagination2:{}
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
                    vue.countAll = response.data.data.feedCount;
                });
            },
            list:function(start){
                const url = this.uri+"/activity_of_page?start="+start+"&size=15";
                axios.get(url).then(function(response) {
                    vue.pagination = response.data.comments;
                    vue.pagination2 = response.data.likes;
                    vue.comments = response.data.comments.content;
                    vue.likes = response.data.likes.content;
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