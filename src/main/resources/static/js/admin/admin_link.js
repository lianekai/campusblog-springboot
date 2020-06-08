$(function(){
    const data4Vue = {
        uri:'links',
        countAll: 0,
        beans: [],
        link: {id:0,blogger:'',url:''},
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
                const url = "admin_link/count_all";
                axios.get(url).then(function(response) {
                    vue.countAll = response.data.data.countAll;
                });
            },
            list:function(start){
                const url = this.uri+"_of_page?start="+start;
                axios.get(url).then(function(response) {
                    vue.pagination = response.data;
                    vue.beans = response.data.content;
                });
            },
            get:function(id) {
                const url = this.uri+"/"+id;
                axios.get(url).then(function(response) {
                    vue.link = response.data;
                    document.getElementById("edit-blogger").value = vue.link.blogger;
                    document.getElementById("edit-url").value = vue.link.url;
                    $('#update-link').modal({
                        relatedElement: this,
                        onConfirm: function() {
                            vue.updateBean(document.getElementById("edit-blogger").value, document.getElementById("edit-url").value);
                        }
                    });
                });
            },
            updateBean: function(blogger, blog_url) {
                const url = this.uri;
                vue.link.blogger = blogger;
                vue.link.url = blog_url;
                axios.put(url, vue.link).then(function(response){
                    if (response.data.id > 0) {
                        location.href = "/admin_link";
                    } else {
                        alert("编辑失败!");
                    }
                }).catch(function (error) {
                    alert(error);
                });
            },
            addBean: function(blogger, blog_url) {
                const url = this.uri+"?blogger="+blogger+"&url="+blog_url;
                axios.post(url,this.category).then(function(response){
                    if (response.data.id > 0) {
                        location.href = "/admin_link";
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

    $('#add-link').on('click', function() {
        $('#my-prompt').modal({
            relatedElement: this,
            onConfirm: function() {
                vue.addBean(document.getElementById("blogger").value, document.getElementById("url").value);
            }
        });
    });
});