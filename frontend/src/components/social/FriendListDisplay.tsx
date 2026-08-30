import { DirectChatService } from "@/services/DirectChatService";
import { FriendshipService } from "@/services/FriendshipService";
import type { DirectChat, FriendList, Friendship, FriendshipStatus } from "@/types/types";
import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import { Clock, UserRound } from "lucide-react";
import { useCallback, useContext, useEffect, useState } from "react";
import { AuthContext } from "../context/AuthContext";
import FriendListItem from "./FriendListItem";
import { useNavigate } from "react-router-dom";

type FriendListDisplayProps = {
    className?: string;
    fullHeight?: boolean;
}

const FriendListDisplay = ({ className, fullHeight = false }: FriendListDisplayProps) => {
    const [friendsList, setFriendsList] = useState<FriendList | null>(null);
    const [activeStatus, setActiveStatus] = useState<Extract<FriendshipStatus, "ACCEPTED" | "PENDING">>("ACCEPTED");
    const [isLoading, setIsLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState("");
    const [startingChatId, setStartingChatId] = useState<string | null>(null);

    const context = useContext(AuthContext);
    const navigate = useNavigate();

    const fetchFriendsList = useCallback(async (status: FriendshipStatus, page?: number, size?: number) => {
        setIsLoading(true);
        setErrorMessage("");

        try {
            const response = await FriendshipService.getFriendListByStatus(status, page, size);
            if (response.ok) {
                const data: FriendList = await response.json();
                setFriendsList(data);
            } else {
                console.error("Failed to fetch friends list:", response.statusText);
                setErrorMessage("Could not load your friends right now.");
            }
        } catch (error) {
            console.error("Error fetching friends list:", error);
            setErrorMessage("Something went wrong while loading your friends.");
        } finally {
            setIsLoading(false);
        }
    }, []);

    const handleAcceptRequest = async (friendshipId: string) => {
        try {
            const response = await FriendshipService.acceptFriendRequest(friendshipId);
            if (response.ok) {
                fetchFriendsList(activeStatus);
            } else {
                console.error("Failed to accept friend request:", response.statusText);
                setErrorMessage("Could not accept the friend request right now.");
            }
        } catch (error) {
            console.error("Error accepting friend request:", error);
            setErrorMessage("Something went wrong while accepting the friend request.");
        }
    };

    const handleDeclineRequest = async (friendshipId: string) => {
        try {
            const response = await FriendshipService.declineFriendRequest(friendshipId);
            if (response.ok) {
                fetchFriendsList(activeStatus);
            } else {
                console.error("Failed to decline friend request:", response.statusText);
                setErrorMessage("Could not decline the friend request right now.");
            }
        } catch (error) {
            console.error("Error declining friend request:", error);
            setErrorMessage("Something went wrong while declining the friend request.");
        }
    }

    const handleRemoveFriend = async (friendshipId: string) => {
        try {
            const response = await FriendshipService.removeFriend(friendshipId);
            if (response.ok) {
                fetchFriendsList(activeStatus);
            } else {
                console.error("Failed to remove friend:", response.statusText);
                setErrorMessage("Could not remove the friend right now.");
            }
        } catch (error) {
            console.error("Error removing friend:", error);
            setErrorMessage("Something went wrong while removing the friend.");
        }
    }

    const getFriendUser = (friendship: Friendship) => {
        return friendship.requester.username === context?.user?.username
            ? friendship.addressee
            : friendship.requester;
    }

    const handleStartChat = async (friendship: Friendship) => {
        const friendUser = getFriendUser(friendship);
        setStartingChatId(friendship.id);
        setErrorMessage("");

        try {
            const response = await DirectChatService.getOrCreateDirectChat(friendUser.id);
            if (response.ok) {
                const directChat: DirectChat = await response.json();
                navigate(`/direct-messages/${directChat.id}`);
            } else {
                console.error("Failed to create direct chat:", response.statusText);
                setErrorMessage("Could not start a chat right now.");
            }
        } catch (error) {
            console.error("Error creating direct chat:", error);
            setErrorMessage("Something went wrong while starting the chat.");
        } finally {
            setStartingChatId(null);
        }
    }

    useEffect(() => {
        fetchFriendsList(activeStatus);
    }, [activeStatus, fetchFriendsList]);

    const currentUsername = context?.user?.username;
    const friends = friendsList?.friends ?? [];
    const isPendingView = activeStatus === "PENDING";
    const sentRequests = isPendingView
        ? friends.filter((friend) => friend.requester.username === currentUsername)
        : [];
    const receivedRequests = isPendingView
        ? friends.filter((friend) => friend.requester.username !== currentUsername)
        : [];
    const title = isPendingView ? "Pending requests" : "Friends";
    const description = friendsList
        ? `${friendsList.totalElements} ${isPendingView ? "pending" : "connected"}`
        : isPendingView ? "Friend requests waiting for a response" : "Your accepted friends";

    return (
        <section
            className={cn(
                "flex w-full flex-col rounded-2xl border border-border bg-card p-5 shadow-[0_12px_36px_-18px_rgba(15,23,42,0.22)]",
                fullHeight ? "h-full min-h-0" : "max-w-md",
                className
            )}
        >
            <div className="mb-4 flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
                <div>
                    <h2 className="text-lg font-semibold tracking-tight text-foreground">{title}</h2>
                    <p className="text-sm text-muted-foreground">
                        {description}
                    </p>
                </div>
                <div className="grid grid-cols-2 gap-2 rounded-2xl bg-muted/50 p-1">
                    <Button
                        onClick={() => setActiveStatus("ACCEPTED")}
                        size="sm"
                        type="button"
                        variant={activeStatus === "ACCEPTED" ? "default" : "ghost"}
                    >
                        Friends
                    </Button>
                    <Button
                        onClick={() => setActiveStatus("PENDING")}
                        size="sm"
                        type="button"
                        variant={activeStatus === "PENDING" ? "default" : "ghost"}
                    >
                        Pending
                    </Button>
                </div>
            </div>

            {isLoading && (
                <div className={cn("space-y-3", fullHeight && "min-h-0 flex-1 overflow-auto")}>
                    {[1, 2, 3, 4, 5].map((item) => (
                        <div key={item} className="flex items-center gap-3 rounded-lg border border-border bg-muted/30 p-3">
                            <div className="h-10 w-10 animate-pulse rounded-full bg-muted" />
                            <div className="space-y-2">
                                <div className="h-3 w-28 animate-pulse rounded-full bg-muted" />
                                <div className="h-2.5 w-16 animate-pulse rounded-full bg-muted" />
                            </div>
                        </div>
                    ))}
                </div>
            )}

            {!isLoading && errorMessage && (
                <p className="rounded-lg border border-destructive/25 bg-destructive/10 px-3 py-2 text-sm text-destructive">
                    {errorMessage}
                </p>
            )}

            {!isLoading && !errorMessage && friends.length === 0 && (
                <div className={cn("rounded-lg border border-dashed border-border bg-muted/20 px-4 py-6 text-center", fullHeight && "flex min-h-0 flex-1 flex-col items-center justify-center")}>
                    <div className="mx-auto mb-3 flex h-11 w-11 items-center justify-center rounded-full bg-primary/10 text-primary">
                        {isPendingView ? <Clock className="h-5 w-5" /> : <UserRound className="h-5 w-5" />}
                    </div>
                    <p className="text-sm font-medium text-foreground">
                        {isPendingView ? "No pending requests" : "No friends yet"}
                    </p>
                    <p className="mt-1 text-sm text-muted-foreground">
                        {isPendingView ? "Pending friend requests will appear here." : "Accepted friends will appear here."}
                    </p>
                </div>
            )}

            {!isLoading && !errorMessage && !isPendingView && friends.length > 0 && (
                <ul className={cn("space-y-2", fullHeight && "min-h-0 flex-1 overflow-auto pr-1")}>
                    {friends.map((friend) => (
                        <FriendListItem
                            key={friend.id}
                            friend={friend}
                            currentUsername={currentUsername}
                            label="Friend"
                            updatedAt={friend.updatedAt}
                            onRemove={() => handleRemoveFriend(friend.id)}
                            onStartChat={() => handleStartChat(friend)}
                            isStartingChat={startingChatId === friend.id}
                        />
                    ))}
                </ul>
            )}

            {!isLoading && !errorMessage && isPendingView && friends.length > 0 && (
                <div className={cn("space-y-5", fullHeight && "min-h-0 flex-1 overflow-auto pr-1")}>
                    <section>
                        <div className="mb-2 flex items-center gap-3">
                            <h3 className="text-sm font-semibold text-foreground">Requests received</h3>
                            <span className="text-xs font-medium text-muted-foreground">{receivedRequests.length}</span>
                        </div>
                        {receivedRequests.length > 0 ? (
                            <ul className="space-y-2">
                                {receivedRequests.map((friend) => (
                                    <FriendListItem
                                        key={friend.id}
                                        friend={friend}
                                        currentUsername={currentUsername}
                                        label={`Request received`}
                                        updatedAt={friend.updatedAt}
                                        onAccept={() => handleAcceptRequest(friend.id)}
                                        onDecline={() => handleDeclineRequest(friend.id)}
                                    />
                                ))}
                            </ul>
                        ) : (
                            <p className="rounded-lg border border-dashed border-border bg-muted/20 px-3 py-3 text-sm text-muted-foreground">
                                No requests received.
                            </p>
                        )}
                    </section>

                    <section>
                        <div className="mb-2 flex items-center gap-3">
                            <h3 className="text-sm font-semibold text-foreground">Requests sent</h3>
                            <span className="text-xs font-medium text-muted-foreground">{sentRequests.length}</span>
                        </div>
                        {sentRequests.length > 0 ? (
                            <ul className="space-y-2">
                                {sentRequests.map((friend) => (
                                    <FriendListItem
                                        key={friend.id}
                                        friend={friend}
                                        currentUsername={currentUsername}
                                        label={`Request sent`}
                                        updatedAt={friend.updatedAt}
                                        onCancel={() => handleDeclineRequest(friend.id)}
                                    />
                                ))}
                            </ul>
                        ) : (
                            <p className="rounded-lg border border-dashed border-border bg-muted/20 px-3 py-3 text-sm text-muted-foreground">
                                No requests sent.
                            </p>
                        )}
                    </section>
                </div>
            )}
        </section>
    )
}

export default FriendListDisplay;
