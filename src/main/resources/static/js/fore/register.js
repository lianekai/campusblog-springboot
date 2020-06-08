$(function(){
    const data4Vue = {
        uri:'users',
        message: '',
        show: false,
        user:{username:'',password:'','checkPassword':''}
    };
    //ViewModel
    const vue = new Vue({
        el: '#app',
        data: data4Vue,
        mounted:function(){ //mounted　表示这个 Vue 对象加载成功了

        },
        methods: {
            register: function () {
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
                if (isEmpty(this.user.checkPassword)) {
                    vue.message = "\"验证密码\"不能为空!";
                    vue.show = true;
                    setTimeout(function () {
                        vue.show = false;
                    }, 3000)
                    return;
                }
                if (this.user.password != this.user.checkPassword) {
                    vue.message = "\"密码\"输入不一致!";
                    vue.show = true;
                    setTimeout(function () {
                        vue.show = false;
                    }, 3000)
                    return;
                }

                const url = this.uri;
                axios.post(url,this.user).then(function(response){
                    if (response.data.code == 1) {
                        vue.show = true;
                        vue.message = "注册成功!";
                        setTimeout(function () {
                            vue.show = false;
                        }, 3000)
                    } else {
                        vue.show = true;
                        vue.message = response.data.message;
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