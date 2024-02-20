var DemoHandler = function () {
  this.onopen = function (event, ws) {
    // ws.send('hello 连上了哦')
    document.getElementById('contentId').innerHTML += 'hello 连上了哦<br>';
  }

  /**
   * 收到服务器发来的消息
   * @param {*} event 
   * @param {*} ws 
   */
  this.onmessage = function (event, ws) {
    var data = event.data
    document.getElementById('contentId').innerHTML += data + '<br>'
  }

  this.onclose = function (e, ws) {
    // error(e, ws)
  }

  this.onerror = function (e, ws) {
    // error(e, ws)
  }

  /**
   * 发送心跳，本框架会自动定时调用该方法，请在该方法中发送心跳
   * @param {*} ws 
   */
  this.ping = function (ws) {
    // log("发心跳了")
    ws.send('心跳内容')
  }
}
