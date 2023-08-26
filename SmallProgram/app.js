//app.js
App({
    onLaunch: function() {
        // 展示本地存储能力
        // var logs = wx.getStorageSync('logs') || []
        // logs.unshift(Date.now())
        // wx.setStorageSync('logs', logs)

        const ip = '192.168.31.57';//电脑本地ip
        // const ip = '192.168.43.83';//手机热点
        const server = '8080';
        const host = 'http://'+ip+':'+server+'/';
        wx.setStorageSync('hostConfig', host);

        const url = 'https://api.weixin.qq.com/sns/jscode2session';
        const appid = 'wx30fa7a77608563b4';
        const secret = 'fa81b6f0174e846fe6c3d9253730a14f';
        // 登录
        wx.login({
            success: res => {
                // console.log(res)
                // 发送 res.code 到后台换取 openId, sessionKey, unionId
                wx.request({
                    url: url + '?appid=' + appid + '&secret=' + secret + '&js_code=' + res.code + '&grant_type=authorization_code',
                    data: {},
                    header: { 'content-type': 'application/json' },
                    method: 'GET',
                    dataType: 'json',
                    responseType: 'text',
                    success: (res) => {
                        // console.log(res)
                        wx.setStorageSync('openid', res.data.openid);
                    },
                    fail: () => {},
                    complete: () => {}
                });
            }
        })

        // 获取用户信息
        wx.getSetting({
            success: res => {
                if (res.authSetting['scope.userInfo']) {
                    // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
                    wx.getUserInfo({
                        success: res => {
                            // console.log(res)
                                // 可以将 res 发送给后台解码出 unionId
                            this.globalData.userInfo = res.userInfo
                                // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
                                // 所以此处加入 callback 以防止这种情况
                            if (this.userInfoReadyCallback) {
                                this.userInfoReadyCallback(res)
                            }

                        }
                    })
                }
            }
        })

    },
    globalData: {
        userInfo: null,
    },
    globalFunc: {
        checkUserInfo: function() {
            if (getApp().globalData.userInfo == null) {
                wx.showModal({
                    content: '未获取到您的用户信息，您无法体验该功能！',
                    showCancel: true,
                    cancelText: '取消',
                    cancelColor: '#000000',
                    confirmText: '去获取',
                    confirmColor: '#3CC51F',
                    success: (res) => {
                        if (res.confirm) {
                            wx.switchTab({
                                url: '/pages/my/myinfo',
                            });
                        }
                    },
                });
                return false;
            } else {
                return true;
            }

        }
    },

})