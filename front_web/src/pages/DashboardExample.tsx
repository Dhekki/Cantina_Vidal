/**
 * EXEMPLO DE INTEGRAÇÃO - DASHBOARD COM DADOS DO BACKEND
 * 
 * Este arquivo demonstra como usar o hook useReports para exibir dados do dashboard.
 * Pode ser integrado no StaffDashboard.tsx.
 */

import { useReports } from '@/hooks/useReports';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { 
  DollarSign, 
  ShoppingCart, 
  Clock, 
  AlertTriangle,
  TrendingUp 
} from 'lucide-react';

const DashboardExample = () => {
  const { dashboardData, isDashboardLoading } = useReports();

  if (isDashboardLoading) {
    return (
      <div className="flex items-center justify-center h-screen">
        <div className="text-lg">Carregando dashboard...</div>
      </div>
    );
  }

  if (!dashboardData) {
    return (
      <div className="flex items-center justify-center h-screen">
        <div className="text-lg text-red-500">Erro ao carregar dados do dashboard</div>
      </div>
    );
  }

  return (
    <div className="p-6 space-y-6">
      <h1 className="text-3xl font-bold">Dashboard</h1>

      {/* Cards de Métricas */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Vendas Hoje</CardTitle>
            <DollarSign className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              R$ {dashboardData.vendas_hoje.toFixed(2)}
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Pedidos Pendentes</CardTitle>
            <ShoppingCart className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {dashboardData.pedidos_pendentes}
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Em Preparo</CardTitle>
            <Clock className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {dashboardData.pedidos_em_preparo}
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Estoque Baixo</CardTitle>
            <AlertTriangle className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {dashboardData.produtos_baixo_estoque}
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Vendas da Semana */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <TrendingUp className="h-5 w-5" />
            Vendas da Semana
          </CardTitle>
        </CardHeader>
        <CardContent>
          <div className="text-3xl font-bold">
            R$ {dashboardData.vendas_semana.toFixed(2)}
          </div>
        </CardContent>
      </Card>

      {/* Top Produtos */}
      <Card>
        <CardHeader>
          <CardTitle>Produtos Mais Vendidos</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            {dashboardData.top_produtos.map((produto, index) => (
              <div key={index} className="flex items-center justify-between">
                <div className="flex items-center gap-3">
                  <div className="flex h-8 w-8 items-center justify-center rounded-full bg-primary text-primary-foreground font-semibold">
                    {index + 1}
                  </div>
                  <span className="font-medium">{produto.nome}</span>
                </div>
                <span className="text-muted-foreground">
                  {produto.quantidade} unidades
                </span>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  );
};

export default DashboardExample;
