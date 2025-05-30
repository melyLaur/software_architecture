import {useAuthSession} from "~/composables/auth/useAuthSession";
import {FetchError} from "ofetch";

export type HttpMethod = 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH';
export type ApiRequestBody = Record<string, unknown> | BodyInit | null | undefined;

interface ApiResponse<T> {
    data: T;
    success: boolean;
    errorCode: string;
}

interface RequestOptions<T extends ApiRequestBody> {
    method: HttpMethod;
    url: string;
    body?: T;
    query?: Record<string, unknown>;
}

export class ApiClient {
    private getAuthHeaders(): HeadersInit {
        const {getToken} = useAuthSession();
        const token = getToken();

        return token
            ? { Authorization: `Bearer ${token}` }
            : {};
    }

    async request<TResponse, TBody extends ApiRequestBody = undefined>(
        options: RequestOptions<TBody>
    ): Promise<ApiResponse<TResponse>> {
        const {method, url, body, query} = options;

        try {
            const response = await $fetch<ApiResponse<TResponse>>(url, {
                method: method,
                headers: this.getAuthHeaders(),
                body: body,
                query: query,
            });

            console.group()
            console.debug('Requête réussie :', method, url);
            console.debug('Corps de la requête :', body);
            console.debug("Réponse de l'API :", response);
            console.groupEnd();
            return response;
        } catch (error) {
            if (error instanceof FetchError) {
                console.group();
                console.error('Erreur lors de la requête :', method, url);
                console.error('Statut HTTP :', error.response?.status);
                console.error('Corps de la réponse :', error.response?._data);
                console.groupEnd();
                throw new Error(error.response?._data?.message ?? 'Erreur inconnue');
            }
            console.error('Erreur inconnue :', error);
            throw error;
        }
    }
}