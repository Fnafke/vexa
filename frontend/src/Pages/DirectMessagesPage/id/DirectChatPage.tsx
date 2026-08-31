import SidebarComponent from "@/components/SidebarComponent";
import ChatsSidebar from "@/components/social/ChatsSidebar";
import { SidebarInset } from "@/components/ui/sidebar";
import type { DirectChat } from "@/types/types";
import { useState } from "react";
import { useParams } from "react-router-dom";

const DirectChatPage = () => {
    const { id } = useParams();
    const [directChat, setDirectChat] = useState<DirectChat | null>(null);
    

    return (
            <>
            <title>Vexa - Direct Messages</title>
            <SidebarComponent />
            <SidebarInset className="min-h-svh p-4 md:p-6">
                <div className="flex min-h-0 w-full flex-1 flex-col gap-4 lg:flex-row">
                    <ChatsSidebar className="lg:h-[calc(100svh-3rem)]" />
                    <div className="flex min-h-0 flex-1 flex-col gap-4">
                    </div>
                </div>
            </SidebarInset>
        </>
    )
}

export default DirectChatPage;