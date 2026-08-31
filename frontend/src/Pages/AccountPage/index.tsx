import AccountDetailsOverview from "@/components/account/AccountDetailsOverview";
import SidebarComponent from "@/components/SidebarComponent";
import { SidebarInset } from "@/components/ui/sidebar";

const AccountPage = () => {
    return (
        <>
            <title>Vexa - Account</title>
            <SidebarComponent />
            <SidebarInset className="min-h-svh p-4 md:p-6">
                <div className="flex min-h-0 w-full flex-1 flex-col gap-4 lg:flex-row">
                    <AccountDetailsOverview/>
                </div>
            </SidebarInset>
        </>
    )
}

export default AccountPage;