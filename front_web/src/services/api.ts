// Configuração base da API usando Fetch API
const baseURL = import.meta.env.DEV ? '/v1' : import.meta.env.VITE_API_URL;

interface RequestConfig extends RequestInit {
  params?: Record<string, any>;
}

class ApiClient {
  private baseURL: string;

  constructor(baseURL: string) {
    this.baseURL = baseURL;
  }

  private async request<T>(
    endpoint: string,
    config: RequestConfig = {}
  ): Promise<T> {
    const { params, ...fetchConfig } = config;
    
    // Constrói URL com query params se existirem
    let url = `${this.baseURL}${endpoint}`;
    if (params) {
      const searchParams = new URLSearchParams();
      Object.entries(params).forEach(([key, value]) => {
        if (value !== undefined && value !== null) {
          searchParams.append(key, String(value));
        }
      });
      const queryString = searchParams.toString();
      if (queryString) url += `?${queryString}`;
    }

    // Adiciona token de autenticação automaticamente
    const token = localStorage.getItem('token');
    const headers: HeadersInit = {
      'Content-Type': 'application/json',
      ...fetchConfig.headers,
    };
    
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    try {
      const response = await fetch(url, {
        ...fetchConfig,
        headers,
      });

      // Trata erro 401 (Unauthorized) - redireciona para login
      if (response.status === 401) {
        localStorage.removeItem('token');
        localStorage.removeItem('staffAuth');
        window.location.href = '/staff/login';
        throw new Error('Unauthorized');
      }

      // Trata outros erros HTTP
      if (!response.ok) {
        let errorMessage = response.statusText;
        
        // Tenta pegar mensagem de erro do corpo da resposta
        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
          try {
            const errorData = await response.json();
            errorMessage = errorData.message || errorData.detail || errorData.error || errorMessage;
          } catch {
            // Se falhar ao fazer parse do JSON, usa statusText
          }
        } else {
          // Se não for JSON, tenta pegar texto
          try {
            const errorText = await response.text();
            if (errorText) errorMessage = errorText;
          } catch {
            // Usa statusText se falhar
          }
        }
        
        throw new Error(errorMessage);
      }

      // Verifica se há conteúdo para retornar
      const contentType = response.headers.get('content-type');
      if (contentType && contentType.includes('application/json')) {
        return await response.json();
      }
      
      // Se não for JSON, retorna objeto vazio para DELETE sem corpo
      return {} as T;
    } catch (error) {
      if (error instanceof Error) {
        throw error;
      }
      throw new Error('Network error');
    }
  }

  async get<T>(endpoint: string, config?: RequestConfig): Promise<T> {
    return this.request<T>(endpoint, { ...config, method: 'GET' });
  }

  async post<T>(endpoint: string, data?: any, config?: RequestConfig): Promise<T> {
    return this.request<T>(endpoint, {
      ...config,
      method: 'POST',
      body: data ? JSON.stringify(data) : undefined,
    });
  }

  async put<T>(endpoint: string, data?: any, config?: RequestConfig): Promise<T> {
    return this.request<T>(endpoint, {
      ...config,
      method: 'PUT',
      body: data ? JSON.stringify(data) : undefined,
    });
  }

  async patch<T>(endpoint: string, data?: any, config?: RequestConfig): Promise<T> {
    return this.request<T>(endpoint, {
      ...config,
      method: 'PATCH',
      body: data ? JSON.stringify(data) : undefined,
    });
  }

  async delete<T>(endpoint: string, config?: RequestConfig): Promise<T> {
    return this.request<T>(endpoint, { ...config, method: 'DELETE' });
  }
}

const api = new ApiClient(baseURL);

export default api;