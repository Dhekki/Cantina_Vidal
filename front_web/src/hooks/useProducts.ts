import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { productsService, Product } from '@/services/products.service';
import { toast } from 'sonner';

export const useProducts = () => {
  const queryClient = useQueryClient();

  const { data: products = [], isLoading, error } = useQuery({
    queryKey: ['products'],
    queryFn: () => productsService.getAll(),
  });

  const createMutation = useMutation({
    mutationFn: (product: Product) => productsService.create(product),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['products'] });
      toast.success('Produto criado com sucesso!');
    },
    onError: (error: any) => {
      toast.error('Erro ao criar produto', {
        description: error.response?.data?.detail || error.message,
      });
    },
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: Partial<Product> }) =>
      productsService.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['products'] });
      toast.success('Produto atualizado com sucesso!');
    },
    onError: (error: any) => {
      toast.error('Erro ao atualizar produto', {
        description: error.response?.data?.detail || error.message,
      });
    },
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => productsService.delete(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['products'] });
      toast.success('Produto excluído com sucesso!');
    },
    onError: (error: any) => {
      toast.error('Erro ao excluir produto', {
        description: error.response?.data?.detail || error.message,
      });
    },
  });

  const updateStockMutation = useMutation({
    mutationFn: ({ id, quantidade }: { id: number; quantidade: number }) =>
      productsService.updateStock(id, quantidade),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['products'] });
      toast.success('Estoque atualizado com sucesso!');
    },
    onError: (error: any) => {
      toast.error('Erro ao atualizar estoque', {
        description: error.response?.data?.detail || error.message,
      });
    },
  });

  return {
    products,
    isLoading,
    error,
    createProduct: createMutation.mutate,
    updateProduct: updateMutation.mutate,
    deleteProduct: deleteMutation.mutate,
    updateStock: updateStockMutation.mutate,
    isCreating: createMutation.isPending,
    isUpdating: updateMutation.isPending,
    isDeleting: deleteMutation.isPending,
  };
};
