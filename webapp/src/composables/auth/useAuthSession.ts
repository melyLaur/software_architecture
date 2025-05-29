export type UserRole = 'EMPLOYEE' | 'MANAGER' | 'SECRETARY'

export interface AuthUser {
    id: string
    name: string
    email: string
    role: UserRole
}

const useLocalStorageService = () => {
    const userKey = 'user';
    const tokenKey = 'token';

    const setItem = (key: string, value: string) => {
        localStorage.setItem(key, value);
    }

    const getItem = (key: string): string | null => {
        return localStorage.getItem(key);
    }

    const removeItem = (key: string) => {
        localStorage.removeItem(key);
    }

    return {
        setItem,
        getItem,
        removeItem,
        keys: {
            UserKey: userKey,
            TokenKey: tokenKey,
        }
    };
}

export const useAuthSession = () => {
    const setToken = (token: string) => {
        const localStorageService = useLocalStorageService();
        localStorageService.setItem(localStorageService.keys.TokenKey, token);
    }

    const getToken = () => {
        const localStorageService = useLocalStorageService();
        return localStorageService.getItem(localStorageService.keys.TokenKey);
    }

    const setUser = (user: AuthUser) => {
        const localStorageService = useLocalStorageService();
        localStorageService.setItem(localStorageService.keys.UserKey, JSON.stringify(user));
        currentUser.value = user;
    }

    const getUser = (): AuthUser | null => {
        const localStorageService = useLocalStorageService();
        const user = localStorageService.getItem(localStorageService.keys.UserKey);
        return user ? JSON.parse(user) : null;
    }

    const currentUser = useState('currentUser', () => {
        const user = getUser();
        return user ? user : null;
    })

    return {
        setToken,
        getToken,
        setUser,
        getUser,
        currentUser,
    }
}