import {
    Message as MessageContainer,
    MessageAvatar,
    MessageContent,
    MessageFooter,
    MessageHeader,
} from "@/components/ui/message";
import type { Message } from "@/types/types";

type DirectChatMessageItemProps = {
    message: Message;
    isOwnMessage: boolean;
}

const timeFormatter = new Intl.DateTimeFormat(undefined, {
    hour: "numeric",
    minute: "2-digit",
});

const getInitials = (username: string) => username.trim().slice(0, 2).toUpperCase();

const DirectChatMessageItem = ({ message, isOwnMessage }: DirectChatMessageItemProps) => {
    return (
        <MessageContainer align={isOwnMessage ? "end" : "start"}>
            <MessageAvatar className="h-8 w-8 text-xs font-semibold">
                {getInitials(message.sender.username)}
            </MessageAvatar>
            <MessageContent className="max-w-[min(78%,36rem)]">
                <MessageHeader className={isOwnMessage ? "justify-end" : undefined}>
                    {message.sender.username}
                </MessageHeader>
                <div
                    data-slot="message-bubble"
                    className={
                        isOwnMessage
                            ? "rounded-2xl rounded-br-md bg-primary px-3 py-2 text-sm text-primary-foreground"
                            : "rounded-2xl rounded-bl-md bg-muted px-3 py-2 text-sm text-foreground"
                    }
                >
                    {message.content}
                </div>
                <MessageFooter>
                    {timeFormatter.format(new Date(message.createdAt))}
                </MessageFooter>
            </MessageContent>
        </MessageContainer>
    )
}

export default DirectChatMessageItem;
