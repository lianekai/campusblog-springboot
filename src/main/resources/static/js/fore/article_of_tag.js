$(function(){
    var data4Vue = {
        uri:'articles',
        pagination:{},
        dates:[],
        tag:{id:0, name:''},
        articles:[],
        title:'',
        year:''
    };
    //ViewModel
    var vue = new Vue({
        el: '#app',
        data: data4Vue,
        mounted:function(){ //mounted　表示这个 Vue 对象加载成功了
            this.get_tag();
            this.list(0);
        },
        methods: {
            get_tag: function(){
                var tid = getUrlParms("tid");
                var url = "/tags/"+tid;
                axios.get(url).then(function(response) {
                    if (response.data == null || $.isEmptyObject(response.data)) {
                        window.location.href="/page404";
                        return;
                    }
                    vue.tag = response.data;
                });
            },
            list: function(start) {
                var tid = getUrlParms("tid");
                var url = this.uri + "/articles_of_tag?tid=" + tid + "&start="+start;
                axios.get(url).then(function(response) {
                    vue.pagination = response.data;
                    vue.articles = response.data.content;
                });
            },
            jump: function(page){
                jump(page, vue); //定义在categories.html 中
                var divs = document.getElementsByClassName("year-category");
                for (var div of divs) {
                    div.style.display = "none";
                }
            },
            jumpByNumber: function(start){
                jumpByNumber(start, vue);
                var divs = document.getElementsByClassName("year-category");
                for (var div of divs) {
                    div.style.display = "none";
                }
            },
            getYear: function (date) {
                return date.toString().substring(0, 4);
            },
            isDiffYear: async function (i,id) {
                if (vue.pagination.current===1&&i===0) {
                    vue.year = await vue.getYear(vue.articles[0].publishDate);
                    document.getElementById(id).style.display = "block";
                } else {
                    var thisYear = await vue.getYear(vue.articles[i].publishDate);
                    if (vue.year !== thisYear) {
                        vue.year = thisYear;
                        document.getElementById(id).style.display = "block";
                    }
                }
            }
        }
    });
});