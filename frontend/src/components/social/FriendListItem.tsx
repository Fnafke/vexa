import type { Friendship } from "@/types/types";
import { parseInstant, timeAgo } from "@/utils/utils";
import { MessageCircle, UserRound } from "lucide-react";
import { Button } from "../ui/button";

type FriendListItemProps = {
    friend: Friendship;
    currentUsername?: string;
    label: string;
    updatedAt?: string;
    onAccept?: () => void;
    onDecline?: () => void;
    onCancel?: () => void;
    onRemove?: () => void;
    onStartChat?: () => void;
    isStartingChat?: boolean;
}

const FriendListItem = ({ friend, currentUsername, label, updatedAt, onAccept, onDecline, onCancel, onRemove, onStartChat, isStartingChat = false }: FriendListItemProps) => {
    const displayName = friend.requester.username === currentUsername
        ? friend.addressee.username
        : friend.requester.username;

    return (
        <li
            className="flex items-center gap-3 rounded-lg border border-border bg-background p-3 transition-colors hover:bg-muted/35"
        >
            <div className="flex h-11 w-11 shrink-0 items-center justify-center rounded-full bg-primary/10 text-primary ring-1 ring-primary/15">
                <UserRound className="h-5 w-5" />
            </div>
            <div className="min-w-0">
                <p className="truncate text-sm font-semibold text-foreground">{displayName}</p>
                <p className="text-xs text-muted-foreground">
                    {label} {updatedAt && `• ${timeAgo(parseInstant(updatedAt))}`}
                </p>
                {onAccept && onDecline && (
                    <div className="mt-2 flex gap-2">
                        <Button
                            className="text-xs text-green-500 hover:text-green-600 cursor-pointer"
                            onClick={onAccept}
                            variant="secondary"
                        >
                            Accept
                        </Button>
                        <Button
                            className="text-xs text-red-500 hover:text-red-600 cursor-pointer"
                            onClick={onDecline}
                            variant="destructive"
                        >
                            Decline
                        </Button>
                    </div>
                )}
                {onCancel && (
                    <div className="mt-2">
                        <Button
                            className="text-xs text-red-500 hover:text-red-600 cursor-pointer"
                            onClick={onCancel}
                            variant="destructive"
                        >
                            Cancel
                        </Button>
                    </div>
                )}
                {(onStartChat || onRemove) && (
                    <div className="mt-2 flex flex-wrap gap-2">
                        {onStartChat && (
                            <Button
                                className="h-8 gap-2 text-xs cursor-pointer"
                                disabled={isStartingChat}
                                onClick={onStartChat}
                                variant="secondary"
                            >
                                <MessageCircle className="h-3.5 w-3.5" />
                                {isStartingChat ? "Opening..." : "Message"}
                            </Button>
                        )}
                        {onRemove && (
                            <Button
                                className="text-xs text-red-500 hover:text-red-600 cursor-pointer"
                                disabled={isStartingChat}
                                onClick={onRemove}
                                variant="destructive"
                            >
                                Remove
                            </Button>
                        )}
                    </div>
                )}
            </div>
        </li>
    );
}

export default FriendListItem;
