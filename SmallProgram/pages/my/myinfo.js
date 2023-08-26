//index.js
//获取应用实例
const app = getApp()

Page({
    data: {
        userInfo: {},
        hasUserInfo: false,
        canIUse: wx.canIUse('button.open-type.getUserInfo')
    },
    onLoad: function() {
        if (app.globalData.userInfo) {
            this.setData({
                userInfo: app.globalData.userInfo,
                hasUserInfo: true
            })
        } else if (this.data.canIUse) {
            // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
            // 所以此处加入 callback 以防止这种情况
            app.userInfoReadyCallback = res => {
                this.setData({
                    userInfo: res.userInfo,
                    hasUserInfo: true
                })
            }
        } else {
            // 在没有 open-type=getUserInfo 版本的兼容处理
            wx.getUserInfo({
                success: res => {
                    app.globalData.userInfo = res.userInfo
                    this.setData({
                        userInfo: res.userInfo,
                        hasUserInfo: true
                    })
                }
            })
        }
    },
    getUserInfo: function(e) {
        app.globalData.userInfo = e.detail.userInfo
        console.log(e)
        var errMsg = e.detail.errMsg;
        if (errMsg == 'getUserInfo:ok') {
            var info = e.detail.userInfo;
            wx.request({
                url: wx.getStorageSync('hostConfig') + 'user/saveUser',
                data: {
                    openId: wx.getStorageSync('openid'),
                    nickName: info.nickName,
                    avatar: info.avatarUrl,
                    gender: info.gender,
                    city: info.city,
                    country: info.country,
                    province: info.province,
                    language: info.language
                },
                header: { "Content-Type": "application/x-www-form-urlencoded" },
                method: 'POST',
                dataType: 'json',
                responseType: 'text',
                success: (res) => {

                },
            });
        }
        this.setData({
            userInfo: e.detail.userInfo,
            hasUserInfo: errMsg == "getUserInfo:fail auth deny" ? false : true
        })
    },
    popUp_QRCode: function() {
        wx.request({
            url: wx.getStorageSync('hostConfig') + 'getQRCode',
            header: { 'content-type': 'application/json' },
            method: 'GET',
            dataType: 'json',
            responseType: 'text',
            success: (res) => {
                if (res.statusCode == 200) {
                    wx.previewImage({
                        urls: [res.data.url],
                    });
                }
            },
            fail: () => {},
        });
    }
})