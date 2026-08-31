import { MessageCircle } from "lucide-react";
import { useEffect, useRef } from "react";

import { MessageGroup } from "@/components/ui/message";
import { Skeleton } from "@/components/ui/skeleton";
import { cn } from "@/lib/utils";
import type { Message } from "@/types/types";

import DirectChatMessageItem from "./DirectChatMessageItem";

type DirectChatMessageListProps = {
    messages: Message[];
    currentUsername?: string;
    isLoading?: boolean;
    className?: string;
}

const DirectChatMessageList = ({ messages, currentUsername, isLoading = false, className }: DirectChatMessageListProps) => {
    const bottomRef = useRef<HTMLDivElement | null>(null);

    useEffect(() => {
        bottomRef.current?.scrollIntoView({ block: "end" });
    }, [messages.length]);

    if (isLoading) {
        return (
            <div className={cn("min-h-0 flex-1 space-y-4 overflow-auto p-4", className)}>
                {[1, 2, 3, 4].map((item) => (
                    <div key={item} className={cn("flex gap-2", item % 2 === 0 && "flex-row-reverse")}>
                        <Skeleton className="h-8 w-8 rounded-full" />
                        <div className={cn("space-y-2", item % 2 === 0 && "items-end")}>
                            <Skeleton className="h-3 w-20" />
                            <Skeleton className="h-10 w-56 rounded-2xl" />
                        </div>
                    </div>
                ))}
            </div>
        )
    }

    if (messages.length === 0) {
        return (
            <div className={cn("flex min-h-0 flex-1 flex-col items-center justify-center rounded-lg border border-dashed border-border bg-muted/20 p-6 text-center", className)}>
                <div className="mb-3 flex h-11 w-11 items-center justify-center rounded-full bg-primary/10 text-primary">
                    <MessageCircle className="h-5 w-5" />
                </div>
                <p className="text-sm font-medium text-foreground">No messages yet</p>
                <p className="mt-1 text-sm text-muted-foreground">Start the conversation below.</p>
            </div>
        )
    }

    return (
        <MessageGroup className={cn("min-h-0 flex-1 overflow-auto p-4", className)}>
            {messages.map((message) => (
                <DirectChatMessageItem
                    key={message.id}
                    message={message}
                    isOwnMessage={message.sender.username === currentUsername}
                />
            ))}
            <div ref={bottomRef} />
        </MessageGroup>
    )
}

export default DirectChatMessageList;
