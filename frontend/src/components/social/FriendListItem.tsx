import type { Friendship } from "@/types/types";
import { parseInstant, timeAgo } from "@/utils/utils";
import { UserRound } from "lucide-react";
import { Button } from "../ui/button";

type FriendListItemProps = {
    friend: Friendship;
    currentUsername?: string;
    label: string;
    updatedAt?: string;
    onAccept?: () => void;
    onDecline?: () => void;
}

const FriendListItem = ({ friend, currentUsername, label, updatedAt, onAccept, onDecline }: FriendListItemProps) => {
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
                        >
                            Accept
                        </Button>
                        <Button
                            className="text-xs text-red-500 hover:text-red-600 cursor-pointer"
                            onClick={onDecline}
                        >
                            Decline
                        </Button>
                    </div>
                )}
            </div>
        </li>
    );
}

export default FriendListItem;