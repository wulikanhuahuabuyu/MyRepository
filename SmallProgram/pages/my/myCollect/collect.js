//Page Object
var app = getApp();
Page({
    data: {
        blogList_c: []
    },
    //options(Object)
    onLoad: function(options) {
        this.getList();
    },
    //获取已收藏的博客列表
    getList: function() {
        var that = this;
        wx.request({
            url: wx.getStorageSync('hostConfig') + 'blog/getMyCollect',
            data: {
                openid: wx.getStorageSync('openid')
            },
            header: { "Content-Type": "application/x-www-form-urlencoded" },
            method: 'POST',
            dataType: 'json',
            responseType: 'text',
            success: (res) => {
                that.setData({
                    blogList_c: res.data.data
                });
            },
        });
    },
    toBlogDetail: function(e) {
        let idx = e.currentTarget.dataset.index;
        let blogId = this.data.blogList_c[idx].id;
        let b_openid = this.data.blogList_c[idx].openId;
        wx.navigateTo({
            url: '../../index/editor/editor?id='+blogId+'&openid='+b_openid,
        });
    },
    onPullDownRefresh: function() {
        this.getList();
    },
});