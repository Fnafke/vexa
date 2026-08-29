export type User = {
    id: string;
    username: string;
    email: string;
    role: Role;
    createdAt: Date;
}

export type AuthenticationRequest = {
    email: string;
    password: string;
}

export type AuthenticationResponse = {
    token: string;
    id: string;
    username: string;
    message: string;
}

export type SignupRequest = {
    username: string;
    email: string;
    password: string;
}

export type PublicUser = {
    id: string;
    username: string;
    createdAt: Date;
}

export type Role = "USER" | "ADMIN"