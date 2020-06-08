$(function(){
    const data4Vue = {
        uri:'articles',
        beans: [],
        pagination:{},
        articles_read_count: {}
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
                const url = this.uri+"?start="+start;
                axios.get(url).then(function(response) {
                    vue.pagination = response.data;
                    vue.beans = response.data.content;

                    for (let i = 0; i < vue.beans.length; ++i) {
                        vue.$set(vue.articles_read_count, vue.beans[i].id, 0);
                        const url = "/articles/read_count";
                        axios.post(url, vue.beans[i]).then(function(response) {
                            vue.$set(vue.articles_read_count, vue.beans[i].id, response.data.data.read_count);
                        });
                    }
                });
            },
            deleteBean: function (id) {
                const url = this.uri+"/"+id;
                axios.delete(url).then(function(response){
                    if(1 === response.data.code)
                        vue.list(0);
                    else
                        alert("删除失败!");
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