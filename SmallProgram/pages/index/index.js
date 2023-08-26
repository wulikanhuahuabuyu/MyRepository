//Page Object
const app = getApp();
Page({
    data: {
        blogList: [],
        clicks: 0,
        idx: '',
        blogIdx: ''
    },
    //options(Object)
    onLoad: function(options) {
        // console.log(options)
        //用户点击分享链接后，跳转到指定页面
        if (Object.keys(options).length != 0) {
            wx.navigateTo({
                url: 'editor/editor?id=' + options.id + '&openid=' + options.openid,
            });
        }
        this.getList();
    },
    onPullDownRefresh: function() {
        this.getList();
    },
    getList: function() {
        var that = this;
        wx.request({
            url: wx.getStorageSync('hostConfig') + 'blog/getBlog',
            data: {},
            header: { 'content-type': 'application/x-www-form-urlencoded' },
            method: 'POST',
            dataType: 'json',
            responseType: 'text',
            success: (res) => {
                console.log(res)
                if (Object.keys(res.data.data).length != 0) {
                    that.setData({
                        blogList: res.data.data,
                    });
                }
            },
        });
    },
    toBlogDetail: function(e) {
        var idx = e.currentTarget.dataset.index;
        var list = this.data.blogList;
        wx.navigateTo({
            url: 'editor/editor?id=' + list[idx].id + '&openid=' + list[idx].openId,
        });
    },
    inputBlog: function() {
        if (app.globalFunc.checkUserInfo()) {
            wx.navigateTo({
                url: 'editor/editor',
            });
        }
    },
    //点赞处理函数
    praise_click: function(e) {
        var that = this;
        var idx = e.currentTarget.dataset.index;
        var list = this.data.blogList;
        var blog_id = list[idx].id;
        var isPraise; //传递到数据库点赞的状态   
        var cookie_pid = wx.getStorageSync('zan') || []; //获取全部点赞的blog_id    
        for (var i = 0; i < list.length; i++) {
            if (list[i].id == blog_id) { //遍历找到对应的id
                var num = list[i].praise; //当前赞数
                if (cookie_pid.includes(blog_id)) { //说明已经点过赞,取消赞  
                    for (var j = 0; j < cookie_pid.length; j++) {
                        if (cookie_pid[j] == blog_id) {
                            cookie_pid.splice(j, 1); //删除取消赞的blog_id 
                        }
                    }
                    --num;
                    if (num < 0) {
                        num = 0;
                    }
                    isPraise = 0; //取消点赞
                    // that.setData({
                    //     [`blogList[${i}].praise`]: num, //es6模板语法（反撇号字符）
                    //     // [`blogList[${i}].favor_img`]: "../../image/favor.png",
                    // })
                    wx.setStorageSync('zan', cookie_pid);
                    wx.showToast({
                        title: "取消点赞!",
                        icon: 'none'
                    })
                } else {
                    isPraise = 1; //已点赞
                    ++num;
                    // that.setData({
                    //     [`blogList[${i}].praise`]: num, //es6模板语法（反撇号字符）
                    //     // [`blogList[${i}].favor_img`]: "../../image/favor_press.png",
                    // })
                    cookie_pid.unshift(blog_id); //新增赞的blog_id
                    wx.setStorageSync('zan', cookie_pid);
                    wx.showToast({
                        title: "点赞成功!",
                        icon: 'none'
                    })
                }
                //console.log(cookie_pid); 
                //点赞数据同步到数据库
                wx.request({
                    url: wx.getStorageSync('hostConfig') + 'blog/praise',
                    data: {
                        // id: list[idx].id,
                        id: blog_id,
                        praise: num,
                        isPraise: isPraise
                    },
                    header: { 'content-type': 'application/json' },
                    method: 'GET',
                    dataType: 'json',
                    responseType: 'text',
                    success: () => {
                        that.getList();
                    },
                });
            }
        }
    },
    //收藏处理函数
    collect_click: function(e) {
        var that = this;
        var idx = e.currentTarget.dataset.index;
        var list = this.data.blogList;
        var blog_id = list[idx].id;
        var isCollect;
        var cookie_cid = wx.getStorageSync('collect') || [];
        for (var i = 0; i < list.length; i++) {
            if (list[i].id == blog_id) { //遍历找到对应的id
                if (cookie_cid.includes(blog_id)) { //说明已收藏,取消收藏 
                    for (var j = 0; j < cookie_cid.length; j++) {
                        if (cookie_cid[j] == blog_id) {
                            cookie_cid.splice(j, 1); //删除取消收藏的blog_id 
                        }
                    }
                    isCollect = 0;
                    wx.setStorageSync('collect', cookie_cid);
                    wx.showToast({
                        title: "取消收藏!",
                        icon: 'none'
                    })
                } else {
                    isCollect = 1;
                    cookie_cid.unshift(blog_id); //新增收藏的blog_id
                    wx.setStorageSync('collect', cookie_cid);
                    wx.showToast({
                        title: "收藏成功!",
                        icon: 'none'
                    })
                }
                //将点赞状态同步到数据库
                wx.request({
                    url: wx.getStorageSync('hostConfig') + 'blog/collect',
                    data: {
                        id: blog_id,
                        isCollect: isCollect
                    },
                    header: { 'content-type': 'application/json' },
                    method: 'GET',
                    dataType: 'json',
                    responseType: 'text',
                    success: () => {
                        that.getList();
                    },
                });
            }
        }
    },
    getIndex: function(e) {
        var index = e.currentTarget.dataset.index;
        this.setData({
            idx: index
        });
    },
    onShareAppMessage: function() {
        var idx = this.data.idx;
        var title = this.data.blogList[idx].title;
        var imageStr = this.data.blogList[idx].image;
        var imageArr = imageStr.split(',');
        var id = this.data.blogList[idx].id;
        var openid = this.data.blogList[idx].openId;
        return {
            title: title,
            path: '/pages/index/index?id=' + id + '&openid=' + openid,
            imageUrl: imageArr[0]
        }
    }
});