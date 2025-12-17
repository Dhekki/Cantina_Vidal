/**
 * EXEMPLO DE INTEGRAÇÃO COM BACKEND
 * 
 * Este arquivo demonstra como integrar os componentes existentes com o backend.
 * Você pode adaptar o ProductsManagement.tsx seguindo este padrão.
 */

import { useState } from 'react';
import { useProducts } from '@/hooks/useProducts';
import { Product } from '@/services/products.service';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
} from '@/components/ui/dialog';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import { Pencil, Trash2, Plus } from 'lucide-react';

const ProductsManagementExample = () => {
  const {
    products,
    isLoading,
    createProduct,
    updateProduct,
    deleteProduct,
    isCreating,
    isUpdating,
    isDeleting,
  } = useProducts();

  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [editingProduct, setEditingProduct] = useState<Product | null>(null);
  const [formData, setFormData] = useState<Partial<Product>>({
    nome: '',
    descricao: '',
    preco: 0,
    estoque: 0,
    categoria: '',
    ativo: true,
  });

  const handleOpenDialog = (product?: Product) => {
    if (product) {
      setEditingProduct(product);
      setFormData({
        nome: product.nome,
        descricao: product.descricao,
        preco: product.preco,
        estoque: product.estoque,
        categoria: product.categoria,
        ativo: product.ativo,
      });
    } else {
      setEditingProduct(null);
      setFormData({
        nome: '',
        descricao: '',
        preco: 0,
        estoque: 0,
        categoria: '',
        ativo: true,
      });
    }
    setIsDialogOpen(true);
  };

  const handleCloseDialog = () => {
    setIsDialogOpen(false);
    setEditingProduct(null);
    setFormData({
      nome: '',
      descricao: '',
      preco: 0,
      estoque: 0,
      categoria: '',
      ativo: true,
    });
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    if (editingProduct) {
      // Atualizar produto existente
      updateProduct(
        { id: editingProduct.id!, data: formData },
        {
          onSuccess: () => {
            handleCloseDialog();
          },
        }
      );
    } else {
      // Criar novo produto
      createProduct(formData as Product, {
        onSuccess: () => {
          handleCloseDialog();
        },
      });
    }
  };

  const handleDelete = (id: number) => {
    if (confirm('Tem certeza que deseja excluir este produto?')) {
      deleteProduct(id);
    }
  };

  if (isLoading) {
    return <div className="p-6">Carregando produtos...</div>;
  }

  return (
    <div className="p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">Gerenciamento de Produtos</h1>
        <Button onClick={() => handleOpenDialog()}>
          <Plus className="mr-2 h-4 w-4" />
          Novo Produto
        </Button>
      </div>

      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Nome</TableHead>
            <TableHead>Categoria</TableHead>
            <TableHead>Preço</TableHead>
            <TableHead>Estoque</TableHead>
            <TableHead>Status</TableHead>
            <TableHead className="text-right">Ações</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {products.map((product) => (
            <TableRow key={product.id}>
              <TableCell className="font-medium">{product.nome}</TableCell>
              <TableCell>{product.categoria}</TableCell>
              <TableCell>R$ {product.preco.toFixed(2)}</TableCell>
              <TableCell>{product.estoque}</TableCell>
              <TableCell>
                <span
                  className={`px-2 py-1 rounded text-xs ${
                    product.ativo
                      ? 'bg-green-100 text-green-800'
                      : 'bg-red-100 text-red-800'
                  }`}
                >
                  {product.ativo ? 'Ativo' : 'Inativo'}
                </span>
              </TableCell>
              <TableCell className="text-right">
                <Button
                  variant="ghost"
                  size="sm"
                  onClick={() => handleOpenDialog(product)}
                  disabled={isUpdating}
                >
                  <Pencil className="h-4 w-4" />
                </Button>
                <Button
                  variant="ghost"
                  size="sm"
                  onClick={() => handleDelete(product.id!)}
                  disabled={isDeleting}
                >
                  <Trash2 className="h-4 w-4" />
                </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>

      <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>
              {editingProduct ? 'Editar Produto' : 'Novo Produto'}
            </DialogTitle>
          </DialogHeader>
          <form onSubmit={handleSubmit}>
            <div className="space-y-4">
              <div>
                <Label htmlFor="nome">Nome</Label>
                <Input
                  id="nome"
                  value={formData.nome}
                  onChange={(e) =>
                    setFormData({ ...formData, nome: e.target.value })
                  }
                  required
                />
              </div>

              <div>
                <Label htmlFor="descricao">Descrição</Label>
                <Input
                  id="descricao"
                  value={formData.descricao || ''}
                  onChange={(e) =>
                    setFormData({ ...formData, descricao: e.target.value })
                  }
                />
              </div>

              <div>
                <Label htmlFor="categoria">Categoria</Label>
                <Input
                  id="categoria"
                  value={formData.categoria}
                  onChange={(e) =>
                    setFormData({ ...formData, categoria: e.target.value })
                  }
                  required
                />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <Label htmlFor="preco">Preço</Label>
                  <Input
                    id="preco"
                    type="number"
                    step="0.01"
                    value={formData.preco}
                    onChange={(e) =>
                      setFormData({
                        ...formData,
                        preco: parseFloat(e.target.value),
                      })
                    }
                    required
                  />
                </div>

                <div>
                  <Label htmlFor="estoque">Estoque</Label>
                  <Input
                    id="estoque"
                    type="number"
                    value={formData.estoque}
                    onChange={(e) =>
                      setFormData({
                        ...formData,
                        estoque: parseInt(e.target.value),
                      })
                    }
                    required
                  />
                </div>
              </div>
            </div>

            <DialogFooter className="mt-6">
              <Button
                type="button"
                variant="outline"
                onClick={handleCloseDialog}
              >
                Cancelar
              </Button>
              <Button type="submit" disabled={isCreating || isUpdating}>
                {isCreating || isUpdating ? 'Salvando...' : 'Salvar'}
              </Button>
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default ProductsManagementExample;
