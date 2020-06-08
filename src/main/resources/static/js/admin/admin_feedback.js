$(function(){
    const data4Vue = {
        uri:'feedbacks',
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
                const url = "admin_feedback/count_all";
                axios.get(url).then(function(response) {
                    vue.countAll = response.data.data.countAll;
                });
            },
            list:function(start){
                const url =  "admin_feedback/"+this.uri+"_of_page?start="+start;
                axios.get(url).then(function(response) {
                    vue.pagination = response.data;
                    vue.beans = response.data.content;
                });
            },
            addBean: function(name) {
                const url = this.uri+"?name="+name;
                axios.post(url,this.category).then(function(response){
                    if (response.data.id > 0) {
                        location.href = "/admin_tag";
                    } else {
                        alert("添加失败!");
                    }
                }).catch(function (error) {
                    alert(error);
                });
            },
            deleteBean: function (id) {
                const url = this.uri+"/"+id;
                axios.delete(url).then(function(response){
                    if(1 === response.data.code) {
                        vue.list(0);
                        vue.count();
                    }
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