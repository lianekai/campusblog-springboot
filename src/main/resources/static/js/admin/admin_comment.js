$(function(){
    const data4Vue = {
        uri:'comments',
        countAll: 0,
        noRead: 0,
        beans: [],
        bean: {parent:{id:0}, article:{id:0}, content:''},
        pagination: {}
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
                var url = "admin_comment/count_all";
                axios.get(url).then(function(response) {
                    vue.countAll = response.data.data.countAll;
                });
                url = "/admin_comment/count_no_read";
                axios.get(url).then(function(response) {
                    vue.noRead = response.data.data.countNoRead;
                });
            },
            list:function(start){
                const url =  "admin_comment/"+this.uri+"_of_page?start="+start;
                axios.get(url).then(function(response) {
                    vue.pagination = response.data;
                    vue.beans = response.data.content;
                });
            },
            addBean: function(aid, cid, content) {
                const url = this.uri;
                this.bean.parent.id = cid;
                this.bean.article.id = aid;
                this.bean.content = content;
                axios.post(url,this.bean).then(function(response){
                    if (response.data.id > 0) {
                        location.href = "/admin_comment";
                    } else {
                        alert("回复失败!");
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
            allOfRead:function(){
                const url = "admin_comment/all_of_read";
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
            },
            reply: function (aid, cid) {
                $('#my-prompt').modal({
                    relatedElement: this,
                    // TODO 回复功能
                    onConfirm: function() {
                        vue.addBean(aid, cid, document.getElementById("name").value);
                    }
                });
            }
        }
    });
});