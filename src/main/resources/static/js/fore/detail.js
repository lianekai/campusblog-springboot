$(function(){
    var aid = getUrlParms("aid");
    var detail_url = location.href.split('#')[0];
    const data4Vue = {
        share_pic: 'http://www.siriusshum.club/img/icon.jpg',
        item: '',
        uri:'articles',
        message: '',
        wsLink: '',
        show: false,
        article: {category:{id:0},author:{id:0}},
        like:{},
        comment_like:{comment:{id:0}},
        pagination:{},
        comments: [],
        commentsCount: 0,
        comment_bean: {content:'',article:{id:0},pId:0},
        liked: false,
        comment_liked: {},
        loginUser: 0,
        read_count: 0,
        isOnReply: {}
    };
    //ViewModel
    const vue = new Vue({
        el: '#app',
        data: data4Vue,
        mounted:function(){ //mounted　表示这个 Vue 对象加载成功了
            // this.getWSLink();
            this.get();
            this.is_liked();
            this.countByArticle(aid);
        },
        methods: {
            get:function(){
                const url = this.uri+"/"+aid;
                axios.get(url).then(function(response) {
                    if (response.data == null || $.isEmptyObject(response.data)) {
                        window.location.href="/page404";
                        return;
                    }
                    vue.article = response.data;
                    vue.list(0);
                    vue.getReadCount();
                    vue.wechatConfig();
                    var qrcode = new QRCode(document.getElementById("qrcode"), {
                        width : 150,
                        height : 150
                    });
                    qrcode.makeCode(detail_url);
                    if (vue.article.img != null && vue.article.img.length > 0) {
                        vue.share_pic = vue.article.img;
                        if (vue.article.img.startsWith('/img')) {
                            vue.share_pic = "http://www.siriusshum.club"+vue.article.img;
                        }
                    }
                    var socket;
                    if(typeof(WebSocket) == "undefined") {
                        console.log("您的浏览器不支持WebSocket");
                    }else{
                        // console.log("您的浏览器支持WebSocket");
                        //实现化WebSocket对象，指定要连接的服务器地址与端口建立连接
                        socket = new WebSocket("ws://www.siriusshum.club/detail/read/"+aid+"/"+vue.loginUser);
                        //打开事件
                        socket.onopen = function() {
                            // console.log("Socket 已打开");
                            //socket.send("这是来自客户端的消息" + location.href + new Date());
                        };
                        //获得消息事件
                        socket.onmessage = function(msg) {
                            // console.log(msg.data);
                            //发现消息进入开始处理前端触发逻辑
                        };
                        //关闭事件
                        socket.onclose = function() {
                            // console.log("Socket已关闭");
                        };
                        //发生了错误事件
                        socket.onerror = function() {
                            console.log("Socket发生了错误");
                            //此时可以尝试刷新页面
                        }
                        //离开页面时，关闭socket
                        //jquery1.8中已经被废弃，3.0中已经移除
                        // $(window).unload(function(){
                        //     socket.close();
                        //});
                    }
                });
            },
            wechatConfig: function() {
                const url = "article/share?aid="+aid;
                axios.get(url).then(function(response) {
                    wx.config({
                        debug: false,
                        appId: response.data.data.appId,
                        timestamp: response.data.data.timestamp,
                        nonceStr: response.data.data.nonceStr,
                        signature: response.data.data.signature,
                        jsApiList: [
                            'onMenuShareTimeline',
                            'onMenuShareAppMessage',
                            'onMenuShareQQ',
                            'onMenuShareQZone',
                            'onMenuShareWeibo'
                        ] // 必填，需要使用的JS接口列表
                    });
                    wx.ready(function() {
                        const title = vue.article.title;
                        const desc = vue.article.description;
                        const imgUrl = 'http://www.siriusshum.club/img/icon.jpg';
                        // 朋友圈
                        wx.onMenuShareTimeline({
                            title: title, // 分享标题
                            link: detail_url, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
                            imgUrl: imgUrl, // 分享图标
                            success: function () {
                                // 设置成功
                            }
                        });

                        // 微信朋友
                        wx.onMenuShareAppMessage({
                            title: title, // 分享标题
                            desc: desc, // 分享描述
                            link: detail_url, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
                            imgUrl: imgUrl, // 分享图标
                            type: 'link', // 分享类型,music、video或link，不填默认为link
                            dataUrl: '' // 如果type是music或video，则要提供数据链接，默认为空
                        });

                        // qq
                        wx.onMenuShareQQ({
                            title: title, // 分享标题
                            desc: desc, // 分享描述
                            link: detail_url, // 分享链接
                            imgUrl: imgUrl // 分享图标
                        });

                        // qq空间
                        wx.onMenuShareQZone({
                            title: title, // 分享标题
                            desc: desc, // 分享描述
                            link: detail_url, // 分享链接
                            imgUrl: imgUrl // 分享图标
                        });

                        // 腾讯微博
                        wx.onMenuShareWeibo({
                            title: title, // 分享标题
                            desc: desc, // 分享描述
                            link: detail_url, // 分享链接
                            imgUrl: imgUrl // 分享图标
                        });
                    });
                    wx.error(function(res){
                        // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
                    });
                });
            },
            is_liked: function() {
                const url = "likes/is_liked?aid="+aid;
                axios.get(url).then(function(response) {
                    vue.liked = response.data.data.liked;
                });
            },
            like_it: function () {
                const url = "/likes";
                this.like.article = vue.article;
                axios.post(url,this.like).then(function(response){
                    if (response.data.id > 0) {
                        vue.liked = true;
                        vue.get();
                    } else if (response.data.id === 0){
                        vue.show = true;
                        vue.message = "您尚未登录";
                        setTimeout(function () {
                            vue.show = false;
                        }, 3000)
                    } else {
                        vue.show = true;
                        vue.message = "服务器出错!";
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
            list: function (start) {
                const url =  "comments/list?aid=" + aid + "&start="+start;
                axios.get(url).then(function(response) {
                    vue.pagination = response.data;
                    vue.comments = response.data.content;
                    for (let i = 0; i < vue.comments.length; ++i) {
                        vue.$set(vue.isOnReply, vue.comments[i].id, 0);
                        const url = "/comments/is_liked?cid="+vue.comments[i].id;
                        axios.get(url).then(function(response) {
                            vue.$set(vue.comment_liked, vue.comments[i].id, response.data.data.liked);
                        });
                    }
                });
            },
            comment: function (cid) {
                var url = "/comments";
                this.comment_bean.article.id=aid;
                this.comment_bean.pId=cid;
                var content=document.getElementById("detail_comment_"+cid);
                if (content != null)
                    this.comment_bean.content=content.value;
                if (this.comment_bean.content.length === 0 || this.comment_bean.content.length > 100) {
                    vue.show = true;
                    vue.message = "评论为空或长度超出限制!";
                    setTimeout(function () {
                        vue.show = false;
                    }, 3000)
                    return;
                }
                axios.post(url,this.comment_bean).then(function(response) {
                    if (response.data.id > 0) {
                        vue.show = true;
                        vue.message = "评论成功!";
                        vue.comment_bean.content = "";
                        setTimeout(function () {
                            vue.show = false;
                        }, 3000)
                        vue.list(0);
                        vue.countByArticle(aid);
                    } else if(response.data.id === 0) {
                        vue.show = true;
                        vue.message = "您尚未登录";
                        setTimeout(function () {
                            vue.show = false;
                        }, 3000)
                    } else {
                        vue.show = true;
                        vue.message = "评论失败!";
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
            countByArticle: function(id) {
                var url =  "/comments/count_by_article/"+id;
                axios.get(url).then(function(response) {
                    vue.commentsCount = response.data.data.count;
                    vue.loginUser = response.data.data.loginUser;
                });
            },
            deleteComment: function (id) {
                $('#my-confirm').modal({
                    relatedElement: this,
                    onConfirm: function() {
                        const url = "comments/"+id;
                        axios.delete(url).then(function(response){
                            if(1 === response.data.code) {
                                vue.list(0);
                                vue.countByArticle(aid);
                            }
                            else {
                                vue.show = true;
                                vue.message = "删除失败!";
                                setTimeout(function () {
                                    vue.show = false;
                                }, 3000)
                            }
                        });
                    },
                    onCancel: function() {
                        return;
                    }
                });
            },
            fun: function(cid) {
                const url = "/comments/fun";
                this.comment_like.comment.id = cid;
                axios.post(url,this.comment_like).then(function(response){
                    if (response.data.id > 0) {
                        vue.list(0);
                    } else if (response.data.id === 0){
                        vue.show = true;
                        vue.message = "您尚未登录";
                        setTimeout(function () {
                            vue.show = false;
                        }, 3000)
                    } else {
                        vue.show = true;
                        vue.message = "服务器出错!";
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
            not_fun: function(cid) {
                const url = "/comments/not_fun";
                this.comment_like.comment.id = cid;
                axios.post(url,this.comment_like).then(function(response){
                    if (response.data.id > 0) {
                        vue.list(0);
                    } else if (response.data.id === 0){
                        vue.show = true;
                        vue.message = "您尚未登录";
                        setTimeout(function () {
                            vue.show = false;
                        }, 3000)
                    } else {
                        vue.show = true;
                        vue.message = "服务器出错!";
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
            getReadCount: function() {
                const url = this.uri+"/read_count";
                axios.post(url, this.article).then(function(response) {
                    vue.read_count = response.data.data.read_count;
                });
            },
            getWSLink: function () {
                const url =  this.uri+"/get_ws_link";
                axios.get(url).then(function(response) {
                    vue.wsLink = response.data.data.wsLink;
                });
            },
            replyStatusOn: function(id) {
                vue.isOnReply[id] = 1;
            },
            replyStatusOff: function(id) {
                vue.isOnReply[id] = 0;
            },
            jump: function(page){
                jump(page,vue); //定义在adminHeader.html 中
            },
            jumpByNumber: function(start){
                jumpByNumber(start,vue);
            },
            forCommentId:function(id){
                return "detail_comment_" +id;
            }
        }
    });
});