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

export type Friendship = {
    id: string;
    requester: PublicUser;
    addressee: PublicUser;
    status: FriendshipStatus;
    createdAt: Date;
}

export type FriendList = {
    friends: Friendship[];
    page: number;
    pageSize: number;
    totalElements: number;
    totalPages: number;
}

export type Role = "USER" | "ADMIN"

export type FriendshipStatus = "PENDING" | "ACCEPTED" | "DECLINED"