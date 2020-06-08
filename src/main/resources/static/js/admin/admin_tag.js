$(function(){
    const data4Vue = {
        uri:'tags',
        countAll: 0,
        beans: [],
        message: '',
        show: false,
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
                const url =  this.uri+"_of_page?start="+start;
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
                    } else if (response.data.id == -1) {
                        vue.message = "\"博客标签\"已存在!";
                        vue.show = true;
                    } else {
                        vue.message = "\"博客标签\"添加失败!";
                        vue.show = true;
                    }
                }).catch(function (error) {
                    vue.message = error;
                    vue.show = true;
                });
            },
            deleteBean: function (id) {
                const url = this.uri+"/"+id;
                axios.delete(url).then(function(response){
                    if(1 === response.data.code) {
                        vue.list(0);
                        vue.count();
                    } else {
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
        }
    });

    $('#add-tag').on('click', function() {
        $('#my-prompt').modal({
            relatedElement: this,
            onConfirm: function() {
                vue.addBean(document.getElementById("name").value);
            }
        });
    });
});