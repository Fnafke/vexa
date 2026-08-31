const parseInstant = (dateString: string): Date => new Date(dateString);

const formatDate = (dateValue: string) => {
        const date = parseInstant(dateValue);

        if (Number.isNaN(date.getTime())) {
            return dateValue;
        }

        return new Intl.DateTimeFormat(undefined, {
            dateStyle: "long",
            timeStyle: "short",
        }).format(date);
    };

const timeAgo = (date: Date): string => {
    const now = new Date();
    const diffMs = now.getTime() - date.getTime();
    const diffSeconds = Math.floor(diffMs / 1000);

    if (diffSeconds < 0) {
        return "just now";
    }

    const intervals: [string, number][] = [
        ["y", 31536000],
        ["mo", 2592000],
        ["w", 604800],
        ["d", 86400],
        ["h", 3600],
        ["m", 60],
    ];

    for (const [label, secondsInUnit] of intervals) {
        const count = Math.floor(diffSeconds / secondsInUnit);
        if (count >= 1) {
            return `${count}${label} ago`;
        }
    }

    return "just now";
};

export const Util = {
    parseInstant,
    timeAgo,
    formatDate
}