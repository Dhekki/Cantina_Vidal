/**
 * EXEMPLO DE INTEGRAÇÃO - GERENCIAMENTO DE PEDIDOS
 * 
 * Este arquivo demonstra como usar o hook useOrders para gerenciar pedidos.
 * Pode ser integrado no InternalOrders.tsx ou componentes similares.
 */

import { useState } from 'react';
import { useOrders } from '@/hooks/useOrders';
import { CreateOrder } from '@/services/orders.service';
import { Button } from '@/components/ui/button';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
} from '@/components/ui/select';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import { Badge } from '@/components/ui/badge';

const OrdersManagementExample = () => {
  const [statusFilter, setStatusFilter] = useState<string | undefined>();
  const {
    orders,
    isLoading,
    createOrder,
    updateOrderStatus,
    cancelOrder,
    isUpdating,
  } = useOrders(statusFilter);

  const handleStatusChange = (orderId: number, newStatus: string) => {
    updateOrderStatus({ id: orderId, status: newStatus });
  };

  const handleCancelOrder = (orderId: number) => {
    if (confirm('Tem certeza que deseja cancelar este pedido?')) {
      cancelOrder(orderId);
    }
  };

  const getStatusColor = (status: string) => {
    const colors: Record<string, string> = {
      pendente: 'bg-yellow-100 text-yellow-800',
      preparando: 'bg-blue-100 text-blue-800',
      pronto: 'bg-green-100 text-green-800',
      entregue: 'bg-gray-100 text-gray-800',
      cancelado: 'bg-red-100 text-red-800',
    };
    return colors[status] || 'bg-gray-100 text-gray-800';
  };

  if (isLoading) {
    return <div className="p-6">Carregando pedidos...</div>;
  }

  return (
    <div className="p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">Gerenciamento de Pedidos</h1>
        
        <Select value={statusFilter || 'all'} onValueChange={(value) => setStatusFilter(value === 'all' ? undefined : value)}>
          <SelectTrigger className="w-[180px]">
            {statusFilter || 'Todos os Status'}
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="all">Todos os Status</SelectItem>
            <SelectItem value="pendente">Pendente</SelectItem>
            <SelectItem value="preparando">Preparando</SelectItem>
            <SelectItem value="pronto">Pronto</SelectItem>
            <SelectItem value="entregue">Entregue</SelectItem>
            <SelectItem value="cancelado">Cancelado</SelectItem>
          </SelectContent>
        </Select>
      </div>

      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>#</TableHead>
            <TableHead>Cliente</TableHead>
            <TableHead>Turma</TableHead>
            <TableHead>Total</TableHead>
            <TableHead>Status</TableHead>
            <TableHead>Data</TableHead>
            <TableHead className="text-right">Ações</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {orders.map((order) => (
            <TableRow key={order.id}>
              <TableCell className="font-medium">#{order.id}</TableCell>
              <TableCell>{order.cliente_nome}</TableCell>
              <TableCell>{order.cliente_turma || '-'}</TableCell>
              <TableCell>R$ {order.total.toFixed(2)}</TableCell>
              <TableCell>
                <Badge className={getStatusColor(order.status)}>
                  {order.status}
                </Badge>
              </TableCell>
              <TableCell>
                {new Date(order.created_at).toLocaleDateString('pt-BR')}
              </TableCell>
              <TableCell className="text-right space-x-2">
                {order.status === 'pendente' && (
                  <Button
                    size="sm"
                    onClick={() => handleStatusChange(order.id, 'preparando')}
                    disabled={isUpdating}
                  >
                    Iniciar Preparo
                  </Button>
                )}
                {order.status === 'preparando' && (
                  <Button
                    size="sm"
                    onClick={() => handleStatusChange(order.id, 'pronto')}
                    disabled={isUpdating}
                  >
                    Marcar Pronto
                  </Button>
                )}
                {order.status === 'pronto' && (
                  <Button
                    size="sm"
                    onClick={() => handleStatusChange(order.id, 'entregue')}
                    disabled={isUpdating}
                  >
                    Entregar
                  </Button>
                )}
                {order.status !== 'entregue' && order.status !== 'cancelado' && (
                  <Button
                    size="sm"
                    variant="destructive"
                    onClick={() => handleCancelOrder(order.id)}
                    disabled={isUpdating}
                  >
                    Cancelar
                  </Button>
                )}
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  );
};

export default OrdersManagementExample;
