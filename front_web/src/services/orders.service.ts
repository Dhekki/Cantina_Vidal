import api from './api';

export interface OrderItem {
  produto_id:      number;
  quantidade:      number;
  preco_unitario?: number;
}

export interface CreateOrder {
  cliente_nome:   string;
  cliente_turma?: string;
  itens:          OrderItem[];
  observacoes?:   string;
}

export interface Order {
  id:             number;
  cliente_nome:   string;
  cliente_turma?: string;
  status:         'pendente' | 'preparando' | 'pronto' | 'entregue' | 'cancelado';
  total:          number;
  created_at:     string;
  updated_at?:    string;
  itens:          OrderItem[];
  observacoes?:   string;
}

export interface UpdateStatusRequest {
  status: 'pendente' | 'preparando' | 'pronto' | 'entregue' | 'cancelado';
}

export const ordersService = {
  // Busca todos os pedidos. Opcional: filtragem por status
  async getAll(status?: string): Promise<Order[]> {
    const params = status ? { status } : {};
    return await api.get<Order[]>('/pedidos/', { params });
  },

  // Busca um pedido por ID
  async getById(id: number): Promise<Order> {
    return await api.get<Order>(`/pedidos/${id}`);
  },

  // Cria um novo pedido
  async create(order: CreateOrder): Promise<Order> {
    return await api.post<Order>('/pedidos/', order);
  },

  // Atualiza o status de um pedido
  async updateStatus(id: number, status: string): Promise<Order> {
    return await api.patch<Order>(`/pedidos/${id}/status`, { status });
  },

  // Cancela um pedido
  async cancel(id: number): Promise<{ message: string }> {
    return await api.delete<{ message: string }>(`/pedidos/${id}`);
  },
};