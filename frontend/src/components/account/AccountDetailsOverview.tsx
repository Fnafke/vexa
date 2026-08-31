import { useContext, useState } from "react";
import { CalendarDays, CircleUserRound, IdCard, Mail, UserRound } from "lucide-react";
import { Separator } from "@/components/ui/separator";
import { cn } from "@/lib/utils";
import { AuthContext } from "../context/AuthContext";
import type { User } from "@/types/types";
import { Util } from "@/utils/utils";

const AccountDetailsOverview = () => {
    const context = useContext(AuthContext);
    const user: User | null = context?.user || null;
    const [revealEmail, setRevealEmail] = useState(false);

    const translateEmail = (email: string) => {
        if (revealEmail) {
            return email;
        }
        return email.replace(/^(.).*(@.*)$/, "$1***$2");
    };

    return (
        <section className="w-full rounded-[28px] border border-border bg-card p-6 shadow-[0_18px_60px_-28px_rgba(15,23,42,0.35)] sm:p-8">
            {user ? (
                <div className="space-y-6">
                    <div className="flex flex-col gap-6 sm:flex-row sm:items-center sm:gap-8">
                        <div className="relative flex h-32 w-32 shrink-0 items-center justify-center rounded-full border border-primary/15 bg-linear-to-br from-primary/15 via-background to-muted/40 text-primary shadow-[0_18px_50px_-24px_rgba(15,23,42,0.45)] sm:h-40 sm:w-40">
                            <CircleUserRound className="h-20 w-20 sm:h-24 sm:w-24" strokeWidth={1.6} />
                        </div>

                        <div className="min-w-0 flex-1 space-y-3">
                            <div className="space-y-1">
                                <p className="text-sm font-medium uppercase tracking-[0.28em] text-muted-foreground">
                                    Account Details
                                </p>
                                <h2 className="text-3xl font-semibold tracking-tight text-foreground sm:text-4xl">
                                    {user.username}
                                </h2>
                                <p className="max-w-xl text-sm leading-6 text-muted-foreground">
                                    Your profile summary and account information.
                                </p>
                            </div>

                            <div className="grid gap-3 rounded-2xl border border-border/70 bg-muted/20 p-4 text-sm sm:grid-cols-2">
                                <div className="flex items-center gap-3 rounded-xl bg-background/80 px-3 py-2">
                                    <div className="flex h-9 w-9 items-center justify-center rounded-full bg-primary/10 text-primary">
                                        <UserRound className="h-4.5 w-4.5" />
                                    </div>
                                    <div className="min-w-0">
                                        <p className="text-xs uppercase tracking-[0.2em] text-muted-foreground">Username</p>
                                        <p className="truncate font-medium text-foreground">{user.username}</p>
                                    </div>
                                </div>

                                <div className="flex items-center gap-3 rounded-xl bg-background/80 px-3 py-2">
                                    <div className="flex h-9 w-9 items-center justify-center rounded-full bg-primary/10 text-primary">
                                        <IdCard className="h-4.5 w-4.5" />
                                    </div>
                                    <div className="min-w-0">
                                        <p className="text-xs uppercase tracking-[0.2em] text-muted-foreground">User ID</p>
                                        <p className="truncate font-medium text-foreground">{user.id}</p>
                                    </div>
                                </div>

                                <div className="flex items-center gap-3 rounded-xl bg-background/80 px-3 py-2">
                                    <div className="flex h-9 w-9 items-center justify-center rounded-full bg-primary/10 text-primary">
                                        <Mail className="h-4.5 w-4.5" />
                                    </div>
                                    <div className="min-w-0">
                                        <p className="text-xs uppercase tracking-[0.2em] text-muted-foreground">Email</p>
                                        <div className="flex items-center gap-2">
                                            <p className="truncate font-medium text-foreground">{translateEmail(user.email)}</p>
                                            <button
                                                className="mt-1 text-xs font-medium text-primary cursor-pointer hover:underline"
                                                onClick={() => setRevealEmail(!revealEmail)}
                                            >
                                                {revealEmail ? "Hide" : "Reveal"}
                                            </button>
                                        </div>
                                    </div>
                                </div>

                                <div className="flex items-center gap-3 rounded-xl bg-background/80 px-3 py-2">
                                    <div className="flex h-9 w-9 items-center justify-center rounded-full bg-primary/10 text-primary">
                                        <CalendarDays className="h-4.5 w-4.5" />
                                    </div>
                                    <div className="min-w-0">
                                        <p className="text-xs uppercase tracking-[0.2em] text-muted-foreground">Created</p>
                                        <p className="truncate font-medium text-foreground">{Util.formatDate(user.createdAt)}</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <Separator className={cn("bg-border/80")} />

                    <p className="text-sm text-muted-foreground">
                        This profile section is using a temporary icon avatar until a real profile picture flow is added.
                    </p>
                </div>
            ) : (
                <div className="flex min-h-70 flex-col items-center justify-center gap-3 rounded-3xl border border-dashed border-border/70 bg-muted/20 px-6 text-center">
                    <div className="flex h-16 w-16 items-center justify-center rounded-full bg-primary/10 text-primary">
                        <CircleUserRound className="h-8 w-8" />
                    </div>
                    <div className="space-y-1">
                        <h2 className="text-lg font-semibold text-foreground">No user data available</h2>
                        <p className="text-sm text-muted-foreground">
                            Sign in again if your account details should be visible here.
                        </p>
                    </div>
                </div>
            )}
        </section>
    )
}

export default AccountDetailsOverview;