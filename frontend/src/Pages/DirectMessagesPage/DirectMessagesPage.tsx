import SidebarComponent from "@/components/SidebarComponent";
import AddAFriendForm from "@/components/social/AddAFriendForm";
import ChatsSidebar from "@/components/social/ChatsSidebar";
import FriendListDisplay from "@/components/social/FriendListDisplay";
import { SidebarInset } from "@/components/ui/sidebar";
import { useCallback, useState } from "react";

const DirectMessagesPage = () => {
    const [pendingRefreshKey, setPendingRefreshKey] = useState<number>(0);

    const handlePendingRefresh = useCallback(() => {
        setPendingRefreshKey((prevKey) => prevKey + 1);
    }, []);

    return (
        <>
            <title>Vexa - Direct Messages</title>
            <SidebarComponent />
            <SidebarInset className="min-h-svh p-4 md:p-6">
                <div className="flex min-h-0 w-full flex-1 flex-col gap-4 lg:flex-row">
                    <ChatsSidebar className="lg:h-[calc(100svh-3rem)]" />
                    <div className="flex min-h-0 flex-1 flex-col gap-4">
                        <AddAFriendForm onFriendRequestSent={handlePendingRefresh} />
                        <FriendListDisplay className="flex-1" fullHeight pendingRefreshKey={pendingRefreshKey} />
                    </div>
                </div>
            </SidebarInset>
        </>
    )
}

export default DirectMessagesPage;
