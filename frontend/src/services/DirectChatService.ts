const getAllDirectChatsForUser = async(): Promise<Response> => {
    try {
        const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/direct-chats`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
            credentials: "include",
        })
        return response;
    } catch (error) {
        console.error("Error fetching direct chats:", error)
        throw error
    }
}

const getDirectChatById = async(directChatId: string): Promise<Response> => {
    try {
        const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/direct-chats/${directChatId}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
            credentials: "include",
        })
        return response;
    } catch (error) {
        console.error("Error fetching direct chat by ID:", error)
        throw error
    }
}

const getOrCreateDirectChat = async(userId: string): Promise<Response> => {
    try {
        const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/direct-chats`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            credentials: "include",
            body: JSON.stringify({ userId }),
        })
        return response;
    } catch (error) {
        console.error("Error creating direct chat:", error)
        throw error
    }
}

export const DirectChatService = {
    getAllDirectChatsForUser,
    getDirectChatById,
    getOrCreateDirectChat,
}