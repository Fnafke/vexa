import SidebarComponent from "@/components/SidebarComponent";
import AddAFriendForm from "@/components/social/AddAFriendForm";
import FriendListDisplay from "@/components/social/FriendListDisplay";

const DirectMessagesPage = () => {
    return (
        <>
            <title>Vexa - Direct Messages</title>
            <SidebarComponent />
            <div className="m-auto flex h-full w-full max-w-4xl flex-col items-center justify-center gap-6 px-4 py-8">
                <AddAFriendForm />
                <FriendListDisplay />
            </div>
        </>
    )
}

export default DirectMessagesPage;