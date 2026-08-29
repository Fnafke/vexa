import SidebarComponent from "@/components/SidebarComponent";
import FriendListDisplay from "@/components/social/FriendListDisplay";

const HomePage = () => {
    return (
        <>
            <title>Vexa - Home</title>
            <SidebarComponent />
            <div className="m-auto flex h-full w-full max-w-4xl flex-col items-center justify-center gap-6 px-4 py-8">
                <h1 className="text-4xl font-bold text-center mb-4">Welcome to Vexa</h1>
                <p className="text-lg text-center text-muted-foreground mb-8">
                    Connect with your friends and enjoy the experience!
                </p>
                <FriendListDisplay />
            </div>
        </>
    )
}

export default HomePage;