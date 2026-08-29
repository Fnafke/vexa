import type { FriendshipStatus } from "@/types/types"

const getFriendListByStatus = async (status: FriendshipStatus, page?: number, size?: number) => {
    try {
        const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/friendships?status=${status}${page ? `&page=${page}` : ""}${size ? `&size=${size}` : ""}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
            credentials: "include",
        })
        return response;
    } catch (error) {
        console.error("Error fetching friend list:", error)
        throw error
    }
}

const sendFriendRequest = async (receiverUsername: string): Promise<Response> => {
    try {
        const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/friendships/request/send?receiverUsername=${receiverUsername}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            credentials: "include",
            body: JSON.stringify({ receiverUsername }),
        })
        return response;
    } catch (error) {
        console.error("Error sending friend request:", error)
        throw error
    }
}

export const FriendshipService = {
    getFriendListByStatus,
    sendFriendRequest,
}