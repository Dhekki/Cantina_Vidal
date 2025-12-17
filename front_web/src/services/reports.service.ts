import api from './api';

export interface SalesReport {
  total_vendas: number;
  total_pedidos: number;
  ticket_medio: number;
  vendas_por_dia?: Array<{
    data: string;
    total: number;
    quantidade: number;
  }>;
}

export interface ProductReport {
  produto_id: number;
  nome: string;
  quantidade_vendida: number;
  receita_total: number;
}

export interface DashboardData {
  vendas_hoje: number;
  pedidos_pendentes: number;
  pedidos_em_preparo: number;
  produtos_baixo_estoque: number;
  vendas_semana: number;
  top_produtos: Array<{
    nome: string;
    quantidade: number;
  }>;
}

export const reportsService = {
  // Obtém relatório de vendas por período
  async getSales(dataInicio?: string, dataFim?: string): Promise<SalesReport> {
    const params: Record<string, string> = {};

    if(dataInicio) params.data_inicio = dataInicio;
    if(dataFim) params.data_fim = dataFim;
    
    return await api.get<SalesReport>('/relatorios/vendas', { params });
  },
  
  // Obtém relatório de produtos mais vendidos
  async getProducts(): Promise<ProductReport[]> {
    return await api.get<ProductReport[]>('/relatorios/produtos');
  },

  // Obtém dados do dashboard
  async getDashboard(): Promise<DashboardData> {
    return await api.get<DashboardData>('/relatorios/dashboard');
  },
};
