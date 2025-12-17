import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { ordersService, Order, CreateOrder } from '@/services/orders.service';
import { toast } from 'sonner';

export const useOrders = (status?: string) => {
  const queryClient = useQueryClient();

  const { data: orders = [], isLoading, error } = useQuery({
    queryKey: ['orders', status],
    queryFn: () => ordersService.getAll(status),
  });

  const createMutation = useMutation({
    mutationFn: (order: CreateOrder) => ordersService.create(order),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['orders'] });
      toast.success('Pedido criado com sucesso!');
    },
    onError: (error: any) => {
      toast.error('Erro ao criar pedido', {
        description: error.response?.data?.detail || error.message,
      });
    },
  });

  const updateStatusMutation = useMutation({
    mutationFn: ({ id, status }: { id: number; status: string }) =>
      ordersService.updateStatus(id, status),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['orders'] });
      toast.success('Status atualizado com sucesso!');
    },
    onError: (error: any) => {
      toast.error('Erro ao atualizar status', {
        description: error.response?.data?.detail || error.message,
      });
    },
  });

  const cancelMutation = useMutation({
    mutationFn: (id: number) => ordersService.cancel(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['orders'] });
      toast.success('Pedido cancelado com sucesso!');
    },
    onError: (error: any) => {
      toast.error('Erro ao cancelar pedido', {
        description: error.response?.data?.detail || error.message,
      });
    },
  });

  return {
    orders,
    isLoading,
    error,
    createOrder: createMutation.mutate,
    updateOrderStatus: updateStatusMutation.mutate,
    cancelOrder: cancelMutation.mutate,
    isCreating: createMutation.isPending,
    isUpdating: updateStatusMutation.isPending,
    isCanceling: cancelMutation.isPending,
  };
};
