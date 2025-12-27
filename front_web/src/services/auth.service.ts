// Configuração base da API
// Em desenvolvimento, usa proxy do Vite (/v1)
// Em produção, usa URL completa do .env
const API_BASE_URL = import.meta.env.DEV ? '/v1' : import.meta.env.VITE_API_URL;

export interface LoginCredentials {
    email: string;
    password: string;
}

export interface AuthResponse {
    access_token:  string;
    refresh_token: string;
    username:      string;
    cargo:         string;
}

export interface User {
    id:            number;
    username:      string;
    nome_completo: string;
    email?:        string;
    ativo:         boolean;
}

// Classe de erro personalizada para erros da API
class ApiError extends Error {
    constructor(
        message: string,
        public status: number,
        public data?: any
    ) {
        super(message);
        this.name = 'ApiError';
    }
}

// Função auxiliar para fazer requisições com fetch
async function fetchWithAuth<T>(
    endpoint: string,
    options: RequestInit = {}
): Promise<T> {
    // Recupera o token do localStorage
    const token = localStorage.getItem('token');
    
    // Configura headers padrão
    const headers: HeadersInit = {
        'Content-Type': 'application/json',
        ...options.headers,
    };
    
    // Adiciona o token de autenticação se existir
    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }
    
    // Faz a requisição
    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
        ...options,
        headers,
    });
    
    // Verifica se a resposta foi bem-sucedida
    if (!response.ok) {
        let errorData;
        try {
            errorData = await response.json();
        } catch {
            errorData = { message: response.statusText };
        }
        
        throw new ApiError(
            errorData.message || `HTTP Error ${response.status}`,
            response.status,
            errorData
        );
    }
    
    // Retorna os dados em JSON
    return response.json();
}

export const authService = {
    async login(credentials: LoginCredentials): Promise<AuthResponse> {
        const response = await fetchWithAuth<AuthResponse>('/auth/login', {
            method: 'POST',
            body: JSON.stringify({
                email: credentials.email,
                password: credentials.password,
            }),
        });

        return response;
    },

    async getMe(): Promise<User> {
        const response = await fetchWithAuth<User>('/auth/me', {
            method: 'GET',
        });

        return response;
    },

    logout(): void {
        localStorage.removeItem('token');
        localStorage.removeItem('staffAuth');
    },
};