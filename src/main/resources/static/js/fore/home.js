$(function(){
    const data4Vue = {
        uri: 'user',
        message: '',
        show: false,
        user: {birthdayStr: "0000-00-00", face: ""},
        file: ""
    };
    //ViewModel
    const vue = new Vue({
        el: '#app',
        data: data4Vue,
        mounted:function(){ //mounted　表示这个 Vue 对象加载成功了
            this.user_info();
        },
        methods: {
            user_info:function(){
                const url = this.uri + "/info";
                axios.get(url).then(function(response) {
                    vue.user = response.data;
                    vue.file = response.data.face;
                    document.getElementById("doc-datepicker").value=vue.user.birthdayStr;
                });
            },
            update:function(){
                const url = this.uri+"/update";
                vue.user.birthdayStr = document.getElementById("doc-datepicker").value;
                vue.user.face = vue.file;
                axios.put(url, vue.user).then(function(response) {
                    if (response.data.id > 0) {
                        vue.show = true;
                        vue.message = "更新成功";
                        setTimeout(function () {
                            vue.show = false;
                        }, 3000)
                    } else {
                        vue.file = response.data.image;
                        vue.show = true;
                        vue.message = "上传失败";
                        setTimeout(function () {
                            vue.show = false;
                        }, 3000)
                    }
                });
            },
            thisFace: function (event) {
                const url = this.uri+"/upload";
                var formData = new FormData();
                formData.append("upload", event.target.files[0]);
                axios.post(url, formData).then(function(response) {
                    if (response.data.uploaded === 1) {
                        vue.file = response.data.image;
                        vue.show = true;
                        vue.message = "上传成功";
                        setTimeout(function () {
                            vue.show = false;
                        }, 3000)
                    } else {
                        vue.show = true;
                        vue.message = "上传失败";
                        setTimeout(function () {
                            vue.show = false;
                        }, 3000)
                    }
                });
            }
        }
    });
});