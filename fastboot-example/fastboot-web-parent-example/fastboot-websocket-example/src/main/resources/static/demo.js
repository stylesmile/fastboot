var ws_protocol = 'ws'; // ws 或 wss
var ip = '127.0.0.1'
var port = 9326

var heartbeatTimeout = 60000; // 心跳超时时间，单位：毫秒
var reconnInterval = 1000; // 重连间隔时间，单位：毫秒

var binaryType = 'blob'; // 'blob' or 'arraybuffer';//arraybuffer是字节
var handler = new DemoHandler()

var tiows

function initWs () {
  var queryString = 'name=kebi&name=kuli&name=kuli&token=11&userid=adfadsf'
  var param = "token=11&userid=adfadsf";
  tiows = new tio.ws(ws_protocol, ip, port, queryString, param, handler, heartbeatTimeout, reconnInterval, binaryType)
  tiows.connect()
}



function send () {
  var msg = document.getElementById('textId')
  tiows.send(msg.value)
}

function clearMsg () {
  document.getElementById('contentId').innerHTML = ''
}

initWs()