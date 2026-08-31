import { UserRound } from "lucide-react";

import { cn } from "@/lib/utils";
import type { DirectChat, Message } from "@/types/types";

import DirectChatComposer from "./DirectChatComposer";
import DirectChatMessageList from "./DirectChatMessageList";

type DirectChatViewProps = {
    chat: DirectChat | null;
    messages: Message[];
    currentUsername?: string;
    draft: string;
    isLoadingMessages?: boolean;
    isSendingDisabled?: boolean;
    errorMessage?: string;
    className?: string;
    onDraftChange: (draft: string) => void;
    onSend: () => void | Promise<void>;
}

const DirectChatView = ({
    chat,
    messages,
    currentUsername,
    draft,
    isLoadingMessages = false,
    isSendingDisabled = false,
    errorMessage,
    className,
    onDraftChange,
    onSend,
}: DirectChatViewProps) => {
    const chatUser = chat
        ? chat.userOne.username === currentUsername
            ? chat.userTwo
            : chat.userOne
        : null;

    return (
        <section
            className={cn(
                "flex min-h-0 flex-1 flex-col overflow-hidden rounded-2xl border border-border bg-card shadow-[0_12px_36px_-18px_rgba(15,23,42,0.22)]",
                className
            )}
        >
            <header className="flex items-center gap-3 border-b border-border px-4 py-3">
                <span className="flex h-10 w-10 shrink-0 items-center justify-center rounded-full bg-primary/10 text-primary ring-1 ring-primary/15">
                    <UserRound className="h-5 w-5" />
                </span>
                <div className="min-w-0">
                    <h1 className="truncate text-base font-semibold text-foreground">
                        {chatUser?.username ?? "Direct chat"}
                    </h1>
                    <p className="text-sm text-muted-foreground">Direct message</p>
                </div>
            </header>

            {errorMessage && (
                <p className="mx-4 mt-4 rounded-lg border border-destructive/25 bg-destructive/10 px-3 py-2 text-sm text-destructive">
                    {errorMessage}
                </p>
            )}

            <DirectChatMessageList
                className="flex-1"
                currentUsername={currentUsername}
                isLoading={isLoadingMessages}
                messages={messages}
            />

            <DirectChatComposer
                disabled={isSendingDisabled}
                draft={draft}
                onDraftChange={onDraftChange}
                onSend={onSend}
            />
        </section>
    )
}

export default DirectChatView;
