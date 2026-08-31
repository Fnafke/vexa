import SidebarComponent from "@/components/SidebarComponent";
import ChatsSidebar from "@/components/social/ChatsSidebar";
import DirectChatView from "@/components/social/DirectChatView";
import { SidebarInset } from "@/components/ui/sidebar";
import { DirectChatService } from "@/services/DirectChatService";
import { MessageService } from "@/services/MessageService";
import { WebSocketService } from "@/services/WebSocketService";
import type { DirectChat, Message } from "@/types/types";
import { useCallback, useEffect, useState } from "react";
import { useParams } from "react-router-dom";

const DirectChatPage = () => {
    const { chatId } = useParams<{ chatId: string }>();
    const [chat, setChat] = useState<DirectChat | null>(null);
    const [messages, setMessages] = useState<Message[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [draft, setDraft] = useState("");

    const fetchDirectChat = useCallback(async () => {
        if (!chatId) return;
        setIsLoading(true);
        try {
            const response = await DirectChatService.getDirectChatById(chatId);
            if (response.ok) {
                const data: DirectChat = await response.json();
                setChat(data);
            } else {
                console.error("Failed to fetch direct chat:", response.statusText);
            }
        } catch (error) {
            console.error("Error fetching direct chat:", error);
        }
        finally {
            setIsLoading(false);
        }
    }, [chatId]);

    useEffect(() => {
        fetchDirectChat();
    }, [fetchDirectChat]);
    
    useEffect(() => {
        if (!chatId) return;

        setIsLoading(true);
        MessageService.getMessages(chatId)
            .then(async (response) => {
                if (response.ok) {
                    const data = await response.json();
                    setMessages([...data.messages].reverse());
                }
            })
            .finally(() => setIsLoading(false));
    }, [chatId]);

    useEffect(() => {
        if (!chatId) return;

        const unsubscribe = WebSocketService.subscribeToDirectChat(chatId, (incoming) => {
            const message = incoming as Message;
            setMessages((prev) => [...prev, message]);
        });

        return unsubscribe;
    }, [chatId]);

    const handleSend = async () => {
        if (!draft.trim() || !chatId) return;

        const content = draft.trim();
        setDraft(""); // clear input immediately for responsiveness — doesn't touch message list

        try {
            const response = await MessageService.sendMessage(chatId, content);
            if (!response.ok) {
                console.error("Failed to send message:", response.statusText);
                setDraft(content); // restore draft so the user doesn't lose what they typed
            }
            // No local append here — the WebSocket subscription above will deliver
            // this exact message back to us once the server broadcasts it.
        } catch (error) {
            console.error("Error sending message:", error);
            setDraft(content);
        }
    };

    return (
            <>
            <title>Vexa - Direct Messages</title>
            <SidebarComponent />
            <SidebarInset className="min-h-svh p-4 md:p-6">
                <div className="flex min-h-0 w-full flex-1 flex-col gap-4 lg:flex-row">
                    <ChatsSidebar className="lg:h-[calc(100svh-3rem)]" />
                    <div className="flex min-h-0 flex-1 flex-col gap-4">
                        <DirectChatView
                            chat={chat}
                            messages={messages}
                            draft={draft}
                            onDraftChange={setDraft}
                            onSend={handleSend}
                        />
                    </div>
                </div>
            </SidebarInset>
        </>
    )
}

export default DirectChatPage;