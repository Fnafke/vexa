const getMessages = async (chatId: string, page: number = 0, pageSize: number = 20) => {
    try {
        const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/chats/${chatId}/messages?page=${page}&size=${pageSize}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
            credentials: "include",
        })
        return response;
    } catch (error) {
        console.error("Error fetching messages:", error)
        throw error
    }
}

const sendMessage = async (chatId: string, content: string) => {
    try {
        const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/chats/${chatId}/messages`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            credentials: "include",
            body: JSON.stringify({ content }),
        })
        return response;
    } catch (error) {
        console.error("Error sending message:", error)
        throw error
    }
}

export const MessageService = {
    getMessages,
    sendMessage,
}