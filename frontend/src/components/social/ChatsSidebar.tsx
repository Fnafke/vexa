import { DirectChatService } from "@/services/DirectChatService";
import type { DirectChat } from "@/types/types";
import { AuthContext } from "@/components/context/AuthContext";
import { Button } from "@/components/ui/button";
import { Separator } from "@/components/ui/separator";
import { Skeleton } from "@/components/ui/skeleton";
import { cn } from "@/lib/utils";
import { MessageCircle, UserRound, UsersRound } from "lucide-react";
import { useCallback, useContext, useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

type ChatsSidebarProps = {
    className?: string;
}

const ChatsSidebar = ({ className }: ChatsSidebarProps) => {
    const [directChats, setDirectChats] = useState<DirectChat[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState("");

    const authContext = useContext(AuthContext);
    const location = useLocation();
    const navigate = useNavigate();

    const fetchDirectChats = useCallback(async () => {
        setIsLoading(true);
        setErrorMessage("");

        try {
            const response = await DirectChatService.getAllDirectChatsForUser();
            if (!response.ok) {
                throw new Error("Failed to fetch direct chats");
            }
            const data: DirectChat[] = await response.json();
            setDirectChats(data);
        } catch (error) {
            console.error("Error fetching direct chats:", error);
            setErrorMessage("Could not load chats.");
        } finally {
            setIsLoading(false);
        }
    }, []);

    useEffect(() => {
        fetchDirectChats();
    }, [fetchDirectChats]);

    const getChatUser = (chat: DirectChat) => {
        return chat.userOne.username === authContext?.user?.username
            ? chat.userTwo
            : chat.userOne;
    }

    return (
        <aside
            className={cn(
                "flex w-full flex-col rounded-2xl border border-border bg-card p-3 shadow-[0_12px_36px_-18px_rgba(15,23,42,0.22)] md:max-w-72",
                className
            )}
        >
            <section className="space-y-2">
                <Button
                    className="h-11 w-full justify-start gap-3 cursor-pointer"
                    onClick={() => navigate("/direct-messages")}
                    type="button"
                    variant={location.pathname === "/direct-messages" ? "default" : "ghost"}
                >
                    <UsersRound className="h-4 w-4" />
                    Friends
                </Button>
            </section>

            <Separator className="my-3" />

            <section className="flex min-h-0 flex-1 flex-col">
                <div className="mb-2 flex items-center justify-between px-2">
                    <h2 className="text-xs font-semibold uppercase text-muted-foreground">
                        Direct chats
                    </h2>
                    <span className="text-xs font-medium text-muted-foreground">
                        {directChats.length}
                    </span>
                </div>

                {isLoading && (
                    <div className="space-y-2 px-1">
                        {[1, 2, 3, 4].map((item) => (
                            <div key={item} className="flex items-center gap-3 rounded-xl px-2 py-2">
                                <Skeleton className="h-9 w-9 rounded-full" />
                                <div className="min-w-0 flex-1 space-y-2">
                                    <Skeleton className="h-3 w-24" />
                                    <Skeleton className="h-2.5 w-16" />
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

                {!isLoading && !errorMessage && directChats.length === 0 && (
                    <div className="rounded-lg border border-dashed border-border bg-muted/20 px-3 py-5 text-center">
                        <div className="mx-auto mb-3 flex h-10 w-10 items-center justify-center rounded-full bg-primary/10 text-primary">
                            <MessageCircle className="h-5 w-5" />
                        </div>
                        <p className="text-sm font-medium text-foreground">No chats yet</p>
                        <p className="mt-1 text-xs text-muted-foreground">Direct chats will appear here.</p>
                    </div>
                )}

                {!isLoading && !errorMessage && directChats.length > 0 && (
                    <ul className="min-h-0 flex-1 space-y-1 overflow-auto pr-1">
                        {directChats.map((chat) => {
                            const chatUser = getChatUser(chat);
                            const chatPath = `/direct-messages/${chat.id}`;

                            return (
                                <li key={chat.id}>
                                    <Button
                                        className="h-12 w-full justify-start gap-3 px-2 cursor-pointer"
                                        onClick={() => navigate(chatPath)}
                                        type="button"
                                        variant={location.pathname === chatPath ? "secondary" : "ghost"}
                                    >
                                        <span className="flex h-9 w-9 shrink-0 items-center justify-center rounded-full bg-primary/10 text-primary ring-1 ring-primary/15">
                                            <UserRound className="h-4 w-4" />
                                        </span>
                                        <span className="min-w-0 text-left">
                                            <span className="block truncate text-sm font-medium">{chatUser.username}</span>
                                            <span className="block truncate text-xs text-muted-foreground">Direct chat</span>
                                        </span>
                                    </Button>
                                </li>
                            );
                        })}
                    </ul>
                )}
            </section>
        </aside>
    )
}

export default ChatsSidebar;
