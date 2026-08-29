import { FriendshipService } from "@/services/FriendshipService";
import type { FriendList, FriendshipStatus } from "@/types/types";
import { UserRound } from "lucide-react";
import { useCallback, useContext, useEffect, useState } from "react";
import { AuthContext } from "../context/AuthContext";

const FriendListDisplay = () => {
    const [friendsList, setFriendsList] = useState<FriendList | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState("");

    const context = useContext(AuthContext);

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

    useEffect(() => {
        fetchFriendsList("ACCEPTED");
    }, [fetchFriendsList]);

    const friends = friendsList?.friends ?? [];

    return (
        <section className="w-full max-w-md rounded-2xl border border-border bg-card p-5 shadow-[0_12px_36px_-18px_rgba(15,23,42,0.22)]">
            <div className="mb-4 flex items-center justify-between gap-3">
                <div>
                    <h2 className="text-lg font-semibold tracking-tight text-foreground">Friends</h2>
                    <p className="text-sm text-muted-foreground">
                        {friendsList ? `${friendsList.totalElements} connected` : "Your accepted friends"}
                    </p>
                </div>
            </div>

            {isLoading && (
                <div className="space-y-3">
                    {[1, 2, 3].map((item) => (
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
                <div className="rounded-lg border border-dashed border-border bg-muted/20 px-4 py-6 text-center">
                    <div className="mx-auto mb-3 flex h-11 w-11 items-center justify-center rounded-full bg-primary/10 text-primary">
                        <UserRound className="h-5 w-5" />
                    </div>
                    <p className="text-sm font-medium text-foreground">No friends yet</p>
                    <p className="mt-1 text-sm text-muted-foreground">Accepted friends will appear here.</p>
                </div>
            )}

            {!isLoading && !errorMessage && friends.length > 0 && (
                <ul className="space-y-2">
                    {friends.map((friend) => (
                        <li
                            key={friend.id}
                            className="flex items-center gap-3 rounded-lg border border-border bg-background p-3 transition-colors hover:bg-muted/35"
                        >
                            <div className="flex h-11 w-11 shrink-0 items-center justify-center rounded-full bg-primary/10 text-primary ring-1 ring-primary/15">
                                <UserRound className="h-5 w-5" />
                            </div>
                            <div className="min-w-0">
                                <p className="truncate text-sm font-semibold text-foreground">{
                                    friend.requester.username === context?.user?.username ? friend.addressee.username : friend.requester.username
                                }</p>
                                <p className="text-xs text-muted-foreground">Friend</p>
                            </div>
                        </li>
                    ))}
                </ul>
            )}
        </section>
    )
}

export default FriendListDisplay;
