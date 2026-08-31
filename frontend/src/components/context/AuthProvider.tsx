import type { User } from "@/types/types";
import { useEffect, useState, type ReactNode } from "react";

import { AuthContext } from "./AuthContext";
import { AuthService } from "@/services/AuthService";
import { UserService } from "@/services/UserService";
import { WebSocketService } from "@/services/WebSocketService";


export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const [user, setUser] = useState<User | null>(null);
    const [isLoading, setIsLoading] = useState(true);

    const login = (userData: User) => {
        setUser(userData);
    };

    const logout = async () => {
        await AuthService.logout();
        setUser(null);
    };

    const fetchUserData = async () => {
        try {
            const response = await UserService.fetchCurrentUser();

            if (response && response.ok) {
                const userData: User = await response.json();
                setUser(userData);
            } else {
                logout();
            }
        } catch {
            setUser(null);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        fetchUserData();
    }, []);

    useEffect(() => {
        if (user) {
            WebSocketService.connect();
        } else {
            WebSocketService.disconnect();
        }
    }, [user]);

    return (
        <AuthContext.Provider value={{ user, isLoading, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};