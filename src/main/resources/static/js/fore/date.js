$(function(){
    var data4Vue = {
        uri:'articles',
        pagination:{},
        dates:[],
        category:{id:0, name:''},
        articles:[],
        title:'',
        year:''
    };
    //ViewModel
    var vue = new Vue({
        el: '#app',
        data: data4Vue,
        mounted:function(){ //mounted　表示这个 Vue 对象加载成功了
            this.list_date();
            this.list(0);
        },
        methods: {
            list_date: function(){
                var url = "/list_date";
                axios.get(url).then(function(response) {
                    vue.dates = response.data;
                    // vue.list_article(cid, name, start);
                });
            },
            countByMonth: function(date){
                var url = "/count_by_month?date="+date;
                axios.get(url).then(function(response) {
                    document.getElementById(date).innerHTML = response.data.data.count;
                });
            },
            list: function(start) {
                var url;
                var date = getUrlParms("date");
                url = this.uri+"?start="+start+"&size=10";
                if (this.title.length > 0) {
                    url = "/articles/articles_of_date?date="+this.title+"&start="+start+"&size=10";
                } else if (date != null && date.length > 0) {
                    this.title = date;
                    url = "/articles/articles_of_date?date="+date+"&start="+start+"&size=10";
                }
                axios.get(url).then(function(response) {
                    vue.pagination = response.data;
                    vue.articles = response.data.content;
                });
            },
            list_article :function(date, start) {
                vue.title = date;
                var url = "/articles/articles_of_date?date="+date+"&start="+start;
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