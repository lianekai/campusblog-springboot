$(function(){
    const data4Vue = {
        uri:'users',
        beans: [],
        user: {id:0,status:0,role:0},
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
                const url =  "admin_user/"+this.uri+"?start="+start;
                axios.get(url).then(function(response) {
                    vue.pagination = response.data;
                    vue.beans = response.data.content;
                });
            },
            update:function (name, status) {
                const url = this.uri;
                this.user.username = name;
                this.user.status = status;
                axios.put(url,vue.user).then(function(response){
                    if (response.data.id > 0) {
                        vue.list(vue.pagination.current-1);
                    } else {
                        alert("操作失败！");
                    }
                }).catch(function (error) {
                    alert(error);
                });
            },
            authorize: function (name, role) {
                const url = this.uri + "/authorize";
                this.user.username = name;
                this.user.role = role;
                axios.put(url,vue.user).then(function(response){
                    if (response.data.id > 0) {
                        vue.list(vue.pagination.current-1);
                    } else {
                        alert("操作失败！");
                    }
                }).catch(function (error) {
                    alert(error);
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