import type { FormEvent } from "react";
import { SendHorizontal } from "lucide-react";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";

type DirectChatComposerProps = {
    draft: string;
    onDraftChange: (draft: string) => void;
    onSend: () => void | Promise<void>;
    disabled?: boolean;
}

const DirectChatComposer = ({ draft, onDraftChange, onSend, disabled = false }: DirectChatComposerProps) => {
    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        void onSend();
    }

    return (
        <form className="flex gap-2 border-t border-border p-3" onSubmit={handleSubmit}>
            <Input
                className="h-10 flex-1 rounded-xl bg-background px-3"
                disabled={disabled}
                onChange={(event) => onDraftChange(event.target.value)}
                placeholder="Type a message"
                value={draft}
            />
            <Button
                aria-label="Send message"
                className="h-10 w-10 cursor-pointer rounded-xl"
                disabled={disabled || !draft.trim()}
                size="icon"
                type="submit"
            >
                <SendHorizontal className="h-4 w-4" />
            </Button>
        </form>
    )
}

export default DirectChatComposer;
