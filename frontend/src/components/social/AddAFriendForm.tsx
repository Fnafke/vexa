import { useState, type FormEvent } from "react";
import { Button } from "../ui/button";
import { Input } from "../ui/input";
import { Check, Plus, UserPlus, X } from "lucide-react";
import { FriendshipService } from "@/services/FriendshipService";

const AddAFriendForm = () => {
    const [showForm, setShowForm] = useState<boolean>(false);
    const [friendUsername, setFriendUsername] = useState<string>('');
    const [successMessage, setSuccessMessage] = useState<string>("");

    const handleSubmit = async(event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        if (!friendUsername.trim()) {
            return;
        }

        const response = await FriendshipService.sendFriendRequest(friendUsername.trim());

        if (!response.ok) {
            const errorData = await response.json();
            setSuccessMessage(`Error: ${errorData.message || "Failed to send friend request."}`);
            return;
        }

        setSuccessMessage(`Friend request sent to ${friendUsername.trim()}!`);
        setFriendUsername("");
    };

    const handleCancel = () => {
        setShowForm(false);
        setFriendUsername("");
        setSuccessMessage("");
    };

    return (
        <div className="w-full max-w-md">
            {showForm && (
                <div className="fixed inset-0 z-50 flex items-center justify-center bg-background/55 px-4 backdrop-blur-md">
                    <form
                        className="w-full max-w-md rounded-2xl border border-primary/15 bg-card p-5 shadow-[0_28px_90px_-28px_rgba(15,23,42,0.65)] ring-1 ring-primary/10"
                        onSubmit={handleSubmit}
                    >
                        <div className="mb-4 flex items-start gap-3">
                            <div className="flex size-11 shrink-0 items-center justify-center rounded-xl bg-primary text-primary-foreground shadow-sm">
                                <UserPlus className="h-5 w-5" />
                            </div>
                            <div className="min-w-0">
                                <h2 className="text-lg font-semibold tracking-tight text-foreground">Add a friend</h2>
                                <p className="text-sm text-muted-foreground">
                                    Search by username to start a new connection.
                                </p>
                            </div>
                        </div>

                        <div className="flex flex-col gap-3 sm:flex-row">
                            <Input
                                autoFocus
                                className="h-10 bg-background px-4"
                                type="text"
                                placeholder="Friend's username"
                                value={friendUsername}
                                onChange={(e) => {
                                    setFriendUsername(e.target.value);
                                    setSuccessMessage("");
                                }}
                            />
                            <Button className="h-10 sm:w-28" disabled={!friendUsername.trim()} type="submit">
                                <Check />
                                Add
                            </Button>
                        </div>

                        {successMessage && (
                            <p className="mt-3 rounded-xl border border-primary/15 bg-primary/5 px-3 py-2 text-sm text-foreground">
                                {successMessage}
                            </p>
                        )}

                        <Button className="mt-3 w-full" onClick={handleCancel} type="button" variant="ghost">
                            <X />
                            Cancel
                        </Button>
                    </form>
                </div>
            )}

            {!showForm && (
                <Button
                    className="h-11 w-full justify-start gap-3 border-primary/15 bg-card px-4 text-left shadow-[0_14px_40px_-24px_rgba(15,23,42,0.45)] hover:bg-primary/5"
                    onClick={() => setShowForm(true)}
                    variant="outline"
                >
                    <span className="flex size-7 items-center justify-center rounded-lg bg-primary text-primary-foreground">
                        <Plus className="h-4 w-4" />
                    </span>
                    Add Friend
                </Button>
            )}
        </div>
    )
}

export default AddAFriendForm;
