import SidebarComponent from "@/components/SidebarComponent";
import AddAFriendForm from "@/components/social/AddAFriendForm";
import FriendListDisplay from "@/components/social/FriendListDisplay";
import { SidebarInset } from "@/components/ui/sidebar";

const DirectMessagesPage = () => {
    return (
        <>
            <title>Vexa - Direct Messages</title>
            <SidebarComponent />
            <SidebarInset className="min-h-svh p-4 md:p-6">
                <div className="flex min-h-0 w-full flex-1 flex-col gap-4">
                    <AddAFriendForm />
                    <FriendListDisplay className="flex-1" fullHeight />
                </div>
            </SidebarInset>
        </>
    )
}

export default DirectMessagesPage;
