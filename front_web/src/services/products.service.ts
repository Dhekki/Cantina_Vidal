import api from './api';

export interface Product {
    id?: number;
    nome: string;
    descricao?: string;
    preco: number;
    estoque: number;
    categoria: string;
    imagem_url?: string;
    ativo?: boolean;
}

export interface UpdateStockRequest {
    quantidade: number;
}

export const productsService = {  
    // Busca todos os produtos
    async getAll(): Promise<Product[]> {
        return await api.get<Product[]>('/produtos/');
    },

    // Busca um produto por ID
    async getById(id: number): Promise<Product> {
        return await api.get<Product>(`/produtos/${id}`);
    },

    // Cria um novo produto
    async create(product: Product): Promise<Product> {
        return await api.post<Product>('/produtos/', product);
    },
  
    // Atualiza um produto existente
    async update(id: number, product: Partial<Product>): Promise<Product> {
        return await api.put<Product>(`/produtos/${id}`, product);
    },

    // Remove um produto
    async delete(id: number): Promise<{ message: string }> {
        return await api.delete<{ message: string }>(`/produtos/${id}`);
    },

    // Atualiza o estoque de um produto
    async updateStock(id: number, quantidade: number): Promise<Product> {
        return await api.patch<Product>(`/produtos/${id}/estoque`, { quantidade });
    },
};