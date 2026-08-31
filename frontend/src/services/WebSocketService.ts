import { Client, type IMessage, type StompSubscription } from "@stomp/stompjs";

const client = new Client({
    brokerURL: import.meta.env.VITE_WEBSOCKET_URL,
    reconnectDelay: 5000, // auto-retry every 5s if disconnected
    onConnect: () => {
        console.log("WebSocket connected");
    },
    onStompError: (frame) => {
        console.error("STOMP error:", frame.headers["message"]);
        console.error(frame.body);
    },
});

const connect = () => {
    if (!client.active) {
        client.activate();
    }
};

const disconnect = () => {
    if (client.active) {
        client.deactivate();
    }
};

const subscribeToDirectChat = (chatId: string, onMessage: (message: unknown) => void): (() => void) => {
    let subscription: StompSubscription | null = null;

    const doSubscribe = () => {
        subscription = client.subscribe(`/topic/chats/${chatId}`, (message: IMessage) => {
            onMessage(JSON.parse(message.body));
        });
    };

    if (client.connected) {
        doSubscribe();
    } else {
        const originalOnConnect = client.onConnect;
        client.onConnect = (frame) => {
            originalOnConnect?.(frame);
            doSubscribe();
        };
    }

    return () => {
        subscription?.unsubscribe();
    };
};

export const WebSocketService = {
    client,
    connect,
    disconnect,
    subscribeToDirectChat,
};