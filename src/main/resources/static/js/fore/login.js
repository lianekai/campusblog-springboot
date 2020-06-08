$(function(){
    const data4Vue = {
        uri:'login',
        message: '',
        show: false,
        rememberMe: false,
        user:{username:'',password:''}
    };
    //ViewModel
    const vue = new Vue({
        el: '#app',
        data: data4Vue,
        mounted:function(){ //mounted　表示这个 Vue 对象加载成功了

        },
        methods: {
            login: function () {
                if (isEmpty(this.user.username)) {
                    vue.message = "\"昵称\"不能为空!";
                    vue.show = true;
                    setTimeout(function () {
                        vue.show = false;
                    }, 3000)
                    return;
                }
                if (isEmpty(this.user.password)) {
                    vue.message = "\"密码\"不能为空!";
                    vue.show = true;
                    setTimeout(function () {
                        vue.show = false;
                    }, 3000)
                    return;
                }

                const url = this.uri + "?rememberMe=" + vue.rememberMe;
                axios.post(url,this.user).then(function(response){
                    if (response.data.code == 1) {
                        location.href = "/";
                    } else {
                        vue.show = true;
                        vue.message = "昵称或密码错误!";
                        setTimeout(function () {
                            vue.show = false;
                        }, 3000)
                    }
                }).catch(function (error) {
                    vue.show = true;
                    vue.message = error;
                    setTimeout(function () {
                        vue.show = false;
                    }, 3000)
                });
            },
            closeAlert: function() {
                vue.show = false;
            }
        }
    });
});