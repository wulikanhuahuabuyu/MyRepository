Page({
    data: {
        formats: {},
        readOnly: false,
        editorHeight: 300,
        keyboardHeight: 0,
        isIOS: false,
        disabled: false,
        hide: true,
        style: '',
        b_title: '',
        title: '',
        content: '',
        image: [],
        blogId: '',
        b_openid: ''
    },
    onLoad(options) {
        // console.log(Object.keys(options).length)
        if (Object.keys(options).length != 0) { //非空判断
            this.setData({
                blogId: options.id,
                b_openid: options.openid
            });
        }

        const platform = wx.getSystemInfoSync().platform
        const isIOS = platform === 'ios'
        this.setData({ isIOS })
        const that = this
        this.updatePosition(0)
        let keyboardHeight = 0
        wx.onKeyboardHeightChange(res => {
            if (res.height === keyboardHeight) return
            const duration = res.height > 0 ? res.duration * 1000 : 0
            keyboardHeight = res.height
            setTimeout(() => {
                wx.pageScrollTo({
                    scrollTop: 0,
                    success() {
                        that.updatePosition(keyboardHeight)
                        that.editorCtx.scrollIntoView()
                    }
                })
            }, duration)

        })
    },
    updatePosition(keyboardHeight) {
        const toolbarHeight = 50
        const { windowHeight, platform } = wx.getSystemInfoSync()
        let editorHeight = keyboardHeight > 0 ? (windowHeight - keyboardHeight - toolbarHeight) : windowHeight
        this.setData({ editorHeight, keyboardHeight })
    },
    calNavigationBarAndStatusBar() {
        const systemInfo = wx.getSystemInfoSync()
        const { statusBarHeight, platform } = systemInfo
        const isIOS = platform === 'ios'
        const navigationBarHeight = isIOS ? 44 : 48
        return statusBarHeight + navigationBarHeight
    },
    onEditorReady() {
        const that = this;
        wx.createSelectorQuery().select('#editor').context(function(res) {
            that.editorCtx = res.context
                // console.log(that.data.blogId)
            if (that.data.blogId != '') {
                wx.request({
                    url: wx.getStorageSync('hostConfig') + 'blog/getBlogDetail',
                    data: {
                        id: that.data.blogId,
                        openid: that.data.b_openid
                    },
                    header: { "Content-Type": "application/x-www-form-urlencoded" },
                    method: 'POST',
                    dataType: 'json',
                    responseType: 'text',
                    success: (res) => {
                        // console.log(res)
                        that.setData({
                            b_title: res.data.data.title,
                            disabled: true,
                            hide: false,
                            style: 'text-align:center;font-weight:bold;width:100%',
                            readOnly: true
                        });
                        that.editorCtx.setContents({
                            html: res.data.data.content,
                            success: function(res) {
                                // console.log(res)
                            }
                        })
                    },
                });
            }
        }).exec()
    },
    blur() {
        this.editorCtx.blur()
    },
    format(e) {
        let { name, value } = e.target.dataset
        if (!name) return
            // console.log('format', name, value)
        this.editorCtx.format(name, value)

    },
    onStatusChange(e) {
        const formats = e.detail
        this.setData({ formats })
    },
    input_title: function(e) {
        this.setData({
            title: e.detail.value
        })
    },
    inputChange: function(e) {
        var content = e.detail.html;
        this.setData({
            content: content
        })
    },
    insertDivider() {
        this.editorCtx.insertDivider({
            success: function() {
                console.log('insert divider success')
            }
        })
    },
    clear() {
        this.editorCtx.clear({
            success: function(res) {
                console.log("clear success")
            }
        })
    },
    removeFormat() {
        this.editorCtx.removeFormat()
    },
    insertDate() {
        const date = new Date()
        const formatDate = `${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()}`
        this.editorCtx.insertText({
            text: formatDate
        })
    },
    insertImage() {
        const that = this
        wx.chooseImage({
            count: 1,
            sizeType: ['original', 'compressed'],
            sourceType: ['album', 'camera'],
            success: function(res) {
                console.log(res)

                wx.uploadFile({
                    url: wx.getStorageSync('hostConfig') + 'blog/image',
                    filePath: res.tempFilePaths[0],
                    name: 'file',
                    formData: {},
                    success: (res) => {
                        // console.log(JSON.parse(res.data))
                        var data = JSON.parse(res.data);
                        var img = wx.getStorageSync('hostConfig') + data.imagePath;
                        that.setData({
                            image: that.data.image.concat(img)
                        });
                        that.editorCtx.insertImage({
                            //图片保存在本地，所以从本地获取图片
                            src: img,
                            data: {
                                id: 'abcd',
                                role: 'god'
                            },
                            width: '80%',
                            success: function() {
                                // console.log('insert image success')
                            }
                        })
                    },
                });

            }
        })
    },
    input: function() {
        var that = this;
        console.log(that.data.image)
        wx.request({
            url: wx.getStorageSync('hostConfig') + 'blog/inputBlog',
            data: {
                openid: wx.getStorageSync('openid'),
                title: that.data.title,
                content: that.data.content,
                image: that.data.image
            },
            header: { "Content-Type": "application/x-www-form-urlencoded" },
            method: 'POST',
            dataType: 'json',
            responseType: 'text',
            success: () => {
                wx.navigateBack({
                    delta: 1
                });
            },
        });
    }
})