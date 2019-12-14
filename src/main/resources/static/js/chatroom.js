let chatroom = new Vue({
    el: '#chatroom',
    data: {
        stompClient: null,
        messages: [],
        messageinput: null,
        number: 0,
        roomId: getQueryString("roomId")
    },
    methods: {
        connectToSocket(){
            this.messages.push({
                creator: '系统消息',
                msgBody: '连接中...'
            });

            var websocket = new SockJS("/chat/room");
            this.stompClient = Stomp.over(websocket);
            this.stompClient.connect({"heart-beat":"0,0", "room-id": this.roomId}, connectFrame => {
                chatroom.messages.push({
                    creator: '系统消息',
                    msgBody: '连接成功！'
                });
                //接收聊天室消息
                chatroom.stompClient.subscribe('/topic/echo.' + this.roomId, function (data) {
                    console.log(data);
                    chatroom.messages.push({
                        creator: '系统消息',
                        msgBody: data.body
                    });
                });
                chatroom.stompClient.subscribe('/user/topic/oneToOne.' + this.roomId, function (data) {
                    console.log(data);
                    chatroom.messages.push({
                        creator: '一对一消息',
                        msgBody: data.body
                    });
                });
                chatroom.stompClient.subscribe('/topic/group.' + this.roomId, function (data) {
                    chatroom.messages.push({
                        creator: data.headers['user-name'],
                        msgBody: data.body
                    });
                });
            }, errorFrame => {
                console.log(errorFrame);
                if (errorFrame.headers) {
                    chatroom.messages.push({
                        creator: "系统消息",
                        msgBody: errorFrame.headers['message']
                    });
                }
            });
        },
        sendMessage: function () {
            if (chatroom.messageinput != null) {
                console.log(chatroom.messageinput);
                let input = chatroom.messageinput.trim();
                let name = null;
                if (input.startsWith("@")){
                    name = input.substr(1, input.indexOf(" ")).trim();
                }
                let dest = (name ? "/user/" + name + "/topic/oneToOne.": "/topic/group.") + this.roomId;
                this.stompClient.send(dest, {}, chatroom.messageinput);
                this.messageinput = null;
            } else if (chatroom.messageinput == null) {
                chatroom.messageinput = "请输入内容!!!";
            }
        }
    },mounted(){
        this.connectToSocket();
    }
});