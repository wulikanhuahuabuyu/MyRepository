Component({
    data: {
        selected: 0,
        color: "#7A7E83",
        selectedColor: "#3cc51f",
    },
    attached() {},
    methods: {
        switchTab(e) {
            const data = e.currentTarget.dataset
            const url = data.path
            console.log(data)
            wx.switchTab({ url })
            this.setData({
                selected: data.index
            })
        }
    }
})